// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class TokenReal10 extends PrologToken {
	//
	protected double value;
	protected boolean isExtendedNumber;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenReal10(double n, boolean isExtended, int position) {
		super(position);
		value= n;
		isExtendedNumber= isExtended;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public PrologTokenType getType() {
		return PrologTokenType.REAL;
	}
	@Override
	public double getRealValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	@Override
	public double getRealValueOrBacktrack() throws Backtracking {
		return value;
	}
	@Override
	public double getRealValueOrTermIsNotAReal() throws TermIsNotAReal {
		return value;
	}
	@Override
	public boolean isExtendedNumber(LexicalScannerMasterInterface master, ChoisePoint iX) {
		return isExtendedNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor != SymbolCodes.symbolCode_E_real_10) {
				return false;
			};
			Term[] list= argument.getStructureArguments(iX);
			if (list.length != 1) {
				return false;
			};
			argument= list[0];
			double v= argument.getRealValue(iX);
			return Arithmetic.realsAreEqual(value,v);
		} catch (TermIsNotAStructure e) {
			return false;
		} catch (TermIsNotAReal e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologReal(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_real_10,arguments);
	}
	@Override
	public String toString() {
		return FormatOutput.realToString(value);
	}
}
