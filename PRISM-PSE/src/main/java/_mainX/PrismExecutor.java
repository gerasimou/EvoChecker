package _mainX;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import prism.PrismException;
import prism.Result;
import pse.BoxRegion;
import pse.BoxRegionValues;
import pse.DecompositionProcedure;
import pse.BoxRegionValues.StateValuesPair;

public class PrismExecutor {
	
	/** Print writer out*/
	private PrintWriter out;

	/** Server socket*/
	private ServerSocket serverSocket;
	
	/** API handler*/
	PrismPSE_API prismAPI = new PrismPSE_API();

	
	/**
	 * Main class
	 * @param args
	 */
	public static void main(String[] args) {
		PrismExecutor prismExecutor = null;
		try{
			prismExecutor = new PrismExecutor();
			prismExecutor.startListening(Integer.parseInt(args[0]));
		}
		catch (IOException | NumberFormatException | PrismException e){
			System.out.println("Prism Executor exception");
			e.printStackTrace();
		}
		finally{
			prismExecutor.out.close();
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
	 * Start listening
	 * @param port
	 * @throws IOException
	 * @throws PrismException 
	 */
	public void startListening(int port) throws IOException, PrismException {
		//create new server socket
		serverSocket = new ServerSocket(port);
		System.out.println("Accepting from port: "+port);
		//accept connections
		Socket socket = serverSocket.accept();
		//create new input reader
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//create new output writer
		out = new PrintWriter(socket.getOutputStream());

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
			String results = printResult(resultList);
			this.writeResult(out, results);
		}
	}
	
	
	private String printResult(List<Result> resultList){
		StringBuilder resultsString = new StringBuilder();
		for (Result res : resultList){
			BoxRegionValues boxresults = (BoxRegionValues)res.getResult();
			System.out.println(boxresults.size() +"\t"+ boxresults.entrySet().size());
			for (Map.Entry<BoxRegion, StateValuesPair> entry : boxresults.entrySet()){
				BoxRegion box = entry.getKey();
				resultsString.append(box.toString()+",");
				resultsString.append(entry.getValue().getMin().getValue(0).toString()+",");
				resultsString.append(entry.getValue().getMax().getValue(0)+"@");
				System.out.println(box.toString() +"\t"+ entry.getValue().getMin().getValue(0).toString() +"\t"+ entry.getValue().getMax().getValue(0));
			}
		}
		return resultList.toString();
	}
	
	
	/**
	 * Send result to client
	 * @param out
	 * @param message
	 */
	private void writeResult(PrintWriter out, String message){
		System.out.println("Sending out: "+message);
		message = message.substring(0, message.length()-1)+"\nEND\n";
		out.print(message);
        out.flush();
//        out.close();
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
}
