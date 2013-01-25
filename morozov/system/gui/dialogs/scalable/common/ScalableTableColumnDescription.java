/*
 * @(#)ScalableTableColumnDescription.java 1.0 2012/08/23
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import morozov.terms.*;

/*
 * ScalableTableColumnDescription implementation for the Actor Prolog language
 * @version 1.0 2012/08/23
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableTableColumnDescription {
	public String name;
	public double width;
	public TextAlignment horizontalAlignment;
	public Term foregroundColor;
	public Term backgroundColor;
	public ScalableTableColumnDescription(String text, double size, TextAlignment hAlign, Term foreground, Term background) {
		name= text;
		width= size;
		horizontalAlignment= hAlign;
		foregroundColor= foreground;
		backgroundColor= background;
	}
}
