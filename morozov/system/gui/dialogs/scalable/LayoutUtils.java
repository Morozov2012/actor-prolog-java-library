/*
 * @(#)LayoutUtils.java 1.0 2010/05/02
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.Rectangle;

public class LayoutUtils {
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
		// FontMetrics metrics= getFontMetrics(font);
		// int charWidth= metrics.charWidth('M');
		FontRenderContext frc= metrics.getFontRenderContext();
		TextLayout layout= new TextLayout("M",font,frc);
		Rectangle rectangle= layout.getPixelBounds(null,0,0);
		double charWidth= rectangle.getWidth();
		double charHeight= rectangle.getHeight();
		// Rectangle2D rectangle2D= font.getMaxCharBounds(frc);
		// double charHeight= rectangle2D.getHeight();
		//
		// GridBagConstraints gBC= gridBagLayout.getConstraints(this);
		// int topBorder= getInitialTopBorder();
		// int leftBorder= getInitialLeftBorder();
		// int bottomBorder= getInitialBottomBorder();
		// int rightBorder= getInitialRightBorder();
		if (isTop) {
			topBorder= topBorder + (int)StrictMath.round(verticalPadding*charHeight);
		};
		if (isLeft) {
			leftBorder= leftBorder + (int)StrictMath.round(horizontalPadding*charWidth);
		};
		if (isBottom) {
			bottomBorder= bottomBorder + (int)StrictMath.round(verticalPadding*charHeight);
		};
		if (isRight) {
			rightBorder= rightBorder + (int)StrictMath.round(horizontalPadding*charWidth);
		};
		// System.out.printf("topBorder=%d,leftBorder=%d,bottomBorder=%d,rightBorder=%d\n",topBorder,leftBorder,bottomBorder,rightBorder);
		return new Insets(topBorder,leftBorder,bottomBorder,rightBorder);
	}
}
