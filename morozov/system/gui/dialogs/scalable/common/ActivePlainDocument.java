/*
 * @(#)ActivePlainDocument.java 1.0 2009/11/23
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ActivePlainDocument implementation for the Actor Prolog language
 * @version 1.0 2009/11/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class ActivePlainDocument extends PlainDocument {
	//
	protected ActiveDocumentReportListener reportListener;
	protected ActiveDocumentFormat format= ActiveDocumentFormat.TEXT;
	//
	public void addReportListener(ActiveDocumentReportListener listener) {
		reportListener= listener;
	}
	//
	public void setFormat(ActiveDocumentFormat fmt) {
		this.format= fmt;
	}
	//
	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs,len);
		verifyNumberValue();
	}
	//
	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offset,str,a);
		verifyNumberValue();
	}
	//
	public void verifyNumberValue() {
		if (reportListener!=null) {
			if (format!=null) {
				try {
					int length= getLength();
					String content= getText(0,length);
					if (format.verify(content)) {
						reportListener.reportSuccess();
					} else {
						reportListener.reportFailure();
					}
				} catch (BadLocationException e) {
					reportListener.reportFailure();
				}
			} else {
				reportListener.reportSuccess();
			}
		}
	}
	//
	public String getTextOrBacktrack() throws Backtracking {
		try {
			int length= getLength();
			if (format!=null) {
				String content= getText(0,length);
				if (format.verify(content)) {
					return content;
				} else {
					throw Backtracking.instance;
				}
			} else {
				return getText(0,length);
			}
		} catch (BadLocationException e) {
			throw Backtracking.instance;
		}
	}
	//
	public String getText() {
		try {
			int length= getLength();
			return getText(0,length);
		} catch (BadLocationException e) {
			return "";
		}
	}
}
