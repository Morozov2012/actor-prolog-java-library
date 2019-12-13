// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.system.signals.*;
import morozov.terms.*;

import javax.swing.SwingUtilities;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.InvocationTargetException;

public class DialogEntry {
	//
	protected AbstractDialog dialog= null;
	protected boolean isSlotName;
	protected boolean isNumericalName= false;
	protected String name= null;
	protected long code;
	protected ActiveComponentInterface component;
	protected boolean isInsistent;
	protected DialogEntryType entryType= DialogEntryType.VALUE;
	protected Object currentValueGuard= new Object();
	protected Object currentRangeGuard= new Object();
	protected Term currentValue= null;
	protected Term currentRange= null;
	protected Term intermediateValue= null;
	protected Term intermediateRange= null;
	protected boolean intermediateMode= false;
	protected AtomicBoolean isInitiated= new AtomicBoolean(false);
	protected AtomicBoolean isToBeRefreshed= new AtomicBoolean(false);
	//
	public DialogEntry(AbstractDialog td, String slotName, boolean flag2, DialogEntryType type) {
		dialog= td;
		isSlotName= true;
		name= slotName;
		component= null;
		isInsistent= flag2;
		entryType= type;
	}
	public DialogEntry(AbstractDialog td, boolean flag1, String slotName, ActiveComponentInterface targetComponent, boolean flag2) {
		dialog= td;
		isSlotName= flag1;
		name= slotName;
		component= targetComponent;
		isInsistent= flag2;
	}
	public DialogEntry(AbstractDialog td, long number, ActiveComponentInterface targetComponent, boolean flag2) {
		dialog= td;
		isSlotName= false;
		isNumericalName= true;
		code= number;
		component= targetComponent;
		isInsistent= flag2;
	}
	public DialogEntry(AbstractDialog td, boolean flag1, String slotName, ActiveComponentInterface targetComponent, boolean flag2, DialogEntryType type) {
		dialog= td;
		isSlotName= flag1;
		name= slotName;
		component= targetComponent;
		isInsistent= flag2;
		entryType= type;
	}
	public DialogEntry(AbstractDialog td, long number, ActiveComponentInterface targetComponent, boolean flag2, DialogEntryType type) {
		dialog= td;
		isSlotName= false;
		isNumericalName= true;
		code= number;
		component= targetComponent;
		isInsistent= flag2;
		entryType= type;
	}
	//
	public boolean isBuiltInAction() {
		if (entryType == DialogEntryType.BUILT_IN_ACTION) {
			return true;
		} else {
			return false;
		}
	}
	//
	public void putValue(final DialogControlOperation operation, final Term value, final ChoisePoint iX) {
		if (operation != DialogControlOperation.VALUE) {
			synchronized (currentValueGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					dialog.incrementTheInsideThePutOperationCounter();
					try {
						component.putValue(operation,value,iX);
					} finally {
						dialog.decrementTheInsideThePutOperationCounter();
					}
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								dialog.incrementTheInsideThePutOperationCounter();
								try {
									component.putValue(operation,value,iX);
								} finally {
									dialog.decrementTheInsideThePutOperationCounter();
								}
							}
						});
					} catch (InterruptedException e) {
					} catch (InvocationTargetException e) {
					}
				}
			}
		} else if (entryType.isValueOrAction()) {
			synchronized (currentValueGuard) {
				Term newValue;
				try {
					newValue= component.standardizeValue(value,iX);
				} catch (RejectValue e) {
					return;
				};
				boolean skipOperation= false;
				if (currentValue==null || isToBeRefreshed.get()) {
					currentValue= newValue;
				} else {
					try {
						currentValue.unifyWith(newValue,iX);
						skipOperation= true;
					} catch (Backtracking b) {
						currentValue= newValue;
					}
				};
				if (!skipOperation || !isInitiated.get()) {
					if (SwingUtilities.isEventDispatchThread()) {
						dialog.incrementTheInsideThePutOperationCounter();
						try {
							component.putValue(operation,currentValue,iX);
						} finally {
							dialog.decrementTheInsideThePutOperationCounter();
							isInitiated.set(true);
							isToBeRefreshed.set(false);
						}
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								@Override
								public void run() {
									dialog.incrementTheInsideThePutOperationCounter();
									try {
										component.putValue(operation,currentValue,iX);
									} finally {
										dialog.decrementTheInsideThePutOperationCounter();
										isInitiated.set(true);
										isToBeRefreshed.set(false);
									}
								}
							});
						} catch (InterruptedException e) {
						} catch (InvocationTargetException e) {
						}
					}
				}
			}
		} else if (entryType==DialogEntryType.RANGE) {
			synchronized (currentRangeGuard) {
				Term newRange;
				try {
					newRange= component.standardizeRange(value,iX);
				} catch (RejectRange e) {
					return;
				};
				boolean skipOperation= false;
				if (currentRange==null || isToBeRefreshed.get()) {
					currentRange= newRange;
				} else {
					try {
						currentRange.unifyWith(newRange,iX);
						skipOperation= true;
					} catch (Backtracking b) {
						currentRange= newRange;
					}
				};
				if (!skipOperation || !isInitiated.get()) {
					if (SwingUtilities.isEventDispatchThread()) {
						dialog.incrementTheInsideThePutOperationCounter();
						try {
							component.putRange(currentRange,iX);
						} finally {
							dialog.decrementTheInsideThePutOperationCounter();
							isInitiated.set(true);
							isToBeRefreshed.set(false);
						}
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								@Override
								public void run() {
									dialog.incrementTheInsideThePutOperationCounter();
									try {
										component.putRange(currentRange,iX);
									} finally {
										dialog.decrementTheInsideThePutOperationCounter();
										isInitiated.set(true);
										isToBeRefreshed.set(false);
									}
								}
							});
						} catch (InterruptedException e) {
						} catch (InvocationTargetException e) {
						}
					}
				}
			}
		} else {
			synchronized (currentValueGuard) {
				Term newValue;
				try {
					newValue= entryType.standardizeValue(value,iX);
				} catch (RejectValue e) {
					isInitiated.set(true);
					return;
				};
				boolean skipOperation= false;
				if (currentValue==null || isToBeRefreshed.get()) {
					currentValue= newValue;
				} else {
					try {
						currentValue.unifyWith(newValue,iX);
						skipOperation= true;
					} catch (Backtracking b) {
						currentValue= newValue;
					}
				};
				if (!skipOperation || !isInitiated.get()) {
					if (SwingUtilities.isEventDispatchThread()) {
						dialog.incrementTheInsideThePutOperationCounter();
						try {
							entryType.putValue(dialog,currentValue,iX);
						} finally {
							dialog.decrementTheInsideThePutOperationCounter();
						}
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								@Override
								public void run() {
									dialog.incrementTheInsideThePutOperationCounter();
									try {
										entryType.putValue(dialog,currentValue,iX);
									} finally {
										dialog.decrementTheInsideThePutOperationCounter();

									}
								}
							});
						} catch (InterruptedException e) {
						} catch (InvocationTargetException e) {
						}
					}
				};
				isInitiated.set(true);
			}
		}
	}
	public void requestValueRefreshing() {
		isToBeRefreshed.set(true);
	}
	public Term refreshAndGetValue() throws Backtracking {
		return getExistedValue(true);
	}
	public Term getExistedValue() throws Backtracking {
		return getExistedValue(false);
	}
	public Term getExistedValue(boolean refreshValue) throws Backtracking {
		if (entryType.isValueOrAction()) {
			synchronized (currentValueGuard) {
				if (currentValue==null || refreshValue || isToBeRefreshed.get()) {
					if (SwingUtilities.isEventDispatchThread()) {
						currentValue= component.getValue(DialogControlOperation.VALUE);
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								@Override
								public void run() {
									intermediateValue= component.getValue(DialogControlOperation.VALUE);
								}
							});
						} catch (InterruptedException e) {
							intermediateValue= PrologUnknownValue.instance;
						} catch (InvocationTargetException e) {
							intermediateValue= PrologUnknownValue.instance;
						};
						if (intermediateValue!=null) {
							currentValue= intermediateValue;
						} else {
							throw Backtracking.instance;
						}
					};
					isToBeRefreshed.set(false);
				};
				return currentValue;
			}
		} else if (entryType==DialogEntryType.RANGE) {
			synchronized (currentRangeGuard) {
				if (currentRange==null || refreshValue || isToBeRefreshed.get()) {
					if (SwingUtilities.isEventDispatchThread()) {
						currentRange= component.getRange();
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								@Override
								public void run() {
									intermediateRange= component.getRange();
								}
							});
						} catch (InterruptedException e) {
							intermediateRange= PrologUnknownValue.instance;
						} catch (InvocationTargetException e) {
							intermediateRange= PrologUnknownValue.instance;
						};
						if (intermediateRange!=null) {
							currentRange= intermediateRange;
						} else {
							throw Backtracking.instance;
						}
					};
					isToBeRefreshed.set(false);
				};
				return currentRange;
			}
		} else {
			if ((entryType==DialogEntryType.X || entryType==DialogEntryType.Y) && !isInitiated.get()) {
				throw Backtracking.instance;
			};
			synchronized (currentValueGuard) {
				if (currentValue==null || refreshValue || isToBeRefreshed.get()) {
					if (SwingUtilities.isEventDispatchThread()) {
						currentValue= entryType.getValue(dialog);
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								@Override
								public void run() {
									intermediateValue= entryType.getValue(dialog);
								}
							});
						} catch (InterruptedException e) {
							intermediateValue= PrologUnknownValue.instance;
						} catch (InvocationTargetException e) {
							intermediateValue= PrologUnknownValue.instance;
						};
						if (intermediateValue!=null) {
							currentValue= intermediateValue;
						} else {
							throw Backtracking.instance;
						}
					};
					isToBeRefreshed.set(false);
				};
				return currentValue;
			}
		}
	}
	public Term getVisibleValue(final DialogControlOperation operation) {
		if (operation != DialogControlOperation.VALUE) {
			synchronized (currentValueGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					return component.getValue(operation);
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								intermediateValue= component.getValue(operation);
							}
						});
					} catch (InterruptedException e) {
						throw new CannotAccessDialogControl(e.getCause());
					} catch (InvocationTargetException e) {
						throw new CannotAccessDialogControl(e.getCause());
					};
					if (intermediateValue!=null) {
						return intermediateValue;
					} else {
						throw new CannotAccessDialogControl(null);
					}
				}
			}
		} else if (entryType.isValueOrAction()) {
			synchronized (currentValueGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					return component.getValue(operation);
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								intermediateValue= component.getValue(operation);
							}
						});
					} catch (InterruptedException e) {
						intermediateValue= PrologUnknownValue.instance;
					} catch (InvocationTargetException e) {
						intermediateValue= PrologUnknownValue.instance;
					};
					if (intermediateValue!=null) {
						return intermediateValue;
					} else {
						return PrologUnknownValue.instance;
					}
				}
			}
		} else if (entryType==DialogEntryType.RANGE) {
			synchronized (currentRangeGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					return component.getRange();
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								intermediateRange= component.getRange();
							}
						});
					} catch (InterruptedException e) {
						intermediateRange= PrologUnknownValue.instance;
					} catch (InvocationTargetException e) {
						intermediateRange= PrologUnknownValue.instance;
					};
					if (intermediateRange!=null) {
						return intermediateRange;
					} else {
						return PrologUnknownValue.instance;
					}
				}
			}
		} else {
			synchronized (currentValueGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					return entryType.getValue(dialog);
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								intermediateValue= entryType.getValue(dialog);
							}
						});
					} catch (InterruptedException e) {
						intermediateValue= PrologUnknownValue.instance;
					} catch (InvocationTargetException e) {
						intermediateValue= PrologUnknownValue.instance;
					};
					if (intermediateValue!=null) {
						return intermediateValue;
					} else {
						return PrologUnknownValue.instance;
					}
				}
			}
		}
	}
	//
	public Term getSlotByName(Dialog targetWorld, ChoisePoint iX) {
		return entryType.getSlotByName(name,targetWorld,iX);
	}
	//
	public void setFieldIsEnabled(final boolean mode) {
		if (entryType.isValueActionOrRange()) {
			synchronized (currentValueGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					component.setIsEnabled(mode);
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								component.setIsEnabled(mode);
							}
						});
					} catch (InterruptedException e) {
					} catch (InvocationTargetException e) {
					}
				}
			}
		}
	}
	//
	public boolean fieldIsEnabled(final boolean mode) {
		if (entryType.isValueActionOrRange()) {
			synchronized (currentValueGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					return component.isEnabled(mode);
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								intermediateMode= component.isEnabled(mode);
							}
						});
					} catch (InterruptedException e) {
						intermediateMode= false;
					} catch (InvocationTargetException e) {
						intermediateMode= false;
					};
					return intermediateMode;
				}
			}
		} else {
			return true;
		}
	}
	//
	public Term toTerm() {
		if (isSlotName) {
			return new PrologSymbol(SymbolTable.retrieveSymbolCode(name));
		} else if (isNumericalName) {
			return new PrologInteger(code);
		} else {
			return new PrologString(name);
		}
	}
	@Override
	public String toString() {
		return String.format("DialogEntry[%s;%s;isSlotName:%s;%s;%s;isInsistent:%s;%s]",
			entryType,dialog,isSlotName,name,component,isInsistent,entryType);
	}
}
