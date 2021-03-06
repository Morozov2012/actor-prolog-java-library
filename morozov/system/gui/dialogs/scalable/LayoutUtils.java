/*
 * @(#)LayoutUtils.java 1.0 2010/05/02
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import morozov.system.*;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Component;

public class LayoutUtils {
	//
	public static Insets calculateLayoutInsets(
			GridBagConstraints gBC,
			Font font,
			FontMetrics metrics,
			double horizontalPadding,
			double verticalPadding,
			double horizontalScaling,
			double verticalScaling,
			boolean isTop,
			boolean isLeft,
			boolean isBottom,
			boolean isRight,
			int topBorder,
			int leftBorder,
			int bottomBorder,
			int rightBorder) {
		FontRenderContext frc= metrics.getFontRenderContext();
		TextLayout layout= new TextLayout("M",font,frc);
		Rectangle rectangle= layout.getPixelBounds(null,0,0);
		double charWidth= rectangle.getWidth();
		double charHeight= rectangle.getHeight();
		if (isTop) {
			topBorder= topBorder + Arithmetic.toInteger(verticalPadding*charHeight);
		};
		if (isLeft) {
			leftBorder= leftBorder + Arithmetic.toInteger(horizontalPadding*charWidth);
		};
		if (isBottom) {
			bottomBorder= bottomBorder + Arithmetic.toInteger(verticalPadding*charHeight);
		};
		if (isRight) {
			rightBorder= rightBorder + Arithmetic.toInteger(horizontalPadding*charWidth);
		};
		return new Insets(topBorder,leftBorder,bottomBorder,rightBorder);
	}
	//
	public static Dimension computeDimension(Font font, Component component, double width, double height) {
		FontMetrics metrics= component.getFontMetrics(font);
		int charWidth= metrics.charWidth('M');
		int currentWidth= Arithmetic.toInteger(width*charWidth);
		FontRenderContext frc= metrics.getFontRenderContext();
		TextLayout layout= new TextLayout("M",font,frc);
		Rectangle rectangle= layout.getPixelBounds(null,0,0);
		double charHeight= rectangle.getHeight();
		int currentHeight= Arithmetic.toInteger(height*charHeight);
		Dimension dimension= new Dimension(currentWidth,currentHeight);
		return dimension;
	}
}
