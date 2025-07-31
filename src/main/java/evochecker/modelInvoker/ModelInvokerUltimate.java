//==============================================================================
//	
//	Copyright (c) 2020-
//	Authors:
//	* Simos Gerasimou (University of York)
//  * Faisal Alhwikem (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.modelInvoker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import evochecker.lifecycle.IUltimate;

import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.DistributionGene;
import evochecker.properties.Property;
import evochecker.EvoChecker;
import evochecker.auxiliary.ConfigurationChecker;
import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;

public class ModelInvokerUltimate implements IModelInvoker {

	/**
	 * Default class constructor
	 */
	public ModelInvokerUltimate() {

	}

	/** Copy constructor **/
	public ModelInvokerUltimate(IModelInvoker anInvoker) {
		this();
	}

	@Override
	public List<String> invoke(String model, String propertyFile, List<Property> objectives, List<Property> constraints,
			PrintWriter out, BufferedReader in) throws IOException {
		throw new UnsupportedOperationException(
				"'invoke' is not implemeted for IModelInvokerEnsemble. Use 'invokeEnsemble' instead.");
	}

	@Override
	public List<String> invokeParam(String model, String propertyFile, List<Property> objectives,
			List<Property> constraints, PrintWriter out, BufferedReader in) throws IOException {
		throw new UnsupportedOperationException(
				"'invokeParam' is not implemeted for IModelInvokerEnsemble. Use 'invokeEnsemble' instead.");
	}

	@Override
	public List<String> invokeEnsemble(String modelFilename,
			HashMap<String, List<List<Property>>> objectiveConstraintsMap, List<AbstractGene> genes) {

		// this should be done elsewhere (outside the loop) in the long-run, but for now
		// I will put it here:
		IUltimate ultimate = EvoChecker.getUltimateInstance();
		File modelFile = new File(modelFilename);

		// Alleles.toString is a limitation (I think). Will have to see how this is
		// handled with other
		// types of invocation.
		HashMap<String, String> evolvableValues = new HashMap<>();
		for (AbstractGene g : genes) {
			evolvableValues.put(g.getName(), g.getAllele().toString());
		}

		List<String> results = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		try {
			root = mapper.readTree(modelFile);
		} catch (Exception e) {
			System.err.println("Error occured whilst reading model file. Is it in the correct format?");
			e.printStackTrace();
			return null;
		}

		JsonNode models = root.get("models");

		for (JsonNode model : models) {
			String id = model.get("id").asText();
			ultimate.setTargetModelId(id);
			// I think the names of the genes match the name of the variable in the model
			// so it should be easy to set the internal parameters as-is.
			// however, this means that all evolvables across the world model
			// must have different names. Maybe this could be ensured by ULTIMATE itself.
			ultimate.setInternalParameters(evolvableValues);
			ultimate.generateModelInstances();
			// System.out.println(objectiveConstraintsMap);
			List<Property> objectiveList = objectiveConstraintsMap.get(id).get(0);
			List<Property> constraintsList = objectiveConstraintsMap.get(id).get(1);
			List<Property> propertyList = new ArrayList<>();
			propertyList.addAll(objectiveList);
			propertyList.addAll(constraintsList);
			for (Property p : propertyList) {
				ultimate.resetResults();
				try {
					String prop = p.getExpression();
					if (prop != null) {
						ultimate.setVerificationProperty(prop);
						ultimate.execute();
					}
				} catch (Exception e) {
					System.err.println("Error executing ULTIMATE for model: " + id);
					e.printStackTrace();
					return null;
				}
				List<Double> resultsList = new ArrayList<>(ultimate.getResults().values());
				for (Double r : resultsList) {
					results.add(r.toString());
				}
			}
		};
		return results;
	}

	private List<String> processResult(BufferedReader in) throws IOException {
		String line;
		try {
			StringBuilder modelBuilder = new StringBuilder();
			do {
				// retrieve from prism
				line = in.readLine();
				if (line.endsWith("END"))
					break;
				modelBuilder.append(line);
				modelBuilder.append("\n");
			} while (true);

			return checkResult(modelBuilder.toString().trim());

		} catch (Exception e) {
			return null;
		}
	}

	private List<String> checkResult(String resultString) {

		if (resultString.equalsIgnoreCase("NULL"))
			return null;
		else
			return Arrays.asList(resultString.split("#"));
	}

	@Override
	public IModelInvoker copy(int id) {
		return new ModelInvokerPrism();
	}
}
