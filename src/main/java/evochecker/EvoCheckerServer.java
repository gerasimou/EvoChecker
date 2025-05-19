// package evochecker;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.ServerSocket;
// import java.net.Socket;

// import org.apache.commons.cli.Option;

// /**
//  * EvoCheckerServer class. Starts EvoChecker in a server mode,
//  * which allows one to send models and properties similarly to the CLI.
//  * Intended for use with ULTIMATE for the policy synthesis functionality.
//  * @author Brendan Devlin-Hill
//  */

// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Iterator;
// import java.util.List;

// // import org.apache.logging.log4j.core.tools.picocli.CommandLine;

// import evochecker.auxiliary.ConfigurationChecker;
// import evochecker.auxiliary.Constants;
// import evochecker.auxiliary.FileUtil;
// import evochecker.auxiliary.Utility;
// import evochecker.exception.EvoCheckerException;
// import evochecker.genetic.GenotypeFactory;
// import evochecker.genetic.genes.AbstractGene;
// import evochecker.genetic.jmetal.metaheuristics.settings.MOCell_Settings;
// import evochecker.genetic.jmetal.metaheuristics.settings.NSGAII_Settings;
// import evochecker.genetic.jmetal.metaheuristics.settings.RandomSearch_Settings;
// import evochecker.genetic.jmetal.metaheuristics.settings.SPEA2_Settings;
// import evochecker.genetic.problem.GeneticModelProblem;
// import evochecker.genetic.problem.GeneticProblem;
// import evochecker.genetic.problem.GeneticProblemParametric;
// import evochecker.genetic.problem.GeneticProblemParametricParallel;
// import evochecker.language.parser.IModelInstantiator;
// import evochecker.language.parser.ModelInstantiator;
// import evochecker.language.parser.ModelInstantiatorParametric;
// import evochecker.plotting.PlotFactory;
// import evochecker.properties.Property;
// import evochecker.properties.PropertyFactory;
// import jmetal.core.Algorithm;
// import jmetal.core.Problem;
// import jmetal.core.Solution;
// import jmetal.core.SolutionSet;
// import jmetal.qualityIndicator.QualityIndicator;
// import jmetal.util.JMException;

// import java.io.IOException;
// import java.util.ArrayList;

// import org.apache.commons.cli.HelpFormatter;
// import org.apache.commons.cli.Option;
// import org.apache.commons.cli.Options;
// import org.apache.commons.cli.ParseException;

// import org.apache.commons.cli.CommandLine;
// import org.apache.commons.cli.CommandLineParser;
// import org.apache.commons.cli.DefaultParser;

// public class EvoCheckerServer {

//     private static Options options = new Options();
//     private static boolean printHelpCli = false;
//     private static int port;

//     private static void setUpCLI() {
//         Option help = new Option("help", "Prints usage help information");

//         Option portOption = Option.builder("p")
//                 .argName("port")
//                 .hasArg()
//                 .desc("The port on which to listen.")
//                 .build();

//         options.addOption(help);
//         options.addOption(portOption);
//     };

//     private static void parseArgs(String[] args) {
//         CommandLineParser parser = new DefaultParser();
//         try {
//             CommandLine line = parser.parse(options, args);
//             String portArg = line.getOptionValue("p");

//             if (line.hasOption("help")) {
//                 printHelpCli = true;
//             }

//             if (portArg != null) {
//                 try {
//                     port = Integer.parseInt(portArg);
//                     System.out.println("Listening on port: " + port);
//                 } catch (NumberFormatException e) {
//                     System.err.println("Invalid port number: " + portArg);
//                 }
//             } else {
//                 System.out.println("Using default port (8080).");
//                 port = 8080; // Default port
//             }

//         } catch (ParseException e) {
//             System.err.println("Failed to set the port.  Reason: " + e.getMessage());
//         }
//     }

//     public static void main(String[] args) {

//         setUpCLI();
//         parseArgs(args);

//         try (ServerSocket serverSocket = new ServerSocket(port)) {
//             System.out.println("EvoChecker running.");
//             while (true) {
//                 try (Socket clientSocket = serverSocket.accept();
//                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

//                     String command = in.readLine();
//                     System.out.println("Received command: " + command);

//                     // Process the command
//                     String response = processCommand(command);
//                     out.println(response); // Send response back to Ultimate
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//     }

//     private static String processCommand(String command) {

//         // cases: run command, start, end,

//         switch (command) {
//             case "INITIALISE":
//                 initialise();
//                 return "Server started.";
//             case "STOP":
//                 return "Server ended.";
//             case "SET_PROBLEM":
//                 // Set the problem (model, properties)
//                 return "Problem set.";
//             case "SET_CONFIG":
//                 // Set the configuration
//                 return "Configuration set.";



//             case "EXECUTE":
//                 // Execute the problem
//                 return "Execution started.";
//             default:
//                 return "Unknown command: " + command;
//         }

//     }

// }