// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.space2d.errors.*;
import morozov.terms.*;

public class Point2DArrays {
	//
	public double[] xPoints;
	public double[] yPoints;
	//
	public Point2DArrays(double[] xList, double[] yList) {
		xPoints= xList;
		yPoints= yList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Point2DArrays termToPoint2DArrays(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		double[] xPoints= new double[termArray.length];
		double[] yPoints= new double[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			try {
				Term[] arguments= termArray[n].isStructure(SymbolCodes.symbolCode_E_p,2,iX);
				xPoints[n]= Converters.argumentToReal(arguments[0],iX);
				yPoints[n]= Converters.argumentToReal(arguments[1],iX);
			} catch (Backtracking b) {
				throw new WrongArgumentIsNotAPoint2D(value);
			}
		};
		return new Point2DArrays(xPoints,yPoints);
	}
}
