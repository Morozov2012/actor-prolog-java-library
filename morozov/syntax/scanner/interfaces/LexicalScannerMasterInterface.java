// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.interfaces;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;

public interface LexicalScannerMasterInterface {
	public void handleError(LexicalScannerError exception, ChoisePoint iX) throws LexicalScannerError;
	public LexicalScannerError handleUnexpectedEndOfText(int position, ChoisePoint iX) throws LexicalScannerError;
}
