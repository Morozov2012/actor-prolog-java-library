// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.interfaces;

import morozov.system.*;
import morozov.system.modes.*;

import java.awt.Color;

public interface IterativeDetailedColorMapInterface {
	//
	public void setUseCustomMap(boolean mode);
	public void setColors(Color[] colors);
	//
	public boolean getUseCustomMap();
	public Color[] getColors();
	//
	public void setName(ColorMapName name);
	public void setSize(ColorMapSize size);
	public void setReverseScale(YesNoDefault mode);
	public void setReverseColors(YesNoDefault mode);
	public void setLowerQuantile(RealAttribute quantile);
	public void setUpperQuantile(RealAttribute quantile);
	public void setLowerBoundIsZero(YesNoDefault mode);
	public void setUpperBoundIsZero(YesNoDefault mode);
	public void setPaletteIterations(IntegerAttribute number);
	public void setReverseMinimalValue(YesNoDefault mode);
	public void setReverseMaximalValue(YesNoDefault mode);
	public void setTincturingCoefficient(TincturingCoefficient value);
	public void setBackgroundColor(ColorAttribute color);
	//
	public ColorMapName getName();
	public ColorMapSize getSize();
	public YesNoDefault getReverseScale();
	public YesNoDefault getReverseColors();
	public RealAttribute getLowerQuantile();
	public RealAttribute getUpperQuantile();
	public YesNoDefault getLowerBoundIsZero();
	public YesNoDefault getUpperBoundIsZero();
	public IntegerAttribute getPaletteIterations();
	public YesNoDefault getReverseMinimalValue();
	public YesNoDefault getReverseMaximalValue();
	public TincturingCoefficient getTincturingCoefficient();
	public ColorAttribute getBackgroundColor();
	//
	public int[][] toColors();
	public long toNumberOfBands();
	public DataColorMap toDataColorMap();
	public int[][] computeColors(int defaultSize);
	public long computeNumberOfBands(IntegerAttribute paletteIterations);
	//
	public void setDefaultName(ColorMapName name);
	public void setDefaultSize(ColorMapSize size);
	public void setDefaultReverseScale(YesNoDefault mode);
	public void setDefaultReverseColors(YesNoDefault mode);
	public void setDefaultLowerQuantile(RealAttribute quantile);
	public void setDefaultUpperQuantile(RealAttribute quantile);
	public void setDefaultLowerBoundIsZero(YesNoDefault mode);
	public void setDefaultUpperBoundIsZero(YesNoDefault mode);
	public void setDefaultPaletteIterations(IntegerAttribute number);
	public void setDefaultReverseMinimalValue(YesNoDefault mode);
	public void setDefaultReverseMaximalValue(YesNoDefault mode);
	public void setDefaultTincturingCoefficient(TincturingCoefficient value);
	public void setDefaultBackgroundColor(ColorAttribute color);
	//
	public ColorMapName getDefaultName();
	public ColorMapSize getDefaultSize();
	public YesNoDefault getDefaultReverseScale();
	public YesNoDefault getDefaultReverseColors();
	public RealAttribute getDefaultLowerQuantile();
	public RealAttribute getDefaultUpperQuantile();
	public YesNoDefault getDefaultLowerBoundIsZero();
	public YesNoDefault getDefaultUpperBoundIsZero();
	public IntegerAttribute getDefaultPaletteIterations();
	public YesNoDefault getDefaultReverseMinimalValue();
	public YesNoDefault getDefaultReverseMaximalValue();
	public TincturingCoefficient getDefaultTincturingCoefficient();
	public ColorAttribute getDefaultBackgroundColor();
	//
	public int[][] nameToArrays(
			ColorMapName mapName,
			int mapLength,
			boolean reverseScale,
			boolean reverseColors,
			IntegerAttribute paletteIterations,
			boolean reverseMinValue,
			boolean reverseMaxValue);
	public int[][] refineColorPalette(
			int[][] array,
			boolean reverseScale,
			boolean reverseColors,
			boolean reverseMinValue,
			boolean reverseMaxValue);
}
