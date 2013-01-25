/*
 * @(#)ListSearcher.java 1.0 2011/04/22
 *
 * Copyright 2011 IRE RAS Alexei A. Morozov
 * Thanks to Matthrew Robinson and Pavel Vorobiev Java SWING (Manning)
*/

package morozov.system.gui.dialogs.scalable.common;

import javax.swing.JList;
import javax.swing.ListModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ListSearcher extends KeyAdapter {
	protected JList<String> m_list;
	protected ListModel<String> m_model;
	protected String m_key= "";
	protected long m_time= 0;
	public static int CHAR_DELTA= 1000;
	public ListSearcher(JList<String> list) {
		m_list= list;
		m_model= m_list.getModel();
	}
	public void keyTyped(KeyEvent e) {
		char ch= e.getKeyChar();
		if (Character.isLetterOrDigit(ch)) {
			if (m_time + CHAR_DELTA < System.currentTimeMillis()) {
				m_key= "";
			};
			m_time= System.currentTimeMillis();
			m_key= m_key + Character.toLowerCase(ch);
			for (int k=0; k < m_model.getSize(); k++) {
				String str= m_model.getElementAt(k).toLowerCase();
				if (str.startsWith(m_key)) {
					m_list.setSelectedIndex(k);
					m_list.ensureIndexIsVisible(k);
					break;
				}
			}
		}
	}
}
