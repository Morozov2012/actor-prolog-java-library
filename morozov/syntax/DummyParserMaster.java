// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax;

import morozov.run.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;

public class DummyParserMaster
		extends DummyLexicalScannerMaster
		implements ParserMasterInterface {
	@Override
	public void handleError(ParserError exception, ChoisePoint iX) throws ParserError {
		throw exception;
	}
	@Override
	public ParserError handleUnexpectedEndOfTokenList(PrologToken[] tokens, ChoisePoint iX) throws ParserError {
		ParserError exception= new UnexpectedEndOfTokenList(tokens);
		return exception;
	}
}
