// (c) 2018 Alexei A. Morozov

package morozov.system.astrohn.frames;

import morozov.system.astrohn.frames.data.*;
import morozov.system.astrohn.frames.interfaces.*;
import morozov.system.frames.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.modes.*;

public class THzDataFrame extends DataFrame implements THzDataFrameInterface {
	//
	protected TVFilterImageHeader packetBody;
	//
	private static final long serialVersionUID= 0x20180225042759L;
	//
	///////////////////////////////////////////////////////////////
	//
	public THzDataFrame(long n, long t, TVFilterImageHeader d, DataFrameBaseAttributesInterface attributes) {
		super(n,t,attributes);
		packetBody= d;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public DataArrayType getDataArrayType() {
		return DataArrayType.THZ_FRAME;
	}
	//
	@Override
	public TVFilterImageHeader getTHzData() {
		return packetBody;
	}
	//
	@Override
	public int getDataSize() {
		return packetBody.getLength();
	}
}
