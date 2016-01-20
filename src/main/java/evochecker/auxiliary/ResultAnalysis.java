package evochecker.auxiliary;

import java.io.File;
import java.util.ArrayList;
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
	public static List<String> getUtilityResultsAsList(List<String> filenames){
		List<String> utilityList = new ArrayList<>();

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
		
		//assemble a list utility lists
		List<String> utilityList = getUtilityResultsAsList(algorithmsDirs);
		
		//export
		Utility.exportToFile(utilityList, "data/experiment2.csv");
	}
	
	
	public static void main(String[] args) {
		prepareUtilityLists();
	}
}
