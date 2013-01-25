// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.terms.*;

import java.net.URI;
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
	protected boolean isFrontTag(String tag) {
		int tagLength= tag.length();
		if (text.regionMatches(true,stack.peek(),tag,0,tagLength)) {
			int nextCharacterPosition= stack.peek() + tagLength;
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
	protected void skipCurrentTag() throws Backtracking {
		int p1= stack.peek();
		if (p1 <= text.length()-1) {
			int p2= text.indexOf('>',p1);
			if (p2 >= 0) {
				if (!containsSuspiciousCharacters(p1,p2)) {
					stack.set(stack.size()-1,p2+1);
					return;
				} else {
					searchEndOfCurrentTag();
				}
			} else {
				throw new Backtracking();
			}
		} else {
			throw new Backtracking();
		}
	}
	protected void searchEndOfCurrentTag() throws Backtracking {
		int stackPosition= stack.size() - 1;
		int indexBound= text.length() - 1;
		boolean charactersAreEnclosed= false;
		for (int p1= stack.peek(); p1 <= indexBound; p1++) {
			if (text.codePointAt(p1) == '>') {
				if (!charactersAreEnclosed) {
					stack.set(stackPosition,p1+1);
					return;
				}
			} else if (text.codePointAt(p1) == '<') {
				if (!charactersAreEnclosed) {
					stack.set(stackPosition,p1);
					return;
				}
			} else if (text.codePointAt(p1) == '"') {
				if (charactersAreEnclosed) {
					charactersAreEnclosed= false;
				} else {
					charactersAreEnclosed= true;
				}
			}
		};
		throw new Backtracking();
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
	//
	protected void skipAnyIdentifier() throws Backtracking {
		int stackPosition= stack.size() - 1;
		skipSpaces();
		if (text.regionMatches(stack.peek(),"\"",0,1)) {
			shiftTextPosition(1);
			int p1= stack.peek();
			int p2= text.indexOf('"',p1);
			if (p2 >= 0) {
				stack.set(stackPosition,p2+1);
				return;
			} else {
				throw new Backtracking();
			}
		} else if (text.regionMatches(stack.peek(),"'",0,1)) {
			shiftTextPosition(1);
			int p1= stack.peek();
			int p2= text.indexOf('\'',p1);
			if (p2 >= 0) {
				stack.set(stackPosition,p2+1);
				return;
			} else {
				throw new Backtracking();
			}
		} else {
			int indexBound= text.length() - 1;
			int p0= stack.peek();
			int p1= p0;
			while(true) {
				if (p1 <= indexBound) {
					int code= text.codePointAt(p1);
					if (code <= 0x20) {
						break;
					} else if (code == '=') {
						break;
					} else if (code == '<') {
						break;
					} else if (code == '>') {
						break;
					} else {
						p1++;
						continue;
					}
				} else {
					break;
				}
			};
			if (p1-1 > p0) {
				stack.set(stackPosition,p1);
				return;
			} else {
				throw new Backtracking();
			}
		}
	}
	//
	protected String extractPairValue(String pairName) throws Backtracking {
		while(stack.peek() < text.length()) {
			skipSpaces();
			if (isFrontTag(pairName)) {
				shiftTextPosition(pairName.length());
				skipSpaces();
				if (text.regionMatches(stack.peek(),"=",0,1)) {
					shiftTextPosition(1);
					skipSpaces();
					return extractPairValue();
				} else {
					throw new Backtracking();
				}
			} else {
				skipAnyIdentifier();
				skipSpaces();
				if (text.regionMatches(stack.peek(),"=",0,1)) {
					shiftTextPosition(1);
					skipSpaces();
					skipAnyIdentifier();
					continue;
				} else {
					continue;
				}
			}
		};
		throw new Backtracking();
	}
	protected String extractPairValue() throws Backtracking {
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
				throw new Backtracking();
			}
		} else if (text.regionMatches(stack.peek(),"'",0,1)) {
			shiftTextPosition(1);
			int p1= stack.peek();
			int p2= text.indexOf('\'',p1);
			if (p2 >= 0) {
				stack.set(stackPosition,p2+1);
				return text.substring(p1,p2);
			} else {
				throw new Backtracking();
			}
		} else {
			int indexBound= text.length() - 1;
			int p0= stack.peek();
			int p1= p0;
			while(true) {
				if (p1 <= indexBound) {
					int code= text.codePointAt(p1);
					if (code <= 0x20) {
						break;
					// } else if (code == '=') {
					//	break;
					} else if (code == '<') {
						break;
					} else if (code == '>') {
						break;
					} else {
						p1++;
						continue;
					}
				} else {
					break;
				}
			};
			if (p1-1 > p0) {
				stack.set(stackPosition,p1);
				return text.substring(p0,p1);
			} else {
				throw new Backtracking();
			}
		}
	}
	//
	protected String deleteControlCharacters(String segment) {
		boolean segmentIsSafe= true;
		for (int n=0; n < segment.length(); n++) {
			int code= segment.codePointAt(n);
			if (code <= 0x20) {
				segmentIsSafe= false;
				break;
			} if (code == '&') {
				segmentIsSafe= false;
				break;
			}
		};
		if (segmentIsSafe) {
			return segment;
		} else {
			StringBuilder buffer= new StringBuilder("");
			boolean previousCharacterWasControl= false;
			for (int n=0; n < segment.length(); n++) {
				int code= segment.codePointAt(n);
				if (code < 0x20) {
					if (n == segment.length() - 2 && segment.codePointAt(n+1) < 0x20) {
						break;
					} else if (n == segment.length() - 1) {
						break;
					} else {
						if (buffer.length() > 0 && !previousCharacterWasControl) {
							buffer.append(' ');
						};
						previousCharacterWasControl= true;
						continue;
					}
				} else if (code <= 0x20) {
					buffer.append(' ');
				} else if (code == '&') {
					if (segment.regionMatches(true,n,"&NBSP;",0,6)) {
						n= n + 5;
						buffer.append(' ');
					} else {
						buffer.appendCodePoint(code);
					}
				} else {
					buffer.appendCodePoint(code);
				};
				previousCharacterWasControl= false;
			};
			return buffer.toString();
		}
	}
	//
	protected String resolveURI(URI baseURI, String path) {
		URI name= baseURI.resolve(path);
		return name.toASCIIString();
	}
	//
	protected Term assembleResultList(ArrayList<Term> items) {
		Term resultList= new PrologEmptyList();
		for (int k= items.size()-1; k >= 0; k--) {
			Term item= items.get(k);
			resultList= new PrologList(item,resultList);
		};
		return resultList;
	}
	protected Term assembleReferenceList(ArrayList<String> items) {
		Term resultList= new PrologEmptyList();
		for (int k= items.size()-1; k >= 0; k--) {
			String item= items.get(k);
			resultList= new PrologList(new PrologString(item),resultList);
		};
		return resultList;
	}
	public static boolean isEnabledReference(String path, Pattern pattern, boolean backslashIsSeparator) {
		int length= path.length();
		for (int n= length-1; n >= 0; n--) {
			int c= path.codePointAt(n);
			if (c==':' || c=='/' || (backslashIsSeparator && c=='\\')) {
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
