/**
    ------------------------------------------------------------------------------

    EvoCheckerServer class. Starts EvoChecker in a server mode,
    which allows one to send models and properties similarly to the CLI.
    Intended for use with ULTIMATE for the policy synthesis functionality.
    @author Brendan Devlin-Hill
    
    ------------------------------------------------------------------------------

    This file is part of EvoChecker.
        
    ==============================================================================
 */

package evochecker;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import evochecker.auxiliary.Utility;

// import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.problem.GeneticModelProblem;
import evochecker.language.parser.IModelInstantiator;
import evochecker.lifecycle.EvoCheckerInitialiser;
import evochecker.lifecycle.Export;
import evochecker.properties.Property;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;

public class EvoCheckerServer {

    private Thread executionThread;
    private volatile boolean isExecuting = false; //

    private static boolean printHelpCli = false;
    private static int port;

    private EvoCheckerInitialiser initialiser = null;
    private Export exporter = null;

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
        exporter = new Export();

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
                + "SET_CONFIG_FILE [path] - set path to config file\n"
                + "INITIALISE - initialise EvoChecker\n"
                + "EXECUTE - execute EvoChecker\n"
                + "SHUTDOWN - shutdown the server\n";

        switch (command) {
            case "COMMANDS":
                handleResponse(helpString);
                break;
            case "HELP":
                handleResponse(helpString);
                break;
            case "INITIALISE":
                try {
                    if (configFile == null) {
                        handleResponse(
                                "Configuration file must be set before initialisation.");
                    }
                    initialise_configuration();
                    initialise_problem();
                } catch (Exception e) {
                    handleResponse("Error during configuration initialisation: " + e.getMessage());
                }
                handleResponse("Initialised EvoChecker successfully.");
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
                    handleResponse("Execution in progress...");
                    execute();
                    Export.exportResults(
                            objectivesList,
                            genes,
                            algorithmName,
                            problemName,
                            solutions,
                            outputDir);
                    handleResponse("Saving results to " + outputDir +
                            "\nExecution completed.");
                } catch (Exception e) {
                    handleResponse("Execution error: " + e.getMessage() + e.getStackTrace());
                }
                break;
            default:
                handleResponse("Unknown command: " + command);
                break;
            case "SHUTDOWN":
                handleResponse("Shutting down server.");
                try {
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    handleResponse("Error closing resources: " + e.getMessage());
                }
                System.exit(0);
                break;
        }

    }

    public String getStatistics() {
        return ((GeneticModelProblem) problem).getStatistics();
    }

    private void handleResponse(String response) {
        // this will eventually log the response in a file as well
        System.out.println(response);
        out.println(response);
    }

    private void execute() throws Exception {
        // Execute the Algorithm
        try {
            solutions = algorithm.execute();
            handleResponse("Execution finished successfully.");
        } catch (Exception e) {
            handleResponse("Execution failed: " + e.getMessage());
        }

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

        initialiser = new EvoCheckerInitialiser();
        exporter = new Export();

        handleResponse("Initialising options");
        initialiser.initialiseEvoCheckerOptions();

        propertiesFilename = initialiser.getPropertiesFilename();
        algorithmName = initialiser.getAlgorithmName();
        problemName = initialiser.getProblemName();
        ecType = initialiser.getEcType();

    }

    private void initialise_problem() throws Exception {

        handleResponse("Initialising problem");
        initialiser.initializeEvoCheckerProblem();
        handleResponse("Initialising algorithm");
        initialiser.initialiseEvoCheckerAlgorithm();
        handleResponse("Initialising output data");
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