// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

enum TrackSegmentEntryType {
	ORIGIN {
		public TrackSegmentEntryType computeComplementaryType() {
			return TrackSegmentEntryType.OUTPUT;
		}
	},
	INPUT {
		public TrackSegmentEntryType computeComplementaryType() {
			return TrackSegmentEntryType.INPUT;
		}
	},
	OUTPUT {
		public TrackSegmentEntryType computeComplementaryType() {
			return TrackSegmentEntryType.ORIGIN;
		}
	};
	public abstract TrackSegmentEntryType computeComplementaryType();
}
