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
import morozov.system.signals.*;
import morozov.terms.*;

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
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class ActiveComponent
		implements
			ActionListener,
			ChangeListener,
			ListSelectionListener,
			FocusListener {
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
	public void setIsEnabled(boolean mode) {
		if (component != null) {
			if (mode) {
				component.setEnabled(true);
			} else {
				component.setEnabled(false);
			}
		}
	}
	//
	public boolean isEnabled(boolean mode) {
		if (component != null) {
			return (component.isEnabled() == mode);
		} else {
			return false;
		}
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
	public void focusGained(FocusEvent e) {
	}
	public void focusLost(FocusEvent e) {
		// area.revalidate();
		// area.repaint();
		// revalidateAndRepaint();
		// 2013.12.07: Без этих комманд происходят престранные
		// вещи. Нажимаю кнопку, открывается новый (модальный)
		// диалог, закрываю диалог, и потом текущий диалог
		// некорректно перерисовывается.
		// См. пример test_117_45_enable_disable_01_jdk.a.
		targetDialog.safelyRevalidateAndRepaint();
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
