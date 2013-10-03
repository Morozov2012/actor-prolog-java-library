// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.math.BigInteger;

public class PrologList extends Term {
	private Term head;
	public Term tail;
	public PrologList(Term aHead, Term aTail) {
		head= aHead;
		tail= aTail;
	}
	public int hashCode() {
		return head.hashCode() + tail.hashCode();
	}
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
	// "Unify with ..." functions
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		head.unifyWith(aHead,cp);
		tail.unifyWith(aTail,cp);
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithList(head,tail,this,cp);
	}
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		head.registerVariables(process,isSuspending,isProtecting);
		tail.registerVariables(process,isSuspending,isProtecting);
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		head.registerTargetWorlds(worlds,cp);
		tail.registerTargetWorlds(worlds,cp);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
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
	// Comparison operations
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareListWith(head,tail,iX,op);
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareWithBigInteger(a,iX,op);
		tail.compareWithBigInteger(a,iX,op);
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareWithLong(a,iX,op);
		tail.compareWithLong(a,iX,op);
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareWithDouble(a,iX,op);
		tail.compareWithDouble(a,iX,op);
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareWithString(a,iX,op);
		tail.compareWithString(a,iX,op);
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareWithDate(a,iX,op);
		tail.compareWithDate(a,iX,op);
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareTermWith(a,iX,op);
		tail.compareTermWith(a,iX,op);
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareBigIntegerWith(a,iX,op);
		tail.compareBigIntegerWith(a,iX,op);
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareLongWith(a,iX,op);
		tail.compareLongWith(a,iX,op);
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareDoubleWith(a,iX,op);
		tail.compareDoubleWith(a,iX,op);
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareStringWith(a,iX,op);
		tail.compareStringWith(a,iX,op);
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareDateWith(a,iX,op);
		tail.compareDateWith(a,iX,op);
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		head.compareTermWith(aHead,iX,op);
		tail.compareTermWith(aTail,iX,op);
	}
	// Arithmetic operations
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactWithString(a,iX,op);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.reactStringWith(a,iX,op);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
	// Bitwise operations
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
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
	// Unary operations
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
				if (nextList.value != null) {
					nextItem= nextList.value;
					continue;
				} else {
					throw new WrongTermIsNotBoundVariable(nextList);
				}
			} else {
				createdList.tail= nextItem.evaluate(iX,op);
				return result;
			}
		}
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder("[");
		buffer.append(head.toString(cp,true,provideStrictSyntax,encoder));
		try {
			Term nextHead;
			Term currentTail= tail;
			try {
				while (true) {
					nextHead= currentTail.getNextListHeadSafely(cp);
					buffer.append(",");
					buffer.append(nextHead.toString(cp,true,provideStrictSyntax,encoder));
					currentTail= currentTail.getNextListTailSafely(cp);
				}
			} catch (EndOfList e) {
				buffer.append("]");
				return buffer.toString();
			} catch (TermIsNotAList e) {
				buffer.append("|");
				buffer.append(currentTail.toString(cp,true,provideStrictSyntax,encoder));
				buffer.append("]");
				return buffer.toString();
			}
		} catch (Throwable e) {
			buffer= new StringBuilder();
			return String.format("[%s|%s]",
				head.toString(cp,true,provideStrictSyntax,encoder),
				tail.toString(cp,true,provideStrictSyntax,encoder));
		}
	}
}
