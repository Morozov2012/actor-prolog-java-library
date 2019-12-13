// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.data.importation.*;
import morozov.syntax.data.initializers.*;
import morozov.syntax.data.parameters.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.converters.*;
import morozov.terms.*;

import java.util.ArrayList;
import java.util.Iterator;

public class PackageParser extends ClassParser {
	//
	protected String packageName= null;
	protected int packageNamePosition= -1;
	protected ActorPrologPackageParameter[] formalParameters= emptyParameterArray;
	protected ActorPrologPragma[] pragmaList= emptyPragmaArray;
	protected ActorPrologPackageImportCommand[] importCommands= emptyImportCommandArray;
	protected ActorPrologUnit[] units= emptyUnitArray;
	protected ActorPrologInitializer project;
	protected String[] projectVariableNames;
	//
	protected static ActorPrologPackageParameter[] emptyParameterArray= new ActorPrologPackageParameter[0];
	protected static ActorPrologPragma[] emptyPragmaArray= new ActorPrologPragma[0];
	protected static ActorPrologPackageImportCommand[] emptyImportCommandArray= new ActorPrologPackageImportCommand[0];
	protected static ActorPrologPackageParameter[] importedPackageArguments= new ActorPrologPackageParameter[0];

	protected static ActorPrologUnit[] emptyUnitArray= new ActorPrologUnit[0];
	//
	///////////////////////////////////////////////////////////////
	//
	public PackageParser(ParserMasterInterface m, boolean rememberPositions) {
		super(m,rememberPositions);
	}
	public PackageParser(ParserMasterInterface m, boolean rememberPositions, boolean implementRobustMode) {
		super(m,rememberPositions,implementRobustMode);
	}
	public PackageParser(ParserMasterInterface m) {
		super(m);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String getPackageName() {
		return packageName;
	}
	public Term getTermPackageName() {
		Term termPackageName;
		if (packageName==null) {
			termPackageName= PrologUnknownValue.instance;
		} else {
			termPackageName= new PrologString(packageName);
			if (rememberTextPositions) {
				termPackageName= attachTermPosition(termPackageName,packageNamePosition);
			}
		};
		return termPackageName;
	}
	public int getPackageNamePosition() {
		return packageNamePosition;
	}
	public ActorPrologPackageParameter[] getFormalParameters() {
		return formalParameters;
	}
	public ActorPrologPragma[] getPragmaList() {
		return pragmaList;
	}
	public ActorPrologPackageImportCommand[] getImportCommands() {
		return importCommands;
	}
	public ActorPrologUnit[] getUnits() {
		return units;
	}
	public ActorPrologInitializer getProject() {
		return project;
	}
	public Term getTermProject() {
		if (project==null) {
			return ActorPrologNoInitializer.getTermNoInitializer();
		} else {
			return project.toActorPrologTerm();
		}
	}
	public String[] getProjectVariableNames() {
		return projectVariableNames;
	}
	public Term getTermProjectVariableNames() {
		if (projectVariableNames==null) {
			return BalancedNameTreeNode.getTermNil();
		} else {
			BalancedNameTreeNode tree= GeneralConverters.stringArrayToBalancedNameTree(projectVariableNames);
			return tree.toTerm();
		}
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE PACKAGE **********************************************
//*********************************************************************
//*********************************************************************
	//
	public void parsePackage(ChoisePoint iX) throws SyntaxError {
		resetClassAttributes();
		setUseSecondVariableNameRegister(false);
		forgetParsedVariableNames();
		//
		packageName= null;
		packageNamePosition= -1;
		formalParameters= emptyParameterArray;
		pragmaList= emptyPragmaArray;
		importCommands= emptyImportCommandArray;
		units= emptyUnitArray;
		project= null;
		projectVariableNames= null;
		boolean packageTitleIsEnabled= true;
		boolean importCommandsAreEnabled= true;
		boolean theMainClassIsToBeAssembled= false;
		int firstClausesSectionPosition= -1;
		//
		ArrayList<ActorPrologPragma> pragmaArray= new ArrayList<>();
		ArrayList<ActorPrologPackageImportCommand> importCommandArray= new ArrayList<>();
		ArrayList<ActorPrologDomainDefinition> domainDefinitionArray= new ArrayList<>();
		ArrayList<ActorPrologPredicateDeclaration> predicateDeclarationArray= new ArrayList<>();
		ArrayList<ActorPrologClause> actingClauseArray= new ArrayList<>();
		ArrayList<ActorPrologClause> modelClauseArray= new ArrayList<>();
		ArrayList<ActorPrologUnit> unitArray= new ArrayList<>();
		ParsePackageLoop: while (true) {
			if (position >= numberOfTokens) {
				break ParsePackageLoop;
			};
			PrologToken firstToken= tokens[position];
			int firstTokenPosition= firstToken.getPosition();
			PrologTokenType firstTokenType= firstToken.getType();
			switch (firstTokenType) {
			case KEYWORD:
///////////////////////////////////////////////////////////////////////
long firstTokenCode= firstToken.getSymbolCode(master,iX);
if (firstTokenCode==SymbolCodes.symbolCode_E_package) {
	position++;
	if (packageTitleIsEnabled) {
		parsePackageTitle(iX);
	} else if (!robustMode) {
		master.handleError(new ThePackageKeywordIsNotExpectedHere(firstTokenPosition),iX);
	};
	packageTitleIsEnabled= false;
} else if (firstTokenCode==SymbolCodes.symbolCode_E_import) {
	position++;
	if (importCommandsAreEnabled) {
		ActorPrologPackageImportCommand importCommand= parseImportCommand(firstTokenPosition,iX);
		importCommandArray.add(importCommand);
	} else if (!robustMode) {
		master.handleError(new TheImportKeywordIsNotExpectedHere(firstTokenPosition),iX);
	};
	packageTitleIsEnabled= false;
} else if (firstTokenCode==SymbolCodes.symbolCode_E_pragma) {
	position++;
	parseColon(iX);
	pragmaArray.add(parsePragma(firstTokenPosition,iX));
} else if (firstTokenCode==SymbolCodes.symbolCode_E_DOMAINS) {
	position++;
	parseColon(iX);
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
	parseDomainDefinitions(domainDefinitionArray,iX);
} else if (firstTokenCode==SymbolCodes.symbolCode_E_PREDICATES) {
	position++;
	parseColon(iX);
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
	parsePredicateDeclarations(predicateDeclarationArray,false,iX);
} else if (firstTokenCode==SymbolCodes.symbolCode_E_IMPERATIVES) {
	position++;
	parseColon(iX);
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
	parsePredicateDeclarations(predicateDeclarationArray,true,iX);
} else if (firstTokenCode==SymbolCodes.symbolCode_E_CLAUSES) {
	position++;
	parseColon(iX);
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
	theMainClassIsToBeAssembled= true;
	if (firstClausesSectionPosition < 0) {
		firstClausesSectionPosition= firstTokenPosition;
	};
	setUseSecondVariableNameRegister(false);
	forgetVariableRoles();
	subgoalArray.clear();
	clearFunctionCallTable();
	slotNameHash.clear();
	parseClauses(actingClauseArray,false,iX);
} else if (firstTokenCode==SymbolCodes.symbolCode_E_MODEL) {
	position++;
	parseColon(iX);
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
	theMainClassIsToBeAssembled= true;
	if (firstClausesSectionPosition < 0) {
		firstClausesSectionPosition= firstTokenPosition;
	};
	setUseSecondVariableNameRegister(false);
	forgetVariableRoles();
	subgoalArray.clear();
	clearFunctionCallTable();
	slotNameHash.clear();
	parseClauses(modelClauseArray,true,iX);
} else if (firstTokenCode==SymbolCodes.symbolCode_E_class) {
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
	parseClass(iX);
	ActorPrologClass c= new ActorPrologClass(
		getClassNameCode(),
		getAncestorNameCode(),
		getSlotDefinitions(),
		getActingClauses(),
		getModelClauses(),
		getTermClassSource(),
		getInterface(),
		getParsedVariableNames(),
		firstTokenPosition);
	checkWhetherClassIsNotDefined(unitArray,getClassNameCode(),getInterface(),false,firstTokenPosition,iX);
	unitArray.add(c);
} else if (	firstTokenCode==SymbolCodes.symbolCode_E_interface ||
		firstTokenCode==SymbolCodes.symbolCode_E_metainterface) {
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
	parseInterface(iX);
	ActorPrologInterface i= new ActorPrologInterface(
		getClassNameCode(),
		getAncestorNameCodes(),
		isMetaInterface(),
		getSlotDeclarations(),
		getDomainDefinitions(),
		getPredicateDeclarations(),
		firstTokenPosition);
	checkWhetherInterfaceIsNotDefined(unitArray,getClassNameCode(),false,firstTokenPosition,iX);
	unitArray.add(i);
} else if (firstTokenCode==SymbolCodes.symbolCode_E_CLASSES) {
	position++;
	parseColon(iX);
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
} else if (firstTokenCode==SymbolCodes.symbolCode_E_project) {
	position++;
	parseColon(iX);
	packageTitleIsEnabled= false;
	importCommandsAreEnabled= false;
	setUseSecondVariableNameRegister(true);
	resetClassAttributes();
	project= parseProject(iX);
	projectVariableNames= getParsedVariableNames();
} else {
	master.handleError(new IncorrectKeyword(firstToken),iX);
	position++;
};
continue ParsePackageLoop;
///////////////////////////////////////////////////////////////////////
			default:
				break ParsePackageLoop;
			}
		};
		if (theMainClassIsToBeAssembled) {
///////////////////////////////////////////////////////////////////////
resetClassAttributes();
classNameCode= SymbolCodes.symbolCode_E_Main;
ancestorNameCode= SymbolCodes.symbolCode_E_DemoConsole;
ancestorNameCodes= emptyLongArray;
isMetaInterface= false;
slotDeclarations= emptySlotDeclarationArray;
slotDefinitions= emptySlotDefinitionArray;
slotDomains= emptyArgumentDomainArray;
domainDefinitions= emptyDomainDefinitionArray;
predicateDeclarations= emptyPredicateDeclarationArray;
actingClauses= actingClauseArray.toArray(new ActorPrologClause[actingClauseArray.size()]);
modelClauses= modelClauseArray.toArray(new ActorPrologClause[modelClauseArray.size()]);
setUseSecondVariableNameRegister(false);
ActorPrologClass c= new ActorPrologClass(
	getClassNameCode(),
	getAncestorNameCode(),
	getSlotDefinitions(),
	getActingClauses(),
	getModelClauses(),
	getTermClassSource(),
	getInterface(),
	getParsedVariableNames(),
	firstClausesSectionPosition);
checkWhetherClassIsNotDefined(unitArray,getClassNameCode(),getInterface(),true,firstClausesSectionPosition,iX);
checkWhetherInterfaceIsNotDefined(unitArray,getClassNameCode(),true,firstClausesSectionPosition,iX);
unitArray.add(c);
///////////////////////////////////////////////////////////////////////
		};
		pragmaList= pragmaArray.toArray(new ActorPrologPragma[pragmaArray.size()]);
		importCommands= importCommandArray.toArray(new ActorPrologPackageImportCommand[importCommandArray.size()]);
		domainDefinitions= domainDefinitionArray.toArray(new ActorPrologDomainDefinition[domainDefinitionArray.size()]);
		predicateDeclarations= predicateDeclarationArray.toArray(new ActorPrologPredicateDeclaration[predicateDeclarationArray.size()]);
		units= unitArray.toArray(new ActorPrologUnit[unitArray.size()]);
	}
	//
	protected void parsePackageTitle(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		int namePosition= tokens[position].getPosition();
		packageName= parsePackageName(iX);
		packageNamePosition= namePosition;
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		PrologTokenType firstTokenType= firstToken.getType();
		if (firstTokenType==PrologTokenType.L_ROUND_BRACKET) {
			position++;
			formalParameters= parsePackageParameters(iX);
		};
		parseColon(iX);
	}
	//
	protected String parsePackageName(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		PrologTokenType firstTokenType= firstToken.getType();
		if (firstTokenType==PrologTokenType.STRING_SEGMENT) {
			position++;
			return parseString(firstToken.getStringValue(master,iX),iX);
		} else {
			master.handleError(new PackageNameIsExpected(firstToken.getPosition()),iX);
			position++;
			return null;
		}
	}
	//
	protected void checkWhetherClassIsNotDefined(ArrayList<ActorPrologUnit> unitArray, long classNameCode1, ActorPrologInterface classInterface1, boolean isTheMainClass, int p1, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		Iterator<ActorPrologUnit> unitArrayIterator= unitArray.iterator();
		while (unitArrayIterator.hasNext()) {
			ActorPrologUnit unit2= unitArrayIterator.next();
			long unitNameCode2= unit2.getClassNameCode();
			if (classNameCode1==unitNameCode2) {
				if (unit2 instanceof ActorPrologClass) {
					if (isTheMainClass) {
						master.handleError(new TheMainClassIsDefinedExplicitlyInTheProgram(classNameCode1,p1),iX);
					} else {
						master.handleError(new ThisClassIsAlreadyDefined(classNameCode1,p1),iX);
					};
					break;
				} else if (classInterface1 != null) {
					if (isTheMainClass) {
						master.handleError(new TheMainInterfaceIsDefinedExplicitlyInTheProgram(classNameCode1,p1),iX);
					} else {
						master.handleError(new TheInterfaceOfThisClassIsAlreadyDefined(classNameCode1,p1),iX);
					};
					break;
				}
			}
		}
	}
	//
	protected void checkWhetherInterfaceIsNotDefined(ArrayList<ActorPrologUnit> unitArray, long classNameCode1, boolean isTheMainClass, int p1, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		Iterator<ActorPrologUnit> unitArrayIterator= unitArray.iterator();
		while (unitArrayIterator.hasNext()) {
			ActorPrologUnit unit2= unitArrayIterator.next();
			long unitNameCode2= unit2.getClassNameCode();
			if (classNameCode1==unitNameCode2) {
				if (unit2 instanceof ActorPrologClass) {
					if (isTheMainClass) {
						master.handleError(new TheMainClassIsDefinedExplicitlyInTheProgram(classNameCode1,p1),iX);
					} else {
						master.handleError(new ThisClassIsAlreadyDefined(classNameCode1,p1),iX);
					}
				} else {
					if (isTheMainClass) {
						master.handleError(new TheMainInterfaceIsDefinedExplicitlyInTheProgram(classNameCode1,p1),iX);
					} else {
						master.handleError(new ThisInterfaceIsAlreadyDefined(classNameCode1,p1),iX);
					}
				};
				break;
			}
		}
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE PROJECT **********************************************
//*********************************************************************
//*********************************************************************
	//
	protected ActorPrologInitializer parseProject(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		forgetParsedVariableNames();
		subgoalArray.clear();
		clearFunctionCallTable();
		slotNameHash.clear();
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		int beginningOfTerm= tokens[position].getPosition();
		parseLeftRoundBracket(iX);
		ActorPrologInitializer constructor;
		initializerIsParsed= true;
		try {
			constructor= parseConstructorOrResident(ConstructorParsingMode.PROJECT_CONSTRUCTOR,beginningOfTerm,iX);
		} finally {
			initializerIsParsed= false;
		};
		checkFunctionVariables(ProhibitedFunctionCallContext.INITIALIZER,iX);
		return constructor;
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE IMPORT COMMAND ***************************************
//*********************************************************************
//*********************************************************************
	//
	protected ActorPrologPackageImportCommand parseImportCommand(int commandPosition, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		ScanImportCommand: switch (firstTokenType) {
		case RANGE:
			{
			position++;
			parseTheFromKeyword(iX);
			String importedPackageName= parsePackageName(iX);
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			ActorPrologPackageParameter[] currentImportedPackageArguments= emptyParameterArray;
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.L_ROUND_BRACKET) {
				position++;
				currentImportedPackageArguments= parsePackageParameters(iX);
			};
			parseSemicolon(iX);
			return new ActorPrologPackageImportAllClassesCommand(
				importedPackageName,
				currentImportedPackageArguments,
				commandPosition);
			}
		case STRING_SEGMENT:
			{
			position++;
			String importedPackageName= parseString(firstToken.getStringValue(master,iX),iX);
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			ActorPrologPackageParameter[] currentImportedPackageArguments= emptyParameterArray;
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.L_ROUND_BRACKET) {
				position++;
				currentImportedPackageArguments= parsePackageParameters(iX);
			};
			parseSemicolon(iX);
			return new ActorPrologPackageImportDomainsCommand(
				importedPackageName,
				currentImportedPackageArguments,
				commandPosition);
			}
		default:
			{
			ActorPrologClassImportCommand[] classRenamingPairs= parseClassRenamingPairs(iX);
			parseTheFromKeyword(iX);
			String importedPackageName= parsePackageName(iX);
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			ActorPrologPackageParameter[] currentImportedPackageArguments= emptyParameterArray;
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.L_ROUND_BRACKET) {
				position++;
				currentImportedPackageArguments= parsePackageParameters(iX);
			};
			parseSemicolon(iX);
			return new ActorPrologPackageImportGivenClassesCommand(
				classRenamingPairs,
				importedPackageName,
				currentImportedPackageArguments,
				commandPosition);
			}
		}
	}
	//
	protected ActorPrologClassImportCommand[] parseClassRenamingPairs(ChoisePoint iX) throws SyntaxError {
		ArrayList<ActorPrologClassImportCommand> classRenamingPairArray= new ArrayList<>();
		ClassRenamingPairsLoop: while (true) {
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken firstToken= tokens[position];
			int firstTokenPosition= firstToken.getPosition();
			PrologTokenType firstTokenType= firstToken.getType();
			ScanRenamingPair: switch (firstTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
position++;
long classNameCode1= firstToken.getSymbolCode(master,iX);
checkWhetherClassNameIsEnclosedInApostrophes(firstToken,iX);
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case KEYWORD:
	{
	long secondTokenCode= secondToken.getSymbolCode(master,iX);
	if (secondTokenCode==SymbolCodes.symbolCode_E_as) {
		position++;
	} else if (secondTokenCode==SymbolCodes.symbolCode_E_from) {
		ActorPrologClassImportCommand pair= new ActorPrologClassImportCommand(classNameCode1,classNameCode1,firstTokenPosition);
		classRenamingPairArray.add(pair);
		break ClassRenamingPairsLoop;
	} else {
		master.handleError(new TheFromKeywordOrTheAsKeywordIsExpected(secondToken.getPosition()),iX);
		position++;
		break ClassRenamingPairsLoop;
	};
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	if (thirdTokenType==PrologTokenType.SYMBOL) {
		position++;
		long classNameCode2= thirdToken.getSymbolCode(master,iX);
		checkWhetherClassNameIsEnclosedInApostrophes(thirdToken,iX);
		ActorPrologClassImportCommand pair= new ActorPrologClassImportCommand(classNameCode1,classNameCode2,firstTokenPosition);
		classRenamingPairArray.add(pair);
	} else {
		master.handleError(new ClassNameIsExpected(thirdToken.getPosition()),iX);
		ActorPrologClassImportCommand pair= new ActorPrologClassImportCommand(classNameCode1,classNameCode1,firstTokenPosition);
		classRenamingPairArray.add(pair);
		position++;
	};
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	switch (fourthTokenType) {
	case KEYWORD:
		long fourthTokenCode= fourthToken.getSymbolCode(master,iX);
		if (fourthTokenCode==SymbolCodes.symbolCode_E_from) {
			break ClassRenamingPairsLoop;
		} else {
			master.handleError(new TheFromKeywordIsExpected(fourthToken.getPosition()),iX);
			position++;
			break ClassRenamingPairsLoop;
		}
	case COMMA:
		position++;
		continue ClassRenamingPairsLoop;
	default:
		master.handleError(new CommaIsExpected(fourthToken.getPosition()),iX);
		position++;
		continue ClassRenamingPairsLoop;
	}
	}
case COMMA:
	{
	position++;
	ActorPrologClassImportCommand pair= new ActorPrologClassImportCommand(classNameCode1,classNameCode1,firstTokenPosition);
	classRenamingPairArray.add(pair);
	continue ClassRenamingPairsLoop;
	}
default:
	{
	master.handleError(new TheFromKeywordOrTheAsKeywordIsExpected(secondToken.getPosition()),iX);
	position++;
	ActorPrologClassImportCommand pair= new ActorPrologClassImportCommand(classNameCode1,classNameCode1,firstTokenPosition);
	classRenamingPairArray.add(pair);
	continue ClassRenamingPairsLoop;
	}
}
///////////////////////////////////////////////////////////////////////
				}
			default:
				master.handleError(new ClassNameIsExpected(firstTokenPosition),iX);
				position++;
			}
		};
		return classRenamingPairArray.toArray(new ActorPrologClassImportCommand[classRenamingPairArray.size()]);
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE PRAGMA ***********************************************
//*********************************************************************
//*********************************************************************
	//
	protected ActorPrologPragma parsePragma(int pragmaPosition, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		String pragmaName;
		ScanPragma: switch (firstTokenType) {
		case VARIABLE:
			position++;
			pragmaName= firstToken.getVariableName(master,iX);
			pragmaName= pragmaName.toUpperCase();
			break ScanPragma;
		case SYMBOL:
			position++;
			if (firstToken.isInQuotes(master,iX) && !robustMode) {
				master.handleError(new SymbolEnclosedInApostrophesIsNotExpectedHere(firstTokenPosition),iX);
			};
			long nameCode= firstToken.getSymbolCode(master,iX);
			SymbolName name= SymbolNames.retrieveSymbolName(nameCode);
			pragmaName= name.toRawString(null);
			break ScanPragma;
		default:
			master.handleError(new PragmaNameIsExpected(firstTokenPosition),iX);
			position++;
			pragmaName= "";
		};
		parseEquation(iX);
		Term attribute= parsePragmaAttributes(iX);
		parseSemicolon(iX);
		return new ActorPrologPragma(pragmaName,attribute,pragmaPosition);
	}
	//
	protected Term parsePragmaAttributes(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		ScanPragmaAttribute: switch (firstTokenType) {
		case L_SQUARE_BRACKET:
///////////////////////////////////////////////////////////////////////
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
if (secondTokenType==PrologTokenType.R_SQUARE_BRACKET) {
	position++;
	Term result= metaTermEmptyList;
	if (rememberTextPositions) {
		result= attachTermPosition(result,secondToken.getPosition());
	};
	return result;
};
ArrayList<Term> internalPragmaArray= new ArrayList<>();
ParseInternalPragmaList: while (true) {
	Term internalPragmaValue= parseSinglePragmaAttribute(iX);
	internalPragmaArray.add(internalPragmaValue);
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case COMMA:
		position++;
		continue ParseInternalPragmaList;
	case R_SQUARE_BRACKET:
		position++;
		break ParseInternalPragmaList;
	default:
		master.handleError(new CommaOrRightRoundBracketIsExpected(thirdToken.getPosition()),iX);
		position++;
		break ParseInternalPragmaList;
	}
};
int size= internalPragmaArray.size();
Term list= metaTermEmptyList;
for (int k=size-1; k > 0; k--) {
	Term[] termArray= new Term[2];
	termArray[0]= internalPragmaArray.get(k);
	termArray[1]= list;
	list= new PrologStructure(SymbolCodes.symbolCode_E_e,termArray);
};
if (rememberTextPositions) {
	list= attachTermPosition(list,firstTokenPosition);
};
return list;
///////////////////////////////////////////////////////////////////////
		default:
			return parseSinglePragmaAttribute(iX);
		}
	}
	//
	protected Term parseSinglePragmaAttribute(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		Term value;
		ScanPragmaAttribute: switch (firstTokenType) {
		case VARIABLE:
			position++;
			String variableName= firstToken.getVariableName(master,iX);
			value= new PrologString(variableName.toUpperCase());
			if (rememberTextPositions) {
				value= attachTermPosition(value,firstTokenPosition);
			};
			break ScanPragmaAttribute;
		case SYMBOL:
			position++;
			long nameCode= firstToken.getSymbolCode(master,iX);
			SymbolName symbolName= SymbolNames.retrieveSymbolName(nameCode);
			value= new PrologString(symbolName.toRawString(null).toUpperCase());
			if (rememberTextPositions) {
				value= attachTermPosition(value,firstTokenPosition);
			};
			break ScanPragmaAttribute;
		case STRING_SEGMENT:
			position++;
			value= new PrologString(parseString(firstToken.getStringValue(master,iX),iX).toUpperCase());
			if (rememberTextPositions) {
				value= attachTermPosition(value,firstTokenPosition);
			};
			break ScanPragmaAttribute;
		case BINARY_SEGMENT:
			position++;
			value= new PrologBinary(parseBinary(firstToken.getBinaryValue(master,iX),iX));
			if (rememberTextPositions) {
				value= attachTermPosition(value,firstTokenPosition);
			};
			break ScanPragmaAttribute;
		case INTEGER:
			position++;
			value= parseInteger(firstToken,firstTokenPosition,iX);
			break ScanPragmaAttribute;
		case REAL:
			position++;
			value= parseReal(firstToken,firstTokenPosition,iX);
			break ScanPragmaAttribute;
		case MINUS:
			position++;
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken secondToken= tokens[position];
			int secondTokenPosition= secondToken.getPosition();
			PrologTokenType secondTokenType= secondToken.getType();
			switch (secondTokenType) {
			case INTEGER:
				position++;
				value= parseInteger(true,secondToken,firstTokenPosition,iX);
				break ScanPragmaAttribute;
			case REAL:
				position++;
				value= parseReal(true,secondToken,firstTokenPosition,iX);
				break ScanPragmaAttribute;
			default:
				master.handleError(new AnIntegerOrARealIsExpected(secondToken.getPosition()),iX);
				position++;
				value= new PrologString("");
				if (rememberTextPositions) {
					value= attachTermPosition(value,firstTokenPosition);
				};
				break ScanPragmaAttribute;
			}
		default:
			master.handleError(new PragmaValueIsExpected(firstTokenPosition),iX);
			position++;
			value= new PrologString("");
			if (rememberTextPositions) {
				value= attachTermPosition(value,firstTokenPosition);
			};
			break ScanPragmaAttribute;
		};
		return value;
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE PACKAGE PARAMETERS ***********************************
//*********************************************************************
//*********************************************************************
	//
	protected ActorPrologPackageParameter[] parsePackageParameters(ChoisePoint iX) throws SyntaxError {
		ArrayList<ActorPrologPackageParameter> parameterArray= new ArrayList<>();
		boolean thisIsTheFirstLoop= true;
		PackageParametersLoop: while (true) {
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken firstToken= tokens[position];
			int firstTokenPosition= firstToken.getPosition();
			PrologTokenType firstTokenType= firstToken.getType();
			ScanParameter: switch (firstTokenType) {
			case VARIABLE:
				{
///////////////////////////////////////////////////////////////////////
position++;
thisIsTheFirstLoop= false;
String domainName1= firstToken.getVariableName(master,iX);
if (domainName1.equals(anonymousVariableName)) {
	master.handleError(new AnonymousDomainCannotBeRedefined(firstTokenPosition),iX);
};
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
ScanAssignment: switch (secondTokenType) {
case ASSIGNMENT:
	{
	position++;
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	if (thirdTokenType==PrologTokenType.VARIABLE) {
		position++;
		String domainName2= thirdToken.getVariableName(master,iX);
		ActorPrologPackageDomainParameter parameter= new ActorPrologPackageDomainParameter(domainName1,domainName2,firstTokenPosition);
		parameterArray.add(parameter);
	} else {
		master.handleError(new DomainNameIsExpected(thirdToken.getPosition()),iX);
		position++;
		ActorPrologPackageDomainParameter parameter= new ActorPrologPackageDomainParameter(domainName1,firstTokenPosition);
		parameterArray.add(parameter);
	};
	break ScanAssignment;
	}
case COMMA:
	{
	position++;
	ActorPrologPackageDomainParameter parameter= new ActorPrologPackageDomainParameter(domainName1,firstTokenPosition);
	parameterArray.add(parameter);
	continue PackageParametersLoop;
	}
case R_ROUND_BRACKET:
	{
	position++;
	ActorPrologPackageDomainParameter parameter= new ActorPrologPackageDomainParameter(domainName1,firstTokenPosition);
	parameterArray.add(parameter);
	break PackageParametersLoop;
	}
default:
	{
	master.handleError(new AssignmentIsExpected(secondToken.getPosition()),iX);
	position++;
	ActorPrologPackageDomainParameter parameter= new ActorPrologPackageDomainParameter(domainName1,firstTokenPosition);
	parameterArray.add(parameter);
	continue PackageParametersLoop;
	}
}
///////////////////////////////////////////////////////////////////////
				};
				break ScanParameter;
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
position++;
thisIsTheFirstLoop= false;
long classNameCode1= firstToken.getSymbolCode(master,iX);
checkWhetherClassNameIsEnclosedInApostrophes(firstToken,iX);
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
ScanAssignment: switch (secondTokenType) {
case ASSIGNMENT:
	{
	position++;
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	if (thirdTokenType==PrologTokenType.SYMBOL) {
		position++;
		long classNameCode2= thirdToken.getSymbolCode(master,iX);
		checkWhetherClassNameIsEnclosedInApostrophes(thirdToken,iX);
		ActorPrologPackageClassParameter parameter= new ActorPrologPackageClassParameter(classNameCode1,classNameCode2,firstTokenPosition);
		parameterArray.add(parameter);
	} else {
		master.handleError(new ClassNameIsExpected(thirdToken.getPosition()),iX);
		position++;
		ActorPrologPackageClassParameter parameter= new ActorPrologPackageClassParameter(classNameCode1,firstTokenPosition);
		parameterArray.add(parameter);
	};
	break ScanAssignment;
	}
case COMMA:
	{
	position++;
	ActorPrologPackageClassParameter parameter= new ActorPrologPackageClassParameter(classNameCode1,firstTokenPosition);
	parameterArray.add(parameter);
	continue PackageParametersLoop;
	}
case R_ROUND_BRACKET:
	{
	position++;
	ActorPrologPackageClassParameter parameter= new ActorPrologPackageClassParameter(classNameCode1,firstTokenPosition);
	parameterArray.add(parameter);
	break PackageParametersLoop;
	}
default:
	{
	master.handleError(new AssignmentIsExpected(secondToken.getPosition()),iX);
	position++;
	ActorPrologPackageClassParameter parameter= new ActorPrologPackageClassParameter(classNameCode1,firstTokenPosition);
	parameterArray.add(parameter);
	continue PackageParametersLoop;
	}
}
///////////////////////////////////////////////////////////////////////
				};
				break ScanParameter;
			case R_ROUND_BRACKET:
				position++;
				if (thisIsTheFirstLoop) {
					break PackageParametersLoop;
				} else {
					master.handleError(new ClassNameOrDomainNameIsExpected(firstTokenPosition),iX);
					position++;
					thisIsTheFirstLoop= false;
					break ScanParameter;
				}
			default:
				master.handleError(new ClassNameOrDomainNameIsExpected(firstTokenPosition),iX);
				position++;
				thisIsTheFirstLoop= false;
				break ScanParameter;
			};
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken secondToken= tokens[position];
			int secondTokenPosition= secondToken.getPosition();
			PrologTokenType secondTokenType= secondToken.getType();
			ScanParameter: switch (secondTokenType) {
			case COMMA:
				position++;
				continue PackageParametersLoop;
			case R_ROUND_BRACKET:
				position++;
				break PackageParametersLoop;
			default:
				master.handleError(new CommaOrRightRoundBracketIsExpected(secondToken.getPosition()),iX);
				position++;
			}
		};
		return parameterArray.toArray(new ActorPrologPackageParameter[parameterArray.size()]);
	}
}
