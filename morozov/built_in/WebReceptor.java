// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.classes.*;
import morozov.run.*;
import morozov.run.errors.*;
import morozov.system.*;
import morozov.system.checker.*;
import morozov.system.checker.errors.*;
import morozov.system.checker.signals.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.net.URI;
import java.util.regex.Pattern;

import java.util.Calendar;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public abstract class WebReceptor extends WebResource {
	//
	// protected FileSystem fileSystem= FileSystems.getDefault();
	//
	protected static final BigDecimal defaultRevisionPeriod= BigDecimal.valueOf(-1);
	protected static final BigDecimal defaultAttemptPeriod= BigDecimal.valueOf(-1);
	private static final BigInteger oneMillion= BigInteger.valueOf(1000000);
	//
	// protected URL_Checker specialProcess;
	protected ActiveResource specialProcess; // = new ActiveResource();
	protected SlotVariable specialPort;
	//
	HashSet<WebReceptorRecord> backtrackableRecords= new HashSet<WebReceptorRecord>();
	HashSet<WebReceptorRecord> permanentRecords= new HashSet<WebReceptorRecord>();
	// GregorianCalendar calendar= new GregorianCalendar();
	//
	protected static String[] defaultUnpairedTagsTable= {
		"AREA","BASE","BASEFONT","BR","COL","FRAME","HR","IMG",
		"INPUT","ISINDEX","LINK","META","PARAM"};
	protected static String[] defaultFlatTagsTable= {
		"BODY","COLGROUP","DD","DT","HEAD","HTML","LI","OPTION",
		"P","TBODY","TD","TFOOT","TH","THEAD","TR"};
	protected static String[][] defaultReferenceContainersTable= {
		{"A","HREF"},{"APPLET","CODEBASE"},{"AREA","HREF"},
		{"BASE","HREF"},{"BLOCKQUOTE","CITE"},{"BODY","BACKGROUND"},
		{"DEL","CITE"},{"FORM","ACTION"},{"FRAME","SRC"},
		{"FRAME","LONGDESC"},{"HEAD","PROFILE"},{"IFRAME","SRC"},
		{"IFRAME","LONGDESC"},{"IMG","SRC"},{"IMG","LONGDESC"},
		{"IMG","USEMAP"},{"INPUT","SRC"},{"INPUT","USEMAP"},
		{"INS","CITE"},{"LINK","HREF"},{"OBJECT","CODEBASE"},
		{"OBJECT","CLASSID"},{"OBJECT","DATA"},{"OBJECT","USEMAP"},
		{"Q","CITE"},{"SCRIPT","SRC"}};
	protected static String[][] defaultSpecialEntitiesTable= {
		{"NBSP"," "},{"SHY",""}};
	//
	abstract protected Term getBuiltInSlot_E_revision_period();
	abstract protected Term getBuiltInSlot_E_attempt_period();
	abstract protected Term getBuiltInSlot_E_tags();
	abstract protected Term getBuiltInSlot_E_extract_attributes();
	abstract protected Term getBuiltInSlot_E_coalesce_adjacent_strings();
	abstract protected Term getBuiltInSlot_E_truncate_strings();
	//
	abstract public long entry_s_UnpairedTagsTable_1_o();
	abstract public long entry_s_FlatTagsTable_1_o();
	abstract public long entry_s_ReferenceContainersTable_2_oo();
	abstract public long entry_s_SpecialEntitiesTable_2_oo();
	//
	public void getText1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		getContent1ff(iX,a1,a2);
	}
	public void getText1fs(ChoisePoint iX, Term a1) {
	}
	public void getText0ff(ChoisePoint iX, PrologVariable a1) {
		getContent0ff(iX,a1);
	}
	public void getText0fs(ChoisePoint iX) {
	}
	//
	public void getReferences2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		String path;
		String mask;
		try {
			path= a2.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		};
		try {
			mask= a3.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a3);
		};
		a1.value= getResourceReferences(path,mask,iX);
		// iX.pushTrail(a1);
	}
	public void getReferences2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	public void getReferences1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		try {
			String path= a2.getStringValue(iX);
			a1.value= getResourceReferences(path,"*",iX);
			// iX.pushTrail(a1);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void getReferences1fs(ChoisePoint iX, Term a1) {
	}
	public void getReferences0ff(ChoisePoint iX, PrologVariable a1) {
		URI uri= retrieveLocationURI(iX);
		a1.value= getResourceReferences(uri,"*",iX);
		// iX.pushTrail(a1);
	}
	public void getReferences0fs(ChoisePoint iX) {
	}
	//
	public class GetReference2ff extends GetReference {
		public GetReference2ff(Continuation aC, PrologVariable a1, Term a2, Term a3) {
			c0= aC;
			result= a1;
			targetAddress= a2;
			mask= a3;
			isFunctionCall= true;
		}
	}
	public class GetReference2fs extends GetReference {
		public GetReference2fs(Continuation aC, Term a1, Term a2) {
			c0= aC;
			// result= a1;
			targetAddress= a1;
			mask= a2;
			isFunctionCall= false;
		}
	}
	public class GetReference1ff extends GetReference {
		public GetReference1ff(Continuation aC, PrologVariable a1, Term a2) {
			c0= aC;
			result= a1;
			targetAddress= a2;
			isFunctionCall= true;
		}
	}
	public class GetReference1fs extends GetReference {
		public GetReference1fs(Continuation aC, Term a1) {
			c0= aC;
			// result= a1;
			targetAddress= a1;
			isFunctionCall= false;
		}
	}
	public class GetReference0ff extends GetReference {
		public GetReference0ff(Continuation aC, PrologVariable a1) {
			c0= aC;
			result= a1;
			retrieveAddressFromSlotValue= true;
			isFunctionCall= true;
		}
	}
	public class GetReference0fs extends GetReference {
		public GetReference0fs(Continuation aC) {
			c0= aC;
			// result= a1;
			retrieveAddressFromSlotValue= true;
			isFunctionCall= false;
		}
	}
	public class GetReference extends Continuation {
		// private Continuation c0;
		protected PrologVariable result;
		protected Term targetAddress;
		protected Term mask;
		protected boolean retrieveAddressFromSlotValue= false;
		protected boolean isFunctionCall= false;
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			URI uri;
			Term content;
			String wildcard;
			if (mask==null) {
				wildcard= "*";
			} else {
				try {
					wildcard= mask.getStringValue(iX);
				} catch (TermIsNotAString e) {
					throw new WrongArgumentIsNotAString(mask);
				}
			};
			if (retrieveAddressFromSlotValue) {
				uri= retrieveLocationURI(iX);
				content= getResourceReferences(uri,wildcard,iX);
			} else {
				try {
					String path= targetAddress.getStringValue(iX);
					content= getResourceReferences(path,wildcard,iX);
				} catch (TermIsNotAString e) {
					throw new WrongArgumentIsNotAString(targetAddress);
				}
			};
			ChoisePoint newIx= new ChoisePoint(iX);
			while(true) {
				try {
					if (isFunctionCall) {
						result.value= content.getNextListHead(iX);
						// newIx.pushTrail(result);
					};
					try {
						c0.execute(newIx);
					} catch (Backtracking b) {
						if (isFunctionCall) {
							result.value= null;
						};
						if (newIx.isEnabled()) {
							newIx.freeTrail();
							content= content.getNextListTail(iX);
							continue;
						} else {
							throw Backtracking.instance;
						}
					};
					return;
				} catch (EndOfList eol) {
					if (isFunctionCall) {
						result.value= null;
					};
					throw Backtracking.instance;
				} catch (TermIsNotAList e) {
					if (isFunctionCall) {
						result.value= null;
					};
					// throw new WrongArgumentIsNotAList(content);
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	protected Term getResourceReferences(String path, String mask, ChoisePoint iX) {
		try {
			URI uri= getURI(iX,path);
			return getResourceReferences(uri,mask,iX);
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	protected Term getResourceReferences(URI uri, String mask, ChoisePoint iX) {
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			CharacterSet characterSet= retrieveCharacterSet(iX);
			int timeout= retrieveMaxWaitingTime(iX);
			BigDecimal revisionPeriod= retrieveRevisionPeriod(iX);
			BigDecimal attemptPeriod= retrieveAttemptPeriod(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			Term result;
			try {
				if (attributes.isDirectory) {
					result= retrieveDirectoryContent(uri,mask,backslashIsSeparator);
				} else {
					String[] tags= retrieveTags(iX);
					boolean extractAttributes= retrieveTheExtractAttributesFlag(iX);
					boolean coalesceAdjacentStrings= retrieveTheCoalesceAdjacentStringsFlag(iX);
					boolean truncateStrings= retrieveTheTruncateStringsFlag(iX);
					String[] unpairedTagsTable= retrieveUnpairedTagsTable(iX);
					String[] flatTagsTable= retrieveFlatTagsTable(iX);
					String[][] referenceContainersTable= retrieveReferenceContainersTable(iX);
					String[][] specialEntitiesTable= retrieveSpecialEntitiesTable(iX);
					String text= URL_Utils.readTextFile(attributes);
					HTML_Explorer parser= new HTML_Explorer(
						extractAttributes,
						coalesceAdjacentStrings,
						truncateStrings,
						tags,
						unpairedTagsTable,
						flatTagsTable,
						referenceContainersTable,
						specialEntitiesTable
						);
					result= parser.textToReferences(text,mask,uri,backslashIsSeparator);
				}
			} catch (Throwable e) {
				return URL_Utils.exceptionToName(e);
			// } finally {
			//	attributes.safeCloseConnection();
			};
			pushWebReceptorRecord(attributes,revisionPeriod,attemptPeriod,iX);
			return result;
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	//
	public void getTrees2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		String path;
		String mask;
		try {
			path= a2.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		};
		try {
			mask= a3.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a3);
		};
		a1.value= getResourceTrees(path,mask,iX);
		// iX.pushTrail(a1);
	}
	public void getTrees2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	public void getTrees1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		try {
			String path= a2.getStringValue(iX);
			a1.value= getResourceTrees(path,"*",iX);
			// iX.pushTrail(a1);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void getTrees1fs(ChoisePoint iX, Term a1) {
	}
	public void getTrees0ff(ChoisePoint iX, PrologVariable a1) {
		URI uri= retrieveLocationURI(iX);
		a1.value= getResourceTrees(uri,"*",iX);
		// iX.pushTrail(a1);
	}
	public void getTrees0fs(ChoisePoint iX) {
	}
	protected Term getResourceTrees(String path, String mask, ChoisePoint iX) {
		try {
			URI uri= getURI(iX,path);
			return getResourceTrees(uri,mask,iX);
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	protected Term getResourceTrees(URI uri, String mask, ChoisePoint iX) {
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			CharacterSet characterSet= retrieveCharacterSet(iX);
			int timeout= retrieveMaxWaitingTime(iX);
			BigDecimal revisionPeriod= retrieveRevisionPeriod(iX);
			BigDecimal attemptPeriod= retrieveAttemptPeriod(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			Term result;
			try {
				if (attributes.isDirectory) {
					result= retrieveDirectoryContent(uri,mask,backslashIsSeparator);
				} else {
					String[] tags= retrieveTags(iX);
					boolean extractAttributes= retrieveTheExtractAttributesFlag(iX);
					boolean coalesceAdjacentStrings= retrieveTheCoalesceAdjacentStringsFlag(iX);
					boolean truncateStrings= retrieveTheTruncateStringsFlag(iX);
					String[] unpairedTagsTable= retrieveUnpairedTagsTable(iX);
					String[] flatTagsTable= retrieveFlatTagsTable(iX);
					String[][] referenceContainersTable= retrieveReferenceContainersTable(iX);
					String[][] specialEntitiesTable= retrieveSpecialEntitiesTable(iX);
					String text= URL_Utils.readTextFile(attributes);
					HTML_Explorer parser= new HTML_Explorer(
						extractAttributes,
						coalesceAdjacentStrings,
						truncateStrings,
						tags,
						unpairedTagsTable,
						flatTagsTable,
						referenceContainersTable,
						specialEntitiesTable
						);
					result= parser.textToTerm(text,uri,mask,backslashIsSeparator);
				}
			} catch (Throwable e) {
				return URL_Utils.exceptionToName(e);
			// } finally {
			//	attributes.safeCloseConnection();
			};
			pushWebReceptorRecord(attributes,revisionPeriod,attemptPeriod,iX);
			return result;
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	//
	public void link1s(ChoisePoint iX, Term a1) {
		try {
			String path= a1.getStringValue(iX);
			linkResource(path,iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	public void link0s(ChoisePoint iX) {
		URI uri= retrieveLocationURI(iX);
		linkResource(uri,iX);
	}
	protected void linkResource(String path, ChoisePoint iX) {
		try {
			URI uri= getURI(iX,path);
			linkResource(uri,iX);
		} catch (Throwable e) {
			// Term error= new PrologString(e.toString());
			// return new PrologStructure(SymbolCodes.symbolCode_E_error,new Term[]{error});
			// return URL_Utils.exceptionToName(e);
		}
	}
	//
	public class UnpairedTagsTable1s extends Continuation {
		// private Continuation c0;
		protected PrologVariable outputResult;
		protected Term inputResult;
		protected boolean hasOutputArgument;
		//
		public UnpairedTagsTable1s(Continuation aC, PrologVariable a1) {
			c0= aC;
			outputResult= a1;
			hasOutputArgument= true;
		}
		public UnpairedTagsTable1s(Continuation aC, Term a1) {
			c0= aC;
			inputResult= a1;
			hasOutputArgument= false;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < defaultUnpairedTagsTable.length; n++) {
				if (hasOutputArgument) {
					outputResult.value= new PrologString(defaultUnpairedTagsTable[n]);
				} else {
					try {
						inputResult.isString(defaultUnpairedTagsTable[n],newIx);
					} catch (Backtracking b) {
						newIx.freeTrail();
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
						continue;
					} else {
						throw Backtracking.instance;
					}
				};
				return;
			};
			if (hasOutputArgument) {
				outputResult.value= null;
			};
			throw Backtracking.instance;
		}
	}
	//
	public class FlatTagsTable1s extends Continuation {
		// private Continuation c0;
		protected PrologVariable outputResult;
		protected Term inputResult;
		protected boolean hasOutputArgument;
		//
		public FlatTagsTable1s(Continuation aC, PrologVariable a1) {
			c0= aC;
			outputResult= a1;
			hasOutputArgument= true;
		}
		public FlatTagsTable1s(Continuation aC, Term a1) {
			c0= aC;
			inputResult= a1;
			hasOutputArgument= false;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < defaultFlatTagsTable.length; n++) {
				if (hasOutputArgument) {
					outputResult.value= new PrologString(defaultFlatTagsTable[n]);
				} else {
					try {
						inputResult.isString(defaultFlatTagsTable[n],newIx);
					} catch (Backtracking b) {
						newIx.freeTrail();
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
						continue;
					} else {
						throw Backtracking.instance;
					}
				};
				return;
			};
			if (hasOutputArgument) {
				outputResult.value= null;
			};
			throw Backtracking.instance;
		}
	}
	//
	public class ReferenceContainersTable2s extends Continuation {
		// private Continuation c0;
		protected PrologVariable outputResult1;
		protected PrologVariable outputResult2;
		protected Term inputResult1;
		protected Term inputResult2;
		protected boolean hasOutputArguments;
		//
		public ReferenceContainersTable2s(Continuation aC, PrologVariable a1, PrologVariable a2) {
			c0= aC;
			outputResult1= a1;
			outputResult2= a2;
			hasOutputArguments= true;
		}
		public ReferenceContainersTable2s(Continuation aC, Term a1, Term a2) {
			c0= aC;
			inputResult1= a1;
			inputResult2= a2;
			hasOutputArguments= false;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < defaultReferenceContainersTable.length; n++) {
				if (hasOutputArguments) {
					outputResult1.value= new PrologString(defaultReferenceContainersTable[n][0]);
					outputResult2.value= new PrologString(defaultReferenceContainersTable[n][1]);
				} else {
					try {
						inputResult1.isString(defaultReferenceContainersTable[n][0],newIx);
						inputResult2.isString(defaultReferenceContainersTable[n][1],newIx);
					} catch (Backtracking b) {
						newIx.freeTrail();
						continue;
					}
				};
				try {
					c0.execute(newIx);
				} catch (Backtracking b) {
					if (hasOutputArguments) {
						outputResult1.value= null;
						outputResult2.value= null;
					};
					if (newIx.isEnabled()) {
						newIx.freeTrail();
						continue;
					} else {
						throw Backtracking.instance;
					}
				};
				return;
			};
			if (hasOutputArguments) {
				outputResult1.value= null;
				outputResult2.value= null;
			};
			throw Backtracking.instance;
		}
	}
	//
	public class SpecialEntitiesTable2s extends Continuation {
		// private Continuation c0;
		protected PrologVariable outputResult1;
		protected PrologVariable outputResult2;
		protected Term inputResult1;
		protected Term inputResult2;
		protected boolean hasOutputArguments;
		//
		public SpecialEntitiesTable2s(Continuation aC, PrologVariable a1, PrologVariable a2) {
			c0= aC;
			outputResult1= a1;
			outputResult2= a2;
			hasOutputArguments= true;
		}
		public SpecialEntitiesTable2s(Continuation aC, Term a1, Term a2) {
			c0= aC;
			inputResult1= a1;
			inputResult2= a2;
			hasOutputArguments= false;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < defaultSpecialEntitiesTable.length; n++) {
				if (hasOutputArguments) {
					outputResult1.value= new PrologString(defaultSpecialEntitiesTable[n][0]);
					outputResult2.value= new PrologString(defaultSpecialEntitiesTable[n][1]);
				} else {
					try {
						inputResult1.isString(defaultSpecialEntitiesTable[n][0],newIx);
						inputResult2.isString(defaultSpecialEntitiesTable[n][1],newIx);
					} catch (Backtracking b) {
						newIx.freeTrail();
						continue;
					}
				};
				try {
					c0.execute(newIx);
				} catch (Backtracking b) {
					if (hasOutputArguments) {
						outputResult1.value= null;
						outputResult2.value= null;
					};
					if (newIx.isEnabled()) {
						newIx.freeTrail();
						continue;
					} else {
						throw Backtracking.instance;
					}
				};
				return;
			};
			if (hasOutputArguments) {
				outputResult1.value= null;
				outputResult2.value= null;
			};
			throw Backtracking.instance;
		}
	}
	//
	protected BigDecimal retrieveRevisionPeriod(ChoisePoint iX) {
		Term period= getBuiltInSlot_E_revision_period();
		try {
			return URL_Utils.termToActionPeriod(period,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				return URL_Utils.termToActionPeriod(DefaultOptions.revisionPeriod,iX);
			} catch (TermIsSymbolDefault e2) {
				return defaultRevisionPeriod;
			}
		}
	}
	//
	protected BigDecimal retrieveAttemptPeriod(ChoisePoint iX) {
		Term period= getBuiltInSlot_E_attempt_period();
		try {
			return URL_Utils.termToActionPeriod(period,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				return URL_Utils.termToActionPeriod(DefaultOptions.attemptPeriod,iX);
			} catch (TermIsSymbolDefault e2) {
				return defaultAttemptPeriod;
			}
		}
	}
	//
	protected String[] retrieveTags(ChoisePoint iX) {
		Term tags= getBuiltInSlot_E_tags();
		return Converters.termToStrings(tags,iX,true);
	}
	protected boolean retrieveTheExtractAttributesFlag(ChoisePoint iX) {
		Term value= getBuiltInSlot_E_extract_attributes();
		return Converters.term2YesNo(value,iX);
	}
	protected boolean retrieveTheCoalesceAdjacentStringsFlag(ChoisePoint iX) {
		Term value= getBuiltInSlot_E_coalesce_adjacent_strings();
		return Converters.term2YesNo(value,iX);
	}
	protected boolean retrieveTheTruncateStringsFlag(ChoisePoint iX) {
		Term value= getBuiltInSlot_E_truncate_strings();
		return Converters.term2YesNo(value,iX);
	}
	protected String[] retrieveUnpairedTagsTable(ChoisePoint iX) {
		long domainSignatureNumber= entry_s_UnpairedTagsTable_1_o();
		return collect_strings(iX,domainSignatureNumber);
	}
	protected String[] retrieveFlatTagsTable(ChoisePoint iX) {
		long domainSignatureNumber= entry_s_FlatTagsTable_1_o();
		return collect_strings(iX,domainSignatureNumber);
	}
	protected String[][] retrieveReferenceContainersTable(ChoisePoint iX) {
		long domainSignatureNumber= entry_s_ReferenceContainersTable_2_oo();
		return collect_pairs(iX,domainSignatureNumber);
	}
	protected String[][] retrieveSpecialEntitiesTable(ChoisePoint iX) {
		long domainSignatureNumber= entry_s_SpecialEntitiesTable_2_oo();
		return collect_pairs(iX,domainSignatureNumber);
	}
	//
	protected String[] collect_strings(ChoisePoint iX, long domainSignatureNumber) {
		final PrologVariable result= new PrologVariable();
		Term[] targetArguments= new Term[1];
		targetArguments[0]= result;
		final ArrayList<String> resultSet= new ArrayList<String>();
		Continuation completion= new Continuation() {
			public void execute(ChoisePoint iX) throws Backtracking {
				Term newResult= result.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
				try {
					resultSet.add(newResult.getStringValue(iX).toUpperCase());
				} catch (TermIsNotAString e) {
				};
				throw Backtracking.instance;
			}
			public boolean isPhaseTermination() {
				return false;
			}
			public String toString() {
				return "RememberResult&Backtrack;";
			}
		};
		Continuation c1= new DomainSwitch(completion,domainSignatureNumber,WebReceptor.this,WebReceptor.this,targetArguments);
		ChoisePoint newIndex= new ChoisePoint(iX);
		try {
			c1.execute(newIndex);
			throw new FailureProcedureSucceed(); // Never happens
		} catch (Backtracking b) {
			newIndex.freeTrail();
			String[] resultArray= resultSet.toArray(new String[0]);
			return resultArray;
		}
	}
	protected String[][] collect_pairs(ChoisePoint iX, long domainSignatureNumber) {
		final PrologVariable result1= new PrologVariable();
		final PrologVariable result2= new PrologVariable();
		Term[] targetArguments= new Term[2];
		targetArguments[0]= result1;
		targetArguments[1]= result2;
		final ArrayList<String[]> resultSet= new ArrayList<String[]>();
		Continuation completion= new Continuation() {
			public void execute(ChoisePoint iX) throws Backtracking {
				Term newResult1= result1.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
				Term newResult2= result2.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
				String[] resultPair= new String[2];
				try {
					resultPair[0]= newResult1.getStringValue(iX).toUpperCase();
					resultPair[1]= newResult2.getStringValue(iX).toUpperCase();
					resultSet.add(resultPair);
				} catch (TermIsNotAString e) {
				};
				throw Backtracking.instance;
			}
			public boolean isPhaseTermination() {
				return false;
			}
			public String toString() {
				return "RememberResult&Backtrack;";
			}
		};
		Continuation c1= new DomainSwitch(completion,domainSignatureNumber,WebReceptor.this,WebReceptor.this,targetArguments);
		ChoisePoint newIndex= new ChoisePoint(iX);
		try {
			c1.execute(newIndex);
			throw new FailureProcedureSucceed(); // Never happens
		} catch (Backtracking b) {
			newIndex.freeTrail();
			String[][] resultArray= resultSet.toArray(new String[0][0]);
			return resultArray;
		}
	}
	//
	protected void linkResource(URI uri, ChoisePoint iX) {
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			CharacterSet characterSet= retrieveCharacterSet(iX);
			int timeout= retrieveMaxWaitingTime(iX);
			BigDecimal revisionPeriod= retrieveRevisionPeriod(iX);
			BigDecimal attemptPeriod= retrieveAttemptPeriod(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			// Term result;
			// boolean ok= false;
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			// if (attributes.connectionWasSuccessful()) {
			//	ok= true;
			// };
			pushWebReceptorRecord(attributes,revisionPeriod,attemptPeriod,iX);
			// return result;
		} catch (Throwable e) {
			// return URL_Utils.exceptionToName(e);
		}
	}
	protected Term getResourceParameters(URI uri, CharacterSet characterSet, int timeout, ChoisePoint iX) {
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			// int timeout= retrieveMaxWaitingTime(iX);
			BigDecimal revisionPeriod= retrieveRevisionPeriod(iX);
			BigDecimal attemptPeriod= retrieveAttemptPeriod(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			// Term result;
			// boolean ok= false;
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			// if (attributes.connectionWasSuccessful()) {
			//	ok= true;
			// };
			pushWebReceptorRecord(attributes,revisionPeriod,attemptPeriod,iX);
			return attributes.toTerm();
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	protected Term getResourceContent(URI uri, ChoisePoint iX) {
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			CharacterSet characterSet= retrieveCharacterSet(iX);
			int timeout= retrieveMaxWaitingTime(iX);
			BigDecimal revisionPeriod= retrieveRevisionPeriod(iX);
			BigDecimal attemptPeriod= retrieveAttemptPeriod(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			Term result;
			try {
				result= getContentOfResource(uri,characterSet,timeout,backslashIsSeparator);
			} catch (CannotRetrieveContent e2) {
				result= e2.getExceptionName();
			} catch (Throwable e2) {
				throw e2;
			// } finally {
			//	attributes.safeCloseConnection();
			};
			pushWebReceptorRecord(attributes,revisionPeriod,attemptPeriod,iX);
			return result;
		} catch (Throwable e1) {
			return URL_Utils.exceptionToName(e1);
		}
	}
	//
	protected Term retrieveDirectoryContent(URI uri, String mask, boolean backslashIsSeparator) {
		char[] nativePattern= mask.toCharArray();
		Pattern pattern= FileNameMask.wildcard2UnixPattern(nativePattern);
		List<Path> content= retrieveDirectoryList(uri);
		Term result= PrologEmptyList.instance;
		for (int n=content.size()-1; n >= 0; n--) {
			String resolvedName= FileUtils.tryToMakeRealName(content.get(n)).toString();
			if (HTML_ExplorerTools.isEnabledReference(resolvedName,pattern,backslashIsSeparator)) {
				result= new PrologList(new PrologString(resolvedName),result);
			}
		};
		return result;
	}
	//
	protected void storePermanentRecord(WebReceptorRecord record) {
		permanentRecords.add(record);
	}
	//
	protected void storeBacktrackableRecord(PredefinedClassRecord record) {
		permanentRecords.clear();
		backtrackableRecords.add((WebReceptorRecord)record);
	}
	//
	public boolean isSpecialWorld() {
		return true;
	}
	//
	public void finishPhaseSuccessfully() {
		permanentRecords.clear();
		createCheckerIfNeed();
		HashMap<ActorAndURI,WebReceptorRecord> actualRecords= new HashMap<ActorAndURI,WebReceptorRecord>();
		Iterator<WebReceptorRecord> backtrackableRecordsIterator= backtrackableRecords.iterator();
		while (backtrackableRecordsIterator.hasNext()) {
			WebReceptorRecord currentRecord= backtrackableRecordsIterator.next();
			ActorAndURI key= new ActorAndURI(currentRecord.actorNumber,currentRecord.attributes.uri);
			WebReceptorRecord existedRecord= actualRecords.get(key);
			if (existedRecord==null) {
				actualRecords.put(key,currentRecord);
			} else if (existedRecord.getCheckUpPeriod().compareTo(currentRecord.getCheckUpPeriod()) > 0) {
				actualRecords.put(key,currentRecord);
			}
		};
		HashSet<ActorNumber> provenActors= currentProcess.getActorIsToBeProved();
		Iterator<ActorNumber> provenActorIterator= provenActors.iterator();
		while (provenActorIterator.hasNext()) {
			ActorNumber actor= provenActorIterator.next();
			specialProcess.forgetEvents(actor);
		};
		// GregorianCalendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		BigInteger currentTime= BigInteger.valueOf(calendar.getTimeInMillis()).multiply(oneMillion);
		specialProcess.addFutureEvents(actualRecords,currentTime);
		backtrackableRecords.clear();
	}
	//
	public void finishPhaseUnsuccessfully() {
		backtrackableRecords.clear();
		createCheckerIfNeed();
		HashMap<ActorAndURI,WebReceptorRecord> actualRecords= new HashMap<ActorAndURI,WebReceptorRecord>();
		Iterator<WebReceptorRecord> permanentRecordsIterator= permanentRecords.iterator();
		while (permanentRecordsIterator.hasNext()) {
			WebReceptorRecord currentRecord= permanentRecordsIterator.next();
			ActorAndURI key= new ActorAndURI(currentRecord.actorNumber,currentRecord.attributes.uri);
			WebReceptorRecord existedRecord= actualRecords.get(key);
			if (existedRecord==null) {
				actualRecords.put(key,currentRecord);
			} else if (existedRecord.getCheckUpPeriod().compareTo(currentRecord.getCheckUpPeriod()) > 0) {
				actualRecords.put(key,currentRecord);
			}
		};
		// HashSet<ActorNumber> provenActors= currentProcess.getActorIsToBeProved();
		// Iterator<ActorNumber> provenActorIterator= provenActors.iterator();
		// while (provenActorIterator.hasNext()) {
		//	ActorNumber actor= provenActorIterator.next();
		//	specialProcess.forgetEvents(actor);
		// };
		// GregorianCalendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		BigInteger currentTime= BigInteger.valueOf(calendar.getTimeInMillis()).multiply(oneMillion);
		specialProcess.checkAndAddFutureEvents(actualRecords,currentTime);
		permanentRecords.clear();
	}
	//
	protected void createCheckerIfNeed() {
		if (specialProcess==null) {
			specialPort= new SlotVariable();
			specialProcess= new ActiveResource(specialPort);
			specialProcess.initiate(currentProcess,staticContext);
			specialPort.registerVariables(currentProcess,false,false);
			specialPort.registerVariables(specialProcess,false,false);
			specialProcess.start();
		}
	}
	protected void pushWebReceptorRecord(URL_Attributes attributes, BigDecimal revisionPeriod, BigDecimal attemptPeriod, ChoisePoint iX) {
		if (revisionPeriod.compareTo(BigDecimal.ZERO) >= 0) {
			if (attemptPeriod.compareTo(BigDecimal.ZERO) < 0) {
				attemptPeriod= revisionPeriod;
			} else if (revisionPeriod.compareTo(attemptPeriod) < 0) {
				attemptPeriod= revisionPeriod;
			}
		};
		boolean ok= attributes.connectionWasSuccessful();
		// if (	(ok && revisionPeriod.compareTo(BigInteger.ZERO) >= 0) ||
		//	(!ok && attemptPeriod.compareTo(BigInteger.ZERO) >= 0)
		if (ok) {
			if (revisionPeriod.compareTo(BigDecimal.ZERO) < 0) {
				return;
			}
		} else {
			if (attemptPeriod.compareTo(BigDecimal.ZERO) < 0) {
				return;
			}
		};
		ActorRegister aR= iX.actorRegister;
		ActorNumber actorNumber= aR.currentActorNumber;
		WebReceptorRecord receptorRecord= new WebReceptorRecord(this,actorNumber,attributes,revisionPeriod,attemptPeriod);
		pushPredefinedClassRecord(receptorRecord);
		storePermanentRecord(receptorRecord);
		createCheckerIfNeed();
		try {
			specialPort.isUnknownValue(iX);
		} catch (Backtracking b) {
			new CannotUnifySpecialPort();
		}
	}
}