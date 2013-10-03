// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

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
		int integerX= (int)StrictMath.round(x1*factorX);
		int integerY= (int)StrictMath.round(y1*factorY);
		int integerWidth= (int)StrictMath.round(width*factorX);
		int integerHeight= (int)StrictMath.round(height*factorY);
		// System.out.printf("W.H.: %s %s\n",width,height);
		// System.out.printf("integer W.H.: %s %s\n",integerWidth,integerHeight);
		if (color==null) {
			g2.drawImage(image,integerX,integerY,integerWidth,integerHeight,null);
		} else {
			g2.drawImage(image,integerX,integerY,integerWidth,integerHeight,color,null);
		}
	}
}
