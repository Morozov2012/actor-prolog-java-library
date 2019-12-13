// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;

public class Java2DSetMesh extends Java2DAuxiliaryCommand {
	//
	protected double columns;
	protected double rows;
	//
	public Java2DSetMesh(double c, double r) {
		columns= c;
		rows= r;
	}
	//
	@Override
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		drawingMode.setMesh(columns,rows);
	}
}
