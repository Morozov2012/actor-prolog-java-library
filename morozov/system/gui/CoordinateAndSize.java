// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.system.gui.signals.*;

public class CoordinateAndSize {
	//
	public int coordinate= 0;
	public int size= 0;
	public boolean coordinateIsToBeCalculatedAutomatically= false;
	//
	public static CoordinateAndSize calculate(ExtendedCoordinate lZ, ExtendedSize lDZ, int spaceLimit, double gridZ) {
		CoordinateAndSize result= new CoordinateAndSize();
		try {
			double logicalCoordinate= lZ.getValue();
			result.coordinate= (int)StrictMath.round( logicalCoordinate * spaceLimit / gridZ );
			try {
				double logicalSize= lDZ.getValue();
				if (logicalSize < 0) {
					result.size= (int)StrictMath.round( (1 - (result.coordinate - logicalSize) / gridZ) * spaceLimit );
				} else {
					result.size= (int)StrictMath.round( logicalSize * spaceLimit / gridZ );
				}
			} catch (UseDefaultSize e2) {
				result.size= (int)StrictMath.round( (1 - logicalCoordinate / gridZ) * spaceLimit );
			}
		} catch (CentreFigure e1) {
			try {
				double logicalSize= lDZ.getValue();
				if (logicalSize < 0) {
					result.size= (int)StrictMath.round( (1 + logicalSize / gridZ) * spaceLimit );
				} else {
					result.size= (int)StrictMath.round( logicalSize * spaceLimit / gridZ );
				};
				result.coordinate= (spaceLimit - result.size) / 2;
			} catch (UseDefaultSize e2) {
				result.coordinate= 0;
				result.size= spaceLimit;
			}
		} catch (UseDefaultLocation e1) {
			result.coordinateIsToBeCalculatedAutomatically= true;
			try {
				double logicalSize= lDZ.getValue();
				if (logicalSize < 0) {
					result.size= (int)StrictMath.round( (1 + logicalSize / gridZ) * spaceLimit );
				} else {
					result.size= (int)StrictMath.round( logicalSize * spaceLimit / gridZ );
				}
			} catch (UseDefaultSize e2) {
				result.size= spaceLimit;
			}
		};
		return result;
	}
}
