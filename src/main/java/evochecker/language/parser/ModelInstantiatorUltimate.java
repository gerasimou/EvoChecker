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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	protected ModelParserUltimate parser;

	protected Map<String, Object> concreteChromosome;

	private String modelFilename;
	private String propertiesFileName;

	public ModelInstantiatorUltimate(String modelFilename, String propertiesFilename) {
		this.modelFilename = modelFilename;
		this.propertiesFileName = propertiesFilename;

		parser = new ModelParserUltimate(modelFilename, propertiesFilename);
		parser.parse();
		elementsMap = new HashMap<AbstractGene, Evolvable>();
		concreteChromosome = new HashMap<String, Object>();
	}

	public ModelInstantiatorUltimate(String modelFilename, String propertiesFilename, ModelParserUltimate modelParser) {
		parser = modelParser;
		parser.parse();
		this.modelFilename = modelFilename;
		this.propertiesFileName = propertiesFilename;

		elementsMap = new HashMap<AbstractGene, Evolvable>();
		concreteChromosome = new HashMap<String, Object>();
	}

	/**
	 * Copy constructor
	 * 
	 * @param instantiator
	 * @throws EvoCheckerException
	 */
	public ModelInstantiatorUltimate(ModelInstantiatorUltimate instantiator) throws EvoCheckerException {
		parser = new ModelParserUltimate(instantiator.parser);
		parser.parse();
	}

	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getGeneEvolvableMap();
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getConcreteModel(Collection<AbstractGene> genes) {
		// StringBuilder concreteModel = new
		// StringBuilder(parser.getModelType().toString().toLowerCase() +"\n\n");
		StringBuilder concreteModel = new StringBuilder("");

		String[] internalRepresentations = parser.getInternalModelRepresentation().split("@@@");
		// parser.printEvolvableElements();
		HashMap<String, List<Evolvable>> evolvableHashMap = parser.getEvolvableHashMap();
		// System.out.println(evolvableHashMap);

		for (String ir : internalRepresentations) {

			String fileName = ir.split("\n")[0].replace("//", "").split("@")[1];
			// System.out.println("fileName: " + fileName);

			List<Evolvable> thisModelEvolvables = evolvableHashMap.get(fileName);
			List<String> thisModelEvolvablesNames = thisModelEvolvables.stream()
					.map(Evolvable::getName).collect(Collectors.toList());;


			for (AbstractGene gene : genes) {
				if (thisModelEvolvablesNames.contains(gene.getName())) {
					// System.out.println("Gene: " + gene.getName() + " in " + fileName);
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
			}

			concreteModel.append("\n" + ir + "@@@");

		}

		return concreteModel.toString();
	}

	@Override
	public String getPropertyFileName() {
		return parser.getPropertyFileName();
	}

	/**
	 * Get list of evolvable elements
	 * 
	 * @return
	 */
	@Override
	public List<Evolvable> getEvolvableList() {
		return parser.getEvolvableList();
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

	protected String getInternalModelRepresentation() {
		return parser.getInternalModelRepresentation();
	}

	@Override
	public MODEL_TYPE getModelType() {
		return parser.getModelType();
	}

	public String getModelFilename() {
		return modelFilename;
	}

	public String getPropertiesFileName() {
		return propertiesFileName;
	}

}
