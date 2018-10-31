// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system;

public class DeviceIdentifier {
	//
	public static DeviceIdentifier DEFAULT= new DeviceIdentifier();
	//
	protected boolean isDefault;
	protected String name;
	//
	public DeviceIdentifier(String s) {
		isDefault= false;
		name= s;
	}
	private DeviceIdentifier() {
		isDefault= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isDefault() {
		return isDefault;
	}
	public String getName() {
		return name;
	}
	public String getNameOrDefault(String defaultName) {
		if (!isDefault) {
			return name;
		} else {
			return defaultName;
		}
	}
	//
	public boolean hasName(String text) {
		if (!isDefault) {
			return name.equals(text);
		} else {
			return false;
		}
	}
}
