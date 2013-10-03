// (c) 2008-2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.system.gui.signals.*;

public class ExtendedCoordinate {
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
			throw UseDefaultLocation.instance;
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
			throw UseDefaultLocation.instance;
		} else if (locateFigureInTheCentre) {
			throw CentreFigure.instance;
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
