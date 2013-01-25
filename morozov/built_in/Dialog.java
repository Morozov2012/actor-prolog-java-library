// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.run.*;
import morozov.terms.*;

import java.awt.Window;

public abstract class Dialog extends Alpha {
	//
	protected AbstractDialog targetObject= null;
	//
	abstract protected Term getBuiltInSlot_E_identifier();
	// abstract protected Term getBuiltInSlot_E_sensitiveness();
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
	abstract public long entry_f_DataRequest_1_i();
	// abstract public long domainSignatureOfSubgoal_0_InClause_1(long predicateSignatureNumber);
	//
	public Dialog() {
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
		if (targetObject!=null) {
			// targetObject.validate();
			targetObject.safelyValidate();
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		if (targetObject!=null) {
			targetObject.safelyHide();
		}
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
	public void put2s(ChoisePoint iX, Term fieldName, Term fieldValue) {
		prepareDialogIfNecessary(false,iX);
		targetObject.putFieldValue(fieldName,fieldValue,iX);
	}
	//
	public void action1s(ChoisePoint iX, Term actionName) {
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
		DesktopUtils.createPaneIfNecessary(staticContext);
		if (targetObject==null) {
			Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(staticContext);
                        targetObject= createDialog(mainWindow,iX);
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
			targetObject.start();
			if (showDialog) {
				MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
				targetObject.safelyAddAndDisplay(desktop,iX);
			};
			// targetObject.registerSlotVariables(); // DEBUG 2011.12.15
                        targetObject.decreaseDepthCounter();
			targetObject.receiveInitiatingMessage();
		} else if (showDialog) {
			MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
                        targetObject.increaseDepthCounter();
			targetObject.safelyDisplay(desktop,iX);
                        targetObject.decreaseDepthCounter();
		}
	}
	//
	protected AbstractDialog createDialog(Window window, ChoisePoint iX) {
		Term identifier= getBuiltInSlot_E_identifier();
		try {
			String name= DialogUtils.termToDialogIdentifier(identifier,iX);
			try {
				return DialogTable.createDialog(name,window);
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
					return DialogTable.createDialog(name,window);
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
			throw new Backtracking();
			// c0.execute(iX);
		}
	}
}
