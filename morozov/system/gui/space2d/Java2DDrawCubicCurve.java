// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;

public class Java2DDrawCubicCurve extends Java2DCommand {
	protected double x1;
	protected double y1;
	protected double ctrlX1;
	protected double ctrlY1;
	protected double ctrlX2;
	protected double ctrlY2;
	protected double x2;
	protected double y2;
	public Java2DDrawCubicCurve(double aX1, double aY1, double aCtrlX1, double aCtrlY1, double aCtrlX2, double aCtrlY2, double aX2, double aY2) {
		x1= aX1;
		y1= aY1;
		ctrlX1= aCtrlX1;
		ctrlY1= aCtrlY1;
		ctrlX2= aCtrlX2;
		ctrlY2= aCtrlY2;
		x2= aX2;
		y2= aY2;
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		drawingMode.drawShape(g2,new CubicCurve2D.Double(x1*factorX,y1*factorY,ctrlX1*factorX,ctrlY1*factorY,ctrlX2*factorX,ctrlY2*factorY,x2*factorX,y2*factorY));
	}
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DDrawCubicCurve) ) {
				return false;
			} else {
				Java2DDrawCubicCurve i= (Java2DDrawCubicCurve) o;
				if (	i.x1==x1 &&
					i.y1==y1 &&
					i.ctrlX1==ctrlX1 &&
					i.ctrlY1==ctrlY1 &&
					i.ctrlX2==ctrlX2 &&
					i.ctrlY2==ctrlY2 &&
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
