// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.checker.*;
import morozov.system.checker.signals.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class ImageConsumer extends Alpha {
	//
	protected static final int defaultWaitingInterval= -1;
	//
	abstract protected Term getBuiltInSlot_E_max_waiting_time();
	abstract protected Term getBuiltInSlot_E_backslash_always_is_separator();
	//
	// abstract protected Term getBuiltInSlot_E_enable_scene_antialiasing();
	//
	public BufferedImage readImage(Term fileName, ChoisePoint iX) {
		URI uri= retrieveLocationURI(fileName,iX);
		return readImage(uri,iX);
	}
	public BufferedImage readImage(String textName, ChoisePoint iX) {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		URI uri= URL_Utils.create_URI(textName,staticContext,backslashIsSeparator);
		return readImage(uri,iX);
	}
	public BufferedImage readImage(URI uri, ChoisePoint iX) {
		int timeout= retrieveMaxWaitingTime(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		InputStream stream;
		try {
			byte[] array= URL_Utils.getContentOfResource(uri,null,timeout,staticContext,backslashIsSeparator);
			stream= new ByteArrayInputStream(array);
		} catch (CannotRetrieveContent e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		};
		try {
			BufferedImage image= ImageIO.read(stream);
			return image;
		} catch(IOException e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		}
	}
	//
	protected URI retrieveLocationURI(Term name, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			URI uri= URL_Utils.create_URI(textName,staticContext,backslashIsSeparator);
			return uri;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(name);
		}
	}
	protected int retrieveMaxWaitingTime(ChoisePoint iX) {
		Term interval= getBuiltInSlot_E_max_waiting_time();
		try {
			return URL_Utils.termToWaitingInterval(interval,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				return URL_Utils.termToWaitingInterval(DefaultOptions.waitingInterval,iX);
			} catch (TermIsSymbolDefault e2) {
				return defaultWaitingInterval;
			}
		}
	}
	// public boolean antialiasingIsEnabled(ChoisePoint iX) {
	//	Term antialiasingFlag= getBuiltInSlot_E_enable_scene_antialiasing();
	//	return Converters.term2YesNo(antialiasingFlag,iX);
	// }
}
