// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.run.errors.*;
import morozov.system.*;
import morozov.system.checker.*;
import morozov.system.checker.errors.*;
import morozov.system.checker.signals.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

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
	public ActionPeriod revisionPeriod= null;
	public ActionPeriod attemptPeriod= null;
	public String[] tags= null;
	public Boolean extractAttributes= null;
	public Boolean coalesceAdjacentStrings= null;
	public Boolean truncateStrings= null;
	//
	protected boolean internalTablesAreInitialized= false;
	protected String[] unpairedTagsTable= null;
	protected String[] flatTagsTable= null;
	protected String[][] referenceContainersTable= null;
	protected String[][] specialEntitiesTable= null;
	//
	protected static final long defaultRevisionPeriodInSeconds= -1;
	protected static final long defaultAttemptPeriodInSeconds= -1;
	protected static final BigDecimal decimalDefaultRevisionPeriodInNanoseconds= BigDecimal.valueOf(defaultRevisionPeriodInSeconds);
	protected static final BigDecimal decimalDefaultAttemptPeriodInNanoseconds= BigDecimal.valueOf(defaultAttemptPeriodInSeconds);
	protected static final Term termDefaultRevisionPeriod= new PrologInteger(defaultRevisionPeriodInSeconds);
	protected static final Term termDefaultAttemptPeriod= new PrologInteger(defaultAttemptPeriodInSeconds);
	protected static final BigInteger oneMillion= BigInteger.valueOf(1_000_000);
	//
	protected ActiveResource specialProcess;
	protected SlotVariable specialPort;
	//
	HashSet<WebReceptorRecord> backtrackableRecords= new HashSet<>();
	HashSet<WebReceptorRecord> permanentRecords= new HashSet<>();
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
	///////////////////////////////////////////////////////////////
	//
	public WebReceptor() {
	}
	public WebReceptor(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_revision_period();
	abstract public Term getBuiltInSlot_E_attempt_period();
	abstract public Term getBuiltInSlot_E_tags();
	abstract public Term getBuiltInSlot_E_extract_attributes();
	abstract public Term getBuiltInSlot_E_coalesce_adjacent_strings();
	abstract public Term getBuiltInSlot_E_truncate_strings();
	//
	abstract public long entry_s_UnpairedTagsTable_1_o();
	abstract public long entry_s_FlatTagsTable_1_o();
	abstract public long entry_s_ReferenceContainersTable_2_oo();
	abstract public long entry_s_SpecialEntitiesTable_2_oo();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set revision_period
	//
	public void setRevisionPeriod1s(ChoisePoint iX, Term a1) {
		setRevisionPeriod(ActionPeriodConverters.argumentToActionPeriod(a1,iX));
	}
	public void setRevisionPeriod(ActionPeriod value) {
		revisionPeriod= value;
	}
	public void getRevisionPeriod0ff(ChoisePoint iX, PrologVariable result) {
		ActionPeriod value= getRevisionPeriod(iX);
		result.setNonBacktrackableValue(ActionPeriodConverters.toTerm(value));
	}
	public void getRevisionPeriod0fs(ChoisePoint iX) {
	}
	public ActionPeriod getRevisionPeriod(ChoisePoint iX) {
		if (revisionPeriod != null) {
			return revisionPeriod;
		} else {
			Term value= getBuiltInSlot_E_revision_period();
			return ActionPeriodConverters.argumentToActionPeriod(value,iX);
		}
	}
	//
	// get/set attempt_period
	//
	public void setAttemptPeriod1s(ChoisePoint iX, Term a1) {
		setAttemptPeriod(ActionPeriodConverters.argumentToActionPeriod(a1,iX));
	}
	public void setAttemptPeriod(ActionPeriod value) {
		attemptPeriod= value;
	}
	public void getAttemptPeriod0ff(ChoisePoint iX, PrologVariable result) {
		ActionPeriod value= getAttemptPeriod(iX);
		result.setNonBacktrackableValue(ActionPeriodConverters.toTerm(value));
	}
	public void getAttemptPeriod0fs(ChoisePoint iX) {
	}
	public ActionPeriod getAttemptPeriod(ChoisePoint iX) {
		if (attemptPeriod != null) {
			return attemptPeriod;
		} else {
			Term value= getBuiltInSlot_E_attempt_period();
			return ActionPeriodConverters.argumentToActionPeriod(value,iX);
		}
	}
	//
	// get/set tags
	//
	public void setTags1s(ChoisePoint iX, Term a1) {
		setTags(GeneralConverters.termToStrings(a1,iX,true));
	}
	public void setTags(String[] value) {
		tags= value;
	}
	public void getTags0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(GeneralConverters.stringArrayToList(getTags(iX)));
	}
	public void getTags0fs(ChoisePoint iX) {
	}
	public String[] getTags(ChoisePoint iX) {
		if (tags != null) {
			return tags;
		} else {
			Term value= getBuiltInSlot_E_tags();
			return GeneralConverters.termToStrings(value,iX,true);
		}
	}
	//
	// get/set extract_attributes
	//
	public void setExtractAttributes1s(ChoisePoint iX, Term a1) {
		setExtractAttributes(YesNoConverters.termYesNo2Boolean(a1,iX));
	}
	public void setExtractAttributes(boolean value) {
		extractAttributes= value;
	}
	public void getExtractAttributes0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(getExtractAttributes(iX)));
	}
	public void getExtractAttributes0fs(ChoisePoint iX) {
	}
	public boolean getExtractAttributes(ChoisePoint iX) {
		if (extractAttributes != null) {
			return extractAttributes;
		} else {
			Term value= getBuiltInSlot_E_extract_attributes();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set coalesce_adjacent_strings
	//
	public void setCoalesceAdjacentStrings1s(ChoisePoint iX, Term a1) {
		setCoalesceAdjacentStrings(YesNoConverters.termYesNo2Boolean(a1,iX));
	}
	public void setCoalesceAdjacentStrings(boolean value) {
		coalesceAdjacentStrings= value;
	}
	public void getCoalesceAdjacentStrings0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(getCoalesceAdjacentStrings(iX)));
	}
	public void getCoalesceAdjacentStrings0fs(ChoisePoint iX) {
	}
	public boolean getCoalesceAdjacentStrings(ChoisePoint iX) {
		if (coalesceAdjacentStrings != null) {
			return coalesceAdjacentStrings;
		} else {
			Term value= getBuiltInSlot_E_coalesce_adjacent_strings();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set truncate_strings
	//
	public void setTruncateStrings1s(ChoisePoint iX, Term a1) {
		setTruncateStrings(YesNoConverters.termYesNo2Boolean(a1,iX));
	}
	public void setTruncateStrings(boolean value) {
		truncateStrings= value;
	}
	public void getTruncateStrings0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(getTruncateStrings(iX)));
	}
	public void getTruncateStrings0fs(ChoisePoint iX) {
	}
	public boolean getTruncateStrings(ChoisePoint iX) {
		if (truncateStrings != null) {
			return truncateStrings;
		} else {
			Term value= getBuiltInSlot_E_truncate_strings();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getText1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		getContent1ff(iX,result,a1);
	}
	public void getText1fs(ChoisePoint iX, Term a1) {
	}
	public void getText0ff(ChoisePoint iX, PrologVariable result) {
		getContent0ff(iX,result);
	}
	public void getText0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getReferences2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		String mask= GeneralConverters.argumentToString(a2,iX);
		result.setNonBacktrackableValue(getResourceReferences(a1,mask,iX));
	}
	public void getReferences2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	public void getReferences1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		result.setNonBacktrackableValue(getResourceReferences(a1,"*",iX));
	}
	public void getReferences1fs(ChoisePoint iX, Term a1) {
	}
	public void getReferences0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getResourceReferences("*",iX));
	}
	public void getReferences0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public class GetReference2ff extends GetReference {
		public GetReference2ff(Continuation aC, PrologVariable result, Term a1, Term a2) {
			c0= aC;
			argumentResult= result;
			targetAddress= a1;
			mask= a2;
			isFunctionCall= true;
		}
	}
	public class GetReference2fs extends GetReference {
		public GetReference2fs(Continuation aC, Term a1, Term a2) {
			c0= aC;
			targetAddress= a1;
			mask= a2;
			isFunctionCall= false;
		}
	}
	public class GetReference1ff extends GetReference {
		public GetReference1ff(Continuation aC, PrologVariable result, Term a1) {
			c0= aC;
			argumentResult= result;
			targetAddress= a1;
			isFunctionCall= true;
		}
	}
	public class GetReference1fs extends GetReference {
		public GetReference1fs(Continuation aC, Term a1) {
			c0= aC;
			targetAddress= a1;
			isFunctionCall= false;
		}
	}
	public class GetReference0ff extends GetReference {
		public GetReference0ff(Continuation aC, PrologVariable result) {
			c0= aC;
			argumentResult= result;
			retrieveAddressFromSlotValue= true;
			isFunctionCall= true;
		}
	}
	public class GetReference0fs extends GetReference {
		public GetReference0fs(Continuation aC) {
			c0= aC;
			retrieveAddressFromSlotValue= true;
			isFunctionCall= false;
		}
	}
	public class GetReference extends Continuation {
		//
		protected PrologVariable argumentResult;
		protected Term targetAddress;
		protected Term mask;
		protected boolean retrieveAddressFromSlotValue= false;
		protected boolean isFunctionCall= false;
		//
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			URI uri;
			Term content;
			String wildcard;
			if (mask==null) {
				wildcard= "*";
			} else {
				wildcard= GeneralConverters.argumentToString(mask,iX);
			};
			if (retrieveAddressFromSlotValue) {
				content= getResourceReferences(wildcard,iX);
			} else {
				content= getResourceReferences(targetAddress,wildcard,iX);
			};
			ChoisePoint newIx= new ChoisePoint(iX);
			while (true) {
				try {
					if (isFunctionCall) {
						argumentResult.setNonBacktrackableValue(content.getNextListHead(iX));
					};
					try {
						c0.execute(newIx);
					} catch (Backtracking b) {
						if (isFunctionCall) {
							argumentResult.clear();
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
						argumentResult.clear();
					};
					throw Backtracking.instance;
				} catch (TermIsNotAList e) {
					if (isFunctionCall) {
						argumentResult.clear();
					};
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term getResourceReferences(String mask, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		String[] currentTags= getTags(iX);
		boolean currentExtractAttributes= getExtractAttributes(iX);
		boolean currentCoalesceAdjacentStrings= getCoalesceAdjacentStrings(iX);
		boolean currentTruncateStrings= getTruncateStrings(iX);
		initializeInternalTables(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			return getReferencesOfResource(
				fileName,
				mask,
				timeout,
				characters,
				revision,
				attempts,
				currentTags,
				currentExtractAttributes,
				currentCoalesceAdjacentStrings,
				currentTruncateStrings,
				iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	protected Term getResourceReferences(Term argument, String mask, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		String[] currentTags= getTags(iX);
		boolean currentExtractAttributes= getExtractAttributes(iX);
		boolean currentCoalesceAdjacentStrings= getCoalesceAdjacentStrings(iX);
		boolean currentTruncateStrings= getTruncateStrings(iX);
		initializeInternalTables(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			return getReferencesOfResource(
				fileName,
				mask,
				timeout,
				characters,
				revision,
				attempts,
				currentTags,
				currentExtractAttributes,
				currentCoalesceAdjacentStrings,
				currentTruncateStrings,
				iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	protected Term getReferencesOfResource(ExtendedFileName fileName, String mask, int timeout, CharacterSet characters, ActionPeriod revision, ActionPeriod attempts, String[] tags, boolean extractAttributes, boolean coalesceAdjacentStrings, boolean truncateStrings, ChoisePoint iX) throws Throwable {
		URL_Attributes attributes= fileName.getUniversalResourceAttributes(timeout,characters,staticContext);
		Term result;
		if (attributes.isDirectory()) {
			result= retrieveDirectoryContent(attributes.getURI(),mask);
		} else {
			String text= SimpleFileName.readStringFromUniversalResource(attributes);
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
			result= parser.textToReferences(text,mask,attributes.getURI());
		};
		pushWebReceptorRecord(attributes,revision,attempts,iX);
		return result;
	}
	//
	protected Term retrieveDirectoryContent(URI uri, String mask) {
		char[] nativePattern= mask.toCharArray();
		Pattern pattern= FileNameMask.wildcard2UnixPattern(nativePattern);
		List<Path> content= SimpleFileName.retrieveDirectoryList(uri);
		Term result= PrologEmptyList.instance;
		for (int n=content.size()-1; n >= 0; n--) {
			String resolvedName= SimpleFileName.tryToMakeRealName(content.get(n)).toString();
			if (HTML_ExplorerTools.isEnabledReference(resolvedName,pattern)) {
				result= new PrologList(new PrologString(resolvedName),result);
			}
		};
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getTrees2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		String mask= GeneralConverters.argumentToString(a2,iX);
		result.setNonBacktrackableValue(getResourceTrees(a1,mask,iX));
	}
	public void getTrees2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	public void getTrees1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		result.setNonBacktrackableValue(getResourceTrees(a1,"*",iX));
	}
	public void getTrees1fs(ChoisePoint iX, Term a1) {
	}
	public void getTrees0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getResourceTrees("*",iX));
	}
	public void getTrees0fs(ChoisePoint iX) {
	}
	protected Term getResourceTrees(String mask, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		String[] currentTags= getTags(iX);
		boolean currentExtractAttributes= getExtractAttributes(iX);
		boolean currentCoalesceAdjacentStrings= getCoalesceAdjacentStrings(iX);
		boolean currentTruncateStrings= getTruncateStrings(iX);
		initializeInternalTables(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			return getTreesOfResource(
				fileName,
				mask,
				timeout,
				characters,
				revision,
				attempts,
				currentTags,
				currentExtractAttributes,
				currentCoalesceAdjacentStrings,
				currentTruncateStrings,
				iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	protected Term getResourceTrees(Term argument, String mask, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		String[] currentTags= getTags(iX);
		boolean currentExtractAttributes= getExtractAttributes(iX);
		boolean currentCoalesceAdjacentStrings= getCoalesceAdjacentStrings(iX);
		boolean currentTruncateStrings= getTruncateStrings(iX);
		initializeInternalTables(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			return getTreesOfResource(
				fileName,
				mask,
				timeout,
				characters,
				revision,
				attempts,
				currentTags,
				currentExtractAttributes,
				currentCoalesceAdjacentStrings,
				currentTruncateStrings,
				iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	protected Term getTreesOfResource(ExtendedFileName fileName, String mask, int timeout, CharacterSet characters, ActionPeriod revision, ActionPeriod attempts, String[] tags, boolean extractAttributes, boolean coalesceAdjacentStrings, boolean truncateStrings, ChoisePoint iX) throws Throwable {
		URL_Attributes attributes= fileName.getUniversalResourceAttributes(timeout,characters,staticContext);
		Term result;
		if (attributes.isDirectory()) {
			result= retrieveDirectoryContent(attributes.getURI(),mask);
		} else {
			String text= SimpleFileName.readStringFromUniversalResource(attributes);
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
			result= parser.textToTerm(text,attributes.getURI(),mask);
		};
		pushWebReceptorRecord(attributes,revision,attempts,iX);
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void link1s(ChoisePoint iX, Term a1) {
		linkResource(a1,iX);
	}
	public void link0s(ChoisePoint iX) {
		linkResource(iX);
	}
	protected void linkResource(ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			linkResource(fileName,timeout,characters,revision,attempts,iX);
		} catch (Throwable e) {
		}
	}
	protected void linkResource(Term argument, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			linkResource(fileName,timeout,characters,revision,attempts,iX);
		} catch (Throwable e) {
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public class UnpairedTagsTable1s extends Continuation {
		//
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
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < defaultUnpairedTagsTable.length; n++) {
				if (hasOutputArgument) {
					outputResult.setBacktrackableValue(new PrologString(defaultUnpairedTagsTable[n]),newIx);
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
						outputResult.clear();
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
				outputResult.clear();
			};
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public class FlatTagsTable1s extends Continuation {
		//
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
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < defaultFlatTagsTable.length; n++) {
				if (hasOutputArgument) {
					outputResult.setBacktrackableValue(new PrologString(defaultFlatTagsTable[n]),newIx);
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
						outputResult.clear();
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
				outputResult.clear();
			};
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public class ReferenceContainersTable2s extends Continuation {
		//
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
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < defaultReferenceContainersTable.length; n++) {
				if (hasOutputArguments) {
					outputResult1.setBacktrackableValue(new PrologString(defaultReferenceContainersTable[n][0]),newIx);
					outputResult2.setBacktrackableValue(new PrologString(defaultReferenceContainersTable[n][1]),newIx);
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
						outputResult1.clear();
						outputResult2.clear();
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
				outputResult1.clear();
				outputResult2.clear();
			};
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public class SpecialEntitiesTable2s extends Continuation {
		//
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
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			for (int n=0; n < defaultSpecialEntitiesTable.length; n++) {
				if (hasOutputArguments) {
					outputResult1.setBacktrackableValue(new PrologString(defaultSpecialEntitiesTable[n][0]),newIx);
					outputResult2.setBacktrackableValue(new PrologString(defaultSpecialEntitiesTable[n][1]),newIx);
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
						outputResult1.clear();
						outputResult2.clear();
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
				outputResult1.clear();
				outputResult2.clear();
			};
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void initializeInternalTables(ChoisePoint iX) {
		if (!internalTablesAreInitialized) {
			unpairedTagsTable= retrieveUnpairedTagsTable(iX);
			flatTagsTable= retrieveFlatTagsTable(iX);
			referenceContainersTable= retrieveReferenceContainersTable(iX);
			specialEntitiesTable= retrieveSpecialEntitiesTable(iX);
			internalTablesAreInitialized= true;
		}
	}
	//
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
		final ArrayList<String> resultSet= new ArrayList<>();
		Continuation completion= new Continuation() {
			@Override
			public void execute(ChoisePoint iX) throws Backtracking {
				Term newResult= result.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
				String newText= GeneralConverters.argumentToString(newResult,iX);
				resultSet.add(newText.toUpperCase());
				throw Backtracking.instance;
			}
			@Override
			public boolean isPhaseTermination() {
				return false;
			}
			@Override
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
			String[] resultArray= resultSet.toArray(new String[resultSet.size()]);
			return resultArray;
		}
	}
	protected String[][] collect_pairs(ChoisePoint iX, long domainSignatureNumber) {
		final PrologVariable result1= new PrologVariable();
		final PrologVariable result2= new PrologVariable();
		Term[] targetArguments= new Term[2];
		targetArguments[0]= result1;
		targetArguments[1]= result2;
		final ArrayList<String[]> resultSet= new ArrayList<>();
		Continuation completion= new Continuation() {
			@Override
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
			@Override
			public boolean isPhaseTermination() {
				return false;
			}
			@Override
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
	///////////////////////////////////////////////////////////////
	//
	protected void linkResource(ExtendedFileName fileName, int timeout, CharacterSet characters, ActionPeriod revision, ActionPeriod attempts, ChoisePoint iX) {
		try {
			URL_Attributes attributes= fileName.getUniversalResourceAttributes(timeout,characters,staticContext);
			pushWebReceptorRecord(attributes,revision,attempts,iX);
		} catch (Throwable e) {
		}
	}
	//
	@Override
	protected Term getUniversalResourceParameters(ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			URL_Attributes attributes= fileName.getUniversalResourceAttributes(timeout,characters,staticContext);
			pushWebReceptorRecord(attributes,revision,attempts,iX);
			return attributes.toTerm();
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	@Override
	protected Term getUniversalResourceParameters(Term argument, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			URL_Attributes attributes= fileName.getUniversalResourceAttributes(timeout,characters,staticContext);
			pushWebReceptorRecord(attributes,revision,attempts,iX);
			return attributes.toTerm();
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	//
	@Override
	protected Term getUniversalResourceContent(ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			URL_Attributes attributes= fileName.getUniversalResourceAttributes(timeout,characters,staticContext);
			Term result;
			try {
				result= fileName.getTermContentOfUniversalResource(attributes);
			} catch (CannotRetrieveContent e2) {
				result= e2.getExceptionName();
			} catch (Throwable e2) {
				throw e2;
			};
			pushWebReceptorRecord(attributes,revision,attempts,iX);
			return result;
		} catch (Throwable e1) {
			return SimpleFileName.channelExceptionToName(e1);
		}
	}
	@Override
	protected Term getUniversalResourceContent(Term argument, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet characters= getCharacterSet(iX);
		ActionPeriod revision= getRevisionPeriod(iX);
		ActionPeriod attempts= getAttemptPeriod(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			URL_Attributes attributes= fileName.getUniversalResourceAttributes(timeout,characters,staticContext);
			Term result;
			try {
				result= fileName.getTermContentOfUniversalResource(attributes);
			} catch (CannotRetrieveContent e2) {
				result= e2.getExceptionName();
			};
			pushWebReceptorRecord(attributes,revision,attempts,iX);
			return result;
		} catch (Throwable e1) {
			return SimpleFileName.channelExceptionToName(e1);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void storePermanentRecord(WebReceptorRecord record) {
		permanentRecords.add(record);
	}
	//
	@Override
	protected void storeBacktrackableRecord(PredefinedClassRecord record) {
		permanentRecords.clear();
		backtrackableRecords.add((WebReceptorRecord)record);
	}
	//
	@Override
	public boolean isSpecialWorld() {
		return true;
	}
	//
	@Override
	public void finishPhaseSuccessfully() {
		permanentRecords.clear();
		createCheckerIfNeed();
		HashMap<ActorAndURI,WebReceptorRecord> actualRecords= new HashMap<>();
		Iterator<WebReceptorRecord> backtrackableRecordsIterator= backtrackableRecords.iterator();
		while (backtrackableRecordsIterator.hasNext()) {
			WebReceptorRecord currentRecord= backtrackableRecordsIterator.next();
			ActorAndURI key= new ActorAndURI(currentRecord.getActorNumber(),currentRecord.getAttributes().getURI());
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
		Calendar calendar= Calendar.getInstance();
		BigInteger currentTime= BigInteger.valueOf(calendar.getTimeInMillis()).multiply(oneMillion);
		specialProcess.addFutureEvents(actualRecords,currentTime);
		backtrackableRecords.clear();
	}
	//
	@Override
	public void finishPhaseUnsuccessfully() {
		backtrackableRecords.clear();
		createCheckerIfNeed();
		HashMap<ActorAndURI,WebReceptorRecord> actualRecords= new HashMap<>();
		Iterator<WebReceptorRecord> permanentRecordsIterator= permanentRecords.iterator();
		while (permanentRecordsIterator.hasNext()) {
			WebReceptorRecord currentRecord= permanentRecordsIterator.next();
			ActorAndURI key= new ActorAndURI(currentRecord.getActorNumber(),currentRecord.getAttributes().getURI());
			WebReceptorRecord existedRecord= actualRecords.get(key);
			if (existedRecord==null) {
				actualRecords.put(key,currentRecord);
			} else if (existedRecord.getCheckUpPeriod().compareTo(currentRecord.getCheckUpPeriod()) > 0) {
				actualRecords.put(key,currentRecord);
			}
		};
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
	//
	protected void pushWebReceptorRecord(URL_Attributes attributes, ActionPeriod revision, ActionPeriod attempts, ChoisePoint iX) {
		BigDecimal revisionTime= ActionPeriodConverters.toNanosecondsOrDefault(revision,DefaultOptions.revisionPeriod,decimalDefaultRevisionPeriodInNanoseconds,iX);
		BigDecimal attemptTime= ActionPeriodConverters.toNanosecondsOrDefault(attempts,DefaultOptions.attemptPeriod,decimalDefaultAttemptPeriodInNanoseconds,iX);
		if (revisionTime.compareTo(BigDecimal.ZERO) >= 0) {
			if (attemptTime.compareTo(BigDecimal.ZERO) < 0) {
				attemptTime= revisionTime;
			} else if (revisionTime.compareTo(attemptTime) < 0) {
				attemptTime= revisionTime;
			}
		};
		boolean ok= attributes.connectionWasSuccessful();
		if (ok) {
			if (revisionTime.compareTo(BigDecimal.ZERO) < 0) {
				return;
			}
		} else {
			if (attemptTime.compareTo(BigDecimal.ZERO) < 0) {
				return;
			}
		};
		ActorRegister aR= iX.getActorRegister();
		ActorNumber actorNumber= aR.getCurrentActorNumber();
		WebReceptorRecord receptorRecord= new WebReceptorRecord(this,actorNumber,attributes,revisionTime,attemptTime);
		pushPredefinedClassRecord(receptorRecord);
		storePermanentRecord(receptorRecord);
		createCheckerIfNeed();
		try {
			specialPort.isUnknownValue(iX);
		} catch (Backtracking b) {
			throw new CannotUnifySpecialPort();
		}
	}
}
