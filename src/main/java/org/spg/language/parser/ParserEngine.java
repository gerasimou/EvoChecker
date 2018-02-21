package org.spg.language.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.spg.language.grammar.antlr.src.gen.PrismLexer;
import org.spg.language.grammar.antlr.src.gen.PrismParser;

import evochecker.evolvable.Evolvable;
import evochecker.evolvable.EvolvableDistribution;
import evochecker.evolvable.EvolvableDouble;
import evochecker.evolvable.EvolvableInteger;
import evochecker.evolvable.EvolvableModule;
import evochecker.evolvable.EvolvableModuleAlternative;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;
import evochecker.parser.handler.PrismListener;
import evochecker.parser.handler.PrismVisitor;

public class ParserEngine implements InstantiatorInterface {

	private String modelFilename;
	
	/** properties filename */
	private String propertiesFilename;

	/** list of evolvable elements*/
	private List<Evolvable> evolvableList;

	/** String that keeps the model template */
	private String internalModelRepresentation;
	
	/** map that keeps pairs of genes and evolvable elements*/
	private Map<AbstractGene, Evolvable> elementsMap;

	List<AbstractGene> genesList;

	
	public ParserEngine(String fileName, String propertiesFilename) {
		String modelString = readFile(fileName);

		this.modelFilename = modelFilename;
		this.propertiesFilename = propertiesFilename;
		elementsMap = new HashMap<AbstractGene, Evolvable>();

		// generatedAntlrFiles("Prism.g4");
		runVisitor(modelString);
		// runListener(model);
	}
	
	
	public ParserEngine (ParserEngine aParser) throws EvoCheckerException{
		ParserEngine parser = (ParserEngine)aParser;
		this.internalModelRepresentation	= parser.internalModelRepresentation;
		this.propertiesFilename				= parser.propertiesFilename;
		this.evolvableList					= new ArrayList<Evolvable>();
		for (Evolvable element : parser.evolvableList)
			if (element instanceof EvolvableInteger)
				this.evolvableList.add(new EvolvableInteger(element));
			else if (element instanceof EvolvableDouble)
				this.evolvableList.add(new EvolvableDouble(element));
			else if (element instanceof EvolvableDistribution)
				this.evolvableList.add(new EvolvableDistribution((EvolvableDistribution)element));
			else if (element instanceof EvolvableModuleAlternative)
				this.evolvableList.add(new EvolvableModuleAlternative((EvolvableModuleAlternative)element));
		
		genesList = GenotypeFactory.createChromosome(evolvableList);
//		
////		GenotypeFactory.createChromosome(evolvableList);
		this.elementsMap					= new LinkedHashMap<AbstractGene, Evolvable>();
		for (int i=0; i<genesList.size(); i++){
			this.elementsMap.put(genesList.get(i), evolvableList.get(i));
		}
	}
	

	public void print() {
		for (Evolvable evolvable : evolvableList) {
			if (evolvable instanceof EvolvableDouble) {
				// modelString =
				// modelString.replaceFirst(evolvable.getIdentifier(), "2.5");
			} 
			else if (evolvable instanceof EvolvableInteger) {
				// modelString =
				// modelString.replaceFirst(evolvable.getIdentifier(), "2");
			} 
			else if (evolvable instanceof EvolvableDistribution) {
				// System.out.println(evolvable.toString());
			} 
			else if (evolvable instanceof EvolvableModule) {
				// System.out.println(evolvable.toString());
			} 
			else if (evolvable instanceof EvolvableModuleAlternative) {
				System.out.println(evolvable.toString());
			}

			// System.out.println(evolvable.toString());
		}

		System.out.println(internalModelRepresentation + evolvableList.size());
		//
	}

//	@SuppressWarnings("unused")
//	private void generatedAntlrFiles(String inputFile) {
//		String[] arg0 = { "-visitor", inputFile, "-o",
//				"./src/org/spg/antlr/src/gen" };
//		// "-package", "org.spg.antlr.src.gen"};
//		org.antlr.v4.Tool.main(arg0);
//	}

	@SuppressWarnings("unused")
	private void runListener(String inputString) {
		// create a CharStream that reads from standard input
		ANTLRInputStream input = new ANTLRInputStream(inputString);
		// create a lexer that feeds off of input CharStream
		PrismLexer lexer = new PrismLexer(input);
		// create a buffer of tokens pulled from the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create a parser that feeds off the tokens buffer
		PrismParser parser = new PrismParser(tokens);
		// begin parsing at model rule
		ParseTree tree = parser.model();

		// Create a generic parse tree walker that can trigger callbacks
		ParseTreeWalker walker = new ParseTreeWalker();
		// Create a listener
		PrismListener listener = new PrismListener(parser);
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(listener, tree);

		listener.printMemory();
	}

	@SuppressWarnings("unused")
	private void runVisitor(String inputString) {
		// create a CharStream that reads from standard input
		ANTLRInputStream input = new ANTLRInputStream(inputString);
		// create a lexer that feeds off of input CharStream
		PrismLexer lexer = new PrismLexer(input);
		// create a buffer of tokens pulled from the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create a parser that feeds off the tokens buffer
		PrismParser parser = new PrismParser(tokens);
		// begin parsing at prog rule
		ParseTree tree = parser.model();
		// Create the visitor
		PrismVisitor visitor = new PrismVisitor();
		// and visit the nodes
		visitor.visit(tree);

		//generate list with evolvable elements
		List<Evolvable> evolvableList = visitor.getEvolvableList();
		setEvolvableList(evolvableList);

		//get internal model representation
		String modelString = visitor.getInternalModelRepresentation();
		setInternalModelRepresentation(modelString);
	}

	@SuppressWarnings("resource")
	private String readFile(String fileName) {
		StringBuilder model = new StringBuilder(100);
		BufferedReader bfr = null;

		try {
			bfr = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			while ((line = bfr.readLine()) != null) {
				model.append(line + "\n");
			}
			model.delete(model.length() - 1, model.length());
			return model.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setEvolvableList(List<Evolvable> evolvableList) {
		this.evolvableList = evolvableList;
	}


	public List<Evolvable> getEvolvableList() {
		return this.evolvableList;
	}

	/**
	 * Set the internal model representation as string
	 * @param modelString
	 */
	private void setInternalModelRepresentation(String modelString) {
		this.internalModelRepresentation = modelString;
	}


	/**
	 * Return the internal model representation
	 * @return
	 */
	public String getInternalModelRepresentation() {
		return internalModelRepresentation;
	}
	
	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getMapping();
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getValidModelInstance(List<AbstractGene> genes) {
		StringBuilder concreteModel = new StringBuilder(this.internalModelRepresentation);
		for (AbstractGene gene : genes) {
			if (gene instanceof IntegerGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			} 
			else if (gene instanceof DoubleGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			} 
			else if (gene instanceof DiscreteDistributionGene) {
				concreteModel.append(elementsMap.get(gene)
										.getCommand((double[]) 
												gene.getAllele()));
			} 
			else if (gene instanceof AlternativeModuleGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			}
		}
		// System.err.println(concreteModel);
		return concreteModel.toString();
	}

	@Override
	public String getPrismPropertyFileName() {
		return this.propertiesFilename;
	}
	
	public List<AbstractGene> getGeneList(){
		return (List<AbstractGene>)Arrays.asList(this.elementsMap.keySet().toArray(new AbstractGene[0]));				
	}

}
