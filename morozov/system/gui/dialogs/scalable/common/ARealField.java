/*
 * @(#)RealField.java 1.0 2010/03/09
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * RealField implementation for the Actor Prolog language
 * @version 1.0 2010/03/09
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ARealField extends ATextField {
	//
	public ARealField(AbstractDialog tD, ActiveComponent tC, String text, int columns) {
		super(tD,tC,text,columns);
		setFormat(ActiveDocumentFormat.REAL);
	}
	//
	@Override
	public Term getValue() {
		ActivePlainDocument activeDocument= (ActivePlainDocument)getDocument();
		try {
			String text= activeDocument.getTextOrBacktrack();
			double number= GeneralConverters.stringToReal(text);
			return new PrologReal(number);
		} catch (Backtracking b) {
			return new PrologString(activeDocument.getText());
		} catch (TermIsNotAReal e) {
			return new PrologString(activeDocument.getText());
		}
	}
}
