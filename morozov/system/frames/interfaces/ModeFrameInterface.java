// (c) 2018 Alexei A. Morozov

package morozov.system.frames.interfaces;

public interface ModeFrameInterface extends DataFrameInterface {
	//
	public String getDescription();
	public void setDescription(String value);
	public String getCopyright();
	public void setCopyright(String value);
	public String getRegistrationDate();
	public void setRegistrationDate(String value);
	public String getRegistrationTime();
	public void setRegistrationTime(String value);
}
