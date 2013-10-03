// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Java2DDrawRectangle extends Java2DCommand {
	protected double x1;
	protected double y1;
	protected double figureWidth;
	protected double figureHeight;
	public Java2DDrawRectangle(double aX1, double aY1, double aH, double aW) {
		x1= aX1;
		y1= aY1;
		figureWidth= aH;
		figureHeight= aW;
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		drawingMode.drawShape(g2,new Rectangle2D.Double(x1*factorX,y1*factorY,figureWidth*factorX,figureHeight*factorY));
	}
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DDrawRectangle) ) {
				return false;
			} else {
				Java2DDrawRectangle i= (Java2DDrawRectangle) o;
				if (	i.x1==x1 &&
					i.y1==y1 &&
					i.figureWidth==figureWidth &&
					i.figureHeight==figureHeight
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
