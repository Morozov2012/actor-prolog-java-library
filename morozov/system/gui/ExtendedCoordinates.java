// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.system.gui.signals.*;

public class ExtendedCoordinates {
	public ExtendedCoordinate x;
	public ExtendedCoordinate y;
	public ExtendedCoordinates(ExtendedCoordinate a, ExtendedCoordinate b) {
		x= a;
		y= b;
	}
	public ExtendedCoordinates(double a, double b) {
		x= new ExtendedCoordinate(a);
		y= new ExtendedCoordinate(b);
	}
	public boolean areCentered() throws UseDefaultLocation {
		if (x.isCentered() || y.isCentered()) {
			return true;
		} else {
			return false;
		}
	}
}
