// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.frames.converters;

import morozov.system.datum.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.frames.converters.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DataFrameRecordingTask extends Thread {
	//
	protected DataFrameProviderInterface dataProvider;
	//
	protected Object objectStreamGuard= new Object();
	protected AtomicBoolean acceptFrames= new AtomicBoolean(false);
	protected AtomicBoolean recordFrames= new AtomicBoolean(false);
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected LinkedList<Object> history= new LinkedList<>();
	protected int defaultWriteBufferSize= 100;
	protected AtomicInteger writeBufferSize= new AtomicInteger(defaultWriteBufferSize);
	protected boolean bufferOverflow= false;
	//
	protected ExtendedFileName extendedFileName;
	protected Path outputFilePath;
	protected OutputStream outputStream;
	protected OutputStream bufferedOutputStream;
	protected ObjectOutputStream objectOutputStream;
	//
	protected long frameNumber;
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	//
	protected static final long emergencyTimeout= 1000;
	//
	///////////////////////////////////////////////////////////////
	//
	public DataFrameRecordingTask() {
		setDaemon(true);
		setPriority(Thread.MAX_PRIORITY);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDataProvider(DataFrameProviderInterface provider) {
		dataProvider= provider;
	}
	//
	public int getWriteBufferSize() {
		return writeBufferSize.get();
	}
	public void setWriteBufferSize(int length) {
		writeBufferSize.set(length);
	}
	public void resetWriteBufferSize() {
		writeBufferSize.set(defaultWriteBufferSize);
	}
	//
	public int getOutputDebugInformation() {
		return outputDebugInformation.get();
	}
	public void setOutputDebugInformation(int value) {
		outputDebugInformation.set(value);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void reset(ExtendedFileName fileName) {
		close();
		startProcessIfNecessary();
		try {
			synchronized (objectStreamGuard) {
				acceptFrames.set(false);
				recordFrames.set(false);
				extendedFileName= fileName;
				outputFilePath= extendedFileName.getPathOfLocalResource();
				outputStream= Files.newOutputStream(outputFilePath);
				bufferedOutputStream= new BufferedOutputStream(outputStream);
				objectOutputStream= new DataStoreOutputStream(bufferedOutputStream);
				recordFrames.set(true);
				acceptFrames.set(true);
			}
		} catch (IOException e) {
			throw new FileInputOutputError(outputFilePath.toString(),e);
		}
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
	@SuppressWarnings("CallToThreadDumpStack")
	public void close() {
		acceptFrames.set(false);
		try {
			while (true) {
				synchronized (history) {
					if (history.isEmpty() || !recordFrames.get()) {
						frameNumber= 0;
						break;
					} else {
						history.wait(emergencyTimeout);
					}
				}
			}
		} catch (InterruptedException e) {
		} catch (ThreadDeath e) {
		} catch (Throwable e) {
			if (reportCriticalErrors()) {
				e.printStackTrace();
			}
		};
		recordFrames.set(false);
		try {
			synchronized (objectStreamGuard) {
				if (objectOutputStream != null) {
					objectOutputStream.close();
					objectOutputStream= null;
				};
				if (outputStream != null) {
					outputStream.close();
					outputStream= null;
				};
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
					bufferedOutputStream= null;
				}
			}
		} catch (IOException e) {
			throw new FileInputOutputError(outputFilePath.toString(),e);
		}
	}
	//
	public boolean outputIsOpen() {
		return acceptFrames.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void store(Object frame) {
		if (acceptFrames.get()) {
			synchronized (history) {
				history.addFirst(frame);
				history.notifyAll();
			}
		}
	}
	//
	@Override
	@SuppressWarnings("CallToThreadDumpStack")
	public void run() {
		while (true) {
			try {
				synchronized (history) {
					if (history.isEmpty() || !recordFrames.get()) {
						history.wait(emergencyTimeout);
					}
				};
				recordFrames();
			} catch (InterruptedException e) {
			} catch (ThreadDeath e) {
				return;
			} catch (Throwable e) {
				if (reportCriticalErrors()) {
					e.printStackTrace();
				}
			}
		}
	}
	//
	protected void recordFrames() throws IOException {
		while (recordFrames.get()) {
			int length;
			Object currentFrame;
			synchronized (history) {
				length= history.size();
				if (length > 0) {
					currentFrame= history.getLast();
				} else {
					break;
				}
			};
			frameNumber++;
			try {
				synchronized (objectStreamGuard) {
					objectOutputStream.writeObject(currentFrame);
					objectOutputStream.reset();
				}
			} catch (Throwable e) {
				recordFrames.set(false);
				close();
				dataProvider.completeDataWriting(frameNumber,e);
				throw e;
			};
			if (reportWarnings()) {
				System.err.printf("RECORDED:Number:%s,Queue:%s\n",frameNumber,length);
			};
			synchronized (history) {
				length= history.size();
				if (length > 0) {
					try {
						history.removeLast();
						length= history.size();
					} finally {
						history.notifyAll();
					};
					int superfluousLength= length - writeBufferSize.get();
					if (superfluousLength > 0) {
						bufferOverflow= true;
						if (dataProvider != null) {
							dataProvider.reportBufferOverflow();
						};
						Iterator<Object> iteratorHistory= history.iterator();
						int step= length / superfluousLength;
						int currentLength= superfluousLength;
						int quantityOfRemovedItems= 0;
						int auxiliaryCounter= step;
						try {
///////////////////////////////////////////////////////////////////////
while (iteratorHistory.hasNext() && currentLength > 0) {
	Object previousFrame= iteratorHistory.next();
	if (!isLightweightFrame(previousFrame)) {
		if (auxiliaryCounter >= step) {
			auxiliaryCounter= 0;
			iteratorHistory.remove();
			if (reportAdmissibleErrors()) {
				System.err.printf("Warning: frame %s is lost.\n",previousFrame);
			};
			currentLength--;
			quantityOfRemovedItems++;
		} else {
			auxiliaryCounter++;
		}
	}
}
///////////////////////////////////////////////////////////////////////
						} finally {
							history.notifyAll();
						}
						if (reportAdmissibleErrors()) {
							System.err.printf("Warning: Buffer overflow (%s); %s frames are lost.\n",length,quantityOfRemovedItems);
						}
					} else {
						if (bufferOverflow) {
							bufferOverflow= false;
							if (dataProvider != null) {
								dataProvider.annulBufferOverflow();
							}
						}
					}
				} else {
					break;
				}
			}
		}
	}
	//
	protected boolean isLightweightFrame(Object frame) {
		boolean answer= false;
		if (frame instanceof DataFrameInterface) {
			answer= ((DataFrameInterface)frame).isLightweightFrame();
		} else if (frame instanceof CompoundFrameInterface ) {
			answer= ((CompoundFrameInterface)frame).isLightweightFrame();
		} else if (frame instanceof KinectFrameInterface ) {
			answer= ((KinectFrameInterface)frame).isLightweightFrame();
		};
		return answer;
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
