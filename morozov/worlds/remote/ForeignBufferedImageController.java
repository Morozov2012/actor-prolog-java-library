// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.vpm.errors.*;
import morozov.terms.*;
import morozov.worlds.remote.errors.*;
import morozov.worlds.remote.signals.*;

import java.rmi.RemoteException;

public class ForeignBufferedImageController {
	//
	protected ForeignWorldWrapper wrapper;
	protected ExternalWorldInterface stub;
	//
	public ForeignBufferedImageController(ForeignWorldWrapper w) {
		wrapper= w;
		stub= wrapper.stub;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public GenericImageEncodingAttributes getCurrentImageEncodingAttributes() {
		try {
			return stub.getCurrentImageEncodingAttributes();
		} catch (OwnWorldIsNotVideoProcessingMachine e) {
			throw new WrongArgumentIsNotVideoProcessingMachine(wrapper);
		} catch (RemoteException e) {
			throw new RemoteCallError(e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
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
					ForeignWorldWrapper currentWrapper= (ForeignWorldWrapper)value;
					currentWrapper.setImage(nativeImage,attributes);
				} else {
					throw new WrongArgumentIsNotBufferedImage(value);
				}
			} catch (OwnWorldIsNotVideoProcessingMachine e) {
				throw new WrongArgumentIsNotVideoProcessingMachine(value);
			} catch (RemoteException e) {
				throw new RemoteCallError(e);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		return wrapper;
	}
}
