// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

// import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
// import morozov.system.errors.*;
// import morozov.system.signals.*;
import morozov.system.vision.*;
import morozov.system.vision.errors.*;
import morozov.terms.*;

public abstract class SynchronizedImageSubtractor extends ImageSubtractor {
	//
	abstract public Term getBuiltInSlot_E_image_subtractor();
	//
	public void subtract2s(ChoisePoint iX, Term a1, Term a2) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.subtract2s(iX,a1,a2);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void subtract3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.subtract3s(iX,a1,a2,a3);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void commit0s(ChoisePoint iX) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.commit0s(iX);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	// public void reset1s(ChoisePoint iX, Term a1) {
	//	Term subtractor= getBuiltInSlot_E_image_subtractor();
	//	if (subtractor instanceof ImageSubtractor) {
	//		ImageSubtractor server= (ImageSubtractor)subtractor;
	//		server.reset1s(iX,a1);
	//	} else {
	//		throw new WrongArgumentIsNotImageSubtractor(subtractor);
	//	}
	// }
	public void resetSettings0s(ChoisePoint iX) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.resetSettings0s(iX);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void resetStatistics0s(ChoisePoint iX) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.resetStatistics0s(iX);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void resetResults0s(ChoisePoint iX) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.resetResults0s(iX);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void resetAll0s(ChoisePoint iX) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.resetAll0s(iX);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getFrameNumber1s(ChoisePoint iX, PrologVariable frameNumber) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getFrameNumber1s(iX,frameNumber);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getRecentImage1s(ChoisePoint iX, Term image) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getRecentImage1s(iX,image);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getBackgroundImage1s(ChoisePoint iX, Term image) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBackgroundImage1s(iX,image);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getSigmaImage1s(ChoisePoint iX, Term image) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getSigmaImage1s(iX,image);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getForegroundImage1s(ChoisePoint iX, Term image) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getForegroundImage1s(iX,image);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getSynthesizedImage1s(ChoisePoint iX, Term image) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getSynthesizedImage1s(iX,image);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getBlobs1s(ChoisePoint iX, PrologVariable blobs) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBlobs1s(iX,blobs);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getTracks1s(ChoisePoint iX, PrologVariable tracks) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getTracks1s(iX,tracks);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getConnectedGraphs1s(ChoisePoint iX, PrologVariable graphs) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getConnectedGraphs1s(iX,graphs);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	// BlobExtractionMode
	public void setBlobExtractionMode1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setBlobExtractionMode1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getBlobExtractionMode0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBlobExtractionMode0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getBlobExtractionMode0fs(ChoisePoint iX) {
	}
	// BlobTracingMode
	public void setBlobTracingMode1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setBlobTracingMode1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getBlobTracingMode0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBlobTracingMode0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getBlobTracingMode0fs(ChoisePoint iX) {
	}
	// MinimalTrainingInterval
	public void setMinimalTrainingInterval1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setMinimalTrainingInterval1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getMinimalTrainingInterval0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getMinimalTrainingInterval0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getMinimalTrainingInterval0fs(ChoisePoint iX) {
	}
	// MaximalTrainingInterval
	public void setMaximalTrainingInterval1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setMaximalTrainingInterval1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getMaximalTrainingInterval0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getMaximalTrainingInterval0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getMaximalTrainingInterval0fs(ChoisePoint iX) {
	}
	// GrayscaleMode
	public void setGrayscaleMode1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setGrayscaleMode1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getGrayscaleMode0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getGrayscaleMode0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getGrayscaleMode0fs(ChoisePoint iX) {
	}
	// BackgroundGaussianFilteringMode
	public void setBackgroundGaussianFilteringMode1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setBackgroundGaussianFilteringMode1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getBackgroundGaussianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBackgroundGaussianFilteringMode0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getBackgroundGaussianFilteringMode0fs(ChoisePoint iX) {
	}
	// BackgroundGaussianFilterRadius
	public void setBackgroundGaussianFilterRadius1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setBackgroundGaussianFilterRadius1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getBackgroundGaussianFilterRadius0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBackgroundGaussianFilterRadius0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getBackgroundGaussianFilterRadius0fs(ChoisePoint iX) {
	}
	// BackgroundMedianFilteringMode
	public void setBackgroundMedianFilteringMode1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setBackgroundMedianFilteringMode1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getBackgroundMedianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBackgroundMedianFilteringMode0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getBackgroundMedianFilteringMode0fs(ChoisePoint iX) {
	}
	// BackgroundMedianFilterThreshold
	public void setBackgroundMedianFilterThreshold1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setBackgroundMedianFilterThreshold1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getBackgroundMedianFilterThreshold0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBackgroundMedianFilterThreshold0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getBackgroundMedianFilterThreshold0fs(ChoisePoint iX) {
	}
	// BackgroundStandardDeviationFactor
	public void setBackgroundStandardDeviationFactor1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setBackgroundStandardDeviationFactor1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getBackgroundStandardDeviationFactor0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getBackgroundStandardDeviationFactor0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getBackgroundStandardDeviationFactor0fs(ChoisePoint iX) {
	}
	// HorizontalBlobBorder
	public void setHorizontalBlobBorder1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setHorizontalBlobBorder1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getHorizontalBlobBorder0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getHorizontalBlobBorder0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getHorizontalBlobBorder0fs(ChoisePoint iX) {
	}
	// VerticalBlobBorder
	public void setVerticalBlobBorder1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setVerticalBlobBorder1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getVerticalBlobBorder0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getVerticalBlobBorder0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getVerticalBlobBorder0fs(ChoisePoint iX) {
	}
	// MinimalBlobIntersectionArea
	public void setMinimalBlobIntersectionArea1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setMinimalBlobIntersectionArea1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getMinimalBlobIntersectionArea0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getMinimalBlobIntersectionArea0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getMinimalBlobIntersectionArea0fs(ChoisePoint iX) {
	}
	// MinimalBlobSize
	public void setMinimalBlobSize1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setMinimalBlobSize1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getMinimalBlobSize0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getMinimalBlobSize0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getMinimalBlobSize0fs(ChoisePoint iX) {
	}
	// MinimalTrackDuration
	public void setMinimalTrackDuration1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setMinimalTrackDuration1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getMinimalTrackDuration0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getMinimalTrackDuration0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getMinimalTrackDuration0fs(ChoisePoint iX) {
	}
	// MaximalBlobInvisibilityInterval
	public void setMaximalBlobInvisibilityInterval1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setMaximalBlobInvisibilityInterval1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getMaximalBlobInvisibilityInterval0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getMaximalBlobInvisibilityInterval0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getMaximalBlobInvisibilityInterval0fs(ChoisePoint iX) {
	}
	// MaximalTrackRetentionInterval
	public void setMaximalTrackRetentionInterval1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setMaximalTrackRetentionInterval1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getMaximalTrackRetentionInterval0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getMaximalTrackRetentionInterval0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getMaximalTrackRetentionInterval0fs(ChoisePoint iX) {
	}
	// InverseTransformationMatrix
	public void setInverseTransformationMatrix1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setInverseTransformationMatrix1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getInverseTransformationMatrix0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getInverseTransformationMatrix0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getInverseTransformationMatrix0fs(ChoisePoint iX) {
	}
	// SamplingRate
	public void setSamplingRate1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setSamplingRate1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getSamplingRate0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getSamplingRate0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getSamplingRate0fs(ChoisePoint iX) {
	}
	// VelocityMedianFilteringMode
	public void setVelocityMedianFilteringMode1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setVelocityMedianFilteringMode1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getVelocityMedianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getVelocityMedianFilteringMode0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getVelocityMedianFilteringMode0fs(ChoisePoint iX) {
	}
	// VelocityMedianFilterHalfwidth
	public void setVelocityMedianFilterHalfwidth1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setVelocityMedianFilterHalfwidth1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getVelocityMedianFilterHalfwidth0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getVelocityMedianFilterHalfwidth0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getVelocityMedianFilterHalfwidth0fs(ChoisePoint iX) {
	}
	// SlowTracksDeletionMode
	public void setSlowTracksDeletionMode1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setSlowTracksDeletionMode1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getSlowTracksDeletionMode0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getSlowTracksDeletionMode0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getSlowTracksDeletionMode0fs(ChoisePoint iX) {
	}
	// FuzzyVelocityThreshold
	public void setFuzzyVelocityThreshold1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setFuzzyVelocityThreshold1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getFuzzyVelocityThreshold0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getFuzzyVelocityThreshold0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getFuzzyVelocityThreshold0fs(ChoisePoint iX) {
	}
	// FuzzyDistanceThreshold
	public void setFuzzyDistanceThreshold1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setFuzzyDistanceThreshold1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getFuzzyDistanceThreshold0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getFuzzyDistanceThreshold0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getFuzzyDistanceThreshold0fs(ChoisePoint iX) {
	}
	// FuzzyThresholdBorder
	public void setFuzzyThresholdBorder1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setFuzzyThresholdBorder1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getFuzzyThresholdBorder0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getFuzzyThresholdBorder0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getFuzzyThresholdBorder0fs(ChoisePoint iX) {
	}
	// SynthesizedImageTransparency
	public void setSynthesizedImageTransparency1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setSynthesizedImageTransparency1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getSynthesizedImageTransparency0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getSynthesizedImageTransparency0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getSynthesizedImageTransparency0fs(ChoisePoint iX) {
	}
	// SynthesizedImageRectangularBlobsMode
	public void setSynthesizedImageRectangularBlobsMode1s(ChoisePoint iX, Term value) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.setSynthesizedImageRectangularBlobsMode1s(iX,value);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	//
	public void getSynthesizedImageRectangularBlobsMode0ff(ChoisePoint iX, PrologVariable result) {
		Term subtractor= getBuiltInSlot_E_image_subtractor();
		if (subtractor instanceof ImageSubtractor) {
			ImageSubtractor server= (ImageSubtractor)subtractor;
			server.getSynthesizedImageRectangularBlobsMode0ff(iX,result);
		} else {
			throw new WrongArgumentIsNotImageSubtractor(subtractor);
		}
	}
	public void getSynthesizedImageRectangularBlobsMode0fs(ChoisePoint iX) {
	}
}
