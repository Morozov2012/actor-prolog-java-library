// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.built_in.*;
import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;

import java.math.BigInteger;
import javax.swing.JDesktopPane;
import javax.swing.WindowConstants;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dialog.ModalityType;

import java.lang.reflect.InvocationTargetException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class UnknownDialogSlotName extends RuntimeException {}
class UnknownDialogEntryCode extends RuntimeException {}
class UnknownDialogEntryName extends RuntimeException {}

public abstract class AbstractDialog
	// extends JInternalFrame
	// extends JDialog
	implements
		// PropertyChangeListener,
		ActionListener,
		ComponentListener,
		ProcessStateListener {
	//
	// public static final int DIALOG_LAYER_CODE= 10;
	// public static final Integer DIALOG_LAYER= new Integer(DIALOG_LAYER_CODE);
	//
	protected DialogOperations dialogContainer;
	public Dialog targetWorld= null;
	protected ActiveUser specialProcess= new ActiveUser();
	protected StaticContext staticContext;
	protected DialogEntry[] controlTable= new DialogEntry[0];
	protected boolean isDraftMode= false;
	//
	protected ChoisePoint modalChoisePoint= null;
	protected BigInteger insideModalDialog= BigInteger.ZERO;
	//
	private AtomicBoolean isInitiated= new AtomicBoolean(false);
	private AtomicBoolean isDisposed= new AtomicBoolean(false);
	//
	protected AtomicReference<ExtendedCoordinates> actualCoordinates= new AtomicReference<ExtendedCoordinates>(new ExtendedCoordinates(new ExtendedCoordinate(),new ExtendedCoordinate()));
	// protected AtomicReference<ExtendedCoordinate> actualY= new AtomicReference<ExtendedCoordinate>(new ExtendedCoordinate());
	// protected AtomicReference<ExtendedCoordinate> actualY= new AtomicReference<ExtendedCoordinate>(new ExtendedCoordinate());
	protected AtomicReference<Point> previousCoordinates= new AtomicReference<Point>(null);
	// private AtomicInteger previousX= new AtomicInteger(0);
	// private AtomicInteger previousY= new AtomicInteger(0);
	protected AtomicReference<Dimension> previousActualSize= new AtomicReference<Dimension>(new Dimension(0,0));
	// protected AtomicInteger previousActualWidth= new AtomicInteger(0);
	// protected AtomicInteger previousActualHeight= new AtomicInteger(0);
	//
	protected static final Color defaultDialogTextColor= null;
	protected static final Color defaultDialogSpaceColor= null;
	protected static final String defaultDialogFontName= Font.MONOSPACED;
	protected static final int defaultDialogFontSize= 16; // 18; // 16; // 14
	protected static final int defaultDialogFontStyle= Font.PLAIN;
	protected static final boolean defaultDialogFontUnderline= false;
	protected static final ExtendedCoordinate defaultDialogX= new ExtendedCoordinate();
	protected static final ExtendedCoordinate defaultDialogY= new ExtendedCoordinate();
	protected static final Color defaultDialogSuccessBackgroundColor= null;
	protected static final Color defaultDialogFailureForegroundColor= null; // Color.WHITE;
	protected static final Color defaultDialogFailureBackgroundColor= Color.RED;
	//
	// public AtomicReference<Color> currentSuccessForegroundColor= new AtomicReference<Color>();
	public AtomicReference<Color> currentSuccessBackgroundColor= new AtomicReference<Color>();
	public AtomicReference<Color> currentFailureForegroundColor= new AtomicReference<Color>();
	public AtomicReference<Color> currentFailureBackgroundColor= new AtomicReference<Color>();
	//
	protected AtomicReference<String> currentFontName= new AtomicReference<String>(defaultDialogFontName);
	protected AtomicInteger currentFontSize= new AtomicInteger(defaultDialogFontSize);
	protected AtomicInteger currentFontStyle= new AtomicInteger(defaultDialogFontStyle);
	protected AtomicBoolean currentFontUnderline= new AtomicBoolean(defaultDialogFontUnderline);
	//
	private Map<Integer,ApprovedFont> approvedFonts= Collections.synchronizedMap(new Hashtable<Integer,ApprovedFont>());
	//
	private static final int maximalNumberOfIterations= 10000;
	//
	protected ScalablePanel mainPanel;
	protected GridBagLayout mainPanelLayout;
	//
	protected static final Term symbolDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	private AtomicBoolean dialogIsProven= new AtomicBoolean(false);
	private AtomicBoolean dialogIsSuspended= new AtomicBoolean(false);
	//
	protected java.util.Timer scheduler;
	protected TimerTask currentTask;
	//
	public AbstractDialog(Window w) {
		if (isToBeModal()) {
			dialogContainer= new ExtendedJDialog(this,w,ModalityType.DOCUMENT_MODAL);
			// dialogContainer.setModalityType(ModalityType.DOCUMENT_MODAL);
		} else {
			dialogContainer= new ExtendedJInternalFrame(this);
			// dialogContainer.setModalityType(ModalityType.MODELESS);
		};
		// <DEBUG:2010.08.19> super(w);
		// setOpaque(false);
		scheduler= new java.util.Timer(true);
	}
	//
	protected boolean isToBeModal() {
		return false;
	}
	protected String getPredefinedTitle() {
		return "";
	}
	protected Term getPredefinedTextColor() {
		return symbolDefault;
	}
	protected Term getPredefinedSpaceColor() {
		return symbolDefault;
	}
	protected Term getPredefinedFontName() {
		return symbolDefault;
	}
	protected Term getPredefinedFontSize() {
		return symbolDefault;
	}
	protected Term getPredefinedFontStyle() {
		return symbolDefault;
	}
	protected Term getPredefinedX() {
		return symbolDefault;
	}
	protected Term getPredefinedY() {
		return symbolDefault;
	}
	protected Term getPredefinedBackgroundColor() {
		return symbolDefault;
	}
	//
	public void setCurrentFontName(String name) {
		currentFontName.set(name);
		resetFontHash();
	}
	public void setCurrentFontSize(int size) {
		currentFontSize.set(size);
		resetFontHash();
	}
	public void setCurrentFontStyle(int style) {
		currentFontStyle.set(style);
		resetFontHash();
	}
	public void setCurrentFontUnderline(boolean flag) {
		currentFontUnderline.set(flag);
		resetFontHash();
	}
	protected void resetFontHash() {
		approvedFonts.clear();
		previousActualSize.set(new Dimension(-1,-1));
		// previousActualWidth.set(-1);
		// previousActualHeight.set(-1);
	}
	public Font getFont() {
		if (mainPanel==null) {
			return null;
		} else {
			return mainPanel.getFont();
		}
	}
	//
	public void initiate(ActiveWorld currentProcess, StaticContext context) {
		staticContext= context;
		dialogContainer.initiate(context);
		specialProcess.initiate(currentProcess,context);
	}
	//
	public void prepare(
			Dialog world,
			// Term sensitiveness,
			Term title,
			Term textColor,
			Term spaceColor,
			Term fontName,
			Term fontSize,
			Term fontStyle,
			Term x,
			Term y,
			Term backgroundColor,
			ChoisePoint iX) {
		// targetWorld= (Dialog)world;
		targetWorld= world;
		// boolean isSensitive= Converters.term2OnOff(sensitiveness,iX);
		String initialTitle= null;
		try {
			initialTitle= DialogUtils.termToDialogTitleSafe(title,iX);
		} catch (TermIsSymbolDefault e) {
			initialTitle= getPredefinedTitle();
		};
		Color initialTextColor= null;
		try {
			initialTextColor= GUI_Utils.termToColorSafe(textColor,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				initialTextColor= GUI_Utils.termToColorSafe(getPredefinedTextColor(),iX);
			} catch (TermIsSymbolDefault e2) {
				initialTextColor= defaultDialogTextColor;
			}
		};
		Color initialSpaceColor= null;
		try {
			initialSpaceColor= GUI_Utils.termToColorSafe(spaceColor,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				initialSpaceColor= GUI_Utils.termToColorSafe(getPredefinedSpaceColor(),iX);
			} catch (TermIsSymbolDefault e2) {
				initialSpaceColor= defaultDialogSpaceColor;
			}
		};
		Color initialSuccessBackgroundColor= null;
		try {
			initialSuccessBackgroundColor= GUI_Utils.termToColorSafe(backgroundColor,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				initialSuccessBackgroundColor= GUI_Utils.termToColorSafe(getPredefinedBackgroundColor(),iX);
			} catch (TermIsSymbolDefault e2) {
				initialSuccessBackgroundColor= defaultDialogSuccessBackgroundColor;
			}
		};
		Color initialFailureForegroundColor= null;
		try {
			initialFailureForegroundColor= GUI_Utils.termToColorSafe(DefaultOptions.failureDrawingForegroundColor,iX);
		} catch (TermIsSymbolDefault e1) {
			initialFailureForegroundColor= defaultDialogFailureForegroundColor;
		};
		Color initialFailureBackgroundColor= null;
		try {
			initialFailureBackgroundColor= GUI_Utils.termToColorSafe(DefaultOptions.failureDrawingBackgroundColor,iX);
		} catch (TermIsSymbolDefault e1) {
			initialFailureBackgroundColor= defaultDialogFailureBackgroundColor;
		};
		String initialFontName;
		try {
			initialFontName= GUI_Utils.termToFontNameSafe(fontName,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				initialFontName= GUI_Utils.termToFontName(getPredefinedFontName(),iX);
			} catch (TermIsSymbolDefault e2) {
				try {
					initialFontName= GUI_Utils.termToFontName(DefaultOptions.dialogFontName,iX);
				} catch (TermIsSymbolDefault e3) {
					initialFontName= defaultDialogFontName;
				}
			}
		};
		int initialFontSize;
		try {
			initialFontSize= GUI_Utils.termToFontSizeSafe(fontSize,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				initialFontSize= GUI_Utils.termToFontSize(getPredefinedFontSize(),iX);
			} catch (TermIsSymbolDefault e2) {
				try {
					initialFontSize= GUI_Utils.termToFontSize(DefaultOptions.dialogFontSize,iX);
				} catch (TermIsSymbolDefault e3) {
					initialFontSize= defaultDialogFontSize;
				}
			}
		};
		int initialFontStyle;
		boolean initialFontUnderline;
		try {
			initialFontStyle= GUI_Utils.termToFontStyleSafe(fontStyle,iX);
			initialFontUnderline= GUI_Utils.fontIsUnderlined(fontStyle,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				initialFontStyle= GUI_Utils.termToFontStyleSafe(getPredefinedFontStyle(),iX);
				initialFontUnderline= GUI_Utils.fontIsUnderlined(getPredefinedFontStyle(),iX);
			} catch (TermIsSymbolDefault e2) {
				try {
					initialFontStyle= GUI_Utils.termToFontStyleSafe(DefaultOptions.dialogFontStyle,iX);
					initialFontUnderline= GUI_Utils.fontIsUnderlined(DefaultOptions.dialogFontStyle,iX);
				} catch (TermIsSymbolDefault e3) {
					initialFontStyle= defaultDialogFontStyle;
					initialFontUnderline= defaultDialogFontUnderline;
				}
			}
		};
		ExtendedCoordinate eX= GUI_Utils.termToCoordinateSafe(x,iX);
		if (eX.isDefault()) {
			eX= GUI_Utils.termToCoordinateSafe(getPredefinedX(),iX);
			if (eX.isDefault()) {
				eX= defaultDialogX;
			}
		};
		ExtendedCoordinate eY= GUI_Utils.termToCoordinateSafe(y,iX);
		if (eY.isDefault()) {
			eY= GUI_Utils.termToCoordinateSafe(getPredefinedY(),iX);
			if (eY.isDefault()) {
				eY= defaultDialogY;
			}
		};
		actualCoordinates.set(new ExtendedCoordinates(eX,eY));
		// actualX.set(eX);
		// actualY.set(eY);
		if (initialFontName!=null) {
			currentFontName.set(initialFontName);
		};
		currentFontSize.set(initialFontSize);
		currentFontStyle.set(initialFontStyle);
		currentFontUnderline.set(initialFontUnderline);
		currentSuccessBackgroundColor.set(initialSuccessBackgroundColor);
		currentFailureForegroundColor.set(initialFailureForegroundColor);
		currentFailureBackgroundColor.set(initialFailureBackgroundColor);
		//
		assemble(iX);
		mainPanel.setTransparency(false);
		dialogContainer.setTitle(initialTitle);
		setNewAlarmColors(initialFailureForegroundColor,initialFailureBackgroundColor);
		Color refinedBackgroundColor= refineBackgroundColor(initialSuccessBackgroundColor);
		setNewBackground(refinedBackgroundColor);
		mainPanel.setHatchColor(hatchColor());
		dialogContainer.add(mainPanel);
	}
	//
	public abstract void assemble(ChoisePoint iX);
	//
	public void positionMainPanel() {
		if (isDisposed.get()) {
                	acceptNewPositionOfDialog();
		};
		try {
			Point point= dialogContainer.computePosition(actualCoordinates);
			previousCoordinates.set(point);
			// previousX.set(point.x);
			// previousY.set(point.y);
			safelySetLocation(point);
		} catch (ExtendedCoordinate.UseDefaultLocation e) {
			if (!dialogContainer.isShowing()) {
				// <DEBUG:2010.08.19>
				dialogContainer.setLocationByPlatform(true);
				isDisposed.set(true);
			}
		// } catch (CannotComputeDesktopSize e) {
		}
	}
	public void centreMainPanelIfNecessary() {
		try {
			// if (actualX.get().isCentered() || actualY.get().isCentered()) {
			if (actualCoordinates.get().areCentered()) {
				positionMainPanel();
			}
		} catch (ExtendedCoordinate.UseDefaultLocation e) {
		}
	}
	public void safelySetLocation(final Point p) {
		if (SwingUtilities.isEventDispatchThread()) {
			dialogContainer.setLocation(p);
			isDisposed.set(true);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						dialogContainer.setLocation(p);
						isDisposed.set(true);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void safelyRestoreSize() {
		positionMainPanel();
	}
	//
	public void implementPreferredSize() {
		//
		Font initialFont= create_new_font();
		setNewFont(initialFont);
		//
		// Dimension preferredSize= getUI().getPreferredSize(this);
		Dimension preferredSize= dialogContainer.getRealPreferredSize();
		// Dimension minimumSize= getUI().getMinimumSize(this);
		Dimension minimumSize= dialogContainer.getRealMinimumSize();
		//
		previousActualSize.set(
			new Dimension(
				StrictMath.max(preferredSize.width,minimumSize.width),
				StrictMath.max(preferredSize.height,minimumSize.height)));
		// previousActualWidth.set(StrictMath.max(preferredSize.width,minimumSize.width));
		// previousActualHeight.set(StrictMath.max(preferredSize.height,minimumSize.height));
		//
		safelySetSize(previousActualSize.get());
		// safelySetSize(new Dimension(previousActualWidth.get(),previousActualHeight.get()));
	}
	public void safelySetSize(final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			setDialogSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						setDialogSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void setDialogSize(Dimension size) {
		dialogContainer.setSize(size);
		// setNormalBounds(new Rectangle(0,0,size.width,size.height));
	}
	//
	public abstract void registerSlotVariables();
	protected abstract void setNewFont(Font commonFont);
	protected abstract void setNewBackground(Color c);
	protected abstract void setNewForeground(Color c);
	protected abstract void setNewSpaceColor(Color c);
	protected abstract void setNewAlarmColors(Color fc, Color bc);
	//
	public void registerPortsAndRecoverPortValues(AbstractDialog dialog, Dialog targetWorld, DialogEntry[] userDefinedSlots, DialogEntry[] systemSlots) {
		int numberOfUserDefinedSlots= userDefinedSlots.length;
		int numberOfSystemSlots= systemSlots.length;
		long numberOfEntries= userDefinedSlots.length + numberOfSystemSlots;
		// if (numberOfEntries > Integer.MAX_VALUE) {
		//	throw new IntegerValueIsTooBig();
		// } else {
			controlTable= new DialogEntry[PrologInteger.toInteger(numberOfEntries)];
			for (int n=0; n < numberOfUserDefinedSlots; n++) {
				controlTable[n]= userDefinedSlots[n];
			};
			for (int n=0; n < numberOfSystemSlots; n++) {
				controlTable[numberOfUserDefinedSlots+n]= systemSlots[n];
			};
			specialProcess.registerPortsAndRecoverPortValues(dialog,targetWorld,userDefinedSlots,systemSlots);
		// }
	}
	//
	public void start() {
		targetWorld.sendStateRequest(this,"");
		specialProcess.startProcesses();
	}
	//
	public void safelyAddAndDisplay(final JDesktopPane desktop, final ChoisePoint iX) {
		if (SwingUtilities.isEventDispatchThread()) {
			addAndDisplay(desktop,iX);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						addAndDisplay(desktop,iX);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void addAndDisplay(JDesktopPane desktop, ChoisePoint iX) {
		// insideModalDialog.incrementAndGet();
		// <DEBUG:2010.08.19> 
		// StaticDesktopAttributes staticAttributes= DesktopUtils.retrieveStaticDesktopAttributes(staticContext);
		// staticAttributes.desktop.add(this,JLayeredPane.MODAL_LAYER);
		dialogContainer.setClosable(true);
		dialogContainer.setResizable(true);
		// dialogContainer.setMaximizable(false);
		dialogContainer.setMaximizable(true);
		dialogContainer.setIconifiable(false);
		dialogContainer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		// <DEBUG:2010.08.19> setModal(false);
		dialogContainer.addComponentListener(this);
		//
		// desktop.add(this,DIALOG_LAYER);
		dialogContainer.addToDesktop(desktop);
		isInitiated.set(true);
		//
		modalChoisePoint= iX;
		// if (isToBeModal()) {
		//	setModalityType(ModalityType.DOCUMENT_MODAL);
		// } else {
		//	setModalityType(ModalityType.MODELESS);
		// };
		defineDefaultButton();
		dialogContainer.pack();
		implementPreferredSize();
		// dialogContainer.setVisible(true);
		positionMainPanel();
		dialogContainer.setVisible(true);
		// insideModalDialog.decrementAndGet();
	}
	//
	public void receiveInitiatingMessage() {
		specialProcess.receiveUserInterfaceMessage(null);
	}
	//
	public void safelyDisplay(final JDesktopPane desktop, final ChoisePoint iX) {
		if (SwingUtilities.isEventDispatchThread()) {
			display(desktop,iX);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						display(desktop,iX);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void display(JDesktopPane desktop, ChoisePoint iX) {
		if (isInitiated.get()) {
			modalChoisePoint= iX;
			// insideModalDialog.incrementAndGet();
			dialogContainer.setVisible(true);
			// insideModalDialog.decrementAndGet();
			dialogContainer.toFront();
		} else {
			addAndDisplay(desktop,iX);
		}
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
	public void safelyHide() {
		if (SwingUtilities.isEventDispatchThread()) {
			setInvisible();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						setInvisible();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public void setInvisible() {
		dialogContainer.setVisible(false);
	}
	//
	protected void defineDefaultButton() {
	}
	//
	// public void propertyChange(PropertyChangeEvent e) {
	// }
	//
	public void actionPerformed(ActionEvent e) {
		boolean isSystemCommand= false;
		boolean isTheResetCommand= false;
		boolean isTheVerifyCommand= false;
		String name= e.getActionCommand();
		// String predicateName= null;
		long domainSignature= -1;
		Term[] arguments= null;
		if (name.equals("close")) {
			dialogContainer.setVisible(false);
			// dispose();
			return;
		} else if (name.equals("verify")) {
			// predicateName= "verify";
			isSystemCommand= true;
			isTheVerifyCommand= true;
			arguments= new Term[0];
		} else if (name.equals("reset")) {
			// predicateName= "reset";
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
		if (isToBeModal()) {
			if (!isSystemCommand) {
				ChoisePoint newIx= new ChoisePoint(modalChoisePoint);
				// targetWorld.staticBinding(predicateName,newIx,arguments,new SuccessTermination());
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
			targetWorld.receiveAsyncCall(call);
		}
	}
	//
	public Term getFieldValue(Term identifier, ChoisePoint iX) {
		try {
			long code= identifier.getSymbolValue(iX);
			String slotName= identifier.toString(iX);
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry e= controlTable[i];
				if (e.isSlotName) {
					if (e.name.equals(slotName)) {
						return e.getVisibleValue();
					}
				}
			};
			throw new UnknownDialogSlotName();
		} catch (TermIsNotASymbol e1) {
			try {
				long number= identifier.getSmallIntegerValue(iX);
				for (int i= 0; i < controlTable.length; i++) {
					DialogEntry e= controlTable[i];
					if (!e.isSlotName && e.isNumericCode) {
						if (e.code==number) {
							return e.getVisibleValue();
						}
					}
				};
				throw new UnknownDialogEntryCode();
			} catch (TermIsNotAnInteger e2) {
				try {
					String name= identifier.getStringValue(iX);
					for (int i= 0; i < controlTable.length; i++) {
						DialogEntry e= controlTable[i];
						if (!e.isSlotName && !e.isNumericCode) {
							if (e.name.equals(name)) {
								return e.getVisibleValue();
							}
						}
					};
					throw new UnknownDialogEntryName();
				} catch (TermIsNotAString e3) {
					throw new WrongTermIsNotDialogEntry(identifier);
				}
			}
		}
	}
	public void putFieldValue(Term identifier, Term fieldValue, ChoisePoint iX) {
		fieldValue= fieldValue.dereferenceValue(iX);
		if (fieldValue.thisIsFreeVariable()) {	// || fieldValue.thisIsUnknownValue()) {
			// throw new Backtracking();
			return;
		};
		try {
			long code= identifier.getSymbolValue(iX);
			String slotName= identifier.toString(iX);
			for (int i= 0; i < controlTable.length; i++) {
				DialogEntry entry= controlTable[i];
				if (entry.isSlotName) {
					if (entry.name.equals(slotName)) {
						entry.putValue(fieldValue,iX);
						specialProcess.receiveUserInterfaceMessage(entry);
						return;
					}
				}
			};
			throw new UnknownDialogSlotName();
		} catch (TermIsNotASymbol e1) {
			try {
				long number= identifier.getSmallIntegerValue(iX);
				for (int i= 0; i < controlTable.length; i++) {
					DialogEntry e= controlTable[i];
					if (!e.isSlotName && e.isNumericCode) {
						if (e.code==number) {
							e.putValue(fieldValue,iX);
							return;
						}
					}
				};
				throw new UnknownDialogEntryCode();
			} catch (TermIsNotAnInteger e2) {
				try {
					String name= identifier.getStringValue(iX);
					for (int i= 0; i < controlTable.length; i++) {
						DialogEntry e= controlTable[i];
						if (!e.isSlotName && !e.isNumericCode) {
							if (e.name.equals(name)) {
								e.putValue(fieldValue,iX);
								return;
							}
						}
					};
					throw new UnknownDialogEntryName();
				} catch (TermIsNotAString e3) {
					throw new WrongTermIsNotDialogEntry(identifier);
				}
			}
		}
	}
	public void reportValueUpdate(ActiveComponent currentComponent) {
		boolean sendDialogValues= false;
		DialogEntry entry= null;
		for (int i= 0; i < controlTable.length; i++) {
			entry= controlTable[i];
			if (entry.isSlotName) {
				if (entry.component==currentComponent && entry.entryType==DialogEntryType.VALUE) {
					sendDialogValues= true;
					break;
				}
			}
		};
		if (sendDialogValues) {
			specialProcess.receiveUserInterfaceMessage(entry);
		}
	}
	public void transmitEntryValue(DialogEntry entry, ChoisePoint iX) {
		if (entry!=null && entry.isSlotName) {
			try {
				Term value= entry.refreshAndGetValue();
				String slotName= entry.name;
				for (int i= 0; i < controlTable.length; i++) {
					DialogEntry e2= controlTable[i];
					if (e2!=entry && e2.isSlotName) {
						if (e2.name.equals(slotName)) {
							e2.putValue(value,iX);
						}
					}
				}
			} catch (Backtracking b) {
			}
		}
	}
	//
	public void prepareAndSendFlowMessages() {
		specialProcess.phaseInitiation();
		for (int i= 0; i < controlTable.length; i++) {
			DialogEntry e= controlTable[i];
			if (e.isSlotName) {
				Term value= null;
				Term slot= targetWorld.getSlotByName(e.name);
				slot= slot.extractSlotVariable();
				if (slot.thisIsSlotVariable()) {
					SlotVariable slotVariable= (SlotVariable)slot;
					try {
						value= e.getExistedValue();
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
	// public void doLayout() {
	//	dialogContainer.doSuperLayout();
	// }
	public void doLayout() {
		doLayout(false);
	}
	public void doLayout(boolean forceCheck) {
		if (previousActualSize.get().width==0) {
			return;
		};
		Dimension dimension= new Dimension();
		dialogContainer.getSize(dimension);
		int givenWidth= dimension.width;
		int givenHeight= dimension.height;
		Dimension minimumSize= dialogContainer.getRealMinimumSize();
		int correctedGivenWidth= StrictMath.max(0,givenWidth-minimumSize.width);
		int correctedGivenHeight= StrictMath.max(0,givenHeight-minimumSize.height);
		boolean interruptLayout= false;
		boolean fontIsToBeModified= false;
		Dimension previousActualWidthAndHeight= previousActualSize.get();
		Dimension prefLS= dialogContainer.getRealPreferredSize();
		if (givenWidth < prefLS.width || givenHeight < prefLS.height) {
			forceCheck= true;
		};
		if (givenWidth!=previousActualWidthAndHeight.width || givenHeight!=previousActualWidthAndHeight.height || forceCheck) {
			// Dimension prefLS= null;
			int preferredWidth= 0;
			int preferredHeight= 0;
			// prefLS= dialogContainer.getRealPreferredSize();
			int maximalRefusedSize= 0;
			for (int i= 1; i <= maximalNumberOfIterations; i++) {
				preferredWidth= StrictMath.max(0,prefLS.width-minimumSize.width);
				preferredHeight= StrictMath.max(0,prefLS.height-minimumSize.height);
				double widthIncrease1= 1;
				double heightIncrease1= 1;
				double increaseCoefficient1= 1;
				if (preferredWidth > 0) {
					widthIncrease1= ((double)correctedGivenWidth) / preferredWidth;
					increaseCoefficient1= widthIncrease1;
				};
				if (preferredHeight > 0) {
					heightIncrease1= ((double)correctedGivenHeight) / preferredHeight;
					if (preferredWidth > 0) {
						increaseCoefficient1= Math.min(widthIncrease1,heightIncrease1);
					} else {
						increaseCoefficient1= heightIncrease1;
					}
				};
				int previousFontSize= currentFontSize.get();
				int prospectiveFontSize= previousFontSize;
				if (increaseCoefficient1 < 1) {
					if (maximalRefusedSize > 0) {
						maximalRefusedSize= StrictMath.min(maximalRefusedSize,prospectiveFontSize);
					} else {
						maximalRefusedSize= prospectiveFontSize;
					};
					prospectiveFontSize= (int)StrictMath.floor((double)prospectiveFontSize * increaseCoefficient1);
				} else {
					prospectiveFontSize= (int)StrictMath.ceil((double)prospectiveFontSize * increaseCoefficient1);
					if (maximalRefusedSize > 0) {
						prospectiveFontSize= StrictMath.min(prospectiveFontSize,maximalRefusedSize-1);
					}
				};
				prospectiveFontSize= Math.max(prospectiveFontSize,1);
				currentFontSize.set(prospectiveFontSize);
				if (approvedFonts.containsKey(prospectiveFontSize)) {
					prefLS= approvedFonts.get(prospectiveFontSize).preferredSize;
					fontIsToBeModified= true;
				} else {
					Font commonFont= create_new_font();
					setNewFont(commonFont);
					prefLS= dialogContainer.getRealPreferredSize();
					approvedFonts.put(prospectiveFontSize,new ApprovedFont(commonFont,prefLS));
				};
				dialogContainer.getSize(dimension);
				if ( (dimension.width != givenWidth) || (dimension.height != givenHeight) ) {
					interruptLayout= true;
					break;
				};
				if (previousFontSize==prospectiveFontSize) {
					break;
				}
			};
			if (!interruptLayout) {
				previousActualSize.set(new Dimension(givenWidth,givenHeight));
				if (fontIsToBeModified) {
					Font commonFont= approvedFonts.get(currentFontSize.get()).font;
					setNewFont(commonFont);
				};
			};
			centreMainPanelIfNecessary();
		};
		dialogContainer.doSuperLayout();
	}
	//
	protected Font create_new_font() {
		return create_new_font(currentFontName.get(),currentFontStyle.get(),currentFontUnderline.get(),currentFontSize.get());
	}
	protected Font create_new_font(String fontName, int fontStyle, boolean fontUnderline, int fontSize) {
		Map<TextAttribute,Object> map= new Hashtable<TextAttribute,Object>();
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
		// map.put(TextAttribute.WIDTH,TextAttribute.WIDTH_EXTENDED);
		// map.put(TextAttribute.WIDTH,TextAttribute.WIDTH_SEMI_EXTENDED);
		// map.put(TextAttribute.WIDTH,TextAttribute.WIDTH_REGULAR);
		map.put(TextAttribute.SIZE,currentFontSize.get());
		return new Font(map);
	}
	//
	public void componentHidden(ComponentEvent event) {
		// Invoked when the component has been made invisible.
	}
	public void componentMoved(ComponentEvent event) {
		dialogContainer.repaintParent();
		acceptNewPositionOfDialog();
	}
	public void componentResized(ComponentEvent event) {
		dialogContainer.repaintParent();
		acceptNewPositionOfDialog();
		specialProcess.receiveUserInterfaceMessage(findDialogEntryFontSize());
	}
	public void componentShown(ComponentEvent event) {
		// Invoked when the component has been made visible.
	}
	//
	public void acceptNewPositionOfDialog() {
		// try {
			Dimension parentLayoutSize= dialogContainer.computeParentLayoutSize();
			Point p= dialogContainer.getLocation();
			Point previousPoint= previousCoordinates.get();
			if (previousPoint==null) {
				double gridX= DefaultOptions.gridWidth;
				double newX= (double)p.x / ( (double)parentLayoutSize.width / gridX );
				double gridY= DefaultOptions.gridHeight;
				double newY= (double)p.y / ( (double)parentLayoutSize.height / gridY );
				actualCoordinates.set(new ExtendedCoordinates(newX,newY));
				specialProcess.receiveUserInterfaceMessage(findDialogEntryX());
				specialProcess.receiveUserInterfaceMessage(findDialogEntryY());
			} else if (previousPoint.x!=p.x || previousPoint.y!=p.y) {
				double newX= previousPoint.x;
				double newY= previousPoint.y;
				if (p.x!=previousPoint.x) {
					double gridX= DefaultOptions.gridWidth;
					newX= (double)p.x / ( (double)parentLayoutSize.width / gridX );
				};
				if (p.y!=previousPoint.y) {
					double gridY= DefaultOptions.gridHeight;
					newY= (double)p.y / ( (double)parentLayoutSize.height / gridY );
				};
				actualCoordinates.set(new ExtendedCoordinates(newX,newY));
				if (p.x!=previousPoint.x) {
					specialProcess.receiveUserInterfaceMessage(findDialogEntryX());
				};
				if (p.y!=previousPoint.y) {
					specialProcess.receiveUserInterfaceMessage(findDialogEntryY());
				}
			}
		// } catch (CannotComputeDesktopSize e) {
		// }
	}
	//
	public DialogEntry findDialogEntryX() {
		for (int i= 0; i < controlTable.length; i++) {
			DialogEntry entry= controlTable[i];
			if (entry.isSlotName) {
				if (entry.entryType==DialogEntryType.X) {
					return entry;
				}
			}
		};
		return null;
	}
	public DialogEntry findDialogEntryY() {
		for (int i= 0; i < controlTable.length; i++) {
			DialogEntry entry= controlTable[i];
			if (entry.isSlotName) {
				if (entry.entryType==DialogEntryType.Y) {
					return entry;
				}
			}
		};
		return null;
	}
	public DialogEntry findDialogEntryFontSize() {
		for (int i= 0; i < controlTable.length; i++) {
			DialogEntry entry= controlTable[i];
			if (entry.isSlotName) {
				if (entry.entryType==DialogEntryType.FONT_SIZE) {
					return entry;
				}
			}
		};
		return null;
	}
	//
	public void rememberStateOfProcess(String identifier, boolean isProven, boolean isSuspended) {
		synchronized(dialogIsProven) {
			if (dialogIsProven.get()==isProven && dialogIsSuspended.get()==isSuspended) {
				return;
			};
			dialogIsProven.set(isProven);
			dialogIsSuspended.set(isSuspended);
			Color refinedBackgroundColor= refineBackgroundColor(currentSuccessBackgroundColor.get());
			setNewBackground(refinedBackgroundColor);
			mainPanel.setHatchColor(hatchColor());
		};
		safelyRepaint();
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
	public BigInteger getDepthCounter() {
		synchronized(insideModalDialog) {
			return insideModalDialog;
		}
	}
	public Color refineBackgroundColor(Color initialColor) {
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
	public Color hatchColor() {
		BigInteger modalDialogCounter= getDepthCounter();
		// if (modalDialogCounter <= 0) {
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
	// public boolean isVisible() {
	//	return dialogContainer.isVisible();
	// }
	public boolean isShowing() {
		return dialogContainer.isShowing();
	}
	public void setLocation(Point p) {
		dialogContainer.setLocation(p);
		isDisposed.set(true);
	}
	public Point getLocation() {
		return dialogContainer.getLocation();
	}
	public Dimension getSize() {
		return dialogContainer.getSize();
	}
	public void setTitle(String title) {
		dialogContainer.setTitle(title);
	}
	public String getTitle() {
		return dialogContainer.getTitle();
	}
	public Color getForeground() {
		return dialogContainer.getForeground();
	}
	public void setBackground(Color c) {
		dialogContainer.setBackground(c);
	}
	public Color getBackground() {
		return dialogContainer.getBackground();
	}
	public void invalidate() { // 2012.03.05
		dialogContainer.invalidate();
	}
	public void validate() {
		dialogContainer.validate();
	}
	public void revalidate() {
		dialogContainer.revalidate();
	}
	public void repaint() {
		dialogContainer.repaint();
	}
	public void repaintAfterDelay() {
		synchronized(this) {
			if (currentTask != null) {
				currentTask.cancel();
			};
			currentTask= new LocalTask(this);
			scheduler.schedule(currentTask,0,1);
		}
	}
	public Dimension computeParentLayoutSize() {
		return dialogContainer.computeParentLayoutSize();
	}
	public void sendResidentRequest(Resident resident, String tableName, ChoisePoint iX) {
		if (targetWorld!=null) {
			long domainSignature= targetWorld.entry_f_DataRequest_1_i();
			Term[] argumentList= new Term[1];
			argumentList[0]= new PrologString(tableName);
			targetWorld.sendResidentRequest(resident,domainSignature,argumentList,false,iX);
		}
	}
	public long get_data_request_signature() {
		return targetWorld.entry_f_DataRequest_1_i();
	}
	public void withdrawRequest(Resident resident) {
		if (targetWorld!=null) {
			targetWorld.withdrawRequest(resident);
		}
	}
}

class LocalTask extends TimerTask {
	private AbstractDialog targetDialog;
	private AtomicInteger counter= new AtomicInteger(100);
	//
	public LocalTask(AbstractDialog target) {
		targetDialog= target;
	}
	//
	public void run() {
		int c= counter.getAndDecrement();
		if (c <= 0) {
			cancel();
		} else {
			// targetDialog.doLayout(true);
			targetDialog.repaint();
		}
	}
}
