/*
 * @(#)ColoredTableCellRenderer.java 1.0 2012/08/25
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Component;

public class ColoredTableCellRenderer extends DefaultTableCellRenderer {
	//
	protected TextAlignment horizontalAlignment;
	//
	public ColoredTableCellRenderer(TextAlignment hAlignment, Color foreground, Color background) {
		horizontalAlignment= hAlignment;
		setForeground(foreground);
		setBackground(background);
	}
	//
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		ColoredTableCellRenderer c= (ColoredTableCellRenderer)super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
		Font font= table.getFont();
		c.setFont(font);
		FontMetrics metrics= getFontMetrics(font);
		int alignment= SwingConstants.CENTER;
		if (horizontalAlignment==TextAlignment.LEFT) {
			alignment= SwingConstants.LEFT;
		} else if (horizontalAlignment==TextAlignment.RIGHT) {
			alignment= SwingConstants.RIGHT;
		} else if (horizontalAlignment==TextAlignment.CENTER) {
			alignment= SwingConstants.CENTER;
		};
		c.setHorizontalAlignment(alignment);
		int charWidth= metrics.charWidth('M');
		int halfWidth= StrictMath.round(charWidth/2);
		int top= 0;
		int left= halfWidth;
		int bottom= 0;
		int rignt= halfWidth;
		c.setBorder(BorderFactory.createEmptyBorder(top,left,bottom,rignt));
		c.setText(value.toString());
		return c;
	}
	public Component getTableCellRendererComponent1(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Font font= table.getFont();
		setFont(font);
		FontMetrics metrics= getFontMetrics(font);
		int alignment= SwingConstants.CENTER;
		if (horizontalAlignment==TextAlignment.LEFT) {
			alignment= SwingConstants.LEFT;
		} else if (horizontalAlignment==TextAlignment.RIGHT) {
			alignment= SwingConstants.RIGHT;
		} else if (horizontalAlignment==TextAlignment.CENTER) {
			alignment= SwingConstants.CENTER;
		};
		setHorizontalAlignment(alignment);
		int charWidth= metrics.charWidth('M');
		int halfWidth= StrictMath.round(charWidth/2);
		int top= 0;
		int left= halfWidth;
		int bottom= 0;
		int rignt= halfWidth;
		setBorder(BorderFactory.createEmptyBorder(top,left,bottom,rignt));
		setText(value.toString());
		return this;
	}
}
