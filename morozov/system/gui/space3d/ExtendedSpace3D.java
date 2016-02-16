// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.built_in.*;
import morozov.system.gui.*;

import java.awt.GraphicsConfiguration;

public class ExtendedSpace3D extends CanvasSpace {
	public ExtendedSpace3D(Canvas3D world, GraphicsConfiguration graphicsConfiguration) {
		// super(graphicsConfiguration);
		targetWorld= world;
		control= new javax.media.j3d.Canvas3D(graphicsConfiguration);
	}
}
