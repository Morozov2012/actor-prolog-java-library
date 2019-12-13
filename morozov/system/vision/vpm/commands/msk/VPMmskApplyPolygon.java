// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

import java.awt.image.Raster;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public abstract class VPMmskApplyPolygon extends VPM_FrameCommand {
	//
	protected double[] xPoints;
	protected double[] yPoints;
	protected int length;
	protected boolean useStandardizedCoordinates;
	//
	protected int[] polygonPixels;
	//
	public VPMmskApplyPolygon(double[] aX, double[] aY, boolean standardizedCoordinates) {
		xPoints= aX;
		yPoints= aY;
		length= StrictMath.min(xPoints.length,yPoints.length);
		useStandardizedCoordinates= standardizedCoordinates;
	}
	//
	@Override
	public void execute(VPM vpm) {
		if (polygonPixels==null) {
			int width= vpm.getOperationalImageWidth();
			int height= vpm.getOperationalImageHeight();
			Path2D polygon= new Path2D.Double(Path2D.WIND_EVEN_ODD,length);
			double x0= xPoints[0];
			double y0= yPoints[0];
			if (useStandardizedCoordinates) {
				x0= x0 * width;
				y0= y0 * height;
			};
			polygon.moveTo(x0,y0);
			for (int n= 1; n < length; n++) {
				double xN= xPoints[n];
				double yN= yPoints[n];
				if (useStandardizedCoordinates) {
					xN= xN * width;
					yN= yN * height;
				};
				polygon.lineTo(xN,yN);
			};
			polygon.closePath();
			java.awt.image.BufferedImage temporaryImage= new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g2= temporaryImage.createGraphics();
			try {
				g2.setPaint(Color.WHITE);
				g2.fill(polygon);
			} finally {
				g2.dispose();
			};
			Raster raster= temporaryImage.getData();
			int vectorLength= width * height;
			polygonPixels= new int[vectorLength];
			raster.getSamples(0,0,width,height,0,polygonPixels);
		};
		boolean[] foregroundMask= vpm.getForegroundMask();
		applyPolygon(foregroundMask,polygonPixels);
	}
	//
	abstract protected void applyPolygon(boolean[] foregroundMask, int[] polygonPixels);
}
