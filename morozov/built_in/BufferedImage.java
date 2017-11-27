// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.files.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;
import morozov.system.gui.space2d.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.GraphicsConfiguration;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BufferedImage extends BufferedImageController {
	//
	// public ExtendedSize width= null;
	// public ExtendedSize height= null;
	public Integer imageType= null;
	public Integer imageTransparency= null;
	//
	protected AtomicReference<java.awt.image.BufferedImage> bufferedImage= new AtomicReference<>(null);
	//
	protected static final Term symbolDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	protected static final Term noValue= new PrologInteger(-1);
	//
	public BufferedImage() {
	}
	public BufferedImage(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	// abstract protected Term getBuiltInSlot_E_name();
	// abstract protected Term getBuiltInSlot_E_extension();
	// abstract protected Term getBuiltInSlot_E_width();
	// abstract protected Term getBuiltInSlot_E_height();
	abstract protected Term getBuiltInSlot_E_image_type();
	abstract protected Term getBuiltInSlot_E_image_transparency();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set imageType
	//
	public void setImageType1s(ChoisePoint iX, Term a1) {
		setImageType(Tools2D.argumentToBufferedImageType(a1,iX));
	}
	public void setImageType(int value) {
		imageType= value;
	}
	public void getImageType0ff(ChoisePoint iX, PrologVariable result) {
		int value= getImageType(iX);
		result.setNonBacktrackableValue(Tools2D.bufferedImageTypeToTerm(value));
	}
	public void getImageType0fs(ChoisePoint iX) {
	}
	public int getImageType(ChoisePoint iX) {
		if (imageType != null) {
			return imageType;
		} else {
			Term value= getBuiltInSlot_E_image_type();
			return Tools2D.argumentToBufferedImageType(value,iX);
		}
	}
	//
	// get/set imageTransparency
	//
	public void setImageTransparency1s(ChoisePoint iX, Term a1) {
		setImageTransparency(Tools2D.argumentToImageTransparency(a1,iX));
	}
	public void setImageTransparency(int value) {
		imageTransparency= value;
	}
	public void getImageTransparency0ff(ChoisePoint iX, PrologVariable result) {
		int value= getImageTransparency(iX);
		result.setNonBacktrackableValue(Tools2D.imageTransparencyToTerm(value));
	}
	public void getImageTransparency0fs(ChoisePoint iX) {
	}
	public int getImageTransparency(ChoisePoint iX) {
		if (imageTransparency != null) {
			return imageTransparency;
		} else {
			Term value= getBuiltInSlot_E_image_transparency();
			return Tools2D.argumentToImageTransparency(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void create0s(ChoisePoint iX) {
		createImage(symbolDefault,symbolDefault,symbolDefault,iX);
	}
	public void create2s(ChoisePoint iX, Term a1, Term a2) {
		createImage(a1,a2,symbolDefault,iX);
	}
	public void create3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		createImage(a1,a2,a3,iX);
	}
	protected java.awt.image.BufferedImage createImage(Term a1, Term a2, Term a3, ChoisePoint iX) {
		ExtendedSize eWidth= ExtendedSize.argumentToExtendedSize(a1,iX);
		ExtendedSize eHeight= ExtendedSize.argumentToExtendedSize(a2,iX);
		int imageType= Tools2D.argumentToBufferedImageType(a3,iX);
		return createImage(eWidth,eHeight,imageType,iX);
	}
	protected java.awt.image.BufferedImage createImage(ExtendedSize eWidth, ExtendedSize eHeight, int imageType, ChoisePoint iX) {
		GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
		// currentImageEncodingAttributes.set(attributes);
		setCurrentImageEncodingAttributes(attributes);
		int width= computeWidth(eWidth,iX);
		int height= computeHeight(eHeight,iX);
		if (imageType==java.awt.image.BufferedImage.TYPE_CUSTOM) {
			imageType= getImageType(iX);
		};
		if (imageType==java.awt.image.BufferedImage.TYPE_CUSTOM) {
			GraphicsConfiguration gc= DesktopUtils.getGraphicsConfiguration(staticContext);
			int imageTransparency= getImageTransparency(iX);
			if (imageTransparency==Tools2D.defaultTransparency) {
				bufferedImage.set(gc.createCompatibleImage(width,height));
			} else {
				bufferedImage.set(gc.createCompatibleImage(width,height,imageTransparency));
			}
		} else {
			bufferedImage.set(new java.awt.image.BufferedImage(width,height,imageType));
		};
		return bufferedImage.get();
	}
	protected int computeWidth(ExtendedSize eWidth, ChoisePoint iX) {
		int width;
		try {
			width= eWidth.getIntegerValue();
		} catch (UseDefaultSize e1) {
			try {
				eWidth= getWidth(iX);
				width= eWidth.getIntegerValue();
			} catch (UseDefaultSize e2) {
				Rectangle defaultBounds= DesktopUtils.getCurrentDeviceBounds(staticContext);
				width= defaultBounds.width;
			}
		};
		return width;
	}
	protected int computeHeight(ExtendedSize eHeight, ChoisePoint iX) {
		int height;
		try {
			height= eHeight.getIntegerValue();
		} catch (UseDefaultSize e1) {
			try {
				eHeight= getHeight(iX);
				height= eHeight.getIntegerValue();
			} catch (UseDefaultSize e2) {
				Rectangle defaultBounds= DesktopUtils.getCurrentDeviceBounds(staticContext);
				height= defaultBounds.height;
			}
		};
		return height;
	}
	//
	public java.awt.image.BufferedImage getImage() {
		// if (bufferedImage.get()==null) {
		//	bufferedImage.set(createImage(symbolDefault,symbolDefault,symbolDefault,iX));
		// };
		return bufferedImage.get();
	}
	public void setImage(java.awt.image.BufferedImage image, GenericImageEncodingAttributes attributes) {
		bufferedImage.set(image);
		if (currentImageEncodingAttributes.get() == null) {
			currentImageEncodingAttributes.set(attributes);
		}
	}
	//
	public void setActualSize2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedSize eWidth= ExtendedSize.argumentToExtendedSize(a1,iX);
		ExtendedSize eHeight= ExtendedSize.argumentToExtendedSize(a2,iX);
		java.awt.image.BufferedImage image= bufferedImage.get();
		if (image != null) {
			int givenWidth= computeWidth(eWidth,iX);
			int givenHeight= computeHeight(eHeight,iX);
			int currentWidth= image.getWidth();
			int currentHeight= image.getHeight();
			if (givenWidth==currentWidth && givenHeight==currentHeight) {
				return;
			} else {
				java.awt.image.BufferedImage newImage;
				int imageType= image.getType();
				if (imageType==java.awt.image.BufferedImage.TYPE_CUSTOM) {
					imageType= getImageType(iX);
				};
				if (imageType==java.awt.image.BufferedImage.TYPE_CUSTOM) {
					GraphicsConfiguration gc= DesktopUtils.getGraphicsConfiguration(staticContext);
					int imageTransparency= image.getTransparency();
					if (imageTransparency==Tools2D.defaultTransparency) {
						newImage= gc.createCompatibleImage(givenWidth,givenHeight);
					} else {
						newImage= gc.createCompatibleImage(givenWidth,givenHeight,imageTransparency);
					}
				} else {
					newImage= new java.awt.image.BufferedImage(givenWidth,givenHeight,imageType);
				};
				Graphics2D g2= newImage.createGraphics();
				try {
					g2.drawImage(image,0,0,givenWidth,givenHeight,null);
				} finally {
					g2.dispose();
				};
				bufferedImage.set(newImage);
			}
		} else {
			createImage(eWidth,eHeight,java.awt.image.BufferedImage.TYPE_CUSTOM,iX);
		}
	}
	public void getActualSize2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		java.awt.image.BufferedImage image= bufferedImage.get();
		if (image != null) {
			a1.setBacktrackableValue(new PrologInteger(image.getWidth()),iX);
			a2.setBacktrackableValue(new PrologInteger(image.getHeight()),iX);
		} else {
			a1.setBacktrackableValue(noValue,iX);
			a2.setBacktrackableValue(noValue,iX);
		}
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
	}
	public void getSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		java.awt.image.BufferedImage image= bufferedImage.get();
		if (image != null) {
			a1.setBacktrackableValue(new PrologInteger(image.getWidth()),iX);
			a2.setBacktrackableValue(new PrologInteger(image.getHeight()),iX);
		} else {
			a1.setBacktrackableValue(noValue,iX);
			a2.setBacktrackableValue(noValue,iX);
		}
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
	}
	//
	public void setPixel5s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5) {
		if (bufferedImage.get()==null) {
			bufferedImage.set(createImage(symbolDefault,symbolDefault,symbolDefault,iX));
		};
		int x= PrologInteger.toInteger(Converters.argumentToRoundInteger(a1,iX));
		int y= PrologInteger.toInteger(Converters.argumentToRoundInteger(a2,iX));
		int red= PrologInteger.toInteger(Converters.argumentToRoundInteger(a3,iX));
		int green= PrologInteger.toInteger(Converters.argumentToRoundInteger(a4,iX));
		int blue= PrologInteger.toInteger(Converters.argumentToRoundInteger(a5,iX));
		Color color= new Color(red,green,blue);
		bufferedImage.get().setRGB(x,y,color.getRGB());
	}
	public void setPixel3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		if (bufferedImage.get()==null) {
			bufferedImage.set(createImage(symbolDefault,symbolDefault,symbolDefault,iX));
		};
		int x= PrologInteger.toInteger(Converters.argumentToRoundInteger(a1,iX));
		int y= PrologInteger.toInteger(Converters.argumentToRoundInteger(a2,iX));
		int color= PrologInteger.toInteger(Converters.argumentToRoundInteger(a3,iX));
		bufferedImage.get().setRGB(x,y,color);
	}
	//
	public void save0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
		java.awt.image.BufferedImage nativeImage= bufferedImage.get();
		writeImage(fileName,nativeImage,attributes);
	}
	public void save1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
		java.awt.image.BufferedImage nativeImage= bufferedImage.get();
		writeImage(fileName,nativeImage,attributes);
	}
	public void save2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		GenericImageEncodingAttributes attributes= GenericImageEncodingAttributes.argumentToImageEncodingAttributes(a2,iX);
		java.awt.image.BufferedImage nativeImage= bufferedImage.get();
		writeImage(fileName,nativeImage,attributes);
	}
	//
	public void load0s(ChoisePoint iX) {
		bufferedImage.set(readImage(iX));
	}
	public void load1s(ChoisePoint iX, Term a1) {
		bufferedImage.set(readImage(a1,iX));
	}
	public void load5s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5) {
		int xSubsampling= Converters.argumentToSmallInteger(a2,iX);
		int ySubsampling= Converters.argumentToSmallInteger(a3,iX);
		int xOffset= Converters.argumentToSmallInteger(a4,iX);
		int yOffset= Converters.argumentToSmallInteger(a5,iX);
		bufferedImage.set(readImage(a1,xSubsampling,ySubsampling,xOffset,yOffset,iX));
	}
}
