// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;
import morozov.system.gui.space2d.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.GraphicsConfiguration;

public abstract class BufferedImage extends ImageConsumer {
	//
	protected java.awt.image.BufferedImage bufferedImage= null;
	protected Color currentBackgroundColor= Color.WHITE;
	//
	protected static final Color defaultBackgroundColor= Color.WHITE;
	// protected static final int defaultWaitingInterval= -1;
	protected static final Term symbolDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	protected static final Term noValue= new PrologInteger(-1);
	//
	abstract protected Term getBuiltInSlot_E_width();
	abstract protected Term getBuiltInSlot_E_height();
	abstract protected Term getBuiltInSlot_E_image_type();
	abstract protected Term getBuiltInSlot_E_image_transparency();
	abstract protected Term getBuiltInSlot_E_background_color();
	// abstract protected Term getBuiltInSlot_E_max_waiting_time();
	// abstract protected Term getBuiltInSlot_E_backslash_always_is_separator();
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
		int width;
		int height;
		Rectangle defaultBounds= null;
		try {
			ExtendedSize eWidth= GUI_Utils.termToSize(a1,iX);
			width= (int)StrictMath.round(eWidth.getValue());
		} catch (UseDefaultSize e1) {
			try {
				ExtendedSize eWidth= GUI_Utils.termToSize(getBuiltInSlot_E_width(),iX);
				width= (int)StrictMath.round(eWidth.getValue());
			} catch (UseDefaultSize e2) {
				if (defaultBounds==null) {
					defaultBounds= DesktopUtils.getCurrentDeviceBounds(staticContext);
				};
				width= defaultBounds.width;
			}
		};
		try {
			ExtendedSize eHeight= GUI_Utils.termToSize(a2,iX);
			height= (int)StrictMath.round(eHeight.getValue());
		} catch (UseDefaultSize e1) {
			try {
				ExtendedSize eHeight= GUI_Utils.termToSize(getBuiltInSlot_E_height(),iX);
				height= (int)StrictMath.round(eHeight.getValue());
			} catch (UseDefaultSize e2) {
				if (defaultBounds==null) {
					defaultBounds= DesktopUtils.getCurrentDeviceBounds(staticContext);
				};
				height= defaultBounds.height;
			}
		};
		try {
			int imageType= Tools2D.termToBufferedImageType(a3,iX);
			bufferedImage= new java.awt.image.BufferedImage(width,height,imageType);
		} catch (TermIsSymbolDefault e1) {
			try {
				int imageType= Tools2D.termToBufferedImageType(getBuiltInSlot_E_image_type(),iX);
				bufferedImage= new java.awt.image.BufferedImage(width,height,imageType);
			} catch (TermIsSymbolDefault e2) {
				// GraphicsEnvironment env= GraphicsEnvironment.getLocalGraphicsEnvironment();
				// GraphicsDevice device= env.getDefaultScreenDevice();
				// GraphicsConfiguration gc= device.getDefaultConfiguration();
				GraphicsConfiguration gc= DesktopUtils.getGraphicsConfiguration(staticContext);
				try {
					int imageTransparency= Tools2D.termToImageTransparency(getBuiltInSlot_E_image_transparency(),iX);
					bufferedImage= gc.createCompatibleImage(width,height,imageTransparency);
				} catch (TermIsSymbolDefault e3) {
					bufferedImage= gc.createCompatibleImage(width,height);
				}
			}
		};
		return bufferedImage;
	}
	//
	public java.awt.image.BufferedImage getImage(ChoisePoint iX) {
		if (bufferedImage==null) {
			bufferedImage= createImage(symbolDefault,symbolDefault,symbolDefault,iX);
		};
		return bufferedImage;
	}
	//
	public void getSize2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (bufferedImage != null) {
			a1.value= new PrologInteger(bufferedImage.getWidth());
			a2.value= new PrologInteger(bufferedImage.getHeight());
		} else {
			a1.value= noValue;
			a2.value= noValue;
		}
	}
	//
	public void changeBackgroundColor1s(ChoisePoint iX, Term backgroundColor) {
		try {
			currentBackgroundColor= GUI_Utils.termToColor(backgroundColor,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				Term color= getBuiltInSlot_E_background_color();
				currentBackgroundColor= GUI_Utils.termToColor(color,iX);
			} catch (TermIsSymbolDefault e2) {
				currentBackgroundColor= defaultBackgroundColor;
			}
		}
	}
	//
	public void setPixel5s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5) {
		if (bufferedImage==null) {
			bufferedImage= createImage(symbolDefault,symbolDefault,symbolDefault,iX);
		};
		int x= PrologInteger.toInteger(Converters.argumentToRoundInteger(a1,iX));
		int y= PrologInteger.toInteger(Converters.argumentToRoundInteger(a2,iX));
		int red= PrologInteger.toInteger(Converters.argumentToRoundInteger(a3,iX));
		int green= PrologInteger.toInteger(Converters.argumentToRoundInteger(a4,iX));
		int blue= PrologInteger.toInteger(Converters.argumentToRoundInteger(a5,iX));
		Color color= new Color(red,green,blue);
		bufferedImage.setRGB(x,y,color.getRGB());
	}
	public void setPixel3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		if (bufferedImage==null) {
			bufferedImage= createImage(symbolDefault,symbolDefault,symbolDefault,iX);
		};
		int x= PrologInteger.toInteger(Converters.argumentToRoundInteger(a1,iX));
		int y= PrologInteger.toInteger(Converters.argumentToRoundInteger(a2,iX));
		int color= PrologInteger.toInteger(Converters.argumentToRoundInteger(a3,iX));
		bufferedImage.setRGB(x,y,color);
	}
	//
	public void save1s(ChoisePoint iX, Term a1) {
		String fileName= retrieveFileName(a1,iX);
		saveContent(fileName,null,iX);
	}
	public void save2s(ChoisePoint iX, Term a1, Term a2) {
		String fileName= retrieveFileName(a1,iX);
		// System.out.printf("(1) %s\n",fileName);
		saveContent(fileName,a2,iX);
	}
	protected void saveContent(String fileName, Term attributes, ChoisePoint iX) {
		if (bufferedImage==null) {
			bufferedImage= createImage(symbolDefault,symbolDefault,symbolDefault,iX);
		};
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		Tools2D.write(fileName,backslashIsSeparator,bufferedImage,attributes,iX);
	}
	//
	public void load1s(ChoisePoint iX, Term a1) {
		bufferedImage= readImage(a1,iX);
	}
	//
	protected String retrieveFileName(Term name, ChoisePoint iX) {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			String textName= name.getStringValue(iX);
			textName= FileUtils.replaceBackslashes(textName,backslashIsSeparator);
			return FileUtils.makeRealName(textName);
		} catch (TermIsNotAString e) {
			throw new WrongTermIsNotFileName(name);
		}
	}
}
