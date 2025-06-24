package evochecker.lifecycle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

import evochecker.genetic.problem.GeneticProblemUltimate;

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
                problem = new GeneticProblemUltimate(genes, modelInstantiator, objectivesList,
                        constraintsList, problemName);
            case REGION:
                throw new EvoCheckerException("EvoChecker Region is still in development!. Exiting");
        }
    }

    /*
     * Parses the .ultimate file to create a hashmap where keys are model filename
     * and values are lists of O/Cs
     * associated with that model.
     */
    private HashMap<String, List<String>> parseUltimateObjectiveConstraints() {

        HashMap<String, List<String>> objectiveConstraintsHashMap = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;

        System.out.println("Parsing ULTIMATE file " + modelFilename);

        File modelFile = new File(modelFilename);

        try {
            root = mapper.readTree(modelFile); // get root json node
        } catch (IOException e) {
            System.err.println("Error reading configuration file: " + e.getMessage());
            System.exit(1);
        }

        JsonNode models = root.get("models");
        for (JsonNode model : models) { // iterating over models in the ensemble
            System.out.println("Model: " + model.get("fileName").asText());
            JsonNode synthesis = model.get("synthesis");
            if (synthesis != null && synthesis.has("properties")) { // access synthesis node
                JsonNode properties = synthesis.get("properties");
                List<String> propertiesList = new ArrayList<>();
                for (JsonNode property : properties) { // iterate over O/Cs in node
                    System.out.println("Properties: " + property.asText());
                    propertiesList.add("//" + property.asText()); // must add '//' for syntax reasons
                }
                objectiveConstraintsHashMap.put(model.get("fileName").asText(), propertiesList);
            }
        }

        return objectiveConstraintsHashMap;

    }

    /*
     * Get the ULTIMATE O/Cs by iterating over the the internal representation,
     * matching
     * the ensemble file names to those in the parsed OCs, and passing to the PRISM
     * API.
     */
    private List<List<Property>> getUltimateObjectiveConstraints(String str) {

        List<List<Property>> list = new ArrayList<>();
        // get the OCs from the parsed hashmap (which comes from the .ultimate file)
        HashMap<String, List<String>> objectivesConstraintsHashMap = parseUltimateObjectiveConstraints();

        String[] internalRepresentations = str.split("@@@"); // splits the internal representation into individual
                                                             // models

        for (String s : internalRepresentations) { // iterate over individual model representations

            // get the filename of the ensemble model:
            String fileName = null;
            for (String line : s.split("\n")) {
                if (line.trim().startsWith("//")) {
                    fileName = line.replace("//", "");
                    break;
                }
            }
            System.out.println("Loading objectives/constraints for: " + fileName);
            List<String> ocs = objectivesConstraintsHashMap.get(fileName); // get OCs (as strings) from the parsed
                                                                           // hashmap
            if (ocs != null && ocs.size() > 0) {
                System.out.println("Found objectives/constaints: " + String.join("\n", ocs));
                String joinedOcs = String.join("", ocs); // join the OCs together into one string
                try {
                    list.addAll(PropertyFactory.getObjectivesConstraints(s, joinedOcs)); // get OCs (as property
                                                                                         // objects) from
                                                                                         // PropertyFactory
                } catch (EvoCheckerException e) {
                    System.err.println("Error getting properties for model'" + fileName + "'\n" + e.getMessage());
                    System.exit(1);
                }
            } else {
                System.out.println("No objectives/constraints found.");
            }
        }

        return list;
    }

    private void initialiseProperties() {

        String str = modelInstantiator.getConcreteModel(genes);
        List<List<Property>> list = null;

        if (ecType == EvoCheckerType.ULTIMATE) {
            list = getUltimateObjectiveConstraints(str);
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
