/*
 * @(#)ScalableButtonGroup.java 1.0 2010/03/08
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import morozov.system.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.terms.*;

import javax.swing.ButtonModel;
import javax.swing.AbstractButton;

import java.math.BigInteger;
import java.util.Enumeration;

public class ScalableButtonGroup extends ActiveComponent {
	//
	public AButtonGroup buttonGroup;
	//
	public ScalableButtonGroup(AbstractDialog tD) {
		super(tD);
		buttonGroup= new AButtonGroup(tD,this);
	}
	//
	// protected int getInitialTopBorder() {return 0;}
	// protected int getInitialLeftBorder() {return 0;}
	// protected int getInitialBottomBorder() {return 0;}
	// protected int getInitialRightBorder() {return 0;}
	//
	public void add(AbstractButton b) {
		if (buttonGroup!=null) {
			buttonGroup.add(b);
		}
	}
	//
	public void setSelected(ButtonModel m, boolean b) {
		if (buttonGroup!=null) {
			buttonGroup.setSelected(m,b);
		}
	}
	//
	public void setUncertain() {
		if (buttonGroup!=null) {
			Enumeration<AbstractButton> buttons= buttonGroup.getElements();
			while (buttons.hasMoreElements()) {
				AbstractButton currentButton= buttons.nextElement();
				ScalableToggleButtonModel model= (ScalableToggleButtonModel)currentButton.getModel();
				model.setUncertain(true);
				currentButton.repaint();
			}
		}
		// if (targetDialog!=null) {
		//	targetDialog.reportValueUpdate(this);
		// }
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (buttonGroup!=null) {
			try {
				long number= value.getLongIntegerValue(iX);
				Enumeration<AbstractButton> buttons= buttonGroup.getElements();
				while (buttons.hasMoreElements()) {
					AbstractButton currentButton= buttons.nextElement();
					ScalableToggleButtonModel model= (ScalableToggleButtonModel)currentButton.getModel();
					long buttonNumber= model.getNumber();
					if (buttonNumber==number) {
						buttonGroup.setSelected(model,true);
						return;
					}
				};
				setUncertain();
			} catch (TermIsNotAnInteger e1) {
				try {
					String label= value.getStringValue(iX);
					Enumeration<AbstractButton> buttons= buttonGroup.getElements();
					while (buttons.hasMoreElements()) {
						AbstractButton currentButton= buttons.nextElement();
						ScalableToggleButtonModel model= (ScalableToggleButtonModel)currentButton.getModel();
						String buttonText= model.getText();
						if (buttonText.equals(label)) {
							buttonGroup.setSelected(model,true);
							return;
						}
					};
					setUncertain();
				} catch (TermIsNotAString e2) {
					setUncertain();
				}
			}
		}
	}
	//
	public Term getValue() {
		if (buttonGroup!=null) {
			Enumeration<AbstractButton> buttons= buttonGroup.getElements();
			while (buttons.hasMoreElements()) {
				AbstractButton currentButton= buttons.nextElement();
				ScalableToggleButtonModel model= (ScalableToggleButtonModel)currentButton.getModel();
				// System.out.printf("ScalableButtonGroup::2::getValue(%s)\n",model);
				if (model.isUncertain()) {
					return new PrologInteger(0);
				} else if (model.isSelected()) {
					String buttonText= model.getText();
					return new PrologString(buttonText);
				}
			};
			return new PrologInteger(0);
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
			try {
				BigInteger bigInteger= value.getIntegerValue(iX);
				if (PrologInteger.isSmallInteger(bigInteger)) {
					return new PrologInteger(bigInteger);
				} else {
					throw new RejectValue();
				}
			} catch (TermIsNotAnInteger e1) {
				try {
					double number= StrictMath.round(value.getRealValue(iX));
					BigInteger bigInteger= Converters.doubleToBigInteger(number);
					if (PrologInteger.isSmallInteger(bigInteger)) {
						return new PrologInteger(bigInteger);
					} else {
						throw new RejectValue();
					}
				} catch (TermIsNotAReal e2) {
					String text= value.toString(iX);
					return new PrologString(text);
				}
			}
		}
	}
}
