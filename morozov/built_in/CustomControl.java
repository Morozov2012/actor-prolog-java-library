// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.gui.*;
import morozov.system.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import javax.swing.JMenuBar;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CustomControl extends Alpha implements ComponentListener {
	//
	public Boolean enableSceneAntialiasing= null;
	public TranslatedMenuItem[] menu= null;
	//
	public AtomicReference<ExtendedTitle> title= new AtomicReference<>();
	public AtomicReference<ExtendedCoordinate> x= new AtomicReference<>();
	public AtomicReference<ExtendedCoordinate> y= new AtomicReference<>();
	public AtomicReference<ColorAttribute> backgroundColor= new AtomicReference<>();
	public Boolean usePixelMeasurements= null;
	public AtomicReference<ColorAttribute> textColor= new AtomicReference<>();
	public AtomicReference<ColorAttribute> spaceColor= new AtomicReference<>();
	//
	public AtomicReference<ExtendedFontName> fontName= new AtomicReference<>();
	public AtomicReference<ExtendedFontSize> fontSize= new AtomicReference<>();
	public AtomicReference<ExtendedFontStyle> fontStyle= new AtomicReference<>();
	//
	protected InnerPage graphicWindow= null;
	protected CanvasSpace canvasSpace= null;
	protected CanvasSpaceAttributes spaceAttributes;
	protected boolean suspendRedrawing= false;
	//
	public CustomControl() {
	}
	public CustomControl(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getBuiltInSlot_E_title() {
		return termDefault;
	}
	public Term getBuiltInSlot_E_x() {
		return termDefault;
	}
	public Term getBuiltInSlot_E_y() {
		return termDefault;
	}
	public Term getBuiltInSlot_E_background_color() {
		return termDefault;
	}
	public Term getBuiltInSlot_E_use_pixel_measurements() {
		return termNo;
	}
	public Term getBuiltInSlot_E_text_color() {
		return termDefault;
	}
	public Term getBuiltInSlot_E_space_color() {
		return termDefault;
	}
	//
	public Term getBuiltInSlot_E_font_name() {
		return termDefault;
	}
	public Term getBuiltInSlot_E_font_size() {
		return termDefault;
	}
	public Term getBuiltInSlot_E_font_style() {
		return termDefault;
	}
	//
	public Term getBuiltInSlot_E_enable_scene_antialiasing() {
		return termNo;
	}
	public Term getBuiltInSlot_E_menu() {
		return PrologEmptyList.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long entry_s_Initialize_0() {
		return -1;
	}
	public long entry_s_Start_0() {
		return -1;
	}
	public long entry_s_Stop_0() {
		return -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Color getDefaultBackgroundColor() {
		return Color.WHITE;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set title
	//
	public void setTitle1s(ChoisePoint iX, Term a1) {
		setTitle(ExtendedTitle.argumentToExtendedTitle(a1,iX));
		changeTitle(iX);
	}
	public void setTitle(ExtendedTitle value) {
		title.set(value);
	}
	public void getTitle0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getTitle(iX).toTerm());
	}
	public void getTitle0fs(ChoisePoint iX) {
	}
	public ExtendedTitle getTitle(ChoisePoint iX) {
		ExtendedTitle text= title.get();
		if (text != null) {
			return text;
		} else {
			Term value= getBuiltInSlot_E_title();
			return ExtendedTitle.argumentToExtendedTitleSafe(value,iX);
		}
	}
	public Term getTitleOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedTitle text= title.get();
		if (text != null) {
			return text.toTerm();
		} else {
			Term value= getBuiltInSlot_E_title();
			return ExtendedTitle.argumentToExtendedTitleOrFail(value,iX);
		}
	}
	//
	// get/set x
	//
	public void setX1s(ChoisePoint iX, Term a1) {
		setX(ExtendedCoordinate.argumentToExtendedCoordinate(a1,iX));
	}
	public void setX(ExtendedCoordinate value) {
		x.set(value);
	}
	public void getX0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getX(iX).toTerm());
	}
	public void getX0fs(ChoisePoint iX) {
	}
	public ExtendedCoordinate getX(ChoisePoint iX) {
		ExtendedCoordinate coordinate= x.get();
		if (coordinate != null) {
			return coordinate;
		} else {
			Term value= getBuiltInSlot_E_x();
			return ExtendedCoordinate.argumentToExtendedCoordinateSafe(value,iX);
		}
	}
	public Term getXOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedCoordinate coordinate= x.get();
		if (coordinate != null) {
			return coordinate.toTerm();
		} else {
			Term value= getBuiltInSlot_E_x();
			return ExtendedCoordinate.argumentToExtendedCoordinateOrFail(value,iX);
		}
	}
	//
	// get/set y
	//
	public void setY1s(ChoisePoint iX, Term a1) {
		setY(ExtendedCoordinate.argumentToExtendedCoordinate(a1,iX));
	}
	public void setY(ExtendedCoordinate value) {
		y.set(value);
	}
	public void getY0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getY(iX).toTerm());
	}
	public void getY0fs(ChoisePoint iX) {
	}
	public ExtendedCoordinate getY(ChoisePoint iX) {
		ExtendedCoordinate coordinate= y.get();
		if (coordinate != null) {
			return coordinate;
		} else {
			Term value= getBuiltInSlot_E_y();
			return ExtendedCoordinate.argumentToExtendedCoordinateSafe(value,iX);
		}
	}
	public Term getYOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedCoordinate coordinate= y.get();
		if (coordinate != null) {
			return coordinate.toTerm();
		} else {
			Term value= getBuiltInSlot_E_y();
			return ExtendedCoordinate.argumentToExtendedCoordinateOrFail(value,iX);
		}
	}
	//
	// get/set defaultPosition
	//
	public void setDefaultPosition2s(ChoisePoint iX, Term a1, Term a2) {
		setX(ExtendedCoordinate.argumentToExtendedCoordinate(a1,iX));
		setY(ExtendedCoordinate.argumentToExtendedCoordinate(a2,iX));
	}
	public void getDefaultPosition2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		a1.setBacktrackableValue(getX(iX).toTerm(),iX);
		a2.setBacktrackableValue(getY(iX).toTerm(),iX);
	}
	//
	// get/set backgroundColor
	//
	public void setBackgroundColor1s(ChoisePoint iX, Term a1) {
		setBackgroundColor(ColorAttributeConverters.argumentToColorAttribute(a1,iX));
		changeBackgroundColor(iX);
	}
	public void setBackgroundColor(ColorAttribute value) {
		backgroundColor.set(value);
	}
	public void getBackgroundColor0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ColorAttributeConverters.toTerm(getBackgroundColor(iX)));
	}
	public void getBackgroundColor0fs(ChoisePoint iX) {
	}
	public ColorAttribute getBackgroundColor(ChoisePoint iX) {
		ColorAttribute color= backgroundColor.get();
		if (color != null) {
			return color;
		} else {
			Term value= getBuiltInSlot_E_background_color();
			return ColorAttributeConverters.argumentToColorAttributeSafe(value,iX);
		}
	}
	public Term getBackgroundColorOrFail(ChoisePoint iX) throws Backtracking {
		ColorAttribute color= backgroundColor.get();
		if (color != null) {
			return ColorAttributeConverters.toTerm(color);
		} else {
			Term value= getBuiltInSlot_E_background_color();
			return ColorAttributeConverters.argumentToColorAttributeOrFail(value,iX);
		}
	}
	//
	// get/set usePixelMeasurements
	//
	public void setUsePixelMeasurements1s(ChoisePoint iX, Term a1) {
		setUsePixelMeasurements(YesNoConverters.termYesNo2Boolean(a1,iX));
	}
	public void setUsePixelMeasurements(boolean value) {
		usePixelMeasurements= value;
	}
	public void getUsePixelMeasurements0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getUsePixelMeasurements(iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void getUsePixelMeasurements0fs(ChoisePoint iX) {
	}
	public boolean getUsePixelMeasurements(ChoisePoint iX) {
		if (usePixelMeasurements != null) {
			return usePixelMeasurements;
		} else {
			Term value= getBuiltInSlot_E_use_pixel_measurements();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set defaultBounds
	//
	public void setDefaultBounds4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		setX(ExtendedCoordinate.argumentToExtendedCoordinate(a1,iX));
		setY(ExtendedCoordinate.argumentToExtendedCoordinate(a2,iX));
		setWidth(ExtendedSize.argumentToExtendedSize(a3,iX));
		setHeight(ExtendedSize.argumentToExtendedSize(a4,iX));
	}
	public void getDefaultBounds4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) {
		a1.setBacktrackableValue(getX(iX).toTerm(),iX);
		a2.setBacktrackableValue(getY(iX).toTerm(),iX);
		a3.setBacktrackableValue(getWidth(iX).toTerm(),iX);
		a4.setBacktrackableValue(getHeight(iX).toTerm(),iX);
	}
	//
	// get/set textColor
	//
	public void setTextColor1s(ChoisePoint iX, Term a1) {
		setTextColor(ColorAttributeConverters.argumentToColorAttribute(a1,iX));
		changeTextColor(iX);
	}
	public void setTextColor(ColorAttribute value) {
		textColor.set(value);
	}
	public void getTextColor0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ColorAttributeConverters.toTerm(getTextColor(iX)));
	}
	public void getTextColor0fs(ChoisePoint iX) {
	}
	public ColorAttribute getTextColor(ChoisePoint iX) {
		ColorAttribute color= textColor.get();
		if (color != null) {
			return color;
		} else {
			Term value= getBuiltInSlot_E_text_color();
			return ColorAttributeConverters.argumentToColorAttributeSafe(value,iX);
		}
	}
	public Term getTextColorOrFail(ChoisePoint iX) throws Backtracking {
		ColorAttribute color= textColor.get();
		if (color != null) {
			return ColorAttributeConverters.toTerm(color);
		} else {
			Term value= getBuiltInSlot_E_text_color();
			return ColorAttributeConverters.argumentToColorAttributeOrFail(value,iX);
		}
	}
	//
	// get/set spaceColor
	//
	public void setSpaceColor1s(ChoisePoint iX, Term a1) {
		setSpaceColor(ColorAttributeConverters.argumentToColorAttribute(a1,iX));
		changeSpaceColor(iX);
	}
	public void setSpaceColor(ColorAttribute value) {
		spaceColor.set(value);
	}
	public void getSpaceColor0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ColorAttributeConverters.toTerm(getSpaceColor(iX)));
	}
	public void getSpaceColor0fs(ChoisePoint iX) {
	}
	public ColorAttribute getSpaceColor(ChoisePoint iX) {
		ColorAttribute color= spaceColor.get();
		if (color != null) {
			return color;
		} else {
			Term value= getBuiltInSlot_E_space_color();
			return ColorAttributeConverters.argumentToColorAttributeSafe(value,iX);
		}
	}
	public Term getSpaceColorOrFail(ChoisePoint iX) throws Backtracking {
		ColorAttribute color= spaceColor.get();
		if (color != null) {
			return ColorAttributeConverters.toTerm(color);
		} else {
			Term value= getBuiltInSlot_E_space_color();
			return ColorAttributeConverters.argumentToColorAttributeOrFail(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set fontName
	//
	public void setFontName1s(ChoisePoint iX, Term a1) {
		setFontName(ExtendedFontName.argumentToExtendedFontName(a1,iX));
		changeFontName(iX);
	}
	public void setFontName(ExtendedFontName value) {
		fontName.set(value);
	}
	public void getFontName0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getFontName(iX).toTerm());
	}
	public void getFontName0fs(ChoisePoint iX) {
	}
	public ExtendedFontName getFontName(ChoisePoint iX) {
		ExtendedFontName name= fontName.get();
		if (name != null) {
			return name;
		} else {
			Term value= getBuiltInSlot_E_font_name();
			return ExtendedFontName.argumentToExtendedFontNameSafe(value,iX);
		}
	}
	public Term getFontNameOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedFontName name= fontName.get();
		if (name != null) {
			return name.toTerm();
		} else {
			Term value= getBuiltInSlot_E_font_name();
			return ExtendedFontName.argumentToExtendedFontNameOrFail(value,iX);
		}
	}
	//
	// get/set fontSize
	//
	public void setFontSize1s(ChoisePoint iX, Term a1) {
		setFontSize(ExtendedFontSize.argumentToExtendedFontSize(a1,iX));
		changeFontSize(iX);
	}
	public void setFontSize(ExtendedFontSize value) {
		fontSize.set(value);
	}
	public void getFontSize0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getFontSize(iX).toTerm());
	}
	public void getFontSize0fs(ChoisePoint iX) {
	}
	public ExtendedFontSize getFontSize(ChoisePoint iX) {
		ExtendedFontSize size= fontSize.get();
		if (size != null) {
			return size;
		} else {
			Term value= getBuiltInSlot_E_font_size();
			return ExtendedFontSize.argumentToExtendedFontSizeSafe(value,iX);
		}
	}
	public Term getFontSizeOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedFontSize size= fontSize.get();
		if (size != null) {
			return size.toTerm();
		} else {
			Term value= getBuiltInSlot_E_font_size();
			return ExtendedFontSize.argumentToExtendedFontSizeOrFail(value,iX);
		}
	}
	//
	// get/set fontStyle
	//
	public void setFontStyle1s(ChoisePoint iX, Term a1) {
		setFontStyle(ExtendedFontStyle.argumentToExtendedFontStyle(a1,iX));
		changeFontStyle(iX);
	}
	public void setFontStyle(ExtendedFontStyle value) {
		fontStyle.set(value);
	}
	public void getFontStyle0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getFontStyle(iX).toTerm());
	}
	public void getFontStyle0fs(ChoisePoint iX) {
	}
	public ExtendedFontStyle getFontStyle(ChoisePoint iX) {
		ExtendedFontStyle style= fontStyle.get();
		if (style != null) {
			return style;
		} else {
			Term value= getBuiltInSlot_E_font_style();
			return ExtendedFontStyle.argumentToExtendedFontStyleSafe(value,iX);
		}
	}
	public Term getFontStyleOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedFontStyle style= fontStyle.get();
		if (style != null) {
			return style.toTerm();
		} else {
			Term value= getBuiltInSlot_E_font_style();
			return ExtendedFontStyle.argumentToExtendedFontStyleOrFail(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set enableSceneAntialiasing
	//
	public void setEnableSceneAntialiasing1s(ChoisePoint iX, Term a1) {
		setEnableSceneAntialiasing(YesNoConverters.termYesNo2Boolean(a1,iX));
	}
	public void setEnableSceneAntialiasing(boolean value) {
		enableSceneAntialiasing= value;
	}
	public void getEnableSceneAntialiasing0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getEnableSceneAntialiasing(iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void getEnableSceneAntialiasing0fs(ChoisePoint iX) {
	}
	public boolean getEnableSceneAntialiasing(ChoisePoint iX) {
		if (enableSceneAntialiasing != null) {
			return enableSceneAntialiasing;
		} else {
			Term value= getBuiltInSlot_E_enable_scene_antialiasing();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set menu
	//
	public void setMenu1s(ChoisePoint iX, Term a1) {
		TranslatedMenuItem[] list= TranslatedMenuItem.argumentToTranslatedMenuItems(a1,iX);
		setMenu(list);
		addMenuIfNecessary(iX,list);
	}
	public void setMenu(TranslatedMenuItem[] value) {
		menu= value;
	}
	public void getMenu0ff(ChoisePoint iX, PrologVariable result) {
		TranslatedMenuItem[] value= getMenu(iX);
		result.setNonBacktrackableValue(TranslatedMenuItem.translatedMenuItemsToTerm(value));
	}
	public void getMenu0fs(ChoisePoint iX) {
	}
	public TranslatedMenuItem[] getMenu(ChoisePoint iX) {
		if (menu != null) {
			return menu;
		} else {
			Term value= getBuiltInSlot_E_menu();
			return TranslatedMenuItem.argumentToTranslatedMenuItems(value,iX);
		}
	}
	//
	protected void addMenuIfNecessary(ChoisePoint iX, TranslatedMenuItem[] list) {
		synchronized (this) {
			if (graphicWindow != null) {
				if (list.length > 0) {
					JMenuBar menuBar= TranslatedMenuItem.argumentToJMenuBar(list,graphicWindow);
					graphicWindow.safelySetMenu(menuBar);
				} else {
					graphicWindow.safelySetMenu(null);
				};
				repaintAfterDelay(iX);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setActualPosition2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedCoordinate eX= ExtendedCoordinate.argumentToExtendedCoordinate(a1,iX);
		ExtendedCoordinate eY= ExtendedCoordinate.argumentToExtendedCoordinate(a2,iX);
		createGraphicWindowIfNecessary(iX,false);
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.setLogicalX(eX);
				graphicWindow.setLogicalY(eY);
				graphicWindow.safelyRestoreSize(staticContext);
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				canvasSpace.safelyRepaint();
			}
		}
	}
	public void getActualPosition2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		boolean currentUsePixelMeasurements= getUsePixelMeasurements(iX);
		Point canvasLocation= new Point();
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyGetComponentLocation(canvasLocation);
			} else {
				if (canvasSpace != null) {
					canvasSpace.safelyGetComponentLocation(canvasLocation);
				}
			}
		};
		double logicalX;
		double logicalY;
		if (currentUsePixelMeasurements) {
			logicalX= canvasLocation.x;
			logicalY= canvasLocation.y;
		} else {
			Dimension desktopSize= getSizeOfMainDesktopPane();
			double gridX= DefaultOptions.gridWidth;
			double gridY= DefaultOptions.gridHeight;
			logicalX= CoordinateAndSize.reconstruct(canvasLocation.x,desktopSize.width,gridX);
			logicalY= CoordinateAndSize.reconstruct(canvasLocation.y,desktopSize.height,gridY);
		};
		a1.setBacktrackableValue(new PrologReal(logicalX),iX);
		a2.setBacktrackableValue(new PrologReal(logicalY),iX);
	}
	//
	public void getPositionInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		Point canvasLocation= new Point();
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyGetComponentLocation(canvasLocation);
			} else {
				if (canvasSpace != null) {
					canvasSpace.safelyGetComponentLocation(canvasLocation);
				}
			}
		};
		a1.setBacktrackableValue(new PrologInteger(canvasLocation.x),iX);
		a2.setBacktrackableValue(new PrologInteger(canvasLocation.y),iX);
	}
	//
	public void setActualSize2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedSize eWidth= ExtendedSize.argumentToExtendedSize(a1,iX);
		ExtendedSize eHeight= ExtendedSize.argumentToExtendedSize(a2,iX);
		createGraphicWindowIfNecessary(iX,false);
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.setLogicalWidth(eWidth);
				graphicWindow.setLogicalHeight(eHeight);
				graphicWindow.safelyRestoreSize(staticContext);
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				canvasSpace.safelyRepaint();
			}
		}
	}
	public void getActualSize2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		boolean currentUsePixelMeasurements= getUsePixelMeasurements(iX);
		Dimension canvasSize= new Dimension();
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyGetComponentSize(canvasSize);
			} else {
				if (canvasSpace != null) {
					canvasSpace.safelyGetComponentSize(canvasSize);
				}
			}
		};
		double logicalWidth;
		double logicalHeight;
		if (currentUsePixelMeasurements) {
			logicalWidth= canvasSize.width;
			logicalHeight= canvasSize.height;
		} else {
			Dimension desktopSize= getSizeOfMainDesktopPane();
			double gridX= DefaultOptions.gridWidth;
			double gridY= DefaultOptions.gridHeight;
			logicalWidth= CoordinateAndSize.reconstruct(canvasSize.width,desktopSize.width,gridX);
			logicalHeight= CoordinateAndSize.reconstruct(canvasSize.height,desktopSize.height,gridY);
		};
		a1.setBacktrackableValue(new PrologReal(logicalWidth),iX);
		a2.setBacktrackableValue(new PrologReal(logicalHeight),iX);
	}
	//
	public void getSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		Dimension canvasSize= new Dimension();
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyGetComponentSize(canvasSize);
			} else {
				if (canvasSpace != null) {
					canvasSpace.safelyGetComponentSize(canvasSize);
				}
			}
		};
		a1.setBacktrackableValue(new PrologInteger(canvasSize.width),iX);
		a2.setBacktrackableValue(new PrologInteger(canvasSize.height),iX);
	}
	//
	public void setActualBounds4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		ExtendedCoordinate eX= ExtendedCoordinate.argumentToExtendedCoordinate(a1,iX);
		ExtendedCoordinate eY= ExtendedCoordinate.argumentToExtendedCoordinate(a2,iX);
		ExtendedSize eWidth= ExtendedSize.argumentToExtendedSize(a3,iX);
		ExtendedSize eHeight= ExtendedSize.argumentToExtendedSize(a4,iX);
		createGraphicWindowIfNecessary(iX,false);
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.setLogicalWidth(eWidth);
				graphicWindow.setLogicalHeight(eHeight);
				graphicWindow.setLogicalX(eX);
				graphicWindow.setLogicalY(eY);
				graphicWindow.safelyRestoreSize(staticContext);
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				canvasSpace.safelyRepaint();
			}
		}
	}
	public void getActualBounds4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) {
		boolean currentUsePixelMeasurements= getUsePixelMeasurements(iX);
		Point canvasLocation= new Point();
		Dimension canvasSize= new Dimension();
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyGetComponentLocation(canvasLocation,canvasSize);
			} else {
				if (canvasSpace != null) {
					canvasSpace.safelyGetComponentLocation(canvasLocation,canvasSize);
				}
			}
		};
		double logicalX;
		double logicalY;
		double logicalWidth;
		double logicalHeight;
		if (currentUsePixelMeasurements) {
			logicalX= canvasLocation.x;
			logicalY= canvasLocation.y;
			logicalWidth= canvasSize.width;
			logicalHeight= canvasSize.height;
		} else {
			Dimension desktopSize= getSizeOfMainDesktopPane();
			double gridX= DefaultOptions.gridWidth;
			double gridY= DefaultOptions.gridHeight;
			logicalX= CoordinateAndSize.reconstruct(canvasLocation.x,desktopSize.width,gridX);
			logicalY= CoordinateAndSize.reconstruct(canvasLocation.y,desktopSize.height,gridY);
			logicalWidth= CoordinateAndSize.reconstruct(canvasSize.width,desktopSize.width,gridX);
			logicalHeight= CoordinateAndSize.reconstruct(canvasSize.height,desktopSize.height,gridY);
		};
		a1.setBacktrackableValue(new PrologReal(logicalX),iX);
		a2.setBacktrackableValue(new PrologReal(logicalY),iX);
		a3.setBacktrackableValue(new PrologReal(logicalWidth),iX);
		a4.setBacktrackableValue(new PrologReal(logicalHeight),iX);
	}
	//
	protected Dimension getSizeOfMainDesktopPane() {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(staticContext);
		Dimension desktopSize;
		if (desktop==null) {
			desktopSize= new Dimension(1,1);
		} else {
			desktopSize= DesktopUtils.safelyGetComponentSize(desktop);
		};
		return desktopSize;
	}
	//
	public void getBoundsInPixels4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) {
		Point canvasLocation= new Point();
		Dimension canvasSize= new Dimension();
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyGetComponentLocation(canvasLocation,canvasSize);
			} else {
				if (canvasSpace != null) {
					canvasSpace.safelyGetComponentLocation(canvasLocation,canvasSize);
				}
			}
		};
		a1.setBacktrackableValue(new PrologInteger(canvasLocation.x),iX);
		a2.setBacktrackableValue(new PrologInteger(canvasLocation.y),iX);
		a3.setBacktrackableValue(new PrologInteger(canvasSize.width),iX);
		a4.setBacktrackableValue(new PrologInteger(canvasSize.height),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void releaseSystemResources() {
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyDispose();
			}
		};
		super.releaseSystemResources();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void changeTitle(ChoisePoint iX) {
		changeTitle(getTitle(iX),iX);
	}
	protected void changeTitle(ExtendedTitle title, ChoisePoint iX) {
		String text= title.getValueOrDefaultText("");
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelySetTitle(text);
			}
		}
	}
	//
	protected void changeTextColor(ChoisePoint iX) {
	}
	//
	protected void changeSpaceColor(ChoisePoint iX) {
	}
	//
	protected void changeBackgroundColor(ChoisePoint iX) {
		changeBackgroundColor(getBackgroundColor(iX),iX);
	}
	protected void changeBackgroundColor(ColorAttribute eColor, ChoisePoint iX) {
		Color color;
		try {
			color= eColor.getValue();
		} catch (UseDefaultColor e) {
			color= getDefaultBackgroundColor();
		};
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelySetBackground(color);
			} else if (canvasSpace != null) {
				canvasSpace.safelySetBackground(color);
			}
		}
	}
	//
	protected void changeFontName(ChoisePoint iX) {
	}
	//
	protected void changeFontSize(ChoisePoint iX) {
	}
	//
	protected void changeFontStyle(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void show0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
	}
	public void show2s(ChoisePoint iX, Term a1, Term a2) {
		createGraphicWindowIfNecessary(iX,true);
	}
	public void show1ms(ChoisePoint iX, Term... args) {
		createGraphicWindowIfNecessary(iX,true);
	}
	//
	public void redraw0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized (this) {
			if (graphicWindow != null) {
				refreshAttributesOfInternalFrame(graphicWindow,iX);
				graphicWindow.safelyRestoreSize(staticContext);
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				refreshAttributesOfCanvasSpace(iX);
				canvasSpace.safelyRepaint();
			}
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		if (canvasSpaceDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			synchronized (this) {
				if (graphicWindow != null) {
					graphicWindow.safelySetVisible(false);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void refreshAttributesOfInternalFrame(InnerPage graphicWindow, ChoisePoint iX) {
		String currentTitle= getTitle(iX).getValueOrDefaultText("");
		refreshAttributesOfInternalFrame(graphicWindow,currentTitle,iX);
	}
	protected void refreshAttributesOfInternalFrame(InnerPage graphicWindow, String title, ChoisePoint iX) {
		//
		if (title != null) {
			graphicWindow.safelySetTitle(title);
		};
		//
		ExtendedCoordinate eX= getX(iX);
		ExtendedCoordinate eY= getY(iX);
		ExtendedSize eWidth= getWidth(iX);
		ExtendedSize eHeight= getHeight(iX);
		boolean pixelMeasurements= getUsePixelMeasurements(iX);
		//
		graphicWindow.setLogicalWidth(eWidth);
		graphicWindow.setLogicalHeight(eHeight);
		graphicWindow.setLogicalX(eX);
		graphicWindow.setLogicalY(eY);
		graphicWindow.setUsePixelMeasurements(pixelMeasurements);
		//
		refreshAttributesOfCanvasSpace(iX);
	}
	//
	protected void refreshAttributesOfCanvasSpace(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void maximize0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyMaximize();
			}
		}
	}
	//
	public void minimize0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyMinimize();
			}
		}
	}
	//
	public void restore0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized (this) {
			if (graphicWindow != null) {
				graphicWindow.safelyRestore();
			}
		}
	}
	//
	public void isVisible0s(ChoisePoint iX) throws Backtracking {
		if (canvasSpaceDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized (this) {
				if (graphicWindow != null) {
					if (!graphicWindow.safelyIsVisible()) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void isHiddenCustomControl(ChoisePoint iX) throws Backtracking {
		if (canvasSpaceDoesNotExist()) {
		} else {
			synchronized (this) {
				if (graphicWindow != null) {
					if (!graphicWindow.safelyIsHidden()) {
						throw Backtracking.instance;
					}

				}
			}
		}
	}
	//
	public void isMaximized0s(ChoisePoint iX) throws Backtracking {
		if (canvasSpaceDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized (this) {
				if (graphicWindow != null) {
					if (!graphicWindow.safelyIsMaximized()) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void isMinimized0s(ChoisePoint iX) throws Backtracking {
		if (canvasSpaceDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized (this) {
				if (graphicWindow != null) {
					if(!graphicWindow.safelyIsMinimized()) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void isRestored0s(ChoisePoint iX) throws Backtracking {
		if (canvasSpaceDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized (this) {
				if (graphicWindow != null) {
					if(!graphicWindow.safelyIsRestored()) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void createGraphicWindowIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		synchronized (this) {
			if (spaceAttributes != null) {
				if (canvasSpaceDoesNotExist() && spaceAttributes.controlIsNotInitialized()) {
					DesktopUtils.createPaneIfNecessary(staticContext);
					graphicWindow= createInternalFrameIfNecessary(iX,enableMovingWindowToFront);
					canvasSpace= graphicWindow.getCanvasSpace();
					addMenuIfNecessary(iX,getMenu(iX));
				} else if (graphicWindow != null) {
					if (!suspendRedrawing) {
						if (enableMovingWindowToFront) {
							graphicWindow.safelyMoveToFront();
						};
						graphicWindow.safelySetVisible(true);
						DesktopUtils.makeExistedMainWindowVisible(staticContext);
					}
				}
			}
		}
	}
	protected InnerPage createInternalFrameIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void action1s(ChoisePoint iX, Term actionName) {
	}
	//
	public class Action1s extends Continuation {
		//
		public Action1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initialize0s(ChoisePoint iX) {
	}
	public void start0s(ChoisePoint iX) {
	}
	public void stop0s(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void windowClosing0s(ChoisePoint iX) {
	}
	public void windowClosed0s(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void refineWidth1s(ChoisePoint iX, Term a1) {
		double ratio= GeneralConverters.argumentToReal(a1,iX);
		synchronized (this) {
			if (canvasSpace != null) {
				canvasSpace.safelyRefineWidth(ratio);
			}
		}
	}
	public void refineHeight1s(ChoisePoint iX, Term a1) {
		double ratio= GeneralConverters.argumentToReal(a1,iX);
		synchronized (this) {
			if (canvasSpace != null) {
				canvasSpace.safelyRefineHeight(ratio);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerCanvasSpace(CanvasSpace s, ChoisePoint iX) {
		synchronized (this) {
			if (canvasSpaceDoesNotExist()) {
				canvasSpace= s;
				canvasSpace.setTargetWorld(this);
				initiateRegisteredCanvasSpace(canvasSpace,iX);
				enableMouseListeners();
			}
		}
	}
	//
	protected void initiateRegisteredCanvasSpace(CanvasSpace s, ChoisePoint iX) {
	}
	protected void enableMouseListeners() {
	}
	protected void disableMouseListeners() {
	}
	//
	public void release(boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		synchronized (this) {
			if (canvasSpace != null && graphicWindow==null) {
				canvasSpace.skipDelayedRepainting();
				disableMouseListeners();
				saveCanvasSpaceAttributes();
				canvasSpace= null;
			};
			clearCustomControlTables();
		};
		long domainSignature= entry_s_Stop_0();
		callInternalProcedure(domainSignature,dialogIsModal,modalChoisePoint);
	}
	//
	public void saveCanvasSpaceAttributes() {
	}
	public void clearCustomControlTables() {
	}
	//
	public void draw(boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		if (spaceAttributes != null) {
			if (spaceAttributes.initializeControlIfNecessary()) {
				refreshAttributesOfCanvasSpace(modalChoisePoint);
				long domainSignature1= entry_s_Initialize_0();
				callInternalProcedure(domainSignature1,dialogIsModal,modalChoisePoint);
			};
			long domainSignature2= entry_s_Start_0();
			callInternalProcedure(domainSignature2,dialogIsModal,modalChoisePoint);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean canvasSpaceDoesNotExist() {
		synchronized (this) {
			return (canvasSpace==null);
		}
	}
	//
	public void repaintAfterDelay(ChoisePoint iX) {
		if (canvasSpaceDoesNotExist()) {
			return;
		} else {
			if (!suspendRedrawing) {
				createGraphicWindowIfNecessary(iX,false);
				synchronized (this) {
					if (graphicWindow != null) {
						graphicWindow.repaintAfterDelay();
					} else if (canvasSpace != null) {
						canvasSpace.repaintAfterDelay();
					}
				}
			}
		}
	}
	public void skipDelayedRepainting(ChoisePoint iX) {
		if (canvasSpaceDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			synchronized (this) {
				if (graphicWindow != null) {
					graphicWindow.skipDelayedRepainting();
				} else if (canvasSpace != null) {
					canvasSpace.skipDelayedRepainting();
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void componentHidden(ComponentEvent e) {
		sendTheWindowClosedMessage();
		DesktopUtils.selectNextInternalFrame(staticContext);
	}
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	@Override
	public void componentResized(ComponentEvent e) {
	}
	@Override
	public void componentShown(ComponentEvent e) {
	}
	//
	protected void sendTheWindowClosingMessage() {
	}
	protected void sendTheWindowClosedMessage() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void mouseClicked1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseClicked1s extends Continuation {
		//
		public MouseClicked1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseEntered1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseEntered1s extends Continuation {
		//
		public MouseEntered1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseExited1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseExited1s extends Continuation {
		//
		public MouseExited1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mousePressed1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MousePressed1s extends Continuation {
		//
		public MousePressed1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseReleased1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseReleased1s extends Continuation {
		//
		public MouseReleased1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseDragged1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseDragged1s extends Continuation {
		//
		public MouseDragged1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseMoved1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseMoved1s extends Continuation {
		//
		public MouseMoved1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
}
