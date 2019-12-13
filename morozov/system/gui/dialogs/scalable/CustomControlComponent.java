/*
 * @(#)CustomControl.java 1.0 2017/10/21
 *
 * Copyright 2017 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * CustomControl implementation for the Actor Prolog language
 * @version 1.0 2017/10/21
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.Dimension;
import java.awt.Font;

public abstract class CustomControlComponent extends ActiveComponent {
	//
	protected double initialWidth= 0;
	protected double initialHeight= 0;
	//
	protected double refinedWidth= 0;
	protected double refinedHeight= 0;
	//
	protected boolean refineWidth= false;
	protected boolean refineHeight= false;
	protected double widthToHeightRatio= 1.0;
	//
	protected Font currentFont;
	//
	protected Dimension previousDimension;
	//
	protected double epsilon= 1e-10;
	//
	///////////////////////////////////////////////////////////////
	//
	public CustomControlComponent(AbstractDialog tD, double columns, double rows) {
		super(tD);
		initialWidth= columns;
		initialHeight= rows;
		refinedWidth= initialWidth;
		refinedHeight= initialHeight;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return PrologUnknownValue.instance;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void refineWidth(double ratio) {
		if (refineWidth && !refineHeight) {
			if (StrictMath.abs(widthToHeightRatio-ratio) < epsilon) {
				return;
			}
		};
		refineWidth= true;
		refineHeight= false;
		widthToHeightRatio= ratio;
		refinedWidth= initialHeight * widthToHeightRatio;
		refinedHeight= initialHeight;
		computeAndSetDimension(currentFont);
	}
	public void refineHeight(double ratio) {
		if (!refineWidth && refineHeight) {
			if (StrictMath.abs(widthToHeightRatio-ratio) < epsilon) {
				return;
			}
		};
		refineWidth= false;
		refineHeight= true;
		widthToHeightRatio= ratio;
		refinedWidth= initialWidth;
		refinedHeight= initialWidth / widthToHeightRatio;
		computeAndSetDimension(currentFont);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		currentFont= font;
		computeAndSetDimension(font);
	}
	protected void computeAndSetDimension(Font font){
		if (component!=null && font!=null) {
			Dimension dimension= LayoutUtils.computeDimension(font,component,refinedWidth,refinedHeight);
			if (previousDimension==null || !previousDimension.equals(dimension)) {
				setDimension(dimension);
				component.revalidate();
				targetDialog.doLayout(true);
			};
			previousDimension= dimension;
		}
	}
	abstract public void setDimension(Dimension d);
}
