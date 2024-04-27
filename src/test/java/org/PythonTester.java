package org;



//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.empty;
//import static org.hamcrest.Matchers.hasItem;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
//import org.python.core.PyException;
//import org.python.core.PyObject;
//import org.python.util.PythonInterpreter;


public class PythonTester {

	    @Rule
	    public ExpectedException thrown = ExpectedException.none();

	    @Test
	    public void givenPythonScript_whenPythonProcessInvoked_thenSuccess() throws Exception {
	        ProcessBuilder processBuilder = new ProcessBuilder("python3", resolvePythonScriptPath("Hello.py"));
	        processBuilder.redirectErrorStream(true);

	        Process process = processBuilder.start();
	        List<String> results = readProcessOutput(process.getInputStream());

//	        assertThat("Results should not be empty", results, is(not(empty())));
//	        assertThat("Results should contain output of script: ", results, hasItem(containsString("Hello Baeldung Readers!!")));

	        int exitCode = process.waitFor();
	        System.out.println(Arrays.toString(results.toArray()));
	        assertEquals("No errors should be detected", 0, exitCode);
	    }
	    
	    
	    @Test
	    public void userPlotFactory() throws Exception {
			String frontFile 	= "/Users/simos/Git/EvoChecker/data/MARC/NSGAII/MARC_NSGAII_170923_1329488796374500851926967_Front";
			String script2DFile = "scripts/plotFront2D.py"; //"plotFront2D.py";

	        ProcessBuilder processBuilder = new ProcessBuilder("/usr/bin/python3", script2DFile, frontFile);
	        processBuilder.redirectErrorStream(true);

	        Process process = processBuilder.start();
	        List<String> results = readProcessOutput(process.getInputStream());

	        int exitCode = process.waitFor();
	        System.out.println(Arrays.toString(results.toArray()));
	        assertEquals("No errors should be detected", 0, exitCode);
	    }
	    
	    

	    private List<String> readProcessOutput(InputStream inputStream) throws IOException {
	        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
	            return output.lines()
	                .collect(Collectors.toList());
	        }
	    }

	    private String resolvePythonScriptPath(String filename) {
	        File file = new File("src/test/resources/" + filename);
	        return file.getAbsolutePath();
	    }

	}
