// (c) 2018 Alexei A. Morozov

package morozov.system.sound;

import morozov.system.sound.interfaces.*;

import javax.swing.SwingUtilities;
import java.io.PipedInputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SoundFramingTask extends Thread {
	//
	public static int bufferSize= 1152*100;
	//
	protected int inputStreamBufferSize= bufferSize*5;
	protected PipedInputStream pipedInputStream= new PipedInputStream(inputStreamBufferSize);
	protected SoundAcquisitionTask soundAcquisitionTask= new SoundAcquisitionTask(pipedInputStream,inputStreamBufferSize);
	//
	protected AtomicReference<AudioDataConsumerInterface> controlPanel= new AtomicReference<>();
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
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
		// start();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void start() {
		soundAcquisitionTask.start();
		super.start();
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
		soundAcquisitionTask.startDataTransfer();
	}
	//
	public void stopDataTransfer() {
		soundAcquisitionTask.stopDataTransfer();
	}
	//
	public void flush() {
		soundAcquisitionTask.flush();
		try {
			int availableBytes= pipedInputStream.available();
			ingoreBytes.set(availableBytes);
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
	public void run() {
		try {
			while (!stopThisThread.get()) {
				dataAcquisition();
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
				pipedInputStream.read();
			}
		};
		int availableBytes= pipedInputStream.available();
		if (availableBytes >= bufferSize) {
			if (	m_buf==null ||
				m_buf.length != bufferSize) {
				m_buf= new byte[bufferSize];
			} else {
				Arrays.fill(m_buf,(byte)0);
			};
			long currentTime= System.currentTimeMillis();
			pipedInputStream.read(m_buf,0,bufferSize);
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
