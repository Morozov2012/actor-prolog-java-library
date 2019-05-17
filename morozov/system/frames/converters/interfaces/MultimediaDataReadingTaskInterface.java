// (c) 2018 Alexei A. Morozov

package morozov.system.frames.converters.interfaces;

public interface MultimediaDataReadingTaskInterface {
	public void readGivenNumberOfFrames(int numberOfFrames);
	public void readFramesUntilGivenTime(long time, int numberOfExtraFrames);
}
