// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

public enum Canvas2DScalingFactor {
	MIN {
		int computeScalingCoefficient(int width, int height) {
			return StrictMath.min(width,height);
		}
	},
	MAX {
		int computeScalingCoefficient(int width, int height) {
			return StrictMath.max(width,height);
		}
	},
	WIDTH {
		int computeScalingCoefficient(int width, int height) {
			return width;
		}
	},
	HEIGHT {
		int computeScalingCoefficient(int width, int height) {
			return height;
		}
	},
	INDEPENDENT {
		int computeScalingCoefficient(int width, int height) {
			return -1;
		}
	};
	abstract int computeScalingCoefficient(int width, int height);
}
