// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

public enum OnOff {
	//
	ON {
		@Override
		public boolean toBoolean() {
			return true;
		}
	},
	OFF {
		@Override
		public boolean toBoolean() {
			return false;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean toBoolean();
}
