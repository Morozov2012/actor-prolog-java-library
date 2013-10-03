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
import morozov.system.gui.dialogs.signals.*;
import morozov.system.gui.reports.*;
import morozov.terms.*;

import java.awt.Dimension;
import java.awt.Font;

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
		space= new ExtendedReportSpace(new TextPaneNoWrap());
		component= space;
		Font dialogFont= dialog.getFont();
		Dimension dimension= LayoutUtils.computeDimension(dialogFont,space,width,height);
		space.setMinimumSize(dimension);
		space.setPreferredSize(dimension);
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
			Dimension dimension= LayoutUtils.computeDimension(externalFont,space,width,height);
			// 2013.08.29: Если устанавливать размеры
			// space, а не component,
			// то SWING сходит с ума.
			// panel.setMinimumSize(dimension);
			// panel.setPreferredSize(dimension);
			space.setMinimumSize(dimension);
			space.setPreferredSize(dimension);
			dialog.doLayout(true);
			dialog.repaint();
			dialog.repaintAfterDelay();
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (space !=null) {
			value= value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			if (value instanceof Report) {
				if (currentValue != null) {
					currentValue.release(space.panel,dialog.isToBeModal(),iX);
				};
				currentValue= (Report)value;
				currentValue.registerReport(space,iX);
				currentValue.draw(dialog.isToBeModal(),iX);
				dialog.doLayout(true);
				dialog.repaint();
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
