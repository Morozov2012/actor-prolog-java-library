/*
 * @(#)ScalableMetalRadioButtonIcon.java 1.0 2009/11/13
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.metal;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.ButtonModel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Component;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.RenderingHints;

/*
 * ScalableMetalRadioButtonIcon implementation for the Actor Prolog language
 * @version 1.0 2009/11/13
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableMetalRadioButtonIcon extends ScalableMetalToggleButtonIcon {
	//
	private static final long serialVersionUID= 0x9782EAEF5B228C2CL; // -7529197313262973908L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.gui.dialogs.scalable.metal","ScalableMetalRadioButtonIcon");
	// }
	//
	public ScalableMetalRadioButtonIcon() {
	}
	//
	@Override
	protected int getSizeCorrection() {
		return 2;
	}
	//
	@Override
	protected String getGradientKey() {
		return "RadioButton.gradient";
	}
	//
	public void paintOceanEye(Component c, Graphics2D g2, int x, int y, int controlSize) {
		ButtonModel model= ((JRadioButton)c).getModel();
		boolean enabled= model.isEnabled();
		boolean pressed= (enabled && model.isPressed() && model.isArmed());
		boolean rollover= (enabled && model.isRollover());
		g2.translate(x,y);
		if (enabled && !pressed) {
			MetalUtils.drawVerticalGradient(c,g2,gradient,0,0,controlSize,controlSize,true);
		} else if (pressed || !enabled) {
			if (pressed) {
				g2.setColor(MetalLookAndFeel.getPrimaryControl());
			} else {
				g2.setColor(MetalLookAndFeel.getControl());
			};
			g2.fill(new Ellipse2D.Double(0,0,controlSize-1,controlSize-1));
		};
		if (pressed) {
			double startAngle= 61.3895;
			double endAngle= 212.4712;
			double angularExtent= endAngle - startAngle;
			Shape shapeOne= new Arc2D.Double(0,0,controlSize-1,controlSize-1,startAngle,angularExtent,Arc2D.PIE);
			Shape shapeTwo= new Ellipse2D.Double(1,1,controlSize-3,controlSize-3);
			Area areaOne= new Area(shapeOne);
			Area areaTwo= new Area(shapeTwo);
			areaOne.subtract(areaTwo);
			if (!enabled) {
				g2.setColor(MetalLookAndFeel.getInactiveControlTextColor());
			} else {
				g2.setColor(MetalLookAndFeel.getControlDarkShadow());
			};
			g2.fill(areaOne);
			g2.draw(new Arc2D.Double(1,1,controlSize-3,controlSize-3,startAngle,angularExtent,Arc2D.OPEN));
		} else if (rollover) {
			int ringWidth= 2;
			Shape shapeOne= new Ellipse2D.Double(0,0,controlSize-1,controlSize-1);
			Shape shapeTwo= new Ellipse2D.Double(ringWidth,ringWidth,controlSize-1-ringWidth*2,controlSize-1-ringWidth*2);
			Area areaOne= new Area(shapeOne);
			Area areaTwo= new Area(shapeTwo);
			areaOne.subtract(areaTwo);
			g2.setColor(MetalLookAndFeel.getPrimaryControl());
			g2.fill(areaOne);
		};
		// Draw Dark Circle (start at top, go clockwise):
		if (!enabled) {
			g2.setColor(MetalLookAndFeel.getInactiveControlTextColor());
		} else {
			g2.setColor(MetalLookAndFeel.getControlDarkShadow());
		};
		g2.draw(new Ellipse2D.Double(0,0,controlSize-1,controlSize-1));
		g2.translate(-x,-y);
	}
	//
	public void paintOrdinaryEye(Component c, Graphics2D g2, int x, int y, int controlSize) {
		JRadioButton rb= (JRadioButton)c;
		ButtonModel model= rb.getModel();
		boolean drawDot= model.isSelected();
		//
		Color background= c.getBackground();
		Color shadow= MetalLookAndFeel.getControlShadow();
		Color darkCircle= MetalLookAndFeel.getControlDarkShadow();
		Color whiteInnerLeftArc= MetalLookAndFeel.getControlHighlight();
		Color whiteOuterRightArc= MetalLookAndFeel.getControlHighlight();
		Color interiorColor= background;
		// Set up colors per RadioButtonModel condition:
		if (!model.isEnabled()) {
			whiteOuterRightArc= background;
			whiteInnerLeftArc= whiteOuterRightArc;
			darkCircle= shadow;
		} else if (model.isPressed() && model.isArmed() ) {
			interiorColor= shadow;
			whiteInnerLeftArc= interiorColor;
			interiorColor= shadow;
		};
		g2.translate(x,y);
		// Draw Outer Right White Arc:
		// Start at upper right corner, go clockwise:
		g2.setColor(whiteOuterRightArc);
		{	double mainAngle= 41;
			double startAngle= 270 - mainAngle;
			double endAngle= 45;
			double angularExtent= mainAngle + 90 + endAngle;
			Shape shapeOne= new Arc2D.Double(-1,-1,controlSize+1,controlSize+1,startAngle,angularExtent,Arc2D.PIE);
			Shape shapeTwo= new Ellipse2D.Double(1,1,controlSize-3,controlSize-3);
			Area areaOne= new Area(shapeOne);
			Area areaTwo= new Area(shapeTwo);
			areaOne.subtract(areaTwo);
			g2.fill(areaOne);
			g2.draw(new Arc2D.Double(-1,-1,controlSize+1,controlSize+1,startAngle,angularExtent,Arc2D.OPEN));
		};
		// Fill interior:
		g2.setColor(interiorColor);
		g2.fill(new Ellipse2D.Double(0,0,controlSize-1,controlSize-1));
		g2.setColor(whiteInnerLeftArc);
		{	double startAngle= 41;
			double endAngle= 270 - startAngle;
			double angularExtent= endAngle - startAngle;
			Shape shapeOne= new Arc2D.Double(0,0,controlSize-1,controlSize-1,startAngle,angularExtent,Arc2D.PIE);
			Shape shapeTwo= new Ellipse2D.Double(1,1,controlSize-3,controlSize-3);
			Area areaOne= new Area(shapeOne);
			Area areaTwo= new Area(shapeTwo);
			areaOne.subtract(areaTwo);
			g2.fill(areaOne);
			g2.draw(new Arc2D.Double(1,1,controlSize-3,controlSize-3,startAngle,angularExtent,Arc2D.OPEN));
		};
		// Draw Dark Circle (start at top, go clockwise):
		g2.setColor(darkCircle);
		g2.draw(new Ellipse2D.Double(0,0,controlSize-1,controlSize-1));
		g2.translate(-x,-y);
	}
	//
	@Override
	protected void drawEye(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		if (MetalUtils.usingOcean()) {
			paintOceanEye(tb,g2,x,y,controlSize);
		} else {
			paintOrdinaryEye(tb,g2,x,y,controlSize);

		};
	}
	//
	@Override
	protected void drawMarker(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		ButtonModel model1= tb.getModel();
		if (model1 instanceof ScalableToggleButtonModel) {
			ScalableToggleButtonModel model2= (ScalableToggleButtonModel)model1;
			if(model2.isUncertain()) {
				if (model2.isEnabled()) {
					drawUncertainMarker(tb,g2,x,y,controlSize,failureBackgroundColor);
				} else {
					drawUncertainMarker(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlDarkShadow());
				}
			} else if(model2.isSelected()) {
				if (model2.isEnabled()) {
					drawCheck(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlInfo());
				} else {
					drawCheck(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlDarkShadow());
				}
			}
		} else {
			if(model1.isSelected()) {
				if (model1.isEnabled()) {
					drawCheck(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlInfo());
				} else {
					drawCheck(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlDarkShadow());
				}
			}
		}
	}
	//
	protected void drawCheck(Component relatedComponent, Graphics2D g2, int x, int y, int controlSize, Color markerColor) {
		drawColoredMarker(relatedComponent,g2,x,y,controlSize,markerColor);
	}
	//
	protected void drawUncertainMarker(Component relatedComponent, Graphics2D g2, int x, int y, int controlSize, Color markerColor) {
		drawColoredMarker(relatedComponent,g2,x,y,controlSize,markerColor);
	}
	//
	protected void drawColoredMarker(Component relatedComponent, Graphics2D g2, int x, int y, int controlSize, Color markerColor) {
		double diameter= controlSize * 0.55;
		double deltaW= (controlSize - diameter)/2;
		double doubleDeltaW= deltaW * 2;
		double deltaH= (controlSize - diameter)/2;
		double doubleDeltaH= deltaH * 2;
		g2.setPaint(markerColor);
		g2.fill(new Ellipse2D.Double(x+deltaW,y+deltaH,controlSize-doubleDeltaW-1,controlSize-doubleDeltaH-1));
		g2.draw(new Ellipse2D.Double(x+deltaW,y+deltaH,controlSize-doubleDeltaW-1,controlSize-doubleDeltaH-1));
	}
}
