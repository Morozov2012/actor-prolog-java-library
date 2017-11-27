// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

public interface FFmpegInterface {
	public void sendFrame(FFmpegFrame frame);
	public void resetBuffer();
	//
	public void completeDataTransfer(long numberOfAcquiredFrames);
	public void completeDataTransfer(long numberOfFrameToBeAcquired, Throwable e);
	//
	public void reportBufferOverflow();
	public void annulBufferOverflow();
}
