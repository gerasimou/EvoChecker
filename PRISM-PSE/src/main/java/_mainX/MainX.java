package _mainX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import prism.PrismCL;
import prism.PrismException;
import prism.Result;
import pse.BoxRegion;
import pse.BoxRegionValues;
import pse.BoxRegionValues.StateValuesPair;
import pse.DecompositionProcedure;

public class MainX {

	public static void main(String[] args) throws InterruptedException, PrismException, FileNotFoundException {

		/** PrismCL */
//		String str = "models/google-source.sm models/google.csl -psecheck c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6 100";
//		PrismCL prismCL = new PrismCL();		
//		prismCL.run(str.split(" "));
//		ResultsCollection results[] = prismCL.getResult();
		
		/** Prism-PSE */
//		PrismPSE_API prismAPI = new PrismPSE_API();
//		prismAPI.loadModelProperties(Utility.readFile("models/google-source.sm"), "models/google.csl");
//		for (int i=0; i<1; i++){
//			List<Result> resultList = prismAPI.launchPrismPSE(DecompositionProcedure.Type.SIMPLE, "c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6", 100);
//			for (Result res : resultList){
//				BoxRegionValues boxresults = (BoxRegionValues)res.getResult();
//				System.out.println(boxresults.size() +"\t"+ boxresults.entrySet().size());
//				for (Map.Entry<BoxRegion, StateValuesPair> entry : boxresults.entrySet()){
//					BoxRegion box = entry.getKey();
//					System.out.println(box.toString() +"\t"+ entry.getValue().getMin().getValue(0) +"\t"+ entry.getValue().getMax().getValue(0));
//	
//				}
//			}
//		}
//		System.exit(0);
		
		/** Prism-PSE API */
		try {
			String serverAddress 			= "127.0.0.1";
			int serverPort       			= 8860;
			Socket socket;
			socket = new Socket(serverAddress, serverPort);
			BufferedReader inFromServer 	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter outToServer			= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
			//send to server
			StringBuilder outputString 		= new StringBuilder();
			outputString.append(Utility.readFile("models/google-source.sm") + "\n@");	//model String
			outputString.append("models/google.csl" +"\n@");							//properties filename
			outputString.append("-psecheck" +"\n@");									//decompositionType	
			outputString.append("c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6" +"\n@");	//params and ranges
			outputString.append("100 \nEND");												//accuracy
			outToServer.println(outputString.toString());
			outToServer.flush();
			//read from server
			String response = inFromServer.readLine();
			System.out.println("Result:\t" + response);
		} 
		catch (IOException e) {
			e.printStackTrace();
		};		
	}

}
