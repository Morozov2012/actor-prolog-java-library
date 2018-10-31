// (c) 2018 Alexei A. Morozov

package morozov.system.ip_camera;

import morozov.system.errors.*;
import morozov.system.ip_camera.errors.*;
import morozov.system.ip_camera.interfaces.*;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class IPCameraDataAcquisition extends Thread {
	//
	protected IPCameraDataConsumerInterface dataConsumer;
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected AtomicBoolean deviceIsOpen= new AtomicBoolean(false);
	//
	protected AtomicBoolean enableDataTransfer= new AtomicBoolean(false);
	protected AtomicBoolean deviceIsToBeClosed= new AtomicBoolean(false);
	//
	// protected String mjpgURL= "http://192.168.10.230/mjpg/video.mjpg";
	// protected String mjpgURL= "http://localhost:8081";;
	protected AtomicReference<String> mjpgURL= new AtomicReference<>();
	//
	protected URL url;
	protected HttpURLConnection connection;
	protected InputStream httpInputStream;
	protected BufferedInputStream bufferedInputStream;
	protected DataInputStream input;
	//
	protected byte[] m_buf;
	//
	protected AtomicInteger deviceOpeningAttemptDelay= new AtomicInteger(100);
	//
	protected byte[] lineByteBuffer= new byte[1024];
	protected int lineByteBufferPosition= 0;
	protected byte[] textContentLength= new byte[]{'C','o','n','t','e','n','t','-','L','e','n','g','t','h',':',' '};
	//
	///////////////////////////////////////////////////////////////
	//
	public IPCameraDataAcquisition() {
		setDaemon(true);
		start();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDataConsumer(IPCameraDataConsumerInterface consumer) {
		dataConsumer= consumer;
	}
	//
	public void setServerAttributes(String url, int delay) {
		synchronized (this) {
			mjpgURL.set(url);
			deviceOpeningAttemptDelay.set(delay);
		}
	}
	//
	public String getURL() {
		return mjpgURL.get();
	}
	public int getOpeningAttemptDelay() {
		return deviceOpeningAttemptDelay.get();
	}
	//
	public void activateDataTransfer() {
		synchronized (this) {
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
	public boolean isNotSuspended() {
		return enableDataTransfer.get();
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
						if (!deviceIsToBeClosed.get()) {
							dataAcquisition();
						} else {
							closeDevice();
							deviceIsOpen.set(false);
							deviceIsToBeClosed.set(false);
						}
					} catch (Throwable e) {
						if (dataConsumer != null) {
							dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e));
						};
						e.printStackTrace();
						closeDevice();
						deviceIsOpen.set(false);
						writeLater(String.format("IP camera acquisition error: %s\n",e));
					}
				} else {
					try {
                	                	tryToOpenDevice();
					} catch (Throwable e1) {
						if (dataConsumer != null) {
							dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e1));
						};
						e1.printStackTrace();
						writeLater(String.format("IP camera initialization error: %s\n",e1));
						try {
							closeDevice();
						} catch (Throwable e2) {
							if (dataConsumer != null) {
								dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e2));
							}
						};
						deviceIsOpen.set(false);
					};
					if (!deviceIsOpen.get()) {
						synchronized (this) {
							wait(deviceOpeningAttemptDelay.get());
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
			if (dataConsumer != null) {
				dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e));
			};
			e.printStackTrace();
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
					String givenURL= mjpgURL.get();
					url= new URL(givenURL);
					connection= (HttpURLConnection)url.openConnection();
					try {
						String type= connection.getContentType();
						if (type != null) {
							return true;
						}
					} finally {
						connection.disconnect();
					}
				} catch (Throwable e) {
					if (dataConsumer != null) {
						dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e));
					};
					e.printStackTrace();
				};
				return false;
			}
		}
	}
	//
	public void tryToOpenDevice() throws UnknownHostException, IOException {
		String givenURL= mjpgURL.get();
		synchronized (this) {
			if (!deviceIsOpen.get()) {
				url= new URL(givenURL);
				connection= (HttpURLConnection)url.openConnection();
				String type= connection.getContentType();
				if (type != null) {
					httpInputStream= connection.getInputStream();
					bufferedInputStream= new BufferedInputStream(httpInputStream);
					input= new DataInputStream(bufferedInputStream);
					deviceIsOpen.set(true);
				} else {
					throw new UnknownHTTPContentType();
				}
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
			if (dataConsumer != null) {
				dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e));
			}
		};
		try {
			if (bufferedInputStream != null) {
				bufferedInputStream.close();
				bufferedInputStream= null;
			}
		} catch (Throwable e) {
			if (dataConsumer != null) {
				dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e));
			}
		};
		try {
			if (httpInputStream != null) {
				httpInputStream.close();
				httpInputStream= null;
			}
		} catch (Throwable e) {
			if (dataConsumer != null) {
				dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e));
			}
		};
		try {
			if (connection != null) {
				connection.disconnect();
				connection= null;
			}
		} catch (Throwable e) {
			if (dataConsumer != null) {
				dataConsumer.setDataAcquisitionError(new DataAcquisitionError(e));
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void dataAcquisition() throws IOException {
		int contentLength= readHeader();
		if (contentLength > 0) {
			long currentTime= System.currentTimeMillis();
			readDummyLines();
			byte[] byteArray= readImage(contentLength);
			if (byteArray != null) {
				if (dataConsumer != null) {
					dataConsumer.setIPCameraData(byteArray,currentTime);
				}
			}
		}
	}
	//
	protected int readHeader() throws IOException {
		MainLoop: while (true) {
			boolean end= false;
			lineByteBufferPosition= 0;
			int firstLetterPosition= -1;
			int lastLetterPosition= -1;
			while (!end) {
				if (lineByteBufferPosition >= lineByteBuffer.length) {
					break;
				};
				input.readFully(lineByteBuffer,lineByteBufferPosition,1);
				int currentCharacter= (byte)lineByteBuffer[lineByteBufferPosition] & 0x00FF;
				if (currentCharacter==0x0A) {
					end= true;
				};
				if (currentCharacter >= '0' && currentCharacter <= '9') {
					if (firstLetterPosition < 0) {
						firstLetterPosition= lineByteBufferPosition;
					};
					lastLetterPosition= lineByteBufferPosition;
				};
				lineByteBufferPosition++;
			};
			if (lineByteBufferPosition < textContentLength.length) {
				continue MainLoop;
			};
			for (int k=0; k < textContentLength.length; k++) {
				if (lineByteBuffer[k] != textContentLength[k]) {
					continue MainLoop;
				}
			};
			if (firstLetterPosition < 0) {
				continue MainLoop;
			};
			String textNumber= new String(lineByteBuffer,firstLetterPosition,lastLetterPosition-firstLetterPosition+1);
			int contentLength= Integer.valueOf(textNumber);
			return contentLength;
		}
	}
	//
	protected void readDummyLines() throws IOException {
		int lineLength= 0;
		while (true) {
			input.readFully(lineByteBuffer,0,1);
			int currentCharacter= (int)lineByteBuffer[0] & 0x00FF;
			if (currentCharacter==0x0A) {
				if (lineLength==0) {
					return;
				} else {
					lineLength= 0;
				}
			} else if (currentCharacter==0x0D) {
			} else {
				lineLength++;
			}
		}
	}
	//
	protected byte[] readImage(int contentLength) throws IOException {
		byte[] imageByteBuffer= new byte[contentLength];
		input.readFully(imageByteBuffer,0,contentLength);
		return imageByteBuffer;
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
	///////////////////////////////////////////////////////////////
	//
	public static java.awt.image.BufferedImage byteArrayToBufferedImage(byte[] byteArray) throws IOException {
		if (byteArray != null) {
			ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(byteArray);
			MemoryCacheImageInputStream memoryCacheImageInputStream= new MemoryCacheImageInputStream(byteArrayInputStream);
			java.awt.image.BufferedImage nativeImage= ImageIO.read(memoryCacheImageInputStream);
			return nativeImage;
		} else {
			return null;
		}
	}
}
