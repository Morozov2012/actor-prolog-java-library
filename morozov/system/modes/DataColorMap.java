// (c) 2018 Alexei A. Morozov

package morozov.system.modes;

import morozov.system.*;

public enum DataColorMap {
	AQUA {
		public ColorMapName toColorMapName() {
			return ColorMapName.AQUA;
		}
	},
	AUTUMN {
		public ColorMapName toColorMapName() {
			return ColorMapName.AUTUMN;
		}
	},
	BLACKHOT {
		public ColorMapName toColorMapName() {
			return ColorMapName.BLACKHOT;
		}
	},
	BLAZE {
		public ColorMapName toColorMapName() {
			return ColorMapName.BLAZE;
		}
	},
	BLUERED {
		public ColorMapName toColorMapName() {
			return ColorMapName.BLUERED;
		}
	},
	BONE {
		public ColorMapName toColorMapName() {
			return ColorMapName.BONE;
		}
	},
	BRIGHT_RAINBOW {
		public ColorMapName toColorMapName() {
			return ColorMapName.BRIGHT_RAINBOW;
		}
	},
	COOL {
		public ColorMapName toColorMapName() {
			return ColorMapName.COOL;
		}
	},
	COPPER {
		public ColorMapName toColorMapName() {
			return ColorMapName.COPPER;
		}
	},
	GRAY {
		public ColorMapName toColorMapName() {
			return ColorMapName.GRAY;
		}
	},
	GREEN {
		public ColorMapName toColorMapName() {
			return ColorMapName.GREEN;
		}
	},
	HOT {
		public ColorMapName toColorMapName() {
			return ColorMapName.HOT;
		}
	},
	HSV {
		public ColorMapName toColorMapName() {
			return ColorMapName.HSV;
		}
	},
	IRON {
		public ColorMapName toColorMapName() {
			return ColorMapName.IRON;
		}
	},
	JET {
		public ColorMapName toColorMapName() {
			return ColorMapName.JET;
		}
	},
	LIGHTJET {
		public ColorMapName toColorMapName() {
			return ColorMapName.LIGHTJET;
		}
	},
	MEDICAL {
		public ColorMapName toColorMapName() {
			return ColorMapName.MEDICAL;
		}
	},
	OCEAN {
		public ColorMapName toColorMapName() {
			return ColorMapName.OCEAN;
		}
	},
	PARULA {
		public ColorMapName toColorMapName() {
			return ColorMapName.PARULA;
		}
	},
	PINK {
		public ColorMapName toColorMapName() {
			return ColorMapName.PINK;
		}
	},
	PRISM {
		public ColorMapName toColorMapName() {
			return ColorMapName.PRISM;
		}
	},
	PURPLE {
		public ColorMapName toColorMapName() {
			return ColorMapName.PURPLE;
		}
	},
	RED {
		public ColorMapName toColorMapName() {
			return ColorMapName.RED;
		}
	},
	REPTILOID {
		public ColorMapName toColorMapName() {
			return ColorMapName.REPTILOID;
		}
	},
	SOFT_RAINBOW {
		public ColorMapName toColorMapName() {
			return ColorMapName.SOFT_RAINBOW;
		}
	},
	SPRING {
		public ColorMapName toColorMapName() {
			return ColorMapName.SPRING;
		}
	},
	SUMMER {
		public ColorMapName toColorMapName() {
			return ColorMapName.SUMMER;
		}
	},
	WINTER {
		public ColorMapName toColorMapName() {
			return ColorMapName.WINTER;
		}
	},
	OPTICAL {
		public ColorMapName toColorMapName() {
			return ColorMapName.OPTICAL;
		}
	},
	NONE {
		public ColorMapName toColorMapName() {
			return ColorMapName.NONE;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public ColorMapName toColorMapName();
}
