// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.tools;

import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import edu.ufl.digitalworlds.j4k.J4K2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class XY_Tools {
	//
	public static int illegalValue= Integer.MIN_VALUE;
	//
	///////////////////////////////////////////////////////////////
	//
	public static float xyzToX(float x0, float z0, float focalLengthX, int correctionX) {
		return xyzToX(x0,z0,focalLengthX,true,correctionX);
	}
	public static float xyzToX(float x0, float z0, float focalLengthX, boolean correctValue, int correctionX) {
		float x1= 0;
		if (z0 > J4K2.FLT_EPSILON) {
			x1= x0 * ( focalLengthX / z0 );
		};
		if (correctValue) {
			x1= x1 + correctionX;
		};
		return x1;
	}
	//
	public static float xyzToY(float y0, float z0, float focalLengthY, int correctionY) {
		return xyzToY(y0,z0,focalLengthY,true,correctionY);
	}
	public static float xyzToY(float y0, float z0, float focalLengthY, boolean correctValue, int correctionY) {
		float y1= 0;
		if (z0 > J4K2.FLT_EPSILON) {
			y1= y0 * ( focalLengthY / z0 );
		};
		if (correctValue) {
			y1= y1 + correctionY;
		};
		return y1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static XY projectXYZ(float x0, float y0, float z0, float focalLengthX, float focalLengthY, int imageWidth, int imageHeight, int correctionX, int correctionY) {
		float x1= xyzToX(x0,z0,focalLengthX,correctionX);
		float y1= xyzToY(y0,z0,focalLengthY,correctionY);
		int x2= imageWidth - centeredXtoIndexUV(x1,imageWidth,false);
		int y2= centeredYtoIndexUV(y1,imageHeight,false);
		return new XY(x2,y2);
	}
	//
	public static XY projectToColorImage(XY_Interface xy, float[][] u, float[][] v, int colorFrameWidth, int colorFrameHeight) {
		int x= xy.getX();
		int y= xy.getY();
		int uvWidth= u.length;
		if (uvWidth > 0) {
			int uvHeight= u[0].length;
			int x2= uvWidth - x;
			int y2= y;
			float x3= getOrComputeU(u,x2,y2,uvWidth,uvHeight);
			float y3= getOrComputeV(v,x2,y2,uvWidth,uvHeight);
			if (isFinite(x3) && isFinite(y3)) {
				int x4= uToColorX(x3,colorFrameWidth,false);
				int y4= vToColorY(y3,colorFrameHeight,false);
				if (isLegal(x4) && isLegal(y4)) {
					return new XY(x4,y4);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int centeredXtoIndexUV(float x1, int imageWidth, boolean truncateValue) {
		int x2= (int)( imageWidth / 2 + x1 );
		if (truncateValue) {
			if (x2 < 0) {
				x2= 0;
			} else if (x2 >= imageWidth) {
				x2= imageWidth - 1;
			}
		};
		return x2;
	}
	public static int centeredYtoIndexUV(float y1, int imageHeight, boolean truncateValue) {
		int y2= (int)( imageHeight / 2 - y1 );
		if (truncateValue) {
			if (y2 < 0) {
				y2= 0;
			} else if (y2 >= imageHeight) {
				y2= imageHeight - 1;
			}
		};
		return y2;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static float getOrComputeU(float[][] u, int x1, int y1, int imageWidth, int imageHeight) {
		if (0 <= x1 && x1 < imageWidth && 0 <= y1 && y1 < imageHeight) {
			float value= u[x1][y1];
			if (isFinite(value)) {
				return value;
			} else {
				return (float)x1 / imageWidth;
			}
		} else {
			return (float)x1 / imageWidth;
		}
	}
	public static float getOrComputeV(float[][] v, int x1, int y1, int imageWidth, int imageHeight) {
		if (0 <= x1 && x1 < imageWidth && 0 <= y1 && y1 < imageHeight) {
			float value= v[x1][y1];
			if (isFinite(value)) {
				return value;
			} else {
				return (float)y1 / imageHeight;
			}
		} else {
			return (float)y1 / imageHeight;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int uToColorIndexX(float u, int colorFrameWidth) {
		return uToColorIndexX(u,colorFrameWidth,true);
	}
	public static int uToColorIndexX(float u, int colorFrameWidth, boolean checkValue) {
		if (isFinite(u)) {
			float x1= (1 - u) * colorFrameWidth;
			int x2= colorFrameWidth - (int)x1;
			if (checkValue) {
				if (x2 < 0 || x2 >= colorFrameWidth) {
					return illegalValue;
				} else {
					return x2;
				}
			} else {
				return x2;
			}
		} else {
			return illegalValue;
		}
	}
	//
	public static int vToColorIndexY(float v, int colorFrameHeight) {
		return vToColorIndexY(v,colorFrameHeight,true);
	}
	public static int vToColorIndexY(float v, int colorFrameHeight, boolean checkValue) {
		if (isFinite(v)) {
			float y1= v * colorFrameHeight;
			int y2= (int)y1;
			if (checkValue) {
				if (y2 < 0 || y2 >= colorFrameHeight) {
					return illegalValue;
				} else {
					return y2;
				}
			} else {
				return y2;
			}
		} else {
			return illegalValue;
		}
	}
	//
	public static int uToColorX(float u, int colorFrameWidth, boolean checkValue) {
		if (isFinite(u)) {
			float x1= u * colorFrameWidth;
			int x2= colorFrameWidth - (int)x1;
			if (checkValue) {
				if (x2 < 0 || x2 >= colorFrameWidth) {
					return illegalValue;
				} else {
					return x2;
				}
			} else {
				return x2;
			}
		} else {
			return illegalValue;
		}
	}
	public static int vToColorY(float v, int colorFrameHeight, boolean checkValue) {
		if (isFinite(v)) {
			float y1= v * colorFrameHeight;
			int y2= (int)y1;
			if (checkValue) {
				if (y2 < 0 || y2 >= colorFrameHeight) {
					return illegalValue;
				} else {
					return y2;
				}
			} else {
				return y2;
			}
		} else {
			return illegalValue;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static boolean isLegal(int value) {
		if (value==illegalValue) {
			return false;
		} else {
			return true;
		}
	}
	//
	public static boolean isFinite(float v) {
		if (Float.isNaN(v) || Float.isInfinite(v)) {
			return false;
		} else {
			return true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String toString(XY_Interface xy) {
		int x= xy.getX();
		int y= xy.getY();
		return "(" + Integer.toString(x) + ";" + Integer.toString(y) + ")";
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(XY_Interface xy, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		int x= xy.getX();
		int y= xy.getY();
		writer.write(String.format(locale,"%s\t%s\n",x,y));
	}
}
