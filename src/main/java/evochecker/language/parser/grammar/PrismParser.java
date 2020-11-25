// Generated from grammar/Prism.g4 by ANTLR 4.5

//  package org.spg.language.prism.grammar;
  package evochecker.language.parser.grammar;
  import java.util.*;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PrismParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, ASSIGN=35, EVOLVE=36, CONST=37, DISTRIBUTION=38, 
		PARAM=39, FUNCTIONIDENTIFIER=40, CONSTANTTYPE=41, SLCOMMENT=42, DTMC=43, 
		CTMC=44, MDP=45, BOOLEAN=46, OPERATOR=47, ID=48, INT=49, DOUBLE=50, STRING=51, 
		WS=52;
	public static final int
		RULE_model = 0, RULE_module = 1, RULE_moduleRenamingVar = 2, RULE_reward = 3, 
		RULE_rewardItem = 4, RULE_rewardPrecondition = 5, RULE_function = 6, RULE_functionParam = 7, 
		RULE_formula = 8, RULE_constant = 9, RULE_evolvable = 10, RULE_varDeclaration = 11, 
		RULE_boundsRange = 12, RULE_boundsDiscrete = 13, RULE_discreteOptionDouble = 14, 
		RULE_discreteOptionInt = 15, RULE_command = 16, RULE_guard = 17, RULE_transition = 18, 
		RULE_statement = 19, RULE_variable = 20, RULE_expression = 21, RULE_modelType = 22, 
		RULE_comparisonOp = 23, RULE_logicalOp = 24, RULE_numericalOp = 25, RULE_intOrVar = 26, 
		RULE_operator = 27;
	public static final String[] ruleNames = {
		"model", "module", "moduleRenamingVar", "reward", "rewardItem", "rewardPrecondition", 
		"function", "functionParam", "formula", "constant", "evolvable", "varDeclaration", 
		"boundsRange", "boundsDiscrete", "discreteOptionDouble", "discreteOptionInt", 
		"command", "guard", "transition", "statement", "variable", "expression", 
		"modelType", "comparisonOp", "logicalOp", "numericalOp", "intOrVar", "operator"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'module'", "'endmodule'", "'['", "']'", "','", "'rewards'", "'endrewards'", 
		"':'", "';'", "'('", "')'", "'formula'", "'bool'", "'{'", "'}'", "'init'", 
		"'..'", "'->'", "'!'", "'+'", "'''", "'&'", "'>'", "'>='", "'<'", "'<='", 
		"'!='", "'|'", "'*'", "'/'", "'-'", "'<=>'", "'=>'", "'?'", "'='", "'evolve'", 
		"'const'", "'distribution'", "'param'", null, null, null, "'dtmc'", "'ctmc'", 
		"'mdp'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, "ASSIGN", 
		"EVOLVE", "CONST", "DISTRIBUTION", "PARAM", "FUNCTIONIDENTIFIER", "CONSTANTTYPE", 
		"SLCOMMENT", "DTMC", "CTMC", "MDP", "BOOLEAN", "OPERATOR", "ID", "INT", 
		"DOUBLE", "STRING", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Prism.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


		Set<String> types = new HashSet<String>() {{add("T");}};
		boolean istype() { return types.contains(getCurrentToken().getText()); }

	public PrismParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ModelContext extends ParserRuleContext {
		public ModelTypeContext modelType() {
			return getRuleContext(ModelTypeContext.class,0);
		}
		public TerminalNode EOF() { return getToken(PrismParser.EOF, 0); }
		public List<ModuleContext> module() {
			return getRuleContexts(ModuleContext.class);
		}
		public ModuleContext module(int i) {
			return getRuleContext(ModuleContext.class,i);
		}
		public List<RewardContext> reward() {
			return getRuleContexts(RewardContext.class);
		}
		public RewardContext reward(int i) {
			return getRuleContext(RewardContext.class,i);
		}
		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}
		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class,i);
		}
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public List<EvolvableContext> evolvable() {
			return getRuleContexts(EvolvableContext.class);
		}
		public EvolvableContext evolvable(int i) {
			return getRuleContext(EvolvableContext.class,i);
		}
		public List<TerminalNode> SLCOMMENT() { return getTokens(PrismParser.SLCOMMENT); }
		public TerminalNode SLCOMMENT(int i) {
			return getToken(PrismParser.SLCOMMENT, i);
		}
		public ModelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_model; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterModel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitModel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitModel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelContext model() throws RecognitionException {
		ModelContext _localctx = new ModelContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_model);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			modelType();
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__5) | (1L << T__11) | (1L << EVOLVE) | (1L << CONST) | (1L << SLCOMMENT))) != 0)) {
				{
				setState(63);
				switch (_input.LA(1)) {
				case T__0:
					{
					setState(57);
					module();
					}
					break;
				case T__5:
					{
					setState(58);
					reward();
					}
					break;
				case CONST:
					{
					setState(59);
					constant();
					}
					break;
				case T__11:
					{
					setState(60);
					formula();
					}
					break;
				case EVOLVE:
					{
					setState(61);
					evolvable();
					}
					break;
				case SLCOMMENT:
					{
					setState(62);
					match(SLCOMMENT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModuleContext extends ParserRuleContext {
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
	 
		public ModuleContext() { }
		public void copyFrom(ModuleContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ModuleSimpleContext extends ModuleContext {
		public Token name;
		public TerminalNode ID() { return getToken(PrismParser.ID, 0); }
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<CommandContext> command() {
			return getRuleContexts(CommandContext.class);
		}
		public CommandContext command(int i) {
			return getRuleContext(CommandContext.class,i);
		}
		public ModuleSimpleContext(ModuleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterModuleSimple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitModuleSimple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitModuleSimple(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ModuleRenamingContext extends ModuleContext {
		public Token newModuleName;
		public Token oldModuleName;
		public ModuleRenamingVarContext moduleRenamingVar() {
			return getRuleContext(ModuleRenamingVarContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(PrismParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(PrismParser.ID, i);
		}
		public ModuleRenamingContext(ModuleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterModuleRenaming(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitModuleRenaming(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitModuleRenaming(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_module);
		int _la;
		try {
			setState(94);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new ModuleSimpleContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				match(T__0);
				setState(71);
				((ModuleSimpleContext)_localctx).name = match(ID);
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ID) {
					{
					{
					setState(72);
					varDeclaration();
					}
					}
					setState(77);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(79); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(78);
					command();
					}
					}
					setState(81); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__2 );
				setState(83);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new ModuleRenamingContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				match(T__0);
				setState(86);
				((ModuleRenamingContext)_localctx).newModuleName = match(ID);
				setState(87);
				match(ASSIGN);
				setState(88);
				((ModuleRenamingContext)_localctx).oldModuleName = match(ID);
				setState(89);
				match(T__2);
				setState(90);
				moduleRenamingVar();
				setState(91);
				match(T__3);
				setState(92);
				match(T__1);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModuleRenamingVarContext extends ParserRuleContext {
		public ModuleRenamingVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleRenamingVar; }
	 
		public ModuleRenamingVarContext() { }
		public void copyFrom(ModuleRenamingVarContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ModuleRenamingVarMultiContext extends ModuleRenamingVarContext {
		public VariableContext newVar;
		public VariableContext oldVar;
		public ModuleRenamingVarContext moduleRenamingVar() {
			return getRuleContext(ModuleRenamingVarContext.class,0);
		}
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public ModuleRenamingVarMultiContext(ModuleRenamingVarContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterModuleRenamingVarMulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitModuleRenamingVarMulti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitModuleRenamingVarMulti(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ModuleRenamingVarSimpleContext extends ModuleRenamingVarContext {
		public VariableContext newVar;
		public VariableContext oldVar;
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public ModuleRenamingVarSimpleContext(ModuleRenamingVarContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterModuleRenamingVarSimple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitModuleRenamingVarSimple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitModuleRenamingVarSimple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleRenamingVarContext moduleRenamingVar() throws RecognitionException {
		ModuleRenamingVarContext _localctx = new ModuleRenamingVarContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_moduleRenamingVar);
		try {
			setState(106);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				_localctx = new ModuleRenamingVarSimpleContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				((ModuleRenamingVarSimpleContext)_localctx).newVar = variable();
				setState(97);
				match(ASSIGN);
				setState(98);
				((ModuleRenamingVarSimpleContext)_localctx).oldVar = variable();
				}
				break;
			case 2:
				_localctx = new ModuleRenamingVarMultiContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				((ModuleRenamingVarMultiContext)_localctx).newVar = variable();
				setState(101);
				match(ASSIGN);
				setState(102);
				((ModuleRenamingVarMultiContext)_localctx).oldVar = variable();
				setState(103);
				match(T__4);
				setState(104);
				moduleRenamingVar();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RewardContext extends ParserRuleContext {
		public Token name;
		public TerminalNode STRING() { return getToken(PrismParser.STRING, 0); }
		public List<RewardItemContext> rewardItem() {
			return getRuleContexts(RewardItemContext.class);
		}
		public RewardItemContext rewardItem(int i) {
			return getRuleContext(RewardItemContext.class,i);
		}
		public RewardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reward; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterReward(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitReward(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitReward(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RewardContext reward() throws RecognitionException {
		RewardContext _localctx = new RewardContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_reward);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			match(T__5);
			setState(109);
			((RewardContext)_localctx).name = match(STRING);
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__9) | (1L << FUNCTIONIDENTIFIER) | (1L << BOOLEAN) | (1L << ID) | (1L << INT) | (1L << DOUBLE))) != 0)) {
				{
				{
				setState(110);
				rewardItem();
				}
				}
				setState(115);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(116);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RewardItemContext extends ParserRuleContext {
		public VariableContext transitionID;
		public RewardPreconditionContext rewardPrecondition() {
			return getRuleContext(RewardPreconditionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public RewardItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rewardItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterRewardItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitRewardItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitRewardItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RewardItemContext rewardItem() throws RecognitionException {
		RewardItemContext _localctx = new RewardItemContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_rewardItem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(118);
				match(T__2);
				setState(120);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(119);
					((RewardItemContext)_localctx).transitionID = variable();
					}
				}

				setState(122);
				match(T__3);
				}
			}

			setState(125);
			rewardPrecondition();
			setState(126);
			match(T__7);
			setState(127);
			expression(0);
			setState(128);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RewardPreconditionContext extends ParserRuleContext {
		public RewardPreconditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rewardPrecondition; }
	 
		public RewardPreconditionContext() { }
		public void copyFrom(RewardPreconditionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class RewardPrecBooleanContext extends RewardPreconditionContext {
		public TerminalNode BOOLEAN() { return getToken(PrismParser.BOOLEAN, 0); }
		public RewardPrecBooleanContext(RewardPreconditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterRewardPrecBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitRewardPrecBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitRewardPrecBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RewardPrecExpressionContext extends RewardPreconditionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public RewardPrecExpressionContext(RewardPreconditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterRewardPrecExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitRewardPrecExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitRewardPrecExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RewardPrecExpressBooleanContext extends RewardPreconditionContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public TerminalNode BOOLEAN() { return getToken(PrismParser.BOOLEAN, 0); }
		public RewardPrecExpressBooleanContext(RewardPreconditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterRewardPrecExpressBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitRewardPrecExpressBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitRewardPrecExpressBoolean(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RewardPreconditionContext rewardPrecondition() throws RecognitionException {
		RewardPreconditionContext _localctx = new RewardPreconditionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_rewardPrecondition);
		try {
			setState(136);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				_localctx = new RewardPrecExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(130);
				expression(0);
				}
				break;
			case 2:
				_localctx = new RewardPrecBooleanContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(131);
				match(BOOLEAN);
				}
				break;
			case 3:
				_localctx = new RewardPrecExpressBooleanContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(132);
				variable();
				setState(133);
				operator();
				setState(134);
				match(BOOLEAN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode FUNCTIONIDENTIFIER() { return getToken(PrismParser.FUNCTIONIDENTIFIER, 0); }
		public FunctionParamContext functionParam() {
			return getRuleContext(FunctionParamContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(FUNCTIONIDENTIFIER);
			setState(139);
			match(T__9);
			setState(140);
			functionParam();
			setState(141);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionParamContext extends ParserRuleContext {
		public FunctionParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionParam; }
	 
		public FunctionParamContext() { }
		public void copyFrom(FunctionParamContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FunctionParamExprContext extends FunctionParamContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FunctionParamExprContext(FunctionParamContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterFunctionParamExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitFunctionParamExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitFunctionParamExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionParamMultiContext extends FunctionParamContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FunctionParamContext functionParam() {
			return getRuleContext(FunctionParamContext.class,0);
		}
		public FunctionParamMultiContext(FunctionParamContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterFunctionParamMulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitFunctionParamMulti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitFunctionParamMulti(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionParamContext functionParam() throws RecognitionException {
		FunctionParamContext _localctx = new FunctionParamContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_functionParam);
		try {
			setState(148);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				_localctx = new FunctionParamExprContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(143);
				expression(0);
				}
				break;
			case 2:
				_localctx = new FunctionParamMultiContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(144);
				expression(0);
				setState(145);
				match(T__4);
				setState(146);
				functionParam();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormulaContext extends ParserRuleContext {
		public VariableContext name;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterFormula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitFormula(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitFormula(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_formula);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(T__11);
			setState(151);
			((FormulaContext)_localctx).name = variable();
			setState(152);
			match(ASSIGN);
			setState(153);
			expression(0);
			setState(154);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
	 
		public ConstantContext() { }
		public void copyFrom(ConstantContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ConstantEntryContext extends ConstantContext {
		public TerminalNode CONST() { return getToken(PrismParser.CONST, 0); }
		public TerminalNode CONSTANTTYPE() { return getToken(PrismParser.CONSTANTTYPE, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(PrismParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConstantEntryContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterConstantEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitConstantEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitConstantEntry(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConstantBoolContext extends ConstantContext {
		public TerminalNode CONST() { return getToken(PrismParser.CONST, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(PrismParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConstantBoolContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterConstantBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitConstantBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitConstantBool(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_constant);
		try {
			setState(172);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				_localctx = new ConstantEntryContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				match(CONST);
				setState(157);
				match(CONSTANTTYPE);
				setState(158);
				variable();
				{
				setState(159);
				match(ASSIGN);
				setState(160);
				expression(0);
				}
				{
				setState(162);
				match(T__8);
				}
				}
				break;
			case 2:
				_localctx = new ConstantBoolContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				match(CONST);
				setState(165);
				match(T__12);
				setState(166);
				variable();
				{
				setState(167);
				match(ASSIGN);
				setState(168);
				expression(0);
				}
				{
				setState(170);
				match(T__8);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EvolvableContext extends ParserRuleContext {
		public EvolvableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evolvable; }
	 
		public EvolvableContext() { }
		public void copyFrom(EvolvableContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class EvolveModuleContext extends EvolvableContext {
		public Token name;
		public TerminalNode EVOLVE() { return getToken(PrismParser.EVOLVE, 0); }
		public TerminalNode ID() { return getToken(PrismParser.ID, 0); }
		public BoundsRangeContext boundsRange() {
			return getRuleContext(BoundsRangeContext.class,0);
		}
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<CommandContext> command() {
			return getRuleContexts(CommandContext.class);
		}
		public CommandContext command(int i) {
			return getRuleContext(CommandContext.class,i);
		}
		public EvolveModuleContext(EvolvableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterEvolveModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitEvolveModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitEvolveModule(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EvolveDiscreteContext extends EvolvableContext {
		public Token CONSTANTTYPE;
		public TerminalNode EVOLVE() { return getToken(PrismParser.EVOLVE, 0); }
		public TerminalNode CONSTANTTYPE() { return getToken(PrismParser.CONSTANTTYPE, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public BoundsDiscreteContext boundsDiscrete() {
			return getRuleContext(BoundsDiscreteContext.class,0);
		}
		public TerminalNode PARAM() { return getToken(PrismParser.PARAM, 0); }
		public EvolveDiscreteContext(EvolvableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterEvolveDiscrete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitEvolveDiscrete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitEvolveDiscrete(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EvolveBoolContext extends EvolvableContext {
		public TerminalNode EVOLVE() { return getToken(PrismParser.EVOLVE, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode PARAM() { return getToken(PrismParser.PARAM, 0); }
		public EvolveBoolContext(EvolvableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterEvolveBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitEvolveBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitEvolveBool(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EvolveRangeContext extends EvolvableContext {
		public Token CONSTANTTYPE;
		public TerminalNode EVOLVE() { return getToken(PrismParser.EVOLVE, 0); }
		public TerminalNode CONSTANTTYPE() { return getToken(PrismParser.CONSTANTTYPE, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public BoundsRangeContext boundsRange() {
			return getRuleContext(BoundsRangeContext.class,0);
		}
		public TerminalNode PARAM() { return getToken(PrismParser.PARAM, 0); }
		public EvolveRangeContext(EvolvableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterEvolveRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitEvolveRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitEvolveRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EvolveDistributionContext extends EvolvableContext {
		public Token cardinality;
		public TerminalNode EVOLVE() { return getToken(PrismParser.EVOLVE, 0); }
		public TerminalNode DISTRIBUTION() { return getToken(PrismParser.DISTRIBUTION, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode INT() { return getToken(PrismParser.INT, 0); }
		public TerminalNode PARAM() { return getToken(PrismParser.PARAM, 0); }
		public List<BoundsRangeContext> boundsRange() {
			return getRuleContexts(BoundsRangeContext.class);
		}
		public BoundsRangeContext boundsRange(int i) {
			return getRuleContext(BoundsRangeContext.class,i);
		}
		public EvolveDistributionContext(EvolvableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterEvolveDistribution(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitEvolveDistribution(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitEvolveDistribution(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EvolvableContext evolvable() throws RecognitionException {
		EvolvableContext _localctx = new EvolvableContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_evolvable);
		int _la;
		try {
			setState(243);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				_localctx = new EvolveRangeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(174);
				match(EVOLVE);
				setState(176);
				_la = _input.LA(1);
				if (_la==PARAM) {
					{
					setState(175);
					match(PARAM);
					}
				}

				setState(178);
				((EvolveRangeContext)_localctx).CONSTANTTYPE = match(CONSTANTTYPE);
				setState(179);
				variable();
				setState(180);
				match(T__2);
				setState(181);
				boundsRange((((EvolveRangeContext)_localctx).CONSTANTTYPE!=null?((EvolveRangeContext)_localctx).CONSTANTTYPE.getText():null));
				setState(182);
				match(T__3);
				setState(183);
				match(T__8);
				}
				break;
			case 2:
				_localctx = new EvolveDiscreteContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(185);
				match(EVOLVE);
				setState(187);
				_la = _input.LA(1);
				if (_la==PARAM) {
					{
					setState(186);
					match(PARAM);
					}
				}

				setState(189);
				((EvolveDiscreteContext)_localctx).CONSTANTTYPE = match(CONSTANTTYPE);
				setState(190);
				variable();
				setState(191);
				match(T__13);
				setState(192);
				boundsDiscrete((((EvolveDiscreteContext)_localctx).CONSTANTTYPE!=null?((EvolveDiscreteContext)_localctx).CONSTANTTYPE.getText():null));
				setState(193);
				match(T__14);
				setState(194);
				match(T__8);
				}
				break;
			case 3:
				_localctx = new EvolveDistributionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(196);
				match(EVOLVE);
				setState(198);
				_la = _input.LA(1);
				if (_la==PARAM) {
					{
					setState(197);
					match(PARAM);
					}
				}

				setState(200);
				match(DISTRIBUTION);
				setState(201);
				variable();
				setState(202);
				match(T__2);
				setState(203);
				((EvolveDistributionContext)_localctx).cardinality = match(INT);
				setState(204);
				match(T__3);
				setState(211);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(205);
					match(T__2);
					setState(206);
					boundsRange("double");
					setState(207);
					match(T__3);
					}
					}
					setState(213);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(214);
				match(T__8);
				}
				break;
			case 4:
				_localctx = new EvolveModuleContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(216);
				match(EVOLVE);
				setState(217);
				match(T__0);
				setState(218);
				((EvolveModuleContext)_localctx).name = match(ID);
				setState(220);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(219);
					boundsRange("int");
					}
					break;
				}
				setState(225);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ID) {
					{
					{
					setState(222);
					varDeclaration();
					}
					}
					setState(227);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(229); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(228);
					command();
					}
					}
					setState(231); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__2 );
				setState(233);
				match(T__1);
				}
				break;
			case 5:
				_localctx = new EvolveBoolContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(235);
				match(EVOLVE);
				setState(237);
				_la = _input.LA(1);
				if (_la==PARAM) {
					{
					setState(236);
					match(PARAM);
					}
				}

				setState(239);
				match(T__12);
				setState(240);
				variable();
				setState(241);
				match(T__8);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclarationContext extends ParserRuleContext {
		public VarDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclaration; }
	 
		public VarDeclarationContext() { }
		public void copyFrom(VarDeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class IntVarDeclarationContext extends VarDeclarationContext {
		public VariableContext name;
		public IntOrVarContext lowerBound;
		public IntOrVarContext upperBound;
		public IntOrVarContext initValue;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public List<IntOrVarContext> intOrVar() {
			return getRuleContexts(IntOrVarContext.class);
		}
		public IntOrVarContext intOrVar(int i) {
			return getRuleContext(IntOrVarContext.class,i);
		}
		public IntVarDeclarationContext(VarDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterIntVarDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitIntVarDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitIntVarDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolVarDeclarationContext extends VarDeclarationContext {
		public VariableContext name;
		public Token initValue;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode BOOLEAN() { return getToken(PrismParser.BOOLEAN, 0); }
		public BoolVarDeclarationContext(VarDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterBoolVarDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitBoolVarDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitBoolVarDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclarationContext varDeclaration() throws RecognitionException {
		VarDeclarationContext _localctx = new VarDeclarationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_varDeclaration);
		int _la;
		try {
			setState(267);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				_localctx = new BoolVarDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(245);
				((BoolVarDeclarationContext)_localctx).name = variable();
				setState(246);
				match(T__7);
				setState(247);
				match(T__12);
				setState(250);
				_la = _input.LA(1);
				if (_la==T__15) {
					{
					setState(248);
					match(T__15);
					setState(249);
					((BoolVarDeclarationContext)_localctx).initValue = match(BOOLEAN);
					}
				}

				setState(252);
				match(T__8);
				}
				break;
			case 2:
				_localctx = new IntVarDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(254);
				((IntVarDeclarationContext)_localctx).name = variable();
				setState(255);
				match(T__7);
				setState(256);
				match(T__2);
				setState(257);
				((IntVarDeclarationContext)_localctx).lowerBound = intOrVar();
				setState(258);
				match(T__16);
				setState(259);
				((IntVarDeclarationContext)_localctx).upperBound = intOrVar();
				setState(260);
				match(T__3);
				setState(263);
				_la = _input.LA(1);
				if (_la==T__15) {
					{
					setState(261);
					match(T__15);
					setState(262);
					((IntVarDeclarationContext)_localctx).initValue = intOrVar();
					}
				}

				setState(265);
				match(T__8);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoundsRangeContext extends ParserRuleContext {
		public String str;
		public Token minValue;
		public Token maxValue;
		public List<TerminalNode> DOUBLE() { return getTokens(PrismParser.DOUBLE); }
		public TerminalNode DOUBLE(int i) {
			return getToken(PrismParser.DOUBLE, i);
		}
		public List<TerminalNode> INT() { return getTokens(PrismParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(PrismParser.INT, i);
		}
		public BoundsRangeContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public BoundsRangeContext(ParserRuleContext parent, int invokingState, String str) {
			super(parent, invokingState);
			this.str = str;
		}
		@Override public int getRuleIndex() { return RULE_boundsRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterBoundsRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitBoundsRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitBoundsRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoundsRangeContext boundsRange(String str) throws RecognitionException {
		BoundsRangeContext _localctx = new BoundsRangeContext(_ctx, getState(), str);
		enterRule(_localctx, 24, RULE_boundsRange);
		try {
			setState(276);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(269);
				if (!(_localctx.str.equals("double"))) throw new FailedPredicateException(this, "$str.equals(\"double\")");
				setState(270);
				((BoundsRangeContext)_localctx).minValue = match(DOUBLE);
				setState(271);
				match(T__16);
				setState(272);
				((BoundsRangeContext)_localctx).maxValue = match(DOUBLE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(273);
				((BoundsRangeContext)_localctx).minValue = match(INT);
				setState(274);
				match(T__16);
				setState(275);
				((BoundsRangeContext)_localctx).maxValue = match(INT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoundsDiscreteContext extends ParserRuleContext {
		public String str;
		public DiscreteOptionDoubleContext discreteOptionDouble() {
			return getRuleContext(DiscreteOptionDoubleContext.class,0);
		}
		public DiscreteOptionIntContext discreteOptionInt() {
			return getRuleContext(DiscreteOptionIntContext.class,0);
		}
		public BoundsDiscreteContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public BoundsDiscreteContext(ParserRuleContext parent, int invokingState, String str) {
			super(parent, invokingState);
			this.str = str;
		}
		@Override public int getRuleIndex() { return RULE_boundsDiscrete; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterBoundsDiscrete(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitBoundsDiscrete(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitBoundsDiscrete(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoundsDiscreteContext boundsDiscrete(String str) throws RecognitionException {
		BoundsDiscreteContext _localctx = new BoundsDiscreteContext(_ctx, getState(), str);
		enterRule(_localctx, 26, RULE_boundsDiscrete);
		try {
			setState(281);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(278);
				if (!( _localctx.str.equals("double"))) throw new FailedPredicateException(this, " $str.equals(\"double\")");
				setState(279);
				discreteOptionDouble(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(280);
				discreteOptionInt(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DiscreteOptionDoubleContext extends ParserRuleContext {
		public DiscreteOptionDoubleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_discreteOptionDouble; }
	 
		public DiscreteOptionDoubleContext() { }
		public void copyFrom(DiscreteOptionDoubleContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DiscreteOptionDoubleMultiContext extends DiscreteOptionDoubleContext {
		public List<DiscreteOptionDoubleContext> discreteOptionDouble() {
			return getRuleContexts(DiscreteOptionDoubleContext.class);
		}
		public DiscreteOptionDoubleContext discreteOptionDouble(int i) {
			return getRuleContext(DiscreteOptionDoubleContext.class,i);
		}
		public DiscreteOptionDoubleMultiContext(DiscreteOptionDoubleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterDiscreteOptionDoubleMulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitDiscreteOptionDoubleMulti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitDiscreteOptionDoubleMulti(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DiscreteOptionDoubleSingleContext extends DiscreteOptionDoubleContext {
		public Token value;
		public TerminalNode DOUBLE() { return getToken(PrismParser.DOUBLE, 0); }
		public DiscreteOptionDoubleSingleContext(DiscreteOptionDoubleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterDiscreteOptionDoubleSingle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitDiscreteOptionDoubleSingle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitDiscreteOptionDoubleSingle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DiscreteOptionDoubleContext discreteOptionDouble() throws RecognitionException {
		return discreteOptionDouble(0);
	}

	private DiscreteOptionDoubleContext discreteOptionDouble(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		DiscreteOptionDoubleContext _localctx = new DiscreteOptionDoubleContext(_ctx, _parentState);
		DiscreteOptionDoubleContext _prevctx = _localctx;
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_discreteOptionDouble, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new DiscreteOptionDoubleSingleContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(284);
			((DiscreteOptionDoubleSingleContext)_localctx).value = match(DOUBLE);
			}
			_ctx.stop = _input.LT(-1);
			setState(291);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new DiscreteOptionDoubleMultiContext(new DiscreteOptionDoubleContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_discreteOptionDouble);
					setState(286);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(287);
					match(T__4);
					setState(288);
					discreteOptionDouble(2);
					}
					} 
				}
				setState(293);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class DiscreteOptionIntContext extends ParserRuleContext {
		public DiscreteOptionIntContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_discreteOptionInt; }
	 
		public DiscreteOptionIntContext() { }
		public void copyFrom(DiscreteOptionIntContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DiscreteOptionIntSingleContext extends DiscreteOptionIntContext {
		public Token value;
		public TerminalNode INT() { return getToken(PrismParser.INT, 0); }
		public DiscreteOptionIntSingleContext(DiscreteOptionIntContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterDiscreteOptionIntSingle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitDiscreteOptionIntSingle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitDiscreteOptionIntSingle(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DiscreteOptionIntMultiContext extends DiscreteOptionIntContext {
		public List<DiscreteOptionIntContext> discreteOptionInt() {
			return getRuleContexts(DiscreteOptionIntContext.class);
		}
		public DiscreteOptionIntContext discreteOptionInt(int i) {
			return getRuleContext(DiscreteOptionIntContext.class,i);
		}
		public DiscreteOptionIntMultiContext(DiscreteOptionIntContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterDiscreteOptionIntMulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitDiscreteOptionIntMulti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitDiscreteOptionIntMulti(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DiscreteOptionIntContext discreteOptionInt() throws RecognitionException {
		return discreteOptionInt(0);
	}

	private DiscreteOptionIntContext discreteOptionInt(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		DiscreteOptionIntContext _localctx = new DiscreteOptionIntContext(_ctx, _parentState);
		DiscreteOptionIntContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_discreteOptionInt, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new DiscreteOptionIntSingleContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(295);
			((DiscreteOptionIntSingleContext)_localctx).value = match(INT);
			}
			_ctx.stop = _input.LT(-1);
			setState(302);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new DiscreteOptionIntMultiContext(new DiscreteOptionIntContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_discreteOptionInt);
					setState(297);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(298);
					match(T__4);
					setState(299);
					discreteOptionInt(2);
					}
					} 
				}
				setState(304);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CommandContext extends ParserRuleContext {
		public Token name;
		public GuardContext guard() {
			return getRuleContext(GuardContext.class,0);
		}
		public TransitionContext transition() {
			return getRuleContext(TransitionContext.class,0);
		}
		public TerminalNode ID() { return getToken(PrismParser.ID, 0); }
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_command);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			match(T__2);
			setState(307);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(306);
				((CommandContext)_localctx).name = match(ID);
				}
			}

			setState(309);
			match(T__3);
			setState(310);
			guard(0);
			setState(311);
			match(T__17);
			setState(312);
			transition(0);
			setState(313);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GuardContext extends ParserRuleContext {
		public GuardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_guard; }
	 
		public GuardContext() { }
		public void copyFrom(GuardContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class GuardParenContext extends GuardContext {
		public GuardContext guard() {
			return getRuleContext(GuardContext.class,0);
		}
		public GuardParenContext(GuardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterGuardParen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitGuardParen(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitGuardParen(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GuardMultiContext extends GuardContext {
		public List<GuardContext> guard() {
			return getRuleContexts(GuardContext.class);
		}
		public GuardContext guard(int i) {
			return getRuleContext(GuardContext.class,i);
		}
		public LogicalOpContext logicalOp() {
			return getRuleContext(LogicalOpContext.class,0);
		}
		public GuardMultiContext(GuardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterGuardMulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitGuardMulti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitGuardMulti(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GuardNotContext extends GuardContext {
		public GuardContext guard() {
			return getRuleContext(GuardContext.class,0);
		}
		public GuardNotContext(GuardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterGuardNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitGuardNot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitGuardNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GuardStringContext extends GuardContext {
		public TerminalNode ID() { return getToken(PrismParser.ID, 0); }
		public GuardStringContext(GuardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterGuardString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitGuardString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitGuardString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GuardExpressionContext extends GuardContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public GuardExpressionContext(GuardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterGuardExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitGuardExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitGuardExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GuardBoolContext extends GuardContext {
		public TerminalNode BOOLEAN() { return getToken(PrismParser.BOOLEAN, 0); }
		public GuardBoolContext(GuardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterGuardBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitGuardBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitGuardBool(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GuardContext guard() throws RecognitionException {
		return guard(0);
	}

	private GuardContext guard(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		GuardContext _localctx = new GuardContext(_ctx, _parentState);
		GuardContext _prevctx = _localctx;
		int _startState = 34;
		enterRecursionRule(_localctx, 34, RULE_guard, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				_localctx = new GuardNotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(316);
				match(T__18);
				setState(317);
				guard(1);
				}
				break;
			case 2:
				{
				_localctx = new GuardBoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(318);
				match(BOOLEAN);
				}
				break;
			case 3:
				{
				_localctx = new GuardStringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(319);
				match(ID);
				}
				break;
			case 4:
				{
				_localctx = new GuardExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(320);
				variable();
				setState(321);
				operator();
				setState(322);
				expression(0);
				}
				break;
			case 5:
				{
				_localctx = new GuardParenContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(324);
				match(T__9);
				setState(325);
				guard(0);
				setState(326);
				match(T__10);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(336);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new GuardMultiContext(new GuardContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_guard);
					setState(330);
					if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
					setState(331);
					logicalOp();
					setState(332);
					guard(4);
					}
					} 
				}
				setState(338);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TransitionContext extends ParserRuleContext {
		public TransitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transition; }
	 
		public TransitionContext() { }
		public void copyFrom(TransitionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TransitionEntryContext extends TransitionContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TransitionEntryContext(TransitionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterTransitionEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitTransitionEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitTransitionEntry(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TransitionMultiContext extends TransitionContext {
		public List<TransitionContext> transition() {
			return getRuleContexts(TransitionContext.class);
		}
		public TransitionContext transition(int i) {
			return getRuleContext(TransitionContext.class,i);
		}
		public TransitionMultiContext(TransitionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterTransitionMulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitTransitionMulti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitTransitionMulti(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransitionContext transition() throws RecognitionException {
		return transition(0);
	}

	private TransitionContext transition(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TransitionContext _localctx = new TransitionContext(_ctx, _parentState);
		TransitionContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_transition, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new TransitionEntryContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(343);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(340);
				expression(0);
				setState(341);
				match(T__7);
				}
				break;
			}
			setState(345);
			statement(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(352);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TransitionMultiContext(new TransitionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_transition);
					setState(347);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(348);
					match(T__19);
					setState(349);
					transition(2);
					}
					} 
				}
				setState(354);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class StatementMultiContext extends StatementContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public StatementMultiContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterStatementMulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitStatementMulti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitStatementMulti(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StatementMainContext extends StatementContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(PrismParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementMainContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterStatementMain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitStatementMain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitStatementMain(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StatementBoolContext extends StatementContext {
		public TerminalNode BOOLEAN() { return getToken(PrismParser.BOOLEAN, 0); }
		public StatementBoolContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterStatementBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitStatementBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitStatementBool(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		return statement(0);
	}

	private StatementContext statement(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		StatementContext _localctx = new StatementContext(_ctx, _parentState);
		StatementContext _prevctx = _localctx;
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_statement, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			switch (_input.LA(1)) {
			case BOOLEAN:
				{
				_localctx = new StatementBoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(356);
				match(BOOLEAN);
				}
				break;
			case T__9:
				{
				_localctx = new StatementMainContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(357);
				match(T__9);
				setState(358);
				variable();
				setState(359);
				match(T__20);
				setState(360);
				match(ASSIGN);
				setState(361);
				expression(0);
				setState(362);
				match(T__10);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(371);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new StatementMultiContext(new StatementContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_statement);
					setState(366);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(367);
					match(T__21);
					setState(368);
					statement(2);
					}
					} 
				}
				setState(373);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public Token name;
		public TerminalNode ID() { return getToken(PrismParser.ID, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374);
			((VariableContext)_localctx).name = match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExpressionValueContext extends ExpressionContext {
		public Token value;
		public TerminalNode INT() { return getToken(PrismParser.INT, 0); }
		public TerminalNode DOUBLE() { return getToken(PrismParser.DOUBLE, 0); }
		public TerminalNode BOOLEAN() { return getToken(PrismParser.BOOLEAN, 0); }
		public ExpressionValueContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterExpressionValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitExpressionValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitExpressionValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionFunctionContext extends ExpressionContext {
		public FunctionContext function() {
			return getRuleContext(FunctionContext.class,0);
		}
		public ExpressionFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterExpressionFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitExpressionFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitExpressionFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionMultiContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public ExpressionMultiContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterExpressionMulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitExpressionMulti(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitExpressionMulti(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionParenContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionParenContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterExpressionParen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitExpressionParen(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitExpressionParen(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionVariableContext extends ExpressionContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public ExpressionVariableContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterExpressionVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitExpressionVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitExpressionVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 42;
		enterRecursionRule(_localctx, 42, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case INT:
			case DOUBLE:
				{
				_localctx = new ExpressionValueContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(377);
				((ExpressionValueContext)_localctx).value = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << INT) | (1L << DOUBLE))) != 0)) ) {
					((ExpressionValueContext)_localctx).value = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			case ID:
				{
				_localctx = new ExpressionVariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(378);
				variable();
				}
				break;
			case T__9:
				{
				_localctx = new ExpressionParenContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(379);
				match(T__9);
				setState(380);
				expression(0);
				setState(381);
				match(T__10);
				}
				break;
			case FUNCTIONIDENTIFIER:
				{
				_localctx = new ExpressionFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(383);
				function();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(392);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExpressionMultiContext(new ExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_expression);
					setState(386);
					if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
					setState(387);
					operator();
					setState(388);
					expression(6);
					}
					} 
				}
				setState(394);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ModelTypeContext extends ParserRuleContext {
		public Token value;
		public TerminalNode DTMC() { return getToken(PrismParser.DTMC, 0); }
		public TerminalNode CTMC() { return getToken(PrismParser.CTMC, 0); }
		public TerminalNode MDP() { return getToken(PrismParser.MDP, 0); }
		public ModelTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterModelType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitModelType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitModelType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelTypeContext modelType() throws RecognitionException {
		ModelTypeContext _localctx = new ModelTypeContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_modelType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			((ModelTypeContext)_localctx).value = _input.LT(1);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DTMC) | (1L << CTMC) | (1L << MDP))) != 0)) ) {
				((ModelTypeContext)_localctx).value = (Token)_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonOpContext extends ParserRuleContext {
		public TerminalNode ASSIGN() { return getToken(PrismParser.ASSIGN, 0); }
		public ComparisonOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterComparisonOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitComparisonOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitComparisonOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonOpContext comparisonOp() throws RecognitionException {
		ComparisonOpContext _localctx = new ComparisonOpContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_comparisonOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << ASSIGN))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalOpContext extends ParserRuleContext {
		public LogicalOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterLogicalOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitLogicalOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitLogicalOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalOpContext logicalOp() throws RecognitionException {
		LogicalOpContext _localctx = new LogicalOpContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_logicalOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__18) | (1L << T__21) | (1L << T__27))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumericalOpContext extends ParserRuleContext {
		public NumericalOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericalOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterNumericalOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitNumericalOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitNumericalOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericalOpContext numericalOp() throws RecognitionException {
		NumericalOpContext _localctx = new NumericalOpContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_numericalOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__19) | (1L << T__28) | (1L << T__29) | (1L << T__30))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntOrVarContext extends ParserRuleContext {
		public IntOrVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intOrVar; }
	 
		public IntOrVarContext() { }
		public void copyFrom(IntOrVarContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class IntOrVarIntContext extends IntOrVarContext {
		public TerminalNode INT() { return getToken(PrismParser.INT, 0); }
		public IntOrVarIntContext(IntOrVarContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterIntOrVarInt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitIntOrVarInt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitIntOrVarInt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntOrVarVarContext extends IntOrVarContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public IntOrVarVarContext(IntOrVarContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterIntOrVarVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitIntOrVarVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitIntOrVarVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntOrVarContext intOrVar() throws RecognitionException {
		IntOrVarContext _localctx = new IntOrVarContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_intOrVar);
		try {
			setState(405);
			switch (_input.LA(1)) {
			case INT:
				_localctx = new IntOrVarIntContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(403);
				match(INT);
				}
				break;
			case ID:
				_localctx = new IntOrVarVarContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(404);
				variable();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public NumericalOpContext numericalOp() {
			return getRuleContext(NumericalOpContext.class,0);
		}
		public ComparisonOpContext comparisonOp() {
			return getRuleContext(ComparisonOpContext.class,0);
		}
		public LogicalOpContext logicalOp() {
			return getRuleContext(LogicalOpContext.class,0);
		}
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrismListener ) ((PrismListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrismVisitor ) return ((PrismVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_operator);
		try {
			setState(414);
			switch (_input.LA(1)) {
			case T__19:
			case T__28:
			case T__29:
			case T__30:
				enterOuterAlt(_localctx, 1);
				{
				setState(407);
				numericalOp();
				}
				break;
			case T__22:
			case T__23:
			case T__24:
			case T__25:
			case T__26:
			case ASSIGN:
				enterOuterAlt(_localctx, 2);
				{
				setState(408);
				comparisonOp();
				}
				break;
			case T__18:
			case T__21:
			case T__27:
				enterOuterAlt(_localctx, 3);
				{
				setState(409);
				logicalOp();
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 4);
				{
				setState(410);
				match(T__31);
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 5);
				{
				setState(411);
				match(T__32);
				}
				break;
			case T__33:
				enterOuterAlt(_localctx, 6);
				{
				setState(412);
				match(T__33);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 7);
				{
				setState(413);
				match(T__7);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return boundsRange_sempred((BoundsRangeContext)_localctx, predIndex);
		case 13:
			return boundsDiscrete_sempred((BoundsDiscreteContext)_localctx, predIndex);
		case 14:
			return discreteOptionDouble_sempred((DiscreteOptionDoubleContext)_localctx, predIndex);
		case 15:
			return discreteOptionInt_sempred((DiscreteOptionIntContext)_localctx, predIndex);
		case 17:
			return guard_sempred((GuardContext)_localctx, predIndex);
		case 18:
			return transition_sempred((TransitionContext)_localctx, predIndex);
		case 19:
			return statement_sempred((StatementContext)_localctx, predIndex);
		case 21:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean boundsRange_sempred(BoundsRangeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return _localctx.str.equals("double");
		}
		return true;
	}
	private boolean boundsDiscrete_sempred(BoundsDiscreteContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return  _localctx.str.equals("double");
		}
		return true;
	}
	private boolean discreteOptionDouble_sempred(DiscreteOptionDoubleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean discreteOptionInt_sempred(DiscreteOptionIntContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean guard_sempred(GuardContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean transition_sempred(TransitionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean statement_sempred(StatementContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 7:
			return precpred(_ctx, 5);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\66\u01a3\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7"+
		"\2B\n\2\f\2\16\2E\13\2\3\2\3\2\3\3\3\3\3\3\7\3L\n\3\f\3\16\3O\13\3\3\3"+
		"\6\3R\n\3\r\3\16\3S\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3a\n"+
		"\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4m\n\4\3\5\3\5\3\5\7\5r\n"+
		"\5\f\5\16\5u\13\5\3\5\3\5\3\6\3\6\5\6{\n\6\3\6\5\6~\n\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u008b\n\7\3\b\3\b\3\b\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\3\t\5\t\u0097\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00af"+
		"\n\13\3\f\3\f\5\f\u00b3\n\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00be"+
		"\n\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00c9\n\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\7\f\u00d4\n\f\f\f\16\f\u00d7\13\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\5\f\u00df\n\f\3\f\7\f\u00e2\n\f\f\f\16\f\u00e5\13\f\3\f\6\f\u00e8"+
		"\n\f\r\f\16\f\u00e9\3\f\3\f\3\f\3\f\5\f\u00f0\n\f\3\f\3\f\3\f\3\f\5\f"+
		"\u00f6\n\f\3\r\3\r\3\r\3\r\3\r\5\r\u00fd\n\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\5\r\u010a\n\r\3\r\3\r\5\r\u010e\n\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\5\16\u0117\n\16\3\17\3\17\3\17\5\17\u011c\n\17\3"+
		"\20\3\20\3\20\3\20\3\20\3\20\7\20\u0124\n\20\f\20\16\20\u0127\13\20\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\7\21\u012f\n\21\f\21\16\21\u0132\13\21\3"+
		"\22\3\22\5\22\u0136\n\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u014b\n\23\3\23"+
		"\3\23\3\23\3\23\7\23\u0151\n\23\f\23\16\23\u0154\13\23\3\24\3\24\3\24"+
		"\3\24\5\24\u015a\n\24\3\24\3\24\3\24\3\24\3\24\7\24\u0161\n\24\f\24\16"+
		"\24\u0164\13\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u016f"+
		"\n\25\3\25\3\25\3\25\7\25\u0174\n\25\f\25\16\25\u0177\13\25\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0183\n\27\3\27\3\27\3\27"+
		"\3\27\7\27\u0189\n\27\f\27\16\27\u018c\13\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\33\3\33\3\34\3\34\5\34\u0198\n\34\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\5\35\u01a1\n\35\3\35\2\b\36 $&(,\36\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\36 \"$&(*,.\60\62\64\668\2\7\4\2\60\60\63\64\3\2-/\4\2\31\35%%"+
		"\5\2\25\25\30\30\36\36\4\2\26\26\37!\u01bf\2:\3\2\2\2\4`\3\2\2\2\6l\3"+
		"\2\2\2\bn\3\2\2\2\n}\3\2\2\2\f\u008a\3\2\2\2\16\u008c\3\2\2\2\20\u0096"+
		"\3\2\2\2\22\u0098\3\2\2\2\24\u00ae\3\2\2\2\26\u00f5\3\2\2\2\30\u010d\3"+
		"\2\2\2\32\u0116\3\2\2\2\34\u011b\3\2\2\2\36\u011d\3\2\2\2 \u0128\3\2\2"+
		"\2\"\u0133\3\2\2\2$\u014a\3\2\2\2&\u0155\3\2\2\2(\u016e\3\2\2\2*\u0178"+
		"\3\2\2\2,\u0182\3\2\2\2.\u018d\3\2\2\2\60\u018f\3\2\2\2\62\u0191\3\2\2"+
		"\2\64\u0193\3\2\2\2\66\u0197\3\2\2\28\u01a0\3\2\2\2:C\5.\30\2;B\5\4\3"+
		"\2<B\5\b\5\2=B\5\24\13\2>B\5\22\n\2?B\5\26\f\2@B\7,\2\2A;\3\2\2\2A<\3"+
		"\2\2\2A=\3\2\2\2A>\3\2\2\2A?\3\2\2\2A@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3"+
		"\2\2\2DF\3\2\2\2EC\3\2\2\2FG\7\2\2\3G\3\3\2\2\2HI\7\3\2\2IM\7\62\2\2J"+
		"L\5\30\r\2KJ\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2NQ\3\2\2\2OM\3\2\2\2"+
		"PR\5\"\22\2QP\3\2\2\2RS\3\2\2\2SQ\3\2\2\2ST\3\2\2\2TU\3\2\2\2UV\7\4\2"+
		"\2Va\3\2\2\2WX\7\3\2\2XY\7\62\2\2YZ\7%\2\2Z[\7\62\2\2[\\\7\5\2\2\\]\5"+
		"\6\4\2]^\7\6\2\2^_\7\4\2\2_a\3\2\2\2`H\3\2\2\2`W\3\2\2\2a\5\3\2\2\2bc"+
		"\5*\26\2cd\7%\2\2de\5*\26\2em\3\2\2\2fg\5*\26\2gh\7%\2\2hi\5*\26\2ij\7"+
		"\7\2\2jk\5\6\4\2km\3\2\2\2lb\3\2\2\2lf\3\2\2\2m\7\3\2\2\2no\7\b\2\2os"+
		"\7\65\2\2pr\5\n\6\2qp\3\2\2\2ru\3\2\2\2sq\3\2\2\2st\3\2\2\2tv\3\2\2\2"+
		"us\3\2\2\2vw\7\t\2\2w\t\3\2\2\2xz\7\5\2\2y{\5*\26\2zy\3\2\2\2z{\3\2\2"+
		"\2{|\3\2\2\2|~\7\6\2\2}x\3\2\2\2}~\3\2\2\2~\177\3\2\2\2\177\u0080\5\f"+
		"\7\2\u0080\u0081\7\n\2\2\u0081\u0082\5,\27\2\u0082\u0083\7\13\2\2\u0083"+
		"\13\3\2\2\2\u0084\u008b\5,\27\2\u0085\u008b\7\60\2\2\u0086\u0087\5*\26"+
		"\2\u0087\u0088\58\35\2\u0088\u0089\7\60\2\2\u0089\u008b\3\2\2\2\u008a"+
		"\u0084\3\2\2\2\u008a\u0085\3\2\2\2\u008a\u0086\3\2\2\2\u008b\r\3\2\2\2"+
		"\u008c\u008d\7*\2\2\u008d\u008e\7\f\2\2\u008e\u008f\5\20\t\2\u008f\u0090"+
		"\7\r\2\2\u0090\17\3\2\2\2\u0091\u0097\5,\27\2\u0092\u0093\5,\27\2\u0093"+
		"\u0094\7\7\2\2\u0094\u0095\5\20\t\2\u0095\u0097\3\2\2\2\u0096\u0091\3"+
		"\2\2\2\u0096\u0092\3\2\2\2\u0097\21\3\2\2\2\u0098\u0099\7\16\2\2\u0099"+
		"\u009a\5*\26\2\u009a\u009b\7%\2\2\u009b\u009c\5,\27\2\u009c\u009d\7\13"+
		"\2\2\u009d\23\3\2\2\2\u009e\u009f\7\'\2\2\u009f\u00a0\7+\2\2\u00a0\u00a1"+
		"\5*\26\2\u00a1\u00a2\7%\2\2\u00a2\u00a3\5,\27\2\u00a3\u00a4\3\2\2\2\u00a4"+
		"\u00a5\7\13\2\2\u00a5\u00af\3\2\2\2\u00a6\u00a7\7\'\2\2\u00a7\u00a8\7"+
		"\17\2\2\u00a8\u00a9\5*\26\2\u00a9\u00aa\7%\2\2\u00aa\u00ab\5,\27\2\u00ab"+
		"\u00ac\3\2\2\2\u00ac\u00ad\7\13\2\2\u00ad\u00af\3\2\2\2\u00ae\u009e\3"+
		"\2\2\2\u00ae\u00a6\3\2\2\2\u00af\25\3\2\2\2\u00b0\u00b2\7&\2\2\u00b1\u00b3"+
		"\7)\2\2\u00b2\u00b1\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4"+
		"\u00b5\7+\2\2\u00b5\u00b6\5*\26\2\u00b6\u00b7\7\5\2\2\u00b7\u00b8\5\32"+
		"\16\2\u00b8\u00b9\7\6\2\2\u00b9\u00ba\7\13\2\2\u00ba\u00f6\3\2\2\2\u00bb"+
		"\u00bd\7&\2\2\u00bc\u00be\7)\2\2\u00bd\u00bc\3\2\2\2\u00bd\u00be\3\2\2"+
		"\2\u00be\u00bf\3\2\2\2\u00bf\u00c0\7+\2\2\u00c0\u00c1\5*\26\2\u00c1\u00c2"+
		"\7\20\2\2\u00c2\u00c3\5\34\17\2\u00c3\u00c4\7\21\2\2\u00c4\u00c5\7\13"+
		"\2\2\u00c5\u00f6\3\2\2\2\u00c6\u00c8\7&\2\2\u00c7\u00c9\7)\2\2\u00c8\u00c7"+
		"\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\7(\2\2\u00cb"+
		"\u00cc\5*\26\2\u00cc\u00cd\7\5\2\2\u00cd\u00ce\7\63\2\2\u00ce\u00d5\7"+
		"\6\2\2\u00cf\u00d0\7\5\2\2\u00d0\u00d1\5\32\16\2\u00d1\u00d2\7\6\2\2\u00d2"+
		"\u00d4\3\2\2\2\u00d3\u00cf\3\2\2\2\u00d4\u00d7\3\2\2\2\u00d5\u00d3\3\2"+
		"\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d8\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d8"+
		"\u00d9\7\13\2\2\u00d9\u00f6\3\2\2\2\u00da\u00db\7&\2\2\u00db\u00dc\7\3"+
		"\2\2\u00dc\u00de\7\62\2\2\u00dd\u00df\5\32\16\2\u00de\u00dd\3\2\2\2\u00de"+
		"\u00df\3\2\2\2\u00df\u00e3\3\2\2\2\u00e0\u00e2\5\30\r\2\u00e1\u00e0\3"+
		"\2\2\2\u00e2\u00e5\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4"+
		"\u00e7\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\u00e8\5\"\22\2\u00e7\u00e6\3"+
		"\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea"+
		"\u00eb\3\2\2\2\u00eb\u00ec\7\4\2\2\u00ec\u00f6\3\2\2\2\u00ed\u00ef\7&"+
		"\2\2\u00ee\u00f0\7)\2\2\u00ef\u00ee\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0"+
		"\u00f1\3\2\2\2\u00f1\u00f2\7\17\2\2\u00f2\u00f3\5*\26\2\u00f3\u00f4\7"+
		"\13\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00b0\3\2\2\2\u00f5\u00bb\3\2\2\2\u00f5"+
		"\u00c6\3\2\2\2\u00f5\u00da\3\2\2\2\u00f5\u00ed\3\2\2\2\u00f6\27\3\2\2"+
		"\2\u00f7\u00f8\5*\26\2\u00f8\u00f9\7\n\2\2\u00f9\u00fc\7\17\2\2\u00fa"+
		"\u00fb\7\22\2\2\u00fb\u00fd\7\60\2\2\u00fc\u00fa\3\2\2\2\u00fc\u00fd\3"+
		"\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00ff\7\13\2\2\u00ff\u010e\3\2\2\2\u0100"+
		"\u0101\5*\26\2\u0101\u0102\7\n\2\2\u0102\u0103\7\5\2\2\u0103\u0104\5\66"+
		"\34\2\u0104\u0105\7\23\2\2\u0105\u0106\5\66\34\2\u0106\u0109\7\6\2\2\u0107"+
		"\u0108\7\22\2\2\u0108\u010a\5\66\34\2\u0109\u0107\3\2\2\2\u0109\u010a"+
		"\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010c\7\13\2\2\u010c\u010e\3\2\2\2"+
		"\u010d\u00f7\3\2\2\2\u010d\u0100\3\2\2\2\u010e\31\3\2\2\2\u010f\u0110"+
		"\6\16\2\3\u0110\u0111\7\64\2\2\u0111\u0112\7\23\2\2\u0112\u0117\7\64\2"+
		"\2\u0113\u0114\7\63\2\2\u0114\u0115\7\23\2\2\u0115\u0117\7\63\2\2\u0116"+
		"\u010f\3\2\2\2\u0116\u0113\3\2\2\2\u0117\33\3\2\2\2\u0118\u0119\6\17\3"+
		"\3\u0119\u011c\5\36\20\2\u011a\u011c\5 \21\2\u011b\u0118\3\2\2\2\u011b"+
		"\u011a\3\2\2\2\u011c\35\3\2\2\2\u011d\u011e\b\20\1\2\u011e\u011f\7\64"+
		"\2\2\u011f\u0125\3\2\2\2\u0120\u0121\f\3\2\2\u0121\u0122\7\7\2\2\u0122"+
		"\u0124\5\36\20\4\u0123\u0120\3\2\2\2\u0124\u0127\3\2\2\2\u0125\u0123\3"+
		"\2\2\2\u0125\u0126\3\2\2\2\u0126\37\3\2\2\2\u0127\u0125\3\2\2\2\u0128"+
		"\u0129\b\21\1\2\u0129\u012a\7\63\2\2\u012a\u0130\3\2\2\2\u012b\u012c\f"+
		"\3\2\2\u012c\u012d\7\7\2\2\u012d\u012f\5 \21\4\u012e\u012b\3\2\2\2\u012f"+
		"\u0132\3\2\2\2\u0130\u012e\3\2\2\2\u0130\u0131\3\2\2\2\u0131!\3\2\2\2"+
		"\u0132\u0130\3\2\2\2\u0133\u0135\7\5\2\2\u0134\u0136\7\62\2\2\u0135\u0134"+
		"\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u0137\3\2\2\2\u0137\u0138\7\6\2\2\u0138"+
		"\u0139\5$\23\2\u0139\u013a\7\24\2\2\u013a\u013b\5&\24\2\u013b\u013c\7"+
		"\13\2\2\u013c#\3\2\2\2\u013d\u013e\b\23\1\2\u013e\u013f\7\25\2\2\u013f"+
		"\u014b\5$\23\3\u0140\u014b\7\60\2\2\u0141\u014b\7\62\2\2\u0142\u0143\5"+
		"*\26\2\u0143\u0144\58\35\2\u0144\u0145\5,\27\2\u0145\u014b\3\2\2\2\u0146"+
		"\u0147\7\f\2\2\u0147\u0148\5$\23\2\u0148\u0149\7\r\2\2\u0149\u014b\3\2"+
		"\2\2\u014a\u013d\3\2\2\2\u014a\u0140\3\2\2\2\u014a\u0141\3\2\2\2\u014a"+
		"\u0142\3\2\2\2\u014a\u0146\3\2\2\2\u014b\u0152\3\2\2\2\u014c\u014d\f\5"+
		"\2\2\u014d\u014e\5\62\32\2\u014e\u014f\5$\23\6\u014f\u0151\3\2\2\2\u0150"+
		"\u014c\3\2\2\2\u0151\u0154\3\2\2\2\u0152\u0150\3\2\2\2\u0152\u0153\3\2"+
		"\2\2\u0153%\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u0159\b\24\1\2\u0156\u0157"+
		"\5,\27\2\u0157\u0158\7\n\2\2\u0158\u015a\3\2\2\2\u0159\u0156\3\2\2\2\u0159"+
		"\u015a\3\2\2\2\u015a\u015b\3\2\2\2\u015b\u015c\5(\25\2\u015c\u0162\3\2"+
		"\2\2\u015d\u015e\f\3\2\2\u015e\u015f\7\26\2\2\u015f\u0161\5&\24\4\u0160"+
		"\u015d\3\2\2\2\u0161\u0164\3\2\2\2\u0162\u0160\3\2\2\2\u0162\u0163\3\2"+
		"\2\2\u0163\'\3\2\2\2\u0164\u0162\3\2\2\2\u0165\u0166\b\25\1\2\u0166\u016f"+
		"\7\60\2\2\u0167\u0168\7\f\2\2\u0168\u0169\5*\26\2\u0169\u016a\7\27\2\2"+
		"\u016a\u016b\7%\2\2\u016b\u016c\5,\27\2\u016c\u016d\7\r\2\2\u016d\u016f"+
		"\3\2\2\2\u016e\u0165\3\2\2\2\u016e\u0167\3\2\2\2\u016f\u0175\3\2\2\2\u0170"+
		"\u0171\f\3\2\2\u0171\u0172\7\30\2\2\u0172\u0174\5(\25\4\u0173\u0170\3"+
		"\2\2\2\u0174\u0177\3\2\2\2\u0175\u0173\3\2\2\2\u0175\u0176\3\2\2\2\u0176"+
		")\3\2\2\2\u0177\u0175\3\2\2\2\u0178\u0179\7\62\2\2\u0179+\3\2\2\2\u017a"+
		"\u017b\b\27\1\2\u017b\u0183\t\2\2\2\u017c\u0183\5*\26\2\u017d\u017e\7"+
		"\f\2\2\u017e\u017f\5,\27\2\u017f\u0180\7\r\2\2\u0180\u0183\3\2\2\2\u0181"+
		"\u0183\5\16\b\2\u0182\u017a\3\2\2\2\u0182\u017c\3\2\2\2\u0182\u017d\3"+
		"\2\2\2\u0182\u0181\3\2\2\2\u0183\u018a\3\2\2\2\u0184\u0185\f\7\2\2\u0185"+
		"\u0186\58\35\2\u0186\u0187\5,\27\b\u0187\u0189\3\2\2\2\u0188\u0184\3\2"+
		"\2\2\u0189\u018c\3\2\2\2\u018a\u0188\3\2\2\2\u018a\u018b\3\2\2\2\u018b"+
		"-\3\2\2\2\u018c\u018a\3\2\2\2\u018d\u018e\t\3\2\2\u018e/\3\2\2\2\u018f"+
		"\u0190\t\4\2\2\u0190\61\3\2\2\2\u0191\u0192\t\5\2\2\u0192\63\3\2\2\2\u0193"+
		"\u0194\t\6\2\2\u0194\65\3\2\2\2\u0195\u0198\7\63\2\2\u0196\u0198\5*\26"+
		"\2\u0197\u0195\3\2\2\2\u0197\u0196\3\2\2\2\u0198\67\3\2\2\2\u0199\u01a1"+
		"\5\64\33\2\u019a\u01a1\5\60\31\2\u019b\u01a1\5\62\32\2\u019c\u01a1\7\""+
		"\2\2\u019d\u01a1\7#\2\2\u019e\u01a1\7$\2\2\u019f\u01a1\7\n\2\2\u01a0\u0199"+
		"\3\2\2\2\u01a0\u019a\3\2\2\2\u01a0\u019b\3\2\2\2\u01a0\u019c\3\2\2\2\u01a0"+
		"\u019d\3\2\2\2\u01a0\u019e\3\2\2\2\u01a0\u019f\3\2\2\2\u01a19\3\2\2\2"+
		")ACMS`lsz}\u008a\u0096\u00ae\u00b2\u00bd\u00c8\u00d5\u00de\u00e3\u00e9"+
		"\u00ef\u00f5\u00fc\u0109\u010d\u0116\u011b\u0125\u0130\u0135\u014a\u0152"+
		"\u0159\u0162\u016e\u0175\u0182\u018a\u0197\u01a0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}