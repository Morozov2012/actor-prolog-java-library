// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.domains.*;
import morozov.domains.errors.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.Serializable;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

public class DatabaseRecord implements Serializable {
	public Term content;
	public DatabaseRecord previousRecord;
	public DatabaseRecord nextRecord;
	//
	public DatabaseRecord(Term item) {
		content= item;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void checkDomain(PrologDomain domain, ChoisePoint iX) {
		DatabaseRecord currentRecord= this;
		while (true) {
			if (!domain.coversTerm(currentRecord.content,iX,false)) {
				throw new WrongTermDoesNotBelongToDomain(currentRecord.content);
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
	//
	///////////////////////////////////////////////////////////////
	//
	public void retractAll() {
		DatabaseRecord currentRecord= this;
		while (true) {
			currentRecord.content= null;
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
		DatabaseRecord currentRecord= this;
		while (true) {
			Term currentValue= currentRecord.content;
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
		saveToTextBuffer(textBuffer,false,cp,encoder);
	}
	public void saveToTextBuffer(StringBuilder textBuffer, boolean encloseRecords, ChoisePoint cp, CharsetEncoder encoder) {
		DatabaseRecord currentRecord= this;
		String textRecord= "";
		if (encloseRecords) {
			textRecord= SymbolNames.retrieveSymbolName(SymbolCodes.symbolCode_E_record).toRawString(encoder);
		};
		while (true) {
			Term currentValue= currentRecord.content;
			if (currentValue != null) {
				if (encloseRecords) {
					textBuffer.append(textRecord);
					textBuffer.append("(");
				};
				textBuffer.append(currentValue.toString(cp,false,true,true,encoder));
				if (encloseRecords) {
                                	textBuffer.append(");\n");
				} else {
                                	textBuffer.append(";\n");
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
	public void copyContent(DatabaseTable table, ActiveWorld currentProcess, ChoisePoint iX) {
		DatabaseRecord currentRecord= this;
		while (true) {
			Term currentValue= currentRecord.content;
			if (currentValue != null) {
				table.appendRecord(currentValue,currentProcess,false,iX,true);
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
