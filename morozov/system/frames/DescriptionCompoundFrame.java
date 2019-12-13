// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.frames;

import morozov.system.frames.interfaces.*;
import morozov.system.frames.text.*;
import morozov.system.modes.*;

public class DescriptionCompoundFrame extends CompoundFrame implements DescriptionCompoundFrameInterface {
	//
	protected String description;
	protected String copyright;
	protected String registrationDate;
	protected String registrationTime;
	//
	private static final long serialVersionUID= 0x1478BBDC995DE6BCL; // 1475135434104104636L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames","DescriptionCompoundFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public DescriptionCompoundFrame(
			long givenFrameNumber,
			long givenFrameTime,
			String givenDescription,
			String givenCopyright,
			String givenRegistrationDate,
			String givenRegistrationTime) {
		super(	givenFrameNumber,
			givenFrameTime);
		arrayType= CompoundArrayType.DESCRIPTION_FRAME;
		description= givenDescription;
		copyright= givenCopyright;
		registrationDate= givenRegistrationDate;
		registrationTime= givenRegistrationTime;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isLightweightFrame() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String value) {
		description= value;
	}
	@Override
	public String getCopyright() {
		return copyright;
	}
	@Override
	public void setCopyright(String value) {
		copyright= value;
	}
	@Override
	public String getRegistrationDate() {
		return registrationDate;
	}
	@Override
	public void setRegistrationDate(String value) {
		registrationDate= value;
	}
	@Override
	public String getRegistrationTime() {
		return registrationTime;
	}
	@Override
	public void setRegistrationTime(String value) {
		registrationTime= value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString() {
		return DescriptionCompoundFrameText.toString(this);
	}
}
