package evochecker.evolvable;

public enum VariableType {
	INT, DOUBLE;
	
	 public static String getVariableTypeLiteral(VariableType varType){
		switch (varType){
			case INT:{
				return "int";
			}
			case DOUBLE:{
				return "double";
			}
			default:{
				throw new RuntimeException("Variable Type not recognised");
			}
		}
	}
}
