// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class SegmentChain {
	//
	protected ConnectedSegment unit;
	protected SegmentChain rest;
	protected ConnectedSegment origin;
	//
	///////////////////////////////////////////////////////////////
	//
	public SegmentChain(ConnectedSegment u, SegmentChain tail, ConnectedSegment firstSegment) {
		unit= u;
		rest= tail;
		origin= firstSegment;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean contains(ConnectedSegment segment) {
		if (unit==segment) {
			return true;
		} else {
			if (rest != null) {
				return rest.contains(segment);
			} else {
				return false;
			}
		}
	}
}
