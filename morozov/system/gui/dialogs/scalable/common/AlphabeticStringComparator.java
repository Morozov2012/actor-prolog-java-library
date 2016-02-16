/*
 * @(#)AlphabeticStringComparator.java 1.0 2015/04/25
 *
 * Copyright 2015 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import java.util.Comparator;

public class AlphabeticStringComparator implements Comparator<StringWithNumber> {
	public int compare(StringWithNumber o1, StringWithNumber o2) {
		String s1= o1.text;
		String s2= o2.text;
		return s1.toLowerCase().compareTo(s2.toLowerCase());
	}
}
