// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public enum TrackSegmentEntryType {
	ORIGIN {
		@Override
		public TrackSegmentEntryType computeComplementaryType() {
			return TrackSegmentEntryType.OUTPUT;
		}
	},
	INPUT {
		@Override
		public TrackSegmentEntryType computeComplementaryType() {
			return TrackSegmentEntryType.INPUT;
		}
	},
	OUTPUT {
		@Override
		public TrackSegmentEntryType computeComplementaryType() {
			return TrackSegmentEntryType.ORIGIN;
		}
	};
	abstract public TrackSegmentEntryType computeComplementaryType();
}
