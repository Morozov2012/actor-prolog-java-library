// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class WindowedR2 {
	//
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
	//
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
	//
	///////////////////////////////////////////////////////////////
	//
	public static WindowedR2 fastWindowedR2(long[] vectorT, long[] vectorF, int windowHalfwidth) {
		int realLength= vectorT.length;
		if (realLength < 2) {
			return new WindowedR2(realLength);
		};
		int fullLength= (int)(vectorT[realLength-1] - vectorT[0] + 1);
		double[] x1= new double[fullLength];
		double[] y1= new double[fullLength];
		double[] xy= new double[fullLength];
		double[] x2= new double[fullLength];
		double[] y2= new double[fullLength];
		// double[] allWindowedR2Values= new double[fullLength];
		boolean[] isSelectedWindowedR2Value= new boolean[fullLength];
		double[] selectedWindowedR2Values= new double[realLength];
		x1[0]= vectorT[0];
		y1[0]= vectorF[0];
		xy[0]= vectorT[0] * vectorF[0];
		x2[0]= vectorT[0] * vectorT[0];
		y2[0]= vectorF[0] * vectorF[0];
		// allWindowedR2Values[0]= -1.0;
		isSelectedWindowedR2Value[0]= true;
		selectedWindowedR2Values[0]= -1.0;
		long time1= (long)vectorT[0];
		double value1= vectorF[0];
		int counter1= 0;
                for (int n=1; n < realLength; n++) {
			long time2= (long)vectorT[n];
			double value2= vectorF[n];
			long deltaTime= time2 - time1;
			if (deltaTime <= 1) {
				counter1= counter1 + 1;
				x1[counter1]= time2;
				y1[counter1]= value2;
				xy[counter1]= time2 * value2;
				x2[counter1]= time2 * time2;
				y2[counter1]= value2 * value2;
				// allWindowedR2Values[counter1]= -1.0;
			} else {
				double k= (value2-value1)/(time2-time1);
				for (int t=1; t <= deltaTime; t++) {
					long time3= time1 + t;
					double value3= k*t + value1;
					counter1= counter1 + 1;
					x1[counter1]= time3;
					y1[counter1]= value3;
					xy[counter1]= time3 * value3;
					x2[counter1]= time3 * time3;
					y2[counter1]= value3 * value3;
					// allWindowedR2Values[counter1]= -1.0;
				}
			};
			isSelectedWindowedR2Value[counter1]= true;
			selectedWindowedR2Values[n]= -1.0;
			time1= time2;
			value1= value2;
		};
		double windowedR2Sum1= 0.0;
		double windowedR2Sum2= 0.0;
		double windowedR2Sum3= 0.0;
		double windowedR2Sum4= 0.0;
		int counter2= 0;
		for (int center=1; center < windowHalfwidth; center++) {
			if (center > fullLength-1) {
				break;
			};
			if (isSelectedWindowedR2Value[center]) {
				counter2= counter2 + 1;
			}
		};
		int counter3= 0;
		for (int center=windowHalfwidth; center < fullLength - windowHalfwidth; center++) {
			int from= center - windowHalfwidth;
			int to= center + windowHalfwidth;
			double sumX= 0.0;	// Сумма X-ов.
			double sumY= 0.0;	// Сумма Y-ов.
			double sumXY= 0.0;	// Нескорректированная сумма парных произведений.
			double sumX2= 0.0;	// Нескорректированная сумма квадратов X-в.
			double sumY2= 0.0;	// Нескорректированная сумма квадратов Y-в.
			int nPoints= to - from + 1;	// Количество точек.
			for (int n=from; n <= to; n++) {
				sumX= sumX + x1[n];
				sumY= sumY + y1[n];
				sumXY= sumXY + xy[n];
				sumX2= sumX2 + x2[n];
				sumY2= sumY2 + y2[n];
			};
			double xyC= sumX*sumY / nPoints;	// Коррекция на среднее.
			double cXY= sumXY - xyC;		// Скорректированная сумма произведений X и Y.
			double x2C= sumX*sumX / nPoints;	// Коррекция на среднее значение X-в.
			double cSX= sumX2 - x2C;		// Скорректированная сумма квадратов X-в.
			double a1= cXY / cSX;
			double b1= (sumY - a1*sumX) / nPoints;
			// Полная (скорректированная) сумма квадратов:
			double sumYY2= sumY2 - sumY*sumY / nPoints;
			// Сумма квадратов, обусловленная регерессией:
			double ss= cXY*cXY / cSX;
			double r2= ss / sumYY2;
			// allWindowedR2Values[center]= r2;
			windowedR2Sum1= windowedR2Sum1 + r2;
			double XYk= r2 * r2;
			windowedR2Sum2= windowedR2Sum2 + XYk;
			XYk= XYk * r2;
			windowedR2Sum3= windowedR2Sum3 + XYk;
			XYk= XYk * r2;
			windowedR2Sum4= windowedR2Sum4 + XYk;
			if (isSelectedWindowedR2Value[center]) {
				counter2= counter2 + 1;
				counter3= counter3 + 1;
				selectedWindowedR2Values[counter2]= r2;
			}
		};
		int windowedR2Cardinality= counter3;
		// int from= windowHalfwidth;
		// int to= fullLength-windowHalfwidth-1;
		// int nPoints= to - from + 1;	// Количество точек.
		int nPoints= fullLength - 2*windowHalfwidth;	// Количество точек.
		if (nPoints < 0) {
			nPoints= 0;
		};
		double moment1= windowedR2Sum1 / nPoints;
		double moment2= windowedR2Sum2 / nPoints;
		double moment3= windowedR2Sum3 / nPoints;
		double moment4= windowedR2Sum4 / nPoints;
		double squaredMoment1= moment1 * moment1;
		double centralMoment2= moment2 - squaredMoment1;
		double centralMoment3= moment3 - 3*moment2*moment1 + 2*moment1*squaredMoment1;
		double centralMoment4= moment4 - 4*moment3*moment1 + 6*moment2*squaredMoment1 - 3*squaredMoment1*squaredMoment1;
		double windowedR2Mean= moment1;
		double sigma= StrictMath.sqrt(centralMoment2);
		double windowedR2StandardDeviation= sigma;
		double windowedR2Skewness= centralMoment3 / (centralMoment2*sigma);
		double windowedR2Kurtosis= centralMoment4 / (centralMoment2*centralMoment2);
		if (nPoints > 1) {
			double windowedR2BiasCorrectedVariance= centralMoment2 * nPoints/(nPoints-1.0);
			windowedR2StandardDeviation= StrictMath.sqrt(windowedR2BiasCorrectedVariance);
			if (nPoints >= 3) {
				windowedR2Skewness= windowedR2Skewness * StrictMath.sqrt((nPoints-1.0)/nPoints) * nPoints/(nPoints-2.0);
				if (nPoints >= 4) {
					windowedR2Kurtosis= ((nPoints+1.0)*windowedR2Kurtosis - 3.0*(nPoints-1.0)) * (nPoints-1.0)/((nPoints-2.0)*(nPoints-3.0)) + 3.0;
				}
			}
		} else {
			double windowedR2Variance= centralMoment2;
			windowedR2StandardDeviation= StrictMath.sqrt(windowedR2Variance);
		};
		windowedR2Kurtosis= windowedR2Kurtosis - 3.0;
		return new WindowedR2(
				windowedR2Cardinality,
				windowedR2Mean,
				windowedR2StandardDeviation,
				windowedR2Skewness,
				windowedR2Kurtosis,
				selectedWindowedR2Values);
	}
}
