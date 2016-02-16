// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.files;

import target.*;

import morozov.run.*;
import morozov.system.files.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FileAccessMode {
	READING,
	WRITING,
	APPENDING,
	MODIFYING;
	//
	public static FileAccessMode termToFileAccessMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_reading) {
				return FileAccessMode.READING;
			} else if (code==SymbolCodes.symbolCode_E_writing) {
				return FileAccessMode.WRITING;
			} else if (code==SymbolCodes.symbolCode_E_appending) {
				return FileAccessMode.APPENDING;
			} else if (code==SymbolCodes.symbolCode_E_modifying) {
				return FileAccessMode.MODIFYING;
			} else {
				throw new WrongArgumentIsNotFileAccessMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotFileAccessMode(value);
		}
	}
}
