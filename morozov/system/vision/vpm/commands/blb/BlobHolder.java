// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.converters.*;

import java.util.HashMap;
import java.math.BigInteger;

public interface BlobHolder {
	public int getOperationalImageWidth();
	public int getOperationalImageHeight();
	public long getRecentFrameNumber();
	public long getRecentTimeInMilliseconds();
	public TransformationMatrices getTransformationMatrices();
	public BlobType[] getBlobTypes();
	public BlobAttributes[] getBlobAttributes();
	public int getMinimalTrackDuration();
	public int getMaximalTrackDuration();
	public int getMaximalBlobInvisibilityInterval();
	public int getMaximalTrackRetentionInterval();
	public double getSamplingRate();
	public int getR2WindowHalfwidth();
	public boolean getCharacteristicLengthMedianFilteringMode();
	public int getCharacteristicLengthMedianFilterHalfwidth();
	public boolean getVelocityMedianFilteringMode();
	public int getVelocityMedianFilterHalfwidth();
	public void setTracks(HashMap<BigInteger,GrowingTrack> t);
}
