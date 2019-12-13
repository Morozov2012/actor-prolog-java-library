// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.space3d.errors.*;
import morozov.system.gui.space3d.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class FieldOfView {
	//
	protected double value= 0.0;
	protected boolean isDefault= false;
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public FieldOfView(double v) {
		value= v;
	}
	public FieldOfView() {
		isDefault= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double getValue() throws UseDefaultFieldOfView {
		if (isDefault) {
			throw UseDefaultFieldOfView.instance;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FieldOfView argumentToFieldOfView(Term argument, ChoisePoint iX) {
		try {
			double v= GeneralConverters.termToReal(argument,iX);
			return new FieldOfView(v);
		} catch (TermIsNotAReal e1) {
			try {
				long code= argument.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					return new FieldOfView();
				} else {
					throw new WrongArgumentIsNotAFieldOfView(argument);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotAFieldOfView(argument);
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
