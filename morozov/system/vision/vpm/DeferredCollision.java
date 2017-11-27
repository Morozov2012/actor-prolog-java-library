// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class DeferredCollision {
	//
	protected long implementationPoint;
	protected GrowingTrack receiver;
	protected long requestedPoint;
	protected long requestedTimeInMilliseconds;
	protected GrowingTrack sender;
	protected TrackSegmentEntryType entryType;
	//
	///////////////////////////////////////////////////////////////
	//
	public DeferredCollision(long f1, GrowingTrack track1, long f2, long t2, GrowingTrack track2, TrackSegmentEntryType mode) {
		implementationPoint= f1;
		receiver= track1;
		requestedPoint= f2;
		requestedTimeInMilliseconds= t2;
		sender= track2;
		entryType= mode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getImplementationPoint() {
		return implementationPoint;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean implement() {
		if (sender.isStrong() && receiver.isStrong()) {
			sender.requestBreakPoint(requestedPoint,requestedTimeInMilliseconds,receiver.getIdentifier(),entryType);
			receiver.requestBreakPoint(requestedPoint,requestedTimeInMilliseconds,sender.getIdentifier(),entryType.computeComplementaryType());
			return true;
		} else if (sender.isDeposed() || receiver.isDeposed()) {
			return true;
		} else {
			return false;
		}
	}
}
