/*
 * @(#)ExtendedFileChooser.java 1.0 2010/11/13
 *
 * (c) 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.special;

import morozov.system.files.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class ExtendedFileChooser extends JFileChooser {
	public void approveSelection() {
		if (getDialogType() == JFileChooser.SAVE_DIALOG) {
			if (isMultiSelectionEnabled()) {
				java.io.File[] selectedFiles= getSelectedFiles();
				boolean allFilesAreAproved= true;
				for (int n=0; n < selectedFiles.length; n++) {
					if (!approveFile(selectedFiles[n])) {
						allFilesAreAproved= false;
						break;
					}
				};
				if (allFilesAreAproved) {
					super.approveSelection();
				}
			} else {
				java.io.File selectedFile= getSelectedFile();
				if (approveFile(selectedFile)) {
					super.approveSelection();
				}
			}
		} else {
			super.approveSelection();
		}
	}
	//
	protected boolean approveFile(java.io.File selectedFile) {
		java.io.File fileToBeApproved;
		FileFilter currentFilter= getFileFilter();
		if (currentFilter instanceof FileNameMask) {
			FileNameMask currentMask= (FileNameMask)currentFilter;
			String selectedName= selectedFile.toPath().toString();
			selectedName= currentMask.appendExtensionIfNecessary(selectedName);
			fileToBeApproved= new java.io.File(selectedName);
		} else {
			fileToBeApproved= selectedFile;
		};
		if (fileToBeApproved.exists()) {
			String fileName= getSelectedFile().getName();
			String message=
				"The \"" + fileToBeApproved.toString() +
				"\" file does already exist.\n" +
				"Do you want to overwrite it?";
			String title= "This file does already exist";
			int answer= JOptionPane.showConfirmDialog(
				this,	// Component parentComponent
				message,
				title,
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
			if (answer==JOptionPane.YES_OPTION) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	//
	public boolean isTraversable(java.io.File f) {
		try {
			return super.isTraversable(f);
		} catch (Throwable e) {
			return false;
		}
	}
}
