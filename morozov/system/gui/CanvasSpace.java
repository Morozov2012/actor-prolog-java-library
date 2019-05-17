// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.built_in.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.system.gui.errors.*;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.GraphicsConfiguration;
import java.awt.Component;
import javax.swing.SwingUtilities;

import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public abstract class CanvasSpace {
	//
	protected CustomControlComponent customControlComponent;
	protected Component control;
	protected AtomicReference<Component> owner= new AtomicReference<Component>();
	protected AbstractDialog dialog;
	protected CustomControl targetWorld;
	//
	public CanvasSpace(CustomControlComponent customComponent) {
		customControlComponent= customComponent;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setJLabel(Component c) {
		owner.set(c);
	}
	public void setDialog(AbstractDialog d) {
		dialog= d;
	}
	public void setTargetWorld(CustomControl target) {
		targetWorld= target;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Component getControl() {
		return control;
	}
	public CustomControl getTargetWorld() {
		return targetWorld;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyRefineWidth(final double ratio) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyRefineWidth(ratio);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyRefineWidth(ratio);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklyRefineWidth(double ratio) {
		if (customControlComponent != null) {
			customControlComponent.refineWidth(ratio);
			dialog.safelyInvalidateAndRepaint();
		}
	}
	//
	public void safelyRefineHeight(final double ratio) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyRefineHeight(ratio);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyRefineHeight(ratio);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklyRefineHeight(double ratio) {
		if (customControlComponent != null) {
			customControlComponent.refineHeight(ratio);
			dialog.safelyInvalidateAndRepaint();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelySetBackground(final Color color) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklySetBackground(color);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklySetBackground(color);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void quicklySetBackground(Color color) {
		control.setBackground(color);
	}
	public void safelyGetComponentLocation(final Point location) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.getLocation(location);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.getLocation(location);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyGetComponentLocation(final Point location, final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.getLocation(location);
			control.getSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.getLocation(location);
						control.getSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyGetComponentSize(final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.getSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.getSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public Dimension safelyGetComponentMinimumSize() {
		if (SwingUtilities.isEventDispatchThread()) {
			return control.getMinimumSize();
		} else {
			try {
				final AtomicReference<Dimension> result= new AtomicReference<>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						result.set(control.getMinimumSize());
					}
				});
				return result.get();
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return new Dimension();
		}
	}
	public void safelySetComponentSize(final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.setSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.setSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyGetBounds(final Rectangle rectangle) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.getBounds(rectangle);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.getBounds(rectangle);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	// public void safelyGetInsets(final Insets insets) {
	//	if (SwingUtilities.isEventDispatchThread()) {
	//		control.getInsets(insets);
	//	} else {
	//		try {
	//			SwingUtilities.invokeAndWait(new Runnable() {
	//				public void run() {
	//					control.getInsets(insets);
	//				}
	//			});
	//		} catch (InterruptedException e) {
	//		} catch (InvocationTargetException e) {
	//		}
	//	}
	// }
	public GraphicsConfiguration safelyGetGraphicsConfiguration() {
		if (SwingUtilities.isEventDispatchThread()) {
			return control.getGraphicsConfiguration();
		} else {
			try {
				final AtomicReference<GraphicsConfiguration> result= new AtomicReference<>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						result.set(control.getGraphicsConfiguration());
					}
				});
				return result.get();
			} catch (InterruptedException e) {
				throw new CannotGetGraphicsConfiguration();
			} catch (InvocationTargetException e) {
				throw new CannotGetGraphicsConfiguration();
			}
		}
	}
	public void safelyRevalidate() {
		if (SwingUtilities.isEventDispatchThread()) {
			control.revalidate();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.revalidate();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyRepaint() {
		if (SwingUtilities.isEventDispatchThread()) {
			control.repaint();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.repaint();
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
	public void repaintAfterDelay() {
		dialog.repaintAfterDelay();
	}
	public void skipDelayedRepainting() {
		dialog.skipDelayedRepainting();
	}
	// public void suspendRedrawing() {
	// }
	// public void releaseRedrawing() {
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	// public void safelyPaintComponent(final Graphics g) {
	//	if (SwingUtilities.isEventDispatchThread()) {
	//		control.paintComponent(g);
	//	} else {
	//		try {
	//			SwingUtilities.invokeAndWait(new Runnable() {
	//				public void run() {
	//					control.paintComponent(g);
	//				}
	//			});
	//		} catch (InterruptedException e) {
	//		} catch (InvocationTargetException e) {
	//		}
	//	}
	// }
	public void safelyPrint(final Graphics g) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.print(g);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.print(g);
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
	public void safelyAddMouseListener(final MouseListener l) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.addMouseListener(l);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.addMouseListener(l);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyRemoveMouseListener(final MouseListener l) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.removeMouseListener(l);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.removeMouseListener(l);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyAddMouseMotionListener(final MouseMotionListener l) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.addMouseMotionListener(l);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.addMouseMotionListener(l);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyRemoveMouseMotionListener(final MouseMotionListener l) {
		if (SwingUtilities.isEventDispatchThread()) {
			control.removeMouseMotionListener(l);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						control.removeMouseMotionListener(l);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
}
