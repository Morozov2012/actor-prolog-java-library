// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.frames.data;

import morozov.system.frames.data.*;
import morozov.system.i3v1.frames.data.interfaces.*;
import morozov.system.modes.*;

import java.io.Serializable;

public class ThermalDataFrameBaseAttributes extends DataFrameBaseAttributes implements ThermalDataFrameBaseAttributesInterface, Serializable {
	//
	protected boolean eliminateAnomalousPixels;
	protected boolean voltageAnomalousPixelDetector;
	protected double voltageAnomalousPixelDetectorThreshold;
	protected boolean temperatureAnomalousPixelDetector;
	protected double temperatureAnomalousPixelDetectorThreshold;
	protected int numberOfDeadPixels;
	protected int numberOfVoltageAnomalousPixels;
	protected int numberOfTemperatureAnomalousPixels;
	protected boolean isCelsius;
	protected int readTimeOut;
	protected int writeTimeOut;
	//
	private static final long serialVersionUID= 0x9036E5C3A551015EL; // -8054998255050620578L;
	//
	// static {
	//	System.out.printf("ThermalDataFrameBaseAttributes: serialVersionUID: %x\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ThermalDataFrameBaseAttributes(
			long givenSerialNumber,
			boolean givenEliminateAnomalousPixels,
			boolean givenVoltageAnomalousPixelDetector,
			double givenVoltageAnomalousPixelDetectorThreshold,
			boolean givenTemperatureAnomalousPixelDetector,
			double givenTemperatureAnomalousPixelDetectorThreshold,
			int givenNumberOfDeadPixels,
			int givenNumberOfVoltageAnomalousPixels,
			int givenNumberOfTemperatureAnomalousPixels,
			boolean givenIsCelsius,
			boolean givenIsAutorangingMode,
			boolean givenIsDoubleColorMapMode,
			DataRange givenSelectedDataRange,
			double givenLowerDataBound,
			double givenUpperDataBound,
			double givenLowerDataQuantile1,
			double givenUpperDataQuantile1,
			double givenLowerDataQuantile2,
			double givenUpperDataQuantile2,
			DataColorMap givenMainColorMap,
			DataColorMap givenAuxiliaryColorMap,
			boolean givenIsAverageMode,
			boolean givenIsZoomingMode,
			int givenZoomingCoefficient,
			int givenReadTimeOut,
			int givenWriteTimeOut
		) {
		super(	givenSerialNumber,
			givenIsAutorangingMode,
			givenIsDoubleColorMapMode,
			givenSelectedDataRange,
			givenLowerDataBound,
			givenUpperDataBound,
			givenLowerDataQuantile1,
			givenUpperDataQuantile1,
			givenLowerDataQuantile2,
			givenUpperDataQuantile2,
			givenMainColorMap,
			givenAuxiliaryColorMap,
			givenIsAverageMode,
			givenIsZoomingMode,
			givenZoomingCoefficient);
		eliminateAnomalousPixels= givenEliminateAnomalousPixels;
		voltageAnomalousPixelDetector= givenVoltageAnomalousPixelDetector;
		voltageAnomalousPixelDetectorThreshold= givenVoltageAnomalousPixelDetectorThreshold;
		temperatureAnomalousPixelDetector= givenTemperatureAnomalousPixelDetector;
		temperatureAnomalousPixelDetectorThreshold= givenTemperatureAnomalousPixelDetectorThreshold;
		numberOfDeadPixels= givenNumberOfDeadPixels;
		numberOfVoltageAnomalousPixels= givenNumberOfVoltageAnomalousPixels;
		numberOfTemperatureAnomalousPixels= givenNumberOfTemperatureAnomalousPixels;
		isCelsius= givenIsCelsius;
		readTimeOut= givenReadTimeOut;
		writeTimeOut= givenWriteTimeOut;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getEliminateAnomalousPixels() {
		return eliminateAnomalousPixels;
	}
	public boolean getVoltageAnomalousPixelDetector() {
		return voltageAnomalousPixelDetector;
	}
	public double getVoltageAnomalousPixelDetectorThreshold() {
		return voltageAnomalousPixelDetectorThreshold;
	}
	public boolean getTemperatureAnomalousPixelDetector() {
		return temperatureAnomalousPixelDetector;
	}
	public double getTemperatureAnomalousPixelDetectorThreshold() {
		return temperatureAnomalousPixelDetectorThreshold;
	}
	public int getNumberOfDeadPixels() {
		return numberOfDeadPixels;
	}
	public int getNumberOfVoltageAnomalousPixels() {
		return numberOfVoltageAnomalousPixels;
	}
	public int getNumberOfTemperatureAnomalousPixels() {
		return numberOfTemperatureAnomalousPixels;
	}
	public boolean isCelsius() {
		return isCelsius;
	}
	public int getReadTimeOut() {
		return readTimeOut;
	}
	public int getWriteTimeOut() {
		return writeTimeOut;
	}
}
