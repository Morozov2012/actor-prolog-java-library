/*
 * @(#)AList.java 1.0 2009/12/29
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * AList implementation for the Actor Prolog language
 * @version 1.0 2009/12/29
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class AList extends JScrollPane {
	//
	public JList<String> list= null;
	protected AbstractDialog targetDialog= null;
	protected ActiveComponent targetComponent= null;
	protected int length;
	protected boolean enableSorting;
	protected boolean useTabStops;
	protected TabListCellRenderer renderer;
	//
	public AList(AbstractDialog tD, ActiveComponent tC, String[] stringList, double visibleRowCount, double visibleColumnCount, boolean mode1, boolean mode2) {
		targetDialog= tD;
		targetComponent= tC;
		length= (int)StrictMath.round(visibleColumnCount);
		enableSorting= mode1;
		useTabStops= mode2;
		if (enableSorting) {
			Arrays.sort(stringList,new AlphabeticComparator());
		};
		list= new JList<String>(stringList);
		// list.setModel(new DefaultListModel());
		// list= new JJList(stringList);
		setFixedWidth(list.getFont());
		list.setVisibleRowCount((int)StrictMath.round(visibleRowCount));
		list.setLayoutOrientation(JList.VERTICAL);
		setViewportView(list);
		if (useTabStops) {
			renderer= new TabListCellRenderer(stringList);
			list.setCellRenderer(renderer);
		};
		list.addKeyListener(new ListSearcher(list));
		list.addListSelectionListener(targetComponent);
		// list.getModel().addListDataListener(targetComponent);
	}
	//
	public void setLayoutOrientation(int layoutOrientation) {
		if (list!=null) {
			list.setLayoutOrientation(layoutOrientation);
		}
	}
	//
	public void setSelectionMode(int selectionMode) {
		if (list!=null) {
			list.setSelectionMode(selectionMode);
		}
	}
	public void setSelectedIndex(int index) {
		if (list!=null) {
			list.setSelectedIndex(index);
		}
	}
	public void setSelectedIndices(int[] indices) {
		if (list!=null) {
			list.setSelectedIndices(indices);
		}
	}
	//
	public void setVisibleRowCount(int visibleRowCount) {
		if (list!=null) {
			list.setVisibleRowCount(visibleRowCount);
		}
	}
	//
	public void setFont(Font font) {
		if (list!=null) {
			list.setFont(font);
			super.setFont(font);
			setFixedWidth(font);
			setMinimumSize(getPreferredSize());
		};
		if (renderer!=null) {
			renderer.setFont(font);
		}
	}
	public void setFixedWidth(Font font) {
		FontMetrics metrics= list.getFontMetrics(font);
		int charWidth= metrics.charWidth('M');
		int currentWidth= (int)StrictMath.round(length*charWidth);
		list.setFixedCellWidth(currentWidth);
	}
	//
	public void setMultipleSelection(boolean flag) {
		if (list!=null) {
			if (flag) {
				list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			} else {
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (list!=null) {
			synchronized(list) {
				ListModel model= list.getModel();
				if (model.getSize() > 0) {
					try {
						int number= value.getSmallIntegerValue(iX);
						if (number > 0 && number <= model.getSize()) {
							// list.clearSelection(); 2012.08.29
							list.setSelectedIndex(number-1);
						} else if (number <= 0) {
							list.clearSelection();
						}
					} catch (TermIsNotAnInteger e1) {
						try {
							long code= value.getSymbolValue(iX);
							String text= value.toString(iX);
							for (int n=0; n <= model.getSize()-1; n++) {
								if (model.getElementAt(n).toString().equals(text)) {
									list.setSelectedIndex(n);
									break;
								}
							}
						} catch (TermIsNotASymbol e2) {
							try {
								String text= value.getStringValue(iX);
								for (int n=0; n <= model.getSize()-1; n++) {
									if (model.getElementAt(n).toString().equals(text)) {
										list.setSelectedIndex(n);
										break;
									}
								}
							} catch (TermIsNotAString e3) {
								try {
									list.setValueIsAdjusting(true);
									list.clearSelection();
									implementMultipleSelection(value,iX,model);
								} finally {
									list.setValueIsAdjusting(false);
								}
							}
						}
					}
				}
			}
		}
	}
	public void putRange(Term value, ChoisePoint iX) {
		if (list!=null) {
			ArrayList<String> stringVector= DialogUtils.listToStringVector(value,iX);
			String[] stringList= stringVector.toArray(new String[0]);
			if (enableSorting) {
				Arrays.sort(stringList,new AlphabeticComparator());
			};
			synchronized(list) {
				try {
					list.setValueIsAdjusting(true);
					list.clearSelection();
					list.setListData(stringList);
					if (useTabStops) {
						renderer= new TabListCellRenderer(stringList);
						list.setCellRenderer(renderer);
					}
				} finally {
					list.setValueIsAdjusting(false);
				};
				targetDialog.repaint();
				targetDialog.repaintAfterDelay();
			}
		}
	}
	public Term getValue() {
		if (list!=null) {
			synchronized(list) {
				ListModel model= list.getModel();
				int mode= list.getSelectionMode();
				if (model.getSize() <= 0 || list.isSelectionEmpty()) {
					return PrologEmptyList.instance;
				} else if (mode==ListSelectionModel.SINGLE_SELECTION) {
					Object selectedValue= list.getSelectedValue();
					if (selectedValue==null) {
						return PrologEmptyList.instance;
					} else {
						return new PrologString(selectedValue.toString());
					}
				} else {
					List<String> values= list.getSelectedValuesList();
					if (values==null) {
						return PrologEmptyList.instance;
					} else {
						Term result= PrologEmptyList.instance;
						for (int n=values.size()-1; n>=0; n--) {
							result= new PrologList(new PrologString(values.get(n).toString()),result);
						};
						return result;
					}
				}
			}
		} else {
			return PrologEmptyList.instance;
		}
	}
	public Term getRange() {
		if (list!=null) {
			synchronized(list) {
				ListModel model= list.getModel();
				Term result= PrologEmptyList.instance;
				for (int n=model.getSize()-1; n>=0; n--) {
					result= new PrologList(new PrologString(model.getElementAt(n).toString()),result);
				};
				return result;
			}
		} else {
			return PrologEmptyList.instance;
			// return PrologUnknownValue.instance;
		}
	}
	//
	public void setEnabled(boolean mode) {
		if (list != null) {
			if (mode) {
				super.setEnabled(true);
				list.setEnabled(true);
			} else {
				super.setEnabled(false);
				list.setEnabled(false);
			}
		}
	}
	//
	public boolean isEnabled() {
		if (list != null) {
			return super.isEnabled() && list.isEnabled();
		} else {
			return false;
		}
	}
	//
	// Auxiliary function
	//
	protected void implementMultipleSelection(Term tail, ChoisePoint iX, ListModel model) {
		try {
			while(true) {
				Term value= tail.getNextListHead(iX);
				try {
					int number= value.getSmallIntegerValue(iX);
					if (number > 0 && number <= model.getSize()) {
						list.addSelectionInterval(number-1,number-1);
					}
				} catch (TermIsNotAnInteger e1) {
					try {
						long code= value.getSymbolValue(iX);
						String text= value.toString(iX);
						for (int n=0; n <= model.getSize()-1; n++) {
							if (model.getElementAt(n).toString().equals(text)) {
								list.addSelectionInterval(n,n);
								break;
							}
						}
					} catch (TermIsNotASymbol e2) {
						try {
							String text= value.getStringValue(iX);
							for (int n=0; n <= model.getSize()-1; n++) {
								if (model.getElementAt(n).toString().equals(text)) {
									list.addSelectionInterval(n,n);
									break;
								}
							}
						} catch (TermIsNotAString e3) {
							implementMultipleSelection(value,iX,model);
						}
					}
				};
				tail= tail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
		}
	}
}
