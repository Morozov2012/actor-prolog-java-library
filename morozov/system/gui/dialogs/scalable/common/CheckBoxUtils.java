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

import morozov.terms.*;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Component;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;

public class CheckBoxUtils {
	public static void drawCheck(Component relatedComponent, Graphics2D g2, int x, int y, int controlSize, Color markerColor, boolean drawCross) {
		// Graphics2D g2= (Graphics2D)g0;
		g2.setColor(markerColor);
		// ERCT = rct(L,T,R,B),
		int left= x;
		int right= x + controlSize - 1;
		int top= y;
		int bottom= y + controlSize - 1;
		if (drawCross) {
			// ERCT = rct(L,T,R,B),
			// R1= R - 1,
			// B1= B - 1,
			// win_SetPen(W,pen(1,ps_Solid,color_Black)),
			// g.setColor(Color.GREEN);
			// draw_Line(W,pnt(L,T),pnt(R1,B1)),
			g2.draw(new Line2D.Double(left+2,top+2,right-2,bottom-2));
			// g.setColor(Color.RED);
			// draw_Line(W,pnt(L,B1),pnt(R1,T)).
			g2.draw(new Line2D.Double(left+2,bottom-2,right-2,top+2));
		} else {
			// DW= val(integer,round((R - L) / 10)),
			int dW= PrologInteger.toInteger((right - left) / 10);
			// DH= val(integer,round((B - T) / 10)),
			int dH= PrologInteger.toInteger((bottom - top) / 10);
			// X2= val(integer,round((L + R) * 0.4)),
			// int x2= PrologInteger.toInteger((left + right) * 0.4);
			int x2= PrologInteger.toInteger((left + right) * 0.4) + 1;
			// Y12= B - 3 - DH,
			// int y12= bottom - 3 - dH;
			int y12= bottom - 2 - dH;
			// DxL= X2 - L - 1 - DW,
			// int dXL= x2 - left - 1 - dW;
			int dXL= x2 - left - 1 - dW;
			// DxR= R - X2 - 2 - DW,
			int dXR= right - x2 - 2 - dW;
			// X1= X2 - DxL,
			// int x1= x2 - dXL;
			int x1= x2 - dXL + 1;
			// Y11= Y12 - DxL,
			// int y11= y12 - dXL;
			int y11= y12 - dXL + 1;
			// X3= X2 + DxR,
			int x3= x2 + dXR;
			// Y13= Y12 - DxR,
			int y13= y12 - dXR;
			// T2= T + DH,
			int t2= top + dH + 1;
			// draw_Vs(W,X1,Y11,X2,Y12,X3,Y13,0,T2).
			// win_SetPen(W,pen(1,ps_Solid,color_Black)),
			int delta= 0;
			while(true) {
				if (y13 - delta <= t2) {
					break;
				};
				int y21= y11 - delta;
				int y22= y12 - delta;
				int y23= y13 - delta;
				// draw_Line(W,pnt(X2,Y22),pnt(X1,Y21)),
				g2.draw(new Line2D.Double(x2,y22,x1,y21));
				// draw_Line(W,pnt(X2,Y22),pnt(X3,Y23)).
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
		int deltaW= PrologInteger.toInteger((right - left) * 0.27);
		int doubleDeltaW= deltaW * 2;
		//
		int deltaH= PrologInteger.toInteger((bottom - top) * 0.27);
		int doubleDeltaH= deltaH * 2;
		//
		// Graphics2D g2= (Graphics2D)g0;
		g2.setColor(markerColor);
		g2.fill(new Rectangle2D.Double(x+deltaW,y+deltaH,controlSize-doubleDeltaW,controlSize-doubleDeltaH));
	}
}
