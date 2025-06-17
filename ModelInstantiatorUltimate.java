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
package evochecker.language.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evochecker.evolvables.Evolvable;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;

public class ModelInstantiatorUltimate implements IModelInstantiator {

	/** map that keeps pairs of genes and evolvable elements */

	
	protected Map<AbstractGene, Evolvable> elementsMap;

	protected ModelParser[] parsers;

	protected Map<String, Object> concreteChromosome;

	public ModelInstantiatorUltimate(String modelFilename, String propertiesFilename) {

		String[] modelFileList = modelFilename.split(",");

		java.io.File modelDir = new java.io.File(modelFilename.trim()).getParentFile();
		if (modelDir != null && modelDir.isDirectory()) {
			java.io.FilenameFilter filter = (dir, name) -> 
				name.endsWith(".dtmc") || name.endsWith(".ctmc") || name.endsWith(".mdp");
			String[] modelFiles = modelDir.list(filter);
			if (modelFiles != null) {
				modelFileList = Arrays.stream(modelFiles)
					.map(f -> new java.io.File(modelDir, f).getAbsolutePath())
					.toArray(String[]::new);
			}
		}
		parsers = new ModelParser[modelFileList.length];

		String[] propertiesFileList = propertiesFilename.split(",");


		for (int i = 0; i < modelFileList.length; i++) {

			if (!new java.io.File(modelFileList[i].trim()).exists()) {
				throw new IllegalArgumentException("Model file does not exist: "
						+ modelFileList[i].trim());
			}

			if (!new java.io.File(propertiesFileList[i].trim()).exists()) {
				throw new IllegalArgumentException("Properties file does not exist: "
						+ propertiesFileList[i].trim());
			}

			parsers[i] = new ModelParser(modelFileList[i].trim(), propertiesFileList[i].trim());
		}

		elementsMap = new HashMap<AbstractGene, Evolvable>();
		concreteChromosome = new HashMap<String, Object>();
	}

	// public ModelInstantiatorUltimate(String modelFilename, String
	// propertiesFilename, ModelParser modelParser) {
	// parser = modelParser;

	// elementsMap = new HashMap<AbstractGene, Evolvable>();

	// concreteChromosome = new HashMap<String, Object>();
	// }

	// /**
	// * Copy constructor
	// * @param instantiator
	// * @throws EvoCheckerException
	// */
	// public ModelInstantiatorUltimate (ModelInstantiator instantiator) throws
	// EvoCheckerException{
	// parser = new ModelParser(instantiator.parser);
	// }

	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getGeneEvolvableMap();
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getConcreteModel(Collection<AbstractGene> genes) {
		StringBuilder concreteModel = new StringBuilder(parser.getModelType().toString().toLowerCase() + "\n\n");

		for (AbstractGene gene : genes) {
			if (gene instanceof IntegerGene) {
				concreteModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			} else if (gene instanceof DoubleGene) {
				concreteModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			} else if (gene instanceof DistributionGene) {
				concreteModel.append(elementsMap.get(gene)
						.getConcreteCommand((double[]) gene.getAllele()));
			} else if (gene instanceof AlternativeModuleGene) {
				concreteModel.append(elementsMap.get(gene).getConcreteCommand(gene.getAllele()));
			}
		}

		concreteModel.append("\n" + parser.getInternalModelRepresentation());

		// System.err.println(concreteModel);
		return concreteModel.toString();
	}

	@Override
	public String getPropertyFileName() {
		// the .ultimate file
		return parser.getPropertyFileName();
	}

	/**
	 * Get list of evolvable elements
	 * 
	 * @return
	 */
	@Override
	public List<Evolvable> getEvolvableList() {
		
		List<Evolvable> allEvolvables = new ArrayList<Evolvable>();

		for (ModelParser parser : parsers) {
			allEvolvables.addAll(parser.getEvolvableList());
		}

		return allEvolvables;
	}

	public List<AbstractGene> getGeneList() {
		return (List<AbstractGene>) Arrays.asList(this.elementsMap.keySet().toArray(new AbstractGene[0]));
	}

	public Map<String, Object> getChromosome(List<AbstractGene> genes) {
		concreteChromosome.clear();

		for (AbstractGene gene : genes) {
			concreteChromosome.put(elementsMap.get(gene).getName(), gene.getAllele());
		}
		return concreteChromosome;
	}

	protected String getInternalModelRepresentation() throws EvoCheckerException {
		throw new EvoCheckerException("Method not implemented for Ultimate model instantiator.");
	}

	@Override
	public MODEL_TYPE getModelType() {
		return MODEL_TYPE.ULTIMATE;
	}

}
