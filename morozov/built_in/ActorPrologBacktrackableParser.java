// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.syntax.*;
import morozov.syntax.errors.*;
import morozov.syntax.data.*;
import morozov.syntax.data.domains.*;
import morozov.syntax.data.importation.*;
import morozov.syntax.data.parameters.*;
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
	protected PackageParser metaTermParser= new PackageParser(this);
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
	public void setUseSecondVariableNameRegister(boolean mode) {
		metaTermParser.setUseSecondVariableNameRegister(mode);
	}
	public boolean useSecondVariableNameRegister() {
		return metaTermParser.useSecondVariableNameRegister();
	}
	//
	public HashMap<String,Integer> getFirstVariableNameRegister() {
		return metaTermParser.getFirstVariableNameRegister();
	}
	public HashMap<String,Integer> getSecondVariableNameRegister() {
		return metaTermParser.getSecondVariableNameRegister();
	}
	//
	public HashMap<Integer,String> getFirstInvertedVariableNameRegister() {
		return metaTermParser.getFirstInvertedVariableNameRegister();
	}
	public HashMap<Integer,String> getSecondInvertedVariableNameRegister() {
		return metaTermParser.getSecondInvertedVariableNameRegister();
	}
	//
	public void setFirstRecentVariableNumber(int value) {
		metaTermParser.setFirstRecentVariableNumber(value);
	}
	public int getFirstRecentVariableNumber() {
		return metaTermParser.getFirstRecentVariableNumber();
	}
	//
	public void setSecondRecentVariableNumber(int value) {
		metaTermParser.setSecondRecentVariableNumber(value);
	}
	public int getSecondRecentVariableNumber() {
		return metaTermParser.getSecondRecentVariableNumber();
	}
	//
	public HashMap<Integer,VariableRole> getFirstVariableRoleRegister() {
		return metaTermParser.getFirstVariableRoleRegister();
	}
	public HashMap<Integer,VariableRole> getSecondVariableRoleRegister() {
		return metaTermParser.getSecondVariableRoleRegister();
	}
	//
	public ArrayList<Integer> getFirstAnonymousVariableRegister() {
		return metaTermParser.getFirstAnonymousVariableRegister();
	}
	public ArrayList<Integer> getSecondAnonymousVariableRegister() {
		return metaTermParser.getSecondAnonymousVariableRegister();
	}
	//
	public int getFirstAnonymousVariableRegisterSize() {
		return metaTermParser.getFirstAnonymousVariableRegisterSize();
	}
	public int getSecondAnonymousVariableRegisterSize() {
		return metaTermParser.getSecondAnonymousVariableRegisterSize();
	}
	//
	public ArrayList<FunctionCallDefinition> getFirstFunctionCallDefinitions() {
		return metaTermParser.getFirstFunctionCallDefinitions();
	}
	public ArrayList<FunctionCallDefinition> getSecondFunctionCallDefinitions() {
		return metaTermParser.getSecondFunctionCallDefinitions();
	}
	//
	public int getFirstFunctionCallDefinitionArraySize() {
		return metaTermParser.getFirstFunctionCallDefinitionArraySize();
	}
	public int getSecondFunctionCallDefinitionArraySize() {
		return metaTermParser.getSecondFunctionCallDefinitionArraySize();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void scanText1s(ChoisePoint iX, Term a1) throws Backtracking {
		String text= GeneralConverters.argumentToString(a1,iX);
		String[] keywordArray= getKeywords(iX);
		boolean includeTextPositions= getKeepTextPositions(iX);
		boolean useRobustMode= getAcceptErroneousText(iX);
		updateGeneralAttributes(iX);
		tokenStack= convertTextToTokens(text,keywordArray,includeTextPositions,useRobustMode,iX);
		currentPosition= 0;
		actingSlotNames= getSlotNames(iX);
	}
	//
	public void pushText1s(ChoisePoint iX, Term a1) throws Backtracking {
		String text= GeneralConverters.argumentToString(a1,iX);
		String[] keywordArray= getKeywords(iX);
		boolean includeTextPositions= getKeepTextPositions(iX);
		boolean useRobustMode= getAcceptErroneousText(iX);
		updateGeneralAttributes(iX);
		PrologToken[] newTokens= convertTextToTokens(text,keywordArray,includeTextPositions,useRobustMode,iX);
		if (tokenStack==null || tokenStack.length <= 0) {
			tokenStack= newTokens;
			currentPosition= 0;
		} else {
			int currentLength= tokenStack.length;
			int additionalLength= newTokens.length;
			tokenStack= new PrologToken[currentLength+additionalLength];
			System.arraycopy(newTokens,0,tokenStack,currentLength,additionalLength);
		};
		actingSlotNames= getSlotNames(iX);
	}
	//
	@Override
	protected PrologToken[] convertTextToTokens(String text, String[] keywordArray, boolean includeTextPositions, boolean useRobustMode, ChoisePoint iX) throws Backtracking {
		LexicalScanner scanner= new LexicalScanner(this,useRobustMode,keywordArray);
		try {
			actingThereAreTranslationErrors.set(false);
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
	public void popToken1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		checkWhetherTokenStackIsInitiated();
		if (currentPosition < tokenStack.length) {
			iX.pushTrail(new ActorPrologBacktrackableParserStackState(this));
			PrologToken token= tokenStack[currentPosition];
			currentPosition++;
			if (!token.correspondsToActorPrologTerm(a1,iX)) {
				throw Backtracking.instance;
			};
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
			if (!token.correspondsToActorPrologTerm(a1,iX)) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	public void popToken1fs(ChoisePoint iX, Term a1) throws Backtracking {
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
	public void popPackage9ff(ChoisePoint iX, PrologVariable result, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6, PrologVariable a7, PrologVariable a8, PrologVariable a9) throws Backtracking {
		popPackage(result,a1,a2,a3,a4,a5,a6,a7,a8,a9,iX);
	}
	public void popPackage9fs(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6, PrologVariable a7, PrologVariable a8, PrologVariable a9) throws Backtracking {
		popPackage(null,a1,a2,a3,a4,a5,a6,a7,a8,a9,iX);
	}
	//
	protected void popPackage(PrologVariable result, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6, PrologVariable a7, PrologVariable a8, PrologVariable a9, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(false,iX);
		int previousPosition= currentPosition;
		try {
			metaTermParser.parsePackage(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			Term termPackageName= metaTermParser.getTermPackageName();
			ActorPrologPackageParameter[] formalParameters= metaTermParser.getFormalParameters();
			ActorPrologPragma[] pragmaList= metaTermParser.getPragmaList();
			ActorPrologPackageImportCommand[] importCommands= metaTermParser.getImportCommands();
			ActorPrologDomainDefinition[] domainDefinitions= metaTermParser.getDomainDefinitions();
			ActorPrologPredicateDeclaration[] predicateDeclarations= metaTermParser.getPredicateDeclarations();
			ActorPrologUnit[] units= metaTermParser.getUnits();
			Term termProject= metaTermParser.getTermProject();
			Term termProjectVariableNames= metaTermParser.getTermProjectVariableNames();
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(termPackageName,iX);
			Term termFormalParameter= ActorPrologPackageParameter.arrayToList(formalParameters);
			a2.setBacktrackableValue(termFormalParameter,iX);
			Term termPragmaList= ActorPrologPragma.arrayToList(pragmaList);
			a3.setBacktrackableValue(termPragmaList,iX);
			Term termImportCommands= ActorPrologPackageImportCommand.arrayToList(importCommands);
			a4.setBacktrackableValue(termImportCommands,iX);
			Term termDomainDefinitions= ActorPrologDomainDefinition.arrayToList(domainDefinitions);
			a5.setBacktrackableValue(termDomainDefinitions,iX);
			Term termPredicateDeclarations= ActorPrologPredicateDeclaration.arrayToList(predicateDeclarations);
			a6.setBacktrackableValue(termPredicateDeclarations,iX);
			Term termUnits= ActorPrologUnit.arrayToList(units);
			a7.setBacktrackableValue(termUnits,iX);
			a8.setBacktrackableValue(termProject,iX);
			a9.setBacktrackableValue(termProjectVariableNames,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
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
	public void popClass8ff(ChoisePoint iX, PrologVariable result, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6, PrologVariable a7, PrologVariable a8) throws Backtracking {
		popClass(result,a1,a2,a3,a4,a5,a6,a7,a8,iX);
	}
	public void popClass8fs(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6, PrologVariable a7, PrologVariable a8) throws Backtracking {
		popClass(null,a1,a2,a3,a4,a5,a6,a7,a8,iX);
	}
	//
	protected void popClass(PrologVariable result, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6, PrologVariable a7, PrologVariable a8, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			metaTermParser.parseClass(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			long classNameCode= metaTermParser.getClassNameCode();
			Term termAncestor= metaTermParser.getTermAncestor();
			ActorPrologSlotDefinition[] slotDefinitions= metaTermParser.getSlotDefinitions();
			ActorPrologClause[] actingClauses= metaTermParser.getActingClauses();
			ActorPrologClause[] modelClauses= metaTermParser.getModelClauses();
			Term termClassSource= metaTermParser.getTermClassSource();
			Term termInterface= metaTermParser.getTermInterface();
			Term termParsedVariableNames= metaTermParser.getTermParsedVariableNames();
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(new PrologSymbol(classNameCode),iX);
			a2.setBacktrackableValue(termAncestor,iX);
			Term termSlotDefinitions= ActorPrologSlotDefinition.arrayToList(slotDefinitions);
			a3.setBacktrackableValue(termSlotDefinitions,iX);
			Term termActingClauses= ActorPrologClause.arrayToList(actingClauses);
			a4.setBacktrackableValue(termActingClauses,iX);
			Term termModelClauses= ActorPrologClause.arrayToList(modelClauses);
			a5.setBacktrackableValue(termModelClauses,iX);
			a6.setBacktrackableValue(termClassSource,iX);
			a7.setBacktrackableValue(termInterface,iX);
			a8.setBacktrackableValue(termParsedVariableNames,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popInterface6ff(ChoisePoint iX, PrologVariable result, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6) throws Backtracking {
		popInterface(result,a1,a2,a3,a4,a5,a6,iX);
	}
	public void popInterface6fs(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6) throws Backtracking {
		popInterface(null,a1,a2,a3,a4,a5,a6,iX);
	}
	//
	protected void popInterface(PrologVariable result, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4, PrologVariable a5, PrologVariable a6, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			metaTermParser.parseInterface(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			long classNameCode= metaTermParser.getClassNameCode();
			long[] ancestorNameCodes= metaTermParser.getAncestorNameCodes();
			boolean isMetaInterface= metaTermParser.isMetaInterface();
			ActorPrologSlotDeclaration[] slotDeclarations= metaTermParser.getSlotDeclarations();
			ActorPrologDomainDefinition[] domainDefinitions= metaTermParser.getDomainDefinitions();
			ActorPrologPredicateDeclaration[] predicateDeclarations= metaTermParser.getPredicateDeclarations();
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(new PrologSymbol(classNameCode),iX);
			a2.setBacktrackableValue(GeneralConverters.codeArrayToSymbolList(ancestorNameCodes),iX);
			a3.setBacktrackableValue(YesNoConverters.boolean2TermYesNo(isMetaInterface),iX);
			Term termSlotDeclarations= ActorPrologSlotDeclaration.arrayToList(slotDeclarations);
			a4.setBacktrackableValue(termSlotDeclarations,iX);
			Term termDomainDefinitions= ActorPrologDomainDefinition.arrayToList(domainDefinitions);
			a5.setBacktrackableValue(termDomainDefinitions,iX);
			Term termPredicateDeclarations= ActorPrologPredicateDeclaration.arrayToList(predicateDeclarations);
			a6.setBacktrackableValue(termPredicateDeclarations,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
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
	public void popPredicateDeclarations2ff(ChoisePoint iX, PrologVariable result, Term a1, PrologVariable a2) throws Backtracking {
		boolean parseImperatives= YesNoConverters.argument2YesNo(a1,iX).toBoolean();
		popPredicateDeclarations(result,parseImperatives,a2,iX);
	}
	public void popPredicateDeclarations2fs(ChoisePoint iX, Term a1, PrologVariable a2) throws Backtracking {
		boolean parseImperatives= YesNoConverters.argument2YesNo(a1,iX).toBoolean();
		popPredicateDeclarations(null,parseImperatives,a2,iX);
	}
	public void popPredicateDeclarations1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popPredicateDeclarations(result,false,a1,iX);
	}
	public void popPredicateDeclarations1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popPredicateDeclarations(null,false,a1,iX);
	}
	//
	protected void popPredicateDeclarations(PrologVariable result, boolean parseImperatives, PrologVariable a2, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			ActorPrologPredicateDeclaration[] definitions= metaTermParser.parsePredicateDeclarations(parseImperatives,iX);
			Term term= ActorPrologPredicateDeclaration.arrayToList(definitions);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a2.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popPredicateDeclaration1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popPredicateDeclaration(result,a1,iX);
	}
	public void popPredicateDeclaration1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popPredicateDeclaration(null,a1,iX);
	}
	//
	protected void popPredicateDeclaration(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			ActorPrologPredicateDeclaration definition= metaTermParser.parseRootPredicateDeclaration(iX);
			Term term= definition.toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
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
	public void popDomainDefinitions1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popDomainDefinitions(result,a1,iX);
	}
	public void popDomainDefinitions1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popDomainDefinitions(null,a1,iX);
	}
	//
	protected void popDomainDefinitions(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			ActorPrologDomainDefinition[] definitions= metaTermParser.parseDomainDefinitions(iX);
			Term term= ActorPrologDomainDefinition.arrayToList(definitions);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popDomainDefinition1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popDomainDefinition(result,a1,iX);
	}
	public void popDomainDefinition1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popDomainDefinition(null,a1,iX);
	}
	//
	protected void popDomainDefinition(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			ActorPrologDomainDefinition[] definitions= metaTermParser.parseDomainDefinition(iX);
			Term term= ActorPrologDomainDefinition.arrayToList(definitions);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
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
	public void popSlotDeclarations1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popSlotDeclarations(result,a1,iX);
	}
	public void popSlotDeclarations1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popSlotDeclarations(null,a1,iX);
	}
	//
	protected void popSlotDeclarations(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			ActorPrologSlotDeclaration[] declarations= metaTermParser.parseRootSlotDeclarations(iX);
			Term term= ActorPrologSlotDeclaration.arrayToList(declarations);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popSlotDefinitions2ff(ChoisePoint iX, PrologVariable result, PrologVariable a1, PrologVariable a2) throws Backtracking {
		popSlotDefinitions(result,a1,a2,iX);
	}
	public void popSlotDefinitions2fs(ChoisePoint iX, PrologVariable a1, PrologVariable a2) throws Backtracking {
		popSlotDefinitions(null,a1,a2,iX);
	}
	//
	protected void popSlotDefinitions(PrologVariable result, PrologVariable a1, PrologVariable a2, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			ActorPrologSlotDefinition[] definitions= metaTermParser.parseRootSlotDefinitions(iX);
			Term term1= ActorPrologSlotDefinition.arrayToList(definitions);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			ActorPrologArgumentDomain[] domains= metaTermParser.getSlotDomains();
			Term term2= ActorPrologArgumentDomain.arrayToList(domains);
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term1,iX);
			a2.setBacktrackableValue(term2,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popInitializer1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popInitializer(result,a1,iX);
	}
	public void popInitializer1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popInitializer(null,a1,iX);
	}
	//
	protected void popInitializer(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootInitializer(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popConstructor1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popConstructor(result,a1,iX);
	}
	public void popConstructor1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popConstructor(null,a1,iX);
	}
	//
	protected void popConstructor(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootConstructor(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
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
	public void popClauses2ff(ChoisePoint iX, PrologVariable result, Term a1, PrologVariable a2) throws Backtracking {
		boolean parseModel= YesNoConverters.argument2YesNo(a1,iX).toBoolean();
		popClauses(result,parseModel,a2,iX);
	}
	public void popClauses2fs(ChoisePoint iX, Term a1, PrologVariable a2) throws Backtracking {
		boolean parseModel= YesNoConverters.argument2YesNo(a1,iX).toBoolean();
		popClauses(null,parseModel,a2,iX);
	}
	public void popClauses1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popClauses(result,false,a1,iX);
	}
	public void popClauses1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popClauses(null,false,a1,iX);
	}
	//
	protected void popClauses(PrologVariable result, boolean parseModel, PrologVariable a2, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			ActorPrologClause[] clauses= metaTermParser.parseRootClauses(parseModel,iX);
			Term term= ActorPrologClause.arrayToList(clauses);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a2.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popClause2ff(ChoisePoint iX, PrologVariable result, Term a1, PrologVariable a2) throws Backtracking {
		boolean parseModel= YesNoConverters.argument2YesNo(a1,iX).toBoolean();
		popClause(result,parseModel,a2,iX);
	}
	public void popClause2fs(ChoisePoint iX, Term a1, PrologVariable a2) throws Backtracking {
		boolean parseModel= YesNoConverters.argument2YesNo(a1,iX).toBoolean();
		popClause(null,parseModel,a2,iX);
	}
	public void popClause1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popClause(result,false,a1,iX);
	}
	public void popClause1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popClause(null,false,a1,iX);
	}
	//
	protected void popClause(PrologVariable result, boolean parseModel, PrologVariable a2, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootClause(parseModel,iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a2.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popSubgoal1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popSubgoal(result,a1,iX);
	}
	public void popSubgoal1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popSubgoal(null,a1,iX);
	}
	//
	protected void popSubgoal(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootSubgoal(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popAtom1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popAtom(result,a1,iX);
	}
	public void popAtom1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popAtom(null,a1,iX);
	}
	//
	protected void popAtom(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootAtom(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popBinaryRelation1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popBinaryRelation(result,a1,iX);
	}
	public void popBinaryRelation1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popBinaryRelation(null,a1,iX);
	}
	//
	protected void popBinaryRelation(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootBinaryRelation(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popFunctionCall1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popFunctionCall(result,a1,iX);
	}
	public void popFunctionCall1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popFunctionCall(null,a1,iX);
	}
	//
	protected void popFunctionCall(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootFunctionCall(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popSimpleAtom1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popSimpleAtom(result,a1,iX);
	}
	public void popSimpleAtom1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popSimpleAtom(null,a1,iX);
	}
	//
	protected void popSimpleAtom(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootSimpleAtom(iX).toActorPrologTerm();
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popMetaExpression1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popMetaExpression(result,a1,iX);
	}
	public void popMetaExpression1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popMetaExpression(null,a1,iX);
	}
	//
	protected void popMetaExpression(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootExpression(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popMetaTerm1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popMetaTerm(result,a1,iX);
	}
	public void popMetaTerm1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popMetaTerm(null,a1,iX);
	}
	//
	protected void popMetaTerm(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateMetaTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= metaTermParser.parseRootTerm(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= metaTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void popGroundTerm1ff(ChoisePoint iX, PrologVariable result, PrologVariable a1) throws Backtracking {
		popGroundTerm(result,a1,iX);
	}
	public void popGroundTerm1fs(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		popGroundTerm(null,a1,iX);
	}
	//
	protected void popGroundTerm(PrologVariable result, PrologVariable a1, ChoisePoint iX) throws Backtracking {
		updateGroundTermParserAttributes(iX);
		int previousPosition= currentPosition;
		try {
			Term term= groundTermParser.parseRootTerm(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= groundTermParser.getPosition();
			a1.setBacktrackableValue(term,iX);
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
		} catch (SyntaxError e) {
			if (actingRaiseRuntimeExceptions.get()) {
				throw new ActorPrologParserError(e);
			};
			throw Backtracking.instance;
		}
	}
	//
	public void checkEndOfText0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		checkEndOfText(result,iX);
	}
	public void checkEndOfText0fs(ChoisePoint iX) throws Backtracking {
		checkEndOfText(null,iX);
	}
	//
	protected void checkEndOfText(PrologVariable result, ChoisePoint iX) throws Backtracking {
		updateGroundTermParserAttributes(false,iX);
		int previousPosition= currentPosition;
		try {
			groundTermParser.checkEndOfText(iX);
			if (actingThereAreTranslationErrors.get()) {
				throw Backtracking.instance;
			};
			currentPosition= groundTermParser.getPosition();
			if (result != null) {
				result.setNonBacktrackableValue(new PrologInteger(previousPosition));
			}
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
		updateMetaTermParserAttributes(true,iX);
	}
	protected void updateMetaTermParserAttributes(boolean doCheckTokenStackSize, ChoisePoint iX) throws Backtracking {
		actingThereAreTranslationErrors.set(false);
		checkWhetherTokenStackIsInitiated();
		updateGeneralAttributes(iX);
		if (doCheckTokenStackSize) {
			checkTokenStackSize(iX);
		};
		boolean currentKeepTextPositions= getKeepTextPositions(iX);
		metaTermParser.setTokens(tokenStack,currentPosition,currentKeepTextPositions);
		metaTermParser.setSlotNameHash(actingSlotNames);
		iX.pushTrail(new ActorPrologBacktrackableParserStackState(this));
	}
	//
	protected void updateGroundTermParserAttributes(ChoisePoint iX) throws Backtracking {
		updateGroundTermParserAttributes(true,iX);
	}
	protected void updateGroundTermParserAttributes(boolean doCheckTokenStackSize, ChoisePoint iX) throws Backtracking {
		actingThereAreTranslationErrors.set(false);
		checkWhetherTokenStackIsInitiated();
		updateGeneralAttributes(iX);
		if (doCheckTokenStackSize) {
			checkTokenStackSize(iX);
		};
		boolean currentKeepTextPositions= getKeepTextPositions(iX);
		groundTermParser.setTokens(tokenStack,currentPosition,currentKeepTextPositions);
		groundTermParser.setSlotNameHash(actingSlotNames);
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
	@Override
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
	@Override
	public ParserError handleUnexpectedEndOfTokenList(PrologToken[] tokens, ChoisePoint iX) throws ParserError {
		ParserError exception= new UnexpectedEndOfTokenList(tokens);
		handleError(exception,iX);
		return exception;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getParsedVariableNames0ff(ChoisePoint iX, PrologVariable result) {
		Term term= metaTermParser.getTermParsedVariableNames();
		result.setNonBacktrackableValue(term);
	}
	public void getParsedVariableNames0fs(ChoisePoint iX) {
	}
	//
	public void forgetParsedVariableNames0s(ChoisePoint iX) {
		metaTermParser.forgetFirstAndSecondParsedVariableNames();
	}
	//
	public void getFunctionCallTable0ff(ChoisePoint iX, PrologVariable result) {
		Term term= metaTermParser.getTermFunctionCallTable();
		result.setNonBacktrackableValue(term);
	}
	public void getFunctionCallTable0fs(ChoisePoint iX) {
	}
	//
	public void clearFunctionCallTable0s(ChoisePoint iX) {
		metaTermParser.clearFunctionCallTable();
	}
	//
	public void getParsedSlotNames0ff(ChoisePoint iX, PrologVariable result) {
		Term term= metaTermParser.getTermParsedSlotNames();
		result.setNonBacktrackableValue(term);
	}
	public void getParsedSlotNames0fs(ChoisePoint iX) {
	}
}
