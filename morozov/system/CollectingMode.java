// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

public enum CollectingMode {
	//
	SET {
		@Override
		public boolean toBoolean() {
			return true;
		}
	},
	BAG {
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
