/*
 * @(#)ScalableLabel.java 1.0 2010/01/04
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableLabel implementation for the Actor Prolog language
 * @version 1.0 2010/01/04
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.gui.dialogs.signals.*;
import morozov.terms.*;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Icon;

public class ScalableLabel extends ActiveComponent {
	//
	protected AbstractDialog dialog;
	protected String text= "";
	protected double length= 0;
	protected TextAlignment alignment= TextAlignment.RIGHT;
	//
	public ScalableLabel(AbstractDialog tD) {
		this(tD,"",null,SwingConstants.LEADING);
	}
	public ScalableLabel(AbstractDialog tD, Icon icon) {
		this(tD,"",icon,SwingConstants.LEADING);
	}
	public ScalableLabel(AbstractDialog tD, Icon icon, int horizontalAlignment) {
		this(tD,"",icon,horizontalAlignment);
	}
	public ScalableLabel(AbstractDialog tD, String text) {
		this(tD,text,null,SwingConstants.LEADING);
	}
	public ScalableLabel(AbstractDialog tD, String text, int horizontalAlignment) {
		this(tD,text,null,horizontalAlignment);
	}
	public ScalableLabel(AbstractDialog tD, String cT, Icon icon, int horizontalAlignment) {
		super(tD);
		dialog= tD;
		text= cT;
		length= text.length();
		JLabel label= new JLabel(text,null,SwingConstants.LEADING);
		label.setOpaque(true);
		component= label;
	}
	public ScalableLabel(AbstractDialog tD, String cT, double l, TextAlignment a) {
		super(tD);
		dialog= tD;
		text= cT;
		length= l;
		alignment= a;
		JLabel label= new JLabel(alignText(text),null,SwingConstants.LEADING);
		label.setOpaque(true);
		component= label;
	}
	//
	// protected int getInitialTopBorder() {return 5;}
	// protected int getInitialLeftBorder() {return 5;}
	// protected int getInitialBottomBorder() {return 5;}
	// protected int getInitialRightBorder() {return 5;}
	//
	public String alignText(String text1) {
		double delta= length - text1.length();
		if (delta > 0) {
			switch(alignment) {
				case LEFT: {
					String f= String.format("%%-%ds",(long)length);
					return String.format(f,text1);
				}
				case RIGHT: {
					String f= String.format("%%%ds",(long)length);
					return String.format(f,text1);
				}
				default: {
					long n= (long)(delta / 2);
					if (n > 0) {
						String f= String.format("%%%ds%%s%%%ds",n,n);
						return String.format(f," ",text1," ");
					} else {
						return text1;
					}
				}
			}
		} else {
			return text1;
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		text= value.toString(iX);
		if (component!=null) {
			((JLabel)component).setText(alignText(text));
			dialog.invalidate(); // 2012.03.05
			dialog.repaint(); // 2013.09.04
		}
	}
	//
	public Term getValue() {
		return new PrologString(text);
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new PrologString("");
		} else {
			return new PrologString(value.toString(iX));
		}
	}
}
