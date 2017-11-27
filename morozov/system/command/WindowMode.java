// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.command;

import target.*;

import morozov.run.*;
import morozov.system.command.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum WindowMode {
	//
	INVISIBLE {
		public Term toTerm() {
			return termInvisible;
		}
	},
	MINIMIZED {
		public Term toTerm() {
			return termMinimized;
		}
	},
	RESTORED {
		public Term toTerm() {
			return termRestored;
		}
	},
	MAXIMIZED {
		public Term toTerm() {
			return termMaximized;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termInvisible= new PrologSymbol(SymbolCodes.symbolCode_E_invisible);
	protected static Term termMinimized= new PrologSymbol(SymbolCodes.symbolCode_E_minimized);
	protected static Term termRestored= new PrologSymbol(SymbolCodes.symbolCode_E_restored);
	protected static Term termMaximized= new PrologSymbol(SymbolCodes.symbolCode_E_maximized);
	//
	///////////////////////////////////////////////////////////////
	//
	public static WindowMode argumentToWindowMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_invisible) {
				return WindowMode.INVISIBLE;
			} else if (code==SymbolCodes.symbolCode_E_minimized) {
				return WindowMode.MINIMIZED;
			} else if (code==SymbolCodes.symbolCode_E_restored) {
				return WindowMode.RESTORED;
			} else if (code==SymbolCodes.symbolCode_E_maximized) {
				return WindowMode.MAXIMIZED;
			} else {
				throw new WrongArgumentIsNotWindowMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotWindowMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
