// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.run.*;
import morozov.terms.*;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Stack;

public class HTML_ExplorerTools {
	//
	protected Stack<Integer> stack= new Stack<Integer>();
	protected String text;
	//
	protected void shiftTextPosition(int increment) {
		stack.set(stack.size()-1,stack.peek()+increment);
	}
	//
	protected void skipSpaces() {
		int stackPosition= stack.size() - 1;
		int indexBound= text.length() - 1;
		int p1= stack.peek();
		while(true) {
			if (p1 <= indexBound && text.codePointAt(p1) <= 0x20) {
				p1++;
				continue;
			} else {
				stack.set(stackPosition,p1);
				return;
			}
		}
	}
	//
	public static String replaceAmpersands(String path) {
		if (path.toLowerCase().contains("&amp")) {
			int length= path.length();
			StringBuilder buffer= new StringBuilder();
			int beginningOfSegment= 0;
			int p= 0;
			while(p < length) {
				if (path.regionMatches(true,p,"&amp;",0,5)) {
					buffer.append(path.substring(beginningOfSegment,p));
					buffer.append("&");
					beginningOfSegment= p + 5;
					p= p + 5;
				} else if (path.regionMatches(true,p,"&amp",0,4)) {
					buffer.append(path.substring(beginningOfSegment,p));
					buffer.append("&");
					beginningOfSegment= p + 4;
					p= p + 4;
				} else {
					p= p + 1;
				}
			};
			buffer.append(path.substring(beginningOfSegment));
			return buffer.toString();
		} else {
			return path;
		}
	}
	//
	protected String extractAttributeValue(boolean skipValue) throws Backtracking {
		int stackPosition= stack.size() - 1;
		skipSpaces();
		if (text.regionMatches(stack.peek(),"\"",0,1)) {
			shiftTextPosition(1);
			int p1= stack.peek();
			int p2= text.indexOf('"',p1);
			if (p2 >= 0) {
				stack.set(stackPosition,p2+1);
				return text.substring(p1,p2);
			} else {
				throw Backtracking.instance;
			}
		} else if (text.regionMatches(stack.peek(),"'",0,1)) {
			shiftTextPosition(1);
			int p1= stack.peek();
			int p2= text.indexOf('\'',p1);
			if (p2 >= 0) {
				stack.set(stackPosition,p2+1);
				return text.substring(p1,p2);
			} else {
				throw Backtracking.instance;
			}
		} else {
			int indexBound= text.length() - 1;
			int p0= stack.peek();
			int p1= p0;
			while(true) {
				if (p1 <= indexBound) {
					int code= text.codePointAt(p1);
					if (isEndOfName(code)) {
						break;
					} else {
						p1++;
						continue;
					}
				} else {
					break;
				}
			};
			if (p1 > p0) {
				stack.set(stackPosition,p1);
				if (skipValue) {
					return null;
				} else {
					return text.substring(p0,p1);
				}
			} else {
				throw Backtracking.instance;
			}
		}
	}
	//
	protected String extractFrontName() throws Backtracking {
		return extractFrontValue().toUpperCase();
	}
	protected String extractFrontValue() throws Backtracking {
		skipSpaces();
		int stackPosition= stack.size() - 1;
		int indexBound= text.length() - 1;
		int p1= stack.peek();
		int p2= p1;
		while(true) {
			if (p2 <= indexBound) {
				int code= text.codePointAt(p2);
				if (isEndOfName(code)) {
					break;
				} else {
					p2++;
					continue;
				}
			} else {
				break;
			}
		};
		if (p2 > p1) {
			stack.set(stackPosition,p2);
			return text.substring(p1,p2);
		} else {
			throw Backtracking.instance;
		}
	}
	protected boolean isEndOfName(int code) {
		if (	code <= 0x20 ||
			code == '<' ||
			code == '>' ||
			code == '/' ||
			code == '=' ) {
			return true;
		} else {
			return false;
		}
	}
	//
	protected boolean isFrontToken(String token) {
		int tokenLength= token.length();
		if (text.regionMatches(true,stack.peek(),token,0,tokenLength)) {
			return true;
		} else {
			return false;
		}
	}
	protected boolean isFrontName(String token) {
		int tokenLength= token.length();
		if (text.regionMatches(true,stack.peek(),token,0,tokenLength)) {
			int nextCharacterPosition= stack.peek() + tokenLength;
			if (nextCharacterPosition < text.length()) {
				int c= text.codePointAt(nextCharacterPosition);
				if (Character.isLetter(c)) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	//
	protected boolean skipCurrentTag() throws Backtracking {
		int indexBound= text.length() - 1;
		int p1= stack.peek();
		if (p1 <= indexBound) {
			int p2= text.indexOf('>',p1);
			if (p2 >= 0) {
				if (!containsSuspiciousCharacters(p1,p2)) {
					stack.set(stack.size()-1,p2+1);
					if (p2 > 0 && text.codePointAt(p2-1)=='/') {
						return true;
					} else {
						return false;
					}
				} else {
					return searchEndOfCurrentTag();
				}
			} else {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	protected boolean containsSuspiciousCharacters(int leftBound, int rightBound) {
		int p1= text.indexOf('<',leftBound);
		if (p1 >= 0 && p1 <= rightBound) {
			return true;
		} else {
			int p2= text.indexOf('"',leftBound);
			if (p2 >= 0 && p2 <= rightBound) {
				return true;
			} else {
				return false;
			}
		}
	}
	protected boolean searchEndOfCurrentTag() throws Backtracking {
		int stackPosition= stack.size() - 1;
		int indexBound= text.length() - 1;
		boolean charactersAreEnclosed= false;
		for (int p1= stack.peek(); p1 <= indexBound; p1++) {
			int code= text.codePointAt(p1);
			if (code == '>') {
				if (!charactersAreEnclosed) {
					stack.set(stackPosition,p1+1);
					if (p1 > 0 && text.codePointAt(p1-1)=='/') {
						return true;
					} else {
						return false;
					}
				}
			} else if (code == '<') {
				if (!charactersAreEnclosed) {
					stack.set(stackPosition,p1);
					return false;
				}
			} else if (code == '"') {
				if (charactersAreEnclosed) {
					charactersAreEnclosed= false;
				} else {
					charactersAreEnclosed= true;
				}
			}
		};
		throw Backtracking.instance;
	}
	//
	protected void skipCurrentComment() {
		int indexBound= text.length() - 1;
		int p1= stack.peek();
		if (p1 <= indexBound) {
			int p2= text.indexOf("--",p1);
			if (p2 >= 0) {
				if (p2+2 <= indexBound && text.codePointAt(p2+2) == '>') {
					stack.set(stack.size()-1,p2+3);
				} else {
					stack.set(stack.size()-1,p2+2);
				}
			} else {
				return;
			}
		} else {
			return;
		}
	}
	//
	protected String deleteSharpIfNecessary(String path) {
		int length= path.length();
		int p1= path.indexOf('#');
		if (p1 >= 0) {
			return path.substring(0,p1);
		} else {
			return path;
		}
	}
	//
	protected Term assembleResultList(ArrayList<Term> items) {
		Term resultList= PrologEmptyList.instance;
		for (int k= items.size()-1; k >= 0; k--) {
			Term item= items.get(k);
			resultList= new PrologList(item,resultList);
		};
		return resultList;
	}
	protected Term assembleReferenceList(ArrayList<String> items) {
		Term resultList= PrologEmptyList.instance;
		for (int k= items.size()-1; k >= 0; k--) {
			String item= items.get(k);
			resultList= new PrologList(new PrologString(item),resultList);
		};
		return resultList;
	}
	public static boolean isEnabledReference(String path, Pattern pattern) {
		int length= path.length();
		for (int n= length-1; n >= 0; n--) {
			int c= path.codePointAt(n);
			if (c==':' || c=='/' || c=='\\') {
				path= path.substring(n+1);
				break;
			}
		};
		if (pattern.matcher(path).matches()) {
			return true;
		} else {
			return false;
		}
	}
}
