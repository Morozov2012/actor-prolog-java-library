// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class Java2DDrawPolygon extends Java2DCommand {
	protected double[] xPoints;
	protected double[] yPoints;
	protected int length;
	public Java2DDrawPolygon(double[] aX, double[] aY) {
		xPoints= aX;
		yPoints= aY;
		length= StrictMath.min(xPoints.length,yPoints.length);
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		Path2D polygon= new Path2D.Double(Path2D.WIND_EVEN_ODD,length);
		polygon.moveTo(xPoints[0]*factorX,yPoints[0]*factorY);
		for (int n= 1; n < length; n++) {
			polygon.lineTo(xPoints[n]*factorX,yPoints[n]*factorY);
		};
		polygon.closePath();
		drawingMode.drawShape(g2,polygon);
	}
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DDrawPolygon) ) {
				return false;
			} else {
				Java2DDrawPolygon i= (Java2DDrawPolygon) o;
				if (	i.xPoints.length==xPoints.length &&
					i.yPoints.length==yPoints.length &&
					i.length==length
						) {
					for (int n= 0; n < xPoints.length; n++) {
						if (xPoints[n] != i.xPoints[n]) {
							return false;
						}
					};
					for (int n= 0; n < yPoints.length; n++) {
						if (yPoints[n] != i.yPoints[n]) {
							return false;
						}
					};
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
