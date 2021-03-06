/*
 * @(#)ActiveFieldErrorBorder.java 1.0 2009/11/23
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ActiveFieldErrorBorder implementation for the Actor Prolog language
 * @version 1.0 2009/11/23
 * @author IRE RAS Alexei A. Morozov
*/

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Color;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import javax.swing.border.EmptyBorder;

public class ActiveFieldErrorBorder extends EmptyBorder {
	//
	protected Component component;
	protected Color failureForegroundColor;
	protected Color failureBackgroundColor;
	//
	public ActiveFieldErrorBorder(Component c, Insets insets) {
		super(insets);
		component= c;
	}
	//
	public void setAlarmColors(Color fc, Color bc) {
		failureForegroundColor= fc;
		failureBackgroundColor= bc;
	}
	//
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2= (Graphics2D)g;
		//
		Stroke oS= g2.getStroke();
		try {
			float dash1[]= {5.0f,1.0f};
			BasicStroke solid= new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
			BasicStroke dashed= new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,dash1,0.0f);
			g2.setPaint(failureForegroundColor);
			g2.setStroke(solid);
			g2.draw(new Rectangle2D.Float(x+1,y+1,width-2,height-2));
			g2.setStroke(dashed);
			g2.setPaint(failureBackgroundColor);
			g2.draw(new Rectangle2D.Float(x+1,y+1,width-2,height-2));
			g2.setStroke(oS);
		} catch (RuntimeException e) {
			g2.setStroke(oS);
			throw e;
		}
	}
}
