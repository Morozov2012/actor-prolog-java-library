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
	public static IndexRange[] termsToIndexRanges(ChoisePoint iX, Term value) {
		ArrayList<IndexRange> list= new ArrayList<IndexRange>();
		Term nextHead= null;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				BigInteger left= null;
				BigInteger right= null;
				try {
					Term leftBound= nextHead.getNextListHead(iX);
					left= termToIndexBound(iX,leftBound,false);
					Term internalListTail= nextHead.getNextListTail(iX);
					Term rightBound= internalListTail.getNextListHead(iX);
					right= termToIndexBound(iX,rightBound,true);
					internalListTail= internalListTail.getNextListTail(iX);
					// String description= nextHead.getStringValue(iX);
				} catch (EndOfList e) {
					if (left==null || right==null) {
						throw new WrongTermIsNotIndexRange(nextHead);
					};
				} catch (TermIsNotAList e) {
					throw new WrongTermIsNotIndexRange(nextHead);
				};
				currentTail= currentTail.getNextListTail(iX);
				list.add(new IndexRange(left,right));
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			throw new WrongTermIsNotIndexRangeList(currentTail);
		};
		return list.toArray(new IndexRange[0]);
	}
	public static BigInteger termToIndexBound(ChoisePoint iX, Term value, boolean isRightBound) {
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
					throw new WrongTermIsNotIndexBound(value);
				}
			} catch (TermIsNotASymbol b2) {
				if (value.thisIsEmptyList()) {
					if (isRightBound) {
						return BigInteger.valueOf(Long.MAX_VALUE);
					} else {
						return BigInteger.valueOf(Long.MIN_VALUE);
					}
				} else {
					throw new WrongTermIsNotIndexBound(value);
				}
			}
		}
	}
}
