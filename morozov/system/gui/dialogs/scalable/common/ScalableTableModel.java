/*
 * @(#)ScalableTableModel.java 1.0 2012/08/25
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import morozov.system.*;
import morozov.system.gui.dialogs.*;
import morozov.terms.*;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.concurrent.atomic.*;

/*
 * ScalableTableModel implementation for the Actor Prolog language
 * @version 1.0 2012/08/25
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableTableModel extends AbstractTableModel {
	//
	protected AbstractDialog targetDialog= null;
	protected ATable table= null;
	protected int numberOfColumns= 0;
	// protected int numberOfRows= 0;
	protected String[] columnNames;
	protected ArrayList<ArrayList<String>> content= new ArrayList<ArrayList<String>>();
	protected ScalableTableResident resident= null;
	protected AtomicBoolean useResident= new AtomicBoolean(false);
	//
	public ScalableTableModel(AbstractDialog dialog, ATable control, ScalableTableColumnDescription[] columnDescriptions, Term initialValue, ChoisePoint iX) {
		targetDialog= dialog;
		table= control;
		// numberOfRows= height;
		numberOfColumns= columnDescriptions.length;
		columnNames= new String[numberOfColumns];
		for (int n=0; n < numberOfColumns; n++) {
			columnNames[n]= columnDescriptions[n].name;
		};
		setTableValues(initialValue,false,iX);
	}
	//
	public void setTableValues(Term value, boolean checkResidentValues, ChoisePoint iX) {
		// System.out.printf("Resident::1:%s\n",value);
		if (checkResidentValues && !useResident.get()) {
			return;
		};
		// System.out.printf("Resident::2\n");
		try {
			String tableName= value.getStringValue(iX);
			useResident.set(true);
			long domainSignature= targetDialog.get_data_request_signature();
			if (resident==null) {
				resident= new ScalableTableResident(targetDialog,table);
				resident.initiate(targetDialog.targetWorld,domainSignature,new PrologString(tableName));
				resident.startProcesses();
				// System.out.printf("Resident::3\n");
			} else {
				resident.initiate(targetDialog.targetWorld,domainSignature,new PrologString(tableName));
				// System.out.printf("Resident::initiate/3(%s,%s,%s,%s)\n",targetDialog.targetWorld,this,domainSignature,new PrologString(tableName));
			}
			// System.out.printf("Resident::4\n");
			// targetDialog.sendResidentRequest(resident,tableName,iX);
		} catch (TermIsNotAString e1) {
			if (!checkResidentValues) {
				useResident.set(false);
				if (resident!=null) {
					targetDialog.withdrawRequest(resident);
				};
			};
			synchronized(content) {
				// int[] selection= table.getSelectedRows();
				// for (int n= 0; n < selection.length; n++) {
				//	selection[n] = table.convertRowIndexToModel(selection[n]);
				//	System.out.printf("selection[%s]=%s\n",n,selection[n]);
				// };
				content.clear();
				try {
					while (true) {
						Term currentRow= value.getNextListHead(iX);
						ArrayList<String> contentRow= new ArrayList<String>();
						content.add(contentRow);
						try {
							// int n= 1;
							while (true) {
								Term currentItem= currentRow.getNextListHead(iX);
								contentRow.add(currentItem.toString(iX));
								// numberOfColumns= StrictMath.max(n,numberOfColumns);
								// n++;
								currentRow= currentRow.getNextListTail(iX);
							}
						} catch (EndOfList e2) {
						} catch (TermIsNotAList e3) {
							throw new WrongArgumentIsNotAList(value);
						};
						value= value.getNextListTail(iX);
					}
				} catch (EndOfList e2) {
				} catch (TermIsNotAList e3) {
					throw new WrongArgumentIsNotAList(value);
				// } finally {
				//	fireTableStructureChanged();
				};
				// ListSelectionModel selectionModel= table.getSelectionModel();
				// selectionModel.clearSelection();
				// for (int n= 0; n < selection.length; n++) {
				//	// selectionModel.addSelectionInterval(selection[n],selection[n]);
				//	table.addRowSelectionInterval(selection[n],selection[n]);
				//	System.out.printf("ADD:selection[%s]=%s\n",n,selection[n]);
				// }
			}
		}
	}
	//
	public int getColumnCount() {
		return numberOfColumns;
        }
	public int getRowCount() {
		synchronized(content) {
			// return numberOfRows;
			return content.size();
		}
	}
	public String getColumnName(int col) {
		return columnNames[col];
	}
	public Object getValueAt(int row, int col) {
		synchronized(content) {
			if (row + 1 <= content.size()) {
				ArrayList<String> contentRow= content.get(row);
				if (col + 1 <= contentRow.size()) {
					return contentRow.get(col);
				} else {
					return "";
				}
			} else {
				return "";
			}
		}
	}
	public Class getColumnClass(int c) {
		return String.class;
	}
	public Term getRows(int[] selection) {
		Term result= new PrologEmptyList();
		synchronized(content) {
			for (int n=selection.length-1; n>=0; n--) {
				int rowNumber= selection[n];
				ArrayList<String> contentRow= content.get(rowNumber);
				Term row= new PrologEmptyList();
				for (int k=numberOfColumns-1; k>=0; k--) {
					String cell;
					if (k + 1 <= contentRow.size()) {
						cell= contentRow.get(k);
					} else {
						cell= "";
					};
					row= new PrologList(new PrologString(cell),row);
				};
				row= new PrologList(new PrologInteger(rowNumber+1),row);
				result= new PrologList(row,result);
			}
		};
		return result;
	}
	public Term getContent() {
		Term result= new PrologEmptyList();
		synchronized(content) {
			for (int rowNumber=content.size()-1; rowNumber>=0; rowNumber--) {
				ArrayList<String> contentRow= content.get(rowNumber);
				Term row= new PrologEmptyList();
				for (int k=numberOfColumns-1; k>=0; k--) {
					String cell;
					if (k + 1 <= contentRow.size()) {
						cell= contentRow.get(k);
					} else {
						cell= "";
					};
					row= new PrologList(new PrologString(cell),row);
				};
				result= new PrologList(row,result);
				// result= new PrologList(new PrologInteger(rowNumber),result);
			}
		};
		return result;
	}
}
