// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

public class InternalName {
	//
	protected String name;
	protected int position;
	//
	public InternalName(String n, int p) {
		name= n;
		position= p;
	}
	//
	public String getName() {
		return name;
	}
	public int getPosition() {
		return position;
	}
	//
	@Override
	public String toString() {
		return name;
	}
}
