// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.terms.*;

import java.net.URI;
import java.net.URISyntaxException;

public class HTML_BasicExplorer extends HTML_ExplorerTools {
	//
	protected URI extractBaseURI(boolean backslashIsSeparator) throws Backtracking {
		while(stack.peek() < text.length()) {
			skipSpaces();
			if (text.regionMatches(stack.peek(),"<",0,1)) {
				shiftTextPosition(1);
				// skipSpaces();
				if (text.regionMatches(stack.peek(),"!",0,1)) {
					shiftTextPosition(1);
					skipCurrentTag();
					// skipSpaces();
					continue;
				} else if (isFrontTag("HTML")) {
					shiftTextPosition(4);
					skipCurrentTag();
					return extractBaseURIFromHTML(backslashIsSeparator);
				} else {
					throw new Backtracking();
				}
			} else {
				throw new Backtracking();
			}
		};
		throw new Backtracking();
	}
	//
	protected URI extractBaseURIFromHTML(boolean backslashIsSeparator) throws Backtracking {
		while(stack.peek() < text.length()) {
			skipSpaces();
			if (text.regionMatches(stack.peek(),"<",0,1)) {
				shiftTextPosition(1);
				// skipSpaces();
				if (isFrontTag("HEAD")) {
					shiftTextPosition(4);
					skipCurrentTag();
					return extractBaseURIFromHEAD(backslashIsSeparator);
				} else {
					if (headerIsOver()) {
						throw new Backtracking();
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
		throw new Backtracking();
	}
	//
	protected URI extractBaseURIFromHEAD(boolean backslashIsSeparator) throws Backtracking {
		while(stack.peek() < text.length()) {
			skipSpaces();
			if (text.regionMatches(stack.peek(),"<",0,1)) {
				shiftTextPosition(1);
				// skipSpaces();
				if (isFrontTag("BASE")) {
					shiftTextPosition(4);
					String path= extractPairValue("HREF");
					path= URL_Utils.replaceSlashesAndSpaces(path,backslashIsSeparator);
					path= URL_Utils.replaceAmpersands(path);
					try {
						return new URI(path);
					} catch (URISyntaxException e) {
						// throw new WrongTermIsMalformedURL(e);
						throw new Backtracking();
					}
				} else {
					if (headerIsOver()) {
						throw new Backtracking();
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
		throw new Backtracking();
	}
	//
	protected boolean headerIsOver() {
		try {
			stack.push(stack.peek());
			if (isFrontTag("BODY")) {
				return true;
			} else if (text.regionMatches(stack.peek(),"/",0,1)) {
				shiftTextPosition(1);
				// skipSpaces();
				if (isFrontTag("HEAD")) {
					return true;
				} else if (isFrontTag("BODY")) {
					return true;
				} else if (isFrontTag("HTML")) {
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
}
