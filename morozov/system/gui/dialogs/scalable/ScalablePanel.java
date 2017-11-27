/*
 * @(#)ScalablePanel.java 1.0 2010/04/28
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalablePanel implementation for the Actor Prolog language
 * @version 1.0 2010/04/28
 * @author IRE RAS Alexei A. Morozov
*/

import target.*;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.TextAttribute;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.HashMap;
import java.util.Map;

public class ScalablePanel extends JPanel implements ActiveComponentInterface {
	//
	protected AbstractDialog targetDialog= null;
	//
	protected GridBagLayout gridBagLayout;
	//
	protected Font colourlessFont;
	protected Color textColor;
	protected Color spaceColor;
	//
	protected Color individualTextColor;
	protected Color individualSpaceColor;
	protected Color individualBackgroundColor;
	//
	protected boolean isTop= false;
	protected boolean isLeft= false;
	protected boolean isBottom= false;
	protected boolean isRight= false;
	//
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
	protected AtomicBoolean isTransparent= new AtomicBoolean(true);
	protected AtomicReference<Color> hatchColor= new AtomicReference<Color>();
	//
	protected static Term termEmptyString= new PrologString("");
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalablePanel(AbstractDialog tD) {
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
				return PrologUnknownValue.instance;
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
	public void putRange(Term value, ChoisePoint iX) {
	}
	public Term getRange() {
		return PrologUnknownValue.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setIsEnabled(boolean mode) {
		if (mode) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
	//
	public boolean isEnabled(boolean mode) {
		return (isEnabled() == mode);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setTransparency(boolean flag) {
		isTransparent.set(flag);
	}
	//
	public void setHatchColor(Color c) {
		hatchColor.set(c);
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
	//
	public void setScaling(double valueHorizontal, double valueVertical) {
		horizontalScaling= valueHorizontal;
		verticalScaling= valueVertical;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setGeneralFont(Font font) {
		colourlessFont= font;
		font= DialogUtils.refineTextAndSpaceColors(font,individualSpaceColor,spaceColor,individualTextColor,textColor);
		if (gridBagLayout != null) {
			GridBagConstraints gBC= gridBagLayout.getConstraints(this);
			FontMetrics metrics= getFontMetrics(font);
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
			gridBagLayout.setConstraints(this,gBC);
		};
		setFont(font);
		//// component.setMinimumSize(component.getPreferredSize());
		//// Предположительно, это может помочь в борьбе
		//// с проблемой схлопывания текстовых полей:
		// setMinimumSize(getPreferredSize());
		invalidate();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setGeneralForeground(Color c) {
		textColor= c;
		if (individualTextColor==null) {
			setForeground(c);
		} else {
			setForeground(individualTextColor);
		};
		if (colourlessFont==null) {
			colourlessFont= getFont();
		};
		setGeneralFont(colourlessFont);
	}
	//
	public void setGeneralSpaceColor(Color c) {
		spaceColor= c;
		if (colourlessFont==null) {
			colourlessFont= getFont();
		};
		setGeneralFont(colourlessFont);
	}
	//
	public void setGeneralBackground(Color c) {
		if (individualBackgroundColor==null) {
			setBackground(c);
		} else {
			setBackground(individualBackgroundColor);
		}
	}
	//
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
		};
		if (colourlessFont==null) {
			colourlessFont= getFont();
		};
		setGeneralFont(colourlessFont);
	}
	public void setIndividualSpaceColor(Term value, ChoisePoint iX) {
		try {
			individualSpaceColor= ExtendedColor.argumentToColorSafe(value,iX);
		} catch (TermIsSymbolDefault e) {
			individualSpaceColor= null;
		};
		if (colourlessFont==null) {
			colourlessFont= getFont();
		};
		setGeneralFont(colourlessFont);
	}
	public void setIndividualBackgroundColor(Term value, ChoisePoint iX) {
		try {
			individualBackgroundColor= ExtendedColor.argumentToColorSafe(value,iX);
			setBackground(individualBackgroundColor);
		} catch (TermIsSymbolDefault e) {
			individualBackgroundColor= null;
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
	public void paint(Graphics g0) {
		if (!isTransparent.get()) {
			Graphics2D g2= (Graphics2D)g0;
			Color bgColor= getBackground();
			g2.setColor(bgColor);
			int height= getHeight();
			int width= getWidth();
			g2.fillRect(0,0,width,height);
			if (hatchColor!=null) {
				g2.setColor(hatchColor.get());
				int x= 0;
				while(x <= width) {
					g2.fill(new Rectangle2D.Double(x,0,3,height));
					x= x + 10;
				}
			}
		};
		paintBorder(g0);
		super.paintComponents(g0);
	}
}
