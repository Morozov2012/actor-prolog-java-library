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
import morozov.system.converters.*;
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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

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
	protected Color backgroundColor;
	//
	protected Color individualTextColor;
	protected Color individualSpaceColor;
	protected Color individualBackgroundColor;
	//
	protected Color supervisoryTextColor;
	protected Color supervisoryBackgroundColor;
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
	@Override
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	}
	@Override
	public Term standardizeRange(Term value, ChoisePoint iX) throws RejectRange {
		return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
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
	@Override
	public void putRange(Term value, ChoisePoint iX) {
	}
	@Override
	public Term getRange() {
		return PrologUnknownValue.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void setScaling(double valueHorizontal, double valueVertical) {
		horizontalScaling= valueHorizontal;
		verticalScaling= valueVertical;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setGeneralFont(Font font) {
		if (font==null) {
			return;
		};
		colourlessFont= font;
		if (component != null) {
			font= DialogUtils.refineTextAndSpaceColors(
				font,
				individualSpaceColor,
				spaceColor,
				supervisoryTextColor,
				individualTextColor,
				textColor);
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
			component.setMinimumSize(component.getPreferredSize());
			component.invalidate();
		}
	}
	public void resetGeneralFont() {
		setGeneralFont(colourlessFont);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setGeneralForeground(Color c) {
		textColor= c;
		if (component != null) {
			if (supervisoryTextColor != null) {
				setForeground(supervisoryTextColor);
			} else if (individualTextColor != null) {
				setForeground(individualTextColor);
			} else {
				setForeground(c);
			};
			if (colourlessFont==null) {
				colourlessFont= component.getFont();
			};
			setGeneralFont(colourlessFont);
		}
	}
	@Override
	public void setGeneralSpaceColor(Color c) {
		spaceColor= c;
		if (component != null) {
			if (colourlessFont==null) {
				colourlessFont= component.getFont();
			};
			setGeneralFont(colourlessFont);
		}
	}
	@Override
	public void setGeneralBackground(Color c) {
		backgroundColor= c;
		if (component != null) {
			if (supervisoryBackgroundColor != null) {
				setBackground(supervisoryBackgroundColor);
			} else if (individualBackgroundColor != null) {
				setBackground(individualBackgroundColor);
			} else {
				setBackground(c);
			}
		}
	}
	public void resetGeneralBackground() {
		if (component != null) {
			if (supervisoryBackgroundColor != null) {
				setBackground(supervisoryBackgroundColor);
			} else if (individualBackgroundColor != null) {
				setBackground(individualBackgroundColor);
			} else {
				setBackground(backgroundColor);
			}
		}
	}
	@Override
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
			individualTextColor= ColorAttributeConverters.argumentToColorSafe(value,iX);
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
			individualSpaceColor= ColorAttributeConverters.argumentToColorSafe(value,iX);
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
			individualBackgroundColor= ColorAttributeConverters.argumentToColorSafe(value,iX);
			setBackground(individualBackgroundColor);
		} catch (TermIsSymbolDefault e) {
			individualBackgroundColor= null;
			setBackground(null);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setSupervisoryTextColor(Color value) {
		supervisoryTextColor= value;
	}
	public void setSupervisoryBackgroundColor(Color value) {
		supervisoryBackgroundColor= value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getIndividualTextColor() {
		if (individualTextColor != null) {
			return ColorAttributeConverters.colorToTerm(individualTextColor);
		} else {
			return termDefault;
		}
	}
	public Term getIndividualSpaceColor() {
		if (individualSpaceColor != null) {
			return ColorAttributeConverters.colorToTerm(individualSpaceColor);
		} else {
			return termDefault;
		}
	}
	public Term getIndividualBackgroundColor() {
		if (individualBackgroundColor != null) {
			return ColorAttributeConverters.colorToTerm(individualBackgroundColor);
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
	@Override
	public void actionPerformed(ActionEvent event) {
		if (targetDialog != null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	@Override
	public void stateChanged(ChangeEvent event) {
		if (targetDialog != null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	@Override
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
	@Override
	public void focusGained(FocusEvent e) {
	}
	@Override
	public void focusLost(FocusEvent e) {
		targetDialog.safelyRevalidateAndRepaint();
	}
}
