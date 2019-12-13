// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import morozov.system.*;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;

public class Java2DDrawImage extends Java2DCommand {
	//
	protected Image image;
	protected Color color= null;
	protected double x1= 0;
	protected double y1= 0;
	//
	public Java2DDrawImage(Image aImage) {
		image= aImage;
	}
	public Java2DDrawImage(Image aImage, Color aC) {
		image= aImage;
		color= aC;
	}
	public Java2DDrawImage(Image aImage, double aX1, double aY1) {
		image= aImage;
		x1= aX1;
		y1= aY1;
	}
	public Java2DDrawImage(Image aImage, double aX1, double aY1, Color aC) {
		image= aImage;
		color= aC;
		x1= aX1;
		y1= aY1;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		int integerX= Arithmetic.toInteger(x1*factorX);
		int integerY= Arithmetic.toInteger(y1*factorY);
		if (color==null) {
			g2.drawImage(image,integerX,integerY,null);
		} else {
			g2.drawImage(image,integerX,integerY,color,null);
		}
	}
}
