/*
 * @(#)ScalableRadioButton.java 1.0 2009/12/11
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.terms.*;

import java.awt.Font;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.plaf.basic.BasicRadioButtonUI;

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
		model.addActionListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void putValue(Term value, ChoisePoint iX) {
	}
	//
	@Override
	public Term getValue() {
		return PrologUnknownValue.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setIndividualText(Term value, ChoisePoint iX) {
		AnnotatedButton.safelyUpdateAbstractButton((AbstractButton)component,value,iX);
		targetDialog.safelyRevalidateAndRepaint();
	}
	//
	@Override
	public Term getIndividualText() {
		if (component!=null) {
			String text= AnnotatedButton.safelyRestoreText((AbstractButton)component);
			return new PrologString(text);
		} else {
			return termEmptyString;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	//
	@Override
	public void setBackground(Color c) {
		super.setBackground(c);
		BasicRadioButtonUI ui= (BasicRadioButtonUI)((JRadioButton)component).getUI();
		Icon defaultIcon= ui.getDefaultIcon();
		if (defaultIcon!=null) {
			if (defaultIcon instanceof ScalableToggleButtonIcon) {
				ScalableToggleButtonIcon scalableIcon= (ScalableToggleButtonIcon)defaultIcon;
				scalableIcon.setBackground(component,c);
			}
		}
	}
}
