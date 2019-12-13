// (c) 2019 Alexei A. Morozov

package morozov.system.webcam;

import morozov.system.webcam.interfaces.*;

import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class WebcamAcquisitionTask
		extends Thread
		implements WebcamListener {
	//
	protected WebcamDataConsumerInterface dataConsumer;
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	//
	protected AtomicReference<com.github.sarxos.webcam.Webcam> currentWebcam= new AtomicReference<>();
	protected AtomicReference<java.awt.image.BufferedImage> recentBufferedImage= new AtomicReference<>(null);
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	//
	///////////////////////////////////////////////////////////////
	//
	public WebcamAcquisitionTask(WebcamDataConsumerInterface consumer) {
		dataConsumer= consumer;
		setDaemon(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setCurrentWebcam(com.github.sarxos.webcam.Webcam webcam) {
		startProcessIfNecessary();
		currentWebcam.set(webcam);
	}
	//
	protected void startProcessIfNecessary() {
		synchronized (this) {
			if (!isAlive()) {
				start();
			};
			notifyAll();
		}
	}
	//
	public com.github.sarxos.webcam.Webcam getCurrentWebcam() {
		return currentWebcam.get();
	}
	//
	public void setOutputDebugInformation(int value) {
		outputDebugInformation.set(value);
	}
	public int getOutputDebugInformation() {
		return outputDebugInformation.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void webcamOpen(WebcamEvent event) {
		dataConsumer.webcamOpen(event);
	}
	@Override
	public void webcamImageObtained(WebcamEvent event) {
		com.github.sarxos.webcam.Webcam webcam= getCurrentWebcam();
		if (webcam != null) {
			recentBufferedImage.set(webcam.getImage());
			synchronized (this) {
				notifyAll();
			}
		}
	}
	@Override
	public void webcamClosed(WebcamEvent event) {
		dataConsumer.webcamClosed(event);
	}
	@Override
	public void webcamDisposed(WebcamEvent event) {
		dataConsumer.webcamDisposed(event);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	@SuppressWarnings("CallToThreadDumpStack")
	public void run() {
		try {
			while (!stopThisThread.get()) {
				dataConsumer.webcamImageObtained(recentBufferedImage.get());
				synchronized (this) {
					wait();
				}
			}
		} catch (InterruptedException e) {
		} catch (ThreadDeath e) {
		} catch (Throwable e) {
			if (reportCriticalErrors()) {
				e.printStackTrace();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean reportCriticalErrors() {
		return outputDebugInformation.get() >= reportCriticalErrorsLevel;
	}
	public boolean reportAdmissibleErrors() {
		return outputDebugInformation.get() >= reportAdmissibleErrorsLevel;
	}
	public boolean reportWarnings() {
		return outputDebugInformation.get() >= reportWarningsLevel;
	}
	//
	public int getReportCriticalErrorsLevel() {
		return reportCriticalErrorsLevel;
	}
	public int getReportAdmissibleErrorsLevel() {
		return reportAdmissibleErrorsLevel;
	}
	public int getReportWarningsLevel() {
		return reportWarningsLevel;
	}
}
