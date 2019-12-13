// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.img;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

import java.awt.Graphics2D;

public class VPMimgResizeImage extends VPM_FrameCommand {
	//
	protected int width;
	protected int height;
	//
	public VPMimgResizeImage(int w, int h) {
		width= w;
		height= h;
	}
	//
	@Override
	public void execute(VPM vpm) {
		int sourceWidth= vpm.getOperationalImageWidth();
		int sourceHeight= vpm.getOperationalImageHeight();
		java.awt.image.BufferedImage sourceImage= vpm.getPreprocessedImage();
		if (sourceImage==null) {
			sourceImage= vpm.getRecentImage();
		};
		int destinationWidth= width;
		int destinationHeight= height;
		if (destinationWidth < 0) {
			if (destinationHeight < 0) {
				destinationWidth= sourceWidth;
				destinationHeight= sourceHeight;
			} else {
				destinationWidth= destinationHeight * sourceWidth / sourceHeight;
			}
		};
		if (destinationHeight < 0) {
			if (destinationWidth < 0) {
				destinationWidth= sourceWidth;
				destinationHeight= sourceHeight;
			} else {
				destinationHeight= destinationWidth * sourceHeight / sourceWidth;
			}
		};
		java.awt.image.BufferedImage scaledImage= new java.awt.image.BufferedImage(destinationWidth,destinationHeight,java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2= scaledImage.createGraphics();
		try {
			g2.drawImage(
				sourceImage,
				0,
				0,
				destinationWidth,
				destinationHeight,
				0,
				0,
				sourceWidth,
				sourceHeight,
				null);
		} finally {
			g2.dispose();
		};
		vpm.setPreprocessedImage(scaledImage);
	}
}
