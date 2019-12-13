// (c) 2018 Alexei A. Morozov

package morozov.system.sound;

import morozov.system.sound.frames.data.*;
import morozov.system.sound.interfaces.*;

import javax.swing.SwingUtilities;
import java.io.PipedInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SoundFramingTask extends Thread {
	//
	protected static int bufferSize= 1152*100;
	//
	protected int inputStreamBufferSize= bufferSize*5;
	protected PipedInputStream pipedInputStream= new PipedInputStream(inputStreamBufferSize);
	protected SoundAcquisitionTask soundAcquisitionTask= new SoundAcquisitionTask(pipedInputStream,inputStreamBufferSize);
	//
	protected AtomicReference<AudioDataConsumerInterface> controlPanel= new AtomicReference<>();
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected AtomicBoolean suspendDataAcquisition= new AtomicBoolean(false);
	protected AtomicInteger ingoreBytes= new AtomicInteger(0);
	//
	protected byte[] m_buf;
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	//
	protected static final long emergencyTimeout= 1000;
	//
	///////////////////////////////////////////////////////////////
	//
	public SoundFramingTask() {
		setDaemon(true);
	}
	public SoundFramingTask(AudioDataConsumerInterface panel) {
		controlPanel.set(panel);
		soundAcquisitionTask.setDataConsumer(panel);
		setDaemon(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setRequestedMixerInfoAndAudioFormat(Mixer.Info mixerInfo, AudioFormat format) {
		soundAcquisitionTask.setRequestedMixerInfoAndAudioFormat(mixerInfo,format);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AudioFormatBaseAttributes getAudioFormat() {
		return soundAcquisitionTask.getAudioFormat();
	}
	//
	public boolean microphoneIsAvailable() {
		return soundAcquisitionTask.microphoneIsAvailable();
	}
	//
	public boolean microphoneIsActive() {
		if (!isAlive()) {
			return false;
		};
		return soundAcquisitionTask.microphoneIsActive();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void start() {
		if (!isAlive()) {
			soundAcquisitionTask.startDataTransfer();
			super.start();
		}
	}
	//
	public static int getBufferSize() {
		return bufferSize;
	}
	public static void setBufferSize(int size) {
		bufferSize= size;
	}
	//
	public void setDataConsumer(AudioDataConsumerInterface panel) {
		controlPanel.set(panel);
		soundAcquisitionTask.setDataConsumer(panel);
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
		if (!isAlive()) {
			start();
		};
		soundAcquisitionTask.startDataTransfer();
	}
	//
	public void stopDataTransfer() {
		soundAcquisitionTask.stopDataTransfer();
	}
	//
	@SuppressWarnings("CallToThreadDumpStack")
	public void flush(boolean synchronizeAudioSystem, boolean forgetAudioFormat) {
		soundAcquisitionTask.flush();
		try {
			int availableBytes;
			synchronized (pipedInputStream) {
				availableBytes= pipedInputStream.available();
			};
			ingoreBytes.set(availableBytes);
			if (synchronizeAudioSystem) {
				suspendDataAcquisition.set(true);
				try {
					synchronized (this) {
						AudioDataConsumerInterface panel= controlPanel.get();
						if (panel != null) {
							panel.implementAudioSystemReset(forgetAudioFormat);
						}
					}
				} finally {
					suspendDataAcquisition.set(false);
				}
			} else if (forgetAudioFormat) {
				suspendDataAcquisition.set(true);
				try {
					synchronized (this) {
						AudioDataConsumerInterface panel= controlPanel.get();
						if (panel != null) {
							panel.implementAudioFormatReset();
						}
					}
				} finally {
					suspendDataAcquisition.set(false);
				}
			}
		} catch (IOException e) {
			if (reportCriticalErrors()) {
				e.printStackTrace();
				writeLater(String.format("SoundFramingTask:IOException:%s\n",e));
			}
		} catch (Throwable e) {
			if (reportCriticalErrors()) {
				e.printStackTrace();
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
				if (!suspendDataAcquisition.get()) {
					synchronized (this) {
						dataAcquisition();
					}
				}
			}
		} catch (InterruptedException e) {
		} catch (ThreadDeath e) {
		} catch (IOException e) {
			if (reportCriticalErrors()) {
				e.printStackTrace();
				writeLater(String.format("SoundFramingTask:IOException:%s\n",e));
			}
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
		return soundAcquisitionTask.deviceDoesExist();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void dataAcquisition() throws InterruptedException, IOException {
		int ignoreBytes= ingoreBytes.get();
		if (ignoreBytes > 0) {
			ingoreBytes.set(0);
			for (int n=1; n <= ignoreBytes; n++) {
				synchronized (pipedInputStream) {
					pipedInputStream.read();
				}
			}
		};
		int availableBytes;
		synchronized (pipedInputStream) {
			availableBytes= pipedInputStream.available();
		};
		if (availableBytes >= bufferSize) {
			if (	m_buf==null ||
				m_buf.length != bufferSize) {
				m_buf= new byte[bufferSize];
			} else {
				Arrays.fill(m_buf,(byte)0);
			};
			long currentTime= System.currentTimeMillis();
			synchronized (pipedInputStream) {
				pipedInputStream.read(m_buf,0,bufferSize);
			};
			AudioDataConsumerInterface panel= controlPanel.get();
			if (panel != null) {
				panel.setAudioData(m_buf,currentTime);
			}
		} else {
			if (availableBytes - bufferSize < bufferSize) {
				synchronized (pipedInputStream) {
					pipedInputStream.wait(emergencyTimeout);
				}
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
