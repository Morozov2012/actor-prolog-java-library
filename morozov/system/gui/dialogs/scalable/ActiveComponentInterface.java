/*
 * @(#)ActiveComponentInterface.java 1.0 2017/03/11
 *
 * Copyright 2017 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ActiveComponentInterface implementation for the Actor Prolog language
 * @version 1.0 2017/03/11
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Color;

public interface ActiveComponentInterface {
	//
	public void putValue(DialogControlOperation operation, Term value, ChoisePoint iX);
	public Term getValue(DialogControlOperation operation);
	public void putRange(Term value, ChoisePoint iX);
	public Term getRange();
	//
	public void setIsEnabled(boolean mode);
	public boolean isEnabled(boolean mode);
	//
	public void setPadding(
			GridBagLayout gBL,
			boolean flagTop, boolean flagLeft, boolean flagBottom, boolean flagRight,
			double valueHorizontal, double valueVertical);
	public void setScaling(double valueHorizontal, double valueVertical);
	//
	public void setGeneralFont(Font font);
	//
	public void setGeneralForeground(Color c);
	public void setGeneralSpaceColor(Color c);
	public void setGeneralBackground(Color c);
	public void setAlarmColors(Color fc, Color bc);
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue;
	public Term standardizeRange(Term value, ChoisePoint iX) throws RejectRange;
}
