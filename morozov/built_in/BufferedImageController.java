// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
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
	protected TextAttribute description;
	protected TextAttribute copyright;
	protected TextAttribute registrationDate;
	protected TextAttribute registrationTime;
	//
	protected static final Term termEmptyString= new PrologString("");
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
	abstract public Term getBuiltInSlot_E_image_encoding_attributes();
	//
	protected Term getBuiltInSlot_E_description() {
		return termEmptyString;
	}
	protected Term getBuiltInSlot_E_copyright() {
		return termEmptyString;
	}
	protected Term getBuiltInSlot_E_registration_date() {
		return termEmptyString;
	}
	protected Term getBuiltInSlot_E_registration_time() {
		return termEmptyString;
	}
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
		setImageEncodingAttributes(GenericImageEncodingAttributes.argumentToImageEncodingAttributes(a1,iX));
	}
	public void setImageEncodingAttributes(GenericImageEncodingAttributes value) {
		imageEncodingAttributes= value;
		setCurrentImageEncodingAttributes(imageEncodingAttributes);
	}
	public void getImageEncodingAttributes0ff(ChoisePoint iX, PrologVariable result) {
		GenericImageEncodingAttributes value= getImageEncodingAttributes(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getImageEncodingAttributes0fs(ChoisePoint iX) {
	}
	protected GenericImageEncodingAttributes getImageEncodingAttributes(ChoisePoint iX) {
		if (imageEncodingAttributes != null) {
			return imageEncodingAttributes;
		} else {
			Term value= getBuiltInSlot_E_image_encoding_attributes();
			return GenericImageEncodingAttributes.argumentToImageEncodingAttributes(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set description
	//
	public void setDescription1s(ChoisePoint iX, Term a1) {
		setDescription(TextAttribute.argumentToTextAttribute(a1,iX));
	}
	public void setDescription(TextAttribute value) {
		description= value;
	}
	public void getDescription0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getDescription(iX).toTerm());
	}
	public void getDescription0fs(ChoisePoint iX) {
	}
	public TextAttribute getDescription(ChoisePoint iX) {
		if (description != null) {
			return description;
		} else {
			Term value= getBuiltInSlot_E_description();
			return TextAttribute.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set copyright
	//
	public void setCopyright1s(ChoisePoint iX, Term a1) {
		setCopyright(TextAttribute.argumentToTextAttribute(a1,iX));
	}
	public void setCopyright(TextAttribute value) {
		copyright= value;
	}
	public void getCopyright0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getCopyright(iX).toTerm());
	}
	public void getCopyright0fs(ChoisePoint iX) {
	}
	public TextAttribute getCopyright(ChoisePoint iX) {
		if (copyright != null) {
			return copyright;
		} else {
			Term value= getBuiltInSlot_E_copyright();
			return TextAttribute.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set registration_date
	//
	public void setRegistrationDate1s(ChoisePoint iX, Term a1) {
		setRegistrationDate(TextAttribute.argumentToTextAttribute(a1,iX));
	}
	public void setRegistrationDate(TextAttribute value) {
		registrationDate= value;
	}
	public void getRegistrationDate0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getRegistrationDate(iX).toTerm());
	}
	public void getRegistrationDate0fs(ChoisePoint iX) {
	}
	public TextAttribute getRegistrationDate(ChoisePoint iX) {
		if (registrationDate != null) {
			return registrationDate;
		} else {
			Term value= getBuiltInSlot_E_registration_date();
			return TextAttribute.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set registration_time
	//
	public void setRegistrationTime1s(ChoisePoint iX, Term a1) {
		setRegistrationTime(TextAttribute.argumentToTextAttribute(a1,iX));
	}
	public void setRegistrationTime(TextAttribute value) {
		registrationTime= value;
	}
	public void getRegistrationTime0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getRegistrationTime(iX).toTerm());
	}
	public void getRegistrationTime0fs(ChoisePoint iX) {
	}
	public TextAttribute getRegistrationTime(ChoisePoint iX) {
		if (registrationTime != null) {
			return registrationTime;
		} else {
			Term value= getBuiltInSlot_E_registration_time();
			return TextAttribute.argumentToTextAttribute(value,iX);
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
			boolean isStandardFile= fileName.isStandardFile();
			if (!isStandardFile) {
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
				if (!isStandardFile) {
					outputStream.flush();
					outputStream.close();
				}
			}
			// }
		} catch (IOException e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected byte[] convertImageToBytes(java.awt.image.BufferedImage nativeImage) {
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
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
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void checkMatrix(double[][] matrix, Term matrixValue) {
		int numberOfRows= matrix.length;
		if (numberOfRows==0) {
		} else if (numberOfRows==3) {
			int numberOfColumns= matrix[0].length;
			if (numberOfColumns != 3) {
				throw new WrongArgumentIsNotAMatrix(matrixValue);
			}
		} else {
			throw new WrongArgumentIsNotAMatrix(matrixValue);
		}
	}
}
