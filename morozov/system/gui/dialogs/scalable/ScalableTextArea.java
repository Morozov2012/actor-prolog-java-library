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

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;

public class ScalableTextArea extends ActiveComponent {
	//
	public JTextArea area= null;
	protected boolean isEditable= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalableTextArea(AbstractDialog tD, String text, int visibleRowCount, int visibleColumnCount) {
		super(tD);
		JScrollPane scrollPane= new JScrollPane();
		component= scrollPane;
		area= new ATextArea(tD,this,text,visibleRowCount,visibleColumnCount);
		area.setEditable(isEditable);
		scrollPane.setViewportView(area);
		area.addFocusListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			if (isEditable) {
				throw RejectValue.instance;
			} else {
				return new PrologString("");
			}
		} else {
			return super.standardizeValue(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (area != null) {
			area.setText(value.toString(iX));
		}
	}
	//
	public Term getValue() {
		if (area != null) {
			return new PrologString(area.getText());
		} else {
			return new PrologString("");
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setIsEnabled(boolean mode) {
		if (component != null && area != null) {
			if (mode) {
				component.setEnabled(true);
				area.setEnabled(true);
			} else {
				component.setEnabled(false);
				area.setEnabled(false);
			}
		}
	}
	//
	public boolean isEnabled(boolean mode1) {
		if (component != null && area != null) {
			boolean mode2= component.isEnabled() && area.isEnabled();
			return mode1 == mode2;
		} else {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setEditable(boolean b) {
		isEditable= b;
		if (area != null) {
			area.setEditable(b);
		}
	}
	//
	public void setFont(Font font) {
		super.setFont(font);
		if (area != null) {
			area.setFont(font);
			// super.setGeneralFont(font);
			if (component != null) {
				JScrollPane scrollPane= (JScrollPane)component;
				scrollPane.setMinimumSize(scrollPane.getPreferredSize());
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setForeground(Color c) {
		if (area != null) {
			area.setForeground(c);
		}
	}
	//
	public void setBackground(Color c) {
		if (area != null) {
			area.setBackground(c);
		}
	}
}
