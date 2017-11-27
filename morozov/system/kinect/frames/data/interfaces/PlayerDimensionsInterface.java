// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface PlayerDimensionsInterface {
	//
	public void refineRectangleBounds(int x, int y);
	public void computeSkeletonDepthRectangleBounds(GeneralSkeletonInterface skeleton, KinectFrameBaseAttributesInterface attributes);
	public void computeSkeletonColorRectangleBounds(GeneralSkeletonInterface skeleton, float[][] u, float[][] v, KinectFrameBaseAttributesInterface attributes);
	public void refineParallelepipedBounds(float x, float y, float z);
	//
	public void computeTotalDepthParallelepiped(KinectFrameBaseAttributesInterface attributes);
	public void computeSkeletonDepthParallelepiped(GeneralSkeletonInterface skeleton, KinectFrameBaseAttributesInterface attributes);
	public void computeTotalColorParallelepiped(float[][] u, float[][] v, KinectFrameBaseAttributesInterface attributes);
	public void computeSkeletonColorParallelepiped(GeneralSkeletonInterface skeleton, float[][] u, float[][] v, KinectFrameBaseAttributesInterface attributes);
	//
	public void projectParallelepipedToColorImage(float[][] u, float[][] v, int colorFrameWidth, int colorFrameHeight);
	//
	public int getIdentifier();
	public boolean areInitialized_2D_Bounds();
	public int getMinimalX_2D();
	public int getMaximalX_2D();
	public int getMinimalY_2D();
	public int getMaximalY_2D();
	//
	public boolean areInitialized_Depth3D_Bounds();
	public float getMinimalX_3D();
	public float getMaximalX_3D();
	public float getMinimalY_3D();
	public float getMaximalY_3D();
	public float getMinimalZ_3D();
	public float getMaximalZ_3D();
	//
	public boolean isInitialized_DepthParallelepiped();
	public boolean isInitialized_ColorParallelepiped();
	public XY_Interface getXY11();
	public XY_Interface getXY12();
	public XY_Interface getXY13();
	public XY_Interface getXY14();
	public XY_Interface getXY21();
	public XY_Interface getXY22();
	public XY_Interface getXY23();
	public XY_Interface getXY24();
}
