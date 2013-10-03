/*
 * @(#)IconImage.java 1.0 2013/08/16
 *
 * Copyright 2013 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * IconImage implementation for the Actor Prolog language
 * @version 1.0 2013/08/16
 * @author IRE RAS Alexei A. Morozov
*/


import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.Dimension;

public class IconImage implements Icon {
	//
	protected BufferedImage image;
	protected boolean keepProportions;
	protected int anchor;
	protected int currentHeight= 0;
	protected int currentWidth= 0;
	//
	public IconImage(BufferedImage i, boolean keepProportionsFlag, int a) {
		image= i;
		keepProportions= keepProportionsFlag;
		anchor= a;
	}
	//
	public void setImage(BufferedImage i) {
		image= i;
	}
	//
	public void setSize(Dimension dimension) {
		currentWidth= dimension.width;
		currentHeight= dimension.height;
	}
	//
	public int getIconHeight() {
		return currentHeight;
	}
	public int getIconWidth() {
		return currentWidth;
	}
	public void paintIcon(Component c, Graphics g0, int x, int y) {
		if (image != null) {
			Graphics gg= g0.create();
			try {
				Graphics2D g2= (Graphics2D)gg;
				// if (enableAntialiasing) {
				//	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				//	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				// };
				int iconHeight= c.getHeight();
				int iconWidth= c.getWidth();
				if (keepProportions) {
					int imageHeight= image.getHeight();
					int imageWidth= image.getWidth();
					double ratio0= (double)iconHeight / iconWidth;
					double ratio1= (double)imageHeight / imageWidth;
					if (ratio0 < ratio1) {
						int w2= (int)StrictMath.round(iconHeight / ratio1);
						if (	anchor==GridBagConstraints.WEST ||
							anchor==GridBagConstraints.NORTHWEST ||
							anchor==GridBagConstraints.SOUTHWEST ) {
							g2.drawImage(image,0,0,w2,iconHeight,null);
						} else if (
							anchor==GridBagConstraints.EAST ||
							anchor==GridBagConstraints.NORTHEAST ||
							anchor==GridBagConstraints.SOUTHEAST ) {
							int x3= iconWidth - w2;
							g2.drawImage(image,x3,0,w2,iconHeight,null);
						} else {
							int deltaW= (int)StrictMath.round((iconWidth - w2) / 2);
							g2.drawImage(image,deltaW,0,w2,iconHeight,null);
						}
					} else {
						int h2= (int)StrictMath.round(iconWidth * ratio1);
						if (	anchor==GridBagConstraints.NORTH ||
							anchor==GridBagConstraints.NORTHWEST ||
							anchor==GridBagConstraints.NORTHEAST ) {
							g2.drawImage(image,0,0,iconWidth,h2,null);
						} else if (
							anchor==GridBagConstraints.SOUTH ||
							anchor==GridBagConstraints.SOUTHWEST ||
							anchor==GridBagConstraints.SOUTHEAST ) {
							int y3= iconHeight - h2;
							g2.drawImage(image,0,y3,iconWidth,h2,null);
						} else {
							int deltaH= (int)StrictMath.round((iconHeight - h2) / 2);
							g2.drawImage(image,0,deltaH,iconWidth,h2,null);
						}
					}
				} else {
					g2.drawImage(image,0,0,iconWidth,iconHeight,null);
				}
			} finally {
				gg.dispose();
			}
		}
	}
}
