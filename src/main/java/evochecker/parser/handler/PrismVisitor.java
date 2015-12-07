package evochecker.parser.handler;

import java.util.ArrayList;
import java.util.List;

import evochecker.parser.evolvable.Evolvable;
import evochecker.parser.evolvable.EvolvableDistribution;
import evochecker.parser.evolvable.EvolvableDouble;
import evochecker.parser.evolvable.EvolvableID;
import evochecker.parser.evolvable.EvolvableInteger;
import evochecker.parser.evolvable.EvolvableModule;
import evochecker.parser.evolvable.EvolvableModuleAlternative;
import evochecker.parser.src.gen.PrismBaseVisitor;
import evochecker.parser.src.gen.PrismParser;
import evochecker.parser.src.gen.PrismParser.CommandContext;
import evochecker.parser.src.gen.PrismParser.ConstantContext;
import evochecker.parser.src.gen.PrismParser.EvolvableContext;
import evochecker.parser.src.gen.PrismParser.ModuleContext;
import evochecker.parser.src.gen.PrismParser.RewardContext;
import evochecker.parser.src.gen.PrismParser.RewardItemContext;
import evochecker.parser.src.gen.PrismParser.VarDeclarationContext;

/* TODO
 * This class needs to parse the model and 
 * 1) identify the evolvable elements and generate a list from these elements
 * 2) assemble a template of the model
 */

public class PrismVisitor extends PrismBaseVisitor<String> {

	/** "memory" for our calculator; variable/value pairs go here */ 
//	Map<String, Integer> memory = new HashMap<String, Integer>();
	
	private List<Evolvable> 			evolvableList 	 			= new ArrayList<Evolvable>();
	private List<EvolvableDouble>		evolvableDoubleList	 		= new ArrayList<EvolvableDouble>();
	private List<EvolvableInteger>		evolvableIntegerList 		= new ArrayList<EvolvableInteger>();
	private List<EvolvableDistribution>	evolvableDistributionList	= new ArrayList<EvolvableDistribution>();
	private List<EvolvableModule>		evolvableModuleList			= new ArrayList<EvolvableModule>();
	private List<EvolvableModuleAlternative> evolvableModuleSuperList		= new ArrayList<EvolvableModuleAlternative>();
	
	private StringBuilder modelString 		= new StringBuilder(100);

		
	@Override
	public String visitModel(PrismParser.ModelContext ctx) {
		String str;

		//visit model type
		str = visit(ctx.modelType());
		modelString.append(str +"\n");
		
		// visit const declarations
		for (ConstantContext constant :  ctx.constant()){
			str = visit(constant) + "\n";
//			System.out.println(str);
			modelString.append(str);
		}
		//visit modules
		for (ModuleContext module :  ctx.module()){
			str = visit(module);
//			System.out.println(str);
			modelString.append(str);
		}
		//visit rewards
		for (RewardContext reward : ctx.reward()){
			str = visit(reward);
//			System.out.println(str);
			modelString.append(str);
		}
		//visit evolvable
		for (EvolvableContext evolvable : ctx.evolvable()){
			str = visit(evolvable);
//			System.out.println(str);
			modelString.append(str);
		}
		
		//print it
//		System.err.println(modelString);

		
		//make some additional work
		prepareEvolvableDistributions();
		prepareEvolvableModulesReplica();
		prepareEvolvableModulesAlternative();
		concatenateEvolvables();
		
		return null;
//		return modelString.toString();
	}

	
	@Override
	public String visitModelType (PrismParser.ModelTypeContext ctx){
		return ctx.value.getText() + "\n";
	}
	
	
	@Override
	public String visitModule(PrismParser.ModuleContext ctx){
		StringBuilder str = new StringBuilder("\nmodule ");
		str.append(ctx.name.getText() + "\n");
		//parse variable declarations
		for (VarDeclarationContext varDeclaration : ctx.varDeclaration()){
			str.append(visit(varDeclaration));
		}
		str.append ("\n");
		//parse commands
		for (CommandContext command : ctx.command()){
			str.append(visit(command));
		}
		str.append("endmodule \n\n");
		return str.toString();
	}
	
	
	@Override
	public String visitIntVarDeclaration (PrismParser.IntVarDeclarationContext ctx){
		String str = "\t" + ctx.name.getText() +":"+ "["+ ctx.lowerBound.getText() +".."+ ctx.upperBound.getText() +"]";
		if (ctx.initValue != null)
			str += " init " + ctx.initValue.getText();
		return str + ";\n";
	}
		
	
	@Override
	public String visitBoolVarDeclaration (PrismParser.BoolVarDeclarationContext ctx){
		String str = "\t" + ctx.name.getText() +" : bool ";
		if (ctx.initValue != null)
			str += "init " + ctx.initValue.getText();
		return str + ";\n";
	}
	
	
	@Override
	public String visitCommand (PrismParser.CommandContext ctx){
		StringBuilder str = new StringBuilder("\t[");
		if (ctx.name != null)
			str.append(ctx.name.getText()); 		//synchronisation label
		str.append("]\t");
		str.append(visit(ctx.guard()));
		str.append(" -> ");
		str.append(visit(ctx.transition()));
		return str.toString() + ";\n";
	}
	
	
	@Override
	/** variable comparisonOp value=(INT | DOUBLE | ID) */
	public String visitGuardExpression (PrismParser.GuardExpressionContext ctx) {
		String str = ctx.variable().getText() +" "+ ctx.operator().getText() +" "+ visit(ctx.expression());
		return str;
	}
	
	
	@Override
	/** BOOLEAN */
	public String visitGuardBool (PrismParser.GuardBoolContext ctx){
		return ctx.BOOLEAN().getText();
	}

	
	@Override 
	/** guard logicalOp guard */
	public String visitGuardMulti(PrismParser.GuardMultiContext ctx) {
		String str = visit(ctx.guard(0)) +" "+ ctx.logicalOp().getText() +" "+ visit(ctx.guard(1));
		return str;
	}
	
	
	@Override
	/** '(' guard ')' */
	public String visitGuardParen (PrismParser.GuardParenContext ctx){
		return "(" + visit(ctx.guard()) + ")";
	}
	
	
	@Override
	/** '!' guard	*/
	public String visitGuardNot (PrismParser.GuardNotContext ctx){
		return "!" + visit(ctx.guard());
	}

	
	@Override
	public String visitTransitionEntry(PrismParser.TransitionEntryContext ctx) {
		String str = ctx.expression()!=null?visit(ctx.expression()) + ":" :"";
//		String str = ctx.probability!=null ? ctx.probability.getText() +":" : 
//					 ( ctx.probabilityVar != null ? ctx.probabilityVar.getText() +":" : "");
		str += visit (ctx.statement());
//		System.out.println(ctx.probability.getText() + "\t");
		return str;
	}
	
	
	@Override 
	public String visitTransitionMulti(PrismParser.TransitionMultiContext ctx) {
		String str = visit(ctx.transition(0)) +" + "+ visit(ctx.transition(1));
		return str;
	}
	
	
	@Override
	/** '('variable '\'' ASSIGN expression ')'*/
	public String visitStatementMain(PrismParser.StatementMainContext ctx) {
		String str = "(" + ctx.variable().getText() +"' = "+ visit(ctx.expression()) +")";
		return str;
	}

	
//	@Override
//	public String visitStatementEv(PrismParser.StatementEvContext ctx) {
//		String str = "(" + ctx.variable().getText() +"' = "+ ctx.INT().getText() + ")"; 
////		String str = ctx.probability!=null ? ctx.probability.getText() : ctx.probabilityVar.getText();
////		str += " : " + visit (ctx.statement());
//		return str;
//	}
//	
//	@Override 
//	public String visitStatementOp (PrismParser.StatementOpContext ctx){
//		String str = "(" + ctx.changingVar.getText() +"' = "+ ctx.update.getText() 
//					 + ctx.operator().getText() + ctx.value.getText() + ")";
//		return str;
//	}

	
	@Override 
	public String visitStatementMulti(PrismParser.StatementMultiContext ctx) {
		String str = visit(ctx.statement(0)) +" & "+ visit(ctx.statement(1));
		return str;
	}
	
	
	@Override 
	public String visitStatementBool (PrismParser.StatementBoolContext ctx){
		String str = ctx.BOOLEAN().getText();
		return str;
	}
		
	
	@Override
	public String visitConstant (PrismParser.ConstantContext ctx){
		StringBuilder str = new StringBuilder("const ");
		str.append(ctx.CONSTANTTYPE()!=null ? ctx.CONSTANTTYPE().getText() : "");
		str.append(" " + ctx.variable().getText());
		if (ctx.expression()!=null){
			str.append(" = ");
			str.append(visit(ctx.expression()));
		}
		 str.append(";");
		return str.toString();
	}
	
	
	@Override
	/** expression operator expression */
	public String visitExpressionMulti (PrismParser.ExpressionMultiContext ctx){
		StringBuilder str = new StringBuilder();
		str.append(visit(ctx.expression(0)));
		str.append(ctx.operator().getText());
		str.append(visit(ctx.expression(1)));
		return str.toString();
	}
	
	
	@Override
	/** value=(INT | DOUBLE) */
	public String visitExpressionValue (PrismParser.ExpressionValueContext ctx){
		return ctx.value.getText();
	}
	
	
	@Override
	/** variable */
	public String visitExpressionVariable (PrismParser.ExpressionVariableContext ctx){
		return ctx.variable().getText();
	}
	
	
	@Override
	/** '(' expression ')' */
	public String visitExpressionParen (PrismParser.ExpressionParenContext ctx){
		StringBuilder str = new StringBuilder("( ");
		str.append(visit(ctx.expression()));
		str.append(" )");
		return str.toString();
	}
	
	
	@Override
	public String visitExpressionFunction (PrismParser.ExpressionFunctionContext ctx){
		return visit(ctx.function());
	}
	
	
	@Override
	public String visitFunction (PrismParser.FunctionContext ctx){
		return ctx.FUNCTIONIDENTIFIER().getText() +" ("+ visit(ctx.functionParam()) +")";
	}
	
	
	@Override
	public String visitFunctionParamExpr (PrismParser.FunctionParamExprContext ctx){
		return visit(ctx.expression());
	}

	
	@Override
	/** expression ',' functionParam	*/
	public String visitFunctionParamMulti (PrismParser.FunctionParamMultiContext ctx){
		return visit(ctx.expression()) +","+ visit(ctx.functionParam());
	}

	
	@Override
	public String  visitReward (PrismParser.RewardContext ctx){
		StringBuilder str = new StringBuilder("rewards ");
		str.append(ctx.name.getText() + "\n");
		for (RewardItemContext item : ctx.rewardItem()){
			str.append("\t" + visit(item));
		}
		str.append("endrewards\n\n");
		return str.toString();
	}
	
	
	@Override 
	public String visitRewardItem (PrismParser.RewardItemContext ctx){
//		('['(transitionID=variable)?']')? rewardPrecondition ':' expression  ';'
		StringBuilder str = new StringBuilder();
		if (ctx.transitionID!=null){
			str.append("[" + ctx.transitionID.getText() + "] ");
		}
		str.append(visit(ctx.rewardPrecondition()));
		str.append(":");
		str.append(visit(ctx.expression()) +";");
		return str.toString() + "\n";
	}
	
	
	@Override
	public String visitRewardPrecExpression (PrismParser.RewardPrecExpressionContext ctx){
		return (ctx.variable().getText() +" "+ ctx.operator().getText() +" "+ visit(ctx.expression()));
	}

	
	@Override
	public String visitRewardPrecBoolean (PrismParser.RewardPrecBooleanContext ctx){
		return (" "+ ctx.BOOLEAN().getText() +" ");
	}
	
	
	@Override
	public String visitIntOrVarInt (PrismParser.IntOrVarIntContext ctx){
		return ctx.getText();
	}
	
	
	@Override
	public String visitIntOrVarVar (PrismParser.IntOrVarVarContext ctx){
		return (ctx.variable().ID().getText());
	}
	
	
	////////////////////////
	//Evolvable elements here
	
	@Override 
	public String visitEvolveConst (PrismParser.EvolveConstContext ctx){
		StringBuilder str 	= new StringBuilder("const ");
		String name			= ctx.variable().getText();
		String[] bounds 	= visit(ctx.bounds()).split(",");
		Number minValue;
		Number maxValue;

		if (ctx.CONSTANTTYPE.getText().equals("int")){
			str.append("int ");
			minValue	= Integer.parseInt(bounds[0]);
			maxValue	= Integer.parseInt(bounds[1]);
			evolvableIntegerList.add(new EvolvableInteger(name, minValue, maxValue));
			str.append(name +" = "+ EvolvableID.getEvolvableIDLiteral(EvolvableID.CONSTANT_INT) + name +";\n");
		}
		else{
			str.append("double ");
			minValue	= Double.parseDouble(bounds[0]);
			maxValue	= Double.parseDouble(bounds[1]);
			evolvableDoubleList.add(new EvolvableDouble(name, minValue, maxValue));
			str.append(name +" = "+ EvolvableID.getEvolvableIDLiteral(EvolvableID.CONSTANT_DOUBLE) + name +";\n");
		}
		
		return "//"+str.toString();
	}
	
	
	@Override
	/** EVOLVE DISTRIBUTION variable '['cardinality=INT']' bounds["double"]* ';' */
	public String visitEvolveDistribution (PrismParser.EvolveDistributionContext ctx){
		StringBuilder str 	= new StringBuilder("[");
		
		String name		= ctx.variable().getText();
		
		//get the number of transitions
		int cardinality = Integer.parseInt(ctx.cardinality.getText());		
		//create an auxiliary 2D-double array to keep the bounds for each transition
		double[][] transitionsBounds = new double [cardinality][2];
		//retrieve (if given) or make default (0,1) if not given
		for (int index=0; index<cardinality; index++){
			if (ctx.bounds(index) != null){
				String[] bounds = visit(ctx.bounds(index)).split(",");
				transitionsBounds[index][0] = Double.parseDouble(bounds[0]);
				transitionsBounds[index][1] = Double.parseDouble(bounds[1]);
			}
			else{
				transitionsBounds[index][0] = 0;
				transitionsBounds[index][1] = 1;
			}
		}
		
		EvolvableDistribution evolvableDistrbution = new EvolvableDistribution(name, transitionsBounds);
		evolvableDistributionList.add(evolvableDistrbution);
		return "//evolvable distribution\n";//cardinality+"";
	}
	
	
	@Override
	/** EVOLVE 'module' name=ID (bounds["int"])? varDeclaration* command+ 'endmodule' */
	public String visitEvolveModule(PrismParser.EvolveModuleContext ctx){
		StringBuilder str = new StringBuilder("\nmodule ");
		
		String name 	 = ctx.name.getText();
		str.append(name+ "\n");
		
		//parse variable declarations
		for (VarDeclarationContext varDeclaration : ctx.varDeclaration()){
			str.append(visit(varDeclaration));
		}
		str.append ("\n");
		//parse commands
		for (CommandContext command : ctx.command()){
			str.append(visit(command));
		}
		str.append("endmodule \n\n");
		
		//check cardinality
		int minValue = 1, maxValue = 1;
		if (ctx.bounds() != null){
			String[] bounds = visit(ctx.bounds()).split(",");
			minValue 	= Integer.parseInt(bounds[0]);
			maxValue	= Integer.parseInt(bounds[1]);
		}
		EvolvableModule evolvableModule = new EvolvableModule(name, minValue, maxValue, str.toString());
		evolvableModuleList.add(evolvableModule);
			
//		System.err.println("module will evolve");

		return "";//str.toString();
	}
	
	
	@Override
	public String visitBounds (PrismParser.BoundsContext ctx){
		return ctx.minValue.getText() +","+ ctx.maxValue.getText();
	}
	
	
	///////////////////////////////////
	// Access methods
	public List<Evolvable> getEvolvableList(){
		return this.evolvableList;
	}
	
	public String getInternalModelRepresentation (){
		return this.modelString.toString();
	}
	
	/**
	 * Scan through the model string and check for evolvable distribution within NON-evolvable modules
	 * If such evolvable element exists, append the appropriate identifier to model string of the module 
	 */
	private void prepareEvolvableDistributions(){
		for (EvolvableDistribution evolvable : evolvableDistributionList){
			String name  			= evolvable.getName();
			int sizeOfName			= name.length();
			int numOfTransitions 	= evolvable.getCardinality();
			int nameIndex 			= 0;
			int transitionCounter	= 1;
			do{
				nameIndex += 2;
				String nameID 			= //EvolvableID.getEvolvableIDLiteral(EvolvableID.DISTRIBUTION) + 
										  name + transitionCounter;
				nameIndex =  modelString.indexOf(name,  nameIndex);
				if (nameIndex!=-1)
					modelString.replace(nameIndex, nameIndex+sizeOfName, nameID);
				if (transitionCounter++ >= numOfTransitions){
					transitionCounter = 1;
				}
			}
			while (nameIndex!=-1 );	
		}
	}
	
	
	private void prepareEvolvableModulesReplica(){
		for (EvolvableModule evolvableModule : evolvableModuleList){
				
			StringBuilder moduleStr = new StringBuilder(evolvableModule. getModuleString());
			for (EvolvableDistribution evolvableDistribution : evolvableDistributionList){
				String name  				= evolvableDistribution.getName();
				int sizeOfName				= name.length();
				int numOfTransitions 		= evolvableDistribution.getCardinality();
				int nameIndex 				= 0;
				int transitionCounter		= 1;
				boolean distributionExists	= false;
				do{
					nameIndex += 2;
					String nameID 			= //EvolvableID.getEvolvableIDLiteral(EvolvableID.DISTRIBUTION) + 
											  name + transitionCounter;
					nameIndex =  moduleStr.indexOf(name,  nameIndex);
					if (nameIndex!=-1){
						moduleStr.replace(nameIndex, nameIndex+sizeOfName, nameID);
						distributionExists 	= true;
					}
					if (transitionCounter++ >= numOfTransitions){
						transitionCounter = 1;
					}
				}
				while (nameIndex!=-1 );	
					
				if (distributionExists){//if it exists append it to the list of evolvableModule
					evolvableModule.appendEvolvableDistribution(evolvableDistribution);
				}				
			}
			evolvableModule.setModuleString(moduleStr.toString());
		}
	}
	
	
	private void prepareEvolvableModulesAlternative(){
//		for (EvolvableModule evolvableModule : evolvableModuleList){
		for (int evolvableModuleIndex=0; evolvableModuleIndex<evolvableModuleList.size();){
			EvolvableModule evolvableModule  = evolvableModuleList.get(evolvableModuleIndex);
			//check if a module with the same name exists
			String nameToCompare = evolvableModule.getName();
			boolean identicalFound = false;
			
//			for (EvolvableModule evolvableM : evolvableModuleList){
			for (int evolvableMIndex=evolvableModuleIndex; evolvableMIndex<evolvableModuleList.size();){
				EvolvableModule evolvableM = evolvableModuleList.get(evolvableMIndex);
				String name = evolvableM.getName();
				//if a module with the same exists and it is not the same
				if (nameToCompare.equals(name) && !evolvableModule.equals(evolvableM)){
					//check if a super module exists with this name
					boolean moduleSuperExists = false;
					for (EvolvableModuleAlternative evolvableModuleSuper : evolvableModuleSuperList){
						if (evolvableModuleSuper.getName().equals(nameToCompare)){
							evolvableModuleSuper.addEvolvableModule(evolvableM);
							moduleSuperExists = true;
							break;
						}
					}
					//if not, create one and append the module
					if (!moduleSuperExists){
						EvolvableModuleAlternative evolvableModuleSuper = new EvolvableModuleAlternative(name);
						evolvableModuleSuper.addEvolvableModule(evolvableM);
						evolvableModuleSuperList.add(evolvableModuleSuper);
					}
					identicalFound = true;
					//remove this module from the evolvableModulelist
					evolvableModuleList.remove(evolvableM);
				}
				else{
					evolvableMIndex++;
				}
			}
			//check if the same module exists in evolvableModuleList
			if (!identicalFound){
				boolean added = false;
				for (EvolvableModuleAlternative evolvableModuleSuper : evolvableModuleSuperList){
					if (evolvableModuleSuper.getName().equals(nameToCompare)
							&& !evolvableModuleSuper.getEvolvableModuleList().contains(evolvableModule)){
						evolvableModuleSuper.addEvolvableModule(evolvableModule);
						evolvableModuleList.remove(evolvableModule);
						added = true;
					}
				}
				if (!added)
					evolvableModuleIndex++;
			}
		}
	}
	
	
	
	private void concatenateEvolvables(){
		for (EvolvableDouble evoDouble : evolvableDoubleList){
			evolvableList.add(evoDouble);
		}
		for (EvolvableInteger evoInteger : evolvableIntegerList){
			evolvableList.add(evoInteger);
		}
		for (EvolvableDistribution evoDistr : evolvableDistributionList){
			evolvableList.add(evoDistr);
		}
		for (EvolvableModule evoModule : evolvableModuleList){
			evolvableList.add(evoModule);
		}
		for (EvolvableModuleAlternative evoModuleSuper : evolvableModuleSuperList){
			evolvableList.add(evoModuleSuper);
		}		
	}
	
	
	@SuppressWarnings("unused")
	private String generateString(String...args){
		String txt = "";
		for (String str : args){
			txt += str;
		}
		return txt;
	}
	
	
	
}
