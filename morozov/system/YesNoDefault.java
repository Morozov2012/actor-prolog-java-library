// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system;

public enum YesNoDefault {
	//
	YES {
		@Override
		public boolean toBoolean(boolean value) {
			return true;
		}
	},
	NO {
		@Override
		public boolean toBoolean(boolean value) {
			return false;
		}
	},
	DEFAULT {
		@Override
		public boolean toBoolean(boolean value) {
			return value;
		}
		@Override
		public YesNoDefault getValue(YesNoDefault value) {
			return value;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean toBoolean(boolean value);
	//
	public YesNoDefault getValue(YesNoDefault value) {
		return this;
	}
}
