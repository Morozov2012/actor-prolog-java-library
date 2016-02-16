// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.files;

import target.*;

import morozov.run.*;
import morozov.system.files.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.Charset;

public class CharacterSet {
	//
	private CharacterSetType type;
	private String name;
	//
	public static CharacterSet NONE= new CharacterSet(CharacterSetType.NONE);
	public static CharacterSet DEFAULT= new CharacterSet(CharacterSetType.DEFAULT);
	//
	///////////////////////////////////////////////////////////////
	//
	public CharacterSet(String s) {
		name= s;
		type= CharacterSetType.NAMED_CHARSET;
	}
	public CharacterSet(CharacterSetType t) {
		type= t;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Charset toCharSet() {
		if (type==CharacterSetType.NAMED_CHARSET) {
			return Charset.forName(name);
		} else {
			return type.toCharSet();
		}
	}
	public boolean isDummy() {
		return type==CharacterSetType.NONE;
	}
	public boolean isDefault() {
		return type==CharacterSetType.DEFAULT;
	}
	public boolean isDummyOrDefault() {
		return type==CharacterSetType.NONE || type==CharacterSetType.DEFAULT;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static CharacterSet term2CharacterSet(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_none) {
				return CharacterSet.NONE;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return CharacterSet.DEFAULT;
			} else if (code==SymbolCodes.symbolCode_E_ISO_8859_1) {
				return new CharacterSet(CharacterSetType.ISO_8859_1);
			} else if (code==SymbolCodes.symbolCode_E_US_ASCII) {
				return new CharacterSet(CharacterSetType.US_ASCII);
			} else if (code==SymbolCodes.symbolCode_E_UTF_16) {
				return new CharacterSet(CharacterSetType.UTF_16);
			} else if (code==SymbolCodes.symbolCode_E_UTF_16BE) {
				return new CharacterSet(CharacterSetType.UTF_16BE);
			} else if (code==SymbolCodes.symbolCode_E_UTF_16LE) {
				return new CharacterSet(CharacterSetType.UTF_16LE);
			} else if (code==SymbolCodes.symbolCode_E_UTF_8) {
				return new CharacterSet(CharacterSetType.UTF_8);
			} else {
				throw new WrongArgumentIsNotCharacterSet(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new CharacterSet(value.getStringValue(iX));
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotCharacterSet(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		return type.toTerm(name);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString() {
		return type.toString(name);
	}
}
