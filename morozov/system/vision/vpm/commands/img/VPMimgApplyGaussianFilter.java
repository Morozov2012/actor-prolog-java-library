// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.img;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.terms.*;

import java.awt.Graphics2D;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.ImagingOpException;

public class VPMimgApplyGaussianFilter extends VPM_FrameCommand {
	//
	protected int radius;
	//
	protected ConvolveOp convolveOperation;
	//
	public VPMimgApplyGaussianFilter(int r) {
		radius= r;
	}
	//
	public void execute(VPM vpm) {
		if (convolveOperation==null) {
			float[] gaussianMatrix= VisionUtils.gaussianMatrix(radius);
			int length= PrologInteger.toInteger(StrictMath.sqrt(gaussianMatrix.length));
			convolveOperation= new ConvolveOp(
				new Kernel(length,length,gaussianMatrix),
				// ConvolveOp.EDGE_NO_OP,
				ConvolveOp.EDGE_ZERO_FILL,
				null);
		};
		// if (vpm.imageIsAnalyzed()) {
		//	throw new ImageFilteringIsToBeDoneBeforeImageAnalysis();
		// };
		int sourceWidth= vpm.getOperationalImageWidth();
		int sourceHeight= vpm.getOperationalImageHeight();
		java.awt.image.BufferedImage previousImage= vpm.getPreprocessedImage();
		if (previousImage==null) {
			previousImage= vpm.getRecentImage();
		};
		java.awt.image.BufferedImage newImage= new java.awt.image.BufferedImage(sourceWidth,sourceHeight,java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2= newImage.createGraphics();
		try {
			try {
				g2.drawImage(previousImage,convolveOperation,0,0);
			} catch (ImagingOpException e) {
				previousImage= convertImage(sourceWidth,sourceHeight,previousImage);
				g2.drawImage(previousImage,convolveOperation,0,0);
			}
		} finally {
			g2.dispose();
		};
		vpm.setPreprocessedImage(newImage);
	}
	//
	protected java.awt.image.BufferedImage convertImage(int width, int height, java.awt.image.BufferedImage previousImage) {
		java.awt.image.BufferedImage newImage= new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2= newImage.createGraphics();
		try {
			g2.drawImage(previousImage,0,0,null);
		} finally {
			g2.dispose();
		};
		return newImage;
	}
}
