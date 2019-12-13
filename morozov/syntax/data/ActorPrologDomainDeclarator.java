// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public enum ActorPrologDomainDeclarator {
	//
	GROUND {
		@Override
		public Term toTerm() {
			return termGround;
		}
	},
	REFERENCE {
		@Override
		public Term toTerm() {
			return termReference;
		}
	},
	LAZY {
		@Override
		public Term toTerm() {
			return termLazy;
		}
	},
	MIXED {
		@Override
		public Term toTerm() {
			return termMixed;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termGround= new PrologSymbol(SymbolCodes.symbolCode_E_ground);
	protected static Term termReference= new PrologSymbol(SymbolCodes.symbolCode_E_reference);
	protected static Term termLazy= new PrologSymbol(SymbolCodes.symbolCode_E_lazy);
	protected static Term termMixed= new PrologSymbol(SymbolCodes.symbolCode_E_mixed);
	//
	///////////////////////////////////////////////////////////////
	//
	public abstract Term toTerm();
}
