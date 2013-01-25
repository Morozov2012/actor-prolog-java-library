// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.files;

import target.*;

import morozov.terms.*;

public class ExtendedFileName {
	public boolean isSystemFile= false;
	public String textName= "";
	public SystemFileName systemName= SystemFileName.STDOUT;
	public ExtendedFileName(String name) {
		textName= name;
	}
	public ExtendedFileName(SystemFileName name) {
		systemName= name;
		isSystemFile= true;
	}
	public Term toTerm() {
		if (!isSystemFile) {
			return new PrologString(textName);
		} else {
			long name;
			if (systemName==SystemFileName.STDIN) {
				name= SymbolCodes.symbolCode_E_stdin;
			} else if (systemName==SystemFileName.STDOUT) {
				name= SymbolCodes.symbolCode_E_stdout;
			} else {
				name= SymbolCodes.symbolCode_E_stderr;
			};
			return new PrologSymbol(name);
		}
	}
	public String toString() {
		if (!isSystemFile) {
			return textName;
		} else {
			long name;
			if (systemName==SystemFileName.STDIN) {
				name= SymbolCodes.symbolCode_E_stdin;
			} else if (systemName==SystemFileName.STDOUT) {
				name= SymbolCodes.symbolCode_E_stdout;
			} else {
				name= SymbolCodes.symbolCode_E_stderr;
			};
			return SymbolNames.retrieveSymbolName(name).toString();
		}
	}
}
