// (c) 2018 Alexei A. Morozov

package morozov.system.frames.interfaces;

import morozov.system.frames.data.interfaces.*;
import morozov.system.modes.*;

public interface DataFrameInterface {
	//
	public DataArrayType getDataArrayType();
	public long getSerialNumber();
	public long getTime();
	public int getDataSize();
	public DataFrameBaseAttributesInterface getBaseAttributes();
	public boolean isLightweightFrame();
}
