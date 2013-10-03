/*
 * @(#)ScalablePanel.java 1.0 2010/04/28
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalablePanel implementation for the Actor Prolog language
 * @version 1.0 2010/04/28
 * @author IRE RAS Alexei A. Morozov
*/

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class ScalablePanel extends JPanel {
	//
	protected GridBagLayout gridBagLayout;
	//
	protected boolean isTop= false;
	protected boolean isLeft= false;
	protected boolean isBottom= false;
	protected boolean isRight= false;
	//
	protected double horizontalPadding= 0;
	protected double verticalPadding= 0;
	protected double horizontalScaling= 1;
	protected double verticalScaling= 1;
	//
	protected int getInitialTopBorder() {return 0;}
	protected int getInitialLeftBorder() {return 0;}
	protected int getInitialBottomBorder() {return 0;}
	protected int getInitialRightBorder() {return 0;}
	//
	public boolean isTransparent= true;
	public Color hatchColor= null;
	//
	public ScalablePanel() {
	}
	//
	public void setPadding(
			GridBagLayout gBL,
			boolean flagTop, boolean flagLeft, boolean flagBottom, boolean flagRight,
			double valueHorizontal, double valueVertical) {
		gridBagLayout= gBL;
		isTop= flagTop;
		isLeft= flagLeft;
		isBottom= flagBottom;
		isRight= flagRight;
		horizontalPadding= valueHorizontal;
		verticalPadding= valueVertical;
	}
	// public boolean isTitledPanel() {
	//	return false;
	// }
	public void setScaling(double valueHorizontal, double valueVertical) {
		horizontalScaling= valueHorizontal;
		verticalScaling= valueVertical;
	}
	// public void setSpaceColor(Color c) {
	// }
	public void setFont(Font font) {
		super.setFont(font);
		if (gridBagLayout!=null) {
			//
			GridBagConstraints gBC= gridBagLayout.getConstraints(this);
			FontMetrics metrics= getFontMetrics(font);
			gBC.insets= LayoutUtils.calculateLayoutInsets(
				gBC,
				font,
				metrics,
				horizontalPadding,
				verticalPadding,
				horizontalScaling,
				verticalScaling,
				isTop,
				isLeft,
				isBottom,
				isRight,
				getInitialTopBorder(),
				getInitialLeftBorder(),
				getInitialBottomBorder(),
				getInitialRightBorder());
			gridBagLayout.setConstraints(this,gBC);
		};
		invalidate();
		// setMinimumSize(getPreferredSize());
	}
	public void setTransparency(boolean flag) {
		isTransparent= flag;
	}
	public void setHatchColor(Color c) {
		hatchColor= c;
	}
	public void setAlarmColors(Color fc, Color bc) {
	}
	public void paint(Graphics g0) {
		if (!isTransparent) {
			// setOpaque(true);
			// Graphics gg= g0.create();
			// try {
				Graphics2D g2= (Graphics2D)g0;
				Color bgColor= getBackground();
				g2.setColor(bgColor);
				// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
				int height= getHeight();
				int width= getWidth();
				g2.fillRect(0,0,width,height);
				if (hatchColor!=null) {
					g2.setColor(hatchColor);
					int x= 0;
					while(x <= width) {
						g2.fill(new Rectangle2D.Double(x,0,3,height));
						x= x + 10;
					}
				}
			// } finally {
			//	gg.dispose();
			// }
		};
		paintBorder(g0);
		super.paintComponents(g0);
	}
}
