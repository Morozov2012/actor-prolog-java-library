// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class SegmentChain {
	public ConnectedSegment unit;
	public SegmentChain rest;
	public ConnectedSegment origin;
	public SegmentChain(ConnectedSegment u, SegmentChain tail, ConnectedSegment firstSegment) {
		unit= u;
		rest= tail;
		origin= firstSegment;
	}
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
