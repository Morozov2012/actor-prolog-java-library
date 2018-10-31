// (c) 2018 Alexei A. Morozov

package morozov.system.astrohn.interfaces;

import morozov.system.astrohn.frames.data.*;

public interface AstrohnDataConsumerInterface {
	public void setAstrohnData(TVFilterImageHeader packetBody, long currentTime);
}
