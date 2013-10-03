// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.domains.*;
import morozov.domains.errors.*;
import morozov.domains.signals.*;
import morozov.run.*;
import morozov.system.checker.*;
import morozov.system.checker.errors.*;
import morozov.system.checker.signals.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.records.*;
import morozov.system.records.errors.*;
import morozov.system.signals.*;
import morozov.syntax.*;
import morozov.syntax.errors.*;
import morozov.syntax.scanner.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.CharsetEncoder;
import java.nio.file.DirectoryNotEmptyException;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Database extends DataAbstraction {
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	protected DatabaseRecord content;
	protected DatabaseRecord ultimateRecord;
	//
	protected String recentErrorText;
	protected long recentErrorPosition= -1;
	protected Throwable recentErrorException;
	//
	protected static final int defaultWaitingInterval= -1;
	//
	abstract protected Term getBuiltInSlot_E_name();
	abstract protected Term getBuiltInSlot_E_extension();
	abstract protected Term getBuiltInSlot_E_max_waiting_time();
	abstract protected Term getBuiltInSlot_E_character_set();
	abstract protected Term getBuiltInSlot_E_backslash_always_is_separator();
	abstract protected PrologDomain getBuiltInSlotDomain_E_target_data();
	//
	public void insert1s(ChoisePoint iX, Term a1) {
		Term copy= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		insertRecord(copy);
	}
	//
	protected void insertRecord(Term copy) {
		DatabaseRecord newRecord= new DatabaseRecord(copy);
		if (content != null) {
			content.previousRecord= newRecord;
			newRecord.nextRecord= content;
			content= newRecord;
		} else {
			content= newRecord;
			ultimateRecord= content;
		};
		updateDatabaseHash(copy,newRecord);
	}
	public void append1s(ChoisePoint iX, Term a1) {
		Term copy= a1.copyValue(iX,TermCircumscribingMode.PROHIBIT_FREE_VARIABLES);
		appendRecord(copy);
	}
	//
	protected void appendRecord(Term copy) {
		DatabaseRecord newRecord= new DatabaseRecord(copy);
		if (content != null) {
			ultimateRecord.nextRecord= newRecord;
			newRecord.previousRecord= ultimateRecord;
			ultimateRecord= newRecord;
		} else {
			content= newRecord;
			ultimateRecord= content;
		};
		updateDatabaseHash(copy,newRecord);
	}
	//
	protected void updateDatabaseHash(Term copy, DatabaseRecord record) {
	}
	//
	public class Find1s extends Continuation {
		// private Continuation c0;
		protected PrologVariable outputResult;
		protected Term inputResult;
		protected boolean hasOutputArgument;
		//
		public Find1s(Continuation aC, PrologVariable a1) {
			c0= aC;
			outputResult= a1;
			hasOutputArgument= true;
		}
		public Find1s(Continuation aC, Term a1) {
			c0= aC;
			inputResult= a1;
			hasOutputArgument= false;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			DatabaseRecord currentRecord= content;
			ChoisePoint newIx= new ChoisePoint(iX);
			while(true) {
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
					// currentRecord= currentRecord.nextRecord;
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
	}
	//
	public class Match1ff extends Match {
		public Match1ff(Continuation aC, PrologVariable a1, Term a2) {
			super(aC,a2);
			result= a1;
			isFunctionCall= true;
		}
	}
	public class Match1fs extends Match {
		public Match1fs(Continuation aC, Term a2) {
			super(aC,a2);
			isFunctionCall= false;
		}
	}
	public class Match extends Continuation {
		// private Continuation c0;
		protected PrologVariable result;
		protected Term pattern;
		protected boolean isFunctionCall;
		//
		public Match(Continuation aC, Term aP) {
			c0= aC;
			pattern= aP;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			DatabaseRecord currentRecord= content;
			ChoisePoint newIx= new ChoisePoint(iX);
			PrologDomain domainItem= getBuiltInSlotDomain_E_target_data();
			if (!domainItem.coversTerm(pattern,newIx,true)) {
				throw new DatabaseSearchPatternIsOfWrongDomain();
			};
			currentRecord= findMatch(currentRecord,pattern,newIx);
			while(true) {
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
	}
	//
	public class Retract1s extends Continuation {
		// private Continuation c0;
		protected PrologVariable outputResult;
		protected Term inputResult;
		protected boolean hasOutputArgument;
		//
		public Retract1s(Continuation aC, PrologVariable a1) {
			c0= aC;
			outputResult= a1;
			hasOutputArgument= true;
		}
		public Retract1s(Continuation aC, Term a1) {
			c0= aC;
			inputResult= a1;
			hasOutputArgument= false;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			DatabaseRecord currentRecord= content;
			ChoisePoint newIx= new ChoisePoint(iX);
			while(true) {
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
					retractCurrentRecord(currentRecord);
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
	}
	//
	// public void clear0s(ChoisePoint iX) {
	//	if (content != null) {
	//		content.retractAll();
	//		content= null;
	//		ultimateRecord= null;
	//	}
	// }
	public void retractAll0s(ChoisePoint iX) {
		if (content != null) {
			content.retractAll();
			content= null;
			ultimateRecord= null;
		}
	}
	public void retractAll1s(ChoisePoint iX, Term a1) {
		DatabaseRecord currentRecord= content;
		ChoisePoint newIx= new ChoisePoint(iX);
		PrologDomain domainItem= getBuiltInSlotDomain_E_target_data();
		if (!domainItem.coversTerm(a1,newIx,true)) {
			throw new DatabaseSearchPatternIsOfWrongDomain();
		};
		while(true) {
			if (currentRecord != null) {
				if (currentRecord.value != null) {
					try {
						a1.unifyWith(currentRecord.value,newIx);
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
					retractCurrentRecord(currentRecord);
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
	protected void retractCurrentRecord(DatabaseRecord currentRecord) {
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
	protected DatabaseRecord getNextValidRecord(DatabaseRecord currentRecord) throws Backtracking {
		if (currentRecord==null) {
			throw Backtracking.instance;
		} else {
			currentRecord= currentRecord.nextRecord;
		};
		while(true) {
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
	public void sortBy1s(ChoisePoint iX, Term targetKey) {
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
	public void save1s(ChoisePoint iX, Term name) {
		String fileName= retrieveDatabaseName(name,iX);
		saveContent(fileName,iX);
	}
	public void save0s(ChoisePoint iX) {
		String fileName= retrieveDatabaseName(iX);
		saveContent(fileName,iX);
	}
	public void saveContent(String fileName, ChoisePoint iX) {
		CharacterSet requestedCharacterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		StringBuilder textBuffer= new StringBuilder();
		// CharsetEncoder encoder= Charset.defaultCharset().newEncoder();
		if (content != null) {
			if (requestedCharacterSet.isDummy()) {
				content.saveToTextBuffer(textBuffer,iX,null);
			} else {
				CharsetEncoder encoder= requestedCharacterSet.toCharSet().newEncoder();
				content.saveToTextBuffer(textBuffer,iX,encoder);
			}
		};
		try {
			// fileName= FileUtils.replaceBackslashes(fileName,backslashIsSeparator);
			FileUtils.create_BAK_File(fileName,backslashIsSeparator);
			FileUtils.writeTextFile(textBuffer.toString(),fileName,requestedCharacterSet);
		} catch (IOException e) {
			throw new FileInputOutputError(fileName,e);
		}
	}
	//
	public void load1s(ChoisePoint iX, Term name) {
		// String fileName= retrieveDatabaseName(name,iX);
		URI uri= retrieveLocationURI(name,iX);
		loadContent(uri,iX);
	}
	public void load0s(ChoisePoint iX) {
		// String fileName= retrieveDatabaseName(iX);
		URI uri= retrieveLocationURI(iX);
		loadContent(uri,iX);
	}
	public void loadContent(URI uri, ChoisePoint iX) {
		recentErrorText= "";
		recentErrorPosition= -1;
		recentErrorException= null;
		CharacterSet requestedCharacterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
		int timeout= retrieveMaxWaitingTime(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			try {
				String textBuffer= getContentOfResource(uri,requestedCharacterSet,timeout,backslashIsSeparator);
				Parser parser= new Parser(true);
				Term[] terms;
				try {
					terms= parser.stringToTerms(textBuffer);
				} catch (LexicalScannerError e) {
					long errorPosition= e.getPosition();
					recentErrorText= textBuffer.toString();
					recentErrorPosition= errorPosition;
					recentErrorException= e;
					throw e;
				} catch (ParserError e) {
					long errorPosition= e.getPosition();
					recentErrorText= textBuffer.toString();
					recentErrorPosition= errorPosition;
					recentErrorException= e;
					throw e;
				};
				PrologDomain domainItem= getBuiltInSlotDomain_E_target_data();
				boolean optimizeSets= DefaultOptions.underdeterminedSetsOptimizationIsEnabled;
				for (int k=0; k < terms.length; k++) {
					Term newItem= terms[k];
					try {
						if (optimizeSets) {
							newItem= domainItem.checkAndOptimizeTerm(newItem,iX);
						} else {
							newItem= domainItem.checkTerm(newItem,iX);
						};
						DatabaseRecord newRecord= new DatabaseRecord(newItem);
						insertNewItem(newItem,newRecord);
					} catch (DomainAlternativeDoesNotCoverTerm e) {
						long errorPosition= e.getPosition();
						recentErrorText= textBuffer.toString();
						recentErrorPosition= errorPosition;
						recentErrorException= e;
						throw new WrongTermDoesNotBelongToDomain(newItem);
					}
				}
			// } catch (URISyntaxException e) {
			//	throw new WrongTermIsMalformedURL(e);
			} catch (CannotRetrieveContent e) {
				throw new FileInputOutputError(uri.toString(),e);
			}
		} catch (RuntimeException e) {
			if (recentErrorException==null) {
				recentErrorException= e;
			};
			throw e;
		}
	}
	protected String getContentOfResource(URI uri, CharacterSet characterSet, int timeout, boolean backslashIsSeparator) throws CannotRetrieveContent {
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			try {
				String text= URL_Utils.readTextFile(attributes);
				if (attributes.connectionWasSuccessful()) {
					return text;
				} else {
					throw new CannotRetrieveContent(attributes.getExceptionName());
				}
			} catch (Throwable e2) {
				throw new CannotRetrieveContent(e2);
			// } finally {
			//	attributes.safeCloseConnection();
			}
		} catch (URISyntaxException e) {
			throw new WrongTermIsMalformedURL(e);
		// } catch (MalformedURLException e1) {
		//	throw e1;
		} catch (IOException e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		// } catch (Throwable e1) {
		//	return URL_Utils.exceptionToName(e1);
		}
	}
	//
	protected void insertNewItem(Term newItem, DatabaseRecord newRecord) {
		if (content != null) {
			// DatabaseRecord newRecord= new DatabaseRecord(newItem);
			ultimateRecord.nextRecord= newRecord;
			newRecord.previousRecord= ultimateRecord;
			ultimateRecord= newRecord;
		} else {
			content= newRecord; // new DatabaseRecord(newItem);
			ultimateRecord= content;
		}
	}
	//
	public void recentLoadingError4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) throws Backtracking {
		if (recentErrorException != null && recentErrorText != null) {
			a1.value= new PrologString(recentErrorText);
			a2.value= new PrologInteger(recentErrorPosition);
			a3.value= new PrologString(recentErrorException.toString());
			a4.value= new PrologString(recentErrorException.toString());
			iX.pushTrail(a1);
			iX.pushTrail(a2);
			iX.pushTrail(a3);
			iX.pushTrail(a4);
		} else {
			throw Backtracking.instance;
		}
	}
	public void recentLoadingError3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) throws Backtracking {
		if (recentErrorException != null && recentErrorText != null) {
			a1.value= new PrologString(recentErrorText);
			a2.value= new PrologInteger(recentErrorPosition);
			a3.value= new PrologString(recentErrorException.toString());
			iX.pushTrail(a1);
			iX.pushTrail(a2);
			iX.pushTrail(a3);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void doesExist1s(ChoisePoint iX, Term name) throws Backtracking {
		// String fileName= retrieveDatabaseName(name,iX);
		URI uri= retrieveLocationURI(name,iX);
		doesExist(uri,iX);
	}
	public void doesExist0s(ChoisePoint iX) throws Backtracking {
		// String fileName= retrieveDatabaseName(iX);
		URI uri= retrieveLocationURI(iX);
		doesExist(uri,iX);
	}
	protected void doesExist(URI uri, ChoisePoint iX) throws Backtracking {
		CharacterSet characterSet= FileUtils.term2CharacterSet(getBuiltInSlot_E_character_set(),iX);
		int timeout= retrieveMaxWaitingTime(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			if (!attributes.connectionWasSuccessful()) {
				throw Backtracking.instance;
			}
		} catch (Throwable e1) {
			throw Backtracking.instance;
		}
	}
	//
	public void delete1s(ChoisePoint iX, Term name) {
		String fileName= retrieveDatabaseName(name,iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		fileName= FileUtils.replaceBackslashes(fileName,backslashIsSeparator);
		deleteFile(fileName);
	}
	public void delete0s(ChoisePoint iX) {
		String fileName= retrieveDatabaseName(iX);
		deleteFile(fileName);
	}
	protected void deleteFile(String fileName) {
		Path path= fileSystem.getPath(fileName);
		try {
			// path.deleteIfExists();
			Files.deleteIfExists(path);
		} catch (DirectoryNotEmptyException e) {
		} catch (IOException e) {
		}
	}
	//
	public void getFullName1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String resolvedName= getFullName(iX,a2);
		a1.value= new PrologString(resolvedName);
	}
	public void getFullName1fs(ChoisePoint iX, Term a1) {
	}
	public void getFullName0ff(ChoisePoint iX, PrologVariable a1) {
		String resolvedName= getFullName(iX);
		a1.value= new PrologString(resolvedName);
	}
	public void getFullName0fs(ChoisePoint iX) {
	}
	protected String getFullName(ChoisePoint iX, Term a1) {
		try {
			String fileName= a1.getStringValue(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			return URL_Utils.getFullName(fileName,staticContext,backslashIsSeparator);
		} catch (TermIsNotAString e1) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	protected String getFullName(ChoisePoint iX) {
		String location= retrieveLocationString(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		return URL_Utils.getFullName(location,staticContext,backslashIsSeparator);
	}
	//
	public void getURL1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String resolvedName= getFullName(iX,a2);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		a1.value= new PrologString(URL_Utils.get_URL_string(resolvedName,staticContext,backslashIsSeparator));
	}
	public void getURL1fs(ChoisePoint iX, Term a1) {
	}
	public void getURL0ff(ChoisePoint iX, PrologVariable a1) {
		String resolvedName= getFullName(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		a1.value= new PrologString(URL_Utils.get_URL_string(resolvedName,staticContext,backslashIsSeparator));
	}
	public void getURL0fs(ChoisePoint iX) {
	}
	//
	public void isLocalResource1s(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			String path= a1.getStringValue(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			String resolvedName= URL_Utils.getFullName(path,staticContext,backslashIsSeparator);
			if (!URL_Utils.isResolvedNameOfLocalResource(resolvedName)) {
				throw Backtracking.instance;
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		} catch (WrongTermIsMalformedURL e) {
		}
	}
	public void isLocalResource0s(ChoisePoint iX) throws Backtracking {
		String path= getFullName(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		if (!URL_Utils.isLocalResource(path,backslashIsSeparator)) {
			throw Backtracking.instance;
		}
	}
	//
	protected String retrieveDatabaseName(Term name, ChoisePoint iX) {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			String textName= name.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			textName= FileUtils.replaceBackslashes(textName,backslashIsSeparator);
			return FileUtils.makeRealName(textName);
		} catch (TermIsNotAString e) {
			throw new WrongTermIsNotFileName(name);
		}
	}
	protected String retrieveDatabaseName(ChoisePoint iX) {
		Term name= getBuiltInSlot_E_name();
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			String textName= name.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			textName= FileUtils.replaceBackslashes(textName,backslashIsSeparator);
			return FileUtils.makeRealName(textName);
		} catch (TermIsNotAString e) {
			throw new WrongTermIsNotFileName(name);
		}
	}
	protected URI retrieveLocationURI(ChoisePoint iX) {
		Term name= getBuiltInSlot_E_name();
		return retrieveLocationURI(name,iX);
	}
	protected URI retrieveLocationURI(Term name, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			URI uri= URL_Utils.create_URI(textName,staticContext,backslashIsSeparator);
			return uri;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(name);
		}
	}
	protected String retrieveLocationString(ChoisePoint iX) {
		Term location= getBuiltInSlot_E_name();
		try {
			String textName= location.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			return textName;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(location);
		}
	}
	protected String appendExtensionIfNecessary(String textName, ChoisePoint iX) {
		if (textName.indexOf('.') == -1) {
			Term extension= getBuiltInSlot_E_extension();
			String textExtension= null;
			try {
				textExtension= extension.getStringValue(iX);
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotAString(extension);
			};
			textName= textName + textExtension;
		};
		return textName;
	}
	//
	protected int retrieveMaxWaitingTime(ChoisePoint iX) {
		Term interval= getBuiltInSlot_E_max_waiting_time();
		try {
			return URL_Utils.termToWaitingInterval(interval,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				return URL_Utils.termToWaitingInterval(DefaultOptions.waitingInterval,iX);
			} catch (TermIsSymbolDefault e2) {
				return defaultWaitingInterval;
			}
		}
	}
}
