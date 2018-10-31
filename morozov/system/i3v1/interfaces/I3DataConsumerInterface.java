// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.interfaces;

public interface I3DataConsumerInterface {
	public void setByteThermalData(byte[] buffer, long time);
	public void reportMissedFrame(long currentTime);
	public void completeCalibration();
}
