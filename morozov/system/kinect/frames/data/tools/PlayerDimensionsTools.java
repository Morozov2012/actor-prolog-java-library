// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.tools;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.tools.*;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class PlayerDimensionsTools {
	//
	protected static float lineWidth_DepthFrame= 5.0f;
	protected static float lineWidth_ColorFrame= 10.0f;
	protected static BasicStroke solidLineStroke_DepthFrame= new BasicStroke(lineWidth_DepthFrame,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	protected static BasicStroke solidLineStroke_ColorFrame= new BasicStroke(lineWidth_ColorFrame,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	protected static BasicStroke dashLineStroke_DepthFrame= new BasicStroke(lineWidth_DepthFrame,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10.0f,new float[]{10.0f},0.0f);
	protected static BasicStroke dashLineStroke_ColorFrame= new BasicStroke(lineWidth_ColorFrame,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10.0f,new float[]{10.0f},0.0f);
	//
	///////////////////////////////////////////////////////////////
	//
	public static void draw(PlayerDimensionsInterface playerDimensions, Graphics2D g2, KinectCircumscriptionMode[] dimensionsModes, int x0, int y0, int imageWidth, int imageHeight, int scaledImageWidth, int scaledImageHeight) {
		KinectCircumscriptionMode firstItem= KinectCircumscriptionModesTools.getFirstItem(dimensionsModes);
		switch (firstItem) {
		case TOTAL_RECTANGLES:
		case SKELETON_RECTANGLES:
			if (playerDimensions.getIdentifier() >= 0 && playerDimensions.areInitialized_2D_Bounds()) {
				int minimalX_2D= playerDimensions.getMinimalX_2D();
				int maximalX_2D= playerDimensions.getMaximalX_2D();
				int minimalY_2D= playerDimensions.getMinimalY_2D();
				int maximalY_2D= playerDimensions.getMaximalY_2D();
				Color color= KinectColorMapTools.getPersonDefaultColor(playerDimensions.getIdentifier());
				g2.setColor(color);
				if (playerDimensions.isInitialized_ColorParallelepiped()) {
					g2.setStroke(solidLineStroke_ColorFrame);
				} else {
					g2.setStroke(solidLineStroke_DepthFrame);
				};
				float ratioX= (float)scaledImageWidth / imageWidth;
				float ratioY= (float)scaledImageHeight / imageHeight;
				int x1= x0 + (int)(minimalX_2D * ratioX);
				int x2= x0 + (int)(maximalX_2D * ratioX);
				int y1= y0 + (int)(minimalY_2D * ratioX);
				int y2= y0 + (int)(maximalY_2D * ratioY);
				g2.draw(new Line2D.Double(x1,y1,x2,y1));
				g2.draw(new Line2D.Double(x1,y2,x2,y2));
				g2.draw(new Line2D.Double(x1,y1,x1,y2));
				g2.draw(new Line2D.Double(x2,y1,x2,y2));
			};
			break;
		case TOTAL_PARALLELEPIPEDS:
		case SKELETON_PARALLELEPIPEDS:
			if (playerDimensions.getIdentifier() >= 0) {
				XY_Interface xy11= playerDimensions.getXY11();
				XY_Interface xy12= playerDimensions.getXY12();
				XY_Interface xy13= playerDimensions.getXY13();
				XY_Interface xy14= playerDimensions.getXY14();
				XY_Interface xy21= playerDimensions.getXY21();
				XY_Interface xy22= playerDimensions.getXY22();
				XY_Interface xy23= playerDimensions.getXY23();
				XY_Interface xy24= playerDimensions.getXY24();
				Color color= KinectColorMapTools.getPersonDefaultColor(playerDimensions.getIdentifier());
				g2.setColor(color);
				if (playerDimensions.isInitialized_ColorParallelepiped()) {
					g2.setStroke(solidLineStroke_ColorFrame);
				} else {
					g2.setStroke(solidLineStroke_DepthFrame);
				};
				float ratioX= (float)scaledImageWidth / imageWidth;
				float ratioY= (float)scaledImageHeight / imageHeight;
				if (xy11 != null && xy12 != null) {
					int x11= xy11.getX(x0,ratioX);
					int y11= xy11.getY(y0,ratioY);
					int x12= xy12.getX(x0,ratioX);
					int y12= xy12.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x11,y11,x12,y12));
				};
				if (xy12 != null && xy13 != null) {
					int x12= xy12.getX(x0,ratioX);
					int y12= xy12.getY(y0,ratioY);
					int x13= xy13.getX(x0,ratioX);
					int y13= xy13.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x12,y12,x13,y13));
				};
				if (xy13 != null && xy14 != null) {
					int x13= xy13.getX(x0,ratioX);
					int y13= xy13.getY(y0,ratioY);
					int x14= xy14.getX(x0,ratioX);
					int y14= xy14.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x13,y13,x14,y14));
				};
				if (xy14 != null && xy11 != null) {
					int x14= xy14.getX(x0,ratioX);
					int y14= xy14.getY(y0,ratioY);
					int x11= xy11.getX(x0,ratioX);
					int y11= xy11.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x14,y14,x11,y11));
				};
				if (playerDimensions.isInitialized_ColorParallelepiped()) {
					g2.setStroke(dashLineStroke_ColorFrame);
				} else {
					g2.setStroke(dashLineStroke_DepthFrame);
				};
				if (xy21 != null && xy22 != null) {
					int x21= xy21.getX(x0,ratioX);
					int y21= xy21.getY(y0,ratioY);
					int x22= xy22.getX(x0,ratioX);
					int y22= xy22.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x21,y21,x22,y22));
				};
				if (xy22 != null && xy23 != null) {
					int x22= xy22.getX(x0,ratioX);
					int y22= xy22.getY(y0,ratioY);
					int x23= xy23.getX(x0,ratioX);
					int y23= xy23.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x22,y22,x23,y23));
				};
				if (xy23 != null && xy24 != null) {
					int x23= xy23.getX(x0,ratioX);
					int y23= xy23.getY(y0,ratioY);
					int x24= xy24.getX(x0,ratioX);
					int y24= xy24.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x23,y23,x24,y24));
				};
				if (xy24 != null && xy21 != null) {
					int x24= xy24.getX(x0,ratioX);
					int y24= xy24.getY(y0,ratioY);
					int x21= xy21.getX(x0,ratioX);
					int y21= xy21.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x24,y24,x21,y21));
				};
				if (xy11 != null && xy21 != null) {
					int x11= xy11.getX(x0,ratioX);
					int y11= xy11.getY(y0,ratioY);
					int x21= xy21.getX(x0,ratioX);
					int y21= xy21.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x11,y11,x21,y21));
				};
				if (xy12 != null && xy22 != null) {
					int x12= xy12.getX(x0,ratioX);
					int y12= xy12.getY(y0,ratioY);
					int x22= xy22.getX(x0,ratioX);
					int y22= xy22.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x12,y12,x22,y22));
				};
				if (xy13 != null && xy23 != null) {
					int x13= xy13.getX(x0,ratioX);
					int y13= xy13.getY(y0,ratioY);
					int x23= xy23.getX(x0,ratioX);
					int y23= xy23.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x13,y13,x23,y23));
				};
				if (xy14 != null && xy24 != null) {
					int x14= xy14.getX(x0,ratioX);
					int y14= xy14.getY(y0,ratioY);
					int x24= xy24.getX(x0,ratioX);
					int y24= xy24.getY(y0,ratioY);
					g2.draw(new Line2D.Double(x14,y14,x24,y24));
				}
			};
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(PlayerDimensionsInterface playerDimensions, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; PLAYER DIMENSIONS:\n");
		writer.write(String.format(locale,"; identifier: %s\n",playerDimensions.getIdentifier()));
		writer.write(String.format(locale,"; minimalX_2D= %s\n",playerDimensions.getMinimalX_2D()));
		writer.write(String.format(locale,"; maximalX_2D= %s\n",playerDimensions.getMaximalX_2D()));
		writer.write(String.format(locale,"; minimalY_2D= %s\n",playerDimensions.getMinimalY_2D()));
		writer.write(String.format(locale,"; maximalY_2D= %s\n",playerDimensions.getMaximalY_2D()));
		writer.write(String.format(locale,"; areInitialized_2D_Bounds= %s\n",playerDimensions.areInitialized_2D_Bounds()));
		writer.write(String.format(locale,"; minimalX_3D= %s\n",playerDimensions.getMinimalX_3D()));
		writer.write(String.format(locale,"; maximalX_3D= %s\n",playerDimensions.getMaximalX_3D()));
		writer.write(String.format(locale,"; minimalY_3D= %s\n",playerDimensions.getMinimalY_3D()));
		writer.write(String.format(locale,"; maximalY_3D= %s\n",playerDimensions.getMaximalY_3D()));
		writer.write(String.format(locale,"; minimalZ_3D= %s\n",playerDimensions.getMinimalZ_3D()));
		writer.write(String.format(locale,"; maximalZ_3D= %s\n",playerDimensions.getMaximalZ_3D()));
		writer.write(String.format(locale,"; areInitialized_Depth3D_Bounds= %s\n",playerDimensions.areInitialized_Depth3D_Bounds()));
		//
		XY_Interface xy11= playerDimensions.getXY11();
		XY_Interface xy12= playerDimensions.getXY12();
		XY_Interface xy13= playerDimensions.getXY13();
		XY_Interface xy14= playerDimensions.getXY14();
		XY_Interface xy21= playerDimensions.getXY21();
		XY_Interface xy22= playerDimensions.getXY22();
		XY_Interface xy23= playerDimensions.getXY23();
		XY_Interface xy24= playerDimensions.getXY24();
		//
		writer.write("; xy11: ");
		if (xy11 != null) {
			XY_Tools.writeText(xy11,writer,exportMode,locale);
		} else {
			writer.write("none\n");
		};
		writer.write("; xy12: ");
		if (xy12 != null) {
			XY_Tools.writeText(xy12,writer,exportMode,locale);
		} else {
			writer.write("none\n");
		};
		writer.write("; xy13: ");
		if (xy13 != null) {
			XY_Tools.writeText(xy13,writer,exportMode,locale);
		} else {
			writer.write("none\n");
		};
		writer.write("; xy14: ");
		if (xy14 != null) {
			XY_Tools.writeText(xy14,writer,exportMode,locale);
		} else {
			writer.write("none\n");
		};
		//
		writer.write("; xy21: ");
		if (xy21 != null) {
			XY_Tools.writeText(xy21,writer,exportMode,locale);
		} else {
			writer.write("none\n");
		};
		writer.write("; xy22: ");
		if (xy22 != null) {
			XY_Tools.writeText(xy22,writer,exportMode,locale);
		} else {
			writer.write("none\n");
		};
		writer.write("; xy23: ");
		if (xy23 != null) {
			XY_Tools.writeText(xy23,writer,exportMode,locale);
		} else {
			writer.write("none\n");
		};
		writer.write("; xy24: ");
		if (xy24 != null) {
			XY_Tools.writeText(xy24,writer,exportMode,locale);
		} else {
			writer.write("none\n");
		};
		//
		writer.write(String.format(locale,"; isInitialized_DepthParallelepiped= %s\n",playerDimensions.isInitialized_DepthParallelepiped()));
		writer.write(String.format(locale,"; isInitialized_ColorParallelepiped= %s\n",playerDimensions.isInitialized_ColorParallelepiped()));
		writer.write("; End of player dimensions.\n");
	}
}
