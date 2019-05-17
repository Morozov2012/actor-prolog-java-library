// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters;

import morozov.terms.*;

import java.io.Serializable;

public class TrackTerm implements Serializable {
	//
	protected int identifier;
	protected long firstFrameNumber= -1;
	protected long lastFrameNumber= -1;
	protected long beginningTime= -1;
	protected long endTime= -1;
	protected Term term;
	//
	private static final long serialVersionUID= 0xE239CBB38A01DBCL; // 1018830268638698940L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data.converters","TrackTerm");
	// }
	//
	public TrackTerm(int i, long fFN, long lFN, long bT, long eT, Term t) {
		identifier= i;
		firstFrameNumber= fFN;
		lastFrameNumber= lFN;
		beginningTime= bT;
		endTime= eT;
		term= t;
	}
	//
	public int getIdentifier() {
		return identifier;
	}
	public long getFirstFrameNumber() {
		return firstFrameNumber;
	}
	public long getLastFrameNumber() {
		return lastFrameNumber;
	}
	public long getBeginningTime() {
		return beginningTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public Term getTerm() {
		return term;
	}
}
