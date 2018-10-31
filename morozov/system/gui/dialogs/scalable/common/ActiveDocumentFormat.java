/*
 * @(#)ActiveDocumentFormat.java 1.0 2011/03/08
 *
 * Copyright 2011 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import morozov.system.converters.*;
import morozov.terms.signals.*;

public enum ActiveDocumentFormat {
	INTEGER {
		boolean verify(String text) {
			try {
				GeneralConverters.stringToStrictInteger(text);
				return true;
			} catch (TermIsNotAnInteger e) {
				return false;
			}
		}
	},
	REAL {
		boolean verify(String text) {
			try {
				GeneralConverters.stringToReal(text);
				return true;
			} catch (TermIsNotAReal e) {
				return false;
			}
		}
	},
	TEXT {
		boolean verify(String text) {
			return true;
		}
	};
	abstract boolean verify(String text);
	// abstract String format(Number number);
}
