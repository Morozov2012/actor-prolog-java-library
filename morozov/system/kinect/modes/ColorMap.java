// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.tools.*;

import java.awt.Color;
import java.io.Serializable;

public class ColorMap implements ColorMapInterface, Serializable {
	//
	protected boolean useCustomMap;
	//
	protected Color[] colors;
	//
	protected ColorMapName name;
	protected ColorMapSizeInterface size;
	protected YesNoDefault reverseScale;
	protected YesNoDefault reverseMinimalValue;
	protected YesNoDefault reverseMaximalValue;
	protected TincturingCoefficientInterface tincturingCoefficient;
	//
	protected int[][] solidMatrix;
	protected int[][] periodicMatrix;
	protected int maximalNumberOfSkeletons= -1;
	//
	///////////////////////////////////////////////////////////////
	//
	public ColorMap(ColorMapName n, ColorMapSizeInterface s, YesNoDefault rS, YesNoDefault rMinV, YesNoDefault rMaxV, TincturingCoefficientInterface tC) {
		useCustomMap= false;
		name= n;
		size= s;
		reverseScale= rS;
		reverseMinimalValue= rMinV;
		reverseMaximalValue= rMaxV;
		tincturingCoefficient= tC;
	}
	public ColorMap(Color[] array, ColorMapSizeInterface s, YesNoDefault rS, YesNoDefault rMinV, YesNoDefault rMaxV, TincturingCoefficientInterface tC) {
		useCustomMap= true;
		colors= array;
		size= s;
		reverseScale= rS;
		reverseMinimalValue= rMinV;
		reverseMaximalValue= rMaxV;
		tincturingCoefficient= tC;
	}
	public ColorMap(Color[] array) {
		useCustomMap= true;
		colors= array;
		size= ColorMapSizeTools.instanceDefault;
		reverseScale= YesNoDefault.NO;
		reverseMinimalValue= YesNoDefault.YES;
		reverseMaximalValue= YesNoDefault.NO;
		tincturingCoefficient= TincturingCoefficientTools.instanceDefault;
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
	public ColorMapSizeInterface getSize() {
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
	public TincturingCoefficientInterface getTincturingCoefficient() {
		return tincturingCoefficient;
	}
	public double getColorMapTincturingCoefficient() {
		return tincturingCoefficient.getColorMapTincturingCoefficient();
	}
	public double getPeopleColorsTincturingCoefficient() {
		return tincturingCoefficient.getPeopleColorsTincturingCoefficient();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[][] toColors() {
		if (solidMatrix != null) {
			return solidMatrix;
		} else {
			solidMatrix= computeColors(ColorMapSizeTools.getDefaultColorMapSize());
			return solidMatrix;
		}
	}
	//
	public int[][] toRepeatedColors(int requiredSize) {
		if (periodicMatrix != null && maximalNumberOfSkeletons==requiredSize) {
			return periodicMatrix;
		} else {
			int[][] matrix1= computePeopleColors(requiredSize);
			int actualSize= matrix1[0].length;
			if (actualSize >= requiredSize) {
				periodicMatrix= matrix1;
			} else {
				int[][] matrix2= new int[3][requiredSize];
				boolean continueLoop= true;
				int k= -1;
				while (continueLoop) {
					for (int n=0; n < actualSize; n++) {
						if (k >= requiredSize-1) {
							continueLoop= false;
							break;
						};
						k++;
						for (int c=0; c < 3; c++) {
							matrix2[c][k]= matrix1[c][n];
						}
					}
				};
				periodicMatrix= matrix2;
			};
			maximalNumberOfSkeletons= requiredSize;
			return periodicMatrix;
		}
	}
	//
	public int[][] computePeopleColors(int requiredSize) {
		int[][] matrix1;
		if (useCustomMap) {
			int length= colors.length;
			if (length <= 0) {
				int[] red= ColorMapTools.getPeopleDefaultColorRed();
				int[] green= ColorMapTools.getPeopleDefaultColorGreen();
				int[] blue= ColorMapTools.getPeopleDefaultColorBlue();
				int[][] array= new int[3][red.length];
				int peopleIndexRatio= ColorMapTools.getDefaultPeopleIndexRatio();
				for (int n=0; n < red.length; n++) {
					array[0][n]= red[n] / peopleIndexRatio;
					array[1][n]= green[n] / peopleIndexRatio;
					array[2][n]= blue[n] / peopleIndexRatio;
				};
				return ColorMapTools.refineColorPalette(
					array,
					length,
					reverseScale.toBoolean(false),
					reverseMinimalValue.toBoolean(false),
					reverseMaximalValue.toBoolean(false));
			} else {
				return computeColors(requiredSize);
			}
		} else {
			return computeColors(requiredSize);
		}
	}
	//
	public int[][] computeColors(int defaultSize) {
		if (useCustomMap) {
			int length= colors.length;
			if (length <= 0) {
				return ColorMapTools.nameToArrays(
					ColorMapTools.getDefaultColorMapName(),
					defaultSize,
					reverseScale.toBoolean(true),
					reverseMinimalValue.toBoolean(true),
					reverseMaximalValue.toBoolean(false));
			};
			int[][] array= new int[3][length];
			for (int n=0; n < length; n++) {
				Color color= colors[n];
				int red= color.getRed();
				int green= color.getGreen();
				int blue= color.getBlue();
				array[0][n]= red;
				array[1][n]= green;
				array[2][n]= blue;
			};
			return ColorMapTools.refineColorPalette(
				array,
				length,
				reverseScale.toBoolean(false),
				reverseMinimalValue.toBoolean(false),
				reverseMaximalValue.toBoolean(false));
		} else {
			int length= size.getValue(defaultSize);
			return ColorMapTools.nameToArrays(
				name,
				length,
				reverseScale.toBoolean(true),
				reverseMinimalValue.toBoolean(true),
				reverseMaximalValue.toBoolean(false));
		}
	}
}
