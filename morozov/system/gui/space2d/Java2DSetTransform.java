// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Java2DSetTransform extends Java2DAuxiliaryCommand {
	//
	protected AffineTransform transform;
	//
	public Java2DSetTransform(AffineTransform t) {
		transform= t;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		g2.setTransform(transform);
	}
}
