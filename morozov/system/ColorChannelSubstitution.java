// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system;

import java.io.Serializable;

public class ColorChannelSubstitution implements Serializable {
	//
	protected ColorChannelSubstitutionName name= ColorChannelSubstitutionName.DEFAULT;
	protected int value= 0;
	protected boolean isNamed= false;
	//
	public static ColorChannelSubstitution instanceDefault= new ColorChannelSubstitution();
	//
	private static final long serialVersionUID= 0x25BFCD329B7B129EL; // 2720118317195661982
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","ColorChannelSubstitution");
	//	SerialVersionChecker.report("morozov.system","ColorChannelSubstitution");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ColorChannelSubstitution() {
		name= ColorChannelSubstitutionName.DEFAULT;
		isNamed= true;
	}
	public ColorChannelSubstitution(ColorChannelSubstitutionName n) {
		name= n;
		isNamed= true;
	}
	public ColorChannelSubstitution(int v) {
		value= v;
		isNamed= false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isNamed() {
		return isNamed;
	}
	//
	public ColorChannelSubstitutionName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	public boolean isDefault() {
		return (isNamed && name==ColorChannelSubstitutionName.DEFAULT);
	}
	//
	public boolean requires_HSB_Channels(int group) {
		if (isNamed) {
			return name.requires_HSB_Channels(group);
		} else {
			return false;
		}
	}
	//
	public boolean requires_HS_Channels(int group) {
		if (isNamed) {
			return name.requires_HS_Channels(group);
		} else {
			return false;
		}
	}
}
