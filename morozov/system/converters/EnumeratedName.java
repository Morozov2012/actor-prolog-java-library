// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import java.util.Arrays;
import java.lang.Comparable;

public class EnumeratedName implements Comparable {
	//
	protected int number;
	protected String name;
	//
	public EnumeratedName(int n, String text) {
		number= n;
		name= text;
	}
	//
	public int getNumber() {
		return number;
	}
	public String getName() {
		return name;
	}
	//
	public static BalancedNameTreeNode toBalancedNameTree(EnumeratedName[] sortedArray) {
		int length= sortedArray.length;
		if (length > 1) {
			int center= length / 2;
			EnumeratedName[] leftNames= Arrays.copyOfRange(sortedArray,0,center);
			EnumeratedName[] rightNames= Arrays.copyOfRange(sortedArray,center+1,length);
			EnumeratedName centerElement= sortedArray[center];
			return new BalancedNameTreeNonleafNode(
				centerElement.getNumber(),
				centerElement.getName(),
				EnumeratedName.toBalancedNameTree(leftNames),
				EnumeratedName.toBalancedNameTree(rightNames));
		} else if (length == 1) {
			EnumeratedName centerElement= sortedArray[0];
			return new BalancedNameTreeNonleafNode(
				centerElement.getNumber(),
				centerElement.getName(),
				BalancedNameTreeNode.leafNode,
				BalancedNameTreeNode.leafNode);
		} else {
			return BalancedNameTreeNode.leafNode;
		}
	}
	//
	@Override
	public int compareTo(Object o) {
		if (o instanceof EnumeratedName) {
			EnumeratedName n= (EnumeratedName)o;
			return Integer.compare(number,n.getNumber());
		} else {
			return -1;
		}
	}
}
