// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.run.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class DialogIdentifierOrAuto {
	//
	public String name;
	public boolean isAutomatic;
	//
	protected static Term termAuto= new PrologSymbol(SymbolCodes.symbolCode_E_auto);
	//
	public DialogIdentifierOrAuto(String identifier) {
		name= identifier;
		isAutomatic= false;
	}
	public DialogIdentifierOrAuto() {
		isAutomatic= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static DialogIdentifierOrAuto termToDialogIdentifierOrAuto(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_auto) {
				return new DialogIdentifierOrAuto();
			} else {
				throw new WrongArgumentIsNotDialogIdentifier(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new DialogIdentifierOrAuto(value.getStringValue(iX));
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotDialogIdentifier(value);
			}
		}
	}
	//
	public static Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return termAuto;
		} else {
			try {
				String text= value.getStringValue(iX);
				return new PrologString(text);
			} catch (TermIsNotAString e1) {
				try {
					long code= value.getSymbolValue(iX);
					if (code==SymbolCodes.symbolCode_E_auto) {
						return termAuto;
					} else {
						throw RejectValue.instance;
					}
				} catch (TermIsNotASymbol e2) {
					throw RejectValue.instance;
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isAutomatic) {
			return termAuto;
		} else {
			return new PrologString(name);
		}
	}
}
