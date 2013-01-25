// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.terms.*;

import java.math.BigInteger;

public class NodeLabel {
	//
	public String textName= "";
	public BigInteger numericalName;
	public boolean isTextName= true;
	//
	public NodeLabel(String text) {
		textName= text;
		isTextName= true;
	}
	public NodeLabel(BigInteger number) {
		numericalName= number;
		isTextName= false;
	}
	public Term toTerm() {
		if (isTextName) {
			return new PrologString(textName);
		} else {
			return new PrologInteger(numericalName);
		}
	}
	//
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof NodeLabel) ) {
				return false;
			} else {
				NodeLabel i= (NodeLabel) o;
				if (isTextName) {
					if (i.isTextName) {
						return textName.equals(i.textName);
					} else {
						return false;
					}
				} else {
					if (! i.isTextName) {
						return numericalName.equals(i.numericalName);
					} else {
						return false;
					}
				}
			}
		}
	}
	//
	public int hashCode() {
		if (isTextName) {
			return textName.hashCode() * 2 + 1;
		} else {
			return numericalName.hashCode() * 2;
		}
	}
	//
	public String toString() {
		if (isTextName) {
			return textName;
		} else {
			return numericalName.toString();
		}
	}
}
