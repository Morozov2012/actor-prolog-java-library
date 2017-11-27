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

import target.*;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.errors.*;
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
			ActiveComponentInterface,
			ActionListener,
			ChangeListener,
			ListSelectionListener,
			FocusListener {
	//
	public Component component;
	//
	protected AbstractDialog targetDialog= null;
	//
	protected Font colourlessFont;
	protected Color textColor;
	protected Color spaceColor;
	//
	protected Color individualTextColor;
	protected Color individualSpaceColor;
	protected Color individualBackgroundColor;
	//
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
	protected static Term termEmptyString= new PrologString("");
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public ActiveComponent(AbstractDialog tD) {
		targetDialog= tD;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	}
	public Term standardizeRange(Term value, ChoisePoint iX) throws RejectRange {
		return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(DialogControlOperation operation, Term value, ChoisePoint iX) {
		switch (operation) {
			case VALUE:
				putValue(value,iX);
				break;
			case TEXT:
				setIndividualText(value,iX);
				break;
			case TEXT_COLOR:
				setIndividualTextColor(value,iX);
				break;
			case SPACE_COLOR:
				setIndividualSpaceColor(value,iX);
				break;
			case BACKGROUND_COLOR:
				setIndividualBackgroundColor(value,iX);
				break;
		}
	}
	//
	public Term getValue(DialogControlOperation operation) {
		switch (operation) {
			case VALUE:
				return getValue();
			case TEXT:
				return getIndividualText();
			case TEXT_COLOR:
				return getIndividualTextColor();
			case SPACE_COLOR:
				return getIndividualSpaceColor();
			case BACKGROUND_COLOR:
				return getIndividualBackgroundColor();
		};
		throw new UnknownDialogControlOperation();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
	}
	public Term getValue() {
		return PrologUnknownValue.instance;
	}
	//
	public void putRange(Term value, ChoisePoint iX) {
	}
	public Term getRange() {
		return PrologUnknownValue.instance;
	}
	//
	///////////////////////////////////////////////////////////////
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
	///////////////////////////////////////////////////////////////
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
	//
	///////////////////////////////////////////////////////////////
	//
	public void setGeneralFont(Font font) {
		colourlessFont= font;
		if (component != null) {
			font= DialogUtils.refineTextAndSpaceColors(font,individualSpaceColor,spaceColor,individualTextColor,textColor);
			if (gridBagLayout != null) {
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
			setFont(font);
			setMargin();
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
	///////////////////////////////////////////////////////////////
	//
	public void setGeneralForeground(Color c) {
		textColor= c;
		if (component != null) {
			if (individualTextColor==null) {
				setForeground(c);
			} else {
				setForeground(individualTextColor);
			};
			if (colourlessFont==null) {
				colourlessFont= component.getFont();
			};
			setGeneralFont(colourlessFont);
		}
	}
	public void setGeneralSpaceColor(Color c) {
		spaceColor= c;
		if (component != null) {
			if (colourlessFont==null) {
				colourlessFont= component.getFont();
			};
			setGeneralFont(colourlessFont);
		}
	}
	public void setGeneralBackground(Color c) {
		if (component != null) {
			if (individualBackgroundColor==null) {
				setBackground(c);
			} else {
				setBackground(individualBackgroundColor);
			}
		}
	}
	public void setAlarmColors(Color fc, Color bc) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setIndividualText(Term value, ChoisePoint iX) {
	}
	//
	public Term getIndividualText() {
		return termEmptyString;
	}
	//
	public void setIndividualTextColor(Term value, ChoisePoint iX) {
		try {
			individualTextColor= ExtendedColor.argumentToColorSafe(value,iX);
			setForeground(individualTextColor);
		} catch (TermIsSymbolDefault e) {
			individualTextColor= null;
			setForeground(null);
		};
		if (colourlessFont==null) {
			colourlessFont= component.getFont();
		};
		setGeneralFont(colourlessFont);
	}
	//
	public void setIndividualSpaceColor(Term value, ChoisePoint iX) {
		try {
			individualSpaceColor= ExtendedColor.argumentToColorSafe(value,iX);
		} catch (TermIsSymbolDefault e) {
			individualSpaceColor= null;
		};
		if (colourlessFont==null) {
			colourlessFont= component.getFont();
		};
		setGeneralFont(colourlessFont);
	}
	//
	public void setIndividualBackgroundColor(Term value, ChoisePoint iX) {
		try {
			individualBackgroundColor= ExtendedColor.argumentToColorSafe(value,iX);
			setBackground(individualBackgroundColor);
		} catch (TermIsSymbolDefault e) {
			individualBackgroundColor= null;
			setBackground(null);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getIndividualTextColor() {
		if (individualTextColor != null) {
			return ExtendedColor.colorToTerm(individualTextColor);
		} else {
			return termDefault;
		}
	}
	public Term getIndividualSpaceColor() {
		if (individualSpaceColor != null) {
			return ExtendedColor.colorToTerm(individualSpaceColor);
		} else {
			return termDefault;
		}
	}
	public Term getIndividualBackgroundColor() {
		if (individualBackgroundColor != null) {
			return ExtendedColor.colorToTerm(individualBackgroundColor);
		} else {
			return termDefault;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setFont(Font font) {
		if (component!=null) {
			component.setFont(font);
		}
	}
	//
	protected void setMargin() {
	}
	//
	public void setForeground(Color c) {
		if (component != null) {
			component.setForeground(c);
		}
	}
	//
	public void setBackground(Color c) {
		if (component != null) {
			component.setBackground(c);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void actionPerformed(ActionEvent event) {
		if (targetDialog != null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	public void stateChanged(ChangeEvent event) {
		if (targetDialog != null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	public void valueChanged(ListSelectionEvent event) {
		if (targetDialog != null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean requestFocusInWindow() {
		if (component != null) {
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
}
