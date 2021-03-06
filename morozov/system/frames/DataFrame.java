// (c) 2018 Alexei A. Morozov

package morozov.system.frames;

import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.interfaces.*;

import java.io.Serializable;

public abstract class DataFrame implements DataFrameInterface, Serializable {
	//
	protected long serialNumber;
	protected long time;
	protected DataFrameBaseAttributesInterface baseAttributes;
	//
	private static final long serialVersionUID= 0xBDD6023F49B31D36L; // -4767620684682420938L;
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system.frames","DescriptionCompoundFrame");
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames","DataFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public DataFrame(long n, long t, DataFrameBaseAttributesInterface givenAttributes) {
		serialNumber= n;
		time= t;
		baseAttributes= givenAttributes;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public long getSerialNumber() {
		return serialNumber;
	}
	@Override
	public long getTime() {
		return time;
	}
	@Override
	public DataFrameBaseAttributesInterface getBaseAttributes() {
		return baseAttributes;
	}
	@Override
	public boolean isLightweightFrame() {
		return false;
	}
}
