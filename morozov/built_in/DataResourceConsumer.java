// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.files.*;
import morozov.terms.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

public abstract class DataResourceConsumer extends DataAbstraction {
	//
	public DataResourceConsumer() {
	}
	public DataResourceConsumer(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	// abstract protected Term getBuiltInSlot_E_max_waiting_time();
	// abstract protected Term getBuiltInSlot_E_backslash_always_is_separator();
	//
	///////////////////////////////////////////////////////////////
	//
	public java.awt.image.BufferedImage readImage(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(iX);
		int timeout= getMaxWaitingTimeInMilliseconds(iX);
		return fileName.readImage(timeout,staticContext);
	}
	public java.awt.image.BufferedImage readImage(Term targetName, ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(targetName,iX);
		int timeout= getMaxWaitingTimeInMilliseconds(iX);
		return fileName.readImage(timeout,staticContext);
	}
	public java.awt.image.BufferedImage readImage(String textName, ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealGlobalFileName(textName,iX);
		int timeout= getMaxWaitingTimeInMilliseconds(iX);
		return fileName.readImage(timeout,staticContext);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public java.awt.image.BufferedImage acquireNativeImage(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)value;
			return image.getImage();
		} else if (value instanceof ForeignWorldWrapper) {
			ForeignWorldWrapper wrapper= (ForeignWorldWrapper)value;
			return wrapper.getImage();
		} else {
			return readImage(value,iX);
		}
	}
}
