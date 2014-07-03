// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.vision;

abstract class Interpolator2D {
	public int meshWidth= 7;
	public int meshHeight= 7;
	public double meshEpsilon= 0.1;
	// public double meshEpsilon= 2;
	public double[][] computeMatrix(int matrixHeight, int matrixWidth, double[][] extraArguments1, double[][] extraArguments2) {
		double[][] matrix= new double[matrixHeight][matrixWidth];
		if (extraArguments1.length <= 1) {
			double defaultValue= computeDefaultValue();
			for (int pY=0; pY < matrixHeight; pY++) {
				for (int pX=0; pX < matrixWidth; pX++) {
					matrix[pY][pX]= defaultValue;
				}
			};
			return matrix;
		};
		double[][] k11= new double[meshHeight+1][meshWidth+1];
		double[][] k12= new double[meshHeight+1][meshWidth+1];
		double[][] k21= new double[meshHeight+1][meshWidth+1];
		double[][] k22= new double[meshHeight+1][meshWidth+1];
		for (int pY=0; pY < meshHeight+1; pY++) {
			for (int pX=0; pX < meshWidth+1; pX++) {
				double distance11= StrictMath.hypot(pX,pY) + 1;
				if (distance11 > 0) {
					k11[pY][pX]= 1 / (distance11*distance11);
				} else {
					k11[pY][pX]= 1;
				};
				double distance12= StrictMath.hypot(pX-meshWidth,pY) + 1;
				if (distance12 > 0) {
					k12[pY][pX]= 1 / (distance12*distance12);
				} else {
					k12[pY][pX]= 1;
				};
				double distance21= StrictMath.hypot(pX,pY-meshHeight) + 1;
				if (distance21 > 0) {
					k21[pY][pX]= 1 / (distance21*distance21);
				} else {
					k21[pY][pX]= 1;
				};
				double distance22= StrictMath.hypot(pX-meshWidth,pY-meshHeight) + 1;
				if (distance22 > 0) {
					k22[pY][pX]= 1 / (distance22*distance22);
				} else {
					k22[pY][pX]= 1;
				}
			}
		};
		for (int pY=0; pY < meshHeight+1; pY++) {
			for (int pX=0; pX < meshWidth+1; pX++) {
				double sum= k11[pY][pX] + k12[pY][pX] + k21[pY][pX] + k22[pY][pX];
				k11[pY][pX]= k11[pY][pX] / sum;
				k12[pY][pX]= k12[pY][pX] / sum;
				k21[pY][pX]= k21[pY][pX] / sum;
				k22[pY][pX]= k22[pY][pX] / sum;
			}
		};
		int nMeshHorizontalLines= ((matrixWidth-1) / meshWidth) + 2;
		int nMeshVerticalLines= ((matrixHeight-1) / meshHeight) + 2;
		double[][] mesh= new double[nMeshVerticalLines][nMeshHorizontalLines];
		for (int nY=0; nY < nMeshVerticalLines; nY++) {
			int pY= nY*meshHeight;
			for (int nX=0; nX < nMeshHorizontalLines; nX++) {
				int pX= nX*meshWidth;
				mesh[nY][nX]= computeMatrixItem(pX+1,pY+1,extraArguments1,extraArguments2);
			}
		};
		for (int nY=0; nY < nMeshVerticalLines-1; nY++) {
			int pY= nY*meshHeight;
			for (int nX=0; nX < nMeshHorizontalLines-1; nX++) {
				int pX= nX*meshWidth;
				double a11= mesh[nY][nX];
				double a12= mesh[nY][nX+1];
				double a21= mesh[nY+1][nX];
				double a22= mesh[nY+1][nX+1];
				double delta1= a11-a12;
				if (delta1 < 0) {
					delta1= -delta1;
				};
				double delta2= a11-a21;
				if (delta2 < 0) {
					delta2= -delta2;
				};
				double delta3= a22-a12;
				if (delta3 < 0) {
					delta3= -delta3;
				};
				double delta4= a22-a21;
				if (delta4 < 0) {
					delta4= -delta4;
				};
				double meanDelta= (delta1+delta2+delta3+delta4) / 4;
				if (meanDelta > meshEpsilon) {
					for (int dX=0; dX <= meshWidth-1; dX++) {
						if (pX+dX >= matrixWidth) {
							continue;
						};
						for (int dY=0; dY <= meshHeight-1; dY++) {
							if (pY+dY >= matrixHeight) {
								continue;
							};
							if (dX==0 && dY==0) {
								matrix[pY+dY][pX+dX]= a11;
							} else {
								matrix[pY+dY][pX+dX]= computeMatrixItem(pX+dX+1,pY+dY+1,extraArguments1,extraArguments2);
							}
						}
					}
				} else {
					for (int dX=0; dX <= meshWidth-1; dX++) {
						if (pX+dX >= matrixWidth) {
							continue;
						};
						for (int dY=0; dY <= meshHeight-1; dY++) {
							if (pY+dY >= matrixHeight) {
								continue;
							};
							if (dX==0 && dY==0) {
								matrix[pY+dY][pX+dX]= a11;
							} else {
								double value= a11*k11[dY][dX] + a12*k12[dY][dX] + a21*k21[dY][dX] + a22*k22[dY][dX];
								matrix[pY+dY][pX+dX]= value;
							}
						}
					}
				}
			}
		};
		return matrix;
	}
	public abstract double computeMatrixItem(int pX, int pY, double[][] extraArguments1, double[][] extraArguments2);
	public abstract double computeDefaultValue();
}
