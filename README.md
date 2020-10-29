# EvoChecker

EvoChecker is a search-based software engineering approach and tool that employs multiobjective optimisation genetic algorithms to automate the synthesis of approximate Pareto-optimal probabilistic models associated with the QoS requirements of a software system.

EvoChecker is the outcome of the research paper "Search-Based Synthesis of Probabilistic Models for Quality-of-Service Softwate Engineering"

You can find details about EvoChecker on our [project webpage] (http://www-users.cs.york.ac.uk/~simos/EvoChecker)


Instructions
------------

EvoChecker is a Java-based tool that uses Maven for managing the project and its dependencies, and for generating the executable jars.

EvoChecker uses under-the-hood JMetal 4.5 for multiobjective optimisation, PRISM 4.5 for probabilistic model checking, and Antl4 for parsing the probabilistic model templates.

1. Import the project in your IDE of preference

2. Set the following environment variable (In Eclipse go to Run / Run Configurations / Environment tab / New)
   > OSX: DYLD\_LIBRARY\_PATH = libs/runtime
   > *NIX: LD\_LIBRARY\_PATH = libs/runtime
   
3. Specify the configuration parameters in file [config.properties](https://github.com/gerasimou/EvoChecker/blob/newEvoChecker/config.properties)

5. Run
