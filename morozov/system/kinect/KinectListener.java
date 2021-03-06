// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect;

import edu.ufl.digitalworlds.j4k.J4KSDK;

import morozov.system.kinect.errors.*;
import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.interfaces.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.tools.*;

import java.util.concurrent.atomic.AtomicInteger;

public class KinectListener extends J4KSDK implements KinectListenerInterface {
	//
	protected FrameMappingTaskInterface frameMappingTask;
	//
	protected boolean isConnected= false;
	protected boolean frameMappingIsInitialized= false;
	//
	protected AtomicInteger horizontalCorrection= new AtomicInteger(ExtendedCorrectionTools.getDefaultHorizontalCorrection());
	protected AtomicInteger verticalCorrection= new AtomicInteger(ExtendedCorrectionTools.getDefaultVerticalCorrection());
	//
	public KinectListener(FrameMappingTaskInterface m) {
		frameMappingTask= m;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setHorizontalCorrection(ExtendedCorrectionInterface x) {
		int valueX= x.getHorizontalCorrection();
		horizontalCorrection.set(valueX);
		frameMappingTask.setCorrection(valueX,verticalCorrection.get());
	}
	@Override
	public void setVerticalCorrection(ExtendedCorrectionInterface y) {
		int valueY= y.getVerticalCorrection();
		verticalCorrection.set(valueY);
		frameMappingTask.setCorrection(horizontalCorrection.get(),valueY);
	}
	@Override
	public void setCorrection(ExtendedCorrectionInterface x, ExtendedCorrectionInterface y) {
		int valueX= x.getHorizontalCorrection();
		int valueY= y.getVerticalCorrection();
		horizontalCorrection.set(valueX);
		verticalCorrection.set(valueY);
		frameMappingTask.setCorrection(valueX,valueY);
	}
	@Override
	public void setCorrection(int valueX, int valueY) {
		horizontalCorrection.set(valueX);
		verticalCorrection.set(valueY);
		frameMappingTask.setCorrection(valueX,valueY);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean initializeDevice(ConsolidatedDataAcquisitionModeInterface acquisitionMode) {
		if (isConnected) {
			return isConnected;
		};
		frameMappingTask.resetCounters();
		frameMappingTask.setModeDataAcquisition(acquisitionMode);
		boolean ok= start(acquisitionMode.getKinectDataAcquisitionMode());
		if(ok) {
			isConnected= true;
			frameMappingIsInitialized= false;
		} else {
			throw new KinectDeviceIsNotInitialized();
		};
		return isConnected;
	}
	@Override
	public void stop() {
		super.stop();
		isConnected= false;
	}
	@Override
	public boolean isConnected() {
		return isConnected;
	}
	@Override
	public boolean frameMappingIsInitialized() {
		return frameMappingIsInitialized;
	}
	//
	@Override
	public void onDepthFrameEvent(short[] depthFrame, byte[] playerIndex, float[] xyz, float[] uv) {
		long time= System.currentTimeMillis();
		initializeFrameMappingIfNecessary();
		frameMappingTask.setDepthFrame(time,depthFrame,playerIndex,xyz,uv);
	}
	@Override
	public void onSkeletonFrameEvent(boolean[] skeletonTracked, float[] positions, float[] orientations, byte[] jointStatus) {
		long time= System.currentTimeMillis();
		initializeFrameMappingIfNecessary();
		frameMappingTask.setSkeletonFrame(time,skeletonTracked,positions,orientations,jointStatus);
	}
	@Override
	public void onColorFrameEvent(byte[] data) {
		long time= System.currentTimeMillis();
		initializeFrameMappingIfNecessary();
		frameMappingTask.setColorFrame(time,data);
	}
	@Override
	public void onInfraredFrameEvent(short[] data) {
		long time= System.currentTimeMillis();
		initializeFrameMappingIfNecessary();
		frameMappingTask.setInfraredFrame(time,data);
	}
	@Override
	public void onLongExposureInfraredFrameEvent(short[] data) {
		long time= System.currentTimeMillis();
		initializeFrameMappingIfNecessary();
		frameMappingTask.setLongExposureInfraredFrame(time,data);
	}
	//
	protected void initializeFrameMappingIfNecessary() {
		if (!frameMappingIsInitialized) {
			byte type= getDeviceType();
			float focX= getFocalLengthX();
			float focY= getFocalLengthY();
			int depthFrameWidth= getDepthWidth();
			int depthFrameHeight= getDepthHeight();
			int colorFrameWidth= getColorWidth();
			int colorFrameHeight= getColorHeight();
			int maxNumberOfSkeletons= getMaxNumberOfSkeletons();
			int correctionX= horizontalCorrection.get();
			int correctionY= verticalCorrection.get();
			KinectFrameWritableBaseAttributes attributes= createKinectFrameWritableBaseAttributes();
			frameMappingTask.initialize(attributes);
			frameMappingIsInitialized= true;
		}
	}
	//
	@Override
	public KinectFrameWritableBaseAttributes createKinectFrameWritableBaseAttributes() {
		byte type= getDeviceType();
		float focX= getFocalLengthX();
		float focY= getFocalLengthY();
		int depthFrameWidth= getDepthWidth();
		int depthFrameHeight= getDepthHeight();
		int colorFrameWidth= getColorWidth();
		int colorFrameHeight= getColorHeight();
		int maxNumberOfSkeletons= getMaxNumberOfSkeletons();
		int correctionX= horizontalCorrection.get();
		int correctionY= verticalCorrection.get();
		KinectFrameWritableBaseAttributes attributes= new KinectFrameWritableBaseAttributes(type,focX,focY,depthFrameWidth,depthFrameHeight,colorFrameWidth,colorFrameHeight,maxNumberOfSkeletons,correctionX,correctionY);
		return attributes;
	}
}
