// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.errors;

import morozov.system.gui.space3d.*;

public class InappropriateLabeledObject extends RuntimeException {
	//
	protected NodeLabel label;
	//
	public InappropriateLabeledObject(NodeLabel l) {
		label= l;
	}
	//
	@Override
	public String toString() {
		return	this.getClass().toString() +
			"(" + label.toString() + ")";
	}
}
