// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class TransformationMatrices {
	//
	protected double[][] inverseTransformationMatrix;
	protected double[][] physicalMatrixX;
	protected double[][] physicalMatrixY;
	protected double[][] characteristicMatrix;
	protected int imageWidth;
	protected int imageHeight;
	//
	protected PhiInterpolator2D phiMatrixInterpolator= new PhiInterpolator2D();
	protected SizeInterpolator2D characteristicLengthInterpolator= new SizeInterpolator2D();
	protected CoordinateXInterpolator2D coordinateXInterpolator= new CoordinateXInterpolator2D();
	protected CoordinateYInterpolator2D coordinateYInterpolator= new CoordinateYInterpolator2D();
	//
	///////////////////////////////////////////////////////////////
	//
	public TransformationMatrices(double[][] iMatrix, int width, int height) {
		inverseTransformationMatrix= iMatrix;
		imageWidth= width;
		imageHeight= height;
		double[][] phiMatrix= phiMatrixInterpolator.computeMatrix(imageHeight,imageWidth,inverseTransformationMatrix,null);
		characteristicMatrix= characteristicLengthInterpolator.computeMatrix(imageHeight,imageWidth,inverseTransformationMatrix,phiMatrix);
		if (inverseTransformationMatrix != null) {
			physicalMatrixX= coordinateXInterpolator.computeMatrix(imageHeight,imageWidth,inverseTransformationMatrix,null);
			physicalMatrixY= coordinateYInterpolator.computeMatrix(imageHeight,imageWidth,inverseTransformationMatrix,null);
		} else {
			physicalMatrixX= new double[imageHeight][imageWidth];
			physicalMatrixY= new double[imageHeight][imageWidth];
			for (int pY=0; pY < imageHeight; pY++) {
				for (int pX=0; pX < imageWidth; pX++) {
					physicalMatrixX[pY][pX]= pX;
					physicalMatrixY[pY][pX]= pY;
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[][] getCharacteristicMatrix() {
		return characteristicMatrix;
	}
	public double[][] getPhysicalMatrixX() {
		return physicalMatrixX;
	}
	public double[][] getPhysicalMatrixY() {
		return physicalMatrixY;
	}
	//
	public double getPhysicalX(int x, int y) {
		if (x < 0) {
			x= 0;
		} else if (x >= imageWidth) {
			x= imageWidth;
		};
		if (y < 0) {
			y= 0;
		} else if (y >= imageHeight) {
			y= imageHeight;
		};
		return physicalMatrixX[y][x];
	}
	public double getPhysicalY(int x, int y) {
		if (x < 0) {
			x= 0;
		} else if (x >= imageWidth) {
			x= imageWidth;
		};
		if (y < 0) {
			y= 0;
		} else if (y >= imageHeight) {
			y= imageHeight;
		};
		return physicalMatrixY[y][x];
	}
}
