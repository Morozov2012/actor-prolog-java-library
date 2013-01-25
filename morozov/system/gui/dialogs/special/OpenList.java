/*
 * @(#)OpenList.java 1.0 2010/04/19
 *
 * (c) Java SWING (Manning) Matthrew Robinson Pavel Vorobiev
 * (c) 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.special;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OpenList extends JPanel
	implements ListSelectionListener, ActionListener {
	protected JLabel m_title;
	protected JTextField m_text;
	protected JList<String> m_list;
	protected JScrollPane m_scroll;
	public OpenList(String[] data, String title) {
		setLayout(null);
		m_title= new JLabel(title,JLabel.LEFT);
		add(m_title);
		m_text= new JTextField();
		m_text.addActionListener(this);
		add(m_text);
		m_list= new JList<String>(data);
		m_list.setVisibleRowCount(4);
		m_list.addListSelectionListener(this);
		m_scroll= new JScrollPane(m_list);
		add(m_scroll);
	}
	public void setSelected(String sel) {
		m_list.setSelectedValue(sel, true);
		m_text.setText(sel);
	}
	public String getSelected() { return m_text.getText(); }
	public void setSelectedInt(int value) {
		setSelected(Integer.toString(value));
	}
	public int getSelectedInt() {
		try {
			return Integer.parseInt(getSelected());
		} catch (NumberFormatException ex) { return -1; }
	}
	public void valueChanged(ListSelectionEvent e) {
		Object obj= m_list.getSelectedValue();
		if (obj != null) {
			m_text.setText(obj.toString());
		}
	}
	public void actionPerformed(ActionEvent e) {
		ListModel model= m_list.getModel();
		String key= m_text.getText().toLowerCase();
		for (int k=0; k<model.getSize(); k++) {
			String data= (String)model.getElementAt(k);
			if (data.toLowerCase().startsWith(key)) {
				m_list.setSelectedValue(data, true);
				break;
			}
		}
	}
	public void addListSelectionListener(ListSelectionListener lst) {
		m_list.addListSelectionListener(lst);
	}
	public Dimension getPreferredSize() {
		Insets ins= getInsets();
		Dimension d1= m_title.getPreferredSize();
		Dimension d2= m_text.getPreferredSize();
		Dimension d3= m_scroll.getPreferredSize();
		int w= Math.max(Math.max(d1.width, d2.width), d3.width);
		int h= d1.height + d2.height + d3.height;
		return new Dimension(w+ins.left+ins.right,h+ins.top+ins.bottom);
	}
	public Dimension getMaximumSize() {
		Insets ins= getInsets();
		Dimension d1= m_title.getMaximumSize();
		Dimension d2= m_text.getMaximumSize();
		Dimension d3= m_scroll.getMaximumSize();
		int w= Math.max(Math.max(d1.width, d2.width), d3.width);
		int h= d1.height + d2.height + d3.height;
		return new Dimension(w+ins.left+ins.right,h+ins.top+ins.bottom);
	}
	public Dimension getMinimumSize() {
		Insets ins= getInsets();
		Dimension d1= m_title.getMinimumSize();
		Dimension d2= m_text.getMinimumSize();
		Dimension d3= m_scroll.getMinimumSize();
		int w= Math.max(Math.max(d1.width, d2.width), d3.width);
		int h= d1.height + d2.height + d3.height;
		return new Dimension(w+ins.left+ins.right,h+ins.top+ins.bottom);
	}
	public void doLayout() {
		Insets ins= getInsets();
		Dimension d= getSize();
		int x= ins.left;
		int y= ins.top;
		int w= d.width-ins.left-ins.right;
		int h= d.height-ins.top-ins.bottom;
		Dimension d1= m_title.getPreferredSize();
		m_title.setBounds(x, y, w, d1.height);
		y += d1.height;
		Dimension d2= m_text.getPreferredSize();
		m_text.setBounds(x, y, w, d2.height);
		y += d2.height;
		m_scroll.setBounds(x, y, w, h-y);
	}
}
