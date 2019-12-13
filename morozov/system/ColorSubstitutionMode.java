// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system;

import java.io.Serializable;

public class ColorSubstitutionMode implements Serializable {
	//
	protected boolean use_RGB_mode= true;
	protected boolean isEmpty= false;
	//
	protected ColorChannelSubstitution firstChannelSubstitution= ColorChannelSubstitution.instanceDefault;
	protected ColorChannelSubstitution secondChannelSubstitution= ColorChannelSubstitution.instanceDefault;
	protected ColorChannelSubstitution thirdChannelSubstitution= ColorChannelSubstitution.instanceDefault;
	//
	private static final long serialVersionUID= 0x883DB0587087BB50L; // -8629547417080448176
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","ColorSubstitutionMode");
	//	SerialVersionChecker.report("morozov.system","ColorSubstitutionMode");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ColorSubstitutionMode(boolean useRGB, ColorChannelSubstitution s1, ColorChannelSubstitution s2, ColorChannelSubstitution s3) {
		use_RGB_mode= useRGB;
		firstChannelSubstitution= s1;
		secondChannelSubstitution= s2;
		thirdChannelSubstitution= s3;
		if (firstChannelSubstitution.isDefault() && secondChannelSubstitution.isDefault() && thirdChannelSubstitution.isDefault()) {
			isEmpty= true;
		} else {
			isEmpty= false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean useRGBMode() {
		return use_RGB_mode;
	}
	public boolean isEmpty() {
		return isEmpty;
	}
	public ColorChannelSubstitution getFirstChannelSubstitution() {
		return firstChannelSubstitution;
	}
	public ColorChannelSubstitution getSecondChannelSubstitution() {
		return secondChannelSubstitution;
	}
	public ColorChannelSubstitution getThirdChannelSubstitution() {
		return thirdChannelSubstitution;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean requires_HSB_Channels(int group) {
		return	firstChannelSubstitution.requires_HSB_Channels(group) ||
			secondChannelSubstitution.requires_HSB_Channels(group) ||
			thirdChannelSubstitution.requires_HSB_Channels(group);
	}
	//
	public boolean requires_HS_Channels(int group) {
		return	firstChannelSubstitution.requires_HS_Channels(group) ||
			secondChannelSubstitution.requires_HS_Channels(group) ||
			thirdChannelSubstitution.requires_HS_Channels(group);
	}
}
