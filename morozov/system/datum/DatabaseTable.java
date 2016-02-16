// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import target.*;

import morozov.domains.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.run.errors.*;
import morozov.syntax.*;
import morozov.syntax.errors.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.datum.errors.*;
import morozov.system.datum.signals.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;

public class DatabaseTable implements Serializable, Cloneable {
	//
	protected DatabaseRecord content;
	protected DatabaseRecord ultimateRecord;
	//
	protected PrologDomain prologDomain;
	protected HashMap<String,PrologDomain> localDomainTable= new HashMap<String,PrologDomain>();
	//
	protected String currentEntryName;
	protected boolean reuseKeyNumbers= true;
	//
	protected transient DatabaseTableContainer container;
	protected transient boolean tableContainsWorlds;
	//
	protected static Term databaseTerm= new PrologSymbol(SymbolCodes.symbolCode_E_Database);
	//
	///////////////////////////////////////////////////////////////
	//
	public DatabaseTable(PrologDomain domain, boolean reuseKN) {
		prologDomain= domain;
		reuseKeyNumbers= reuseKN;
		// isShared= shared;
		prologDomain.collectLocalDomainTable(localDomainTable);
	}
	public DatabaseTable(PrologDomain domain, boolean reuseKN, HashMap<String,PrologDomain> domainTable) {
		prologDomain= domain;
		reuseKeyNumbers= reuseKN;
		// isShared= shared;
		localDomainTable.clear();
		localDomainTable.putAll(domainTable);
		prologDomain.acceptLocalDomainTable(localDomainTable);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setContainer(DatabaseTableContainer c) {
		container= c;
	}
	//
	public void setReuseKeyNumbers(boolean reuseKN, ActiveWorld currentProcess, boolean checkPrivileges) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		reuseKeyNumbers= reuseKN;
	}
	//
	public String getEntryName() {
		return currentEntryName;
	}
	//
	public Term getType() {
		return databaseTerm;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void declareWhetherAWorldIsDetected(boolean mode) {
		tableContainsWorlds= mode;
	}
	//
	public void checkDomain(PrologDomain domain, ChoisePoint iX) {
		try {
			prologDomain.isEqualTo(domain,new HashSet<PrologDomainPair>());
		} catch (PrologDomainsAreNotEqual e) {
			throw new DatabaseTableHasDifferentDomain(prologDomain,domain);
		};
		if (tableContainsWorlds) {
			if (content != null) {
				content.checkDomain(domain,iX);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DatabaseRecord insertRecord(Term copy, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX, boolean isInnerOperation) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		DatabaseRecord newRecord= new DatabaseRecord(copy);
		if (content != null) {
			content.previousRecord= newRecord;
			newRecord.nextRecord= content;
			content= newRecord;
		} else {
			content= newRecord;
			ultimateRecord= content;
		};
		return newRecord;
	}
	//
	public DatabaseRecord appendRecord(Term copy, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX, boolean isInnerOperation) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		DatabaseRecord newRecord= new DatabaseRecord(copy);
		if (content != null) {
			ultimateRecord.nextRecord= newRecord;
			newRecord.previousRecord= ultimateRecord;
			ultimateRecord= newRecord;
		} else {
			content= newRecord;
			ultimateRecord= content;
		};
		return newRecord;
	}
	//
	public void findRecord(PrologVariable outputResult, Term inputResult, boolean hasOutputArgument, Continuation c0, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		DatabaseRecord currentRecord= content;
		ChoisePoint newIx= new ChoisePoint(iX);
		while (true) {
			if (currentRecord != null) {
				if (hasOutputArgument) {
					outputResult.value= currentRecord.value;
				} else {
					try {
						inputResult.unifyWith(currentRecord.value,newIx);
					} catch (Backtracking b) {
						newIx.freeTrail();
						currentRecord= getNextValidRecord(currentRecord);
						continue;
					}
				};
				try {
					c0.execute(newIx);
				} catch (Backtracking b) {
					if (hasOutputArgument) {
						outputResult.value= null;
					};
					if (newIx.isEnabled()) {
						newIx.freeTrail();
						container.claimReadingAccess(currentProcess,checkPrivileges);
						currentRecord= getNextValidRecord(currentRecord);
						continue;
					} else {
						throw Backtracking.instance;
					}
				};
				return;
			} else {
				if (hasOutputArgument) {
					outputResult.value= null;
				};
				throw Backtracking.instance;
			}
		}
	}
	//
	public void matchRecord(PrologVariable result, Term pattern, boolean isFunctionCall, Continuation c0, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		DatabaseRecord currentRecord= content;
		ChoisePoint newIx= new ChoisePoint(iX);
		// 2015.02.01 This check is not implemented in VIP!
		if (!prologDomain.coversTerm(pattern,newIx,true)) {
			throw new DatabaseSearchPatternIsOfWrongDomain();
		};
		currentRecord= findMatch(currentRecord,pattern,newIx);
		while (true) {
			if (currentRecord != null) {
				if (isFunctionCall) {
					result.value= currentRecord.value;
				};
				try {
					c0.execute(newIx);
				} catch (Backtracking b) {
					if (isFunctionCall) {
						result.value= null;
					};
					if (newIx.isEnabled()) {
						newIx.freeTrail();
						if (currentRecord==null) {
							throw Backtracking.instance;
						} else {
							container.claimReadingAccess(currentProcess,checkPrivileges);
							currentRecord= currentRecord.nextRecord;
						};
						currentRecord= findMatch(currentRecord,pattern,newIx);
						continue;
					} else {
						throw Backtracking.instance;
					}
				};
				return;
			} else {
				if (isFunctionCall) {
					result.value= null;
				};
				throw Backtracking.instance;
			}
		}
	}
	//
	public void retractRecord(PrologVariable outputResult, Term inputResult, boolean hasOutputArgument, Continuation c0, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws Backtracking {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		DatabaseRecord currentRecord= content;
		ChoisePoint newIx= new ChoisePoint(iX);
		while (true) {
			if (currentRecord != null) {
				if (hasOutputArgument) {
					outputResult.value= currentRecord.value;
				} else {
					try {
						inputResult.unifyWith(currentRecord.value,newIx);
					} catch (Backtracking b) {
						newIx.freeTrail();
						currentRecord= getNextValidRecord(currentRecord);
						continue;
					}
				};
				retractCurrentRecord(currentRecord,currentProcess,checkPrivileges,iX);
				try {
					c0.execute(newIx);
				} catch (Backtracking b) {
					if (hasOutputArgument) {
						outputResult.value= null;
					};
					if (newIx.isEnabled()) {
						newIx.freeTrail();
						currentRecord= getNextValidRecord(currentRecord);
						continue;
					} else {
						throw Backtracking.instance;
					}
				};
				return;
			} else {
				if (hasOutputArgument) {
					outputResult.value= null;
				};
				throw Backtracking.instance;
			}
		}
	}
	//
	public void retractAll(ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		if (content != null) {
			content.retractAll();
			content= null;
			ultimateRecord= null;
		}
	}
	public void retractAll(Term pattern, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		DatabaseRecord currentRecord= content;
		ChoisePoint newIx= new ChoisePoint(iX);
		if (!prologDomain.coversTerm(pattern,newIx,true)) {
			throw new DatabaseSearchPatternIsOfWrongDomain();
		};
		while (true) {
			if (currentRecord != null) {
				if (currentRecord.value != null) {
					try {
						pattern.unifyWith(currentRecord.value,newIx);
					} catch (Backtracking b1) {
						newIx.freeTrail();
						try {
							currentRecord= getNextValidRecord(currentRecord);
						} catch (Backtracking b2) {
							return;
						};
						continue;
					};
					newIx.freeTrail();
					retractCurrentRecord(currentRecord,currentProcess,false,iX);
				};
				try {
					currentRecord= getNextValidRecord(currentRecord);
				} catch (Backtracking b3) {
					return;
				};
			} else {
				return;
			}
		}
	}
	//
	public void sortBy(Term targetKey, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		long key= DatabaseUtils.termToSortingKey(targetKey,iX);
		ArrayList<ComparablePair> pairs= new ArrayList<ComparablePair>();
		ArrayList<Term> rest= new ArrayList<Term>();
		content.retrieveComparablePairs(key,pairs,rest,iX);
		Collections.sort(pairs);
		if (content != null) {
			content.retractAll();
			content= null;
			ultimateRecord= null;
		};
		for (int k=0; k < pairs.size(); k++) {
			Term newItem= pairs.get(k).value;
			if (content != null) {
				DatabaseRecord newRecord= new DatabaseRecord(newItem);
				ultimateRecord.nextRecord= newRecord;
				newRecord.previousRecord= ultimateRecord;
				ultimateRecord= newRecord;
			} else {
				content= new DatabaseRecord(newItem);
				ultimateRecord= content;
			}
		};
		for (int k=0; k < rest.size(); k++) {
			Term newItem= rest.get(k);
			if (content != null) {
				DatabaseRecord newRecord= new DatabaseRecord(newItem);
				ultimateRecord.nextRecord= newRecord;
				newRecord.previousRecord= ultimateRecord;
				ultimateRecord= newRecord;
			} else {
				content= new DatabaseRecord(newItem);
				ultimateRecord= content;
			}
		}
	}
	//
	public void saveContent(ExtendedFileName fileName, CharacterSet requestedCharacterSet, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimReadingAccess(currentProcess,checkPrivileges);
		StringBuilder textBuffer= new StringBuilder();
		if (content != null) {
			if (requestedCharacterSet.isDummy()) {
				content.saveToTextBuffer(textBuffer,iX,null);
			} else {
				CharsetEncoder encoder= requestedCharacterSet.toCharSet().newEncoder();
				content.saveToTextBuffer(textBuffer,iX,encoder);
			}
		};
		try {
			fileName.create_BAK_File();
			fileName.writeTextFile(textBuffer.toString(),requestedCharacterSet);
		} catch (IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void saveToTextBuffer(StringBuilder textBuffer, ChoisePoint iX, CharsetEncoder encoder) {
		String textReuseKeyNumbers= SymbolNames.retrieveSymbolName(SymbolCodes.symbolCode_E_reuse_key_numbers).toRawString(encoder);
		textBuffer.append(textReuseKeyNumbers);
		textBuffer.append("(\'");
		textBuffer.append(YesNo.boolean2StringYesNo(reuseKeyNumbers));
		textBuffer.append("\');\n");
		String textTargetDomain= SymbolNames.retrieveSymbolName(SymbolCodes.symbolCode_E_target_domain).toRawString(encoder);
		textBuffer.append(textTargetDomain);
		textBuffer.append("(");
		textBuffer.append(prologDomain.toString(encoder));
		textBuffer.append(");\n");
		String textDomain= SymbolNames.retrieveSymbolName(SymbolCodes.symbolCode_E_domain).toRawString(encoder);
		Set<String> keys= localDomainTable.keySet();
		Iterator<String> iterator= keys.iterator();
		while (iterator.hasNext()) {
			String key= iterator.next();
			PrologDomain localDomain= localDomainTable.get(key);
			textBuffer.append(textDomain);
			textBuffer.append("(\"");
			String textKey= FormatOutput.encodeString(key,false,encoder);
			textBuffer.append(textKey);
			textBuffer.append("\",");
			textBuffer.append(localDomain.toString(encoder));
			textBuffer.append(");\n");
		};
		if (content != null) {
			content.saveToTextBuffer(textBuffer,true,iX,encoder);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void loadContent(String textBuffer, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) throws LexicalScannerError, ParserError, DatabaseRecordDoesNotBelongToDomain {
		// String textBuffer= fileName.getTextData(timeout,requestedCharacterSet,staticContext);
		Parser parser= new Parser(true);
		Term[] terms= parser.stringToTerms(textBuffer);
		boolean optimizeSets= DefaultOptions.underdeterminedSetsOptimizationIsEnabled;
		for (int k=0; k < terms.length; k++) {
			Term newItem= terms[k];
			try {
				if (optimizeSets) {
					newItem= prologDomain.checkAndOptimizeTerm(newItem,iX);
				} else {
					newItem= prologDomain.checkTerm(newItem,iX);
				};
				appendRecord(newItem,currentProcess,false,iX,false);
			} catch (DomainAlternativeDoesNotCoverTerm e) {
				throw new DatabaseRecordDoesNotBelongToDomain(newItem,textBuffer.toString(),e.getPosition());
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected DatabaseRecord getNextValidRecord(DatabaseRecord currentRecord) throws Backtracking {
		if (currentRecord==null) {
			throw Backtracking.instance;
		} else {
			currentRecord= currentRecord.nextRecord;
		};
		while (true) {
			if (currentRecord==null) {
				throw Backtracking.instance;
			} else if (currentRecord.value != null) {
				return currentRecord;
			} else {
				DatabaseRecord nextRecord= currentRecord.nextRecord;
				if (nextRecord != null) {
					currentRecord= nextRecord;
					continue;
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	protected DatabaseRecord findMatch(DatabaseRecord currentRecord, Term pattern, ChoisePoint iX) throws Backtracking {
		while (true) {
			if (currentRecord==null) {
				throw Backtracking.instance;
			} else if (currentRecord.value != null) {
				try {
					currentRecord.value.unifyWith(pattern,iX);
					// iX.freeTrail();
					return currentRecord;
				} catch (Backtracking b) {
					iX.freeTrail();
				}
			};
			DatabaseRecord nextRecord= currentRecord.nextRecord;
			if (nextRecord != null) {
				currentRecord= nextRecord;
				continue;
			} else {
				throw Backtracking.instance;
			}
		}
	}
	//
	protected void retractCurrentRecord(DatabaseRecord currentRecord, ActiveWorld currentProcess, boolean checkPrivileges, ChoisePoint iX) {
		container.claimModifyingAccess(currentProcess,checkPrivileges);
		DatabaseRecord previousRecord= currentRecord.previousRecord;
		DatabaseRecord nextRecord= currentRecord.nextRecord;
		if (previousRecord != null) {
			previousRecord.nextRecord= nextRecord;
		} else {
			content= nextRecord;
		};
		if (nextRecord != null) {
			nextRecord.previousRecord= previousRecord;
		} else {
			ultimateRecord= previousRecord;
		};
		currentRecord.value= null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void acceptAttributes(String entryName, DatabaseTableContainer c) {
		currentEntryName= entryName;
		container= c;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// private void writeObject(ObjectOutputStream stream) throws IOException {
	//	stream.defaultWriteObject();
	// }
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		if (stream instanceof DataStoreInputStream) {
			DataStoreInputStream dataStream= (DataStoreInputStream)stream;
			tableContainsWorlds= false;
			dataStream.beginDatabaseTable(this);
			stream.defaultReadObject();
			dataStream.endDatabaseTable(this);
		} else {
			stream.defaultReadObject();
		};
		prologDomain.acceptLocalDomainTable(localDomainTable);
		Set<String> keys= localDomainTable.keySet();
		Iterator<String> iterator= keys.iterator();
		while (iterator.hasNext()) {
			String key= iterator.next();
			PrologDomain localDomain= localDomainTable.get(key);
			localDomain.acceptLocalDomainTable(localDomainTable);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Object clone() {
		DatabaseTable o;
		try {
			o= (DatabaseTable)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new CloningError();
		};
		o.content= null;
		o.ultimateRecord= null;
		o.currentEntryName= null;
		return o;
	}
	public void copyContent(DatabaseTable copyOfTable, ActiveWorld currentProcess, ChoisePoint iX) {
		if (content != null) {
			content.copyContent(copyOfTable,currentProcess,iX);
		}
	}
}