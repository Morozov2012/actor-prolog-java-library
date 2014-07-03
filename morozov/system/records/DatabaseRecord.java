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
	public void retractAll() {
		DatabaseRecord currentRecord= this;
		while (true) {
			currentRecord.value= null;
			currentRecord.previousRecord= null;
			DatabaseRecord subsequentRecord= currentRecord.nextRecord;
			currentRecord.nextRecord= null;
			if (subsequentRecord != null) {
				currentRecord= subsequentRecord;
				continue;
			} else {
				break;
			}
		}
	}
	public void retrieveComparablePairs(long key, ArrayList<ComparablePair> pairs, ArrayList<Term> rest, ChoisePoint cp) {
		// if (value != null) {
		//	try {
		//		Term element= value.getNamedElement(key,cp);
		//		pairs.add(new ComparablePair(element,value));
		//	} catch (Backtracking e) {
		//		rest.add(value);
		//	}
		// };
		// if (nextRecord != null) {
		//	nextRecord.retrieveComparablePairs(key,pairs,rest,cp);
		// }
		DatabaseRecord currentRecord= this;
		while (true) {
			Term currentValue= currentRecord.value;
			if (currentValue != null) {
				try {
					Term element= currentValue.getNamedElement(key,cp);
					pairs.add(new ComparablePair(element,currentValue));
				} catch (Backtracking e) {
					rest.add(currentValue);
				}
			};
			DatabaseRecord subsequentRecord= currentRecord.nextRecord;
			if (subsequentRecord != null) {
				currentRecord= subsequentRecord;
				continue;
			} else {
				break;
			}
		}
	}
	public void saveToTextBuffer(StringBuilder textBuffer, ChoisePoint cp, CharsetEncoder encoder) {
		// if (value != null) {
		//	textBuffer.append(value.toString(cp,false,true,encoder));
		//	textBuffer.append(";\n");
		// };
		// if (nextRecord != null) {
		//	nextRecord.saveToTextBuffer(textBuffer,cp,encoder);
		// }
		DatabaseRecord currentRecord= this;
		while (true) {
			Term currentValue= currentRecord.value;
			if (currentValue != null) {
				textBuffer.append(currentValue.toString(cp,false,true,encoder));
				textBuffer.append(";\n");
			};
			DatabaseRecord subsequentRecord= currentRecord.nextRecord;
			if (subsequentRecord != null) {
				currentRecord= subsequentRecord;
				continue;
			} else {
				break;
			}
		}
	}
}
