// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.util.Comparator;

class BlobIntersectionsComparator implements Comparator<BlobIntersection> {
	public int compare(BlobIntersection o1, BlobIntersection o2) {
		if (o1.isStrong) {
			if (o2.isStrong) {
				return Integer.compare(o2.commonAreaSize,o1.commonAreaSize);
			} else {
				return -1;
			}
		} else {
			if (o2.isStrong) {
				return 1;
			} else {
				return Integer.compare(o2.commonAreaSize,o1.commonAreaSize);
			}
		}
	}
}
