// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.terms.*;

import java.math.BigInteger;

public enum ComparisonOperation {
	//
	GT {
		@Override
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) > 0;
		}
		@Override
		public boolean eval(double n1, double n2) {
			return n1 > n2;
		}
		@Override
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) > 0;
		}
		@Override
		public boolean eval(byte[] s1, byte[] s2) {
			return PrologBinary.compareTwoBinaries(s1,s2) > 0;
		}
	},
	GE {
		@Override
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) >= 0;
		}
		@Override
		public boolean eval(double n1, double n2) {
			return n1 >= n2;
		}
		@Override
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) >= 0;
		}
		@Override
		public boolean eval(byte[] s1, byte[] s2) {
			return PrologBinary.compareTwoBinaries(s1,s2) >= 0;
		}
	},
	LT {
		@Override
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) < 0;
		}
		@Override
		public boolean eval(double n1, double n2) {
			return n1 < n2;
		}
		@Override
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) < 0;
		}
		@Override
		public boolean eval(byte[] s1, byte[] s2) {
			return PrologBinary.compareTwoBinaries(s1,s2) < 0;
		}
	},
	LE {
		@Override
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) <= 0;
		}
		@Override
		public boolean eval(double n1, double n2) {
			return n1 <= n2;
		}
		@Override
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) <= 0;
		}
		@Override
		public boolean eval(byte[] s1, byte[] s2) {
			return PrologBinary.compareTwoBinaries(s1,s2) <= 0;
		}
	},
	NE {
		@Override
		public boolean eval(BigInteger n1, BigInteger n2) {
			return n1.compareTo(n2) != 0;
		}
		@Override
		public boolean eval(double n1, double n2) {
			return n1 != n2;
		}
		@Override
		public boolean eval(String s1, String s2) {
			return s1.compareTo(s2) != 0;
		}
		@Override
		public boolean eval(byte[] s1, byte[] s2) {
			return PrologBinary.compareTwoBinaries(s1,s2) != 0;
		}
/*
		public ComparisonOperation reverse() {
			return ComparisonOperation.NE;
		}
*/
	};
	abstract public boolean eval(BigInteger n1, BigInteger n2);
	abstract public boolean eval(double n1, double n2);
	abstract public boolean eval(String n1, String n2);
	abstract public boolean eval(byte[] n1, byte[] n2);
}
