// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.run.*;
import morozov.syntax.data.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.terms.*;

public class VariableRole {
	//
	protected int number;
	protected int firstPosition;
	protected int secondPosition;
	protected boolean isUsedManyTimes= false;
	protected PrologSymbol termVariableRole;
	//
	protected boolean mustBePlain= false;
	protected boolean mustBeRest= false;
	protected boolean mustBePrimaryFunction= false;
	protected boolean mustBeOptimizedPrimaryFunction= false;
	protected boolean mustBeSecondaryFunction= false;
	protected boolean mustBeOptimizedSecondaryFunction= false;
	protected boolean mustBeMetaFunctor= false;
	protected boolean mustBeMetaPredicate= false;
	//
	protected static PrologSymbol termPlain= new PrologSymbol(SymbolCodes.symbolCode_E_plain);
	protected static PrologSymbol termRest= new PrologSymbol(SymbolCodes.symbolCode_E_rest);
	protected static PrologSymbol termPrimaryFunction= new PrologSymbol(SymbolCodes.symbolCode_E_primary_function);
	protected static PrologSymbol termOptimizedPrimaryFunction= new PrologSymbol(SymbolCodes.symbolCode_E_optimized_primary_function);
	protected static PrologSymbol termSecondaryFunction= new PrologSymbol(SymbolCodes.symbolCode_E_secondary_function);
	protected static PrologSymbol termOptimizedSecondaryFunction= new PrologSymbol(SymbolCodes.symbolCode_E_optimized_secondary_function);
	protected static PrologSymbol termMetaFunctor= new PrologSymbol(SymbolCodes.symbolCode_E_meta_functor);
	protected static PrologSymbol termMetaPredicate= new PrologSymbol(SymbolCodes.symbolCode_E_meta_predicate);
	//
	public VariableRole(int n, int p) {
		number= n;
		firstPosition= p;
		termVariableRole= termPlain;
	}
	//
	public int getNumber() {
		return number;
	}
	public int getFirstPosition() {
		return firstPosition;
	}
	public int getSecondPosition() {
		return secondPosition;
	}
	public boolean isUsedManyTimes() {
		return isUsedManyTimes;
	}
	public PrologSymbol toTerm() {
		return termVariableRole;
	}
	//
	public static PrologSymbol getTermPlain() {
		return termPlain;
	}
	public static PrologSymbol getTermRest() {
		return termRest;
	}
	public static PrologSymbol getTermPrimaryFunction() {
		return termPrimaryFunction;
	}
	public static PrologSymbol getTermOptimizedPrimaryFunction() {
		return termOptimizedPrimaryFunction;
	}
	public static PrologSymbol getTermSecondaryFunction() {
		return termSecondaryFunction;
	}
	public static PrologSymbol getTermOptimizedSecondaryFunction() {
		return termOptimizedSecondaryFunction;
	}
	public static PrologSymbol getTermMetaFunctor() {
		return termMetaFunctor;
	}
	public static PrologSymbol getTermMetaPredicate() {
		return termMetaPredicate;
	}
	//
	public boolean mustBePlain() {
		return mustBePlain;
	}
	public boolean mustBeRest() {
		return mustBeRest;
	}
	public boolean mustBeFunctionVariable() {
		return	mustBePrimaryFunction ||
			mustBeOptimizedPrimaryFunction ||
			mustBeSecondaryFunction ||
			mustBeOptimizedSecondaryFunction;
	}
	public boolean mustBePrimaryFunction() {
		return mustBePrimaryFunction;
	}
	public boolean mustBeOptimizedPrimaryFunction() {
		return mustBeOptimizedPrimaryFunction;
	}
	public boolean mustBeSecondaryFunction() {
		return mustBeSecondaryFunction;
	}
	public boolean mustBeOptimizedSecondaryFunction() {
		return mustBeOptimizedSecondaryFunction;
	}
	public boolean mustBeMetaFunctor() {
		return mustBeMetaFunctor;
	}
	public boolean mustBeMetaPredicate() {
		return mustBeMetaPredicate;
	}
	//
	public void checkPosition(int p) {
		if (firstPosition != p) {
			isUsedManyTimes= true;
			secondPosition= p;
		}
	}
	//
	public void declarePlainVariable(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (!mustBePlain) {
			checkCurrentRole(position,master,iX);
			mustBePlain= true;
			termVariableRole= termPlain;
		}
	}
	//
	public void declareRestVariable(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (!mustBeRest) {
			checkCurrentRole(position,master,iX);
			mustBeRest= true;
			termVariableRole= termRest;
		}
	}
	//
	public void declarePrimaryFunctionVariable(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (!mustBePrimaryFunction) {
			checkCurrentRole(position,master,iX);
			mustBePrimaryFunction= true;
			termVariableRole= termPrimaryFunction;
		}
	}
	//
	public void declareOptimizedPrimaryFunctionVariable(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (!mustBeOptimizedPrimaryFunction) {
			checkCurrentRole(position,master,iX);
			mustBeOptimizedPrimaryFunction= true;
			termVariableRole= termOptimizedPrimaryFunction;
		}
	}
	//
	public void declareSecondaryFunctionVariable(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (mustBePrimaryFunction) {
			mustBePrimaryFunction= false;
		} else if (!mustBeSecondaryFunction) {
			checkCurrentRole(position,master,iX);
		};
		mustBeSecondaryFunction= true;
		termVariableRole= termSecondaryFunction;
	}
	//
	public void declareOptimizedSecondaryFunctionVariable(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (mustBeOptimizedPrimaryFunction) {
			mustBeOptimizedPrimaryFunction= false;
		} else if (!mustBeOptimizedSecondaryFunction) {
			checkCurrentRole(position,master,iX);
		};
		mustBeOptimizedSecondaryFunction= true;
		termVariableRole= termOptimizedSecondaryFunction;
	}
	//
	public void declareMetaFunctorVariable(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (!mustBeMetaFunctor) {
			checkCurrentRole(position,master,iX);
			mustBeMetaFunctor= true;
			termVariableRole= termMetaFunctor;
		}
	}
	//
	public void declareMetaPredicateVariable(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (!mustBeMetaPredicate) {
			checkCurrentRole(position,master,iX);
			mustBeMetaPredicate= true;
			termVariableRole= termMetaPredicate;
		}
	}
	//
	protected void checkCurrentRole(int position, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (mustBePlain) {
			master.handleError(new PlainVariableIsNotAllowedHere(position),iX);
		} else if (mustBeRest) {
			master.handleError(new RestVariableIsNotAllowedHere(position),iX);
		} else if (mustBePrimaryFunction) {
			master.handleError(new PrimaryFunctionVariableIsNotAllowedHere(position),iX);
		} else if (mustBeOptimizedPrimaryFunction) {
			master.handleError(new OptimizedPrimaryFunctionVariableIsNotAllowedHere(position),iX);
		} else if (mustBeSecondaryFunction) {
			master.handleError(new SecondaryFunctionVariableIsNotAllowedHere(position),iX);
		} else if (mustBeOptimizedSecondaryFunction) {
			master.handleError(new OptimizedSecondaryFunctionVariableIsNotAllowedHere(position),iX);
		} else if (mustBeMetaFunctor) {
			master.handleError(new MetaFunctorVariableIsNotAllowedHere(position),iX);
		} else if (mustBeMetaPredicate) {
			master.handleError(new MetaPredicateVariableIsNotAllowedHere(position),iX);
		}
	}
}
