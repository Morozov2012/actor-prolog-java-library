// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Paint;

public class BrushAttributes {
	//
	public boolean isSwitch= false;
	public boolean fillFigures= true;
	public Paint paint;
	//
	public BrushAttributes(Paint p) {
		paint= p;
		isSwitch= false;
		fillFigures= true;
	}
	public BrushAttributes(boolean flag) {
		paint= null;
		isSwitch= true;
		fillFigures= flag;
	}
}
