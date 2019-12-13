/*
 * @(#)ActiveTextField.java 1.0 2009/11/23
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ActiveTextField implementation for the Actor Prolog language
 * @version 1.0 2009/11/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.Color;

public class ActiveTextField extends ActiveComponent {
	//
	protected boolean isEditable= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public ActiveTextField(AbstractDialog target, int columns) {
		this(target,"",columns);
	}
	public ActiveTextField(AbstractDialog target, String text, int columns) {
		super(target);
		ATextField textFiled= new ATextField(target,this,text,columns);
		component= textFiled;
		textFiled.setEditable(isEditable);
		textFiled.addFocusListener(this);
	}
	public ActiveTextField(AbstractDialog target) {
		super(target);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			((ATextField)component).setText(value.toString(iX));
		}
	}
	//
	@Override
	public Term getValue() {
		if (component!=null) {
			return ((ATextField)component).getValue();
		} else {
			return PrologUnknownValue.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setGeneralForeground(Color c) {
	}
	@Override
	public void setGeneralBackground(Color c) {
	}
	//
	@Override
	public void setAlarmColors(Color fc, Color bc) {
		if (component!=null) {
			((ATextField)component).setAlarmColors(fc,bc);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setEditable(boolean b) {
		isEditable= b;
		if (component!=null) {
			((ATextField)component).setEditable(b);
		}
	}
	//
	public void setHorizontalAlignment(int alignment) {
		if (component!=null) {
			((ATextField)component).setHorizontalAlignment(alignment);
		}
	}
}
