// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.Color;

public class Java2DSetPen extends Java2DAuxiliaryCommand {
	protected StrokeAndColor attributes;
	public Java2DSetPen(StrokeAndColor argument) {
		attributes= argument;
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		Color color= attributes.color;
		if (color != null) {
			drawingMode.penColor= color;
			g2.setColor(color);
		};
		g2.setStroke(attributes);
	}
}
