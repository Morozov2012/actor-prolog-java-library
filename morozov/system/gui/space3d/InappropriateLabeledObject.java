// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

public class InappropriateLabeledObject extends RuntimeException {
	//
	public NodeLabel label;
	//
	public InappropriateLabeledObject(NodeLabel l) {
		label= l;
	}
	//
	public String toString() {
		return	this.getClass().toString() +
			"(" + label.toString() + ")";
	}
}
