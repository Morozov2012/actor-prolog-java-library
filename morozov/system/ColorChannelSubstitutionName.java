// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system;

public enum ColorChannelSubstitutionName {
	//
	RED1 {
	},
	GREEN1 {
	},
	BLUE1 {
	},
	RED2 {
	},
	GREEN2 {
	},
	BLUE2 {
	},
	HUE1 {
		@Override
		public boolean requires_HSB_Channels(int group) {
			return (group==1);
		}
		@Override
		public boolean requires_HS_Channels(int group) {
			return (group==1);
		}
	},
	SATURATION1 {
		@Override
		public boolean requires_HSB_Channels(int group) {
			return (group==1);
		}
		@Override
		public boolean requires_HS_Channels(int group) {
			return (group==1);
		}
	},
	BRIGHTNESS1 {
		@Override
		public boolean requires_HSB_Channels(int group) {
			return (group==1);
		}
	},
	HUE2 {
		@Override
		public boolean requires_HSB_Channels(int group) {
			return (group==2);
		}
		@Override
		public boolean requires_HS_Channels(int group) {
			return (group==2);
		}
	},
	SATURATION2 {
		@Override
		public boolean requires_HSB_Channels(int group) {
			return (group==2);
		}
		@Override
		public boolean requires_HS_Channels(int group) {
			return (group==2);
		}
	},
	BRIGHTNESS2 {
		@Override
		public boolean requires_HSB_Channels(int group) {
			return (group==2);
		}
	},
	ZERO {
	},
	FULL {
	},
	HALF {
	},
	DEFAULT {
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean requires_HSB_Channels(int group) {
		return false;
	}
	public boolean requires_HS_Channels(int group) {
		return false;
	}
}
