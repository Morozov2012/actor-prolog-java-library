/*
 * @(#)ATable.java 1.0 2012/08/23
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ATable implementation for the Actor Prolog language
 * @version 1.0 2012/08/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import javax.swing.border.Border;
import java.awt.Insets;
import java.util.concurrent.atomic.AtomicBoolean;

public class ATable extends JScrollPane {
	//
	public JTable table= null;
	protected AbstractDialog targetDialog= null;
	protected ActiveComponent targetComponent= null;
	protected int length;
	// protected int height;
	protected AtomicBoolean tableIsInitiated= new AtomicBoolean(false);
	protected int[] initiallySelectedIndices;
	//
	public ATable(AbstractDialog tD, ActiveComponent tC, double tableLength, double tableHeight, ScalableTableColors colors, ScalableTableColumnDescription[] columnDescriptions, Term initialValue, ChoisePoint iX) {
		targetDialog= tD;
		targetComponent= tC;
		length= (int)StrictMath.round(tableLength);
		// height= (int)StrictMath.round(tableHeight);
		table= new ExtendedJTable(tableHeight,colors,columnDescriptions,new ScalableTableModel(targetDialog,this,columnDescriptions,initialValue,iX),iX);
		// table.setFillsViewportHeight(true);
		table.setFillsViewportHeight(false);
		// targetDialog.doLayout(true);
		setViewportSize(getFont());
		// Dimension tableSize= table.getPreferredSize();
		// table.setPreferredScrollableViewportSize(tableSize);
		// setMinimumSize(tableSize);
		setViewportView(table);
		table.getSelectionModel().addListSelectionListener(targetComponent);
		// targetDialog.sendResidentRequest(new ScalableTableResident(),"TABLE1",iX);
		// setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
	}
	//
	public void setMultipleSelection(boolean flag) {
		if (table!=null) {
			if (flag) {
				table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			} else {
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
	}
	public void setSelectedIndex(int index) {
		if (table!=null) {
			if (!tableIsInitiated.get()) {
				synchronized(tableIsInitiated) {
					initiallySelectedIndices= new int[1];
					initiallySelectedIndices[0]= index;
				}
			};
			ListSelectionModel model= table.getSelectionModel();
			try {
				model.setValueIsAdjusting(true);
				model.clearSelection();
				if (index > 0 && index <= table.getRowCount()) {
					model.addSelectionInterval(index-1,index-1);
				}
			} finally {
				model.setValueIsAdjusting(false);
			}
		}
	}
	public void setSelectedIndices(int[] indices) {
		if (table!=null) {
			if (!tableIsInitiated.get()) {
				synchronized(tableIsInitiated) {
					initiallySelectedIndices= indices;
				}
			};
			ListSelectionModel model= table.getSelectionModel();
			int selectionMode= model.getSelectionMode();
			try {
				model.setValueIsAdjusting(true);
				model.clearSelection();
				if (selectionMode==ListSelectionModel.SINGLE_SELECTION) {
					if (indices.length > 0) {
						int index= indices[0];
						if (index > 0 && index <= table.getRowCount()) {
							model.addSelectionInterval(index-1,index-1);
						}
					}
				} else {
					for (int n=0; n < indices.length; n++) {
						int index= indices[n];
						if (index > 0 && index <= table.getRowCount()) {
							model.addSelectionInterval(index-1,index-1);
						}
					}
				}
			} finally {
				model.setValueIsAdjusting(false);
			}
		}
	}
	//
	public void setFont(Font font) {
		// System.out.printf("font=%s\n",font);
		super.setFont(font);
		if (table!=null) {
			table.setFont(font);
			setViewportSize(font);
		}
	}
	public void setViewportSize(Font font) {
		JTableHeader header= table.getTableHeader();
		Dimension headerPreferableSize= header.getPreferredSize();
		// System.out.printf("headerPreferableSize=%s\n",headerPreferableSize);
		FontMetrics metrics= getFontMetrics(font);
		int rowHeight= metrics.getHeight();
		// targetDialog.doLayout(true);
		Dimension tableSize= table.getPreferredSize();
		// System.out.printf("===\n");
		// System.out.printf("border=%s\n",getBorder());
		// System.out.printf("Insets=%s\n",getBorder().getBorderInsets(this));
		// System.out.printf("metrics.getHeight()=%s\n",rowHeight);
		// System.out.printf("rowMargin=%s\n",table.getRowMargin());
		// System.out.printf("headerPreferableSize.height=%s\n",headerPreferableSize.height);
		int rowMargin= table.getRowMargin();
		Border border= getBorder();
		int extraSpace= 0;
		if (border != null) {
			Insets borderInsets= border.getBorderInsets(this);
			if (borderInsets != null) {
				extraSpace= borderInsets.top + borderInsets.bottom;
			}
		};
		int criticalHeight= headerPreferableSize.height + rowHeight + rowMargin + extraSpace; // + 2;
		Dimension tableSize2= new Dimension(tableSize.width,StrictMath.max(tableSize.height,criticalHeight));
		table.setPreferredScrollableViewportSize(tableSize2);
		setMinimumSize(tableSize2);
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (table!=null) {
			synchronized(table) {
				if (table.getRowCount() > 0) {
					try {
						int number= value.getSmallIntegerValue(iX);
						ListSelectionModel model= table.getSelectionModel();
						try {
							model.setValueIsAdjusting(true);
							if (number > 0 && number <= table.getRowCount()) {
								model.clearSelection();
								model.addSelectionInterval(number-1,number-1);
							} else if (number <= 0) {
								model.clearSelection();
							}
						} finally {
							model.setValueIsAdjusting(false);
						}
					} catch (TermIsNotAnInteger e1) {
						// try {
						implementMultipleSelection(value,iX);
						// } catch(Throwable ee) {
						// System.out.printf("ee=%s\n",ee);
						// }
					}
				}
			}
		}
	}
	public void putRange(Term value, ChoisePoint iX) {
		putRange(value,false,iX);
	}
	public void putRange(Term value, boolean checkResidentValues, ChoisePoint iX) {
		if (table!=null) {
			synchronized(table) {
				ScalableTableModel tableModel= (ScalableTableModel)table.getModel();
				ListSelectionModel selectionModel= table.getSelectionModel();
				int[] selection;
				if (tableIsInitiated.compareAndSet(false,true)) {
					int selectionMode= selectionModel.getSelectionMode();
					int length= initiallySelectedIndices.length;
					synchronized(tableIsInitiated) {
						if (selectionMode==ListSelectionModel.SINGLE_SELECTION) {
							if (length > 1) {
								length= 1;
							}
						};
						selection= new int[length];
						for (int n= 0; n < length; n++) {
							selection[n]= initiallySelectedIndices[n] - 1;
						}
					}
				} else {
					selection= table.getSelectedRows();
					for (int n= 0; n < selection.length; n++) {
						if (n < tableModel.getRowCount()) {
							try {
								selection[n]= table.convertRowIndexToModel(selection[n]);
							} catch (IndexOutOfBoundsException e) {
							}
						}
					}
				};
				tableModel.fireTableStructureChanged();
				try {
					selectionModel.setValueIsAdjusting(true);
					tableModel.setTableValues(value,checkResidentValues,iX);
					selectionModel.clearSelection();
					for (int n= 0; n < selection.length; n++) {
						int rowNumber= selection[n];
						if (0 <= rowNumber && rowNumber < tableModel.getRowCount()) {
							selectionModel.addSelectionInterval(rowNumber,rowNumber);
						}
					}
				} finally {
					selectionModel.setValueIsAdjusting(false);
				};
				targetDialog.doLayout(true);
				Dimension tableSize= table.getPreferredSize();
				targetDialog.repaint();
				targetDialog.repaintAfterDelay();
			}
		}
	}
	public Term getValue() {
		if (table!=null) {
			synchronized(table) {
				int[] selection= table.getSelectedRows();
				for (int n= 0; n < selection.length; n++) {
					selection[n] = table.convertRowIndexToModel(selection[n]);
				};
				ScalableTableModel model= (ScalableTableModel)table.getModel();
				return model.getRows(selection);
			}
		} else {
			return PrologEmptyList.instance;
		}
	}
	public Term getRange() {
		if (table!=null) {
			synchronized(table) {
				ScalableTableModel model= (ScalableTableModel)table.getModel();
				return model.getContent();
			}
		} else {
			return PrologEmptyList.instance;
			// return PrologUnknownValue.instance;
		}
	}
	//
	public int[] getSelectedRows() {
		return table.getSelectedRows();
	}
	public ListSelectionModel getSelectionModel() {
		return table.getSelectionModel();
	}
	public int convertRowIndexToModel(int viewRowIndex) {
		return table.convertRowIndexToModel(viewRowIndex);
	}
	public void addRowSelectionInterval(int index0, int index1) {
		table.addRowSelectionInterval(index0,index1);
	}
	//
	// Auxiliary function
	//
	protected void implementMultipleSelection(Term tail, ChoisePoint iX) {
		ListSelectionModel model= table.getSelectionModel();
		try {
			model.setValueIsAdjusting(true);
			model.clearSelection();
			try {
				while(true) {
					Term value= tail.getNextListHead(iX);
					try {
						int number= value.getSmallIntegerValue(iX);
						if (number > 0 && number <= table.getRowCount()) {
							model.addSelectionInterval(number-1,number-1);
						}
					} catch (TermIsNotAnInteger e1) {
						selectRowIfNecessary(value,iX,model);
					};
					tail= tail.getNextListTail(iX);
				}
			} catch (EndOfList e) {
			} catch (TermIsNotAList e) {
			}
		} finally {
			model.setValueIsAdjusting(false);
		}
	}
	protected void selectRowIfNecessary(Term tail, ChoisePoint iX, ListSelectionModel selectionModel) {
		TableModel tableModel= table.getModel();
		try {
			Term value= tail.getNextListHead(iX);
			value= value.dereferenceValue(iX);
			if (value.thisIsFreeVariable()) {
				return;
			} else {
				if (value.thisIsUnknownValue()) {
					tail= tail.getNextListTail(iX);
					for (int k=0; k < table.getRowCount(); k++) {
						if (isTargetRow(k,tail,iX,tableModel)) {
							selectionModel.addSelectionInterval(k,k);
							return;
						}
					}
				} else {
					try {
						int number= value.getSmallIntegerValue(iX);
						if (number > 0 && number <= table.getRowCount()) {
							tail= tail.getNextListTail(iX);
							if (isTargetRow(number-1,tail,iX,tableModel)) {
								selectionModel.addSelectionInterval(number-1,number-1);
							}
						}
					} catch (TermIsNotAnInteger e1) {
					}
				}
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
		}
	}
	protected boolean isTargetRow(int rowNumber, Term tail, ChoisePoint iX, TableModel tableModel) {
		int currentColumn= 0;
		try {
			while(true) {
				Term value= tail.getNextListHead(iX);
				value= value.dereferenceValue(iX);
				if (value.thisIsFreeVariable()) {
					return false;
				} else if (value.thisIsUnknownValue()) {
				} else {
					String cellValue= "";
					if (currentColumn < tableModel.getColumnCount()) {
						cellValue= tableModel.getValueAt(rowNumber,currentColumn).toString();
					};
					if (!cellValue.equals(value.toString(iX))) {
						return false;
					}
				};
				tail= tail.getNextListTail(iX);
				currentColumn++;
			}
		} catch (EndOfList e) {
			if (currentColumn < tableModel.getColumnCount()-1) {
				for (int k=currentColumn; currentColumn < tableModel.getColumnCount(); currentColumn++) {
					String cellValue= tableModel.getValueAt(rowNumber,currentColumn).toString();
					if (!cellValue.isEmpty()) {
						return false;
					}
				}
			};
			return true;
		} catch (TermIsNotAList e) {
			return false;
		}
	}
}
