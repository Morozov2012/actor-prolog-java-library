// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;

import java.io.Serializable;

public class GeneralSkeleton implements GeneralSkeletonInterface, Serializable {
	//
	protected int identifier;
	protected boolean isTracked;
	protected long frameNumber;
	protected long time;
	//
	protected float[] jointPositions;
	protected float[] jointOrientations;
	protected byte[] jointTrackingStates;
	//
	protected boolean isInitialized_Depth= false;
	//
	protected int[] x_Depth;
	protected int[] y_Depth;
	//
	protected boolean isInitialized_Color= false;
	//
	protected int[] x_Color;
	protected int[] y_Color;
	//
	protected KinectFrameBaseAttributesInterface baseAttributes;
	//
	private static final long serialVersionUID= 0x418B726549CC6503L; // 4722994413583492355L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","GeneralSkeleton");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public GeneralSkeleton() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void computeDepthFXY(int correctionX, int correctionY) {
		int arrayLength= jointTrackingStates.length;
		x_Depth= new int[arrayLength];
		y_Depth= new int[arrayLength];
		float focalLengthX= baseAttributes.getFocalLengthX();
		float focalLengthY= baseAttributes.getFocalLengthY();
		int depthFrameWidth= baseAttributes.getDepthFrameWidth();
		int depthFrameHeight= baseAttributes.getDepthFrameHeight();
		for (int n=0; n < arrayLength; n++) {
			float x0= get3DJointX(n);
			float y0= get3DJointY(n);
			float z0= get3DJointZ(n);
			float x1= XY_Tools.xyzToX(x0,z0,focalLengthX,correctionX);
			float y1= XY_Tools.xyzToY(y0,z0,focalLengthY,correctionY);
			int x2= XY_Tools.centeredXtoIndexUV(x1,depthFrameWidth,false);
			int y2= XY_Tools.centeredYtoIndexUV(y1,depthFrameHeight,false);
			x_Depth[n]= depthFrameWidth - x2;
			y_Depth[n]= y2;
		}
	}
	//
	public void computeColorFXY(float[][] u, float[][] v, int correctionX, int correctionY) {
		int uvWidth= u.length;
		if (uvWidth <= 0) {
			return;
		};
		int uvHeight= u[0].length;
		int arrayLength= jointTrackingStates.length;
		x_Color= new int[arrayLength];
		y_Color= new int[arrayLength];
		float focalLengthX= baseAttributes.getFocalLengthX();
		float focalLengthY= baseAttributes.getFocalLengthY();
		int colorFrameWidth= baseAttributes.getColorFrameWidth();
		int colorFrameHeight= baseAttributes.getColorFrameHeight();
		for (int n=0; n < arrayLength; n++) {
			float x0= get3DJointX(n);
			float y0= get3DJointY(n);
			float z0= get3DJointZ(n);
			float fX= XY_Tools.xyzToX(x0,z0,focalLengthX,correctionX);
			float fY= XY_Tools.xyzToY(y0,z0,focalLengthY,correctionY);
			int x2= XY_Tools.centeredXtoIndexUV(fX,uvWidth,false);
			int y2= XY_Tools.centeredYtoIndexUV(fY,uvHeight,false);
			float x3= XY_Tools.getOrComputeU(u,x2,y2,uvWidth,uvHeight);
			float y3= XY_Tools.getOrComputeV(v,x2,y2,uvWidth,uvHeight);
			x_Color[n]= XY_Tools.uToColorX(x3,colorFrameWidth,false);
			y_Color[n]= XY_Tools.vToColorY(y3,colorFrameHeight,false);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int id) {
		identifier= id;
	}
	public boolean isTracked() {
		return isTracked;
	}
	public void setIsTracked(boolean value) {
		isTracked= value;
	}
	public long getFrameNumber() {
		return frameNumber;
	}
	public void setFrameNumber(long n) {
		frameNumber= n;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long t) {
		time= t;
	}
	//
	public float[] getJointPositions() {
		return jointPositions;
	}
	public void setJointPositions(float[] array) {
		jointPositions= array;
	}
	public float[] getJointOrientations() {
		return jointOrientations;
	}
	public void setJointOrientations(float[] array) {
		jointOrientations= array;
	}
	public byte[] getJointTrackingStates() {
		return jointTrackingStates;
	}
	public void setJointTrackingStates(byte[] array) {
		jointTrackingStates= array;
	}
	//
	public float get3DJointX(int jointId) {
		return jointPositions[jointId*3+0];
	}
	public float get3DJointY(int jointId) {
		return jointPositions[jointId*3+1];
	}
	public float get3DJointZ(int jointId) {
		return jointPositions[jointId*3+2];
	}
	//
	public float getJointOrientationX(int jointId) {
		return jointOrientations[jointId*4+0];
	}
	public float getJointOrientationY(int jointId) {
		return jointOrientations[jointId*4+1];
	}
	public float getJointOrientationZ(int jointId) {
		return jointOrientations[jointId*4+2];
	}
	public float getJointOrientationW(int jointId) {
		return jointOrientations[jointId*4+3];
	}
	//
	public byte getJointTrackingState(int jointId) {
		return jointTrackingStates[jointId];
	}
	public int getNumberOfJoints() {
		return jointTrackingStates.length;
	}
	//
	public boolean isInitialized_Depth() {
		return isInitialized_Depth;
	}
	public void setIsInitialized_Depth(boolean value) {
		isInitialized_Depth= value;
	}
	public int[] getDepthX() {
		return x_Depth;
	}
	public int[] getDepthY() {
		return y_Depth;
	}
	//
	public boolean isInitialized_Color() {
		return isInitialized_Color;
	}
	public void setIsInitialized_Color(boolean value) {
		isInitialized_Color= value;
	}
	public int[] getColorX() {
		return x_Color;
	}
	public int[] getColorY() {
		return y_Color;
	}
	//
	public KinectFrameBaseAttributesInterface getBaseAttributes() {
		return baseAttributes;
	}
	public void setBaseAttributes(KinectFrameBaseAttributesInterface attributes) {
		baseAttributes= attributes;
	}
	public byte getDeviceType() {
		return baseAttributes.getDeviceType();
	}
	public float getFocalLengthX() {
		return baseAttributes.getFocalLengthX();
	}
	public float getFocalLengthY() {
		return baseAttributes.getFocalLengthY();
	}
	public int getDepthFrameWidth() {
		return baseAttributes.getDepthFrameWidth();
	}
	public int getDepthFrameHeight() {
		return baseAttributes.getDepthFrameHeight();
	}
	public int getColorFrameWidth() {
		return baseAttributes.getColorFrameWidth();
	}
	public int getColorFrameHeight() {
		return baseAttributes.getColorFrameHeight();
	}
}
