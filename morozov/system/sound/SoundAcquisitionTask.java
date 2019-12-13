// (c) 2018 Alexei A. Morozov

package morozov.system.sound;

import morozov.system.sound.frames.data.*;
import morozov.system.sound.interfaces.*;

import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SoundAcquisitionTask extends Thread {
	//
	protected AtomicReference<Mixer.Info> requestedMixerInfo= new AtomicReference<>();
	protected AtomicReference<AudioFormat> requestedAudioFormat= new AtomicReference<>();
	//
	protected TargetDataLine microphone;
	protected PipedInputStream pipedInputStream;
	protected PipedOutputStream pipedOutputStream;
	protected int inputStreamBufferSize;
	//
	protected AtomicReference<AudioDataConsumerInterface> controlPanel= new AtomicReference<>();
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected AtomicBoolean deviceIsOpen= new AtomicBoolean(false);
	protected AtomicBoolean enableDataTransfer= new AtomicBoolean(false);
	protected AtomicBoolean suspendDataAcquisition= new AtomicBoolean(false);
	//
	protected byte[] m_buf;
	protected int numberOfErrors= 0;
	protected static int maximalNumberOfErrors= 100;
	//
	protected static int deviceConnectionAttemptPeriod= 100;
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	//
	///////////////////////////////////////////////////////////////
	//
	public SoundAcquisitionTask(PipedInputStream inputStream, int size) {
		pipedInputStream= inputStream;
		inputStreamBufferSize= size;
		try {
			pipedOutputStream= new PipedOutputStream(pipedInputStream);
		} catch (IOException e) {
			if (reportCriticalErrors()) {
				writeLater(String.format("SoundAcquisitionTask:CannotCreatePipedOutputStream:%s\n",e));
			}
		};
		setDaemon(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setRequestedMixerInfoAndAudioFormat(Mixer.Info mixerInfo, AudioFormat format) {
		suspendDataAcquisition.set(true);
		try {
			synchronized (this) {
				requestedMixerInfo.set(mixerInfo);
				requestedAudioFormat.set(format);
			}
		} finally {
			suspendDataAcquisition.set(false);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AudioFormatBaseAttributes getAudioFormat() {
		suspendDataAcquisition.set(true);
		try {
			synchronized (this) {
				if (microphone==null) {
					if (!deviceDoesExist()) {
						return null;
					}
				};
				if (microphone != null) {
					AudioFormat format= microphone.getFormat();
					return new AudioFormatBaseAttributes(
						format.getChannels(),
						format.getEncoding().toString(),
						format.getFrameRate(),
						format.getFrameSize(),
						format.getSampleRate(),
						format.getSampleSizeInBits(),
						format.isBigEndian());
				} else {
					return null;
				}
			}
		} finally {
			suspendDataAcquisition.set(false);
		}
	}
	//
	public boolean microphoneIsAvailable() {
		suspendDataAcquisition.set(true);
		try {
			synchronized (this) {
				if (microphone==null) {
					if (!deviceDoesExist()) {
						return false;
					}
				};
				return (microphone != null);
			}
		} finally {
			suspendDataAcquisition.set(false);
		}
	}
	//
	public boolean microphoneIsActive() {
		suspendDataAcquisition.set(true);
		try {
			synchronized (this) {
				if (microphone==null) {
					return false;
				};
				if (!isAlive()) {
					return false;
				};
				return enableDataTransfer.get();
			}
		} finally {
			suspendDataAcquisition.set(false);
		}
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
	public boolean dataTransferIsEnabled() {
		return enableDataTransfer.get();
	}
	public boolean deviceIsOpen() {
		return deviceIsOpen.get();
	}
	//
	public void startDataTransfer() {
		suspendDataAcquisition.set(true);
		try {
			synchronized (this) {
				enableDataTransfer.set(true);
				if (!isAlive()) {
					start();
				};
				notifyAll();
			}
		} finally {
			suspendDataAcquisition.set(false);
		}
	}
	//
	public void stopDataTransfer() {
		suspendDataAcquisition.set(true);
		try {
			synchronized (this) {
				if (microphone != null) {
					closeMicrophone();
					enableDataTransfer.set(false);
					notifyAll();
				}
			}
		} finally {
			suspendDataAcquisition.set(false);
		}
	}
	//
	protected void closeMicrophone() {
		suspendDataAcquisition.set(true);
		try {
			synchronized (this) {
				if (microphone != null) {
					microphone.close();
				}
			}
		} finally {
			suspendDataAcquisition.set(false);
		};
		try {
			pipedOutputStream.flush();
		} catch (IOException e) {
			if (reportCriticalErrors()) {
				writeLater(String.format("SoundAcquisitionTask:CannotFlushPipedOutputStream:%s\n",e));
			}
		}
	}
	//
	public void flush() {
		suspendDataAcquisition.set(true);
		try {
			synchronized (this) {
				if (microphone != null) {
					microphone.flush();
				}
			}
		} finally {
			suspendDataAcquisition.set(false);
		};
		try {
			pipedOutputStream.flush();
		} catch (IOException e) {
			if (reportCriticalErrors()) {
				writeLater(String.format("SoundAcquisitionTask:CannotFlushPipedOutputStream:%s\n",e));
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	@SuppressWarnings("CallToThreadDumpStack")
	public void run() {
		try {
			while (true) {
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
						if (reportCriticalErrors()) {
							e.printStackTrace();
							writeLater(String.format("Sound acquisition error: %s\n",e));
						}
					}
				} else {
					try {
                	                	tryToOpenDevice();
					} catch (LineUnavailableException e) {
						reportMicrophoneAvailability(false);
					} catch (SecurityException e) {
						reportMicrophoneAvailability(false);
					} catch (IllegalArgumentException e) {
						reportMicrophoneAvailability(false);
					} catch (Throwable e) {
						reportMicrophoneAvailability(false);
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
			suspendDataAcquisition.set(true);
			try {
				synchronized (this) {
					Mixer.Info mixerInfo= requestedMixerInfo.get();
					AudioFormat audioFormat= requestedAudioFormat.get();
					if (mixerInfo != null && audioFormat != null) {
						microphone= AudioSystem.getTargetDataLine(audioFormat,mixerInfo);
					} else {
						microphone= AudioSystem.getTargetDataLine(null);
					}
				}
			} finally {
				suspendDataAcquisition.set(false);
			};
			return true;
		} catch (LineUnavailableException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		};
		return false;
	}
	//
	public void tryToOpenDevice() throws LineUnavailableException {
		synchronized (this) {
			if (!deviceIsOpen.get()) {
				Mixer.Info mixerInfo= requestedMixerInfo.get();
				AudioFormat audioFormat= requestedAudioFormat.get();
				if (mixerInfo != null && audioFormat != null) {
					microphone= AudioSystem.getTargetDataLine(audioFormat,mixerInfo);
				} else {
					microphone= AudioSystem.getTargetDataLine(null);
				};
				int dataGroupSize= microphone.getBufferSize() / 5;
				if (dataGroupSize > inputStreamBufferSize) {
					dataGroupSize= inputStreamBufferSize;
				};
				if (	m_buf==null ||
					m_buf.length != dataGroupSize) {
					m_buf= new byte[dataGroupSize];
				};
				if (audioFormat != null) {
					microphone.open(audioFormat);
				} else {
					microphone.open();
				};
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
		int numberOfReceivedBytes;
		synchronized (this) {
			numberOfReceivedBytes= microphone.read(m_buf,0,m_buf.length);
		};
		if (numberOfReceivedBytes == m_buf.length) {
			pipedOutputStream.write(m_buf,0,numberOfReceivedBytes);
			synchronized (pipedInputStream) {
				pipedInputStream.notifyAll();
			}
		};
		if (numberOfReceivedBytes > 0) {
			numberOfErrors= 0;
		} else {
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
			@Override
			public void run() {
				System.err.print(text);
			}
		});
	}
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
