// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import target.*;

import morozov.system.files.*;
import morozov.terms.*;

import java.net.URI;

import java.util.regex.Pattern;
import java.util.Stack;
import java.util.ArrayList;

public class HTML_Explorer extends HTML_BasicExplorer {
	//
	protected Stack<String> tagNesting= new Stack<String>();
	//
	public Term textToTerm(String aText, URI baseURI, String[] tags, String mask, boolean backslashIsSeparator) {
		text= aText;
		stack.clear();
		stack.push(0);
		try {
			stack.push(stack.peek());
			URI uri= extractBaseURI(backslashIsSeparator);
			baseURI= uri;
		} catch (Backtracking b) {
		} finally {
			stack.pop();
		};
		try {
			stack.push(stack.peek());
			char[] nativePattern= mask.toCharArray();
			Pattern pattern= FileNameMask.wildcard2UnixPattern(nativePattern);
			Term list= extractTagStructure(baseURI,tags,pattern,backslashIsSeparator);
			return list;
		} finally {
			stack.pop();
		}
	}
	public Term textToReferences(String aText, String mask, URI baseURI, boolean backslashIsSeparator) {
		text= aText;
		stack.clear();
		stack.push(0);
		try {
			stack.push(stack.peek());
			URI uri= extractBaseURI(backslashIsSeparator);
			baseURI= uri;
		} catch (Backtracking b) {
		} finally {
			stack.pop();
		};
		try {
			stack.push(stack.peek());
			Term list= extractReferences(baseURI,mask,backslashIsSeparator);
			return list;
		} finally {
			stack.pop();
		}
	}
	//
	protected Term extractTagStructure(URI baseURI, String[] tags, Pattern pattern, boolean backslashIsSeparator) {
		ArrayList<Term> items= new ArrayList<Term>();
		while(stack.peek() < text.length()) {
			int p1= stack.peek();
			int pNext= p1;
			try {
				// skipSpaces();
				if (text.regionMatches(stack.peek(),"<",0,1)) {
					if (isEndOfBlock()) {
						return assembleResultList(items);
					} else {
						shiftTextPosition(1);
						if (isHyperReference(baseURI,tags,pattern,items,backslashIsSeparator)) {
							continue;
						} else {
							acceptBlock(baseURI,tags,pattern,items,backslashIsSeparator);
							continue;
						}
					}
				}
			} catch (IllegalArgumentException e) {
				stack.set(stack.size()-1,p1);
				pNext= pNext + 1;
			} catch (Backtracking b) {
				stack.set(stack.size()-1,p1);
				pNext= pNext + 1;
			} catch(ReferenceIsNotEnabled e) {
				continue;
			};
			int p2= text.indexOf('<',pNext);
			if (p2 >= 0) {
				if (p1 < p2) {
					String textSegment= text.substring(p1,p2);
					stack.set(stack.size()-1,p2);
					textSegment= deleteControlCharacters(textSegment);
					if (textSegment.length() > 0) {
						items.add(new PrologString(textSegment));
					}
				} else {
					String textSegment= text.substring(p1);
					stack.set(stack.size()-1,text.length());
					textSegment= deleteControlCharacters(textSegment);
					if (textSegment.length() > 0) {
						items.add(new PrologString(textSegment));
					};
					return assembleResultList(items);
				}
			} else {
				if (p1 < text.length()) {
					String textSegment= text.substring(p1);
					stack.set(stack.size()-1,text.length());
					textSegment= deleteControlCharacters(textSegment);
					if (textSegment.length() > 0) {
						items.add(new PrologString(textSegment));
					}
				} else {
					return assembleResultList(items);
				}
			}
		};
		return assembleResultList(items);
	}
	protected Term extractReferences(URI baseURI, String mask, boolean backslashIsSeparator) {
		char[] nativePattern= mask.toCharArray();
		Pattern pattern= FileNameMask.wildcard2UnixPattern(nativePattern);
		ArrayList<String> items= new ArrayList<String>();
		while(stack.peek() < text.length()) {
			int p1= stack.peek();
			int p2= text.indexOf('<',p1);
			if (p2 >= 0) {
				stack.set(stack.size()-1,p2+1);
				try {
					if (isFrontTag("A")) {
						shiftTextPosition(1);
						String path= extractPairValue("HREF");
						path= URL_Utils.replaceSlashesAndSpaces(path,backslashIsSeparator);
						path= URL_Utils.replaceAmpersands(path);
						path= resolveURI(baseURI,path);
						if (!items.contains(path) && isEnabledReference(path,pattern,backslashIsSeparator)) {
							items.add(path);
						}
					} else if (isFrontTag("IMG")) {
						shiftTextPosition(3);
						String path= extractPairValue("SRC");
						path= URL_Utils.replaceSlashesAndSpaces(path,backslashIsSeparator);
						path= URL_Utils.replaceAmpersands(path);
						path= resolveURI(baseURI,path);
						if (!items.contains(path) && isEnabledReference(path,pattern,backslashIsSeparator)) {
							items.add(path);
						}
					} else if (isFrontTag("FRAME")) {
						shiftTextPosition(5);
						String path= extractPairValue("SRC");
						path= URL_Utils.replaceSlashesAndSpaces(path,backslashIsSeparator);
						path= URL_Utils.replaceAmpersands(path);
						path= resolveURI(baseURI,path);
						if (!items.contains(path) && isEnabledReference(path,pattern,backslashIsSeparator)) {
							items.add(path);
						}
					};
					skipCurrentTag();
				} catch (IllegalArgumentException e) {
				} catch (Backtracking b) {
					stack.set(stack.size()-1,p2+1);
				}
			} else {
				return assembleReferenceList(items);
			}
		};
		return assembleReferenceList(items);
	}
	protected boolean isEndOfBlock() {
		try {
			stack.push(stack.peek());
			// skipSpaces();
			if (text.regionMatches(stack.peek(),"<",0,1)) {
				shiftTextPosition(1);
				if (	isFrontTag("P") &&
					tagNesting.size() > 0 &&
					tagNesting.peek().regionMatches(true,0,"P",0,1)) {
					return true;
				} else if (text.regionMatches(stack.peek(),"/",0,1)) {
					shiftTextPosition(1);
					// skipSpaces();
					boolean isEnd= false;
					String outerTag= null;
					// int outerTagLength= 0;
					for (int n= 0; n < tagNesting.size(); n++) {
						outerTag= tagNesting.get(n);
						if (isFrontTag(outerTag)) {
							return true;
						}
					}
				}
			};
			return false;
		} finally {
			stack.pop();
		}
	}
	protected boolean isHyperReference(URI baseURI, String[] tags, Pattern pattern, ArrayList<Term> items, boolean backslashIsSeparator) throws Backtracking, ReferenceIsNotEnabled {
		if (isFrontTag("A")) {
			shiftTextPosition(1);
			String path= extractPairValue("HREF");
			path= URL_Utils.replaceSlashesAndSpaces(path,backslashIsSeparator);
			path= URL_Utils.replaceAmpersands(path);
			path= resolveURI(baseURI,path);
			skipCurrentTag();
			if (isEnabledReference(path,pattern,backslashIsSeparator)) {
				try {
					// skipCurrentTag();
					tagNesting.push("A");
					Term internalStructure= extractTagStructure(baseURI,tags,pattern,backslashIsSeparator);
					items.add(new PrologStructure(SymbolCodes.symbolCode_E_ref,new Term[]{new PrologString(path),internalStructure}));
					return true;
				} finally {
					tagNesting.pop();
				}
			} else {
				// return false;
				throw new ReferenceIsNotEnabled();
			}
		} else if (isFrontTag("IMG")) {
			shiftTextPosition(3);
			String path= extractPairValue("SRC");
			path= URL_Utils.replaceSlashesAndSpaces(path,backslashIsSeparator);
			path= URL_Utils.replaceAmpersands(path);
			path= resolveURI(baseURI,path);
			skipCurrentTag();
			if (isEnabledReference(path,pattern,backslashIsSeparator)) {
				items.add(new PrologStructure(SymbolCodes.symbolCode_E_ref,new Term[]{new PrologString(path),new PrologEmptyList()}));
			};
			return true;
		} else if (isFrontTag("FRAME")) {
			shiftTextPosition(5);
			String path= extractPairValue("SRC");
			path= URL_Utils.replaceSlashesAndSpaces(path,backslashIsSeparator);
			path= URL_Utils.replaceAmpersands(path);
			path= resolveURI(baseURI,path);
			skipCurrentTag();
			if (isEnabledReference(path,pattern,backslashIsSeparator)) {
				try {
					// skipCurrentTag();
					tagNesting.push("FRAME");
					Term internalStructure= extractTagStructure(baseURI,tags,pattern,backslashIsSeparator);
					items.add(new PrologStructure(SymbolCodes.symbolCode_E_ref,new Term[]{new PrologString(path),internalStructure}));
					return true;
				} finally {
					tagNesting.pop();
				}
			} else {
				// return false;
				throw new ReferenceIsNotEnabled();
			}
		} else {
			return false;
		}
	}
	//
	protected void acceptBlock(URI baseURI, String[] tags, Pattern pattern, ArrayList<Term> items, boolean backslashIsSeparator) throws Backtracking {
		// skipSpaces();
		boolean flag= false;
		String tag= null;
		int tagLength= 0;
		for (int n= 0; n < tags.length; n++) {
			tag= tags[n];
			if (isFrontTag(tag)) {
				flag= true;
				tagLength= tag.length();
				break;
			}
		};
		if (flag) {
			shiftTextPosition(tagLength);
			try {
				skipCurrentTag();
				tagNesting.push(tag);
				Term internalStructure= extractTagStructure(baseURI,tags,pattern,backslashIsSeparator);
				items.add(new PrologStructure(SymbolCodes.symbolCode_E_block,new Term[]{new PrologString(tag),internalStructure}));
				// return true;
			} finally {
				tagNesting.pop();
				// return false;
			}
		} else {
			skipCurrentTag();
			// return true;
		}
	}
}
