// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class TokenInteger10 extends PrologToken {
	//
	protected BigInteger value;
	protected boolean isExtendedNumber;
	//
	///////////////////////////////////////////////////////////////
	//
	public TokenInteger10(BigInteger n, boolean isExtended, int position) {
		super(position);
		value= n;
		isExtendedNumber= isExtended;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public PrologTokenType getType() {
		return PrologTokenType.INTEGER;
	}
	@Override
	public BigInteger getIntegerValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		return value;
	}
	@Override
	public BigInteger getIntegerValueOrBacktrack() throws Backtracking {
		return value;
	}
	@Override
	public BigInteger getIntegerValueOrTermIsNotAnInteger() throws TermIsNotAnInteger {
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
			if (functor != SymbolCodes.symbolCode_E_integer_10) {
				return false;
			};
			Term[] list= argument.getStructureArguments(iX);
			if (list.length != 1) {
				return false;
			};
			argument= list[0];
			BigInteger v= argument.getIntegerValue(iX);
			return value.equals(v);
		} catch (TermIsNotAStructure e) {
			return false;
		} catch (TermIsNotAnInteger e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= new Term[]{new PrologInteger(value)};
		return new PrologStructure(SymbolCodes.symbolCode_E_integer_10,arguments);
	}
	@Override
	public String toString() {
		return FormatOutput.integerToString(value);
	}
}
