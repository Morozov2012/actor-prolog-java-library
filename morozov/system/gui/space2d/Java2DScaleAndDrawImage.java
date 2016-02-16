// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import morozov.terms.*;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;

public class Java2DScaleAndDrawImage extends Java2DCommand {
	protected Image image;
	protected Color color= null;
	protected double x1= 0;
	protected double y1= 0;
	protected double width;
	protected double height;
	//
	public Java2DScaleAndDrawImage(Image aImage, double aX1, double aY1, double aWidth, double aHeight) {
		image= aImage;
		x1= aX1;
		y1= aY1;
		width= aWidth;
		height= aHeight;
	}
	public Java2DScaleAndDrawImage(Image aImage, double aX1, double aY1, double aWidth, double aHeight, Color aC) {
		image= aImage;
		color= aC;
		x1= aX1;
		y1= aY1;
		width= aWidth;
		height= aHeight;
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		int integerX= PrologInteger.toInteger(x1*factorX);
		int integerY= PrologInteger.toInteger(y1*factorY);
		int integerWidth= PrologInteger.toInteger(width*factorX);
		int integerHeight= PrologInteger.toInteger(height*factorY);
		if (color==null) {
			g2.drawImage(image,integerX,integerY,integerWidth,integerHeight,null);
		} else {
			g2.drawImage(image,integerX,integerY,integerWidth,integerHeight,color,null);
		}
	}
}
