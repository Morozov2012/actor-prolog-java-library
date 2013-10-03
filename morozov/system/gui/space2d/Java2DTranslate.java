// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;

public class Java2DTranslate extends Java2DCommand {
	protected double kX;
	protected double kY;
	public Java2DTranslate(double aX, double aY) {
		kX= aX;
		kY= aY;
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		g2.translate(kX*factorX,kY*factorY);
	}
}
