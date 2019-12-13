// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import javax.media.j3d.RenderingErrorListener;
import javax.media.j3d.RenderingError;

public class OffScreenCanvas3DRenderingErrorListener implements RenderingErrorListener {
	@Override
	public void errorOccurred(RenderingError error) {
		error.printVerbose();
	}
}
