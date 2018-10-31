// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.util.Arrays;

public class KinectForegroundPointCloudsFrame extends KinectPointCloudsFrame implements KinectForegroundPointCloudsFrameInterface {
	//
	protected ForegroundPointCloud[] pointClouds;
	protected transient float[] xyz;
	//
	private static final long serialVersionUID= 0xBA203E2FCF66842DL; // -5034956008336227283L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectForegroundPointCloudsFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectForegroundPointCloudsFrame(
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			float[] givenXYZ,
			byte[][] givenMappedRed,
			byte[][] givenMappedGreen,
			byte[][] givenMappedBlue,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			byte[] givenPlayerIndex,
			KinectFrameBaseAttributesInterface givenAttributes) {
		super(	givenSerialNumber,
			givenTargetFrameNumber,
			givenColorFrameNumber,
			givenSkeletonsFrameNumber,
			givenTargetFrameTime,
			givenColorFrameTime,
			givenSkeletonsFrameTime,
			null,
			givenMappedRed,
			givenMappedGreen,
			givenMappedBlue,
			givenSkeletons,
			givenDimensions,
			givenPlayerIndex,
			givenAttributes);
		xyz= givenXYZ;
		int expectedNumberOfSkeletons= givenAttributes.getNumberOfSkeletons();
		pointClouds= new ForegroundPointCloud[expectedNumberOfSkeletons];
		for (int n=0; n < expectedNumberOfSkeletons; n++) {
			pointClouds[n]= new ForegroundPointCloud();
		};
		FrameSize frameSize= FrameSize.computePlayerIndexFrameSize(givenPlayerIndex);
		int frameWidth= frameSize.width;
		int frameHeight= frameSize.height;
		int counter1= -1;
		for (int h=0; h < frameHeight; h++) {
			for (int w=0; w < frameWidth; w++) {
				counter1++;
				int index= givenPlayerIndex[counter1];
				if (index >= 0) {
					pointClouds[index].refineBounds(w,h);
				}
			}
		};
		for (int n=0; n < expectedNumberOfSkeletons; n++) {
			pointClouds[n].fillMatrix(givenXYZ,n,givenPlayerIndex);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ForegroundPointCloud[] getPointClouds() {
		return pointClouds;
	}
	public float[] getXYZ() {
		if (xyz != null) {
			return xyz;
		} else {
			int frameWidth= getDepthFrameWidth();
			int frameHeight= getDepthFrameHeight();
			float[] xyz= new float[frameWidth*frameHeight*3];
			int expectedNumberOfSkeletons= getNumberOfSkeletons();
			Arrays.fill(xyz,(float)Float.NaN);
			for (int n=0; n < expectedNumberOfSkeletons; n++) {
				pointClouds[n].getValues(xyz);
			};
			return xyz;
		}
	}
}
