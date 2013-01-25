/*
 * @(#)ColoredTableCellRenderer.java 1.0 2012/08/25
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;

public class ColoredTableCellHeaderRenderer
		extends JLabel
		implements TableCellRenderer {
	// Color titleForegroundColor;
	public ColoredTableCellHeaderRenderer(Color titleForeground, Color titleBackground) {
		// titleForegroundColor= titleForeground;
		setOpaque(true);
		setForeground(titleForeground);
		setBackground(titleBackground);
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// setOpaque(true);
		// setForeground(titleForegroundColor);
		setFont(table.getFont());
		setHorizontalAlignment(SwingConstants.CENTER);
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setText(value.toString());
		return this;
	}
}
