/*
 * @(#)APasswordField.java 1.0 2010/03/09
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * APasswordField implementation for the Actor Prolog language
 * @version 1.0 2010/03/09
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;

import javax.swing.JPasswordField;
import javax.swing.text.Document;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class APasswordField
	extends JPasswordField
	implements ActiveDocumentReportListener, FocusListener {
	//
	AbstractDialog targetDialog= null;
	ActiveComponent targetComponent= null;
	//
	public APasswordField(AbstractDialog tD, ActiveComponent tC, String text,int columns) {
		super(text,columns);
		targetDialog= tD;
		targetComponent= tC;
		ActivePlainDocument activeDocument= (ActivePlainDocument)getDocument();
		activeDocument.addReportListener(this);
		// setHorizontalAlignment(JTextField.RIGHT);
		addFocusListener(this);
	}
	// Method to create default model
	protected Document createDefaultModel() {
		return new ActivePlainDocument();
	}
	//
	public void reportSuccess() {
		// By default, just beep
		// Toolkit.getDefaultToolkit().beep();
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(targetComponent);
		}
	}
	public void reportFailure() {
		// By default, just beep
		// Toolkit.getDefaultToolkit().beep();
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(targetComponent);
		}
	}
	//
	public void processComponentKeyEvent(KeyEvent evt) {
		switch (evt.getID()) {
		case KeyEvent.KEY_PRESSED:
		case KeyEvent.KEY_RELEASED:
			if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
				targetDialog.reportCompleteEditing(targetComponent);
				return;
			};
			break;
		case KeyEvent.KEY_TYPED:
			if (evt.getKeyChar() == '\r') {
				targetDialog.reportCompleteEditing(targetComponent);
				return;
			};
			break;
		};
		super.processComponentKeyEvent(evt);
	}
	//
	public void focusGained(FocusEvent e) {
		// Invoked when a component gains the keyboard focus.
	}
	public void focusLost(FocusEvent e) {
		// Invoked when a component loses the keyboard focus.
		targetDialog.reportCompleteEditing(targetComponent);
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		setText(value.toString(iX));
	}
	public Term getValue() {
		return new PrologString(new String(getPassword()));
	}
}
