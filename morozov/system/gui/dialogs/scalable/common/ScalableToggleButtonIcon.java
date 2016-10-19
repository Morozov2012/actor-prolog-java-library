/*
 * @(#)ScalableToggleButtonIcon.java 1.0 2009/11/18
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ScalableToggleButtonIcon implementation for the Actor Prolog language
 * @version 1.0 2009/11/18
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.system.gui.*;
import morozov.terms.*;

import javax.swing.JToggleButton;
import javax.swing.Icon;
import javax.swing.plaf.UIResource;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.HashMap;

public abstract class ScalableToggleButtonIcon implements Icon, UIResource, Serializable, Cloneable {
	//
	// protected Color uncertaincyColor;
	protected Component relatedComponent;
	protected Font givenFont;
	protected HashMap<Font,Integer> measuredFonts;
	protected Font measuredFont;
	protected int measuredSize;
	protected Color backgroundColor;
	protected Color failureForegroundColor;
	protected Color failureBackgroundColor;
	//
	public ScalableToggleButtonIcon() {
		initiate();
	}
	// public ScalableToggleButtonIcon(Color color) {
	//	uncertaincyColor= color;
	// }
	//
	public void initiate() {
		// uncertaincyColor= Color.MAGENTA;
		relatedComponent= null;
		givenFont= null;
		measuredFonts= new HashMap<Font,Integer>();
		measuredFont= null;
		measuredSize= 10;
		backgroundColor= null;
		failureForegroundColor= null;
		failureBackgroundColor= null;
	}
	//
	public ScalableToggleButtonIcon makeCopy() {
		try {
			ScalableToggleButtonIcon copy= (ScalableToggleButtonIcon)clone();
			copy.initiate();
			return copy;
		} catch (CloneNotSupportedException e) {
			return this;
		}
	}
	//
	public void setFont(Component relatedComponent, Font font, double horizontalScaling) {
		if (measuredFont!=font) {
			givenFont= font;
			Integer size= measuredFonts.get(givenFont);
			if (size!=null) {
				measuredSize= size;
			} else if (relatedComponent!=null) {
				// Graphics2D g2D= (Graphics2D)relatedComponent.getGraphics();
				Graphics2D g2D= DesktopUtils.safelyGetGraphics2D(relatedComponent);
				if (g2D!=null) {
					FontRenderContext fRC= g2D.getFontRenderContext();
					Rectangle2D r2D= givenFont.getStringBounds("M",fRC);
					// Returns an integer Rectangle that completely encloses the Shape:
					Rectangle rect= r2D.getBounds();
					int width= PrologInteger.toInteger(rect.getWidth());
					// int height= PrologInteger.toInteger(rect.getHeight());
					measuredSize= PrologInteger.toInteger(width*horizontalScaling);
					measuredFonts.put(givenFont,measuredSize);
				}
			}
		}
	}
	public void setBackground(Component relatedComponent, Color color) {
		backgroundColor= color;
	}
	public void setAlarmColors(Color fc, Color bc) {
		failureForegroundColor= fc;
		failureBackgroundColor= bc;
	}
	//
	protected int getControlSize() {
		// return 130;
		return (int)StrictMath.floor(measuredSize + getSizeCorrection());
		// return 13;
	}
	//
	protected int getSizeCorrection() {
		return 3;
	}
	//
	public int getIconWidth() {
		return getControlSize();
	}
	//
	public int getIconHeight() {
		return getControlSize();
	}
	//
	public void paintIcon(Component relatedComponent, Graphics g0, int x, int y) {
		Graphics gg= g0.create();
		try {
			Graphics2D g2= (Graphics2D)gg;
			// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			// g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			JToggleButton tb= (JToggleButton)relatedComponent;
			int controlSize= getControlSize();
			drawEye(tb,g2,x,y,controlSize);
			drawMarker(tb,g2,x,y,controlSize);
		} finally {
			gg.dispose();
		}
	}
	//
	abstract protected void drawEye(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize);
	abstract protected void drawMarker(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize);
}
