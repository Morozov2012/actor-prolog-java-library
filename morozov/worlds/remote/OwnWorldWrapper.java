// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.built_in.*;
import morozov.domains.*;
import morozov.run.*;
import morozov.run.errors.*;
import morozov.system.*;
import morozov.system.datum.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;
import morozov.worlds.remote.signals.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class OwnWorldWrapper
		extends AbstractWorld
		implements ExternalWorldInterface {
	//
	protected AbstractWorld ownWorld;
	//
	public static HashMap<AbstractWorld,ExternalWorldInterface> ownWorldRegister= new HashMap<>();
	public static HashMap<ExternalWorldInterface,OwnWorldWrapper> invertedOwnWorldRegister= new HashMap<>();
	//
	public static HashMap<ExternalWorldInterface,ForeignWorldWrapper> foreignWorldRegister= new HashMap<>();
	//
	protected transient MethodSignature[] methodSignatures= null;
	protected transient HashMap<String,MethodSignature[]> methodHash= new HashMap<>();
	//
	///////////////////////////////////////////////////////////////
	//
	public OwnWorldWrapper(AbstractWorld w) {
		super(w.getGlobalWorldIdentifier());
		ownWorld= w;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExternalWorldInterface registerWorld(AbstractWorld world) throws RemoteException {
		if (world instanceof ForeignWorldWrapper) {
			ForeignWorldWrapper wrapper= (ForeignWorldWrapper)world;
			return wrapper.stub;
		} else {
			ExternalWorldInterface stub= ownWorldRegister.get(world);
			if (stub == null) {
				OwnWorldWrapper wrapper= new OwnWorldWrapper(world);
				stub= (ExternalWorldInterface) UnicastRemoteObject.exportObject(wrapper,0);
				ownWorldRegister.put(world,stub);
				invertedOwnWorldRegister.put(stub,wrapper);
			};
			return stub;
		}
	}
	//
	public static AbstractWorld registerWrapper(ExternalWorldInterface stub, DataStoreInputStream dataStream) {
		OwnWorldWrapper wrapper1= invertedOwnWorldRegister.get(stub);
		if (wrapper1 != null) {
			dataStream.declareWorld();
			return wrapper1.ownWorld;
		} else {
			ForeignWorldWrapper wrapper2= foreignWorldRegister.get(stub);
			if (wrapper2 == null) {
				wrapper2= new ForeignWorldWrapper(stub);
				foreignWorldRegister.put(stub,wrapper2);
			};
			return wrapper2;
		}
	}
	public static AbstractWorld registerWrapper(ExternalWorldInterface stub) {
		OwnWorldWrapper wrapper1= invertedOwnWorldRegister.get(stub);
		if (wrapper1 != null) {
			return wrapper1.ownWorld;
		} else {
			ForeignWorldWrapper wrapper2= foreignWorldRegister.get(stub);
			if (wrapper2 == null) {
				wrapper2= new ForeignWorldWrapper(stub);
				foreignWorldRegister.put(stub,wrapper2);
			};
			return wrapper2;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long selectSignature(MethodSignature foreignSignature, byte[] arrayTable) {
		HashMap<String,PrologDomain> foreignDomainTable= Converters.deserializeDomainTable(arrayTable);
		foreignSignature.acceptLocalDomainTable(foreignDomainTable);
		if (methodSignatures==null) {
			methodSignatures= ownWorld.getMethodSignatures();
			for (int n=0; n < methodSignatures.length; n++) {
				MethodSignature ownSignature= methodSignatures[n];
				String methodName= ownSignature.methodName;
				MethodSignature[] value= methodHash.get(methodName);
				if (value == null) {
					int counter= 0;
					for (int k=0; k < methodSignatures.length; k++) {
						if (methodName.equals(methodSignatures[k].methodName)) {
							counter++;
						}
					};
					MethodSignature[] localList= new MethodSignature[counter];
					counter= 0;
					for (int k=0; k < methodSignatures.length; k++) {
						if (methodName.equals(methodSignatures[k].methodName)) {
							localList[counter]= methodSignatures[k];
							counter++;
						}
					};
					methodHash.put(methodName,localList);
				}
			};
		};
		String methodName= foreignSignature.methodName;
		MethodSignature[] localList= methodHash.get(methodName);
		if (localList != null) {
			for (int k=0; k < localList.length; k ++) {
				if (localList[k].match(foreignSignature)) {
					return localList[k].domainSignature;
				}
			}
		};
		throw new CannotMatchSignature();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList) {
		ownWorld.sendResidentRequest(resident,domainSignature,arguments,sortAndReduceResultList);
	}
	public void sendResidentRequest(ExternalResidentInterface stub, long domainSignatureNumber, byte[] argumentByteArray, boolean sortAndReduceResultList) {
		Resident resident= OwnResidentWrapper.registerWrapper(stub);
		Term[] arguments= Converters.deserializeArguments(argumentByteArray,domainSignatureNumber);
		ownWorld.sendResidentRequest(resident,domainSignatureNumber,arguments,sortAndReduceResultList);
	}
	//
	public void withdrawRequest(Resident resident) {
		ownWorld.withdrawRequest(resident);
	}
	public void withdrawRequest(ExternalResidentInterface stub) {
		Resident resident= OwnResidentWrapper.registerWrapper(stub);
		ownWorld.withdrawRequest(resident);
	}
	//
	public void transmitAsyncCall(AsyncCall item, ChoisePoint iX) {
		ownWorld.transmitAsyncCall(item,iX);
	}
	//
	public void receiveAsyncCall(AsyncCall item) {
		ownWorld.receiveAsyncCall(item);
	}
	public void receiveAsyncCall(long domainSignatureNumber, boolean isControlCall, boolean useBuffer, byte[] argumentByteArray) {
		Term[] arguments= Converters.deserializeArguments(argumentByteArray,domainSignatureNumber);
		// ownWorld.receiveAsyncCall(new AsyncCall(domainSignatureNumber,ownWorld,ownWorld,isControlCall,useBuffer,arguments,true));
		ChoisePoint iX= null;
		ownWorld.transmitAsyncCall(new AsyncCall(domainSignatureNumber,ownWorld,ownWorld,isControlCall,useBuffer,arguments,true),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public byte[] getImage() throws OwnWorldIsNotBufferedImage {
		if (ownWorld instanceof BufferedImage) {
			BufferedImage bufferedImage= (BufferedImage)ownWorld;
			java.awt.image.BufferedImage nativeImage= bufferedImage.getImage();
			GenericImageEncodingAttributes attributes= bufferedImage.getCurrentImageEncodingAttributes();
			if (nativeImage != null && attributes != null) {
				Space2DWriter writer= Space2DWriter.createSpace2DWriter(nativeImage,attributes);
				try {
					return writer.imageToBytes(nativeImage);
				} finally {
					writer.dispose();
				}
			} else {
				return null;
			}
		} else {
			throw OwnWorldIsNotBufferedImage.instance;
		}
	}
	//
	public void setImage(byte[] bytes, GenericImageEncodingAttributes attributes) throws OwnWorldIsNotBufferedImage {
		if (ownWorld instanceof BufferedImage) {
			BufferedImage bufferedImage= (BufferedImage)ownWorld;
			java.awt.image.BufferedImage nativeImage= Space2DWriter.bytesToImage(bytes);
			bufferedImage.setImage(nativeImage,attributes);
		} else {
			throw OwnWorldIsNotBufferedImage.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void subtract(long frame, byte[] image, boolean takeFrameIntoAccount, GenericImageEncodingAttributes attributes) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.subtract(frame,image,takeFrameIntoAccount,iX,attributes);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public long getFrameNumber() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			long frameNumber= subtractor.getFrameNumber(iX);
			return frameNumber;
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public void commit() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.commit(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public void resetSettings() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.resetSettings(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public void resetStatistics() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.resetStatistics(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public void resetResults() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.resetResults(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public void resetAll() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.resetAll(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public byte[] getBlobs() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedBlobs(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public byte[] getTracks() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedTracks(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public byte[] getConnectedGraphs() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedConnectedGraphs(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public byte[] getRecentImage() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedRecentImage(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public byte[] getBackgroundImage() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedBackgroundImage(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public byte[] getSigmaImage() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedSigmaImage(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public byte[] getForegroundImage() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedForegroundImage(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public byte[] getSynthesizedImage() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedSynthesizedImage(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public GenericImageEncodingAttributes getCurrentImageEncodingAttributes() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getCurrentImageEncodingAttributes();
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public boolean getBlobExtractionMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getBlobExtractionMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getBlobTracingMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getBlobTracingMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getMinimalTrainingInterval() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getMinimalTrainingInterval(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getMaximalTrainingInterval() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getMaximalTrainingInterval(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getGrayscaleMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getGrayscaleMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getBackgroundGaussianFilteringMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getBackgroundGaussianFilteringMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getBackgroundGaussianFilterRadius() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getBackgroundGaussianFilterRadius(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getBackgroundRankFilteringMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getBackgroundRankFilteringMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getBackgroundRankFilterThreshold() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getBackgroundRankFilterThreshold(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public double getBackgroundStandardDeviationFactor() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getBackgroundStandardDeviationFactor(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getForegroundContouringMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getForegroundContouringMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getR2WindowHalfwidth() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getR2WindowHalfwidth(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getHorizontalBlobBorder() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getHorizontalBlobBorder(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getVerticalBlobBorder() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getVerticalBlobBorder(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public double getHorizontalExtraBorderCoefficient() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getHorizontalExtraBorderCoefficient(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public double getVerticalExtraBorderCoefficient() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getVerticalExtraBorderCoefficient(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getMinimalBlobIntersectionArea() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getMinimalBlobIntersectionArea(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getMinimalBlobSize() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getMinimalBlobSize(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getMinimalTrackDuration() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getMinimalTrackDuration(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getMaximalBlobInvisibilityInterval() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getMaximalBlobInvisibilityInterval(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getMaximalTrackRetentionInterval() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getMaximalTrackRetentionInterval(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public double[][] getInverseTransformationMatrix() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getInverseTransformationMatrix(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public byte[] getSerializedInverseTransformationMatrix() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSerializedInverseTransformationMatrix(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public double getSamplingRate() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSamplingRate(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getCharacteristicLengthMedianFilteringMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getCharacteristicLengthMedianFilteringMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getCharacteristicLengthMedianFilterHalfwidth() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getCharacteristicLengthMedianFilterHalfwidth(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getVelocityMedianFilteringMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getVelocityMedianFilteringMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getVelocityMedianFilterHalfwidth() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getVelocityMedianFilterHalfwidth(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getSlowTracksDeletionMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSlowTracksDeletionMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public double getFuzzyVelocityThreshold() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getFuzzyVelocityThreshold(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public double getFuzzyDistanceThreshold() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getFuzzyDistanceThreshold(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public double getFuzzyThresholdBorder() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getFuzzyThresholdBorder(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public int getSynthesizedImageTransparency() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSynthesizedImageTransparency(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public boolean getSynthesizedImageRectangularBlobsMode() throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			return subtractor.getSynthesizedImageRectangularBlobsMode(iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	public void setBlobExtractionMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setBlobExtractionMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setBlobTracingMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setBlobTracingMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setMinimalTrainingInterval(int frames) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setMinimalTrainingInterval(frames,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setMaximalTrainingInterval(int frames) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setMaximalTrainingInterval(frames,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setGrayscaleMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setGrayscaleMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setBackgroundGaussianFilteringMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setBackgroundGaussianFilteringMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setBackgroundGaussianFilterRadius(int radius) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setBackgroundGaussianFilterRadius(radius,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setBackgroundRankFilteringMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setBackgroundRankFilteringMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setBackgroundRankFilterThreshold(int threshold) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setBackgroundRankFilterThreshold(threshold,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setBackgroundStandardDeviationFactor(double factor) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setBackgroundStandardDeviationFactor(factor,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setForegroundContouringMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setForegroundContouringMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setR2WindowHalfwidth(int halfwidth) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setR2WindowHalfwidth(halfwidth,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setHorizontalBlobBorder(int size) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setHorizontalBlobBorder(size,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setVerticalBlobBorder(int size) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setVerticalBlobBorder(size,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setHorizontalExtraBorderCoefficient(double coefficient) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setHorizontalExtraBorderCoefficient(coefficient,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setVerticalExtraBorderCoefficient(double coefficient) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setVerticalExtraBorderCoefficient(coefficient,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setMinimalBlobIntersectionArea(int size) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setMinimalBlobIntersectionArea(size,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setMinimalBlobSize(int size) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setMinimalBlobSize(size,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setMinimalTrackDuration(int frames) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setMinimalTrackDuration(frames,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setMaximalBlobInvisibilityInterval(int frames) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setMaximalBlobInvisibilityInterval(frames,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setMaximalTrackRetentionInterval(int frames) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setMaximalTrackRetentionInterval(frames,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setInverseTransformationMatrix(double[][] matrix) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setInverseTransformationMatrix(matrix,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setSerializedInverseTransformationMatrix(byte[] matrix) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setSerializedInverseTransformationMatrix(matrix,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setSamplingRate(double rate) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setSamplingRate(rate,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setCharacteristicLengthMedianFilteringMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setCharacteristicLengthMedianFilteringMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setCharacteristicLengthMedianFilterHalfwidth(halfwidth,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setVelocityMedianFilteringMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setVelocityMedianFilteringMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setVelocityMedianFilterHalfwidth(int halfwidth) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setVelocityMedianFilterHalfwidth(halfwidth,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setSlowTracksDeletionMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setSlowTracksDeletionMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setFuzzyVelocityThreshold(double threshold) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setFuzzyVelocityThreshold(threshold,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setFuzzyDistanceThreshold(double threshold) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setFuzzyDistanceThreshold(threshold,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setFuzzyThresholdBorder(double threshold) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setFuzzyThresholdBorder(threshold,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setSynthesizedImageTransparency(int transparency) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setSynthesizedImageTransparency(transparency,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	public void setSynthesizedImageRectangularBlobsMode(boolean mode) throws OwnWorldIsNotImageSubtractor {
		if (ownWorld instanceof ImageSubtractorOperations) {
			ChoisePoint iX= null;
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)ownWorld;
			subtractor.setSynthesizedImageRectangularBlobsMode(mode,iX);
		} else {
			throw OwnWorldIsNotImageSubtractor.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void startProcesses() {
		ownWorld.startProcesses();
	}
	public void closeFiles() {
		ownWorld.closeFiles();
	}
	public void stopProcesses() {
		ownWorld.stopProcesses();
	}
	public MethodSignature[] getMethodSignatures() {
		return ownWorld.getMethodSignatures();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean thisIsOwnWorld() {
		return false;
	}
	//
	public boolean thisIsForeignWorld() {
		return false;
	}
	//
	public boolean isInternalWorldOf(AbstractProcess currentProcess) {
		return ownWorld.isInternalWorldOf(currentProcess);
	}
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return ownWorld.getInternalWorld(cp);
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		return ownWorld.internalWorld(process,cp);
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		return ownWorld.internalWorld(iX);
	}
	//
	public AbstractInternalWorld internalWorld() {
		return ownWorld.internalWorld();
	}
	//
	public boolean isNumberOfTemporaryActor() {
		return ownWorld.isNumberOfTemporaryActor();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
	//	stream.defaultReadObject();
	// }
}
