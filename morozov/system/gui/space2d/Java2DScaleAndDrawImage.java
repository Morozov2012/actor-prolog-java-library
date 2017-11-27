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
		int integerWidth;
		int integerHeight;
		if (width > 0 && height > 0) {
			integerWidth= PrologInteger.toInteger(width*factorX);
			integerHeight= PrologInteger.toInteger(height*factorY);
		} else if (width <= 0 && height <= 0) {
			int imageWidth= image.getWidth(null);
			int imageHeight= image.getHeight(null);
			double imageRatio= (double)imageWidth / imageHeight;
			int windowWidth= drawingMode.getWindowWidth();
			int windowHeight= drawingMode.getWindowHeight();
			double actualWindowWidth= windowWidth*(1.0-x1);
			double actualWindowHeight= windowHeight*(1.0-y1);
			double windowRatio= actualWindowWidth / actualWindowHeight;
			if (windowRatio > imageRatio) {
				integerHeight= PrologInteger.toInteger(actualWindowHeight);
				integerWidth= PrologInteger.toInteger(actualWindowHeight*imageRatio);
			} else {
				integerWidth= PrologInteger.toInteger(actualWindowWidth);
				integerHeight= PrologInteger.toInteger(actualWindowWidth/imageRatio);
			}
		} else {
			int imageWidth= image.getWidth(null);
			int imageHeight= image.getHeight(null);
			double imageRatio= (double)imageWidth / imageHeight;
			if (width <= 0) {
				int windowHeight= drawingMode.getWindowHeight();
				double actualWindowHeight= height*windowHeight;
				integerHeight= PrologInteger.toInteger(actualWindowHeight);
				integerWidth= PrologInteger.toInteger(actualWindowHeight*imageRatio);
			} else {
				int windowWidth= drawingMode.getWindowWidth();
				double actualWindowWidth= width*windowWidth;
				integerWidth= PrologInteger.toInteger(actualWindowWidth);
				integerHeight= PrologInteger.toInteger(actualWindowWidth/imageRatio);
			}
		};
		if (color==null) {
			g2.drawImage(image,integerX,integerY,integerWidth,integerHeight,null);
		} else {
			g2.drawImage(image,integerX,integerY,integerWidth,integerHeight,color,null);
		}
	}
}
