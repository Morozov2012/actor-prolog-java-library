/*
 * @(#)FontSystemSimulationMode.java 1.0 2013/12/21
 *
 * Copyright 2013 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui;

import morozov.system.*;

public enum FontSystemSimulationMode {
	JAVA {
	},
	WINDOWS {
		@Override
		public int simulate(int size1) {
			int size2= Arithmetic.toInteger((size1 - regressionCoefficient0) / regressionCoefficient1);
			return size2;
		}
		@Override
		public int reconstruct(int size1) {
			int size2= Arithmetic.toInteger(size1 * regressionCoefficient1 + regressionCoefficient0);
			return size2;
		}
	},
	NONE {
	};
	protected double regressionCoefficient0	= 0.231857886891225940;
	protected double regressionCoefficient1	= 0.748865860647563890;
	public int simulate(int fontSize) {
		return fontSize;
	}
	public int reconstruct(int fontSize) {
		return fontSize;
	}
}
