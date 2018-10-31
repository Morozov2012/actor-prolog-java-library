/*
 * @(#)RealField.java 1.0 2010/03/09
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * RealField implementation for the Actor Prolog language
 * @version 1.0 2010/03/09
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class RealField extends ActiveTextField {
	public RealField(AbstractDialog target, int columns) {
		this(target,"",columns);
	}
	public RealField(AbstractDialog target, String text, int columns) {
		super(target);
		component= new ARealField(target,this,text,columns);
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		try {
			double number= GeneralConverters.termToReal(value,iX);
			return new PrologReal(number);
		} catch (TermIsNotAReal e) {
			throw RejectValue.instance;
		}
	}
}
