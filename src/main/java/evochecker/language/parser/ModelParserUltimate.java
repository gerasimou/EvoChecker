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

public class ModelParserUltimate implements IModelParser {

	/** properties filename */
	private String propertiesFilename;

	/** model filename */
	protected String modelFilename;

	/** String that keeps the model template */
	protected String internalModelRepresentation;

	/** list of evolvable elements */
	protected List<Evolvable> evolvableList;

	/** model type **/
	protected MODEL_TYPE modelType;

	/**
	 * Parser Engine Constructor
	 * 
	 * @param modelFilename
	 * @param propertiesFilename
	 */
	public ModelParserUltimate(String modelFilename, String propertiesFilename) {
		this.modelFilename = modelFilename;
		this.propertiesFilename = propertiesFilename;
		this.modelType = MODEL_TYPE.ULTIMATE;

		parse();
	}

	/**
	 * Parser engine default copy constructor
	 */
	protected ModelParserUltimate(ModelParserUltimate aParser) {
		this.modelFilename = aParser.modelFilename;
		this.propertiesFilename = aParser.propertiesFilename;
		this.modelType = MODEL_TYPE.ULTIMATE;
		this.internalModelRepresentation = aParser.internalModelRepresentation;
		this.modelType = aParser.modelType;

		this.evolvableList = new ArrayList<Evolvable>();
		for (Evolvable element : aParser.evolvableList)
			if (element instanceof EvolvableInteger)
				this.evolvableList.add(new EvolvableInteger((EvolvableInteger) element));
			else if (element instanceof EvolvableDouble)
				this.evolvableList.add(new EvolvableDouble((EvolvableDouble) element));
			else if (element instanceof EvolvableDistribution)
				this.evolvableList.add(new EvolvableDistribution((EvolvableDistribution) element));
			else if (element instanceof EvolvableModuleAlternative)
				this.evolvableList.add(new EvolvableModuleAlternative((EvolvableModuleAlternative) element));
		// TODO
		// else if (element instanceof EvolvableOption)
		// this.evolvableList.add(new EvolvableOption((EvolvableOption))element);
	}

	/**
	 * Parse input
	 */
	protected void parse() {

		// get a list of all the model files associated with ULTIMATE model

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		try {
			root = mapper.readTree(new File("modelFilename"));
		} catch (IOException e) {
			System.err.println("Error reading configuration file: " + e.getMessage());
			System.exit(1);
		}

		JsonNode models = root.get("models");

		for (JsonNode model : models) {
			try {
				String modelString = FileUtil.readFile(
						model.get("fileName").asText());
				runVisitor(modelString);
			} catch (EvoCheckerException e) {
				// e.printStackTrace();
				System.err.println(e.getMessage() + ".");
				System.exit(0);
			}
		}
	}

	/**
	 * Run visitor
	 * 
	 * @param inputString
	 * @throws EvoCheckerException
	 */
	private void runVisitor(String inputString) throws EvoCheckerException {
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
			throw new EvoCheckerException("The input model is incorrect! Please fix the errors and try again. Exiting");

		// Create the visitor
		PrismVisitor visitor = new PrismVisitor();
		// and visit the nodes
		visitor.visit(tree);

		// generate list with evolvable elements
		List<Evolvable> evolvableList = visitor.getEvolvableList();
		this.evolvableList.addAll(evolvableList);

		// set internal model representation
		// String modelString = visitor.getInternalModelRepresentation();
		this.internalModelRepresentation = "ULTIMATE MODEL"; // modelString;
	}

	/**
	 * Print the evolvable elements
	 */
	public void printEvolvableElements() {
		for (Evolvable evolvable : evolvableList) {
			System.out.println(evolvable.toString());
		}

		System.out.println(internalModelRepresentation + evolvableList.size());
	}

	@Override
	public String getInternalModelRepresentation() {
		return this.internalModelRepresentation;
	}

	@Override
	public String getPropertyFileName() {
		return this.propertiesFilename;
	}

	@Override
	public List<Evolvable> getEvolvableList() {
		return this.evolvableList;
	}

	public void setEvolvableList(List<Evolvable> evolvableList) {
		this.evolvableList = evolvableList;
	}

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
