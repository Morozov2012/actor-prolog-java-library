// (c) 2018 Alexei A. Morozov

package morozov.system.frames.data.interfaces;

import morozov.system.modes.*;

public interface DataFrameBaseAttributesInterface {
	public long getSerialNumber();
	public boolean isAutorangingMode();
	public boolean isDoubleColorMapMode();
	public DataRange getSelectedDataRange();
	public double getLowerDataBound();
	public double getUpperDataBound();
	public double getLowerDataQuantile1();
	public double getUpperDataQuantile1();
	public double getLowerDataQuantile2();
	public double getUpperDataQuantile2();
	public DataColorMap getMainColorMap();
	public DataColorMap getAuxiliaryColorMap();
	public boolean isAverageMode();
	public boolean isZoomingMode();
	public int getZoomingCoefficient();
}
