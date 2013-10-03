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
//System.out.printf("[1] ImageConsumer::fileName: %s\n",fileName);
		URI uri= retrieveLocationURI(fileName,iX);
//System.out.printf("[2] ImageConsumer::fileName: %s\n",uri);
		return readImage(uri,iX);
	}
	public BufferedImage readImage(String textName, ChoisePoint iX) {
//System.out.printf("[1.0.A] ImageConsumer::fileName: %s\n",textName);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		URI uri= URL_Utils.create_URI(textName,staticContext,backslashIsSeparator);
		return readImage(uri,iX);
	}
	public BufferedImage readImage(URI uri, ChoisePoint iX) {
//System.out.printf("[1.0.B] ImageConsumer::fileName: %s\n",uri);
		// URI uri= retrieveLocationURI(fileName,iX);
		int timeout= retrieveMaxWaitingTime(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		InputStream stream;
		try {
//System.out.printf("[1.1] ImageConsumer::fileName: %s\n",uri);
			byte[] array= URL_Utils.getContentOfResource(uri,null,timeout,staticContext,backslashIsSeparator);
//System.out.printf("[3.1] ImageConsumer::array: %s\n",array.length);
			stream= new ByteArrayInputStream(array);
//System.out.printf("[3.2] ImageConsumer::stream= %s\n",stream);
		} catch (CannotRetrieveContent e1) {
//System.out.printf("[3.3] ImageConsumer::e1=%s\n",e1);
			throw new FileInputOutputError(uri.toString(),e1);
		};
		try {
//System.out.printf("[5.1] ImageConsumer::stream: %s\n",stream);
			BufferedImage image= ImageIO.read(stream);
//System.out.printf("[5.2] ImageConsumer::image: %s\n",image);
			return image;
		} catch(IOException e1) {
//System.out.printf("[5.3] ImageConsumer::e1: %s\n",e1);
			throw new FileInputOutputError(uri.toString(),e1);
		}
	}
	protected URI retrieveLocationURI(Term name, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			// textName= appendExtensionIfNecessary(textName,iX);
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
