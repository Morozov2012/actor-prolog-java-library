// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices;

import target.*;

import morozov.run.*;
import morozov.system.indices.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;
import java.util.ArrayList;

public class ArrayUtils {
	//
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_none);
	//
	public static IndexRange[] termsToIndexRanges(Term value, ChoisePoint iX) {
		ArrayList<IndexRange> list= new ArrayList<>();
		Term nextHead= null;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				BigInteger left= null;
				BigInteger right= null;
				try {
					Term leftBound= nextHead.getNextListHead(iX);
					left= argumentToIndexBound(iX,leftBound,false);
					Term internalListTail= nextHead.getNextListTail(iX);
					Term rightBound= internalListTail.getNextListHead(iX);
					right= argumentToIndexBound(iX,rightBound,true);
					internalListTail.getNextListTail(iX);
				} catch (EndOfList e) {
					if (left==null || right==null) {
						throw new WrongArgumentIsNotIndexRange(nextHead);
					};
				} catch (TermIsNotAList e) {
					throw new WrongArgumentIsNotIndexRange(nextHead);
				};
				currentTail= currentTail.getNextListTail(iX);
				list.add(new IndexRange(left,right));
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			throw new WrongArgumentIsNotIndexRangeList(currentTail);
		};
		return list.toArray(new IndexRange[list.size()]);
	}
	public static BigInteger argumentToIndexBound(ChoisePoint iX, Term value, boolean isRightBound) {
		value= value.dereferenceValue(iX);
		try {
			return value.getIntegerValue(iX);
		} catch (TermIsNotAnInteger b1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_none) {
					if (isRightBound) {
						return BigInteger.valueOf(Long.MAX_VALUE);
					} else {
						return BigInteger.valueOf(Long.MIN_VALUE);
					}
				} else {
					throw new WrongArgumentIsNotIndexBound(value);
				}
			} catch (TermIsNotASymbol b2) {
				if (value.thisIsEmptyList()) {
					if (isRightBound) {
						return BigInteger.valueOf(Long.MAX_VALUE);
					} else {
						return BigInteger.valueOf(Long.MIN_VALUE);
					}
				} else {
					throw new WrongArgumentIsNotIndexBound(value);
				}
			}
		}
	}
	//
	public static Term indexRangesToTerm(IndexRange[] array) {
		int length= array.length;
		Term result= PrologEmptyList.instance;
		for (int n=length-1; n >= 0; n--) {
			IndexRange range= array[n];
			Term left= leftRangeBoundToTerm(range.leftBound);
			Term right= rightRangeBoundToTerm(range.rightBound);
			Term head= new PrologList(right,PrologEmptyList.instance);
			head= new PrologList(left,head);
			result= new PrologList(head,result);
		};
		return result;
	}
	protected static Term leftRangeBoundToTerm(BigInteger value) {
		if (value.equals(BigInteger.valueOf(Long.MIN_VALUE))) {
			return termNone;
		} else {
			return new PrologInteger(value);
		}
	}
	protected static Term rightRangeBoundToTerm(BigInteger value) {
		if (value.equals(BigInteger.valueOf(Long.MAX_VALUE))) {
			return termNone;
		} else {
			return new PrologInteger(value);
		}
	}
}
