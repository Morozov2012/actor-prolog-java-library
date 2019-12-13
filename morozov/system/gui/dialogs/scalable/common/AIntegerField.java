/*
 * @(#)IntegerField.java 1.0 2009/11/21
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * IntegerField implementation for the Actor Prolog language
 * @version 1.0 2009/11/21
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class AIntegerField extends ATextField {
	//
	public AIntegerField(AbstractDialog tD, ActiveComponent tC, String text, int columns) {
		super(tD,tC,text,columns);
		setFormat(ActiveDocumentFormat.INTEGER);
	}
	//
	@Override
	public Term getValue() {
		ActivePlainDocument activeDocument= (ActivePlainDocument)getDocument();
		try {
			String text= activeDocument.getTextOrBacktrack();
			BigInteger number= GeneralConverters.stringToStrictInteger(text);
			return new PrologInteger(number);
		} catch (Backtracking b) {
			return new PrologString(activeDocument.getText());
		} catch (TermIsNotAnInteger e) {
			return new PrologString(activeDocument.getText());
		}
	}
}
