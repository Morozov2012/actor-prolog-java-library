/*
 * @(#)ATextField.java 1.0 2010/03/09
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ATextField implementation for the Actor Prolog language
 * @version 1.0 2010/03/09
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.terms.*;

import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class ATextField
		extends JTextField
		implements ActiveDocumentReportListener, FocusListener {
	//
	protected ActiveFieldErrorBorder outerBorder= null;
	protected Border nativeBorder= null;
	//
	protected AbstractDialog targetDialog= null;
	protected ActiveComponent targetComponent= null;
	//
	public ATextField(AbstractDialog tD, ActiveComponent tC, int columns) {
		this(tD,tC,"",columns);
	}
	public ATextField(AbstractDialog tD, ActiveComponent tC, String text, int columns) {
		super(null,text,columns);
		targetDialog= tD;
		targetComponent= tC;
		initiateActiveTextField();
		addFocusListener(this);
	}
	//
	protected void initiateActiveTextField() {
		ActivePlainDocument activeDocument= (ActivePlainDocument)getDocument();
		activeDocument.addReportListener(this);
		nativeBorder= getBorder();
		outerBorder= new ActiveFieldErrorBorder(this,nativeBorder.getBorderInsets(this));
	}
	// Method to create default model:
	@Override
	protected Document createDefaultModel() {
		return new ActivePlainDocument();
	}
	//
	public void setFormat(ActiveDocumentFormat format) {
		ActivePlainDocument activeDocument= (ActivePlainDocument)getDocument();
		if (format!=null) {
			activeDocument.setFormat(format);
		}
	}
	//
	@Override
	public void reportSuccess() {
		setBorder(nativeBorder);
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(targetComponent);
		}
	}
	@Override
	public void reportFailure() {
		setBorder(outerBorder);
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(targetComponent);
		}
	}
	//
	@Override
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
	@Override
	public void focusGained(FocusEvent e) {
	}
	@Override
	public void focusLost(FocusEvent e) {
		targetDialog.reportCompleteEditing(targetComponent);
	}
	//
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		setMinimumSize(getPreferredSize());
	}
	public void setAlarmColors(Color fc, Color bc) {
		if (outerBorder!=null) {
			outerBorder.setAlarmColors(fc,bc);
		}
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
