/*
 * @(#)ScalableWindowsCheckBoxIcon.java 1.0 2009/11/27
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.windows;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.UIManager;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;

/*
 * ScalableWindowsCheckBoxIcon implementation for the Actor Prolog language
 * @version 1.0 2009/11/27
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableWindowsCheckBoxIcon extends ScalableToggleButtonIcon {
	//
	public ScalableWindowsCheckBoxIcon() {
	}
	//
	protected int getSizeCorrection() {
		return 3;
	}
	//
	protected void drawEye(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize) {
		JCheckBox cb= (JCheckBox)tb;
		ButtonModel model= cb.getModel();
		// Graphics2D g2= (Graphics2D)g0;
		Color controlShadow= UIManager.getColor("CheckBox.shadow");
		Color eyeColor= null;
		if((model.isPressed() && model.isArmed()) || !model.isEnabled()) {
			eyeColor= UIManager.getColor("CheckBox.background");
		} else {
			eyeColor= UIManager.getColor("CheckBox.interiorBackground");
		};
		if(!cb.isBorderPaintedFlat()) {
			// y++;
			Color controlLightHighlight= UIManager.getColor("CheckBox.highlight");
			Color controlDarkShadow= UIManager.getColor("CheckBox.darkShadow");
			Color controlHighlight= UIManager.getColor("CheckBox.light");
			g2.setColor(eyeColor);
			g2.fill(new Rectangle2D.Double(x,y,controlSize,controlSize));
			// ERCT = rct(L,T,R,B),
			int left= x;
			int right= x + controlSize - 1;
			int top= y;
			int bottom= y + controlSize - 1;
			int xR1= right - 1;
			int yB1= bottom - 1;
			// win_SetPen(W,pen(1,ps_Solid,color_Gray)),
			g2.setColor(controlShadow);
			// draw_Line(W,pnt(L,T),pnt(X_R1,T)),
			g2.draw(new Line2D.Double(left,top,xR1,top));
			// draw_line(W,pnt(L,T),pnt(L,Y_B1)),
			g2.draw(new Line2D.Double(left,top,left,yB1));
			// (6)
			// win_SetPen(W,pen(1,ps_Solid,color_White)),
			g2.setColor(controlLightHighlight);
			// draw_Line(W,pnt(L,Y_B1),pnt(R,Y_B1)),
			g2.draw(new Line2D.Double(left,bottom,right,bottom));
			// draw_line(W,pnt(X_R1,T),pnt(X_R1,B)),
			g2.draw(new Line2D.Double(right,top,right,bottom));
			// (7)
			int xL1= left + 1;
			int yT1= top + 1;
			int xR2= xR1 - 1;
			int yB2= yB1 - 1;
			// win_SetPen(W,pen(1,ps_Solid,color_Black)),
			g2.setColor(controlDarkShadow);
			// draw_Line(W,pnt(X_L1,Y_T1),pnt(X_R2,Y_T1)),
			g2.draw(new Line2D.Double(xL1,yT1,xR2,yT1));
			// draw_line(W,pnt(X_L1,Y_T1),pnt(X_L1,Y_B2)),
			g2.draw(new Line2D.Double(xL1,yT1,xL1,yB2));
			// (8)
			// win_SetPen(W,pen(1,ps_Solid,color_LtGray)),
			g2.setColor(controlHighlight);
			// draw_Line(W,pnt(X_L1,Y_B2),pnt(X_R1,Y_B2)),
			g2.draw(new Line2D.Double(xL1,yB1,xR1,yB1));
			// draw_line(W,pnt(X_R2,Y_T1),pnt(X_R2,Y_B2)).
			g2.draw(new Line2D.Double(xR1,yT1,xR1,yB1));
			// g.setColor(controlInfo);
		} else {
			g2.setColor(eyeColor);
			g2.fill(new Rectangle2D.Double(x+2,y+2,controlSize-4,controlSize-4));
			g2.setColor(controlShadow);
			g2.draw(new Rectangle2D.Double(x+1,y+1,controlSize-3, controlSize-3));
		}
	}
	//
	protected void drawMarker(JToggleButton tb, Graphics2D g2, int x, int y, int controlSize) {
		ButtonModel model1= tb.getModel();
		if (model1 instanceof ScalableToggleButtonModel) {
			ScalableToggleButtonModel model2= (ScalableToggleButtonModel)model1;
			if(model2.isUncertain()) {
				if (model2.isEnabled()) {
					CheckBoxUtils.drawUncertainMarker(tb,g2,x,y,controlSize,failureBackgroundColor);
				} else {
					CheckBoxUtils.drawUncertainMarker(tb,g2,x,y,controlSize,UIManager.getColor("CheckBox.shadow"));
				}
			} else if(model2.isSelected()) {
				if (model2.isEnabled()) {
					CheckBoxUtils.drawCheck(tb,g2,x,y,controlSize,UIManager.getColor("CheckBox.foreground"),model2.hasClassicStyle());
				} else {
					CheckBoxUtils.drawCheck(tb,g2,x,y,controlSize,UIManager.getColor("CheckBox.shadow"),model2.hasClassicStyle());
				}
			}
		} else {
			if(model1.isSelected()) {
				if (model1.isEnabled()) {
					CheckBoxUtils.drawCheck(tb,g2,x,y,controlSize,UIManager.getColor("CheckBox.foreground"),false);
				} else {
					CheckBoxUtils.drawCheck(tb,g2,x,y,controlSize,UIManager.getColor("CheckBox.shadow"),false);
				}
			}
		}
	}
}
