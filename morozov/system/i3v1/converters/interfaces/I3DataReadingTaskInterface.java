// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.converters.interfaces;

public interface I3DataReadingTaskInterface {
	public void readGivenNumberOfFrames(int numberOfFrames);
	public void readFramesUntilGivenTime(long time, int numberOfExtraFrames);
}
