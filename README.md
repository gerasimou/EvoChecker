## Running EvoChecker From Jar

EvoChecker is a Java-based tool that uses Maven for managing the project and its dependencies, and for generating the executable jars.

EvoChecker uses under-the-hood JMetal 4.5 for multiobjective optimisation, PRISM 4.5 for probabilistic model checking, and Antl4 for parsing the probabilistic model templates.

This branch shows how you can use EvoChecker as a Jar by specifying a configuration file (e.g., [config.properties](https://github.com/gerasimou/EvoChecker/blob/evoCheckerJar/resources/config.properties)) and **less than five lines of code**. 

    //1) Create EvoChecker instance
    EvoChecker ec = new EvoChecker();
		
    //2) Set configuration file
    String configFile ="resources/config.properties"; 
    ec.setConfigurationFile(configFile);
		
    //3) Start EvoChecker
    ec.start();		
    
The [EvoCheckerRunner class](https://github.com/gerasimou/EvoChecker/blob/evoCheckerJar/src/examples/EvoCheckerRunner.java) in the examples directory provides the complete source-code.
