// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.*;
import morozov.terms.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class InternalFrame2D
		extends InnerPage
		implements ActionListener, MouseListener, MouseMotionListener {
	protected Canvas2D targetWorld;
	protected AtomicReference<Canvas2DScalingFactor> scalingFactor;
	//
	public InternalFrame2D(Canvas2D target, String title, StaticContext context, List<Java2DCommand> commandList, AtomicReference<Canvas2DScalingFactor> scaling) {
		super(title);
		targetWorld= target;
		scalingFactor= scaling;
		ExtendedSpace2D extendedSpace2D= new ExtendedSpace2D(targetWorld,commandList,scalingFactor,target.sceneAntialiasingIsEnabled);
		canvasSpace= extendedSpace2D;
		safelyAdd(extendedSpace2D);
	}
	//
	public DrawingMode getDrawingMode() {
		if (canvasSpace != null) {
			return ((ExtendedSpace2D)canvasSpace).getDrawingMode();
		} else {
			Dimension size= new Dimension();
			Canvas2DScalingFactor currentScalingFactor= Canvas2DScalingFactor.INDEPENDENT;
			return new DrawingMode(size,currentScalingFactor);
		}
	}
	//
	public void safelySetBackground(Color c) {
		if (canvasSpace != null) {
			canvasSpace.safelySetBackground(c);
		}
	}
	//
	public void safelyRepaint() {
		if (canvasSpace != null) {
			canvasSpace.safelyRepaint();
		}
	}
	//
	public void actionPerformed(ActionEvent event) {
		long domainSignature= targetWorld.entry_s_Action_1_i();
		Term predicateArgument= new PrologString(event.getActionCommand());
		Term[] arguments= new Term[]{predicateArgument};
		AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
		targetWorld.transmitAsyncCall(call,null);
	}
	//
	public void mouseClicked(MouseEvent ev) {
		if (targetWorld.mouseClickedEventIsEnabled.get()) {
			long domainSignature= targetWorld.entry_s_MouseClicked_1_i();
			sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_CLICKED,true);
		}
	}
	public void mouseEntered(MouseEvent ev) {
		if (targetWorld.mouseEnteredEventIsEnabled.get()) {
			long domainSignature= targetWorld.entry_s_MouseEntered_1_i();
			sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_ENTERED,true);
		}
	}
	public void mouseExited(MouseEvent ev) {
		if (targetWorld.mouseExitedEventIsEnabled.get()) {
			long domainSignature= targetWorld.entry_s_MouseExited_1_i();
			sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_EXITED,true);
		}
	}
	public void mousePressed(MouseEvent ev) {
		if (targetWorld.mousePressedEventIsEnabled.get()) {
			long domainSignature= targetWorld.entry_s_MousePressed_1_i();
			sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_PRESSED,true);
		}
	}
	public void mouseReleased(MouseEvent ev) {
		if (targetWorld.mouseReleasedEventIsEnabled.get()) {
			long domainSignature= targetWorld.entry_s_MouseReleased_1_i();
			sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_RELEASED,true);
		}
	}
	public void mouseDragged(MouseEvent ev) {
		if (targetWorld.mouseDraggedEventIsEnabled.get()) {
			long domainSignature= targetWorld.entry_s_MouseDragged_1_i();
			sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_DRAGGED,false);
		}
	}
	public void mouseMoved(MouseEvent ev) {
		if (targetWorld.mouseMovedEventIsEnabled.get()) {
			long domainSignature= targetWorld.entry_s_MouseMoved_1_i();
			sendMouseEvent(ev,domainSignature,SymbolCodes.symbolCode_E_MOUSE_MOVED,false);
		}
	}
	protected void sendMouseEvent(MouseEvent ev, long domainSignature, long mouseTypeCode, boolean useBuffer) {
		if (canvasSpace != null) {
			Dimension size= new Dimension();
			canvasSpace.getControl().getSize(size);
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
}
