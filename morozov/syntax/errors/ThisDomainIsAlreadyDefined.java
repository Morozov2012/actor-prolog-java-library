// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class ThisDomainIsAlreadyDefined extends ParserError {
	//
	protected String name;
	//
	public ThisDomainIsAlreadyDefined(String n, int position) {
		super(position);
		name= n;
	}
	//
	public String getName() {
		return name;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(name: " + name + "; position:" + Integer.toString(position) + ")";
	}
}
