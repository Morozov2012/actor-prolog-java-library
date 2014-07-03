// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.gui.dialogs.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.GraphicsConfiguration;
import java.awt.Window;

public abstract class Dialog extends ImageConsumer {
	//
	protected AbstractDialog targetObject= null;
	//
	abstract protected Term getBuiltInSlot_E_identifier();
	// abstract protected Term getBuiltInSlot_E_sensitiveness();
	abstract protected Term getBuiltInSlot_E_is_top_level_window();
	abstract protected Term getBuiltInSlot_E_title();
	abstract protected Term getBuiltInSlot_E_text_color();
	abstract protected Term getBuiltInSlot_E_space_color();
	abstract protected Term getBuiltInSlot_E_font_name();
	abstract protected Term getBuiltInSlot_E_font_size();
	abstract protected Term getBuiltInSlot_E_font_style();
	abstract protected Term getBuiltInSlot_E_x();
	abstract protected Term getBuiltInSlot_E_y();
	abstract protected Term getBuiltInSlot_E_background_color();
	//
	abstract public long entry_s_Action_1_i();
	abstract public long entry_s_CreatedControl_1_i();
	abstract public long entry_s_ModifiedControl_1_i();
	abstract public long entry_f_DataRequest_1_i();
	//
	public Dialog() {
	}
	//
	public void closeFiles() {
		if (targetObject != null) {
			targetObject.safelyDispose();
		};
		super.closeFiles();
	}
	//
	public void show2s(ChoisePoint iX, Term a1, Term a2) {
		prepareDialogIfNecessary(true,iX);
	}
	public void show0s(ChoisePoint iX) {
		prepareDialogIfNecessary(true,iX);
	}
	//
	public void activate0s(ChoisePoint iX) {
		prepareDialogIfNecessary(false,iX);
	}
	//
	public void redraw0s(ChoisePoint iX) {
		if (targetObject != null) {
			// targetObject.validate();
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
		// } else {
		//	throw Backtracking.instance;
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
		prepareDialogIfNecessary(false,iX);
		targetObject.putFieldValue(fieldName,fieldValue,iX);
	}
	//
	public void get1ff(ChoisePoint iX, PrologVariable fieldValue, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
		fieldValue.value= targetObject.getFieldValue(fieldName,iX);
		// iX.pushTrail(fieldValue);
	}
	public void get1fs(ChoisePoint iX, Term fieldName) {
		prepareDialogIfNecessary(false,iX);
		targetObject.getFieldValue(fieldName,iX);
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
	public void changeBackgroundColor1s(ChoisePoint iX, Term backgroundColor) {
		try {
			long code= backgroundColor.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				Term color= getBuiltInSlot_E_background_color();
				prepareDialogIfNecessary(false,iX);
				targetObject.putSymbolicFieldValue(SymbolCodes.symbolCode_E_background_color,color,iX);
			} else {
				prepareDialogIfNecessary(false,iX);
				targetObject.putSymbolicFieldValue(SymbolCodes.symbolCode_E_background_color,backgroundColor,iX);
			}
		} catch (TermIsNotASymbol e1) {
			prepareDialogIfNecessary(false,iX);
			targetObject.putSymbolicFieldValue(SymbolCodes.symbolCode_E_background_color,backgroundColor,iX);
		}
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
		boolean isTopLevelWindow= Converters.term2YesNo(getBuiltInSlot_E_is_top_level_window(),iX);
		if (!isTopLevelWindow) {
			DesktopUtils.createPaneIfNecessary(staticContext);
		};
		if (targetObject==null) {
			Window mainWindow= null;
			if (!isTopLevelWindow) {
				mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
				DesktopUtils.safelySetVisible(true,mainWindow);
			};
			targetObject= createDialog(isTopLevelWindow,mainWindow,iX);
			targetObject.initiate(currentProcess,staticContext);
			targetObject.increaseDepthCounter();
			targetObject.prepare(
				this,
				// getBuiltInSlot_E_sensitiveness(),
				getBuiltInSlot_E_title(),
				getBuiltInSlot_E_text_color(),
				getBuiltInSlot_E_space_color(),
				getBuiltInSlot_E_font_name(),
				getBuiltInSlot_E_font_size(),
				getBuiltInSlot_E_font_style(),
				getBuiltInSlot_E_x(),
				getBuiltInSlot_E_y(),
				getBuiltInSlot_E_background_color(),
				iX);
			// boolean IsSensitive= DialogUtils.termToDialogSensitiveness(getBuiltInSlot_E_sensitiveness(),iX);
			targetObject.registerSlotVariables();
			// targetObject.start();
			if (showDialog) {
				MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
				targetObject.safelyAddAndDisplay(desktop,iX);
			};
			// targetObject.registerSlotVariables(); // DEBUG 2011.12.15
			targetObject.decreaseDepthCounter();
			targetObject.receiveInitiatingMessage();
			// targetObject.start();
		} else if (showDialog) {
			MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
			targetObject.increaseDepthCounter();
			targetObject.safelyDisplay(desktop,iX);
			targetObject.decreaseDepthCounter();
		}
	}
	//
	protected AbstractDialog createDialog(boolean isTopLevel, Window window, ChoisePoint iX) {
		Term identifier= getBuiltInSlot_E_identifier();
		try {
			String name= DialogUtils.termToDialogIdentifier(identifier,iX);
			try {
				return DialogTable.createDialog(name,isTopLevel,window);
			} catch (UndefinedDialogTableEntry e) {
				throw new ClassNotFound(name);
			}
		} catch (TermIsSymbolAuto e1) {
			long packageNumber= getPackageCode();
			long[] classList= getClassHierarchy();
			for (int i= 0; i < classList.length; i++) {
				long classCode= classList[i];
				String name= String.format("d%02d_%04d",
					packageNumber,
					classCode);
				try {
					return DialogTable.createDialog(name,isTopLevel,window);
				} catch (UndefinedDialogTableEntry e2) {
				}
			};
			// return new DummyDialog(window);
			throw new DialogNotFound();
		}
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
	//
	public GraphicsConfiguration getGraphicsConfiguration() {
		if (targetObject != null) {
			return targetObject.getGraphicsConfiguration();
		} else {
		}
			return null;
	}
}
