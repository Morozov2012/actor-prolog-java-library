// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;

public abstract class Java2DSetModeCommand extends Java2DAuxiliaryCommand {
	public abstract void execute(Graphics2D g2, DrawingMode drawingMode);
	public boolean isSetModeCommand() {
		return true;
	}
}
