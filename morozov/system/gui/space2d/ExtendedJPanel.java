// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Component;
import javax.swing.JPanel;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ExtendedJPanel extends JPanel {
	//
	protected AtomicReference<Component> owner;
	protected List<Java2DCommand> currentCommands;
	protected AtomicReference<Java2DCommand[]> oldCommands;
	protected AtomicReference<Canvas2DScalingFactor> scalingFactor;
	protected AtomicBoolean sceneAntialiasingIsEnabled;
	protected AtomicBoolean redrawingIsSuspended;
	//
	///////////////////////////////////////////////////////////////
	//
	public ExtendedJPanel(AtomicReference<Component> o, List<Java2DCommand> cC, AtomicReference<Java2DCommand[]> oC, AtomicReference<Canvas2DScalingFactor> sF, AtomicBoolean sAIE, AtomicBoolean rIS) {
		owner= o;
		currentCommands= cC;
		oldCommands= oC;
		scalingFactor= sF;
		sceneAntialiasingIsEnabled= sAIE;
		redrawingIsSuspended= rIS;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setScalingFactor(AtomicReference<Canvas2DScalingFactor> factor) {
		scalingFactor= factor;
	}
	public void setEnableAntialiasing(AtomicBoolean enableAntialiasing) {
		sceneAntialiasingIsEnabled= enableAntialiasing;
	}
	public void setCommands(List<Java2DCommand> commandList) {
		currentCommands= commandList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void paintComponent(Graphics g) {
		if (currentCommands != null) {
			Graphics2D g2= (Graphics2D)g;
			boolean enableSceneAntialiasing= sceneAntialiasingIsEnabled.get();
			if (enableSceneAntialiasing) {
				RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHints(rh);
			};
			quicklyCorrectSizeIfNecessary();
			Dimension size= getSize();
			Canvas2DScalingFactor currentScalingFactor= scalingFactor.get();
			synchronized (currentCommands) {
				super.paintComponent(g);
				DrawingMode drawingMode= new DrawingMode(size,currentScalingFactor);
				if (redrawingIsSuspended.get()) {
					Java2DCommand[] oldCommandArray= oldCommands.get();
					if (oldCommandArray != null) {
						for (int n=0; n < oldCommandArray.length; n++) {
							Java2DCommand command= oldCommandArray[n];
							command.execute(g2,drawingMode);
						}
					}
				} else {
					ListIterator<Java2DCommand> iterator= currentCommands.listIterator();
					while (iterator.hasNext()) {
						Java2DCommand command= iterator.next();
						command.execute(g2,drawingMode);
					}
				}
			}
		}
	}
	protected void quicklyCorrectSizeIfNecessary() {
		if (owner.get() != null) {
			Dimension currentSize= getSize();
			Dimension minimumSize= getMinimumSize();
			if (currentSize.width != minimumSize.width || currentSize.height != minimumSize.height) {
				setSize(minimumSize);
			}
		}
	}
}
