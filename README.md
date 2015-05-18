# EvoChecker

EvoChecker is a search-based software engineering approach and tool that employs multiobjective optimisation genetic algorithms to automate the synthesis of approximate Pareto-optimal probabilistic models associated with the QoS requirements of a software system.

EvoChecker is the outcome of the research paper "Search-Based Synthesis of Probabilistic Models for Quality-of-Service Softwate Engineering"

You can find details about EvoChecker on our [project webpage] (http://www-users.cs.york.ac.uk/~simos/EvoChecker)


Instructions
------------

EvoChecker is a Java-based tool that uses Maven for managing the project and its dependencies, and for generating the executable jars.

EvoChecker uses under-the-hood JMetal 4.5 for multiobjective optimisation, PRISM 4.2.1 for probabilistic model checking, and Antl4 for parsing the probabilistic model templates.

1) Install JMetal 4.5, Prism 4.2.1, Antlr 4 in your local maven repository

2) Import the project in your IDE of preference

3) Set the following environment variable (In Eclipse go to Run / Run Configurations / Environment tab / New)
   > DYLD_LIBRARY_PATH = full/path/to/PRISM/lib (e.g., /Users/aUser/Documents/Prism/prism-4.2.1/lib)
   * for Linux the env. variable name is LD_LIBRARY_PATH

4) Follow the instructions in res / config.properties

5) Run
