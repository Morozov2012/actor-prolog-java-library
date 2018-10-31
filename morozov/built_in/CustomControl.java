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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CustomControl extends Alpha implements ComponentListener {
	//
	public Boolean enableSceneAntialiasing= null;
	public TranslatedMenuItem[] menu= null;
	//
	public AtomicReference<ExtendedTitle> title= new AtomicReference<>();
	public AtomicReference<ExtendedCoordinate> x= new AtomicReference<>();
	public AtomicReference<ExtendedCoordinate> y= new AtomicReference<>();
	public AtomicReference<ExtendedColor> backgroundColor= new AtomicReference<>();
	// public AtomicReference<ExtendedSize> width= new AtomicReference<>();
	// public AtomicReference<ExtendedSize> height= new AtomicReference<>();
	public AtomicReference<ExtendedColor> textColor= new AtomicReference<>();
	public AtomicReference<ExtendedColor> spaceColor= new AtomicReference<>();
	//
	public AtomicReference<ExtendedFontName> fontName= new AtomicReference<>();
	public AtomicReference<ExtendedFontSize> fontSize= new AtomicReference<>();
	public AtomicReference<ExtendedFontStyle> fontStyle= new AtomicReference<>();
	//
	public AtomicBoolean sceneAntialiasingIsEnabled= new AtomicBoolean(false);
	//
	// public AtomicBoolean mouseClickedEventIsEnabled= new AtomicBoolean(false);
	// public AtomicBoolean mouseEnteredEventIsEnabled= new AtomicBoolean(false);
	// public AtomicBoolean mouseExitedEventIsEnabled= new AtomicBoolean(false);
	// public AtomicBoolean mousePressedEventIsEnabled= new AtomicBoolean(false);
	// public AtomicBoolean mouseReleasedEventIsEnabled= new AtomicBoolean(false);
	// public AtomicBoolean mouseDraggedEventIsEnabled= new AtomicBoolean(false);
	// public AtomicBoolean mouseMovedEventIsEnabled= new AtomicBoolean(false);
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
	protected Term getBuiltInSlot_E_title() {
		return termDefault;
	}
	protected Term getBuiltInSlot_E_x() {
		return termDefault;
	}
	protected Term getBuiltInSlot_E_y() {
		return termDefault;
	}
	protected Term getBuiltInSlot_E_background_color() {
		return termDefault;
	}
	protected Term getBuiltInSlot_E_text_color() {
		return termDefault;
	}
	protected Term getBuiltInSlot_E_space_color() {
		return termDefault;
	}
	//
	protected Term getBuiltInSlot_E_font_name() {
		return termDefault;
	}
	protected Term getBuiltInSlot_E_font_size() {
		return termDefault;
	}
	protected Term getBuiltInSlot_E_font_style() {
		return termDefault;
	}
	//
	protected Term getBuiltInSlot_E_enable_scene_antialiasing() {
		return termNo;
	}
	protected Term getBuiltInSlot_E_menu() {
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
		setBackgroundColor(ExtendedColor.argumentToExtendedColor(a1,iX));
		changeBackgroundColor(iX);
	}
	public void setBackgroundColor(ExtendedColor value) {
		backgroundColor.set(value);
	}
	public void getBackgroundColor0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getBackgroundColor(iX).toTerm());
	}
	public void getBackgroundColor0fs(ChoisePoint iX) {
	}
	public ExtendedColor getBackgroundColor(ChoisePoint iX) {
		ExtendedColor color= backgroundColor.get();
		if (color != null) {
			return color;
		} else {
			Term value= getBuiltInSlot_E_background_color();
			return ExtendedColor.argumentToExtendedColorSafe(value,iX);
		}
	}
	public Term getBackgroundColorOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedColor color= backgroundColor.get();
		if (color != null) {
			return color.toTerm();
		} else {
			Term value= getBuiltInSlot_E_background_color();
			return ExtendedColor.argumentToExtendedColorOrFail(value,iX);
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
		setTextColor(ExtendedColor.argumentToExtendedColor(a1,iX));
		changeTextColor(iX);
	}
	public void setTextColor(ExtendedColor value) {
		textColor.set(value);
	}
	public void getTextColor0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getTextColor(iX).toTerm());
	}
	public void getTextColor0fs(ChoisePoint iX) {
	}
	public ExtendedColor getTextColor(ChoisePoint iX) {
		ExtendedColor color= textColor.get();
		if (color != null) {
			return color;
		} else {
			Term value= getBuiltInSlot_E_text_color();
			return ExtendedColor.argumentToExtendedColorSafe(value,iX);
		}
	}
	public Term getTextColorOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedColor color= textColor.get();
		if (color != null) {
			return color.toTerm();
		} else {
			Term value= getBuiltInSlot_E_text_color();
			return ExtendedColor.argumentToExtendedColorOrFail(value,iX);
		}
	}
	//
	// get/set spaceColor
	//
	public void setSpaceColor1s(ChoisePoint iX, Term a1) {
		setSpaceColor(ExtendedColor.argumentToExtendedColor(a1,iX));
		changeSpaceColor(iX);
	}
	public void setSpaceColor(ExtendedColor value) {
		spaceColor.set(value);
	}
	public void getSpaceColor0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getSpaceColor(iX).toTerm());
	}
	public void getSpaceColor0fs(ChoisePoint iX) {
	}
	public ExtendedColor getSpaceColor(ChoisePoint iX) {
		ExtendedColor color= spaceColor.get();
		if (color != null) {
			return color;
		} else {
			Term value= getBuiltInSlot_E_space_color();
			return ExtendedColor.argumentToExtendedColorSafe(value,iX);
		}
	}
	public Term getSpaceColorOrFail(ChoisePoint iX) throws Backtracking {
		ExtendedColor color= spaceColor.get();
		if (color != null) {
			return color.toTerm();
		} else {
			Term value= getBuiltInSlot_E_space_color();
			return ExtendedColor.argumentToExtendedColorOrFail(value,iX);
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
		setEnableSceneAntialiasing(YesNo.termYesNo2Boolean(a1,iX));
	}
	public void setEnableSceneAntialiasing(boolean value) {
		enableSceneAntialiasing= value;
	}
	public void getEnableSceneAntialiasing0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getEnableSceneAntialiasing(iX);
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(value));
	}
	public void getEnableSceneAntialiasing0fs(ChoisePoint iX) {
	}
	public boolean getEnableSceneAntialiasing(ChoisePoint iX) {
		if (enableSceneAntialiasing != null) {
			return enableSceneAntialiasing;
		} else {
			Term value= getBuiltInSlot_E_enable_scene_antialiasing();
			return YesNo.termYesNo2Boolean(value,iX);
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
		// createGraphicWindowIfNecessary(iX,false);
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
				graphicWindow.logicalX.set(eX);
				graphicWindow.logicalY.set(eY);
				graphicWindow.safelyRestoreSize(staticContext);
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				// redrawCanvas(iX);
				canvasSpace.safelyRepaint();
			}
		}
	}
	public void getActualPosition2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
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
		Dimension desktopSize= getSizeOfMainDesctopPane();
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		double logicalX= CoordinateAndSize.reconstruct(canvasLocation.x,desktopSize.width,gridX);
		double logicalY= CoordinateAndSize.reconstruct(canvasLocation.y,desktopSize.height,gridY);
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
				graphicWindow.logicalWidth.set(eWidth);
				graphicWindow.logicalHeight.set(eHeight);
				graphicWindow.safelyRestoreSize(staticContext);
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				// redrawCanvas(iX);
				canvasSpace.safelyRepaint();
			}
		}
	}
	public void getActualSize2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
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
		Dimension desktopSize= getSizeOfMainDesctopPane();
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		double logicalWidth= CoordinateAndSize.reconstruct(canvasSize.width,desktopSize.width,gridX);
		double logicalHeight= CoordinateAndSize.reconstruct(canvasSize.height,desktopSize.height,gridY);
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
				graphicWindow.logicalWidth.set(eWidth);
				graphicWindow.logicalHeight.set(eHeight);
				graphicWindow.logicalX.set(eX);
				graphicWindow.logicalY.set(eY);
				graphicWindow.safelyRestoreSize(staticContext);
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				// redrawCanvas(iX);
				canvasSpace.safelyRepaint();
			}
		}
	}
	public void getActualBounds4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) {
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
		Dimension desktopSize= getSizeOfMainDesctopPane();
		double gridX= DefaultOptions.gridWidth;
		double gridY= DefaultOptions.gridHeight;
		double logicalX= CoordinateAndSize.reconstruct(canvasLocation.x,desktopSize.width,gridX);
		double logicalY= CoordinateAndSize.reconstruct(canvasLocation.y,desktopSize.height,gridY);
		double logicalWidth= CoordinateAndSize.reconstruct(canvasSize.width,desktopSize.width,gridX);
		double logicalHeight= CoordinateAndSize.reconstruct(canvasSize.height,desktopSize.height,gridY);
		a1.setBacktrackableValue(new PrologReal(logicalX),iX);
		a2.setBacktrackableValue(new PrologReal(logicalY),iX);
		a3.setBacktrackableValue(new PrologReal(logicalWidth),iX);
		a4.setBacktrackableValue(new PrologReal(logicalHeight),iX);
	}
	//
	protected Dimension getSizeOfMainDesctopPane() {
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
		// createGraphicWindowIfNecessary(iX,false);
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
	protected void changeBackgroundColor(ExtendedColor eColor, ChoisePoint iX) {
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
		String title= getTitle(iX).getValueOrDefaultText("");
		refreshAttributesOfInternalFrame(graphicWindow,title,iX);
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
		//
		graphicWindow.logicalWidth.set(eWidth);
		graphicWindow.logicalHeight.set(eHeight);
		graphicWindow.logicalX.set(eX);
		graphicWindow.logicalY.set(eY);
		//
		refreshAttributesOfCanvasSpace(iX);
	}
	// protected void refreshAttributesOfInternalFrame(InnerPage graphicWindow, ChoisePoint iX) {
	// }
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
	public void isHidden0s(ChoisePoint iX) throws Backtracking {
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
					canvasSpace= graphicWindow.canvasSpace;
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
		// private Continuation c0;
		public Action1s(Continuation aC, Term actionName) {
			c0= aC;
		}
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
				// ((ExtendedSpace2D)canvasSpace).setCommands(actualCommands);
				// ((ExtendedSpace2D)canvasSpace).setEnableAntialiasing(sceneAntialiasingIsEnabled);
				initiateRegisteredCanvasSpace(canvasSpace,iX);
				enableMouseListeners();
				// reviseMouseListenerStatus();
				// reviseMouseMotionListenerStatus();
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
				// canvasSpace.disableMouseListener();
				// canvasSpace.disableMouseMotionListener();
				// actualCommands.clear();
				// retractedCommands.clear();
				// implementDelayedCleaning= false;
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
				// renewCanvasSpaceAttributes(modalChoisePoint);
				refreshAttributesOfCanvasSpace(modalChoisePoint);
				long domainSignature1= entry_s_Initialize_0();
				callInternalProcedure(domainSignature1,dialogIsModal,modalChoisePoint);
			};
			long domainSignature2= entry_s_Start_0();
			callInternalProcedure(domainSignature2,dialogIsModal,modalChoisePoint);
		}
	}
	// protected void renewCanvasSpaceAttributes(ChoisePoint iX) {
	// }
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
	public void componentHidden(ComponentEvent e) {
		DesktopUtils.selectNextInternalFrame(staticContext);
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentResized(ComponentEvent e) {
	}
	public void componentShown(ComponentEvent e) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void mouseClicked1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseClicked1s extends Continuation {
		// private Continuation c0;
		public MouseClicked1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseEntered1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseEntered1s extends Continuation {
		// private Continuation c0;
		public MouseEntered1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseExited1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseExited1s extends Continuation {
		// private Continuation c0;
		public MouseExited1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mousePressed1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MousePressed1s extends Continuation {
		// private Continuation c0;
		public MousePressed1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseReleased1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseReleased1s extends Continuation {
		// private Continuation c0;
		public MouseReleased1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseDragged1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseDragged1s extends Continuation {
		// private Continuation c0;
		public MouseDragged1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseMoved1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseMoved1s extends Continuation {
		// private Continuation c0;
		public MouseMoved1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
}
