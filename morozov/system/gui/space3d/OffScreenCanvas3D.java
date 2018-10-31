// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.ImageComponent;

import java.awt.image.BufferedImage;
import java.awt.GraphicsConfiguration;

public class OffScreenCanvas3D extends Canvas3D {
	public OffScreenCanvas3D(GraphicsConfiguration configuration) {
		super(configuration,true);
	}
	public BufferedImage renderImage(int width, int height) {
		if (width <= 0 || height <= 0) {
			return null;
		};
		BufferedImage image= new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		// ImageComponent2D:
		// This class defines a 2D image component. This is
		// used for texture images, background images and
		// raster components of Shape3D nodes. 
		ImageComponent2D buffer= new ImageComponent2D(ImageComponent.FORMAT_RGBA,image);
		// setOffScreenBuffer:
		// Sets the off-screen buffer for this Canvas3D. The
		// specified image is written into by the Java 3D
		// renderer. The size of the specified ImageComponent
		// determines the size, in pixels, of this Canvas3D -
		// the size inherited from Component is ignored.
		// NOTE: the size, physical width, and physical height
		// of the associated Screen3D must be set explicitly
		// prior to rendering. Failure to do so will result in
		// an exception.
		// Parameters: buffer - the image component that will
		// be rendered into by subsequent calls to
		// renderOffScreenBuffer. The image component must not
		// be part of a live scene graph, nor may it
		// subsequently be made part of a live scene graph
		// while being used as an off-screen buffer; an
		// IllegalSharingException is thrown in such cases.
		// The buffer may be null, indicating that the
		// previous off-screen buffer is released without a
		// new buffer being set.
		setOffScreenBuffer(null);
		setOffScreenBuffer(buffer);
		// renderOffScreenBuffer:
		// Schedules the rendering of a frame into this
		// Canvas3D's off-screen buffer. The rendering is done
		// from the point of view of the View object to which
		// this Canvas3D has been added. No rendering is
		// performed if this Canvas3D object has not been added
		// to an active View. This method does not wait for the
		// rendering to actually happen. An application that
		// wishes to know when the rendering is complete must
		// either subclass Canvas3D and override the postSwap
		// method, or call waitForOffScreenRendering.
		renderOffScreenBuffer();
		// waitForOffScreenRendering:
		// Waits for this Canvas3D's off-screen rendering to be
		// done. This method will wait until the postSwap method
		// of this off-screen Canvas3D has completed. If this
		// Canvas3D has not been added to an active view or if
		// the renderer is stopped for this Canvas3D, then this
		// method will return immediately. This method must not
		// be called from a render callback method of an
		// off-screen Canvas3D.
		waitForOffScreenRendering();
		// getOffScreenBuffer:
		// Retrieves the off-screen buffer for this Canvas3D.
		image= getOffScreenBuffer().getImage();
		return image;
	}
	public void postSwap() {
	}
}
