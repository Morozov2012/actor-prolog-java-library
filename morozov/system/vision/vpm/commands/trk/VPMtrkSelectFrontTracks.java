// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.trk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.math.BigInteger;

public class VPMtrkSelectFrontTracks extends VPM_SnapshotCommand {
	//
	protected int numberOfTracks;
	protected TrackSortingCriterion sortingCriterion;
	protected SortingMode sortingMode;
	//
	public VPMtrkSelectFrontTracks(int givenNumberOfTracks, TrackSortingCriterion givenSortingCriterion, SortingMode givenSortingMode) {
		numberOfTracks= givenNumberOfTracks;
		sortingCriterion= givenSortingCriterion;
		sortingMode= givenSortingMode;
	}
	//
	@Override
	public void execute(VPM_Snapshot snapshot) {
		HashMap<BigInteger,StableTrack> tracks= snapshot.getTracks();
		if (tracks==null) {
			return;
		};
		if (tracks.size() <= numberOfTracks) {
			return;
		};
		Collection<StableTrack> collectionOfValues= tracks.values();
		StableTrack[] trackArray= collectionOfValues.toArray(new StableTrack[collectionOfValues.size()]);
		TrackComparator trackComparator= new TrackComparator(sortingCriterion,sortingMode);
		Arrays.sort(trackArray,trackComparator);
		trackArray= Arrays.copyOfRange(trackArray,0,numberOfTracks);
		tracks= new HashMap<>();
		for (int k=0; k < trackArray.length; k++) {
			StableTrack track= trackArray[k];
			tracks.put(track.getIdentifier(),track);
		};
		snapshot.setTracks(tracks);
	}
}
