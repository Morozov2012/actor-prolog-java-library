// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.gui.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.awt.Point;
import java.awt.Window;

public abstract class Dialog extends DataResourceConsumer {
	//
	protected AbstractDialog targetObject= null;
	//
	protected DialogIdentifierOrAuto identifier= null;
	protected YesNoDefault isModal= null;
	protected YesNo isTopLevelWindow= null;
	protected YesNo isAlwaysOnTop= null;
	protected OnOff closingConfirmation= null;
	protected YesNoDefault exitOnClose= null;
	//
	public Dialog() {
	}
	public Dialog(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	abstract public Term getBuiltInSlot_E_identifier();
	abstract public Term getBuiltInSlot_E_is_modal();
	abstract public Term getBuiltInSlot_E_is_top_level_window();
	abstract public Term getBuiltInSlot_E_is_always_on_top();
	abstract public Term getBuiltInSlot_E_closing_confirmation();
	abstract public Term getBuiltInSlot_E_exit_on_close();
	//
	abstract public long entry_s_Action_1_i();
	abstract public long entry_s_WindowClosing_0();
	abstract public long entry_s_WindowClosed_0();
	abstract public long entry_s_CreatedControl_1_i();
	abstract public long entry_s_ModifiedControl_1_i();
	abstract public long entry_s_CompleteEditing_1_i();
	abstract public long entry_f_DataRequest_1_i();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set identifier
	//
	public void setIdentifier1s(ChoisePoint iX, Term a1) {
		setIdentifier(DialogIdentifierOrAuto.argumentToDialogIdentifierOrAuto(a1,iX));
	}
	public void setIdentifier(DialogIdentifierOrAuto value) {
		identifier= value;
	}
	public void getIdentifier0ff(ChoisePoint iX, PrologVariable result) {
		DialogIdentifierOrAuto value= getIdentifier(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getIdentifier0fs(ChoisePoint iX) {
	}
	public DialogIdentifierOrAuto getIdentifier(ChoisePoint iX) {
		if (identifier != null) {
			return identifier;
		} else {
			Term value= getBuiltInSlot_E_identifier();
			return DialogIdentifierOrAuto.argumentToDialogIdentifierOrAuto(value,iX);
		}
	}
	//
	// get/set isModal
	//
	public void setIsModal1s(ChoisePoint iX, Term a1) {
		setIsModal(YesNoDefaultConverters.argument2YesNoDefault(a1,iX));
	}
	public void setIsModal(YesNoDefault value) {
		isModal= value;
	}
	public void getIsModal0ff(ChoisePoint iX, PrologVariable result) {
		YesNoDefault value= getIsModal(iX);
		result.setNonBacktrackableValue(YesNoDefaultConverters.toTerm(value));
	}
	public void getIsModal0fs(ChoisePoint iX) {
	}
	public YesNoDefault getIsModal(ChoisePoint iX) {
		if (isModal != null) {
			return isModal;
		} else {
			Term value= getBuiltInSlot_E_is_modal();
			return YesNoDefaultConverters.argument2YesNoDefault(value,iX);
		}
	}
	//
	// get/set isTopLevelWindow
	//
	public void setIsTopLevelWindow1s(ChoisePoint iX, Term a1) {
		setIsTopLevelWindow(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setIsTopLevelWindow(YesNo value) {
		isTopLevelWindow= value;
	}
	public void getIsTopLevelWindow0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getIsTopLevelWindow(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getIsTopLevelWindow0fs(ChoisePoint iX) {
	}
	public YesNo getIsTopLevelWindow(ChoisePoint iX) {
		if (isTopLevelWindow != null) {
			return isTopLevelWindow;
		} else {
			Term value= getBuiltInSlot_E_is_top_level_window();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set isAlwaysOnTop
	//
	public void setIsAlwaysOnTop1s(ChoisePoint iX, Term a1) {
		setIsAlwaysOnTop(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setIsAlwaysOnTop(YesNo value) {
		isAlwaysOnTop= value;
	}
	public void getIsAlwaysOnTop0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getIsAlwaysOnTop(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getIsAlwaysOnTop0fs(ChoisePoint iX) {
	}
	public YesNo getIsAlwaysOnTop(ChoisePoint iX) {
		if (isAlwaysOnTop != null) {
			return isAlwaysOnTop;
		} else {
			Term value= getBuiltInSlot_E_is_always_on_top();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set closingConfirmation
	//
	public void setClosingConfirmation1s(ChoisePoint iX, Term a1) {
		setClosingConfirmation(OnOffConverters.argument2OnOff(a1,iX));
	}
	public void setClosingConfirmation(OnOff value) {
		closingConfirmation= value;
	}
	public void getClosingConfirmation0ff(ChoisePoint iX, PrologVariable result) {
		OnOff value= getClosingConfirmation(iX);
		result.setNonBacktrackableValue(OnOffConverters.toTerm(value));
	}
	public void getClosingConfirmation0fs(ChoisePoint iX) {
	}
	public OnOff getClosingConfirmation(ChoisePoint iX) {
		if (closingConfirmation != null) {
			return closingConfirmation;
		} else {
			Term value= getBuiltInSlot_E_closing_confirmation();
			return OnOffConverters.argument2OnOff(value,iX);
		}
	}
	//
	// get/set exitOnClose
	//
	public void setExitOnClose1s(ChoisePoint iX, Term a1) {
		setExitOnClose(YesNoDefaultConverters.argument2YesNoDefault(a1,iX));
	}
	public void setExitOnClose(YesNoDefault value) {
		exitOnClose= value;
	}
	public void getExitOnClose0ff(ChoisePoint iX, PrologVariable result) {
		YesNoDefault value= getExitOnClose(iX);
		result.setNonBacktrackableValue(YesNoDefaultConverters.toTerm(value));
	}
	public void getExitOnClose0fs(ChoisePoint iX) {
	}
	public YesNoDefault getExitOnClose(ChoisePoint iX) {
		if (exitOnClose != null) {
			return exitOnClose;
		} else {
			Term value= getBuiltInSlot_E_exit_on_close();
			return YesNoDefaultConverters.argument2YesNoDefault(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void releaseSystemResources() {
		if (targetObject != null) {
			targetObject.safelyDispose();
		};
		super.releaseSystemResources();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setActualPosition2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedCoordinate eX= ExtendedCoordinate.argumentToExtendedCoordinate(a1,iX);
		ExtendedCoordinate eY= ExtendedCoordinate.argumentToExtendedCoordinate(a2,iX);
		boolean pixelMeasurements= getUsePixelMeasurements(iX);
		prepareDialogIfNecessary(false,iX);
		if (targetObject != null) {
			targetObject.changeActualPosition(eX,eY,pixelMeasurements,iX);
		}
	}
	@Override
	public void getActualPosition2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		double givenX= 0.0;
		double givenY= 0.0;
		if (targetObject != null) {
			ExtendedCoordinates coordinates= targetObject.getActualPosition(getUsePixelMeasurements(iX));
			try {
				givenX= coordinates.getX().getDoubleValue();
			} catch (UseDefaultLocation e) {
			} catch (CentreFigure e) {
			};
			try {
				givenY= coordinates.getY().getDoubleValue();
			} catch (UseDefaultLocation e) {
			} catch (CentreFigure e) {
			};
		};
		a1.setBacktrackableValue(new PrologReal(givenX),iX);
		a2.setBacktrackableValue(new PrologReal(givenY),iX);
	}
	//
	@Override
	public void getPositionInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		int givenX= 0;
		int givenY= 0;
		if (targetObject != null) {
			Point position= targetObject.safelyGetLocation();
			givenX= position.x;
			givenY= position.y;
		};
		a1.setBacktrackableValue(new PrologInteger(givenX),iX);
		a2.setBacktrackableValue(new PrologInteger(givenY),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void changeTitle(ExtendedTitle title, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeTitle(title);
		}
	}
	//
	@Override
	protected void changeTextColor(ChoisePoint iX) {
		changeTextColor(getTextColor(iX),iX);
	}
	protected void changeTextColor(ColorAttribute color, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeTextColor(color,true,iX);
		}
	}
	//
	@Override
	protected void changeSpaceColor(ChoisePoint iX) {
		changeSpaceColor(getSpaceColor(iX),iX);
	}
	protected void changeSpaceColor(ColorAttribute color, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeSpaceColor(color,true,iX);
		}
	}
	//
	@Override
	protected void changeBackgroundColor(ColorAttribute color, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeBackgroundColor(color,true,iX);
		}
	}
	//
	@Override
	protected void changeFontName(ChoisePoint iX) {
		changeFontName(getFontName(iX),iX);
	}
	protected void changeFontName(ExtendedFontName eName, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeFontName(eName,iX);
		}
	}
	//
	@Override
	protected void changeFontSize(ChoisePoint iX) {
		changeFontSize(getFontSize(iX),iX);
	}
	protected void changeFontSize(ExtendedFontSize eSize, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeFontSize(eSize,iX);
		}
	}
	//
	@Override
	protected void changeFontStyle(ChoisePoint iX) {
		changeFontStyle(getFontStyle(iX),iX);
	}
	protected void changeFontStyle(ExtendedFontStyle eStyle, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeFontStyle(eStyle,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void show2s(ChoisePoint iX, Term a1, Term a2) {
		prepareDialogIfNecessary(true,DialogInitialState.RESTORED,iX);
	}
	@Override
	public void show0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,DialogInitialState.RESTORED,iX);
	}
	@Override
	public void show1ms(ChoisePoint iX, Term... args) {
		prepareDialogIfNecessary(true,DialogInitialState.RESTORED,iX);
	}
	//
	public void activate0s(ChoisePoint iX) {
		prepareDialogIfNecessary(false,iX);
	}
	//
	@Override
	public void redraw0s(ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.safelyValidate();
		}
	}
	//
	@Override
	public void hide0s(ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.safelyHide();
		}
	}
	//
	@Override
	public void maximize0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,DialogInitialState.MAXIMIZED,iX);
		if (targetObject != null) {
			targetObject.safelyMaximize();
		}
	}
	//
	@Override
	public void minimize0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,DialogInitialState.MINIMIZED,iX);
		if (targetObject != null) {
			targetObject.safelyMinimize();
		}
	}
	//
	@Override
	public void restore0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,DialogInitialState.RESTORED,iX);
		if (targetObject != null) {
			targetObject.safelyRestore();
		}
	}
	//
	@Override
	public void isVisible0s(ChoisePoint iX) throws Backtracking {
		if (targetObject != null) {
			if (!targetObject.safelyIsVisible()) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	@Override
	public void isHidden0s(ChoisePoint iX) throws Backtracking {
		if (targetObject != null) {
			if (!targetObject.safelyIsHidden()) {
				throw Backtracking.instance;
			}
		}
	}
	//
	@Override
	public void isMaximized0s(ChoisePoint iX) throws Backtracking {
		if (targetObject != null) {
			if (!targetObject.safelyIsMaximized()) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	@Override
	public void isMinimized0s(ChoisePoint iX) throws Backtracking {
		if (targetObject != null) {
			if (!targetObject.safelyIsMinimized()) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	@Override
	public void isRestored0s(ChoisePoint iX) throws Backtracking {
		if (targetObject != null) {
			if (!targetObject.safelyIsRestored()) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void put2s(ChoisePoint iX, Term fieldName, Term fieldValue) {
		putValue(fieldName,fieldValue,iX);
	}
	//
	protected void putValue(Term fieldName, Term fieldValue, ChoisePoint iX) {
		try {
			long code= fieldName.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_identifier) {
				DialogIdentifierOrAuto value= DialogIdentifierOrAuto.argumentToDialogIdentifierOrAuto(fieldValue,iX);
				setIdentifier(value);
			} else if (code==SymbolCodes.symbolCode_E_is_modal) {
				YesNoDefault value= YesNoDefaultConverters.argument2YesNoDefault(fieldValue,iX);
				setIsModal(value);
			} else if (code==SymbolCodes.symbolCode_E_is_top_level_window) {
				YesNo value= YesNoConverters.argument2YesNo(fieldValue,iX);
				setIsTopLevelWindow(value);
			} else if (code==SymbolCodes.symbolCode_E_is_always_on_top) {
				YesNo value= YesNoConverters.argument2YesNo(fieldValue,iX);
				setIsAlwaysOnTop(value);
			} else if (code==SymbolCodes.symbolCode_E_closing_confirmation) {
				OnOff value= OnOffConverters.argument2OnOff(fieldValue,iX);
				setClosingConfirmation(value);
			} else if (code==SymbolCodes.symbolCode_E_exit_on_close) {
				YesNoDefault value= YesNoDefaultConverters.argument2YesNoDefault(fieldValue,iX);
				setExitOnClose(value);
			} else {
				prepareDialogIfNecessary(false,iX);
				targetObject.putFieldValue(DialogControlOperation.VALUE,fieldName,fieldValue,iX);
			}
		} catch (TermIsNotASymbol e) {
			prepareDialogIfNecessary(false,iX);
			targetObject.putFieldValue(DialogControlOperation.VALUE,fieldName,fieldValue,iX);
		}
	}
	//
	public void get1ff(ChoisePoint iX, PrologVariable result, Term fieldName) {
		result.setNonBacktrackableValue(getValue(fieldName,iX));
	}
	public void get1fs(ChoisePoint iX, Term fieldName) {
		getValue(fieldName,iX);
	}
	//
	protected Term getValue(Term fieldName, ChoisePoint iX) {
		try {
			long code= fieldName.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_identifier) {
				return getIdentifier(iX).toTerm();
			} else if (code==SymbolCodes.symbolCode_E_is_modal) {
				return YesNoDefaultConverters.toTerm(getIsModal(iX));
			} else if (code==SymbolCodes.symbolCode_E_is_top_level_window) {
				return YesNoConverters.toTerm(getIsTopLevelWindow(iX));
			} else if (code==SymbolCodes.symbolCode_E_is_always_on_top) {
				return YesNoConverters.toTerm(getIsAlwaysOnTop(iX));
			} else if (code==SymbolCodes.symbolCode_E_closing_confirmation) {
				return OnOffConverters.toTerm(getClosingConfirmation(iX));
			} else if (code==SymbolCodes.symbolCode_E_exit_on_close) {
				return YesNoDefaultConverters.toTerm(getExitOnClose(iX));
			} else {
				prepareDialogIfNecessary(false,iX);
				return targetObject.getFieldValue(DialogControlOperation.VALUE,fieldName,iX);
			}
		} catch (TermIsNotASymbol e) {
			prepareDialogIfNecessary(false,iX);
			return targetObject.getFieldValue(DialogControlOperation.VALUE,fieldName,iX);
		}
	}
	//
	public void enable1s(ChoisePoint iX, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
		targetObject.setFieldIsEnabled(fieldName,true,iX);
	}
	//
	public void disable1s(ChoisePoint iX, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
		targetObject.setFieldIsEnabled(fieldName,false,iX);
	}
	//
	public void isEnabled1s(ChoisePoint iX, Term fieldName) throws Backtracking {
		prepareDialogIfNecessary(false,iX);
		targetObject.checkIfFieldIsEnabled(fieldName,true,iX);
	}
	//
	public void isDisabled1s(ChoisePoint iX, Term fieldName) throws Backtracking {
		prepareDialogIfNecessary(false,iX);
		targetObject.checkIfFieldIsEnabled(fieldName,false,iX);
	}
	//
	public void setControlText2s(ChoisePoint iX, Term fieldName, Term fieldValue) {
		prepareDialogIfNecessary(false,iX);
		targetObject.putFieldValue(DialogControlOperation.TEXT,fieldName,fieldValue,iX);
	}
	//
	public void getControlText1ff(ChoisePoint iX, PrologVariable result, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
		result.setNonBacktrackableValue(targetObject.getFieldValue(DialogControlOperation.TEXT,fieldName,iX));
	}
	public void getControlText1fs(ChoisePoint iX, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
	}
	//
	public void setControlTextColor2s(ChoisePoint iX, Term fieldName, Term fieldValue) {
		prepareDialogIfNecessary(false,iX);
		targetObject.putFieldValue(DialogControlOperation.TEXT_COLOR,fieldName,fieldValue,iX);
	}
	//
	public void getControlTextColor1ff(ChoisePoint iX, PrologVariable result, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
		result.setNonBacktrackableValue(targetObject.getFieldValue(DialogControlOperation.TEXT_COLOR,fieldName,iX));
	}
	public void getControlTextColor1fs(ChoisePoint iX, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
	}
	//
	public void setControlSpaceColor2s(ChoisePoint iX, Term fieldName, Term fieldValue) {
		prepareDialogIfNecessary(false,iX);
		targetObject.putFieldValue(DialogControlOperation.SPACE_COLOR,fieldName,fieldValue,iX);
	}
	//
	public void getControlSpaceColor1ff(ChoisePoint iX, PrologVariable result, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
		result.setNonBacktrackableValue(targetObject.getFieldValue(DialogControlOperation.SPACE_COLOR,fieldName,iX));
	}
	public void getControlSpaceColor1fs(ChoisePoint iX, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
	}
	//
	public void setControlBackgroundColor2s(ChoisePoint iX, Term fieldName, Term fieldValue) {
		prepareDialogIfNecessary(false,iX);
		targetObject.putFieldValue(DialogControlOperation.BACKGROUND_COLOR,fieldName,fieldValue,iX);
	}
	//
	public void getControlBackgroundColor1ff(ChoisePoint iX, PrologVariable result, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
		result.setNonBacktrackableValue(targetObject.getFieldValue(DialogControlOperation.BACKGROUND_COLOR,fieldName,iX));
	}
	public void getControlBackgroundColor1fs(ChoisePoint iX, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
	}
	//
	@Override
	public void action1s(ChoisePoint iX, Term actionName) {
	}
	//
	@Override
	public void windowClosing0s(ChoisePoint iX) {
	}
	//
	@Override
	public void windowClosed0s(ChoisePoint iX) {
	}
	//
	public void createdControl1s(ChoisePoint iX, Term actionName) {
	}
	//
	public void modifiedControl1s(ChoisePoint iX, Term actionName) {
	}
	//
	public void completeEditing1s(ChoisePoint iX, Term actionName) {
	}
	//
	public class Action1s extends Continuation {
		//
		public Action1s(Continuation aC) {
			c0= aC;
		}
		//
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	protected void prepareDialogIfNecessary(boolean showDialog, ChoisePoint iX) {
		prepareDialogIfNecessary(showDialog,DialogInitialState.RESTORED,iX);
	}
	protected void prepareDialogIfNecessary(boolean showDialog, DialogInitialState initialState, ChoisePoint iX) {
		if (targetObject==null) {
			YesNoDefault isModalDialog= getIsModal(iX);
			boolean isTopLevelDialog= getIsTopLevelWindow(iX).toBoolean();
			boolean isAlwaysOnTopDialog= getIsAlwaysOnTop(iX).toBoolean();
			boolean confirmationOnDialogClose= getClosingConfirmation(iX).toBoolean();
			YesNoDefault exitProgramOnClose= getExitOnClose(iX);
			Window mainWindow= null;
			if (!isTopLevelDialog) {
				mainWindow= StaticDesktopAttributes.retrieveTopLevelWindow(staticContext);
				if (mainWindow==null) {
					DesktopUtils.createPaneIfNecessary(staticContext);
					mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
				};
				DesktopUtils.safelySetVisible(true,mainWindow);
			};
			targetObject= createDialog(
				isModalDialog,
				isTopLevelDialog,
				isAlwaysOnTopDialog,
				confirmationOnDialogClose,
				exitProgramOnClose,
				mainWindow,
				iX);
			targetObject.initiate(currentProcess,staticContext);
			targetObject.increaseDepthCounter();
			targetObject.prepare(
				this,
				getTitle(iX),
				getTextColor(iX),
				getSpaceColor(iX),
				getFontName(iX),
				getFontSize(iX),
				getFontStyle(iX),
				getX(iX),
				getY(iX),
				getBackgroundColor(iX),
				getUsePixelMeasurements(iX),
				iX);
			targetObject.registerSlotVariables();
			if (showDialog) {
				targetObject.safelyAddAndDisplay(initialState,iX,staticContext);
			} else {
				targetObject.safelyPositionMainPanel(); // 2014.08.04
			};
			targetObject.decreaseDepthCounter();
			targetObject.receiveInitiatingMessage();
		} else if (showDialog) {
			targetObject.increaseDepthCounter();
			targetObject.safelyAcceptNewPositionOfDialog(); // 2014.08.04
			targetObject.safelyDisplay(initialState,iX,staticContext);
			targetObject.decreaseDepthCounter();
		}
	}
	//
	protected AbstractDialog createDialog(YesNoDefault isModalDialog, boolean isTopLevelDialog, boolean isAlwaysOnTopDialog, boolean confirmationOnDialogClose, YesNoDefault exitProgramOnClose, Window window, ChoisePoint iX) {
		DialogIdentifierOrAuto id= getIdentifier(iX);
		if (id.isAutomatic()) {
			return createAutomaticDialog(isModalDialog,isTopLevelDialog,isAlwaysOnTopDialog,confirmationOnDialogClose,exitProgramOnClose,window);
		} else {
			return createNamedDialog(id.getName(),isModalDialog,isTopLevelDialog,isAlwaysOnTopDialog,confirmationOnDialogClose,exitProgramOnClose,window);
		}
	}
	protected AbstractDialog createNamedDialog(String name, YesNoDefault isModalDialog, boolean isTopLevelDialog, boolean isAlwaysOnTopDialog, boolean confirmationOnDialogClose, YesNoDefault exitProgramOnClose, Window window) {
		try {
			return DialogTable.createDialog(name,isModalDialog,isTopLevelDialog,isAlwaysOnTopDialog,confirmationOnDialogClose,exitProgramOnClose,window);
		} catch (UndefinedDialogTableEntry e) {
			throw new ClassNotFound(name);
		}
	}
	protected AbstractDialog createAutomaticDialog(YesNoDefault isModalDialog, boolean isTopLevelDialog, boolean isAlwaysOnTopDialog, boolean confirmationOnDialogClose, YesNoDefault exitProgramOnClose, Window window) {
		long packageNumber= getPackageCode();
		long[] classList= getClassHierarchy();
		for (int i= 0; i < classList.length; i++) {
			long classCode= classList[i];
			String givenName= String.format("d%02d_%04d",
				packageNumber,
				classCode);
			try {
				return DialogTable.createDialog(givenName,isModalDialog,isTopLevelDialog,isAlwaysOnTopDialog,confirmationOnDialogClose,exitProgramOnClose,window);
			} catch (UndefinedDialogTableEntry e2) {
			}
		};
		throw new DialogNotFound();
	}
	//
	public class DataRequest1ff extends DataRequest {
		public DataRequest1ff(Continuation aC, PrologVariable result, Term a1) {
			super(aC,a1);
			argumentResult= result;
			isFunctionCall= true;
		}
	}
	public class DataRequest1fs extends DataRequest {
		public DataRequest1fs(Continuation aC, Term a1) {
			super(aC,a1);
			isFunctionCall= false;
		}
	}
	public class DataRequest extends Continuation {
		protected PrologVariable argumentResult;
		protected Term tableName;
		protected boolean isFunctionCall;
		//
		public DataRequest(Continuation aC, Term aP) {
			c0= aC;
			tableName= aP;
		}
		//
		@Override
		public void execute(ChoisePoint iX) throws Backtracking {
			throw Backtracking.instance;
		}
	}
}
