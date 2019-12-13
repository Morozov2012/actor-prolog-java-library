// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public enum ActorPrologPredicateDeterminancy {
	//
	IMPERATIVE {
		@Override
		public Term toTerm() {
			return termImperative;
		}
	},
	DETERMINISTIC {
		@Override
		public Term toTerm() {
			return termDeterministic;
		}
	},
	NONDETERMINISTIC {
		@Override
		public Term toTerm() {
			return termNondeterministic;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termImperative= new PrologSymbol(SymbolCodes.symbolCode_E_imperative);
	protected static Term termDeterministic= new PrologSymbol(SymbolCodes.symbolCode_E_deterministic);
	protected static Term termNondeterministic= new PrologSymbol(SymbolCodes.symbolCode_E_nondeterministic);
	//
	///////////////////////////////////////////////////////////////
	//
	public abstract Term toTerm();
}
