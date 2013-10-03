// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;

public class Java2DSetPaintMode extends Java2DSetModeCommand {
	public Java2DSetPaintMode() {
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		g2.setPaintMode();
	}
	public boolean isSetPaintModeCommand() {
		return true;
	}
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DSetPaintMode) ) {
				return false;
			} else {
				return true;
			}
		}
	}
}
