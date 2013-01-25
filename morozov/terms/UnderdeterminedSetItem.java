// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

public abstract class UnderdeterminedSetItem extends UnderdeterminedSetWithTail {
	protected long name;
	public int hashCode() {
		return (int)name + tail.hashCode();
	}
}
