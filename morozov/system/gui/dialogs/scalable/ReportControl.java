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
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class ReportControl extends ActiveComponent {
	//
	protected AbstractDialog dialog;
	protected double width= 0;
	protected double height= 0;
	protected Report currentValue= null;
	protected ExtendedReportSpace space;
	//
	public ReportControl(AbstractDialog tD, ChoisePoint iX, double columns, double rows, boolean keepProportions, int anchor) {
		super(tD);
		dialog= tD;
		height= rows;
		width= columns;
		space= new ExtendedReportSpace(null,new TextPaneNoWrap());
		space.setDialog(dialog);
		component= space.getControl();
		safelyInitiateControlSize();
	}
	//
	protected void safelyInitiateControlSize() {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyInitiateControlSize();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
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
		Font dialogFont= dialog.quicklyGetFont();
		Dimension dimension= LayoutUtils.computeDimension(dialogFont,space.getControl(),width,height);
		space.getControl().setMinimumSize(dimension);
		space.getControl().setPreferredSize(dimension);
	}
	//
	// protected int getInitialTopBorder() {return 5;}
	// protected int getInitialLeftBorder() {return 5;}
	// protected int getInitialBottomBorder() {return 5;}
	// protected int getInitialRightBorder() {return 5;}
	//
	public void setFont(Font externalFont) {
		super.setFont(externalFont);
		if (space != null) {
			space.setPanelFontSize(externalFont.getSize(),false);
			Dimension dimension= LayoutUtils.computeDimension(externalFont,space.getControl(),width,height);
			// 2013.08.29: Если устанавливать размеры
			// space, а не component,
			// то SWING сходит с ума.
			// panel.setMinimumSize(dimension);
			// panel.setPreferredSize(dimension);
			space.getControl().setMinimumSize(dimension);
			space.getControl().setPreferredSize(dimension);
			dialog.doLayout(true);
			dialog.safelyRepaint();
			dialog.repaintAfterDelay();
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (space !=null) {
			value= value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			if (value instanceof Report) {
				if (currentValue != null) {
					// currentValue.release(space.panel,dialog.isModal,iX);
					currentValue.release(dialog.isModal,iX);
				};
				currentValue= (Report)value;
				// currentValue.registerReport(space,iX);
				currentValue.registerCanvasSpace(space,iX);
				currentValue.draw(dialog.isModal,iX);
				dialog.doLayout(true);
				dialog.safelyRepaint();
				dialog.repaintAfterDelay();
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
