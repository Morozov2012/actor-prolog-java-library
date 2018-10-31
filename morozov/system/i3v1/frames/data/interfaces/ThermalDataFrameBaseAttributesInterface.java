// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.frames.data.interfaces;

import morozov.system.frames.data.interfaces.*;

public interface ThermalDataFrameBaseAttributesInterface extends DataFrameBaseAttributesInterface {
	public boolean getEliminateAnomalousPixels();
	public boolean getVoltageAnomalousPixelDetector();
	public double getVoltageAnomalousPixelDetectorThreshold();
	public boolean getTemperatureAnomalousPixelDetector();
	public double getTemperatureAnomalousPixelDetectorThreshold();
	public int getNumberOfDeadPixels();
	public int getNumberOfVoltageAnomalousPixels();
	public int getNumberOfTemperatureAnomalousPixels();
	public boolean isCelsius();
	public int getReadTimeOut();
	public int getWriteTimeOut();
}
