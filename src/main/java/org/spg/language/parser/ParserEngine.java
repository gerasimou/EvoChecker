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
package org.spg.language.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.spg.language.prism.grammar.PrismLexer;
import org.spg.language.prism.grammar.PrismParser;

import evochecker.auxiliary.FileUtil;
import evochecker.evolvable.Evolvable;
import evochecker.evolvable.EvolvableDistribution;
import evochecker.evolvable.EvolvableDouble;
import evochecker.evolvable.EvolvableInteger;
import evochecker.evolvable.EvolvableModule;
import evochecker.evolvable.EvolvableModuleAlternative;



public class ParserEngine2{

	/** String that keeps the model template */
	protected String internalModelRepresentation;

	/** properties filename */
	protected String propertiesFilename;

	/** list of evolvable elements*/
	protected List<Evolvable> evolvableList;	
	
	protected String modelFilename;
	

	
	/**
	 * Parser Engine Constructor
	 * @param modelFilename
	 * @param propertiesFilename
	 */
	public ParserEngine2(String modelFilename, String propertiesFilename){
		this.modelFilename 		= modelFilename;
		this.propertiesFilename	= propertiesFilename;		
		
		parse();			
	}

	/**
	 * Parser engine default copy constructor
	 */
	public ParserEngine2(ParserEngine2 aParser){
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

		ParserEngine2 parser = new ParserEngine2(modelFilename, propertiesFilename);

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
	public void parse() {
		String modelString = FileUtil.readFile(modelFilename);
		runVisitor(modelString);
//		runListener(modelString);

	}
	

	
	
	/**
	 * Run visitor
	 * @param inputString
	 */
	@SuppressWarnings("unused")
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
		setEvolvableList(evolvableList);

		//get internal model representation
		String modelString = visitor.getInternalModelRepresentation();
		setInternalModelRepresentation(modelString);		
	}
	
	
	
	/**
	 * Print the evolvable elements
	 */
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
//				System.out.println(evolvable.toString());
			}

			 System.out.println(evolvable.toString());
		}

		System.out.println(internalModelRepresentation + evolvableList.size());
	}
	
	
	
	/**
	 * Set evolvable list
	 * @param evolvableList
	 */
	private void setEvolvableList (List<Evolvable> evolvableList){
		this.evolvableList = evolvableList;
	}
	
	
	/**
	 * Get list of evolvable elements
	 * @return
	 */
	public List<Evolvable> getEvolvableList (){
		return this.evolvableList;
	}

	
	
	/**
	 * Set the internal model representation as string
	 * @param modelString
	 */
	private void setInternalModelRepresentation(String modelString) {
		this.internalModelRepresentation = modelString;
	}

	public String getInternalModelRepresentation() {
		return this.internalModelRepresentation;
	}
	
}
