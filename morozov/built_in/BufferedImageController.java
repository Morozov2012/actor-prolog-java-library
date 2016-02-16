// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.gui.space2d.*;
import morozov.system.gui.space2d.errors.*;
import morozov.terms.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BufferedImageController extends DataResourceConsumer {
	//
	public GenericImageEncodingAttributes imageEncodingAttributes = null;
	protected AtomicReference<GenericImageEncodingAttributes> currentImageEncodingAttributes= new AtomicReference<>(GenericImageEncodingAttributes.instance);
	//
	///////////////////////////////////////////////////////////////
	//
	public BufferedImageController() {
	}
	public BufferedImageController(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract protected Term getBuiltInSlot_E_image_encoding_attributes();
	//
	///////////////////////////////////////////////////////////////
	//
	public void setCurrentImageEncodingAttributes(GenericImageEncodingAttributes attributes) {
		currentImageEncodingAttributes.set(attributes);
	}
	public GenericImageEncodingAttributes getCurrentImageEncodingAttributes() {
		return currentImageEncodingAttributes.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set imageEncodingAttributes
	//
	public void setImageEncodingAttributes1s(ChoisePoint iX, Term a1) {
		setImageEncodingAttributes(GenericImageEncodingAttributes.termToImageEncodingAttributes(a1,iX));
	}
	public void setImageEncodingAttributes(GenericImageEncodingAttributes value) {
		imageEncodingAttributes= value;
		setCurrentImageEncodingAttributes(imageEncodingAttributes);
	}
	public void getImageEncodingAttributes0ff(ChoisePoint iX, PrologVariable a1) {
		GenericImageEncodingAttributes value= getImageEncodingAttributes(iX);
		a1.value= value.toTerm();
	}
	public void getImageEncodingAttributes0fs(ChoisePoint iX) {
	}
	protected GenericImageEncodingAttributes getImageEncodingAttributes(ChoisePoint iX) {
		if (imageEncodingAttributes != null) {
			return imageEncodingAttributes;
		} else {
			Term value= getBuiltInSlot_E_image_encoding_attributes();
			return GenericImageEncodingAttributes.termToImageEncodingAttributes(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void modifyImage(Term value, java.awt.image.BufferedImage nativeImage, ChoisePoint iX) {
		if (nativeImage != null) {
			value= value.dereferenceValue(iX);
			if (value instanceof BufferedImage) {
				GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
				BufferedImage image= (BufferedImage)value;
				image.setImage(nativeImage,attributes);
			} else if (value instanceof ForeignWorldWrapper) {
				GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
				ForeignWorldWrapper wrapper= (ForeignWorldWrapper)value;
				wrapper.setImage(nativeImage,attributes);
			} else {
				throw new WrongArgumentIsNotBufferedImage(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeImage(ExtendedFileName fileName, java.awt.image.BufferedImage nativeImage, GenericImageEncodingAttributes attributes) {
		String formatName= fileName.extractGraphicsFormatName();
		Space2DWriter writer= Space2DWriter.createSpace2DWriter(formatName,nativeImage,attributes);
		try {
			writeImage(fileName,nativeImage,writer);
		} finally {
			writer.dispose();
		}
	}
	public static void writeImage(ExtendedFileName fileName, java.awt.image.BufferedImage nativeImage, Space2DWriter writer) {
		try {
			OutputStream outputStream;
			if (!fileName.isStandardFile()) {
				Path path= fileName.getPathOfLocalResource();
				outputStream= Files.newOutputStream(path);
			} else {
				StandardFileName systemName= fileName.getSystemName();
				if (systemName==StandardFileName.STDIN) {
					throw new StandardInputStreamDoesNotSupportThisOperation();
				} else if (systemName==StandardFileName.STDOUT) {
					outputStream= new BufferedOutputStream(System.out);
				} else {
					outputStream= new BufferedOutputStream(System.err);
				}
			};
			// if (writer==null) {
			//	ImageIO.setUseCache(false);
			//	ImageIO.write(nativeImage,formatName,outputStream);
			// } else {
			ImageOutputStream output= new MemoryCacheImageOutputStream(outputStream);
			try {
				writer.setOutput(output);
				writer.write(nativeImage);
			} finally {
				output.flush();
				output.close();
				// writer.dispose();
			}
			// }
		} catch (IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
}
