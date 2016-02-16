// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.worlds;

public class GlobalWorldIdentifier {
	protected Long value;
	public GlobalWorldIdentifier(long id) {
		value= id;
	}
	public int hashCode() {
		return value.hashCode();
	}
	public boolean equals(Object o2) {
		if (o2 instanceof GlobalWorldIdentifier) {
			GlobalWorldIdentifier i2= (GlobalWorldIdentifier)o2;
			return value.equals(i2.value);
		} else {
			return false;
		}
	}
	public int compare(GlobalWorldIdentifier i2) {
		return Long.compare(value,i2.value);
	}
}
