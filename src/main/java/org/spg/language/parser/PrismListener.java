package org.spg.language.parser;

import java.util.HashMap;
import java.util.Map;

import org.spg.language.prism.grammar.PrismBaseListener;
import org.spg.language.prism.grammar.PrismParser;

class PrismListener extends PrismBaseListener {
	
	PrismParser parser;
	
	/** "memory" for our calculator; variable/value pairs go here */ 
	Map<String, String> memory = new HashMap<String, String>();
	
	public PrismListener(PrismParser parser){
		this.parser = parser;
	}
	
	
	@Override 
	public void enterModelType(PrismParser.ModelTypeContext ctx) { 
		memory.put("modelType", ctx.getText());		
	}
	
	@Override 
	public void enterModuleSimple (PrismParser.ModuleSimpleContext ctx) {
		String str = ctx.name.getText();
//		ctx.variables.
		memory.put("module " + str, ctx.getText());
	}

	
	@Override 
	public void enterIntVarDeclaration(PrismParser.IntVarDeclarationContext ctx) { 
//		System.err.println("Integer variable: \t" + ctx.name.getText() +"\t"+ ctx.lowerBound.getText() +"\t"+ ctx.upperBound.getText());
	}

	
	@Override
	public void enterBoolVarDeclaration(PrismParser.BoolVarDeclarationContext ctx){
//		System.err.println("Boolean variable \t" + ctx.name.getText() +"\t"+ ctx.initValue.getText());
	}
	
	@Override
	public void enterCommand(PrismParser.CommandContext ctx){
//		System.err.println("Command \t" + ctx.guardName.getText());
	}
	
//	@Override 
//	public void exitGuardEntry (PrismParser.GuardEntryContext ctx){
////		System.err.println("Guard \t" + ctx.lo());
//	}
	
	@Override
	public void exitTransitionEntry (PrismParser.TransitionEntryContext ctx){
//		if (ctx.getChildCount())
//		System.out.println(ctx.getChildCount() +"\t"+ ctx.getText());
	}
	
//	@Override
//	public void exitStatementEv (PrismParser.StatementEvContext ctx){
//		System.out.println("(" + ctx.toState.getText() + "'=" + ctx.INT()  +")");
//	}
	

	public void printMemory(){
		System.out.println(memory.size());
		for (Map.Entry<String, String> entry : memory.entrySet()) {
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}
	
	

}
