// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;

public class SymbolName {
	public String identifier;
	public boolean isSafe;
	public SymbolName(String name, boolean flag) {
		identifier= name;
		isSafe= flag;
	}
	public String extractClassName() {
		if (identifier.length() < 2) {
			return identifier;
		} else {
			char[] characters= identifier.toCharArray();
			int textLength= characters.length;
			// for (int n=0; n < textLength; n++) {
			//	System.out.printf("%d) %d %c\n",n,(int)characters[n],characters[n]);
			// }
			if (characters[0]==20) {
				for (int n=0; n < textLength; n++) {
					if (characters[n]==21) {
						if (textLength-n-1 > 0) {
							return new String(characters,n+1,textLength-n-1);
						} else {
							return "";
						}
					}
				}
			};
			return identifier;
		}
	}
	public String toString(CharsetEncoder encoder) {
		if (isSafe) {
			return identifier;
		} else {
			return "'" + FormatOutput.encodeString(identifier,true,encoder) + "'";
		}
	}
	public String toString() {
		if (isSafe) {
			return identifier;
		} else {
			return "'" + FormatOutput.encodeString(identifier,true,null) + "'";
		}
	}
}
