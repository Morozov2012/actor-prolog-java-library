// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.frames;

import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.text.*;
import morozov.system.modes.*;

public class ModeFrame extends DataFrame implements ModeFrameInterface {
	//
	protected String description;
	protected String copyright;
	protected String registrationDate;
	protected String registrationTime;
	//
	private static final long serialVersionUID= 0xCC70509604A379D4L; // -3715381087327520300L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames","ModeFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ModeFrame(
			long givenFrameNumber,
			long givenFrameTime,
			String givenDescription,
			String givenCopyright,
			String givenRegistrationDate,
			String givenRegistrationTime,
			DataFrameBaseAttributesInterface givenAttributes) {
		super(	givenFrameNumber,
			givenFrameTime,
			givenAttributes);
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
	public DataArrayType getDataArrayType() {
		return DataArrayType.MODE_FRAME;
	}
	//
	@Override
	public int getDataSize() {
		return 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString() {
		return ModeFrameText.toString(this);
	}
}
