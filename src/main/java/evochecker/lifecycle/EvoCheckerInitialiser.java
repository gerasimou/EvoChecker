package evochecker.lifecycle;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

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
import evochecker.language.parser.ModelInstantiator;
import evochecker.language.parser.ModelInstantiatorParametric;
import evochecker.properties.Property;
import evochecker.properties.PropertyFactory;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import ultimate.Ultimate;

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
        // modelFileList = modelFilename.split(",");
        // System.out.println("Model files: " + String.join(" | ", modelFileList));
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
                break;
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
                modelInstantiator = new ModelInstantiator(modelFilename, propertiesFilename);
                break;
            case PARAMETRIC:
                modelInstantiator = new ModelInstantiatorParametric(modelFilename, propertiesFilename);
                break;
            case ULTIMATE:
                modelInstantiator = new ModelInstantiatorUltimate(modelFilename, propertiesFilename);
                break;
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

    // private List<List<Property>> getUltimateObjectivesConstraints(String str) {
    //     List<List<Property>> list = new java.util.ArrayList<>();
    //     String[] modelStrings = str.split("@@@");
    //     for (String s : modelStrings) {
    //         System.out.println("Model string\n" + s);
    //         String propertiesFileName = Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD);

    //         // get the properties directly

    //         // Load the properties file
    //         String propertiesContent = FileUtil.readFile(propertiesFileName);

    //         // Regex match every two lines: first line starts with "//", second with
    //         // alphanumeric
    //         java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(?m)^//.*\\R^[a-zA-Z0-9].*");
    //         java.util.regex.Matcher matcher = pattern.matcher(propertiesContent);

    //         String[] matches = matcher.results()
    //                 .map(m -> m.group())
    //                 .toArray(String[]::new);

    //         for (String m : matches) {
    //             System.out.println("Found property:\n" + m);
    //             java.util.regex.Pattern quotePattern = java.util.regex.Pattern.compile("\"([^\"]*)\"");
    //             java.util.regex.Matcher quoteMatcher = quotePattern.matcher(m);

    //             // Extract matches without quotes
    //             String[] quoteMatches = quoteMatcher.results()
    //                     .map(qm -> qm.group(1))
    //                     .toArray(String[]::new);
    //             System.out.println("Quote matches found in model string: " + String.join(" --- ", quoteMatches));

    //             for (String qm : quoteMatches) {
    //                 if (s.contains(qm)) {
    //                     System.out.println("Property found in file" + str.split("\n")[0]);
    //                     try {
    //                         list.addAll(PropertyFactory.getObjectivesConstraints(s, m));
    //                     } catch (EvoCheckerException e) {
    //                         System.err.println("Error parsing properties from model string: " + s);
    //                         e.printStackTrace();
    //                         System.exit(1);
    //                     }
    //                 }
    //             }
    //         }
    //     }

    //     return list;
    // }

    private void initialiseProperties() {

        String str = modelInstantiator.getConcreteModel(genes);
        List<List<Property>> list = null;

        if (ecType == EvoCheckerType.ULTIMATE) {

            // Issue:
            //
            // PRISM API requires that when parsing properties file a model file is provided as well,
            // and the properties must be relevant to that model file. If a property is not found in the model,
            // the API causes the program to close with a System.exit() without throwing an exception. This means
            // that for us to parse the properties using the PRISM API, we would need some way of knowing which
            // objective/constraint applies to which model in the ensemble, so that on each API invocation we could
            // pass only one model and all of its associated O/Cs.

            System.out.println("ULTIMATE Unimplemented. Exiting");
            System.exit(1);

        } else {

            list = PropertyFactory.getObjectivesConstraints(str);
        }

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
