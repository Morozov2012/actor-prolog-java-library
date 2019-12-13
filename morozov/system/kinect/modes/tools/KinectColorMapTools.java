// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.tools;

import morozov.system.*;

import java.awt.Color;

public class KinectColorMapTools {
	//
	protected static final int maxColorValue= 255;
	//
	protected static final int[] peopleDefaultColorRed= new int[]
		{maxColorValue,0,0,maxColorValue,maxColorValue,0,maxColorValue};
	protected static final int[] peopleDefaultColorGreen= new int[]
		{0,maxColorValue,0,maxColorValue,0,maxColorValue,maxColorValue};
	protected static final int[] peopleDefaultColorBlue= new int[]
		{0,0,maxColorValue,0,maxColorValue,maxColorValue,maxColorValue};
	//
	public static final int defaultPeopleIndexRatio= 2;
	//
	protected static DetailedColorMap peopleDefaultColorMap;
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
		peopleDefaultColorMap= new DetailedColorMap(colors);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int[] getPeopleDefaultColorRed() {
		return peopleDefaultColorRed;
	}
	public static int[] getPeopleDefaultColorGreen() {
		return peopleDefaultColorGreen;
	}
	public static int[] getPeopleDefaultColorBlue() {
		return peopleDefaultColorBlue;
	}
	public static DetailedColorMap getPeopleDefaultColorMap() {
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
}
