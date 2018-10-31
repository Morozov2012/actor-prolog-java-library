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
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected AtomicInteger ingoreBytes= new AtomicInteger(0);
	//
	protected byte[] m_buf;
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
	public void setControlPanel(AudioDataConsumerInterface panel) {
		controlPanel.set(panel);
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
			writeLater(String.format("SoundFramingTask:IOException:%s\n",e));
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
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
			writeLater(String.format("SoundFramingTask:IOException:%s\n",e));
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
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
					pipedInputStream.wait();
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
}
