package evochecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.cli.Option;

/**
 * EvoCheckerServer class. Starts EvoChecker in a server mode,
 * which allows one to send models and properties similarly to the CLI.
 * Intended for use with ULTIMATE for the policy synthesis functionality.
 * @author Brendan Devlin-Hill
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import evochecker.exception.EvoCheckerException;
import evochecker.initialisation.EvoCheckerInitialiser;
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
import evochecker.genetic.problem.GeneticModelProblem;
import evochecker.genetic.problem.GeneticProblem;
import evochecker.genetic.problem.GeneticProblemParametric;
import evochecker.genetic.problem.GeneticProblemParametricParallel;
import evochecker.language.parser.IModelInstantiator;
import evochecker.language.parser.ModelInstantiator;
import evochecker.language.parser.ModelInstantiatorParametric;
import evochecker.plotting.PlotFactory;
import evochecker.properties.Property;
import evochecker.properties.PropertyFactory;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class EvoCheckerServer {

    private static boolean printHelpCli = false;
    private static int port;

    private EvoCheckerInitialiser initialiser = null;

    /** problem trying to solve */
    private Problem problem;

    /** properties list */
    private List<Property> objectivesList;
    private List<Property> constraintsList;

    /** problem genes */
    private List<AbstractGene> genes = new ArrayList<AbstractGene>();

    /** parser engine handler */
    private IModelInstantiator modelInstantiator;

    /** model filename */
    private String modelFilename;

    /** property filename */
    private String propertiesFilename;

    /** algorithm to be executed */
    private Algorithm algorithm;

    /** problem name */
    private String algorithmName;

    /** problem name */
    private String problemName;

    /** Pareto front filename */
    private String paretoFrontFile;

    /** Pareto set filename */
    private String paretoSetFile;

    /** Solution set */
    private SolutionSet solutions;

    /** */
    private EvoCheckerType ecType;

    /** */
    private double executionTime;

    /** */
    private String outputDir;

    private static Boolean commandLineInvoked = false;

    private String configFile;
    private String modelFile;
    private String propertyFile;

    private static Options options = new Options();

    private BufferedReader in;
    private PrintWriter out;

    private static void setUpCLI() {
        Option help = new Option("help", "Prints usage help information");

        Option portOption = Option.builder("p")
                .argName("port")
                .hasArg()
                .desc("The port on which to listen.")
                .build();

        options.addOption(help);
        options.addOption(portOption);
    };

    private static void parseArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            String portArg = line.getOptionValue("p");

            if (line.hasOption("help")) {
                printHelpCli = true;
            }

            if (portArg != null) {
                try {
                    port = Integer.parseInt(portArg);
                    System.out.println("Listening on port: " + port);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid port number: " + portArg);
                }
            } else {
                System.out.println("Using default port (8080).");
                port = 8080; // Default port
            }

        } catch (ParseException e) {
            System.err.println("Failed to set the port.  Reason: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws EvoCheckerException {

        EvoCheckerServer ecs = new EvoCheckerServer();

        setUpCLI();
        parseArgs(args);

        if (printHelpCli) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("headless", options);
            return;
        }

        ecs.start();
    }
    
    public void start() {
        
        initialiser = new EvoCheckerInitialiser();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("EvoChecker running.");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {

                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream(), true);


                    String command = in.readLine();
                    handleResponse(command);

                    // Process the command
                    processCommand(command, in);
                    // out.println(response); // Send response back to Ultimate
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processCommand(String command, BufferedReader inputBuffer) throws IOException {

        // cases: run command, start, end,
        String helpString = "COMMANDS: \n"
                + "SET_MODEL_FILE [path] - set path to model file\n"
                + "SET_PROPERTIES_FILE [path] - set path to properties file\n"
                + "SET_CONFIG_FILE [path] - set path to config file\n"
                + "INITIALISE_CONFIG - initialise EvoChecker configuration\n"
                + "INITIALISE_PROBLEM - initialise EvoChecker for new model and properties file\n"
                + "EXECUTE - execute EvoChecker\n";

        switch (command) {
            case "COMMANDS":
                handleResponse(helpString);
                break;
            case "HELP":
                handleResponse(helpString);
                break;
            case "INITIALISE_CONFIG":
                try {
                    if (configFile == null) {
                        handleResponse(
                                "Configuration file must be set before initialisation.");
                    }
                    initialise_configuration();
                } catch (Exception e) {
                    handleResponse("Error during initialisation: " + e.getMessage());
                }
                handleResponse("Initialised EvoChecker successfully.");
                break;
            case "INITIALISE_PROBLEM":
                try {
                    if (modelFile == null || propertyFile == null) {
                        handleResponse(
                                "Model file and property file must be set before initialisation.");
                    }
                    initialise_problem();
                } catch (Exception e) {
                    handleResponse("Error during initialisation: " + e.getMessage());
                }
                handleResponse("Initialised EvoChecker successfully.");
                break;
            // case "STOP":
            // handleResponse("Server ended.");
            // break;
            case "SET_MODEL_FILE":
                try {
                    set_model_file_path(inputBuffer);
                    Utility.setModelFileOverride(modelFile);
                    handleResponse("Model file set successfully: " + modelFile);
                } catch (Exception e) {
                    handleResponse("Error setting model file: " + e.getMessage());
                }
                break;
            case "SET_PROPERTIES_FILE":
                try {
                    set_property_file_path(inputBuffer);
                    Utility.setPropertiesFileOverride(propertyFile);
                    handleResponse("Property file set successfully: " + propertyFile);
                } catch (Exception e) {
                    handleResponse("Error setting property file: " + e.getMessage());
                }
                break;
            case "SET_CONFIG_FILE":
                try {
                    set_config_file_path(inputBuffer);
                    Utility.setPropertiesFile(configFile);
                    handleResponse("Configuration set successfully.");
                } catch (Exception e) {
                    handleResponse("Error setting configuration file: " + e.getMessage());
                }
                break;
            case "EXECUTE":
                try {
                    solutions = execute();
                    handleResponse("Execution completed.");
                } catch (Exception e) {
                    handleResponse("Execution error: " + e.getMessage());
                }
                break;
            default:
                handleResponse("Unknown command: " + command);
                break;
        }

    }

    private void handleResponse(String response) {
        // this will eventually log the response in a file as well
        System.out.println(response);
        out.println(response);
    }

    private SolutionSet execute() throws Exception {
        // Execute the Algorithm
        System.out.println("Starting evolution");
        SolutionSet solutions = algorithm.execute();

        return solutions;
    }

    private void set_config_file_path(BufferedReader in) throws IOException {
        // Read the config file path as a single line from the input stream
        String configPath = in.readLine();
        if (configPath == null || configPath.isEmpty()) {
            throw new IOException("No config file path provided.");
        }

        this.configFile = new File(configPath).getAbsolutePath();
        System.out.println("Config file set to: " + configPath);
    }

    private void set_model_file_path(BufferedReader in) throws IOException {
        // Read the model file path as a single line from the input stream
        String modelPath = in.readLine();
        if (modelPath == null || modelPath.isEmpty()) {
            throw new IOException("No model file path provided.");
        }

        this.modelFile = new File(modelPath).getAbsolutePath();
        System.out.println("Model file set to: " + modelPath);
    }

    private void set_property_file_path(BufferedReader in) throws IOException {
        // Read the property file path as a single line from the input stream
        String propertyPath = in.readLine();
        if (propertyPath == null || propertyPath.isEmpty()) {
            throw new IOException("No property file path provided.");
        }

        this.propertyFile = new File(propertyPath).getAbsolutePath();
        System.out.println("Property file set to: " + propertyPath);

    }

    private void initialise_configuration() throws Exception {

        handleResponse("Initialising EvoChecker options");
        initialiser.initialiseEvoCheckerOptions();

        propertiesFilename = initialiser.getPropertiesFilename();
        algorithmName = initialiser.getAlgorithmName();
        problemName = initialiser.getProblemName();
        ecType = initialiser.getEcType();

    }

    private void initialise_problem() throws Exception {

        handleResponse("Initialising EvoChecker problem");
        initialiser.initializeEvoCheckerProblem();
        handleResponse("Initialising EvoChecker algorithm");
        initialiser.initialiseEvoCheckerAlgorithm();
        handleResponse("Initialising EvoChecker output data");
        initialiser.initialiseOutputData();

        modelInstantiator = initialiser.getModelInstantiator();
        genes = initialiser.getGenes();
        objectivesList = initialiser.getObjectivesList();
        constraintsList = initialiser.getConstraintsList();
        problem = initialiser.getProblem();

        algorithm = initialiser.getAlgorithm();

        outputDir = initialiser.getOutputDir();

    }

}