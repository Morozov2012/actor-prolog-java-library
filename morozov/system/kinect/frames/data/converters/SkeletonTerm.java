// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters;

import morozov.terms.*;

import java.io.Serializable;

public class SkeletonTerm implements Serializable {
	//
	protected int identifier;
	protected long frameNumber= -1;
	protected long time= -1;
	protected Term term;
	//
	private static final long serialVersionUID= 0xAC0D5A28E7B63287L; // -6049079592756039033L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data.converters","SkeletonTerm");
	// }
	//
	public SkeletonTerm(int i, long fN, long t, Term data) {
		identifier= i;
		frameNumber= fN;
		time= t;
		term= data;
	}
	//
	public int getIdentifier() {
		return identifier;
	}
	public long getFrameNumber() {
		return frameNumber;
	}
	public long getTime() {
		return time;
	}
	public Term getTerm() {
		return term;
	}
}
