// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.frames.data;

import morozov.system.*;
import morozov.system.i3v1.frames.data.interfaces.*;
import morozov.system.modes.*;

import java.io.Serializable;

public class ThermalDataFrameColorfulAttributes extends ThermalDataFrameBaseAttributes implements ThermalDataFrameColorfulAttributesInterface, Serializable {
	//
	protected DetailedColorMap detailedMainColorMap;
	protected DetailedColorMap detailedAuxiliaryColorMap;
	protected NumericalValue detailedZoomingCoefficient;
	//
	private static final long serialVersionUID= 0xA671867731401ABDL; // -6453228944559695171L;
	//
	// static {
	//	System.out.printf("ThermalDataFrameBaseAttributes: serialVersionUID: %X\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ThermalDataFrameColorfulAttributes(
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
			DetailedColorMap givenMainColorMap,
			DetailedColorMap givenAuxiliaryColorMap,
			boolean givenIsAverageMode,
			boolean givenIsZoomingMode,
			NumericalValue givenZoomingCoefficient,
			int givenReadTimeOut,
			int givenWriteTimeOut
		) {
		super(	givenSerialNumber,
			givenEliminateAnomalousPixels,
			givenVoltageAnomalousPixelDetector,
			givenVoltageAnomalousPixelDetectorThreshold,
			givenTemperatureAnomalousPixelDetector,
			givenTemperatureAnomalousPixelDetectorThreshold,
			givenNumberOfDeadPixels,
			givenNumberOfVoltageAnomalousPixels,
			givenNumberOfTemperatureAnomalousPixels,
			givenIsCelsius,
			givenIsAutorangingMode,
			givenIsDoubleColorMapMode,
			givenSelectedDataRange,
			givenLowerDataBound,
			givenUpperDataBound,
			givenLowerDataQuantile1,
			givenUpperDataQuantile1,
			givenLowerDataQuantile2,
			givenUpperDataQuantile2,
			givenMainColorMap.toDataColorMap(),
			givenAuxiliaryColorMap.toDataColorMap(),
			givenIsAverageMode,
			givenIsZoomingMode,
			givenZoomingCoefficient.toInteger(),
			givenReadTimeOut,
			givenWriteTimeOut);
		detailedMainColorMap= givenMainColorMap;
		detailedAuxiliaryColorMap= givenAuxiliaryColorMap;
		detailedZoomingCoefficient= givenZoomingCoefficient;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DetailedColorMap getDetailedMainColorMap() {
		return detailedMainColorMap;
	}
	public DetailedColorMap getDetailedAuxiliaryColorMap() {
		return detailedAuxiliaryColorMap;
	}
	public NumericalValue getDetailedZoomingCoefficient() {
		return detailedZoomingCoefficient;
	}
}
