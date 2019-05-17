// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.system.gui.signals.*;

public class ExtendedCoordinates {
	//
	public ExtendedCoordinate x;
	public ExtendedCoordinate y;
	public boolean usePixelMeasurements;
	//
	public ExtendedCoordinates(ExtendedCoordinate a, ExtendedCoordinate b, boolean mode) {
		x= a;
		y= b;
		usePixelMeasurements= mode;
	}
	public ExtendedCoordinates(double a, double b, boolean mode) {
		x= new ExtendedCoordinate(a);
		y= new ExtendedCoordinate(b);
		usePixelMeasurements= mode;
	}
	public boolean areCentered() throws UseDefaultLocation {
		if (x.isCentered() || y.isCentered()) {
			return true;
		} else {
			return false;
		}
	}
	public boolean usePixelMeasurements() {
		return usePixelMeasurements;
	}
}
