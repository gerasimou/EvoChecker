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
   
3. Specify the configuration parameters in file [config.properties](https://github.com/gerasimou/EvoChecker/blob/master/config.properties)

5. Run

Headless mode
------------

To run in headless mode:

1. Add the relevant line to your .bashrc:

```
export LD_LIBRARY_PATH=libs/runtime # OSX
export DYLD\_LIBRARY\_PATH=libs/runtime # *NIX
```


2. In /EvoChecker: `mvn clean install`

3. Run EvoChecker. You may either do this in two ways:

```
java -jar target/EvoChecker-1.1.1.jar
```

in which case EvoChecker will access [config.properties](https://github.com/gerasimou/EvoChecker/blob/master/config.properties) and execute using the model file and properties file there.

Alternatively, you may run:

```
java -jar target/EvoChecker-1.1.1.jar -cf [path to config file] -pf [path to model file] -p [path to properties file, or formatted property string]
```

in which case the -pf and -p arguments will override the files specified in the config file (which may in fact be left unspecified for this usage).

The -p option may be used to specify a properties file (e.g. [fxNano.pctl](https://github.com/gerasimou/EvoChecker/blob/master/models/FXParam/fxNano.pctl)) or a string containing the properties directly. For the latter method, the string should contain a series of objectives and constraints in the format:

```
//[decorator]:[formula]
```

This results in be exactly the same format as an equivalent properties file, except with no new line characters and a colon ":" between each property decorator and the property formula. For example, if the property file contained:

>//objective, max
>P=? [ F state=15 ]
>
>
>//objective, min
>R{"cost"}=? [ F state=5 |state=15]

then the equivalent property string would be

```
'//objective, max:P=? [ F state=15 ]//objective, min:R{"cost"}=? [ F state=5 |state=15]'
```

Be sure to include single quotes around the string so as not to interfere with the double quotes typically used for referring to state labels.

