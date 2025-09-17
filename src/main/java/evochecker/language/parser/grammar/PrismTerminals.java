// Generated from PrismTerminals.g4 by ANTLR 4.4

	//package org.spg.language.prism.grammar;
	package evochecker.language.parser.grammar;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PrismTerminals extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ASSIGN=1, EVOLVE=2, CONST=3, DISTRIBUTION=4, PARAM=5, FUNCTIONIDENTIFIER=6, 
		CONSTANTTYPE=7, SLCOMMENT=8, DTMC=9, CTMC=10, MDP=11, POMDP=12, BOOLEAN=13, 
		OPERATOR=14, ID=15, INT=16, DOUBLE=17, STRING=18, WS=19;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'"
	};
	public static final String[] ruleNames = {
		"ASSIGN", "EVOLVE", "CONST", "DISTRIBUTION", "PARAM", "FUNCTIONIDENTIFIER", 
		"CONSTANTTYPE", "SLCOMMENT", "DTMC", "CTMC", "MDP", "POMDP", "BOOLEAN", 
		"OPERATOR", "ID", "INT", "DOUBLE", "STRING", "WS"
	};


	public PrismTerminals(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PrismTerminals.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\25\u00d5\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7d\n\7\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bs\n\b\3\t\3\t\3\t\3\t\7\t"+
		"y\n\t\f\t\16\t|\13\t\3\t\5\t\177\n\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3"+
		"\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00a2\n\16\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00b2"+
		"\n\17\3\20\3\20\7\20\u00b6\n\20\f\20\16\20\u00b9\13\20\3\21\6\21\u00bc"+
		"\n\21\r\21\16\21\u00bd\3\22\5\22\u00c1\n\22\3\22\3\22\3\22\3\23\3\23\7"+
		"\23\u00c8\n\23\f\23\16\23\u00cb\13\23\3\23\3\23\3\24\6\24\u00d0\n\24\r"+
		"\24\16\24\u00d1\3\24\3\24\3z\2\25\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25\3\2\t\6\2,-//\61"+
		"\61>>\5\2##((~~\5\2C\\aac|\6\2\62;C\\aac|\3\2\62;\4\2$$^^\5\2\13\f\17"+
		"\17\"\"\u00eb\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2"+
		"\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2"+
		"\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\3)\3\2\2\2\5+\3\2\2\2\7\62\3\2"+
		"\2\2\t8\3\2\2\2\13E\3\2\2\2\rc\3\2\2\2\17r\3\2\2\2\21t\3\2\2\2\23\u0084"+
		"\3\2\2\2\25\u0089\3\2\2\2\27\u008e\3\2\2\2\31\u0092\3\2\2\2\33\u00a1\3"+
		"\2\2\2\35\u00b1\3\2\2\2\37\u00b3\3\2\2\2!\u00bb\3\2\2\2#\u00c0\3\2\2\2"+
		"%\u00c5\3\2\2\2\'\u00cf\3\2\2\2)*\7?\2\2*\4\3\2\2\2+,\7g\2\2,-\7x\2\2"+
		"-.\7q\2\2./\7n\2\2/\60\7x\2\2\60\61\7g\2\2\61\6\3\2\2\2\62\63\7e\2\2\63"+
		"\64\7q\2\2\64\65\7p\2\2\65\66\7u\2\2\66\67\7v\2\2\67\b\3\2\2\289\7f\2"+
		"\29:\7k\2\2:;\7u\2\2;<\7v\2\2<=\7t\2\2=>\7k\2\2>?\7d\2\2?@\7w\2\2@A\7"+
		"v\2\2AB\7k\2\2BC\7q\2\2CD\7p\2\2D\n\3\2\2\2EF\7r\2\2FG\7c\2\2GH\7t\2\2"+
		"HI\7c\2\2IJ\7o\2\2J\f\3\2\2\2KL\7o\2\2LM\7k\2\2Md\7p\2\2NO\7o\2\2OP\7"+
		"c\2\2Pd\7z\2\2QR\7h\2\2RS\7n\2\2ST\7q\2\2TU\7q\2\2Ud\7t\2\2VW\7e\2\2W"+
		"X\7g\2\2XY\7k\2\2Yd\7n\2\2Z[\7r\2\2[\\\7q\2\2\\d\7y\2\2]^\7o\2\2^_\7q"+
		"\2\2_d\7f\2\2`a\7n\2\2ab\7q\2\2bd\7i\2\2cK\3\2\2\2cN\3\2\2\2cQ\3\2\2\2"+
		"cV\3\2\2\2cZ\3\2\2\2c]\3\2\2\2c`\3\2\2\2d\16\3\2\2\2ef\7d\2\2fg\7q\2\2"+
		"gh\7q\2\2hs\7n\2\2ij\7k\2\2jk\7p\2\2ks\7v\2\2lm\7f\2\2mn\7q\2\2no\7w\2"+
		"\2op\7d\2\2pq\7n\2\2qs\7g\2\2re\3\2\2\2ri\3\2\2\2rl\3\2\2\2s\20\3\2\2"+
		"\2tu\7\61\2\2uv\7\61\2\2vz\3\2\2\2wy\13\2\2\2xw\3\2\2\2y|\3\2\2\2z{\3"+
		"\2\2\2zx\3\2\2\2{~\3\2\2\2|z\3\2\2\2}\177\7\17\2\2~}\3\2\2\2~\177\3\2"+
		"\2\2\177\u0080\3\2\2\2\u0080\u0081\7\f\2\2\u0081\u0082\3\2\2\2\u0082\u0083"+
		"\b\t\2\2\u0083\22\3\2\2\2\u0084\u0085\7f\2\2\u0085\u0086\7v\2\2\u0086"+
		"\u0087\7o\2\2\u0087\u0088\7e\2\2\u0088\24\3\2\2\2\u0089\u008a\7e\2\2\u008a"+
		"\u008b\7v\2\2\u008b\u008c\7o\2\2\u008c\u008d\7e\2\2\u008d\26\3\2\2\2\u008e"+
		"\u008f\7o\2\2\u008f\u0090\7f\2\2\u0090\u0091\7r\2\2\u0091\30\3\2\2\2\u0092"+
		"\u0093\7r\2\2\u0093\u0094\7q\2\2\u0094\u0095\7o\2\2\u0095\u0096\7f\2\2"+
		"\u0096\u0097\7r\2\2\u0097\32\3\2\2\2\u0098\u0099\7v\2\2\u0099\u009a\7"+
		"t\2\2\u009a\u009b\7w\2\2\u009b\u00a2\7g\2\2\u009c\u009d\7h\2\2\u009d\u009e"+
		"\7c\2\2\u009e\u009f\7n\2\2\u009f\u00a0\7u\2\2\u00a0\u00a2\7g\2\2\u00a1"+
		"\u0098\3\2\2\2\u00a1\u009c\3\2\2\2\u00a2\34\3\2\2\2\u00a3\u00b2\t\2\2"+
		"\2\u00a4\u00a5\7>\2\2\u00a5\u00b2\7?\2\2\u00a6\u00a7\7@\2\2\u00a7\u00b2"+
		"\7?\2\2\u00a8\u00b2\4?@\2\u00a9\u00aa\7#\2\2\u00aa\u00b2\7?\2\2\u00ab"+
		"\u00b2\t\3\2\2\u00ac\u00ad\7>\2\2\u00ad\u00ae\7?\2\2\u00ae\u00b2\7@\2"+
		"\2\u00af\u00b0\7?\2\2\u00b0\u00b2\7@\2\2\u00b1\u00a3\3\2\2\2\u00b1\u00a4"+
		"\3\2\2\2\u00b1\u00a6\3\2\2\2\u00b1\u00a8\3\2\2\2\u00b1\u00a9\3\2\2\2\u00b1"+
		"\u00ab\3\2\2\2\u00b1\u00ac\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\36\3\2\2"+
		"\2\u00b3\u00b7\t\4\2\2\u00b4\u00b6\t\5\2\2\u00b5\u00b4\3\2\2\2\u00b6\u00b9"+
		"\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8 \3\2\2\2\u00b9"+
		"\u00b7\3\2\2\2\u00ba\u00bc\t\6\2\2\u00bb\u00ba\3\2\2\2\u00bc\u00bd\3\2"+
		"\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\"\3\2\2\2\u00bf\u00c1"+
		"\5!\21\2\u00c0\u00bf\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2"+
		"\u00c3\7\60\2\2\u00c3\u00c4\5!\21\2\u00c4$\3\2\2\2\u00c5\u00c9\7$\2\2"+
		"\u00c6\u00c8\n\7\2\2\u00c7\u00c6\3\2\2\2\u00c8\u00cb\3\2\2\2\u00c9\u00c7"+
		"\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cc"+
		"\u00cd\7$\2\2\u00cd&\3\2\2\2\u00ce\u00d0\t\b\2\2\u00cf\u00ce\3\2\2\2\u00d0"+
		"\u00d1\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d3\3\2"+
		"\2\2\u00d3\u00d4\b\24\2\2\u00d4(\3\2\2\2\16\2crz~\u00a1\u00b1\u00b7\u00bd"+
		"\u00c0\u00c9\u00d1\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}