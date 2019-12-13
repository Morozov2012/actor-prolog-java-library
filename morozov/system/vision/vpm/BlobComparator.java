// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.vision.vpm.converters.*;

import java.util.Comparator;

public class BlobComparator implements Comparator<BlobAttributes> {
	//
	protected BlobSortingCriterion sortingCriterion;
	protected SortingMode sortingMode;
	//
	///////////////////////////////////////////////////////////////
	//
	public BlobComparator(BlobSortingCriterion criterion, SortingMode mode) {
		sortingCriterion= criterion;
		sortingMode= mode;
		if (sortingMode==SortingMode.DEFAULT) {
			switch (sortingCriterion) {
			case SIZE:
			case FOREGROUND_AREA:
			case CONTOUR_LENGTH:
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
	public int compare(BlobAttributes o1, BlobAttributes o2) {
		if (sortingMode==SortingMode.DESCENDING_ORDER) {
			BlobAttributes o3= o1;
			o1= o2;
			o2= o3;
		};
		switch (sortingCriterion) {
		case SIZE:
			int size1= (o1.getX2()-o1.getX1()+1)*(o1.getY2()-o1.getY1()+1);
			int size2= (o2.getX2()-o2.getX1()+1)*(o2.getY2()-o2.getY1()+1);
			return Integer.compare(size1,size2);
		case FOREGROUND_AREA:
			return Long.compare(o1.getForegroundArea(),o2.getForegroundArea());
		case CONTOUR_LENGTH:
			return Long.compare(o1.getContourLength(),o2.getContourLength());
		case CENTROID_X:
			return Integer.compare(o1.getCentroidX(),o2.getCentroidX());
		case CENTROID_Y:
			return Integer.compare(o1.getCentroidY(),o2.getCentroidY());
		case LEFT_X:
			return Integer.compare(o1.getX1(),o2.getX1());
		case RIGHT_X:
			return Integer.compare(o1.getX2(),o2.getX2());
		case TOP_Y:
			return Integer.compare(o1.getY2(),o2.getY2());
		case BOTTOM_Y:
			return Integer.compare(o1.getY1(),o2.getY1());
		default:
			System.err.printf("Unknown sorting criterion: %s\n",sortingCriterion);
		};
		return 0;
	}
}
