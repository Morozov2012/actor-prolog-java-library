// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.interfaces;

import morozov.system.*;
import morozov.system.kinect.modes.*;

import java.awt.Color;

public interface ColorMapInterface {
	//
	public boolean getUseCustomMap();
	public Color[] getColors();
	public ColorMapName getColorMapName();
	public ColorMapSizeInterface getSize();
	public YesNoDefault getReverseScale();
	public YesNoDefault getReverseMinimalValue();
	public YesNoDefault getReverseMaximalValue();
	public TincturingCoefficientInterface getTincturingCoefficient();
	public double getColorMapTincturingCoefficient();
	public double getPeopleColorsTincturingCoefficient();
	//
	public int[][] toColors();
	public int[][] toRepeatedColors(int requiredSize);
	public int[][] computePeopleColors(int requiredSize);
	public int[][] computeColors(int defaultSize);
}
