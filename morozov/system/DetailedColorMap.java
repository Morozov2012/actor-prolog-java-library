// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.modes.*;

import java.awt.Color;
import java.io.Serializable;

public class DetailedColorMap implements Serializable {
	//
	protected boolean useCustomMap;
	//
	protected Color[] colors;
	//
	protected ColorMapName name;
	protected ColorMapSize size= ColorMapSize.instanceDefault;
	protected YesNoDefault reverseScale= YesNoDefault.DEFAULT;
	protected YesNoDefault reverseMinimalValue= YesNoDefault.DEFAULT;
	protected YesNoDefault reverseMaximalValue= YesNoDefault.DEFAULT;
	protected TincturingCoefficient tincturingCoefficient= TincturingCoefficient.instanceDefault;
	//
	protected transient int[][] solidMatrix;
	//
	protected static DetailedColorMap defaultMainColorMap=
		new DetailedColorMap(
			ColorMapName.PURPLE,
			ColorMapSize.instanceDefault,
			YesNoDefault.NO,
			YesNoDefault.NO,
			YesNoDefault.NO,
			TincturingCoefficient.instanceDefault);
	protected static DetailedColorMap defaultGrayColorMap=
		new DetailedColorMap(
			ColorMapName.GRAY,
			ColorMapSize.instanceDefault,
			YesNoDefault.NO,
			YesNoDefault.NO,
			YesNoDefault.NO,
			TincturingCoefficient.instanceDefault);
	//
	private static final long serialVersionUID= 0xA3ABA1BF08DFF680L; // -6653046182650055040L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system","DetailedColorMap");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public DetailedColorMap(ColorMapName n, ColorMapSize s, YesNoDefault rS, YesNoDefault rMinV, YesNoDefault rMaxV, TincturingCoefficient tC) {
		useCustomMap= false;
		name= n;
		size= s;
		reverseScale= rS;
		reverseMinimalValue= rMinV;
		reverseMaximalValue= rMaxV;
		tincturingCoefficient= tC;
	}
	public DetailedColorMap(DataColorMap dataColorMap) {
		useCustomMap= false;
		name= dataColorMap.toColorMapName();
		size= ColorMapSize.instanceDefault;
		reverseScale= YesNoDefault.NO;
		reverseMinimalValue= YesNoDefault.NO;
		reverseMaximalValue= YesNoDefault.NO;
		tincturingCoefficient= TincturingCoefficient.instanceDefault;
	}
	public DetailedColorMap(Color[] array, ColorMapSize s, YesNoDefault rS, YesNoDefault rMinV, YesNoDefault rMaxV, TincturingCoefficient tC) {
		useCustomMap= true;
		colors= array;
		size= s;
		reverseScale= rS;
		reverseMinimalValue= rMinV;
		reverseMaximalValue= rMaxV;
		tincturingCoefficient= tC;
	}
	public DetailedColorMap(Color[] array) {
		useCustomMap= true;
		colors= array;
		size= new ColorMapSize(array.length);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseCustomMap() {
		return useCustomMap;
	}
	public Color[] getColors() {
		return colors;
	}
	public ColorMapName getColorMapName() {
		return name;
	}
	public ColorMapSize getSize() {
		return size;
	}
	public YesNoDefault getReverseScale() {
		return reverseScale;
	}
	public YesNoDefault getReverseMinimalValue() {
		return reverseMinimalValue;
	}
	public YesNoDefault getReverseMaximalValue() {
		return reverseMaximalValue;
	}
	public TincturingCoefficient getTincturingCoefficient() {
		return tincturingCoefficient;
	}
	public double getColorMapTincturingCoefficient() {
		return tincturingCoefficient.getColorMapTincturingCoefficient();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[][] toColors() {
		if (solidMatrix != null) {
			return solidMatrix;
		} else {
			solidMatrix= computeColors(ColorMapSize.getDefaultColorMapSize());
			return solidMatrix;
		}
	}
	//
	public DataColorMap toDataColorMap() {
		if (useCustomMap) {
			return getDefaultDataColorMap();
		} else {
			return name.toDataColorMap();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[][] computeColors(int defaultSize) {
		if (useCustomMap) {
			int length= colors.length;
			if (length <= 0) {
				return nameToArrays(
					getDefaultColorMapName(),
					defaultSize,
					reverseScale.toBoolean(getDefaultReverseCustomColorMapScale()),
					reverseMinimalValue.toBoolean(getDefaultReverseCustomColorMapMinimalValue()),
					reverseMaximalValue.toBoolean(getDefaultReverseCustomColorMapMaximalValue()));
			};
			int[][] array= new int[4][length];
			for (int n=0; n < length; n++) {
				Color color= colors[n];
				int red= color.getRed();
				int green= color.getGreen();
				int blue= color.getBlue();
				int alpha= color.getAlpha();
				array[0][n]= red;
				array[1][n]= green;
				array[2][n]= blue;
				array[3][n]= alpha;
			};
			return refineColorPalette(
				array,
				length,
				reverseScale.toBoolean(false),
				reverseMinimalValue.toBoolean(false),
				reverseMaximalValue.toBoolean(false));
		} else {
			int length= size.getValue(defaultSize);
			return nameToArrays(
				name,
				length,
				reverseScale.toBoolean(getDefaultReverseCustomColorMapScale()),
				reverseMinimalValue.toBoolean(getDefaultReverseCustomColorMapMinimalValue()),
				reverseMaximalValue.toBoolean(getDefaultReverseCustomColorMapMaximalValue()));
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static DetailedColorMap getDefaultColorMap() {
		return defaultMainColorMap;
	}
	public static DetailedColorMap getDefaultGrayColorMap() {
		return defaultGrayColorMap;
	}
	public ColorMapName getDefaultColorMapName() {
		return ColorMapName.PURPLE;
	}
	public DataColorMap getDefaultDataColorMap() {
		return DataColorMap.PURPLE;
	}
	//
	public boolean getDefaultReverseCustomColorMapScale() {
		return false;
	}
	public boolean getDefaultReverseCustomColorMapMinimalValue() {
		return false;
	}
	public boolean getDefaultReverseCustomColorMapMaximalValue() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int[][] nameToArrays(ColorMapName mapName, int mapLength, boolean reverseMap, boolean reverseMinValue, boolean reverseMaxValue) {
		if (mapLength <= 0) {
			mapLength= ColorMapSize.getDefaultColorMapSize();
		};
		int[][] array1= mapName.toColors(mapLength);
		return refineColorPalette(array1,mapLength,reverseMap,reverseMinValue,reverseMaxValue);
	}
	//
	public static int[][] refineColorPalette(int[][] array1, int mapLength, boolean reverseMap, boolean reverseMinValue, boolean reverseMaxValue) {
		if (reverseMap) {
			int[][] array2= new int[4][mapLength];
			for (int n=0; n < mapLength; n++) {
				array2[0][n]= array1[0][mapLength-n-1];
				array2[1][n]= array1[1][mapLength-n-1];
				array2[2][n]= array1[2][mapLength-n-1];
				array2[3][n]= array1[3][mapLength-n-1];
			};
			array1= array2;
		};
		int redMinimalValue= array1[0][0];
		int greenMinimalValue= array1[1][0];
		int blueMinimalValue= array1[2][0];
		int alphaMinimalValue= array1[3][0];
		int end= array1[0].length-1;
		int redMaximalValue= array1[0][end];
		int greenMaximalValue= array1[1][end];
		int blueMaximalValue= array1[2][end];
		int alphaMaximalValue= array1[3][end];
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
