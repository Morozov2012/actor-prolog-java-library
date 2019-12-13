/*
 * @(#)ScalableComboBoxArrowButton.java 1.0 2009/12/29
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.windows;

import morozov.system.*;

import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;

/*
 * ScalableWindowsComboBoxUI implementation for the Actor Prolog language
 * @version 1.0 2009/12/29
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableComboBoxArrowButton extends BasicArrowButton {
	//
	public ScalableComboBoxArrowButton(int direction, Color background, Color shadow, Color darkShadow, Color highlight) {
		super(direction,background,shadow,darkShadow,highlight);
	}
	//
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Arithmetic.toInteger(16*(2.0/5)),16);
	}
	@Override
	public Insets getInsets() {
		Insets insets= super.getInsets();
		insets.set(insets.bottom,Arithmetic.toInteger(insets.left*(2.0/5)),insets.top,Arithmetic.toInteger(insets.right*(2.0/5)));
		return insets;
	}
}
