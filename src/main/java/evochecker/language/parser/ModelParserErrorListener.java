package evochecker.language.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ModelParserErrorListener extends BaseErrorListener {

	private boolean faultyInput = false;
	
	public ModelParserErrorListener() {
		
	}
	
	public boolean isInputFaulty() {
		return faultyInput;
	}

	
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
							int charPositionInLine, String msg, RecognitionException e) {
//		System.err.println("line " + line + ":" + charPositionInLine + " " + msg);
		System.err.println("[Syntax error - line " + line + ":" + charPositionInLine + "] " + msg);
		faultyInput = true;
	}
	
	
}
