/*
 * @(#)ScalableComboBoxArrowButton.java 1.0 2009/12/29
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.windows;

import morozov.terms.*;

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
	public ScalableComboBoxArrowButton(int direction, Color background, Color shadow, Color darkShadow, Color highlight) {
		super(direction,background,shadow,darkShadow,highlight);
	}
	//
	public Dimension getPreferredSize() {
		// return new Dimension(16,16);
		return new Dimension(PrologInteger.toInteger(16*(2.0/5)),16);
		// return new Dimension(PrologInteger.toInteger(8*(2.0/5)),8);
		// return new Dimension(PrologInteger.toInteger(32*(2.0/5)),32);
		// return new Dimension(PrologInteger.toInteger(64*(2.0/5)),64);
	}
	public Insets getInsets() {
		Insets insets= super.getInsets();
		insets.set(insets.bottom,PrologInteger.toInteger(insets.left*(2.0/5)),insets.top,PrologInteger.toInteger(insets.right*(2.0/5)));
		return insets;
	}
}
