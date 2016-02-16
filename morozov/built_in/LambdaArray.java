// (c) 2012-2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.run.errors.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.indices.*;
import morozov.system.indices.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.math.BigInteger;
import java.util.HashMap;

public abstract class LambdaArray extends Lambda {
	//
	protected HashMap<ArrayIndices,SlotVariable> volume= new HashMap<ArrayIndices,SlotVariable>();
	//
	protected IndexRange[] indicesRange= null;
	protected Boolean indexChecking= null;
	//
	public LambdaArray() {
	}
	public LambdaArray(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_indices_range();
	abstract public Term getBuiltInSlot_E_index_checking();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set indicesRange
	//
	public void setIndicesRange1s(ChoisePoint iX, Term a1) {
		setIndicesRange(ArrayUtils.termsToIndexRanges(a1,iX));
	}
	public void setIndicesRange(IndexRange[] value) {
		indicesRange= value;
	}
	public void getIndicesRange0ff(ChoisePoint iX, PrologVariable a1) {
		IndexRange[] value= getIndicesRange(iX);
		a1.value= ArrayUtils.indexRangesToTerm(value);
	}
	public void getIndicesRange0fs(ChoisePoint iX) {
	}
	public IndexRange[] getIndicesRange(ChoisePoint iX) {
		if (indicesRange != null) {
			return indicesRange;
		} else {
			return ArrayUtils.termsToIndexRanges(getBuiltInSlot_E_indices_range(),iX);
		}
	}
	//
	// get/set indexChecking
	//
	public void setIndexChecking1s(ChoisePoint iX, Term a1) {
		setIndexChecking(OnOff.termOnOff2Boolean(a1,iX));
	}
	public void setIndexChecking(boolean value) {
		indexChecking= value;
	}
	public void getIndexChecking0ff(ChoisePoint iX, PrologVariable a1) {
		boolean value= getIndexChecking(iX);
		a1.value= OnOff.boolean2TermOnOff(value);
	}
	public void getIndexChecking0fs(ChoisePoint iX) {
	}
	public boolean getIndexChecking(ChoisePoint iX) {
		if (indexChecking != null) {
			return indexChecking;
		} else {
			return OnOff.termOnOff2Boolean(getBuiltInSlot_E_index_checking(),iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void element1mff(ChoisePoint iX, PrologVariable result, Term... givenIndices) {
		ArrayIndices arrayIndices= collectArrayIndices((Term[])givenIndices,iX);
		try {
			Term value= accessArrayElement(arrayIndices,iX);
			result.value= value;
		} catch (Backtracking b) {
			throw new ImperativeProcedureFailed();
		}
	}
	public void element1mfs(ChoisePoint iX, Term... givenIndices) {
	}
	//
	protected ArrayIndices collectArrayIndices(Term[] givenIndices, ChoisePoint iX) {
		IndexRange[] indexRanges= getIndicesRange(iX);
		if (givenIndices.length != indexRanges.length) {
			throw new IllegalNumberOfIndices(givenIndices.length,indexRanges.length);
		};
		BigInteger[] currentIndexValue= new BigInteger[givenIndices.length];
		Term index= null;
		try {
			for (int n=0; n < givenIndices.length; n++) {
				index= givenIndices[n].dereferenceValue(iX);
				if (index.thisIsFreeVariable()) {
					throw new UninstantiatedIndexInDeterministicAccessProcedure();
				} else {
					BigInteger indexValue= index.getIntegerValue(iX);
					indexRanges[n].checkIndexValue(indexValue);
					currentIndexValue[n]= indexValue;
				}
			}
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(index);
		};
		ArrayIndices arrayIndices= new ArrayIndices(currentIndexValue);
		return arrayIndices;
	}
	//
	public class Element1mff extends Element {
		public Element1mff(Continuation aC, PrologVariable a1, Term... a2) {
			super(aC,a2);
			result= a1;
		}
	}
	public class Element1mfs extends Element {
		public Element1mfs(Continuation aC, Term... a2) {
			super(aC,a2);
			isFunctionCall= false;
		}
	}
	public class Element0ff extends Element {
		public Element0ff(Continuation aC, PrologVariable a1) {
			super(aC);
			result= a1;
		}
	}
	public class Element0fs extends Element {
		public Element0fs(Continuation aC) {
			super(aC);
			isFunctionCall= false;
		}
	}
	public class Element1ff extends Element {
		public Element1ff(Continuation aC, PrologVariable a1, Term a2) {
			super(aC,a2);
			result= a1;
		}
	}
	public class Element1fs extends Element {
		public Element1fs(Continuation aC, Term a2) {
			super(aC,a2);
			isFunctionCall= false;
		}
	}
	public class Element2ff extends Element {
		public Element2ff(Continuation aC, PrologVariable a1, Term a2, Term a3) {
			super(aC,a2,a3);
			result= a1;
		}
	}
	public class Element2fs extends Element {
		public Element2fs(Continuation aC, Term a2, Term a3) {
			super(aC,a2,a3);
			isFunctionCall= false;
		}
	}
	public class Element3ff extends Element {
		public Element3ff(Continuation aC, PrologVariable a1, Term a2, Term a3, Term a4) {
			super(aC,a2,a3,a4);
			result= a1;
		}
	}
	public class Element3fs extends Element {
		public Element3fs(Continuation aC, Term a2, Term a3, Term a4) {
			super(aC,a2,a3,a4);
			isFunctionCall= false;
		}
	}
	public class Element4ff extends Element {
		public Element4ff(Continuation aC, PrologVariable a1, Term a2, Term a3, Term a4, Term a5) {
			super(aC,a2,a3,a4,a5);
			result= a1;
		}
	}
	public class Element4fs extends Element {
		public Element4fs(Continuation aC, Term a2, Term a3, Term a4, Term a5) {
			super(aC,a2,a3,a4,a5);
			isFunctionCall= false;
		}
	}
	public class Element5ff extends Element {
		public Element5ff(Continuation aC, PrologVariable a1, Term a2, Term a3, Term a4, Term a5, Term a6) {
			super(aC,a2,a3,a4,a5,a6);
			result= a1;
		}
	}
	public class Element5fs extends Element {
		public Element5fs(Continuation aC, Term a2, Term a3, Term a4, Term a5, Term a6) {
			super(aC,a2,a3,a4,a5,a6);
			isFunctionCall= false;
		}
	}
	public class Element6ff extends Element {
		public Element6ff(Continuation aC, PrologVariable a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7) {
			super(aC,a2,a3,a4,a5,a6,a7);
			result= a1;
		}
	}
	public class Element6fs extends Element {
		public Element6fs(Continuation aC, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7) {
			super(aC,a2,a3,a4,a5,a6,a7);
			isFunctionCall= false;
		}
	}
	public class Element7ff extends Element {
		public Element7ff(Continuation aC, PrologVariable a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8) {
			super(aC,a2,a3,a4,a5,a6,a7,a8);
			result= a1;
		}
	}
	public class Element7fs extends Element {
		public Element7fs(Continuation aC, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8) {
			super(aC,a2,a3,a4,a5,a6,a7,a8);
			isFunctionCall= false;
		}
	}
	public class Element8ff extends Element {
		public Element8ff(Continuation aC, PrologVariable a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9) {
			super(aC,a2,a3,a4,a5,a6,a7,a8,a9);
			result= a1;
		}
	}
	public class Element8fs extends Element {
		public Element8fs(Continuation aC, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9) {
			super(aC,a2,a3,a4,a5,a6,a7,a8,a9);
			isFunctionCall= false;
		}
	}
	public class Element9ff extends Element {
		public Element9ff(Continuation aC, PrologVariable a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9, Term a10) {
			super(aC,a2,a3,a4,a5,a6,a7,a8,a9,a10);
			result= a1;
		}
	}
	public class Element9fs extends Element {
		public Element9fs(Continuation aC, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9, Term a10) {
			super(aC,a2,a3,a4,a5,a6,a7,a8,a9,a10);
			isFunctionCall= false;
		}
	}
	public class Element10ff extends Element {
		public Element10ff(Continuation aC, PrologVariable a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9, Term a10, Term a11) {
			super(aC,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11);
			result= a1;
		}
	}
	public class Element10fs extends Element {
		public Element10fs(Continuation aC, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9, Term a10, Term a11) {
			super(aC,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11);
			isFunctionCall= false;
		}
	}
	public class Element extends Continuation {
		protected PrologVariable result;
		protected Term[] givenIndices;
		protected boolean isFunctionCall= true;
		//
		public Element(Continuation aC, Term... a2) {
			c0= aC;
			givenIndices= a2;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			IndexRange[] indexRanges= getIndicesRange(iX);
			boolean checkIndicesRange= getIndexChecking(iX);
			if (givenIndices.length != indexRanges.length) {
				if (checkIndicesRange) {
					throw new IllegalNumberOfIndices(givenIndices.length,indexRanges.length);
				} else {
					throw Backtracking.instance;
				}
			};
			boolean[] instantiateIndex= new boolean[givenIndices.length];
			int numberOfIndicesToBeInstantiated= 0;
			BigInteger[] currentIndexValue= new BigInteger[givenIndices.length];
			Term index= null;
			BigInteger maximumRadius= BigInteger.ZERO;
			try {
				for (int n=0; n < givenIndices.length; n++) {
					maximumRadius= maximumRadius.max(indexRanges[n].computeRadius());
					index= givenIndices[n].dereferenceValue(iX);
					if (index.thisIsFreeVariable()) {
						instantiateIndex[n]= true;
						numberOfIndicesToBeInstantiated++;
						currentIndexValue[n]= BigInteger.ZERO;
					} else {
						instantiateIndex[n]= false;
						BigInteger indexValue= index.getIntegerValue(iX);
						if (checkIndicesRange) {
							indexRanges[n].checkIndexValue(indexValue);
							currentIndexValue[n]= indexValue;
						} else {
							if (indexRanges[n].includesValue(indexValue)) {
								currentIndexValue[n]= indexValue;
							} else {
								throw Backtracking.instance;
							}
						}
					}
				}
			} catch (TermIsNotAnInteger e) {
				if (checkIndicesRange) {
					throw new WrongArgumentIsNotAnInteger(index);
				} else {
					throw Backtracking.instance;
				}
			};
			ChoisePoint newIx= new ChoisePoint(iX);
			if (numberOfIndicesToBeInstantiated==0) {
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: No indices are to be instantiated                            :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
ArrayIndices arrayIndices= new ArrayIndices(currentIndexValue);
Term value= accessArrayElement(arrayIndices,newIx);
if (isFunctionCall) {
	result.value= value;
};
c0.execute(newIx);
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else {
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: Several indices are to be instantiated                       :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
IndexRange[] rangesOfIndicesToBeInstantiated= new IndexRange[numberOfIndicesToBeInstantiated];
int counter1= 0;
for (int n=0; n < givenIndices.length; n++) {
	if (instantiateIndex[n]) {
		rangesOfIndicesToBeInstantiated[counter1++]= indexRanges[n];
	}
};
BigInteger radius= BigInteger.ONE.negate();
radiusLoop: while(true) {
	if (radius.compareTo(maximumRadius) >= 0) {
		break radiusLoop;
	};
	radius= radius.add(BigInteger.ONE);
	for (int numberOfSelectedIndices= numberOfIndicesToBeInstantiated; numberOfSelectedIndices > 0; numberOfSelectedIndices--) {
		if (radius.compareTo(BigInteger.ZERO) == 0) {
			if (numberOfSelectedIndices < numberOfIndicesToBeInstantiated) {
				continue;
			}
		};
		boolean[] bits= new boolean[numberOfIndicesToBeInstantiated];
		mainLoop: while(true) {
			for (int n=0; n <= numberOfIndicesToBeInstantiated; n++) {
				if (n==numberOfIndicesToBeInstantiated) {
					break mainLoop;
				};
				if (!bits[n]) {
					bits[n]= true;
					break;
				} else {
					bits[n]= false;
				}
			};
			int sum= 0;
			for (int n=0; n < numberOfIndicesToBeInstantiated; n++) {
				if (bits[n]) {
					sum++;
				}
			};
			if (sum != numberOfSelectedIndices) {
				continue;
			};
			BigInteger[] indices= new BigInteger[numberOfIndicesToBeInstantiated];
			for (int n=0; n < numberOfIndicesToBeInstantiated; n++) {
				if (bits[n]) {
					indices[n]= rangesOfIndicesToBeInstantiated[n].center.subtract(radius);
				} else {
					indices[n]= rangesOfIndicesToBeInstantiated[n].center.subtract(radius).add(BigInteger.ONE);
				}
			};
			shellCoveringLoop: while(true) {
// ====================================================================
boolean skipPass= false;
int counter2= 0;
for (int n=0; n < givenIndices.length; n++) {
	if (instantiateIndex[n]) {
		if (!indexRanges[n].includesValue(indices[counter2++])) {
			skipPass= true;
			break;
		}
	}
};
boolean processBacktracking= false;
if (!skipPass) {
	int counter3= 0;
	for (int n=0; n < givenIndices.length; n++) {
		if (instantiateIndex[n]) {
			currentIndexValue[n]= indices[counter3++];
			givenIndices[n].isInteger(currentIndexValue[n],newIx);
		}
	};
	ArrayIndices arrayIndices= new ArrayIndices(currentIndexValue);
	Term value= accessArrayElement(arrayIndices,newIx);
	if (isFunctionCall) {
		result.value= value;
	};
	try {
		c0.execute(newIx);
		return;
	} catch (Backtracking b) {
		processBacktracking= true;
	}
};
if (processBacktracking || skipPass) {
	if (isFunctionCall) {
		result.value= null;
	};
	if (newIx.isEnabled()) {
		newIx.freeTrail();
// --------------------------------------------------------------------
		if (radius.compareTo(BigInteger.ZERO)==0) {
			break shellCoveringLoop;
		};
		for (int n=0; n <= numberOfIndicesToBeInstantiated; n++) {
			if (n==numberOfIndicesToBeInstantiated) {
				break shellCoveringLoop;
			};
			BigInteger previousValue= indices[n];
			if (bits[n]) {
				indices[n]= rangesOfIndicesToBeInstantiated[n].reflectValue(previousValue);
				if (previousValue.compareTo(rangesOfIndicesToBeInstantiated[n].center) < 0) {
					break;
				} else {
					continue;
				}
			} else {
				BigInteger newValue= previousValue.add(BigInteger.ONE);
				if (newValue.compareTo(rangesOfIndicesToBeInstantiated[n].center.add(radius)) < 0) {
					indices[n]= newValue;
					break;
				} else {
					indices[n]= rangesOfIndicesToBeInstantiated[n].center.subtract(radius).add(BigInteger.ONE);
					continue;
				}
			}
		};
// --------------------------------------------------------------------
		continue;
	} else {
		throw Backtracking.instance;
	}
}
// ====================================================================
			}
		}
	}
};
throw Backtracking.instance;
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		}
	}
	protected Term accessArrayElement(ArrayIndices arrayIndices, ChoisePoint cp) throws Backtracking {
		SlotVariable variable= volume.get(arrayIndices);
		if (variable==null) {
			SlotVariable newSlot= new SlotVariable();
			variable= newSlot;
			volume.put(arrayIndices,variable);
			cp.pushTrail(new HashMapState(volume,arrayIndices,newSlot));
		};
		return variable;
	}
}
