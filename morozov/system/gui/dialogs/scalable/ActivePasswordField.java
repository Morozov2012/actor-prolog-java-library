/*
 * @(#)ActivePasswordField.java 1.0 2010/03/09
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ActivePasswordField implementation for the Actor Prolog language
 * @version 1.0 2010/03/09
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.Color;

public class ActivePasswordField extends ActiveComponent {
	//
	protected boolean isEditable= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public ActivePasswordField(AbstractDialog target, int columns) {
		this(target,"",columns);
	}
	public ActivePasswordField(AbstractDialog target, String text, int columns) {
		super(target);
		APasswordField textFiled= new APasswordField(target,this,text,columns);
		component= textFiled;
		textFiled.setEditable(isEditable);
		textFiled.addFocusListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw RejectValue.instance;
		} else {
			return super.standardizeValue(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			((APasswordField)component).setText(value.toString(iX));
		}
	}
	//
	public Term getValue() {
		if (component!=null) {
			return new PrologString(new String(((APasswordField)component).getPassword()));
		} else {
			return PrologUnknownValue.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setGeneralForeground(Color c) {
	}
	public void setGeneralBackground(Color c) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setEditable(boolean b) {
		isEditable= b;
		if (component!=null) {
			((APasswordField)component).setEditable(b);
		}
	}
	//
	public void setHorizontalAlignment(int alignment) {
		if (component!=null) {
			((APasswordField)component).setHorizontalAlignment(alignment);
		}
	}
}
