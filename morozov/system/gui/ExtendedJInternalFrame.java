// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;

import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ExtendedJInternalFrame extends JInternalFrame {
	//
	public AtomicReference<ExtendedCoordinate> logicalX;
	public AtomicReference<ExtendedCoordinate> logicalY;
	public AtomicReference<ExtendedSize> logicalWidth;
	public AtomicReference<ExtendedSize> logicalHeight;
	public AtomicBoolean usePixelMeasurements;
	//
	public ExtendedJInternalFrame(InnerPage page, String title, AtomicReference<ExtendedCoordinate> x, AtomicReference<ExtendedCoordinate> y, AtomicReference<ExtendedSize> width, AtomicReference<ExtendedSize> height, AtomicBoolean pixelMeasurements) {
		super(	title, // "Event Generator"
			true, // Resizable
			true, // Closable
			true, // Maximizable
			true); // Iconifiable
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		addFocusListener(page);
		logicalX= x;
		logicalY= y;
		logicalWidth= width;
		logicalHeight= height;
		usePixelMeasurements= pixelMeasurements;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void quicklyRestoreSize(StaticContext context) {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(context);
		if (desktop==null) {
			return;
		};
		Dimension size= desktop.getSize();
		int desktopWidth= size.width;
		int desktopHeight= size.height;
		//
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		//
		CoordinateAndSize realXandWidth;
		CoordinateAndSize realYandHeight;
		if (usePixelMeasurements.get()) {
			realXandWidth= CoordinateAndSize.calculate(logicalX.get(),logicalWidth.get(),desktopWidth);
			realYandHeight= CoordinateAndSize.calculate(logicalY.get(),logicalHeight.get(),desktopHeight);
		} else {
			realXandWidth= CoordinateAndSize.calculate(logicalX.get(),logicalWidth.get(),desktopWidth,gridX);
			realYandHeight= CoordinateAndSize.calculate(logicalY.get(),logicalHeight.get(),desktopHeight,gridY);
		};
		//
		int realX= realXandWidth.coordinate;
		int realY= realYandHeight.coordinate;
		int realWidth= realXandWidth.size;
		int realHeight= realYandHeight.size;
		//
		if (	realXandWidth.coordinateIsToBeCalculatedAutomatically &&
			realYandHeight.coordinateIsToBeCalculatedAutomatically) {
			Dimension sizeDifference= new Dimension();
			getSizeDifference(sizeDifference);
			int step= StrictMath.max(sizeDifference.width,sizeDifference.height);
			int newPosition= StaticDesktopAttributes.increaseDefaultPosition(context,step,realWidth,realHeight,desktopWidth,desktopHeight);
			if (newPosition <= 0) {
				realY= 0;
				realX= 0;
			} else {
				realY= step * newPosition;
				realX= step * newPosition;
			}
		} else if (realXandWidth.coordinateIsToBeCalculatedAutomatically) {
			Dimension sizeDifference= new Dimension();
			getSizeDifference(sizeDifference);
			int step= StrictMath.max(sizeDifference.width,sizeDifference.height);
			int newPosition= StaticDesktopAttributes.increaseDefaultPosition(context,step,realWidth,desktopWidth);
			if (newPosition <= 0) {
				realX= 0;
			} else {
				realX= step * newPosition;
			}
		} else if (realYandHeight.coordinateIsToBeCalculatedAutomatically) {
			Dimension sizeDifference= new Dimension();
			getSizeDifference(sizeDifference);
			int step= StrictMath.max(sizeDifference.width,sizeDifference.height);
			int newPosition= StaticDesktopAttributes.increaseDefaultPosition(context,step,realHeight,desktopHeight);
			if (newPosition <= 0) {
				realY= 0;
			} else {
				realY= step * newPosition;
			}
		};
		restoreSize(realX,realY,realWidth,realHeight);
	}
	protected void getSizeDifference(Dimension sizeDifference) {
		getSize(sizeDifference);
		Dimension cd= getContentPane().getSize();
		sizeDifference.width= sizeDifference.width - cd.width;
		sizeDifference.height= sizeDifference.height - cd.height;
	}
	protected void restoreSize(int x, int y, int width, int height) {
		setLocation(x,y);
		setSize(width,height);
	}
}
