/*
 * @(#)ScalableTableColors.java 1.0 2012/08/23
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ScalableTableColors implementation for the Actor Prolog language
 * @version 1.0 2012/08/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.terms.*;

public class ScalableTableColors {
	//
	protected Term titleForegroundColor;
	protected Term titleBackgroundColor;
	protected Term cellBackgroundColor;
	protected Term emptyCellsColor;
	//
	public ScalableTableColors(Term titleText, Term titleBackground, Term cellBackground, Term emptyCells) {
		titleForegroundColor= titleText;
		titleBackgroundColor= titleBackground;
		cellBackgroundColor= cellBackground;
		emptyCellsColor= emptyCells;
	}
}
