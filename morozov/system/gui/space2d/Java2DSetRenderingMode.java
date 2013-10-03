// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Java2DSetRenderingMode extends Java2DAuxiliaryCommand {
	protected RenderingHints attributes;
	public Java2DSetRenderingMode(RenderingHints argument) {
		attributes= argument;
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		g2.setRenderingHints(attributes);
	}
}
