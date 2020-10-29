package examples;

import evochecker.EvoChecker;

public class EvoCheckerRunner {

	public static void main(String[] args) {
		//1) Create EvoChecker instance
		EvoChecker ec = new EvoChecker();
		
		//2) Set configuration file
		String configFile ="config.properties"; 
		ec.setConfigurationFile(configFile);
		
		//3) Start EvoChecker
		ec.start();		
	}

}
