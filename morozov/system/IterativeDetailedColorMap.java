// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.interfaces.*;
import morozov.system.modes.*;

import java.awt.Color;

public class IterativeDetailedColorMap
		extends DetailedColorMap
		implements IterativeDetailedColorMapInterface {
	//
	protected YesNoDefault reverseColors= YesNoDefault.DEFAULT;
	protected RealAttribute lowerQuantile= RealAttribute.instanceDefault;
	protected RealAttribute upperQuantile= RealAttribute.instanceDefault;
	protected YesNoDefault lowerBoundIsZero= YesNoDefault.DEFAULT;
	protected YesNoDefault upperBoundIsZero= YesNoDefault.DEFAULT;
	protected IntegerAttribute paletteIterations= IntegerAttribute.instanceDefault;
	protected ColorAttribute backgroundColor= ColorAttribute.instanceDefault;
	//
	protected long recentNumberOfBands= 1;
	//
	protected ColorMapName defaultName= ColorMapName.PURPLE;
	protected ColorMapSize defaultSize= ColorMapSize.instanceDefault;
	protected YesNoDefault defaultReverseScale= YesNoDefault.NO;
	protected YesNoDefault defaultReverseColors= YesNoDefault.NO;
	protected RealAttribute defaultLowerQuantile= RealAttribute.instanceDefault;
	protected RealAttribute defaultUpperQuantile= RealAttribute.instanceDefault;
	protected YesNoDefault defaultLowerBoundIsZero= YesNoDefault.NO;
	protected YesNoDefault defaultUpperBoundIsZero= YesNoDefault.NO;
	protected IntegerAttribute defaultPaletteIterations= IntegerAttribute.instanceDefault;
	protected YesNoDefault defaultReverseMinimalValue= YesNoDefault.NO;
	protected YesNoDefault defaultReverseMaximalValue= YesNoDefault.NO;
	protected TincturingCoefficient defaultTincturingCoefficient= TincturingCoefficient.instanceDefault;
	protected ColorAttribute defaultBackgroundColor= ColorAttribute.instanceDefault;
	//
	protected static IterativeDetailedColorMap defaultIterativeDetailedMainColorMap= new IterativeDetailedColorMap(
		ColorMapName.PURPLE,
		ColorMapSize.instanceDefault,
		YesNoDefault.NO,
		YesNoDefault.NO,
		RealAttribute.instanceDefault,
		RealAttribute.instanceDefault,
		YesNoDefault.NO,
		YesNoDefault.NO,
		IntegerAttribute.instanceDefault,
		YesNoDefault.NO,
		YesNoDefault.NO,
		TincturingCoefficient.instanceDefault,
		ColorAttribute.instanceDefault);
	protected static IterativeDetailedColorMap defaultAuxiliaryColorMap= new IterativeDetailedColorMap(
		ColorMapName.GRAY,
		ColorMapSize.instanceDefault,
		YesNoDefault.NO,
		YesNoDefault.NO,
		RealAttribute.instanceDefault,
		RealAttribute.instanceDefault,
		YesNoDefault.NO,
		YesNoDefault.NO,
		IntegerAttribute.instanceDefault,
		YesNoDefault.NO,
		YesNoDefault.NO,
		TincturingCoefficient.instanceDefault,
		ColorAttribute.instanceDefault);
	//
	protected static int maximalColorValue= 255;
	//
	private static final long serialVersionUID= 0xF09CD0F800A79263L; // -1108781644658535837
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","IterativeDetailedColorMap");
	//	SerialVersionChecker.report("morozov.system","IterativeDetailedColorMap");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public IterativeDetailedColorMap(
			ColorMapName givenName,
			ColorMapSize givenSize,
			YesNoDefault givenReverseScale,
			YesNoDefault givenReverseColors,
			RealAttribute givenLowerQuantile,
			RealAttribute givenUpperQuantile,
			YesNoDefault givenLowerBoundIsZero,
			YesNoDefault givenUpperBoundIsZero,
			IntegerAttribute givenPaletteIterations,
			YesNoDefault givenReverseMinimalValue,
			YesNoDefault givenReverseMaximalValue,
			TincturingCoefficient givenTincturingCoefficient,
			ColorAttribute givenBackgroundColor) {
		super(	givenName,
			givenSize,
			givenReverseScale,
			givenReverseMinimalValue,
			givenReverseMaximalValue,
			givenTincturingCoefficient);
		reverseColors= givenReverseColors;
		lowerQuantile= givenLowerQuantile;
		upperQuantile= givenUpperQuantile;
		lowerBoundIsZero= givenLowerBoundIsZero;
		upperBoundIsZero= givenUpperBoundIsZero;
		paletteIterations= givenPaletteIterations;
		backgroundColor= givenBackgroundColor;
	}
	public IterativeDetailedColorMap(
			DataColorMap givenDataColorMap) {
		super(givenDataColorMap);
		reverseColors= YesNoDefault.DEFAULT;
		lowerQuantile= RealAttribute.instanceDefault;
		upperQuantile= RealAttribute.instanceDefault;
		lowerBoundIsZero= YesNoDefault.DEFAULT;
		upperBoundIsZero= YesNoDefault.DEFAULT;
		paletteIterations= IntegerAttribute.instanceDefault;
		backgroundColor= ColorAttribute.instanceDefault;
	}
	public IterativeDetailedColorMap(
			Color[] givenArray,
			ColorMapSize givenSize,
			YesNoDefault givenReverseScale,
			YesNoDefault givenReverseColors,
			RealAttribute givenLowerQuantile,
			RealAttribute givenUpperQuantile,
			YesNoDefault givenLowerBoundIsZero,
			YesNoDefault givenUpperBoundIsZero,
			IntegerAttribute givenPaletteIterations,
			YesNoDefault givenReverseMinimalValue,
			YesNoDefault givenReverseMaximalValue,
			TincturingCoefficient givenTincturingCoefficient,
			ColorAttribute givenBackgroundColor) {
		super(	givenArray,
			givenSize,
			givenReverseScale,
			givenReverseMinimalValue,
			givenReverseMaximalValue,
			givenTincturingCoefficient);
		reverseColors= givenReverseColors;
		lowerQuantile= givenLowerQuantile;
		upperQuantile= givenUpperQuantile;
		lowerBoundIsZero= givenLowerBoundIsZero;
		upperBoundIsZero= givenUpperBoundIsZero;
		paletteIterations= givenPaletteIterations;
		backgroundColor= givenBackgroundColor;
	}
	public IterativeDetailedColorMap(Color[] givenArray) {
		super(givenArray);
		reverseColors= YesNoDefault.DEFAULT;
		lowerQuantile= RealAttribute.instanceDefault;
		upperQuantile= RealAttribute.instanceDefault;
		lowerBoundIsZero= YesNoDefault.DEFAULT;
		upperBoundIsZero= YesNoDefault.DEFAULT;
		paletteIterations= IntegerAttribute.instanceDefault;
		backgroundColor= ColorAttribute.instanceDefault;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setUseCustomMap(boolean mode) {
		useCustomMap= mode;
	}
	@Override
	public void setColors(Color[] c) {
		colors= c;
	}
	@Override
	public void setName(ColorMapName n) {
		name= n;
	}
	@Override
	public void setSize(ColorMapSize s) {
		size= s;
	}
	@Override
	public void setReverseScale(YesNoDefault mode) {
		reverseScale= mode;
	}
	@Override
	public void setReverseColors(YesNoDefault mode) {
		reverseColors= mode;
	}
	@Override
	public void setLowerQuantile(RealAttribute q) {
		lowerQuantile= q;
	}
	@Override
	public void setUpperQuantile(RealAttribute q) {
		upperQuantile= q;
	}
	@Override
	public void setLowerBoundIsZero(YesNoDefault mode) {
		lowerBoundIsZero= mode;
	}
	@Override
	public void setUpperBoundIsZero(YesNoDefault mode) {
		upperBoundIsZero= mode;
	}
	@Override
	public void setPaletteIterations(IntegerAttribute number) {
		paletteIterations= number;
	}
	@Override
	public void setReverseMinimalValue(YesNoDefault mode) {
		reverseMinimalValue= mode;
	}
	@Override
	public void setReverseMaximalValue(YesNoDefault mode) {
		reverseMaximalValue= mode;
	}
	@Override
	public void setTincturingCoefficient(TincturingCoefficient c) {
		tincturingCoefficient= c;
	}
	@Override
	public void setBackgroundColor(ColorAttribute b) {
		backgroundColor= b;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public ColorMapName getName() {
		return name;
	}
	@Override
	public ColorMapSize getSize() {
		return size.getValue(defaultSize);
	}
	@Override
	public YesNoDefault getReverseScale() {
		return reverseScale.getValue(defaultReverseScale);
	}
	@Override
	public YesNoDefault getReverseColors() {
		return reverseColors.getValue(defaultReverseColors);
	}
	@Override
	public RealAttribute getLowerQuantile() {
		return lowerQuantile.getValue(defaultLowerQuantile);
	}
	@Override
	public RealAttribute getUpperQuantile() {
		return upperQuantile.getValue(defaultUpperQuantile);
	}
	@Override
	public YesNoDefault getLowerBoundIsZero() {
		return lowerBoundIsZero.getValue(defaultLowerBoundIsZero);
	}
	@Override
	public YesNoDefault getUpperBoundIsZero() {
		return upperBoundIsZero.getValue(defaultUpperBoundIsZero);
	}
	@Override
	public IntegerAttribute getPaletteIterations() {
		return paletteIterations.getValue(defaultPaletteIterations);
	}
	@Override
	public YesNoDefault getReverseMinimalValue() {
		return reverseMinimalValue.getValue(defaultReverseMinimalValue);
	}
	@Override
	public YesNoDefault getReverseMaximalValue() {
		return reverseMaximalValue.getValue(defaultReverseMaximalValue);
	}
	@Override
	public TincturingCoefficient getTincturingCoefficient() {
		return tincturingCoefficient.getValue(defaultTincturingCoefficient);
	}
	@Override
	public ColorAttribute getBackgroundColor() {
		return backgroundColor.getValue(defaultBackgroundColor);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public ColorMapName getColorMapName() {
		return getName();
	}
	@Override
	public double getColorMapTincturingCoefficient() {
		return getTincturingCoefficient().getColorMapTincturingCoefficient();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int[][] toColors() {
		if (solidMatrix != null) {
			return solidMatrix;
		} else {
			solidMatrix= computeColors(ColorMapSize.getDefaultColorMapSize());
			recentNumberOfBands= computeNumberOfBands(paletteIterations);
			return solidMatrix;
		}
	}
	//
	@Override
	public long toNumberOfBands() {
		if (solidMatrix != null) {
			return recentNumberOfBands;
		} else {
			solidMatrix= computeColors(ColorMapSize.getDefaultColorMapSize());
			recentNumberOfBands= computeNumberOfBands(paletteIterations);
			return recentNumberOfBands;
		}
	}
	//
	@Override
	public int[][] computeColors(int defaultSize) {
		boolean doUseCustomMap= false;
		if (useCustomMap) {
			int length= colors.length;
			if (length > 0) {
				doUseCustomMap= true;
			}
		};
		if (doUseCustomMap) {
			int colorNumber= colors.length;
			long iterations= paletteIterations.getValue(getDefaultPaletteIterations()).getValue(1);
			int iterationNumber= Arithmetic.toInteger(iterations);
			int length= colorNumber * iterationNumber;
			int[][] array= new int[4][length];
			int counter= 0;
			for (int k=0; k < iterationNumber; k++) {
				for (int m=0; m < colorNumber; m++) {
					Color color= colors[m];
					int red= color.getRed();
					int green= color.getGreen();
					int blue= color.getBlue();
					int alpha= color.getAlpha();
					array[0][counter]= red;
					array[1][counter]= green;
					array[2][counter]= blue;
					array[3][counter]= alpha;
					counter++;
				}
			};
			return refineColorPalette(
				array,
				getReverseScale().toBoolean(false),
				getReverseColors().toBoolean(false),
				getReverseMinimalValue().toBoolean(false),
				getReverseMaximalValue().toBoolean(false));
		} else {
			return nameToArrays(
				name,
				getSize().getValue(defaultSize),
				getReverseScale().toBoolean(false),
				getReverseColors().toBoolean(false),
				getPaletteIterations(),
				getReverseMinimalValue().toBoolean(false),
				getReverseMaximalValue().toBoolean(false));
		}
	}
	//
	@Override
	public long computeNumberOfBands(IntegerAttribute paletteIterations) {
		boolean doUseCustomMap= false;
		if (useCustomMap) {
			int length= colors.length;
			if (length > 0) {
				doUseCustomMap= true;
			}
		};
		if (doUseCustomMap) {
			return paletteIterations.getValue(getDefaultPaletteIterations()).getValue(1);
		} else {
			return name.computeNumberOfBands(paletteIterations);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static IterativeDetailedColorMap getDefaultMainColorMap() {
		return defaultIterativeDetailedMainColorMap;
	}
	public static IterativeDetailedColorMap getDefaultAuxiliaryColorMap() {
		return defaultAuxiliaryColorMap;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setDefaultName(ColorMapName name) {
		defaultName= name;
	}
	@Override
	public void setDefaultSize(ColorMapSize size) {
		defaultSize= size;
	}
	@Override
	public void setDefaultReverseScale(YesNoDefault mode) {
		defaultReverseScale= mode;
	}
	@Override
	public void setDefaultReverseColors(YesNoDefault mode) {
		defaultReverseColors= mode;
	}
	@Override
	public void setDefaultLowerQuantile(RealAttribute quantile) {
		defaultLowerQuantile= quantile;
	}
	@Override
	public void setDefaultUpperQuantile(RealAttribute quantile) {
		defaultUpperQuantile= quantile;
	}
	@Override
	public void setDefaultLowerBoundIsZero(YesNoDefault mode) {
		defaultLowerBoundIsZero= mode;
	}
	@Override
	public void setDefaultUpperBoundIsZero(YesNoDefault mode) {
		defaultUpperBoundIsZero= mode;
	}
	@Override
	public void setDefaultPaletteIterations(IntegerAttribute number) {
		defaultPaletteIterations= number;
	}
	@Override
	public void setDefaultReverseMinimalValue(YesNoDefault mode) {
		defaultReverseMinimalValue= mode;
	}
	@Override
	public void setDefaultReverseMaximalValue(YesNoDefault mode) {
		defaultReverseMaximalValue= mode;
	}
	@Override
	public void setDefaultTincturingCoefficient(TincturingCoefficient value) {
		defaultTincturingCoefficient= value;
	}
	@Override
	public void setDefaultBackgroundColor(ColorAttribute color) {
		defaultBackgroundColor= color;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public ColorMapName getDefaultName() {
		return defaultName;
	}
	@Override
	public ColorMapSize getDefaultSize() {
		return defaultSize;
	}
	@Override
	public YesNoDefault getDefaultReverseScale() {
		return defaultReverseScale;
	}
	@Override
	public YesNoDefault getDefaultReverseColors() {
		return defaultReverseColors;
	}
	@Override
	public RealAttribute getDefaultLowerQuantile() {
		return defaultLowerQuantile;
	}
	@Override
	public RealAttribute getDefaultUpperQuantile() {
		return defaultUpperQuantile;
	}
	@Override
	public YesNoDefault getDefaultLowerBoundIsZero() {
		return defaultLowerBoundIsZero;
	}
	@Override
	public YesNoDefault getDefaultUpperBoundIsZero() {
		return defaultUpperBoundIsZero;
	}
	@Override
	public IntegerAttribute getDefaultPaletteIterations() {
		return defaultPaletteIterations;
	}
	@Override
	public YesNoDefault getDefaultReverseMinimalValue() {
		return defaultReverseMinimalValue;
	}
	@Override
	public YesNoDefault getDefaultReverseMaximalValue() {
		return defaultReverseMaximalValue;
	}
	@Override
	public TincturingCoefficient getDefaultTincturingCoefficient() {
		return defaultTincturingCoefficient;
	}
	@Override
	public ColorAttribute getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean getDefaultReverseCustomColorMapScale() {
		return getDefaultReverseScale().toBoolean(false);
	}
	@Override
	public boolean getDefaultReverseCustomColorMapMinimalValue() {
		return getDefaultReverseMinimalValue().toBoolean(false);
	}
	@Override
	public boolean getDefaultReverseCustomColorMapMaximalValue() {
		return getDefaultReverseMaximalValue().toBoolean(false);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int[][] nameToArrays(
			ColorMapName mapName,
			int mapLength,
			boolean reverseScale,
			boolean reverseColors,
			IntegerAttribute paletteIterations,
			boolean reverseMinValue,
			boolean reverseMaxValue) {
		if (mapLength <= 0) {
			mapLength= ColorMapSize.getDefaultColorMapSize();
		};
		int[][] array1= mapName.toColors(mapLength,paletteIterations);
		return refineColorPalette(
			array1,
			reverseScale,
			reverseColors,
			reverseMinValue,
			reverseMaxValue);
	}
	//
	@Override
	public int[][] refineColorPalette(
			int[][] array1,
			boolean reverseScale,
			boolean reverseColors,
			boolean reverseMinValue,
			boolean reverseMaxValue) {
		int mapLength= array1[0].length;
		if (reverseScale) {
			int[][] array2= new int[4][mapLength];
			for (int n=0; n < mapLength; n++) {
				array2[0][n]= array1[0][mapLength-n-1];
				array2[1][n]= array1[1][mapLength-n-1];
				array2[2][n]= array1[2][mapLength-n-1];
				array2[3][n]= array1[3][mapLength-n-1];
			};
			array1= array2;
			if (reverseColors) {
				for (int n=0; n < mapLength; n++) {
					array1[0][n]= maximalColorValue - array1[0][n];
					array1[1][n]= maximalColorValue - array1[1][n];
					array1[2][n]= maximalColorValue - array1[2][n];
				}
			}
		} else {
			if (reverseColors) {
				int[][] array2= new int[4][mapLength];
				for (int n=0; n < mapLength; n++) {
					array2[0][n]= maximalColorValue - array1[0][n];
					array2[1][n]= maximalColorValue - array1[1][n];
					array2[2][n]= maximalColorValue - array1[2][n];
					array2[3][n]= array1[3][n];
				};
				array1= array2;
			}
		};
		int end= array1[0].length-1;
		int redMaximalValue= 0;
		int greenMaximalValue= 0;
		int blueMaximalValue= 0;
		int alphaMaximalValue= 0;
		int redMinimalValue= 0;
		int greenMinimalValue= 0;
		int blueMinimalValue= 0;
		int alphaMinimalValue= 0;
		if (reverseMinValue) {
			redMaximalValue= array1[0][end];
			greenMaximalValue= array1[1][end];
			blueMaximalValue= array1[2][end];
			alphaMaximalValue= array1[3][end];
		};
		if (reverseMaxValue) {
			redMinimalValue= array1[0][0];
			greenMinimalValue= array1[1][0];
			blueMinimalValue= array1[2][0];
			alphaMinimalValue= array1[3][0];
		};
		if (reverseMinValue) {
			array1[0][0]= redMaximalValue;
			array1[1][0]= greenMaximalValue;
			array1[2][0]= blueMaximalValue;
			array1[3][0]= alphaMaximalValue;
		};
		if (reverseMaxValue) {
			array1[0][end]= redMinimalValue;
			array1[1][end]= greenMinimalValue;
			array1[2][end]= blueMinimalValue;
			array1[3][end]= alphaMinimalValue;
		};
		return array1;
	}
}
