// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Java2DDrawLine extends Java2DCommand {
	//
	protected double x1;
	protected double y1;
	protected double x2;
	protected double y2;
	//
	public Java2DDrawLine(double aX1, double aY1, double aX2, double aY2) {
		x1= aX1;
		y1= aY1;
		x2= aX2;
		y2= aY2;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		g2.draw(new Line2D.Double(x1*factorX,y1*factorY,x2*factorX,y2*factorY));
	}
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DDrawLine) ) {
				return false;
			} else {
				Java2DDrawLine i= (Java2DDrawLine) o;
				if (	i.x1==x1 &&
					i.y1==y1 &&
					i.x2==x2 &&
					i.y2==y2
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
