// (c) 2018 Alexei A. Morozov

package morozov.system.astrohn;

import morozov.system.astrohn.frames.data.*;
import morozov.system.astrohn.frames.data.errors.*;
import morozov.system.astrohn.interfaces.*;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.swing.SwingUtilities;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AstrohnDataAcquisition extends Thread {
	//
	protected AtomicReference<AstrohnDataConsumerInterface> dataConsumer= new AtomicReference<>();
	//
	protected AtomicReference<String> serverAddress= new AtomicReference<>("192.168.10.227");
	protected AtomicInteger serverPort= new AtomicInteger(41865);
	protected AtomicInteger deviceConnectionAttemptPeriod= new AtomicInteger(100);
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected AtomicBoolean deviceIsOpen= new AtomicBoolean(false);
	//
	protected AtomicBoolean enableDataTransfer= new AtomicBoolean(false);
	protected AtomicBoolean deviceIsToBeClosed= new AtomicBoolean(false);
	//
	protected Socket socket;
	protected InputStream socketInputStream;
	protected BufferedInputStream bufferedInputStream;
	protected DataInputStream input;
	//
	protected byte[] m_buf;
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	//
	///////////////////////////////////////////////////////////////
	//
	public AstrohnDataAcquisition() {
		setDaemon(true);
		start();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDataConsumer(AstrohnDataConsumerInterface consumer) {
		dataConsumer.set(consumer);
	}
	//
	public void setServerAttributes(String address, int port, int delay) {
		synchronized (this) {
			serverAddress.set(address);
			serverPort.set(port);
			deviceConnectionAttemptPeriod.set(delay);
		}
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
	public void activateDataTransfer(int givenOutputDebugInformation) {
		synchronized (this) {
			outputDebugInformation.set(givenOutputDebugInformation);
			enableDataTransfer.set(true);
			deviceIsToBeClosed.set(false);
			notify();
		}
	}
	//
	public void suspendDataTransfer() {
		synchronized (this) {
			enableDataTransfer.set(false);
			notify();
		}
	}
	//
	public void stopDataTransfer() {
		synchronized (this) {
			enableDataTransfer.set(false);
			deviceIsToBeClosed.set(true);
			notify();
		}
	}
	//
	public boolean isSuspended() {
		return !enableDataTransfer.get();
	}
	//
	// public boolean isNotSuspended() {
	//	return enableDataTransfer.get();
	// }
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
						if (!deviceIsToBeClosed.get()) {
							dataAcquisition();
						} else {
							closeDevice();
							deviceIsOpen.set(false);
							deviceIsToBeClosed.set(false);
						}
					} catch (Throwable e) {
						if (reportCriticalErrors()) {
							e.printStackTrace();
							writeLater(String.format("THz data acquisition error: %s\n",e));
						}
					}
				} else {
					try {
                	                	tryToOpenDevice();
					} catch (Throwable e1) {
						if (reportAdmissibleErrors()) {
							e1.printStackTrace();
							writeLater(String.format("THz camera initialization error: %s\n",e1));
						};
						try {
							closeDevice();
						} catch (Throwable e2) {
						};
						deviceIsOpen.set(false);
					};
					if (!deviceIsOpen.get()) {
						synchronized (this) {
							wait(deviceConnectionAttemptPeriod.get());
						}
					}
				}
			};
			try {
				if (deviceIsOpen.get()) {
					closeDevice();
				}
			} finally {
				deviceIsOpen.set(false);
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
		synchronized (this) {
			if (deviceIsOpen.get()) {
				return true;
			} else {
				try {
					InetAddress ipAddress= InetAddress.getByName(serverAddress.get());
					Socket socket= new Socket(ipAddress,serverPort.get());
					socket.close();
					return true;
				} catch (Throwable e) {
				};
				return false;
			}
		}
	}
	//
	public void tryToOpenDevice() throws UnknownHostException, IOException {
		synchronized (this) {
			if (!deviceIsOpen.get()) {
				InetAddress ipAddress= InetAddress.getByName(serverAddress.get());
				socket= new Socket(ipAddress,serverPort.get());
				socketInputStream= socket.getInputStream();
				bufferedInputStream= new BufferedInputStream(socketInputStream);
				input= new DataInputStream(bufferedInputStream);
				deviceIsOpen.set(true);
			}
		}
	}
	//
	protected void closeDevice() {
		try {
			if (input != null) {
				input.close();
				input= null;
			}
		} catch (Throwable e) {
		};
		try {
			if (bufferedInputStream != null) {
				bufferedInputStream.close();
				bufferedInputStream= null;
			}
		} catch (Throwable e) {
		};
		try {
			if (socketInputStream != null) {
				socketInputStream.close();
				socketInputStream= null;
			}
		} catch (Throwable e) {
		};
		try {
			if (socket != null) {
				socket.close();
				socket= null;
			}
		} catch (Throwable e) {
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void dataAcquisition() throws IOException {
		int lengthP= PacketHeader.getLength(input);
		if (lengthP > 0) {
			long currentTime= System.currentTimeMillis();
			boolean reportWarnings= reportWarnings();
			TVFilterImageHeader packetBody=
				TVFilterImageHeader.readTVFilterImageHeader(input,lengthP,reportWarnings);
			AstrohnDataConsumerInterface consumer= dataConsumer.get();
			if (consumer != null) {
				consumer.setAstrohnData(packetBody,currentTime);
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
	//
	///////////////////////////////////////////////////////////////
	//
	public static double[] tvFilterImageHeaderToDouble(TVFilterImageHeader terahertzPacketBody) {
		double[] dataBuffer;
		switch (terahertzPacketBody.getFormat()) {
		case 2:
			int columns= terahertzPacketBody.getColumns();
			int rows= terahertzPacketBody.getRows();
			int imageSize= columns * rows;
			dataBuffer= new double[imageSize];
			TVVidImage image= terahertzPacketBody.getImage();
			byte[] imageData= image.getImageData();
			for (int k=0; k < imageSize; k++) {
				byte a= imageData[k*2];
				byte b= imageData[k*2+1];
				int integerValue= ((b << 8) & 0xff00) | (a & 0xff);
				dataBuffer[k]= (double)integerValue;
			};
			break;
		default:
			throw new UnknownImageFormat(terahertzPacketBody.getFormat());
		};
		return dataBuffer;
	}
}
