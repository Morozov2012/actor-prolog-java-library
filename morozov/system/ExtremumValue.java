// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system;

import java.math.BigInteger;

import java.io.Serializable;

public class ExtremumValue implements Serializable {
	//
	protected boolean isInitiated;
	protected BigInteger integerValue;
	protected double realValue;
	protected boolean hasRealValue;
	//
	private static final long serialVersionUID= 0x9137E7A506693BC4L; // -7982657118527734844
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","ExtremumValue");
	//	SerialVersionChecker.report("morozov.system","ExtremumValue");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public void setIsInitiated(boolean mode) {
		isInitiated= mode;
	}
	public boolean isInitiated() {
		return isInitiated;
	}
	//
	public void setIntegerValue(BigInteger value) {
		integerValue= value;
	}
	public BigInteger getIntegerValue() {
		return integerValue;
	}
	//
	public void setRealValue(double value) {
		realValue= value;
	}
	public double getRealValue() {
		return realValue;
	}
	//
	public void setHasRealValue(boolean mode) {
		hasRealValue= mode;
	}
	public boolean hasRealValue() {
		return hasRealValue;
	}
	//
	public void refineIntegerMinimum(BigInteger value) {
		integerValue= integerValue.min(value);
	}
	public void refineRealMinimum(double value) {
		realValue= StrictMath.min(realValue,value);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void refineIntegerMaximum(BigInteger value) {
		integerValue= integerValue.max(value);
	}
	public void refineRealMaximum(double value) {
		realValue= StrictMath.max(realValue,value);
	}
}
