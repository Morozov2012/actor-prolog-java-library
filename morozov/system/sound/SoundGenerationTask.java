// (c) 2018 Alexei A. Morozov

package morozov.system.sound;

import morozov.system.sound.interfaces.*;

import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.LineUnavailableException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.LinkedBlockingQueue;

public class SoundGenerationTask extends Thread {
	//
	protected SourceDataLine speakers;
	protected LinkedBlockingQueue<byte[]> queue= new LinkedBlockingQueue<>();
	//
	protected AtomicReference<AudioDataConsumerInterface> controlPanel= new AtomicReference<>();
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected AtomicBoolean deviceIsOpen= new AtomicBoolean(false);
	//
	protected AtomicBoolean enableDataTransfer= new AtomicBoolean(true);
	//
	protected byte[] m_buf;
	//
	protected static int deviceConnectionAttemptPeriod= 100;
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	//
	///////////////////////////////////////////////////////////////
	//
	public SoundGenerationTask() {
		setDaemon(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDataConsumer(AudioDataConsumerInterface panel) {
		controlPanel.set(panel);
	}
	//
	public void setOutputDebugInformation(int value) {
		outputDebugInformation.set(value);
	}
	public int getOutputDebugInformation() {
		return outputDebugInformation.get();
	}
	//
	public void startDataTransfer() {
		synchronized (this) {
			queue.clear();
			enableDataTransfer.set(true);
			notifyAll();
		}
	}
	//
	public void stopDataTransfer() {
		synchronized (this) {
			queue.clear();
			if (speakers != null) {
				closeSpeakers();
				enableDataTransfer.set(false);
				notifyAll();
			}
		}
	}
	//
	protected void closeSpeakers() {
		speakers.drain();
		speakers.flush();
		speakers.close();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void run() {
		try {
			while (!stopThisThread.get()) {
				synchronized (this) {
					if (!enableDataTransfer.get()) {
						wait();
						continue;
					}
				};
				if (deviceIsOpen.get()) {
					doGenerateSound();
				} else {
					try {
                	                	tryToOpenDevice();
					} catch (LineUnavailableException e) {
						if (reportCriticalErrors()) {
							writeLater(String.format("LineUnavailableException: %s\n",e));
						}
					} catch (SecurityException e) {
						if (reportCriticalErrors()) {
							writeLater(String.format("SecurityException: %s\n",e));
						}
					} catch (IllegalArgumentException e) {
						if (reportCriticalErrors()) {
							writeLater(String.format("IllegalArgumentException: %s\n",e));
						}
					} catch (Throwable e) {
						if (reportAdmissibleErrors()) {
							e.printStackTrace();
							writeLater(String.format("Camera initialization error: %s\n",e));
						};
						deviceIsOpen.set(false);
					};
					if (!deviceIsOpen.get()) {
						synchronized (this) {
							wait(deviceConnectionAttemptPeriod);
						}
					}
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
	public boolean deviceDoesExist() {
		try {
			synchronized (this) {
				AudioSystem.getSourceDataLine(null);
			};
			return true;
		} catch (LineUnavailableException e) {
			if (reportCriticalErrors()) {
				writeLater(String.format("LineUnavailableException: %s\n",e));
			}
		} catch (SecurityException e) {
			if (reportCriticalErrors()) {
				writeLater(String.format("SecurityException: %s\n",e));
			}
		} catch (IllegalArgumentException e) {
			if (reportCriticalErrors()) {
				writeLater(String.format("IllegalArgumentException: %s\n",e));
			}
		};
		return false;
	}
	//
	public void tryToOpenDevice() throws LineUnavailableException {
		synchronized (this) {
			if (!deviceIsOpen.get()) {
				speakers= AudioSystem.getSourceDataLine(null);
				speakers.open();
				speakers.start();
				deviceIsOpen.set(true);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void generateSound(byte[] targetData) {
		try {
			queue.put(targetData);
		} catch (InterruptedException e) {
		}
	}
	//
	protected void doGenerateSound() {
		if (deviceIsOpen.get()) {
			try {
                        	byte[] targetData= queue.take();
				int numberOfBytesActuallyWritten= speakers.write(targetData,0,targetData.length);
				if (numberOfBytesActuallyWritten != targetData.length) {
					closeSpeakers();
					deviceIsOpen.set(false);
				}
			} catch (InterruptedException e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeLater(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.err.print(text);
			}
		});
	}
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
