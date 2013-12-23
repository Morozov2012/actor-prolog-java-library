// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.checker.*;
import morozov.system.checker.errors.*;
import morozov.system.errors.*;
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
import java.net.URI;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.DirectoryNotEmptyException;

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
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	abstract protected Term getBuiltInSlot_E_name();
	abstract protected Term getBuiltInSlot_E_extension();
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
	public void setImage(java.awt.image.BufferedImage image) {
		bufferedImage= image;
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
	public void save0s(ChoisePoint iX) {
		String fileName= retrieveFileName(iX);
		saveContent(fileName,null,iX);
	}
	public void save1s(ChoisePoint iX, Term a1) {
		String fileName= retrieveFileName(a1,iX);
		saveContent(fileName,null,iX);
	}
	public void save2s(ChoisePoint iX, Term a1, Term a2) {
		String fileName= retrieveFileName(a1,iX);
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
	public void load0s(ChoisePoint iX) {
		URI uri= retrieveLocationURI(iX);
		bufferedImage= readImage(uri,iX);
	}
	public void load1s(ChoisePoint iX, Term a1) {
		bufferedImage= readImage(a1,iX);
	}
	//
	public void doesExist0s(ChoisePoint iX) throws Backtracking {
		try {
			URI uri= retrieveLocationURI(iX);
			doesExist(uri,iX);
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	public void doesExist1s(ChoisePoint iX, Term name) throws Backtracking {
		try {
			URI uri= retrieveLocationURI(name,iX);
			doesExist(uri,iX);
		} catch (Throwable e) {
			throw Backtracking.instance;
		}
	}
	protected void doesExist(URI uri, ChoisePoint iX) throws Backtracking {
		CharacterSet characterSet= new CharacterSet(CharacterSetType.DEFAULT);
		int timeout= retrieveMaxWaitingTime(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			if (!attributes.connectionWasSuccessful()) {
				throw Backtracking.instance;
			}
		} catch (Throwable e1) {
			throw Backtracking.instance;
		}
	}
	//
	public void isLocalResource0s(ChoisePoint iX) throws Backtracking {
		String fileName= retrieveFileName(iX);
		isLocalResource(fileName,iX);
	}
	public void isLocalResource1s(ChoisePoint iX, Term a1) throws Backtracking {
		String fileName= retrieveFileName(a1,iX);
		isLocalResource(fileName,iX);
	}
	protected void isLocalResource(String fileName, ChoisePoint iX) throws Backtracking {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		if (!URL_Utils.isLocalResource(fileName,backslashIsSeparator)) {
			throw Backtracking.instance;
		}
	}
	//
	public void getFullName0ff(ChoisePoint iX, PrologVariable a1) {
		String resolvedName= getFullName(iX);
		a1.value= new PrologString(resolvedName);
	}
	public void getFullName0fs(ChoisePoint iX) {
	}
	public void getFullName1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String resolvedName= getFullName(iX,a2);
		a1.value= new PrologString(resolvedName);
	}
	public void getFullName1fs(ChoisePoint iX, Term a1) {
	}
	protected String getFullName(ChoisePoint iX) {
		String location= retrieveLocationString(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		return URL_Utils.getFullName(location,staticContext,backslashIsSeparator);
	}
	protected String getFullName(ChoisePoint iX, Term a1) {
		try {
			String fileName= a1.getStringValue(iX);
			fileName= appendExtensionIfNecessary(fileName,iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			return URL_Utils.getFullName(fileName,staticContext,backslashIsSeparator);
		} catch (TermIsNotAString e1) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	//
	public void getURL0ff(ChoisePoint iX, PrologVariable a1) {
		String resolvedName= getFullName(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		a1.value= new PrologString(URL_Utils.get_URL_string(resolvedName,staticContext,backslashIsSeparator));
	}
	public void getURL0fs(ChoisePoint iX) {
	}
	public void getURL1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String resolvedName= getFullName(iX,a2);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		a1.value= new PrologString(URL_Utils.get_URL_string(resolvedName,staticContext,backslashIsSeparator));
	}
	public void getURL1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void delete0s(ChoisePoint iX) {
		String fileName= retrieveFileName(iX);
		deleteFile(fileName);
	}
	public void delete1s(ChoisePoint iX, Term name) {
		String fileName= retrieveFileName(name,iX);
		deleteFile(fileName);
	}
	protected void deleteFile(String fileName) {
		Path path= fileSystem.getPath(fileName);
		try {
			Files.deleteIfExists(path);
		} catch (DirectoryNotEmptyException e) {
		} catch (IOException e) {
		}
	}
	//
	protected String retrieveFileName(ChoisePoint iX) {
		Term name= getBuiltInSlot_E_name();
		try {
			String textName= name.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			textName= FileUtils.replaceBackslashes(textName,backslashIsSeparator);
			return FileUtils.makeRealName(textName);
		} catch (TermIsNotAString e) {
			throw new WrongTermIsNotFileName(name);
		}
	}
	protected String retrieveFileName(Term name, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			textName= FileUtils.replaceBackslashes(textName,backslashIsSeparator);
			return FileUtils.makeRealName(textName);
		} catch (TermIsNotAString e) {
			throw new WrongTermIsNotFileName(name);
		}
	}
	protected URI retrieveLocationURI(ChoisePoint iX) {
		Term name= getBuiltInSlot_E_name();
		return retrieveLocationURI(name,iX);
	}
	protected URI retrieveLocationURI(Term name, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
			URI uri= URL_Utils.create_URI(textName,staticContext,backslashIsSeparator);
			return uri;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(name);
		}
	}
	protected String retrieveLocationString(ChoisePoint iX) {
		Term location= getBuiltInSlot_E_name();
		try {
			String textName= location.getStringValue(iX);
			textName= appendExtensionIfNecessary(textName,iX);
			return textName;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(location);
		}
	}
	protected String appendExtensionIfNecessary(String textName, ChoisePoint iX) {
		if (textName.indexOf('.') == -1) {
			Term extension= getBuiltInSlot_E_extension();
			String textExtension= null;
			try {
				textExtension= extension.getStringValue(iX);
			} catch (TermIsNotAString e2) {
				throw new WrongArgumentIsNotAString(extension);
			};
			textName= textName + textExtension;
		};
		return textName;
	}
}
