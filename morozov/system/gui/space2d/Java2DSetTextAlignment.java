// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;

public class Java2DSetTextAlignment extends Java2DAuxiliaryCommand {
	//
	protected Canvas2DHorizontalAlignment horizontalAlignment;
	protected Canvas2DVerticalAlignment verticalAlignment;
	//
	public Java2DSetTextAlignment(Canvas2DHorizontalAlignment hA, Canvas2DVerticalAlignment vA) {
		horizontalAlignment= hA;
		verticalAlignment= vA;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		drawingMode.setTextAlignment(horizontalAlignment,verticalAlignment);
	}
}
