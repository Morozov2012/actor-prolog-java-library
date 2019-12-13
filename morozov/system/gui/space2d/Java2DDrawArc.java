// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

public class Java2DDrawArc extends Java2DCommand {
	//
	protected double x1;
	protected double y1;
	protected double figureWidth;
	protected double figureHeight;
	protected double start;
	protected double extent;
	protected int type;
	//
	public Java2DDrawArc(double aX1, double aY1, double aH, double aW, double aStart, double aExtent, int aType) {
		x1= aX1;
		y1= aY1;
		figureWidth= aH;
		figureHeight= aW;
		start= aStart;
		extent= aExtent;
		type= aType;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		drawingMode.drawShape(g2,new Arc2D.Double(x1*factorX,y1*factorY,figureWidth*factorX,figureHeight*factorY,start,extent,type));
	}
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DDrawArc) ) {
				return false;
			} else {
				Java2DDrawArc i= (Java2DDrawArc) o;
				if (	i.x1==x1 &&
					i.y1==y1 &&
					i.figureWidth==figureWidth &&
					i.figureHeight==figureHeight &&
					i.start==start &&
					i.extent==extent &&
					i.type==type
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
