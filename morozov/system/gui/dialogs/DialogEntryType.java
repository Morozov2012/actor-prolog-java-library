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
import morozov.system.converters.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.awt.Font;

public enum DialogEntryType {
	VALUE {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getSlotByName(name);
		}
		@Override
		public boolean isValueOrAction() {
			return true;
		}
		@Override
		public boolean isValueActionOrRange() {
			return true;
		}
	},
	RANGE {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getSlotByName(name);
		}
		@Override
		public boolean isValueActionOrRange() {
			return true;
		}
	},
	ACTION {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public boolean isValueOrAction() {
			return true;
		}
		@Override
		public boolean isValueActionOrRange() {
			return true;
		}
	},
	BUILT_IN_ACTION {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public boolean isValueOrAction() {
			return true;
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public boolean isValueActionOrRange() {
			return true;
		}
	},
	TITLE {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedTitle title= ExtendedTitle.argumentToExtendedTitleSafe(value,iX);
			dialog.changeTitle(title);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			String title= dialog.safelyGetTitle();
			if (title==null) {
				title= "";
			};
			return new PrologString(title);
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getTitleOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
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
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ColorAttribute color= ColorAttributeConverters.argumentToColorAttributeSafe(value,iX);
			dialog.changeTextColor(color,true,iX);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			Color foregroundColor= dialog.safelyGetForegroundColor();
			if (foregroundColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(foregroundColor.getRGB());
			}
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getTextColorOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ColorAttributeConverters.standardizeColorValue(value,iX);
		}
	},
	SPACE_COLOR {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ColorAttribute color= ColorAttributeConverters.argumentToColorAttributeSafe(value,iX);
			dialog.changeSpaceColor(color,true,iX);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			Color spaceColor= dialog.safelyGetBackgroundColor();
			if (spaceColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(spaceColor.getRGB());
			}
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getSpaceColorOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ColorAttributeConverters.standardizeColorValue(value,iX);
		}
	},
	BACKGROUND_COLOR {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ColorAttribute color= ColorAttributeConverters.argumentToColorAttributeSafe(value,iX);
			dialog.changeBackgroundColor(color,true,iX);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			Color backgroundColor= dialog.currentSuccessBackgroundColor.get();
			if (backgroundColor==null) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} else {
				return new PrologInteger(backgroundColor.getRGB());
			}
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getBackgroundColorOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ColorAttributeConverters.standardizeColorValue(value,iX);
		}
	},
	FONT_NAME {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedFontName fontName= ExtendedFontName.argumentToExtendedFontNameSafe(value,iX);
			dialog.changeFontName(fontName,iX);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			return new PrologString(dialog.getCurrentFontName());
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getFontNameOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedFontName.standardizeFontNameValue(value,iX);
		}

	},
	FONT_SIZE {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedFontSize fontSize= ExtendedFontSize.argumentToExtendedFontSizeSafe(value,iX);
			dialog.changeFontSize(fontSize,iX);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			int size2= dialog.getCurrentFontSize();
			int size1= DefaultOptions.fontSystemSimulationMode.reconstruct(size2);
			return new PrologInteger(size2);
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getFontSizeOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedFontSize.standardizeFontSizeValue(value,iX);
		}
	},
	FONT_STYLE {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedFontStyle fontStyle= ExtendedFontStyle.argumentToExtendedFontStyleSafe(value,iX);
			dialog.changeFontStyle(fontStyle,iX);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			int fontStyle= dialog.getCurrentFontStyle();
			boolean isBold= (fontStyle & Font.BOLD) != 0;
			boolean isItalic= (fontStyle & Font.ITALIC) != 0;
			boolean isUnderline= dialog.getCurrentFontUnderline();
			return ExtendedFontStyle.fontStyleToTerm(isBold,isItalic,isUnderline);
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getFontStyleOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedFontStyle.standardizeFontStyleValue(value,iX);
		}
	},
	X {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedCoordinate x= ExtendedCoordinate.argumentToExtendedCoordinateSafe(value,iX);
			dialog.changeActualX(x,iX);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			return new PrologReal(dialog.getActualX());
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getXOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedCoordinate.standardizeCoordinateValue(value,iX);
		}
	},
	Y {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			ExtendedCoordinate y= ExtendedCoordinate.argumentToExtendedCoordinateSafe(value,iX);
			dialog.changeActualY(y,iX);
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			return new PrologReal(dialog.getActualY());
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			try {
				return targetWorld.getYOrFail(iX);
			} catch (Backtracking b) {
				return targetWorld.getSlotByName(name);
			}
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return ExtendedCoordinate.standardizeCoordinateValue(value,iX);
		}
	},
	IDENTIFIER {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setIdentifier(DialogIdentifierOrAuto.argumentToDialogIdentifierOrAuto(value,iX));
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return targetWorld.getIdentifier(iX).toTerm();
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return DialogIdentifierOrAuto.standardizeValue(value,iX);
		}
	},
	IS_MODAL {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setIsModal(YesNoDefaultConverters.argument2YesNoDefault(value,iX));
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return YesNoDefaultConverters.toTerm(targetWorld.getIsModal(iX));
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return YesNoDefaultConverters.standardizeValue(value,iX);
		}
	},
	IS_TOP_LEVEL_WINDOW {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setIsTopLevelWindow(YesNoConverters.argument2YesNo(value,iX));
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return YesNoConverters.toTerm(targetWorld.getIsTopLevelWindow(iX));
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return YesNoConverters.standardizeValue(value,iX);
		}
	},
	IS_ALWAYS_ON_TOP {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setIsAlwaysOnTop(YesNoConverters.argument2YesNo(value,iX));
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return YesNoConverters.toTerm(targetWorld.getIsAlwaysOnTop(iX));
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return YesNoConverters.standardizeValue(value,iX);
		}
	},
	CLOSING_CONFIRMATION {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setClosingConfirmation(OnOffConverters.argument2OnOff(value,iX));
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return OnOffConverters.toTerm(targetWorld.getClosingConfirmation(iX));
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return YesNoConverters.standardizeValue(value,iX);
		}
	},
	IS_EXIT_ON_CLOSE {
		@Override
		void putValue(AbstractDialog dialog, Term value, ChoisePoint iX) {
			dialog.getTargetWorld().setExitOnClose(YesNoDefaultConverters.argument2YesNoDefault(value,iX));
		}
		@Override
		Term getValue(AbstractDialog dialog) {
			throw new IllegalCallOfInternalMethod();
		}
		@Override
		public Term getSlotByName(String name, Dialog targetWorld, ChoisePoint iX) {
			return YesNoDefaultConverters.toTerm(targetWorld.getExitOnClose(iX));
		}
		@Override
		public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
			return YesNoDefaultConverters.standardizeValue(value,iX);
		}
	};
	abstract void putValue(AbstractDialog dialog, Term value, ChoisePoint iX);
	abstract Term getValue(AbstractDialog dialog);
	abstract Term getSlotByName(String name, Dialog dialog, ChoisePoint iX);
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
