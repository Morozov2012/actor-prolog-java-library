/*
 * @(#)Control3D.java 1.0 2013/08/23
 *
 * Copyright 2013 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * Control3D implementation for the Actor Prolog language
 * @version 1.0 2013/08/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.space3d.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;

public class Control3D extends ActiveComponent {
	//
	protected AbstractDialog dialog;
	protected double width= 0;
	protected double height= 0;
	protected ExtendedSpace3D space;
	protected morozov.built_in.Canvas3D currentValue= null;
	//
	protected int minimalSafeSize= 35; // Wintel7: > 31, <= 32
	//
	public Control3D(AbstractDialog tD, ChoisePoint iX, double columns, double rows, boolean keepProportions, int anchor) {
		super(tD);
		dialog= tD;
		height= rows;
		width= columns;
		GraphicsConfiguration dialogGraphicsConfiguration= dialog.safelyGetGraphicsConfiguration();
		GraphicsConfiguration canvas3DGraphicsConfig= Canvas3D.refineGraphicsConfiguration(dialogGraphicsConfiguration);
		space= new ExtendedSpace3D(null,canvas3DGraphicsConfig);
		space.setDialog(dialog);
		component= space.getControl();
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
			component.setMinimumSize(dimension);
			component.setPreferredSize(dimension);
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			value= value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			if (value instanceof morozov.built_in.Canvas3D) {
				if (currentValue != null) {
					currentValue.release(dialog.isModal,iX);
				};
				currentValue= (morozov.built_in.Canvas3D)value;
				currentValue.registerCanvasSpace(space,iX);
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
