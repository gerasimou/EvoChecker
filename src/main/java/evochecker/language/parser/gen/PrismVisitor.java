// Generated from grammar/Prism.g4 by ANTLR 4.5

  package evochecker.language.parser.gen;
  import java.util.*;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PrismParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PrismVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PrismParser#model}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel(PrismParser.ModelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code moduleSimple}
	 * labeled alternative in {@link PrismParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleSimple(PrismParser.ModuleSimpleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code moduleRenaming}
	 * labeled alternative in {@link PrismParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleRenaming(PrismParser.ModuleRenamingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code moduleRenamingVarSimple}
	 * labeled alternative in {@link PrismParser#moduleRenamingVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleRenamingVarSimple(PrismParser.ModuleRenamingVarSimpleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code moduleRenamingVarMulti}
	 * labeled alternative in {@link PrismParser#moduleRenamingVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleRenamingVarMulti(PrismParser.ModuleRenamingVarMultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#reward}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReward(PrismParser.RewardContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#rewardItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRewardItem(PrismParser.RewardItemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rewardPrecExpression}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRewardPrecExpression(PrismParser.RewardPrecExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rewardPrecBoolean}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRewardPrecBoolean(PrismParser.RewardPrecBooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rewardPrecExpressBoolean}
	 * labeled alternative in {@link PrismParser#rewardPrecondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRewardPrecExpressBoolean(PrismParser.RewardPrecExpressBooleanContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(PrismParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionParamExpr}
	 * labeled alternative in {@link PrismParser#functionParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParamExpr(PrismParser.FunctionParamExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionParamMulti}
	 * labeled alternative in {@link PrismParser#functionParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParamMulti(PrismParser.FunctionParamMultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormula(PrismParser.FormulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(PrismParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code evolveRange}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvolveRange(PrismParser.EvolveRangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code evolveDiscrete}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvolveDiscrete(PrismParser.EvolveDiscreteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code evolveDistribution}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvolveDistribution(PrismParser.EvolveDistributionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code evolveModule}
	 * labeled alternative in {@link PrismParser#evolvable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvolveModule(PrismParser.EvolveModuleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolVarDeclaration}
	 * labeled alternative in {@link PrismParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolVarDeclaration(PrismParser.BoolVarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intVarDeclaration}
	 * labeled alternative in {@link PrismParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntVarDeclaration(PrismParser.IntVarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#boundsRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundsRange(PrismParser.BoundsRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#boundsDiscrete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundsDiscrete(PrismParser.BoundsDiscreteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code discreteOptionDoubleMulti}
	 * labeled alternative in {@link PrismParser#discreteOptionDouble}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiscreteOptionDoubleMulti(PrismParser.DiscreteOptionDoubleMultiContext ctx);
	/**
	 * Visit a parse tree produced by the {@code discreteOptionDoubleSingle}
	 * labeled alternative in {@link PrismParser#discreteOptionDouble}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiscreteOptionDoubleSingle(PrismParser.DiscreteOptionDoubleSingleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code discreteOptionIntSingle}
	 * labeled alternative in {@link PrismParser#discreteOptionInt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiscreteOptionIntSingle(PrismParser.DiscreteOptionIntSingleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code discreteOptionIntMulti}
	 * labeled alternative in {@link PrismParser#discreteOptionInt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiscreteOptionIntMulti(PrismParser.DiscreteOptionIntMultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(PrismParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code guardParen}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardParen(PrismParser.GuardParenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code guardMulti}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardMulti(PrismParser.GuardMultiContext ctx);
	/**
	 * Visit a parse tree produced by the {@code guardNot}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardNot(PrismParser.GuardNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code guardString}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardString(PrismParser.GuardStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code guardExpression}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardExpression(PrismParser.GuardExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code guardBool}
	 * labeled alternative in {@link PrismParser#guard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardBool(PrismParser.GuardBoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code transitionEntry}
	 * labeled alternative in {@link PrismParser#transition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionEntry(PrismParser.TransitionEntryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code transitionMulti}
	 * labeled alternative in {@link PrismParser#transition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionMulti(PrismParser.TransitionMultiContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementMulti}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementMulti(PrismParser.StatementMultiContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementMain}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementMain(PrismParser.StatementMainContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementBool}
	 * labeled alternative in {@link PrismParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementBool(PrismParser.StatementBoolContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(PrismParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionValue}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionValue(PrismParser.ExpressionValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionFunction}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionFunction(PrismParser.ExpressionFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionMulti}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionMulti(PrismParser.ExpressionMultiContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionParen}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionParen(PrismParser.ExpressionParenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionVariable}
	 * labeled alternative in {@link PrismParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionVariable(PrismParser.ExpressionVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#modelType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelType(PrismParser.ModelTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#comparisonOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOp(PrismParser.ComparisonOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#logicalOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOp(PrismParser.LogicalOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#numericalOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericalOp(PrismParser.NumericalOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intOrVarInt}
	 * labeled alternative in {@link PrismParser#intOrVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntOrVarInt(PrismParser.IntOrVarIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intOrVarVar}
	 * labeled alternative in {@link PrismParser#intOrVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntOrVarVar(PrismParser.IntOrVarVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrismParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(PrismParser.OperatorContext ctx);
}