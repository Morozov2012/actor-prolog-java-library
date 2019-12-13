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
	protected ExternalWorldInterface stub;
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
	@Override
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
	@Override
	public void withdrawRequest(Resident resident) {
                try {
			ExternalResidentInterface residentStub= OwnResidentWrapper.registerResident(resident);
			stub.withdrawRequest(residentStub);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	@Override
	public void receiveAsyncCall(AsyncCall item) {
		long domainSignatureNumber= item.getDomainSignatureNumber();
		Long externalSignatureNumber= changeSignatureNumber(domainSignatureNumber);
		byte[] byteArray= GeneralConverters.serializeArguments(item.getArguments());
                try {
			stub.receiveAsyncCall(
				externalSignatureNumber,
				item.isControlCall(),
				item.useBuffer(),
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
	@Override
	public void startProcesses() {
	}
	@Override
	public void releaseSystemResources() {
	}
	@Override
	public void stopProcesses() {
	}
	//
	@Override
	public MethodSignature[] getMethodSignatures() {
		return emptySignatureList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean thisIsOwnWorld() {
		return false;
	}
	//
	@Override
	public boolean thisIsForeignWorld() {
		return true;
	}
	//
	@Override
	public boolean isInternalWorldOf(AbstractProcess currentProcess) {
		return false;
	}
	//
	@Override
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw Backtracking.instance;
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		throw new AnAttemptToExtractInternalWorldFromForeignWorldWrapper();
	}
	//
	@Override
	public AbstractInternalWorld internalWorld() {
		throw new AnAttemptToExtractInternalWorldFromForeignWorldWrapper();
	}
	//
	@Override
	public boolean isNumberOfTemporaryActor() {
		return false;
	}
}
