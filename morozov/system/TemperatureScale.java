// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system;

public enum TemperatureScale {
	//
	CELSIUS {
		@Override
		public boolean isCelsius() {
			return true;
		}
	},
	FAHRENHEIT {
		@Override
		public boolean isCelsius() {
			return false;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public boolean isCelsius();
}
