// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import target.*;

import morozov.run.*;
import morozov.system.files.*;
import morozov.terms.*;

import java.net.URI;
import java.util.regex.Pattern;
import java.util.Stack;
import java.util.ArrayList;

public class HTML_Explorer extends HTML_BasicExplorer {
	//
	protected boolean extractAttributes;
	protected boolean coalesceAdjacentStrings;
	protected boolean truncateStrings;
	protected String[] tags;
	protected String[] unpairedTagsTable;
	protected String[] flatTagsTable;
	protected String[][] referenceContainersTable;
	protected String[][] specialEntitiesTable;
	//
	protected Stack<String> tagNesting= new Stack<>();
	//
	public HTML_Explorer(
			boolean aExtractAttributes,
			boolean aCoalesceAdjacentStrings,
			boolean aTruncateStrings,
			String[] aTags,
			String[] aUnpairedTagsTable,
			String[] aFlatTagsTable,
			String[][] aReferenceContainersTable,
			String[][] aSpecialEntitiesTable) {
		extractAttributes= aExtractAttributes;
		coalesceAdjacentStrings= aCoalesceAdjacentStrings;
		truncateStrings= aTruncateStrings;
		tags= aTags;
		unpairedTagsTable= aUnpairedTagsTable;
		flatTagsTable= aFlatTagsTable;
		referenceContainersTable= aReferenceContainersTable;
		specialEntitiesTable= aSpecialEntitiesTable;
	}
	//
	public Term textToTerm(String aText, URI baseURI, String mask) {
		text= aText;
		stack.clear();
		stack.push(0);
		try {
			stack.push(stack.peek());
			URI uri= extractBaseURI();
			baseURI= uri;
		} catch (Backtracking b) {
		} finally {
			stack.pop();
		};
		try {
			stack.push(stack.peek());
			char[] nativePattern= mask.toCharArray();
			Pattern pattern= FileNameMask.wildcard2UnixPattern(nativePattern);
			Term list= extractTagStructure(baseURI,pattern);
			return list;
		} finally {
			stack.pop();
		}
	}
	public Term textToReferences(String aText, String mask, URI baseURI) {
		text= aText;
		stack.clear();
		stack.push(0);
		try {
			stack.push(stack.peek());
			URI uri= extractBaseURI();
			baseURI= uri;
		} catch (Backtracking b) {
		} finally {
			stack.pop();
		};
		try {
			stack.push(stack.peek());
			Term list= extractReferences(baseURI,mask);
			return list;
		} finally {
			stack.pop();
		}
	}
	//
	protected Term extractTagStructure(URI baseURI, Pattern pattern) {
		ArrayList<Term> items= new ArrayList<>();
		StringBuilder textBuffer= new StringBuilder();
		while(stack.peek() < text.length()) {
			int p1= stack.peek();
			int pNext= p1;
			int code= text.codePointAt(stack.peek());
			if (code < 0x20) {
				shiftTextPosition(1);
				continue;
			};
			try {
				if (text.regionMatches(stack.peek(),"<",0,1)) {
					if (text.regionMatches(stack.peek()+1,"!--",0,3)) {
						shiftTextPosition(4);
						skipCurrentComment();
						continue;
					};
					if (isEndOfBlock()) {
						break;
					} else {
						shiftTextPosition(1);
						if (text.regionMatches(stack.peek(),"/",0,1)) {
							skipCurrentTag();
							continue;
						} else if (isHyperReference(baseURI,pattern,items,textBuffer)) {
							continue;
						} else {
							acceptBlock(baseURI,pattern,items,textBuffer);
							continue;
						}
					}
				}
			} catch (Backtracking b) {
				stack.set(stack.size()-1,p1);
				pNext= pNext + 1;
			} catch (IllegalArgumentException e) {
				stack.set(stack.size()-1,p1);
				pNext= pNext + 1;
			};
			int p2= text.indexOf('<',pNext);
			if (p2 >= 0) {
				if (p1 < p2) {
					String textSegment= text.substring(p1,p2);
					appendString(textSegment,items,textBuffer);
					stack.set(stack.size()-1,p2);
				} else {
					String textSegment= text.substring(p1);
					appendString(textSegment,items,textBuffer);
					stack.set(stack.size()-1,text.length());
					break;
				}
			} else {
				if (p1 < text.length()) {
					String textSegment= text.substring(p1);
					appendString(textSegment,items,textBuffer);
					stack.set(stack.size()-1,text.length());
				} else {
					break;
				}
			}
		};
		if (textBuffer.length() > 0) {
			items.add(new PrologString(textBuffer.toString()));
			textBuffer.setLength(0);
		};
		return assembleResultList(items);
	}
	protected void appendString(String segment, ArrayList<Term> items, StringBuilder textBuffer) {
		if (truncateStrings) {
			int beginning= segment.length();
			for (int n= 0; n < segment.length(); n++) {
				int code= segment.codePointAt(n);
				if (code <= 0x20) {
					continue;
				} else {
					beginning= n;
					break;
				}
			};
			int end= -1;
			for (int n= segment.length()-1; n >= beginning; n--) {
				int code= segment.codePointAt(n);
				if (code <= 0x20) {
					continue;
				} else {
					end= n;
					break;
				}
			};
			if (beginning > end) {
				return;
			} else if (beginning > 0 || end < segment.length()-1) {
				segment= segment.substring(beginning,end+1);
			}
		};
		segment= deleteControlCharacters(segment,true);
		if (segment.length() > 0) {
			if (coalesceAdjacentStrings) {
				if (textBuffer.length() > 0) {
					textBuffer.append(' ');
				};
				textBuffer.append(segment);
			} else {
				items.add(new PrologString(segment));
			}
		}
	}
	protected Term extractReferences(URI baseURI, String mask) {
		char[] nativePattern= mask.toCharArray();
		Pattern pattern= FileNameMask.wildcard2UnixPattern(nativePattern);
		ArrayList<String> items= new ArrayList<>();
		while(stack.peek() < text.length()) {
			int p1= stack.peek();
			int p2= text.indexOf('<',p1);
			if (p2 >= 0) {
				stack.set(stack.size()-1,p2+1);
				if (text.regionMatches(stack.peek(),"!--",0,3)) {
					shiftTextPosition(3);
					skipCurrentComment();
					continue;
				};
				skipSpaces();
				try {
					for (int n= 0; n < referenceContainersTable.length; n++) {
						String containerName= referenceContainersTable[n][0];
						if (isFrontName(containerName)) {
							shiftTextPosition(containerName.length());
							String path= extractPairValue(referenceContainersTable[n][1]);
							path= scanURI(path,baseURI);
							if (!items.contains(path) && isEnabledReference(path,pattern)) {
								items.add(path);
							};
							break;
						}
					};
					skipCurrentTag();
				} catch (Backtracking b) {
					stack.set(stack.size()-1,p2+1);
				} catch (IllegalArgumentException e) {
					stack.set(stack.size()-1,p2+1);
				}
			} else {
				return assembleReferenceList(items);
			}
		};
		return assembleReferenceList(items);
	}
	protected boolean isEndOfBlock() {
		if (tagNesting.size() <= 0) {
			return false;
		};
		try {
			stack.push(stack.peek());
			if (text.regionMatches(stack.peek(),"<",0,1)) {
				shiftTextPosition(1);
				String currentTag= tagNesting.peek();
				if (text.regionMatches(stack.peek(),"/",0,1)) {
					shiftTextPosition(1);
					skipSpaces();
					if (isFrontName(currentTag)) {
						return true;
					} else {
						for (int n= tagNesting.size()-1; n >= 0; n--) {
							String outerTag= tagNesting.get(n);
							if (isFrontName(outerTag)) {
								return true;
							}
						};
						return false;
					}
				} else {
					skipSpaces();
					if (isFrontName(currentTag)) {
						for (int n= 0; n < flatTagsTable.length-1; n++) {
							if (currentTag.equals(flatTagsTable[n])) {
								return true;
							}
						};
						return false;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		} finally {
			stack.pop();
		}
	}
	protected boolean isHyperReference(URI baseURI, Pattern pattern, ArrayList<Term> items, StringBuilder textBuffer) {
		try {
			stack.push(stack.peek());
			skipSpaces();
			String containerName= extractFrontName();
			for (int n= 0; n < tags.length; n++) {
				if (containerName.equals(tags[n])) {
					stack.pop();
					return false;
				}
			};
			for (int n= 0; n < referenceContainersTable.length; n++) {
				if (containerName.equals(referenceContainersTable[n][0])) {
					int p1= stack.peek();
					String path= extractPairValue(referenceContainersTable[n][1]);
					path= scanURI(path,baseURI);
					if (isEnabledReference(path,pattern)) {
						ArrayList<Term> tagAttributes= new ArrayList<>();
						if (extractAttributes) {
							stack.set(stack.size()-1,p1);
							extractTagAttributes(tagAttributes);
						};
						boolean tagIsUnpaired= skipCurrentTag();
						if (!tagIsUnpaired) {
							for (int i= 0; i < unpairedTagsTable.length; i++) {
								if (containerName.equals(unpairedTagsTable[i])) {
									tagIsUnpaired= true;
									break;
								}
							}
						};
						if (tagIsUnpaired) {
							if (textBuffer.length() > 0) {
								items.add(new PrologString(textBuffer.toString()));
								textBuffer.setLength(0);
							};
							Term internalStructure= PrologEmptyList.instance;
							for (int k= tagAttributes.size()-1; k >= 0; k--) {
								internalStructure= new PrologList(tagAttributes.get(k),internalStructure);
							};
							items.add(new PrologStructure(SymbolCodes.symbolCode_E_ref,new Term[]{new PrologString(path),internalStructure}));
							return true;
						} else {
							try {
								if (textBuffer.length() > 0) {
									items.add(new PrologString(textBuffer.toString()));
									textBuffer.setLength(0);
								};
								tagNesting.push(containerName);
								Term internalStructure= extractTagStructure(baseURI,pattern);
								for (int k= tagAttributes.size()-1; k >= 0; k--) {
									internalStructure= new PrologList(tagAttributes.get(k),internalStructure);
								};
								items.add(new PrologStructure(SymbolCodes.symbolCode_E_ref,new Term[]{new PrologString(path),internalStructure}));
								return true;
							} finally {
								tagNesting.pop();
							}
						}
					} else {
						skipCurrentTag();
						return true;
					}
				}
			};
			stack.pop();
			return false;
		} catch (Backtracking b) {
			stack.pop();
			return false;
		} catch (IllegalArgumentException e) {
			stack.pop();
			return false;
		}
	}
	//
	protected void acceptBlock(URI baseURI, Pattern pattern, ArrayList<Term> items, StringBuilder textBuffer) throws Backtracking {
		String tag;
		try {
			tag= extractFrontName();
		} catch (Backtracking b) {
			skipCurrentTag();
			return;
		};
		boolean flag= false;
		for (int n= 0; n < tags.length; n++) {
			if (tag.equals(tags[n])) {
				flag= true;
				break;
			}
		};
		if (flag) {
			ArrayList<Term> tagAttributes= new ArrayList<>();
			if (extractAttributes) {
				extractTagAttributes(tagAttributes);
			};
			boolean tagIsUnpaired= skipCurrentTag();
			if (!tagIsUnpaired) {
				for (int n= 0; n < unpairedTagsTable.length; n++) {
					if (tag.equals(unpairedTagsTable[n])) {
						tagIsUnpaired= true;
						break;
					}
				}
			};
			if (tagIsUnpaired) {
				if (textBuffer.length() > 0) {
					items.add(new PrologString(textBuffer.toString()));
					textBuffer.setLength(0);
				};
				Term internalStructure= PrologEmptyList.instance;
				for (int k= tagAttributes.size()-1; k >= 0; k--) {
					internalStructure= new PrologList(tagAttributes.get(k),internalStructure);
				};
				items.add(new PrologStructure(SymbolCodes.symbolCode_E_block,new Term[]{new PrologString(tag),internalStructure}));
				return;
			} else {
				try {
					if (textBuffer.length() > 0) {
						items.add(new PrologString(textBuffer.toString()));
						textBuffer.setLength(0);
					};
					tagNesting.push(tag);
					Term internalStructure= extractTagStructure(baseURI,pattern);
					for (int k= tagAttributes.size()-1; k >= 0; k--) {
						internalStructure= new PrologList(tagAttributes.get(k),internalStructure);
					};
					items.add(new PrologStructure(SymbolCodes.symbolCode_E_block,new Term[]{new PrologString(tag),internalStructure}));
				} finally {
					tagNesting.pop();
				}
			}
		} else {
			skipCurrentTag();
		}
	}
	//
	protected String scanURI(String path, URI baseURI) {
		path= deleteControlCharacters(path,false);
		path= SimpleFileName.replaceBackslashesAndSpaces(path);
		path= replaceAmpersands(path);
		path= deleteSharpIfNecessary(path);
		URI name= baseURI.resolve(path);
		return name.toASCIIString();
	}
	//
	protected String deleteControlCharacters(String segment, boolean enableSpecialEntitiesReplace) {
		boolean segmentIsSafe= true;
		for (int n=0; n < segment.length(); n++) {
			int code= segment.codePointAt(n);
			if (code < 0x20) {
				segmentIsSafe= false;
				break;
			} else if (code == '&' && enableSpecialEntitiesReplace) {
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
					previousCharacterWasControl= true;
					continue;
				} else if (code == 0x20) {
					if (previousCharacterWasControl && buffer.length() > 0) {
						buffer.append(' ');
					};
					buffer.append(' ');
					previousCharacterWasControl= false;
				} else if (code == '&' && enableSpecialEntitiesReplace) {
					boolean isSpecialEntity= false;
					for (int k= 0; k < specialEntitiesTable.length; k++) {
						String currentSpecialEntity= specialEntitiesTable[k][0];
						int specialEntityLength= currentSpecialEntity.length();
						int endPosition= n + 1 + specialEntityLength;
						if (	endPosition < text.length() &&
							segment.regionMatches(true,n+1,currentSpecialEntity,0,specialEntityLength) &&
							segment.codePointAt(endPosition) == ';') {
							isSpecialEntity= true;
							n= endPosition;
							if (previousCharacterWasControl && buffer.length() > 0) {
								buffer.append(' ');
							};
							buffer.append(specialEntitiesTable[k][1]);
							previousCharacterWasControl= false;
							break;
						}
					};
					if (!isSpecialEntity) {
						if (previousCharacterWasControl && buffer.length() > 0) {
							buffer.append(' ');
						};
						buffer.appendCodePoint(code);
						previousCharacterWasControl= false;
					}
				} else {
					if (previousCharacterWasControl && buffer.length() > 0) {
						buffer.append(' ');
					};
					buffer.appendCodePoint(code);
					previousCharacterWasControl= false;
				}
			};
			return buffer.toString();
		}
	}
}
