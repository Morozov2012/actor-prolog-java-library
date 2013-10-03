/*
 * @(#)DialogEntryType.java 1.0 2010/03/21
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs;

import target.*;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.signals.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Font;

class IllegalCallOfInternalMethod extends RuntimeException {}

public enum DialogEntryType {
	VALUE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
	},
	RANGE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
	},
	TITLE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			String title= null;
			try {
				title= GUI_Utils.termToFrameTitleSafe(value,iX);
			} catch (TermIsSymbolDefault e1) {
				title= dialog.getPredefinedTitle();
			};
			dialog.setTitle(title);
		}
		Term getValue(AbstractDialog dialog) {
			String title= dialog.getTitle();
			if (title==null) {
				title= "";
			};
			return new PrologString(title);
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			value= value.dereferenceValue(iX);
			if (value.thisIsUnknownValue()) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				try {
					long code= value.getSymbolValue(iX);
					if (code==SymbolCodes.symbolCode_E_default) {
						return new PrologSymbol(code);
					} else {
						throw RejectValue.instance;
					}
				} catch (TermIsNotASymbol e1) {
					try {
						String text= value.getStringValue(iX);
						return new PrologString(text);
					} catch (TermIsNotAString e2) {
						throw RejectValue.instance;
					}
				}
			}
		}
	},
	X {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedCoordinate actualX= GUI_Utils.termToCoordinateSafe(value,iX);
			try {
				Rectangle bounds= dialog.computeParentLayoutSize();
				double gridX= DefaultOptions.gridWidth;
				Point p= dialog.getLocation();
				Dimension size= dialog.getSize();
				p.x= DialogUtils.calculateRealCoordinate(actualX,bounds.x,bounds.width,gridX,size.getWidth());
				dialog.setLocation(p);
			} catch (UseDefaultLocation e) {
			}
		}
		Term getValue(AbstractDialog dialog) {
			Rectangle bounds= dialog.computeParentLayoutSize();
			double gridX= DefaultOptions.gridWidth;
			Point p= dialog.getLocation();
			return new PrologReal((double)p.x/(((double)(bounds.width-bounds.x))/gridX));
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogUtils.standardizeCoordinateValue(value,iX);
		}
	},
	Y {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedCoordinate actualY= GUI_Utils.termToCoordinateSafe(value,iX);
			try {
				Rectangle bounds= dialog.computeParentLayoutSize();
				double gridY= DefaultOptions.gridHeight;
				Point p= dialog.getLocation();
				Dimension size= dialog.getSize();
				p.y= DialogUtils.calculateRealCoordinate(actualY,bounds.y,bounds.height,gridY,size.getHeight());
				dialog.setLocation(p);
			} catch (UseDefaultLocation e) {
			//} catch (Throwable eee) {
			//	throw eee;
			}
		}
		Term getValue(AbstractDialog dialog) {
			Rectangle bounds= dialog.computeParentLayoutSize();
			double gridY= DefaultOptions.gridHeight;
			Point p= dialog.getLocation();
			return new PrologReal((double)p.y/(((double)(bounds.height-bounds.y))/gridY));
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogUtils.standardizeCoordinateValue(value,iX);
		}
	},
	TEXT_COLOR {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			Color foregroundColor= null;
			try {
				foregroundColor= GUI_Utils.termToColorSafe(value,iX);
			} catch (TermIsSymbolDefault e1) {
				try {
					foregroundColor= GUI_Utils.termToColorSafe(dialog.getPredefinedTextColor(),iX);
				} catch (TermIsSymbolDefault e2) {
					foregroundColor= dialog.defaultDialogTextColor;
				}
			};
			dialog.setNewForeground(foregroundColor);
			dialog.repaint();
		}
		Term getValue(AbstractDialog dialog) {
			Color foregroundColor= dialog.getForeground();
			if (foregroundColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(foregroundColor.getRGB());
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogUtils.standardizeColorValue(value,iX);
		}
	},
	SPACE_COLOR {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			Color spaceColor= null;
			try {
				spaceColor= GUI_Utils.termToColorSafe(value,iX);
			} catch (TermIsSymbolDefault e1) {
				try {
					spaceColor= GUI_Utils.termToColorSafe(dialog.getPredefinedSpaceColor(),iX);
				} catch (TermIsSymbolDefault e2) {
					spaceColor= dialog.defaultDialogSpaceColor;
				}
			};
			dialog.setNewSpaceColor(spaceColor);
			dialog.repaint();
		}
		Term getValue(AbstractDialog dialog) {
			Color spaceColor= dialog.getBackground();
			if (spaceColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(spaceColor.getRGB());
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogUtils.standardizeColorValue(value,iX);
		}
	},
	BACKGROUND_COLOR {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			Color backgroundColor= null;
			try {
				backgroundColor= GUI_Utils.termToColorSafe(value,iX);
			} catch (TermIsSymbolDefault e1) {
				try {
					backgroundColor= GUI_Utils.termToColorSafe(dialog.getPredefinedBackgroundColor(),iX);
				} catch (TermIsSymbolDefault e2) {
					backgroundColor= dialog.defaultDialogSuccessBackgroundColor;
				}
			};
			dialog.currentSuccessBackgroundColor.set(backgroundColor);
			Color refinedBackgroundColor= dialog.refineBackgroundColor(backgroundColor);
			dialog.setNewBackground(refinedBackgroundColor);
			dialog.repaint();
		}
		Term getValue(AbstractDialog dialog) {
			Color backgroundColor= dialog.currentSuccessBackgroundColor.get();
			if (backgroundColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(backgroundColor.getRGB());
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogUtils.standardizeColorValue(value,iX);
		}
	},
	FONT_NAME {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			String fontName= null;
			try {
				fontName= GUI_Utils.termToFontNameSafe(value,iX);
			} catch (TermIsSymbolDefault e1) {
				try {
					fontName= GUI_Utils.termToFontName(dialog.getPredefinedFontName(),iX);
				} catch (TermIsSymbolDefault e2) {
					try {
						fontName= GUI_Utils.termToFontName(DefaultOptions.dialogFontName,iX);
					} catch (TermIsSymbolDefault e3) {
						fontName= dialog.defaultDialogFontName;
					}
				}
			};
			dialog.setCurrentFontName(fontName);
			Font newFont= dialog.create_new_font();
			dialog.setNewFont(newFont);
			dialog.implementPreferredSize();
			dialog.validate();
		}
		Term getValue(AbstractDialog dialog) {
			// Font oldFont= dialog.getFont();
			// return new PrologString(oldFont.getFamily());
			return new PrologString(dialog.currentFontName.get());
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogUtils.standardizeFontNameValue(value,iX);
		}

	},
	FONT_SIZE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			int fontSize= dialog.defaultDialogFontSize;
			try {
				fontSize= GUI_Utils.termToFontSizeSafe(value,iX);
			} catch (TermIsSymbolDefault e1) {
				try {
					fontSize= GUI_Utils.termToFontSize(dialog.getPredefinedFontSize(),iX);
				} catch (TermIsSymbolDefault e2) {
					try {
						fontSize= GUI_Utils.termToFontSize(DefaultOptions.dialogFontSize,iX);
					} catch (TermIsSymbolDefault e3) {
						fontSize= dialog.defaultDialogFontSize;
					}
				}
			};
			if (fontSize < 1) {
				fontSize= 1;
			};
			dialog.setCurrentFontSize(fontSize);
			Font newFont= dialog.create_new_font();
			dialog.setNewFont(newFont);
			dialog.implementPreferredSize();
			dialog.validate();
		}
		Term getValue(AbstractDialog dialog) {
			// Font oldFont= dialog.getFont();
			// return new PrologInteger(oldFont.getSize());
			return new PrologReal(dialog.currentFontSize.get());
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogUtils.standardizeFontSizeValue(value,iX);
		}
	},
	FONT_STYLE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			int fontStyle= Font.PLAIN;
			boolean fontUnderline= false;
			try {
				fontStyle= GUI_Utils.termToFontStyleSafe(value,iX);
				fontUnderline= GUI_Utils.fontIsUnderlined(value,iX);
			} catch (TermIsSymbolDefault e1) {
				try {
					fontStyle= GUI_Utils.termToFontStyleSafe(dialog.getPredefinedFontStyle(),iX);
					fontUnderline= GUI_Utils.fontIsUnderlined(dialog.getPredefinedFontStyle(),iX);
				} catch (TermIsSymbolDefault e2) {
					try {
						fontStyle= GUI_Utils.termToFontStyleSafe(DefaultOptions.dialogFontStyle,iX);
						fontUnderline= GUI_Utils.fontIsUnderlined(DefaultOptions.dialogFontStyle,iX);
					} catch (TermIsSymbolDefault e3) {
						fontStyle= dialog.defaultDialogFontStyle;
						fontUnderline= dialog.defaultDialogFontUnderline;
					}
				}
			};
			dialog.setCurrentFontStyle(fontStyle);
			dialog.setCurrentFontUnderline(fontUnderline);
			Font newFont= dialog.create_new_font();
			dialog.setNewFont(newFont);
			dialog.implementPreferredSize();
			// dialog.validate();
			// dialog.repaint();
			dialog.invalidate(); // 2012.03.05
		}
		Term getValue(AbstractDialog dialog) {
			// Font oldFont= dialog.getFont();
			int fontStyle= dialog.currentFontStyle.get();
			boolean isBold= (fontStyle & Font.BOLD) != 0;
			boolean isItalic= (fontStyle & Font.ITALIC) != 0;
			boolean isUnderline= dialog.currentFontUnderline.get();
			return GUI_Utils.fontStyleToTerm(isBold,isItalic,isUnderline);
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogUtils.standardizeFontStyleValue(value,iX);
		}
	};
	abstract void putValue(AbstractDialog dialog, Term value, ChoisePoint iX);
	abstract Term getValue(AbstractDialog dialog);
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	}
}
