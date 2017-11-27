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
import javax.swing.BoundedRangeModel;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.Rectangle;
import java.awt.Dimension;

public class ScalableSlider extends ActiveComponent {
	//
	protected int orientation;
	protected double length;
	protected long intermediateValue;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalableSlider(AbstractDialog tD, int direction, double n, int min, int max, int value) {
		super(tD);
		orientation= direction;
		length= n;
		// 2017.08.06: Fixed Bug
		// java.lang.IllegalArgumentException: invalid range properties
		// orientation=0,min=1,max=1000,value=0
		// java version "1.7.0_80"
		// Java(TM) SE Runtime Environment (build 1.7.0_80-b15)
		// Java HotSpot(TM) Client VM (build 24.80-b11, mixed mode, sharing)
		if (value < min) {
			value= min;
		};
		if (value > max) {
			value= max;
		};
		JSlider slider= new JSlider(orientation,min,max,value);
		component= slider;
		slider.addChangeListener(this);
		slider.addFocusListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		return new PrologInteger(DialogUtils.termToSmallIntegerOrReject(value,iX));
	}
	//
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
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			try {
				int position= value.getSmallIntegerValue(iX);
				quicklySetValue(position);
			} catch (TermIsNotAnInteger e1) {
				try {
					int position= PrologInteger.toInteger(value.getRealValue(iX));
					quicklySetValue(position);
				} catch (TermIsNotAReal e2) {
				}
			}
		}
	}
	protected void quicklySetValue(int value) {
		((JSlider)component).setValue(value);
	}
	//
	public void putRange(Term value, ChoisePoint iX) {
		if (component!=null) {
			implementSliderRange(value,iX,((JSlider)component).getModel());
		}
	}
	//
	public Term getValue() {
		if (component!=null) {
			return new PrologInteger(quicklyGetValue());
		} else {
			return PrologUnknownValue.instance;
		}
	}
	protected int quicklyGetValue() {
		return ((JSlider)component).getValue();
	}
	//
	public Term getRange() {
		if (component!=null) {
			return quicklyGetRange();
		} else {
			// return PrologEmptyList.instance;
			return PrologUnknownValue.instance;
		}
	}
	protected Term quicklyGetRange() {
		Term result= PrologEmptyList.instance;
		result= new PrologList(new PrologInteger(((JSlider)component).getMaximum()),result);
		result= new PrologList(new PrologInteger(((JSlider)component).getMinimum()),result);
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setFont(Font font) {
		super.setFont(font);
		if (component!=null) {
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
	///////////////////////////////////////////////////////////////
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
			quicklySetSliderRange(model,minimum,maximum);
		} catch (TermIsNotAList e1) {
			// throw new TermIsNotSliderRange();
		} catch (EndOfList e1) {
			// throw new TermIsNotSliderRange();
		}
	}
	protected void quicklySetSliderRange(BoundedRangeModel model, int minimum, int maximum) {
		model.setMinimum(minimum);
		model.setMaximum(maximum);
	}
}
