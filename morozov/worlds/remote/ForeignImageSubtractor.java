// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.*;
import morozov.system.vision.errors.*;
import morozov.terms.*;
import morozov.worlds.remote.*;
import morozov.worlds.remote.errors.*;
import morozov.worlds.remote.signals.*;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

public class ForeignImageSubtractor
		// extends GenericImageSubtractor
		implements ImageSubtractorOperations {
	//
	public AtomicReference<ImageSubtractorOperations> imageSubtractor= new AtomicReference<>(null);
	//
	protected ForeignWorldWrapper wrapper;
	protected ExternalWorldInterface stub;
	//
	public ForeignImageSubtractor(ForeignWorldWrapper w) {
		wrapper= w;
		stub= wrapper.stub;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public GenericImageEncodingAttributes getCurrentImageEncodingAttributes() {
		try {
			return stub.getCurrentImageEncodingAttributes();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// BlobExtractionMode
	//
	public void setBlobExtractionMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setBlobExtractionMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getBlobExtractionMode(ChoisePoint iX) {
		try {
			return stub.getBlobExtractionMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// BlobTracingMode
	//
	public void setBlobTracingMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setBlobTracingMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getBlobTracingMode(ChoisePoint iX) {
		try {
			return stub.getBlobTracingMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// MinimalTrainingInterval
	//
	public void setMinimalTrainingInterval(int frames, ChoisePoint iX) {
		try {
			stub.setMinimalTrainingInterval(frames);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getMinimalTrainingInterval(ChoisePoint iX) {
		try {
			return stub.getMinimalTrainingInterval();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// MaximalTrainingInterval
	//
	public void setMaximalTrainingInterval(int frames, ChoisePoint iX) {
		try {
			stub.setMaximalTrainingInterval(frames);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getMaximalTrainingInterval(ChoisePoint iX) {
		try {
			return stub.getMaximalTrainingInterval();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// GrayscaleMode
	//
	public void setGrayscaleMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setGrayscaleMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getGrayscaleMode(ChoisePoint iX) {
		try {
			return stub.getGrayscaleMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// BackgroundGaussianFilteringMode
	//
	public void setBackgroundGaussianFilteringMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setBackgroundGaussianFilteringMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getBackgroundGaussianFilteringMode(ChoisePoint iX) {
		try {
			return stub.getBackgroundGaussianFilteringMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// BackgroundGaussianFilterRadius
	//
	public void setBackgroundGaussianFilterRadius(int radius, ChoisePoint iX) {
		try {
			stub.setBackgroundGaussianFilterRadius(radius);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getBackgroundGaussianFilterRadius(ChoisePoint iX) {
		try {
			return stub.getBackgroundGaussianFilterRadius();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// BackgroundRankFilteringMode
	//
	public void setBackgroundRankFilteringMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setBackgroundRankFilteringMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getBackgroundRankFilteringMode(ChoisePoint iX) {
		try {
			return stub.getBackgroundRankFilteringMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// BackgroundRankFilterThreshold
	//
	public void setBackgroundRankFilterThreshold(int threshold, ChoisePoint iX) {
		try {
			stub.setBackgroundRankFilterThreshold(threshold);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getBackgroundRankFilterThreshold(ChoisePoint iX) {
		try {
			return stub.getBackgroundRankFilterThreshold();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// BackgroundStandardDeviationFactor
	//
	public void setBackgroundStandardDeviationFactor(double factor, ChoisePoint iX) {
		try {
			stub.setBackgroundStandardDeviationFactor(factor);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double getBackgroundStandardDeviationFactor(ChoisePoint iX) {
		try {
			return stub.getBackgroundStandardDeviationFactor();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// ForegroundContouringMode
	//
	public void setForegroundContouringMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setForegroundContouringMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getForegroundContouringMode(ChoisePoint iX) {
		try {
			return stub.getForegroundContouringMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// R2WindowHalfwidth
	//
	public void setR2WindowHalfwidth(int halfwidth, ChoisePoint iX) {
		try {
			stub.setR2WindowHalfwidth(halfwidth);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getR2WindowHalfwidth(ChoisePoint iX) {
		try {
			return stub.getR2WindowHalfwidth();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// HorizontalBlobBorder
	//
	public void setHorizontalBlobBorder(int size, ChoisePoint iX) {
		try {
			stub.setHorizontalBlobBorder(size);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getHorizontalBlobBorder(ChoisePoint iX) {
		try {
			return stub.getHorizontalBlobBorder();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// VerticalBlobBorder
	//
	public void setVerticalBlobBorder(int size, ChoisePoint iX) {
		try {
			stub.setVerticalBlobBorder(size);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getVerticalBlobBorder(ChoisePoint iX) {
		try {
			return stub.getVerticalBlobBorder();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// HorizontalExtraBorderCoefficient
	//
	public void setHorizontalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		try {
			stub.setHorizontalExtraBorderCoefficient(coefficient);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double getHorizontalExtraBorderCoefficient(ChoisePoint iX) {
		try {
			return stub.getHorizontalExtraBorderCoefficient();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// VerticalExtraBorderCoefficient
	//
	public void setVerticalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		try {
			stub.setVerticalExtraBorderCoefficient(coefficient);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double getVerticalExtraBorderCoefficient(ChoisePoint iX) {
		try {
			return stub.getVerticalExtraBorderCoefficient();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// MinimalBlobIntersectionArea
	//
	public void setMinimalBlobIntersectionArea(int size, ChoisePoint iX) {
		try {
			stub.setMinimalBlobIntersectionArea(size);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getMinimalBlobIntersectionArea(ChoisePoint iX) {
		try {
			return stub.getMinimalBlobIntersectionArea();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// MinimalBlobSize
	//
	public void setMinimalBlobSize(int size, ChoisePoint iX) {
		try {
			stub.setMinimalBlobSize(size);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getMinimalBlobSize(ChoisePoint iX) {
		try {
			return stub.getMinimalBlobSize();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// MinimalTrackDuration
	//
	public void setMinimalTrackDuration(int frames, ChoisePoint iX) {
		try {
			stub.setMinimalTrackDuration(frames);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getMinimalTrackDuration(ChoisePoint iX) {
		try {
			return stub.getMinimalTrackDuration();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// MaximalBlobInvisibilityInterval
	//
	public void setMaximalBlobInvisibilityInterval(int frames, ChoisePoint iX) {
		try {
			stub.setMaximalBlobInvisibilityInterval(frames);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getMaximalBlobInvisibilityInterval(ChoisePoint iX) {
		try {
			return stub.getMaximalBlobInvisibilityInterval();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// MaximalTrackRetentionInterval
	//
	public void setMaximalTrackRetentionInterval(int frames, ChoisePoint iX) {
		try {
			stub.setMaximalTrackRetentionInterval(frames);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getMaximalTrackRetentionInterval(ChoisePoint iX) {
		try {
			return stub.getMaximalTrackRetentionInterval();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// InverseTransformationMatrix
	//
	public void setInverseTransformationMatrix(double[][] matrix, ChoisePoint iX) {
		try {
			byte[] bytes= Converters.serializeMatrix(matrix);
			stub.setSerializedInverseTransformationMatrix(bytes);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public void setSerializedInverseTransformationMatrix(byte[] matrix, ChoisePoint iX) {
		try {
			stub.setSerializedInverseTransformationMatrix(matrix);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double[][] getInverseTransformationMatrix(ChoisePoint iX) {
		try {
			byte[] bytes= stub.getSerializedInverseTransformationMatrix();
			return Converters.deserializeMatrix(bytes);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedInverseTransformationMatrix(ChoisePoint iX) {
		try {
			return stub.getSerializedInverseTransformationMatrix();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// SamplingRate
	//
	public void setSamplingRate(double rate, ChoisePoint iX) {
		try {
			stub.setSamplingRate(rate);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double getSamplingRate(ChoisePoint iX) {
		try {
			return stub.getSamplingRate();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// CharacteristicLengthMedianFilteringMode
	//
	public void setCharacteristicLengthMedianFilteringMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setCharacteristicLengthMedianFilteringMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getCharacteristicLengthMedianFilteringMode(ChoisePoint iX) {
		try {
			return stub.getCharacteristicLengthMedianFilteringMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// CharacteristicLengthMedianFilterHalfwidth
	//
	public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		try {
			stub.setCharacteristicLengthMedianFilterHalfwidth(halfwidth);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getCharacteristicLengthMedianFilterHalfwidth(ChoisePoint iX) {
		try {
			return stub.getCharacteristicLengthMedianFilterHalfwidth();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// VelocityMedianFilteringMode
	//
	public void setVelocityMedianFilteringMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setVelocityMedianFilteringMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getVelocityMedianFilteringMode(ChoisePoint iX) {
		try {
			return stub.getVelocityMedianFilteringMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// VelocityMedianFilterHalfwidth
	//
	public void setVelocityMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		try {
			stub.setVelocityMedianFilterHalfwidth(halfwidth);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getVelocityMedianFilterHalfwidth(ChoisePoint iX) {
		try {
			return stub.getVelocityMedianFilterHalfwidth();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// SlowTracksDeletionMode
	//
	public void setSlowTracksDeletionMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setSlowTracksDeletionMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getSlowTracksDeletionMode(ChoisePoint iX) {
		try {
			return stub.getSlowTracksDeletionMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// FuzzyVelocityThreshold
	//
	public void setFuzzyVelocityThreshold(double threshold, ChoisePoint iX) {
		try {
			stub.setFuzzyVelocityThreshold(threshold);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double getFuzzyVelocityThreshold(ChoisePoint iX) {
		try {
			return stub.getFuzzyVelocityThreshold();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// FuzzyDistanceThreshold
	//
	public void setFuzzyDistanceThreshold(double threshold, ChoisePoint iX) {
		try {
			stub.setFuzzyDistanceThreshold(threshold);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double getFuzzyDistanceThreshold(ChoisePoint iX) {
		try {
			return stub.getFuzzyDistanceThreshold();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// FuzzyThresholdBorder
	//
	public void setFuzzyThresholdBorder(double size, ChoisePoint iX) {
		try {
			stub.setFuzzyThresholdBorder(size);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double getFuzzyThresholdBorder(ChoisePoint iX) {
		try {
			return stub.getFuzzyThresholdBorder();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// SynthesizedImageTransparency
	//
	public void setSynthesizedImageTransparency(int transparency, ChoisePoint iX) {
		try {
			stub.setSynthesizedImageTransparency(transparency);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public int getSynthesizedImageTransparency(ChoisePoint iX) {
		try {
			return stub.getSynthesizedImageTransparency();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	// SynthesizedImageRectangularBlobsMode
	//
	public void setSynthesizedImageRectangularBlobsMode(boolean mode, ChoisePoint iX) {
		try {
			stub.setSynthesizedImageRectangularBlobsMode(mode);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public boolean getSynthesizedImageRectangularBlobsMode(ChoisePoint iX) {
		try {
			return stub.getSynthesizedImageRectangularBlobsMode();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void subtract(long frame, java.awt.image.BufferedImage nativeImage, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		Space2DWriter writer= Space2DWriter.createSpace2DWriter(nativeImage,attributes);
		try {
			byte[] bytes= writer.imageToBytes(nativeImage);
			try {
				stub.subtract(frame,bytes,takeFrameIntoAccount,attributes);
			} catch (OwnWorldIsNotImageSubtractor e) {
				throw new WrongArgumentIsNotImageSubtractor(wrapper);
			} catch (RemoteException e) {
				throw new RemoteCallError(e);
			}
		} finally {
			writer.dispose();
		}
	}
	public void subtract(long frame, byte[] image, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		try {
			stub.subtract(frame,image,takeFrameIntoAccount,attributes);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit(ChoisePoint iX) {
		try {
			stub.commit();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetSettings(ChoisePoint iX) {
		try {
			stub.resetSettings();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void resetStatistics(ChoisePoint iX) {
		try {
			stub.resetStatistics();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void resetResults(ChoisePoint iX) {
		try {
			stub.resetResults();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void resetAll(ChoisePoint iX) {
		try {
			stub.resetAll();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		return new PrologInteger(getFrameNumber(iX));
	}
	public long getFrameNumber(ChoisePoint iX) {
		try {
			return stub.getFrameNumber();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage(Term value, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= getRecentImage(iX);
		modifyImage(value,nativeImage,iX);
	}
	public java.awt.image.BufferedImage getRecentImage(ChoisePoint iX) {
		try {
			byte[] bytes= stub.getRecentImage();
			return convertBytesToImage(bytes);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedRecentImage(ChoisePoint iX) {
		try {
			return stub.getRecentImage();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void getBackgroundImage(Term value, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= getBackgroundImage(iX);
		modifyImage(value,nativeImage,iX);
	}
	public java.awt.image.BufferedImage getBackgroundImage(ChoisePoint iX) {
		try {
			byte[] bytes= stub.getBackgroundImage();
			return convertBytesToImage(bytes);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedBackgroundImage(ChoisePoint iX) {
		try {
			return stub.getBackgroundImage();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void getSigmaImage(Term value, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= getSigmaImage(iX);
		modifyImage(value,nativeImage,iX);
	}
	public java.awt.image.BufferedImage getSigmaImage(ChoisePoint iX) {
		try {
			byte[] bytes= stub.getSigmaImage();
			return convertBytesToImage(bytes);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedSigmaImage(ChoisePoint iX) {
		try {
			return stub.getSigmaImage();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void getForegroundImage(Term value, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= getForegroundImage(iX);
		modifyImage(value,nativeImage,iX);
	}
	public java.awt.image.BufferedImage getForegroundImage(ChoisePoint iX) {
		try {
			byte[] bytes= stub.getForegroundImage();
			return convertBytesToImage(bytes);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedForegroundImage(ChoisePoint iX) {
		try {
			return stub.getForegroundImage();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void getSynthesizedImage(Term value, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= getSynthesizedImage(iX);
		modifyImage(value,nativeImage,iX);
	}
	public java.awt.image.BufferedImage getSynthesizedImage(ChoisePoint iX) {
		try {
			byte[] bytes= stub.getSynthesizedImage();
			return convertBytesToImage(bytes);
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX) {
		try {
			return stub.getSynthesizedImage();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	protected java.awt.image.BufferedImage convertBytesToImage(byte[] bytes) {
		if (bytes != null) {
			java.awt.image.BufferedImage nativeImage= Space2DWriter.bytesToImage(bytes);
			if (nativeImage != null) {
				return nativeImage;
			} else {
				throw new ImageDecodingError();
			}
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void modifyImage(Term value, java.awt.image.BufferedImage nativeImage, ChoisePoint iX) {
		if (nativeImage != null) {
			try {
				value= value.dereferenceValue(iX);
				if (value instanceof BufferedImage) {
					GenericImageEncodingAttributes attributes= stub.getCurrentImageEncodingAttributes();
					BufferedImage image= (BufferedImage)value;
					image.setImage(nativeImage,attributes);
				} else if (value instanceof ForeignWorldWrapper) {
					GenericImageEncodingAttributes attributes= stub.getCurrentImageEncodingAttributes();
					ForeignWorldWrapper wrapper= (ForeignWorldWrapper)value;
					wrapper.setImage(nativeImage,attributes);
				} else {
					throw new WrongArgumentIsNotBufferedImage(value);
				}
			} catch (OwnWorldIsNotImageSubtractor e) {
				throw new WrongArgumentIsNotImageSubtractor(wrapper);
			} catch (RemoteException e) {
				throw new RemoteCallError(e);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getBlobs(ChoisePoint iX) {
		return Converters.deserializeArgument(getSerializedBlobs(iX));
	}
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		try {
			return stub.getBlobs();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public Term getTracks(ChoisePoint iX) {
		return Converters.deserializeArgument(getSerializedTracks(iX));
	}
	public byte[] getSerializedTracks(ChoisePoint iX) {
		try {
			return stub.getTracks();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public Term getConnectedGraphs(ChoisePoint iX) {
		return Converters.deserializeArgument(getSerializedConnectedGraphs(iX));
	}
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		try {
			return stub.getConnectedGraphs();
		} catch (OwnWorldIsNotImageSubtractor e) {
			throw new WrongArgumentIsNotImageSubtractor(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		return wrapper;
	}
}
