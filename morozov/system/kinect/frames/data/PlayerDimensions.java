// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import edu.ufl.digitalworlds.j4k.Skeleton;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;

import java.io.Serializable;

public class PlayerDimensions implements PlayerDimensionsInterface, Serializable {
	//
	protected int identifier= -1;
	//
	protected boolean areInitialized_2D_Bounds= false;
	//
	protected int minimalX_2D= 0;
	protected int maximalX_2D= 0;
	protected int minimalY_2D= 0;
	protected int maximalY_2D= 0;
	//
	protected boolean areInitialized_Depth3D_Bounds= false;
	//
	protected float minimalX_3D= 0;
	protected float maximalX_3D= 0;
	protected float minimalY_3D= 0;
	protected float maximalY_3D= 0;
	protected float minimalZ_3D= 0;
	protected float maximalZ_3D= 0;
	//
	protected boolean isInitialized_DepthParallelepiped= false;
	protected boolean isInitialized_ColorParallelepiped= false;
	//
	protected XY_Interface xy11;
	protected XY_Interface xy12;
	protected XY_Interface xy13;
	protected XY_Interface xy14;
	protected XY_Interface xy21;
	protected XY_Interface xy22;
	protected XY_Interface xy23;
	protected XY_Interface xy24;
	//
	private static final long serialVersionUID= 0xD4082ABD2F7C533CL; // -3168235345821281476L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","PlayerDimensions");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public PlayerDimensions(int n) {
		identifier= n;
	}
	//
	/////////////////////////////////////////////////////////////
	//
	@Override
	public void refineRectangleBounds(int x, int y) {
		if (XY_Tools.isLegal(x) && XY_Tools.isLegal(y)) {
			if (areInitialized_2D_Bounds) {
				if (minimalX_2D > x) {
					minimalX_2D= x;
				};
				if (maximalX_2D < x) {
					maximalX_2D= x;
				};
				if (minimalY_2D > y) {
					minimalY_2D= y;
				};
				if (maximalY_2D < y) {
					maximalY_2D= y;
				}
			} else {
				minimalX_2D= x;
				maximalX_2D= x;
				minimalY_2D= y;
				maximalY_2D= y;
				areInitialized_2D_Bounds= true;
			}
		}
	}
	//
	/////////////////////////////////////////////////////////////
	//
	@Override
	public void computeSkeletonDepthRectangleBounds(GeneralSkeletonInterface skeleton, KinectFrameBaseAttributesInterface attributes) {
		float focalLengthX= attributes.getFocalLengthX();
		float focalLengthY= attributes.getFocalLengthY();
		int imageWidth= attributes.getDepthFrameWidth();
		int imageHeight= attributes.getDepthFrameHeight();
		int correctionX= attributes.getCorrectionX();
		int correctionY= attributes.getCorrectionY();
		if (skeleton.isTracked()) {
			float[] positions= skeleton.getJointPositions();
			byte[] states= skeleton.getJointTrackingStates();
			for (int n=0; n < states.length; n++) {
				if (states[n]==Skeleton.TRACKED) {
					float x0= positions[n*3+0];
					float y0= positions[n*3+1];
					float z0= positions[n*3+2];
					float fX= XY_Tools.xyzToX(x0,z0,focalLengthX,correctionX);
					float fY= XY_Tools.xyzToY(y0,z0,focalLengthY,correctionY);
					int x2= imageWidth - XY_Tools.centeredXtoIndexUV(fX,imageWidth,false);
					int y2= XY_Tools.centeredYtoIndexUV(fY,imageHeight,false);
					refineRectangleBounds(x2,y2);
				}
			}
		}
	}
	@Override
	public void computeSkeletonColorRectangleBounds(GeneralSkeletonInterface skeleton, float[][] u, float[][] v, KinectFrameBaseAttributesInterface attributes) {
		float focalLengthX= attributes.getFocalLengthX();
		float focalLengthY= attributes.getFocalLengthY();
		int imageWidth= attributes.getDepthFrameWidth();
		int imageHeight= attributes.getDepthFrameHeight();
		int colorFrameWidth= attributes.getColorFrameWidth();
		int colorFrameHeight= attributes.getColorFrameHeight();
		int correctionX= attributes.getCorrectionX();
		int correctionY= attributes.getCorrectionY();
		if (skeleton.isTracked()) {
			int uvWidth= u.length;
			if (uvWidth <= 0) {
				return;
			};
			int uvHeight= u[0].length;
			float[] positions= skeleton.getJointPositions();
			byte[] states= skeleton.getJointTrackingStates();
			for (int n=0; n < states.length; n++) {
				if (states[n]==Skeleton.TRACKED) {
					float x0= positions[n*3+0];
					float y0= positions[n*3+1];
					float z0= positions[n*3+2];
					float fX= XY_Tools.xyzToX(x0,z0,focalLengthX,correctionX);
					float fY= XY_Tools.xyzToY(y0,z0,focalLengthY,correctionY);
					int x2= XY_Tools.centeredXtoIndexUV(fX,uvWidth,false);
					int y2= XY_Tools.centeredYtoIndexUV(fY,uvHeight,false);
					float x3= XY_Tools.getOrComputeU(u,x2,y2,uvWidth,uvHeight);
					float y3= XY_Tools.getOrComputeV(v,x2,y2,uvWidth,uvHeight);
					if (XY_Tools.isFinite(x3) && XY_Tools.isFinite(y3)) {
						int x4= XY_Tools.uToColorX(x3,colorFrameWidth,false);
						int y4= XY_Tools.vToColorY(y3,colorFrameHeight,false);
						refineRectangleBounds(x4,y4);
					}
				}
			}
		}
	}
	//
	/////////////////////////////////////////////////////////////
	//
	@Override
	public void refineParallelepipedBounds(float x, float y, float z) {
		if (areInitialized_Depth3D_Bounds) {
			if (minimalX_3D > x) {
				minimalX_3D= x;
			};
			if (maximalX_3D < x) {
				maximalX_3D= x;
			};
			if (minimalY_3D > y) {
				minimalY_3D= y;
			};
			if (maximalY_3D < y) {
				maximalY_3D= y;
			}
			if (minimalZ_3D > z) {
				minimalZ_3D= z;
			};
			if (maximalZ_3D < z) {
				maximalZ_3D= z;
			}
		} else {
			minimalX_3D= x;
			maximalX_3D= x;
			minimalY_3D= y;
			maximalY_3D= y;
			minimalZ_3D= z;
			maximalZ_3D= z;
			areInitialized_Depth3D_Bounds= true;
		}
	}
	//
	/////////////////////////////////////////////////////////////
	//
	@Override
	public void computeTotalDepthParallelepiped(KinectFrameBaseAttributesInterface attributes) {
		if (identifier >= 0 && areInitialized_Depth3D_Bounds) {
			computeParallelepiped(attributes);
			isInitialized_DepthParallelepiped= true;
		}
	}
	@Override
	public void computeSkeletonDepthParallelepiped(GeneralSkeletonInterface skeleton, KinectFrameBaseAttributesInterface attributes) {
		if (identifier >= 0 && skeleton.isTracked()) {
			computeSkeletonParallelepipedBounds(skeleton);
			computeParallelepiped(attributes);
			isInitialized_DepthParallelepiped= true;
		}
	}
	@Override
	public void computeTotalColorParallelepiped(float[][] u, float[][] v, KinectFrameBaseAttributesInterface attributes) {
		int colorFrameWidth= attributes.getColorFrameWidth();
		int colorFrameHeight= attributes.getColorFrameHeight();
		if (identifier >= 0 && areInitialized_Depth3D_Bounds) {
			computeParallelepiped(attributes);
			projectParallelepipedToColorImage(u,v,colorFrameWidth,colorFrameHeight);
			isInitialized_ColorParallelepiped= true;
		}
	}
	@Override
	public void computeSkeletonColorParallelepiped(GeneralSkeletonInterface skeleton, float[][] u, float[][] v, KinectFrameBaseAttributesInterface attributes) {
		int colorFrameWidth= attributes.getColorFrameWidth();
		int colorFrameHeight= attributes.getColorFrameHeight();
		if (identifier >= 0 && skeleton.isTracked()) {
			computeSkeletonParallelepipedBounds(skeleton);
			computeParallelepiped(attributes);
			projectParallelepipedToColorImage(u,v,colorFrameWidth,colorFrameHeight);
			isInitialized_ColorParallelepiped= true;
		}
	}
	protected void computeParallelepiped(KinectFrameBaseAttributesInterface attributes) {
		//
		float x11= minimalX_3D;
		float y11= maximalY_3D;
		float z11= minimalZ_3D;
		//
		float x12= maximalX_3D;
		float y12= maximalY_3D;
		float z12= minimalZ_3D;
		//		
		float x13= maximalX_3D;
		float y13= minimalY_3D;
		float z13= minimalZ_3D;
		//		
		float x14= minimalX_3D;
		float y14= minimalY_3D;
		float z14= minimalZ_3D;
		//		
		float x21= minimalX_3D;
		float y21= maximalY_3D;
		float z21= maximalZ_3D;
		//
		float x22= maximalX_3D;
		float y22= maximalY_3D;
		float z22= maximalZ_3D;
		//		
		float x23= maximalX_3D;
		float y23= minimalY_3D;
		float z23= maximalZ_3D;
		//		
		float x24= minimalX_3D;
		float y24= minimalY_3D;
		float z24= maximalZ_3D;
		//
		float focalLengthX= attributes.getFocalLengthX();
		float focalLengthY= attributes.getFocalLengthY();
		int imageWidth= attributes.getDepthFrameWidth();
		int imageHeight= attributes.getDepthFrameHeight();
		int correctionX= attributes.getCorrectionX();
		int correctionY= attributes.getCorrectionY();
		//
		xy11= XY_Tools.projectXYZ(x11,y11,z11,focalLengthX,focalLengthY,imageWidth,imageHeight,correctionX,correctionY);
		xy12= XY_Tools.projectXYZ(x12,y12,z12,focalLengthX,focalLengthY,imageWidth,imageHeight,correctionX,correctionY);
		xy13= XY_Tools.projectXYZ(x13,y13,z13,focalLengthX,focalLengthY,imageWidth,imageHeight,correctionX,correctionY);
		xy14= XY_Tools.projectXYZ(x14,y14,z14,focalLengthX,focalLengthY,imageWidth,imageHeight,correctionX,correctionY);
		//
		xy21= XY_Tools.projectXYZ(x21,y21,z21,focalLengthX,focalLengthY,imageWidth,imageHeight,correctionX,correctionY);
		xy22= XY_Tools.projectXYZ(x22,y22,z22,focalLengthX,focalLengthY,imageWidth,imageHeight,correctionX,correctionY);
		xy23= XY_Tools.projectXYZ(x23,y23,z23,focalLengthX,focalLengthY,imageWidth,imageHeight,correctionX,correctionY);
		xy24= XY_Tools.projectXYZ(x24,y24,z24,focalLengthX,focalLengthY,imageWidth,imageHeight,correctionX,correctionY);
	}
	protected void computeSkeletonParallelepipedBounds(GeneralSkeletonInterface skeleton) {
		float[] positions= skeleton.getJointPositions();
		byte[] states= skeleton.getJointTrackingStates();
		for (int n=0; n < states.length; n++) {
			if (states[n]==Skeleton.TRACKED) {
				float x0= positions[n*3+0];
				float y0= positions[n*3+1];
				float z0= positions[n*3+2];
				refineParallelepipedBounds(x0,y0,z0);
			}
		}
	}
	@Override
	public void projectParallelepipedToColorImage(float[][] u, float[][] v, int colorFrameWidth, int colorFrameHeight) {
		//
		xy11= XY_Tools.projectToColorImage(xy11,u,v,colorFrameWidth,colorFrameHeight);
		xy12= XY_Tools.projectToColorImage(xy12,u,v,colorFrameWidth,colorFrameHeight);
		xy13= XY_Tools.projectToColorImage(xy13,u,v,colorFrameWidth,colorFrameHeight);
		xy14= XY_Tools.projectToColorImage(xy14,u,v,colorFrameWidth,colorFrameHeight);
		//
		xy21= XY_Tools.projectToColorImage(xy21,u,v,colorFrameWidth,colorFrameHeight);
		xy22= XY_Tools.projectToColorImage(xy22,u,v,colorFrameWidth,colorFrameHeight);
		xy23= XY_Tools.projectToColorImage(xy23,u,v,colorFrameWidth,colorFrameHeight);
		xy24= XY_Tools.projectToColorImage(xy24,u,v,colorFrameWidth,colorFrameHeight);
	}
	//
	/////////////////////////////////////////////////////////////
	//
	@Override
	public int getIdentifier() {
		return identifier;
	}
	@Override
	public boolean areInitialized_2D_Bounds() {
		return areInitialized_2D_Bounds;
	}
	@Override
	public int getMinimalX_2D() {
		return minimalX_2D;
	}
	@Override
	public int getMaximalX_2D() {
		return maximalX_2D;
	}
	@Override
	public int getMinimalY_2D() {
		return minimalY_2D;
	}
	@Override
	public int getMaximalY_2D() {
		return maximalY_2D;
	}
	@Override
	public boolean areInitialized_Depth3D_Bounds() {
		return areInitialized_Depth3D_Bounds;
	}
	@Override
	public float getMinimalX_3D() {
		return minimalX_3D;
	}
	@Override
	public float getMaximalX_3D() {
		return maximalX_3D;
	}
	@Override
	public float getMinimalY_3D() {
		return minimalY_3D;
	}
	@Override
	public float getMaximalY_3D() {
		return maximalY_3D;
	}
	@Override
	public float getMinimalZ_3D() {
		return minimalZ_3D;
	}
	@Override
	public float getMaximalZ_3D() {
		return maximalZ_3D;
	}
	@Override
	public boolean isInitialized_DepthParallelepiped() {
		return isInitialized_DepthParallelepiped;
	}
	@Override
	public boolean isInitialized_ColorParallelepiped() {
		return isInitialized_ColorParallelepiped;
	}
	@Override
	public XY_Interface getXY11() {
		return xy11;
	}
	@Override
	public XY_Interface getXY12() {
		return xy12;
	}
	@Override
	public XY_Interface getXY13() {
		return xy13;
	}
	@Override
	public XY_Interface getXY14() {
		return xy14;
	}
	@Override
	public XY_Interface getXY21() {
		return xy21;
	}
	@Override
	public XY_Interface getXY22() {
		return xy22;
	}
	@Override
	public XY_Interface getXY23() {
		return xy23;
	}
	@Override
	public XY_Interface getXY24() {
		return xy24;
	}
}
