/*
 * @(#)ExtendedJTable.java 1.0 2012/08/25
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ExtendedJTable implementation for the Actor Prolog language
 * @version 1.0 2012/08/25
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.Color;

public class ExtendedJTable extends JTable {
	//
	protected ScalableTableColumnDescription[] columnDescriptions;
	protected int numberOfColumns= 0;
	protected double tableHeight;
	protected ColoredTableCellRenderer[] cellRenderers;
	protected ColoredTableCellHeaderRenderer[] columnHeaderRenderers;
	//
	public ExtendedJTable(double height, ScalableTableColors colors, ScalableTableColumnDescription[] descriptions, TableModel dm, ChoisePoint iX) {
		super(dm);
		tableHeight= height;
		columnDescriptions= descriptions;
		numberOfColumns= columnDescriptions.length;
		cellRenderers= new ColoredTableCellRenderer[numberOfColumns];
		columnHeaderRenderers= new ColoredTableCellHeaderRenderer[numberOfColumns];
		FontMetrics metrics= getFontMetrics(getFont());
		int charWidth= metrics.charWidth('M');
		for (int n=0; n < numberOfColumns; n++) {
			double width= columnDescriptions[n].width;
			TextAlignment horizontalAlignment= columnDescriptions[n].horizontalAlignment;
			Term cellForegroundColor= columnDescriptions[n].foregroundColor;
			Term cellBackgroundColor= columnDescriptions[n].backgroundColor;
			Term titleForegroundColor= colors.titleForegroundColor;
			Term titleBackgroundColor= colors.titleBackgroundColor;
			Color cellForeground= null;
			Color cellBackground= null;
			Color titleForeground= null;
			Color titleBackground= null;
			try {
				cellForeground= ColorAttributeConverters.argumentToColorSafe(cellForegroundColor,iX);
			} catch (TermIsSymbolDefault e) {
			};
			try {
				cellBackground= ColorAttributeConverters.argumentToColorSafe(cellBackgroundColor,iX);
			} catch (TermIsSymbolDefault e) {
			};
			try {
				titleForeground= ColorAttributeConverters.argumentToColorSafe(titleForegroundColor,iX);
			} catch (TermIsSymbolDefault e) {
			};
			try {
				titleBackground= ColorAttributeConverters.argumentToColorSafe(titleBackgroundColor,iX);
			} catch (TermIsSymbolDefault e) {
			};
			setAutoCreateColumnsFromModel(false);
			cellRenderers[n]= new ColoredTableCellRenderer(horizontalAlignment,cellForeground,cellBackground);
			columnHeaderRenderers[n]= new ColoredTableCellHeaderRenderer(titleForeground,titleBackground);
			TableColumn column= columnModel.getColumn(n);
			int preferredWidth= Arithmetic.toInteger(width*charWidth);
			column.setPreferredWidth(preferredWidth);
		};
		getTableHeader().setReorderingAllowed(false);
		TableColumnModel tableColumnModel= getColumnModel();
		for (int n=0; n < numberOfColumns; n++) {
			TableColumn column= tableColumnModel.getColumn(n);
			column.setHeaderRenderer(columnHeaderRenderers[n]);
		};
		setAutoCreateRowSorter(true);
	}
	//
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if (column + 1 <= numberOfColumns) {
			return cellRenderers[column];
		} else {
			return super.getCellRenderer(row,column);
		}
	}
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		FontMetrics metrics= getFontMetrics(getFont());
		int charWidth= metrics.charWidth('M');
		for (int n=0; n < numberOfColumns; n++) {
			double width= columnDescriptions[n].width;
			TableColumn column= columnModel.getColumn(n);
			int preferredWidth= Arithmetic.toInteger(width*charWidth);
			column.setPreferredWidth(preferredWidth);
		};
		int metricsRowHeight= metrics.getHeight();
		setRowHeight(metricsRowHeight);
	}
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		TableModel model= getModel();
		Dimension size= super.getPreferredScrollableViewportSize();
		FontMetrics metrics= getFontMetrics(getFont());
		int metricsRowHeight= metrics.getHeight(); // + margin;
		int rowCount= model.getRowCount();
		double actualHeight;
		if (rowCount > 0) {
			actualHeight= StrictMath.min(tableHeight,model.getRowCount());
		} else {
			actualHeight= tableHeight;
		};
		int preferredHeight= Arithmetic.toInteger(actualHeight*metricsRowHeight);
		return new Dimension(size.width,preferredHeight);
	}
}
