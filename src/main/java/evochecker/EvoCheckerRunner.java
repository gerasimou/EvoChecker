package evochecker;

import java.util.Arrays;

public class EvoCheckerRunner {

	public static void main(String[] args) {
		String evaluations[];
//		if (args.length > 0)
//			evaluations = args;
//		else
			evaluations = new String[] {"1000", "2000", "5000", "10000"};
		
		double[] time  = new double[evaluations.length];
		String[] stats = new String[evaluations.length];
		
		for (int i=0; i<evaluations.length; i++) {
			//1) Create EvoChecker instance
			EvoChecker ec = new EvoChecker();
			
			//2) Set configuration file
			String configFile ="config.properties"; 
			ec.setConfigurationFile(configFile);

			ec.setProperty("PLOT_PARETO_FRONT", "false");
			ec.setProperty("MAX_EVALUATIONS", evaluations[i]);
			
			//3) Start EvoChecker
			ec.start();
			
			time[i]  = ec.getExecutionTime();
			stats[i] = ec.getStatistics();
			System.out.println(time[i]);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		System.out.println("Time:\t"  + Arrays.toString(time));	
		System.out.println("Stats:\t" + Arrays.toString(stats));	
	}

}
