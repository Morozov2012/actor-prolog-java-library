// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;

import java.io.Serializable;

public class Dimensions implements DimensionsInterface, Serializable {
	//
	protected int numberOfSkeletons;
	protected PlayerDimensionsInterface[] totalDepthDimensions;
	protected PlayerDimensionsInterface[] skeletonsDepthDimensions;
	protected PlayerDimensionsInterface[] totalColorDimensions;
	protected PlayerDimensionsInterface[] skeletonsColorDimensions;
	//
	private static final long serialVersionUID= 0x600F9BD28ABB579BL; // 6921922480864712603L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","Dimensions");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public Dimensions(int expectedNumberOfSkeletons) {
		numberOfSkeletons= expectedNumberOfSkeletons;
		totalDepthDimensions= new PlayerDimensions[expectedNumberOfSkeletons];
		skeletonsDepthDimensions= new PlayerDimensions[expectedNumberOfSkeletons];
		totalColorDimensions= new PlayerDimensions[expectedNumberOfSkeletons];
		skeletonsColorDimensions= new PlayerDimensions[expectedNumberOfSkeletons];
		for (int n=0; n < expectedNumberOfSkeletons; n++) {
			totalDepthDimensions[n]= new PlayerDimensions(n);
			skeletonsDepthDimensions[n]= new PlayerDimensions(n);
			totalColorDimensions[n]= new PlayerDimensions(n);
			skeletonsColorDimensions[n]= new PlayerDimensions(n);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void computeDimensions(byte[] playerIndex, float[] xyz, float[][] u, float[][] v, GeneralSkeletonInterface[] skeletons, KinectFrameBaseAttributesInterface attributes) {
		int imageWidth= attributes.getDepthFrameWidth();
		int imageHeight= attributes.getDepthFrameHeight();
		int bandLength= imageWidth * imageHeight;
		int counter1= -1;
		for (int h=0; h < imageHeight; h++) {
			for (int w=0; w < imageWidth; w++) {
				counter1++;
				int index= (int)playerIndex[counter1];
				if (index >= 0) {
					totalDepthDimensions[index].refineRectangleBounds(imageWidth-w,h);
				}
			}
		};
		int colorFrameWidth= attributes.getColorFrameWidth();
		int colorFrameHeight= attributes.getColorFrameHeight();
		if (u != null && v != null && colorFrameWidth > 0 && colorFrameHeight > 0) {
			counter1= -1;
			for (int h=0; h < imageHeight; h++) {
				for (int w=0; w < imageWidth; w++) {
					counter1++;
					int index= (int)playerIndex[counter1];
					if (index >= 0) {
						float x1= u[w][h];
						float y1= v[w][h];
						if (XY_Tools.isFinite(x1) && XY_Tools.isFinite(y1)) {
							int x2= XY_Tools.uToColorX(x1,colorFrameWidth,false);
							int y2= XY_Tools.vToColorY(y1,colorFrameHeight,false);
							totalColorDimensions[index].refineRectangleBounds(x2,y2);
						}
					}
				}
			}
		};
		if (skeletons != null) {
			for (int n=0; n < skeletons.length; n++) {
				skeletonsDepthDimensions[n].computeSkeletonDepthRectangleBounds(skeletons[n],attributes);
			};
			if (u != null && v != null && colorFrameWidth > 0 && colorFrameHeight > 0) {
				for (int n=0; n < skeletons.length; n++) {
					skeletonsColorDimensions[n].computeSkeletonColorRectangleBounds(skeletons[n],u,v,attributes);
				}
			}
		};
		if (xyz != null) {
			for (int n=0; n < bandLength; n++) {
				int index= (int)playerIndex[n];
				if (index >= 0) {
					float x0= xyz[n*3];
					float y0= xyz[n*3+1];
					float z0= xyz[n*3+2];
					totalDepthDimensions[index].refineParallelepipedBounds(x0,y0,z0);
					totalColorDimensions[index].refineParallelepipedBounds(x0,y0,z0);
				}
			};
			for (int n=0; n < totalDepthDimensions.length; n++) {
				totalDepthDimensions[n].computeTotalDepthParallelepiped(attributes);
			};
			if (u != null && v != null && colorFrameWidth > 0 && colorFrameHeight > 0) {
				for (int n=0; n < totalDepthDimensions.length; n++) {
					totalColorDimensions[n].computeTotalColorParallelepiped(u,v,attributes);
				}
			}
		};
		if (skeletons != null) {
			for (int n=0; n < skeletons.length; n++) {
				skeletonsDepthDimensions[n].computeSkeletonDepthParallelepiped(skeletons[n],attributes);
			};
			if (u != null && v != null && colorFrameWidth > 0 && colorFrameHeight > 0) {
				for (int n=0; n < skeletonsDepthDimensions.length; n++) {
					skeletonsColorDimensions[n].computeSkeletonColorParallelepiped(skeletons[n],u,v,attributes);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int getSize() {
		return numberOfSkeletons;
	}
	@Override
	public PlayerDimensionsInterface[] getTotalDepthDimensions() {
		return totalDepthDimensions;
	}
	@Override
	public PlayerDimensionsInterface getTotalDepthDimensions(int n) {
		if (totalDepthDimensions != null) {
			return totalDepthDimensions[n];
		} else {
			return null;
		}
	}
	@Override
	public PlayerDimensionsInterface[] getSkeletonsDepthDimensions() {
		return skeletonsDepthDimensions;
	}
	@Override
	public PlayerDimensionsInterface getSkeletonDepthDimensions(int n) {
		if (skeletonsDepthDimensions != null) {
			return skeletonsDepthDimensions[n];
		} else {
			return null;
		}
	}
	@Override
	public PlayerDimensionsInterface[] getTotalColorDimensions() {
		return totalColorDimensions;
	}
	@Override
	public PlayerDimensionsInterface getTotalColorDimensions(int n) {
		if (totalColorDimensions != null) {
			return totalColorDimensions[n];
		} else {
			return null;
		}
	}
	@Override
	public PlayerDimensionsInterface[] getSkeletonsColorDimensions() {
		return skeletonsColorDimensions;
	}
	@Override
	public PlayerDimensionsInterface getSkeletonColorDimensions(int n) {
		if (skeletonsColorDimensions != null) {
			return skeletonsColorDimensions[n];
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public SkeletonDimensions getSkeletonDimensions(int n) {
		PlayerDimensionsInterface totalDepthDimensions= getTotalDepthDimensions(n);
		PlayerDimensionsInterface skeletonDepthDimensions= getSkeletonDepthDimensions(n);
		PlayerDimensionsInterface totalColorDimensions= getTotalColorDimensions(n);
		PlayerDimensionsInterface skeletonColorDimensions= getSkeletonColorDimensions(n);
		return new SkeletonDimensions(totalDepthDimensions,skeletonDepthDimensions,totalColorDimensions,skeletonColorDimensions);
	}
}
