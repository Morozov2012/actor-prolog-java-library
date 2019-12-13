// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.interfaces;

import morozov.system.ffmpeg.*;

public interface FFmpegInterface {
	//
	public void sendFFmpegFrame(FFmpegFrame frame);
	public void sendFFmpegAudioData(FFmpegAudioData frame);
	public void resetCounters();
	//
	public void setDeliveredDescription(String value);
	public void setDeliveredCopyright(String value);
	public void setDeliveredRegistrationTime(String value);
	public void setDeliveredRegistrationDate(String value);
	//
	public void completeDataReading(long numberOfAcquiredVideoFrames);
	public void completeDataReading(long numberOfVideoFrameToBeAcquired, Throwable e);
	//
	public void completeDataWriting(long numberOfFrames, Throwable e);
	//
	public void reportBufferOverflow();
	public void annulBufferOverflow();
}
