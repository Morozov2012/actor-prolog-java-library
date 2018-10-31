// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.system.gui.*;
import morozov.system.gui.signals.*;
import morozov.terms.*;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DialogKernel extends DialogFoundation {
	//
	protected AtomicBoolean isInitiated= new AtomicBoolean(false);
	//
	protected AtomicBoolean insideDoLayout= new AtomicBoolean(false);
	//
	protected DialogEntry[] controlTable= new DialogEntry[0];
	//
	protected ActiveUser specialProcess= new ActiveUser();
	//
	///////////////////////////////////////////////////////////////
	//
	private static final int maximalNumberOfIterations= 1000;
	//
	///////////////////////////////////////////////////////////////
	//
	public void doLayout() {
		doLayout(false);
	}
	public void doLayout(boolean forceCheck) {
		if (insideDoLayout.compareAndSet(false,true)) {
			try {
				safelyDoLayout(forceCheck);
			} finally {
				insideDoLayout.set(false);
			}
		}
	}
	public void safelyDoLayout(final boolean forceCheck) {
		if (SwingUtilities.isEventDispatchThread()) {
			do_layout(forceCheck);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						do_layout(forceCheck);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void do_layout(boolean forceCheck) {
		if (previousActualSize.get().width==0) {
			return;
		};
		Dimension dimension= new Dimension();
		dialogContainer.getSize(dimension);
		int givenWidth= dimension.width;
		int givenHeight= dimension.height;
		if (givenWidth <= 0 || givenHeight <= 0) {
			return;
		};
		Dimension minimumSize= dialogContainer.getRealMinimumSize();
		int correctedGivenWidth= StrictMath.max(0,givenWidth-minimumSize.width);
		int correctedGivenHeight= StrictMath.max(0,givenHeight-minimumSize.height);
		boolean interruptLayout= false;
		Dimension previousActualWidthAndHeight= previousActualSize.get();
		Dimension prefLS= dialogContainer.getRealPreferredSize();
		if (givenWidth < prefLS.width || givenHeight < prefLS.height) {
			forceCheck= true;
		};
		if (givenWidth!=previousActualWidthAndHeight.width || givenHeight!=previousActualWidthAndHeight.height || forceCheck) {
			int netPreferredWidth= 0;
			int netPreferredHeight= 0;
			double maximalRefusedSize= 0;
			for (int i= 1; i <= maximalNumberOfIterations; i++) {
				int minimumWidth= minimumSize.width;
				int minimumHeight= minimumSize.height;
				if (minimumWidth <= 0 || minimumHeight <= 0) {
					interruptLayout= true;
					break;
				};
				int requestedPreferredWidth= prefLS.width;
				int requestedPreferredHeight= prefLS.height;
				netPreferredWidth= StrictMath.max(0,requestedPreferredWidth-minimumWidth);
				netPreferredHeight= StrictMath.max(0,requestedPreferredHeight-minimumHeight);
				double widthIncrease1= 1;
				double heightIncrease1= 1;
				double increaseCoefficient1= 1;
				if (netPreferredWidth > 0) {
					widthIncrease1= ((double)correctedGivenWidth) / netPreferredWidth;
					increaseCoefficient1= widthIncrease1;
				};
				if (netPreferredHeight > 0) {
					heightIncrease1= ((double)correctedGivenHeight) / netPreferredHeight;
					if (netPreferredWidth > 0) {
						increaseCoefficient1= Math.min(widthIncrease1,heightIncrease1);
					} else {
						increaseCoefficient1= heightIncrease1;
					}
				};
				int previousFontSize= getCurrentFontSize();
				int prospectiveFontSize= previousFontSize;
				if (increaseCoefficient1 < 1) {
					if (maximalRefusedSize > 0) {
						maximalRefusedSize= StrictMath.min(maximalRefusedSize,prospectiveFontSize);
					} else {
						maximalRefusedSize= prospectiveFontSize;
					};
					prospectiveFontSize= PrologInteger.toInteger(prospectiveFontSize * increaseCoefficient1);
				} else {
					prospectiveFontSize= PrologInteger.toInteger(prospectiveFontSize * increaseCoefficient1);
					if (maximalRefusedSize > 0) {
						prospectiveFontSize= (int)StrictMath.min(prospectiveFontSize,maximalRefusedSize-1);
					}
				};
				prospectiveFontSize= StrictMath.max(prospectiveFontSize,1);
				setCurrentFontSize(prospectiveFontSize);
				Font prospectiveFont;
				if (approvedFonts.containsKey(prospectiveFontSize)) {
					prospectiveFont= approvedFonts.get(prospectiveFontSize);
				} else {
					prospectiveFont= create_new_font();
				};
				if (previousFontSize != prospectiveFontSize) {
					setGeneralFont(prospectiveFont);
				};
				prefLS= dialogContainer.getRealPreferredSize();
				dialogContainer.getSize(dimension);
				if ( (dimension.width != givenWidth) || (dimension.height != givenHeight) ) {
					interruptLayout= true;
					break;
				};
				if (previousFontSize==prospectiveFontSize) {
					if (prefLS.width > givenWidth || prefLS.height > givenHeight) {
						prospectiveFontSize= prospectiveFontSize - 1;
						prospectiveFontSize= StrictMath.max(prospectiveFontSize,1);
						setCurrentFontSize(prospectiveFontSize);
						if (approvedFonts.containsKey(prospectiveFontSize)) {
							prospectiveFont= approvedFonts.get(prospectiveFontSize);
						} else {
							prospectiveFont= create_new_font();
						};
						setGeneralFont(prospectiveFont);
					}
					break;
				}
			};
			if (!interruptLayout) {
				previousActualSize.set(new Dimension(givenWidth,givenHeight));
			};
			safelyCentreMainPanelIfNecessary();
		};
		dialogContainer.doSuperLayout();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyAcceptNewPositionOfDialog() {
		Point p= safelyGetLocation();
		Point previousPoint= previousCoordinates.get();
		if (previousPoint==null || previousPoint.x!=p.x || previousPoint.y!=p.y) {
			Rectangle parentLayoutSize= safelyComputeParentLayoutSize();
			double gridX= DefaultOptions.gridWidth;
			double gridY= DefaultOptions.gridHeight;
			double newX= (double)p.x / ( ((double)(parentLayoutSize.width-parentLayoutSize.x)) / gridX );
			double newY= (double)p.y / ( ((double)(parentLayoutSize.height-parentLayoutSize.y)) / gridY );
			actualCoordinates.set(new ExtendedCoordinates(newX,newY));
			if (previousPoint==null || previousPoint.x!=p.x) {
				specialProcess.receiveUserInterfaceMessage(findDialogEntryX(),true,DialogEventType.NONE);
			};
			if (previousPoint==null || previousPoint.y!=p.y) {
				specialProcess.receiveUserInterfaceMessage(findDialogEntryY(),true,DialogEventType.NONE);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DialogEntry findDialogEntryX() {
		for (int i= 0; i < controlTable.length; i++) {
			DialogEntry entry= controlTable[i];
			if (entry.isSlotName) {
				if (entry.entryType==DialogEntryType.X) {
					return entry;
				}
			}
		};
		return null;
	}
	public DialogEntry findDialogEntryY() {
		for (int i= 0; i < controlTable.length; i++) {
			DialogEntry entry= controlTable[i];
			if (entry.isSlotName) {
				if (entry.entryType==DialogEntryType.Y) {
					return entry;
				}
			}
		};
		return null;
	}
	public DialogEntry findDialogEntryFontSize() {
		for (int i= 0; i < controlTable.length; i++) {
			DialogEntry entry= controlTable[i];
			if (entry.isSlotName) {
				if (entry.entryType==DialogEntryType.FONT_SIZE) {
					return entry;
				}
			}
		};
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Point safelyComputePosition(AtomicReference<ExtendedCoordinates> actualCoordinates) throws UseDefaultLocation {
		//
		Dimension initialSize= safelyGetSize();
		//
		int initialWidth= initialSize.width;
		int initialHeight= initialSize.height;
		//
		int x= 0;
		int y= 0;
		//
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		//
		Rectangle parentLayoutSize= safelyComputeParentLayoutSize();
		//
		ExtendedCoordinates actualPoint= actualCoordinates.get();
		x= DialogUtils.calculateRealCoordinate(actualPoint.x,parentLayoutSize.x,parentLayoutSize.width,gridX,initialWidth);
		y= DialogUtils.calculateRealCoordinate(actualPoint.y,parentLayoutSize.y,parentLayoutSize.height,gridY,initialHeight);
		//
		return new Point(x,y);
	}
}
