/*
 * @(#)ScalableMetalCheckBoxIcon.java 1.0 2009/11/13
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.metal;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.ButtonModel;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/*
 * ScalableMetalCheckboxIcon implementation for the Actor Prolog language
 * @version 1.0 2009/11/13
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableMetalCheckBoxIcon extends ScalableMetalToggleButtonIcon {
	//
	private static final long serialVersionUID= 0x4C3383B794208FC7L; // 5490877195182182343L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.gui.dialogs.scalable.metal","ScalableMetalCheckBoxIcon");
	// }
	//
	public ScalableMetalCheckBoxIcon() {
	}
	//
	@Override
	protected String getGradientKey() {
		return "CheckBox.gradient";
	}
	//
	private void paintOceanEye(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize) {
		ButtonModel model= tb.getModel();
		int w= getIconWidth();
		int h= getIconHeight();
		g2.translate(x,y);
		if (model.isEnabled()) {
			if (model.isPressed() && model.isArmed()) {
				g2.setColor(MetalLookAndFeel.getControlShadow());
				g2.fill(new Rectangle2D.Double(0,0,w,h));
				g2.setColor(MetalLookAndFeel.getControlDarkShadow());
				g2.fill(new Rectangle2D.Double(0,0,w,2));
				g2.fill(new Rectangle2D.Double(0,2,2,h-2));
				g2.fill(new Rectangle2D.Double(w-1,1,1,h-1));
				g2.fill(new Rectangle2D.Double(1,h-1,w-2,1));
			} else if (model.isRollover()) {
				MetalUtils.drawVerticalGradient(tb,g2,gradient,0,0,w,h,false);
				g2.setColor(MetalLookAndFeel.getControlDarkShadow());
				g2.draw(new Rectangle2D.Double(0,0,w-1,h-1));
				g2.setColor(MetalLookAndFeel.getPrimaryControl());
				g2.draw(new Rectangle2D.Double(1,1,w-3,h-3));
				g2.draw(new Rectangle2D.Double(2,2,w-5,h-5));
			} else {
				MetalUtils.drawVerticalGradient(tb,g2,gradient,0,0,w,h,false);
				g2.setColor(MetalLookAndFeel.getControlDarkShadow());
				g2.draw(new Rectangle2D.Double(0,0,w-1,h-1));
			}
		} else {
			g2.setColor(MetalLookAndFeel.getControlDarkShadow());
			g2.draw(new Rectangle2D.Double(0,0,w-1,h-1));
		};
		g2.translate(-x,-y);
	}
	private void paintOrdinaryEye(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize) {
		ButtonModel model= tb.getModel();
		if (model.isEnabled()) {
			if (model.isPressed() && model.isArmed()) {
				g2.setColor(MetalLookAndFeel.getControlShadow());
				g2.fill(new Rectangle2D.Double(x,y,controlSize-1,controlSize-1));
				MetalUtils.drawPressed3DBorder(g2,x,y,controlSize,controlSize);
			} else {
				MetalUtils.drawFlush3DBorder(g2,x,y,controlSize,controlSize);
			}
		} else {
			g2.setColor(MetalLookAndFeel.getControlShadow());
			g2.draw(new Rectangle2D.Double(x,y,controlSize-2,controlSize-2));
		}
	}
	//
	@Override
	protected void drawEye(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize) {
		if (MetalUtils.usingOcean()) {
			paintOceanEye(tb,g2,x,y,controlSize);
		} else {
			paintOrdinaryEye(tb,g2,x,y,controlSize);
		}
	}
	//
	@Override
	protected void drawMarker(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize) {
		ButtonModel model1= tb.getModel();
		if (model1 instanceof ScalableToggleButtonModel) {
			ScalableToggleButtonModel model2= (ScalableToggleButtonModel)model1;
			if(model2.isUncertain()) {
				if (model2.isEnabled()) {
					CheckBoxUtils.drawUncertainMarker(tb,g2,x,y,controlSize,failureBackgroundColor);
				} else {
					CheckBoxUtils.drawUncertainMarker(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlDarkShadow());
				}
			} else if(model2.isSelected()) {
				if (model2.isEnabled()) {
					CheckBoxUtils.drawCheck(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlInfo(),model2.hasClassicStyle());
				} else {
					CheckBoxUtils.drawCheck(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlDarkShadow(),model2.hasClassicStyle());
				}
			}
		} else {
			if(model1.isSelected()) {
				if (model1.isEnabled()) {
					CheckBoxUtils.drawCheck(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlInfo(),false);
				} else {
					CheckBoxUtils.drawCheck(tb,g2,x,y,controlSize,MetalLookAndFeel.getControlDarkShadow(),false);
				}
			}
		}
	}
}
