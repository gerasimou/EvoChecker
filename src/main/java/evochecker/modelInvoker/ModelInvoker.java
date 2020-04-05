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
import java.util.Arrays;
import java.util.List;

import evochecker.genetic.genes.AbstractGene;

public class ModelInvoker implements IModelInvoker {

	/**
	 * Default class constructor
	 */
	public ModelInvoker() {
		
	}
	
	/** Copy constructor **/
	public ModelInvoker (IModelInvoker anInvoker) {
		this();
	}
	
	// Q: evaluation of Prism return results for properties as they appear in properties file
	// not necessary first ones
	@Override
	public List<String> invoke(String model, String propertyFile, PrintWriter out, BufferedReader in, List<AbstractGene> genes) throws IOException {

		
		// TODO: need to modify to allow parametric model checking
		String names[] = new String[genes.size()];
		String lbs[] = new String[names.length]; // lower bounds
		String ubs[] = new String[names.length]; // upper bounds
		for (int i = 0; i < genes.size(); i++) {
			AbstractGene g = genes.get(i);
			names[i] = g.getName();
			lbs[i] = g.getMinValue().toString();
			ubs[i] = g.getMaxValue().toString();
		}
		out.print(model + "#" + propertyFile + "#" + names + "#" + lbs + "#" + ubs + "\nEND\n");
		out.flush();

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

	@Override
	public List<String> invoke(String model, String propertyFile, PrintWriter out, BufferedReader in) throws IOException {

		out.print(model + "#" + propertyFile + "\nEND\n");
		out.flush();

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

		String res[] = modelBuilder.toString().trim().split("#");
//		System.out.println("Received from PRISM: "+ modelBuilder.toString());
		return Arrays.asList(res);
	}
	
}
