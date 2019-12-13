// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;
import morozov.system.kinect.frames.tools.*;

import java.io.Serializable;

public class ForegroundPointCloud implements ForegroundPointCloudInterface, Serializable {
	//
	protected int minimalX= -1;
	protected int maximalX= -1;
	protected int minimalY= -1;
	protected int maximalY= -1;
	protected float[][] matrixX;
	protected float[][] matrixY;
	protected float[][] matrixZ;
	protected int frameWidth;
	protected int frameHeight;
	//
	private static final long serialVersionUID= 0x32267A82CB8EEF69L; // 3613710453190881129L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","ForegroundPointCloud");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void refineBounds(int w, int h) {
		if (minimalX < 0) {
			minimalX= w;
		} else {
			if (w < minimalX) {
				minimalX= w;
			}
		};
		if (maximalX < 0) {
			maximalX= w;
		} else {
			if (w > maximalX) {
				maximalX= w;
			}
		};
		if (minimalY < 0) {
			minimalY= h;
		} else {
			if (h < minimalY) {
				minimalY= h;
			}
		};
		if (maximalY < 0) {
			maximalY= h;
		} else {
			if (h > maximalY) {
				maximalY= h;
			}
		}
	}
	//
	@Override
	public void fillMatrix(float[] xyz, int selectedIndex, byte[] payerIndex) {
		if (minimalX < 0 || maximalX < 0 || minimalY < 0 || maximalY < 0) {
			return;
		};
		FrameSize frameSize= FrameSize.computeXYZFrameSize(xyz);
		frameWidth= frameSize.getWidth();
		frameHeight= frameSize.getHeight();
		int width= maximalX - minimalX + 1;
		int height= maximalY - minimalY + 1;
		matrixX= new float[width][height];
		matrixY= new float[width][height];
		matrixZ= new float[width][height];
		for (int h=0; h < height; h++) {
			for (int w=0; w < width; w++) {
				matrixX[w][h]= Float.NaN;
				matrixY[w][h]= Float.NaN;
				matrixZ[w][h]= Float.NaN;
			}
		};
		for (int h=0; h < height; h++) {
			for (int w=0; w < width; w++) {
				int x= minimalX + w;
				int y= minimalY + h;
				int counter1= (y * frameWidth) + x;
				byte currentIndex= payerIndex[counter1];
				if (selectedIndex==currentIndex) {
					matrixX[w][h]= xyz[counter1*3];
					matrixY[w][h]= xyz[counter1*3+1];
					matrixZ[w][h]= xyz[counter1*3+2];
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void getValues(float[] xyz) {
		if (minimalX < 0 || maximalX < 0 || minimalY < 0 || maximalY < 0) {
			return;
		};
		int width= maximalX - minimalX + 1;
		int height= maximalY - minimalY + 1;
		for (int h=0; h < height; h++) {
			for (int w=0; w < width; w++) {
				int x2d= minimalX + w;
				int y2d= minimalY + h;
				int counter1= (y2d * frameWidth) + x2d;
				float x3d= matrixX[w][h];
				float y3d= matrixY[w][h];
				float z3d= matrixZ[w][h];
				if (XY_Tools.isFinite(x3d) && XY_Tools.isFinite(y3d) && XY_Tools.isFinite(z3d)) {
					xyz[counter1*3]= x3d;
					xyz[counter1*3+1]= y3d;
					xyz[counter1*3+2]= z3d;
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int getMinimalX() {
		return minimalX;
	}
	@Override
	public int getMaximalX() {
		return maximalX;
	}
	@Override
	public int getMinimalY() {
		return minimalY;
	}
	@Override
	public int getMaximalY() {
		return maximalY;
	}
	//
	@Override
	public float[][] getMatrixX() {
		return matrixX;
	}
	@Override
	public float[][] getMatrixY() {
		return matrixY;
	}
	@Override
	public float[][] getMatrixZ() {
		return matrixZ;
	}
	//
	@Override
	public int getFrameWidth() {
		return frameWidth;
	}
	@Override
	public int getFrameHeight() {
		return frameHeight;
	}
}
