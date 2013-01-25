// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.indices;

import java.math.BigInteger;

public class ArrayIndices {
	public BigInteger[] vector;
	public ArrayIndices(BigInteger[] list) {
		vector= new BigInteger[list.length];
		for (int n=0; n < list.length; n++) {
			vector[n]= list[n];
		};
	}
	//
	public boolean equals(Object o) {
		// System.out.printf("ArrayIndices:: %s ??? %s\n\n",o,this);
		// System.out.printf("ArrayIndices:: VECTOR\n");
		// for (int n=0; n < vector.length; n++) {
		//	System.out.printf(" %s ",vector[n]);
		// };
		// System.out.printf("O.K.\n\n");
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
	public int hashCode() {
		int sum= 0;
		for (int n=0; n < vector.length; n++) {
			sum+= vector[n].intValue();
		};
		// System.out.printf("ArrayIndices:: HASH\n");
		// for (int n=0; n < vector.length; n++) {
		//	System.out.printf(" %s ",vector[n]);
		// };
		// System.out.printf("==> %s\n\n",sum);
		return sum;
	}
	//
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
