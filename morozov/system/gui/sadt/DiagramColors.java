// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import target.*;

import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.signals.*;

import java.awt.Font;
import java.awt.Color;

public class DiagramColors {
	//
	private static final String defaultDiagramFontName= Font.MONOSPACED;
	private static final int defaultDiagramFontSize= 14;
	private static final int defaultDiagramFontStyle= Font.PLAIN;
	//
	public int minimalFontSize= 1;
	//
	public String fontName;
	public int fontSize;
	public int fontStyle;
	//
	public Font initialFont;
	public Font minimalFont;
	//
	private static final Color defaultSuccessDrawingForegroundColor= null;
	private static final Color defaultSuccessDrawingBackgroundColor= Color.GREEN;
	private static final Color defaultSuccessTextForegroundColor= Color.BLACK;
	private static final Color defaultSuccessTextBackgroundColor= null;
	//
	public Color successDrawingForegroundColor;
	public Color successDrawingBackgroundColor;
	public Color successTextForegroundColor;
	public Color successTextBackgroundColor;
	//
	private static final Color defaultFailureDrawingForegroundColor= null;
	private static final Color defaultFailureDrawingBackgroundColor= Color.RED;
	private static final Color defaultFailureTextForegroundColor= Color.BLACK;
	private static final Color defaultFailureTextBackgroundColor= null;
	//
	public Color failureDrawingForegroundColor;
	public Color failureDrawingBackgroundColor;
	public Color failureTextForegroundColor;
	public Color failureTextBackgroundColor;
	//
	private static final Color defaultLabelTextColor= Color.BLACK;
	private static final Color defaultLabelSpaceColor= new Color(0x00E0E0E0);
	//
	public Color labelTextColor;
	public Color labelSpaceColor;
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
			successDrawingForegroundColor= ExtendedColor.argumentToColorSafe(DefaultOptions.successDrawingForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			successDrawingForegroundColor= defaultSuccessDrawingForegroundColor;
		};
		try {
			successDrawingBackgroundColor= ExtendedColor.argumentToColorSafe(DefaultOptions.successDrawingBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			successDrawingBackgroundColor= defaultSuccessDrawingBackgroundColor;
		};
		try {
			successTextForegroundColor= ExtendedColor.argumentToColorSafe(DefaultOptions.successTextForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			successTextForegroundColor= defaultSuccessTextForegroundColor;
		};
		try {
			successTextBackgroundColor= ExtendedColor.argumentToColorSafe(DefaultOptions.successTextBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			successTextBackgroundColor= defaultSuccessTextBackgroundColor;
		};
		initialFont= DiagramUtils.computeFont(fontName,fontSize,fontStyle,successTextForegroundColor,successTextBackgroundColor);
		minimalFont= DiagramUtils.computeFont(fontName,minimalFontSize,fontStyle,successTextForegroundColor,successTextBackgroundColor);
		try {
			failureDrawingForegroundColor= ExtendedColor.argumentToColorSafe(DefaultOptions.failureDrawingForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			failureDrawingForegroundColor= defaultFailureDrawingForegroundColor;
		};
		try {
			failureDrawingBackgroundColor= ExtendedColor.argumentToColorSafe(DefaultOptions.failureDrawingBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			failureDrawingBackgroundColor= defaultFailureDrawingBackgroundColor;
		};
		try {
			failureTextForegroundColor= ExtendedColor.argumentToColorSafe(DefaultOptions.failureTextForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			failureTextForegroundColor= defaultFailureTextForegroundColor;
		};
		try {
			failureTextBackgroundColor= ExtendedColor.argumentToColorSafe(DefaultOptions.failureTextBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			failureTextBackgroundColor= defaultFailureTextBackgroundColor;
		};
		try {
			labelTextColor= ExtendedColor.argumentToColorSafe(DefaultOptions.textLabelForegroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			labelTextColor= defaultLabelTextColor;
		};
		try {
			labelSpaceColor= ExtendedColor.argumentToColorSafe(DefaultOptions.textLabelBackgroundColor,null);
		} catch (TermIsSymbolDefault e1) {
			labelSpaceColor= defaultLabelSpaceColor;
		};
	}
}
