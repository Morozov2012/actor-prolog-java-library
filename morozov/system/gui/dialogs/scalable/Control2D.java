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
import morozov.terms.*;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dimension;

public class Control2D extends CustomControlComponent {
	//
	protected morozov.built_in.Canvas2D currentValue= null;
	protected ExtendedSpace2D space2D;
	protected Icon2D icon2D;
	//
	///////////////////////////////////////////////////////////////
	//
	public Control2D(AbstractDialog tD, ChoisePoint iX, double columns, double rows, boolean keepProportions, int anchor) {
		super(tD,columns,rows);
		space2D= new ExtendedSpace2D(this,keepProportions);
		String label= "";
		icon2D= new Icon2D(space2D,keepProportions,anchor);
		JLabel jL= new JLabel(label,icon2D,SwingConstants.LEADING);
		space2D.setJLabel(jL);
		space2D.setDialog(targetDialog);
		jL.setOpaque(false);
		component= jL;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			value= value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			if (value instanceof morozov.built_in.Canvas2D) {
				if (currentValue != null) {
					currentValue.release(targetDialog.isModal(),iX);
				};
				currentValue= (morozov.built_in.Canvas2D)value;
				currentValue.registerCanvasSpace(space2D,iX);
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
			space2D.getControl().setMinimumSize(dimension);
			space2D.getControl().setPreferredSize(dimension);
			component.setMinimumSize(dimension);
			component.setPreferredSize(dimension);
			icon2D.setSize(dimension);
		}
	}
}
