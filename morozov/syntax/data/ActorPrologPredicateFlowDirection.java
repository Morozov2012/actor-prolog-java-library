// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public enum ActorPrologPredicateFlowDirection {
	//
	IN(false) {
		@Override
		public Term toTerm() {
			return termIn;
		}
	},
	OUT(false) {
		@Override
		public Term toTerm() {
			return termOut;
		}
	},
	ANY(false) {
		@Override
		public Term toTerm() {
			return termAny;
		}
	},
	LIST_IN(true) {
		@Override
		public Term toTerm() {
			return termListIn;
		}
	},
	LIST_OUT(true) {
		@Override
		public Term toTerm() {
			return termListOut;
		}
	},
	LIST_ANY(true) {
		@Override
		public Term toTerm() {
			return termListAny;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	protected boolean isList= false;
	//
	ActorPrologPredicateFlowDirection(boolean mode) {
		isList= mode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static final Term termIn= new PrologSymbol(SymbolCodes.symbolCode_E_in);
	protected static final Term termOut= new PrologSymbol(SymbolCodes.symbolCode_E_out);
	protected static final Term termAny= new PrologSymbol(SymbolCodes.symbolCode_E_any);
	protected static final Term termListIn= new PrologStructure(SymbolCodes.symbolCode_E_list,new Term[]{termIn});
	protected static final Term termListOut= new PrologStructure(SymbolCodes.symbolCode_E_list,new Term[]{termOut});
	protected static final Term termListAny= new PrologStructure(SymbolCodes.symbolCode_E_list,new Term[]{termAny});
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isList() {
		return isList;
	}
	//
	public abstract Term toTerm();
}
