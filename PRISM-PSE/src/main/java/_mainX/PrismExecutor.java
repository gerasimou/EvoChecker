package _mainX;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import prism.PrismException;
import prism.PrismLangException;
import prism.Result;
import pse.BoxRegion;
import pse.BoxRegionValues;
import pse.BoxRegionValues.StateValuesPair;
import pse.DecompositionProcedure;

public class PrismExecutor {
	
	/** Print writer out*/
	private ObjectOutputStream oos;
	private PrintWriter outWriter;
	
	/** Reader */
	private BufferedReader in;

	/** Server socket*/
	private ServerSocket serverSocket;
	
	/** GSON object*/
	Gson gson;
	
	/** API handler*/
	PrismPSE_API prismAPI = new PrismPSE_API();

	
	/**
	 * Main class
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		PrismExecutor prismExecutor = null;
		try{
			prismExecutor = new PrismExecutor();
//			prismExecutor.initialiseSocketsData(Integer.parseInt(args[0]));
			prismExecutor.initialiseSocketsString(Integer.parseInt(args[0]));
			prismExecutor.startListening();
		}
		catch (IOException | NumberFormatException | PrismException | NullPointerException e){
			System.err.println("Prism Executor exception");
//			e.printStackTrace();
			prismExecutor.outWriter.close();
		}
	}

	
	
	public String analyze(String modelString, String propertiesFilename,
						  DecompositionProcedure.Type pseCheckType, String pseSwitch, double pseAccuracy) throws FileNotFoundException, PrismException {
		//load model
//		prismAPI.loadModelProperties(modelString, propertiesFilename);
//		List<Result> resultList = prismAPI.launchPrismPSE(pseCheckType, pseSwitch, pseAccuracy);
//		List<Result> res = prismAPI.l
//		StringBuilder finalRes = new StringBuilder();
//		for (Double value : res) {
//			finalRes.append(String.valueOf(value));
//			finalRes.append("@");
//		}
////		api.closeDown();
////		 api = null;
//		 return finalRes.toString();
		return null;
	}

	
	/**
	 * Initialise string sockets
	 * @param port
	 * @throws IOException
	 * @throws PrismException 
	 */
	public void initialiseSocketsString(int port) throws IOException, PrismException {
		//create new server socket
		serverSocket = new ServerSocket(port);
		System.out.println("Accepting from port: "+port);
		//accept connections
		Socket socket 	= serverSocket.accept();
		//create new input reader
		in 				= new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//create new output writer
		outWriter 		= new PrintWriter(socket.getOutputStream());
		//create GSON object
		gson			= new GsonBuilder()
				             .disableHtmlEscaping()
				             .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//				             .setPrettyPrinting()
				             .serializeNulls()
							 .create();//new Gson();
	}
	
	
	/**
	 * Initialise data sockets
	 * @param port
	 * @throws IOException
	 * @throws PrismException 
	 */
	public void initialiseSocketsData(int port) throws IOException, PrismException {
		//create new server socket
		serverSocket = new ServerSocket(port);
		System.out.println("Accepting from port: "+port);
		//accept connections
		Socket socket = serverSocket.accept();
		//create new input reader
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//create new output writer
		oos = new ObjectOutputStream(socket.getOutputStream());
	}
	
	
	/**
	 * Start listening
	 * @throws IOException 
	 * @throws PrismException 
	 */
	public void startListening() throws IOException, PrismException{
		//repeat forever
		while (true) {
			//parse input string
			String[] input 								= parseInput(in);
			if (input.length != 5){
				throw new IOException("Input is missing required parameters");
			}
			String modelString 							= input[0];
			String propertiesFilename 					= input[1];
			DecompositionProcedure.Type pseCheckType 	= DecompositionProcedure.Type.SIMPLE;
			String pseSwitch							= input[3];
			double pseAccuracy							= Double.parseDouble(input[4]); 
			
			//load model
			prismAPI.loadModelProperties(modelString, propertiesFilename);
			//analyse
			List<Result> resultList = prismAPI.launchPrismPSE(pseCheckType, pseSwitch, pseAccuracy);
			//export results
//			printResultsList(resultList);
			JsonElement element = prepareJSON(resultList);
			sendJSONResults(element);
//			this.writeResult(out, results);
		}
	}
	
	
	/**
	 * Send results back to the client through serialisation
	 * @throws IOException
	 */
	private void sendResults(List<Result> resultsList) throws IOException{
		oos.writeObject(resultsList);
		oos.flush();
	}
	

	/**
	 * Send results back to the client as a JSON element
	 * @param JSONElement
	 */
	private void sendJSONResults(JsonElement JSONElement){
		outWriter.println(gson.toJson(JSONElement));
        outWriter.flush();
	}
	
	/** Read the model
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String[] parseInput(BufferedReader in) throws IOException {
		String line;
		StringBuilder modelBuilder = new StringBuilder();
		do {
			line = in.readLine();
			if(line.endsWith("END"))
				break;
			modelBuilder.append(line);
			modelBuilder.append("\n");
		} while (true);

//		System.out.println("Received from client..." + modelBuilder.toString());
		String res[] = modelBuilder.toString().split("@");
		res[1] =res[1].trim(); 
		return res;
	}
	
	
	/**
	 * Print the results
	 * @param resultList
	 * @throws PrismLangException
	 */
	private void printResultsList(List<Result> resultList) throws PrismLangException{
		for (Result res : resultList){
			BoxRegionValues boxresults = (BoxRegionValues)res.getResult();
			System.out.println(boxresults.size() +"\t"+ boxresults.entrySet().size());
			for (Map.Entry<BoxRegion, StateValuesPair> entry : boxresults.entrySet()){
				BoxRegion box = entry.getKey();
				int numValues = box.getLowerBounds().getNumValues();
				for (int index=0; index<numValues; index++){
					System.out.print(box.getLowerBounds().getValue(index) +"\t"+ box.getUpperBounds().getValue(index) +"\t");
				}
				System.out.println(entry.getValue().getMin().getValue(0) +"\t"+ entry.getValue().getMax().getValue(0));	
			}
		}
	}
	

	/**
	 * Prepare JSON
	 * @param resultList
	 * @return
	 */
	public static JsonElement prepareJSON(List<Result> resultList){
		JsonObject propertiesJSON 	= new JsonObject();
		int propertyIndex     		= 0;
		for (Result res : resultList){
			//get the result
			BoxRegionValues boxresults = (BoxRegionValues)res.getResult();
			//JSON property
			JsonArray JSONproperty = new JsonArray();
			//iterate over properties
			for (Map.Entry<BoxRegion, StateValuesPair> entry : boxresults.entrySet()){
				//create new JSON array
				JsonObject JSONsubregion = new JsonObject();
				//get the key
				BoxRegion box = entry.getKey();
				//get number of parameters
				int numParams = box.getLowerBounds().getNumValues();
				//iterate over params & assemble a list of : param_name: [min, max]
				for (int index=0; index<numParams; index++){
					//create new JSON array
					JsonArray paramArray = new JsonArray();
					paramArray.add(new JsonPrimitive((double)box.getLowerBounds().getValue(index)));
					paramArray.add(new JsonPrimitive((double)box.getUpperBounds().getValue(index)));
					JSONsubregion.add(box.getLowerBounds().getName(index), paramArray);
				}
				//append min max for subregion
				JSONsubregion.add("min", new JsonPrimitive((double)entry.getValue().getMin().getValue(0)));
				JSONsubregion.add("max", new JsonPrimitive((double)entry.getValue().getMax().getValue(0)));
				//add subregion to JSON property
				JSONproperty.add(JSONsubregion);
			}
			//add property to properties array
			propertiesJSON.add("property"+propertyIndex++, JSONproperty);
		}
		System.out.println(propertiesJSON);
		return propertiesJSON;
	}
	
	
	/**
	 * Send result to client
	 * @param out
	 * @param message
	 * @deprecated
	 */
	private void writeResult(PrintWriter out, String message){
		System.out.println("Sending out: "+message);
		message = message.substring(0, message.length()-1)+"\nEND\n";
		out.print(message);
        out.flush();
//        out.close();
	}
}
