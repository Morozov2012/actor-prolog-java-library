/*
 * @(#)ScalableRadioButton.java 1.0 2009/12/11
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.terms.*;

import java.awt.Font;
import java.awt.Color;
import javax.swing.*;
import javax.swing.plaf.basic.*;

public class ScalableRadioButton extends ScalableAbstractButton {
	//
	public ScalableRadioButton(AbstractDialog tD, long buttonNumber) {
		this(tD,null,null,false,buttonNumber);
	}
	public ScalableRadioButton(AbstractDialog tD, Icon icon, long buttonNumber) {
		this(tD,null,icon,false,buttonNumber);
	}
	public ScalableRadioButton(AbstractDialog tD, Icon icon, boolean selected, long buttonNumber) {
		this(tD,null,icon,selected,buttonNumber);
	}
	public ScalableRadioButton(AbstractDialog tD, String text, long buttonNumber) {
		this(tD,text,null,false,buttonNumber);
	}
	public ScalableRadioButton(AbstractDialog tD, String text, boolean selected, long buttonNumber) {
		this(tD,text,null,selected,buttonNumber);
	}
	public ScalableRadioButton(AbstractDialog tD, String text, Icon icon, long buttonNumber) {
		this(tD,text,icon,false,buttonNumber);
	}
	public ScalableRadioButton(AbstractDialog tD, Action a, long buttonNumber) {
		this(tD,null,null,false,buttonNumber);
		if (component!=null) {
			((JRadioButton)component).setAction(a);
		}
	}
	public ScalableRadioButton(AbstractDialog tD, String text, Icon icon, boolean selected, long buttonNumber) {
		super(tD);
		component= new JRadioButton(text,icon,selected);
		ScalableToggleButtonModel model= new ScalableToggleButtonModel(buttonNumber,text);
		((JRadioButton)component).setModel(model);
		// model.addChangeListener(this);
		model.addActionListener(this);
	}
	//
	// protected int getInitialTopBorder() {return 5;}
	// protected int getInitialLeftBorder() {return 18;}
	// protected int getInitialBottomBorder() {return 5;}
	// protected int getInitialRightBorder() {return 18;}
	//
	public void setFont(Font font) {
		super.setFont(font);
		if (component!=null) {
			BasicRadioButtonUI ui= (BasicRadioButtonUI)((JRadioButton)component).getUI();
			Icon defaultIcon= ui.getDefaultIcon();
			if (defaultIcon!=null) {
				if (defaultIcon instanceof ScalableToggleButtonIcon) {
					ScalableToggleButtonIcon scalableIcon= (ScalableToggleButtonIcon)defaultIcon;
					scalableIcon.setFont(component,font,horizontalScaling);
				}
			}
		}
	}
	public void setBackground(Color c) {
		BasicRadioButtonUI ui= (BasicRadioButtonUI)((JRadioButton)component).getUI();
		Icon defaultIcon= ui.getDefaultIcon();
		if (defaultIcon!=null) {
			if (defaultIcon instanceof ScalableToggleButtonIcon) {
				ScalableToggleButtonIcon scalableIcon= (ScalableToggleButtonIcon)defaultIcon;
				scalableIcon.setBackground(component,c);
			}
		};
		super.setBackground(c);
	}
	public void setAlarmColors(Color fc, Color bc) {
		BasicRadioButtonUI ui= (BasicRadioButtonUI)((JRadioButton)component).getUI();
		Icon defaultIcon= ui.getDefaultIcon();
		if (defaultIcon!=null) {
			if (defaultIcon instanceof ScalableToggleButtonIcon) {
				ScalableToggleButtonIcon scalableIcon= (ScalableToggleButtonIcon)defaultIcon;
				scalableIcon.setAlarmColors(fc,bc);
			}
		}
	}
	// public void stateChanged(ChangeEvent event) {
	// }
	public void putValue(Term value, ChoisePoint iX) {
	}
	public Term getValue() {
		return new PrologUnknownValue();
	}
}
