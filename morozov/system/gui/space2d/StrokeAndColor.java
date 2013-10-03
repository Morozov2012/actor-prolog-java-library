// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Color;
import java.awt.BasicStroke;

public class StrokeAndColor extends BasicStroke {
	public Color color= null;
	public StrokeAndColor(Color c, float width, int cap, int join, float miterlimit) {
		super(width,cap,join,miterlimit);
		color= c;
	}
	public StrokeAndColor(Color c, float width, int cap, int join, float miterlimit, float[] dash, float dash_phase) {
		super(width,cap,join,miterlimit,dash,dash_phase);
		color= c;
	}
}
