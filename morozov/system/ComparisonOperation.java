// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import java.math.BigInteger;

public enum ComparisonOperation {
	GT {
		boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) > 0;
		}
		boolean eval(double n1, double n2) {
			return n1 > n2;
		}
		boolean eval(String s1, String s2) {
			return s1.compareTo(s2) > 0;
		}
	},
	GE {
		boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) >= 0;
		}
		boolean eval(double n1, double n2) {
			return n1 >= n2;
		}
		boolean eval(String s1, String s2) {
			return s1.compareTo(s2) >= 0;
		}
	},
	LT {
		boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) < 0;
		}
		boolean eval(double n1, double n2) {
			return n1 < n2;
		}
		boolean eval(String s1, String s2) {
			return s1.compareTo(s2) < 0;
		}
	},
	LE {
		boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) <= 0;
		}
		boolean eval(double n1, double n2) {
			return n1 <= n2;
		}
		boolean eval(String s1, String s2) {
			return s1.compareTo(s2) <= 0;
		}
	},
	NE {
		boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) != 0;
		}
		boolean eval(double n1, double n2) {
			return n1 != n2;
		}
		boolean eval(String s1, String s2) {
			return s1.compareTo(s2) != 0;
		}
	};
	abstract boolean eval(BigInteger n1, BigInteger n2);
	abstract boolean eval(double n1, double n2);
	abstract boolean eval(String n1, String n2);
}
