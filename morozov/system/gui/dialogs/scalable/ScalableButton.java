/*
 * @(#)ScalableButton.java 1.0 2010/02/19
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.terms.*;

import javax.swing.JButton;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.Action;

import java.awt.Insets;
import java.awt.Dimension;

public class ScalableButton extends ScalableAbstractButton {
	//
	protected int initialTopMargin= 0; // -1;
	protected int initialLeftMargin= 7;
	protected int initialBottomMargin= 0;
	protected int initialRightMargin= 7;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalableButton(AbstractDialog tD) {
		this(tD,null,null);
	}
	public ScalableButton(AbstractDialog tD, Action a) {
		this(tD,null,null);
		if (component!=null) {
			((JButton)component).setAction(a);
		}
	}
	public ScalableButton(AbstractDialog tD, Icon icon) {
		this(tD,null,icon);
	}
	public ScalableButton(AbstractDialog tD, String text) {
		this(tD,text,null);
	}
	public ScalableButton(AbstractDialog tD, String text, Icon icon) {
		super(tD);
		component= new JButton(text,icon);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void setMargin() {
		if (component!=null) {
			if (horizontalScaling > 0 && verticalScaling > 0) {
				((AbstractButton)component).setMargin(new Insets(initialTopMargin,initialLeftMargin,initialBottomMargin,initialRightMargin));
				Dimension preferredSize= component.getPreferredSize();
				double scaledWidth= preferredSize.getWidth() * horizontalScaling;
				double scaledHeight= preferredSize.getHeight() * verticalScaling;
				int deltaWidth= Arithmetic.toInteger((scaledWidth - preferredSize.getWidth()) / 2);
				int deltaHeight= Arithmetic.toInteger((scaledHeight - preferredSize.getHeight()) / 2);
				int topMargin= initialTopMargin + deltaHeight;
				int leftMargin= initialLeftMargin + deltaWidth;
				int bottomMargin= initialBottomMargin + deltaHeight;
				int rightMargin= initialRightMargin + deltaWidth;
				((AbstractButton)component).setMargin(new Insets(topMargin,leftMargin,bottomMargin,rightMargin));
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setIndividualText(Term value, ChoisePoint iX) {
		if (component!=null) {
			AnnotatedButton.safelyUpdateAbstractButton((AbstractButton)component,value,iX);
			targetDialog.safelyRevalidateAndRepaint();
			targetDialog.repaintAfterDelay();
		}
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
}
