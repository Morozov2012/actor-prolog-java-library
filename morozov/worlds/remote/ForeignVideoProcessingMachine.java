// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.errors.*;
import morozov.terms.*;
import morozov.worlds.remote.errors.*;
import morozov.worlds.remote.signals.*;

import java.rmi.RemoteException;

public class ForeignVideoProcessingMachine
		extends ForeignBufferedImageController
		implements VideoProcessingMachineOperations {
	//
	public ForeignVideoProcessingMachine(ForeignWorldWrapper w) {
		super(w);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		Space2DWriter writer= Space2DWriter.createSpace2DWriter(nativeImage,attributes);
		try {
			byte[] bytes= writer.imageToBytes(nativeImage);
			try {
				stub.process(nativeImage,frameNumber,timeInMilliseconds,takeFrameIntoAccount,attributes);
			} catch (OwnWorldIsNotVideoProcessingMachine e) {
				throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
			} catch (RemoteException e) {
				throw new RemoteCallError(e);
			}
		} finally {
			writer.dispose();
		}
	}
	public void process(byte[] bytes, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		try {
			stub.process(bytes,frameNumber,timeInMilliseconds,takeFrameIntoAccount,attributes);
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
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
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
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
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public void resetStatistics(ChoisePoint iX) {
		try {
			stub.resetStatistics();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public void resetResults(ChoisePoint iX) {
		try {
			stub.resetResults();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public void resetAll(ChoisePoint iX) {
		try {
			stub.resetAll();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getFrameNumber(ChoisePoint iX) {
		try {
			return stub.getFrameNumber();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		return new PrologInteger(getFrameNumber(iX));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getFrameTime(ChoisePoint iX) {
		try {
			return stub.getFrameTime();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public Term getFrameTimeOrSpacer(ChoisePoint iX) {
		return new PrologInteger(getFrameTime(iX));
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
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedRecentImage(ChoisePoint iX) {
		try {
			return stub.getRecentImage();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void getPreprocessedImage(Term value, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= getPreprocessedImage(iX);
		modifyImage(value,nativeImage,iX);
	}
	public java.awt.image.BufferedImage getPreprocessedImage(ChoisePoint iX) {
		try {
			byte[] bytes= stub.getPreprocessedImage();
			return convertBytesToImage(bytes);
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedPreprocessedImage(ChoisePoint iX) {
		try {
			return stub.getPreprocessedImage();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
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
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedForegroundImage(ChoisePoint iX) {
		try {
			return stub.getForegroundImage();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
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
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX) {
		try {
			return stub.getSynthesizedImage();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getBackgroundImage(Term value, int layerNumber, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= getBackgroundImage(layerNumber,iX);
		modifyImage(value,nativeImage,iX);
	}
	public java.awt.image.BufferedImage getBackgroundImage(int layerNumber, ChoisePoint iX) {
		try {
			byte[] bytes= stub.getBackgroundImage(layerNumber);
			return convertBytesToImage(bytes);
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedBackgroundImage(int layerNumber, ChoisePoint iX) {
		try {
			return stub.getBackgroundImage(layerNumber);
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void getSigmaImage(Term value, int layerNumber, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= getSigmaImage(layerNumber,iX);
		modifyImage(value,nativeImage,iX);
	}
	public java.awt.image.BufferedImage getSigmaImage(int layerNumber, ChoisePoint iX) {
		try {
			byte[] bytes= stub.getSigmaImage(layerNumber);
			return convertBytesToImage(bytes);
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public byte[] getSerializedSigmaImage(int layerNumber, ChoisePoint iX) {
		try {
			return stub.getSigmaImage(layerNumber);
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getBlobs(ChoisePoint iX) {
		return GeneralConverters.deserializeArgument(getSerializedBlobs(iX));
	}
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		try {
			return stub.getBlobs();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public Term getTracks(ChoisePoint iX) {
		return GeneralConverters.deserializeArgument(getSerializedTracks(iX));
	}
	public byte[] getSerializedTracks(ChoisePoint iX) {
		try {
			return stub.getTracks();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public Term getConnectedGraphs(ChoisePoint iX) {
		return GeneralConverters.deserializeArgument(getSerializedConnectedGraphs(iX));
	}
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		try {
			return stub.getConnectedGraphs();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public Term getChronicle(ChoisePoint iX) {
		return GeneralConverters.deserializeArgument(getSerializedChronicle(iX));
	}
	public byte[] getSerializedChronicle(ChoisePoint iX) {
		try {
			return stub.getChronicle();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[] physicalCoordinates(int pixelX, int pixelY, ChoisePoint iX) {
		try {
			return stub.physicalCoordinates(pixelX,pixelY);
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	public double characteristicLength(int x, int y, ChoisePoint iX) {
		try {
			return stub.characteristicLength(x,y);
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
}
