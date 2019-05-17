// (c) 2018 Alexei A. Morozov

package morozov.system.frames.tools;

public class MetadataDescription {
	//
	protected String description;
	protected String copyright;
	protected String registrationDate;
	protected String registrationTime;
	//
	public MetadataDescription(String givenDescription, String givenCopyright, String givenDate, String givenTime) {
		description= givenDescription;
		copyright= givenCopyright;
		registrationDate= givenDate;
		registrationTime= givenTime;
	}
	//
	public String getDescription() {
		return description;
	}
	public String getCopyright() {
		return copyright;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public String getRegistrationTime() {
		return registrationTime;
	}
}
