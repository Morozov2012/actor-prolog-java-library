/*
 * @(#)Control2D.java 1.0 2013/08/24
 *
 * Copyright 2013 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * Control2D implementation for the Actor Prolog language
 * @version 1.0 2013/08/24
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.gui.space2d.*;
import morozov.system.signals.*;
import morozov.terms.*;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;

public class Control2D extends ActiveComponent {
	//
	protected AbstractDialog dialog;
	protected double width= 0;
	protected double height= 0;
	protected morozov.built_in.Canvas2D currentValue= null;
	protected ExtendedSpace2D space2D;
	protected Icon2D icon2D;
	//
	public Control2D(AbstractDialog tD, ChoisePoint iX, double columns, double rows, boolean keepProportions, int anchor) {
		super(tD);
		dialog= tD;
		height= rows;
		width= columns;
		space2D= new ExtendedSpace2D(keepProportions);
		String label= "";
		icon2D= new Icon2D(space2D,keepProportions,anchor);
		JLabel jL= new JLabel(label,icon2D,SwingConstants.LEADING);
		space2D.setJLabel(jL);
		space2D.setDialog(dialog);
		jL.setOpaque(false);
		component= jL;
	}
	//
	// protected int getInitialTopBorder() {return 5;}
	// protected int getInitialLeftBorder() {return 5;}
	// protected int getInitialBottomBorder() {return 5;}
	// protected int getInitialRightBorder() {return 5;}
	//
	public void setFont(Font font) {
		super.setFont(font);
		if (component!=null) {
			component.setFont(font);
			Dimension dimension= LayoutUtils.computeDimension(font,component,width,height);
			space2D.getControl().setMinimumSize(dimension);
			space2D.getControl().setPreferredSize(dimension);
			component.setMinimumSize(dimension);
			component.setPreferredSize(dimension);
			icon2D.setSize(dimension);
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			value= value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			if (value instanceof morozov.built_in.Canvas2D) {
				if (currentValue != null) {
					currentValue.release(dialog.isModal,iX);
				};
				currentValue= (morozov.built_in.Canvas2D)value;
				currentValue.registerCanvasSpace(space2D,iX);
				currentValue.draw(dialog.isModal,iX);
				// dialog.invalidate();
				// dialog.repaint();
				dialog.safelyInvalidateAndRepaint();
			}
		}
	}
	//
	public Term getValue() {
		return currentValue;
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return PrologUnknownValue.instance;
		} else {
			return value;
		}
	}
}
