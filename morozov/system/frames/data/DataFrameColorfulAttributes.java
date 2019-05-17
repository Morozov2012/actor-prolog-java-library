// (c) 2018 Alexei A. Morozov

package morozov.system.frames.data;

import morozov.system.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.modes.*;

public class DataFrameColorfulAttributes extends DataFrameBaseAttributes implements DataFrameColorfulAttributesInterface {
	//
	protected DetailedColorMap detailedMainColorMap;
	protected DetailedColorMap detailedAuxiliaryColorMap;
	protected NumericalValue detailedZoomingCoefficient;
	//
	private static final long serialVersionUID= 0xC7D97E2D16B290ADL;
	//
	// static {
	//	System.out.printf("DataFrameBaseAttributes: serialVersionUID: %x\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public DataFrameColorfulAttributes(
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
			DetailedColorMap givenMainColorMap,
			DetailedColorMap givenAuxiliaryColorMap,
			boolean givenIsAverageMode,
			boolean givenIsZoomingMode,
			NumericalValue givenZoomingCoefficient
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
			givenMainColorMap.toDataColorMap(),
			givenAuxiliaryColorMap.toDataColorMap(),
			givenIsAverageMode,
			givenIsZoomingMode,
			toInteger(givenZoomingCoefficient));
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
