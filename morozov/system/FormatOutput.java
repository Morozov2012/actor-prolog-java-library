// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.ArrayList;

public class FormatOutput {
	// Output operations
	public static StringBuilder termsToString(ChoisePoint cp, Term... args) {
		StringBuilder textBuffer= new StringBuilder();
		for(int i= 0; i < args.length; i++) {
			Term item= args[i];
			if (item.thisIsArgumentNumber()) {
				// System.out.printf("FormatOutput:: i=%s; thisIsArgumentNumber: %s\n",i,item);
				Term extraItems= args[i+1];
				// try {
				for (int j= 0; j < item.getNumber(); j++) {
					if (j > 0) {
						extraItems= extraItems.getExistentTail();
					};
					// System.out.printf("FormatOutput:: extraItems= %s; j=%s; Size=%s\n",extraItems,j,item.getNumber());
					Term extraItem= extraItems.getExistentHead();
					// System.out.printf("FormatOutput:: extraItem= %s\n",extraItem);
					textBuffer.append(extraItem.toString(cp));
				};
				break;
			} else {
				textBuffer.append(item.toString(cp));
			}
		};
		return textBuffer;
	}
	//
	public static StringBuilder termsToFormattedString(ChoisePoint cp, Term... args) {
		StringBuilder textBuffer= new StringBuilder();
		String formatString= new String();
		boolean formatStringIsAccepted= false;
		ArrayList<Term> argumentTable= new ArrayList<Term>();
		for(int i= 0; i < args.length; i++) {
			Term item= args[i];
			if (item.thisIsArgumentNumber()) {
				Term extraItems= args[i+1];
				// try {
				for (int j= 0; j < item.getNumber(); j++) {
					if (j > 0) {
						extraItems= extraItems.getExistentTail();
					};
					Term extraItem= extraItems.getExistentHead();
					if (formatStringIsAccepted) {
						argumentTable.add(extraItem);
					} else {
						try {
							formatString= extraItem.getStringValue(cp);
						} catch (TermIsNotAString e) {
							throw new WrongArgumentIsNotAString(extraItem);
						};
						formatStringIsAccepted= true;
					}
				};
				break;
			} else {
				if (formatStringIsAccepted) {
					argumentTable.add(item);
				} else {
					try {
						formatString= item.getStringValue(cp);
					} catch (TermIsNotAString e) {
						throw new WrongArgumentIsNotAString(item);
					};
					formatStringIsAccepted= true;
				}
			}
		};
		if (argumentTable.size() > 0) {
			Term[] argumentArray= new Term[argumentTable.size()];
			textBuffer= implementFormat(textBuffer,formatString,cp,argumentTable.toArray(argumentArray));
		} else {
			textBuffer= implementFormat(textBuffer,formatString,cp);
		};
		return textBuffer;
	}
	//
	public static StringBuilder implementFormat(StringBuilder textBuffer, String format, ChoisePoint cp, Term... args) {
		// System.out.printf("FormatOutput::format=>>>%s<<<\n",format);
		if (args.length > 0) {
			for(int i= 0; i < args.length; i++) {
				int end= format_length(format);
				String front= format.substring(0,end);
				if (front.length() <= 0) {
					throw new WrongNumberOfArgumentsInTheFormatString();
				};
				textBuffer= output_item(textBuffer,front,cp,args[i]);
				// if (end+1 > format.length() )
				//	break;
				format= format.substring(end);
			};
			int end= format_length(format);
			// System.out.printf("FormatOutput::END::format=>>>%s<<< end=%d %% \n",format,end);
			if (end > 0) {
				throw new WrongNumberOfArgumentsInTheFormatString();
			}
		} else {
			int end= front_length(format);
			String front= format.substring(0,end);
			textBuffer.append(front);
		};
		return textBuffer;
	}
	//
	private static int format_length(String commands) {
		boolean flag= false;
		int counter= 0;
		for (int n=0; n < commands.length(); n++) {
			if (counter < commands.length()) {
				// System.out.printf("FormatOutput:: %d: %s flag=%s\n",counter,commands.substring(counter),flag);
				if (	counter+1 < commands.length() &&
					commands.codePointAt(counter)=='%' &&
					commands.codePointAt(counter+1)=='%' ) {
					counter= counter + 2;
					continue;
				} else {
					if (commands.codePointAt(counter)=='%') {
						if (flag) {
							return counter;
						} else {
							flag= true;
						}
					};
					counter= counter + 1;
				}
			} else {
				return commands.length();
			}
		};
		return counter;
	}
	//
	private static int front_length(String commands) {
		int counter= 0;
		for (int n=0; n < commands.length(); n++) {
			if (counter < commands.length()) {
				if (	counter+1 <= commands.length() &&
					commands.codePointAt(counter)=='%' &&
					commands.codePointAt(counter+1)=='%' ) {
					counter= counter + 2;
					continue;
				} else {
					if (commands.codePointAt(counter)=='%')
						return counter;
					counter= counter + 1;
				}
			} else {
				return commands.length();
			}
		};
		return counter;
	}
	//
	private static StringBuilder output_item(StringBuilder textBuffer, String formatString, ChoisePoint cp, Term v) {
		v= v.dereferenceValue(cp);
		if (v.thisIsFreeVariable()) {
			textBuffer.append("_");
		} else {
			try {
				if (is_format_c(formatString)) {
					int n= v.getSmallIntegerValue(cp);
					textBuffer.append(String.format(formatString,(char)n));
				} else {
					BigInteger n= v.getIntegerValue(cp);
					textBuffer.append(String.format(formatString,n));
				}
			} catch (TermIsNotAnInteger e1) {
				try {
					double n= v.getRealValue(cp);
					textBuffer.append(String.format(formatString,n));
				} catch (TermIsNotAReal e2) {
					// System.out.printf("FormatOutput::formatString=|%s|,v=|%s|\n",formatString,v);
					textBuffer.append(String.format(formatString,v.toString(cp)));
				}
			}
		};
		return textBuffer;
	}
	//
	private static boolean is_format_c(String format) {
		// System.out.printf("is_format_c:>>>%s<<<\n",format);
		boolean isFormatString= false;
		int counter= 0;
		for (int i= 0; i < format.length(); i++) {
			// System.out.printf("%d: is_format_c:>>>%s<<<\n",i,format.substring(counter));
			if (counter < format.length()) {
				if (isFormatString) {
					if (format.codePointAt(counter)=='-') {
					} else if (Character.isDigit(format.codePointAt(counter))) {
					} else if (format.codePointAt(counter)=='.') {
					} else if (Character.toLowerCase(format.codePointAt(counter))=='c') {
						// System.out.printf("TRUE: is_format_c:>>>%s<<<\n",format);
						return true;
					} else {
						// System.out.printf("FALSE: is_format_c:>>>%s<<<\n",format);
						return false;
					}
				} else if (
					counter+1 < format.length() &&
					format.codePointAt(counter)=='%' &&
					format.codePointAt(counter+1)=='%') {
					counter= counter + 2;
					continue;
				} else if (format.codePointAt(counter)=='%') {
					isFormatString= true;
				}
			} else {
				// System.out.printf("ERROR: is_format_c:>>>%s<<<\n",format);
				return false;
			};
			counter++;
		};
		// System.out.printf("FALSE: is_format_c:>>>%s<<<\n",format);
		return false;
	}
	//
	public static String integerToString(long value) {
		return String.format("%d",value);
	}
	public static String integerToString(BigInteger value) {
		return String.format("%d",value);
	}
	//
	public static String realToString(double value) {
		return truncZeros(String.format("%G",value));
	}
	private static String truncZeros(String s1) {
		s1= s1.replace(',','.');
		int length= s1.length();
		int lastZeroPosition= 0;
		boolean hasZeroEnd= false;
		boolean hasPoint= false;
		for (int i= 0; i<length; i++) {
			int currentCharacter= s1.codePointAt(i);
			if (currentCharacter=='.') {
				hasPoint= true;
			} else if (currentCharacter=='e') {
				return s1;
			} else if (currentCharacter=='E') {
				return s1;
			}
		};
		if (hasPoint) {
			for (int i=length-1; i>=0; i--) {
				if (s1.codePointAt(i)=='0') {
					lastZeroPosition= i;
					hasZeroEnd= true;
				} else {
					break;
				}
			};
			if (	lastZeroPosition > 0 &&
				s1.codePointAt(lastZeroPosition-1)=='.') {
				lastZeroPosition= lastZeroPosition + 1;
			};
			if (	hasZeroEnd &&
				lastZeroPosition > 0 &&
				lastZeroPosition <= length-1) {
				return s1.substring(0,lastZeroPosition);
			}
		};
		return s1;
	}
	public static String encodeString(String text, boolean isSymbol, CharsetEncoder encoder) {
		int length= text.length();
		boolean containsSpecialCharacters= false;
		if (encoder!=null && !encoder.canEncode(text)) {
			containsSpecialCharacters= true;
		} else {
			for (int p1=0; p1 < length; p1++) {
				int c= text.codePointAt(p1);
				if (c >= 8 && c <= 13) {
					containsSpecialCharacters= true;
					break;
				} else if (c=='\\') {
					containsSpecialCharacters= true;
					break;
				// } else if (c >= 0xFE) {
				//	containsSpecialCharacters= true;
				//	break;
				} else if (c=='\'') {
					if (isSymbol) {
						containsSpecialCharacters= true;
						break;
					} else {
						continue;
					}
				} else if (c=='\"') {
					if (!isSymbol) {
						containsSpecialCharacters= true;
						break;
					} else {
						continue;
					}
				}
			}
		};
		if (containsSpecialCharacters) {
			StringBuilder buffer= new StringBuilder();
			int beginningOfSegment= 0;
			for (int p1=0; p1 < length; p1++) {
				// System.out.printf("p=%s; buffer=>>>%s<<<\n",p1,buffer.toString());
				int c= text.codePointAt(p1);
				if (c >= 8 && c <= 13) {
					buffer.append(text.substring(beginningOfSegment,p1));
					beginningOfSegment= p1 + 1;
					switch (c) {
					case 8:
						buffer.append("\\b");
						continue;
					case 9:
						buffer.append("\\t");
						continue;
					case 10:
						buffer.append("\\n");
						continue;
					case 11:
						buffer.append("\\v");
						continue;
					case 12:
						buffer.append("\\f");
						continue;
					case 13:
						buffer.append("\\r");
						continue;
					}
				} else if (c=='\\') {
					buffer.append(text.substring(beginningOfSegment,p1));
					beginningOfSegment= p1 + 1;
					buffer.append("\\\\");
					continue;
				} else if (c=='\'') {
					if (isSymbol) {
						buffer.append(text.substring(beginningOfSegment,p1));
						beginningOfSegment= p1 + 1;
						buffer.append("\\'");
					};
					continue;
				} else if (c=='\"') {
					if (!isSymbol) {
						buffer.append(text.substring(beginningOfSegment,p1));
						beginningOfSegment= p1 + 1;
						buffer.append("\\\"");
					};
					continue;
				} else if (encoder!=null && !encoder.canEncode(text.charAt(p1))) {
					buffer.append(text.substring(beginningOfSegment,p1));
					beginningOfSegment= p1 + 1;
					buffer.append("\\16#");
					buffer.append(String.format("%04X",(int)c));
					buffer.append("#");
					continue;
				}
			};
			buffer.append(text.substring(beginningOfSegment));
			return buffer.toString();
		} else {
			return text;
		}

	}
}
