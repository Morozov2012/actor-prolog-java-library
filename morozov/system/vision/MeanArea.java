// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class MeanArea {
	public double minimalCenterX;
	public double maximalCenterX;
	public double minimalCenterY;
	public double maximalCenterY;
	public double meanCenterX;
	public double meanCenterY;
	public double minimalWidth;
	public double maximalWidth;
	public double minimalHeight;
	public double maximalHeight;
	public double meanWidth;
	public double meanHeight;
	public double minimalArea;
	public double maximalArea;
	public double meanArea;
	public int totalQuantity= 0;
	public MeanArea() {
	}
	public void add(int x1, int x2, int y1, int y2) {
		double centerX= (x1 + x2) / 2;
		double centerY= (y1 + y2) / 2;
		int width= x2 - x1 + 1;
		int height= y2 - y1 + 1;
		double area= width * height;
		if (totalQuantity > 0) {
			if (minimalCenterX > centerX) {
				minimalCenterX= centerX;
			};
			if (maximalCenterX < centerX) {
				maximalCenterX= centerX;
			};
			if (minimalCenterY > centerY) {
				minimalCenterY= centerY;
			};
			if (maximalCenterY < centerY) {
				maximalCenterY= centerY;
			};
			meanCenterX= meanCenterX + centerX;
			meanCenterY= meanCenterY + centerY;
			if (minimalWidth > width) {
				minimalWidth= width;
			};
			if (maximalWidth < width) {
				maximalWidth= width;
			};
			if (minimalHeight > height) {
				minimalHeight= height;
			};
			if (maximalHeight < height) {
				maximalHeight= height;
			};
			meanWidth= meanWidth + width;
			meanHeight= meanHeight + height;
			if (minimalArea > area) {
				minimalArea= area;
			};
			if (maximalArea < area) {
				maximalArea= area;
			};
			meanArea= meanArea + area;
		} else {
			minimalCenterX= centerX;
			maximalCenterX= centerX;
			minimalCenterY= centerY;
			maximalCenterY= centerY;
			meanCenterX= centerX;
			meanCenterY= centerY;
			minimalWidth= width;
			maximalWidth= width;
			minimalHeight= height;
			maximalHeight= height;
			meanWidth= width;
			meanHeight= height;
			minimalArea= area;
			maximalArea= area;
			meanArea= area;
		};
		totalQuantity++;
	}
	public double getMinimalCenterX() {
		if (totalQuantity > 0) {
			return minimalCenterX;
		} else {
			return -1;
		}
	}
	public double getMaximalCenterX() {
		if (totalQuantity > 0) {
			return maximalCenterX;
		} else {
			return -1;
		}
	}
	public double getMinimalCenterY() {
		if (totalQuantity > 0) {
			return minimalCenterY;
		} else {
			return -1;
		}
	}
	public double getMaximalCenterY() {
		if (totalQuantity > 0) {
			return maximalCenterY;
		} else {
			return -1;
		}
	}
	public double getMeanCenterX() {
		if (totalQuantity > 0) {
			return meanCenterX / totalQuantity;
		} else {
			return -1;
		}
	}
	public double getMeanCenterY() {
		if (totalQuantity > 0) {
			return meanCenterY / totalQuantity;
		} else {
			return -1;
		}
	}
	public double getMinimalWidth() {
		if (totalQuantity > 0) {
			return minimalWidth;
		} else {
			return -1;
		}
	}
	public double getMaximalWidth() {
		if (totalQuantity > 0) {
			return maximalWidth;
		} else {
			return -1;
		}
	}
	public double getMinimalHeight() {
		if (totalQuantity > 0) {
			return minimalHeight;
		} else {
			return -1;
		}
	}
	public double getMaximalHeight() {
		if (totalQuantity > 0) {
			return maximalHeight;
		} else {
			return -1;
		}
	}
	public double getMeanWidth() {
		if (totalQuantity > 0) {
			return meanWidth / totalQuantity;
		} else {
			return -1;
		}
	}
	public double getMeanHeight() {
		if (totalQuantity > 0) {
			return meanHeight / totalQuantity;
		} else {
			return -1;
		}
	}
	public double getMinimalArea() {
		if (totalQuantity > 0) {
			return minimalArea;
		} else {
			return -1;
		}
	}
	public double getMaximalArea() {
		if (totalQuantity > 0) {
			return maximalArea;
		} else {
			return -1;
		}
	}
	public double getMeanArea() {
		if (totalQuantity > 0) {
			return meanArea / totalQuantity;
		} else {
			return -1;
		}
	}
}
