// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
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
	protected YesNoDefault exitOnClose= null;
	//
	public Dialog() {
	}
	public Dialog(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	abstract protected Term getBuiltInSlot_E_identifier();
	abstract protected Term getBuiltInSlot_E_is_modal();
	abstract protected Term getBuiltInSlot_E_is_top_level_window();
	abstract protected Term getBuiltInSlot_E_exit_on_close();
	// abstract protected Term getBuiltInSlot_E_title();
	// abstract protected Term getBuiltInSlot_E_text_color();
	// abstract protected Term getBuiltInSlot_E_space_color();
	// abstract protected Term getBuiltInSlot_E_font_name();
	// abstract protected Term getBuiltInSlot_E_font_size();
	// abstract protected Term getBuiltInSlot_E_font_style();
	// abstract protected Term getBuiltInSlot_E_x();
	// abstract protected Term getBuiltInSlot_E_y();
	// abstract protected Term getBuiltInSlot_E_background_color();
	//
	abstract public long entry_s_Action_1_i();
	abstract public long entry_s_CreatedControl_1_i();
	abstract public long entry_s_ModifiedControl_1_i();
	abstract public long entry_f_DataRequest_1_i();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set identifier
	//
	public void setIdentifier1s(ChoisePoint iX, Term a1) {
		setIdentifier(DialogIdentifierOrAuto.termToDialogIdentifierOrAuto(a1,iX));
	}
	public void setIdentifier(DialogIdentifierOrAuto value) {
		identifier= value;
	}
	public void getIdentifier0ff(ChoisePoint iX, PrologVariable a1) {
		DialogIdentifierOrAuto value= getIdentifier(iX);
		a1.value= value.toTerm();
	}
	public void getIdentifier0fs(ChoisePoint iX) {
	}
	public DialogIdentifierOrAuto getIdentifier(ChoisePoint iX) {
		if (identifier != null) {
			return identifier;
		} else {
			Term value= getBuiltInSlot_E_identifier();
			return DialogIdentifierOrAuto.termToDialogIdentifierOrAuto(value,iX);
		}
	}
	//
	// get/set isModal
	//
	public void setIsModal1s(ChoisePoint iX, Term a1) {
		setIsModal(YesNoDefault.term2YesNoDefault(a1,iX));
	}
	public void setIsModal(YesNoDefault value) {
		isModal= value;
	}
	public void getIsModal0ff(ChoisePoint iX, PrologVariable a1) {
		YesNoDefault value= getIsModal(iX);
		a1.value= value.toTerm();
	}
	public void getIsModal0fs(ChoisePoint iX) {
	}
	public YesNoDefault getIsModal(ChoisePoint iX) {
		if (isModal != null) {
			return isModal;
		} else {
			Term value= getBuiltInSlot_E_is_modal();
			return YesNoDefault.term2YesNoDefault(value,iX);
		}
	}
	//
	// get/set isTopLevelWindow
	//
	public void setIsTopLevelWindow1s(ChoisePoint iX, Term a1) {
		setIsTopLevelWindow(YesNo.term2YesNo(a1,iX));
	}
	public void setIsTopLevelWindow(YesNo value) {
		isTopLevelWindow= value;
	}
	public void getIsTopLevelWindow0ff(ChoisePoint iX, PrologVariable a1) {
		YesNo value= getIsTopLevelWindow(iX);
		a1.value= value.toTerm();
	}
	public void getIsTopLevelWindow0fs(ChoisePoint iX) {
	}
	public YesNo getIsTopLevelWindow(ChoisePoint iX) {
		if (isTopLevelWindow != null) {
			return isTopLevelWindow;
		} else {
			Term value= getBuiltInSlot_E_is_top_level_window();
			return YesNo.term2YesNo(value,iX);
		}
	}
	//
	// get/set exitOnClose
	//
	public void setExitOnClose1s(ChoisePoint iX, Term a1) {
		setExitOnClose(YesNoDefault.term2YesNoDefault(a1,iX));
	}
	public void setExitOnClose(YesNoDefault value) {
		exitOnClose= value;
	}
	public void getExitOnClose0ff(ChoisePoint iX, PrologVariable a1) {
		YesNoDefault value= getExitOnClose(iX);
		a1.value= value.toTerm();
	}
	public void getExitOnClose0fs(ChoisePoint iX) {
	}
	public YesNoDefault getExitOnClose(ChoisePoint iX) {
		if (exitOnClose != null) {
			return exitOnClose;
		} else {
			Term value= getBuiltInSlot_E_exit_on_close();
			return YesNoDefault.term2YesNoDefault(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void closeFiles() {
		if (targetObject != null) {
			targetObject.safelyDispose();
		};
		super.closeFiles();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setActualPosition2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedCoordinate eX= ExtendedCoordinate.termToExtendedCoordinate(a1,iX);
		ExtendedCoordinate eY= ExtendedCoordinate.termToExtendedCoordinate(a2,iX);
		// createGraphicWindowIfNecessary(iX,false);
		prepareDialogIfNecessary(false,iX);
		if (targetObject != null) {
			targetObject.changeActualPosition(eX,eY,iX);
		}
	}
	public void getActualPosition2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		double x= 0.0;
		double y= 0.0;
		if (targetObject != null) {
			ExtendedCoordinates coordinates= targetObject.getActualPosition();
			try {
				x= coordinates.x.getDoubleValue();
			} catch (UseDefaultLocation e) {
			} catch (CentreFigure e) {
			};
			try {
				y= coordinates.y.getDoubleValue();
			} catch (UseDefaultLocation e) {
			} catch (CentreFigure e) {
			};
		};
		a1.value= new PrologReal(x);
		a2.value= new PrologReal(y);
		iX.pushTrail(a1);
		iX.pushTrail(a2);
	}
	//
	public void getPositionInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		int x= 0;
		int y= 0;
		if (targetObject != null) {
			Point position= targetObject.safelyGetLocation();
			x= position.x;
			y= position.y;
		};
		a1.value= new PrologInteger(x);
		a2.value= new PrologInteger(y);
		iX.pushTrail(a1);
		iX.pushTrail(a2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void changeTitle(ExtendedTitle title, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeTitle(title);
		}
	}
	//
	protected void changeTextColor(ChoisePoint iX) {
		changeTextColor(getTextColor(iX),iX);
	}
	protected void changeTextColor(ExtendedColor color, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeTextColor(color,true,iX);
		}
	}
	//
	protected void changeSpaceColor(ChoisePoint iX) {
		changeSpaceColor(getSpaceColor(iX),iX);
	}
	protected void changeSpaceColor(ExtendedColor color, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeSpaceColor(color,true,iX);
		}
	}
	//
	protected void changeBackgroundColor(ExtendedColor color, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeBackgroundColor(color,true,iX);
		}
	}
	//
	protected void changeFontName(ChoisePoint iX) {
		changeFontName(getFontName(iX),iX);
	}
	protected void changeFontName(ExtendedFontName eName, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeFontName(eName,iX);
		}
	}
	//
	protected void changeFontSize(ChoisePoint iX) {
		changeFontSize(getFontSize(iX),iX);
	}
	protected void changeFontSize(ExtendedFontSize eSize, ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.changeFontSize(eSize,iX);
		}
	}
	//
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
	public void show2s(ChoisePoint iX, Term a1, Term a2) {
		prepareDialogIfNecessary(true,iX);
	}
	public void show0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,iX);
	}
	public void show1ms(ChoisePoint iX, Term... args) {
		prepareDialogIfNecessary(true,iX);
	}
	//
	public void activate0s(ChoisePoint iX) {
		prepareDialogIfNecessary(false,iX);
	}
	//
	public void redraw0s(ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.safelyValidate();
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		if (targetObject != null) {
			targetObject.safelyHide();
		}
	}
	//
	public void maximize0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,iX);
		if (targetObject != null) {
			targetObject.safelyMaximize();
		}
	}
	//
	public void minimize0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,iX);
		if (targetObject != null) {
			targetObject.safelyMinimize();
		}
	}
	//
	public void restore0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,iX);
		if (targetObject != null) {
			targetObject.safelyRestore();
		}
	}
	//
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
	public void isHidden0s(ChoisePoint iX) throws Backtracking {
		if (targetObject != null) {
			if (!targetObject.safelyIsHidden()) {
				throw Backtracking.instance;
			}
		}
	}
	//
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
				DialogIdentifierOrAuto value= DialogIdentifierOrAuto.termToDialogIdentifierOrAuto(fieldValue,iX);
				setIdentifier(value);
			} else if (code==SymbolCodes.symbolCode_E_is_modal) {
				YesNoDefault value= YesNoDefault.term2YesNoDefault(fieldValue,iX);
				setIsModal(value);
			} else if (code==SymbolCodes.symbolCode_E_is_top_level_window) {
				YesNo value= YesNo.term2YesNo(fieldValue,iX);
				setIsTopLevelWindow(value);
			} else if (code==SymbolCodes.symbolCode_E_exit_on_close) {
				YesNoDefault value= YesNoDefault.term2YesNoDefault(fieldValue,iX);
				setExitOnClose(value);
			} else {
				prepareDialogIfNecessary(false,iX);
				targetObject.putFieldValue(fieldName,fieldValue,iX);
			}
		} catch (TermIsNotASymbol e) {
			prepareDialogIfNecessary(false,iX);
			targetObject.putFieldValue(fieldName,fieldValue,iX);
		}
	}
	//
	public void get1ff(ChoisePoint iX, PrologVariable fieldValue, Term fieldName) {
		fieldValue.value= getValue(fieldName,iX);
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
				return getIsModal(iX).toTerm();
			} else if (code==SymbolCodes.symbolCode_E_is_top_level_window) {
				return getIsTopLevelWindow(iX).toTerm();
			} else if (code==SymbolCodes.symbolCode_E_exit_on_close) {
				return getExitOnClose(iX).toTerm();
			} else {
				prepareDialogIfNecessary(false,iX);
				return targetObject.getFieldValue(fieldName,iX);
			}
		} catch (TermIsNotASymbol e) {
			prepareDialogIfNecessary(false,iX);
			return targetObject.getFieldValue(fieldName,iX);
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
	public void action1s(ChoisePoint iX, Term actionName) {
	}
	//
	public void createdControl1s(ChoisePoint iX, Term actionName) {
	}
	//
	public void modifiedControl1s(ChoisePoint iX, Term actionName) {
	}
	//
	public class Action1s extends Continuation {
		// private Continuation c0;
		//
		public Action1s(Continuation aC) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	protected void prepareDialogIfNecessary(boolean showDialog, ChoisePoint iX) {
		if (targetObject==null) {
			YesNoDefault isModalDialog= getIsModal(iX);
			boolean isTopLevelDialog= getIsTopLevelWindow(iX).toBoolean();
			YesNoDefault exitProgramOnClose= getExitOnClose(iX);
			if (!isTopLevelDialog) {
				DesktopUtils.createPaneIfNecessary(staticContext);
			};
			Window mainWindow= null;
			if (!isTopLevelDialog) {
				mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
				DesktopUtils.safelySetVisible(true,mainWindow);
			};
			targetObject= createDialog(isModalDialog,isTopLevelDialog,exitProgramOnClose,mainWindow,iX);
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
				iX);
			targetObject.registerSlotVariables();
			if (showDialog) {
				MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
				targetObject.safelyAddAndDisplay(desktop,iX);
			} else {
				targetObject.safelyPositionMainPanel(); // 2014.08.04
			};
			targetObject.decreaseDepthCounter();
			targetObject.receiveInitiatingMessage();
		} else if (showDialog) {
			MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
			targetObject.increaseDepthCounter();
			targetObject.safelyAcceptNewPositionOfDialog(); // 2014.08.04
			targetObject.safelyDisplay(desktop,iX);
			targetObject.decreaseDepthCounter();
		}
	}
	//
	protected AbstractDialog createDialog(YesNoDefault isModalDialog, boolean isTopLevelDialog, YesNoDefault exitProgramOnClose, Window window, ChoisePoint iX) {
		DialogIdentifierOrAuto id= getIdentifier(iX);
		if (id.isAutomatic) {
			return createAutomaticDialog(isModalDialog,isTopLevelDialog,exitProgramOnClose,window);
		} else {
			return createNamedDialog(id.name,isModalDialog,isTopLevelDialog,exitProgramOnClose,window);
		}
	}
	protected AbstractDialog createNamedDialog(String name, YesNoDefault isModalDialog, boolean isTopLevelDialog, YesNoDefault exitProgramOnClose, Window window) {
		try {
			return DialogTable.createDialog(name,isModalDialog,isTopLevelDialog,exitProgramOnClose,window);
		} catch (UndefinedDialogTableEntry e) {
			throw new ClassNotFound(name);
		}
	}
	protected AbstractDialog createAutomaticDialog(YesNoDefault isModalDialog, boolean isTopLevelDialog, YesNoDefault exitProgramOnClose, Window window) {
		long packageNumber= getPackageCode();
		long[] classList= getClassHierarchy();
		for (int i= 0; i < classList.length; i++) {
			long classCode= classList[i];
			String name= String.format("d%02d_%04d",
				packageNumber,
				classCode);
			try {
				return DialogTable.createDialog(name,isModalDialog,isTopLevelDialog,exitProgramOnClose,window);
			} catch (UndefinedDialogTableEntry e2) {
			}
		};
		throw new DialogNotFound();
	}
	//
	public class DataRequest1ff extends DataRequest {
		public DataRequest1ff(Continuation aC, PrologVariable a1, Term a2) {
			super(aC,a2);
			result= a1;
			isFunctionCall= true;
		}
	}
	public class DataRequest1fs extends DataRequest {
		public DataRequest1fs(Continuation aC, Term a2) {
			super(aC,a2);
			isFunctionCall= false;
		}
	}
	public class DataRequest extends Continuation {
		// private Continuation c0;
		protected PrologVariable result;
		protected Term tableName;
		protected boolean isFunctionCall;
		//
		public DataRequest(Continuation aC, Term aP) {
			c0= aC;
			tableName= aP;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			throw Backtracking.instance;
			// c0.execute(iX);
		}
	}
}
