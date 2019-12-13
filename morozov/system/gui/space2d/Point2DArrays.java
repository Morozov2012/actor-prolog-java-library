// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.space2d.errors.*;
import morozov.terms.*;

public class Point2DArrays {
	//
	protected double[] xPoints;
	protected double[] yPoints;
	//
	public Point2DArrays(double[] xList, double[] yList) {
		xPoints= xList;
		yPoints= yList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[] getXPoints() {
		return xPoints;
	}
	public double[] getYPoints() {
		return yPoints;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Point2DArrays argumentToPoint2DArrays(Term value, ChoisePoint iX) {
		Term[] termArray= GeneralConverters.listToArray(value,iX);
		double[] xPoints= new double[termArray.length];
		double[] yPoints= new double[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			try {
				Term[] arguments= termArray[n].isStructure(SymbolCodes.symbolCode_E_p,2,iX);
				xPoints[n]= GeneralConverters.argumentToReal(arguments[0],iX);
				yPoints[n]= GeneralConverters.argumentToReal(arguments[1],iX);
			} catch (Backtracking b) {
				throw new WrongArgumentIsNotAPoint2D(value);
			}
		};
		return new Point2DArrays(xPoints,yPoints);
	}
}
