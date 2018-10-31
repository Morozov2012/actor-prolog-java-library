/*
 * @(#)ScalableComboBox.java 1.0 2009/11/23
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableComboBox implementation for the Actor Prolog language
 * @version 1.0 2009/11/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.JComboBox;
import javax.swing.ListModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.ComponentUI;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import java.util.ArrayList;

public class ScalableComboBox extends ActiveComponent implements ActionListener, DocumentListener {
	//
	protected boolean isEditable= false;
	protected boolean enableSorting= false;
	protected SortedStrings sortedStrings;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalableComboBox(AbstractDialog tD, String[] items, double visibleRowCount, double visibleColumnCount, boolean enableSorting) {
		this(tD,items,visibleRowCount,visibleColumnCount,true,enableSorting);
	}
	public ScalableComboBox(AbstractDialog tD, String[] items, double visibleRowCount, double visibleColumnCount, boolean useEditor, boolean sortList) {
		super(tD);
		isEditable= useEditor;
		enableSorting= sortList;
		if (enableSorting) {
			// Arrays.sort(items,new AlphabeticComparator());
			sortedStrings= new SortedStrings(items);
			items= sortedStrings.sortedArray;
		};
		JComboBox<String> comboBox= new JComboBox<String>(items);
		component= comboBox; // new JComboBox<String>(items);
		comboBox.setPrototypeDisplayValue(createPrototypeString(PrologInteger.toInteger(visibleColumnCount)));
		comboBox.setMaximumRowCount(PrologInteger.toInteger(visibleRowCount));
		// if (useTabStops) {
		//	TabListCellRenderer renderer= new TabListCellRenderer(items);
		//	comboBox.setRenderer(renderer);
		// };
		// comboBox.addKeyListener(new ListSearcher(comboBox));
		if (isEditable) {
			Component textField= comboBox.getEditor().getEditorComponent();
			if (textField instanceof JTextField) {
				((JTextField)textField).getDocument().addDocumentListener(this);
			} else {
				comboBox.addActionListener(this);
			}
		} else {
			comboBox.addActionListener(this);
		};
		// comboBox.addItemListener(this);
		// comboBox.addListSelectionListener(component);
		comboBox.setEditable(isEditable);
		comboBox.addFocusListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw RejectValue.instance;
		} else {
			ArrayList<Term> items= DialogUtils.listToTermArray(value,iX);
			// return GeneralConverters.arrayListToTerm(items);
			if (items.size() >= 1) {
				return items.get(items.size()-1);
			} else {
				return new PrologString("");
			}
		}
	}
	//
	public Term standardizeRange(Term value, ChoisePoint iX) {
		ArrayList<String> items= DialogUtils.listToStringArray(value,iX);
		return GeneralConverters.stringArrayToList(items);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			synchronized (component) {
				ListModel model= ((JComboBox)component).getModel();
				try {
					int number= value.getSmallIntegerValue(iX);
					if (number > 0 && number <= model.getSize()) {
						int index= number-1;
						if (enableSorting) {
							index= sortedStrings.resolveIndex(index);
						};
						((JComboBox)component).setSelectedIndex(index);
					} else if (number <= 0) {
						((JComboBox)component).setSelectedIndex(-1);
					}
				} catch (TermIsNotAnInteger e1) {
					try {
						long code= value.getSymbolValue(iX);
						String text= value.toString(iX);
						boolean elementIsFound= false;
						for (int n=0; n <= model.getSize()-1; n++) {
							if (model.getElementAt(n).toString().equals(text)) {
								((JComboBox)component).setSelectedIndex(n);
								elementIsFound= true;
								break;
							}
						};
						if (!elementIsFound) {
							((JComboBox)component).setSelectedItem(text);
						}
					} catch (TermIsNotASymbol e2) {
						try {
							String text= value.getStringValue(iX);
							boolean elementIsFound= false;
							for (int n=0; n <= model.getSize()-1; n++) {
								if (model.getElementAt(n).toString().equals(text)) {
									((JComboBox)component).setSelectedIndex(n);
									elementIsFound= true;
									break;
								}
							};
							if (!elementIsFound) {
								((JComboBox)component).setSelectedItem(text);
							}
						} catch (TermIsNotAString e3) {
							((JComboBox)component).setSelectedItem(value.toString(iX));
						}
					}
				}
			}
		}
	}
	//
	public void putRange(Term value, ChoisePoint iX) {
		if (component!=null) {
			synchronized (component) {
				addListOfItems(value,iX);
			}
		}
	}
	//
	public Term getValue() {
		if (component!=null) {
			synchronized (component) {
				if (isEditable) {
					Component textField= ((JComboBox)component).getEditor().getEditorComponent();
					if (textField instanceof JTextField) {
						try {
							String text= ((JTextField)textField).getText();
							if (text != null) {
								return new PrologString(text);
							} else {
								// return PrologUnknownValue.instance;
								return new PrologString("");
							}
						} catch (NullPointerException e) {
							// return PrologUnknownValue.instance;
							return new PrologString("");
						}
					} else {
						Object selectedValue= ((JComboBox)component).getSelectedItem();
						if (selectedValue==null) {
							// return PrologEmptyList.instance;
							// return PrologUnknownValue.instance;
							return new PrologString("");
						} else {
							return new PrologString(selectedValue.toString());
						}
					}
				} else {
					Object selectedValue= ((JComboBox)component).getSelectedItem();
					if (selectedValue==null) {
						// return PrologUnknownValue.instance;
						return PrologEmptyList.instance;
					} else {
						return new PrologString(selectedValue.toString());
					}
				}
			}
		} else {
			// return PrologEmptyList.instance;
			return PrologUnknownValue.instance;
		}
	}
	//
	public Term getRange() {
		if (component!=null) {
			synchronized (component) {
				ListModel model= ((JComboBox)component).getModel();
				Term result= PrologEmptyList.instance;
				for (int n=model.getSize()-1; n>=0; n--) {
					result= new PrologList(new PrologString(model.getElementAt(n).toString()),result);
				};
				return result;
			}
		} else {
			// return PrologEmptyList.instance;
			return PrologUnknownValue.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setBackground(Color c) {
		super.setBackground(c);
		if (component!=null) {
			ComponentUI cui= ((JComboBox)component).getUI();
			if (cui instanceof ScalableComboBoxUI) {
				ScalableComboBoxUI scbui= (ScalableComboBoxUI)cui;
				scbui.setBackground(c);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setEditable(boolean flag) {
		if (component!=null) {
			((JComboBox)component).setEditable(flag);
		}
	}
	public void setSelectedIndex(int index) {
		if (component!=null) {
			if (enableSorting) {
				index= sortedStrings.resolveIndex(index);
			};
			((JComboBox)component).setSelectedIndex(index);
		}
	}
	//
	protected String createPrototypeString(int delta) {
		StringBuilder buffer= new StringBuilder(0);
		for (int j=0; j < delta; j++) {
			buffer= buffer.append("M");
		};
		return buffer.toString();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@SuppressWarnings("unchecked")
	protected void addListOfItems(Term list, ChoisePoint iX) {
		String[] items= GeneralConverters.termToStrings(list,iX);
		if (enableSorting) {
			// Arrays.sort(items,new AlphabeticComparator());
			sortedStrings= new SortedStrings(items);
			items= sortedStrings.sortedArray;
		};
		JComboBox<String> comboBox= (JComboBox<String>)component;
		// ((JComboBox)component).removeAllItems();
		comboBox.removeAllItems();
		for (int n=0; n < items.length; n++) {
			// ((JComboBox)component).addItem(makeObj(items[n]));
			// ((JComboBox)component).addItem(items[n]);
			comboBox.addItem(items[n]);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void changedUpdate(DocumentEvent e) {
		// Gives notification that an attribute or set of attributes changed.
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	public void insertUpdate(DocumentEvent e) {
		// Gives notification that there was an insert into the document.
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	public void removeUpdate(DocumentEvent e) {
		// Gives notification that a portion of the document has been removed.
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void actionPerformed(ActionEvent event) {
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(this);
		}
	}
}
