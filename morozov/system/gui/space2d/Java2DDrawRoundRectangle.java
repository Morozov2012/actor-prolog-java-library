// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class Java2DDrawRoundRectangle extends Java2DCommand {
	//
	protected double x1;
	protected double y1;
	protected double figureWidth;
	protected double figureHeight;
	protected double arcW;
	protected double arcH;
	//
	public Java2DDrawRoundRectangle(double aX1, double aY1, double aH, double aW, double aArcW, double aArcH) {
		x1= aX1;
		y1= aY1;
		figureWidth= aH;
		figureHeight= aW;
		arcW= aArcW;
		arcH= aArcH;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		drawingMode.drawShape(g2,new RoundRectangle2D.Double(x1*factorX,y1*factorY,figureWidth*factorX,figureHeight*factorY,arcW*factorX,arcH*factorY));
	}
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DDrawRoundRectangle) ) {
				return false;
			} else {
				Java2DDrawRoundRectangle i= (Java2DDrawRoundRectangle) o;
				if (	i.x1==x1 &&
					i.y1==y1 &&
					i.figureWidth==figureWidth &&
					i.figureHeight==figureHeight &&
					i.arcW==arcW &&
					i.arcH==arcH
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
