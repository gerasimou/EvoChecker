## Running EvoChecker From Jar

EvoChecker is a Java-based tool that uses Maven for managing the project and its dependencies, and for generating the executable jars.

EvoChecker uses under-the-hood JMetal 4.5 for multiobjective optimisation, PRISM 4.5 for probabilistic model checking, and Antl4 for parsing the probabilistic model templates.

### Runnning Within an IDE

This branch shows how you can use EvoChecker as a Jar by specifying a configuration file (e.g., [config.properties](https://github.com/gerasimou/EvoChecker/blob/evoCheckerJar/config.properties)) and **less than five lines of code**. 

    //1) Create EvoChecker instance
    EvoChecker ec = new EvoChecker();
		
    //2) Set configuration file
    String configFile ="config.properties"; 
    ec.setConfigurationFile(configFile);
		
    //3) Start EvoChecker
    ec.start();		
    
The [EvoCheckerRunner class](https://github.com/gerasimou/EvoChecker/blob/evoCheckerJar/src/examples/EvoCheckerRunner.java) in the examples directory provides the complete source-code.


### Runnning From Terminal

1) Define your properties in the config.properties file

2) Use the terminal to execute the launch script

     ./launch.sh



###### Remember to set the Java library path (DYLD\_LIBRARY\_PATH on OSX; LD\_LIBRARY\_PATH on *NIX)
* On Eclipse: Run > Run Configurations > Environment > New > Variable: DYLD_LIBRARY_PATH; Value:libs/runtime.



---
--

Should you have any comments, suggestions or questions, please email simos.gerasimou-at-.york.ac.uk
