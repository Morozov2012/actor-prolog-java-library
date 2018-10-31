// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.trk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMtrkRefuseSlowTracks extends VPM_SnapshotCommand {
	//
	protected double velocityThreshold;
	protected double distanceThreshold;
	protected double fuzzyThresholdBorder;
	//
	public VPMtrkRefuseSlowTracks(double givenVelocityThreshold, double givenDistanceThreshold, double givenFuzzyThresholdBorder) {
		velocityThreshold= givenVelocityThreshold;
		distanceThreshold= givenDistanceThreshold;
		fuzzyThresholdBorder= givenFuzzyThresholdBorder;
	}
	//
	public void execute(VPM_Snapshot snapshot) {
		snapshot.setSlowTrackDeletionAttributes(velocityThreshold,distanceThreshold,fuzzyThresholdBorder);
	}
}
