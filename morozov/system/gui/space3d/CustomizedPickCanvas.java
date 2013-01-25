// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.built_in.*;
import morozov.classes.*;
import morozov.terms.*;

import javax.swing.SwingUtilities;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;
import java.awt.HeadlessException;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

public class CustomizedPickCanvas
		extends PickCanvas
		implements MouseListener, MouseMotionListener {
	//
	protected boolean handleMouseClicked= false;
	protected boolean handleMouseEntered= false;
	protected boolean handleMouseExited= false;
	protected boolean handleMousePressed= false;
	protected boolean handleMouseReleased= false;
	protected boolean handleMouseDragged= false;
	protected boolean handleMouseMoved= false;
	protected boolean isPassive= false;
	protected long period= 150; // Actor Prolog default value
	//
	protected Canvas3D targetWorld;
	protected javax.media.j3d.Canvas3D canvas3D;
	protected java.util.Timer scheduler;
	protected TimerTask currentTask;
	//
	protected HashSet<NodeLabel> currentlyIndicatedLabels= new HashSet<NodeLabel>();
	//
	// protected Appearance lit= new Appearance();
	//
	public CustomizedPickCanvas(BranchGroup b, Canvas3D world, javax.media.j3d.Canvas3D c) {
		super(c,b);
		targetWorld= world;
		canvas3D= c;
		// lit.setMaterial(new Material());
		refineMouseEventListenerList();
		refineMouseMotionEventListenerList();
	}
	//
	public void setHandleMouseClicked(boolean flag) {
		handleMouseClicked= flag;
		refineMouseEventListenerList();
	}
	public void setHandleMouseEntered(boolean flag) {
		handleMouseEntered= flag;
		refineMouseEventListenerList();
	}
	public void setHandleMouseExited(boolean flag) {
		handleMouseExited= flag;
		refineMouseEventListenerList();
	}
	public void setHandleMousePressed(boolean flag) {
		handleMousePressed= flag;
		refineMouseEventListenerList();
	}
	public void setHandleMouseReleased(boolean flag) {
		handleMouseReleased= flag;
		refineMouseEventListenerList();
	}
	public void setHandleMouseDragged(boolean flag) {
		handleMouseDragged= flag;
		refineMouseMotionEventListenerList();
	}
	public void setHandleMouseMoved(boolean flag) {
		handleMouseMoved= flag;
		refineMouseMotionEventListenerList();
	}
	public void setIsPassive(boolean flag) {
		isPassive= flag;
	}
	public void setPeriod(long value) {
		period= value;
	}
	//
	protected void refineMouseEventListenerList() {
		// MouseListener[] list1= canvas3D.getMouseListeners();
		// for (int n= 0; n < list1.length; n++) {
		//	System.out.printf("%s) list: %s\n",n,list1[n]);
		// };
		MouseListener[] list= canvas3D.getMouseListeners();
		if (	handleMouseClicked ||
			handleMouseEntered ||
			handleMouseExited ||
			handleMousePressed ||
			handleMouseReleased ) {
			for (int n= 0; n < list.length; n++) {
				if (list[n].equals(this)) {
					return;
				}
			};
			canvas3D.addMouseListener(this);
		} else {
			canvas3D.removeMouseListener(this);
		}
	}
	protected void refineMouseMotionEventListenerList() {
		MouseMotionListener[] list= canvas3D.getMouseMotionListeners();
		if (	handleMouseDragged ||
			handleMouseMoved ) {
			for (int n= 0; n < list.length; n++) {
				if (list[n].equals(this)) {
					return;
				}
			};
			canvas3D.addMouseMotionListener(this);
		} else {
			canvas3D.removeMouseMotionListener(this);
		}
	}
	//
	public void mouseClicked(MouseEvent mouseEvent) {
		// Invoked when the mouse button has been clicked (pressed and released) on a component.
		if (handleMouseClicked) {
			try {
				Term predicateArgument= retrieveNodeLabelList(mouseEvent);
				long domainSignature= targetWorld.entry_s_MouseClicked_1_i();
				sendMessage(domainSignature,predicateArgument);
			} catch (NoObjectSelected e) {
			}
		}
	}
	public void mouseEntered(MouseEvent mouseEvent) {
		// System.out.printf("CustomizedBehavior::mouseEntered(%s)\n",mouseEvent);
		// Invoked when the mouse enters a component.
		activateTimer();
		if (handleMouseEntered) {
			long domainSignature= targetWorld.entry_s_MouseEntered_1_i();
			sendMessage(domainSignature,new PrologEmptyList());
		}
	}
	public void mouseExited(MouseEvent mouseEvent) {
		// System.out.printf("CustomizedBehavior::mouseExited(%s)\n",mouseEvent);
		// Invoked when the mouse exits a component.
		suspendTimer();
		if (handleMouseExited) {
			long domainSignature= targetWorld.entry_s_MouseExited_1_i();
			sendMessage(domainSignature,new PrologEmptyList());
		}
	}
	public void mousePressed(MouseEvent mouseEvent) {
		// Invoked when a mouse button has been pressed on a component.
		if (handleMousePressed) {
			try {
				Term predicateArgument= retrieveNodeLabelList(mouseEvent);
				long domainSignature= targetWorld.entry_s_MousePressed_1_i();
				sendMessage(domainSignature,predicateArgument);
			} catch (NoObjectSelected e) {
			}
		}
	}
	public void mouseReleased(MouseEvent mouseEvent) {
		// Invoked when a mouse button has been released on a component.
		if (handleMousePressed) {
			try {
				Term predicateArgument= retrieveNodeLabelList(mouseEvent);
				long domainSignature= targetWorld.entry_s_MouseReleased_1_i();
				sendMessage(domainSignature,predicateArgument);
			} catch (NoObjectSelected e) {
			}
		}
	}
	//
	public void mouseDragged(MouseEvent mouseEvent) {
		// Invoked when a mouse button is pressed on a component and then dragged.
		if (handleMouseDragged) {
			try {
				Term predicateArgument= retrieveNodeLabelList(mouseEvent);
				long domainSignature= targetWorld.entry_s_MouseDragged_1_i();
				sendMessage(domainSignature,predicateArgument);
			} catch (NoObjectSelected e) {
			}
		}
	}
	public void mouseMoved(MouseEvent mouseEvent) {
		// Invoked when the mouse cursor has been moved onto a component but no buttons have been pushed.
		if (handleMouseMoved) {
			try {
				Term predicateArgument= retrieveNodeLabelList(mouseEvent);
				long domainSignature= targetWorld.entry_s_MouseMoved_1_i();
				sendMessage(domainSignature,predicateArgument);
			} catch (NoObjectSelected e) {
			}
		}
	}
	//
	public void mouseWatch() {
		try {
			PointerInfo info= MouseInfo.getPointerInfo();
			if (info != null) {
				Point location= info.getLocation();
				// System.out.printf("Watch: %s\n",location);
				SwingUtilities.convertPointFromScreen(location,canvas3D);
				// System.out.printf("Watch: [converted] %s\n",location);
				setShapeLocation(location.x,location.y);
				NodeLabel[] labels= retrieveNodeLabels();
				updateNodeLabels(labels);
			}
		} catch (HeadlessException e) {
		}
	}
	protected void updateNodeLabels(NodeLabel[] labels) {
		int numberOfLabels= labels.length;
		NodeLabel[] newLabels= new NodeLabel[numberOfLabels];
		ArrayList<NodeLabel> removedLabels= new ArrayList<NodeLabel>();
		int numberOfNewLabels= 0;
		int numberOfRemovedLabels= 0;
		Iterator<NodeLabel> iterator= currentlyIndicatedLabels.iterator();
		while (iterator.hasNext()) {
			NodeLabel currentNodeLabel= iterator.next();
			boolean removeLabel= true;
			for (int n= 0; n < labels.length; n++) {
				if (labels[n].equals(currentNodeLabel)) {
					removeLabel= false;
					break;
				}
			};
			if (removeLabel) {
				removedLabels.add(currentNodeLabel);
				numberOfRemovedLabels= numberOfRemovedLabels + 1;
				iterator.remove();
			}
		};
		for (int n= 0; n < labels.length; n++) {
			NodeLabel currentNodeLabel= labels[n];
			if (!currentlyIndicatedLabels.contains(currentNodeLabel)) {
				newLabels[numberOfNewLabels]= currentNodeLabel;
				numberOfNewLabels= numberOfNewLabels + 1;
				currentlyIndicatedLabels.add(currentNodeLabel);
			}
		};
		// System.out.printf("numberOfRemovedLabels: %s\n",numberOfRemovedLabels);
		// System.out.printf("numberOfNewLabels: %s\n",numberOfNewLabels);
		if (numberOfRemovedLabels > 0 && handleMouseExited) {
			long domainSignature= targetWorld.entry_s_MouseExited_1_i();
			Term predicateArgument= nodeLabelsToTerm(removedLabels);
			sendMessage(domainSignature,predicateArgument);
		};
		if (numberOfNewLabels > 0 && handleMouseEntered) {
			newLabels= Arrays.copyOf(newLabels,numberOfNewLabels);
			long domainSignature= targetWorld.entry_s_MouseEntered_1_i();
			Term predicateArgument= nodeLabelsToTerm(newLabels);
			sendMessage(domainSignature,predicateArgument);
		}
	}
	//
	protected Term retrieveNodeLabelList(MouseEvent mouseEvent) throws NoObjectSelected {
		// System.out.printf("mouseEvent.getScreen X,Y: %s\n",mouseEvent.getLocationOnScreen());
		setShapeLocation(mouseEvent);
		return retrieveNodeLabelList();
	}
	protected Term retrieveNodeLabelList(Point location) throws NoObjectSelected {
		setShapeLocation(location.x,location.y);
		return retrieveNodeLabelList();
	}
	protected Term retrieveNodeLabelList() throws NoObjectSelected {
		NodeLabel[] labels= retrieveNodeLabels();
		if (labels.length==0) {
			throw new NoObjectSelected();
		};
		return nodeLabelsToTerm(labels);
	}
	protected NodeLabel[] retrieveNodeLabels() {
		PickResult[] results= pickAllSorted();
		if (results != null) {
			NodeLabel[] labels= new NodeLabel[results.length];
			int numberOfLabels= 0;
			for (int n= 0; n < results.length; n++) {
				Node node= results[n].getObject();
				NodeLabel label= targetWorld.retrieveNodeLabel(node);
				if (label != null) {
					boolean acceptLabel= true;
					for (int k= 0; k < numberOfLabels; k++) {
						if (label.equals(labels[k])) {
							acceptLabel= false;
							break;
						}
					};
					if (acceptLabel) {
						labels[numberOfLabels]= label;
						numberOfLabels= numberOfLabels + 1;
					}
				}
			};
			return Arrays.copyOf(labels,numberOfLabels);
		} else {
			return new NodeLabel[0];
		}
	}
	protected Term nodeLabelsToTerm(NodeLabel[] labels) {
		Term result= new PrologEmptyList();
		for (int n= labels.length-1; n >= 0; n--) {
			NodeLabel label= labels[n];
			result= new PrologList(label.toTerm(),result);
		};
		return result;
	}
	protected Term nodeLabelsToTerm(ArrayList<NodeLabel> labels) {
		Term result= new PrologEmptyList();
		for (int n= labels.size()-1; n >= 0; n--) {
			NodeLabel label= labels.get(n);
			result= new PrologList(label.toTerm(),result);
		};
		return result;
	}
	protected void sendMessage(long domainSignature, Term predicateArgument) {
		Term[] arguments= new Term[]{predicateArgument};
		AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
		targetWorld.receiveAsyncCall(call);
	}
	//
	public void activateTimer() {
		if (!isPassive) {
			if (handleMouseEntered || handleMouseExited) {
				if (currentTask==null) {
					if (scheduler==null) {
						scheduler= new java.util.Timer(true);
					};
					currentTask= new LocalTask(this);
					long correctedPeriod= period;
					if (correctedPeriod <= 1) {
						correctedPeriod= 1;
					};
					scheduler.scheduleAtFixedRate(currentTask,correctedPeriod,correctedPeriod);
				}
			}
		}
	}
	public void suspendTimer() {
		if (!isPassive) {
			if (scheduler != null) {
				if (currentTask != null) {
					currentTask.cancel();
					currentTask= null;
				}
			}
		}
	}
}

class LocalTask extends TimerTask {
	//
	protected CustomizedPickCanvas pickCanvas;
	//
	public LocalTask(CustomizedPickCanvas canvas) {
		pickCanvas= canvas;
	}
	//
	public void run() {
		pickCanvas.mouseWatch();
	}
}
