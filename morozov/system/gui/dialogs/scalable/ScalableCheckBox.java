/*
 * @(#)ScalableCheckBox.java 1.0 2009/12/11
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import target.*;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.Icon;
import javax.swing.Action;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.JCheckBox;

import java.awt.Font;
import java.awt.Color;

public class ScalableCheckBox extends ScalableAbstractButton {
	//
	public ScalableCheckBox(AbstractDialog tD) {
		this(tD,null,null,false);
	}
	public ScalableCheckBox(AbstractDialog tD, Icon icon) {
		this(tD,null,icon,false);
	}
	public ScalableCheckBox(AbstractDialog tD, Icon icon, boolean selected) {
		this(tD,null,icon,selected);
	}
	public ScalableCheckBox(AbstractDialog tD, String text) {
		this(tD,text,null,false);
	}
	public ScalableCheckBox(AbstractDialog tD, String text, boolean selected) {
		this(tD,text,null,selected);
	}
	public ScalableCheckBox(AbstractDialog tD, String text, Icon icon) {
		this(tD,text,icon,false);
	}
	public ScalableCheckBox(AbstractDialog tD, Action a) {
		this(tD,null,null,false);
		if (component!=null) {
			((JCheckBox)component).setAction(a);
		}
	}
	public ScalableCheckBox(AbstractDialog tD, String text, Icon icon, boolean selected) {
		super(tD);
		component= new JCheckBox(text,icon,selected);
		ScalableToggleButtonModel model= new ScalableToggleButtonModel(selected,0,text);
		((JCheckBox)component).setModel(model);
		// ((JCheckBox)component).addChangeListener(this);
		((JCheckBox)component).addActionListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_yes) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_yes);
			} else if (code==SymbolCodes.symbolCode_E_no) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_no);
			} else if (code==SymbolCodes.symbolCode_E_unknown) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_unknown);
			} else {
				// return new PrologSymbol(SymbolCodes.symbolCode_E_unknown);
				throw RejectValue.instance;
			}
		} catch (TermIsNotASymbol e1) {
			try {
				String text= value.getStringValue(iX);
				if (text.equalsIgnoreCase("yes")) {
					return new PrologSymbol(SymbolCodes.symbolCode_E_yes);
				} else if (text.equalsIgnoreCase("no")) {
					return new PrologSymbol(SymbolCodes.symbolCode_E_no);
				} else if (text.equalsIgnoreCase("unknown")) {
					return new PrologSymbol(SymbolCodes.symbolCode_E_unknown);
				} else {
					// return new PrologSymbol(SymbolCodes.symbolCode_E_unknown);
					throw RejectValue.instance;
				}
			} catch (TermIsNotAString e2) {
				// return new PrologSymbol(SymbolCodes.symbolCode_E_unknown);
				throw RejectValue.instance;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_yes) {
					if (isUncertain()) {
						((JCheckBox)component).setSelected(true);
						setUncertain(false);
					} else {
						((JCheckBox)component).setSelected(true);
					}
				} else if (code==SymbolCodes.symbolCode_E_no) {
					if (isUncertain()) {
						((JCheckBox)component).setSelected(false);
						setUncertain(false);
					} else {
						((JCheckBox)component).setSelected(false);
					}
				} else {
					setUncertain(true);
				}
			} catch (TermIsNotASymbol e1) {
				try {
					String text= value.getStringValue(iX);
					if (text.equalsIgnoreCase("yes")) {
						if (isUncertain()) {
							((JCheckBox)component).setSelected(true);
							setUncertain(false);
						} else {
							((JCheckBox)component).setSelected(true);
						}
					} else if (text.equalsIgnoreCase("no")) {
						if (isUncertain()) {
							((JCheckBox)component).setSelected(false);
							setUncertain(false);
						} else {
							((JCheckBox)component).setSelected(false);
						}
					} else {
						setUncertain(true);
					}
				} catch (TermIsNotAString e2) {
					setUncertain(true);
				}
			}
			// if (targetDialog!=null) {
			//	targetDialog.reportValueUpdate(this);
			// }
		}
	}
	//
	public Term getValue() {
		if (component!=null) {
			if (isUncertain()) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_unknown);
			} else {
				if (((JCheckBox)component).isSelected()) {
					return new PrologSymbol(SymbolCodes.symbolCode_E_yes);
				} else {
					return new PrologSymbol(SymbolCodes.symbolCode_E_no);
				}
			}
		} else {
			return PrologUnknownValue.instance;
			// return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setAlarmColors(Color fc, Color bc) {
		BasicRadioButtonUI ui= (BasicRadioButtonUI)((JCheckBox)component).getUI();
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
	public void setIndividualText(Term value, ChoisePoint iX) {
		AnnotatedButton.safelyUpdateAbstractButton((JCheckBox)component,value,iX);
		targetDialog.safelyRevalidateAndRepaint();
	}
	//
	public Term getIndividualText() {
		if (component!=null) {
			String text= AnnotatedButton.safelyRestoreText((JCheckBox)component);
			return new PrologString(text);
		} else {
			return termEmptyString;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setFont(Font font) {
		super.setFont(font);
		if (component!=null) {
			// ((JCheckBox)component).setFont(font);
			BasicRadioButtonUI ui= (BasicRadioButtonUI)((JCheckBox)component).getUI();
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
	///////////////////////////////////////////////////////////////
	//
	public boolean hasClassicStyle() {
		if (component!=null) {
			ScalableToggleButtonModel model= (ScalableToggleButtonModel)((JCheckBox)component).getModel();
			return model.hasClassicStyle();
		} else {
			return false;
		}
	}
	//
	public void setHasClassicStyle(boolean flag) {
		if (component!=null) {
			ScalableToggleButtonModel model= (ScalableToggleButtonModel)((JCheckBox)component).getModel();
			model.setHasClassicStyle(flag);
		}
	}
}
