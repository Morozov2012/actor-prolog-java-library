// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.syntax.scanner.signals.*;
import morozov.terms.*;

public enum PrologTokenType {
	//
	END_OF_TEXT {
		String toText() {
			return "";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termEndOfText;
		}
	},
	REST_OF_TEXT {
		String toText() {
			return "";
		}
		Term toTerm() throws TokenIsCompound {
			return termRestOfText;
		}
	},
	END_OF_LINE {
		String toText() {
			return "\n";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termEndOfLine;
		}
	},
	INTEGER {
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		Term toTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	REAL {
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		Term toTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	SYMBOL {
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		Term toTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	STRING {
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		Term toTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	BINARY {
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		Term toTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	KEYWORD {
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		Term toTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	VARIABLE {
		String toText() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
		Term toTerm() throws TokenIsCompound {
			throw TokenIsCompound.instance;
		}
	},
	COMMA {
		String toText() {
			return ",";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termComma;
		}
	},
	DOT {
		String toText() {
			return ".";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termDot;
		}
	},
	EXCLAM {
		String toText() {
			return "!";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termExclam;
		}
	},
	COLON {
		String toText() {
			return ":";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termColon;
		}
	},
	SEMICOLON {
		String toText() {
			return ";";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termSemicolon;
		}
	},
	QUESTION_MARK {
		String toText() {
			return "?";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termQuestionMark;
		}
	},
	NUMBER_SIGN {
		String toText() {
			return "#";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termNumberSign;
		}
	},
	L_ROUND_BRACKET {
		String toText() {
			return "(";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termLRoundBracket;
		}
	},
	R_ROUND_BRACKET {
		String toText() {
			return ")";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termRRoundBracket;
		}
	},
	BAR {
		String toText() {
			return "|";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termBar;
		}
	},
	L_BRACE {
		String toText() {
			return "{";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termLBrace;
		}
	},
	R_BRACE {
		String toText() {
			return "}";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termRBrace;
		}
	},
	L_SQUARE_BRACKET {
		String toText() {
			return "[";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termLSquareBracket;
		}
	},
	R_SQUARE_BRACKET {
		String toText() {
			return "]";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termRSquareBracket;
		}
	},
	MULTIPLY {
		String toText() {
			return "*";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termMyltiply;
		}
	},
	PLUS {
		String toText() {
			return "+";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termPlus;
		}
	},
	MINUS {
		String toText() {
			return "-";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termMinus;
		}
	},
	DIVIDE {
		String toText() {
			return "/";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termDivide;
		}
	},
	LT {
		String toText() {
			return "<";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termLT;
		}
	},
	EQ {
		String toText() {
			return "=";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termEQ;
		}
	},
	GT {
		String toText() {
			return ">";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termGT;
		}
	},
	INFORMATIONAL_MESSAGE {
		String toText() {
			return "<<";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termInformationalMessage;
		}
	},
	CONTROL_MESSAGE {
		String toText() {
			return "<-";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termControlMessage;
		}
	},
	RESIDENT {
		String toText() {
			return "??";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termResident;
		}
	},
	IMPLICATION {
		String toText() {
			return ":-";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termImplication;
		}
	},
	EQUALITY {
		String toText() {
			return "==";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termEquality;
		}
	},
	ASSIGNMENT {
		String toText() {
			return ":=";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termAssignment;
		}
	},
	NE {
		String toText() {
			return "<>";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termNE;
		}
	},
	LE {
		String toText() {
			return "<=";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termLE;
		}
	},
	GE {
		String toText() {
			return ">=";
		}
		Term toTerm() {
			initiateStaticTermsIfNecessary();
			return termGE;
		}
	},
	RANGE {
		String toText() {
			return "..";
		}
		Term toTerm() {
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
				termInformationalMessage= new PrologSymbol(SymbolCodes.symbolCode_E_informational_message);
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
	abstract String toText() throws TokenIsCompound;
	abstract Term toTerm() throws TokenIsCompound;
}
