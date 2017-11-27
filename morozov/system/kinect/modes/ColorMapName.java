// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

public enum ColorMapName {
	//
	AUTUMN {
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= maximalColorValue;
				cm[2][0]= 0;
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				int green= maximalColorValue * n / p1;
				if (green > maximalColorValue) {
					green= maximalColorValue;
				};
				cm[0][n]= maximalColorValue;
				cm[1][n]= green;
				cm[2][n]= 0;
			};
			return cm;
		}
	},
	BONE {
		public int[][] toColors(int size) {
			int[][] cm= HOT.toColors(size);
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= maximalColorValue;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				int red1= cm[0][n];
				int green1= cm[1][n];
				int blue1= cm[2][n];
				float v= (float)maximalColorValue * n / p1;
				int red2= (int)((blue1 + v * 7) / 8);
				int green2= (int)((green1 + v * 7) / 8);
				int blue2= (int)((red1 + v * 7) / 8);
				if (red2 > maximalColorValue) {
					red2= maximalColorValue;
				};
				if (green2 > maximalColorValue) {
					green2= maximalColorValue;
				};
				if (blue2 > maximalColorValue) {
					blue2= maximalColorValue;
				};
				cm[0][n]= red2;
				cm[1][n]= green2;
				cm[2][n]= blue2;
			};
			return cm;
		}
	},
	BRIGHT_RAINBOW {
		public int getRepetitionFactor() {
			return 5;
		}
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= 0;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			if (size==2) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= 0;
				cm[2][0]= 0;
				cm[0][1]= maximalColorValue;
				cm[1][1]= 0;
				cm[2][1]= maximalColorValue;
				return cm;
			};
			float k= (float)size / 5;
			int p2= (int)k;
			int p3= (int)(2*k);
			int p4= (int)(3*k);
			int p5= (int)(4*k);
			int p6= (int)(5*k);
			int p7= (int)(6*k);
			for (int n=0; n<=p2; n++) {
				int green;
				if (p2 > 0) {
					green= maximalColorValue * n / p2;
					if (green > maximalColorValue) {
						green= maximalColorValue;
					}
				} else {
					green= maximalColorValue;
				};
				cm[0][n]= maximalColorValue;
				cm[1][n]= green;
				cm[2][n]= 0;
			};
			for (int n=p2+1; n<=p3; n++) {
				int red= maximalColorValue*(p3-n)/(p3-p2);
				if (red < 0) {
					red= 0;
				};
				cm[0][n]= red;
				cm[1][n]= maximalColorValue;
				cm[2][n]= 0;
			};
			for (int n=p3+1; n<=p4; n++) {
				int blue= maximalColorValue*(n-p3)/(p4-p3);
				if (blue > maximalColorValue) {
					blue= maximalColorValue;
				};
				cm[0][n]= 0;
				cm[1][n]= maximalColorValue;
				cm[2][n]= blue;
			};
			for (int n=p4+1; n<=p5; n++) {
				int green= maximalColorValue*(p5-n)/(p5-p4);
				if (green < 0) {
					green= 0;
				};
				cm[0][n]= 0;
				cm[1][n]= green;
				cm[2][n]= maximalColorValue;
			};
			for (int n=p5+1; n<=p1; n++) {
				int red= maximalColorValue*(n-p5)/(p6-p5);
				if (red > maximalColorValue) {
					red= maximalColorValue;
				};
				cm[0][n]= red;
				cm[1][n]= 0;
				cm[2][n]= maximalColorValue;
			};
			return cm;
		}
	},
	COOL {
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= 0;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				int red= maximalColorValue * n / p1;
				if (red > maximalColorValue) {
					red= maximalColorValue;
				};
				int green= maximalColorValue * (p1-n) / p1;
				if (green < 0) {
					green= 0;
				};
				cm[0][n]= red;
				cm[1][n]= green;
				cm[2][n]= maximalColorValue;
			};
			return cm;
		}
	},
	COPPER {
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			double k1= 1.2500 * maximalColorValue;
			double k2= 0.7812 * maximalColorValue;
			double k3= 0.4975 * maximalColorValue;
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= (int)k2;
				cm[2][0]= (int)k3;
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				int red= (int)(k1 * n / p1);
				if (red > maximalColorValue) {
					red= maximalColorValue;
				};
				int green= (int)(k2 * n / p1);
				if (green > maximalColorValue) {
					green= maximalColorValue;
				};
				int blue= (int)(k3 * n / p1);
				if (blue > maximalColorValue) {
					blue= maximalColorValue;
				};
				cm[0][n]= red;
				cm[1][n]= green;
				cm[2][n]= blue;
			};
			return cm;
		}
	},
	GRAY {
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= maximalColorValue;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				int gray= maximalColorValue * n / p1;
				if (gray > maximalColorValue) {
					gray= maximalColorValue;
				};
				cm[0][n]= gray;
				cm[1][n]= gray;
				cm[2][n]= gray;
			};
			return cm;
		}
	},
	HOT {
		public int getRepetitionFactor() {
			return 8;
		}
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= maximalColorValue;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			if (size==2) {
				cm[0][0]= 0;
				cm[1][0]= 0;
				cm[2][0]= 0;
				cm[0][1]= maximalColorValue;
				cm[1][1]= maximalColorValue;
				cm[2][1]= maximalColorValue;
				return cm;
			};
			double k= 3.0 / 8.0 * size;
			int p2= (int)k;
			int p3= (int)(k-1);
			int p4= (int)(2*k-1);
			int p5= (int)(size-1-2*k+1);
			for (int n=0; n<=p1; n++) {
				int red;
				int green;
				int blue;
				if (n <= p3) {
					if (p3 > 0) {
						red= maximalColorValue * n / p3;
						if (red > maximalColorValue) {
							red= maximalColorValue;
						}
					} else {
						red= maximalColorValue;
					};
					green= 0;
					blue= 0;
				} else {
					red= maximalColorValue;
					if (n <= p4) {
						if (p2 > 0) {
							green= maximalColorValue * (n-p3) / p2;
							if (green > maximalColorValue) {
								green= maximalColorValue;
							}
						} else {
							green= maximalColorValue;
						};
						blue= 0;
					} else {
						green= maximalColorValue;
						if (p5 > 0) {
							blue= maximalColorValue * (n-p4) / p5;
							if (blue > maximalColorValue) {
								blue= maximalColorValue;
							}
						} else {
							blue= maximalColorValue;
						}
					}
				};
				cm[0][n]= red;
				cm[1][n]= green;
				cm[2][n]= blue;
			};
			return cm;
		}
	},
	HSV {
		public int getRepetitionFactor() {
			return 6;
		}
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= 0;
				cm[2][0]= 0;
				return cm;
			};
			if (size==2) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= 0;
				cm[2][0]= 0;
				cm[0][1]= maximalColorValue;
				cm[1][1]= 0;
				cm[2][1]= 0;
				return cm;
			};
			float k= (float)size / 6;
			int p2= (int)k;
			int p3= (int)(2*k);
			int p4= (int)(3*k);
			int p5= (int)(4*k);
			int p6= (int)(5*k);
			int p7= (int)(6*k);
			for (int n=0; n<=p2; n++) {
				int green;
				if (p2 > 0) {
					green= maximalColorValue * n / p2;
					if (green > maximalColorValue) {
						green= maximalColorValue;
					}
				} else {
					green= maximalColorValue;
				};
				cm[0][n]= maximalColorValue;
				cm[1][n]= green;
				cm[2][n]= 0;
			};
			for (int n=p2+1; n<=p3; n++) {
				int red= maximalColorValue*(p3-n)/(p3-p2);
				if (red < 0) {
					red= 0;
				};
				cm[0][n]= red;
				cm[1][n]= maximalColorValue;
				cm[2][n]= 0;
			};
			for (int n=p3+1; n<=p4; n++) {
				int blue= maximalColorValue*(n-p3)/(p4-p3);
				if (blue > maximalColorValue) {
					blue= maximalColorValue;
				};
				cm[0][n]= 0;
				cm[1][n]= maximalColorValue;
				cm[2][n]= blue;
			};
			for (int n=p4+1; n<=p5; n++) {
				int green= maximalColorValue*(p5-n)/(p5-p4);
				if (green < 0) {
					green= 0;
				};
				cm[0][n]= 0;
				cm[1][n]= green;
				cm[2][n]= maximalColorValue;
			};
			for (int n=p5+1; n<=p6; n++) {
				int red= maximalColorValue*(n-p5)/(p6-p5);
				if (red > maximalColorValue) {
					red= maximalColorValue;
				};
				cm[0][n]= red;
				cm[1][n]= 0;
				cm[2][n]= maximalColorValue;
			};
			for (int n=p6+1; n<=p1; n++) {
				int blue= maximalColorValue*(p7-n)/(p7-p6);
				if (blue < 0) {
					blue= 0;
				};
				cm[0][n]= maximalColorValue;
				cm[1][n]= 0;
				cm[2][n]= blue;
			};
			return cm;
		}
	},
	JET {
		public int getRepetitionFactor() {
			return 8;
		}
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue / 2;
				cm[1][0]= 0;
				cm[2][0]= 0;
				return cm;
			};
			if (size==2) {
				cm[0][0]= 0;
				cm[1][0]= 0;
				cm[2][0]= maximalColorValue / 2;
				cm[0][1]= maximalColorValue / 2;
				cm[1][1]= 0;
				cm[2][1]= 0;
				return cm;
			};
			float k= (float)size / 8;
			int p3= (int)k;
			int p2= (int)(p3+k);
			int p4= (int)(3*k);
			int p5= (int)(5*k);
			int p6= (int)(7*k);
			int p7= (int)(9*k);
			for (int n=0; n<=p3; n++) {
				int blue;
				if (p2 > 0) {
					blue= (int)(maximalColorValue*(n+k)/p2);
					if (blue > maximalColorValue) {
						blue= maximalColorValue;
					}
				} else {
					blue= maximalColorValue;
				};
				cm[0][n]= 0;
				cm[1][n]= 0;
				cm[2][n]= blue;
			};
			for (int n=p3+1; n<=p4; n++) {
				int green= maximalColorValue*(n-p3)/(p4-p3);
				if (green > maximalColorValue) {
					green= maximalColorValue;
				};
				cm[0][n]= 0;
				cm[1][n]= green;
				cm[2][n]= maximalColorValue;
			};
			for (int n=p4+1; n<=p5; n++) {
				int red= maximalColorValue*(n-p4)/(p5-p4);
				if (red > maximalColorValue) {
					red= maximalColorValue;
				};
				int blue= maximalColorValue*(p5-n)/(p5-p4);
				if (blue < 0) {
					blue= 0;
				};
				cm[0][n]= red;
				cm[1][n]= maximalColorValue;
				cm[2][n]= blue;
			};
			for (int n=p5+1; n<=p6; n++) {
				int green= maximalColorValue*(p6-n)/(p6-p5);
				if (green < 0) {
					green= 0;
				};
				cm[0][n]= maximalColorValue;
				cm[1][n]= green;
				cm[2][n]= 0;
			};
			for (int n=p6+1; n<=p1; n++) {
				int red= maximalColorValue*(p7-n)/(p7-p6);
				if (red < 0) {
					red= 0;
				};
				cm[0][n]= red;
				cm[1][n]= 0;
				cm[2][n]= 0;
			};
			return cm;
		}
	},
	OCEAN {
		public int getRepetitionFactor() {
			return 3;
		}
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= maximalColorValue;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			if (size==2) {
				cm[0][0]= 0;
				cm[1][0]= 0;
				cm[2][0]= 0;
				cm[0][1]= maximalColorValue;
				cm[1][1]= maximalColorValue;
				cm[2][1]= maximalColorValue;
				return cm;
			};
			float k= (float)size / 3;
			int p2= (int)k;
			int p3= (int)(2*k);
			for (int n=0; n<=p2; n++) {
				int blue= (int)(maximalColorValue * n / p1);
				if (blue > maximalColorValue) {
					blue= maximalColorValue;
				};
				cm[0][n]= 0;
				cm[1][n]= 0;
				cm[2][n]= blue;
			};
			for (int n=p2+1; n<=p3; n++) {
				int green= (int)(maximalColorValue*(n-p2)/(p1-p2));
				if (green > maximalColorValue) {
					green= maximalColorValue;
				};
				int blue= (int)(maximalColorValue * n / p1);
				if (blue > maximalColorValue) {
					blue= maximalColorValue;
				};
				cm[0][n]= 0;
				cm[1][n]= green;
				cm[2][n]= blue;
			};
			for (int n=p3+1; n<=p1; n++) {
				int red= (int)(maximalColorValue*(n-p3)/(p1-p3));
				if (red > maximalColorValue) {
					red= maximalColorValue;
				};
				int green= (int)(maximalColorValue*(n-p2)/(p1-p2));
				if (green > maximalColorValue) {
					green= maximalColorValue;
				};
				int blue= (int)(maximalColorValue * n / p1);
				if (blue > maximalColorValue) {
					blue= maximalColorValue;
				};
				cm[0][n]= red;
				cm[1][n]= green;
				cm[2][n]= blue;
			};
			return cm;
		}
	},
	PINK {
		public int[][] toColors(int size) {
			int[][] cm= HOT.toColors(size);
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= maximalColorValue;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				float red1= (float)cm[0][n] / maximalColorValue;
				float green1= (float)cm[1][n] / maximalColorValue;
				float blue1= (float)cm[2][n] / maximalColorValue;
				float v= (float)n / p1;
				int red2= (int)(maximalColorValue*StrictMath.sqrt((red1 + v * 2) / 3));
				int green2= (int)(maximalColorValue*StrictMath.sqrt((green1 + v * 2) / 3));
				int blue2= (int)(maximalColorValue*StrictMath.sqrt((blue1 + v * 2) / 3));
				if (red2 > maximalColorValue) {
					red2= maximalColorValue;
				};
				if (green2 > maximalColorValue) {
					green2= maximalColorValue;
				};
				if (blue2 > maximalColorValue) {
					blue2= maximalColorValue;
				};
				cm[0][n]= red2;
				cm[1][n]= green2;
				cm[2][n]= blue2;
			};
			return cm;
		}
	},
	PRISM {
		public int getRepetitionFactor() {
			return 6;
		}
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue * 2 / 3;
				cm[1][0]= 0;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			int bandWidth= size / 10 / 6;
			int k= 0;
			int m= 0;
			for (int n=0; n<=p1; n++) {
				if (k >= 6) {
					k= 0;
				};
				int red;
				int green;
				int blue;
				switch (k) {
				case 0:
					red= maximalColorValue;
					green= 0;
					blue= 0;
					break;
				case 1:
					red= maximalColorValue;
					green= maximalColorValue/2;
					blue= 0;
					break;
				case 2:
					red= maximalColorValue;
					green= maximalColorValue;
					blue= 0;
					break;
				case 3:
					red= 0;
					green= maximalColorValue;
					blue= 0;
					break;
				case 4:
					red= 0;
					green= 0;
					blue= maximalColorValue;
					break;
				default:
					red= maximalColorValue * 2 / 3;
					green= 0;
					blue= maximalColorValue;
					break;
				};
				cm[0][n]= red;
				cm[1][n]= green;
				cm[2][n]= blue;
				m= m + 1;
				if (m >= bandWidth) {
					k= k + 1;
					m= 0;
				}
			};
			return cm;
		}
	},
	SOFT_RAINBOW {
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= 0;
				cm[2][0]= maximalColorValue;
				return cm;
			};
			double f= 2 * StrictMath.PI;
			double shift1= 2 * StrictMath.PI / 3;
			double shift0= - (shift1 + StrictMath.PI/2);
			for (int n=0; n<=p1; n++) {
				double x= (1.0 - (double)n/p1) * (1.0-1.0/6);
				int red= (int)StrictMath.round(maximalColorValue*(StrictMath.sin(x*f + shift0) + 1) / 2);
				int green= (int)StrictMath.round(maximalColorValue*(StrictMath.sin(x*f + shift1 + shift0) + 1) / 2);
				int blue= (int)StrictMath.round(maximalColorValue*(StrictMath.sin(x*f + 2*shift1 + shift0) + 1) / 2);
				cm[0][n]= red;
				cm[1][n]= green;
				cm[2][n]= blue;
			};
			return cm;
		}
	},
	SPRING {
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= maximalColorValue;
				cm[2][0]= 0;
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				int green= maximalColorValue * n / p1;
				if (green > maximalColorValue) {
					green= maximalColorValue;
				};
				int blue= maximalColorValue * (p1-n) / p1;
				if (blue < 0) {
					blue= 0;
				};
				cm[0][n]= maximalColorValue;
				cm[1][n]= green;
				cm[2][n]= blue;
			};
			return cm;
		}
	},
	SUMMER {
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= maximalColorValue;
				cm[1][0]= maximalColorValue;
				cm[2][0]= (int)(maximalColorValue * 0.4);
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				int red= maximalColorValue * n / p1;
				if (red > maximalColorValue) {
					red= maximalColorValue;
				};
				int green= (int)(maximalColorValue * (0.5 + ((float)n / p1) / 2));
				if (green > maximalColorValue) {
					green= maximalColorValue;
				};
				cm[0][n]= red;
				cm[1][n]= green;
				cm[2][n]= (int)(maximalColorValue * 0.4);
			};
			return cm;
		}
	},
	WINTER {
		public int[][] toColors(int size) {
			int[][] cm= new int[3][size];
			if (size <= 0) {
				return cm;
			};
			int p1= size-1;
			if (p1 <= 0) {
				cm[0][0]= 0;
				cm[1][0]= maximalColorValue;
				cm[2][0]= (int)(maximalColorValue * 0.5);
				return cm;
			};
			for (int n=0; n<=p1; n++) {
				int green= maximalColorValue * n / p1;
				if (green > maximalColorValue) {
					green= maximalColorValue;
				};
				int blue= (int)(maximalColorValue * (0.5 + (1-(float)n/p1)/2));
				if (blue < 0) {
					blue= 0;
				};
				cm[0][n]= 0;
				cm[1][n]= green;
				cm[2][n]= blue;
			};
			return cm;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public int getRepetitionFactor() {
		return 1;
	}
	//
	abstract public int[][] toColors(int size);
	//
	///////////////////////////////////////////////////////////////
	//
	protected static int maximalColorValue= 255;
}
