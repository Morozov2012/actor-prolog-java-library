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

import morozov.system.*;
import morozov.system.gui.*;

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
	protected Component relatedComponent;
	protected Font givenFont;
	protected HashMap<Font,Integer> measuredFonts;
	protected Font measuredFont;
	protected int measuredSize;
	protected Color backgroundColor;
	protected Color failureForegroundColor;
	protected Color failureBackgroundColor;
	//
	private static final long serialVersionUID= 0x161EBFBAF1465C4AL; // 1593922127768476746L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.gui.dialogs.scalable.common","ScalableToggleButtonIcon");
	// }
	//
	public ScalableToggleButtonIcon() {
		initiate();
	}
	//
	public void initiate() {
		relatedComponent= null;
		givenFont= null;
		measuredFonts= new HashMap<>();
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
				Graphics2D g2D= DesktopUtils.safelyGetGraphics2D(relatedComponent);
				if (g2D!=null) {
					try {
						FontRenderContext fRC= g2D.getFontRenderContext();
						Rectangle2D r2D= givenFont.getStringBounds("M",fRC);
						// Returns an integer Rectangle that completely encloses the Shape:
						Rectangle rect= r2D.getBounds();
						int width= Arithmetic.toInteger(rect.getWidth());
						measuredSize= Arithmetic.toInteger(width*horizontalScaling);
						measuredFonts.put(givenFont,measuredSize);
					} finally {
						g2D.dispose();
					}
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
		return (int)StrictMath.floor(measuredSize + getSizeCorrection());
	}
	//
	protected int getSizeCorrection() {
		return 3;
	}
	//
	@Override
	public int getIconWidth() {
		return getControlSize();
	}
	//
	@Override
	public int getIconHeight() {
		return getControlSize();
	}
	//
	@Override
	public void paintIcon(Component relatedComponent, Graphics g0, int x, int y) {
		Graphics gg= g0.create();
		try {
			Graphics2D g2= (Graphics2D)gg;
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
