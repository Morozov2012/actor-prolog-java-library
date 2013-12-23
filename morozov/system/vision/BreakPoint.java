// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.math.BigInteger;

class BreakPoint {
	public long time;
	public HashSet<BigInteger> entries= null;
	public long numberOfOrigins= 0;
	public long numberOfInputs= 0;
	public long numberOfOutputs= 0;
	public BreakPoint(long t) {
		time= t;
	}
	public BreakPoint(long t, BigInteger neighbour, TrackSegmentEntryType entryType) {
		time= t;
		addBranch(neighbour,entryType);
	}
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
		if (numberOfOrigins>0 && numberOfInputs<=0 && numberOfOutputs<=0) {
			return true;
		} else {
			return false;
		}
	}
}
