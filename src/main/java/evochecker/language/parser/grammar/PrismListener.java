// Generated from grammar/Prism.g4 by ANTLR 4.5

//  package org.spg.language.prism.grammar;
  package evochecker.language.parser.grammar;
  import java.util.*;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PrismParser}.
 */
public interface PrismListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PrismParser#model}.
	 * @param ctx the parse tree
	 */
	void enterModel(PrismParser.ModelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#model}.
	 * @param ctx the parse tree
	 */
	void exitModel(PrismParser.ModelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduleSimple}
	 * labeled alternative in {@link PrismParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModuleSimple(PrismParser.ModuleSimpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduleSimple}
	 * labeled alternative in {@link PrismParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModuleSimple(PrismParser.ModuleSimpleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduleRenaming}
	 * labeled alternative in {@link PrismParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModuleRenaming(PrismParser.ModuleRenamingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduleRenaming}
	 * labeled alternative in {@link PrismParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModuleRenaming(PrismParser.ModuleRenamingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduleRenamingVarSimple}
	 * labeled alternative in {@link PrismParser#moduleRenamingVar}.
	 * @param ctx the parse tree
	 */
	void enterModuleRenamingVarSimple(PrismParser.ModuleRenamingVarSimpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduleRenamingVarSimple}
	 * labeled alternative in {@link PrismParser#moduleRenamingVar}.
	 * @param ctx the parse tree
	 */
	void exitModuleRenamingVarSimple(PrismParser.ModuleRenamingVarSimpleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduleRenamingVarMulti}
	 * labeled alternative in {@link PrismParser#moduleRenamingVar}.
	 * @param ctx the parse tree
	 */
	void enterModuleRenamingVarMulti(PrismParser.ModuleRenamingVarMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduleRenamingVarMulti}
	 * labeled alternative in {@link PrismParser#moduleRenamingVar}.
	 * @param ctx the parse tree
	 */
	void exitModuleRenamingVarMulti(PrismParser.ModuleRenamingVarMultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#reward}.
	 * @param ctx the parse tree
	 */
	void enterReward(PrismParser.RewardContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#reward}.
	 * @param ctx the parse tree
	 */
	void exitReward(PrismParser.RewardContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#rewardItem}.
	 * @param ctx the parse tree
	 */
	void enterRewardItem(PrismParser.RewardItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#rewardItem}.
	 * @param ctx the parse tree
	 */
	void exitRewardItem(PrismParser.RewardItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rewardPrecExpression}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 */
	void enterRewardPrecExpression(PrismParser.RewardPrecExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rewardPrecExpression}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 */
	void exitRewardPrecExpression(PrismParser.RewardPrecExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rewardPrecBoolean}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 */
	void enterRewardPrecBoolean(PrismParser.RewardPrecBooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rewardPrecBoolean}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 */
	void exitRewardPrecBoolean(PrismParser.RewardPrecBooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rewardPrecExpressBoolean}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 */
	void enterRewardPrecExpressBoolean(PrismParser.RewardPrecExpressBooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rewardPrecExpressBoolean}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 */
	void exitRewardPrecExpressBoolean(PrismParser.RewardPrecExpressBooleanContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(PrismParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(PrismParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionParamExpr}
	 * labeled alternative in {@link PrismParser#functionParam}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParamExpr(PrismParser.FunctionParamExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionParamExpr}
	 * labeled alternative in {@link PrismParser#functionParam}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParamExpr(PrismParser.FunctionParamExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionParamMulti}
	 * labeled alternative in {@link PrismParser#functionParam}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParamMulti(PrismParser.FunctionParamMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionParamMulti}
	 * labeled alternative in {@link PrismParser#functionParam}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParamMulti(PrismParser.FunctionParamMultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterFormula(PrismParser.FormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitFormula(PrismParser.FormulaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantEntry}
	 * labeled alternative in {@link PrismParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstantEntry(PrismParser.ConstantEntryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantEntry}
	 * labeled alternative in {@link PrismParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstantEntry(PrismParser.ConstantEntryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantBool}
	 * labeled alternative in {@link PrismParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstantBool(PrismParser.ConstantBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantBool}
	 * labeled alternative in {@link PrismParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstantBool(PrismParser.ConstantBoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code evolveRange}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void enterEvolveRange(PrismParser.EvolveRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code evolveRange}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void exitEvolveRange(PrismParser.EvolveRangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code evolveDiscrete}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void enterEvolveDiscrete(PrismParser.EvolveDiscreteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code evolveDiscrete}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void exitEvolveDiscrete(PrismParser.EvolveDiscreteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code evolveDistribution}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void enterEvolveDistribution(PrismParser.EvolveDistributionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code evolveDistribution}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void exitEvolveDistribution(PrismParser.EvolveDistributionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code evolveModule}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void enterEvolveModule(PrismParser.EvolveModuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code evolveModule}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void exitEvolveModule(PrismParser.EvolveModuleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code evolveBool}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void enterEvolveBool(PrismParser.EvolveBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code evolveBool}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 */
	void exitEvolveBool(PrismParser.EvolveBoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolVarDeclaration}
	 * labeled alternative in {@link PrismParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterBoolVarDeclaration(PrismParser.BoolVarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolVarDeclaration}
	 * labeled alternative in {@link PrismParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitBoolVarDeclaration(PrismParser.BoolVarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intVarDeclaration}
	 * labeled alternative in {@link PrismParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterIntVarDeclaration(PrismParser.IntVarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intVarDeclaration}
	 * labeled alternative in {@link PrismParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitIntVarDeclaration(PrismParser.IntVarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#boundsRange}.
	 * @param ctx the parse tree
	 */
	void enterBoundsRange(PrismParser.BoundsRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#boundsRange}.
	 * @param ctx the parse tree
	 */
	void exitBoundsRange(PrismParser.BoundsRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#boundsDiscrete}.
	 * @param ctx the parse tree
	 */
	void enterBoundsDiscrete(PrismParser.BoundsDiscreteContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#boundsDiscrete}.
	 * @param ctx the parse tree
	 */
	void exitBoundsDiscrete(PrismParser.BoundsDiscreteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code discreteOptionDoubleMulti}
	 * labeled alternative in {@link PrismParser#discreteOptionDouble}.
	 * @param ctx the parse tree
	 */
	void enterDiscreteOptionDoubleMulti(PrismParser.DiscreteOptionDoubleMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code discreteOptionDoubleMulti}
	 * labeled alternative in {@link PrismParser#discreteOptionDouble}.
	 * @param ctx the parse tree
	 */
	void exitDiscreteOptionDoubleMulti(PrismParser.DiscreteOptionDoubleMultiContext ctx);
	/**
	 * Enter a parse tree produced by the {@code discreteOptionDoubleSingle}
	 * labeled alternative in {@link PrismParser#discreteOptionDouble}.
	 * @param ctx the parse tree
	 */
	void enterDiscreteOptionDoubleSingle(PrismParser.DiscreteOptionDoubleSingleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code discreteOptionDoubleSingle}
	 * labeled alternative in {@link PrismParser#discreteOptionDouble}.
	 * @param ctx the parse tree
	 */
	void exitDiscreteOptionDoubleSingle(PrismParser.DiscreteOptionDoubleSingleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code discreteOptionIntSingle}
	 * labeled alternative in {@link PrismParser#discreteOptionInt}.
	 * @param ctx the parse tree
	 */
	void enterDiscreteOptionIntSingle(PrismParser.DiscreteOptionIntSingleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code discreteOptionIntSingle}
	 * labeled alternative in {@link PrismParser#discreteOptionInt}.
	 * @param ctx the parse tree
	 */
	void exitDiscreteOptionIntSingle(PrismParser.DiscreteOptionIntSingleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code discreteOptionIntMulti}
	 * labeled alternative in {@link PrismParser#discreteOptionInt}.
	 * @param ctx the parse tree
	 */
	void enterDiscreteOptionIntMulti(PrismParser.DiscreteOptionIntMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code discreteOptionIntMulti}
	 * labeled alternative in {@link PrismParser#discreteOptionInt}.
	 * @param ctx the parse tree
	 */
	void exitDiscreteOptionIntMulti(PrismParser.DiscreteOptionIntMultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(PrismParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(PrismParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code guardParen}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void enterGuardParen(PrismParser.GuardParenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code guardParen}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void exitGuardParen(PrismParser.GuardParenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code guardMulti}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void enterGuardMulti(PrismParser.GuardMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code guardMulti}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void exitGuardMulti(PrismParser.GuardMultiContext ctx);
	/**
	 * Enter a parse tree produced by the {@code guardNot}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void enterGuardNot(PrismParser.GuardNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code guardNot}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void exitGuardNot(PrismParser.GuardNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code guardString}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void enterGuardString(PrismParser.GuardStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code guardString}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void exitGuardString(PrismParser.GuardStringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code guardExpression}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void enterGuardExpression(PrismParser.GuardExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code guardExpression}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void exitGuardExpression(PrismParser.GuardExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code guardBool}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void enterGuardBool(PrismParser.GuardBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code guardBool}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 */
	void exitGuardBool(PrismParser.GuardBoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code transitionEntry}
	 * labeled alternative in {@link PrismParser#transition}.
	 * @param ctx the parse tree
	 */
	void enterTransitionEntry(PrismParser.TransitionEntryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code transitionEntry}
	 * labeled alternative in {@link PrismParser#transition}.
	 * @param ctx the parse tree
	 */
	void exitTransitionEntry(PrismParser.TransitionEntryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code transitionMulti}
	 * labeled alternative in {@link PrismParser#transition}.
	 * @param ctx the parse tree
	 */
	void enterTransitionMulti(PrismParser.TransitionMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code transitionMulti}
	 * labeled alternative in {@link PrismParser#transition}.
	 * @param ctx the parse tree
	 */
	void exitTransitionMulti(PrismParser.TransitionMultiContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementMulti}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementMulti(PrismParser.StatementMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementMulti}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementMulti(PrismParser.StatementMultiContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementMain}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementMain(PrismParser.StatementMainContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementMain}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementMain(PrismParser.StatementMainContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementBool}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementBool(PrismParser.StatementBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementBool}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementBool(PrismParser.StatementBoolContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(PrismParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(PrismParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionValue}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionValue(PrismParser.ExpressionValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionValue}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionValue(PrismParser.ExpressionValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionFunction}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionFunction(PrismParser.ExpressionFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionFunction}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionFunction(PrismParser.ExpressionFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionMulti}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionMulti(PrismParser.ExpressionMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionMulti}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionMulti(PrismParser.ExpressionMultiContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionParen}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionParen(PrismParser.ExpressionParenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionParen}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionParen(PrismParser.ExpressionParenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionVariable}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionVariable(PrismParser.ExpressionVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionVariable}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionVariable(PrismParser.ExpressionVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#modelType}.
	 * @param ctx the parse tree
	 */
	void enterModelType(PrismParser.ModelTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#modelType}.
	 * @param ctx the parse tree
	 */
	void exitModelType(PrismParser.ModelTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#comparisonOp}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOp(PrismParser.ComparisonOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#comparisonOp}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOp(PrismParser.ComparisonOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#logicalOp}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOp(PrismParser.LogicalOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#logicalOp}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOp(PrismParser.LogicalOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#numericalOp}.
	 * @param ctx the parse tree
	 */
	void enterNumericalOp(PrismParser.NumericalOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#numericalOp}.
	 * @param ctx the parse tree
	 */
	void exitNumericalOp(PrismParser.NumericalOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intOrVarInt}
	 * labeled alternative in {@link PrismParser#intOrVar}.
	 * @param ctx the parse tree
	 */
	void enterIntOrVarInt(PrismParser.IntOrVarIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intOrVarInt}
	 * labeled alternative in {@link PrismParser#intOrVar}.
	 * @param ctx the parse tree
	 */
	void exitIntOrVarInt(PrismParser.IntOrVarIntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intOrVarVar}
	 * labeled alternative in {@link PrismParser#intOrVar}.
	 * @param ctx the parse tree
	 */
	void enterIntOrVarVar(PrismParser.IntOrVarVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intOrVarVar}
	 * labeled alternative in {@link PrismParser#intOrVar}.
	 * @param ctx the parse tree
	 */
	void exitIntOrVarVar(PrismParser.IntOrVarVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrismParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(PrismParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrismParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(PrismParser.OperatorContext ctx);
}