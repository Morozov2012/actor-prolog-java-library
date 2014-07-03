// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class WindowedR2 {
	public long cardinality= 0;
	public static double noValue= -1.0;
	public double mean= noValue;
	public double standardDeviation= 0.0;
	public double skewness= 0.0;
	public double kurtosis= 0.0;
	public double[] referentValues;
	public WindowedR2() {
		referentValues= new double[0];
	}
	public WindowedR2(int length) {
		if (length < 0) {
			length= 0;
		};
		cardinality= length;
		referentValues= new double[length];
		for (int n=0; n < length; n++) {
			referentValues[n]= -1.0;
		}
	}
	public WindowedR2(long c, double m, double sigma, double s, double k, double[] vector) {
		cardinality= c;
		mean= m;
		standardDeviation= sigma;
		skewness= s;
		kurtosis= k;
		referentValues= vector;
	}
}
