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

import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.terms.*;

import java.awt.Font;
import java.awt.Color;

public class ActiveTextField extends ActiveComponent {
	//
	protected boolean isEditable= false;
	//
	public ActiveTextField(AbstractDialog target, int columns) {
		this(target,"",columns);
	}
	public ActiveTextField(AbstractDialog target, String text, int columns) {
		super(target);
		component= new ATextField(target,this,text,columns);
		((ATextField)component).setEditable(isEditable);
	}
	public ActiveTextField(AbstractDialog target) {
		super(target);
	}
	//
	// protected int getInitialTopBorder() {return 5;}
	// protected int getInitialLeftBorder() {return 5;}
	// protected int getInitialBottomBorder() {return 5;}
	// protected int getInitialRightBorder() {return 5;}
	//
	public void setEditable(boolean b) {
		isEditable= b;
		if (component!=null) {
			((ATextField)component).setEditable(b);
		}
	}
	public void setFont(Font font) {
		if (component!=null) {
			component.setFont(font);
		};
		super.setFont(font);
	}
	public void setHorizontalAlignment(int alignment) {
		if (component!=null) {
			((ATextField)component).setHorizontalAlignment(alignment);
		}
	}
	public void setBackground(Color c) {
	}
	public void setForeground(Color c) {
	}
	public void setAlarmColors(Color fc, Color bc) {
		if (component!=null) {
			((ATextField)component).setAlarmColors(fc,bc);
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			((ATextField)component).setText(value.toString(iX));
		}
	}
	public Term getValue() {
		if (component!=null) {
			return ((ATextField)component).getValue();
		} else {
			return new PrologUnknownValue();
		}
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw new RejectValue();
		} else {
			return super.standardizeValue(value,iX);
		}
	}
}
