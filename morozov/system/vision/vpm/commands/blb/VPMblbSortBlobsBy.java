// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

import java.util.Arrays;

public class VPMblbSortBlobsBy extends VPM_FrameCommand {
	//
	protected BlobSortingCriterion sortingCriterion;
	protected SortingMode sortingMode;
	//
	protected BlobComparator blobComparator;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPMblbSortBlobsBy(BlobSortingCriterion criterion, SortingMode mode) {
		sortingCriterion= criterion;
		sortingMode= mode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void execute(VPM vpm) {
		BlobGroup group= vpm.getRecentBlobGroup();
		if (group==null) {
			return;
		};
		if (blobComparator==null) {
			blobComparator= new BlobComparator(sortingCriterion,sortingMode);
		};
		BlobAttributes[] attributeArray= group.getAttributeArray();
		Arrays.sort(attributeArray,blobComparator);
		// group.setAttributeArray(attributeArray);
	}
}
