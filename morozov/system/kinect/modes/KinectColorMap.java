// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.*;
import morozov.system.kinect.modes.tools.*;
import morozov.system.modes.*;

import java.awt.Color;

public class KinectColorMap extends DetailedColorMap {
	//
	protected int[][] periodicMatrix;
	protected int maximalNumberOfSkeletons= -1;
	//
	protected static DetailedColorMap defaultKinectColorMap=
		new DetailedColorMap(
			ColorMapName.OCEAN,
			ColorMapSize.instanceDefault,
			YesNoDefault.YES,
			YesNoDefault.NO,
			YesNoDefault.NO,
			TincturingCoefficient.instanceDefault);
	//
	private static final long serialVersionUID= 0x969020A0B5547BA0L; // -7597536696764957792L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.modes","KinectColorMap");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectColorMap(ColorMapName n, ColorMapSize s, YesNoDefault rS, YesNoDefault rMinV, YesNoDefault rMaxV, TincturingCoefficient tC) {
		super(n,s,rS,rMinV,rMaxV,tC);
	}
	public KinectColorMap(Color[] array, ColorMapSize s, YesNoDefault rS, YesNoDefault rMinV, YesNoDefault rMaxV, TincturingCoefficient tC) {
		super(array,s,rS,rMinV,rMaxV,tC);
	}
	public KinectColorMap(Color[] array) {
		super(array);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static DetailedColorMap getDefaultColorMap() {
		return defaultKinectColorMap;
	}
	public ColorMapName getDefaultColorMapName() {
		return ColorMapName.OCEAN;
	}
	public DataColorMap getDefaultDataColorMap() {
		return DataColorMap.OCEAN;
	}
	//
	public boolean getDefaultReverseCustomColorMapScale() {
		return true;
	}
	public boolean getDefaultReverseCustomColorMapMinimalValue() {
		return true;
	}
	public boolean getDefaultReverseCustomColorMapMaximalValue() {
		return false;
	}
	//
	public double getPeopleColorsTincturingCoefficient() {
		return TincturingCoefficientTools.getPeopleColorsTincturingCoefficient(tincturingCoefficient);
	}
	//
	///////////////////////////////////////////////////////////////
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
				int[] red= KinectColorMapTools.getPeopleDefaultColorRed();
				int[] green= KinectColorMapTools.getPeopleDefaultColorGreen();
				int[] blue= KinectColorMapTools.getPeopleDefaultColorBlue();
				int[][] array= new int[4][red.length];
				int peopleIndexRatio= KinectColorMapTools.getDefaultPeopleIndexRatio();
				for (int n=0; n < red.length; n++) {
					array[0][n]= red[n] / peopleIndexRatio;
					array[1][n]= green[n] / peopleIndexRatio;
					array[2][n]= blue[n] / peopleIndexRatio;
					array[3][n]= 0;
				};
				return refineColorPalette(
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
}
