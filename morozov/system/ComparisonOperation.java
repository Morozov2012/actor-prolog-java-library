// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import java.math.BigInteger;

public enum ComparisonOperation {
	GT {
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) > 0;
		}
		public boolean eval(double n1, double n2) {
			return n1 > n2;
		}
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) > 0;
		}
	},
	GE {
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) >= 0;
		}
		public boolean eval(double n1, double n2) {
			return n1 >= n2;
		}
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) >= 0;
		}
	},
	LT {
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) < 0;
		}
		public boolean eval(double n1, double n2) {
			return n1 < n2;
		}
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) < 0;
		}
	},
	LE {
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) <= 0;
		}
		public boolean eval(double n1, double n2) {
			return n1 <= n2;
		}
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) <= 0;
		}
	},
	NE {
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) != 0;
		}
		public boolean eval(double n1, double n2) {
			return n1 != n2;
		}
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) != 0;
		}
		public ComparisonOperation reverse() {
			return ComparisonOperation.NE;
		}
	};
	abstract public boolean eval(BigInteger n1, BigInteger n2);
	abstract public boolean eval(double n1, double n2);
	abstract public boolean eval(String n1, String n2);
}
