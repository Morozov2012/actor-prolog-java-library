// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.classes.*;

import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.Dimension;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public class InnerPage extends JInternalFrame {
	//
	public AtomicReference<ExtendedSize> logicalWidth= new AtomicReference<ExtendedSize>(new ExtendedSize());
	public AtomicReference<ExtendedSize> logicalHeight= new AtomicReference<ExtendedSize>(new ExtendedSize());
	public AtomicReference<ExtendedCoordinate> logicalX= new AtomicReference<ExtendedCoordinate>(new ExtendedCoordinate());
	public AtomicReference<ExtendedCoordinate> logicalY= new AtomicReference<ExtendedCoordinate>(new ExtendedCoordinate());
	//
	protected java.util.Timer scheduler;
	protected TimerTask currentTask;
	//
	protected long repaintingDelay= 10; // ms
	// protected long repaintingDelay= 1; // ms
	// protected long repaintingDelay= 100; // ms
	//
	public InnerPage(String title) {
		super(	title, // "Event Generator"
			true, // Resizable
			true, // Closable
			true, // Maximizable
			true); // Iconifiable
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		// setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		// addPropertyChangeListener(this);
		scheduler= new java.util.Timer(true);
	}
	public void safelyRestoreSize(StaticContext context) {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
		if (desktop==null) {
			return;
		};
		Dimension size= DesktopUtils.safelyGetComponentSize(desktop);
		int desktopWidth= size.width;
		int desktopHeight= size.height;
		//
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		//
		// try {
		//
		CoordinateAndSize realXandWidth= CoordinateAndSize.calculate(logicalX.get(),logicalWidth.get(),desktopWidth,gridX);
		CoordinateAndSize realYandHeight= CoordinateAndSize.calculate(logicalY.get(),logicalHeight.get(),desktopHeight,gridY);
		//
		int realX= realXandWidth.coordinate;
		int realY= realYandHeight.coordinate;
		int realWidth= realXandWidth.size;
		int realHeight= realYandHeight.size;
		//
		if (	realXandWidth.coordinateIsToBeCalculatedAutomatically &&
			realYandHeight.coordinateIsToBeCalculatedAutomatically) {
			Dimension sizeDifference= new Dimension();
			DesktopUtils.safelyGetSizeDifference(this,sizeDifference);
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
			DesktopUtils.safelyGetSizeDifference(this,sizeDifference);
			int step= StrictMath.max(sizeDifference.width,sizeDifference.height);
			int newPosition= StaticDesktopAttributes.increaseDefaultPosition(context,step,realWidth,desktopWidth);
			if (newPosition <= 0) {
				realX= 0;
			} else {
				realX= step * newPosition;
			}
		} else if (realYandHeight.coordinateIsToBeCalculatedAutomatically) {
			Dimension sizeDifference= new Dimension();
			DesktopUtils.safelyGetSizeDifference(this,sizeDifference);
			int step= StrictMath.max(sizeDifference.width,sizeDifference.height);
			int newPosition= StaticDesktopAttributes.increaseDefaultPosition(context,step,realHeight,desktopHeight);
			if (newPosition <= 0) {
				realY= 0;
			} else {
				realY= step * newPosition;
			}
		};
		safelyRestoreSize(realX,realY,realWidth,realHeight);
	}
	// public void safelyRestoreSize(StaticContext context) {
	//	MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
	//	if (desktop!=null) {
	//		Dimension size= DesktopUtils.safelyGetComponentSize(desktop);
	//		safelyRestoreSize(0,0,size.width,size.height);
	//	}
	// }
	public void safelyRestoreSize(final int x, final int y, final int width, final int height) {
		if (SwingUtilities.isEventDispatchThread()) {
			restoreSize(x,y,width,height);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						restoreSize(x,y,width,height);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void restoreSize(int x, int y, int width, int height) {
		setLocation(x,y);
		setSize(width,height);
	}
	// public void propertyChange(PropertyChangeEvent evt) {
	//	System.out.printf("\nEVT=%s\n",evt);
	// }
	//
	public void repaintAfterDelay() {
		synchronized(this) {
			if (currentTask != null) {
				currentTask.cancel();
				scheduler.purge();
			};
			currentTask= new LocalTask(this);
			scheduler.schedule(currentTask,repaintingDelay);
		}
	}
	public void skipDelayedRepainting() {
		synchronized(this) {
			if (currentTask != null) {
				currentTask.cancel();
				scheduler.purge();
			};
			currentTask= null;
		}
	}
	//
	public void repaint() {
		super.repaint();
	}
}

class LocalTask extends TimerTask {
	private InnerPage innerPage;
	//
	public LocalTask(InnerPage page) {
		innerPage= page;
	}
	//
	public void run() {
		// System.out.printf("LOCAL TASK!!!\n");
		DesktopUtils.safelyRepaint(innerPage);
		// innerPage.repaint();
		// innerPage.revalidate();
	}
}
