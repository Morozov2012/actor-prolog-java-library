// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.built_in.*;
import morozov.classes.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.terms.*;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.GraphicsConfiguration;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ExtendedSpace2D
		extends JPanel
		implements ActionListener, MouseListener, MouseMotionListener {
	protected Canvas2D targetWorld= null;
	protected Component owner;
	protected AbstractDialog dialog;
	// protected String identifier;
	// protected StaticContext staticContext;
	protected List<Java2DCommand> commands= null;
	protected AtomicReference<Canvas2DScalingFactor> scalingFactor;
	// protected boolean enableSceneAntialiasing= false;
	protected AtomicBoolean sceneAntialiasingIsEnabled;
	//
	public ExtendedSpace2D(boolean keepProportions) {
		Canvas2DScalingFactor scaling;
		if (keepProportions) {
			scaling= Canvas2DScalingFactor.MIN;
		} else {
			scaling= Canvas2DScalingFactor.INDEPENDENT;
		};
		scalingFactor= new AtomicReference<Canvas2DScalingFactor>(scaling);
		sceneAntialiasingIsEnabled= new AtomicBoolean(false);
	}
	public ExtendedSpace2D(Canvas2D target, List<Java2DCommand> commandList, AtomicReference<Canvas2DScalingFactor> scaling, AtomicBoolean enableAntialiasing) {
		targetWorld= target;
		commands= commandList;
		scalingFactor= scaling;
		sceneAntialiasingIsEnabled= enableAntialiasing;
	}
	//
	public void setTargetWorld(Canvas2D target) {
		targetWorld= target;
	}
	public void setJLabel(Component c) {
		owner= c;
	}
	public void setDialog(AbstractDialog d) {
		dialog= d;
	}
	public void setEnableAntialiasing(AtomicBoolean enableAntialiasing) {
		sceneAntialiasingIsEnabled= enableAntialiasing;
	}
	public void setCommands(List<Java2DCommand> commandList) {
		commands= commandList;
	}
	//
	public void enableMouseListener() {
		if (owner != null) {
			owner.addMouseListener(this);
		}
	}
	public void disableMouseListener() {
		if (owner != null) {
 			owner.removeMouseListener(this);
		}
	}
	public void enableMouseMotionListener() {
		if (owner != null) {
			owner.addMouseMotionListener(this);
		}
	}
	public void disableMouseMotionListener() {
		if (owner != null) {
			owner.removeMouseMotionListener(this);
		}
	}
	//
	public GraphicsConfiguration getGraphicsConfiguration() {
		if (owner != null) {
			return owner.getGraphicsConfiguration();
		} else {
			return null;
		}
	}
	//
	public void paintComponent(Graphics g) {
		if (commands != null) {
			Graphics2D g2= (Graphics2D)g;
			boolean enableSceneAntialiasing= sceneAntialiasingIsEnabled.get();
			if (enableSceneAntialiasing) {
				RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHints(rh);
			};
			Dimension size= getSize();
			Canvas2DScalingFactor currentScalingFactor= scalingFactor.get();
			synchronized (commands) {
				super.paintComponent(g);
				DrawingMode drawingMode= new DrawingMode(size,currentScalingFactor);
				ListIterator<Java2DCommand> iterator= commands.listIterator();
				while (iterator.hasNext()) {
					Java2DCommand command= iterator.next();
					command.execute(g2,drawingMode);
				}
			}
		}
	}
	//
	public void actionPerformed(ActionEvent event) {
		if (targetWorld != null) {
			long domainSignature= targetWorld.entry_s_Action_1_i();
			Term predicateArgument= new PrologString(event.getActionCommand());
			Term[] arguments= new Term[]{predicateArgument};
			AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
			targetWorld.receiveAsyncCall(call);
		}
	}
	//
	public void mouseClicked(MouseEvent ev) {
		if (targetWorld != null) {
			if (targetWorld.mouseClickedEventIsEnabled.get()) {
				long domainSignature= targetWorld.entry_s_MouseClicked_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_CLICKED,true);
			}
		}
	}
	public void mouseEntered(MouseEvent ev) {
		if (targetWorld != null) {
			if (targetWorld.mouseEnteredEventIsEnabled.get()) {
				long domainSignature= targetWorld.entry_s_MouseEntered_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_ENTERED,true);
			}
		}
	}
	public void mouseExited(MouseEvent ev) {
		if (targetWorld != null) {
			if (targetWorld.mouseExitedEventIsEnabled.get()) {
				long domainSignature= targetWorld.entry_s_MouseExited_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_EXITED,true);
			}
		}
	}
	public void mousePressed(MouseEvent ev) {
		if (targetWorld != null) {
			if (targetWorld.mousePressedEventIsEnabled.get()) {
				long domainSignature= targetWorld.entry_s_MousePressed_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_PRESSED,true);
			}
		}
	}
	public void mouseReleased(MouseEvent ev) {
		if (targetWorld != null) {
			if (targetWorld.mouseReleasedEventIsEnabled.get()) {
				long domainSignature= targetWorld.entry_s_MouseReleased_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_RELEASED,true);
			}
		}
	}
	public void mouseDragged(MouseEvent ev) {
		if (targetWorld != null) {
			if (targetWorld.mouseDraggedEventIsEnabled.get()) {
				long domainSignature= targetWorld.entry_s_MouseDragged_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_DRAGGED,false);
			}
		}
	}
	public void mouseMoved(MouseEvent ev) {
		if (targetWorld != null) {
			if (targetWorld.mouseMovedEventIsEnabled.get()) {
				long domainSignature= targetWorld.entry_s_MouseMoved_1_i();
				sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_MOVED,false);
			}
		}
	}
	protected void sendMouseEvent(MouseEvent ev, long domainSignature, long mouseTypeCode, boolean useBuffer) {
		if (targetWorld != null) {
			Dimension size= new Dimension();
			DesktopUtils.safelyGetSize(this,size);
			int width= size.width;
			int height= size.height;
			Canvas2DScalingFactor factor= scalingFactor.get();
			int coefficient= factor.computeScalingCoefficient(width,height);
			Term predicateArgument= MenuUtils.mouseEvent2Term(ev,width,height,coefficient,mouseTypeCode);
			Term[] arguments= new Term[]{predicateArgument};
			AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,useBuffer,arguments,true);
			targetWorld.receiveAsyncCall(call);
		}
	}
	//
	public void repaintAfterDelay() {
		if (dialog != null) {
			dialog.repaintAfterDelay();
		}
	}
	public void skipDelayedRepainting() {
		if (dialog != null) {
			dialog.skipDelayedRepainting();
		}
	}
}
