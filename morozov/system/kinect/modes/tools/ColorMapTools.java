// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.tools;

import morozov.system.*;
import morozov.system.kinect.modes.*;

import java.awt.Color;

public class ColorMapTools {
	//
	public static ColorMapName defaultColorMapName= ColorMapName.JET;
	//
	protected static ColorMap defaultColorMap= new ColorMap(defaultColorMapName,ColorMapSizeTools.instanceDefault,YesNoDefault.YES,YesNoDefault.NO,YesNoDefault.NO,TincturingCoefficientTools.instanceDefault);
	//
	protected static int maxColorValue= 255;
	//
	protected static int[] peopleDefaultColorRed= new int[]
		{maxColorValue,0,0,maxColorValue,maxColorValue,0,maxColorValue};
	protected static int[] peopleDefaultColorGreen= new int[]
		{0,maxColorValue,0,maxColorValue,0,maxColorValue,maxColorValue};
	protected static int[] peopleDefaultColorBlue= new int[]
		{0,0,maxColorValue,0,maxColorValue,maxColorValue,maxColorValue};
	//
	protected static ColorMap peopleDefaultColorMap;
	//
	public static int defaultPeopleIndexRatio= 2;
	//
	///////////////////////////////////////////////////////////////
	//
	static {
		Color[] colors= new Color[peopleDefaultColorRed.length];
		for (int n=0; n < peopleDefaultColorRed.length; n++) {
			int red= peopleDefaultColorRed[n];
			int green= peopleDefaultColorGreen[n];
			int blue= peopleDefaultColorBlue[n];
			colors[n]= new Color(red,green,blue);
		};
		peopleDefaultColorMap= new ColorMap(colors);
	}	
	//
	///////////////////////////////////////////////////////////////
	//
	public static ColorMapName getDefaultColorMapName() {
		return defaultColorMapName;
	}
	public static ColorMap getDefaultColorMap() {
		return defaultColorMap;
	}
	public static int[] getPeopleDefaultColorRed() {
		return peopleDefaultColorRed;
	}
	public static int[] getPeopleDefaultColorGreen() {
		return peopleDefaultColorGreen;
	}
	public static int[] getPeopleDefaultColorBlue() {
		return peopleDefaultColorBlue;
	}
	public static ColorMap getPeopleDefaultColorMap() {
		return peopleDefaultColorMap;
	}
	//
	public static Color getPersonDefaultColor(int id) {
		if (id > peopleDefaultColorRed.length) {
			id= peopleDefaultColorRed.length;
		};
		int red= peopleDefaultColorRed[id];
		int green= peopleDefaultColorGreen[id];
		int blue= peopleDefaultColorBlue[id];
		return new Color(red,green,blue);
	}
	//
	public static int getDefaultPeopleIndexRatio() {
		return defaultPeopleIndexRatio;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int[][] nameToArrays(ColorMapName mapName, int mapLength, boolean reverseMap, boolean reverseMinValue, boolean reverseMaxValue) {
		if (mapLength <= 0) {
			mapLength= ColorMapSizeTools.getDefaultColorMapSize();
		};
		int[][] array1= mapName.toColors(mapLength);
		return refineColorPalette(array1,mapLength,reverseMap,reverseMinValue,reverseMaxValue);
	}
	//
	public static int[][] refineColorPalette(int[][] array1, int mapLength, boolean reverseMap, boolean reverseMinValue, boolean reverseMaxValue) {
		if (reverseMap) {
			int[][] array2= new int[3][mapLength];
			for (int n=0; n < mapLength; n++) {
				array2[0][n]= array1[0][mapLength-n-1];
				array2[1][n]= array1[1][mapLength-n-1];
				array2[2][n]= array1[2][mapLength-n-1];
			};
			array1= array2;
		};
		int redMinimalValue= array1[0][0];
		int greenMinimalValue= array1[1][0];
		int blueMinimalValue= array1[2][0];
		int end= array1[0].length-1;
		int redMaximalValue= array1[0][end];
		int greenMaximalValue= array1[1][end];
		int blueMaximalValue= array1[2][end];
		if (reverseMinValue) {
			array1[0][0]= redMaximalValue;
			array1[1][0]= greenMaximalValue;
			array1[2][0]= blueMaximalValue;
		};
		if (reverseMaxValue) {
			array1[0][end]= redMinimalValue;
			array1[1][end]= greenMinimalValue;
			array1[2][end]= blueMinimalValue;
		};
		return array1;
	}
}
