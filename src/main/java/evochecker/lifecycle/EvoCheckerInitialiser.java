package evochecker.lifecycle;

import java.io.File;
import java.util.List;

// import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import evochecker.EvoCheckerType;
import evochecker.auxiliary.ConfigurationChecker;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.FileUtil;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.metaheuristics.settings.MOCell_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.NSGAII_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.RandomSearch_Settings;
import evochecker.genetic.jmetal.metaheuristics.settings.SPEA2_Settings;
import evochecker.genetic.problem.GeneticProblem;
import evochecker.genetic.problem.GeneticProblemParametricParallel;
import evochecker.language.parser.IModelInstantiator;
import evochecker.language.parser.ModelInstantiatorUltimate;
// import evochecker.language.parser.ModelInstantiatorUltimate;
import evochecker.language.parser.ModelInstantiatorParametric;
import evochecker.properties.Property;
import evochecker.properties.PropertyFactory;
import jmetal.core.Algorithm;
import jmetal.core.Problem;


public class EvoCheckerInitialiser {

    String modelFilenameOverride = null;
    String propertiesFilenameOverride = null;

    String modelFilename = null;
    String[] modelFileList = null;
    String propertiesFilename = null;
    String algorithmName = null;
    String problemName = null;
    EvoCheckerType ecType = null;
    Algorithm algorithm = null;
    Problem problem = null;
    IModelInstantiator modelInstantiator = null;
    List<AbstractGene> genes = null;
    List<Property> objectivesList = null;
    List<Property> constraintsList = null;
    String outputDir = null;
    Boolean useFileOverrides = false; // if true, model and properties
                                      // filenames in config file are overridden
                                      // by those specified in command line
    Boolean allowUnspecifiedFiles = false; // if true, model and properties
    // filenames in config file are not checked
    // for existence

    public void initialiseEvoCheckerOptions()

            /*
             * Initialises the following:
             * modelFilename
             * propertiesFilename
             * algorithmName
             * problemName
             * ecType
             */

            throws Exception {
        // 0) check configuration script
        ConfigurationChecker.checkConfiguration();

        modelFilename = new File(Utility.getProperty(Constants.MODEL_FILE_KEYWORD)).getAbsolutePath();
        propertiesFilename = new File(Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD))
                .getAbsolutePath();

        // // check if modelFilename can be split into a list
        // if (modelFilename.contains(",")) {
        //     modelFileList = modelFilename.split(",");
        //     System.out.println("Model files: " + String.join(" | ", modelFileList));
        // }
        

        // 1) initialise problem
        algorithmName = Utility.getProperty(Constants.ALGORITHM_KEYWORD).toUpperCase();
        problemName = Utility.getProperty(Constants.PROBLEM_KEYWORD).toUpperCase();

        switch (EvoCheckerType.valueOf(Utility.getPropertyIgnoreNull(Constants.EVOCHECKER_TYPE).toUpperCase())) {
            case NORMAL:
                ecType = EvoCheckerType.NORMAL;
                break;
            case PARAMETRIC:
                ecType = EvoCheckerType.PARAMETRIC;
                break;
            case ULTIMATE:
                ecType = EvoCheckerType.ULTIMATE;
            case REGION:
                ecType = EvoCheckerType.REGION;
                throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");
        }
    }

    public void initializeEvoCheckerProblem() throws EvoCheckerException {

        /*
         * Initialises the following:
         * modelInstantiator
         * genes
         * objectivesList
         * constraintsList
         * problem
         */

        // 1) parse model template


        System.out.println("Model file: " + modelFilename);
        System.out.println("Properties file: " + propertiesFilename);

        switch (ecType) {
            case NORMAL:
                modelInstantiator = new ModelInstantiatorUltimate(modelFilename, propertiesFilename);
                break;
            case PARAMETRIC:
                modelInstantiator = new ModelInstantiatorParametric(modelFilename, propertiesFilename);
                break;
            // case ULTIMATE:
            //     modelInstantiator = new ModelInstantiatorUltimate(modelFilename, propertiesFilename);
            //     break;
            case REGION:
                throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");
        }

        // 2) create chromosome
        genes = GenotypeFactory.createChromosome(modelInstantiator.getEvolvableList(), false);

        // 3) create (gene,evolvable element) pairs
        modelInstantiator.createMapping();

        // 4) create properties list
        initialiseProperties();

        // 5) instantiate the problem
        switch (ecType) {
            case NORMAL:
                problem = new GeneticProblem(genes, modelInstantiator, objectivesList, constraintsList, problemName);
                break;
            // case PARAMETRIC : problem = new GeneticProblemParametric (genes,
            // modelInstantiator, objectivesList, constraintsList, problemName);break;
            case PARAMETRIC:
                problem = new GeneticProblemParametricParallel(genes, modelInstantiator, objectivesList,
                        constraintsList, problemName);
                break;
            case ULTIMATE:
                throw new EvoCheckerException("EvoChecker Ultimate is still in development!. Exiting");
            case REGION:
                throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");
        }
    }

    private void initialiseProperties() {
        System.out.println("INITIALISE PROPERTIES");
        String str = modelInstantiator.getConcreteModel(genes);
        System.out.println("str: START\n" + str + "\nEND");
        List<List<Property>> list = PropertyFactory.getObjectivesConstraints(str);
        objectivesList = list.get(0);
        constraintsList = list.get(1);

        System.out.println("Objectives (O)/Constraints(C)");
        for (Property p : objectivesList)
            System.out.print("O: " + p.toString());
        for (Property p : constraintsList)
            System.out.print("C: " + p.toString());
        System.out.println();
    }

    /**
     * initialise algorithm
     * 
     * @throws Exception
     */
    public void initialiseEvoCheckerAlgorithm() throws Exception {

        System.out.println("Algorithm: " + algorithmName); 
        System.out.println("Problem: " + problemName);

        if (algorithmName != null) {
            if (algorithmName.equals(Constants.ALGORITHM.NSGAII.toString())) {
                NSGAII_Settings nsgaiiSettings = new NSGAII_Settings(problemName, problem);
                algorithm = nsgaiiSettings.configure();
            } else if (algorithmName.equals(Constants.ALGORITHM.RANDOM.toString())) {
                RandomSearch_Settings rsSettings = new RandomSearch_Settings(problemName, problem);
                algorithm = rsSettings.configure();
            } else if (algorithmName.equals(Constants.ALGORITHM.SPEA2.toString())) {
                SPEA2_Settings spea2Settings = new SPEA2_Settings(problemName, problem);
                algorithm = spea2Settings.configure();
            } else if (algorithmName.equals(Constants.ALGORITHM.MOCELL.toString())) {
                MOCell_Settings mocellSettings = new MOCell_Settings(problemName, problem);
                algorithm = mocellSettings.configure();
            }
            // else if (algorithmStr.equals("SGA")){
            // int numOfConstraints = 0;
            // problem = new GeneticProblemSingle(genes, propertyList, parserEngine,
            // numOfConstraints);
            // SingleGA_Settings sga_setting = new SingleGA_Settings("GeneticProblem",
            // problem);
            // algorithm = sga_setting.configure();
            // }
            else
                throw new Exception("Algorithm not recognised");
        }


    }

    /**
     * Initialise data structure and variables for saving execution results
     */
    public void initialiseOutputData() {
        // create output dir
        String outputDir = "data" + File.separator
                + Utility.getProperty(Constants.PROBLEM_KEYWORD) + File.separator
                + Utility.getProperty(Constants.ALGORITHM_KEYWORD) + File.separator;
        FileUtil.createDir(outputDir);

        this.outputDir = outputDir;

        // int run = RODESExperimentRuns.getRun();
        // String outputFileSuffix = tolerance +"_"+ epsilon +"_"+ run;

    }

    public String getModelFilename() {
        return modelFilename;
    }

    public String getPropertiesFilename() {
        return propertiesFilename;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getProblemName() {
        return problemName;
    }

    public EvoCheckerType getEcType() {
        return ecType;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public Problem getProblem() {
        return problem;
    }

    public IModelInstantiator getModelInstantiator() {
        return modelInstantiator;
    }

    public List<AbstractGene> getGenes() {
        return genes;
    }

    public List<Property> getObjectivesList() {
        return objectivesList;
    }

    public List<Property> getConstraintsList() {
        return constraintsList;
    }

    public String getOutputDir() {
        return outputDir;
    }
}
