// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.built_in.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.scalable.*;

import javax.media.j3d.Screen3D;
// import javax.media.j3d.RenderingError;
// import javax.media.j3d.RenderingErrorListener;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.DisplayMode;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.ImageCapabilities;

public class ExtendedSpace3D extends CanvasSpace {
	protected javax.media.j3d.Canvas3D nativeCanvas3D;
	protected OffScreenCanvas3D offScreenCanvas3D;
	protected SimpleUniverse simpleUniverse;
	public ExtendedSpace3D(CustomControlComponent customControl, Canvas3D world, GraphicsConfiguration configuration) {
		super(customControl);
		targetWorld= world;
		nativeCanvas3D= new javax.media.j3d.Canvas3D(configuration);
		control= nativeCanvas3D;
		initializeOffScreenCanvas3D(configuration);
	}
	public void setUniverse(SimpleUniverse universe) {
		simpleUniverse= universe;
		// Attach the offscreen canvas to the view:
		simpleUniverse.getViewer().getView().addCanvas3D(offScreenCanvas3D);
	}
	protected void initializeOffScreenCanvas3D(GraphicsConfiguration configuration) {
		if (nativeCanvas3D != null) {
			offScreenCanvas3D= new OffScreenCanvas3D(configuration);
			// Set the off-screen size:
			Screen3D screen1= nativeCanvas3D.getScreen3D();
			Screen3D screen2= offScreenCanvas3D.getScreen3D();
			Dimension dimension= screen1.getSize();
			screen2.setSize(dimension);
			screen2.setPhysicalScreenWidth(screen1.getPhysicalScreenWidth());
			screen2.setPhysicalScreenHeight(screen1.getPhysicalScreenHeight());
		}
	}
	public OffScreenCanvas3D getOffScreenCanvas3D() {
		return offScreenCanvas3D;
	}
	public java.awt.image.BufferedImage createBufferedImage(int width, int height) {
		if (simpleUniverse != null && nativeCanvas3D != null) {
			repairOffScreenCanvasIfNecessary();
			return offScreenCanvas3D.renderImage(width,height);
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage createBufferedImage() {
		if (simpleUniverse != null && nativeCanvas3D != null) {
			repairOffScreenCanvasIfNecessary();
			Dimension dimension= nativeCanvas3D.getSize();
			return offScreenCanvas3D.renderImage(dimension.width,dimension.height);
		} else {
			return null;
		}
	}
	protected void repairOffScreenCanvasIfNecessary() {
		if (!offScreenCanvas3D.isRendererRunning()) {
			offScreenCanvas3D.setOffScreenBuffer(null);
			simpleUniverse.getViewer().getView().removeCanvas3D(offScreenCanvas3D);
			GraphicsConfiguration configuration= nativeCanvas3D.getGraphicsConfiguration();
			configuration= Canvas3D.refineGraphicsConfiguration(configuration);
			initializeOffScreenCanvas3D(configuration);
			simpleUniverse.getViewer().getView().addCanvas3D(offScreenCanvas3D);
		}
	}
}
