// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system;

import java.io.Serializable;
import java.math.BigInteger;

public class NumericalValue implements Serializable {
	//
	protected boolean useDoubleValue= false;
	protected BigInteger integerValue;
	protected double doubleValue;
	//
	private static final long serialVersionUID= 0x34C0417223A79850L; // 3801110043980961872L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system","NumericalValue");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public NumericalValue(BigInteger v) {
		useDoubleValue= false;
		integerValue= v;
	}
	public NumericalValue(double v) {
		useDoubleValue= true;
		doubleValue= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean useDoubleValue() {
		return useDoubleValue;
	}
	public BigInteger getIntegerValue() {
		return integerValue;
	}
	public double getDoubleValue() {
		return doubleValue;
	}
}
