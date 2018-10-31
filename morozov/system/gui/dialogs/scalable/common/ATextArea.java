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

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;

import javax.swing.JTextArea;
import javax.swing.text.Document;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class ATextArea
	extends JTextArea
	implements ActiveDocumentReportListener, FocusListener {
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
		addFocusListener(this);
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
		// repaint(); // Возможно, это причина глюков.
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(targetComponent);
			targetDialog.safelyInvalidate();
			// targetDialog.repaint(); // Возможно, это причина глюков.
			// Попытка бороться с глюками SWING:
			// targetDialog.repaintAfterDelay();
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
	public void focusGained(FocusEvent e) {
		// Invoked when a component gains the keyboard focus.
	}
	public void focusLost(FocusEvent e) {
		// Invoked when a component loses the keyboard focus.
		targetDialog.reportCompleteEditing(targetComponent);
	}
	//
	// public void setFont(Font font) {
	//	super.setFont(font);
	//	setMinimumSize(getPreferredSize());
	// }
	//
	// public void setText(String t) {
	//	if (t.equalsIgnoreCase("#")) {
	//		throw new WrongArgumentIsNotSwitch();
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
