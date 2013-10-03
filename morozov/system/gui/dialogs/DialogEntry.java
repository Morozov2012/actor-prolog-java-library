// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.run.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.system.gui.dialogs.signals.*;
import morozov.terms.*;

import javax.swing.SwingUtilities;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.InvocationTargetException;

public class DialogEntry {
	protected AbstractDialog dialog= null;
	protected boolean isSlotName;
	protected boolean isNumericCode= false;
	protected String name= null;
	protected long code;
	protected ActiveComponent component;
	protected boolean isInsistent;
	protected DialogEntryType entryType= DialogEntryType.VALUE;
	Object currentValueGuard= new Object();
	Object currentRangeGuard= new Object();
	Term currentValue= null;
	Term currentRange= null;
	private Term intermediateValue= null;
	private Term intermediateRange= null;
	private AtomicBoolean isInitiated= new AtomicBoolean(false);
	//
	public DialogEntry(AbstractDialog td, String slotName, boolean flag2, DialogEntryType type) {
		dialog= td;
		isSlotName= true;
		name= slotName;
		component= null;
		isInsistent= flag2;
		entryType= type;
	}
	public DialogEntry(AbstractDialog td, boolean flag1, String slotName, ActiveComponent targetComponent, boolean flag2) {
		dialog= td;
		isSlotName= flag1;
		name= slotName;
		component= targetComponent;
		isInsistent= flag2;
	}
	public DialogEntry(AbstractDialog td, long number, ActiveComponent targetComponent, boolean flag2) {
		dialog= td;
		isSlotName= false;
		isNumericCode= true;
		code= number;
		component= targetComponent;
		isInsistent= flag2;
	}
	public DialogEntry(AbstractDialog td, boolean flag1, String slotName, ActiveComponent targetComponent, boolean flag2, DialogEntryType type) {
		dialog= td;
		isSlotName= flag1;
		name= slotName;
		component= targetComponent;
		isInsistent= flag2;
		entryType= type;
	}
	public DialogEntry(AbstractDialog td, long number, ActiveComponent targetComponent, boolean flag2, DialogEntryType type) {
		dialog= td;
		isSlotName= false;
		isNumericCode= true;
		code= number;
		component= targetComponent;
		isInsistent= flag2;
		entryType= type;
	}
	//
	public void putValue(Term value, final ChoisePoint iX) {
		if (entryType==DialogEntryType.VALUE) {
			synchronized(currentValueGuard) {
				Term newValue;
				try {
					newValue= component.standardizeValue(value,iX);
				} catch (RejectValue e) {
					return;
				};
				boolean skipOperation= false;
				if (currentValue==null) {
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
						dialog.insideThePutOperation.set(true);
						try {
							component.putValue(currentValue,iX);
						} finally {
							dialog.insideThePutOperation.set(false);
							isInitiated.set(true);
						}
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									dialog.insideThePutOperation.set(true);
									try {
										component.putValue(currentValue,iX);
									} finally {
										dialog.insideThePutOperation.set(false);
										isInitiated.set(true);
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
			synchronized(currentRangeGuard) {
				Term newRange;
				try {
					newRange= component.standardizeRange(value,iX);
				} catch (RejectRange e) {
					return;
				};
				boolean skipOperation= false;
				if (currentRange==null) {
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
						dialog.insideThePutOperation.set(true);
						try {
							component.putRange(currentRange,iX);
						} finally {
							dialog.insideThePutOperation.set(false);
							isInitiated.set(true);
						}
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									dialog.insideThePutOperation.set(true);
									try {
										component.putRange(currentRange,iX);
									} finally {
										dialog.insideThePutOperation.set(false);
										isInitiated.set(true);
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
			synchronized(currentValueGuard) {
				Term newValue;
				try {
					newValue= entryType.standardizeValue(value,iX);
				} catch (RejectValue e) {
					isInitiated.set(true); // 2012.09.09
					return;
				};
				boolean skipOperation= false;
				if (currentValue==null) {
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
						dialog.insideThePutOperation.set(true);
						try {
							entryType.putValue(dialog,currentValue,iX);
						} finally {
							dialog.insideThePutOperation.set(false);
							// isInitiated.set(true);
						}
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									dialog.insideThePutOperation.set(true);
									try {
										entryType.putValue(dialog,currentValue,iX);
									} finally {
										dialog.insideThePutOperation.set(false);
										// isInitiated.set(true);
									}
								}
							});
						} catch (InterruptedException e) {
						} catch (InvocationTargetException e) {
						}
					}
				};
				isInitiated.set(true); // 2012.09.09
			}
		}
	}
	public Term refreshAndGetValue() throws Backtracking {
		return getExistedValue(true);
	}
	public Term getExistedValue() throws Backtracking {
		return getExistedValue(false);
	}
	public Term getExistedValue(boolean refreshValue) throws Backtracking {
		if (entryType==DialogEntryType.VALUE) {
			synchronized(currentValueGuard) {
				if (currentValue==null || refreshValue) {
					if (SwingUtilities.isEventDispatchThread()) {
						currentValue= component.getValue();
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									intermediateValue= component.getValue();
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
					}
				};
				return currentValue;
			}
		} else if (entryType==DialogEntryType.RANGE) {
			synchronized(currentRangeGuard) {
				if (currentRange==null || refreshValue) {
					if (SwingUtilities.isEventDispatchThread()) {
						currentRange= component.getRange();
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
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
					}
				};
				return currentRange;
			}
		} else {
			if ((entryType==DialogEntryType.X || entryType==DialogEntryType.Y) && !isInitiated.get()) {
				throw Backtracking.instance;
			};
			synchronized(currentValueGuard) {
				// try {
					if (currentValue==null || refreshValue) {
						if (SwingUtilities.isEventDispatchThread()) {
							currentValue= entryType.getValue(dialog);
						} else {
							try {
								SwingUtilities.invokeAndWait(new Runnable() {
									public void run() {
										// try {
											intermediateValue= entryType.getValue(dialog);
										// } catch (CannotComputeDesktopSize e) {
										// }
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
						}
					};
					return currentValue;
				// } catch (CannotComputeDesktopSize e) {
				//	throw Backtracking.instance;
				// }
			}
		}
	}
	public Term getVisibleValue() {
		if (entryType==DialogEntryType.VALUE) {
			synchronized(currentValueGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					return component.getValue();
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								intermediateValue= component.getValue();
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
			synchronized(currentRangeGuard) {
				if (SwingUtilities.isEventDispatchThread()) {
					return component.getRange();
				} else {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								// try {
								intermediateRange= component.getRange();
								// } catch (Backtracking e) {
								//	intermediateRange= null;
								// }
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
			synchronized(currentValueGuard) {
				// try {
					if (SwingUtilities.isEventDispatchThread()) {
						return entryType.getValue(dialog);
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									// try {
										intermediateValue= entryType.getValue(dialog);
									// } catch (CannotComputeDesktopSize e) {
									// }
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
				// } catch (CannotComputeDesktopSize e) {
				//	return PrologUnknownValue.instance;
				// }
			}
		}
	}
	//
	public Term toTerm() {
		if (isSlotName) {
			return new PrologSymbol(SymbolTable.retrieveSymbolCode(name));
		} else if (isNumericCode) {
			return new PrologInteger(code);
		} else {
			return new PrologString(name);
		}
	}
	public String toString() {
		return String.format("DialogEntry[%s;%s;isSlotName:%s;%s;%s;isInsistent:%s;%s]",
			entryType,dialog,isSlotName,name,component,isInsistent,entryType);
	}
}
