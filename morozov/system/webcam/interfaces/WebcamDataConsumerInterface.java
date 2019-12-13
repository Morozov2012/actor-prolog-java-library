// (c) 2019 Alexei A. Morozov

package morozov.system.webcam.interfaces;

import com.github.sarxos.webcam.WebcamEvent;

public interface WebcamDataConsumerInterface {
	public void webcamOpen(WebcamEvent event);
	public void webcamImageObtained(java.awt.image.BufferedImage image);
	public void webcamClosed(WebcamEvent event);
	public void webcamDisposed(WebcamEvent event);
}
