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
	public static TranslatedMenuItem[] termToTranslatedMenuItems(Term value, ChoisePoint iX) {
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
	public static JMenuBar termToJMenuBar(TranslatedMenuItem[] value, ActionListener listener) {
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
		AnnotatedButton button= interpretAmpersands(text);
		JMenuItem item= new JMenuItem(button.name);
		if (button.mnemonicIndex >= 0) {
			item.setMnemonic(button.mnemonicCode);
			try {
				item.setDisplayedMnemonicIndex(button.mnemonicIndex);
			} catch (IllegalArgumentException e) {
			}
		};
		return item;
	}
	protected static JMenu createNamedJMenu(String text) {
		AnnotatedButton button= interpretAmpersands(text);
		JMenu menu= new JMenu(button.name);
		if (button.mnemonicIndex >= 0) {
			menu.setMnemonic(button.mnemonicCode);
			try {
				menu.setDisplayedMnemonicIndex(button.mnemonicIndex);
			} catch (IllegalArgumentException e) {
			}
		};
		return menu;
	}
	protected static AnnotatedButton interpretAmpersands(String text) {
		int mnemonicCode= -1;
		int mnemonicIndex= -1;
		char[] characters= text.toCharArray();
		int textLength= characters.length;
		StringBuilder buffer= new StringBuilder();
		boolean processSupplementaryCharacter= false;
		int position= 0;
		char c1;
		char c2;
		int code;
		while(true) {
			if (position >= textLength) {
				break;
			} else {
				c1= characters[position];
				if (	(position + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[position+1])) {
					c2= characters[position+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
			if (code == '&') {
				if (position + 1 <= textLength - 1) {
					if (characters[position+1]=='&') {
						buffer.append(c1);
						position= position + 2;
						continue;
					} else {
						if (mnemonicIndex==-1) {
							mnemonicCode= characters[position+1];
							mnemonicIndex= position;
						};
						position++;
						continue;
					}
				} else {
					position++;
					continue;
				}
			} else {
				if (processSupplementaryCharacter) {
					buffer.append(c1);
					buffer.append(c2);
					position= position + 2;
				} else {
					buffer.append(c1);
					position++;
				}
			}
		};
		return new AnnotatedButton(buffer.toString(),mnemonicCode,mnemonicIndex);
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
