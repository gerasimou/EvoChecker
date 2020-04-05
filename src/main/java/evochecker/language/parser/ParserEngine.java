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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import evochecker.language.parser.gen.PrismLexer;
import evochecker.language.parser.gen.PrismParser;



public class ParserEngine implements IModelParser{

	/** properties filename */
	private String propertiesFilename;

	/** model filename*/
	protected String modelFilename;

	/** String that keeps the model template */
	private String internalModelRepresentation;

	/** list of evolvable elements*/
	private List<Evolvable> evolvableList;	
		

	
	/**
	 * Parser Engine Constructor
	 * @param modelFilename
	 * @param propertiesFilename
	 */
	protected ParserEngine(String modelFilename, String propertiesFilename){
		this.modelFilename 		= modelFilename;
		this.propertiesFilename	= propertiesFilename;		
		
		parse();			
	}

	
	/**
	 * Parser engine default copy constructor
	 */
	protected ParserEngine(ParserEngine aParser){
		this.modelFilename 					= aParser.modelFilename;
		this.propertiesFilename				= aParser.propertiesFilename;
		this.internalModelRepresentation	= aParser.internalModelRepresentation;
		
		this.evolvableList					= new ArrayList<Evolvable>();
		for (Evolvable element : aParser.evolvableList)
			if (element instanceof EvolvableInteger)
				this.evolvableList.add(new EvolvableInteger((EvolvableInteger)element));
			else if (element instanceof EvolvableDouble)
				this.evolvableList.add(new EvolvableDouble((EvolvableDouble)element));
			else if (element instanceof EvolvableDistribution)
				this.evolvableList.add(new EvolvableDistribution((EvolvableDistribution)element));
			else if (element instanceof EvolvableModuleAlternative)
				this.evolvableList.add(new EvolvableModuleAlternative((EvolvableModuleAlternative)element));
			//TODO
//			else if (element instanceof EvolvableOption)
//				this.evolvableList.add(new EvolvableOption((EvolvableOption))element);
	}

	
	public static void main (String args[]){	
//		String 		modelFilename		= "models/Cluster/clusterTemplate2.sm";
//		String 		propertiesFilename 	= "models/Cluster/cluster.csl";

//		String 		modelFilename		= "models/COPE/cope.pm";
//		String 		propertiesFilename 	= "models/COPE/cope.pctl";

//		String 		modelFilename		= "models/DPM/DPM.pm";
//		String 		propertiesFilename 	= "models/DPM/dpm.pctl";

//		String 		modelFilename		= "models/FX/fx.pm";
//		String 		propertiesFilename 	= "models/FX/fx.pctl";
		
//		String 	modelFilename		= "models/Hadoop/HadoopTemplate.sm";
//		String	propertiesFilename 	= "models/Hadoop/HadoopTemplate.pctl";

//		String 		modelFilename		= "models/UUV/uuv.sm";
//		String 		propertiesFilename 	= "models/UUV/uuv.csl";

		String 		modelFilename		= "models/VIRUS/virus.pm";
		String 		propertiesFilename 	= "models/VIRUS/virus.pm";


		
		if (args.length == 2){
			modelFilename		= args[0];
			propertiesFilename 	= args[1];
		}

		ParserEngine parser = new ParserEngine(modelFilename, propertiesFilename);

		//Parse model
		System.out.println("Checking " + modelFilename);
		parser.parse();			
		
		
		//print some information
//		parser.print();
//		System.out.println(Arrays.toString(parser.evolvableList.toArray()));
		
		FileUtil.createDir("tests/Virus");
		FileUtil.saveToFile("tests/Virus/ParsedEvolvables.txt", Arrays.toString(parser.evolvableList.toArray()), false);
		FileUtil.saveToFile("tests/Virus/ParsedInternalRepresentation.txt", parser.internalModelRepresentation, false);
		
		System.out.println("DONE");
	}
	

	/** 
	 * Parse input
	 */
	private void parse() {
		String modelString = FileUtil.readFile(modelFilename);
		runVisitor(modelString);
	}
	
	
	/**
	 * Run visitor
	 * @param inputString
	 */
	private  void runVisitor(String inputString){
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
		//Create the visitor
		PrismVisitor visitor = new PrismVisitor();
		// and visit the nodes
		visitor.visit(tree);
		
		//generate list with evolvable elements
		List<Evolvable> evolvableList = visitor.getEvolvableList();
		this.evolvableList = evolvableList;

		//set internal model representation
		String modelString = visitor.getInternalModelRepresentation();
		this.internalModelRepresentation = modelString;
	}
	
	
	
	/**
	 * Print the evolvable elements
	 */
	public void printEvolvableElements() {
		for (Evolvable evolvable : evolvableList) {
			if (evolvable instanceof EvolvableDouble) {
				//...
			} 
			else if (evolvable instanceof EvolvableInteger) {
				//...
			} 
			else if (evolvable instanceof EvolvableDistribution) {
				//...
			} 
			else if (evolvable instanceof EvolvableModule) {
				//...
			} 
			else if (evolvable instanceof EvolvableModuleAlternative) {
				//...
			}

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

	
}
