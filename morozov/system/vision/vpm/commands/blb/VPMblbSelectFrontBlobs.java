// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

import java.util.Arrays;

public class VPMblbSelectFrontBlobs extends VPM_FrameCommand {
	//
	protected int numberOfBlobs;
	protected BlobSortingCriterion sortingCriterion;
	protected SortingMode sortingMode;
	//
	protected BlobComparator blobComparator;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPMblbSelectFrontBlobs(int n) {
		numberOfBlobs= n;
	}
	public VPMblbSelectFrontBlobs(int n, BlobSortingCriterion criterion, SortingMode mode) {
		numberOfBlobs= n;
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
		BlobAttributes[] attributeArray= group.getAttributeArray();
		if (attributeArray.length <= numberOfBlobs) {
			return;
		};
		if (sortingCriterion==null) {
			attributeArray= Arrays.copyOfRange(attributeArray,0,numberOfBlobs);
			group.setAttributeArray(attributeArray);
		} else {
			if (blobComparator==null) {
				blobComparator= new BlobComparator(sortingCriterion,sortingMode);
			};
			Arrays.sort(attributeArray,blobComparator);
			attributeArray= Arrays.copyOfRange(attributeArray,0,numberOfBlobs);
			group.setAttributeArray(attributeArray);
		}
	}
}
