//==============================================================================
//	
//	Copyright (c) 2020-
//	Authors:
//	* Simos Gerasimou (University of York)
//  * Faisal Alhwikem (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================
package evochecker.evaluator;

public class ModelEvaluatorImpl {

	public static IParallelEvaluator instantiateModelChecker(String modelCheckerEngine) throws Exception {
		if (modelCheckerEngine != null) {
			switch (modelCheckerEngine) {
			case "prism":
				return new MultiProcessModelEvaluator();
			}
		}
		throw new IllegalArgumentException("Unspecified model checker engine");
	}
}
