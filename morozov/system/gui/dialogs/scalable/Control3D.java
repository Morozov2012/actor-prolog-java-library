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
import morozov.terms.*;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Color;

public class Control3D extends CustomControlComponent {
	//
	protected ExtendedSpace3D space;
	protected morozov.built_in.Canvas3D currentValue= null;
	//
	protected int minimalSafeSize= 35;
	//
	///////////////////////////////////////////////////////////////
	//
	public Control3D(AbstractDialog tD, ChoisePoint iX, double columns, double rows, boolean keepProportions, int anchor) {
		super(tD,columns,rows);
		GraphicsConfiguration dialogGraphicsConfiguration= targetDialog.safelyGetGraphicsConfiguration();
		GraphicsConfiguration canvas3DGraphicsConfig= Canvas3D.refineGraphicsConfiguration(dialogGraphicsConfiguration);
		space= new ExtendedSpace3D(this,null,canvas3DGraphicsConfig);
		space.setDialog(targetDialog);
		component= space.getControl();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			value= value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			if (value instanceof morozov.built_in.Canvas3D) {
				if (currentValue != null) {
					currentValue.release(targetDialog.isModal(),iX);
				};
				currentValue= (morozov.built_in.Canvas3D)value;
				currentValue.registerCanvasSpace(space,iX);
				currentValue.draw(targetDialog.isModal(),iX);
				targetDialog.safelyInvalidateAndRepaint();
			}
		}
	}
	//
	@Override
	public Term getValue() {
		return currentValue;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setDimension(Dimension dimension) {
		if (component!=null) {
			component.setMinimumSize(dimension);
			component.setPreferredSize(dimension);
		}
	}
	@Override
	public void setBackground(Color c) {
		super.setBackground(c);
	}
}
