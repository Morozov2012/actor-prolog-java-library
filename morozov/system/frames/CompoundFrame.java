// (c) 2018 Alexei A. Morozov

package morozov.system.frames;

import morozov.system.frames.interfaces.*;
import morozov.system.modes.*;

import java.util.HashMap;
import java.util.Set;
import java.io.Serializable;

public class CompoundFrame implements CompoundFrameInterface, Serializable {
	//
	protected CompoundArrayType arrayType= CompoundArrayType.DATA_FRAME;
	protected long serialNumber;
	protected long time;
	//
	protected HashMap<String,Object> localMemory= new HashMap<>();
	//
	private static final long serialVersionUID= 0x64ED1F009FD0EF8EL; // 7272503060814950286L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames","CompoundFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public CompoundFrame(long n, long t) {
		serialNumber= n;
		time= t;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setCompoundArrayType(CompoundArrayType type) {
		arrayType= type;
	}
	public CompoundArrayType getCompoundArrayType() {
		return arrayType;
	}
	//
	public void setSerialNumber(long number) {
		serialNumber= number;
	}
	public long getSerialNumber() {
		return serialNumber;
	}
	//
	public void setTime(long t) {
		time= t;
	}
	public long getTime() {
		return time;
	}
	//
	public boolean isLightweightFrame() {
		return false;
	}
	//
	public void insertComponent(String key, Object frame) {
		localMemory.put(key,frame);
	}
	//
	public Object getComponent(String key) {
		return localMemory.get(key);
	}
	//
	public void deleteComponent(String key) {
		localMemory.remove(key);
	}
	//
	public boolean hasComponent(String key) {
		return localMemory.containsKey(key);
	}
	//
	public boolean isEmpty() {
		return localMemory.isEmpty();
	}
	//
	public int size() {
		return localMemory.size();
	}
	//
	public Set<String> keySet() {
		return localMemory.keySet();
	}
}
