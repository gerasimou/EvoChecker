//==============================================================================
//	
//	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.language.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import evochecker.auxiliary.FileUtil;
import evochecker.evolvables.Evolvable;
import evochecker.evolvables.EvolvableDistribution;
import evochecker.evolvables.EvolvableDouble;
import evochecker.evolvables.EvolvableInteger;
import evochecker.evolvables.EvolvableModule;
import evochecker.evolvables.EvolvableModuleAlternative;
import evochecker.exception.EvoCheckerException;
import evochecker.language.parser.grammar.PrismLexer;
import evochecker.language.parser.grammar.PrismParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;

public class ModelParserUltimate extends ModelParser {

	private HashMap<String, List<Evolvable>> evolvableHashMap;
	private List<String> modelRepresentations;

	/**
	 * Parser Engine Constructor
	 * 
	 * @param modelFilename
	 * @param propertiesFilename
	 */
	public ModelParserUltimate(String modelFilename, String propertiesFilename) {
		super(modelFilename, propertiesFilename);
	}

	/**
	 * Parser engine default copy constructor
	 */
	protected ModelParserUltimate(ModelParserUltimate aParser) {
		super(aParser.modelFilename, aParser.propertiesFilename);
		// this.modelFilename = aParser.modelFilename;
		// this.propertiesFilename = aParser.propertiesFilename;
		// this.modelType = MODEL_TYPE.ULTIMATE;
		this.internalModelRepresentation = aParser.internalModelRepresentation;
		this.modelType = aParser.modelType;
		this.evolvableHashMap = new HashMap<>();

		for (Map.Entry<String, List<Evolvable>> entry : aParser.evolvableHashMap.entrySet()) {
			String fileName = entry.getKey();
			List<Evolvable> el = entry.getValue();
			List<Evolvable> newList = new ArrayList<>();
			for (Evolvable e : el) {
				if (e instanceof EvolvableInteger) {
					newList.add(new EvolvableInteger((EvolvableInteger) e));
				} else if (e instanceof EvolvableDouble) {
					newList.add(new EvolvableDouble((EvolvableDouble) e));
				} else if (e instanceof EvolvableDistribution) {
					newList.add(new EvolvableDistribution((EvolvableDistribution) e));
				} else if (e instanceof EvolvableModuleAlternative) {
					newList.add(
							new EvolvableModuleAlternative((EvolvableModuleAlternative) e));
				} else if (e instanceof EvolvableModule) {
					newList.add(new EvolvableModule((EvolvableModule) e));
				}
			}
			this.evolvableHashMap.put(fileName, newList);
		}

	}

	/**
	 * Parse input
	 */
	public void parse() throws EvoCheckerException {

		// this.evolvableList = new ArrayList<Evolvable>();
		this.evolvableHashMap = new HashMap<>();
		this.modelRepresentations = new ArrayList<String>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;

		System.out.println("Parsing ULTIMATE file: " + modelFilename);

		// Get the directory that modelFilename is in
		File modelFile = new File(modelFilename);
		String modelDirectory = modelFile.getParent();
		try {
			root = mapper.readTree(modelFile);
		} catch (IOException e) {
			throw new EvoCheckerException("Could not parse the ULTIMATE model file: " + e.getMessage());
		}

		JsonNode models = root.get("models");

		for (JsonNode model : models) {
			String fileName = model.get("fileName").asText(); // e.g. casino.dtmc
			String path = modelDirectory + "/" + fileName;
			System.out.println("Parsing component model at " + path);
			String modelString = FileUtil.readFile(path); // model string for casino.dtmc
			PrismVisitor visitor = runPrismVisitor(modelString);
			addEvolvablesFromVisitor(visitor, fileName);
			addModelRepresentationFromVisitor(visitor, fileName, model.get("id").asText());
		}

		this.internalModelRepresentation = String.join("@@@", modelRepresentations);

		System.out.println("\nEvolvables in world model:");
		printEvolvableElements();

	}

	/**
	 * Run visitor
	 * 
	 * @param inputString
	 * @throws EvoCheckerException
	 */
	private PrismVisitor runPrismVisitor(String inputString) throws EvoCheckerException {
		// create a CharStream that reads from standard input
		ANTLRInputStream input = new ANTLRInputStream(inputString);
		// create a lexer that feeds off of input CharStream
		PrismLexer lexer = new PrismLexer(input);
		// create a buffer of tokens pulled from the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create a parser that feeds off the tokens buffer
		PrismParser parser = new PrismParser(tokens);

		// setup custom error listener
		parser.removeErrorListeners();
		ModelParserErrorListener errorListener = new ModelParserErrorListener();
		parser.addErrorListener(errorListener);

		// begin parsing at prog rule
		ParseTree tree = parser.model();

		// stop if there is an error in the model
		if (errorListener.isInputFaulty())
			throw new EvoCheckerException(
					"The ULTIMATE model is incorrect! Please fix the errors and try again. Exiting");

		// Create the visitor
		PrismVisitor visitor = new PrismVisitor();
		// and visit the nodes
		visitor.visit(tree);

		return visitor;
	}

	private void addEvolvablesFromVisitor(PrismVisitor visitor, String fileName) {
		List<Evolvable> evolvableList = visitor.getEvolvableList();
		if (evolvableList != null) {
			// TODO: see if I can replace the fileName with modelId - should be possible and
			// cleaner.
			this.evolvableHashMap.put(fileName, evolvableList);
		}
	}

	private void addModelRepresentationFromVisitor(PrismVisitor visitor, String fileName, String modelId) {
		// TODO: see if I can remove fileName
		this.modelRepresentations
				.add("//" + modelId + "@" + fileName + "\n" + visitor.getInternalModelRepresentation());
	}

	/**
	 * Print the evolvable elements
	 */
	public void printEvolvableElements() {
		for (Map.Entry<String, List<Evolvable>> entry : evolvableHashMap.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().toString());
		}
	}

	// public String setInternalModelRepresentation(String modelId){

	// }

	@Override
	public String getInternalModelRepresentation() {

		return this.internalModelRepresentation;
	}

	// public void setInternalModelRepresentation() {

	// // return the internal model representation of the first model
	// // hacky solution needed for PRISM api

	// String inputString = this.modelStrings.get(0);

	// ANTLRInputStream input = new ANTLRInputStream(inputString);
	// PrismLexer lexer = new PrismLexer(input);
	// CommonTokenStream tokens = new CommonTokenStream(lexer);

	// PrismParser parser = new PrismParser(tokens);
	// ParseTree tree = parser.model();
	// PrismVisitor visitor = new PrismVisitor();
	// visitor.visit(tree);

	// String modelString = visitor.getInternalModelRepresentation();

	// this.internalModelRepresentation = modelString;
	// this.modelType = visitor.getModelType();

	// }

	@Override
	public String getPropertyFileName() {
		return this.propertiesFilename;
	}

	@Override
	public List<Evolvable> getEvolvableList() {

		List<Evolvable> allEvolvables = new ArrayList<>();
		if (evolvableHashMap != null && !evolvableHashMap.isEmpty()) {
			for (Map.Entry<String, List<Evolvable>> entry : evolvableHashMap.entrySet()) {
				allEvolvables.addAll(entry.getValue());
			}
		}
		return allEvolvables;
	}

	public HashMap<String, List<Evolvable>> getEvolvableHashMap() {
		return this.evolvableHashMap;
	}

	// public void setEvolvableList(List<Evolvable> evolvableList) {
	// this.evolvableList = evolvableList;
	// }

	@Override
	public MODEL_TYPE getModelType() {
		return this.modelType;
	}

}
