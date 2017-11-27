// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.files;

import target.*;

import morozov.run.*;
import morozov.system.files.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum PositioningMode {
	START,
	RELATIVE,
	END;
	//
	public static PositioningMode argumentToPositioningMode(Term mode, ChoisePoint iX) {
		try {
			long code= mode.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_start) {
				return PositioningMode.START;
			} else if (code==SymbolCodes.symbolCode_E_relative) {
				return PositioningMode.RELATIVE;
			} else if (code==SymbolCodes.symbolCode_E_end) {
				return PositioningMode.END;
			} else {
				throw new WrongArgumentIsNotPositioningMode(mode);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotPositioningMode(mode);
		}
	}
}
