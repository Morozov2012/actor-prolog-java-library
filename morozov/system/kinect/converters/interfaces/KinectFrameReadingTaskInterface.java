// (c) 2018 Alexei A. Morozov

package morozov.system.kinect.converters.interfaces;

public interface KinectFrameReadingTaskInterface {
	public void readGivenNumberOfTargetFrames(int numberOfFrames);
	public void readFramesUntilGivenTime(long time, int numberOfExtraFrames);
}
