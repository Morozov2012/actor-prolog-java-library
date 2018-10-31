// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

import java.awt.image.Raster;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

abstract public class VPMmskApplyPolygon extends VPM_FrameCommand {
	//
	protected double[] xPoints;
	protected double[] yPoints;
	protected int length;
	//
	protected int[] polygonPixels;
	//
	public VPMmskApplyPolygon(double[] aX, double[] aY) {
		xPoints= aX;
		yPoints= aY;
		length= StrictMath.min(xPoints.length,yPoints.length);
	}
	//
	public void execute(VPM vpm) {
		if (polygonPixels==null) {
			int width= vpm.getOperationalImageWidth();
			int height= vpm.getOperationalImageHeight();
			Path2D polygon= new Path2D.Double(Path2D.WIND_EVEN_ODD,length);
			polygon.moveTo(xPoints[0],yPoints[0]);
			for (int n= 1; n < length; n++) {
				polygon.lineTo(xPoints[n],yPoints[n]);
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
	protected abstract void applyPolygon(boolean[] foregroundMask, int[] polygonPixels);
}
