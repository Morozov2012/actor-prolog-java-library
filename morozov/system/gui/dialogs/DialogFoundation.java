// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Color;
import java.awt.GraphicsConfiguration;

import java.math.BigInteger;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DialogFoundation {
	//
	protected Dialog targetWorld= null;
	//
	protected DialogOperations dialogContainer;
	//
	private AtomicBoolean dialogIsProven= new AtomicBoolean(false);
	private AtomicBoolean dialogIsSuspended= new AtomicBoolean(false);
	//
	private AtomicBoolean isDisposed= new AtomicBoolean(false);
	//
	protected BigInteger insideModalDialog= BigInteger.ZERO;
	//
	protected AtomicReference<ExtendedCoordinates> actualCoordinates= new AtomicReference<ExtendedCoordinates>(new ExtendedCoordinates(new ExtendedCoordinate(),new ExtendedCoordinate()));
	protected AtomicReference<Point> previousCoordinates= new AtomicReference<Point>(null);
	//
	protected AtomicReference<Dimension> previousActualSize= new AtomicReference<Dimension>(new Dimension(0,0));
	//
	protected AtomicReference<Color> currentSuccessBackgroundColor= new AtomicReference<Color>();
	protected AtomicReference<Color> currentFailureForegroundColor= new AtomicReference<Color>();
	protected AtomicReference<Color> currentFailureBackgroundColor= new AtomicReference<Color>();
	//
	private AtomicReference<String> currentFontName= new AtomicReference<String>(defaultDialogFontName);
	private AtomicInteger currentFontSize= new AtomicInteger(defaultDialogFontSize);
	private AtomicInteger currentFontStyle= new AtomicInteger(defaultDialogFontStyle);
	private AtomicBoolean currentFontUnderline= new AtomicBoolean(defaultDialogFontUnderline);
	//
	protected Map<Integer,Font> approvedFonts= Collections.synchronizedMap(new HashMap<Integer,Font>());
	//
	protected ScalablePanel mainPanel;
	protected GridBagLayout mainPanelLayout;
	//
	///////////////////////////////////////////////////////////////
	//
	protected static final Color defaultDialogTextColor= null;
	protected static final Color defaultDialogSpaceColor= null;
	protected static final String defaultDialogFontName= Font.MONOSPACED;
	protected static final int defaultDialogFontSize= 16;
	protected static final int defaultDialogFontStyle= Font.PLAIN;
	protected static final boolean defaultDialogFontUnderline= false;
	//
	protected static final Color defaultDialogSuccessBackgroundColor= null;
	protected static final Color defaultDialogFailureForegroundColor= null;
	protected static final Color defaultDialogFailureBackgroundColor= Color.RED;
	//
	protected static final ExtendedCoordinate defaultDialogX= new ExtendedCoordinate();
	protected static final ExtendedCoordinate defaultDialogY= new ExtendedCoordinate();
	//
	protected static final Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	protected abstract void setGeneralFont(Font commonFont);
	// protected abstract void setGeneralBackground(Color c);
	protected abstract void setGeneralForeground(Color c);
	protected abstract void setGeneralSpaceColor(Color c);
	protected abstract void setAlarmColors(Color fc, Color bc);
	//
	abstract public void assemble(ChoisePoint iX);
	//
	protected void defineDefaultButton() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected String getPredefinedTitle() {
		return "";
	}
	protected Term getPredefinedTextColor() {
		return termDefault;
	}
	protected Term getPredefinedSpaceColor() {
		return termDefault;
	}
	protected Term getPredefinedBackgroundColor() {
		return termDefault;
	}
	protected Term getPredefinedFontName() {
		return termDefault;
	}
	protected Term getPredefinedFontSize() {
		return termDefault;
	}
	protected Term getPredefinedFontStyle() {
		return termDefault;
	}
	protected Term getPredefinedX() {
		return termDefault;
	}
	protected Term getPredefinedY() {
		return termDefault;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void prepare(
			Dialog world,
			ExtendedTitle title,
			ExtendedColor textColor,
			ExtendedColor spaceColor,
			ExtendedFontName fontName,
			ExtendedFontSize fontSize,
			ExtendedFontStyle fontStyle,
			ExtendedCoordinate x,
			ExtendedCoordinate y,
			ExtendedColor backgroundColor,
			ChoisePoint iX) {
		targetWorld= world;
		String initialTitle= instantiateTitle(title);
		Color initialTextColor= instantiateTextColor(textColor,iX);
		Color initialSpaceColor= instantiateSpaceColor(spaceColor,iX);
		Color initialSuccessBackgroundColor= instantiateSuccessBackgroundColor(backgroundColor,iX);
		Color refinedBackgroundColor= refineBackgroundColor(initialSuccessBackgroundColor);
		Color initialFailureForegroundColor= instantiateFailureForegroundColor(iX);
		Color initialFailureBackgroundColor= instantiateFailureBackgroundColor(iX);
		String initialFontName= instantiateFontName(fontName,iX);
		int initialFontSize= instantiateFontSize(fontSize,iX);
		FontStyleAndUnderlineMode initialFontStyle= instantiateFontStyle(fontStyle,iX);
		x= instantiateX(x,iX);
		y= instantiateY(y,iX);
		actualCoordinates.set(new ExtendedCoordinates(x,y));
		if (initialFontName!=null) {
			currentFontName.set(initialFontName);
		};
		currentFontSize.set(initialFontSize);
		currentFontStyle.set(initialFontStyle.style);
		currentFontUnderline.set(initialFontStyle.isUnderlined);
		currentSuccessBackgroundColor.set(initialSuccessBackgroundColor);
		currentFailureForegroundColor.set(initialFailureForegroundColor);
		currentFailureBackgroundColor.set(initialFailureBackgroundColor);
		safelyAssembleAndAdd(initialTitle,initialFailureForegroundColor,initialFailureBackgroundColor,refinedBackgroundColor,iX);
	}
	//
	public Dialog getTargetWorld() {
		return targetWorld;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected String instantiateTitle(ExtendedTitle title) {
		String value;
		try {
			value= title.getValue();
		} catch (UseDefaultTitle e) {
			value= getPredefinedTitle();
		};
		return value;
	}
	public void changeTitle(ExtendedTitle title) {
		String text= instantiateTitle(title);
		safelySetTitle(text);
	}
	public void safelySetTitle(final String title) {
		if (SwingUtilities.isEventDispatchThread()) {
			dialogContainer.setTitle(title);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						dialogContainer.setTitle(title);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public String safelyGetTitle() {
		if (SwingUtilities.isEventDispatchThread()) {
			return dialogContainer.getTitle();
		} else {
			try {
				final AtomicReference<String> value= new AtomicReference<String>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(dialogContainer.getTitle());
					}
				});
				return value.get();
			} catch (InterruptedException e) {
				return "";
			} catch (InvocationTargetException e) {
				return "";
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Color instantiateTextColor(ExtendedColor textColor, ChoisePoint iX) {
		Color value;
		try {
			value= textColor.getValue();
		} catch (UseDefaultColor e1) {
			try {
				value= ExtendedColor.argumentToColorSafe(getPredefinedTextColor(),iX);
			} catch (TermIsSymbolDefault e2) {
				value= defaultDialogTextColor;
			}
		};
		return value;
	}
	public void changeTextColor(ExtendedColor textColor, boolean repaintContainer, ChoisePoint iX) {
		Color value= instantiateTextColor(textColor,iX);
		safelySetTextColor(value,repaintContainer);
	}
	public void safelySetTextColor(final Color color, final boolean repaintContainer) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklySetTextColor(color,repaintContainer);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklySetTextColor(color,repaintContainer);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklySetTextColor(Color color, boolean repaintContainer) {
		setGeneralForeground(color);
		if (repaintContainer) {
			dialogContainer.repaint();
		}
	}
	public Color safelyGetForegroundColor() {
		if (SwingUtilities.isEventDispatchThread()) {
			return dialogContainer.getForeground();
		} else {
			try {
				final AtomicReference<Color> value= new AtomicReference<Color>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(dialogContainer.getForeground());
					}
				});
				return value.get();
			} catch (InterruptedException e) {
				return null;
			} catch (InvocationTargetException e) {
				return null;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Color instantiateSpaceColor(ExtendedColor spaceColor, ChoisePoint iX) {
		Color value;
		try {
			value= spaceColor.getValue();
		} catch (UseDefaultColor e1) {
			try {
				value= ExtendedColor.argumentToColorSafe(getPredefinedSpaceColor(),iX);
			} catch (TermIsSymbolDefault e2) {
				value= defaultDialogSpaceColor;
			}
		};
		return value;
	}
	public void changeSpaceColor(ExtendedColor spaceColor, boolean repaintContainer, ChoisePoint iX) {
		Color value= instantiateSpaceColor(spaceColor,iX);
		safelySetSpaceColor(value,repaintContainer);
	}
	public void safelySetSpaceColor(final Color color, final boolean repaintContainer) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklySetSpaceColor(color,repaintContainer);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklySetSpaceColor(color,repaintContainer);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklySetSpaceColor(Color color, boolean repaintContainer) {
		setGeneralSpaceColor(color);
		if (repaintContainer) {
			dialogContainer.repaint();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Color instantiateSuccessBackgroundColor(ExtendedColor backgroundColor, ChoisePoint iX) {
		Color value;
		try {
			value= backgroundColor.getValue();
		} catch (UseDefaultColor e1) {
			try {
				value= ExtendedColor.argumentToColorSafe(getPredefinedBackgroundColor(),iX);
			} catch (TermIsSymbolDefault e2) {
				value= defaultDialogSuccessBackgroundColor;
			}
		};
		return value;
	}
	public void changeBackgroundColor(ExtendedColor backgroundColor, boolean repaintContainer, ChoisePoint iX) {
		Color successColor= instantiateSuccessBackgroundColor(backgroundColor,iX);
		currentSuccessBackgroundColor.set(successColor);
		Color refinedColor= refineBackgroundColor(successColor);
		safelySetBackgroundColor(refinedColor,repaintContainer);
	}
	public void safelySetBackgroundColor(final Color color, final boolean repaintContainer) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklySetBackgroundColor(color,repaintContainer);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklySetBackgroundColor(color,repaintContainer);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklySetBackgroundColor(Color color, boolean repaintContainer) {
		setGeneralBackground(color);
		if (repaintContainer) {
			dialogContainer.repaint();
		}
	}
	protected void setGeneralBackground(Color c) { // To be called from setGeneralBackground.
		dialogContainer.setBackground(c);
	}
	public Color safelyGetBackgroundColor() {
		if (SwingUtilities.isEventDispatchThread()) {
			return dialogContainer.getBackground();
		} else {
			try {
				final AtomicReference<Color> value= new AtomicReference<Color>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(dialogContainer.getBackground());
					}
				});
				return value.get();
			} catch (InterruptedException e) {
				return null;
			} catch (InvocationTargetException e) {
				return null;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Color instantiateFailureForegroundColor(ChoisePoint iX) {
		Color value;
		try {
			value= ExtendedColor.argumentToColorSafe(DefaultOptions.failureDrawingForegroundColor,iX);
		} catch (TermIsSymbolDefault e1) {
			value= defaultDialogFailureForegroundColor;
		};
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Color instantiateFailureBackgroundColor(ChoisePoint iX) {
		Color value;
		try {
			value= ExtendedColor.argumentToColorSafe(DefaultOptions.failureDrawingBackgroundColor,iX);
		} catch (TermIsSymbolDefault e1) {
			value= defaultDialogFailureBackgroundColor;
		};
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected String instantiateFontName(ExtendedFontName fontName, ChoisePoint iX) {
		String value;
		try {
			value= fontName.getValue();
		} catch (UseDefaultFontName e1) {
			try {
				value= ExtendedFontName.argumentToFontName(getPredefinedFontName(),iX);
			} catch (TermIsSymbolDefault e2) {
				try {
					value= ExtendedFontName.argumentToFontName(DefaultOptions.dialogFontName,iX);
				} catch (TermIsSymbolDefault e3) {
					value= defaultDialogFontName;
				}
			}
		};
		return value;
	}
	public void changeFontName(ExtendedFontName fontName, ChoisePoint iX) {
		String value= instantiateFontName(fontName,iX);
		setCurrentFontName(value);
		safelyCreateNewFontAndResize();
	}
	public void setCurrentFontName(String name) {
		currentFontName.set(name);
		resetFontHash();
	}
	public String getCurrentFontName() {
		return currentFontName.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected int instantiateFontSize(ExtendedFontSize fontSize, ChoisePoint iX) {
		int value;
		try {
			value= fontSize.getValue();
		} catch (UseDefaultFontSize e1) {
			try {
				value= ExtendedFontSize.argumentToFontSize(getPredefinedFontSize(),iX);
			} catch (TermIsSymbolDefault e2) {
				try {
					value= ExtendedFontSize.argumentToFontSize(DefaultOptions.dialogFontSize,iX);
				} catch (TermIsSymbolDefault e3) {
					value= defaultDialogFontSize;
				}
			}
		};
		if (value < 1) {
			value= 1;
		};
		return value;
	}
	public void changeFontSize(ExtendedFontSize fontSize, ChoisePoint iX) {
		int value= instantiateFontSize(fontSize,iX);
		setCurrentFontSize(value);
		safelyCreateNewFontAndResize();
	}
	public void setCurrentFontSize(int size) {
		currentFontSize.set(size);
	}
	public int getCurrentFontSize() {
		return currentFontSize.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected FontStyleAndUnderlineMode instantiateFontStyle(ExtendedFontStyle fontStyle, ChoisePoint iX) {
		int style;
		boolean isUnderlined;
		try {
			style= fontStyle.getValue();
			isUnderlined= fontStyle.isUnderlined();
		} catch (UseDefaultFontStyle e1) {
			Term predefinedFontStyle= getPredefinedFontStyle();
			try {
				style= ExtendedFontStyle.argumentToFontStyleSafe(predefinedFontStyle,iX);
				isUnderlined= ExtendedFontStyle.fontIsUnderlinedSafe(predefinedFontStyle,iX);
			} catch (TermIsSymbolDefault e2) {
				Term dialogFontStyle= DefaultOptions.dialogFontStyle;
				try {
					style= ExtendedFontStyle.argumentToFontStyleSafe(dialogFontStyle,iX);
					isUnderlined= ExtendedFontStyle.fontIsUnderlinedSafe(dialogFontStyle,iX);
				} catch (TermIsSymbolDefault e3) {
					style= defaultDialogFontStyle;
					isUnderlined= defaultDialogFontUnderline;
				}
			}
		};
		return new FontStyleAndUnderlineMode(style,isUnderlined);
	}
	public void changeFontStyle(ExtendedFontStyle fontStyle, ChoisePoint iX) {
		FontStyleAndUnderlineMode value= instantiateFontStyle(fontStyle,iX);
		setCurrentFontStyle(value.style);
		setCurrentFontUnderline(value.isUnderlined);
		safelyCreateNewFontAndResize();
	}
	public void setCurrentFontStyle(int style) {
		currentFontStyle.set(style);
		resetFontHash();
	}
	public int getCurrentFontStyle() {
		return currentFontStyle.get();
	}
	public void setCurrentFontUnderline(boolean flag) {
		currentFontUnderline.set(flag);
		resetFontHash();
	}
	public boolean getCurrentFontUnderline() {
		return currentFontUnderline.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected ExtendedCoordinate instantiateX(ExtendedCoordinate x, ChoisePoint iX) {
		if (x.isDefault()) {
			x= ExtendedCoordinate.argumentToExtendedCoordinateSafe(getPredefinedX(),iX);
			if (x.isDefault()) {
				x= defaultDialogX;
			}
		};
		return x;
	}
	protected ExtendedCoordinate instantiateY(ExtendedCoordinate y, ChoisePoint iX) {
		if (y.isDefault()) {
			y= ExtendedCoordinate.argumentToExtendedCoordinateSafe(getPredefinedY(),iX);
			if (y.isDefault()) {
				y= defaultDialogY;
			}
		};
		return y;
	}
	public void changeActualX(ExtendedCoordinate actualX, ChoisePoint iX) {
		actualX= instantiateX(actualX,iX);
		ExtendedCoordinates currentCoordinates= actualCoordinates.get();
		actualCoordinates.set(new ExtendedCoordinates(actualX,currentCoordinates.y));
		try {
			Rectangle bounds= safelyComputeParentLayoutSize();
			double gridX= DefaultOptions.gridWidth;
			Point p= safelyGetLocation();
			Dimension size= safelyGetSize();
			p.x= DialogUtils.calculateRealCoordinate(actualX,bounds.x,bounds.width,gridX,size.getWidth());
			safelySetLocation(p);
		} catch (UseDefaultLocation e) {
		}
	}
	public void changeActualY(ExtendedCoordinate actualY, ChoisePoint iX) {
		actualY= instantiateY(actualY,iX);
		ExtendedCoordinates currentCoordinates= actualCoordinates.get();
		actualCoordinates.set(new ExtendedCoordinates(currentCoordinates.x,actualY));
		try {
			Rectangle bounds= safelyComputeParentLayoutSize();
			double gridY= DefaultOptions.gridHeight;
			Point p= safelyGetLocation();
			Dimension size= safelyGetSize();
			p.y= DialogUtils.calculateRealCoordinate(actualY,bounds.y,bounds.height,gridY,size.getHeight());
			safelySetLocation(p);
		} catch (UseDefaultLocation e) {
		}
	}
	public void changeActualPosition(ExtendedCoordinate actualX, ExtendedCoordinate actualY, ChoisePoint iX) {
		actualX= instantiateX(actualX,iX);
		actualY= instantiateY(actualY,iX);
		actualCoordinates.set(new ExtendedCoordinates(actualX,actualY));
		try {
			Rectangle bounds= safelyComputeParentLayoutSize();
			double gridX= DefaultOptions.gridWidth;
			double gridY= DefaultOptions.gridHeight;
			Point p= safelyGetLocation();
			Dimension size= safelyGetSize();
			p.x= DialogUtils.calculateRealCoordinate(actualX,bounds.x,bounds.width,gridX,size.getWidth());
			p.y= DialogUtils.calculateRealCoordinate(actualY,bounds.y,bounds.height,gridY,size.getHeight());
			safelySetLocation(p);
		} catch (UseDefaultLocation e) {
		}
	}
	public double getActualX() {
		Rectangle bounds= safelyComputeParentLayoutSize();
		double gridX= DefaultOptions.gridWidth;
		Point p= safelyGetLocation();
		return (double)p.x/(((double)(bounds.width-bounds.x))/gridX);
	}
	public double getActualY() {
		Rectangle bounds= safelyComputeParentLayoutSize();
		double gridY= DefaultOptions.gridHeight;
		Point p= safelyGetLocation();
		return (double)p.y/(((double)(bounds.height-bounds.y))/gridY);
	}
	public ExtendedCoordinates getActualPosition() {
		Rectangle bounds= safelyComputeParentLayoutSize();
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		Point p= safelyGetLocation();
		double x= (double)p.x/(((double)(bounds.width-bounds.x))/gridX);
		double y= (double)p.y/(((double)(bounds.height-bounds.y))/gridY);
		return new ExtendedCoordinates(new ExtendedCoordinate(x),new ExtendedCoordinate(y));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getDepthCounter() {
		synchronized(insideModalDialog) {
			return insideModalDialog;
		}
	}
	public void increaseDepthCounter() {
		synchronized(insideModalDialog) {
			BigInteger n= insideModalDialog;
			insideModalDialog= n.add(BigInteger.ONE);
		}
	}
	public void decreaseDepthCounter() {
		synchronized(insideModalDialog) {
			BigInteger n= insideModalDialog;
			insideModalDialog= n.subtract(BigInteger.ONE);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void rememberStateOfProcess(String identifier, boolean isProven, boolean isSuspended) {
		synchronized(dialogIsProven) {
			if (dialogIsProven.get()==isProven && dialogIsSuspended.get()==isSuspended) {
				return;
			};
			dialogIsProven.set(isProven);
			dialogIsSuspended.set(isSuspended);
			Color refinedBackgroundColor= refineBackgroundColor(currentSuccessBackgroundColor.get());
			safelySetBackgroundColor(refinedBackgroundColor,false);
			mainPanel.setHatchColor(hatchColor());
		};
		safelyRepaint();
	}
	//
	protected Color refineBackgroundColor(Color initialColor) {
		BigInteger modalDialogCounter= getDepthCounter();
		if (modalDialogCounter.compareTo(BigInteger.ZERO) <= 0) {
			synchronized(dialogIsProven) {
				if (!dialogIsSuspended.get() && dialogIsProven.get()) {
					if (initialColor==null) {
						return UIManager.getColor("control");
					} else {
						return initialColor;
					}
				} else {
					return currentFailureBackgroundColor.get();
				}
			}
		} else {
			if (initialColor==null) {
				return UIManager.getColor("control");
			} else {
				return initialColor;
			}
		}
	}
	//
	protected Color hatchColor() {
		BigInteger modalDialogCounter= getDepthCounter();
		if (modalDialogCounter.compareTo(BigInteger.ZERO) <= 0) {
			synchronized(dialogIsProven) {
				if (!dialogIsSuspended.get() && dialogIsProven.get()) {
					return null;
				} else {
					return currentFailureForegroundColor.get();
				}
			}
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Font create_new_font() {
		return create_new_font(currentFontName.get(),currentFontStyle.get(),currentFontUnderline.get(),currentFontSize.get());
	}
	protected Font create_new_font(String fontName, int fontStyle, boolean fontUnderline, int fontSize) {
		Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
		map.put(TextAttribute.FAMILY,fontName);
		boolean isBold= (fontStyle & Font.BOLD) != 0;
		boolean isItalic= (fontStyle & Font.ITALIC) != 0;
		if (isBold) {
			map.put(TextAttribute.WEIGHT,TextAttribute.WEIGHT_BOLD);
		} else {
			map.put(TextAttribute.WEIGHT,TextAttribute.WEIGHT_REGULAR);
		};
		if (isItalic) {
			map.put(TextAttribute.POSTURE,TextAttribute.POSTURE_OBLIQUE);
		} else {
			map.put(TextAttribute.POSTURE,TextAttribute.POSTURE_REGULAR);
		};
		if (fontUnderline) {
			map.put(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON);
		};
		int realFontSize= DefaultOptions.fontSystemSimulationMode.simulate(fontSize);
		map.put(TextAttribute.SIZE,realFontSize);
		return new Font(map);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetFontHash() {
		approvedFonts.clear();
		previousActualSize.set(new Dimension(-1,-1));
	}
	//
	protected void safelyCreateNewFontAndResize() {
		Font newFont= create_new_font();
		safelySetGeneralFont(newFont);
		implementPreferredSize();
		safelyValidate();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void safelyAssembleAndAdd(final String initialTitle, final Color initialFailureForegroundColor, final Color initialFailureBackgroundColor, final Color refinedBackgroundColor, final ChoisePoint iX) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyAssembleAndAdd(initialTitle,initialFailureForegroundColor,initialFailureBackgroundColor,refinedBackgroundColor,iX);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyAssembleAndAdd(initialTitle,initialFailureForegroundColor,initialFailureBackgroundColor,refinedBackgroundColor,iX);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
				throw new DialogCreationError(e.getCause());
			}
		}
	}
	private void quicklyAssembleAndAdd(String initialTitle, Color initialFailureForegroundColor, Color initialFailureBackgroundColor, Color refinedBackgroundColor, ChoisePoint iX) {
		assemble(iX);
		mainPanel.setTransparency(false);
		mainPanel.setHatchColor(hatchColor());
		dialogContainer.setTitle(initialTitle);
		setAlarmColors(initialFailureForegroundColor,initialFailureBackgroundColor);
		setGeneralBackground(refinedBackgroundColor);
		dialogContainer.add(mainPanel);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void implementPreferredSize() {
		//
		if (dialogContainer.safelyIsMaximized()) {
			return;
		};
		//
		Font initialFont= create_new_font();
		safelySetGeneralFont(initialFont);
		//
		Dimension preferredSize= new Dimension();
		Dimension minimumSize= new Dimension();
		safelyGetPreferredAndMinimumSize(preferredSize,minimumSize);
		//
		previousActualSize.set(
			new Dimension(
				StrictMath.max(preferredSize.width,minimumSize.width),
				StrictMath.max(preferredSize.height,minimumSize.height)));
		//
		safelySetSizeAndCentreMainPanel(previousActualSize.get());
	}
	//
	protected void safelySetSizeAndCentreMainPanel(final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklySetSizeAndCentreMainPanel(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklySetSizeAndCentreMainPanel(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklySetSizeAndCentreMainPanel(Dimension size) {
		quicklySetSize(size);
		safelyCentreMainPanelIfNecessary();
	}
	//
	public void safelyCentreMainPanelIfNecessary() {
		try {
			if (actualCoordinates.get().areCentered()) {
				safelyPositionMainPanel();
			}
		} catch (UseDefaultLocation e) {
		}
	}
	//
	public void safelyPositionMainPanel() {
		try {
			Point point= safelyComputePosition(actualCoordinates);
			previousCoordinates.set(point);
			safelySetLocation(point);
		} catch (UseDefaultLocation e) {
			if (!dialogContainer.isShowing()) {
				safelySetLocationByPlatform();
			}
		}
	}
	//
	public Point safelyComputePosition(AtomicReference<ExtendedCoordinates> actualCoordinates) throws UseDefaultLocation {
		//
		Dimension initialSize= safelyGetSize();
		//
		int initialWidth= initialSize.width;
		int initialHeight= initialSize.height;
		//
		int x= 0;
		int y= 0;
		//
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		//
		Rectangle parentLayoutSize= safelyComputeParentLayoutSize();
		//
		ExtendedCoordinates actualPoint= actualCoordinates.get();
		x= DialogUtils.calculateRealCoordinate(actualPoint.x,parentLayoutSize.x,parentLayoutSize.width,gridX,initialWidth);
		y= DialogUtils.calculateRealCoordinate(actualPoint.y,parentLayoutSize.y,parentLayoutSize.height,gridY,initialHeight);
		//
		return new Point(x,y);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyGetPreferredAndMinimumSize(final Dimension preferredSize, final Dimension minimumSize) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyGetPreferredAndMinimumSize(preferredSize,minimumSize);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyGetPreferredAndMinimumSize(preferredSize,minimumSize);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklyGetPreferredAndMinimumSize(final Dimension preferredSize, final Dimension minimumSize) {
		preferredSize.setSize(dialogContainer.getRealPreferredSize());
		minimumSize.setSize(dialogContainer.getRealMinimumSize());
	}
	//
	public Rectangle safelyComputeParentLayoutSize() {
		if (SwingUtilities.isEventDispatchThread()) {
			return dialogContainer.computeParentLayoutSize();
		} else {
			try {
				final AtomicReference<Rectangle> value= new AtomicReference<Rectangle>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(dialogContainer.computeParentLayoutSize());
					}
				});
				return value.get();
			} catch (InterruptedException e) {
				return new Rectangle();
			} catch (InvocationTargetException e) {
				return new Rectangle();
			}
		}
	}
	//
	public void safelySetSize(final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklySetSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklySetSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklySetSize(Dimension size) {
		dialogContainer.setSize(size);
	}
	//
	public Dimension safelyGetSize() {
		if (SwingUtilities.isEventDispatchThread()) {
			return dialogContainer.getSize();
		} else {
			try {
				final AtomicReference<Dimension> value= new AtomicReference<Dimension>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(dialogContainer.getSize());
					}
				});
				return value.get();
			} catch (InterruptedException e) {
				return new Dimension();
			} catch (InvocationTargetException e) {
				return new Dimension();
			}
		}
	}
	//
	public void safelySetGeneralFont(final Font font) {
		if (SwingUtilities.isEventDispatchThread()) {
			setGeneralFont(font);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						setGeneralFont(font);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public Font quicklyGetFont() {
		if (mainPanel==null) {
			return null;
		} else {
			return mainPanel.getFont();
		}
	}
	//
	public void safelyRevalidateAndRepaint() {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyRevalidateAndRepaint();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyRevalidateAndRepaint();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklyRevalidateAndRepaint() {
		dialogContainer.revalidate();
		// dialogContainer.invalidate();
		// dialogContainer.revalidate();
		// dialogContainer.validateTree();
		dialogContainer.repaint();
	}
	//
	public void safelyInvalidateAndRepaint() {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyInvalidateAndRepaint();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyInvalidateAndRepaint();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklyInvalidateAndRepaint() {
		dialogContainer.invalidate();
		dialogContainer.repaint();
	}
	//
	public void safelyValidate() {
		if (SwingUtilities.isEventDispatchThread()) {
			dialogContainer.validate();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						dialogContainer.validate();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public void safelyInvalidate() {
		if (SwingUtilities.isEventDispatchThread()) {
			dialogContainer.invalidate();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						dialogContainer.invalidate();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public void safelyRepaint() {
		if (SwingUtilities.isEventDispatchThread()) {
			dialogContainer.repaint();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						dialogContainer.repaint();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelySetLocationByPlatform() {
		if (SwingUtilities.isEventDispatchThread()) {
			setLocationByPlatform();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						setLocationByPlatform();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void setLocationByPlatform() {
		dialogContainer.setLocationByPlatform(true);
		isDisposed.set(true);
	}
	//
	public void safelySetLocation(final Point p) {
		if (SwingUtilities.isEventDispatchThread()) {
			setLocation(p);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						setLocation(p);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void setLocation(Point p) {
		dialogContainer.setLocation(p);
		isDisposed.set(true);
	}
	//
	protected boolean isDisposed() {
		return isDisposed.get();
	}
	//
	public Point safelyGetLocation() {
		if (SwingUtilities.isEventDispatchThread()) {
			return dialogContainer.getLocation();
		} else {
			try {
				final AtomicReference<Point> value= new AtomicReference<Point>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(dialogContainer.getLocation());
					}
				});
				return value.get();
			} catch (InterruptedException e) {
				return null;
			} catch (InvocationTargetException e) {
				return null;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public GraphicsConfiguration safelyGetGraphicsConfiguration() {
		if (SwingUtilities.isEventDispatchThread()) {
			return quicklyGetGraphicsConfiguration();
		} else {
			try {
				final AtomicReference<GraphicsConfiguration> value= new AtomicReference<GraphicsConfiguration>();
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(quicklyGetGraphicsConfiguration());
					}
				});
				return value.get();
			} catch (InterruptedException e) {
				return null;
			} catch (InvocationTargetException e) {
				return null;
			}
		}
	}
	private GraphicsConfiguration quicklyGetGraphicsConfiguration() {
		if (dialogContainer != null) {
			return dialogContainer.getGraphicsConfiguration();
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyHide() {
		dialogContainer.safelySetVisible(false);
	}
	public void safelyDispose() {
		dialogContainer.safelyDispose();
	}
	public void safelyMaximize() {
		dialogContainer.safelyMaximize();
	}
	public void safelyMinimize() {
		dialogContainer.safelyMinimize();
	}
	public void safelyRestore() {
		dialogContainer.safelyRestore();
	}
	//
	public boolean safelyIsVisible() {
		return dialogContainer.safelyIsVisible();
	}
	public boolean safelyIsHidden() {
		return dialogContainer.safelyIsHidden();
	}
	public boolean safelyIsMaximized() {
		return dialogContainer.safelyIsMaximized();
	}
	public boolean safelyIsMinimized() {
		return dialogContainer.safelyIsMinimized();
	}
	public boolean safelyIsRestored() {
		return dialogContainer.safelyIsRestored();
	}
	//
	public boolean safelyIsShowing() {
		if (SwingUtilities.isEventDispatchThread()) {
			return isShowing();
		} else {
			final AtomicBoolean value= new AtomicBoolean(false);
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						value.set(isShowing());
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return value.get();
		}
	}
	private boolean isShowing() {
		return dialogContainer.isShowing();
	}
	//
	public void safelyDefineDefaultButton() {
		if (SwingUtilities.isEventDispatchThread()) {
			defineDefaultButton();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						defineDefaultButton();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
}
