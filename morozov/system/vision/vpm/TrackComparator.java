// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.vision.vpm.converters.*;

import java.util.Comparator;

public class TrackComparator implements Comparator<StableTrack> {
	//
	protected TrackSortingCriterion sortingCriterion;
	protected SortingMode sortingMode;
	//
	///////////////////////////////////////////////////////////////
	//
	public TrackComparator(TrackSortingCriterion criterion, SortingMode mode) {
		sortingCriterion= criterion;
		sortingMode= mode;
		if (sortingMode==SortingMode.DEFAULT) {
			switch (sortingCriterion) {
			case NUMBER_OF_FRAMES:
			case MEAN_BLOB_AREA:
			case MEAN_FOREGROUND_AREA:
			case MEAN_CONTOUR_LENGTH:
			case TOTAL_DISTANCE:
			case TOTAL_SHIFT:
			case MEAN_VELOCITY:
				sortingMode= SortingMode.DESCENDING_ORDER;
				break;
			default:
				sortingMode= SortingMode.ASCENDING_ORDER;
				break;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int compare(StableTrack o1, StableTrack o2) {
		if (sortingMode==SortingMode.DESCENDING_ORDER) {
			StableTrack o3= o1;
			o1= o2;
			o2= o3;
		};
		switch (sortingCriterion) {
		case NUMBER_OF_FRAMES:
			return Integer.compare(o1.getNumberOfFrames(),o2.getNumberOfFrames());
		case MEAN_BLOB_AREA:
			return Double.compare(o1.getMeanBlobArea(),o2.getMeanBlobArea());
		case MEAN_FOREGROUND_AREA:
			return Double.compare(o1.getMeanForegroundArea(),o2.getMeanForegroundArea());
		case MEAN_CONTOUR_LENGTH:
			return Double.compare(o1.getMeanContourLength(),o2.getMeanContourLength());
		case TOTAL_DISTANCE:
			return Double.compare(o1.getTotalDistance(),o2.getTotalDistance());
		case TOTAL_SHIFT:
			return Double.compare(o1.getTotalShift(),o2.getTotalShift());
		case MEAN_VELOCITY:
			return Double.compare(o1.getMeanVelocity(),o2.getMeanVelocity());
		default:
			System.err.printf("Unknown sorting criterion: %s\n",sortingCriterion);
		};
		return 0;
	}
}
