// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.system.gui.signals.*;
import morozov.terms.*;

public class CoordinateAndSize {
	//
	public int coordinate= 0;
	public int size= 0;
	public boolean coordinateIsToBeCalculatedAutomatically= false;
	//
	public static CoordinateAndSize calculate(ExtendedCoordinate lZ, ExtendedSize lDZ, int spaceLimit, double gridZ) {
		CoordinateAndSize result= new CoordinateAndSize();
		try {
			double logicalCoordinate= lZ.getDoubleValue();
			result.coordinate= PrologInteger.toInteger( logicalCoordinate * spaceLimit / gridZ );
			try {
				double logicalSize= lDZ.getDoubleValue();
				if (logicalSize < 0) {
					result.size= PrologInteger.toInteger( (1 - (logicalCoordinate - logicalSize) / gridZ) * spaceLimit );
				} else {
					result.size= PrologInteger.toInteger( logicalSize * spaceLimit / gridZ );
				}
			} catch (UseDefaultSize e2) {
				result.size= PrologInteger.toInteger( (1 - logicalCoordinate / gridZ) * spaceLimit );
			}
		} catch (CentreFigure e1) {
			try {
				double logicalSize= lDZ.getDoubleValue();
				if (logicalSize < 0) {
					result.size= PrologInteger.toInteger( (1 + logicalSize / gridZ) * spaceLimit );
				} else {
					result.size= PrologInteger.toInteger( logicalSize * spaceLimit / gridZ );
				};
				result.coordinate= (spaceLimit - result.size) / 2;
			} catch (UseDefaultSize e2) {
				result.coordinate= 0;
				result.size= spaceLimit;
			}
		} catch (UseDefaultLocation e1) {
			result.coordinateIsToBeCalculatedAutomatically= true;
			try {
				double logicalSize= lDZ.getDoubleValue();
				if (logicalSize < 0) {
					result.size= PrologInteger.toInteger( (1 + logicalSize / gridZ) * spaceLimit );
				} else {
					result.size= PrologInteger.toInteger( logicalSize * spaceLimit / gridZ );
				}
			} catch (UseDefaultSize e2) {
				result.size= spaceLimit;
			}
		};
		return result;
	}
	public static double reconstruct(double actualSize, double desktopSize, double gridSize) {
		return actualSize * gridSize / desktopSize;
	}
}
