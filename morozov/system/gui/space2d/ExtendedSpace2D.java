// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.system.gui.errors.*;
import morozov.terms.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.GraphicsConfiguration;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.InvocationTargetException;

public class ExtendedSpace2D
		extends CanvasSpace
		implements ActionListener, MouseListener, MouseMotionListener {
	//
	protected List<Java2DCommand> currentCommands= null;
	protected AtomicReference<Java2DCommand[]> oldCommands= new AtomicReference<>(null);
	protected AtomicReference<Canvas2DScalingFactor> scalingFactor;
	protected AtomicBoolean sceneAntialiasingIsEnabled;
	protected AtomicBoolean redrawingIsSuspended= new AtomicBoolean(false);
	//
	///////////////////////////////////////////////////////////////
	//
	public ExtendedSpace2D(CustomControlComponent customControl, boolean keepProportions) {
		super(customControl);
		Canvas2DScalingFactor scaling;
		scaling= Canvas2DScalingFactor.INDEPENDENT;
		scalingFactor= new AtomicReference<>(scaling);
		sceneAntialiasingIsEnabled= new AtomicBoolean(false);
		control= new ExtendedJPanel(owner,currentCommands,oldCommands,scalingFactor,sceneAntialiasingIsEnabled,redrawingIsSuspended);
	}
	public ExtendedSpace2D(CustomControlComponent customControl, Canvas2D target, List<Java2DCommand> commandList, AtomicReference<Canvas2DScalingFactor> scaling, AtomicBoolean enableAntialiasing) {
		super(customControl);
		targetWorld= target;
		currentCommands= commandList;
		scalingFactor= scaling;
		sceneAntialiasingIsEnabled= enableAntialiasing;
		control= new ExtendedJPanel(owner,currentCommands,oldCommands,scalingFactor,sceneAntialiasingIsEnabled,redrawingIsSuspended);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setScalingFactor(AtomicReference<Canvas2DScalingFactor> factor) {
		scalingFactor= factor;
		((ExtendedJPanel)control).setScalingFactor(factor);
	}
	public void setEnableAntialiasing(AtomicBoolean enableAntialiasing) {
		sceneAntialiasingIsEnabled= enableAntialiasing;
		((ExtendedJPanel)control).setEnableAntialiasing(enableAntialiasing);
	}
	public void setCommands(List<Java2DCommand> commandList) {
		currentCommands= commandList;
		((ExtendedJPanel)control).setCommands(commandList);
	}
	public DrawingMode getDrawingMode() {
		Dimension size= new Dimension();
		safelyGetComponentSize(size);
		Canvas2DScalingFactor currentScalingFactor= scalingFactor.get();
		return new DrawingMode(size,currentScalingFactor);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void enableMouseListeners() {
		enableMouseListener();
	}
	protected void disableMouseListeners() {
		disableMouseListener();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void enableMouseListener() {
		final Component o= owner.get();
		if (o != null) {
			if (SwingUtilities.isEventDispatchThread()) {
				o.addMouseListener(this);
			} else {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							o.addMouseListener(ExtendedSpace2D.this);
						}
					});
				} catch (InterruptedException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
	}
	public void disableMouseListener() {
		final Component o= owner.get();
		if (o != null) {
			if (SwingUtilities.isEventDispatchThread()) {
				o.removeMouseListener(this);
			} else {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							o.removeMouseListener(ExtendedSpace2D.this);
						}
					});
				} catch (InterruptedException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
	}
	public void enableMouseMotionListener() {
		final Component o= owner.get();
		if (o != null) {
			if (SwingUtilities.isEventDispatchThread()) {
				o.addMouseMotionListener(this);
			} else {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							o.addMouseMotionListener(ExtendedSpace2D.this);
						}
					});
				} catch (InterruptedException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
	}
	public void disableMouseMotionListener() {
		final Component o= owner.get();
		if (o != null) {
			if (SwingUtilities.isEventDispatchThread()) {
				o.removeMouseMotionListener(this);
			} else {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							o.removeMouseMotionListener(ExtendedSpace2D.this);
						}
					});
				} catch (InterruptedException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public GraphicsConfiguration getGraphicsConfiguration() {
		Component o= owner.get();
		if (o != null) {
			return o.getGraphicsConfiguration();
		} else {
			return null;
		}
	}
	//
	public void suspendRedrawing() {
		synchronized (currentCommands) {
			if (redrawingIsSuspended.get()) {
				return;
			} else {
				redrawingIsSuspended.set(true);
				oldCommands.set(currentCommands.toArray(new Java2DCommand[currentCommands.size()]));
			}
		}
	}
	public void releaseRedrawing() {
		synchronized (currentCommands) {
			if (!redrawingIsSuspended.get()) {
				return;
			} else {
				oldCommands.set(null);
				redrawingIsSuspended.set(false);
			}
		}
	}
	//
	public void quicklySetSize(Dimension size) {
		control.setSize(size);
	}
	public void quicklyPaintComponent(Graphics g) {
		((ExtendedJPanel)control).paintComponent(g);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void actionPerformed(ActionEvent event) {
		if (targetWorld != null) {
			long domainSignature= ((Canvas2D)targetWorld).entry_s_Action_1_i();
			Term predicateArgument= new PrologString(event.getActionCommand());
			Term[] arguments= new Term[]{predicateArgument};
			AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
			targetWorld.transmitAsyncCall(call,null);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void mouseClicked(MouseEvent ev) {
		if (targetWorld != null) {
			if (((Canvas2D)targetWorld).mouseClickedEventIsEnabled.get()) {
				long domainSignature= ((Canvas2D)targetWorld).entry_s_MouseClicked_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_CLICKED,true);
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent ev) {
		if (targetWorld != null) {
			if (((Canvas2D)targetWorld).mouseEnteredEventIsEnabled.get()) {
				long domainSignature= ((Canvas2D)targetWorld).entry_s_MouseEntered_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_ENTERED,true);
			}
		}
	}
	@Override
	public void mouseExited(MouseEvent ev) {
		if (targetWorld != null) {
			if (((Canvas2D)targetWorld).mouseExitedEventIsEnabled.get()) {
				long domainSignature= ((Canvas2D)targetWorld).entry_s_MouseExited_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_EXITED,true);
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent ev) {
		if (targetWorld != null) {
			if (((Canvas2D)targetWorld).mousePressedEventIsEnabled.get()) {
				long domainSignature= ((Canvas2D)targetWorld).entry_s_MousePressed_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_PRESSED,true);
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent ev) {
		if (targetWorld != null) {
			if (((Canvas2D)targetWorld).mouseReleasedEventIsEnabled.get()) {
				long domainSignature= ((Canvas2D)targetWorld).entry_s_MouseReleased_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_RELEASED,true);
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent ev) {
		if (targetWorld != null) {
			if (((Canvas2D)targetWorld).mouseDraggedEventIsEnabled.get()) {
				long domainSignature= ((Canvas2D)targetWorld).entry_s_MouseDragged_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_DRAGGED,false);
			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent ev) {
		if (targetWorld != null) {
			if (((Canvas2D)targetWorld).mouseMovedEventIsEnabled.get()) {
				long domainSignature= ((Canvas2D)targetWorld).entry_s_MouseMoved_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_MOVED,false);
			}
		}
	}
	protected void sendMouseEvent(MouseEvent ev, long domainSignature, long mouseTypeCode, boolean useBuffer) {
		if (targetWorld != null) {
			Dimension size= new Dimension();
			safelyGetComponentSize(size);
			int width= size.width;
			int height= size.height;
			Canvas2DScalingFactor factor= scalingFactor.get();
			int coefficient= factor.computeScalingCoefficient(width,height);
			Term predicateArgument= MenuUtils.mouseEvent2Term(ev,width,height,coefficient,mouseTypeCode);
			Term[] arguments= new Term[]{predicateArgument};
			AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,useBuffer,arguments,true);
			targetWorld.transmitAsyncCall(call,null);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void repaintAfterDelay() {
		if (dialog != null) {
			dialog.repaintAfterDelay();
		}
	}
	@Override
	public void skipDelayedRepainting() {
		if (dialog != null) {
			dialog.skipDelayedRepainting();
		}
	}
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
					@Override
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
		JPanel panel= (JPanel)control;
		Rectangle bounds= control.getBounds(null);
		Insets insents= panel.getInsets();
		Dimension size= new Dimension();
		control.getSize(size);
		if (!selectRegion) {
			integerX= 0;
			integerY= 0;
			integerWidth= size.width;
			integerHeight= size.height;
		};
		int x0= - (bounds.width - size.width - insents.right + integerX);
		int y0= - (bounds.height - size.height - insents.bottom + integerY);
		Component o= owner.get();
		GraphicsConfiguration gc= o.getGraphicsConfiguration();
		if (gc != null) {
			java.awt.image.BufferedImage bufferedImage= gc.createCompatibleImage(integerWidth,integerHeight);
			Graphics g= DesktopUtils.safelyGetGraphics2D(bufferedImage);
			try {
				g.translate(x0,y0);
				control.print(g);
				return bufferedImage;
			} finally {
				g.dispose();
			}
		} else {
			throw new CannotGetGraphicsConfiguration();
		}
	}
}
