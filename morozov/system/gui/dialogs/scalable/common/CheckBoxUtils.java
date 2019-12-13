/*
 * @(#)CheckBoxUtils.java 1.0 2009/12/29
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * CheckBoxUtils implementation for the Actor Prolog language
 * @version 1.0 2009/12/29
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.system.*;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Component;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;

public class CheckBoxUtils {
	//
	public static void drawCheck(Component relatedComponent, Graphics2D g2, int x, int y, int controlSize, Color markerColor, boolean drawCross) {
		g2.setColor(markerColor);
		int left= x;
		int right= x + controlSize - 1;
		int top= y;
		int bottom= y + controlSize - 1;
		if (drawCross) {
			g2.draw(new Line2D.Double(left+2,top+2,right-2,bottom-2));
			g2.draw(new Line2D.Double(left+2,bottom-2,right-2,top+2));
		} else {
			int dW= Arithmetic.toInteger((right - left) / 10);
			int dH= Arithmetic.toInteger((bottom - top) / 10);
			int x2= Arithmetic.toInteger((left + right) * 0.4) + 1;
			int y12= bottom - 2 - dH;
			int dXL= x2 - left - 1 - dW;
			int dXR= right - x2 - 2 - dW;
			int x1= x2 - dXL + 1;
			int y11= y12 - dXL + 1;
			int x3= x2 + dXR;
			int y13= y12 - dXR;
			int t2= top + dH + 1;
			int delta= 0;
			while (true) {
				if (y13 - delta <= t2) {
					break;
				};
				int y21= y11 - delta;
				int y22= y12 - delta;
				int y23= y13 - delta;
				g2.draw(new Line2D.Double(x2,y22,x1,y21));
				g2.draw(new Line2D.Double(x2,y22,x3,y23));
				delta++;
			}
		}
	}
	//
	public static void drawUncertainMarker(Component relatedComponent, Graphics2D g2, int x, int y, int controlSize, Color markerColor) {
		int left= x;
		int right= x + controlSize - 1;
		int top= y;
		int bottom= y + controlSize - 1;
		//
		int deltaW= Arithmetic.toInteger((right - left) * 0.27);
		int doubleDeltaW= deltaW * 2;
		//
		int deltaH= Arithmetic.toInteger((bottom - top) * 0.27);
		int doubleDeltaH= deltaH * 2;
		//
		g2.setColor(markerColor);
		g2.fill(new Rectangle2D.Double(x+deltaW,y+deltaH,controlSize-doubleDeltaW,controlSize-doubleDeltaH));
	}
}
