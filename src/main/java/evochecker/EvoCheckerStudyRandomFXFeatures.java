package evochecker;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import evochecker.auxiliary.Utility;

public class EvoCheckerStudyRandomFXFeatures {
	
	Random rand = new Random(System.currentTimeMillis());
	List<Service> serviceList;
	List<Double> failureProbList;
	List<Double> costList;
	List<Double> timeList;

	private EvoCheckerStudyRandomFXFeatures(){
		serviceList		= new ArrayList<EvoCheckerStudyRandomFXFeatures.Service>();
		failureProbList = new ArrayList<Double> ();
		costList 		= new ArrayList<Double> ();
		timeList 		= new ArrayList<Double> ();
	}
	
	public static void main(String[] args) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM_HH:mm:ss");
		Date date 			  = new Date();		
		
		EvoCheckerStudyRandomFXFeatures mainX = new EvoCheckerStudyRandomFXFeatures();
		int services			= 4;
//		int operationsArray[] 	= {1, 2, 4, 5};
//		int operationsArray[] 	= {1, 2, 3, 4, 5, 6};
		int operationsArray[] 	= {1, 2, 3, 4, 5, 6, 7, 8};
				
		for (int iteration=5; iteration<=30; iteration++){
//			
		mainX.createServiceCharacteristics(operationsArray, services);
		
		StringBuilder output = new StringBuilder();
		for (int i=0; i<mainX.serviceList.size(); i++){
			output.append(mainX.serviceList.get(i).toString());
		}
		
		String baseModelFileName	=  Utility.getProperty("MODEL_TEMPLATE_BASE_FILE");//e.g., "fx_43_Base.pm"
		int hashIndex				= -1;//baseModelFileName.lastIndexOf("/");
		int dotIndex 				= baseModelFileName.lastIndexOf("_");
		String outputModelFileName	= baseModelFileName.substring(hashIndex+1, dotIndex) + ".pm";
//		System.out.println(outputModelFileName);		
		Utility.createFileAndExport(baseModelFileName, outputModelFileName, output.toString());

		try {				
			String df = dateFormat.format(date);
			String strArray[] = {df};
//			EvoPrismStudy.main(strArray);
			
//			int iteration = 30;
			
			//rename evostudy directory results using the iteration counter
//				String outputDir 			= Utility.getProperty("OUTPUTDIR") ;
//				String experimentName 		= Utility.getProperty("EXPERIMENT") ;
//				String experimentFileName	= outputDir + experimentName;
//				File file = new File(experimentFileName);
//				experimentFileName += "_" + df;//"_R" + iteration;
//			    file.renameTo(new File(experimentFileName));
		    
		    //rename output generate model  file
		    File outputModelFile	= new File(outputModelFileName);
//		    outputModelFileName		= baseModelFileName.substring(hashIndex+1, dotIndex) + "_"+ df +".pm";//"_R" +iteration + ".pm"; 
		    outputModelFileName		= baseModelFileName.substring(hashIndex+1, dotIndex) + "_R" +iteration + ".pm"; 
		    outputModelFile.renameTo(new File(outputModelFileName));
//			    System.out.println(experimentFileName +"\t"+ outputModelFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}//for
		
	    System.err.println("Done");
		
	}//main
	
	
	private void createServiceCharacteristics(int[] operationsArray, int services){
		int operations = operationsArray.length;
		
		generateAllLists(operations, services);
		serviceList.clear();

		for (int i=0; i<operations; i++){
		
			for (int j=0; j<services; j++){
				int serviceIndex 	= rand.nextInt(failureProbList.size());
				Double failureProb 	= failureProbList.get(serviceIndex);
				Double cost			= costList.get(serviceIndex);
				Double time			= timeList.get(serviceIndex);
				serviceList.add(new Service(failureProb, cost, time, operationsArray[i], j+1));				
				
				//remove the used characteristics from the lists
				failureProbList.remove(serviceIndex);
				costList.remove(serviceIndex);
				timeList.remove(serviceIndex);
			}//for
			
		}//for
		
	}//createServiceCharacteristics
	
	
	private void generateAllLists(int operations, int services){
		//generate lists
		double failureProbMin   = 1 - 0.9999;
		double failureProbMax  	= 1 - 0.98;
		failureProbList = generateRandomList(operations, services, failureProbMax, failureProbMin);
		costList 		= generateRandomList(operations, services, 1, 15);
		timeList 		= generateRandomList(operations, services, 0.5, 5);

		//Sort the lists
		Collections.sort(failureProbList );Collections.reverse(failureProbList);
		Collections.sort(costList);
		Collections.sort(timeList); Collections.reverse(timeList);
		
		//print
//		System.out.println(failureProbList.toString());
//		System.out.println(costList.toString());
//		System.out.println(timeList.toString());
//		System.out.println();
	}//generateAllLists
	
	
	private List<Double>generateRandomList(int operations, int services, double max, double min){
		List<Double> aList 	= new ArrayList<Double>();
		
		DecimalFormat df=new DecimalFormat("#.#########");
		String formatted = null;

		
		double range = max - min;
		for (int i=0; i<operations; i++){

			for (int j=0; j< services; j++){
				double value 	= rand.nextDouble();
				double scaled 	= value * range;
				double shifted 	= scaled + min; 
				
				formatted		= df.format(shifted);
//				double failureProbFormatted = (Double)df.parse((df.format(failureProb)));				
				try {
					aList.add((Double)df.parse(formatted));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//for
						
		}//for

		return aList;
	}//generateRandomFXFeatures
	
	
	
	private class Service{
	
		protected String failureProb;
		protected Double cost;
		protected Double time;
		protected int    operation;
		protected int	 service;
		
		
		public Service(Double failProb, Double cost, Double time, int operation, int service){
			this.failureProb	= new BigDecimal(failProb+"", MathContext.DECIMAL64).toPlainString();
			this.cost			= cost;
			this.time			= time;
			this.operation		= operation;
			this.service		= service;
		}
		
		public String toString(){
			String str = "";

			str = failureProb +","+ cost +","+ time;
			
			str  = "const double op" + operation +"S"+ service +"Fail = " + failureProb +";\n";
			str += "const double op" + operation +"S"+ service +"Time = " + time 		+";\n";
			str += "const double op" + operation +"S"+ service +"Cost = " + cost		+";\n\n";
			return str;
		}
		
	}

}//class