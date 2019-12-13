/*
 * @(#)ScalableTitledPanel.java 1.0 2010/04/28
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableTitledPanel implementation for the Actor Prolog language
 * @version 1.0 2010/04/28
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.*;
import morozov.terms.*;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.Color;

public class ScalableTitledPanel extends ScalablePanel {
	//
	protected TitledBorder border;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalableTitledPanel(AbstractDialog tD, String title) {
		super(tD);
		border= BorderFactory.createTitledBorder(title);
		setBorder(border);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setGeneralForeground(Color c) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setIndividualText(Term value, ChoisePoint iX) {
		String title1= value.toString(iX);
		String title2= AnnotatedButton.reduceAmpersands(title1);
		border= BorderFactory.createTitledBorder(title2);
		setBorder(border);
		if (colourlessFont==null) {
			colourlessFont= getFont();
		};
		setGeneralFont(colourlessFont);
	}
	//
	@Override
	public Term getIndividualText() {
		if (border != null) {
			String title2= border.getTitle();
			String title1= AnnotatedButton.restoreAmpersands(title2);
			return new PrologString(title1);
		} else {
			return termEmptyString;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		colourlessFont= font;
		if (border != null) {
			border.setTitleFont(font);
			targetDialog.safelyRevalidateAndRepaint();
		}
	}
	//
	@Override
	public void setForeground(Color c) {
		super.setForeground(c);
		if (border != null) {
			border.setTitleColor(c);
		}
	}
}
