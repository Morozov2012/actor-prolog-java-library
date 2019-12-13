// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public abstract class PrologToken {
	//
	protected int position;
	//
	protected static String emptyString= "";
	protected static byte[] emptyByteArray= new byte[0];
	protected static String anonymousVariableName= "_";
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologToken(int p) {
		position= p;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getPosition() {
		return position;
	}
	//
	abstract public PrologTokenType getType();
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getIntegerValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		master.handleError(new WrongTokenIsNotAnInteger(position),iX);
		return BigInteger.ZERO;
	}
	public BigInteger getIntegerValueOrBacktrack() throws Backtracking {
		throw Backtracking.instance;
	}
	public BigInteger getIntegerValueOrTermIsNotAnInteger() throws TermIsNotAnInteger {
		throw TermIsNotAnInteger.instance;
	}
	public boolean isExtendedNumber(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError{
		master.handleError(new WrongTokenIsNotANumber(position),iX);
		return false;
	}
	public double getRealValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		master.handleError(new WrongTokenIsNotAReal(position),iX);
		return 0.0;
	}
	public double getRealValueOrBacktrack() throws Backtracking {
		throw Backtracking.instance;
	}
	public double getRealValueOrTermIsNotAReal() throws TermIsNotAReal {
		throw TermIsNotAReal.instance;
	}
	public long getSymbolCode(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		master.handleError(new WrongTokenIsNotASymbol(position),iX);
		return SymbolCodes.symbolCode_E_;
	}
	public boolean isInQuotes(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		master.handleError(new WrongTokenIsNotASymbol(position),iX);
		return false;
	}
	public String getStringValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		master.handleError(new WrongTokenIsNotAString(position),iX);
		return emptyString;
	}
	public byte[] getBinaryValue(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		master.handleError(new WrongTokenIsNotABinary(position),iX);
		return emptyByteArray;
	}
	public String getVariableName(LexicalScannerMasterInterface master, ChoisePoint iX) throws LexicalScannerError {
		master.handleError(new WrongTokenIsNotAVariable(position),iX);
		return anonymousVariableName;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isFinalToken() {
		return getType().isFinalToken();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toActorPrologTerm();
	//
	public Term toTermTP() {
		Term[] arguments= new Term[2];
		arguments[0]= toActorPrologTerm();
		arguments[1]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_t,arguments);
	}
}
