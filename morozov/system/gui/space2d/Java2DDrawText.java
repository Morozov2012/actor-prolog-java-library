// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;

public class Java2DDrawText extends Java2DCommand {
	//
	protected double x1;
	protected double y1;
	protected String text;
	//
	public Java2DDrawText(double aX1, double aY1, String aT) {
		x1= aX1;
		y1= aY1;
		text= aT;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		drawingMode.drawString(g2,x1*factorX,y1*factorY,text);
	}
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DDrawText) ) {
				return false;
			} else {
				Java2DDrawText i= (Java2DDrawText) o;
				if (	i.x1==x1 &&
					i.y1==y1 &&
					i.text.equals(text)
						) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
