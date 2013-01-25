/*
 * @(#)ExtendedFileChooser.java 1.0 2010/11/13
 *
 * (c) 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.special;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ExtendedFileChooser extends JFileChooser {
	public void approveSelection() {
		if (getDialogType() == JFileChooser.SAVE_DIALOG) {
			// System.out.printf("SAVE: Called by the UI when the user hits the Approve button\n");
			java.io.File file= getSelectedFile();
			if (file.exists()) {
				String fileName= getSelectedFile().getName();
				String message=
					"The \"" + file.toString() +
					"\" file does exist already.\n" +
					"Do you want to overwrite it?";
				String title= "This file does exist already";
				int answer= JOptionPane.showConfirmDialog(
					this,	// Component parentComponent
					message,
					title,
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
				if (answer==JOptionPane.YES_OPTION) {
					super.approveSelection();
				}
			} else {
				super.approveSelection();
			}
		} else {
			// System.out.printf("OPEN: Called by the UI when the user hits the Approve button\n");
			super.approveSelection();
		}
	}
	//
	public boolean isTraversable(java.io.File f) {
		try {
			return super.isTraversable(f);
		} catch (Throwable e) {
			// System.out.printf("isTraversable: %s\n",e);
			return false;
		}
	}
}
