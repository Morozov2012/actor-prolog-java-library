/*
 * @(#)MetalUtils.java 1.0 2009/12/13
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.metal;

import morozov.terms.*;

import javax.swing.plaf.metal.OceanTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.GradientPaint;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Area;

class MetalUtils {
	static void drawFlush3DBorder(Graphics g0, int x, int y, int w, int h) {
		Graphics2D g2= (Graphics2D)g0;
		g2.translate(x,y);
		g2.setColor(MetalLookAndFeel.getControlDarkShadow());
		g2.draw(new Rectangle2D.Double(0,0,w-2,h-2));
		g2.setColor(MetalLookAndFeel.getControlHighlight());
		g2.draw(new Rectangle2D.Double(1,1,w-2,h-2));
		g2.setColor(MetalLookAndFeel.getControl());
		g2.draw(new Line2D.Double(0,h-1,1,h-2));
		g2.draw(new Line2D.Double(w-1,0,w-2,1));
		g2.translate(-x,-y);
	}
	static void drawPressed3DBorder(Graphics g0, int x, int y, int w, int h) {
		Graphics2D g2= (Graphics2D)g0;
		g2.translate(x,y);
		drawFlush3DBorder(g2,0,0,w,h);
		g2.setColor(MetalLookAndFeel.getControlShadow());
		g2.draw(new Line2D.Double(1,1,1,h-2));
		g2.draw(new Line2D.Double(1,1,w-2,1));
		g2.translate(-x,-y);
	}
	static boolean drawVerticalGradient(Component c, Graphics g0, java.util.List gradient, int x, int y, int w, int h, boolean fillEllipse) {
		if (gradient == null) {
			return false;
		};
		if (w <= 0 || h <= 0) {
			return true;
		};
		Graphics2D g2= (Graphics2D)g0;
		synchronized (c.getTreeLock()) {
			if (fillEllipse) {
				drawVerticalOvalGradient(
					g2,
					((Number)gradient.get(0)).floatValue(),
					((Number)gradient.get(1)).floatValue(),
					(Color)gradient.get(2),
					(Color)gradient.get(3),
					(Color)gradient.get(4),
					w,h);
			} else {
				drawVerticalRectangleGradient(
					g2,
					((Number)gradient.get(0)).floatValue(),
					((Number)gradient.get(1)).floatValue(),
					(Color)gradient.get(2),
					(Color)gradient.get(3),
					(Color)gradient.get(4),
					w,h);
			}
		};
		return true;
	}
	//
	private static void drawVerticalRectangleGradient(Graphics2D g2, float ratio1, float ratio2, Color c1, Color c2, Color c3, int w, int h) {
		int mid= PrologInteger.toInteger(ratio1 * h);
		int mid2= PrologInteger.toInteger(ratio2 * h);
		if (mid > 0) {
			g2.setPaint(getGradient(0,0,c1,0,mid,c2));
			g2.fillRect(0,0,w,mid);
		};
		if (mid2 > 0) {
			g2.setColor(c2);
			g2.fillRect(0,mid,w,mid2);
		};
		if (mid > 0) {
			g2.setPaint(getGradient(0,mid+mid2,c2,0,mid*2+mid2,c1));
			g2.fillRect(0,mid+mid2,w,mid);
		};
		if (h - mid * 2 - mid2 > 0) {
			g2.setPaint(getGradient(0,mid*2+mid2,c1,0,h,c3));
			g2.fillRect(0,mid*2+mid2,w,h-mid*2-mid2);
		}
	}
	private static void drawVerticalOvalGradient(Graphics2D g2, float ratio1, float ratio2, Color c1, Color c2, Color c3, int w, int h) {
		int mid= PrologInteger.toInteger(ratio1 * h);
		int mid2= PrologInteger.toInteger(ratio2 * h);
		Shape shapeOne= new Ellipse2D.Double(0,0,h,w);
		// Shape shapeOne= new Ellipse2D.Double(1,1,h-3,w-3);
		// Shape shapeOne= new Ellipse2D.Double(0,0,h-1,w-1);
		Area areaOne= new Area(shapeOne);
		if (mid > 0) {
			g2.setPaint(getGradient(0,0,c1,0,mid,c2));
			// g2.setPaint(Color.RED);
			// g2.fillRect(0,0,w,mid);
			Shape shapeTwo= new Rectangle2D.Double(0,0,w,mid);
			Area areaTwo= new Area(shapeTwo);
			areaTwo.intersect(areaOne);
			g2.fill(areaTwo);
		};
		if (mid2 > 0) {
			g2.setColor(c2);
			// g2.setPaint(Color.RED);
			// g2.fillRect(0,mid,w,mid2);
			Shape shapeTwo= new Rectangle2D.Double(0,mid,w,mid2);
			Area areaTwo= new Area(shapeTwo);
			areaTwo.intersect(areaOne);
			g2.fill(areaTwo);
		};
		if (mid > 0) {
			g2.setPaint(getGradient(0,mid+mid2,c2,0,mid*2+mid2,c1));
			// g2.setPaint(Color.RED);
			// g2.fillRect(0,mid+mid2,w,mid);
			Shape shapeTwo= new Rectangle2D.Double(0,mid+mid2,w,mid);
			Area areaTwo= new Area(shapeTwo);
			areaTwo.intersect(areaOne);
			g2.fill(areaTwo);
		};
		if (h - mid * 2 - mid2 > 0) {
			g2.setPaint(getGradient(0,mid*2+mid2,c1,0,h,c3));
			// g2.setPaint(Color.RED);
			// g2.fillRect(0,mid*2+mid2,w,h-mid*2-mid2);
			Shape shapeTwo= new Rectangle2D.Double(0,mid*2+mid2,w,h-mid*2-mid2);
			Area areaTwo= new Area(shapeTwo);
			areaTwo.intersect(areaOne);
			g2.fill(areaTwo);
		}
	}
	//
	private static GradientPaint getGradient(double x1, double y1, Color c1, double x2, double y2, Color c2) {
		return new GradientPaint(new Point2D.Double(x1,y1),c1,new Point2D.Double(x2,y2),c2,true);
	}
	//
	protected static boolean usingOcean() {
		return (MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme);
		// return false;
	}
}
