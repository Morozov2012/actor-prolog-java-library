/*
 * @(#)FileMask.java 1.0 2010/11/11
 *
 * (c) 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.files;

/*
 * This file filter accepts file patterns containing
 * special wildcards:
 * "*?" on Windows and
 * "*?[]" on Unix.
*/

import javax.swing.filechooser.FileFilter;
import java.util.regex.Pattern;

public class FileNameMask extends FileFilter {
	protected String textDescription;
	protected String[] fileNameMasks;
	protected String firstExtension= "";
	private Pattern[] patterns;
	public FileNameMask(String description, String requiredPatterns) {
		this(description, new String[] {requiredPatterns});
	}
	public FileNameMask(String description, String[] requiredPatterns) {
		textDescription= description;
		fileNameMasks= requiredPatterns;
		if (fileNameMasks.length > 0) {
			String firstMask= fileNameMasks[0];
			firstExtension= extractExtension(firstMask);
		};
		int length= requiredPatterns.length;
		patterns= new Pattern[length];
		for (int n=0; n < length; n++) {
			patterns[n]= wildcard2pattern(requiredPatterns[n]);
		}
	}
	protected String extractExtension(String name) {
		int p1= name.lastIndexOf('.');
		if (p1 >= 0) {
			name= name.substring(p1);
		};
		int p2= name.lastIndexOf(']');
		if (p2 >= 0) {
			name= name.substring(0,p2);
		};
		int p3= name.lastIndexOf('[');
		if (p3 >= 0) {
			name= name.substring(0,p3);
		};
		int p4= name.lastIndexOf('*');
		if (p4 >= 0) {
			return "";
		};
		int p5= name.lastIndexOf('?');
		if (p5 >= 0) {
			return "";
		};
		return name;
	}
	//
	public boolean accept(java.io.File f) {
		if (f==null) {
			return false;
		} else if (f.isDirectory()) {
			return true;
		} else {
			String fileName= f.getName();
			for (int n=0; n < patterns.length; n++) {
				if (patterns[n].matcher(fileName).matches()) {
					return true;
				}
			};
			return false;
		}
	}
	public String getDescription() {
		return textDescription;
	}
	public boolean containsWildcard(String wildcard) {
		if (textDescription.equals(wildcard)) {
			return true;
		} else {
			for (int n=0; n < fileNameMasks.length; n++) {
				if (fileNameMasks[n].equals(wildcard)) {
					return true;
				}
			};
			return false;
		}
	}
	//
	public static Pattern wildcard2pattern(String textPattern) {
		char[] nativePattern= textPattern.toCharArray();
		boolean isWin32= (java.io.File.separatorChar=='\\');
		if (isWin32) {
			return wildcard2WinPattern(nativePattern);
		} else {
			return wildcard2UnixPattern(nativePattern);
		}
	}
	public static Pattern wildcard2WinPattern(char[] nativePattern) {
		char[] regularPattern= new char[nativePattern.length * 2];
		int j= 0;
		// On windows a pattern ending with *.*
		// is equal to ending with *
		int length= nativePattern.length;
		if (	length >= 3 &&
			nativePattern[length-3]=='*' &&
			nativePattern[length-2]=='.' &&
			nativePattern[length-1]=='*' ) {
			length= length - 2;
		};
		for (int i= 0; i < length; i++) {
			switch(nativePattern[i]) {
			case '\\':
				regularPattern[j++]= '\\';
				regularPattern[j++]= '\\';
				break;
			case '?':
				regularPattern[j++]= '.';
				break;
			case '*':
				regularPattern[j++]= '.';
				regularPattern[j++]= '*';
				break;
			default:
				// Disabled characters: "+()^$.{}[]"
				if (!Character.isLetterOrDigit(nativePattern[i])) {
					regularPattern[j++]= '\\';
				};
				regularPattern[j++]= nativePattern[i];
				break;
			}
		};
		return Pattern.compile(new String(regularPattern,0,j),Pattern.CASE_INSENSITIVE);
	}
	public static Pattern wildcard2UnixPattern(char[] nativePattern) {
		char[] regularPattern= new char[nativePattern.length * 2];
		int j= 0;
		boolean inBrackets= false;
		for (int i= 0; i < nativePattern.length; i++) {
			switch(nativePattern[i]) {
			case '\\':
				if (i==0 && nativePattern.length > 1 && nativePattern[1]=='~') {
					regularPattern[j++]= nativePattern[++i];
				} else {
					regularPattern[j++]= '\\';
					if (i < nativePattern.length-1 && "*?[]".indexOf(nativePattern[i+1]) >= 0) {
						regularPattern[j++]= nativePattern[++i];
					} else {
						regularPattern[j++]= '\\';
					}
				};
				break;
			case '?':
				regularPattern[j++]= inBrackets ? '?' : '.';
				break;
			case '*':
				if (!inBrackets) {
					regularPattern[j++]= '.';
				};
				regularPattern[j++]= '*';
				break;
			case '[':
				inBrackets= true;
				regularPattern[j++]= nativePattern[i];
				if (i < nativePattern.length-1) {
					switch (nativePattern[i+1]) {
					case '!':
					case '^':
						regularPattern[j++]= '^';
						i++;
						break;
					case ']':
						regularPattern[j++]= nativePattern[++i];
						break;
					}
				};
				break;
			case ']':
				regularPattern[j++]= nativePattern[i];
				inBrackets= false;
				break;
			default:
				// Disabled characters: "+()|^$.{}<>"
				if (!Character.isLetterOrDigit(nativePattern[i])) {
					regularPattern[j++]= '\\';
				};
				regularPattern[j++]= nativePattern[i];
				break;
			}
		};
		return Pattern.compile(new String(regularPattern,0,j),Pattern.CASE_INSENSITIVE);
	}
	//
	public String appendExtensionIfNecessary(String name) {
		if (firstExtension.length() > 0) {
			int p1= name.lastIndexOf('.');
			if (p1 < 0) {
				name= name + firstExtension;
			}
		};
		return name;
	}
}
