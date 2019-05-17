// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.syntax.*;
import morozov.syntax.errors.*;
import morozov.syntax.data.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public abstract class ActorPrologBacktrackableParser
		extends ActorPrologScanner
		implements ParserMasterInterface {
	//
	protected PrologToken[] tokenStack;
	protected int currentPosition= 0;
	//
	protected HashSet<Long> slotNames;
	protected HashSet<Long> actingSlotNames;
	//
	protected MetaTermParser metaTermParser= new MetaTermParser(this);
	protected GroundTermParser groundTermParser= new GroundTermParser(this);
	//
	protected static Term termParserError= new PrologSymbol(SymbolCodes.symbolCode_E_ParserError);
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologBacktrackableParser() {
	}
	public ActorPrologBacktrackableParser(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_slot_names();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set slot_names
	//
	public void setSlotNames1s(ChoisePoint iX, Term a1) {
		HashSet<Long> symbolCodeList= new HashSet<>();
		GeneralConverters.termToSymbolCodes(symbolCodeList,a1,iX);
		setSlotNames(symbolCodeList);
	}
	public void setSlotNames(HashSet<Long> value) {
		slotNames= value;
	}
	public void getSlotNames0ff(ChoisePoint iX, PrologVariable result) {
		HashSet<Long> value= getSlotNames(iX);
		result.setNonBacktrackableValue(GeneralConverters.codeArrayToSymbolList(value));
	}
	public void getSlotNames0fs(ChoisePoint iX) {
	}
	public HashSet<Long> getSlotNames(ChoisePoint iX) {
		if (slotNames != null) {
			return slotNames;
		} else {
			Term value= getBuiltInSlot_E_slot_names();
			HashSet<Long> symbolCodeList= new HashSet<>();
			GeneralConverters.termToSymbolCodes(symbolCodeList,value,iX);
			return symbolCodeList;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getStackPosition() {
		return currentPosition;
	}
	public void setStackPosition(int p) {
		currentPosition= p;
	}
	//
	public HashMap<String,Integer> getVariableNameRegister() {
		return metaTermParser.getVariableNameRegister();
	}
	public HashMap<Integer,String> getInvertedVariableNameRegister() {
		return metaTermParser.getInvertedVariableNameRegister();
	}
	public HashMap<Integer,VariableRole> getVariableRoleRegister() {
		return metaTermParser.getVariableRoleRegister();
	}
	//
	public void setRecentVariableNumber(int value) {
		metaTermParser.setRecentVariableNumber(value);
	}
	public int getRecentVariableNumber() {
		return metaTermParser.getRecentVariableNumber();
	}
	//
	public ArrayList<FunctionCallDefinition> getFunctionCallDefinitions() {
		return metaTermParser.getFunctionCallDefinitions();
	}
	public int getFunctionCallDefinitionArraySize() {
		return metaTermParser.getFunctionCallDefinitionArraySize();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void scanText1s(ChoisePoint iX, Term a1) throws Backtracking {
		String text= GeneralConverters.argumentToString(a1,iX);
		String[] keywordArray= getKeywords(iX);
		boolean includeTextPositions= getKeepTextPositions(iX);
		updateGeneralAttributes(iX);
		tokenStack= convertTextToTokens(text,keywordArray,includeTextPositions,iX);
		currentPosition= 0;
		actingSlotNames= getSlotNames(iX);
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
	public void popToken1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		checkWhetherTokenStackIsInitiated();
		if (currentPosition < tokenStack.length) {
			iX.pushTrail(new ActorPrologBacktrackableParserStackState(this));
			PrologToken token= tokenStack[currentPosition];
			currentPosition++;
			a1.setBacktrackableValue(token.toActorPrologTerm(),iX);
			result.setNonBacktrackableValue(new PrologInteger(token.getPosition()));
		} else {
			throw Backtracking.instance;
		}
	}
	public void popToken1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		checkWhetherTokenStackIsInitiated();
		if (currentPosition < tokenStack.length) {
			iX.pushTrail(new ActorPrologBacktrackableParserStackState(this));
			PrologToken token= tokenStack[currentPosition];
			currentPosition++;
			a1.setBacktrackableValue(token.toActorPrologTerm(),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void popClause1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseClause(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popClause1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseClause(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popSubgoal1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseSubgoal(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popSubgoal1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseSubgoal(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popAtom1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseAtom(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popAtom1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseAtom(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popBinaryRelation1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootBinaryRelation(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popBinaryRelation1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootBinaryRelation(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popFunctionCall1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootFunctionCall(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popFunctionCall1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootFunctionCall(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popSimpleAtom1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootSimpleAtom(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popSimpleAtom1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootSimpleAtom(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popMetaExpression1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootExpression(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popMetaExpression1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootExpression(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popMetaTerm1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootTerm(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popMetaTerm1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootTerm(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popGroundTerm1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= groundTermParser.parseRootTerm(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void popGroundTerm1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= groundTermParser.parseRootTerm(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void checkEndOfText0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		// updateMetaTermParserAttributes(iX);
		checkWhetherTokenStackIsInitiated();
		updateGeneralAttributes(iX);
		// checkTokenStackSize(iX);
		boolean keepTextPositions= getKeepTextPositions(iX);
		metaTermParser.setTokens(tokenStack,currentPosition,keepTextPositions);
		metaTermParser.setSlotNameHash(actingSlotNames);
		iX.pushTrail(new ActorPrologBacktrackableParserStackState(this));
		int previousPosition= currentPosition;
		try {
			groundTermParser.checkEndOfText(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			result.setNonBacktrackableValue(new PrologInteger(previousPosition));
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	public void checkEndOfText0fs(ChoisePoint iX) throws Backtracking {
		// updateMetaTermParserAttributes(iX);
		checkWhetherTokenStackIsInitiated();
		updateGeneralAttributes(iX);
		// checkTokenStackSize(iX);
		boolean keepTextPositions= getKeepTextPositions(iX);
		metaTermParser.setTokens(tokenStack,currentPosition,keepTextPositions);
		metaTermParser.setSlotNameHash(actingSlotNames);
		iX.pushTrail(new ActorPrologBacktrackableParserStackState(this));
		int previousPosition= currentPosition;
		try {
			groundTermParser.checkEndOfText(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateMetaTermParserAttributes(ChoisePoint iX) throws Backtracking {
		checkWhetherTokenStackIsInitiated();
		updateGeneralAttributes(iX);
		checkTokenStackSize(iX);
		boolean keepTextPositions= getKeepTextPositions(iX);
		metaTermParser.setTokens(tokenStack,currentPosition,keepTextPositions);
		metaTermParser.setSlotNameHash(actingSlotNames);
		iX.pushTrail(new ActorPrologBacktrackableParserStackState(this));
	}
	//
	protected void checkWhetherTokenStackIsInitiated() {
		if (tokenStack==null) {
			throw new ParserStackIsNotInitialized();
		}
	}
	//
	protected void checkTokenStackSize(ChoisePoint iX) throws Backtracking {
		if (	currentPosition >= tokenStack.length ||
			tokenStack[currentPosition].isFinalToken()) {
			ParserError exception= new UnexpectedEndOfTokenList(tokenStack);
			if (actingSendErrorMessages.get()) {
				sendErrorMessage(exception,termParserError,iX);
			};
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(exception);
			};
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void handleError(ParserError exception, ChoisePoint iX) throws ParserError {
		actingThereAreTranslationErrors.set(true);
		if (actingSendErrorMessages.get()) {
			sendErrorMessage(exception,termParserError,iX);
		};
		if (actingRaiseRuntimeExceptions.get()) {
			throw exception;
		} else if (actingStopTranslationAfterFirstError.get()) {
			throw exception;
		}
	}
	//
	public ParserError handleUnexpectedEndOfTokenList(PrologToken[] tokens, ChoisePoint iX) throws ParserError {
		ParserError exception= new UnexpectedEndOfTokenList(tokens);
		handleError(exception,iX);
		return exception;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getVariableNames0ff(ChoisePoint iX, PrologVariable result) {
		Term term= metaTermParser.getVariableNamesTerm();
		result.setNonBacktrackableValue(term);
	}
	public void getVariableNames0fs(ChoisePoint iX) {
	}
	//
	public void forgetVariableNames0s(ChoisePoint iX) {
		metaTermParser.forgetVariableNames();
	}
	//
	public void getFunctionCallTable0ff(ChoisePoint iX, PrologVariable result) {
		Term term= metaTermParser.getFunctionCallTableTerm();
		result.setNonBacktrackableValue(term);
	}
	public void getFunctionCallTable0fs(ChoisePoint iX) {
	}
	//
	public void clearFunctionCallTable0s(ChoisePoint iX) {
		metaTermParser.clearFunctionCallTable();
	}
}
