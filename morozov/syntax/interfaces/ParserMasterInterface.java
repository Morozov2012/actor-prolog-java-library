// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.interfaces;

import morozov.run.*;
import morozov.syntax.errors.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.interfaces.*;

public interface ParserMasterInterface extends LexicalScannerMasterInterface {
	public void handleError(ParserError exception, ChoisePoint iX) throws ParserError;
	public ParserError handleUnexpectedEndOfTokenList(PrologToken[] tokens, ChoisePoint iX) throws ParserError;
}
