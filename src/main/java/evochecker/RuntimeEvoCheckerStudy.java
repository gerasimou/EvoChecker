package evochecker;

import evochecker.auxiliary.Utility;

public class RuntimeEvoCheckerStudy {

	
	public static void main(String[] args) {
		EvoChecker evoChecker = new EvoChecker();

		int MAXRUNS = 30;

//		String seedings[] = new String[] {"BEST", "POPULATION"};
//		
////		for all the seeding techniques
//		for (String seeding : seedings){
//			Utility.setProperty("SEEDING", seeding);
			System.out.println(Utility.getProperty("SEEDING"));

			//run for MAXRUNS
			for (int run=0; run<MAXRUNS; run++){
				evoChecker.main(null);
			}
//		}
		
	}

}
