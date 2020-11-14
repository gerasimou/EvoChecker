package evochecker.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class EvaluatorTester {
	
	private static String modelFile 		= "models/cope/copeModelTest.pm";
	private static String propertiesFile = "models/cope/copeProperties.pctl";
	private static int 	 port		    = 8860;

	public static void main(String[] args) throws IOException, InterruptedException {

		//Run MultiProcessEvaluator
//		MultiProcessEvaluator evaluator = new MultiProcessEvaluator(1);
//		PrintWriter out = evaluator.out[0];
//		BufferedReader in = evaluator.in[0];
		
		
//		Run PrismExecutor from source code
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				PrismExecutor.main(new String[] {port+""});				
//			}
//		});
//		t.start();
		
		String params = 	"/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/bin/java" 
//		String params = "/System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java "
						+ 	" -jar "
						+	"lib/PrismTacas17.jar "
						+	port + " 1";
						
		Process p = null;
		do {
			System.out.println("Connecting to " + params);
			p = Runtime.getRuntime().exec(params);
			Thread.sleep(5000);
		} 
		while (!p.isAlive());		
		
		Socket socket;
		socket = new Socket("127.0.0.1", port);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream());

		
		String model = new String(Files.readAllBytes(Paths.get(modelFile)));
		
		for (int i=0; i<1; i++) {
			System.out.print("Sending...\t" + i +"\t");
	
			out.print(model + "@" + propertiesFile + "\nEND\n");
			out.flush();
			
			String line;
			StringBuilder modelBuilder = new StringBuilder();
			do {
				line = in.readLine();
				if (line.endsWith("END"))
					break;
				modelBuilder.append(line);
				modelBuilder.append("\n");
			} while (true);
	
			//print results
			String results[] = modelBuilder.toString().trim().split("@");
			System.out.println(Arrays.toString(results));
			
		}
			
			//end connection
			out.print("DONE\nEND\n");
			out.flush();
//			socket.close();
//			evaluator.stopEvaluator();
	}

}
