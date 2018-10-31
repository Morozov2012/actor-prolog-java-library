// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools;

import morozov.system.kinect.frames.tools.errors.*;

public class FrameSize {
	//
	public int width;
	public int height;
	//
	public FrameSize(int w, int h) {
		width= w;
		height= h;
	}
	//
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	//
	public static FrameSize computeDepthFrameSize(short[] depthFrame) {
		int frameLength= depthFrame.length;
		return computeFrameSize(frameLength,1);
	}
	//
	public static FrameSize computeXYZFrameSize(float[] xyzFrame) {
		int frameLength= xyzFrame.length;
		return computeFrameSize(frameLength,3);
	}
	//
	public static FrameSize computeUVFrameSize(float[] uvFrame) {
		int frameLength= uvFrame.length;
		return computeFrameSize(frameLength,2);
	}
	//
	public static FrameSize computeInfraredFrameSize(short[] depthFrame) {
		int frameLength= depthFrame.length;
		return computeFrameSize(frameLength,1);
	}
	//
	public static FrameSize computeLongExposureInfraredFrameSize(short[] depthFrame) {
		int frameLength= depthFrame.length;
		return computeFrameSize(frameLength,1);
	}
	//
	public static FrameSize computePlayerIndexFrameSize(byte[] playerIndexFrame) {
		int frameLength= playerIndexFrame.length;
		return computeFrameSize(frameLength,1);
	}
	//
	public static FrameSize computeFrameSize(int frameLength, int factor) {
		int width= 0;
		int height= 0;
		if (frameLength == 512 * 424 * factor) {
			width= 512;
			height= 424;
		} else if (frameLength == 80 * 60 * factor) {
			width= 80;
			height= 60;
		} else if (frameLength == 320 * 240 * factor) {
			width= 320;
			height= 240;
		} else if (frameLength == 640 * 480 * factor) {
			width= 640;
			height= 480;
		} else {
			throw new UnexpectedLengthOfFrame(frameLength);
		};
		return new FrameSize(width,height);
	}
	//
	public static FrameSize computeColorFrameSize(byte[] colorFrame) {
		int frameLength= colorFrame.length;
		int width= 0;
		int height= 0;
		if (frameLength == 1920 * 1080 * 4) {
			width= 1920;
			height= 1080;
		} else if (frameLength == 640 * 480 * 4) {
			width= 640;
			height= 480;
		} else if (frameLength == 1280 * 960 * 4) {
			width= 1280;
			height= 960;
		} else {
			throw new UnexpectedLengthOfColorFrame(frameLength);
		};
		return new FrameSize(width,height);
	}
}
