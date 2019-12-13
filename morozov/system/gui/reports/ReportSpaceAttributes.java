// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import morozov.run.*;
import morozov.system.gui.*;

import javax.swing.text.StyledDocument;
import javax.swing.text.DefaultStyledDocument;
import java.awt.Color;
import java.awt.Font;

public class ReportSpaceAttributes extends CanvasSpaceAttributes {
	//
	public static final int automaticFontSizeAdjustment= -1;
	//
	protected StaticContext staticContext;
	protected StyledDocument currentStyledDocument= null;
	protected Integer currentCaretPosition= null;
	protected Integer currentSelectionStart= null;
	protected Integer currentSelectionEnd= null;
	protected Color currentBackgroundColor= null;
	protected Font currentFont= null;
	protected int fontSize= automaticFontSizeAdjustment;
	protected boolean keepFontSize= false;
	//
	synchronized public void setStaticContext(StaticContext context) {
		staticContext= context;
		if (currentStyledDocument==null) {
			currentStyledDocument= new DefaultStyledDocument();
		}
	}
	synchronized public void setStyledDocument(StyledDocument document) {
		currentStyledDocument= document;
	}
	synchronized public StyledDocument getStyledDocument() {
		if (currentStyledDocument==null) {
			currentStyledDocument= new DefaultStyledDocument();
		};
		return currentStyledDocument;
	}
	synchronized public void setFontSize(int size) {
		fontSize= size;
	}
	synchronized public int getFontSize() {
		return fontSize;
	}
	synchronized public void setTheKeepFontSizeFlag(boolean flag) {
		keepFontSize= flag;
	}
	synchronized public boolean getTheKeepFontSizeFlag() {
		return keepFontSize;
	}
	synchronized public void collectValues(ExtendedReportSpace space) {
		TextPaneNoWrap panel= space.panel;
		if (panel != null) {
			currentStyledDocument= panel.getStyledDocument();
			currentSelectionStart= panel.getSelectionStart();
			currentSelectionEnd= panel.getSelectionEnd();
			currentBackgroundColor= panel.getBackground();
			currentFont= panel.getFont();
		};
		space.setAttributes(null);
	}
	synchronized public void implementValues(ExtendedReportSpace space) {
		space.setAttributes(this);
		TextPaneNoWrap panel= space.panel;
		if (panel != null) {
			if (currentStyledDocument==null) {
				currentStyledDocument= new DefaultStyledDocument();
			};
			panel.setStyledDocument(currentStyledDocument);
			if (currentCaretPosition != null) {
				panel.setCaretPosition(currentCaretPosition);
			} else {
				panel.setCaretPosition(0);
			};
			if (currentSelectionStart != null) {
				panel.setSelectionStart(currentSelectionStart);
			} else {
				panel.setSelectionStart(0);
			};
			if (currentSelectionEnd != null) {
				panel.setSelectionEnd(currentSelectionEnd);
			} else {
				panel.setSelectionEnd(0);
			};
			if (currentBackgroundColor != null) {
				panel.setBackground(currentBackgroundColor);
			};
			if (currentFont != null) {
				panel.setFont(currentFont);
			}
		}
	}
}
