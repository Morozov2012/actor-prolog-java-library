/*
 * @(#)ActiveComponent.java 1.0 2010/01/04
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ActiveComponent implementation for the Actor Prolog language
 * @version 1.0 2010/01/04
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.signals.*;
import morozov.terms.*;

// import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.TextAttribute;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;

import java.util.HashMap;
import java.util.Map;

public abstract class ActiveComponent implements ActionListener, ChangeListener, ListSelectionListener {
	//
	// public JComponent component;
	public Component component;
	//
	protected AbstractDialog targetDialog= null;
	//
	protected Font colourlessFont;
	protected Color spaceColor;
	protected GridBagLayout gridBagLayout;
	protected boolean isTop= false;
	protected boolean isLeft= false;
	protected boolean isBottom= false;
	protected boolean isRight= false;
	protected double horizontalPadding= 0;
	protected double verticalPadding= 0;
	protected double horizontalScaling= 1;
	protected double verticalScaling= 1;
	//
	protected int getInitialTopBorder() {return 0;}
	protected int getInitialLeftBorder() {return 0;}
	protected int getInitialBottomBorder() {return 0;}
	protected int getInitialRightBorder() {return 0;}
	//
	public ActiveComponent(AbstractDialog tD) {
		targetDialog= tD;
	}
	//
	public abstract void putValue(Term value, ChoisePoint iX);
	public abstract Term getValue();
	//
	public void putRange(Term value, ChoisePoint iX) {
	}
	public Term getRange() {
		// return PrologEmptyList.instance;
		return PrologUnknownValue.instance;
	}
	//
	public void setPadding(
			GridBagLayout gBL,
			boolean flagTop, boolean flagLeft, boolean flagBottom, boolean flagRight,
			double valueHorizontal, double valueVertical) {
		gridBagLayout= gBL;
		isTop= flagTop;
		isLeft= flagLeft;
		isBottom= flagBottom;
		isRight= flagRight;
		horizontalPadding= valueHorizontal;
		verticalPadding= valueVertical;
	}
	public void setScaling(double valueHorizontal, double valueVertical) {
		horizontalScaling= valueHorizontal;
		verticalScaling= valueVertical;
	}
	// public double getHorizontalScaling() {
	//	return horizontalScaling;
	// }
	// public double getVerticalScaling() {
	//	return verticalScaling;
	// }
	public void setFont(Font font) {
		colourlessFont= font;
		if (component!=null) {
			if (spaceColor!=null) {
				Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
				map.put(TextAttribute.BACKGROUND,spaceColor);
				font= font.deriveFont(map);
			};
			if (gridBagLayout!=null) {
				GridBagConstraints gBC= gridBagLayout.getConstraints(component);
				FontMetrics metrics= component.getFontMetrics(font);
				gBC.insets= LayoutUtils.calculateLayoutInsets(
					gBC,
					font,
					metrics,
					horizontalPadding,
					verticalPadding,
					horizontalScaling,
					verticalScaling,
					isTop,
					isLeft,
					isBottom,
					isRight,
					getInitialTopBorder(),
					getInitialLeftBorder(),
					getInitialBottomBorder(),
					getInitialRightBorder());
				gridBagLayout.setConstraints(component,gBC);
			};
			component.setFont(font);
			// component.setMinimumSize(component.getPreferredSize());
			// Предположительно, это может помочь в борьбе
			// с проблемой схлопывания текстовых полей:
			component.setMinimumSize(component.getPreferredSize());
			// component.repaint();
			// component.validate();
			component.invalidate();
		}
	}
	//
	public boolean requestFocusInWindow() {
		if (component!=null) {
			return component.requestFocusInWindow();
		} else {
			return false;
		}
	}
	//
	public void setForeground(Color c) {
		if (component!=null) {
			component.setForeground(c);
		}
	}
	public void setSpaceColor(Color c) {
		spaceColor= c;
		if (component!=null && colourlessFont!=null) {
			setFont(colourlessFont);
		}
	}
	public void setBackground(Color c) {
		if (component!=null) {
			component.setBackground(c);
		}
	}
	// public void setOpaque(boolean f) {
	//	if (component!=null) {
	//		component.setOpaque(f);
	//	}
	// }
	public void setAlarmColors(Color fc, Color bc) {
	}
	//
	public void actionPerformed(ActionEvent event) {
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	public void stateChanged(ChangeEvent event) {
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	public void valueChanged(ListSelectionEvent event) {
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	}
	public Term standardizeRange(Term value, ChoisePoint iX) throws RejectRange {
		return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	}
}
