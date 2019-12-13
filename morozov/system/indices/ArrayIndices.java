// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.indices;

import java.math.BigInteger;

public class ArrayIndices {
	//
	protected BigInteger[] vector;
	//
	public ArrayIndices(BigInteger[] list) {
		vector= new BigInteger[list.length];
		System.arraycopy(list,0,vector,0,list.length);
	}
	//
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof ArrayIndices) ) {
				return false;
			} else {
				ArrayIndices i= (ArrayIndices) o;
				if (i.vector.length==vector.length) {
					for (int n=0; n < vector.length; n++) {
						if (i.vector[n].compareTo(vector[n]) != 0) {
							return false;
						}
					};
					return true;
				} else {
					return false;
				}
			}
		}
	}
	//
	@Override
	public int hashCode() {
		int sum= 0;
		for (int n=0; n < vector.length; n++) {
			sum+= vector[n].intValue();
		};
		return sum;
	}
	//
	@Override
	public String toString() {
		StringBuilder buffer= new StringBuilder();
		for (int n=0; n < vector.length; n++) {
			if (n > 0) {
				buffer.append(",");
			};
			buffer.append(vector[n].toString());
		};
		return "{" + buffer.toString() + "}";
	}
}
