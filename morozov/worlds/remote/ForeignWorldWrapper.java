// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import target.*;

import morozov.domains.*;
import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.space2d.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;
import morozov.worlds.remote.errors.*;
import morozov.worlds.remote.signals.*;

import java.rmi.RemoteException;
import java.util.HashMap;

public class ForeignWorldWrapper extends AbstractWorld {
	//
	public ExternalWorldInterface stub;
	//
	protected HashMap<Long,Long> signatureMap= new HashMap<>();
	//
	private static final long serialVersionUID= 0xC9F4C0E6A8A4AE0L; // 909529274552830688L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds.remote","ForeignWorldWrapper");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ForeignWorldWrapper(ExternalWorldInterface s) {
		stub= s;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendResidentRequest(Resident resident, long domainSignatureNumber, Term[] arguments, boolean sortAndReduceResultList) {
		Long externalSignatureNumber= changeSignatureNumber(domainSignatureNumber);
                try {
			ExternalResidentInterface residentStub= OwnResidentWrapper.registerResident(resident);
			byte[] byteArray= GeneralConverters.serializeArguments(arguments);
			stub.sendResidentRequest(
				residentStub,
				externalSignatureNumber,
				byteArray,
				sortAndReduceResultList);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void withdrawRequest(Resident resident) {
                try {
			ExternalResidentInterface residentStub= OwnResidentWrapper.registerResident(resident);
			stub.withdrawRequest(residentStub);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void receiveAsyncCall(AsyncCall item) {
		long domainSignatureNumber= item.domainSignatureNumber;
		Long externalSignatureNumber= changeSignatureNumber(domainSignatureNumber);
		byte[] byteArray= GeneralConverters.serializeArguments(item.arguments);
                try {
			stub.receiveAsyncCall(
				externalSignatureNumber,
				item.isControlCall,
				item.useBuffer,
				byteArray);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected long changeSignatureNumber(long domainSignatureNumber) {
		Long externalSignatureNumber= signatureMap.get(domainSignatureNumber);
		if (externalSignatureNumber==null) {
			MethodSignature signature= MethodSignatures.getSignature(domainSignatureNumber);
			HashMap<String,PrologDomain> localDomainTable= new HashMap<>();
			signature.collectLocalDomainTable(localDomainTable);
			byte[] arrayTable= GeneralConverters.serializeDomainTable(localDomainTable);
			try {
				externalSignatureNumber= stub.selectSignature(signature,arrayTable);
				signatureMap.put(domainSignatureNumber,externalSignatureNumber);
			} catch (RemoteException e) {
				throw new RemoteCallError(e);
			}
		};
		return externalSignatureNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public java.awt.image.BufferedImage getImage() {
		try {
			byte[] bytes= stub.getImage();
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
		} catch (OwnWorldIsNotBufferedImage e) {
			throw new WrongArgumentIsNotBufferedImage(stub);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	public void setImage(java.awt.image.BufferedImage nativeImage, GenericImageEncodingAttributes attributes) {
		Space2DWriter writer= Space2DWriter.createSpace2DWriter(nativeImage,attributes);
		try {
			byte[] bytes= writer.imageToBytes(nativeImage);
			try {
				stub.setImage(bytes,attributes);
			} catch (OwnWorldIsNotBufferedImage e) {
				throw new WrongArgumentIsNotBufferedImage(stub);
			} catch (RemoteException e) {
				throw new RemoteCallError(e);
			}
		} finally {
			writer.dispose();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void startProcesses() {
	}
	public void releaseSystemResources() {
	}
	public void stopProcesses() {
	}
	//
	public MethodSignature[] getMethodSignatures() {
		return emptySignatureList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean thisIsOwnWorld() {
		return false;
	}
	//
	public boolean thisIsForeignWorld() {
		return true;
	}
	//
	public boolean isInternalWorldOf(AbstractProcess currentProcess) {
		return false;
	}
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw Backtracking.instance;
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		throw new AnAttemptToExtractInternalWorldFromForeignWorldWrapper();
	}
	//
	public AbstractInternalWorld internalWorld() {
		throw new AnAttemptToExtractInternalWorldFromForeignWorldWrapper();
	}
	//
	public boolean isNumberOfTemporaryActor() {
		return false;
	}
}
