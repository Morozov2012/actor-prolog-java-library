// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import java.util.HashSet;
import java.math.BigInteger;

public class BreakPoint {
	//
	protected long frameNumber;
	protected long timeInMilliseconds;
	//
	protected HashSet<BigInteger> entries= null;
	protected long numberOfOrigins= 0;
	protected long numberOfInputs= 0;
	protected long numberOfOutputs= 0;
	//
	///////////////////////////////////////////////////////////////
	//
	public BreakPoint(long f, long t) {
		frameNumber= f;
		timeInMilliseconds= t;
	}
	public BreakPoint(long f, long t, BigInteger neighbour, TrackSegmentEntryType entryType) {
		frameNumber= f;
		timeInMilliseconds= t;
		addBranch(neighbour,entryType);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getFrameNumber() {
		return frameNumber;
	}
	public long getTimeInMilliseconds() {
		return timeInMilliseconds;
	}
	public HashSet<BigInteger> getEntries() {
		return entries;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void addBranch(BigInteger neighbour, TrackSegmentEntryType entryType) {
		if (entries==null) {
			entries= new HashSet<BigInteger>();
		};
		entries.add(neighbour);
		switch (entryType) {
		case ORIGIN:
			numberOfOrigins++;
			break;
		case INPUT:
			numberOfInputs++;
			break;
		case OUTPUT:
			numberOfOutputs++;
			break;
		}
	}
	public boolean isPureOrigin() {
		if (numberOfOrigins > 0 && numberOfInputs <= 0 && numberOfOutputs <= 0) {
			return true;
		} else {
			return false;
		}
	}
}
