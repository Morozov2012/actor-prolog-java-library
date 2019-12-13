/*
 * @(#)StringWithNumber.java 1.0 2015/04/25
 *
 * Copyright 2015 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

public class StringWithNumber implements Comparable<StringWithNumber> {
	//
	protected int number;
	protected String text;
	//
	public StringWithNumber(int n, String t) {
		number= n;
		text= t;
	}
	//
	@Override
	public int compareTo(StringWithNumber o) {
		return text.toLowerCase().compareTo(o.text.toLowerCase());
	}
	//
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof StringWithNumber) ) {
				return false;
			} else {
				StringWithNumber s= (StringWithNumber) o;
					return text.equals(s.text);
			}
		}
	}
	//
	@Override
	public int hashCode() {
		return text.hashCode();
	}
	//
	@Override
	public String toString() {
		return text;
	}
}
