// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.gui.space2d.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum Canvas2DVerticalAlignment {
	//
	DEFAULT,
	BASELINE,
	TOP,
	BOTTOM,
	CENTER;
	//
	///////////////////////////////////////////////////////////////
	//
	public static Canvas2DVerticalAlignment argumentToVerticalAlignment(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return Canvas2DVerticalAlignment.DEFAULT;
			} else if (code==SymbolCodes.symbolCode_E_BASELINE) {
				return Canvas2DVerticalAlignment.BASELINE;
			} else if (code==SymbolCodes.symbolCode_E_TOP) {
				return Canvas2DVerticalAlignment.TOP;
			} else if (code==SymbolCodes.symbolCode_E_BOTTOM) {
				return Canvas2DVerticalAlignment.BOTTOM;
			} else if (code==SymbolCodes.symbolCode_E_CENTER) {
				return Canvas2DVerticalAlignment.CENTER;
			} else {
				throw new WrongArgumentIsNotVerticalAlignment(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotVerticalAlignment(value);
		}
	}
}
