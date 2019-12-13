// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Color;

public class Java2DSetBrushAttributes extends Java2DAuxiliaryCommand {
	//
	protected BrushAttributes attributes;
	//
	public Java2DSetBrushAttributes(BrushAttributes argument) {
		attributes= argument;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		Paint paint= attributes.paint;
		if (paint != null) {
			drawingMode.paint= paint;
			drawingMode.fillFigures= true;
			g2.setPaint(paint);
		} else {
			if (attributes.isSwitch) {
				if (attributes.fillFigures) {
					drawingMode.fillFigures= true;
					paint= drawingMode.paint;
					if (paint != null) {
						g2.setPaint(paint);
					}
				} else {
					drawingMode.fillFigures= false;
					Color penColor= drawingMode.penColor;
					if (penColor != null) {
						g2.setColor(penColor);
					}
				}
			}
		}
	}
}
