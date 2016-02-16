// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.run.errors.*;
import morozov.syntax.scanner.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.ArrayList;
import java.util.Locale;

public abstract class SymbolicInformation extends DataAbstraction {
	//
	public Boolean caseSensitivity= null;
	//
	///////////////////////////////////////////////////////////////
	//
	public SymbolicInformation() {
	}
	public SymbolicInformation(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract protected Term getBuiltInSlot_E_case_sensitivity();
	//
	abstract public long entry_f_GetString_0();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set case_sensitivity
	//
	public void setCaseSensitivity1s(ChoisePoint iX, Term a1) {
		setCaseSensitivity(OnOff.termOnOff2Boolean(a1,iX));
	}
	public void setCaseSensitivity(boolean value) {
		caseSensitivity= value;
	}
	public void getCaseSensitivity0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= OnOff.boolean2TermOnOff(getCaseSensitivity(iX));
	}
	public void getCaseSensitivity0fs(ChoisePoint iX) {
	}
	public boolean getCaseSensitivity(ChoisePoint iX) {
		if (caseSensitivity != null) {
			return caseSensitivity;
		} else {
			Term value= getBuiltInSlot_E_case_sensitivity();
			return OnOff.termOnOff2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long domainSignatureOfSubgoal_0_InClause_1(long predicateSignatureNumber) {
		throw new UndefinedInternalCallTableEntry();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void length1ff(ChoisePoint iX, PrologVariable result, Term a2) {
		String text= Converters.argumentToString(a2,iX);
		result.value= new PrologInteger(text.length());
	}
	public void length1fs(ChoisePoint iX, Term inputText) {
	}
	public void length0ff(ChoisePoint iX, PrologVariable result) {
		Term inputText= retrieveTextString(iX);
		length1ff(iX,result,inputText);
	}
	public void length0fs(ChoisePoint iX) {
	}
	//
	public void format2mff(ChoisePoint iX, PrologVariable a1, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToFormattedString(iX,(Term[])args);
		a1.value= new PrologString(textBuffer.toString());
		// iX.pushTrail(a1);
	}
	public void format2mfs(ChoisePoint iX, Term... args) {
	}
	//
	public void upper1ff(ChoisePoint iX, PrologVariable result, Term a2) {
		String text= Converters.argumentToString(a2,iX);
		result.value= new PrologString(text.toUpperCase(Locale.ENGLISH));
	}
	public void upper1fs(ChoisePoint iX, Term inputText) {
	}
	public void upper0ff(ChoisePoint iX, PrologVariable result) {
		Term inputText= retrieveTextString(iX);
		upper1ff(iX,result,inputText);
	}
	public void upper0fs(ChoisePoint iX) {
	}
	//
	public void lower1ff(ChoisePoint iX, PrologVariable result, Term a2) {
		String text= Converters.argumentToString(a2,iX);
		result.value= new PrologString(text.toLowerCase(Locale.ENGLISH));
	}
	public void lower1fs(ChoisePoint iX, Term inputText) {
	}
	public void lower0ff(ChoisePoint iX, PrologVariable result) {
		Term inputText= retrieveTextString(iX);
		lower1ff(iX,result,inputText);
	}
	public void lower0fs(ChoisePoint iX) {
	}
	//
	public void convertToInteger0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		convertToInteger1ff(iX,result,inputText);
	}
	public void convertToInteger0fs(ChoisePoint iX) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		convertToInteger1fs(iX,inputText);
	}
	//
	public void convertToReal0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		convertToReal1ff(iX,result,inputText);
	}
	public void convertToReal0fs(ChoisePoint iX) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		convertToReal1fs(iX,inputText);
	}
	//
	public void convertToNumerical0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		convertToNumerical1ff(iX,result,inputText);
	}
	public void convertToNumerical0fs(ChoisePoint iX) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		convertToNumerical1fs(iX,inputText);
	}
	//
	public void stringToTerm0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		stringToTerm1ff(iX,result,inputText);
	}
	public void stringToTerm0fs(ChoisePoint iX) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		stringToTerm1fs(iX,inputText);
	}
	//
	public void stringToTerms0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		stringToTerms1ff(iX,result,inputText);
	}
	public void stringToTerms0fs(ChoisePoint iX) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		stringToTerms1fs(iX,inputText);
	}
	//
	public void isIdentifier1s(ChoisePoint iX, Term a1) throws Backtracking {
		String text= Converters.argumentToString(a1,iX);
		isIdentifier(text);
	}
	public void isIdentifier0s(ChoisePoint iX) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		isIdentifier1s(iX,inputText);
	}
	protected void isIdentifier(String text) throws Backtracking {
		if (!LexicalScanner.isAnyIdentifier(text)) {
			throw Backtracking.instance;
		}
	}
	//
	public class Search2ff extends Search {
		public Search2ff(Continuation aC, PrologVariable a1, Term a2, Term a3) {
			c0= aC;
			outputResult= a1;
			inputText= a2;
			inputTarget= a3;
			hasOutput= true;
			retrieveTextString= false;
		}
	}
	public class Search2fs extends Search {
		public Search2fs(Continuation aC, Term a2, Term a3) {
			c0= aC;
			inputText= a2;
			inputTarget= a3;
			hasOutput= false;
			retrieveTextString= false;
		}
	}
	public class Search1ff extends Search {
		public Search1ff(Continuation aC, PrologVariable a1, Term a2) {
			c0= aC;
			outputResult= a1;
			inputTarget= a2;
			hasOutput= true;
			retrieveTextString= true;
		}
	}
	public class Search1fs extends Search {
		public Search1fs(Continuation aC, Term a1) {
			c0= aC;
			inputTarget= a1;
			hasOutput= false;
			retrieveTextString= true;
		}
	}
	public class Search extends Continuation {
		protected PrologVariable outputResult;
		protected boolean hasOutput;
		protected boolean retrieveTextString;
		protected Term inputText;
		protected Term inputTarget;
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			if (retrieveTextString) {
				inputText= retrieveTextString(iX);
			};
			String text= Converters.argumentToString(inputText,iX);
			String target= Converters.argumentToString(inputTarget,iX);
			boolean caseSensitivity= getCaseSensitivity(iX);
			int textLength= text.length();
			ChoisePoint newIx= new ChoisePoint(iX);
			int currentPosition= 0;
			while(true) {
				if (currentPosition <= textLength) {
					int p1= SystemUtils.indexOf(text,target,currentPosition,caseSensitivity);
					if (p1 >= 0) {
						if (hasOutput) {
							outputResult.value= new PrologInteger(p1+1);
							// newIx.pushTrail(outputResult);
						};
						try {
							c0.execute(newIx);
						} catch (Backtracking b) {
							if (newIx.isEnabled()) {
								newIx.freeTrail();
								currentPosition= p1 + 1;
								continue;
							} else {
								throw Backtracking.instance;
							}
						};
						return;
					} else {
						break;
					}
				} else {
					break;
				}
			};
			throw Backtracking.instance;
		}
	}
	//
	public void split4s(ChoisePoint iX, Term position, Term inputText, PrologVariable front, PrologVariable tail) throws Backtracking {
		int n= Converters.argumentToSmallInteger(position,iX);
		String text= Converters.argumentToString(inputText,iX);
		if (n < 0 || n > text.length()) {
			throw Backtracking.instance;
		} else {
			front.value= new PrologString(text.substring(0,n));
			tail.value= new PrologString(text.substring(n));
			iX.pushTrail(front);
			iX.pushTrail(tail);
		}
	}
	public void split4s(ChoisePoint iX, Term position, Term inputText, Term front, PrologVariable tail) throws Backtracking {
		int n= Converters.argumentToSmallInteger(position,iX);
		String text= Converters.argumentToString(inputText,iX);
		if (n < 0 || n > text.length()) {
			throw Backtracking.instance;
		} else {
			front.isString(text.substring(0,n),iX);
			tail.value= new PrologString(text.substring(n));
			iX.pushTrail(tail);
		}
	}
	public void split4s(ChoisePoint iX, Term position, Term inputText, PrologVariable front, Term tail) throws Backtracking {
		int n= Converters.argumentToSmallInteger(position,iX);
		String text= Converters.argumentToString(inputText,iX);
		if (n < 0 || n > text.length()) {
			throw Backtracking.instance;
		} else {
			front.value= new PrologString(text.substring(0,n));
			tail.isString(text.substring(n),iX);
			iX.pushTrail(front);
		}
	}
	public void split4s(ChoisePoint iX, Term position, Term inputText, Term front, Term tail) throws Backtracking {
		int n= Converters.argumentToSmallInteger(position,iX);
		String text= Converters.argumentToString(inputText,iX);
		if (n < 0 || n > text.length()) {
			throw Backtracking.instance;
		} else {
			front.isString(text.substring(0,n),iX);
			tail.isString(text.substring(n),iX);
		}
	}
	public void split3s(ChoisePoint iX, Term position, PrologVariable front, PrologVariable tail) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		split4s(iX,position,inputText,front,tail);
	}
	public void split3s(ChoisePoint iX, Term position, Term front, PrologVariable tail) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		split4s(iX,position,inputText,front,tail);
	}
	public void split3s(ChoisePoint iX, Term position, PrologVariable front, Term tail) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		split4s(iX,position,inputText,front,tail);
	}
	public void split3s(ChoisePoint iX, Term position, Term front, Term tail) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		split4s(iX,position,inputText,front,tail);
	}
	//
	public void extractFrontToken3s(ChoisePoint iX, Term a1, PrologVariable a2, PrologVariable a3) throws Backtracking {
		String text= Converters.argumentToString(a1,iX);
		String[] textSegments= extractFrontToken(text);
		a2.value= new PrologString(textSegments[0]);
		a3.value= new PrologString(textSegments[1]);
		iX.pushTrail(a2);
		iX.pushTrail(a3);
	}
	public void extractFrontToken3s(ChoisePoint iX, Term a1, Term a2, PrologVariable a3) throws Backtracking {
		String text= Converters.argumentToString(a1,iX);
		String[] textSegments= extractFrontToken(text);
		a2.isString(textSegments[0],iX);
		a3.value= new PrologString(textSegments[1]);
		// iX.pushTrail(a2);
		iX.pushTrail(a3);
	}
	public void extractFrontToken3s(ChoisePoint iX, Term a1, PrologVariable a2, Term a3) throws Backtracking {
		String text= Converters.argumentToString(a1,iX);
		String[] textSegments= extractFrontToken(text);
		a2.value= new PrologString(textSegments[0]);
		a3.isString(textSegments[1],iX);
		iX.pushTrail(a2);
		// iX.pushTrail(a3);
	}
	public void extractFrontToken3s(ChoisePoint iX, Term a1, Term a2, Term a3) throws Backtracking {
		String text= Converters.argumentToString(a1,iX);
		String[] textSegments= extractFrontToken(text);
		a2.isString(textSegments[0],iX);
		a3.isString(textSegments[1],iX);
		// iX.pushTrail(a2);
		// iX.pushTrail(a3);
	}
	public void extractFrontToken3s(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) throws Backtracking {
		String token= Converters.argumentToString(a2,iX);
		String rest= Converters.argumentToString(a3,iX);
		a1.value= new PrologString(token+rest);
		iX.pushTrail(a1);
	}
	public void extractFrontToken2s(ChoisePoint iX, PrologVariable token, PrologVariable rest) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		extractFrontToken3s(iX,inputText,token,rest);
	}
	public void extractFrontToken2s(ChoisePoint iX, Term token, PrologVariable rest) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		extractFrontToken3s(iX,inputText,token,rest);
	}
	public void extractFrontToken2s(ChoisePoint iX, PrologVariable token, Term rest) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		extractFrontToken3s(iX,inputText,token,rest);
	}
	public void extractFrontToken2s(ChoisePoint iX, Term token, Term rest) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		extractFrontToken3s(iX,inputText,token,rest);
	}
	protected String[] extractFrontToken(String text) throws Backtracking {
		LexicalScanner scanner= new LexicalScanner(true);
		PrologToken[] tokens= scanner.analyse(text,true);
		if (tokens.length > 2) { // The last token is END_OF_TEXT/REST_OF_TEXT.
			int p1= tokens[0].position;
			int p2= tokens[1].position;
			String[] result= new String[2];
			result[0]= text.substring(p1,p2);
			result[1]= text.substring(p2);
			return result;
		} else if (tokens.length==2) {
			int p1= tokens[0].position;
			int p2= tokens[1].position;
			String[] result= new String[2];
			if (p2 <= text.length()) {
				result[0]= text.substring(p1,p2);
				result[1]= text.substring(p2);
			} else {
				result[0]= text.substring(p1);
				result[1]= "";
			};
			return result;
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void extractTokens1ff(ChoisePoint iX, PrologVariable result, Term a2) {
		String text= Converters.argumentToString(a2,iX);
		result.value= extractTokens(text);
	}
	public void extractTokens1fs(ChoisePoint iX, Term a1) {
	}
	public void extractTokens0ff(ChoisePoint iX, PrologVariable result) {
		Term inputText= retrieveTextString(iX);
		extractTokens1ff(iX,result,inputText);
	}
	public void extractTokens0fs(ChoisePoint iX) {
	}
	protected Term extractTokens(String text) {
		ArrayList<Term> tokens= new ArrayList<Term>(0);
		while (true) {
			try {
				String[] textSegments= extractFrontToken(text);
				tokens.add(new PrologString(textSegments[0]));
				text= textSegments[1];
			} catch (Backtracking b) {
				break;
			}
		};
		return Converters.arrayListToTerm(tokens);
	}
	//
	public void skipSpaces1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String text= Converters.argumentToString(a2,iX);
		String rest= LexicalScanner.skipFrontSpaces(text);
		a1.value= new PrologString(rest);
		// iX.pushTrail(a1);
	}
	public void skipSpaces1fs(ChoisePoint iX, PrologVariable a1, Term a2) {
	}
	public void skipSpaces0ff(ChoisePoint iX, PrologVariable a1) {
		Term inputText= retrieveTextString(iX);
		skipSpaces1ff(iX,a1,inputText);
	}
	public void skipSpaces0fs(ChoisePoint iX, PrologVariable a1) {
	}
	//
	public void trim1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String text= Converters.argumentToString(a2,iX);
		String rest= text.trim();
		a1.value= new PrologString(rest);
		// iX.pushTrail(a1);
	}
	public void trim1fs(ChoisePoint iX, PrologVariable a1, Term a2) {
	}
	public void trim0ff(ChoisePoint iX, PrologVariable a1) {
		Term inputText= retrieveTextString(iX);
		trim1ff(iX,a1,inputText);
	}
	public void trim0fs(ChoisePoint iX, PrologVariable a1) {
	}
	//
	public void extractLines1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String text= Converters.argumentToString(a2,iX);
		a1.value= extractLines(text);
	}
	public void extractLines1fs(ChoisePoint iX, Term a1) {
	}
	public void extractLines0ff(ChoisePoint iX, PrologVariable a1) {
		Term inputText= retrieveTextString(iX);
		extractLines1ff(iX,a1,inputText);
	}
	public void extractLines0fs(ChoisePoint iX) {
	}
	protected Term extractLines(String text) {
		String[] values= text.split("\n",-1);
		int lastElement= values.length-1;
		if (values[lastElement].isEmpty()) {
			lastElement= lastElement - 1;
		};
		Term result= PrologEmptyList.instance;
		for (int n=lastElement; n >= 0; n--) {
			String value= values[n];
			while (true) {
				if (value.endsWith("\r")) {
					if (value.length() > 1) {
						value= value.substring(0,value.length()-1);
					} else {
						value= "";
						break;
					}
				} else {
					break;
				}
			};
			result= new PrologList(new PrologString(value),result);
		};
		return result;
	}
	// (i,o,o)
	public void extractFrontCode3s(ChoisePoint iX, Term inputText, PrologVariable front, PrologVariable tail) throws Backtracking {
		String text= Converters.argumentToString(inputText,iX);
		if (text.length() < 1) {
			throw Backtracking.instance;
		} else {
			int code= text.codePointAt(0);
			front.value= new PrologInteger(code);
			tail.value= new PrologString(text.substring(1));
			iX.pushTrail(front);
			iX.pushTrail(tail);
		}
	}
	public void extractFrontCode2s(ChoisePoint iX, PrologVariable front, PrologVariable tail) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		extractFrontCode3s(iX,inputText,front,tail);
	}
	// (i,i,o)
	public void extractFrontCode3s(ChoisePoint iX, Term inputText, Term front, PrologVariable tail) throws Backtracking {
		String text= Converters.argumentToString(inputText,iX);
		if (text.length() < 1) {
			throw Backtracking.instance;
		} else {
			int code1= Converters.argumentToSmallInteger(front,iX);
			int code2= text.codePointAt(0);
			if (code1 != code2) {
				throw Backtracking.instance;
			} else {
				tail.value= new PrologString(text.substring(1));
				iX.pushTrail(tail);
			}
		}
	}
	public void extractFrontCode2s(ChoisePoint iX, Term front, PrologVariable tail) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		extractFrontCode3s(iX,inputText,front,tail);
	}
	// (i,o,i)
	public void extractFrontCode3s(ChoisePoint iX, Term inputText, PrologVariable front, Term tail) throws Backtracking {
		String text= Converters.argumentToString(inputText,iX);
		if (text.length() < 1) {
			throw Backtracking.instance;
		} else {
			String t2= Converters.argumentToString(tail,iX);
			if ( !t2.equals(text.substring(1)) ) {
				throw Backtracking.instance;
			} else {
				int code= text.codePointAt(0);
				front.value= new PrologInteger(code);
				iX.pushTrail(front);
			}
		}
	}
	public void extractFrontCode2s(ChoisePoint iX, PrologVariable front, Term tail) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		extractFrontCode3s(iX,inputText,front,tail);
	}
	// (i,i,i)
	public void extractFrontCode3s(ChoisePoint iX, Term inputText, Term front, Term tail) throws Backtracking {
		String text= Converters.argumentToString(inputText,iX);
		if (text.length() < 1) {
			throw Backtracking.instance;
		} else {
			int code1= Converters.argumentToSmallInteger(front,iX);
			String t2= Converters.argumentToString(tail,iX);
			int code2= text.codePointAt(0);
			if (code1 != code2) {
				throw Backtracking.instance;
			} else {
				if ( !t2.equals(text.substring(1)) ) {
					throw Backtracking.instance;
				}
			}
		}
	}
	public void extractFrontCode2s(ChoisePoint iX, Term front, Term tail) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		extractFrontCode3s(iX,inputText,front,tail);
	}
	// (o,i,i)
	public void extractFrontCode3s(ChoisePoint iX, PrologVariable outputText, Term front, Term tail) throws Backtracking {
		int codePoint1= Converters.argumentToSmallInteger(front,iX);
		String t2= Converters.argumentToString(tail,iX);
		outputText.value= new PrologString(new String(new int[]{codePoint1},0,1).concat(t2));
		iX.pushTrail(outputText);
	}
	//
	public void replace3ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3, Term a4) throws Backtracking {
		String text= Converters.argumentToString(a2,iX);
		String target= Converters.argumentToString(a3,iX);
		String replacement= Converters.argumentToString(a4,iX);
		boolean caseSensitivity= getCaseSensitivity(iX);
		int index= SystemUtils.indexOf(text,target,0,caseSensitivity);
		if (index == -1) {
			throw Backtracking.instance;
		} else {
			StringBuilder result= new StringBuilder(text.substring(0,index));
			result.append(replacement);
			result.append(text.substring(index+target.length()));
			a1.value= new PrologString(result.toString());
			// iX.pushTrail(a1);
		}
	}
	public void replace3fs(ChoisePoint iX, Term a1, Term a2, Term a3) throws Backtracking {
		String text= Converters.argumentToString(a1,iX);
		String target= Converters.argumentToString(a2,iX);
		String replacement= Converters.argumentToString(a3,iX);
		boolean caseSensitivity= getCaseSensitivity(iX);
		int index= SystemUtils.indexOf(text,target,0,caseSensitivity);
		if (index == -1) {
			throw Backtracking.instance;
		}
	}
	public void replace2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		replace3ff(iX,a1,inputText,a2,a3);
	}
	public void replace2fs(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		Term inputText= retrieveTextString(iX);
		replace3fs(iX,inputText,a1,a2);
	}
	//
	public void replaceAll3ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3, Term a4) {
		String text= Converters.argumentToString(a2,iX);
		String target= Converters.argumentToString(a3,iX);
		String replacement= Converters.argumentToString(a4,iX);
		boolean caseSensitivity= getCaseSensitivity(iX);
		String modifiedText;
		if (!caseSensitivity && target.length() > 0) {
			ArrayList<String> table= new ArrayList<String>();
			String rest;
			int currentPosition= 0;
			while (true) {
				if (currentPosition < text.length()) {
					int index= SystemUtils.indexOf(text,target,currentPosition,caseSensitivity);
					if (index == -1) {
						rest= text.substring(currentPosition);
						break;
					} else {
						table.add(text.substring(currentPosition,index));
						currentPosition= index + target.length();
					}
				} else {
					rest= "";
					break;
				}
			};
			StringBuilder buffer= new StringBuilder();
			for (int n=0; n < table.size(); n++) {
				buffer.append(table.get(n));
				buffer.append(replacement);
			};
			buffer.append(rest);
			modifiedText= buffer.toString();
		} else {
			modifiedText= text.replace(target,replacement);
		};
		a1.value= new PrologString(modifiedText);
		// iX.pushTrail(a1);
	}
	public void replaceAll3fs(ChoisePoint iX, Term a1, Term a2, Term a3) {
	}
	public void replaceAll2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Term inputText= retrieveTextString(iX);
		replaceAll3ff(iX,a1,inputText,a2,a3);
	}
	public void replaceAll2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term retrieveTextString(ChoisePoint iX) {
		long domainSignature= entry_f_GetString_0();
		PrologVariable inputText= new PrologVariable();
		Term[] arguments= new Term[1];
		arguments[0]= inputText;
		Continuation c1= new DomainSwitch(new SuccessTermination(),domainSignature,SymbolicInformation.this,SymbolicInformation.this,arguments);
		ChoisePoint newIndex= new ChoisePoint(iX);
		try {
			c1.execute(newIndex);
		} catch (Backtracking b) {
			throw new GetStringProcedureFailed();
		};
		return inputText;
	}
}