/*
 * @(#)TabListCellRenderer.java 1.0 2011/04/22
 *
 * Copyright 2011 IRE RAS Alexei A. Morozov
 * Thanks to Matthrew Robinson and Pavel Vorobiev Java SWING (Manning)
*/

package morozov.system.gui.dialogs.scalable.common;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TabListCellRenderer extends JLabel implements ListCellRenderer<Object> {
	//
	protected static Border m_noFocusBorder;
	protected String[] listItems;
	protected Font currentFont;
	protected int m_defaultDistance= 10;
	protected int[] m_tabs= null;
	//
	public TabListCellRenderer(String[] rows) {
		super();
		listItems= rows;
		m_noFocusBorder= new EmptyBorder(1,1,1,1);
		setOpaque(true);
		setBorder(m_noFocusBorder);
	}
	//
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		setText(value.toString());
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		setFont(list.getFont());
		setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : m_noFocusBorder);
		return this;
	}
	protected void computeTabsIfNecessary(Font font) {
		if (currentFont==null) {
			currentFont= font;
			setTabs(computeTabs(listItems));
		} else {
			if (currentFont==font) {
				return;
			} else {
				currentFont= font;
				setTabs(computeTabs(listItems));
			}
		} 
	}
	public int[] computeTabs(String[] rows) {
		ArrayList<Integer> rowWidth= new ArrayList<>();
		FontMetrics metrics= getFontMetrics(getFont());
		for (int n=0; n < rows.length; n++) {
			StringTokenizer st= new StringTokenizer(rows[n],"\t");
			int k= 0;
			while (st.hasMoreTokens()) {
				String sNext= st.nextToken();
				int columnWdth= metrics.stringWidth(sNext) + m_defaultDistance;
				if (k >= rowWidth.size()) {
					rowWidth.add(columnWdth);
				} else {
					rowWidth.set(k,StrictMath.max(rowWidth.get(k),columnWdth));
				};
				k= k + 1;
			}
		};
		int tabsNumber= rowWidth.size()-1;
		int[] tabs= new int[tabsNumber];
		int sum= 0;
		for (int k=0; k < tabsNumber; k++) {
			sum= sum + rowWidth.get(k);
			tabs[k]= sum;
		};
		return tabs;
	}
	public void setTabs(int[] tabs) {
		m_tabs= tabs;
	}
	@Override
	public void paint(Graphics g) {
		Font m_font= getFont();
		g.setFont(m_font);
		computeTabsIfNecessary(m_font);
		FontMetrics m_fm= g.getFontMetrics();
		g.setColor(getBackground());
		g.fillRect(0,0,getWidth(),getHeight());
		getBorder().paintBorder(this,g,0,0,getWidth(),getHeight());
		g.setColor(getForeground());
		Insets m_insets= getInsets();
		int x= m_insets.left;
		int y= m_insets.top + m_fm.getAscent();
		StringTokenizer st= new StringTokenizer(getText(),"\t");
		int k= 0;
		while (st.hasMoreTokens()) {
			String sNext= st.nextToken();
			g.drawString(sNext,x,y);
			if (st.hasMoreTokens()) {
				x= m_tabs[k] + m_insets.left;
				k= k + 1;
			} else {
				break;
			};
		}
	}
}
