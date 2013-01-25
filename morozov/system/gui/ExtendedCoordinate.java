// (c) 2008-2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.terms.*;

public class ExtendedCoordinate {
	//
	public class CentreFigure extends LightweightException {}
	public class UseDefaultLocation extends LightweightException {}
	//
	private boolean locateFigureInDefaultPosition= true;
	private boolean locateFigureInTheCentre= false;
	private double value= 0;
	//
	public ExtendedCoordinate() {
	}
	public ExtendedCoordinate(double v) {
		locateFigureInDefaultPosition= false;
		locateFigureInTheCentre= false;
		value= v;
	}
	//
	public void useDefaultLocation() {
		locateFigureInDefaultPosition= true;
	}
	public boolean isDefault() {
		return locateFigureInDefaultPosition;
	}
	public void centreFigure() {
		locateFigureInTheCentre= true;
		locateFigureInDefaultPosition= false;
	}
	public boolean isCentered() throws UseDefaultLocation {
		if (locateFigureInDefaultPosition) {
			throw new UseDefaultLocation();
		} else {
			return locateFigureInTheCentre;
		}
	}
	public void setValue(double v) {
		value= v;
		locateFigureInDefaultPosition= false;
		locateFigureInTheCentre= false;
	}
	public double getValue() throws UseDefaultLocation, CentreFigure {
		if (locateFigureInDefaultPosition) {
			throw new UseDefaultLocation();
		} else if (locateFigureInTheCentre) {
			throw new CentreFigure();
		} else {
			return value;
		}
	}
	public String toString() {
		return "(" +
			String.format("%B,",locateFigureInDefaultPosition) +
			String.format("%B,",locateFigureInTheCentre) +
			String.format("%f",value) + ")";
	}
}
