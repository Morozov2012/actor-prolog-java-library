// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public enum PrologTokenType {
	END_OF_TEXT {
		String toText() {
			return "";
		}
	},
	REST_OF_TEXT {
		String toText() throws TokenIsCompound {
			throw new TokenIsCompound();
		}
	},
	INTEGER {
		String toText() throws TokenIsCompound {
			throw new TokenIsCompound();
		}
	},
	REAL {
		String toText() throws TokenIsCompound {
			throw new TokenIsCompound();
		}
	},
	STRING {
		String toText() throws TokenIsCompound {
			throw new TokenIsCompound();
		}
	},
	SYMBOL {
		String toText() throws TokenIsCompound {
			throw new TokenIsCompound();
		}
	},
	VARIABLE {
		String toText() throws TokenIsCompound {
			throw new TokenIsCompound();
		}
	},
	COMMA {
		String toText() {
			return ",";
		}
	},
	DOT {
		String toText() {
			return ".";
		}
	},
	EXCLAM {
		String toText() {
			return "!";
		}
	},
	COLON {
		String toText() {
			return ":";
		}
	},
	SEMICOLON {
		String toText() {
			return ";";
		}
	},
	QUESTION_MARK {
		String toText() {
			return "?";
		}
	},
	NUMBER_SIGN {
		String toText() {
			return "#";
		}
	},
	L_ROUND_BRACKET {
		String toText() {
			return "(";
		}
	},
	R_ROUND_BRACKET {
		String toText() {
			return ")";
		}
	},
	BAR {
		String toText() {
			return "|";
		}
	},
	L_BRACE {
		String toText() {
			return "{";
		}
	},
	R_BRACE {
		String toText() {
			return "}";
		}
	},
	L_SQUARE_BRACKET {
		String toText() {
			return "[";
		}
	},
	R_SQUARE_BRACKET {
		String toText() {
			return "]";
		}
	},
	MULTIPLY {
		String toText() {
			return "*";
		}
	},
	PLUS {
		String toText() {
			return "+";
		}
	},
	MINUS {
		String toText() {
			return "-";
		}
	},
	DIVIDE {
		String toText() {
			return "/";
		}
	},
	LT {
		String toText() {
			return "<";
		}
	},
	EQ {
		String toText() {
			return "=";
		}
	},
	GT {
		String toText() {
			return ">";
		}
	},
	INFORMATIONAL_MESSAGE {
		String toText() {
			return "<<";
		}
	},
	CONTROL_MESSAGE {
		String toText() {
			return "<-";
		}
	},
	RESIDENT {
		String toText() {
			return "?";
		}
	},
	IMPLICATION {
		String toText() {
			return ":-";
		}
	},
	EQUALITY {
		String toText() {
			return "==";
		}
	},
	ASSIGNMENT {
		String toText() {
			return ":=";
		}
	},
	NE {
		String toText() {
			return "<>";
		}
	},
	LE {
		String toText() {
			return "<=";
		}
	},
	GE {
		String toText() {
			return ">=";
		}
	},
	RANGE {
		String toText() {
			return "..";
		}
	};
	abstract String toText() throws TokenIsCompound;
}
