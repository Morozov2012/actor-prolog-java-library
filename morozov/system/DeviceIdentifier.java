// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system;

import java.io.Serializable;

public class DeviceIdentifier implements Serializable {
	//
	public static DeviceIdentifier DEFAULT= new DeviceIdentifier();
	//
	protected boolean isDefault;
	protected String name;
	//
	private static final long serialVersionUID= 0xA8F417CB8B7B22CCL; // -6272362218034355508
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","DeviceIdentifier");
	//	SerialVersionChecker.report("morozov.system","DeviceIdentifier");
	// }
	//
	///////////////////////////////////////////////////////////////
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
