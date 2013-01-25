/*
 * @(#)ScalableWindowsRadioButtonIcon.java 1.0 2009/11/13
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.windows;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

/*
 * ScalableWindowsRadioButtonIcon implementation for the Actor Prolog language
 * @version 1.0 2009/11/13
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableWindowsRadioButtonIcon extends ScalableToggleButtonIcon {
	//
	public ScalableWindowsRadioButtonIcon() {
	}
	//
	protected int getSizeCorrection() {
		return 2;
	}
	//
	protected void drawEye(JToggleButton tb, Graphics g0, int x, int y, int controlSize) {
		ButtonModel model= tb.getModel();
		Graphics2D g2= (Graphics2D)g0;
		Color controlHighlight= UIManager.getColor("RadioButton.light");
		Color controlLightHighlight= UIManager.getColor("RadioButton.highlight");
		Color controlShadow= UIManager.getColor("RadioButton.shadow");
		Color controlDarkShadow= UIManager.getColor("RadioButton.darkShadow");
		Color spaceColor;
		if (backgroundColor==null) {
			spaceColor= UIManager.getColor("RadioButton.background");
		} else {
			spaceColor= backgroundColor;
		};
		Color eyeColor= null;
		if((model.isPressed() && model.isArmed()) || !model.isEnabled()) {
			eyeColor= UIManager.getColor("RadioButton.background");
		} else {
			eyeColor= UIManager.getColor("RadioButton.interiorBackground");
		};
		g2.setPaint(spaceColor);
		g2.fill(new Rectangle2D.Double(x,y,controlSize,controlSize));
		g2.draw(new Rectangle2D.Double(x,y,controlSize-1,controlSize-1));
		// (1)
		g2.setPaint(controlHighlight);
		g2.fill(new Ellipse2D.Double(x,y,controlSize-1,controlSize-1));
		g2.setPaint(controlLightHighlight);
		float startAngle= (float) 41;
		float endAngle= (float) 219.2894;
		float angularExtent= (float) (endAngle - startAngle);
		g2.draw(new Arc2D.Double(x,y,controlSize-1,controlSize-1,endAngle,360-angularExtent,Arc2D.OPEN));
		// (2)
		g2.setPaint(controlShadow);
		g2.fill(new Arc2D.Double(x,y,controlSize-1,controlSize-1,startAngle,angularExtent,Arc2D.PIE));
		//
		g2.setPaint(controlDarkShadow);
		g2.draw(new Arc2D.Double(x+1,y+1,controlSize-3,controlSize-3,startAngle,angularExtent,Arc2D.OPEN));
		g2.fill(new Arc2D.Double(x,y,controlSize-1,controlSize-1,startAngle,angularExtent,Arc2D.PIE));
		g2.setPaint(controlShadow);
		g2.draw(new Arc2D.Double(x,y,controlSize-1,controlSize-1,startAngle,angularExtent,Arc2D.OPEN));
		// (3)
		g2.setPaint(eyeColor);
		g2.fill(new Ellipse2D.Double(x+2,y+2,controlSize-5,controlSize-5));
		g2.draw(new Ellipse2D.Double(x+2,y+2,controlSize-5,controlSize-5));
	}
	//
	protected void drawMarker(JToggleButton tb, Graphics g, int x, int y, int controlSize) {
		ButtonModel model1= tb.getModel();
		if (model1 instanceof ScalableToggleButtonModel) {
			ScalableToggleButtonModel model2= (ScalableToggleButtonModel)model1;
			if(model2.isUncertain()) {
				if (model2.isEnabled()) {
					drawUncertainMarker(tb,g,x,y,controlSize,failureBackgroundColor);
				} else {
					drawUncertainMarker(tb,g,x,y,controlSize,UIManager.getColor("RadioButton.shadow"));
				}
			} else if(model2.isSelected()) {
				if (model2.isEnabled()) {
					drawCheck(tb,g,x,y,controlSize,UIManager.getColor("RadioButton.foreground"));
				} else {
					drawCheck(tb,g,x,y,controlSize,UIManager.getColor("RadioButton.shadow"));
				}
			}
		} else {
			if(model1.isSelected()) {
				if (model1.isEnabled()) {
					drawCheck(tb,g,x,y,controlSize,UIManager.getColor("RadioButton.foreground"));
				} else {
					drawCheck(tb,g,x,y,controlSize,UIManager.getColor("RadioButton.shadow"));
				}
			}
		}
	}
	//
	protected void drawCheck(Component relatedComponent, Graphics g, int x, int y, int controlSize, Color markerColor) {
		drawColoredMarker(relatedComponent,g,x,y,controlSize,markerColor);
	}
	//
	protected void drawUncertainMarker(Component relatedComponent, Graphics g, int x, int y, int controlSize, Color markerColor) {
		drawColoredMarker(relatedComponent,g,x,y,controlSize,markerColor);
	}
	//
	protected void drawColoredMarker(Component relatedComponent, Graphics g, int x, int y, int controlSize, Color markerColor) {
		float diameter= (float) ((controlSize - 4) * 0.41);
		Graphics2D g2= (Graphics2D)g;
		int deltaW= (int)StrictMath.round((controlSize - diameter)/2);
		int doubleDeltaW= deltaW * 2;
		int deltaH= (int)StrictMath.round((controlSize - diameter)/2);
		int doubleDeltaH= deltaH * 2;
		g2.setPaint(markerColor);
		g2.fill(new Ellipse2D.Float(x+deltaW,y+deltaH,controlSize-doubleDeltaW-1,controlSize-doubleDeltaH-1));
		g2.draw(new Ellipse2D.Float(x+deltaW,y+deltaH,controlSize-doubleDeltaW-1,controlSize-doubleDeltaH-1));
	}
}
