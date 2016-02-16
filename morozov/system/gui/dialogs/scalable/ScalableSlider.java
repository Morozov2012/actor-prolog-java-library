/*
 * @(#)ScalableSlider.java 1.0 2010/03/09
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableSlider implementation for the Actor Prolog language
 * @version 1.0 2010/03/09
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.JSlider;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.BoundedRangeModel;

public class ScalableSlider extends ActiveComponent {
	//
	protected int orientation;
	protected double length;
	protected long intermediateValue;
	//
	public ScalableSlider(AbstractDialog tD, int direction, double n, int min, int max, int value) {
		super(tD);
		orientation= direction;
		length= n;
		JSlider slider= new JSlider(orientation,min,max,value);
		component= slider;
		slider.addChangeListener(this);
		slider.addFocusListener(this);
	}
	// protected int getInitialTopBorder() {return 5;}
	// protected int getInitialLeftBorder() {return 5;}
	// protected int getInitialBottomBorder() {return 5;}
	// protected int getInitialRightBorder() {return 5;}
	//
	public void setFont(Font font) {
		super.setFont(font);
		if (component!=null) {
			component.setFont(font);
			FontMetrics metrics= component.getFontMetrics(font);
			Dimension size= component.getPreferredSize();
			int currentHeight= size.height;
			int currentWidth= size.width;
			if (orientation==JSlider.HORIZONTAL) {
				int charWidth= metrics.charWidth('M');
				currentWidth= PrologInteger.toInteger(length*charWidth);
			} else {
				FontRenderContext frc= metrics.getFontRenderContext();
				TextLayout layout= new TextLayout("M",font,frc);
				Rectangle rectangle= layout.getPixelBounds(null,0,0);
				double charHeight= rectangle.getHeight();
				currentHeight= PrologInteger.toInteger(length*charHeight);
			};
			component.setMinimumSize(new Dimension(currentWidth,currentHeight));
			component.setPreferredSize(new Dimension(currentWidth,currentHeight));
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			try {
				int position= value.getSmallIntegerValue(iX);
				((JSlider)component).setValue(position);
				// int minimum= ((JSlider)component).getMinimum();
				// int maximum= ((JSlider)component).getMaximum();
				// int position= maximum + minimum - value.getSmallIntegerValue(iX);
				// ((JSlider)component).setValue(position);
			} catch (TermIsNotAnInteger e1) {
				try {
					int position= PrologInteger.toInteger(value.getRealValue(iX));
					((JSlider)component).setValue(position);
				} catch (TermIsNotAReal e2) {
				}
			}
		}
	}
	public void putRange(Term value, ChoisePoint iX) {
		if (component!=null) {
			implementSliderRange(value,iX,((JSlider)component).getModel());
		}
	}
	public Term getValue() {
		if (component!=null) {
			return new PrologInteger(((JSlider)component).getValue());
			// int minimum= ((JSlider)component).getMinimum();
			// int maximum= ((JSlider)component).getMaximum();
			// int position= maximum + minimum - ((JSlider)component).getValue();
			// return new PrologInteger(position);
		} else {
			return PrologUnknownValue.instance;
		}
	}
	public Term getRange() {
		if (component!=null) {
			Term result= PrologEmptyList.instance;
			result= new PrologList(new PrologInteger(((JSlider)component).getMaximum()),result);
			result= new PrologList(new PrologInteger(((JSlider)component).getMinimum()),result);
			return result;
		} else {
			// return PrologEmptyList.instance;
			return PrologUnknownValue.instance;
		}
	}
	//
	protected void implementSliderRange(Term iA1, ChoisePoint iX, BoundedRangeModel model) {
		try {
			Term number1= iA1.getNextListHead(iX);
			Term rest1= iA1.getNextListTail(iX);
			Term number2= rest1.getNextListHead(iX);
			Term rest2= rest1.getNextListTail(iX);
			rest2= rest2.dereferenceValue(iX);
			if (!rest2.thisIsEmptyList()) {
				// throw new TermIsNotSliderRange();
				return;
			};
			int minimum= 0;
			int maximum= 0;
			try {
				minimum= PrologInteger.toInteger(number1.getRealValue(iX));
			} catch (TermIsNotAReal e1) {
				try {
					minimum= number1.getSmallIntegerValue(iX);
				} catch (TermIsNotAnInteger e2) {
					// throw Backtracking.instance;
					// throw new TermIsNotSliderRange();
					return;
				}
			};
			try {
				maximum= PrologInteger.toInteger(number2.getRealValue(iX));
			} catch (TermIsNotAReal e1) {
				try {
					maximum= number2.getSmallIntegerValue(iX);
				} catch (TermIsNotAnInteger e2) {
					// throw Backtracking.instance;
					// throw new TermIsNotSliderRange();
					return;
				}
			};
			model.setMinimum(minimum);
			model.setMaximum(maximum);
		} catch (TermIsNotAList e1) {
			// throw new TermIsNotSliderRange();
		} catch (EndOfList e1) {
			// throw new TermIsNotSliderRange();
		}
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		return new PrologInteger(DialogUtils.termToSmallIntegerOrReject(value,iX));
	}
	public Term standardizeRange(Term value, ChoisePoint iX) throws RejectRange {
		try {
			value= value.dereferenceValue(iX);
			if (value.thisIsFreeVariable()) {
				throw RejectRange.instance;
			};
			Term number1= value.getNextListHead(iX);
			number1= number1.dereferenceValue(iX);
			if (number1.thisIsFreeVariable()) {
				throw RejectRange.instance;
			};
			Term rest1= value.getNextListTail(iX);
			rest1= rest1.dereferenceValue(iX);
			if (rest1.thisIsFreeVariable()) {
				throw RejectRange.instance;
			};
			Term number2= rest1.getNextListHead(iX);
			number2= number2.dereferenceValue(iX);
			if (number2.thisIsFreeVariable()) {
				throw RejectRange.instance;
			};
			Term rest2= rest1.getNextListTail(iX);
			rest2= rest2.dereferenceValue(iX);
			if (rest2.thisIsFreeVariable()) {
				throw RejectRange.instance;
			} else if (!rest2.thisIsEmptyList()) {
				// throw new TermIsNotSliderRange();
				// return PrologUnknownValue.instance;
				throw RejectRange.instance;
			};
			int minimum= DialogUtils.termToSmallIntegerOrReject(number1,iX);
			int maximum= DialogUtils.termToSmallIntegerOrReject(number2,iX);
			Term result= PrologEmptyList.instance;
			result= new PrologList(new PrologInteger(maximum),result);
			result= new PrologList(new PrologInteger(minimum),result);
			return result;
		} catch (TermIsNotAList e1) {
			// throw new TermIsNotSliderRange();
			// return PrologUnknownValue.instance;
			throw RejectRange.instance;
		} catch (EndOfList e1) {
			// throw new TermIsNotSliderRange();
			// return PrologUnknownValue.instance;
			throw RejectRange.instance;
		} catch (RejectValue e1) {
			throw RejectRange.instance;
		}
	}
}
