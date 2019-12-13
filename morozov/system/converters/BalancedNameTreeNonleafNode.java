// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.terms.*;

public class BalancedNameTreeNonleafNode extends BalancedNameTreeNode {
	//
	protected int number;
	protected String name;
	protected BalancedNameTreeNode leftBranch;
	protected BalancedNameTreeNode rightBranch;
	//
	public BalancedNameTreeNonleafNode(int n, String text, BalancedNameTreeNode left, BalancedNameTreeNode right) {
		number= n;
		name= text;
		leftBranch= left;
		rightBranch= right;
	}
	//
	@Override
	public String getName(int n) {
		if (n < number) {
			return leftBranch.getName(n);
		} else if (n > number) {
			return rightBranch.getName(n);
		} else {
			return name;
		}
	}
	//
	@Override
	public boolean isNil() {
		return false;
	}
	//
	@Override
	public Term toTerm() {
		if (leftBranch.isNil() && rightBranch.isNil()) {
			Term[] internalArray= new Term[2];
			internalArray[0]= new PrologInteger(number);
			internalArray[1]= new PrologString(name);
			return new PrologStructure(SymbolCodes.symbolCode_E_leaf_node,internalArray);
		} else {
			Term[] internalArray= new Term[4];
			internalArray[0]= new PrologInteger(number);
			internalArray[1]= new PrologString(name);
			internalArray[2]= leftBranch.toTerm();
			internalArray[3]= rightBranch.toTerm();
			return new PrologStructure(SymbolCodes.symbolCode_E_node,internalArray);
		}
	}
}
