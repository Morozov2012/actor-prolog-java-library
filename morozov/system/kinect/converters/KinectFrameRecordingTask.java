// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters;

import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.interfaces.*;

import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
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

public class KinectFrameRecordingTask extends Thread {
	//
	protected KinectBufferInterface owner;
	//
	protected Object objectStreamGuard= new Object();
	protected AtomicBoolean acceptFrames= new AtomicBoolean(false);
	protected AtomicBoolean recordFrames= new AtomicBoolean(false);
	//
	protected LinkedList<KinectFrameInterface> history= new LinkedList<>();
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
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	public KinectFrameRecordingTask(KinectBufferInterface kinectBuffer) {
		owner= kinectBuffer;
		setDaemon(true);
		setPriority(Thread.MAX_PRIORITY);
		start();
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
	public void reset(ExtendedFileName fileName) {
		close();
		try {
			synchronized (objectStreamGuard) {
				acceptFrames.set(false);
				recordFrames.set(false);
				extendedFileName= fileName;
				outputFilePath= extendedFileName.getPathOfLocalResource();
				outputStream= Files.newOutputStream(outputFilePath);
				bufferedOutputStream= new BufferedOutputStream(outputStream);
				objectOutputStream= new ObjectOutputStream(bufferedOutputStream);
				recordFrames.set(true);
				acceptFrames.set(true);
			}
		} catch (IOException e) {
			throw new FileInputOutputError(outputFilePath.toString(),e);
		}
	}
	public void close() {
		acceptFrames.set(false);
		try {
			while (true) {
				synchronized (history) {
					if (history.isEmpty()) {
						frameNumber= 0;
						break;
					} else {
						history.wait();
					}
				}
			}
		} catch (InterruptedException e) {
		} catch (ThreadDeath e) {
		} catch (Throwable e) {
			e.printStackTrace();
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
	public void store(KinectFrameInterface frame) {
		if (acceptFrames.get()) {
			synchronized (history) {
				history.addFirst(frame);
				history.notify();
			}
		}
	}
	//
	public void run() {
		while (true) {
			try {
				synchronized (history) {
					if (history.isEmpty()) {
						history.wait();
					}
				};
				recordFrames();
			} catch (InterruptedException e) {
			} catch (ThreadDeath e) {
				return;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	//
	protected void recordFrames() {
		while (recordFrames.get()) {
			frameNumber++;
			int length;
			KinectFrameInterface currentFrame;
			synchronized (history) {
				length= history.size();
				if (length > 0) {
					currentFrame= history.getLast();
				} else {
					break;
				}
			};
			try {
				synchronized (objectStreamGuard) {
					objectOutputStream.writeObject(currentFrame);
					objectOutputStream.reset();
				}
			} catch (IOException e) {
				throw new FileInputOutputError(outputFilePath.toString(),e);
			};
System.err.printf("RECORDED:Number:%s,Type:%s;Queue:%s\n",frameNumber,currentFrame.getDataArrayType(),length);
			synchronized (history) {
				length= history.size();
				if (length > 0) {
					try {
						history.removeLast();
						length= history.size();
					} finally {
						history.notify();
					};
					int superfluousLength= length - writeBufferSize.get();
					if (superfluousLength > 0) {
						bufferOverflow= true;
						owner.reportBufferOverflow();
						Iterator<KinectFrameInterface> iteratorHistory= history.iterator();
						int step= length / superfluousLength;
						int currentLength= superfluousLength;
						int quantityOfRemovedItems= 0;
						int auxiliaryCounter= step;
						try {
///////////////////////////////////////////////////////////////////////
while (iteratorHistory.hasNext() && currentLength > 0) {
	KinectFrameInterface previousFrame= iteratorHistory.next();
	if (!previousFrame.isLightweightFrame()) {
		if (auxiliaryCounter >= step) {
			auxiliaryCounter= 0;
			iteratorHistory.remove();
System.err.printf("Warning: frame %s is lost.\n",previousFrame.getActingFrameNumber());
			currentLength--;
			quantityOfRemovedItems++;
		} else {
			auxiliaryCounter++;
		}
	}
}
///////////////////////////////////////////////////////////////////////
						} finally {
							history.notify();
						}
System.err.printf("Warning: Buffer overflow (%s); %s frames are lost.\n",length,quantityOfRemovedItems);
					} else {
						if (bufferOverflow) {
							bufferOverflow= false;
							owner.annulBufferOverflow();
						}
					}
				}
			}
		}
	}
}
