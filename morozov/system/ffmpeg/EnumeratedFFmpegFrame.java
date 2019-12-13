// (c) 2018 Alexei A. Morozov

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avutil.*;

import morozov.built_in.*;
import morozov.system.ffmpeg.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.gui.space2d.*;

import java.io.Serializable;

public class EnumeratedFFmpegFrame extends EnumeratedFrame implements Serializable {
	//
	protected transient FFmpegFrameInterface frame;
	protected byte[] byteArray;
	protected int width;
	protected int height;
	protected long timeInMilliseconds;
	protected long timeInBaseUnits;
	protected int timeBaseNumerator;
	protected int timeBaseDenominator;
	protected int averageFrameRateNumerator;
	protected int averageFrameRateDenominator;
	protected long number;
	//
	protected long numberOfFrame;
	//
	private static final long serialVersionUID= 0xA2EFBFDC7870BD11L; // -6705930366497014511L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.ffmpeg","EnumeratedFFmpegFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public EnumeratedFFmpegFrame(
			FFmpegFrameInterface givenFrame,
			FFmpeg targetClass,
			long givenNumberOfFrame) {
		frame= givenFrame;
		java.awt.image.BufferedImage image= givenFrame.getImage();
		byteArray= targetClass.convertImageToBytes(image);
		width= image.getWidth();
		height= image.getHeight();
		timeInMilliseconds= givenFrame.getTimeInMilliseconds();
		timeInBaseUnits= givenFrame.getTimeInBaseUnits();
		AVRational timeBase= givenFrame.getTimeBase();
		timeBaseNumerator= timeBase.num();
		timeBaseDenominator= timeBase.den();
		AVRational averageFrameRate= givenFrame.getAverageFrameRate();
		averageFrameRateNumerator= averageFrameRate.num();
		averageFrameRateDenominator= averageFrameRate.den();
		number= givenFrame.getNumber();
		numberOfFrame= givenNumberOfFrame;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegFrameInterface getFrame() {
		if (frame==null) {
			frame= new FFmpegFrame(
				Space2DWriter.bytesToImage(byteArray),
				timeInMilliseconds,
				timeInBaseUnits,
				av_make_q(timeBaseNumerator,timeBaseDenominator),
				av_make_q(averageFrameRateNumerator,averageFrameRateDenominator),
				number);
		};
		return frame;
	}
	//
	public long getNumberOfFrame() {
		return numberOfFrame;
	}
	//
	@Override
	public long getTime() {
		return timeInMilliseconds;
	}
}
