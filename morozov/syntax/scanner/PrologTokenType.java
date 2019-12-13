// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum PrologTokenType {
	//
	END_OF_TEXT {
		@Override
		public boolean isFinalToken() {
			return true;
		}
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_end_of_text;
		}
		@Override
		String toText() {
			return "";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termEndOfText;
		}
	},
	REST_OF_TEXT {
		@Override
		public boolean isFinalToken() {
			return true;
		}
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_rest_of_text;
		}
		@Override
		String toText() {
			return "";
		}
		@Override
		Term toActorPrologTerm() throws TokenIsCompound {
			return termRestOfText;
		}
	},
	END_OF_LINE {
		@Override
		public boolean isFinalToken() {
			return true;
		}
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_end_of_line;
		}
		@Override
		String toText() {
			return "\n";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termEndOfLine;
		}
	},
	INTEGER {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return false;
		}
		@Override
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		@Override
		Term toActorPrologTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	REAL {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return false;
		}
		@Override
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		@Override
		Term toActorPrologTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	SYMBOL {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return false;
		}
		@Override
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		@Override
		Term toActorPrologTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	STRING_SEGMENT {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return false;
		}
		@Override
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		@Override
		Term toActorPrologTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	BINARY_SEGMENT {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return false;
		}
		@Override
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		@Override
		Term toActorPrologTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	KEYWORD {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return false;
		}
		@Override
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		@Override
		Term toActorPrologTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	VARIABLE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return false;
		}
		@Override
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		@Override
		Term toActorPrologTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	COMMA {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_comma;
		}
		@Override
		String toText() {
			return ",";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termComma;
		}
	},
	DOT {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_dot;
		}
		@Override
		String toText() {
			return ".";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termDot;
		}
	},
	EXCLAM {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_exclam;
		}
		@Override
		String toText() {
			return "!";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termExclam;
		}
	},
	COLON {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_colon;
		}
		@Override
		String toText() {
			return ":";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termColon;
		}
	},
	SEMICOLON {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_semicolon;
		}
		@Override
		String toText() {
			return ";";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termSemicolon;
		}
	},
	QUESTION_MARK {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_question_mark;
		}
		@Override
		String toText() {
			return "?";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termQuestionMark;
		}
	},
	NUMBER_SIGN {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_number_sign;
		}
		@Override
		String toText() {
			return "#";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termNumberSign;
		}
	},
	L_ROUND_BRACKET {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_l_round_bracket;
		}
		@Override
		String toText() {
			return "(";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termLRoundBracket;
		}
	},
	R_ROUND_BRACKET {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_r_round_bracket;
		}
		@Override
		String toText() {
			return ")";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termRRoundBracket;
		}
	},
	BAR {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_bar;
		}
		@Override
		String toText() {
			return "|";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termBar;
		}
	},
	L_BRACE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_l_brace;
		}
		@Override
		String toText() {
			return "{";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termLBrace;
		}
	},
	R_BRACE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_r_brace;
		}
		@Override
		String toText() {
			return "}";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termRBrace;
		}
	},
	L_SQUARE_BRACKET {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_l_square_bracket;
		}
		@Override
		String toText() {
			return "[";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termLSquareBracket;
		}
	},
	R_SQUARE_BRACKET {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_r_square_bracket;
		}
		@Override
		String toText() {
			return "]";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termRSquareBracket;
		}
	},
	MULTIPLY {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_multiply;
		}
		@Override
		String toText() {
			return "*";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termMyltiply;
		}
	},
	PLUS {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_plus;
		}
		@Override
		String toText() {
			return "+";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termPlus;
		}
	},
	MINUS {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_minus;
		}
		@Override
		String toText() {
			return "-";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termMinus;
		}
	},
	DIVIDE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_divide;
		}
		@Override
		String toText() {
			return "/";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termDivide;
		}
	},
	LT {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_lt;
		}
		@Override
		String toText() {
			return "<";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termLT;
		}
	},
	EQ {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_eq;
		}
		@Override
		String toText() {
			return "=";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termEQ;
		}
	},
	GT {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_gt;
		}
		@Override
		String toText() {
			return ">";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termGT;
		}
	},
	DATA_MESSAGE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_data_message;
		}
		@Override
		String toText() {
			return "<<";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termInformationalMessage;
		}
	},
	CONTROL_MESSAGE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_control_message;
		}
		@Override
		String toText() {
			return "<-";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termControlMessage;
		}
	},
	RESIDENT {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_resident;
		}
		@Override
		String toText() {
			return "??";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termResident;
		}
	},
	IMPLICATION {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_implication;
		}
		@Override
		String toText() {
			return ":-";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termImplication;
		}
	},
	EQUALITY {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_equality;
		}
		@Override
		String toText() {
			return "==";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termEquality;
		}
	},
	ASSIGNMENT {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_assignment;
		}
		@Override
		String toText() {
			return ":=";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termAssignment;
		}
	},
	NE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_ne;
		}
		@Override
		String toText() {
			return "<>";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termNE;
		}
	},
	LE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_le;
		}
		@Override
		String toText() {
			return "<=";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termLE;
		}
	},
	GE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_ge;
		}
		@Override
		String toText() {
			return ">=";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termGE;
		}
	},
	RANGE {
		@Override
		boolean correspondsToSymbolCode(long code) {
			return code==SymbolCodes.symbolCode_E_range;
		}
		@Override
		String toText() {
			return "..";
		}
		@Override
		Term toActorPrologTerm() {
			initiateStaticTermsIfNecessary();
			return termRange;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static volatile boolean staticTermsAreInitiated= false;
	public static volatile Object guard= new Object();
	public static Term termEndOfText;
	public static Term termRestOfText;
	public static Term termEndOfLine;
	public static Term termComma;
	public static Term termDot;
	public static Term termExclam;
	public static Term termColon;
	public static Term termSemicolon;
	public static Term termQuestionMark;
	public static Term termNumberSign;
	public static Term termLRoundBracket;
	public static Term termRRoundBracket;
	public static Term termBar;
	public static Term termLBrace;
	public static Term termRBrace;
	public static Term termLSquareBracket;
	public static Term termRSquareBracket;
	public static Term termMyltiply;
	public static Term termPlus;
	public static Term termMinus;
	public static Term termDivide;
	public static Term termLT;
	public static Term termEQ;
	public static Term termGT;
	public static Term termInformationalMessage;
	public static Term termControlMessage;
	public static Term termResident;
	public static Term termImplication;
	public static Term termEquality;
	public static Term termAssignment;
	public static Term termNE;
	public static Term termLE;
	public static Term termGE;
	public static Term termRange;
	//
	public static void initiateStaticTermsIfNecessary() {
		if (!staticTermsAreInitiated) {
			synchronized (guard) {
				if (staticTermsAreInitiated) {
					return;
				};
				termEndOfText= new PrologSymbol(SymbolCodes.symbolCode_E_end_of_text);
				termRestOfText= new PrologSymbol(SymbolCodes.symbolCode_E_rest_of_text);
				termEndOfLine= new PrologSymbol(SymbolCodes.symbolCode_E_end_of_line);
				termComma= new PrologSymbol(SymbolCodes.symbolCode_E_comma);
				termDot= new PrologSymbol(SymbolCodes.symbolCode_E_dot);
				termExclam= new PrologSymbol(SymbolCodes.symbolCode_E_exclam);
				termColon= new PrologSymbol(SymbolCodes.symbolCode_E_colon);
				termSemicolon= new PrologSymbol(SymbolCodes.symbolCode_E_semicolon);
				termQuestionMark= new PrologSymbol(SymbolCodes.symbolCode_E_question_mark);
				termNumberSign= new PrologSymbol(SymbolCodes.symbolCode_E_number_sign);
				termLRoundBracket= new PrologSymbol(SymbolCodes.symbolCode_E_l_round_bracket);
				termRRoundBracket= new PrologSymbol(SymbolCodes.symbolCode_E_r_round_bracket);
				termBar= new PrologSymbol(SymbolCodes.symbolCode_E_bar);
				termLBrace= new PrologSymbol(SymbolCodes.symbolCode_E_l_brace);
				termRBrace= new PrologSymbol(SymbolCodes.symbolCode_E_r_brace);
				termLSquareBracket= new PrologSymbol(SymbolCodes.symbolCode_E_l_square_bracket);
				termRSquareBracket= new PrologSymbol(SymbolCodes.symbolCode_E_r_square_bracket);
				termMyltiply= new PrologSymbol(SymbolCodes.symbolCode_E_multiply);
				termPlus= new PrologSymbol(SymbolCodes.symbolCode_E_plus);
				termMinus= new PrologSymbol(SymbolCodes.symbolCode_E_minus);
				termDivide= new PrologSymbol(SymbolCodes.symbolCode_E_divide);
				termLT= new PrologSymbol(SymbolCodes.symbolCode_E_lt);
				termEQ= new PrologSymbol(SymbolCodes.symbolCode_E_eq);
				termGT= new PrologSymbol(SymbolCodes.symbolCode_E_gt);
				termInformationalMessage= new PrologSymbol(SymbolCodes.symbolCode_E_data_message);
				termControlMessage= new PrologSymbol(SymbolCodes.symbolCode_E_control_message);
				termResident= new PrologSymbol(SymbolCodes.symbolCode_E_resident);
				termImplication= new PrologSymbol(SymbolCodes.symbolCode_E_implication);
				termEquality= new PrologSymbol(SymbolCodes.symbolCode_E_equality);
				termAssignment= new PrologSymbol(SymbolCodes.symbolCode_E_assignment);
				termNE= new PrologSymbol(SymbolCodes.symbolCode_E_ne);
				termLE= new PrologSymbol(SymbolCodes.symbolCode_E_le);
				termGE= new PrologSymbol(SymbolCodes.symbolCode_E_ge);
				termRange= new PrologSymbol(SymbolCodes.symbolCode_E_range);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isFinalToken() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean correspondsToActorPrologTerm(Term argument, ChoisePoint iX) {
		try {
			long code= argument.getSymbolValue(iX);
			return correspondsToSymbolCode(code);
		} catch (TermIsNotASymbol e) {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract boolean correspondsToSymbolCode(long code);
	abstract String toText() throws TokenIsCompound;
	abstract Term toActorPrologTerm() throws TokenIsCompound;
}
