// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;

public class DummyLexicalScannerMaster implements LexicalScannerMasterInterface {
	public void handleError(LexicalScannerError exception, ChoisePoint iX) throws LexicalScannerError {
		throw exception;
	}
	public LexicalScannerError handleUnexpectedEndOfText(int position, ChoisePoint iX) throws LexicalScannerError {
		return new UnexpectedEndOfText(position);
	}
}
