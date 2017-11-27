/*
 * @(#)Icon2D.java 1.0 2013/08/25
 *
 * Copyright 2013 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * Icon2D implementation for the Actor Prolog language
 * @version 1.0 2013/08/25
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.system.gui.space2d.*;
import morozov.terms.*;

import java.awt.GridBagConstraints;
import javax.swing.Icon;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Dimension;

public class Icon2D implements Icon {
	//
	protected ExtendedSpace2D space2D;
	protected boolean keepProportions;
	protected int anchor;
	protected int currentHeight= 0;
	protected int currentWidth= 0;
	//
	public Icon2D(ExtendedSpace2D c, boolean keepProportionsFlag, int a) {
		space2D= c;
		keepProportions= keepProportionsFlag;
		anchor= a;
	}
	//
	public void setCanvas2D(ExtendedSpace2D c) {
		space2D= c;
	}
	//
	public void setSize(Dimension d) {
		currentWidth= d.width;
		currentHeight= d.height;
	}
	//
	public int getIconHeight() {
		return currentHeight;
	}
	public int getIconWidth() {
		return currentWidth;
	}
	public void paintIcon(Component c, Graphics g0, int x, int y) {
		if (space2D != null) {
			if (keepProportions) {
				Graphics gg= g0.create();
				try {
					int iconHeight= c.getHeight();
					int iconWidth= c.getWidth();
					int imageHeight= currentHeight;
					int imageWidth= currentWidth;
					double ratio0= (double)iconHeight / iconWidth;
					double ratio1= (double)imageHeight / imageWidth;
					if (ratio0 < ratio1) {
						int w2= PrologInteger.toInteger(iconHeight / ratio1);
						if (	anchor==GridBagConstraints.WEST ||
							anchor==GridBagConstraints.NORTHWEST ||
							anchor==GridBagConstraints.SOUTHWEST ) {
							space2D.quicklyPaintComponent(gg);
						} else if (
							anchor==GridBagConstraints.EAST ||
							anchor==GridBagConstraints.NORTHEAST ||
							anchor==GridBagConstraints.SOUTHEAST ) {
							int x3= iconWidth - w2;
							gg.translate(x3,0);
							space2D.quicklyPaintComponent(gg);
						} else {
							int deltaW= PrologInteger.toInteger((iconWidth - w2) / 2);
							gg.translate(deltaW,0);
							space2D.quicklyPaintComponent(gg);
						}
					} else {
						int h2= PrologInteger.toInteger(iconWidth * ratio1);
						if (	anchor==GridBagConstraints.NORTH ||
							anchor==GridBagConstraints.NORTHWEST ||
							anchor==GridBagConstraints.NORTHEAST ) {
							space2D.quicklyPaintComponent(gg);
						} else if (
							anchor==GridBagConstraints.SOUTH ||
							anchor==GridBagConstraints.SOUTHWEST ||
							anchor==GridBagConstraints.SOUTHEAST ) {
							int y3= iconHeight - h2;
							gg.translate(0,y3);
							space2D.quicklyPaintComponent(gg);
						} else {
							int deltaH= PrologInteger.toInteger((iconHeight - h2) / 2);
							gg.translate(0,deltaH);
							space2D.quicklyPaintComponent(gg);
						}
					}
				} finally {
					gg.dispose();
				}
                        } else {
				Dimension d= new Dimension();
				c.getSize(d);
				space2D.quicklySetSize(d);
				space2D.quicklyPaintComponent(g0);
			}
		}
	}
}
