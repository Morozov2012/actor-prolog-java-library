// (c) 2018 Alexei A. Morozov

package morozov.system.frames.interfaces;

import morozov.system.modes.*;

public interface CompoundFrameInterface {
	public void setCompoundArrayType(CompoundArrayType type);
	public CompoundArrayType getCompoundArrayType();
	public void setSerialNumber(long number);
	public long getSerialNumber();
	public void setTime(long time);
	public long getTime();
	public boolean isLightweightFrame();
	public void insertComponent(String key, Object frame);
	public Object getComponent(String key);
	public void deleteComponent(String key);
	public boolean hasComponent(String key);
}
