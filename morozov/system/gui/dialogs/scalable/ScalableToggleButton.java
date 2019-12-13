/*
 * @(#)ScalableToggleButton.java 1.0 2018/07/28
 *
 * Copyright 2018 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import target.*;

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.Icon;
import javax.swing.Action;
import javax.swing.JToggleButton;
import java.awt.event.ActionEvent;

import java.awt.Font;
import java.awt.Color;

public class ScalableToggleButton extends ScalableButton {
	//
	protected Color failureForegroundColor;
	protected Color failureBackgroundColor;
	//
	public ScalableToggleButton(AbstractDialog tD) {
		this(tD,null,null,false);
	}
	public ScalableToggleButton(AbstractDialog tD, Action a) {
		this(tD,null,null,false);
		if (component!=null) {
			((JToggleButton)component).setAction(a);
		}
	}
	public ScalableToggleButton(AbstractDialog tD, Icon icon) {
		this(tD,null,icon,false);
	}
	public ScalableToggleButton(AbstractDialog tD, Icon icon, boolean selected) {
		this(tD,null,icon,selected);
	}
	public ScalableToggleButton(AbstractDialog tD, String text) {
		this(tD,text,null,false);
	}
	public ScalableToggleButton(AbstractDialog tD, String text, boolean selected) {
		this(tD,text,null,selected);
	}
	public ScalableToggleButton(AbstractDialog tD, String text, Icon icon) {
		this(tD,text,icon,false);
	}
	public ScalableToggleButton(AbstractDialog tD, String text, Icon icon, boolean selected) {
		super(tD);
		component= new JToggleButton(text,icon,selected);
		ScalableToggleButtonModel model= new ScalableToggleButtonModel(selected,0,text);
		((JToggleButton)component).setModel(model);
		((JToggleButton)component).addActionListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
					throw RejectValue.instance;
				}
			} catch (TermIsNotAString e2) {
				throw RejectValue.instance;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_yes) {
					if (isUncertain()) {
						((JToggleButton)component).setSelected(true);
						setUncertain(false);
					} else {
						((JToggleButton)component).setSelected(true);
					}
				} else if (code==SymbolCodes.symbolCode_E_no) {
					if (isUncertain()) {
						((JToggleButton)component).setSelected(false);
						setUncertain(false);
					} else {
						((JToggleButton)component).setSelected(false);
					}
				} else {
					setUncertain(true);
				}
			} catch (TermIsNotASymbol e1) {
				try {
					String text= value.getStringValue(iX);
					if (text.equalsIgnoreCase("yes")) {
						if (isUncertain()) {
							((JToggleButton)component).setSelected(true);
							setUncertain(false);
						} else {
							((JToggleButton)component).setSelected(true);
						}
					} else if (text.equalsIgnoreCase("no")) {
						if (isUncertain()) {
							((JToggleButton)component).setSelected(false);
							setUncertain(false);
						} else {
							((JToggleButton)component).setSelected(false);
						}
					} else {
						setUncertain(true);
					}
				} catch (TermIsNotAString e2) {
					setUncertain(true);
				}
			};
			targetDialog.safelyInvalidateAndRepaint();
		}
	}
	//
	@Override
	public Term getValue() {
		if (component!=null) {
			if (isUncertain()) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_unknown);
			} else {
				if (((JToggleButton)component).isSelected()) {
					return new PrologSymbol(SymbolCodes.symbolCode_E_yes);
				} else {
					return new PrologSymbol(SymbolCodes.symbolCode_E_no);
				}
			}
		} else {
			return PrologUnknownValue.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setAlarmColors(Color fc, Color bc) {
		super.setAlarmColors(fc,bc);
		failureForegroundColor= fc;
		failureBackgroundColor= bc;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setUncertain(boolean flag) {
		boolean doCheckState= (isUncertain() != flag);
		super.setUncertain(flag);
		if (doCheckState) {
			checkUncertainState();
		}
	}
	//
	@Override
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		checkUncertainState();
	}
	//
	public void checkUncertainState() {
		if (isUncertain()) {
			setSupervisoryTextColor(failureForegroundColor);
			setSupervisoryBackgroundColor(failureBackgroundColor);
		} else {
			setSupervisoryTextColor(null);
			setSupervisoryBackgroundColor(null);
		};
		resetGeneralFont();
		resetGeneralBackground();
		targetDialog.safelyRevalidateAndRepaint();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setFont(Font font) {
		super.setFont(font);
	}
}
