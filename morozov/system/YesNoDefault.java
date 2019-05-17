// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system;

public enum YesNoDefault {
	//
	YES {
		public boolean toBoolean(boolean value) {
			return true;
		}
	},
	NO {
		public boolean toBoolean(boolean value) {
			return false;
		}
	},
	DEFAULT {
		public boolean toBoolean(boolean value) {
			return value;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean toBoolean(boolean value);
}
