package _mainX;

import prism.PrismCL;
import prism.PrismException;
import prism.ResultsCollection;

public class MainX {

	public static void main(String[] args) throws InterruptedException, PrismException {
		PrismCL prismCL = new PrismCL();
		
		String str = "models/google-source.sm models/google.csl -psecheck c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6 100";
		prismCL.run(str.split(" "));
		ResultsCollection results[] = prismCL.getResult();
		
		System.out.println("Results \t" + results.length);
		for (ResultsCollection result : results){
//			Vector<DefinedConstant> definedConstantsV = result.getNumModelRangingConstants();
//			BoxRegionValues boxresults = (BoxRegionValues)result.getResult(new Values());
//			System.out.println(result.getResult(new Values()).getClass() +"\t"+ boxresults.getNumRegions() +"\t"+ 
//					result.getNumModelRangingConstants() + result.getNumPropertyRangingConstants());

//			BoxRegionValues boxresults = (BoxRegionValues)result.getResult(new Values());
//			for (Map.Entry<BoxRegion, StateValuesPair> entry : boxresults.entrySet()){
//				BoxRegion box = entry.getKey();
//				System.out.println(box.toString() +"\t"+ entry.getValue().getMin().getValue(1) +"\t"+ entry.getValue().getMin().getValue(0));
//			}
		}
		
		
//		String modelFilename 		= "models/google-source.sm";
//		String propertiesFilename	= "models/google.csl";
//		PrismAPI prism = new PrismAPI(propertiesFilename, modelFilename);
//		prism.pri
	}

}
