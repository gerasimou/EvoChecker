//==============================================================================
//	
//	Copyright (c) 2013-
//	Authors:
//	* Dave Parker <david.parker@comlab.ox.ac.uk> (University of Oxford)
//	* Ernst Moritz Hahn <emhahn@cs.ox.ac.uk> (University of Oxford)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of PRISM.
//	
//	PRISM is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//	
//	PRISM is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License
//	along with PRISM; if not, write to the Free Software Foundation,
//	Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//	
//==============================================================================

package pse;

import java.util.LinkedList;

import explicit.StateValues;
import parser.Values;
import parser.ast.Expression;
import parser.ast.ExpressionProb;
import parser.ast.LabelList;
import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.PrismComponent;
import prism.PrismException;
import prism.Result;

/**
 * CTMC model checker based on fast adaptive uniformisation.
 */
public class PSEFastAdaptiveUniformisationModelChecker extends PrismComponent
{
	// Model file
	private ModulesFile modulesFile;
	// Properties file
	private PropertiesFile propertiesFile;
	// Constants from model
	private Values constantValues;
	// Labels from the model
	private LabelList labelListModel;
	// Labels from the property file
	private LabelList labelListProp;
	
	/**
	 * Constructor.
	 */
	public PSEFastAdaptiveUniformisationModelChecker(PrismComponent parent)
	{
		super(parent);
	}

	public void setModulesFileAndPropertiesFile(ModulesFile modulesFile, PropertiesFile propertiesFile)
	{
		this.modulesFile = modulesFile;
		this.propertiesFile = propertiesFile;
		// Get combined constant values from model/properties
		constantValues = new Values();
		constantValues.addValues(modulesFile.getConstantValues());
		if (propertiesFile != null)
			constantValues.addValues(propertiesFile.getConstantValues());
		this.labelListModel = modulesFile.getLabelList();
		this.labelListProp = propertiesFile.getLabelList();

		//stateChecker.setModulesFileAndPropertiesFile(modulesFile, propertiesFile);
	}

	/**
	 * Model check a property.
	 */
	public Result check(Expression expr) throws PrismException
	{
		Result res;
		String resultString;
		long timer;

		// Starting model checking
		timer = System.currentTimeMillis();

		// Do model checking
		res = checkExpression(expr);

		// Model checking complete
		timer = System.currentTimeMillis() - timer;
		mainLog.println("\nModel checking completed in " + (timer / 1000.0) + " secs.");

		// Print result to log
		resultString = "Result";
		if (!("Result".equals(expr.getResultName())))
			resultString += " (" + expr.getResultName().toLowerCase() + ")";
		resultString += ": " + res;
		mainLog.print("\n" + resultString + "\n");

		// Return result
		return res;
	}

	/**
	 * Model check an expression (used recursively).
	 */
	private Result checkExpression(Expression expr) throws PrismException
	{
		Result res;

		// Current range of supported properties is quite limited...
		if (expr instanceof ExpressionProb)
			res = checkExpressionProb((ExpressionProb) expr);
		//else if (expr instanceof ExpressionReward)
		//	res = checkExpressionReward((ExpressionReward) expr);
		else
			throw new PrismException("Fast adaptive uniformisation not yet supported for this operator");

		return res;
	}

	/**
	 * Model check a P operator.
	 */
	private Result checkExpressionProb(ExpressionProb expr) throws PrismException
	{
		return null;
		// TODO
	}

	// Transient analysis
	
	public BoxRegionValues doTransient(PSEModelExplorer modelExplorer, double t, BoxRegionValues initDist, DecompositionProcedure decompositionProcedure)
			throws PrismException
	{
		// TODO: initial distributions

		// For decomposing of the parameter space
		LinkedList<BoxRegion> regions = new LinkedList<BoxRegion>();
		regions.add(modelExplorer.getCompleteSpace());

		// Start bounded probabilistic reachability
		long timer = System.currentTimeMillis();
		mainLog.println("\nStarting PSE+FAU transient probability computation...");
		mainLog.println();

		BoxRegionValues regionValues = new BoxRegionValues();
		PSEFastAdaptiveUniformisation fau = new PSEFastAdaptiveUniformisation(this, modelExplorer);
		while (regions.size() != 0) {
			BoxRegion region = regions.remove();
			fau.configureParameterSpace(region);
			mainLog.println("Computing probabilities for parameter region " + region);

			StateValues probsMin, probsMax;
			probsMin = fau.doTransient(t, initDist); //, PSEFastAdaptiveUniformisation.VectorType.MIN);
			probsMax = fau.doTransient(t, initDist); //, PSEFastAdaptiveUniformisation.VectorType.MAX);
			try {
				decompositionProcedure.examinePartialComputation(regionValues, region, probsMin.getDoubleArray(), probsMax.getDoubleArray());
				regionValues.put(region, probsMin, probsMax);
			} catch (DecompositionProcedure.DecompositionNeeded e) {
				e.printRegionsToDecompose(mainLog);
				for (BoxRegion regionToDecompose : e.getRegionsToDecompose()) {
					regions.addAll(regionToDecompose.decompose());
				}
			}
		}

		// Finished bounded probabilistic reachability
		timer = System.currentTimeMillis() - timer;
		mainLog.print("\nPSE+FAU transient probability computation");
		mainLog.print(" took " + timer / 1000.0 + " seconds");
		mainLog.println(" (producing " + regionValues.getNumRegions() + " final regions).");

		return regionValues;
	}
}
