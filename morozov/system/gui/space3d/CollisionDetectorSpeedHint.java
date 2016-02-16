// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.run.*;
import morozov.system.gui.space3d.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum CollisionDetectorSpeedHint {
	//
	USE_BOUNDS,
	USE_GEOMETRY;
	//
	///////////////////////////////////////////////////////////////
	//
	public static CollisionDetectorSpeedHint termToCollisionDetectorSpeedHint(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_USE_BOUNDS) {
				return CollisionDetectorSpeedHint.USE_BOUNDS;
			} else if (code==SymbolCodes.symbolCode_E_USE_GEOMETRY) {
				return CollisionDetectorSpeedHint.USE_GEOMETRY;
			} else {
				throw new WrongArgumentIsNotCollisionDetectorSpeedHint(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotCollisionDetectorSpeedHint(value);
		}
	}
}
