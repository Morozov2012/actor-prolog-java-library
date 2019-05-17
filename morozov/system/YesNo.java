// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system;

public enum YesNo {
	//
	YES {
		public boolean toBoolean() {
			return true;
		}
	},
	NO {
		public boolean toBoolean() {
			return false;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean toBoolean();
}
