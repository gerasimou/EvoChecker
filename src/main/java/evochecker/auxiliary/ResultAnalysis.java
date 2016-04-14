package evochecker.auxiliary;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultAnalysis {

	/** Return a list of utility results for a specific algorithm */
	public static List<Double> getUtilityResultsAsList(String filename){
		List<Double> utilityList = new ArrayList<>();

		int run=0;
		do{
			String file = filename +"."+ run;
			String result = Utility.readFile(file);
			
			String objectives[] = result.split(" ");
			double utility 		= Double.parseDouble(objectives[objectives.length-1]);
			utilityList.add(utility);
			System.out.println(objectives[objectives.length-1]);
			run++;
		}
		while(new File(filename +"."+ run).exists());
		
		return utilityList;
	}
	
	
	/** Return a list of utility results for a specific algorithm */
	public static List<Object> getUtilityResultsAsList(List<String> filenames){
		List<Object> utilityList = new ArrayList<>();

		int run=0;		
		do{
			String rowString = "";
			for (String filename : filenames){
				String file = filename +"."+ run;
				String result = Utility.readFile(file);
				
				String objectives[] = result.split(" ");
				rowString += objectives[objectives.length-1]; 
				
				//if it is not the last entry, add a comma
				if (!filename.equals(filenames.get(filenames.size()-1)))
					rowString +=",";
			}
			utilityList.add(rowString);
			run++;
		}		
		while(new File(filenames.get(0) +"."+ run).exists());
		
		return utilityList;
	}
	
	
	public static void prepareUtilityLists(){
		
		List<String> algorithmsDirs= new ArrayList<>();
		for (String algorithm : new String[]{"pgGA","pRandomSearchSingle"}){
			algorithmsDirs.add("data/EvoStudyFXsmall/data/" + algorithm + "/GeneticProblemSingle/FUN");
		}
		
		//assemble a utility list
		List<Object> utilityList = getUtilityResultsAsList(algorithmsDirs);
		
		//export
		Utility.exportToFile(utilityList, "data/experiment2.csv");
	}

	
	
	private static void prepareTimelineData(){
		//read input model
		String inputModelFilename 	= "models/FX/runtime/fxSmall%.pm";
		String propertiesFilename	= "models/FX/fxSmall.pctl";
		PrismAPI prism				= new PrismAPI(propertiesFilename);

		int servicesIndex[]			= new int[] {1,2,4,5};
		
		
	   //Strategy templates
		String probStrategyTemplate	= Utility.readFile("models/ResultAnalysisFX/probabilisticStrategy.txt"); 
		String seqStrategyTemplate	= Utility.readFile("models/ResultAnalysisFX/sequentialStrategy.txt");
	
	
		int count=0;

		//
		String resultsFilename		= "/Users/sgerasimou/Documents/Programming/workspace R/RunEvo/GA_Population/FX_Small_New/VAR_SGA_POPULATION_E%_5000";
		for (int event=1; event<=11; event++){
			String results 			= Utility.readFile(resultsFilename.replace("%", event+""));
			String[] resultsArray		= results.split("\n");
						
			for (int config=0; config<29; config++){
				StringBuilder controller = new StringBuilder("\n\n");
				
				String configurations[] = resultsArray[config].split(" ");
//				System.out.println(configIndex +"\t"+ Arrays.toString(configuration));
				
				//prepare probabilities
				for (int j=0; j<4; j++){
					int index = j*3;
					double sum 				= Double.parseDouble(configurations[index]) + Double.parseDouble(configurations[index+1]) + Double.parseDouble(configurations[index+2]);
					configurations[index] 	= (Double.parseDouble(configurations[index]) / sum) + "";
					configurations[index+1] 	= (Double.parseDouble(configurations[index+1]) / sum) + "";
					configurations[index+2] 	= (1-Double.parseDouble(configurations[index]) - Double.parseDouble(configurations[index+1])) + "";
					
					controller.append("const double probOp" + (servicesIndex[j]) +"1 = "+  configurations[index] + ";\n");
					controller.append("const double probOp" + (servicesIndex[j]) +"2 = "+  configurations[index+1] + ";\n");
					controller.append("const double probOp" + (servicesIndex[j]) +"3 = "+  configurations[index+2] + ";\n");
				}
				
				//prepare opCode
				for (int j=0; j<4; j++){
					int index = 12;
					controller.append("const int op" + servicesIndex[j] +"Code = "+ configurations[index+j] +";\n");
				}
				
				//prepare sequence
				for (int j=0; j<4; j++){
					int index = 16;
					controller.append("const int seqOp" + servicesIndex[j] +" = "+ configurations[index+j] +";\n");
				}

				//prepare strategy
				for (int j=0; j<4; j++){
					int index = 20;
					controller.append("const int STRATEGYOP" + servicesIndex[j] +" = "+ configurations[index+j] +";\n");
					if (configurations[index+j].equals("1")){//seq strategy
						controller.append(seqStrategyTemplate.replaceAll("%", servicesIndex[j]+""));
					}
					else{
						controller.append(probStrategyTemplate.replaceAll("%", servicesIndex[j]+""));
					}
					controller.append("\n\n");
				}

				//evaluate for current & next event
				for (int evaluation=0; evaluation<2; evaluation++){
					String currentEventModel	= Utility.readFile(inputModelFilename.replace("%", (event+evaluation)+""));
					
					StringBuilder concreteModel = new StringBuilder(currentEventModel +"\n");
					concreteModel.append(controller);
//					System.out.println(concreteModel);
					
					prism.loadModel(concreteModel.toString());
					List<Double> resultsList 	=  prism.runPrism();
					if (resultsList.get(3)<0.98){
						resultsList.set(3, 10.0);
						count++;
					}
					System.out.println((event+evaluation) +"\t"+ config +"\t"+ resultsList.toString());
				}
				
//				System.out.println(configIndex +"\t"+ Arrays.toString(configuration) +"\n");
//				System.out.println(model);
			
//				prism.loadModel(controller.toString());
//				List<Double> resultsList =  prism.runPrism();
//				System.out.println(event +"\t"+ config +"\t"+ resultsList.toString());
				
	
			}
			System.out.println(count);

		}		
		
	}
	
	
	
	
	
	public static void main(String[] args) {
//		prepareUtilityLists();
		prepareTimelineData();
//		String probStrategyTemplate	= Utility.readFile("models/ResultAnalysisFX/probabilisticStrategy.txt"); 
//		System.out.println(probStrategyTemplate.replaceAll("%", "2"));

	}
}
