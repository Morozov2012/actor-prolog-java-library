// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.img;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMimgGetSubimage extends VPM_FrameCommand {
	//
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	//
	public VPMimgGetSubimage(int givenX, int givenY, int givenWidth, int givenHeight) {
		x= givenX;
		y= givenY;
		width= givenWidth;
		height= givenHeight;
	}
	//
	public void execute(VPM vpm) {
		// if (vpm.imageIsAnalyzed()) {
		//	throw new ImageResizingIsToBeDoneBeforeImageAnalysis();
		// };
		int sourceWidth= vpm.getOperationalImageWidth();
		int sourceHeight= vpm.getOperationalImageHeight();
		java.awt.image.BufferedImage sourceImage= vpm.getPreprocessedImage();
		if (sourceImage==null) {
			sourceImage= vpm.getRecentImage();
		};
		int maximalWidth= sourceWidth - x;
		int maximalHeight= sourceHeight - y;
		int destinationWidth= width;
		int destinationHeight= height;
		if (destinationWidth < 0 || destinationWidth > maximalWidth) {
			destinationWidth= maximalWidth;
		};
		if (destinationHeight < 0 || destinationHeight > maximalHeight) {
			destinationHeight= maximalHeight;
		};
		java.awt.image.BufferedImage subimage= sourceImage.getSubimage(x,y,destinationWidth,destinationHeight);
		vpm.setPreprocessedImage(subimage);
	}
}
