// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface ForegroundPointCloudInterface {
	//
	public void refineBounds(int w, int h);
	public void fillMatrix(float[] xyz, int selectedIndex, byte[] payerIndex);
	//
	public void getValues(float[] xyz);
	//
	public int getMinimalX();
	public int getMaximalX();
	public int getMinimalY();
	public int getMaximalY();
	//
	public float[][] getMatrixX();
	public float[][] getMatrixY();
	public float[][] getMatrixZ();
	//
	public int getFrameWidth();
	public int getFrameHeight();
}
