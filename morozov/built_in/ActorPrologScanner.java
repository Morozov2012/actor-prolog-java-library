// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.system.*;
import morozov.syntax.scanner.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ActorPrologScanner
		extends Text
		implements LexicalScannerMasterInterface {
	//
	protected String[] keywords;
	protected Boolean keepTextPositions;
	//
	protected YesNo stopTranslationAfterFirstError;
	protected YesNo raiseRuntimeExceptions;
	protected YesNo sendErrorMessages;
	//
	protected AtomicBoolean actingStopTranslationAfterFirstError= new AtomicBoolean(false);
	protected AtomicBoolean actingRaiseRuntimeExceptions= new AtomicBoolean(true);
	protected AtomicBoolean actingSendErrorMessages= new AtomicBoolean(false);
	protected AtomicBoolean actingThereAreTranslationErrors= new AtomicBoolean(false);
	//
	protected static Term termLexicalScannerError= new PrologSymbol(SymbolCodes.symbolCode_E_LexicalScannerError);
	// protected static Term termEmptyString= new PrologString("");
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologScanner() {
	}
	public ActorPrologScanner(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_keywords();
	abstract public Term getBuiltInSlot_E_keep_text_positions();
	abstract public Term getBuiltInSlot_E_stop_translation_after_first_error();
	abstract public Term getBuiltInSlot_E_raise_runtime_exceptions();
	abstract public Term getBuiltInSlot_E_send_error_messages();
	//
	abstract public long entry_s_SyntaxError_4_iiii();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set keywords
	//
	public void setKeywords1s(ChoisePoint iX, Term a1) {
		setKeywords(GeneralConverters.termToStrings(a1,iX));
	}
	public void setKeywords(String[] value) {
		keywords= value;
	}
	public void getKeywords0ff(ChoisePoint iX, PrologVariable result) {
		String[] value= getKeywords(iX);
		result.setNonBacktrackableValue(GeneralConverters.stringArrayToList(value));
	}
	public void getKeywords0fs(ChoisePoint iX) {
	}
	public String[] getKeywords(ChoisePoint iX) {
		if (keywords != null) {
			return keywords;
		} else {
			Term value= getBuiltInSlot_E_keywords();
			return GeneralConverters.termToStrings(value,iX);
		}
	}
	//
	// get/set keepTextPositions
	//
	public void setKeepTextPositions1s(ChoisePoint iX, Term a1) {
		setKeepTextPositions(YesNoConverters.termYesNo2Boolean(a1,iX));
	}
	public void setKeepTextPositions(boolean value) {
		keepTextPositions= value;
	}
	public void getKeepTextPositions0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getKeepTextPositions(iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void getKeepTextPositions0fs(ChoisePoint iX) {
	}
	public boolean getKeepTextPositions(ChoisePoint iX) {
		if (keepTextPositions != null) {
			return keepTextPositions;
		} else {
			Term value= getBuiltInSlot_E_keep_text_positions();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set stop_translation_after_first_error
	//
	public void setStopTranslationAfterFirstError1s(ChoisePoint iX, Term a1) {
		setStopTranslationAfterFirstError(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setStopTranslationAfterFirstError(YesNo value) {
		stopTranslationAfterFirstError= value;
		actingStopTranslationAfterFirstError.set(value.toBoolean());
	}
	public void getStopTranslationAfterFirstError0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getStopTranslationAfterFirstError(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getStopTranslationAfterFirstError0fs(ChoisePoint iX) {
	}
	public YesNo getStopTranslationAfterFirstError(ChoisePoint iX) {
		if (stopTranslationAfterFirstError != null) {
			return stopTranslationAfterFirstError;
		} else {
			Term value= getBuiltInSlot_E_stop_translation_after_first_error();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set raise_runtime_exceptions
	//
	public void setRaiseRuntimeExceptions1s(ChoisePoint iX, Term a1) {
		setRaiseRuntimeExceptions(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setRaiseRuntimeExceptions(YesNo value) {
		raiseRuntimeExceptions= value;
		actingRaiseRuntimeExceptions.set(value.toBoolean());
	}
	public void getRaiseRuntimeExceptions0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getRaiseRuntimeExceptions(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getRaiseRuntimeExceptions0fs(ChoisePoint iX) {
	}
	public YesNo getRaiseRuntimeExceptions(ChoisePoint iX) {
		if (raiseRuntimeExceptions != null) {
			return raiseRuntimeExceptions;
		} else {
			Term value= getBuiltInSlot_E_raise_runtime_exceptions();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set send_error_messages
	//
	public void setSendErrorMessages1s(ChoisePoint iX, Term a1) {
		setSendErrorMessages(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setSendErrorMessages(YesNo value) {
		sendErrorMessages= value;
		actingSendErrorMessages.set(value.toBoolean());
	}
	public void getSendErrorMessages0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getSendErrorMessages(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getSendErrorMessages0fs(ChoisePoint iX) {
	}
	public YesNo getSendErrorMessages(ChoisePoint iX) {
		if (sendErrorMessages != null) {
			return sendErrorMessages;
		} else {
			Term value= getBuiltInSlot_E_send_error_messages();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void convertToTokens1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		String text= GeneralConverters.argumentToString(a1,iX);
		String[] keywordArray= getKeywords(iX);
		boolean includeTextPositions= getKeepTextPositions(iX);
		updateGeneralAttributes(iX);
		PrologToken[] tokens= convertTextToTokens(text,keywordArray,includeTextPositions,iX);
		ArrayList<Term> tokenTermArray= new ArrayList<>();
		int counter= 0;
		while (counter < tokens.length) {
			PrologToken token= tokens[counter];
			Term term;
			if (includeTextPositions) {
				term= token.toTermTP();
			} else {
				term= token.toActorPrologTerm();
			};
			tokenTermArray.add(term);
			counter++;
		};
		Term tokenListTerm= PrologEmptyList.instance;
		for (int k=tokenTermArray.size()-1; k >= 0; k--) {
			tokenListTerm= new PrologList(tokenTermArray.get(k),tokenListTerm);
		};
		result.setNonBacktrackableValue(tokenListTerm);
	}
	public void convertToTokens1fs(ChoisePoint iX, Term a1) throws Backtracking {
	}
	//
	protected PrologToken[] convertTextToTokens(String text, String[] keywordArray, boolean includeTextPositions, ChoisePoint iX) throws Backtracking {
		HashSet<String> keywordHash= new HashSet<>();
		HashSet<String> reservedNameHash= new HashSet<>();
		for (int k=0; k < keywordArray.length; k++) {
			String keyword= keywordArray[k];
			keywordHash.add(keyword);
			reservedNameHash.add(keyword.toLowerCase());
		};
		LexicalScanner scanner= new LexicalScanner(this,false,keywordHash,reservedNameHash);
		try {
			PrologToken[] tokens= scanner.analyse(text,iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			return tokens;
		} catch (LexicalScannerError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologLexicalScannerError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateGeneralAttributes(ChoisePoint iX) {
		boolean stopTranslation= getStopTranslationAfterFirstError(iX).toBoolean();
		boolean raiseExceptions= getRaiseRuntimeExceptions(iX).toBoolean();
		boolean sendMessages= getSendErrorMessages(iX).toBoolean();
		actingStopTranslationAfterFirstError.set(stopTranslation);
		actingRaiseRuntimeExceptions.set(raiseExceptions);
		actingSendErrorMessages.set(sendMessages);
		actingThereAreTranslationErrors.set(false);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void handleError(LexicalScannerError exception, ChoisePoint iX) throws LexicalScannerError {
		actingThereAreTranslationErrors.set(true);
		if (actingSendErrorMessages.get()) {
			sendErrorMessage(exception,termLexicalScannerError,iX);
		};
		if (actingRaiseRuntimeExceptions.get()) {
			throw exception;
		} else if (actingStopTranslationAfterFirstError.get()) {
			throw exception;
		}
	}
	//
	public LexicalScannerError handleUnexpectedEndOfText(int position, ChoisePoint iX) throws LexicalScannerError {
		LexicalScannerError exception= new UnexpectedEndOfText(position);
		handleError(exception,iX);
		return exception;
	}
	//
	protected void sendErrorMessage(SyntaxError exception, Term errorType, ChoisePoint iX) {
		Term[] arguments= new Term[4];
		arguments[0]= errorType;
		String errorName= exception.getClass().getName();
		int beginning= errorName.lastIndexOf('.');
		if (beginning >= 0) {
			beginning++;
			errorName= errorName.substring(beginning);
		};
		arguments[1]= new PrologString(errorName);
		arguments[2]= new PrologInteger(exception.getPosition());
		arguments[3]= new PrologString(exception.toString());
		long domainSignature= entry_s_SyntaxError_4_iiii();
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void syntaxError4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
	}
}
