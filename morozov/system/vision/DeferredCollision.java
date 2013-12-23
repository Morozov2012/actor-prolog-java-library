// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.io.PrintStream;

class DeferredCollision {
	public long implementationTime;
	public GrowingTrack receiver;
	public long requestedTime;
	public GrowingTrack sender;
	public TrackSegmentEntryType entryType;
	public DeferredCollision(long t1, GrowingTrack track1, long t2, GrowingTrack track2, TrackSegmentEntryType mode) {
		implementationTime= t1;
		receiver= track1;
		requestedTime= t2;
		sender= track2;
		entryType= mode;
	}
	public void implement() {
		if (sender.isStrong && receiver.isStrong) {
			if (!sender.isRarefied && !receiver.isRarefied) {
				sender.requestBreakPoint(requestedTime,receiver.identifier,entryType);
				receiver.requestBreakPoint(requestedTime,sender.identifier,entryType.computeComplementaryType());
			}
		}
	}
}
