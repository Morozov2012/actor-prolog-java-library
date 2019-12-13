// (c) 2018 Alexei A. Morozov

package morozov.system.modes;

import morozov.system.*;

public enum DataColorMap {
	AQUA {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.AQUA;
		}
	},
	AUTUMN {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.AUTUMN;
		}
	},
	BLACKHOT {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.BLACKHOT;
		}
	},
	BLAZE {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.BLAZE;
		}
	},
	BLUERED {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.BLUERED;
		}
	},
	BONE {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.BONE;
		}
	},
	BRIGHT_RAINBOW {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.BRIGHT_RAINBOW;
		}
	},
	COOL {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.COOL;
		}
	},
	COPPER {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.COPPER;
		}
	},
	GRAY {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.GRAY;
		}
	},
	GREEN {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.GREEN;
		}
	},
	HOT {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.HOT;
		}
	},
	HSV {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.HSV;
		}
	},
	IRON {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.IRON;
		}
	},
	JET {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.JET;
		}
	},
	LIGHTJET {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.LIGHTJET;
		}
	},
	MEDICAL {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.MEDICAL;
		}
	},
	OCEAN {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.OCEAN;
		}
	},
	PARULA {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.PARULA;
		}
	},
	PINK {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.PINK;
		}
	},
	PRISM {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.PRISM;
		}
	},
	PURPLE {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.PURPLE;
		}
	},
	RED {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.RED;
		}
	},
	REPTILOID {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.REPTILOID;
		}
	},
	SOFT_RAINBOW {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.SOFT_RAINBOW;
		}
	},
	SPRING {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.SPRING;
		}
	},
	SUMMER {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.SUMMER;
		}
	},
	WINTER {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.WINTER;
		}
	},
	OPTICAL {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.OPTICAL;
		}
	},
	NONE {
		@Override
		public ColorMapName toColorMapName() {
			return ColorMapName.NONE;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public ColorMapName toColorMapName();
}
