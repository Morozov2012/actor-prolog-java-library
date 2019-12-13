// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class DeviceIdentifierConverters {
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static DeviceIdentifier argumentToDeviceIdentifier(Term a, ChoisePoint iX) {
		try {
			String identifier= a.getStringValue(iX);
			return new DeviceIdentifier(identifier);
		} catch (TermIsNotAString e1) {
			try {
				long code= a.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					return DeviceIdentifier.DEFAULT;
				} else {
					throw new WrongArgumentIsNotDeviceIdentifier(a);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotDeviceIdentifier(a);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(DeviceIdentifier identifier) {
		if (identifier.isDefault()) {
			return termDefault;
		} else {
			return new PrologString(identifier.getName());
		}
	}
}
