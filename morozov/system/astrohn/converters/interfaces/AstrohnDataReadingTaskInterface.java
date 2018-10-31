// (c) 2018 Alexei A. Morozov

package morozov.system.astrohn.converters.interfaces;

public interface AstrohnDataReadingTaskInterface {
	public void readGivenNumberOfTerahertzFrames(int numberOfTerahertzFrames);
	public void readFramesUntilGivenTime(long time, int numberOfExtraTerahertzFrames);
}
