// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.system.gui.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.KeyStroke;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;


public class MenuUtils {
	public static JMenuBar termToJMenuBar(Term value, ActionListener listener, ChoisePoint iX) {
		JMenuBar mb= new JMenuBar();
		term2JMenu(mb,null,true,listener,value,iX);
		return mb;
	}
	protected static void term2JMenu(JMenuBar bar, JMenu menu, boolean isMenuBar, ActionListener listener, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			while(true) {
				Term value= tail.getNextListHead(iX);
				term2JMenuItem(bar,menu,isMenuBar,listener,value,iX);
				tail= tail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			tail= tail.dereferenceValue(iX);
			if (tail.thisIsFreeVariable()) {
				return;
			};
			if (!tail.thisIsUnknownValue()) {
				JMenuItem item= new JMenuItem(tail.toString(iX));
				item.addActionListener(listener);
				if (isMenuBar) {
					bar.add(item);
				} else {
					menu.add(item);
				}
			}
		}

	}
	protected static void term2JMenuItem(JMenuBar bar, JMenu menu, boolean isMenuBar, ActionListener listener, Term value, ChoisePoint iX) {
		try {
			String menuActionName= value.getStringValue(iX);
			JMenuItem item= createNamedJMenuItem(menuActionName);
			item.addActionListener(listener);
			if (isMenuBar) {
				bar.add(item);
			} else {
				menu.add(item);
			}
		} catch (TermIsNotAString e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_separator) {
					if (!isMenuBar) {
						menu.addSeparator();
					}
				} else if (code==SymbolCodes.symbolCode_E_spacer) {
					if (isMenuBar) {
						bar.add(Box.createHorizontalGlue());
					}
				} else {
					throw new WrongTermIsNotMenuItem(value);
				}
			} catch (TermIsNotASymbol e2) {
				try {
					Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_item,2,iX);
					try {
						String menuActionName= arguments[0].getStringValue(iX);
						try {
							String acceleratorDefinition= arguments[1].getStringValue(iX);
							JMenuItem item= createNamedJMenuItem(menuActionName);
							KeyStroke keyStroke= KeyStroke.getKeyStroke(acceleratorDefinition);
							if (keyStroke != null) {
								item.setAccelerator(keyStroke);
							};
							item.addActionListener(listener);
							if (isMenuBar) {
								bar.add(item);
							} else {
								menu.add(item);
							}
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
							JMenu submenu= createNamedJMenu(menuActionName);
							term2JMenu(null,submenu,false,listener,arguments[1],iX);
							if (isMenuBar) {
								bar.add(submenu);
							} else {
								menu.add(submenu);
							}
						} catch (TermIsNotAString e5) {
							throw new WrongArgumentIsNotAString(arguments[0]);
						}
					} catch (Backtracking b2) {
						throw new WrongTermIsNotMenuItem(value);
					}
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
	public static Term mouseEvent2Term(MouseEvent ev, int windowWidth, int windowHeight, int sizeFactor, long mouseTypeCode) {
		//
		Term value= PrologEmptySet.instance;
		long flag= SymbolCodes.symbolCode_E_no;
		//
		if (ev.isShiftDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isShiftDown,new PrologSymbol(flag),value);
		//
		if (ev.isMetaDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isMetaDown,new PrologSymbol(flag),value);
		//
		if (ev.isControlDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isControlDown,new PrologSymbol(flag),value);
		//
		if (ev.isAltGraphDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isAltGraphDown,new PrologSymbol(flag),value);
		//
		if (ev.isAltDown()) {
			flag= SymbolCodes.symbolCode_E_yes;
		} else {
			flag= SymbolCodes.symbolCode_E_no;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_isAltDown,new PrologSymbol(flag),value);
		//
		long when= ev.getWhen();
		value= new PrologSet(-SymbolCodes.symbolCode_E_when,new PrologInteger(when),value);
		//
		double x= ev.getX();
		if (sizeFactor < 0) {
			x= x / windowWidth;
		} else {
			x= x / sizeFactor;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_x,new PrologReal(x),value);
		//
		double y= ev.getY();
		if (sizeFactor < 0) {
			y= y / windowHeight;
		} else {
			y= y / sizeFactor;
		};
		value= new PrologSet(-SymbolCodes.symbolCode_E_y,new PrologReal(y),value);
		//
		int count= ev.getClickCount();
		value= new PrologSet(-SymbolCodes.symbolCode_E_clickCount,new PrologInteger(count),value);
		//
		int button= ev.getButton();
		value= new PrologSet(-SymbolCodes.symbolCode_E_button,new PrologInteger(button),value);
		//
		value= new PrologSet(-UnderdeterminedSet.keyNameCode,new PrologSymbol(mouseTypeCode),value);
		//
		return value;
	}
}
