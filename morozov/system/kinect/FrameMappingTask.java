// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect;

import morozov.system.kinect.frames.*;
import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.interfaces.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.tools.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FrameMappingTask extends Thread implements FrameMappingTaskInterface {
	//
	protected ConsolidatedDataAcquisitionModeInterface recentDataAcquisitionMode= new ConsolidatedDataAcquisitionMode();
	//
	protected boolean depthDataIsUpdated= false;
	protected boolean infraredDataIsUpdated= false;
	protected boolean longExposureInfraredDataIsUpdated= false;
	protected boolean colorDataIsUpdated= false;
	protected boolean skeletonDataIsUpdated= false;
	//
	protected long localNumberOfDepthFrame= 0;
	protected long localNumberOfInfraredFrame= 0;
	protected long localNumberOfLongExposureInfraredFrame= 0;
	protected long localNumberOfColorFrame= 0;
	protected long localNumberOfSkeletonFrame= 0;
	//
	protected AtomicLong serialNumberOfDepthFrame= new AtomicLong(0);
	protected AtomicLong serialNumberOfInfraredFrame= new AtomicLong(0);
	protected AtomicLong serialNumberOfLongExposureInfraredFrame= new AtomicLong(0);
	protected AtomicLong serialNumberOfMappedColorFrame= new AtomicLong(0);
	protected AtomicLong serialNumberOfEntirePointCloudsFrame= new AtomicLong(0);
	protected AtomicLong serialNumberOfForegroundPointCloudsFrame= new AtomicLong(0);
	protected AtomicLong serialNumberOfColorFrame= new AtomicLong(0);
	//
	protected AtomicInteger durationOfSkeletonsDemonstration= new AtomicInteger(0);
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	//
	protected long recentDepthFrameTime= -1;
	protected short[] recentDepthFrame= null;
	protected byte[] recentPlayerIndex= null;
	protected float[] recentXYZ= null;
	protected float[] recentUV= null;
	//
	protected long recentInfraredFrameTime= -1;
	protected short[] recentInfraredFrame= null;
	//
	protected long recentLongExposureInfraredFrameTime= -1;
	protected short[] recentLongExposureInfraredFrame= null;
	//
	protected long recentColorFrameTime= -1;
	protected byte[] recentColorData= null;
	//
	protected KinectFrameBaseAttributesInterface recentBaseAttributes;
	//
	protected long recentSkeletonTime= -1;
	protected boolean[] recentSkeletonTracked= null;
	protected float[] recentPositions= null;
	protected float[] recentOrientations= null;
	protected byte[] recentJointStatus= null;
	//
	protected boolean resetInternalArrays= false;
	//
	protected GeneralSkeletonInterface[] computedSkeletons;
	protected DimensionsInterface computedDimensions;
	protected float[][] computedU= null;
	protected float[][] computedV= null;
	protected byte[][] computedMappedRed= null;
	protected byte[][] computedMappedGreen= null;
	protected byte[][] computedMappedBlue= null;
	//
	protected KinectInterface owner;
	//
	public static int defaultSkeletonReleaseTime= 3;
	//
	protected AtomicInteger skeletonReleaseTime= new AtomicInteger(defaultSkeletonReleaseTime);
	//
	///////////////////////////////////////////////////////////////
	//
	public FrameMappingTask(KinectInterface o) {
		owner= o;
		setDaemon(true);
		start();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void initialize(KinectFrameBaseAttributesInterface attributes) {
		synchronized (this) {
			recentBaseAttributes= attributes;
		}
	}
	@Override
	public void resetCounters() {
		synchronized (this) {
			depthDataIsUpdated= false;
			infraredDataIsUpdated= false;
			longExposureInfraredDataIsUpdated= false;
			colorDataIsUpdated= false;
			skeletonDataIsUpdated= false;
			//
			localNumberOfDepthFrame= 0;
			localNumberOfInfraredFrame= 0;
			localNumberOfLongExposureInfraredFrame= 0;
			localNumberOfColorFrame= 0;
			localNumberOfSkeletonFrame= 0;
			//
			serialNumberOfDepthFrame.set(0);
			serialNumberOfInfraredFrame.set(0);
			serialNumberOfLongExposureInfraredFrame.set(0);
			serialNumberOfMappedColorFrame.set(0);
			serialNumberOfEntirePointCloudsFrame.set(0);
			serialNumberOfForegroundPointCloudsFrame.set(0);
			serialNumberOfColorFrame.set(0);
			//
			durationOfSkeletonsDemonstration.set(0);
			stopThisThread.set(false);
			//
			recentDepthFrameTime= -1;
			recentDepthFrame= null;
			recentPlayerIndex= null;
			recentXYZ= null;
			recentUV= null;
			//
			recentInfraredFrameTime= -1;
			recentInfraredFrame= null;
			//
			recentLongExposureInfraredFrameTime= -1;
			recentLongExposureInfraredFrame= null;
			//
			recentColorFrameTime= -1;
			recentColorData= null;
			//
			recentSkeletonTime= -1;
			recentSkeletonTracked= null;
			recentPositions= null;
			recentOrientations= null;
			recentJointStatus= null;
			//
			resetInternalArrays= false;
		}
	}
	@Override
	public void setModeDataAcquisition(ConsolidatedDataAcquisitionModeInterface mode) {
		synchronized (this) {
			recentDataAcquisitionMode= mode;
		}
	}
	//
	@Override
	public int getHorizontalCorrection() {
		if (recentBaseAttributes != null) {
			return recentBaseAttributes.getCorrectionX();
		} else {
			return ExtendedCorrectionTools.getDefaultHorizontalCorrection();
		}
	}
	@Override
	public int getVerticalCorrection() {
		if (recentBaseAttributes != null) {
			return recentBaseAttributes.getCorrectionY();
		} else {
			return ExtendedCorrectionTools.getDefaultVerticalCorrection();
		}
	}
	@Override
	public void setCorrection(int x, int y) {
		if (recentBaseAttributes != null) {
			if (recentBaseAttributes instanceof KinectFrameWritableBaseAttributesInterface) {
				KinectFrameWritableBaseAttributesInterface attributes= (KinectFrameWritableBaseAttributes)recentBaseAttributes;
				attributes.setCorrection(x,y);
			}
		}
	}
	//
	@Override
	public int getSkeletonReleaseTime() {
		return skeletonReleaseTime.get();
	}
	@Override
	public void setSkeletonReleaseTime(int length) {
		skeletonReleaseTime.set(length);
	}
	@Override
	public void resetSkeletonReleaseTime() {
		skeletonReleaseTime.set(defaultSkeletonReleaseTime);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setDepthFrame(long time, short[] depthFrame, byte[] playerIndex, float[] xyz, float[] uv) {
		synchronized (this) {
			depthDataIsUpdated= true;
			localNumberOfDepthFrame++;
			recentDepthFrameTime= time;
			recentDepthFrame= depthFrame;
			recentPlayerIndex= playerIndex;
			recentXYZ= xyz;
			recentUV= uv;
			notify();
		}
	}
	@Override
	public void setSkeletonFrame(long time, boolean[] skeletonTracked, float[] positions, float[] orientations, byte[] jointStatus) {
		synchronized (this) {
			skeletonDataIsUpdated= true;
			localNumberOfSkeletonFrame++;
			durationOfSkeletonsDemonstration.set(0);
			recentSkeletonTime= time;
			recentSkeletonTracked= skeletonTracked;
			recentPositions= positions;
			recentOrientations= orientations;
			recentJointStatus= jointStatus;
			notify();
		}
	}
	@Override
	public void setColorFrame(long time, byte[] colorFrame) {
		synchronized (this) {
			colorDataIsUpdated= true;
			localNumberOfColorFrame++;
			recentColorFrameTime= time;
			recentColorData= colorFrame;
			notify();
		}
	}
	@Override
	public void setInfraredFrame(long time, short[] infraredFrame) {
		synchronized (this) {
			infraredDataIsUpdated= true;
			localNumberOfInfraredFrame++;
			recentInfraredFrameTime= time;
			recentInfraredFrame= infraredFrame;
			notify();
		}
	}
	@Override
	public void setLongExposureInfraredFrame(long time, short[] longExposureInfraredFrame) {
		synchronized (this) {
			localNumberOfLongExposureInfraredFrame++;
			longExposureInfraredDataIsUpdated= true;
			recentLongExposureInfraredFrameTime= time;
			recentLongExposureInfraredFrame= longExposureInfraredFrame;
			notify();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void run() {
		while (!stopThisThread.get()) {
			try {
				synchronized (this) {
					if (	!depthDataIsUpdated &&
						!infraredDataIsUpdated &&
						!longExposureInfraredDataIsUpdated &&
						!colorDataIsUpdated &&
						!skeletonDataIsUpdated) {
						wait();
					}
				};
				mapFrames();
			} catch (InterruptedException e) {
			} catch (ThreadDeath e) {
				return;
			}
		}
	}
	//
	@Override
	public void mapFrames() {
		ConsolidatedDataAcquisitionModeInterface dataAcquisitionMode;
		boolean depthDataWereUpdated;
		boolean	infraredDataWereUpdated;
		boolean longExposureInfraredDataWereUpdated;
		boolean colorDataWereUpdated;
		boolean	skeletonDataWereUpdated;
		long currentLocalNumberOfDepthFrame;
		long currentLocalNumberOfInfraredFrame;
		long currentLocalNumberOfLongExposureInfraredFrame;
		long currentLocalNumberOfColorFrame;
		long currentLocalNumberOfSkeletonFrame;
		long depthFrameTime;
		short[] depthFrame;
		byte[] playerIndex;
		float[] xyz;
		float[] uv;
		long skeletonTime;
		boolean[] skeletonTracked;
		float[] positions;
		float[] orientations;
		byte[] jointStatus;
		KinectFrameBaseAttributesInterface baseAttributes;
		long colorFrameTime;
		byte[] colorData;
		long infraredFrameTime;
		short[] infraredFrame;
		long longExposureInfraredFrameTime;
		short[] longExposureInfraredFrame;
		synchronized (this) {
			dataAcquisitionMode= recentDataAcquisitionMode;
			depthDataWereUpdated= depthDataIsUpdated;
			infraredDataWereUpdated= infraredDataIsUpdated;
			longExposureInfraredDataWereUpdated= longExposureInfraredDataIsUpdated;
			colorDataWereUpdated= colorDataIsUpdated;
			skeletonDataWereUpdated= skeletonDataIsUpdated;
			currentLocalNumberOfDepthFrame= localNumberOfDepthFrame;
			currentLocalNumberOfInfraredFrame= localNumberOfInfraredFrame;
			currentLocalNumberOfLongExposureInfraredFrame= localNumberOfLongExposureInfraredFrame;
			currentLocalNumberOfColorFrame= localNumberOfColorFrame;
			currentLocalNumberOfSkeletonFrame= localNumberOfSkeletonFrame;
			depthDataIsUpdated= false;
			infraredDataIsUpdated= false;
			longExposureInfraredDataIsUpdated= false;
			colorDataIsUpdated= false;
			skeletonDataIsUpdated= false;
			depthFrameTime= recentDepthFrameTime;
			depthFrame= recentDepthFrame;
			playerIndex= recentPlayerIndex;
			xyz= recentXYZ;
			uv= recentUV;
			skeletonTime= recentSkeletonTime;
			skeletonTracked= recentSkeletonTracked;
			positions= recentPositions;
			orientations= recentOrientations;
			jointStatus= recentJointStatus;
			baseAttributes= recentBaseAttributes;
			colorFrameTime= recentColorFrameTime;
			colorData= recentColorData;
			infraredFrameTime= recentInfraredFrameTime;
			infraredFrame= recentInfraredFrame;
			longExposureInfraredFrameTime= recentLongExposureInfraredFrameTime;
			longExposureInfraredFrame= recentLongExposureInfraredFrame;
			if (resetInternalArrays) {
				computedSkeletons= null;
				computedDimensions= null;
				computedU= null;
				computedV= null;
				computedMappedRed= null;
				computedMappedGreen= null;
				computedMappedBlue= null;
				resetInternalArrays= false;
			}
		};
		int expectedNumberOfSkeletons= baseAttributes.getNumberOfSkeletons();
		if (depthDataWereUpdated) {
			if (uv != null) {
				FrameSize uvFrameSize= FrameSize.computeUVFrameSize(uv);
				int uvFrameWidth= uvFrameSize.getWidth();
				int uvFrameHeight= uvFrameSize.getHeight();
				if (uvFrameWidth > 0 && uvFrameHeight > 0) {
					float[][] u= new float[uvFrameWidth][uvFrameHeight];
					float[][] v= new float[uvFrameWidth][uvFrameHeight];
					int counter1= 0;
					for (int h=0; h < uvFrameHeight; h++) {
						for (int w=0; w < uvFrameWidth; w++) {
							int counter2= h*uvFrameWidth + (uvFrameWidth-w-1);
							u[w][h]= uv[counter1++];
							v[w][h]= uv[counter1++];
						}
					};
					computedU= u;
					computedV= v;
				}
			}
		};
		if (colorDataWereUpdated || depthDataWereUpdated) {
			if (computedU != null && computedV != null && colorData != null) {
				int uvFrameWidth= computedU.length;
				if (uvFrameWidth > 0) {
					int uvFrameHeight= computedU[0].length;
					if (uvFrameHeight > 0) {
						FrameSize colorFrameSize= FrameSize.computeColorFrameSize(colorData);
						int colorFrameWidth= colorFrameSize.getWidth();
						int colorFrameHeight= colorFrameSize.getHeight();
						if (colorFrameWidth > 0 || colorFrameHeight > 0) {
							byte[][] mappedRed= new byte[uvFrameWidth][uvFrameHeight];
							byte[][] mappedGreen= new byte[uvFrameWidth][uvFrameHeight];
							byte[][] mappedBlue= new byte[uvFrameWidth][uvFrameHeight];
							for (int h=0; h < uvFrameHeight; h++) {
								for (int w=0; w < uvFrameWidth; w++) {
									float x1= computedU[w][h];
									float y1= computedV[w][h];
									int x2= XY_Tools.uToColorIndexX(x1,colorFrameWidth);
									int y2= XY_Tools.vToColorIndexY(y1,colorFrameHeight);
									if (XY_Tools.isLegal(x2) && XY_Tools.isLegal(y2)) {
										int counter1= y2*colorFrameWidth + x2;
										mappedRed[w][h]= colorData[counter1*4+2];
										mappedGreen[w][h]= colorData[counter1*4+1];
										mappedBlue[w][h]= colorData[counter1*4];
									}
								}
							};
							computedMappedRed= mappedRed;
							computedMappedGreen= mappedGreen;
							computedMappedBlue= mappedBlue;
						}
					}
				}
			}
		};
		if (skeletonDataWereUpdated || depthDataWereUpdated || colorDataIsUpdated) {
			int depthFrameWidth= -1;
			int depthFrameHeight= -1;
			if (depthFrame != null) {
				FrameSize depthFrameSize= FrameSize.computeDepthFrameSize(depthFrame);
				depthFrameWidth= depthFrameSize.getWidth();
				depthFrameHeight= depthFrameSize.getHeight();
			} else if (infraredFrame != null) {
				FrameSize infraredFrameSize= FrameSize.computeInfraredFrameSize(infraredFrame);
				depthFrameWidth= infraredFrameSize.getWidth();
				depthFrameHeight= infraredFrameSize.getHeight();
			} else if (longExposureInfraredFrame != null) {
				FrameSize longExposureInfraredFrameSize= FrameSize.computeLongExposureInfraredFrameSize(longExposureInfraredFrame);
				depthFrameWidth= longExposureInfraredFrameSize.getWidth();
				depthFrameHeight= longExposureInfraredFrameSize.getHeight();
			};
			if (depthFrameWidth > 0 || depthFrameHeight > 0) {
				int colorFrameWidth= -1;
				int colorFrameHeight= -1;
				if (colorData != null) {
					FrameSize colorFrameSize= FrameSize.computeColorFrameSize(colorData);
					colorFrameWidth= colorFrameSize.getWidth();
					colorFrameHeight= colorFrameSize.getHeight();
				};
				if (!skeletonDataWereUpdated && durationOfSkeletonsDemonstration.get() >= skeletonReleaseTime.get()) {
					synchronized (this) {
						recentSkeletonTime= -1;
						recentSkeletonTracked= null;
						recentPositions= null;
						recentOrientations= null;
						recentJointStatus= null;
					};
					computedSkeletons= null;
					computedDimensions= null;
				} else {
					durationOfSkeletonsDemonstration.incrementAndGet();
					if (skeletonTracked != null && positions != null && orientations != null && jointStatus != null) {
						computedSkeletons= new GeneralSkeleton[expectedNumberOfSkeletons];
						for(int n=0; n < expectedNumberOfSkeletons; n++) {
							computedSkeletons[n]= GeneralSkeletonTools.assembleSkeleton(n,currentLocalNumberOfSkeletonFrame,skeletonTime,skeletonTracked,positions,orientations,jointStatus,computedU,computedV,baseAttributes);
						}
					}
				};
				if (playerIndex != null) {
					computedDimensions= DimensionsTools.computeDimensions(expectedNumberOfSkeletons,playerIndex,xyz,computedU,computedV,computedSkeletons,baseAttributes);
				}
			}
		};
		if (dataAcquisitionMode.getDepthMapsAreRequested()) {
			if (depthFrame != null) {
				KinectDepthFrame frame= new KinectDepthFrame(
					serialNumberOfDepthFrame.incrementAndGet(),
					currentLocalNumberOfDepthFrame,
					currentLocalNumberOfColorFrame,
					currentLocalNumberOfSkeletonFrame,
					depthFrameTime,
					colorFrameTime,
					skeletonTime,
					depthFrame,
					playerIndex,
					computedSkeletons,
					computedDimensions,
					computedMappedRed,
					computedMappedGreen,
					computedMappedBlue,
					baseAttributes);
				owner.sendDepthFrame(frame);
			}
		};
		if (dataAcquisitionMode.getInfraredFramesAreRequested()) {
			if (infraredFrame != null) {
				KinectInfraredFrame frame= new KinectInfraredFrame(
					serialNumberOfInfraredFrame.incrementAndGet(),
					currentLocalNumberOfInfraredFrame,
					currentLocalNumberOfColorFrame,
					currentLocalNumberOfSkeletonFrame,
					infraredFrameTime,
					colorFrameTime,
					skeletonTime,
					infraredFrame,
					computedSkeletons,
					computedDimensions,
					playerIndex,
					computedMappedRed,
					computedMappedGreen,
					computedMappedBlue,
					baseAttributes);
				owner.sendInfraredFrame(frame);
			}
		};
		if (dataAcquisitionMode.getLongExposureInfraredFramesAreRequested()) {
			if (longExposureInfraredFrame != null) {
				KinectLongExposureInfraredFrame frame= new KinectLongExposureInfraredFrame(
					serialNumberOfLongExposureInfraredFrame.incrementAndGet(),
					currentLocalNumberOfLongExposureInfraredFrame,
					currentLocalNumberOfColorFrame,
					currentLocalNumberOfSkeletonFrame,
					longExposureInfraredFrameTime,
					colorFrameTime,
					skeletonTime,
					longExposureInfraredFrame,
					computedSkeletons,
					computedDimensions,
					playerIndex,
					computedMappedRed,
					computedMappedGreen,
					computedMappedBlue,
					baseAttributes);
				owner.sendLongExposureInfraredFrame(frame);
			}
		};
		if (dataAcquisitionMode.getMappedColorFramesAreRequested()) {
			if (computedMappedRed != null && computedMappedGreen != null && computedMappedBlue != null) {
				KinectMappedColorFrame frame= new KinectMappedColorFrame(
					serialNumberOfMappedColorFrame.incrementAndGet(),
					currentLocalNumberOfDepthFrame,
					currentLocalNumberOfColorFrame,
					currentLocalNumberOfSkeletonFrame,
					depthFrameTime,
					colorFrameTime,
					skeletonTime,
					computedMappedRed,
					computedMappedGreen,
					computedMappedBlue,
					computedSkeletons,
					computedDimensions,
					playerIndex,
					baseAttributes);
				owner.sendMappedColorFrame(frame);
			}
		};
		if (dataAcquisitionMode.getEntirePointCloudsAreRequested()) {
			if (xyz != null && computedMappedRed != null && computedMappedGreen != null && computedMappedBlue != null) {
				KinectPointCloudsFrame frame= new KinectPointCloudsFrame(
					serialNumberOfEntirePointCloudsFrame.incrementAndGet(),
					currentLocalNumberOfDepthFrame,
					currentLocalNumberOfColorFrame,
					currentLocalNumberOfSkeletonFrame,
					depthFrameTime,
					colorFrameTime,
					skeletonTime,
					xyz,
					computedMappedRed,
					computedMappedGreen,
					computedMappedBlue,
					computedSkeletons,
					computedDimensions,
					playerIndex,
					baseAttributes);
				owner.sendEntirePointCloudsFrame(frame);
			}
		};
		if (dataAcquisitionMode.getForegroundPointCloudsAreRequested()) {
			if (xyz != null && computedMappedRed != null && computedMappedGreen != null && computedMappedBlue != null) {
				if (playerIndex != null) {
					KinectForegroundPointCloudsFrame frame= new KinectForegroundPointCloudsFrame(
						serialNumberOfForegroundPointCloudsFrame.incrementAndGet(),
						currentLocalNumberOfDepthFrame,
						currentLocalNumberOfColorFrame,
						currentLocalNumberOfSkeletonFrame,
						depthFrameTime,
						colorFrameTime,
						skeletonTime,
						xyz,
						computedMappedRed,
						computedMappedGreen,
						computedMappedBlue,
						computedSkeletons,
						computedDimensions,
						playerIndex,
						baseAttributes);
					owner.sendForegroundPointCloudsFrame(frame);
				}
			}
		};
		if (dataAcquisitionMode.getColorFramesAreRequested()) {
			if (colorData != null) {
				KinectColorFrame frame= new KinectColorFrame(
					serialNumberOfColorFrame.incrementAndGet(),
					currentLocalNumberOfDepthFrame,
					currentLocalNumberOfColorFrame,
					currentLocalNumberOfSkeletonFrame,
					depthFrameTime,
					colorFrameTime,
					skeletonTime,
					colorData,
					computedSkeletons,
					computedDimensions,
					playerIndex,
					computedU,
					computedV,
					baseAttributes);
				owner.sendColorFrame(frame);
			}
		}
	}
}
