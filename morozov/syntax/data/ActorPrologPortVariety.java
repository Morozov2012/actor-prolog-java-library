// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public enum ActorPrologPortVariety {
	//
	UNRESTRICTED {
		@Override
		public Term toTerm() {
			return termUnrestricted;
		}
	},
	CONSTANT {
		@Override
		public Term toTerm() {
			return termConstant;
		}
	},
	INTERNAL {
		@Override
		public Term toTerm() {
			return termInternal;
		}
	},
	VARIABLE {
		@Override
		public Term toTerm() {
			return termVariable;
		}
	},
	SUSPENDING {
		@Override
		public Term toTerm() {
			return termSuspending;
		}
	},
	PROTECTING {
		@Override
		public Term toTerm() {
			return termProtecting;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termUnrestricted= new PrologSymbol(SymbolCodes.symbolCode_E_unrestricted);
	protected static Term termConstant= new PrologSymbol(SymbolCodes.symbolCode_E_constant);
	protected static Term termInternal= new PrologSymbol(SymbolCodes.symbolCode_E_internal);
	protected static Term termVariable= new PrologSymbol(SymbolCodes.symbolCode_E_variable);
	protected static Term termSuspending= new PrologSymbol(SymbolCodes.symbolCode_E_suspending);
	protected static Term termProtecting= new PrologSymbol(SymbolCodes.symbolCode_E_protecting);
	//
	///////////////////////////////////////////////////////////////
	//
	public abstract Term toTerm();
}
