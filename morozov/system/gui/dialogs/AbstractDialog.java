// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Window;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Dialog.ModalityType;

import java.math.BigInteger;
import java.lang.reflect.InvocationTargetException;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractDialog
	extends
		DialogKernel
	implements
		ActionListener,
		ComponentListener,
		ProcessStateListener {
	//
	protected boolean isDraftMode= false;
	//
	public boolean isModal= false;
	public boolean isTopLevelWindow= false;
	public boolean isAlwaysOnTopWindow= false;
	public boolean confirmationOnWindowClose= false;
	public boolean exitOnClose= false;
	//
	protected StaticContext staticContext;
	protected ChoisePoint modalChoisePoint= null;
	//
	protected AtomicInteger insideThePutOperation= new AtomicInteger(0);
	//
	protected java.util.Timer scheduler;
	protected LocalDialogTask currentTask;
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public void registerSlotVariables();
	//
	public boolean isToBeModal() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractDialog(YesNoDefault modality, boolean topLevelWindowFlag, boolean alwaysOnTopWindowFlag, boolean closingConfirmationFlag, YesNoDefault closingMode, Window parent) {
		isModal= modality.toBoolean(isToBeModal());
		if (isModal) {
			isTopLevelWindow= false;
			isAlwaysOnTopWindow= false;
			confirmationOnWindowClose= false;
			exitOnClose= false;
		} else {
			isTopLevelWindow= topLevelWindowFlag;
			isAlwaysOnTopWindow= alwaysOnTopWindowFlag;
			confirmationOnWindowClose= closingConfirmationFlag;
			if (isTopLevelWindow) {
				exitOnClose= closingMode.toBoolean(isTopLevelWindow);
			} else {
				exitOnClose= false;
			}
		};
		if (isModal) {
			if (isTopLevelWindow) {
				dialogContainer= new ExtendedJDialog(this,null,ModalityType.DOCUMENT_MODAL);
			} else {
				dialogContainer= new ExtendedJDialog(this,parent,ModalityType.DOCUMENT_MODAL);
			}
		} else {
			if (isTopLevelWindow) {
				dialogContainer= new ExtendedJFrame(this);
			} else {
				dialogContainer= new DialogJInternalFrame(this);
			}
		};
		scheduler= new java.util.Timer(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerPortsAndRecoverPortValues(AbstractDialog dialog, Dialog targetWorld, DialogEntry[] userDefinedSlots, DialogEntry[] systemSlots) {
		int numberOfUserDefinedSlots= userDefinedSlots.length;
		int numberOfSystemSlots= systemSlots.length;
		long numberOfEntries= userDefinedSlots.length + numberOfSystemSlots;
		controlTable= new DialogEntry[PrologInteger.toInteger(numberOfEntries)];
		for (int n=0; n < numberOfUserDefinedSlots; n++) {
			controlTable[n]= userDefinedSlots[n];
		};
		for (int n=0; n < numberOfSystemSlots; n++) {
			controlTable[numberOfUserDefinedSlots+n]= systemSlots[n];
		};
		specialProcess.registerPortsAndRecoverPortValues(dialog,targetWorld,userDefinedSlots,systemSlots);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void safelyDisplay(final DialogInitialState initialState, final ChoisePoint iX, final StaticContext context) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyDisplay(initialState,iX,context);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyDisplay(initialState,iX,context);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklyDisplay(DialogInitialState initialState, ChoisePoint iX, StaticContext context) {
		if (isInitiated.get()) {
			modalChoisePoint= iX;
			dialogContainer.safelySetVisible(true);
			dialogContainer.toFront();
		} else {
			quicklyAddAndDisplay(initialState,iX,context);
		}
	}
	//
	public void safelyAddAndDisplay(final DialogInitialState initialState, final ChoisePoint iX, final StaticContext context) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyAddAndDisplay(initialState,iX,context);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyAddAndDisplay(initialState,iX,context);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private void quicklyAddAndDisplay(DialogInitialState initialState, ChoisePoint iX, final StaticContext context) {
		dialogContainer.setClosable(true);
		dialogContainer.setResizable(true);
		dialogContainer.setMaximizable(true);
		dialogContainer.setIconifiable(false);
		if (confirmationOnWindowClose) {
			dialogContainer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		} else {
			if (exitOnClose) {
				try {
					dialogContainer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
// dialogContainer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				} catch (SecurityException e) {
					dialogContainer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				}
			} else {
					dialogContainer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			}
		};
		dialogContainer.addComponentListener(this);
		//
		dialogContainer.addToDesktopIfNecessary(context);
		//
		modalChoisePoint= iX;
		// defineDefaultButton();
		dialogContainer.pack();
		implementPreferredSize();
		safelyPositionMainPanel();
		isInitiated.set(true);
		start();
		safelyDefineDefaultButton(); // For modal dialogs
		dialogContainer.safelySetVisible(true);
		switch (initialState) {
		case MINIMIZED:
			dialogContainer.safelyMinimize();
			break;
		case MAXIMIZED:
			dialogContainer.safelyMaximize();
			break;
		default:
			break;
		};
		// dialogContainer.toFront();
		safelyDefineDefaultButton(); // For modeless dialogs
		// defineDefaultButton();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initiate(ActiveWorld currentProcess, StaticContext context) {
		staticContext= context;
		dialogContainer.initiate(context);
		specialProcess.initiate(currentProcess,context);
		if (isTopLevelWindow) {
			Window window= dialogContainer.getWindow();
			if (window != null) {
				StaticDesktopAttributes.setTopLevelWindow(window,context);
			}
		};
		if (isAlwaysOnTopWindow) {
			dialogContainer.safelySetAlwaysOnTop(true);
		}
	}
	//
	public void start() {
		targetWorld.sendStateRequest(this,"");
		specialProcess.startProcesses();
	}
	//
	public void receiveInitiatingMessage() {
		specialProcess.receiveUserInterfaceMessage(null,true,DialogEventType.NONE);
	}
	//
	public void sendTheWindowClosingOrWindowClosedMessage() {
		if (confirmationOnWindowClose) {
			sendTheWindowClosingMessage();
		} else {
			sendTheWindowClosedMessage();
		}
	}
	public void sendTheWindowClosingMessage() {
		specialProcess.receiveUserInterfaceMessage(null,true,DialogEventType.WINDOW_CLOSING);
	}
	public void sendTheWindowClosedMessage() {
		specialProcess.receiveUserInterfaceMessage(null,true,DialogEventType.WINDOW_CLOSED);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void incrementTheInsideThePutOperationCounter() {
		insideThePutOperation.incrementAndGet();
	}
	//
	public void decrementTheInsideThePutOperationCounter() {
		int counter= insideThePutOperation.decrementAndGet();
		if (counter <= 0) {
			// repaintAfterDelay();
			safelyRepaint();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void reportValueUpdate(ActiveComponent currentComponent) {
		boolean entryIsFound= false;
		boolean sendFlowMessage= false;
		DialogEntry entry= null;
		for (int i= 0; i < controlTable.length; i++) {
			entry= controlTable[i];
			if (entry.component==currentComponent && entry.entryType==DialogEntryType.VALUE) {
				entryIsFound= true;
				if (entry.isSlotName) {
					sendFlowMessage= true;
				};
				break;
			}
		};
		if (entryIsFound) {
			if (sendFlowMessage) {
				if (insideThePutOperation.intValue()<=0) {
					entry.requestValueRefreshing();
					specialProcess.receiveUserInterfaceMessage(entry,true,DialogEventType.MODIFIED_CONTROL);
				} else {
					specialProcess.receiveUserInterfaceMessage(entry,true,DialogEventType.NONE);
				}
			} else {
				if (insideThePutOperation.intValue()<=0) {
					entry.requestValueRefreshing();
					specialProcess.receiveUserInterfaceMessage(entry,false,DialogEventType.MODIFIED_CONTROL);
				}
			}
		}
	}
	public void reportCompleteEditing(ActiveComponent currentComponent) {
		boolean entryIsFound= false;
		boolean sendFlowMessage= false;
		DialogEntry entry= null;
		for (int i= 0; i < controlTable.length; i++) {
			entry= controlTable[i];
			if (entry.component==currentComponent && entry.entryType==DialogEntryType.VALUE) {
				entryIsFound= true;
				if (entry.isSlotName) {
					sendFlowMessage= true;
				};
				break;
			}
		};
		if (entryIsFound) {
			if (sendFlowMessage) {
				if (insideThePutOperation.intValue()<=0) {
					entry.requestValueRefreshing();
					specialProcess.receiveUserInterfaceMessage(entry,true,DialogEventType.COMPLETE_EDITING);
				} else {
					specialProcess.receiveUserInterfaceMessage(entry,true,DialogEventType.NONE);
				}
			} else {
				if (insideThePutOperation.intValue()<=0) {
					entry.requestValueRefreshing();
					specialProcess.receiveUserInterfaceMessage(entry,false,DialogEventType.COMPLETE_EDITING);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void transmitEntryValue(DialogEntry entry, ChoisePoint iX) {
		if (entry!=null && entry.isSlotName) {
			try {
				Term value= entry.refreshAndGetValue();
				String slotName= entry.name;
				for (int i= 0; i < controlTable.length; i++) {
					DialogEntry e2= controlTable[i];
					if (e2!=entry && e2.isSlotName) {
						if (e2.name.equals(slotName)) {
							e2.putValue(DialogControlOperation.VALUE,value,iX);
						}
					}
				}
			} catch (Backtracking b) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putFieldValue(DialogControlOperation operation, Term identifier, Term fieldValue, ChoisePoint iX) {
		specialProcess.putFieldValue(operation,identifier,fieldValue,iX);
	}
	public void quicklyPutFieldValue(DialogControlOperation operation, Term identifier, Term fieldValue, ChoisePoint iX) {
		fieldValue= fieldValue.dereferenceValue(iX);
		if (fieldValue.thisIsFreeVariable()) {
			return;
		};
		try {
			long code= identifier.getSymbolValue(iX);
			String slotOrActionName= identifier.toString(iX);
			boolean slotIsFound= false;
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isSlotName) {
					if (entry.name.equals(slotOrActionName)) {
						entry.putValue(operation,fieldValue,iX);
						specialProcess.receiveUserInterfaceMessage(entry,true,DialogEventType.NONE);
						slotIsFound= true;
						// return;
					}
				}
			};
			if (slotIsFound) {
				return;
			};
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isBuiltInAction()) {
					if (entry.name.equals(slotOrActionName)) {
						entry.putValue(operation,fieldValue,iX);
						slotIsFound= true;
						// return;
					}
				}
			};
			if (!slotIsFound) {
				throw new UnknownDialogSlotOrActionName(slotOrActionName);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				long number= identifier.getSmallIntegerValue(iX);
				boolean slotIsFound= false;
				for (int i= 0; i < controlTable.length; i++) {
					DialogEntry entry= controlTable[i];
					if (!entry.isSlotName && entry.isNumericalName) {
						if (entry.code==number) {
							entry.putValue(operation,fieldValue,iX);
							slotIsFound= true;
							// return;
						}
					}
				};
				if (!slotIsFound) {
					throw new UnknownDialogEntryCode(number);
				}
			} catch (TermIsNotAnInteger e2) {
				try {
					String name= identifier.getStringValue(iX);
					boolean slotIsFound= false;
					for (int i= 0; i < controlTable.length; i++) {
						DialogEntry entry= controlTable[i];
						if (!entry.isSlotName && !entry.isNumericalName) {
							if (entry.name.equals(name)) {
								entry.putValue(operation,fieldValue,iX);
								slotIsFound= true;
								// return;
							}
						}
					};
					if (!slotIsFound) {
						throw new UnknownDialogEntryName(name);
					}
				} catch (TermIsNotAString e3) {
					throw new WrongArgumentIsNotDialogEntry(identifier);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getFieldValue(DialogControlOperation operation, Term identifier, ChoisePoint iX) {
		return specialProcess.getFieldValue(operation,identifier,iX);
	}
	public Term quicklyGetFieldValue(DialogControlOperation operation, Term identifier, ChoisePoint iX) {
		try {
			long code= identifier.getSymbolValue(iX);
			String slotOrActionName= identifier.toString(iX);
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isSlotName) {
					if (entry.name.equals(slotOrActionName)) {
						return entry.getVisibleValue(operation);
					}
				}
			};
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isBuiltInAction()) {
					if (entry.name.equals(slotOrActionName)) {
						return entry.getVisibleValue(operation);
					}
				}
			};
			throw new UnknownDialogSlotOrActionName(slotOrActionName);
		} catch (TermIsNotASymbol e1) {
			try {
				long number= identifier.getSmallIntegerValue(iX);
				for (int i= 0; i < controlTable.length; i++) {
					DialogEntry entry= controlTable[i];
					if (!entry.isSlotName && entry.isNumericalName) {
						if (entry.code==number) {
							return entry.getVisibleValue(operation);
						}
					}
				};
				throw new UnknownDialogEntryCode(number);
			} catch (TermIsNotAnInteger e2) {
				try {
					String name= identifier.getStringValue(iX);
					for (int i= 0; i < controlTable.length; i++) {
						DialogEntry entry= controlTable[i];
						if (!entry.isSlotName && !entry.isNumericalName) {
							if (entry.name.equals(name)) {
								return entry.getVisibleValue(operation);
							}
						}
					};
					throw new UnknownDialogEntryName(name);
				} catch (TermIsNotAString e3) {
					throw new WrongArgumentIsNotDialogEntry(identifier);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setFieldIsEnabled(Term identifier, boolean mode, ChoisePoint iX) {
		try {
			long code= identifier.getSymbolValue(iX);
			String slotOrActionName= identifier.toString(iX);
			boolean slotIsFound= false;
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isSlotName) {
					if (entry.name.equals(slotOrActionName)) {
						entry.setFieldIsEnabled(mode);
						slotIsFound= true;
						// return;
					}
				}
			};
			if (slotIsFound) {
				return;
			};
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isBuiltInAction()) {
					if (entry.name.equals(slotOrActionName)) {
						entry.setFieldIsEnabled(mode);
						slotIsFound= true;
						// return;
					}
				}
			};
			if (!slotIsFound) {
				throw new UnknownDialogSlotOrActionName(slotOrActionName);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				long number= identifier.getSmallIntegerValue(iX);
				boolean slotIsFound= false;
				for (int i= 0; i < controlTable.length; i++) {
					DialogEntry entry= controlTable[i];
					if (!entry.isSlotName && entry.isNumericalName) {
						if (entry.code==number) {
							entry.setFieldIsEnabled(mode);
							slotIsFound= true;
							// return;
						}
					}
				};
				if (!slotIsFound) {
					throw new UnknownDialogEntryCode(number);
				}
			} catch (TermIsNotAnInteger e2) {
				try {
					String name= identifier.getStringValue(iX);
					boolean slotIsFound= false;
					for (int i= 0; i < controlTable.length; i++) {
						DialogEntry entry= controlTable[i];
						if (!entry.isSlotName && !entry.isNumericalName) {
							if (entry.name.equals(name)) {
								entry.setFieldIsEnabled(mode);
								slotIsFound= true;
								// return;
							}
						}
					};
					if (!slotIsFound) {
						throw new UnknownDialogEntryName(name);
					}
				} catch (TermIsNotAString e3) {
					throw new WrongArgumentIsNotDialogEntry(identifier);
				}
			}
		}
	}
	//
	public void checkIfFieldIsEnabled(Term identifier, boolean mode, ChoisePoint iX) throws Backtracking {
		try {
			long code= identifier.getSymbolValue(iX);
			String slotOrActionName= identifier.toString(iX);
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isSlotName) {
					if (entry.name.equals(slotOrActionName)) {
						if (!entry.fieldIsEnabled(mode)) {
							throw Backtracking.instance;
						};
						return;
					}
				}
			};
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isBuiltInAction()) {
					if (entry.name.equals(slotOrActionName)) {
						if (!entry.fieldIsEnabled(mode)) {
							throw Backtracking.instance;
						};
						return;
					}
				}
			};
			throw new UnknownDialogSlotOrActionName(slotOrActionName);
		} catch (TermIsNotASymbol e1) {
			try {
				long number= identifier.getSmallIntegerValue(iX);
				for (int i= 0; i < controlTable.length; i++) {
					DialogEntry entry= controlTable[i];
					if (!entry.isSlotName && entry.isNumericalName) {
						if (entry.code==number) {
							if (!entry.fieldIsEnabled(mode)) {
								throw Backtracking.instance;
							};
							return;
						}
					}
				};
				throw new UnknownDialogEntryCode(number);
			} catch (TermIsNotAnInteger e2) {
				try {
					String name= identifier.getStringValue(iX);
					for (int i= 0; i < controlTable.length; i++) {
						DialogEntry entry= controlTable[i];
						if (!entry.isSlotName && !entry.isNumericalName) {
							if (entry.name.equals(name)) {
								if (!entry.fieldIsEnabled(mode)) {
									throw Backtracking.instance;
								};
								return;
							}
						}
					};
					throw new UnknownDialogEntryName(name);
				} catch (TermIsNotAString e3) {
					throw new WrongArgumentIsNotDialogEntry(identifier);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void prepareAndSendFlowMessages() {
		specialProcess.phaseInitiation();
		for (int i= 0; i < controlTable.length; i++) {
			DialogEntry entry= controlTable[i];
			if (entry.isSlotName) {
				Term value= null;
				Term slot= targetWorld.getSlotByName(entry.name);
				slot= slot.extractSlotVariable();
				if (slot.thisIsSlotVariable()) {
					SlotVariable slotVariable= (SlotVariable)slot;
					try {
						value= entry.getExistedValue();
						specialProcess.prepareFlowMessage(slotVariable,value);
					} catch (Backtracking b) {
					}
				}
			}
		};
		specialProcess.fixSlotVariables(false);
		specialProcess.sendActualFlowMessages();
		specialProcess.storeSlotVariables();
		specialProcess.freeTrail();
	}
	//
	public void sendModifiedControlMessage(DialogEntry entry, ChoisePoint iX) {
		if (entry!=null) {
			Term predicateArgument= entry.toTerm();
			Term[] arguments= new Term[]{predicateArgument};
			long domainSignature= targetWorld.entry_s_ModifiedControl_1_i();
			if (isModal) {
				ChoisePoint newIx= new ChoisePoint(modalChoisePoint);
				Continuation c1= new DomainSwitch(new SuccessTermination(),domainSignature,targetWorld,targetWorld,arguments);
				try {
					c1.execute(newIx);
				} catch (Backtracking b) {
				};
				newIx.freeTrail();
			} else {
				AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
				// 2018.10.30: call= new AsyncCall(domainSignature,targetWorld,true,false,arguments,true);
				targetWorld.transmitAsyncCall(call,iX);
			}
		}
	}
	//
	public void sendCompleteEditingMessage(DialogEntry entry, ChoisePoint iX) {
		if (entry!=null) {
			Term predicateArgument= entry.toTerm();
			Term[] arguments= new Term[]{predicateArgument};
			long domainSignature= targetWorld.entry_s_CompleteEditing_1_i();
			if (isModal) {
				ChoisePoint newIx= new ChoisePoint(modalChoisePoint);
				Continuation c1= new DomainSwitch(new SuccessTermination(),domainSignature,targetWorld,targetWorld,arguments);
				try {
					c1.execute(newIx);
				} catch (Backtracking b) {
				};
				newIx.freeTrail();
			} else {
				AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
				targetWorld.transmitAsyncCall(call,iX);
			}
		}
	}
	//
	public void sendCreatedControlMessage(DialogEntry entry, ChoisePoint iX) {
		if (entry!=null) {
			Term predicateArgument= entry.toTerm();
			Term[] arguments= new Term[]{predicateArgument};
			long domainSignature= targetWorld.entry_s_CreatedControl_1_i();
			if (isModal) {
				ChoisePoint newIx= new ChoisePoint(modalChoisePoint);
				Continuation c1= new DomainSwitch(new SuccessTermination(),domainSignature,targetWorld,targetWorld,arguments);
				try {
					c1.execute(newIx);
				} catch (Backtracking b) {
				};
				newIx.freeTrail();
			} else {
				AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
				targetWorld.transmitAsyncCall(call,iX);
			}
		}
	}
	//
	public void transmitTheWindowClosingMessage(ChoisePoint iX) {
		Term[] arguments= new Term[0];
		long domainSignature= targetWorld.entry_s_WindowClosing_0();
		AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
		targetWorld.transmitAsyncCall(call,iX);
	}
	//
	public void transmitTheWindowClosedMessage(ChoisePoint iX) {
		Term[] arguments= new Term[0];
		long domainSignature= targetWorld.entry_s_WindowClosed_0();
		AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
		targetWorld.transmitAsyncCall(call,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void repaintAfterDelay() {
		synchronized (this) {
			if (currentTask != null) {
				currentTask.cancel();
				scheduler.purge();
			};
			currentTask= new LocalDialogTask(this,scheduler);
			scheduler.schedule(currentTask,0,1);
		}
	}
	//
	public void skipDelayedRepainting() {
		synchronized (this) {
			if (currentTask != null) {
				currentTask.cancel();
				scheduler.purge();
			};
			currentTask= null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long get_data_request_signature() {
		return targetWorld.entry_f_DataRequest_1_i();
	}
	//
	public void withdrawRequest(Resident resident) {
		if (targetWorld!=null) {
			targetWorld.withdrawRequest(resident);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void actionPerformed(ActionEvent e) {
		// 2013.12.07: Без этих двух комманд
		// происходят престранные вещи. Нажимаю кнопку,
		// открывается новый (модальный) диалог, закрываю
		// диалог, и потом текущий диалог некорректно
		// перерисовывается.
		// См. пример test_117_45_enable_disable_01_jdk.a.
		// revalidate();
		// repaint();
		//
		boolean isSystemCommand= false;
		boolean isTheResetCommand= false;
		boolean isTheVerifyCommand= false;
		String name= e.getActionCommand();
		long domainSignature= -1;
		Term[] arguments= null;
		if (name.equals("close")) {
			sendTheWindowClosingMessage();
			dialogContainer.safelySetVisible(false);
			// 2017.02.10:
			if (exitOnClose) {
				Runtime runtime= Runtime.getRuntime();
				runtime.exit(0);
			};
			return;
		} else if (name.equals("verify")) {
			isSystemCommand= true;
			isTheVerifyCommand= true;
			arguments= new Term[0];
		} else if (name.equals("reset")) {
			isSystemCommand= true;
			isTheResetCommand= true;
			arguments= new Term[0];
		} else {
			domainSignature= targetWorld.entry_s_Action_1_i();
			Term predicateArgument= null;
			if (name.regionMatches(0,"name:",0,5)) {
				predicateArgument= new PrologString(name.substring(5));
			} else if (name.regionMatches(0,"code:",0,5)) {
				predicateArgument= new PrologInteger(new BigInteger(name.substring(5)));
			} else {
				predicateArgument= new PrologString(name);
			};
			arguments= new Term[]{predicateArgument};
		};
		if (isModal) {
			if (!isSystemCommand) {
				ChoisePoint newIx= new ChoisePoint(modalChoisePoint);
				Continuation c1= new DomainSwitch(new SuccessTermination(),domainSignature,targetWorld,targetWorld,arguments);
				try {
					c1.execute(newIx);
				} catch (Backtracking b) {
				};
				newIx.freeTrail();
			}
		} else {
			AsyncCall call= null;
			if (isSystemCommand) {
				if (isTheVerifyCommand) {
					call= new TheVerifyCommand(domainSignature,targetWorld,true,true,arguments,true);
				} else if (isTheResetCommand) {
					call= new TheResetCommand(domainSignature,targetWorld,true,true,arguments,true);
				}
			} else {
				call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
			};
			targetWorld.transmitAsyncCall(call,null);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void componentHidden(ComponentEvent event) {
		// Invoked when the component has been made invisible.
	}
	public void componentMoved(ComponentEvent event) {
		dialogContainer.repaintParent();
		safelyAcceptNewPositionOfDialog();
	}
	public void componentResized(ComponentEvent event) {
		dialogContainer.repaintParent();
		safelyAcceptNewPositionOfDialog();
		specialProcess.receiveUserInterfaceMessage(findDialogEntryFontSize(),true,DialogEventType.NONE);
	}
	public void componentShown(ComponentEvent event) {
		// Invoked when the component has been made visible.
	}
}
