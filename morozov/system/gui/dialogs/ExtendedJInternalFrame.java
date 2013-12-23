// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.classes.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Container;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;

public class ExtendedJInternalFrame
		extends JInternalFrame
		implements DialogOperations, MouseListener {
	//
	protected AbstractDialog dialog;
	protected StaticContext staticContext;
	protected AtomicBoolean insideDoLayout= new AtomicBoolean(false);
	//
	public ExtendedJInternalFrame(AbstractDialog d) {
		dialog= d;
		putClientProperty("JInternalFrame.frameType","optionDialog");
		addMouseListener(this);
	}
	//
	public void initiate(StaticContext context) {
		staticContext= context;
	}
	//
	public Point computePosition(AtomicReference<ExtendedCoordinates> actualCoordinates) throws UseDefaultLocation {
		//
		Dimension initialSize= getSize();
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
		Rectangle parentLayoutSize= computeParentLayoutSize();
		//
		ExtendedCoordinates actualPoint= actualCoordinates.get();
//System.out.printf("JInternalFrame::[0]actualPoint.x=%s,parentLayoutSize.x=%s,parentLayoutSize.width=%s,gridX=%s,initialWidth=%s;\n",actualPoint.x,parentLayoutSize.x,parentLayoutSize.width,gridX,initialWidth);
		x= DialogUtils.calculateRealCoordinate(actualPoint.x,parentLayoutSize.x,parentLayoutSize.width,gridX,initialWidth);
		y= DialogUtils.calculateRealCoordinate(actualPoint.y,parentLayoutSize.y,parentLayoutSize.height,gridY,initialHeight);
		//
		return new Point(x,y);
	}
	public Rectangle computeParentLayoutSize() {
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(dialog.staticContext);
		//
		Dimension parentDimension= new Dimension();
		desktop.getSize(parentDimension);
		//
		int parentWidth= parentDimension.width;
		int parentHeight= parentDimension.height;
		//
		Dimension minimumParentSize= desktop.getUI().getMinimumSize(this);
		//
		int parentLayoutWidth= parentWidth - minimumParentSize.width;
		int parentLayoutHeight= parentHeight - minimumParentSize.height;
		//
		return new Rectangle(0,0,parentLayoutWidth,parentLayoutHeight);
	}
	//
	public void addToDesktop(JDesktopPane desktop) {
		desktop.add(this,DesktopUtils.DIALOG_LAYER);
	}
	public void setLocationByPlatform(boolean locationByPlatform) {
	}
	//
	public Dimension getRealMinimumSize() {
		return getUI().getMinimumSize(this);
	}
	public Dimension getRealPreferredSize() {
		return getUI().getPreferredSize(this);
	}
	//
	public void repaintParent() {
		Container container= getParent();
		if (container != null) {
			container.repaint();
		}
	}
	public void doSuperLayout() {
		super.doLayout();
	}
	//
	public void safelyMaximize() {
		DesktopUtils.safelyMaximize(this);
	}
	public void safelyMinimize() {
		DesktopUtils.safelyMinimize(this);
	}
	public void safelyRestore() {
		DesktopUtils.safelyRestore(this);
	}
	public void safelyRestoreSize() {
		dialog.safelyRestoreSize();
	}
	public boolean safelyIsVisible() {
		return DesktopUtils.safelyIsVisible(this);
	}
	public boolean safelyIsHidden() {
		return DesktopUtils.safelyIsHidden(this);
	}
	public boolean safelyIsMaximized() {
		return DesktopUtils.safelyIsMaximized(this);
	}
	public boolean safelyIsMinimized() {
		return DesktopUtils.safelyIsMinimized(this);
	}
	public boolean safelyIsRestored() {
		return DesktopUtils.safelyIsRestored(this);
	}
	public void doLayout() {
		if (insideDoLayout.compareAndSet(false,true)) {
			try {
				// dialog.doLayout(true);
				// dialog.previousActualSize= new AtomicReference<Dimension>(new Dimension(10,10));
				dialog.doLayout();
				// dialog.doLayout();
			} finally {
				insideDoLayout.set(false);
			}
		}
	}
	//
	public void mouseClicked(MouseEvent event) {
	}
	public void mouseEntered(MouseEvent event) {
	}
	public void mouseExited(MouseEvent event) {
	}
	public void mousePressed(MouseEvent event) {
		mousePressedOrReleased(event);
	}
	public void mouseReleased(MouseEvent event) {
		// mousePressedOrReleased(event);
	}
	//
	private void mousePressedOrReleased(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
			if (event.getClickCount() > 1) {
				try {
					if (isMaximum()) {
						setMaximum(false);
						// safelyRestore();
					} else {
						setMaximum(true);
						// safelyMaximize();
					}
				} catch (PropertyVetoException e) {
				}
			}
		}
	}
}
