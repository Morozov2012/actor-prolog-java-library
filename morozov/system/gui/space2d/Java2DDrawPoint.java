// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Java2DDrawPoint extends Java2DCommand {
	protected double x1;
	protected double y1;
	public Java2DDrawPoint(double aX1, double aY1) {
		x1= aX1;
		y1= aY1;
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		g2.draw(new Line2D.Double(x1*factorX,y1*factorY,x1*factorX,y1*factorY));
	}
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DDrawPoint) ) {
				return false;
			} else {
				Java2DDrawPoint i= (Java2DDrawPoint) o;
				if (	i.x1==x1 &&
					i.y1==y1
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
