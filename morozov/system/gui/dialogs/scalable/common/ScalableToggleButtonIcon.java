/*
 * @(#)ScalableToggleButtonIcon.java 1.0 2009/11/18
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;

/*
 * ScalableToggleButtonIcon implementation for the Actor Prolog language
 * @version 1.0 2009/11/18
 * @author IRE RAS Alexei A. Morozov
*/

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
				Graphics2D g2D= (Graphics2D)relatedComponent.getGraphics();
				if (g2D!=null) {
					FontRenderContext fRC= g2D.getFontRenderContext();
					Rectangle2D r2D= givenFont.getStringBounds("M",fRC);
					Rectangle rect= r2D.getBounds();
					int width= (int)StrictMath.round(rect.getWidth());
					int height= (int)StrictMath.round(rect.getHeight());
					measuredSize= (int)StrictMath.round(width*horizontalScaling);
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
		return measuredSize + getSizeCorrection();
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
	public void paintIcon(Component relatedComponent, Graphics g, int x, int y) {
		JToggleButton tb= (JToggleButton)relatedComponent;
		int controlSize= getControlSize();
		drawEye(tb,g,x,y,controlSize);
		drawMarker(tb,g,x,y,controlSize);
	}
	//
	abstract protected void drawEye(JToggleButton tb, Graphics g, int x, int y, int controlSize);
	abstract protected void drawMarker(JToggleButton tb, Graphics g, int x, int y, int controlSize);
}
