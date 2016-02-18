package _main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import prism.PrismCL;
import prism.PrismException;
import prism.PrismLangException;
import prism.Result;
import prism.ResultsCollection;
import pse.BoxRegion;
import pse.BoxRegionValues;
import pse.BoxRegionValues.StateValuesPair;
import pse.DecompositionProcedure;

public class MainX {
	
	private static Gson gson = new GsonBuilder()
            					.disableHtmlEscaping()
            					.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            					.setPrettyPrinting()
            					.serializeNulls()
            					.create();

	public static void main(String[] args){
		serverAPI();
	}
	
	
	/** Run PrismCL */
	private static void PrismCL(){ 
		String str = "models/google-source.sm models/google.csl -psecheck c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6 100";
		PrismCL prismCL = new PrismCL();		
		prismCL.run(str.split(" "));
		ResultsCollection results[] = prismCL.getResult();
	}
		
	/** Run Prism-PSE API 
	 * @throws PrismException 
	 * @throws FileNotFoundException */
	private static void API() throws FileNotFoundException, PrismException{
		PrismPSE_API prismAPI = new PrismPSE_API();
		prismAPI.loadModelProperties(Utility.readFile("models/google-source.sm"), "models/google.csl");
		for (int i=0; i<1; i++){
			List<Result> resultList = prismAPI.launchPrismPSE(DecompositionProcedure.Type.SIMPLE, 
															  "c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6", 100);
			printResultsList(resultList);
			PrismExecutor.prepareJSON(resultList);
		}
	}
		
		
	/** Run Prism-PSE API  as a server and send a string as reponse*/
	private static void serverAPI(){
		try {
			String params[] = new String[4];
			params[0] = "/System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java";//Utility.getProperty("JVM");
			params[1] = "-jar";
			params[2] = "/Users/sgerasimou/Documents/Git/EvoChecker/PRISM-PSE/target/PRISM-PSY-fat.jar";
			params[3] = "8860";
			System.out.println("Starting PRISM-PSY server @ " + params[3]);
			Process p = Runtime.getRuntime().exec(params);
			System.out.println(p.isAlive());
			Thread.sleep(3000);
//			System.exit(0);
		
		
			String serverAddress 			= "127.0.0.1";
			int serverPort       			= 8860;
			Socket socket;
			socket = new Socket(serverAddress, serverPort);
			BufferedReader inFromServer 	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter outToServer			= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
			//send to server
			for (int i=0; i<10; i++){
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
//			System.out.println("Result:\t" + response);
			printJSON(gson.fromJson(response, JsonObject.class));
			
			Thread.sleep(2000);
			}
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
		
	/** Run Prism-PSE API as a server and serialise response
	 * @throws PrismLangException */
	private static void serverAPIserialisation() throws PrismLangException{
		try {
			String serverAddress 			= "127.0.0.1";
			int serverPort       			= 8860;
			Socket socket;
			socket = new Socket(serverAddress, serverPort);
			ObjectInputStream inFromServer 	= new ObjectInputStream(socket.getInputStream());
			PrintWriter outToServer			= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
			//send to server
			StringBuilder outputString 		= new StringBuilder();
			outputString.append(Utility.readFile("models/google-source.sm") + "\n@");	//model String
			outputString.append("models/google.csl" +"\n@");							//properties filename
			outputString.append("-psecheck" +"\n@");									//decompositionType	
			outputString.append("c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6" +"\n@");	//params and ranges
			outputString.append("1000 \nEND");												//accuracy
			outToServer.println(outputString.toString());
			outToServer.flush();
			//read from server
			List<Result> resultList = (List<Result>) inFromServer.readObject();
			printResultsList(resultList);
		} 
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Print response given as a JSON element
	 * @param JSONelement
	 */
	private static void printJSON(JsonObject JSONobject){
		//for each property
		for (Entry<String, JsonElement> entry :  JSONobject.entrySet()){
			System.out.println(entry.getKey() +"\t"+ entry.getValue());
			JsonArray propertiesJSON = (JsonArray)entry.getValue();
			//for each subregion of a property
			Iterator propertiesSubregion = propertiesJSON.iterator();
			while (propertiesSubregion.hasNext()){
				JsonObject JSONsubregion = (JsonObject) propertiesSubregion.next();
				System.out.println(JSONsubregion.get("min") +"\t");
				System.out.println(JSONsubregion.get("max") +"\t");				
			}
		}
	}
	
	
	/**
	 * Print the results
	 * @param resultList
	 * @throws PrismLangException
	 */
	private static void printResultsList(List<Result> resultList) throws PrismLangException{
		for (Result res : resultList){
			BoxRegionValues boxresults = (BoxRegionValues)res.getResult();
			System.out.println(boxresults.size() +"\t"+ boxresults.entrySet().size());
			for (Map.Entry<BoxRegion, StateValuesPair> entry : boxresults.entrySet()){
				BoxRegion box = entry.getKey();
				int numValues = box.getLowerBounds().getNumValues();
				for (int index=0; index<numValues; index++){
					System.out.print( box.getLowerBounds().getName(index)  +"\t"+
									  box.getLowerBounds().getValue(index) +"\t"+ 
									  box.getUpperBounds().getValue(index) +"\t");
				}
				System.out.println( entry.getValue().getMin().getValue(0)   +"\t"+ 
									entry.getValue().getMax().getValue(0));	
			}
		}
	}

}
