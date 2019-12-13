/*
 * @(#)ReportControl.java 1.0 2013/08/27
 *
 * Copyright 2013 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ReportControl implementation for the Actor Prolog language
 * @version 1.0 2013/08/27
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.reports.*;
import morozov.terms.*;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class ReportControl extends CustomControlComponent {
	//
	protected Report currentValue= null;
	protected ExtendedReportSpace space;
	//
	///////////////////////////////////////////////////////////////
	//
	public ReportControl(AbstractDialog tD, ChoisePoint iX, double columns, double rows, boolean keepProportions, int anchor) {
		super(tD,columns,rows);
		space= new ExtendedReportSpace(this,null,new TextPaneNoWrap());
		space.setDialog(targetDialog);
		component= space.getControl();
		safelyInitiateControlSize();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void putValue(Term value, ChoisePoint iX) {
		if (space !=null) {
			value= value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			if (value instanceof Report) {
				if (currentValue != null) {
					currentValue.release(targetDialog.isModal(),iX);
				};
				currentValue= (Report)value;
				currentValue.registerCanvasSpace(space,iX);
				currentValue.draw(targetDialog.isModal(),iX);
				targetDialog.doLayout(true);
				targetDialog.safelyRepaint();
				targetDialog.repaintAfterDelay();
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
	protected void safelyInitiateControlSize() {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyInitiateControlSize();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						quicklyInitiateControlSize();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklyInitiateControlSize() {
		Font dialogFont= targetDialog.quicklyGetFont();
		Dimension dimension= LayoutUtils.computeDimension(dialogFont,space.getControl(),refinedWidth,refinedHeight);
		space.getControl().setMinimumSize(dimension);
		space.getControl().setPreferredSize(dimension);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (space != null) {
			space.setPanelFontSize(font.getSize(),false);
		}
	}
	//
	@Override
	public void setDimension(Dimension dimension) {
		if (space != null) {
			space.getControl().setMinimumSize(dimension);
			space.getControl().setPreferredSize(dimension);
			targetDialog.doLayout(true);
			targetDialog.safelyRepaint();
			targetDialog.repaintAfterDelay();
		}
	}
}
