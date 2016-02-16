// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.files.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class WebResource extends SymbolicInformation {
	//
	protected String location= null;
	//
	///////////////////////////////////////////////////////////////
	//
	public WebResource() {
	}
	public WebResource(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract protected Term getBuiltInSlot_E_location();
	// abstract protected Term getBuiltInSlot_E_max_waiting_time();
	// abstract protected Term getBuiltInSlot_E_character_set();
	// abstract protected Term getBuiltInSlot_E_backslash_always_is_separator();
	// abstract protected Term getBuiltInSlot_E_mask();
	// abstract protected Term getBuiltInSlot_E_send_full_path();
	// abstract protected Term getBuiltInSlot_E_query();
	// abstract protected Term getBuiltInSlot_E_content_type();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set location
	//
	public void setLocation1s(ChoisePoint iX, Term a1) {
		setLocation(Converters.argumentToString(a1,iX));
	}
	public void setLocation(String value) {
		location= value;
	}
	public void getLocation0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= new PrologString(getLocation(iX));
	}
	public void getLocation0fs(ChoisePoint iX) {
	}
	public String getLocation(ChoisePoint iX) {
		if (location != null) {
			return location;
		} else {
			Term value= getBuiltInSlot_E_location();
			return Converters.argumentToString(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public SimpleFileName getName(ChoisePoint iX) {
		boolean backslashIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		return new SimpleFileName(getLocation(iX),backslashIsSeparator,acceptOnlyURI);
	}
	public String getExtension(ChoisePoint iX) {
		return "";
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected ExtendedFileName retrieveRealGlobalFileName(ChoisePoint iX) {
		SimpleFileName fileName= getName(iX);
		return fileName.formRealFileNameBasedOnPath(false,true,getExtension(iX),null,staticContext);
	}
	protected ExtendedFileName retrieveRealGlobalFileName(Term value, ChoisePoint iX) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		ExtendedFileName baseResource= retrieveRealGlobalFileName(iX);
		SimpleFileName fileName= SimpleFileName.termToSimpleFileName(value,backslashAlwaysIsSeparator,acceptOnlyURI,iX);
		return fileName.formRealFileNameBasedOnEFN(false,true,getExtension(iX),baseResource,staticContext);
	}
	protected ExtendedFileName retrieveRealGlobalFileName(String value, ChoisePoint iX) {
		boolean backslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		ExtendedFileName baseResource= retrieveRealGlobalFileName(iX);
		SimpleFileName fileName= SimpleFileName.termToSimpleFileName(value,backslashAlwaysIsSeparator,acceptOnlyURI);
		return fileName.formRealFileNameBasedOnEFN(false,false,"",baseResource,staticContext);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getParameters0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= getUniversalResourceParameters(iX);
	}
	public void getParameters0fs(ChoisePoint iX) {
	}
	public void getParameters1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		a1.value= getUniversalResourceParameters(a2,iX);
	}
	public void getParameters1fs(ChoisePoint iX, Term a1) {
	}
	//
	protected Term getUniversalResourceParameters(ChoisePoint iX) {
		int timeout= getMaxWaitingTimeInMilliseconds(iX);
		CharacterSet characterSet= getCharacterSet(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			return fileName.getUniversalResourceParameters(timeout,characterSet,staticContext,iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	protected Term getUniversalResourceParameters(Term argument, ChoisePoint iX) {
		int timeout= getMaxWaitingTimeInMilliseconds(iX);
		CharacterSet characterSet= getCharacterSet(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			return fileName.getUniversalResourceParameters(timeout,characterSet,staticContext,iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getString0ff(ChoisePoint iX, PrologVariable outputText) {
		getContent0ff(iX,outputText);
	}
	public void getString0fs(ChoisePoint iX) {
	}
	//
	public void getContent0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= getUniversalResourceContent(iX);
	}
	public void getContent0fs(ChoisePoint iX) {
	}
	public void getContent1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		a1.value= getUniversalResourceContent(a2,iX);
	}
	public void getContent1fs(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term getUniversalResourceContent(ChoisePoint iX) {
		int timeout= getMaxWaitingTimeInMilliseconds(iX);
		CharacterSet characterSet= getCharacterSet(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			return fileName.getUniversalResourceContent(timeout,characterSet,staticContext,iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	protected Term getUniversalResourceContent(Term argument, ChoisePoint iX) {
		int timeout= getMaxWaitingTimeInMilliseconds(iX);
		CharacterSet characterSet= getCharacterSet(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			return fileName.getUniversalResourceContent(timeout,characterSet,staticContext,iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
}
