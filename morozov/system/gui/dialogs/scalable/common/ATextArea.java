/*
 * @(#)ATextArea.java 1.0 2010/03/17
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ATextArea implementation for the Actor Prolog language
 * @version 1.0 2010/03/17
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;

import javax.swing.*;
import javax.swing.text.*;

public class ATextArea
	extends JTextArea
	implements ActiveDocumentReportListener {
	//
	AbstractDialog targetDialog= null;
	ActiveComponent targetComponent= null;
	//
	public ATextArea(AbstractDialog tD, ActiveComponent tC, int rows, int columns) {
		this(tD,tC,"",rows,columns);
	}
	public ATextArea(AbstractDialog tD, ActiveComponent tC, String text, int rows, int columns) {
		super(null,text,rows,columns);
		targetDialog= tD;
		targetComponent= tC;
		ActivePlainDocument activeDocument= (ActivePlainDocument)getDocument();
		activeDocument.addReportListener(this);
	}
	// Method to create default model
	protected Document createDefaultModel() {
		return new ActivePlainDocument();
	}
	//
	// public void setValue(Number number) {
	//	setText(getFormat().format(number));
	// }
	//
	// Override to handle insertion error
	public void reportSuccess() {
		// By default, just beep
		// Toolkit.getDefaultToolkit().beep();
		// outerBorder.setActiveFieldValueIsValid(true);
		repaint();
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(targetComponent);
		}
	}
	public void reportFailure() {
		reportSuccess();
	}
	//
	// public void processComponentKeyEvent(KeyEvent evt) {
	//	switch (evt.getID()) {
	//	case KeyEvent.KEY_PRESSED:
	//	case KeyEvent.KEY_RELEASED:
	//		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
	//			return;
	//		};
	//		break;
	//	case KeyEvent.KEY_TYPED:
	//		if (evt.getKeyChar() == '\r') {
	//			return;
	//		};
	//		break;
	//	};
	//	super.processComponentKeyEvent(evt);
	// }
	//
	// public void setFont(Font font) {
	//	super.setFont(font);
	//	setMinimumSize(getPreferredSize());
	// }
	//
	// public void setText(String t) {
	//	if (t.equalsIgnoreCase("#")) {
	//		throw new WrongTermIsNotSwitch();
	//	}
	// }
	public void putValue(Term value, ChoisePoint iX) {
		setText(value.toString(iX));
	}
	public Term getValue() {
		ActivePlainDocument activeDocument= (ActivePlainDocument)getDocument();
		try {
			return new PrologString(activeDocument.getTextOrBacktrack());
		} catch (Backtracking b) {
			return new PrologString(activeDocument.getText());
		}
	}
}
