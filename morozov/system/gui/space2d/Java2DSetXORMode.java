// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.Color;

public class Java2DSetXORMode extends Java2DSetModeCommand {
	//
	protected Color color;
	//
	public Java2DSetXORMode(Color c) {
		color= c;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		g2.setXORMode(color);
	}
	@Override
	public boolean isSet_XOR_ModeCommand() {
		return true;
	}
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof Java2DSetXORMode) ) {
				return false;
			} else {
				Java2DSetXORMode i= (Java2DSetXORMode) o;
				if (i.color==color) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
