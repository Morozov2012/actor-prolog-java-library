// (c) 2018 Alexei A. Morozov

package morozov.system.ip_camera.converters.interfaces;

public interface IPCameraFrameReadingTaskInterface {
	public void readGivenNumberOfColorFrames(int numberOfColorFrames);
	public void readFramesUntilGivenTime(long time, int numberOfExtraColorFrames);
}
