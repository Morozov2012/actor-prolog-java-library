// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.records;

import morozov.run.*;
import morozov.terms.*;

import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

public class DatabaseRecord {
	public Term value;
	public DatabaseRecord previousRecord;
	public DatabaseRecord nextRecord;
	public DatabaseRecord(Term item) {
		value= item;
	}
	// public DatabaseRecord(Term item, DatabaseRecord firstRecord) {
	//	value= item;
	//	nextRecord= firstRecord;
	// }
	public void retractAll() {
		value= null;
		previousRecord= null;
		if (nextRecord != null) {
			nextRecord.retractAll();
			nextRecord= null;
		}
	}
	public void retrieveComparablePairs(long key, ArrayList<ComparablePair> pairs, ArrayList<Term> rest, ChoisePoint cp) {
		if (value != null) {
			try {
				Term element= value.getNamedElement(key,cp);
				pairs.add(new ComparablePair(element,value));
			} catch (Backtracking e) {
				rest.add(value);
			}
		};
		if (nextRecord != null) {
			nextRecord.retrieveComparablePairs(key,pairs,rest,cp);
		}
	}
	public void saveToTextBuffer(StringBuilder textBuffer, ChoisePoint cp, CharsetEncoder encoder) {
		if (value != null) {
			textBuffer.append(value.toString(cp,false,true,encoder));
			textBuffer.append(";\n");
		};
		if (nextRecord != null) {
			nextRecord.saveToTextBuffer(textBuffer,cp,encoder);
		}
	}
}
