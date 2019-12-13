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
	protected AbstractDialog targetDialog= null;
	protected ActiveComponent targetComponent= null;
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
	// Method to create default model:
	@Override
	protected Document createDefaultModel() {
		return new ActivePlainDocument();
	}
	// Override to handle insertion error:
	@Override
	public void reportSuccess() {
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(targetComponent);
			targetDialog.safelyInvalidate();
		}
	}
	@Override
	public void reportFailure() {
		reportSuccess();
	}
	//
	@Override
	public void focusGained(FocusEvent e) {
		// Invoked when a component gains the keyboard focus.
	}
	@Override
	public void focusLost(FocusEvent e) {
		// Invoked when a component loses the keyboard focus.
		targetDialog.reportCompleteEditing(targetComponent);
	}
	//
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
