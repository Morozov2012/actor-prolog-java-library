/*
 * @(#)ScalableTableColumnDescription.java 1.0 2012/08/23
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ScalableTableColumnDescription implementation for the Actor Prolog language
 * @version 1.0 2012/08/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.terms.*;

public class ScalableTableColumnDescription {
	//
	protected String name;
	protected double width;
	protected TextAlignment horizontalAlignment;
	protected Term foregroundColor;
	protected Term backgroundColor;
	//
	public ScalableTableColumnDescription(String text, int size, TextAlignment hAlign, Term foreground, Term background) {
		name= text;
		width= size;
		horizontalAlignment= hAlign;
		foregroundColor= foreground;
		backgroundColor= background;
	}
}
