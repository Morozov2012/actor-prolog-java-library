// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.Font;

public class Java2DSetFont extends Java2DAuxiliaryCommand {
	//
	protected Font font;
	//
	public Java2DSetFont(Font f) {
		font= f;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		g2.setFont(font);
	}
}
