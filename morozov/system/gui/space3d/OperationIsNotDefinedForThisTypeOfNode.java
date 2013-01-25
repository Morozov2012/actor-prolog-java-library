// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

public class OperationIsNotDefinedForThisTypeOfNode extends RuntimeException {
	//
	public ContentType type;
	//
	public OperationIsNotDefinedForThisTypeOfNode(ContentType t) {
		type= t;
	}
	//
	public String toString() {
		return	this.getClass().toString() +
			"(" + type.toString() + ")";
	}
}
