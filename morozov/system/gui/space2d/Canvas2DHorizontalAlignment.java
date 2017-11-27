// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.gui.space2d.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum Canvas2DHorizontalAlignment {
	//
	DEFAULT,
	LEFT,
	RIGHT,
	CENTER;
	//
	///////////////////////////////////////////////////////////////
	//
	public static Canvas2DHorizontalAlignment argumentToHorizontalAlignment(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return Canvas2DHorizontalAlignment.DEFAULT;
			} else if (code==SymbolCodes.symbolCode_E_LEFT) {
				return Canvas2DHorizontalAlignment.LEFT;
			} else if (code==SymbolCodes.symbolCode_E_RIGHT) {
				return Canvas2DHorizontalAlignment.RIGHT;
			} else if (code==SymbolCodes.symbolCode_E_CENTER) {
				return Canvas2DHorizontalAlignment.CENTER;
			} else {
				throw new WrongArgumentIsNotHorizontalAlignment(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotHorizontalAlignment(value);
		}
	}
}
