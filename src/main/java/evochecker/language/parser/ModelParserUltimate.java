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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ModelParserUltimate implements IModelParser {

	/** properties filename */
	private String propertiesFilename;

	/** model filename */
	protected String modelFilename;

	/** String that keeps the model template */
	protected String internalModelRepresentation;

	/** list of evolvable elements */
	// protected List<Evolvable> evolvableList;
	private HashMap<String, Evolvable> evolvableHashMap;

	/** model type **/
	protected MODEL_TYPE modelType;

	private List<String> modelRepresentations;

	/**
	 * Parser Engine Constructor
	 * 
	 * @param modelFilename
	 * @param propertiesFilename
	 */
	public ModelParserUltimate(String modelFilename, String propertiesFilename) {
		this.modelFilename = modelFilename;
		this.propertiesFilename = propertiesFilename;
		// this.modelType = MODEL_TYPE.ULTIMATE;

		parse();
	}

	/**
	 * Parser engine default copy constructor
	 */
	protected ModelParserUltimate(ModelParserUltimate aParser) {
		this.modelFilename = aParser.modelFilename;
		this.propertiesFilename = aParser.propertiesFilename;
		// this.modelType = MODEL_TYPE.ULTIMATE;
		this.internalModelRepresentation = aParser.internalModelRepresentation;
		this.modelType = aParser.modelType;
		this.evolvableHashMap = new HashMap<>();

		for (Map.Entry<String, Evolvable> entry : aParser.evolvableHashMap.entrySet()) {
			Evolvable value = entry.getValue();
			if (value instanceof EvolvableInteger) {
				this.evolvableHashMap.put(entry.getKey(), new EvolvableInteger((EvolvableInteger) value));
			} else if (value instanceof EvolvableDouble) {
				this.evolvableHashMap.put(entry.getKey(), new EvolvableDouble((EvolvableDouble) value));
			} else if (value instanceof EvolvableDistribution) {
				this.evolvableHashMap.put(entry.getKey(), new EvolvableDistribution((EvolvableDistribution) value));
			} else if (value instanceof EvolvableModuleAlternative) {
				this.evolvableHashMap.put(entry.getKey(), new EvolvableModuleAlternative((EvolvableModuleAlternative) value));
			} else if (value instanceof EvolvableModule) {
				this.evolvableHashMap.put(entry.getKey(), new EvolvableModule((EvolvableModule) value));
			}
		}

		// for (Evolvable element : aParser.getEvolvableHashMap())
		// if (element instanceof EvolvableInteger)
		// this.evolvableList.add(new EvolvableInteger((EvolvableInteger) element));
		// else if (element instanceof EvolvableDouble)
		// this.evolvableList.add(new EvolvableDouble((EvolvableDouble) element));
		// else if (element instanceof EvolvableDistribution)
		// this.evolvableList.add(new EvolvableDistribution((EvolvableDistribution)
		// element));
		// else if (element instanceof EvolvableModuleAlternative)
		// this.evolvableList.add(new
		// EvolvableModuleAlternative((EvolvableModuleAlternative) element));
		// // TODO
		// // else if (element instanceof EvolvableOption)
		// // this.evolvableList.add(new EvolvableOption((EvolvableOption))element);
	}

	/**
	 * Parse input
	 */
	protected void parse() {

		// this.evolvableList = new ArrayList<Evolvable>();
		this.evolvableHashMap = new HashMap<>();
		this.modelRepresentations = new ArrayList<String>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;

		System.out.println("Parsing ULTIMATE folder " + modelFilename);

		// Get the directory that modelFilename is in
		File modelFile = new File(modelFilename);
		String modelDirectory = modelFile.getParent();

		try {
			root = mapper.readTree(modelFile);
		} catch (IOException e) {
			System.err.println("Error reading configuration file: " + e.getMessage());
			System.exit(1);
		}

		JsonNode models = root.get("models");

		for (JsonNode model : models) {
			try {
				String fileName = model.get("fileName").asText();
				String path = modelDirectory + "/" + fileName;
				System.out.println("Parsing component model at " + path);
				String modelString = FileUtil.readFile(path);
				runVisitor(modelString, fileName);
			} catch (EvoCheckerException e) {
				// e.printStackTrace();
				System.err.println(e.getMessage() + ".");
				System.exit(0);
			}
		}

		System.out.println("\nEvolvables in world model:");
		for (Map.Entry<String, Evolvable> entry : evolvableHashMap.entrySet()) {
			System.out.println(entry.getKey() + "-" + entry.getValue());
		}
		System.out.println("\n");

		// this.setInternalModelRepresentation();
		this.internalModelRepresentation = String.join("\n@@@\n", modelRepresentations);
	}

	/**
	 * Run visitor
	 * 
	 * @param inputString
	 * @throws EvoCheckerException
	 */
	private void runVisitor(String inputString, String fileName) throws EvoCheckerException {
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

		// generate list with evolvable elements
		List<Evolvable> evolvableList = visitor.getEvolvableList();
		if (evolvableList != null) {
			// this.evolvableList.addAll(evolvableList);
			for (Evolvable e : evolvableList) {
				this.evolvableHashMap.put(fileName, e);
			}
		}

		// set internal model representation
		modelRepresentations.add("//"+ fileName +"\n" + visitor.getInternalModelRepresentation());
	}

	/**
	 * Print the evolvable elements
	 */
	public void printEvolvableElements() {
		for (Map.Entry<String, Evolvable> entry : evolvableHashMap.entrySet()) {
			System.out.println(entry.getValue().toString());
		}

		System.out.println(internalModelRepresentation + evolvableHashMap.size());
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
			for (Evolvable e: evolvableHashMap.values()) {
				allEvolvables.add(e);
			}
		}
		return allEvolvables;
	}

	public HashMap<String, Evolvable> getEvolvableHashMap() {
		return this.evolvableHashMap;
	}

	// public void setEvolvableList(List<Evolvable> evolvableList) {
	// this.evolvableList = evolvableList;
	// }

	@Override
	public MODEL_TYPE getModelType() {
		return this.modelType;
	}

	public static void main(String args[]) {
		String modelFilename = "models/SESAME-EDDI/palEvoChecker.prism";
		String propertiesFilename = "models/SESAME-EDDI/pal.csl";

		if (args.length == 2) {
			modelFilename = args[0];
			propertiesFilename = args[1];
		}

		ModelParser parser = new ModelParser(modelFilename, propertiesFilename);

		// Parse model
		System.out.println("Checking " + modelFilename);
		parser.parse();

		System.out.println("DONE");
	}

}
