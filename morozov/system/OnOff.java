// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

public enum OnOff {
	//
	ON {
		public boolean toBoolean() {
			return true;
		}
	},
	OFF {
		public boolean toBoolean() {
			return false;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean toBoolean();
}
