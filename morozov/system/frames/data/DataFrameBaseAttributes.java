// (c) 2018 Alexei A. Morozov

package morozov.system.frames.data;

import morozov.system.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.modes.*;

import java.math.BigInteger;
import java.io.Serializable;

public class DataFrameBaseAttributes implements DataFrameBaseAttributesInterface, Serializable {
	//
	protected long serialNumber;
	protected boolean isAutorangingMode;
	protected boolean isDoubleColorMapMode;
	protected DataRange selectedDataRange;
	protected double lowerDataBound;
	protected double upperDataBound;
	protected double lowerDataQuantile1;
	protected double upperDataQuantile1;
	protected double lowerDataQuantile2;
	protected double upperDataQuantile2;
	protected DataColorMap mainColorMap;
	protected DataColorMap auxiliaryColorMap;
	protected boolean isAverageMode;
	protected boolean isZoomingMode;
	protected int zoomingCoefficient;
	//
	private static final long serialVersionUID= 0xB55D6293FB160449L; // -5378033992298265527L;
	//
	// static {
	//	System.out.printf("DataFrameBaseAttributes: serialVersionUID: %X\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public DataFrameBaseAttributes(
			long givenSerialNumber,
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
			int givenZoomingCoefficient
			) {
		serialNumber= givenSerialNumber;
		isAutorangingMode= givenIsAutorangingMode;
		isDoubleColorMapMode= givenIsDoubleColorMapMode;
		selectedDataRange= givenSelectedDataRange;
		lowerDataBound= givenLowerDataBound;
		upperDataBound= givenUpperDataBound;
		lowerDataQuantile1= givenLowerDataQuantile1;
		upperDataQuantile1= givenUpperDataQuantile1;
		lowerDataQuantile2= givenLowerDataQuantile2;
		upperDataQuantile2= givenUpperDataQuantile2;
		mainColorMap= givenMainColorMap;
		auxiliaryColorMap= givenAuxiliaryColorMap;
		isAverageMode= givenIsAverageMode;
		isZoomingMode= givenIsZoomingMode;
		zoomingCoefficient= givenZoomingCoefficient;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getSerialNumber() {
		return serialNumber;
	}
	public boolean isAutorangingMode() {
		return isAutorangingMode;
	}
	public boolean isDoubleColorMapMode() {
		return isDoubleColorMapMode;
	}
	public DataRange getSelectedDataRange() {
		return selectedDataRange;
	}
	public double getLowerDataBound() {
		return lowerDataBound;
	}
	public double getUpperDataBound() {
		return upperDataBound;
	}
	public double getLowerDataQuantile1() {
		return lowerDataQuantile1;
	}
	public double getUpperDataQuantile1() {
		return upperDataQuantile1;
	}
	public double getLowerDataQuantile2() {
		return lowerDataQuantile2;
	}
	public double getUpperDataQuantile2() {
		return upperDataQuantile2;
	}
	public DataColorMap getMainColorMap() {
		return mainColorMap;
	}
	public DataColorMap getAuxiliaryColorMap() {
		return auxiliaryColorMap;
	}
	public boolean isAverageMode() {
		return isAverageMode;
	}
	public boolean isZoomingMode() {
		return isZoomingMode;
	}
	public int getZoomingCoefficient() {
		return zoomingCoefficient;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int toInteger(NumericalValue value) {
		if (value.useDoubleValue()) {
			double doubleValue= StrictMath.round(value.getDoubleValue());
			if (doubleValue >= Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			} else if (doubleValue <= Integer.MIN_VALUE) {
				return Integer.MIN_VALUE;
			} else {
				return (int)doubleValue;
			}
		} else {
			BigInteger bigInteger= value.getIntegerValue();
			if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0) {
				return Integer.MAX_VALUE;
			} else if (bigInteger.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) <= 0) {
				return Integer.MIN_VALUE;
			} else {
				return bigInteger.intValue();
			}
		}
	}
}
