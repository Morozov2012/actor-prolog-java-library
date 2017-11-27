// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.space3d.errors.*;
import morozov.system.gui.space3d.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ClipDistance {
	//
	public double value= 0.0;
	public boolean isDefault= false;
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public ClipDistance(double v) {
		value= v;
	}
	public ClipDistance() {
		isDefault= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double getValue() throws UseDefaultClipDistance {
		if (isDefault) {
			throw UseDefaultClipDistance.instance;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ClipDistance argumentToClipDistance(Term argument, ChoisePoint iX) {
		try {
			double v= Converters.termToReal(argument,iX);
			return new ClipDistance(v);
		} catch (TermIsNotAReal e1) {
			try {
				long code= argument.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					return new ClipDistance();
				} else {
					throw new WrongArgumentIsNotAClipDistance(argument);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotAClipDistance(argument);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isDefault) {
			return termDefault;
		} else {
			return new PrologReal(value);
		}
	}
}
