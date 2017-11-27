// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.system.errors.*;
import morozov.system.gui.errors.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.Box;
import java.util.ArrayList;

public class TranslatedMenuItem {
	public String name= "";
	public String accelerator= "";
	public TranslatedMenuItem[] submenu= null;
	public boolean isSeparator= false;
	public boolean isSpacer= false;
	protected static Term termSeparator= new PrologSymbol(SymbolCodes.symbolCode_E_separator);
	protected static Term termSpacer= new PrologSymbol(SymbolCodes.symbolCode_E_spacer);
	//
	///////////////////////////////////////////////////////////////
	//
	public TranslatedMenuItem(String text) {
		name= text;
	}
	public TranslatedMenuItem(String n, String a) {
		name= n;
		accelerator= a;
	}
	public TranslatedMenuItem(boolean spacerFlag) {
		isSeparator= !spacerFlag;
		isSpacer= spacerFlag;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setSubmenu(TranslatedMenuItem[] array) {
		submenu= array;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static TranslatedMenuItem[] argumentToTranslatedMenuItems(Term value, ChoisePoint iX) {
		ArrayList<TranslatedMenuItem> vector= new ArrayList<TranslatedMenuItem>();
		term2TranslatedMenuItems(vector,true,value,iX);
		return vector.toArray(new TranslatedMenuItem[vector.size()]);
	}
	protected static void term2TranslatedMenuItems(ArrayList<TranslatedMenuItem> vector, boolean isMenuBar, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			while(true) {
				Term value= tail.getNextListHead(iX);
				term2TranslatedMenuItem(vector,isMenuBar,value,iX);
				tail= tail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			tail= tail.dereferenceValue(iX);
			if (tail.thisIsFreeVariable()) {
				return;
			};
			if (!tail.thisIsUnknownValue()) {
				TranslatedMenuItem item= new TranslatedMenuItem(tail.toString(iX));
				vector.add(item);
			}
		}

	}
	protected static void term2TranslatedMenuItem(ArrayList<TranslatedMenuItem> vector, boolean isMenuBar, Term value, ChoisePoint iX) {
		try {
			String menuActionName= value.getStringValue(iX);
			int p1= menuActionName.indexOf('\t');
			if (p1 >= 0) {
				menuActionName= menuActionName.substring(0,p1);
			};
			TranslatedMenuItem item= new TranslatedMenuItem(menuActionName);
			vector.add(item);
		} catch (TermIsNotAString e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_separator) {
					if (!isMenuBar) {
						vector.add(new TranslatedMenuItem(false));
					}
				} else if (code==SymbolCodes.symbolCode_E_spacer) {
					if (isMenuBar) {
						vector.add(new TranslatedMenuItem(true));
					}
				} else {
					throw new WrongArgumentIsNotMenuItem(value);
				}
			} catch (TermIsNotASymbol e2) {
				try {
					Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_item,2,iX);
					try {
						String menuActionName= arguments[0].getStringValue(iX);
						int p1= menuActionName.indexOf('\t');
						if (p1 >= 0) {
							menuActionName= menuActionName.substring(0,p1);
						};
						try {
							String acceleratorDefinition= arguments[1].getStringValue(iX);
							TranslatedMenuItem item= new TranslatedMenuItem(menuActionName,acceleratorDefinition);
							vector.add(item);
						} catch (TermIsNotAString e3) {
							throw new WrongArgumentIsNotAString(arguments[1]);
						}
					} catch (TermIsNotAString e4) {
						throw new WrongArgumentIsNotAString(arguments[0]);
					}
				} catch (Backtracking b1) {
					try {
						Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_submenu,2,iX);
						try {
							String menuActionName= arguments[0].getStringValue(iX);
							int p1= menuActionName.indexOf('\t');
							if (p1 >= 0) {
								menuActionName= menuActionName.substring(0,p1);
							};
							TranslatedMenuItem item= new TranslatedMenuItem(menuActionName);
							ArrayList<TranslatedMenuItem> sublist= new ArrayList<TranslatedMenuItem>();
							term2TranslatedMenuItems(sublist,false,arguments[1],iX);
							item.setSubmenu(sublist.toArray(new TranslatedMenuItem[sublist.size()]));
							vector.add(item);
						} catch (TermIsNotAString e5) {
							throw new WrongArgumentIsNotAString(arguments[0]);
						}
					} catch (Backtracking b2) {
						throw new WrongArgumentIsNotMenuItem(value);
					}
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static JMenuBar argumentToJMenuBar(TranslatedMenuItem[] value, ActionListener listener) {
		JMenuBar mb= new JMenuBar();
		term2JMenu(mb,null,true,listener,value);
		return mb;
	}
	protected static void term2JMenu(JMenuBar bar, JMenu menu, boolean isMenuBar, ActionListener listener, TranslatedMenuItem[] list) {
		for (int n=0; n < list.length; n++) {
			term2JMenuItem(bar,menu,isMenuBar,listener,list[n]);
		}
	}
	protected static void term2JMenuItem(JMenuBar bar, JMenu menu, boolean isMenuBar, ActionListener listener, TranslatedMenuItem value) {
		if (value.isSeparator) {
			if (!isMenuBar) {
				menu.addSeparator();
			}
		} else if (value.isSpacer) {
			if (isMenuBar) {
				bar.add(Box.createHorizontalGlue());
			}
		} else {
			String menuActionName= value.name;
			TranslatedMenuItem[] sublist= value.submenu;
			if (sublist==null || sublist.length <= 0) {
				JMenuItem item= createNamedJMenuItem(menuActionName);
				String acceleratorDefinition= value.accelerator;
				if (!acceleratorDefinition.isEmpty()) {
					KeyStroke keyStroke= KeyStroke.getKeyStroke(acceleratorDefinition);
					if (keyStroke != null) {
						item.setAccelerator(keyStroke);
					}
				};
				item.addActionListener(listener);
				if (isMenuBar) {
					bar.add(item);
				} else {
					menu.add(item);
				}
			} else {
				JMenu submenu= createNamedJMenu(menuActionName);
				term2JMenu(null,submenu,false,listener,sublist);
				if (isMenuBar) {
					bar.add(submenu);
				} else {
					menu.add(submenu);
				}
			}
		}
	}
	protected static JMenuItem createNamedJMenuItem(String text) {
		AnnotatedButton button= AnnotatedButton.interpretAmpersands(text);
		JMenuItem item= new JMenuItem(button.getName());
		if (button.hasMnemonicIndex()) {
			item.setMnemonic(button.getMnemonicCode());
			try {
				item.setDisplayedMnemonicIndex(button.getMnemonicIndex());
			} catch (IllegalArgumentException e) {
			}
		};
		return item;
	}
	protected static JMenu createNamedJMenu(String text) {
		AnnotatedButton button= AnnotatedButton.interpretAmpersands(text);
		JMenu menu= new JMenu(button.getName());
		if (button.hasMnemonicIndex()) {
			menu.setMnemonic(button.getMnemonicCode());
			try {
				menu.setDisplayedMnemonicIndex(button.getMnemonicIndex());
			} catch (IllegalArgumentException e) {
			}
		};
		return menu;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term translatedMenuItemsToTerm(TranslatedMenuItem[] items) {
		Term result= PrologEmptyList.instance;
		for (int n=items.length-1; n >= 0; n--) {
			TranslatedMenuItem item= items[n];
			result= new PrologList(item.toTerm(),result);
		};
		return result;
	}
	public Term toTerm() {
		if (isSeparator) {
			return termSeparator;
		} else if (isSpacer) {
			return termSpacer;
		} else {
			if (submenu==null || submenu.length<=0) {
				if (accelerator.isEmpty()) {
					return new PrologString(name);
				} else {
					Term[] arguments= new Term[2];
					arguments[0]= new PrologString(name);
					arguments[1]= new PrologString(accelerator);
					return new PrologStructure(SymbolCodes.symbolCode_E_item,arguments);
				}
			} else {
				Term[] arguments= new Term[2];
				arguments[0]= new PrologString(name);
				arguments[1]= translatedMenuItemsToTerm(submenu);
				return new PrologStructure(SymbolCodes.symbolCode_E_submenu,arguments);
			}
		}
	}
}
