package evochecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import evochecker.auxiliary.Utility;

public class RuntimeEvoCheckerStudy {

	
	public static void main(String[] args) {
		EvoChecker evoChecker = new EvoChecker();

		int MAXRUNS = 30;

//		System.out.println(Utility.getProperty("SEEDING"));
		//run for MAXRUNS
//		for (int run=0; run<MAXRUNS; run++){
//			setupDisruptiveEvent("models/FX/runtime/fxSmall13Template.pm", "models/FX/runtime/fxSmall13.pm");
//			evoChecker.main(null);
//		}
		

		//run for MAXRUNS
		for (int run=0; run<MAXRUNS; run++){
			evoChecker.main(null);	
		}
	}

	
	
	/**
	 * Setup a disruptive event where only one implementation per service is functioning properly
	 * i.e., its reliability is low 
	 * @param template
	 * @param output
	 */
	private static void setupDisruptiveEvent(String template, String output){
		StringBuilder str 			= new StringBuilder(Utility.readFile(template));
		str.append("// Reliability: user-defined parameters\n");
		Random rand					= new Random(System.currentTimeMillis());
		
		int services[] 				= new int[]{1,2,4,5};
		
		for (int service : services){
			int numOfImplementations 	= 3;
			List<Integer> implementations = new ArrayList<>(Arrays.asList(1,2,3));

			for (int i=1; i<numOfImplementations; ){
				int implementation = implementations.get(rand.nextInt(numOfImplementations));
				double randomValue = 0.2 + (0.5 - 0.2) * rand.nextDouble();
				str.append("const double op" + service + "S" + implementation + "Fail=" + randomValue +";\n");
				numOfImplementations--;
				implementations.remove(implementations.indexOf(implementation));
			}
			double randomValue = 0.006 + (0.01 - 0.006) * rand.nextDouble();

			str.append("const double op" + service + "S" + implementations.get(0) + "Fail=" + randomValue +";\n");			
		}
		str.append("\n\n");
		
		System.out.println(str.toString());
//		Utility.exportToFile(output, str.toString(), false);
	}
}
