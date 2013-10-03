// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import target.*;

import morozov.run.*;
import morozov.terms.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class HTML_BasicExplorer extends HTML_ExplorerTools {
	//
	protected URI extractBaseURI(boolean backslashIsSeparator) throws Backtracking {
		while(stack.peek() < text.length()) {
			skipSpaces();
			if (text.regionMatches(stack.peek(),"<",0,1)) {
				shiftTextPosition(1);
				if (text.regionMatches(stack.peek(),"!--",0,3)) {
					shiftTextPosition(3);
					skipCurrentComment();
					continue;
				};
				skipSpaces();
				if (isFrontName("HTML")) {
					shiftTextPosition(4);
					skipCurrentTag();
					return extractBaseURIFromHTML(backslashIsSeparator);
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		};
		throw Backtracking.instance;
	}
	//
	protected URI extractBaseURIFromHTML(boolean backslashIsSeparator) throws Backtracking {
		while(stack.peek() < text.length()) {
			skipSpaces();
			if (text.regionMatches(stack.peek(),"<",0,1)) {
				shiftTextPosition(1);
				if (text.regionMatches(stack.peek(),"!--",0,3)) {
					shiftTextPosition(3);
					skipCurrentComment();
					continue;
				};
				skipSpaces();
				if (isFrontName("HEAD")) {
					shiftTextPosition(4);
					skipCurrentTag();
					return extractBaseURIFromHEAD(backslashIsSeparator);
				} else {
					if (headerIsOver()) {
						throw Backtracking.instance;
					};
					skipCurrentTag();
					continue;
				}
			} else {
				shiftTextPosition(1);
				skipSpaces();
				continue;
			}
		};
		throw Backtracking.instance;
	}
	//
	protected URI extractBaseURIFromHEAD(boolean backslashIsSeparator) throws Backtracking {
		while(stack.peek() < text.length()) {
			skipSpaces();
			if (text.regionMatches(stack.peek(),"<",0,1)) {
				shiftTextPosition(1);
				if (text.regionMatches(stack.peek(),"!--",0,3)) {
					shiftTextPosition(3);
					skipCurrentComment();
					continue;
				};
				skipSpaces();
				if (isFrontName("BASE")) {
					shiftTextPosition(4);
					String path= extractPairValue("HREF");
					path= URL_Utils.replaceSlashesAndSpaces(path,backslashIsSeparator);
					path= URL_Utils.replaceAmpersands(path);
					try {
						return new URI(path);
					} catch (URISyntaxException e) {
						throw Backtracking.instance;
					}
				} else {
					if (headerIsOver()) {
						throw Backtracking.instance;
					};
					skipCurrentTag();
					continue;
				}
			} else {
				shiftTextPosition(1);
				skipSpaces();
				continue;
			}
		};
		throw Backtracking.instance;
	}
	//
	protected boolean headerIsOver() {
		try {
			stack.push(stack.peek());
			if (isFrontName("BODY")) {
				return true;
			} else if (text.regionMatches(stack.peek(),"/",0,1)) {
				shiftTextPosition(1);
				skipSpaces();
				if (isFrontName("HEAD")) {
					return true;
				} else if (isFrontName("BODY")) {
					return true;
				} else if (isFrontName("HTML")) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} finally {
			stack.pop();
		}
	}
	//
	protected void extractTagAttributes(ArrayList<Term> tagAttributes) {
		while (stack.peek() < text.length()) {
			try {
				String name= extractAttributeValue(false);
				skipSpaces();
				if (isFrontToken("=")) {
					shiftTextPosition(1);
					String value= extractAttributeValue(false);
					tagAttributes.add(new PrologStructure(SymbolCodes.symbolCode_E_attribute,new Term[]{new PrologString(name.toUpperCase()),new PrologString(value)}));
				} else {
					tagAttributes.add(new PrologStructure(SymbolCodes.symbolCode_E_attribute,new Term[]{new PrologString(""),new PrologString(name)}));
					continue;
				}
			} catch (Backtracking b) {
				return;
			}
		};
		return;
	}
	//
	protected String extractPairValue(String pairName) throws Backtracking {
		while (stack.peek() < text.length()) {
			String name= extractAttributeValue(false);
			skipSpaces();
			if (isFrontToken("=")) {
				shiftTextPosition(1);
				if (name.equalsIgnoreCase(pairName)) {
					String value= extractAttributeValue(false);
					return value;
				} else {
					extractAttributeValue(true);
				}
			} else {
				if (name.equalsIgnoreCase(pairName)) {
					throw Backtracking.instance;
				} else {
					continue;
				}
			}
		};
		throw Backtracking.instance;
	}
}
