// (c) 2018 Alexei A. Morozov

package morozov.system.sound.frames.interfaces;

import morozov.system.frames.interfaces.*;

public interface AudioDataFrameInterface extends DataFrameInterface {
	public byte[] getAudioData();
}
