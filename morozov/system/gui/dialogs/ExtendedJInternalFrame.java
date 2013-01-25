// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.classes.*;
import morozov.system.gui.*;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicReference;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;

public class ExtendedJInternalFrame
		extends JInternalFrame
		implements DialogOperations, MouseListener {
	//
	protected AbstractDialog dialog;
	protected StaticContext staticContext;
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
	public Point computePosition(AtomicReference<ExtendedCoordinates> actualCoordinates) throws ExtendedCoordinate.UseDefaultLocation {
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
		Dimension parentLayoutSize= computeParentLayoutSize();
		//
		ExtendedCoordinates actualPoint= actualCoordinates.get();
		x= DialogUtils.calculateRealCoordinate(actualPoint.x,parentLayoutSize.width,gridX,initialWidth);
		y= DialogUtils.calculateRealCoordinate(actualPoint.y,parentLayoutSize.height,gridY,initialHeight);
		//
		return new Point(x,y);
	}
	public Dimension computeParentLayoutSize() {
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(dialog.staticContext);
		// Container parent= getParent();
		//
		// if (desktop != null) {
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
			return new Dimension(parentLayoutWidth,parentLayoutHeight);
		// } else {
		//	throw new CannotComputeDesktopSize();
		// }
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
		getParent().repaint();
	}
	public void doSuperLayout() {
		super.doLayout();
	}
	//
	public void safelyRestoreSize() {
		dialog.safelyRestoreSize();
	}
	public void doLayout() {
		// dialog.doLayout(true);
		// dialog.previousActualSize= new AtomicReference<Dimension>(new Dimension(10,10));
		dialog.doLayout();
		// dialog.doLayout();
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
					} else {
						setMaximum(true);
					}
				} catch (PropertyVetoException e) {
				}
			}
		}
	}
}
