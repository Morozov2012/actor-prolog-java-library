// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.trk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.math.BigInteger;

public class VPMtrkSelectSuperiorTrack extends VPM_SnapshotCommand {
	//
	protected TrackSortingCriterion sortingCriterion;
	protected SortingMode sortingMode;
	//
	public VPMtrkSelectSuperiorTrack(TrackSortingCriterion givenSortingCriterion, SortingMode givenSortingMode) {
		sortingCriterion= givenSortingCriterion;
		sortingMode= givenSortingMode;
	}
	//
	public void execute(VPM_Snapshot snapshot) {
		HashMap<BigInteger,StableTrack> tracks= snapshot.getTracks();
		if (tracks==null) {
			return;
		};
		if (tracks.size() <= 1) {
			return;
		};
		TrackComparator trackComparator= new TrackComparator(sortingCriterion,sortingMode);
		StableTrack superiorElement= null;
		Set<BigInteger> trackIdentifiers= tracks.keySet();
		Iterator<BigInteger> trackIdentifiersIterator= trackIdentifiers.iterator();
		while (trackIdentifiersIterator.hasNext()) {
			BigInteger trackIdentifier= trackIdentifiersIterator.next();
			StableTrack element= tracks.get(trackIdentifier);
			if (superiorElement != null) {
				if (trackComparator.compare(superiorElement,element) > 0) {
					superiorElement= element;
				}
			} else {
				superiorElement= element;
			}
		};
		tracks.clear();
		tracks.put(superiorElement.getIdentifier(),superiorElement);
		snapshot.setTracks(tracks);
	}
}
