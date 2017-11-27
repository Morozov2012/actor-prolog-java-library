// (c) 2010-2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.math.BigInteger;

public abstract class DataAbstraction extends CustomControl {
	//
	protected Path currentDirectory= null;
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	public SimpleFileName name= null;
	public String extension= null;
	public TimeInterval transactionWaitingPeriod= null;
	public TimeInterval transactionSleepPeriod= null;
	public BigInteger transactionMaximalRetryNumber= null;
	public Boolean watchUpdates= null;
	//
	protected double defaultTransactionWaitingPeriod= 10.0; // [sec]
	protected double defaultTransactionSleepPeriod= 0.1; // [sec]
	protected int defaultTransactionMaximalRetryNumber= 1000;
	//
	public DataAbstraction() {
	}
	public DataAbstraction(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	protected Term getBuiltInSlot_E_name() {
		return new PrologString("A-Prolog.log");
	}
	protected Term getBuiltInSlot_E_extension() {
		return new PrologString("");
	}
	protected Term getBuiltInSlot_E_transaction_waiting_period() {
		return new PrologReal(defaultTransactionWaitingPeriod);
	}
	protected Term getBuiltInSlot_E_transaction_sleep_period() {
		return new PrologReal(defaultTransactionSleepPeriod);
	}
	protected Term getBuiltInSlot_E_transaction_maximal_retry_number() {
		return new PrologInteger(defaultTransactionMaximalRetryNumber);
	}
	protected Term getBuiltInSlot_E_watch_updates() {
		return termNo;
	}
	public long entry_s_Update_0() {
		return -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set name
	//
	public void setName1s(ChoisePoint iX, Term a1) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		setName(SimpleFileName.argumentToSimpleFileName(a1,backslashAlwaysIsSeparator,acceptOnlyURI,iX));
	}
	public void setName(SimpleFileName value) {
		name= value;
	}
	public void getName0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getName(iX).toTerm());
	}
	public void getName0fs(ChoisePoint iX) {
	}
	public SimpleFileName getName(ChoisePoint iX) {
		if (name != null) {
			return name;
		} else {
			Term value= getBuiltInSlot_E_name();
			boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
			boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
			return SimpleFileName.argumentToSimpleFileName(value,backslashAlwaysIsSeparator,acceptOnlyURI,iX);
		}
	}
	//
	// get/set extension
	//
	public void setExtension1s(ChoisePoint iX, Term a1) {
		try {
			setExtension(a1.getStringValue(iX));
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotFileExtension(a1);
		}
	}
	public void setExtension(String value) {
		extension= value;
	}
	public void getExtension0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologString(getExtension(iX)));
	}
	public void getExtension0fs(ChoisePoint iX) {
	}
	public String getExtension(ChoisePoint iX) {
		if (extension != null) {
			return extension;
		} else {
			Term value= getBuiltInSlot_E_extension();
			try {
				return value.getStringValue(iX);
			} catch (TermIsNotAString e) {
				throw new WrongArgumentIsNotFileExtension(value);
			}
		}
	}
	//
	// get/set transactionWaitingPeriod
	//
	public void setTransactionWaitingPeriod1s(ChoisePoint iX, Term a1) {
		setTransactionWaitingPeriod(TimeInterval.argumentSecondsToTimeInterval(a1,iX));
	}
	public void setTransactionWaitingPeriod(TimeInterval value) {
		transactionWaitingPeriod= value;
	}
	public void getTransactionWaitingPeriod0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getTransactionWaitingPeriod(iX).toTerm());
	}
	public void getTransactionWaitingPeriod0fs(ChoisePoint iX) {
	}
	public TimeInterval getTransactionWaitingPeriod(ChoisePoint iX) {
		if (transactionWaitingPeriod != null) {
			return transactionWaitingPeriod;
		} else {
			Term value= getBuiltInSlot_E_transaction_waiting_period();
			return TimeInterval.argumentSecondsToTimeInterval(value,iX);
		}
	}
	//
	// get/set transactionSleepPeriod
	//
	public void setTransactionSleepPeriod1s(ChoisePoint iX, Term a1) {
		setTransactionSleepPeriod(TimeInterval.argumentSecondsToTimeInterval(a1,iX));
	}
	public void setTransactionSleepPeriod(TimeInterval value) {
		transactionSleepPeriod= value;
	}
	public void getTransactionSleepPeriod0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getTransactionSleepPeriod(iX).toTerm());
	}
	public void getTransactionSleepPeriod0fs(ChoisePoint iX) {
	}
	public TimeInterval getTransactionSleepPeriod(ChoisePoint iX) {
		if (transactionSleepPeriod != null) {
			return transactionSleepPeriod;
		} else {
			Term value= getBuiltInSlot_E_transaction_sleep_period();
			return TimeInterval.argumentSecondsToTimeInterval(value,iX);
		}
	}
	//
	// get/set transaction_maximal_retry_number
	//
	public void setTransactionMaximalRetryNumber1s(ChoisePoint iX, Term a1) {
		setTransactionMaximalRetryNumber(Converters.argumentToStrictInteger(a1,iX));
	}
	public void setTransactionMaximalRetryNumber(BigInteger value) {
		transactionMaximalRetryNumber= value;
	}
	public void getTransactionMaximalRetryNumber0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getTransactionMaximalRetryNumber(iX)));
	}
	public void getTransactionMaximalRetryNumber0fs(ChoisePoint iX) {
	}
	public BigInteger getTransactionMaximalRetryNumber(ChoisePoint iX) {
		if (transactionMaximalRetryNumber != null) {
			return transactionMaximalRetryNumber;
		} else {
			Term value= getBuiltInSlot_E_transaction_maximal_retry_number();
			return Converters.argumentToStrictInteger(value,iX);
		}
	}
	//
	// get/set watch_updates
	//
	public void setWatchUpdates1s(ChoisePoint iX, Term a1) {
		boolean value= YesNo.termYesNo2Boolean(a1,iX);
		setWatchUpdates(value);
	}
	public void setWatchUpdates(boolean value) {
		watchUpdates= value;
	}
	public void getWatchUpdates0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getWatchUpdates(iX)));
	}
	public void getWatchUpdates0fs(ChoisePoint iX) {
	}
	public boolean getWatchUpdates(ChoisePoint iX) {
		if (watchUpdates != null) {
			return watchUpdates;
		} else {
			Term value= getBuiltInSlot_E_watch_updates();
			return YesNo.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected RelativeFileName retrieveRelativeGlobalFileName(ChoisePoint iX) {
		SimpleFileName fileName= getName(iX);
		return fileName.formRelativeFileName(true,getExtension(iX));
	}
	protected RelativeFileName retrieveRelativeGlobalFileName(Term value, ChoisePoint iX) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		SimpleFileName fileName= SimpleFileName.argumentToSimpleFileName(value,backslashAlwaysIsSeparator,acceptOnlyURI,iX);
		return fileName.formRelativeFileName(true,getExtension(iX));
	}
	//
	protected ExtendedFileName retrieveRealGlobalFileName(ChoisePoint iX) {
		SimpleFileName fileName= getName(iX);
		return fileName.formRealFileNameBasedOnPath(false,true,getExtension(iX),currentDirectory,staticContext);
	}
	protected ExtendedFileName retrieveRealGlobalFileName(Term value, ChoisePoint iX) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		// ExtendedFileName baseResource= retrieveRealGlobalFileName(iX);
		SimpleFileName fileName= SimpleFileName.argumentToSimpleFileName(value,backslashAlwaysIsSeparator,acceptOnlyURI,iX);
		return fileName.formRealFileNameBasedOnPath(false,true,getExtension(iX),currentDirectory,staticContext);
	}
	protected ExtendedFileName retrieveRealGlobalFileName(String value, ChoisePoint iX) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		// ExtendedFileName baseResource= retrieveRealGlobalFileName(iX);
		SimpleFileName fileName= SimpleFileName.argumentToSimpleFileName(value,backslashAlwaysIsSeparator,acceptOnlyURI);
		return fileName.formRealFileNameBasedOnPath(false,false,"",currentDirectory,staticContext);
	}
	//
	protected ExtendedFileName retrieveRealLocalFileName(ChoisePoint iX) {
		SimpleFileName fileName= getName(iX);
		return fileName.formRealFileNameBasedOnPath(true,true,getExtension(iX),currentDirectory,staticContext);
	}
	protected ExtendedFileName retrieveRealLocalFileName(Term value, ChoisePoint iX) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		// ExtendedFileName baseResource= retrieveRealGlobalFileName(iX);
		SimpleFileName fileName= SimpleFileName.argumentToSimpleFileName(value,backslashAlwaysIsSeparator,acceptOnlyURI,iX);
		return fileName.formRealFileNameBasedOnPath(true,true,getExtension(iX),currentDirectory,staticContext);
	}
	//
	protected ExtendedFileName retrieveRealLocalDirectoryName(ChoisePoint iX) {
		SimpleFileName fileName= getName(iX);
		return fileName.formRealFileNameBasedOnPath(true,false,getExtension(iX),currentDirectory,staticContext);
	}
	protected ExtendedFileName retrieveRealLocalDirectoryName(Term value, ChoisePoint iX) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		// ExtendedFileName baseResource= retrieveRealGlobalFileName(iX);
		SimpleFileName fileName= SimpleFileName.argumentToSimpleFileName(value,backslashAlwaysIsSeparator,acceptOnlyURI,iX);
		return fileName.formRealFileNameBasedOnPath(true,false,getExtension(iX),currentDirectory,staticContext);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setCurrentDirectory0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalDirectoryName(iX);
		currentDirectory= fileName.setCurrentDirectory();
	}
	public void setCurrentDirectory1s(ChoisePoint iX, Term value) {
		ExtendedFileName fileName= retrieveRealLocalDirectoryName(value,iX);
		currentDirectory= fileName.setCurrentDirectory();
	}
	//
	public void getCurrentDirectory0ff(ChoisePoint iX, PrologVariable result) {
		String textName= ExtendedFileName.getCurrentDirectory(currentDirectory);
		result.setNonBacktrackableValue(new PrologString(textName));
	}
	public void getCurrentDirectory0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void makeDirectory0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalDirectoryName(iX);
		fileName.makeDirectory();
	}
	public void makeDirectory1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalDirectoryName(a1,iX);
		fileName.makeDirectory();
	}
	//
	public void listDirectory0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ExtendedFileName.listDirectory(currentDirectory));
	}
	public void listDirectory0fs(ChoisePoint iX) {
	}
	public void listDirectory1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		try {
			String mask= a1.getStringValue(iX);
			result.setNonBacktrackableValue(ExtendedFileName.listDirectory(currentDirectory,mask,backslashAlwaysIsSeparator));
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	public void listDirectory1fs(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getFullName0ff(ChoisePoint iX, PrologVariable result) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		result.setNonBacktrackableValue(fileName.toFullName());
	}
	public void getFullName0fs(ChoisePoint iX) {
	}
	public void getFullName1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		result.setNonBacktrackableValue(fileName.toFullName());
	}
	public void getFullName1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void getURL0ff(ChoisePoint iX, PrologVariable result) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		result.setNonBacktrackableValue(fileName.toURL());
	}
	public void getURL0fs(ChoisePoint iX) {
	}
	public void getURL1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		result.setNonBacktrackableValue(fileName.toURL());
	}
	public void getURL1fs(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractPath3s(ChoisePoint iX, Term a1, PrologVariable a2, PrologVariable a3) {
		RelativeFileName fileName= retrieveRelativeGlobalFileName(a1,iX);
		fileName.extractFileName(iX,a2,a3);
	}
	public void extractPath2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		RelativeFileName fileName= retrieveRelativeGlobalFileName(iX);
		fileName.extractFileName(iX,a1,a2);
	}
	//
	public void extractExtension2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		RelativeFileName fileName= retrieveRelativeGlobalFileName(iX);
		fileName.extractFileExtension(iX,a1,a2);
	}
	public void extractExtension3s(ChoisePoint iX, Term a1, PrologVariable a2, PrologVariable a3) {
		RelativeFileName fileName= retrieveRelativeGlobalFileName(a1,iX);
		fileName.extractFileExtension(iX,a2,a3);
	}
	//
	public void replaceExtension1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		RelativeFileName fileName= retrieveRelativeGlobalFileName(iX);
		String newExtension= Converters.argumentToString(a1,iX);
		result.setNonBacktrackableValue(fileName.replaceFileExtension(newExtension));
		// iX.pushTrail(result);
	}
	public void replaceExtension1fs(ChoisePoint iX, Term a1) {
	}
	public void replaceExtension2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		RelativeFileName fileName= retrieveRelativeGlobalFileName(a1,iX);
		String newExtension= Converters.argumentToString(a2,iX);
		result.setNonBacktrackableValue(fileName.replaceFileExtension(newExtension));
		// iX.pushTrail(result);
	}
	public void replaceExtension2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void doesExist0s(ChoisePoint iX) throws Backtracking {
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			int timeout= getMaximalWaitingTimeInMilliseconds(iX);
			CharacterSet characterSet= getCharacterSet(iX);
			fileName.doesExist(timeout,characterSet,staticContext);
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	public void doesExist1s(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
			int timeout= getMaximalWaitingTimeInMilliseconds(iX);
			CharacterSet characterSet= getCharacterSet(iX);
			fileName.doesExist(timeout,characterSet,staticContext);
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	//
	public void isLocalResource0s(ChoisePoint iX) throws Backtracking {
		// RelativeFileName fileName= retrieveRelativeGlobalFileName(iX);
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		fileName.isLocalResource();
	}
	public void isLocalResource1s(ChoisePoint iX, Term a1) throws Backtracking {
		// RelativeFileName fileName= retrieveRelativeGlobalFileName(a1,iX);
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		fileName.isLocalResource();
	}
	//
	public void isStandardFile0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		if (!fileName.isStandardFile()) {
			throw Backtracking.instance;
		}
	}
	public void isStandardFile1s(ChoisePoint iX, Term a1) throws Backtracking {
		ExtendedFileName fileName= retrieveRealGlobalFileName(a1,iX);
		if (!fileName.isStandardFile()) {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void delete0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		fileName.delete();
	}
	public void delete1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		fileName.delete();
	}
	//
	public void rename1s(ChoisePoint iX, Term destination) {
		ExtendedFileName fileName2= retrieveRealLocalFileName(destination,iX);
		ExtendedFileName fileName1= retrieveRealLocalFileName(iX);
		fileName1.renameFile(fileName2);
	}
	public void rename2s(ChoisePoint iX, Term source, Term destination) {
		ExtendedFileName fileName1= retrieveRealLocalFileName(source,iX);
		ExtendedFileName fileName2= retrieveRealLocalFileName(destination,iX);
		fileName1.renameFile(fileName2);
	}
	//
	public void copy1s(ChoisePoint iX, Term destination) {
		ExtendedFileName fileName2= retrieveRealLocalFileName(destination,iX);
		ExtendedFileName fileName1= retrieveRealLocalFileName(iX);
		fileName1.copyFile(fileName2);
	}
	public void copy2s(ChoisePoint iX, Term source, Term destination) {
		ExtendedFileName fileName1= retrieveRealLocalFileName(source,iX);
		ExtendedFileName fileName2= retrieveRealLocalFileName(destination,iX);
		fileName1.copyFile(fileName2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isDirectory0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalDirectoryName(iX);
		fileName.isDirectory();
	}
	public void isDirectory1s(ChoisePoint iX, Term a1) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalDirectoryName(a1,iX);
		fileName.isDirectory();
	}
	//
	public void isNormal0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		fileName.isNormal();
	}
	public void isNormal1s(ChoisePoint iX, Term a1) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		fileName.isNormal();
	}
	//
	public void isArchive0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		fileName.isArchive();
	}
	public void isArchive1s(ChoisePoint iX, Term a1) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		fileName.isArchive();
	}
	//
	public void isHidden0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		fileName.isHidden();
	}
	public void isHidden1s(ChoisePoint iX, Term a1) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		fileName.isHidden();
	}
	//
	public void isReadOnly0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		fileName.isReadOnly();
	}
	public void isReadOnly1s(ChoisePoint iX, Term a1) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		fileName.isReadOnly();
	}
	//
	public void isSystem0s(ChoisePoint iX) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		fileName.isSystem();
	}
	public void isSystem1s(ChoisePoint iX, Term a1) throws Backtracking {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		fileName.isSystem();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setArchive1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		boolean flag= OnOff.termOnOff2Boolean(mode,iX);
		fileName.setArchive(flag);
	}
	public void setArchive2s(ChoisePoint iX, Term a1, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		boolean flag= OnOff.termOnOff2Boolean(mode,iX);
		fileName.setArchive(flag);
	}
	//
	public void setHidden1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		boolean flag= OnOff.termOnOff2Boolean(mode,iX);
		fileName.setHidden(flag);
	}
	public void setHidden2s(ChoisePoint iX, Term a1, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		boolean flag= OnOff.termOnOff2Boolean(mode,iX);
		fileName.setHidden(flag);
	}
	//
	public void setReadOnly1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		boolean flag= OnOff.termOnOff2Boolean(mode,iX);
		fileName.setReadOnly(flag);
	}
	public void setReadOnly2s(ChoisePoint iX, Term a1, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		boolean flag= OnOff.termOnOff2Boolean(mode,iX);
		fileName.setReadOnly(flag);
	}
	//
	public void setSystem1s(ChoisePoint iX, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		boolean flag= OnOff.termOnOff2Boolean(mode,iX);
		fileName.setSystem(flag);
	}
	public void setSystem2s(ChoisePoint iX, Term a1, Term mode) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		boolean flag= OnOff.termOnOff2Boolean(mode,iX);
		fileName.setSystem(flag);
	}
}
