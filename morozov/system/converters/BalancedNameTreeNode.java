// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.terms.*;

public class BalancedNameTreeNode {
	//
	protected static BalancedNameTreeNode leafNode= new BalancedNameTreeNode();
	//
	protected static PrologSymbol termNil= new PrologSymbol(SymbolCodes.symbolCode_E_nil);
	//
	protected BalancedNameTreeNode() {
	}
	//
	public String getName(int n) {
		return "";
	}
	//
	public boolean isNil() {
		return true;
	}
	//
	public static Term getTermNil() {
		return termNil;
	}
	//
	public Term toTerm() {
		return termNil;
	}
}
