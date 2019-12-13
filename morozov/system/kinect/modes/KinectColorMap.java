// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.*;
import morozov.system.kinect.modes.tools.*;
import morozov.system.interfaces.*;

import java.io.Serializable;

public class KinectColorMap implements Serializable {
	//
	protected IterativeDetailedColorMapInterface iterativeDetailedColorMap;
	protected int[][] periodicMatrix;
	protected int maximalNumberOfSkeletons= -1;
	//
	private static final long serialVersionUID= 0x969020A0B5547BA0L; // -7597536696764957792L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.modes","KinectColorMap");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectColorMap(IterativeDetailedColorMap map) {
		map.setDefaultName(getDefaultName());
		map.setDefaultSize(getDefaultSize());
		map.setDefaultReverseScale(getDefaultReverseScale());
		map.setDefaultReverseColors(getDefaultReverseColors());
		map.setDefaultLowerQuantile(getDefaultLowerQuantile());
		map.setDefaultUpperQuantile(getDefaultUpperQuantile());
		map.setDefaultLowerBoundIsZero(getDefaultLowerBoundIsZero());
		map.setDefaultUpperBoundIsZero(getDefaultUpperBoundIsZero());
		map.setDefaultPaletteIterations(getDefaultPaletteIterations());
		map.setDefaultReverseMinimalValue(getDefaultReverseMinimalValue());
		map.setDefaultReverseMaximalValue(getDefaultReverseMaximalValue());
		map.setDefaultTincturingCoefficient(getDefaultTincturingCoefficient());
		map.setDefaultBackgroundColor(getDefaultBackgroundColor());
		iterativeDetailedColorMap= map;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public IterativeDetailedColorMapInterface getIterativeDetailedColorMap() {
		return iterativeDetailedColorMap;
	}
	public int[][] getPeriodicMatrix() {
		return periodicMatrix;
	}
	public int getMaximalNumberOfSkeletons() {
		return maximalNumberOfSkeletons;
	}
	//
	public ColorMapName getName() {
		return iterativeDetailedColorMap.getName();
	}
	public ColorMapSize getSize() {
		return iterativeDetailedColorMap.getSize();
	}
	public YesNoDefault getReverseScale() {
		return iterativeDetailedColorMap.getReverseScale();
	}
	public YesNoDefault getReverseColors() {
		return iterativeDetailedColorMap.getReverseColors();
	}
	public RealAttribute getLowerQuantile() {
		return iterativeDetailedColorMap.getLowerQuantile();
	}
	public RealAttribute getUpperQuantile() {
		return iterativeDetailedColorMap.getUpperQuantile();
	}
	public YesNoDefault getLowerBoundIsZero() {
		return iterativeDetailedColorMap.getLowerBoundIsZero();
	}
	public YesNoDefault getUpperBoundIsZero() {
		return iterativeDetailedColorMap.getUpperBoundIsZero();
	}
	public IntegerAttribute getPaletteIterations() {
		return iterativeDetailedColorMap.getPaletteIterations();
	}
	public YesNoDefault getReverseMinimalValue() {
		return iterativeDetailedColorMap.getReverseMinimalValue();
	}
	public YesNoDefault getReverseMaximalValue() {
		return iterativeDetailedColorMap.getReverseMaximalValue();
	}
	public TincturingCoefficient getTincturingCoefficient() {
		return iterativeDetailedColorMap.getTincturingCoefficient();
	}
	public double getColorMapTincturingCoefficient() {
		return iterativeDetailedColorMap.getTincturingCoefficient().getColorMapTincturingCoefficient();
	}
	public double getPeopleColorsTincturingCoefficient() {
		return TincturingCoefficientTools.getPeopleColorsTincturingCoefficient(iterativeDetailedColorMap.getTincturingCoefficient());
	}
	public ColorAttribute getBackgroundColor() {
		return iterativeDetailedColorMap.getBackgroundColor();
	}
	//
	public int[][] toColors() {
		return iterativeDetailedColorMap.toColors();
	}
	public long toNumberOfBands() {
		return iterativeDetailedColorMap.toNumberOfBands();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ColorMapName getDefaultName() {
		return ColorMapName.OCEAN;
	}
	public ColorMapSize getDefaultSize() {
		return ColorMapSize.instanceDefault;
	}
	public YesNoDefault getDefaultReverseScale() {
		return YesNoDefault.YES;
	}
	public YesNoDefault getDefaultReverseColors() {
		return YesNoDefault.DEFAULT;
	}
	public RealAttribute getDefaultLowerQuantile() {
		return RealAttribute.instanceDefault;
	}
	public RealAttribute getDefaultUpperQuantile() {
		return RealAttribute.instanceDefault;
	}
	public YesNoDefault getDefaultLowerBoundIsZero() {
		return YesNoDefault.DEFAULT;
	}
	public YesNoDefault getDefaultUpperBoundIsZero() {
		return YesNoDefault.DEFAULT;
	}
	public IntegerAttribute getDefaultPaletteIterations() {
		return IntegerAttribute.instanceDefault;
	}
	public YesNoDefault getDefaultReverseMinimalValue() {
		return YesNoDefault.YES;
	}
	public YesNoDefault getDefaultReverseMaximalValue() {
		return YesNoDefault.DEFAULT;
	}
	public TincturingCoefficient getDefaultTincturingCoefficient() {
		return TincturingCoefficient.instanceDefault;
	}
	public ColorAttribute getDefaultBackgroundColor() {
		return ColorAttribute.instanceDefault;
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
		if (iterativeDetailedColorMap.getUseCustomMap()) {
			int length= iterativeDetailedColorMap.getColors().length;
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
				return iterativeDetailedColorMap.refineColorPalette(
					array,
					iterativeDetailedColorMap.getReverseScale().toBoolean(false),
					iterativeDetailedColorMap.getReverseColors().toBoolean(false),
					iterativeDetailedColorMap.getReverseMinimalValue().toBoolean(false),
					iterativeDetailedColorMap.getReverseMaximalValue().toBoolean(false));
			} else {
				return iterativeDetailedColorMap.computeColors(requiredSize);
			}
		} else {
			return iterativeDetailedColorMap.computeColors(requiredSize);
		}
	}
}
