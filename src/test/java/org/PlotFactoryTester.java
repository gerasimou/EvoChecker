package org;

import org.junit.Test;

import evochecker.auxiliary.Utility;
import evochecker.plotting.PlotFactory;

public class PlotFactoryTester {

//	public static void main(String[] args) throws IOException, InterruptedException {
	
	@Test
    public void userPlotFactory() {	
		String frontFile 	= "/Users/simos/Git/EvoChecker/data/MARC/NSGAII/MARC_NSGAII_170923_1329488796374500851926967_Front";
		String configFile	= "config.properties";
		

		Utility.setPropertiesFile(configFile);
		PlotFactory.plotParetoFront(frontFile, 2);
	}
}
