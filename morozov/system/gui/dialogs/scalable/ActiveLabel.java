/*
 * @(#)ActiveLabel.java 1.0 2010/01/04
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ActiveLabel implementation for the Actor Prolog language
 * @version 1.0 2010/01/04
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.terms.*;

import javax.swing.*;

public class ActiveLabel extends ActiveComponent {
	//
	protected AbstractDialog dialog;
	protected String text= "";
	protected double length= 0;
	protected TextAlignment alignment= TextAlignment.RIGHT;
	//
	public ActiveLabel(AbstractDialog tD) {
		this(tD,"",null,SwingConstants.LEADING);
	}
	public ActiveLabel(AbstractDialog tD, Icon icon) {
		this(tD,"",icon,SwingConstants.LEADING);
	}
	public ActiveLabel(AbstractDialog tD, Icon icon, int horizontalAlignment) {
		this(tD,"",icon,horizontalAlignment);
	}
	public ActiveLabel(AbstractDialog tD, String text) {
		this(tD,text,null,SwingConstants.LEADING);
	}
	public ActiveLabel(AbstractDialog tD, String text, int horizontalAlignment) {
		this(tD,text,null,horizontalAlignment);
	}
	public ActiveLabel(AbstractDialog tD, String cT, Icon icon, int horizontalAlignment) {
		super(tD);
		dialog= tD;
		text= cT;
		length= text.length();
		component= new JLabel(text,null,SwingConstants.LEADING);
		component.setOpaque(true);
	}
	public ActiveLabel(AbstractDialog tD, String cT, double l, TextAlignment a) {
		super(tD);
		dialog= tD;
		text= cT;
		length= l;
		alignment= a;
		component= new JLabel(alignText(text),null,SwingConstants.LEADING);
		component.setOpaque(true);
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
			// ((JLabel)component).setText(text);
			// dialog.repaint();
			dialog.invalidate(); // 2012.03.05
		}
	}
	//
	public Term getValue() {
		// if (component!=null) {
		//	return new PrologString(((JLabel)component).getText());
		// } else {
		//	return new PrologUnknownValue();
		// }
		return new PrologString(text);
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			// return new PrologString(alignText(""));
			return new PrologString("");
		} else {
			// return new PrologString(alignText(value.toString(iX)));
			return new PrologString(value.toString(iX));
		}
	}
}
