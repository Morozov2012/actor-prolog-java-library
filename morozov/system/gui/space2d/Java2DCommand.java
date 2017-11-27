// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;

public abstract class Java2DCommand {
	abstract public void execute(Graphics2D g2, DrawingMode drawingMode);
	public boolean isAuxiliaryCommand() {
		return false;
	}
	public boolean isSetModeCommand() {
		return false;
	}
	public boolean isSet_XOR_ModeCommand() {
		return false;
	}
	public boolean isSetPaintModeCommand() {
		return false;
	}
}
