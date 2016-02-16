// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.worlds.remote.errors;

import morozov.terms.*;
import morozov.worlds.remote.*;

public class WrongArgumentIsNotBufferedImage extends RuntimeException {
	protected ExternalWorldInterface world1;
	protected Term world2;
	public WrongArgumentIsNotBufferedImage(ExternalWorldInterface w) {
		world1= w;
	}
	public WrongArgumentIsNotBufferedImage(Term w) {
		world2= w;
	}
	public String toString() {
		if (world1 != null) {
			return	this.getClass().toString() + "(" + world1.toString() + ")";
		} else if (world2 != null) {
			return	this.getClass().toString() + "(" + world2.toString() + ")";
		} else {
			return	this.getClass().toString();
		}
	}
}
