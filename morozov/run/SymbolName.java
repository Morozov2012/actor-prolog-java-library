// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.system.*;

import java.io.Serializable;
import java.nio.charset.CharsetEncoder;

public class SymbolName implements Serializable {
	//
	public String identifier;
	public boolean isSafe;
	//
	private static final long serialVersionUID= 0xB79D129E6929A518L; // -5215992321846696680L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","SymbolName");
	// }
	//
	public SymbolName(String name, boolean flag) {
		identifier= name;
		isSafe= flag;
	}
	//
	public String extractClassName() {
		if (identifier.length() < 2) {
			return identifier;
		} else {
			char[] characters= identifier.toCharArray();
			int textLength= characters.length;
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
	public String toSafeString(CharsetEncoder encoder) {
		if (isSafe) {
			return FormatOutput.encodeString(identifier,true,encoder);
		} else {
			return "'" + FormatOutput.encodeString(identifier,true,encoder) + "'";
		}
	}
	public String toSafeString() {
		if (isSafe) {
			return identifier;
		} else {
			return "'" + FormatOutput.encodeString(identifier,true,null) + "'";
		}
	}
	public String toRawString(CharsetEncoder encoder) {
		return FormatOutput.encodeString(identifier,true,encoder);
	}
}
