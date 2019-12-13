// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1;

import java.util.Arrays;

public class ShutterLess {
	//
	protected CommonData commData;
	//
	protected double m_dTemperatureGain= 1.0d;
	protected double m_dTemperatureOffset= 0.0d;
	protected double m_dTestTempOffset= 0.0d;
	//
	protected float[] m_pdSL_Gain= null;
	protected float[] m_pdSL_Offset= null;
	protected boolean m_bIsSLLoad= false;
	//
	protected double[] sortFpaTemp= new double[]{0.0d, 0.0d, 0.0d};
	protected double[] ambientConstA= new double[]{0.0d, 0.0d};
	protected double[] ambientConstB= new double[]{0.0d, 0.0d};
	protected double[] ambientOffsetConstA= new double[]{0.0d, 0.0d, 0.0d, 0.0d};
	protected double[] ambientOffsetConstB= new double[]{0.0d, 0.0d, 0.0d, 0.0d};
	protected double[] m_dAmbientTempCoeffi= new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
	protected double m_dDriftInitFactor= 0.0d;
	protected double m_dDriftTempCoeffi1= 0.0d;
	protected double m_dDriftTempCoeffi2= 0.0d;
	protected double m_dDriftTempCoeffi3= 0.0d;
	protected double m_dDriftTempCoeffi4= 0.0d;
	protected double checkcamberTemp= 0.0d;
	//
	protected double m_dEmissivity= 1.0d;
	protected double[] m_pdSL_SkimOs= null;
	//
	protected double m_dInitOnTimeCorrFactor= 1.0d;
	protected double[] m_pdInitOnTimeCorr= new double[]{0.1475d, -2.0964d, 11.024d, -26.576d, 24.463d};
	//
	protected static double[] multiChamberTemp_m20_10_50= new double[]{-20.0d, 10.0d, 50.0d};
	protected static double[] multiChamberTemp_m10_0_10_25_40= new double[]{-10.0d, 0.0d, 10.0d, 25.0d, 40.0d};
	//
	protected static final double coefficient11= 7146.4357337d;
	protected static final double coefficient12= 9.8736116d;
	protected static final double coefficient13= 169533.38422877376d;
	protected static final double coefficient14= -411.744319d;
	protected static final double coefficient15= 4.9368058d;
	//
	protected static final double coefficient21= 20.0d;
	protected static final double coefficient22= 8.716667d;
	//
	///////////////////////////////////////////////////////////////
	//
	public ShutterLess(CommonData c) {
		commData= c;
		initWindowingData();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setTemperatureGain(double value) {
		m_dTemperatureGain= value;
	}
	public double getTemperatureGain() {
		return m_dTemperatureGain;
	}
	//
	public void setTemperatureOffset(double value) {
		m_dTemperatureOffset= value;
	}
	public double getTemperatureOffset() {
		return m_dTemperatureOffset;
	}
	//
	public void setTestTempOffset(double value) {
		m_dTestTempOffset= value;
	}
	public double getTestTempOffset() {
		return m_dTestTempOffset;
	}
	//
	public void setEmissivity(double value) {
		m_dEmissivity= value;
	}
	public double getEmissivity() {
		return m_dEmissivity;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initWindowingData() {
		m_pdSL_Gain= new float[(commData.getWindowingSize() * 4)];
		m_pdSL_Offset= new float[(commData.getWindowingSize() * 4)];
		m_pdSL_SkimOs= new double[commData.getWindowingSize()];
	}
	//
	public double[] getSortFpaTemp() {
		return sortFpaTemp;
	}
	//
	public void setSortFpaTemp(double[] temp) {
		sortFpaTemp= temp;
	}
	//
	public void setFpaTemp1(double temp) {
		sortFpaTemp[0]= temp;
	}
	public void setFpaTemp2(double temp) {
		sortFpaTemp[1]= temp;
	}
	public void setFpaTemp3(double temp) {
		sortFpaTemp[2]= temp;
	}
	//
	public double[] getAmbientTempCoeffi() {
		return m_dAmbientTempCoeffi;
	}
	//
	public void setAmbientTempCoeffi(double[] c) {
		m_dAmbientTempCoeffi= c;
	}
	//
	public void setAmbientTempCoeffi1(double c) {
		m_dAmbientTempCoeffi[0]= c;
	}
	public void setAmbientTempCoeffi2(double c) {
		m_dAmbientTempCoeffi[1]= c;
	}
	public void setAmbientTempCoeffi3(double c) {
		m_dAmbientTempCoeffi[2]= c;
	}
	public void setAmbientTempCoeffi4(double c) {
		m_dAmbientTempCoeffi[3]= c;
	}
	public void setAmbientTempCoeffi5(double c) {
		m_dAmbientTempCoeffi[4]= c;
	}
	//
	public int getCoefficientSize() {
		return 4;
	}
	//
	public double getDriftInitFactor() {
		return m_dDriftInitFactor;
	}
	//
	public void setDriftInitFactor(double _factor) {
		if (_factor== 0.0d) {
			m_dDriftInitFactor= 0.0d;
		} else {
			m_dDriftInitFactor= (_factor - coefficient21) / coefficient22;
		}
	}
	//
	public double getDriftTempCoeffi1() {
		return m_dDriftTempCoeffi1;
	}
	public double getDriftTempCoeffi2() {
		return m_dDriftTempCoeffi2;
	}
	public double getDriftTempCoeffi3() {
		return m_dDriftTempCoeffi3;
	}
	public double getDriftTempCoeffi4() {
		return m_dDriftTempCoeffi4;
	}
	//
	public void setDriftTempCoeffi1(double c) {
		m_dDriftTempCoeffi1= c;
	}
	public void setDriftTempCoeffi2(double c) {
		m_dDriftTempCoeffi2= c;
	}
	public void setDriftTempCoeffi3(double c) {
		m_dDriftTempCoeffi3= c;
	}
	public void setDriftTempCoeffi4(double c) {
		m_dDriftTempCoeffi4= c;
	}
	//
	public double[] getAmbientConstA() {
		return ambientConstA;
	}
	public void setAmbientConstA(double[] c) {
		ambientConstA= c;
	}
	//
	public double[] getAmbientConstB() {
		return ambientConstB;
	}
	public void setAmbientConstB(double[] c) {
		ambientConstB= c;
	}
	//
	public double getCheckcamberTemp() {
		return checkcamberTemp;
	}
	public void setCheckcamberTemp(double t) {
		checkcamberTemp= t;
	}
	//
	public void calcAmbientCoeffi() {
		Arrays.sort(sortFpaTemp);
		for (int pos=0; pos < 2; pos++) {
			ambientConstA[pos]= (multiChamberTemp_m20_10_50[pos + 1] - multiChamberTemp_m20_10_50[pos]) / (sortFpaTemp[pos + 1] - sortFpaTemp[pos]);
			ambientConstB[pos]= multiChamberTemp_m20_10_50[pos] - (ambientConstA[pos] * sortFpaTemp[pos]);
		};
		checkcamberTemp= sortFpaTemp[0];
	}
	//
	public double[] getAmbientOffsetConstA() {
		return ambientOffsetConstA;
	}
	public void setAmbientOffsetConstA(double[] c) {
		ambientOffsetConstA= c;
	}
	//
	public double[] getAmbientOffsetConstB() {
		return ambientOffsetConstB;
	}
	public void setAmbientOffsetConstB(double[] c) {
		ambientOffsetConstB= c;
	}
	//
	public void calcAmbientTempOffset() {
		for (int pos=0; pos < 4; pos++) {
			// The problem is in that m_dAmbientTempCoeffi==0,
			// because the camera has returned zeros.
			ambientOffsetConstA[pos]= (m_dAmbientTempCoeffi[pos + 1] - m_dAmbientTempCoeffi[pos]) / (multiChamberTemp_m10_0_10_25_40[pos + 1] - multiChamberTemp_m10_0_10_25_40[pos]);
			ambientOffsetConstB[pos]= m_dAmbientTempCoeffi[pos] - (ambientOffsetConstA[pos] * multiChamberTemp_m10_0_10_25_40[pos]);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double computeTargetTemperature(double value, boolean isCelsius) {
		double correctedValue= (coefficient11 - value) * coefficient12;
		if (coefficient13 < correctedValue) {
			return 0.0d;
		} else {
			double result= (((m_dTemperatureGain * (coefficient14 + Math.sqrt(coefficient13 - correctedValue))) / coefficient15) - m_dTemperatureOffset) + m_dTestTempOffset;
			if (!isCelsius) {
				result= CommonData.convCelsiusToFaherenheit(result);
			};
			return result;
		}
	}
	public double[] computeTargetTemperatures(boolean isCelsius) {
		double[] targetTemperatures= commData.getTargetTemperatures();
		return computeTargetTemperatures(targetTemperatures,isCelsius);
	}
	public double[] computeTargetTemperatures(double[] targetArray, boolean isCelsius) {
		double[] map= commData.m_pdTemperatureMap;
		int length= map.length;
		for (int k=0; k < length; k++) {
			double value= map[k];
			double correctedValue= (coefficient11 - value) * coefficient12;
			if (coefficient13 < correctedValue) {
				targetArray[k]= 0.0d;
			} else {
				targetArray[k]= (((m_dTemperatureGain * (coefficient14 + Math.sqrt(coefficient13 - correctedValue))) / coefficient15) - m_dTemperatureOffset) + m_dTestTempOffset;
			}
		};
		if (!isCelsius) {
			CommonData.convCelsiusToFaherenheit(targetArray);
		};
		return targetArray;
	}
	//
	public double calcAmbientChamberTemp(double fpaTemp) {
		if (fpaTemp < sortFpaTemp[1]) {
			return (ambientConstA[0] * fpaTemp) + ambientConstB[0];
		} else {
			return (ambientConstA[1] * fpaTemp) + ambientConstB[1];
		}
	}
}
