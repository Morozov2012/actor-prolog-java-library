/*
 * @(#)DialogEntryType.java 1.0 2010/03/21
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
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
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getSlotByName(name);
		}
		public boolean isValueOrAction() {
			return true;
		}
		public boolean isValueActionOrRange() {
			return true;
		}
	},
	RANGE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getSlotByName(name);
		}
		public boolean isValueActionOrRange() {
			return true;
		}
	},
	ACTION {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		public boolean isValueOrAction() {
			return true;
		}
		public boolean isValueActionOrRange() {
			return true;
		}
	},
	BUILT_IN_ACTION {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		public boolean isValueOrAction() {
			return true;
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		public boolean isValueActionOrRange() {
			return true;
		}
	},
	TITLE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedTitle title= ExtendedTitle.argumentToExtendedTitleSafe(value,iX);
			dialog.changeTitle(title);
		}
		Term getValue(AbstractDialog dialog) {
			String title= dialog.safelyGetTitle();
			if (title==null) {
				title= "";
			};
			return new PrologString(title);
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getTitleOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
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
	TEXT_COLOR {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedColor color= ExtendedColor.argumentToExtendedColorSafe(value,iX);
			dialog.changeTextColor(color,true,iX);
			// dialog.safelyRepaint();
		}
		Term getValue(AbstractDialog dialog) {
			Color foregroundColor= dialog.safelyGetForegroundColor();
			if (foregroundColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(foregroundColor.getRGB());
			}
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getTextColorOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedColor.standardizeColorValue(value,iX);
		}
	},
	SPACE_COLOR {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedColor color= ExtendedColor.argumentToExtendedColorSafe(value,iX);
			dialog.changeSpaceColor(color,true,iX);
			// dialog.safelyRepaint();
		}
		Term getValue(AbstractDialog dialog) {
			Color spaceColor= dialog.safelyGetBackgroundColor();
			if (spaceColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(spaceColor.getRGB());
			}
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getSpaceColorOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedColor.standardizeColorValue(value,iX);
		}
	},
	BACKGROUND_COLOR {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedColor color= ExtendedColor.argumentToExtendedColorSafe(value,iX);
			dialog.changeBackgroundColor(color,true,iX);
			// dialog.safelyRepaint();
		}
		Term getValue(AbstractDialog dialog) {
			Color backgroundColor= dialog.currentSuccessBackgroundColor.get();
			if (backgroundColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(backgroundColor.getRGB());
			}
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getBackgroundColorOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedColor.standardizeColorValue(value,iX);
		}
	},
	FONT_NAME {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedFontName fontName= ExtendedFontName.argumentToExtendedFontNameSafe(value,iX);
			dialog.changeFontName(fontName,iX);
		}
		Term getValue(AbstractDialog dialog) {
			return new PrologString(dialog.getCurrentFontName());
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getFontNameOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedFontName.standardizeFontNameValue(value,iX);
		}

	},
	FONT_SIZE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedFontSize fontSize= ExtendedFontSize.argumentToExtendedFontSizeSafe(value,iX);
			dialog.changeFontSize(fontSize,iX);
		}
		Term getValue(AbstractDialog dialog) {
			int size2= dialog.getCurrentFontSize();
			int size1= DefaultOptions.fontSystemSimulationMode.reconstruct(size2);
			// return new PrologReal(size2);
			return new PrologInteger(size2);
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getFontSizeOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedFontSize.standardizeFontSizeValue(value,iX);
		}
	},
	FONT_STYLE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedFontStyle fontStyle= ExtendedFontStyle.argumentToExtendedFontStyleSafe(value,iX);
			dialog.changeFontStyle(fontStyle,iX);
		}
		Term getValue(AbstractDialog dialog) {
			int fontStyle= dialog.getCurrentFontStyle();
			boolean isBold= (fontStyle & Font.BOLD) != 0;
			boolean isItalic= (fontStyle & Font.ITALIC) != 0;
			boolean isUnderline= dialog.getCurrentFontUnderline();
			return ExtendedFontStyle.fontStyleToTerm(isBold,isItalic,isUnderline);
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getFontStyleOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedFontStyle.standardizeFontStyleValue(value,iX);
		}
	},
	X {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedCoordinate x= ExtendedCoordinate.argumentToExtendedCoordinateSafe(value,iX);
			dialog.changeActualX(x,iX);
		}
		Term getValue(AbstractDialog dialog) {
			return new PrologReal(dialog.getActualX());
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getXOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedCoordinate.standardizeCoordinateValue(value,iX);
		}
	},
	Y {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedCoordinate y= ExtendedCoordinate.argumentToExtendedCoordinateSafe(value,iX);
			dialog.changeActualY(y,iX);
		}
		Term getValue(AbstractDialog dialog) {
			return new PrologReal(dialog.getActualY());
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getYOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedCoordinate.standardizeCoordinateValue(value,iX);
		}
	},
	IDENTIFIER {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setIdentifier(DialogIdentifierOrAuto.argumentToDialogIdentifierOrAuto(value,iX));
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getIdentifier(iX).toTerm();
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogIdentifierOrAuto.standardizeValue(value,iX);
		}
	},
	IS_MODAL {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setIsModal(YesNoDefault.term2YesNoDefault(value,iX));
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getIsModal(iX).toTerm();
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return YesNoDefault.standardizeValue(value,iX);
		}
	},
	IS_TOP_LEVEL_WINDOW {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setIsTopLevelWindow(YesNo.term2YesNo(value,iX));
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getIsTopLevelWindow(iX).toTerm();
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return YesNo.standardizeValue(value,iX);
		}
	},
	IS_EXIT_ON_CLOSE {
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setExitOnClose(YesNoDefault.term2YesNoDefault(value,iX));
		}
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getExitOnClose(iX).toTerm();
		}
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return YesNoDefault.standardizeValue(value,iX);
		}
	};
	abstract void putValue(AbstractDialog dialog, Term value, ChoisePoint iX);
	abstract Term getValue(AbstractDialog dialog);
	abstract Term getSlotByName(String name, Dialog dialog, ChoisePoint iX);
	// abstract Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue;
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	}
	public boolean isValueOrAction() {
		return false;
	}
	public boolean isValueActionOrRange() {
		return false;
	}
}
