// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import target.*;

import morozov.system.converters.*;
import morozov.system.gui.*;
import morozov.system.signals.*;

import java.awt.Font;
import java.awt.Color;

public class DiagramColors {
	//
	protected static final String defaultDiagramFontName= Font.MONOSPACED;
	protected static final int defaultDiagramFontSize= 14;
	protected static final int defaultDiagramFontStyle= Font.PLAIN;
	//
	protected int minimalFontSize= 1;
	//
	protected String fontName;
	protected int fontSize;
	protected int fontStyle;
	//
	protected Font initialFont;
	protected Font minimalFont;
	//
	protected static final Color defaultSuccessDrawingForegroundColor= null;
	protected static final Color defaultSuccessDrawingBackgroundColor= Color.GREEN;
	protected static final Color defaultSuccessTextForegroundColor= Color.BLACK;
	protected static final Color defaultSuccessTextBackgroundColor= null;
	//
	protected Color successDrawingForegroundColor;
	protected Color successDrawingBackgroundColor;
	protected Color successTextForegroundColor;
	protected Color successTextBackgroundColor;
	//
	protected static final Color defaultFailureDrawingForegroundColor= null;
	protected static final Color defaultFailureDrawingBackgroundColor= Color.RED;
	protected static final Color defaultFailureTextForegroundColor= Color.BLACK;
	protected static final Color defaultFailureTextBackgroundColor= null;
	//
	protected Color failureDrawingForegroundColor;
	protected Color failureDrawingBackgroundColor;
	protected Color failureTextForegroundColor;
	protected Color failureTextBackgroundColor;
	//
	protected static final Color defaultLabelTextColor= Color.BLACK;
	protected static final Color defaultLabelSpaceColor= new Color(0x00E0E0E0);
	//
	protected Color labelTextColor;
	protected Color labelSpaceColor;
	//
	public DiagramColors() {
		try {
			fontName= ExtendedFontName.argumentToFontName(DefaultOptions.diagramFontName,null);
		} catch (TermIsSymbolDefault e1) {
			fontName= defaultDiagramFontName;
		};
		try {
			fontSize= ExtendedFontSize.argumentToFontSize(DefaultOptions.diagramFontSize,null);
		} catch (TermIsSymbolDefault e1) {
			fontSize= defaultDiagramFontSize;
		};
		try {
			fontStyle= ExtendedFontStyle.argumentToFontStyleSafe(DefaultOptions.diagramFontStyle,null);
		} catch (TermIsSymbolDefault e1) {
			fontStyle= defaultDiagramFontStyle;
		};
		try {
			successDrawingForegroundColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.successDrawingForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			successDrawingForegroundColor= defaultSuccessDrawingForegroundColor;
		};
		try {
			successDrawingBackgroundColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.successDrawingBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			successDrawingBackgroundColor= defaultSuccessDrawingBackgroundColor;
		};
		try {
			successTextForegroundColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.successTextForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			successTextForegroundColor= defaultSuccessTextForegroundColor;
		};
		try {
			successTextBackgroundColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.successTextBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			successTextBackgroundColor= defaultSuccessTextBackgroundColor;
		};
		initialFont= DiagramUtils.computeFont(fontName,fontSize,fontStyle,successTextForegroundColor,successTextBackgroundColor);
		minimalFont= DiagramUtils.computeFont(fontName,minimalFontSize,fontStyle,successTextForegroundColor,successTextBackgroundColor);
		try {
			failureDrawingForegroundColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.failureDrawingForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			failureDrawingForegroundColor= defaultFailureDrawingForegroundColor;
		};
		try {
			failureDrawingBackgroundColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.failureDrawingBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			failureDrawingBackgroundColor= defaultFailureDrawingBackgroundColor;
		};
		try {
			failureTextForegroundColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.failureTextForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			failureTextForegroundColor= defaultFailureTextForegroundColor;
		};
		try {
			failureTextBackgroundColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.failureTextBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			failureTextBackgroundColor= defaultFailureTextBackgroundColor;
		};
		try {
			labelTextColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.textLabelForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			labelTextColor= defaultLabelTextColor;
		};
		try {
			labelSpaceColor= ColorAttributeConverters.argumentToColorSafe(DefaultOptions.textLabelBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			labelSpaceColor= defaultLabelSpaceColor;
		};
	}
}
