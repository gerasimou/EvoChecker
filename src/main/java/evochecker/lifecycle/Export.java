
/**
    ------------------------------------------------------------------------------

    Export class. Deals with exporting solutions after EvoChecker has executed/
    @author Brendan Devlin-Hill
    
    ------------------------------------------------------------------------------
    
    This file is part of EvoChecker.
        
    ==============================================================================
 */

package evochecker.lifecycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
import evochecker.genetic.problem.GeneticModelProblem;
import evochecker.genetic.problem.GeneticProblem;
import evochecker.genetic.problem.GeneticProblemParametric;
import evochecker.genetic.problem.GeneticProblemParametricParallel;
import evochecker.language.parser.IModelInstantiator;
import evochecker.language.parser.ModelInstantiatorUltimate;
import evochecker.language.parser.ModelInstantiatorParametric;
import evochecker.lifecycle.EvoCheckerInitialiser;
import evochecker.plotting.PlotFactory;
import evochecker.properties.Property;
import evochecker.properties.PropertyFactory;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class Export {

    /** */
    public static String[] exportResults(List<Property> objectivesList, List<AbstractGene> genes, String algorithmName,
            String problemName, SolutionSet solutions, String outputDir) throws JMException {
        // Print results to console
        System.out.println("-------------------------------------------------");
        System.out.println("SOLUTIONS: \t" + solutions.size());

        String identifier = problemName + "_" + algorithmName + "_" + Utility.getTimeStamp();
        String frontFile = outputDir + identifier + "_Front";
        String setFile = outputDir + identifier + "_Set";
        try {
            File pf = File.createTempFile(identifier, "_Front", new File(outputDir));
            File ps = File.createTempFile(identifier, "_Set", new File(outputDir));

            frontFile = pf.getAbsolutePath();
            setFile = ps.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // generate and save headers
        String setHeader = String.join("\t", GenotypeFactory.getEvolvableNames());
        FileUtil.saveToFile(setFile, setHeader + "\n", true);
        StringBuilder frontHeader = new StringBuilder();
        Iterator<Property> it = objectivesList.iterator();
        while (it.hasNext()) {
            Property p = it.next();
            frontHeader.append(p.getExpression());
            if (it.hasNext())
                frontHeader.append("\t");
        }
        FileUtil.saveToFile(frontFile, frontHeader.toString(), true);

        List<Solution> solutionList = new ArrayList<Solution>();
        // System.out.println(solutions)
        for (int i = 0; i < solutions.size(); i++)
            solutionList.add(solutions.get(i));
        Utility.printObjectivesToFile(frontFile, solutionList, objectivesList);
        Utility.printVariablesToFile2(setFile, solutionList, GenotypeFactory.getGeneEvolvableMap(), genes);

        System.out.println("\nPareto Front and Pareto set saved at: " + outputDir);
        System.out.println("Pareto Front: " + frontFile);
        System.out.println("Pareto Set: " + setFile);
    
        // return the files created
        String[] files = new String[2];
        files[0] = frontFile;
        files[1] = setFile;
        return files;

    }

    // it's redundant to have this here, but I'm leaving it for simplicity
    public static void displayParetoFrontPlot(String frontFile, int numObjectives) {
        PlotFactory.plotParetoFront(frontFile, numObjectives);
    }

}