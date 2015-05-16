package evochecker.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleConstGene;
import evochecker.genetic.genes.IntegerConstGene;
import evochecker.parser.evolvable.Evolvable;
import evochecker.parser.evolvable.EvolvableDistribution;
import evochecker.parser.evolvable.EvolvableDouble;
import evochecker.parser.evolvable.EvolvableInteger;
import evochecker.parser.evolvable.EvolvableModule;
import evochecker.parser.evolvable.EvolvableModuleAlternative;
import evochecker.parser.handler.PrismListener;
import evochecker.parser.handler.PrismVisitor;
import evochecker.parser.src.gen.PrismLexer;
import evochecker.parser.src.gen.PrismParser;

public class ParserEngine implements InstantiatorInterface {

	private String modelFilename;
	private String propertiesFilename;

	private List<Evolvable> evolvableList;
	private String modelTemplate;
	private Map<AbstractGene, Evolvable> elementsMap;

	public ParserEngine(String fileName, String propertiesFilename) {
		String modelString = readFile(fileName);

		this.modelFilename = modelFilename;
		this.propertiesFilename = propertiesFilename;
		elementsMap = new HashMap<AbstractGene, Evolvable>();

		// generatedAntlrFiles("Prism.g4");
		runVisitor(modelString);
		// runListener(model);
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

		System.out.println(modelTemplate + evolvableList.size());
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

		List<Evolvable> evolvableList = visitor.getEvolvableList();
		setEvolvableList(evolvableList);

		String modelString = visitor.getModelString();
		setModelString(modelString);
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

	private void setModelString(String modelString) {
		this.modelTemplate = modelString;
	}

	public List<Evolvable> getEvolvableList() {
		return this.evolvableList;
	}

	public String getModelTemplate() {
		return modelTemplate;
	}

	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getMapping();
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getPrismModelInstance(List<AbstractGene> genes) {
		StringBuilder concreteModel = new StringBuilder(this.modelTemplate);
		for (AbstractGene gene : genes) {
			if (gene instanceof IntegerConstGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			} 
			else if (gene instanceof DoubleConstGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			} 
			else if (gene instanceof DiscreteDistributionGene) {
				concreteModel.append(elementsMap.get(gene).getCommand((double[]) gene.getAllele()));
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

}
