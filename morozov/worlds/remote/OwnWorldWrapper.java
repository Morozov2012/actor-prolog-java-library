// (c) 2015-2017 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.built_in.*;
import morozov.domains.*;
import morozov.run.*;
import morozov.run.errors.*;
import morozov.system.*;
import morozov.system.datum.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.plain.*;
import morozov.system.vision.vpm.*;
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
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, GenericImageEncodingAttributes attributes) throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			vpm.process(nativeImage,frameNumber,timeInMilliseconds, takeFrameIntoAccount,iX,attributes);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public void process(byte[] bytes, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, GenericImageEncodingAttributes attributes) throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			vpm.process(bytes,frameNumber,timeInMilliseconds, takeFrameIntoAccount,iX,attributes);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	//
	public long getFrameNumber() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getFrameNumber(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	//
	public void commit() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			vpm.commit(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	//
	public void resetSettings() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			vpm.resetSettings(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public void resetStatistics() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			vpm.resetStatistics(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public void resetResults() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			vpm.resetResults(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public void resetAll() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			vpm.resetAll(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	//
	public byte[] getBlobs() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedBlobs(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public byte[] getTracks() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedTracks(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public byte[] getConnectedGraphs() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedConnectedGraphs(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public byte[] getChronicle() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedChronicle(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	//
	public byte[] getRecentImage() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedRecentImage(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public byte[] getPreprocessedImage() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedPreprocessedImage(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public byte[] getForegroundImage() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedForegroundImage(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public byte[] getSynthesizedImage() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedSynthesizedImage(iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	//
	public byte[] getBackgroundImage(int layerNumber) throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedBackgroundImage(layerNumber,iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public byte[] getSigmaImage(int layerNumber) throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getSerializedSigmaImage(layerNumber,iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	//
	public double[] physicalCoordinates(int pixelX, int pixelY) throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.physicalCoordinates(pixelX,pixelY,iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	public double characteristicLength(int x, int y) throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			ChoisePoint iX= null;
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.characteristicLength(x,y,iX);
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
		}
	}
	//
	public GenericImageEncodingAttributes getCurrentImageEncodingAttributes() throws OwnWorldIsNotVideoProcessingMachine {
		if (ownWorld instanceof VideoProcessingMachineOperations) {
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)ownWorld;
			return vpm.getCurrentImageEncodingAttributes();
		} else {
			throw OwnWorldIsNotVideoProcessingMachine.instance;
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
}
