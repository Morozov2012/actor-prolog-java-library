// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1;

import morozov.system.*;
import morozov.system.i3v1.interfaces.*;

import javax.swing.SwingUtilities;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class I3DataAcquisition extends Thread {
	//
	protected I3Camera camera;
	//
	protected AtomicReference<I3DataConsumerInterface> dataConsumer= new AtomicReference<>();
	protected AtomicReference<DeviceIdentifier> requestedIdentifier= new AtomicReference<>(DeviceIdentifier.DEFAULT);
	protected AtomicReference<DeviceIdentifier> currentIdentifier= new AtomicReference<>(DeviceIdentifier.DEFAULT);
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected AtomicBoolean deviceIsConnected= new AtomicBoolean(false);
	//
	protected AtomicBoolean doNotSuspendUSBDataTransfer= new AtomicBoolean(true);
	protected AtomicBoolean enableDataTransfer= new AtomicBoolean(false);
	protected AtomicBoolean deviceIsToBeClosed= new AtomicBoolean(false);
	//
	protected byte[] m_buf;
	//
	protected AtomicLong deviceConnectionAttemptPeriod= new AtomicLong(100);
	protected AtomicInteger maximalNumberOfErrors= new AtomicInteger(30);
	protected AtomicInteger readTimeOut= new AtomicInteger(70);
	protected AtomicInteger writeTimeOut= new AtomicInteger(70);
	// protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected AtomicInteger numberOfErrors= new AtomicInteger(0);
	//
	protected static String emptyDeviceName= "";
	//
	///////////////////////////////////////////////////////////////
	//
	public I3DataAcquisition(I3Camera c) {
		camera= c;
		setDaemon(true);
		start();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDataConsumer(I3DataConsumerInterface consumer) {
		dataConsumer.set(consumer);
		camera.setDataConsumer(consumer);
	}
	//
	public void setDoNotSuspendUSBDataTransfer(boolean value) {
		doNotSuspendUSBDataTransfer.set(value);
	}
	public boolean getDoNotSuspendUSBDataTransfer() {
		return doNotSuspendUSBDataTransfer.get();
	}
	//
	public void setDeviceConnectionAttemptPeriod(long value) {
		deviceConnectionAttemptPeriod.set(value);
	}
	public long getDeviceConnectionAttemptPeriod() {
		return deviceConnectionAttemptPeriod.get();
	}
	//
	public void setMaximalNumberOfErrors(int value) {
		maximalNumberOfErrors.set(value);
	}
	public int getMaximalNumberOfErrors() {
		return maximalNumberOfErrors.get();
	}
	//
	public void setReadTimeOut(int value) {
		camera.setReadTimeOut(value);
	}
	public int getReadTimeOut() {
		return camera.getReadTimeOut();
	}
	//
	public void setWriteTimeOut(int value) {
		camera.setWriteTimeOut(value);
	}
	public int getWriteTimeOut() {
		return camera.getWriteTimeOut();
	}
	//
	public void setOutputDebugInformation(int value) {
		camera.setOutputDebugInformation(value);
	}
	public int getOutputDebugInformation() {
		return camera.getOutputDebugInformation();
	}
	//
	public boolean connectDevice(DeviceIdentifier identifier, long period, int maximalErrorsQuantity) {
		if (!deviceIsConnected.get()) {
			requestedIdentifier.set(identifier);
			deviceConnectionAttemptPeriod.set(period);
			maximalNumberOfErrors.set(maximalErrorsQuantity);
			try {
				tryToConnectDevice();
			} catch (Throwable e1) {
				if (camera.reportAdmissibleErrors()) {
					writeLater(String.format("Camera initialization error: %s\n",e1));
				};
				try {
					camera.disconnectDevice();
					if (camera.reportWarnings()) {
						writeLater("Device is closed!\n");
					}
				} catch (Throwable e2) {
				};
				deviceIsConnected.set(false);
			}
		};
		return deviceIsConnected.get();
	}
	//
	public String getCurrentDeviceIdentifier() {
		DeviceIdentifier identifier= currentIdentifier.get();
		if (identifier==null) {
			return emptyDeviceName;
		} else {
			return identifier.getNameOrDefault(emptyDeviceName);
		}
	}
	//
	public void activateDataTransfer(DeviceIdentifier identifier, long period, int maximalErrorsQuantity, int givenReadTimeOut, int givenWriteTimeOut, int givenOutputDebugInformation) {
		synchronized (this) {
			requestedIdentifier.set(identifier);
			deviceConnectionAttemptPeriod.set(period);
			maximalNumberOfErrors.set(maximalErrorsQuantity);
			readTimeOut.set(givenReadTimeOut);
			writeTimeOut.set(givenWriteTimeOut);
			camera.setOutputDebugInformation(givenOutputDebugInformation);
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
			if (!doNotSuspendUSBDataTransfer.get()) {
				deviceIsToBeClosed.set(true);
			};
			notify();
		}
	}
	//
	public void stopDataTransferAndExitProgram() {
		synchronized (this) {
			stopThisThread.set(true);
			enableDataTransfer.set(false);
			deviceIsToBeClosed.set(true);
			notify();
		}
	}
	//
	public boolean isConnected() {
		return deviceIsConnected.get();
	}
	//
	public boolean isSuspended() {
		return !enableDataTransfer.get();
	}
	//
	public boolean isNotSuspended() {
		return enableDataTransfer.get();
	}
	//
	public void disconnectDevice() {
		while (deviceIsConnected.get()) {
			if (stopThisThread.get()) {
				return;
			};
			synchronized (this) {
				enableDataTransfer.set(false);
				deviceIsToBeClosed.set(true);
				notify();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void run() {
		try {
			while (!stopThisThread.get()) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
synchronized (this) {
	if (	!enableDataTransfer.get() &&
		!deviceIsToBeClosed.get()) {
		wait();
		continue;
	}
};
if (!deviceIsToBeClosed.get()) {
	if (deviceIsConnected.get()) {
		try {
			dataAcquisition();
		} catch (Throwable e) {
			if (camera.reportAdmissibleErrors()) {
				e.printStackTrace();
				writeLater(String.format("Data acquisition error: %s\n",e));
			}
		}
	} else {
		try {
			tryToConnectDevice();
		} catch (Throwable e1) {
			if (camera.reportAdmissibleErrors()) {
				writeLater(String.format("Camera initialization error: %s\n",e1));
			};
			try {
				camera.disconnectDevice();
				if (camera.reportWarnings()) {
					writeLater("Device is closed!\n");
				}
			} catch (Throwable e2) {
			};
			deviceIsConnected.set(false);
		};
		if (!deviceIsConnected.get()) {
			synchronized (this) {
				wait(deviceConnectionAttemptPeriod.get());
			}
		}
	}
} else {
	try {
		camera.disconnectDevice();
		if (camera.reportWarnings()) {
			writeLater("Device is closed!\n");
		};
		deviceIsConnected.set(false);
		deviceIsToBeClosed.set(false);
	} catch (Throwable e) {
		if (camera.reportAdmissibleErrors()) {
			writeLater(String.format("Data acquisition error: %s\n",e));
			e.printStackTrace();
		}
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			};
			try {
				if (deviceIsConnected.get()) {
					camera.disconnectDevice();
					if (camera.reportWarnings()) {
						writeLater("Device is closed!\n");
					}
				}
			} finally {
				camera.closeUSB();
				deviceIsConnected.set(false);
			};
			// writeLater("Bye!\n");
			// System.exit(0);
		} catch (InterruptedException e) {
		} catch (ThreadDeath e) {
		} catch (Throwable e) {
			if (camera.reportAdmissibleErrors()) {
				e.printStackTrace();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean deviceDoesExist(DeviceIdentifier identifier) {
		requestedIdentifier.set(identifier);
		synchronized (this) {
			if (deviceIsConnected.get()) {
				return true;
			} else {
				return camera.findDevice(requestedIdentifier.get());
			}
		}
	}
	//
	protected void tryToConnectDevice() {
		synchronized (this) {
			if (!deviceIsConnected.get()) {
				DeviceIdentifier identifier= requestedIdentifier.get();
				boolean result1= camera.findDevice(identifier);
				if (!result1) {
					return;
				};
				camera.setReadTimeOut(readTimeOut.get());
				camera.setWriteTimeOut(writeTimeOut.get());
				// camera.setOutputDebugInformation(outputDebugInformation.get());
				camera.connectDevice();
				camera.initCamera();
				if (m_buf==null) {
					m_buf= camera.createBuffer();
				};
				currentIdentifier.set(identifier);
				deviceIsConnected.set(true);
				if (camera.reportWarnings()) {
					writeLater("Device is open!\n");
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void dataAcquisition() {
		int numberOfReceivedBytes= camera.read(m_buf,m_buf.length);
		long currentTime= System.currentTimeMillis();
		I3DataConsumerInterface consumer= dataConsumer.get();
		if (consumer != null) {
			if (numberOfReceivedBytes == m_buf.length) {
				consumer.setByteThermalData(m_buf,currentTime);
				numberOfErrors.set(0);
			} else {
				if (camera.reportAdmissibleErrors()) {
					writeLater(String.format("?! numberOfReceivedBytes (%s) <> m_buf.length (%s)\n",numberOfReceivedBytes,m_buf.length));
				};
				consumer.reportMissedFrame(currentTime);
				int errorNumber= numberOfErrors.incrementAndGet();
				if (errorNumber > maximalNumberOfErrors.get()) {
					try {
						camera.disconnectDevice();
						if (camera.reportWarnings()) {
							writeLater("Device is closed!\n");
						}
					} catch (Throwable e2) {
					};
					deviceIsConnected.set(false);
					// tryToConnectDevice();
					numberOfErrors.set(0);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static void writeLater(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.err.print(text);
			}
		});
	}
}
