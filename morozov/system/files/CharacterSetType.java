// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.files;

import target.*;

import morozov.run.*;
import morozov.system.files.errors.*;
import morozov.terms.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public enum CharacterSetType {
	NAMED_CHARSET {
		public Charset toCharSet() {
			throw new IsNotAStandardCharacterSet();
		}
		public Term toTerm(String name) {
			return new PrologString(name);
		}
		public String toString(String name) {
			return "\"" + name + "\"";
		}
		public long toLong() {
			throw new IsNotAStandardCharacterSet();
		}
	},
	NONE {
		public Charset toCharSet() {
			throw new NoCharacterSetIsDefined();
		}
		public Term toTerm(String name) {
			return termNone;
		}
		public long toLong() {
			return longNone;
		}
	},
	DEFAULT {
		public Charset toCharSet() {
			return Charset.defaultCharset();
		}
		public Term toTerm(String name) {
			return termDefault;
		}
		public long toLong() {
			return longDefault;
		}
	},
	ISO_8859_1 {
		public Charset toCharSet() {
			return StandardCharsets.ISO_8859_1;
		}
		public Term toTerm(String name) {
			return term_ISO_8859_1;
		}
		public long toLong() {
			return long_ISO_8859_1;
		}
	},
	US_ASCII {
		public Charset toCharSet() {
			return StandardCharsets.US_ASCII;
		}
		public Term toTerm(String name) {
			return term_US_ASCII;
		}
		public long toLong() {
			return long_US_ASCII;
		}
	},
	UTF_16 {
		public Charset toCharSet() {
			return StandardCharsets.UTF_16;
		}
		public Term toTerm(String name) {
			return term_UTF_16;
		}
		public long toLong() {
			return long_UTF_16;
		}
	},
	UTF_16BE {
		public Charset toCharSet() {
			return StandardCharsets.UTF_16BE;
		}
		public Term toTerm(String name) {
			return term_UTF_16BE;
		}
		public long toLong() {
			return long_UTF_16BE;
		}
	},
	UTF_16LE {
		public Charset toCharSet() {
			return StandardCharsets.UTF_16LE;
		}
		public Term toTerm(String name) {
			return term_UTF_16LE;
		}
		public long toLong() {
			return long_UTF_16LE;
		}
	},
	UTF_8 {
		public Charset toCharSet() {
			return StandardCharsets.UTF_8;
		}
		public Term toTerm(String name) {
			return term_UTF_8;
		}
		public long toLong() {
			return long_UTF_8;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Charset toCharSet();
	abstract public Term toTerm(String name);
	abstract public long toLong();
	//
	public String toString(String name) {
		SymbolName symbolName= SymbolNames.retrieveSymbolName(toLong());
		return "'" + symbolName.toRawString(null) + "'";
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static long longNone= SymbolCodes.symbolCode_E_none;
	protected static long longDefault= SymbolCodes.symbolCode_E_default;
	protected static long long_ISO_8859_1= SymbolCodes.symbolCode_E_ISO_8859_1;
	protected static long long_US_ASCII= SymbolCodes.symbolCode_E_US_ASCII;
	protected static long long_UTF_16= SymbolCodes.symbolCode_E_UTF_16;
	protected static long long_UTF_16BE= SymbolCodes.symbolCode_E_UTF_16BE;
	protected static long long_UTF_16LE= SymbolCodes.symbolCode_E_UTF_16LE;
	protected static long long_UTF_8= SymbolCodes.symbolCode_E_UTF_8;
	//
	protected static Term termNone= new PrologSymbol(longNone);
	protected static Term termDefault= new PrologSymbol(longDefault);
	protected static Term term_ISO_8859_1= new PrologSymbol(long_ISO_8859_1);
	protected static Term term_US_ASCII= new PrologSymbol(long_US_ASCII);
	protected static Term term_UTF_16= new PrologSymbol(long_UTF_16);
	protected static Term term_UTF_16BE= new PrologSymbol(long_UTF_16BE);
	protected static Term term_UTF_16LE= new PrologSymbol(long_UTF_16LE);
	protected static Term term_UTF_8= new PrologSymbol(long_UTF_8);
}
