// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.math.BigInteger;

public class PrologList extends Term {
	//
	private Term head;
	public Term tail;
	//
	public PrologList(Term aHead, Term aTail) {
		head= aHead;
		tail= aTail;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int hashCode() {
		int result= head.hashCode();
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				result= result + nextList.head.hashCode();
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					return result;
				}
			} else {
				result= result + nextItem.hashCode();
				return result;
			}
		}
	}
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToList(head,tail);
		} else {
			return false;
		}
	}
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithList(head,tail);
		} else {
			return 1;
		}
	}
	public boolean isEqualToList(Term h2, Term t2) {
		if (head.equals(h2)) {
			ChoisePoint cp= null;
			Term t1= tail;
			while (true) {
				try {
					Term h1= t1.getListHead(cp);
					h2= t2.getListHead(cp);
					if (h1.equals(h2)) {
						Term firstTerm;
						Term secondTerm;
						try {
							firstTerm= t1.getNextListTail(cp);
						} catch (EndOfList eol1) {
							try {
								secondTerm= t2.getNextListTail(cp);
								return false;
							} catch (EndOfList eol2) {
								return true;
							} catch (TermIsNotAList tnl2) {
								throw Backtracking.instance;
							}
						} catch (TermIsNotAList tnl1) {
							throw Backtracking.instance;
						};
						try {
							secondTerm= t2.getNextListTail(cp);
							t1= firstTerm;
							t2= secondTerm;
							continue;
						} catch (EndOfList eol3) {
							return false;
						} catch (TermIsNotAList tnl3) {
							throw Backtracking.instance;
						}
					} else {
						return false;
					}
				} catch (Backtracking b1) {
					return t1.equals(t2);
				}
			}
		} else {
			return false;
		}
	}
	public int compareWithList(Term h2, Term t2) {
		int result1= head.compare(h2);
		if (result1==0) {
			ChoisePoint cp= null;
			Term t1= tail;
			while (true) {
				try {
					Term h1= t1.getListHead(cp);
					h2= t2.getListHead(cp);
					int result2= h1.compare(h2);
					if (result2==0) {
						Term firstTerm;
						Term secondTerm;
						try {
							firstTerm= t1.getNextListTail(cp);
						} catch (EndOfList eol1) {
							try {
								secondTerm= t2.getNextListTail(cp);
								return -1;
							} catch (EndOfList eol2) {
								return 0;
							} catch (TermIsNotAList tnl2) {
								throw Backtracking.instance;
							}
						} catch (TermIsNotAList tnl1) {
							throw Backtracking.instance;
						};
						try {
							secondTerm= t2.getNextListTail(cp);
							t1= firstTerm;
							t2= secondTerm;
							continue;
						} catch (EndOfList eol3) {
							return 1;
						} catch (TermIsNotAList tnl3) {
							throw Backtracking.instance;
						}
					} else {
						return result2;
					}
				} catch (Backtracking b1) {
					return t1.compare(t2);
				}
			}
		} else {
			return result1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		return head;
	}
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		return tail;
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList {
		return head;
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList {
		return tail;
	}
	public Term getExistentHead() {
		return head;
	}
	public Term getExistentTail() {
		return tail;
	}
	public Term getOutputHead(ChoisePoint cp) {
		return head;
	}
	public Term getOutputTail(ChoisePoint cp) {
		return tail;
	}
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList {
		return head;
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList {
		return tail;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		head.unifyWith(aHead,cp);
		tail.unifyWith(aTail,cp);
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithList(head,tail,this,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		// head.registerVariables(process,isSuspending,isProtecting);
		// tail.registerVariables(process,isSuspending,isProtecting);
		head.registerVariables(process,isSuspending,isProtecting);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.registerVariables(process,isSuspending,isProtecting);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotConstructorArgument(nextList);
				}
			} else {
				nextItem.registerVariables(process,isSuspending,isProtecting);
				return;
			}
		}
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		// head.registerTargetWorlds(worlds,cp);
		// tail.registerTargetWorlds(worlds,cp);
		head.registerTargetWorlds(worlds,cp);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.registerTargetWorlds(worlds,cp);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					// throw new WrongArgumentIsNotConstructorArgument(nextList);
					return;
				}
			} else {
				nextItem.registerTargetWorlds(worlds,cp);
				return;
			}
		}
	}
	public PrologList circumscribe() {
		// return new PrologList(
		//	head.circumscribe(),
		//	tail.circumscribe());
		PrologList result= new PrologList(head.circumscribe(),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.circumscribe(),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					createdList.tail= PrologUnknownValue.instance;
					return result;
				}
			} else {
				createdList.tail= nextItem.circumscribe();
				return result;
			}
		}
	}
	public PrologList copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		// return new PrologList(
		//	head.copyValue(cp,mode),
		//	tail.copyValue(cp,mode));
		PrologList result= new PrologList(head.copyValue(cp,mode),null);
		PrologList createdList= result;
		Term nextItem= tail.dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.copyValue(cp,mode),null);
				createdList.tail= newList;
				nextItem= nextList.tail.dereferenceValue(cp);
				createdList= newList;
			} else {
				createdList.tail= nextItem.copyValue(cp,mode);
				return result;
			}
		}
	}
	public PrologList copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		// return new PrologList(
		//	head.copyGroundValue(cp),
		//	tail.copyGroundValue(cp));
		PrologList result= new PrologList(head.copyGroundValue(cp),null);
		PrologList createdList= result;
		Term nextItem= tail.dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.copyGroundValue(cp),null);
				createdList.tail= newList;
				nextItem= nextList.tail.dereferenceValue(cp);
				createdList= newList;
			} else {
				createdList.tail= nextItem.copyGroundValue(cp);
				return result;
			}
		}
	}
	public PrologList substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		// return new PrologList(
		//	head.substituteWorlds(map,cp),
		//	tail.substituteWorlds(map,cp));
		PrologList result= new PrologList(head.substituteWorlds(map,cp),null);
		PrologList createdList= result;
		Term nextItem= tail.dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.substituteWorlds(map,cp),null);
				createdList.tail= newList;
				nextItem= nextList.tail.dereferenceValue(cp);
				createdList= newList;
			} else {
				createdList.tail= nextItem.substituteWorlds(map,cp);
				return result;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareListWith(head,tail,iX,op);
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareWithBigInteger(a,iX,op);
		// tail.compareWithBigInteger(a,iX,op);
		head.compareWithBigInteger(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareWithBigInteger(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareWithBigInteger(a,iX,op);
				return;
			}
		}
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareWithLong(a,iX,op);
		// tail.compareWithLong(a,iX,op);
		head.compareWithLong(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareWithLong(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareWithLong(a,iX,op);
				return;
			}
		}
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareWithDouble(a,iX,op);
		// tail.compareWithDouble(a,iX,op);
		head.compareWithDouble(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareWithDouble(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareWithDouble(a,iX,op);
				return;
			}
		}
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareWithString(a,iX,op);
		// tail.compareWithString(a,iX,op);
		head.compareWithString(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareWithString(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareWithString(a,iX,op);
				return;
			}
		}
	}
	public void compareWithBinary(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareWithBinary(a,iX,op);
		// tail.compareWithBinary(a,iX,op);
		head.compareWithBinary(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareWithBinary(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareWithBinary(a,iX,op);
				return;
			}
		}
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareWithDate(a,iX,op);
		// tail.compareWithDate(a,iX,op);
		head.compareWithDate(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareWithDate(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareWithDate(a,iX,op);
				return;
			}
		}
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareTermWith(a,iX,op);
		// tail.compareTermWith(a,iX,op);
		head.compareTermWith(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareTermWith(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareTermWith(a,iX,op);
				return;
			}
		}
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareBigIntegerWith(a,iX,op);
		// tail.compareBigIntegerWith(a,iX,op);
		head.compareBigIntegerWith(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareBigIntegerWith(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareBigIntegerWith(a,iX,op);
				return;
			}
		}
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareLongWith(a,iX,op);
		// tail.compareLongWith(a,iX,op);
		head.compareLongWith(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareLongWith(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareLongWith(a,iX,op);
				return;
			}
		}
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareDoubleWith(a,iX,op);
		// tail.compareDoubleWith(a,iX,op);
		head.compareDoubleWith(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareDoubleWith(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareDoubleWith(a,iX,op);
				return;
			}
		}
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareStringWith(a,iX,op);
		// tail.compareStringWith(a,iX,op);
		head.compareStringWith(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareStringWith(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareStringWith(a,iX,op);
				return;
			}
		}
	}
	public void compareBinaryWith(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareBinaryWith(a,iX,op);
		// tail.compareBinaryWith(a,iX,op);
		head.compareBinaryWith(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareBinaryWith(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareBinaryWith(a,iX,op);
				return;
			}
		}
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareDateWith(a,iX,op);
		// tail.compareDateWith(a,iX,op);
		head.compareDateWith(a,iX,op);
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				nextList.head.compareDateWith(a,iX,op);
				nextItem= nextList.tail; // .dereferenceValue(cp);
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				nextItem.compareDateWith(a,iX,op);
				return;
			}
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		// head.compareTermWith(aHead,iX,op);
		// tail.compareTermWith(aTail,iX,op);
		head.compareTermWith(aHead,iX,op);
		Term t1= tail;
		while (true) {
			try {
				Term h1= t1.getListHead(iX);
				aHead= aTail.getListHead(iX);
				h1.compareTermWith(aHead,iX,op);
				Term firstTerm;
				Term secondTerm;
				try {
					firstTerm= t1.getNextListTail(iX);
				} catch (EndOfList eol1) {
					try {
						secondTerm= aTail.getNextListTail(iX);
						throw Backtracking.instance;
					} catch (EndOfList eol2) {
						return;
					} catch (TermIsNotAList tnl2) {
						throw Backtracking.instance;
					}
				} catch (TermIsNotAList tnl1) {
					t1.compareTermWith(aTail,iX,op);
					return;
				};
				try {
					secondTerm= aTail.getNextListTail(iX);
					t1= firstTerm;
					aTail= secondTerm;
					continue;
				} catch (EndOfList eol3) {
					throw Backtracking.instance;
				} catch (TermIsNotAList tnl3) {
					throw Backtracking.instance;
				}
			} catch (Backtracking b1) {
				t1.compare(aTail);
				return;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.reactListWith(head,tail,iX,op);
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactWithBigInteger(a,iX,op),
		//	tail.reactWithBigInteger(a,iX,op));
		PrologList result= new PrologList(head.reactWithBigInteger(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactWithBigInteger(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactWithBigInteger(a,iX,op);
				return result;
			}
		}
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactWithLong(a,iX,op),
		//	tail.reactWithLong(a,iX,op));
		PrologList result= new PrologList(head.reactWithLong(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactWithLong(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactWithLong(a,iX,op);
				return result;
			}
		}
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactWithDouble(a,iX,op),
		//	tail.reactWithDouble(a,iX,op));
		PrologList result= new PrologList(head.reactWithDouble(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactWithDouble(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactWithDouble(a,iX,op);
				return result;
			}
		}
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactWithString(a,iX,op),
		//	tail.reactWithString(a,iX,op));
		PrologList result= new PrologList(head.reactWithString(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactWithString(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactWithString(a,iX,op);
				return result;
			}
		}
	}
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactWithBinary(a,iX,op),
		//	tail.reactWithBinary(a,iX,op));
		PrologList result= new PrologList(head.reactWithBinary(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactWithBinary(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactWithBinary(a,iX,op);
				return result;
			}
		}
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactWithDate(a,iX,op),
		//	tail.reactWithDate(a,iX,op));
		PrologList result= new PrologList(head.reactWithDate(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactWithDate(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactWithDate(a,iX,op);
				return result;
			}
		}
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactWithTime(a,iX,op),
		//	tail.reactWithTime(a,iX,op));
		PrologList result= new PrologList(head.reactWithTime(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactWithTime(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactWithTime(a,iX,op);
				return result;
			}
		}
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactBigIntegerWith(a,iX,op),
		//	tail.reactBigIntegerWith(a,iX,op));
		PrologList result= new PrologList(head.reactBigIntegerWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactBigIntegerWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactBigIntegerWith(a,iX,op);
				return result;
			}
		}
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactLongWith(a,iX,op),
		//	tail.reactLongWith(a,iX,op));
		PrologList result= new PrologList(head.reactLongWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactLongWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactLongWith(a,iX,op);
				return result;
			}
		}
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactDoubleWith(a,iX,op),
		//	tail.reactDoubleWith(a,iX,op));
		PrologList result= new PrologList(head.reactDoubleWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactDoubleWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactDoubleWith(a,iX,op);
				return result;
			}
		}
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactStringWith(a,iX,op),
		//	tail.reactStringWith(a,iX,op));
		PrologList result= new PrologList(head.reactStringWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactStringWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactStringWith(a,iX,op);
				return result;
			}
		}
	}
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactBinaryWith(a,iX,op),
		//	tail.reactBinaryWith(a,iX,op));
		PrologList result= new PrologList(head.reactBinaryWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactBinaryWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactBinaryWith(a,iX,op);
				return result;
			}
		}
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactDateWith(a,iX,op),
		//	tail.reactDateWith(a,iX,op));
		PrologList result= new PrologList(head.reactDateWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactDateWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactDateWith(a,iX,op);
				return result;
			}
		}
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.reactTimeWith(a,iX,op),
		//	tail.reactTimeWith(a,iX,op));
		PrologList result= new PrologList(head.reactTimeWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.reactTimeWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactTimeWith(a,iX,op);
				return result;
			}
		}
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.reactWithTerm(head,iX,op),
			aTail.reactWithTerm(tail,iX,op));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.blitListWith(head,tail,iX,op);
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.blitWithBigInteger(a,iX,op),
		//	tail.blitWithBigInteger(a,iX,op));
		PrologList result= new PrologList(head.blitWithBigInteger(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.blitWithBigInteger(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.blitWithBigInteger(a,iX,op);
				return result;
			}
		}
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.blitWithLong(a,iX,op),
		//	tail.blitWithLong(a,iX,op));
		PrologList result= new PrologList(head.blitWithLong(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.blitWithLong(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.blitWithLong(a,iX,op);
				return result;
			}
		}
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.blitWithDouble(a,iX,op),
		//	tail.blitWithDouble(a,iX,op));
		PrologList result= new PrologList(head.blitWithDouble(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.blitWithDouble(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.blitWithDouble(a,iX,op);
				return result;
			}
		}
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.blitBigIntegerWith(a,iX,op),
		//	tail.blitBigIntegerWith(a,iX,op));
		PrologList result= new PrologList(head.blitBigIntegerWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.blitBigIntegerWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.blitBigIntegerWith(a,iX,op);
				return result;
			}
		}
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.blitLongWith(a,iX,op),
		//	tail.blitLongWith(a,iX,op));
		PrologList result= new PrologList(head.blitLongWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.blitLongWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.blitLongWith(a,iX,op);
				return result;
			}
		}
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		// return new PrologList(
		//	head.blitDoubleWith(a,iX,op),
		//	tail.blitDoubleWith(a,iX,op));
		PrologList result= new PrologList(head.blitDoubleWith(a,iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.blitDoubleWith(a,iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.blitDoubleWith(a,iX,op);
				return result;
			}
		}
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.blitWithTerm(head,iX,op),
			aTail.blitWithTerm(tail,iX,op));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		// return new PrologList(
		//	head.evaluate(iX,op),
		//	tail.evaluate(iX,op));
		PrologList result= new PrologList(head.evaluate(iX,op),null);
		PrologList createdList= result;
		Term nextItem= tail; // .dereferenceValue(cp);
		while (true) {
			if (nextItem instanceof PrologList) {
				PrologList nextList= (PrologList)nextItem;
				PrologList newList= new PrologList(nextList.head.evaluate(iX,op),null);
				createdList.tail= newList;
				nextItem= nextList.tail; // .dereferenceValue(cp);
				createdList= newList;
			} else if (nextItem instanceof PrologVariable) {
				PrologVariable nextList= (PrologVariable)nextItem;
				Term nextValue= nextList.getValueOfVariable();
				if (nextValue != null) {
					nextItem= nextValue;
					continue;
				} else {
					throw new WrongArgumentIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.evaluate(iX,op);
				return result;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder("[");
		buffer.append(head.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
		try {
			Term nextHead;
			Term currentTail= tail;
			try {
				while (true) {
					nextHead= currentTail.getNextListHeadSafely(cp);
					buffer.append(",");
					buffer.append(nextHead.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
					currentTail= currentTail.getNextListTailSafely(cp);
				}
			} catch (EndOfList e) {
				buffer.append("]");
				return buffer.toString();
			} catch (TermIsNotAList e) {
				buffer.append("|");
				buffer.append(currentTail.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
				buffer.append("]");
				return buffer.toString();
			}
		} catch (Throwable e) {
			buffer= new StringBuilder();
			return String.format("[%s|%s]",
				head.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder),
				tail.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder));
		}
	}
}
