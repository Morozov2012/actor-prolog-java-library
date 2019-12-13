// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public enum ActorPrologVisibility {
	//
	VISIBLE {
		@Override
		public Term toTerm() {
			return termVisible;
		}
	},
	PRIVATE {
		@Override
		public Term toTerm() {
			return termPrivate;
		}
	},
	UNDEFINED {
		@Override
		public Term toTerm() {
			return termUndefined;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termVisible= new PrologSymbol(SymbolCodes.symbolCode_E_visible);
	protected static Term termPrivate= new PrologSymbol(SymbolCodes.symbolCode_E_private);
	protected static Term termUndefined= new PrologSymbol(SymbolCodes.symbolCode_E_undefined);
	//
	///////////////////////////////////////////////////////////////
	//
	public abstract Term toTerm();
}
