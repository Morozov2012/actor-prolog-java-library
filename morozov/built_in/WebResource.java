// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.converters.*;
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
	abstract public Term getBuiltInSlot_E_location();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set location
	//
	public void setLocation1s(ChoisePoint iX, Term a1) {
		setLocation(GeneralConverters.argumentToString(a1,iX));
	}
	public void setLocation(String value) {
		location= value;
	}
	public void getLocation0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologString(getLocation(iX)));
	}
	public void getLocation0fs(ChoisePoint iX) {
	}
	public String getLocation(ChoisePoint iX) {
		if (location != null) {
			return location;
		} else {
			Term value= getBuiltInSlot_E_location();
			return GeneralConverters.argumentToString(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public SimpleFileName getName(ChoisePoint iX) {
		boolean currentBackslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		return new SimpleFileName(getLocation(iX),currentBackslashAlwaysIsSeparator,acceptOnlyURI);
	}
	@Override
	public String getExtension(ChoisePoint iX) {
		return "";
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected ExtendedFileName retrieveRealGlobalFileName(ChoisePoint iX) {
		SimpleFileName fileName= getName(iX);
		return fileName.formRealFileNameBasedOnPath(false,true,getExtension(iX),null,staticContext);
	}
	@Override
	protected ExtendedFileName retrieveRealGlobalFileName(Term value, ChoisePoint iX) {
		boolean currentBackslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		ExtendedFileName baseResource= retrieveRealGlobalFileName(iX);
		SimpleFileName fileName= SimpleFileName.argumentToSimpleFileName(value,currentBackslashAlwaysIsSeparator,acceptOnlyURI,iX);
		return fileName.formRealFileNameBasedOnEFN(false,true,getExtension(iX),baseResource,staticContext);
	}
	protected ExtendedFileName retrieveRealGlobalFileName(String value, ChoisePoint iX) {
		boolean currentBackslashAlwaysIsSeparator= getBackslashAlwaysIsSeparator(iX);
		boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
		ExtendedFileName baseResource= retrieveRealGlobalFileName(iX);
		SimpleFileName fileName= SimpleFileName.argumentToSimpleFileName(value,currentBackslashAlwaysIsSeparator,acceptOnlyURI);
		return fileName.formRealFileNameBasedOnEFN(false,false,"",baseResource,staticContext);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getParameters0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getUniversalResourceParameters(iX));
	}
	public void getParameters0fs(ChoisePoint iX) {
	}
	public void getParameters1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		result.setNonBacktrackableValue(getUniversalResourceParameters(a1,iX));
	}
	public void getParameters1fs(ChoisePoint iX, Term a1) {
	}
	//
	protected Term getUniversalResourceParameters(ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			return fileName.getUniversalResourceParameters(timeout,currentCharacterSet,staticContext,iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	protected Term getUniversalResourceParameters(Term argument, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			return fileName.getUniversalResourceParameters(timeout,currentCharacterSet,staticContext,iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void getString0ff(ChoisePoint iX, PrologVariable result) {
		getContent0ff(iX,result);
	}
	@Override
	public void getString0fs(ChoisePoint iX) {
	}
	//
	public void getContent0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getUniversalResourceContent(iX));
	}
	public void getContent0fs(ChoisePoint iX) {
	}
	public void getContent1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		result.setNonBacktrackableValue(getUniversalResourceContent(a1,iX));
	}
	public void getContent1fs(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term getUniversalResourceContent(ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
			return fileName.getUniversalResourceContent(timeout,currentCharacterSet,staticContext,iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
	protected Term getUniversalResourceContent(Term argument, ChoisePoint iX) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		try {
			ExtendedFileName fileName= retrieveRealGlobalFileName(argument,iX);
			return fileName.getUniversalResourceContent(timeout,currentCharacterSet,staticContext,iX);
		} catch (Throwable e) {
			return SimpleFileName.channelExceptionToName(e);
		}
	}
}
