/*
 * @(#)ScalableTextArea.java 1.0 2009/12/29
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableTextArea implementation for the Actor Prolog language
 * @version 1.0 2009/12/29
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.terms.*;

import javax.swing.*;
import java.awt.Font;

public class ScalableTextArea extends ActiveComponent {
	//
	public JTextArea area= null;
	protected boolean isEditable= false;
	//
	public ScalableTextArea(AbstractDialog tD, String text, int visibleRowCount, int visibleColumnCount) {
		super(tD);
		component= new JScrollPane();
		area= new ATextArea(tD,this,text,visibleRowCount,visibleColumnCount);
		area.setEditable(isEditable);
		((JScrollPane)component).setViewportView(area);
	}
	//
	// protected int getInitialTopBorder() {return 5;}
	// protected int getInitialLeftBorder() {return 5;}
	// protected int getInitialBottomBorder() {return 5;}
	// protected int getInitialRightBorder() {return 5;}
	//
	public void setEditable(boolean b) {
		isEditable= b;
		if (area!=null) {
			area.setEditable(b);
		}
	}
	//
	public void setFont(Font font) {
		super.setFont(font);
		if (area!=null) {
			area.setFont(font);
			super.setFont(font);
			if (component!=null) {
				((JScrollPane)component).setMinimumSize(((JScrollPane)component).getPreferredSize());
			}
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (area!=null) {
			area.setText(value.toString(iX));
		}
	}
	public Term getValue() {
		if (area!=null) {
			return new PrologString(area.getText());
		} else {
			return new PrologString("");
		}
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			if (isEditable) {
				throw new RejectValue();
			} else {
				return new PrologString("");
			}
		} else {
			return super.standardizeValue(value,iX);
		}
	}
}
