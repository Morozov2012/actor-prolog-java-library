// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.frames;

import morozov.system.frames.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.i3v1.frames.interfaces.*;
import morozov.system.modes.*;

import java.util.Arrays;

public class DoubleDataFrame extends DataFrame implements DoubleDataFrameInterface {
	//
	protected double[] data;
	//
	private static final long serialVersionUID= 0x8066516AF1B2B3A9L; // -9194572069466885207L;
	//
	// static {
	//	System.out.printf("DoubleDataFrame: serialVersionUID: %x\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public DoubleDataFrame(long n, long t, double[] d, DataFrameBaseAttributesInterface givenAttributes) {
		super(n,t,givenAttributes);
		data= Arrays.copyOf(d,d.length);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public DataArrayType getDataArrayType() {
		return DataArrayType.DOUBLE_FRAME;
	}
	@Override
	public double[] getDoubleData() {
		return data;
	}
	@Override
	public int getDataSize() {
		return data.length;
	}
}
