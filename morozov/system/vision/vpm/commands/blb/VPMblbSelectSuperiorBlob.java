// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

public class VPMblbSelectSuperiorBlob extends VPM_FrameCommand {
	//
	protected BlobSortingCriterion sortingCriterion;
	protected SortingMode sortingMode;
	//
	protected BlobComparator blobComparator;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPMblbSelectSuperiorBlob(BlobSortingCriterion criterion, SortingMode mode) {
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
		if (attributeArray.length < 2) {
			return;
		};
		BlobAttributes superiorElement= attributeArray[0];
		for (int k=1; k < attributeArray.length; k++) {
			BlobAttributes element= attributeArray[k];
			if (blobComparator.compare(superiorElement,element) > 0) {
				superiorElement= element;
			}
		};
		attributeArray= new BlobAttributes[]{superiorElement};
		group.setAttributeArray(attributeArray);
	}
}
