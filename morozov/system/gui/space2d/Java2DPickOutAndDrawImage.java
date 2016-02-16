// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import morozov.terms.*;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;

public class Java2DPickOutAndDrawImage extends Java2DCommand {
	protected Image image;
	protected Color color= null;
	protected double destinationX1= 0;
	protected double destinationY1= 0;
	protected double destinationX2= 0;
	protected double destinationY2= 0;
	protected double sourceX1= 0;
	protected double sourceY1= 0;
	protected double sourceX2= 0;
	protected double sourceY2= 0;
	//
	public Java2DPickOutAndDrawImage(Image aImage, double dX1, double dY1, double dX2, double dY2, double sX1, double sY1, double sX2, double sY2) {
		image= aImage;
		destinationX1= dX1;
		destinationY1= dY1;
		destinationX2= dX2;
		destinationX2= dY2;
		sourceX1= sX1;
		sourceY1= sY1;
		sourceX2= sX2;
		sourceX2= sY2;
	}
	public Java2DPickOutAndDrawImage(Image aImage, double dX1, double dY1, double dX2, double dY2, double sX1, double sY1, double sX2, double sY2, Color aC) {
		image= aImage;
		color= aC;
		destinationX1= dX1;
		destinationY1= dY1;
		destinationX2= dX2;
		destinationX2= dY2;
		sourceX1= sX1;
		sourceY1= sY1;
		sourceX2= sX2;
		sourceX2= sY2;
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		double factorX= drawingMode.getFactorX();
		double factorY= drawingMode.getFactorY();
		int integerDestinationX1= PrologInteger.toInteger(destinationX1*factorX);
		int integerDestinationY1= PrologInteger.toInteger(destinationY1*factorX);
		int integerDestinationX2= PrologInteger.toInteger(destinationX2*factorX);
		int integerDestinationY2= PrologInteger.toInteger(destinationY2*factorX);
		int integerSourceX1= PrologInteger.toInteger(sourceX1*factorX);
		int integerSourceY1= PrologInteger.toInteger(sourceY1*factorX);
		int integerSourceX2= PrologInteger.toInteger(sourceX2*factorX);
		int integerSourceY2= PrologInteger.toInteger(sourceY2*factorX);
		if (color==null) {
			g2.drawImage(
				image,
				integerDestinationX1,
				integerDestinationY1,
				integerDestinationX2,
				integerDestinationY2,
				integerSourceX1,
				integerSourceY1,
				integerSourceX2,
				integerSourceY2,
				null);
		} else {
			g2.drawImage(
				image,
				integerDestinationX1,
				integerDestinationY1,
				integerDestinationX2,
				integerDestinationY2,
				integerSourceX1,
				integerSourceY1,
				integerSourceX2,
				integerSourceY2,
				color,
				null);
		}
	}
}
