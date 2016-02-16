// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;
import morozov.system.gui.errors.*;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Component;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.awt.GraphicsConfiguration;
import java.awt.Graphics;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.JDesktopPane;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;

public class InnerPage implements ActionListener, MouseListener, MouseMotionListener, FocusListener {
	//
	protected ExtendedJInternalFrame internalFrame;
	//
	public CanvasSpace canvasSpace;
	//
	public AtomicReference<ExtendedSize> logicalWidth= new AtomicReference<ExtendedSize>(new ExtendedSize());
	public AtomicReference<ExtendedSize> logicalHeight= new AtomicReference<ExtendedSize>(new ExtendedSize());
	public AtomicReference<ExtendedCoordinate> logicalX= new AtomicReference<ExtendedCoordinate>(new ExtendedCoordinate());
	public AtomicReference<ExtendedCoordinate> logicalY= new AtomicReference<ExtendedCoordinate>(new ExtendedCoordinate());
	//
	protected AtomicReference<JMenuBar> currentMenuBar= new AtomicReference<JMenuBar>(null);
	//
	protected java.util.Timer scheduler;
	protected LocalInnerPageTask currentTask;
	//
	protected long repaintingDelay= 10; // ms
	// protected long repaintingDelay= 10000; // ms
	// protected long repaintingDelay= 1; // ms
	// protected long repaintingDelay= 100; // ms
	//
	public InnerPage(String title) {
		internalFrame= new ExtendedJInternalFrame(this,title,logicalX,logicalY,logicalWidth,logicalHeight);
		// safelySetDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		//// setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		// addPropertyChangeListener(this);
		scheduler= new java.util.Timer(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public JInternalFrame getInternalFrame() {
		return internalFrame;
	}
	public CanvasSpace getCanvasSpace() {
		return canvasSpace;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyAdd(final CanvasSpace space) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.add(space.getControl());
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.add(space.getControl());
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyAdd(final Component c, final int index) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.add(c,index);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.add(c,index);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelySetTitle(final String title) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.setTitle(title);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.setTitle(title);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelySetBackground(final Color color) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.setBackground(color);
			if (canvasSpace != null) {
				canvasSpace.quicklySetBackground(color);
			}
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.setBackground(color);
						if (canvasSpace != null) {
							canvasSpace.quicklySetBackground(color);
						}
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
/*
	public void safelySetDefaultCloseOperation(final int operation) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.setDefaultCloseOperation(operation);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.setDefaultCloseOperation(operation);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
*/
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyAddComponentListener(final ComponentListener l) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.addComponentListener(l);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.addComponentListener(l);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyGetComponentLocation(final Point location) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.getLocation(location);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.getLocation(location);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyGetComponentLocation(final Point location, final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.getLocation(location);
			internalFrame.getSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.getLocation(location);
						internalFrame.getSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyGetComponentSize(final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.getSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.getSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyGetSizeDifference(final Dimension sizeDifference) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyGetSizeDifference(sizeDifference);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyGetSizeDifference(sizeDifference);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklyGetSizeDifference(final Dimension sizeDifference) {
		internalFrame.getSize(sizeDifference);
		Dimension cd= internalFrame.getContentPane().getSize();
		sizeDifference.width= sizeDifference.width - cd.width;
		sizeDifference.height= sizeDifference.height - cd.height;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelySetVisible(final boolean mode) {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.setVisible(mode);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.setVisible(mode);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyMoveToFront() {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.moveToFront();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.moveToFront();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyRevalidate() {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.revalidate();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.revalidate();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyRepaint() {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.repaint();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.repaint();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyDispose() {
		if (SwingUtilities.isEventDispatchThread()) {
			internalFrame.dispose();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						internalFrame.dispose();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyMaximize() {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyMaximize();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyMaximize();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklyMaximize() {
		if (internalFrame.isIcon()) {
			try {
				internalFrame.setIcon(false);
			} catch (PropertyVetoException e) {
			}
		};
		try {
			internalFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
		}
	}
	public void safelyMinimize() {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyMinimize();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyMinimize();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklyMinimize() {
		if (internalFrame.isMaximum()) {
			try {
				internalFrame.setMaximum(false);
			} catch (PropertyVetoException e) {
			}
		};
		try {
			internalFrame.setIcon(true);
		} catch (PropertyVetoException e) {
		}
	}
	public void safelyRestore() {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyRestore();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyRestore();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklyRestore() {
		if (internalFrame.isMaximum()) {
			try {
				internalFrame.setMaximum(false);
			} catch (PropertyVetoException e) {
			}
		};
		if (internalFrame.isIcon()) {
			try {
				internalFrame.setIcon(false);
			} catch (PropertyVetoException e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean safelyIsMaximized() {
		if (SwingUtilities.isEventDispatchThread()) {
			return internalFrame.isMaximum();
		} else {
			try {
				final AtomicBoolean result= new AtomicBoolean(false);
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						result.set(internalFrame.isMaximum());
					}
				});
				return result.get();
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return false;
		}
	}
	public boolean safelyIsMinimized() {
		if (SwingUtilities.isEventDispatchThread()) {
			return internalFrame.isIcon();
		} else {
			try {
				final AtomicBoolean result= new AtomicBoolean(false);
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						result.set(internalFrame.isIcon());
					}
				});
				return result.get();
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return false;
		}
	}
	public boolean safelyIsRestored() {
		if (SwingUtilities.isEventDispatchThread()) {
			return quicklyIsRestored();
		} else {
			try {
				final AtomicBoolean result= new AtomicBoolean(false);
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						result.set(quicklyIsRestored());
					}
				});
				return result.get();
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return false;
		}
	}
	protected boolean quicklyIsRestored() {
		return (!internalFrame.isMaximum() && !internalFrame.isIcon());
	}
	public boolean safelyIsVisible() {
		if (SwingUtilities.isEventDispatchThread()) {
			return internalFrame.isVisible();
		} else {
			try {
				final AtomicBoolean result= new AtomicBoolean(false);
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						result.set(internalFrame.isVisible());
					}
				});
				return result.get();
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return false;
		}
	}
	public boolean safelyIsHidden() {
		return !safelyIsVisible();
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
			safelyGetSizeDifference(sizeDifference);
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
			safelyGetSizeDifference(sizeDifference);
			int step= StrictMath.max(sizeDifference.width,sizeDifference.height);
			int newPosition= StaticDesktopAttributes.increaseDefaultPosition(context,step,realWidth,desktopWidth);
			if (newPosition <= 0) {
				realX= 0;
			} else {
				realX= step * newPosition;
			}
		} else if (realYandHeight.coordinateIsToBeCalculatedAutomatically) {
			Dimension sizeDifference= new Dimension();
			safelyGetSizeDifference(sizeDifference);
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
	public void safelyRestoreSize(final int x, final int y, final int width, final int height) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyRestoreSize(x,y,width,height);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyRestoreSize(x,y,width,height);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void quicklyRestoreSize(int x, int y, int width, int height) {
		internalFrame.setLocation(x,y);
		internalFrame.setSize(width,height);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void repaintAfterDelay() {
		synchronized(this) {
			if (currentTask != null) {
				currentTask.cancel();
				scheduler.purge();
			};
			currentTask= new LocalInnerPageTask(this);
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
	// public void repaint() {
	//	super.repaint();
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public java.awt.image.BufferedImage safelyGetBufferedImage(final boolean selectRegion, final int integerX, final int integerY, final int integerWidth, final int integerHeight) {
		if (SwingUtilities.isEventDispatchThread()) {
			return quicklyGetBufferedImage(selectRegion,integerX,integerY,integerWidth,integerHeight);
		} else {
			try {
				final AtomicReference<java.awt.image.BufferedImage> result= new AtomicReference<>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						result.set(quicklyGetBufferedImage(selectRegion,integerX,integerY,integerWidth,integerHeight));
					}
				});
				return result.get();
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			throw new CannotGetBufferedImage();
		}
	}
	protected java.awt.image.BufferedImage quicklyGetBufferedImage(boolean selectRegion, int integerX, int integerY, int integerWidth, int integerHeight) {
		Rectangle bounds= internalFrame.getBounds(null);
		Insets insents= internalFrame.getInsets();
		Dimension size= new Dimension();
		internalFrame.getSize(size);
		if (!selectRegion) {
			integerX= 0;
			integerY= 0;
			integerWidth= size.width;
			integerHeight= size.height;
		};
		int x0= - (bounds.width - size.width - insents.right + integerX);
		int y0= - (bounds.height - size.height - insents.bottom + integerY);
		GraphicsConfiguration gc= internalFrame.getGraphicsConfiguration();
		if (gc != null) {
			java.awt.image.BufferedImage bufferedImage= gc.createCompatibleImage(integerWidth,integerHeight);
			Graphics g= bufferedImage.getGraphics();
			try {
				g.translate(x0,y0);
				internalFrame.print(g);
				return bufferedImage;
			} finally {
				g.dispose();
			}
		} else {
			throw new CannotGetGraphicsConfiguration();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	//public void safelySetMenu(final JMenuBar menuBar) {
	//	((JDesktopPane)internalFrame.getParent()).getRootPane().setJMenuBar(menuBar);
	//}
	public void safelySetMenu(final JMenuBar menuBar) {
		currentMenuBar.set(menuBar);
		if (SwingUtilities.isEventDispatchThread()) {
			quicklySetMenu(menuBar);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklySetMenu(menuBar);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklySetMenu(final JMenuBar menuBar) {
		// internalFrame.setJMenuBar(menuBar);
		JDesktopPane desktop= (JDesktopPane)internalFrame.getParent();
		desktop.getRootPane().setJMenuBar(menuBar);
		desktop.revalidate();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void enableMouseListener() {
		if (canvasSpace != null) {
			canvasSpace.safelyAddMouseListener(this);
		}
	}
	public void disableMouseListener() {
		if (canvasSpace != null) {
			canvasSpace.safelyRemoveMouseListener(this);
		}
	}
	public void enableMouseMotionListener() {
		if (canvasSpace != null) {
			canvasSpace.safelyAddMouseMotionListener(this);
		}
	}
	public void disableMouseMotionListener() {
		if (canvasSpace != null) {
			canvasSpace.safelyRemoveMouseMotionListener(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void actionPerformed(ActionEvent event) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void mouseClicked(MouseEvent ev) {
	}
	public void mouseEntered(MouseEvent ev) {
	}
	public void mouseExited(MouseEvent ev) {
	}
	public void mousePressed(MouseEvent ev) {
	}
	public void mouseReleased(MouseEvent ev) {
	}
	public void mouseDragged(MouseEvent ev) {
	}
	public void mouseMoved(MouseEvent ev) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void focusGained(FocusEvent e) {
		// Invoked when a component gains the keyboard focus.
		JMenuBar menuBar= currentMenuBar.get();
		quicklySetMenu(menuBar);
	}
	public void focusLost(FocusEvent e) {
		// Invoked when a component loses the keyboard focus.
	}
}
