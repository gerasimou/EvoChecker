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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.DistributionGene;

public class ModelInvokerPrism implements IModelInvoker {

	/**
	 * Default class constructor
	 */
	public ModelInvokerPrism() {
		
	}
	
	/** Copy constructor **/
	public ModelInvokerPrism (IModelInvoker anInvoker) {
		this();
	}
	
	// Q: evaluation of Prism return results for properties as they appear in properties file
	// not necessary first ones
	@Override
	public List<String> invokeParam(String model, String propertyFile, PrintWriter out, BufferedReader in, List<AbstractGene> genes) throws IOException {
		// TODO: need to modify to allow parametric model checking
//		String names[] = new String[genes.size()];
//		String lbs[] = new String[names.length]; // lower bounds
//		String ubs[] = new String[names.length]; // upper bounds
		List<String> names = new ArrayList<String>();
		List<String> lbs   = new ArrayList<String>();
		List<String> ubs = new ArrayList<String>();
		for (int i = 0; i < genes.size(); i++) {
			AbstractGene g = genes.get(i);
			if (g instanceof DistributionGene) {
				//TODO
			}
			names.add(g.getName());
			lbs.add(g.getMinValue().toString());
			ubs.add(g.getMaxValue().toString());
		}
		out.print(model + "#" + propertyFile + "#" + Arrays.toString(names.toArray()) + "#" + Arrays.toString(lbs.toArray()) + "#" + Arrays.toString(ubs.toArray()) + "\nEND\n");
		out.flush();

		List<String> results = processResult(in); 
		return results;
	}


	@Override
	public List<String> invoke(String model, String propertyFile, PrintWriter out, BufferedReader in) throws IOException {
		out.print(model + "#" + propertyFile + "\nEND\n");
		out.flush();

		List<String> results = processResult(in); 
		return results;
	}

	
	@Override
	public List<String> invokeParam(String model, String propertyFile, PrintWriter out, BufferedReader in) throws IOException {
		out.print(model + "#" + propertyFile + "#param\nEND\n");
		out.flush();

		List<String> results = processResult(in); 
		return results;
	}
	
	
	private List<String> processResult(BufferedReader in) throws IOException{
		String line;
		StringBuilder modelBuilder = new StringBuilder();
		do {
			// retrieve from prism
			line = in.readLine();
			if (line.endsWith("END"))
				break;
			modelBuilder.append(line);
			modelBuilder.append("\n");
		} while (true);

		// System.out.println("Received from PRISM: " + modelBuilder.toString());
		return Arrays.asList(modelBuilder.toString().trim().split("#"));

	}
	
}
