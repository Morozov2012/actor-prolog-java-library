// (c) 2018 Alexei A. Morozov

package morozov.system.sound;

import morozov.system.sound.interfaces.*;

import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.LineUnavailableException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SoundAcquisitionTask extends Thread {
	//
	protected TargetDataLine microphone;
	protected PipedInputStream pipedInputStream;
	protected PipedOutputStream pipedOutputStream;
	protected int inputStreamBufferSize;
	//
	protected AtomicReference<AudioDataConsumerInterface> controlPanel= new AtomicReference<>();
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected AtomicBoolean deviceIsOpen= new AtomicBoolean(false);
	protected AtomicBoolean enableDataTransfer= new AtomicBoolean(false);
	//
	protected byte[] m_buf;
	protected int numberOfErrors= 0;
	protected static int maximalNumberOfErrors= 100;
	//
	protected static int deviceOpeningAttemptDelay= 100;
	//
	///////////////////////////////////////////////////////////////
	//
	public SoundAcquisitionTask(PipedInputStream inputStream, int size) {
		pipedInputStream= inputStream;
		inputStreamBufferSize= size;
		try {
			pipedOutputStream= new PipedOutputStream(pipedInputStream);
		} catch (IOException e) {
			writeLater(String.format("SoundAcquisitionTask:CannotCreatePipedOutputStream:%s\n",e));
		};
		setDaemon(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setControlPanel(AudioDataConsumerInterface panel) {
		controlPanel.set(panel);
	}
	//
	public boolean dataTransferIsEnabled() {
		return enableDataTransfer.get();
	}
	public boolean deviceIsOpen() {
		return deviceIsOpen.get();
	}
	//
	public void startDataTransfer() {
		synchronized (this) {
			enableDataTransfer.set(true);
			notify();
		}
	}
	//
	public void stopDataTransfer() {
		synchronized (this) {
			if (microphone != null) {
				closeMicrophone();
				enableDataTransfer.set(false);
				notify();
			}
		}
	}
	//
	protected void closeMicrophone() {
		microphone.close();
		try {
			pipedOutputStream.flush();
		} catch (IOException e) {
			writeLater(String.format("SoundAcquisitionTask:CannotFlushPipedOutputStream:%s\n",e));
		}
	}
	//
	public void flush() {
		microphone.flush();
		try {
			pipedOutputStream.flush();
		} catch (IOException e) {
			writeLater(String.format("SoundAcquisitionTask:CannotFlushPipedOutputStream:%s\n",e));
		}
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
					try {
						dataAcquisition();
					} catch (Throwable e) {
						e.printStackTrace();
						writeLater(String.format("Sound acquisition error: %s\n",e));
					}
				} else {
					try {
                	                	tryToOpenDevice();
					} catch (LineUnavailableException e) {
						reportMicrophoneAvailability(false);
						// writeLater(String.format("LineUnavailableException: %s\n",e));
					} catch (SecurityException e) {
						reportMicrophoneAvailability(false);
						// writeLater(String.format("SecurityException: %s\n",e));
					} catch (IllegalArgumentException e) {
						reportMicrophoneAvailability(false);
						// writeLater(String.format("IllegalArgumentException: %s\n",e));
					} catch (Throwable e) {
						reportMicrophoneAvailability(false);
						e.printStackTrace();
						writeLater(String.format("Camera initialization error: %s\n",e));
						deviceIsOpen.set(false);
					};
					if (!deviceIsOpen.get()) {
						synchronized (this) {
							wait(deviceOpeningAttemptDelay);
						}
					}
				}
			}
		} catch (InterruptedException e) {
		} catch (ThreadDeath e) {
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	//
	protected void reportMicrophoneAvailability(boolean state) {
		AudioDataConsumerInterface panel= controlPanel.get();
		if (panel != null) {
			panel.reportMicrophoneAvailability(state);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean deviceDoesExist() {
		try {
			synchronized (this) {
				AudioSystem.getTargetDataLine(null);
			};
			return true;
		} catch (LineUnavailableException e) {
			// writeLater(String.format("LineUnavailableException: %s\n",e));
		} catch (SecurityException e) {
			// writeLater(String.format("SecurityException: %s\n",e));
		} catch (IllegalArgumentException e) {
			// writeLater(String.format("IllegalArgumentException: %s\n",e));
		};
		return false;
	}
	//
	public void tryToOpenDevice() throws LineUnavailableException {
		synchronized (this) {
			if (!deviceIsOpen.get()) {
				microphone= AudioSystem.getTargetDataLine(null);
				int dataGroupSize= microphone.getBufferSize() / 5;
				if (dataGroupSize > inputStreamBufferSize) {
					dataGroupSize= inputStreamBufferSize;
				};
				if (	m_buf==null ||
					m_buf.length != dataGroupSize) {
					m_buf= new byte[dataGroupSize];
				};
				microphone.open();
				microphone.start();
				deviceIsOpen.set(true);
				numberOfErrors= 0;
				reportMicrophoneAvailability(true);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void dataAcquisition() throws IOException {
		int numberOfReceivedBytes= microphone.read(m_buf,0,m_buf.length);
		if (numberOfReceivedBytes == m_buf.length) {
			pipedOutputStream.write(m_buf,0,numberOfReceivedBytes);
			synchronized (pipedInputStream) {
				pipedInputStream.notify();
			}
		};
		if (numberOfReceivedBytes > 0) {
			numberOfErrors= 0;
		} else {
			reportMicrophoneAvailability(false);
			if (numberOfErrors > maximalNumberOfErrors) {
				closeMicrophone();
				deviceIsOpen.set(false);
				numberOfErrors= 0;
			} else {
				numberOfErrors++;
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
}
