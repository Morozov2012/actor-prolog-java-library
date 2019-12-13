// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.frames.data;

import morozov.system.i3v1.frames.data.interfaces.*;

import java.util.Arrays;
import java.io.Serializable;

public class I3CameraFlashAttributes implements I3CameraFlashAttributesInterface, Serializable {
	//
	protected byte[] m_pbyDeadData;
	protected double temperatureOffset;
	protected double temperatureGain;
	protected short recognizedCode1;
	protected short recognizedCode2;
	protected short recognizedCode3;
	protected double[] sortFpaTemp;
	protected double m_dDriftInitFactor;
	protected double m_dDriftTempCoeffi1;
	protected double m_dDriftTempCoeffi2;
	protected double m_dDriftTempCoeffi3;
	protected double m_dDriftTempCoeffi4;
	protected double[] m_dAmbientTempCoeffi;
	protected double[] ambientConstA;
	protected double[] ambientConstB;
	protected double checkcamberTemp;
	protected double[] ambientOffsetConstA;
	protected double[] ambientOffsetConstB;
	protected float[] m_pdSL_Gain;
	protected float[] m_pdSL_Offset;
	protected boolean m_bIsSLLoad;
	protected float[][] m_ppfMultiOs_OsData;
	protected double m_dInitOnTimeCorrFactor;
	protected double[] m_pdInitOnTimeCorr;
	protected double[] m_pdMultiOs_FpaTemp;
	protected boolean m_bIsMultiOs;
	protected boolean m_bIsSkimOs;
	//
	protected double[] m_pdSL_SkimOs;
	protected double[] m_pdShutterTemp;
	protected boolean[] m_AnomalousPixels;
	protected double m_dShutterCenterTemp;
	protected int numberOfVoltageAnomalousPixels;
	protected int numberOfTemperatureAnomalousPixels;
	//
	private static final long serialVersionUID= 0x39A4061AF75305D2L; // 4153451469230769618L;
	//
	// static {
	//	System.out.printf("CameraFlashAttributes: serialVersionUID: %x\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public I3CameraFlashAttributes(
			byte[] givenMpbyDeadData,
			double givenTemperatureOffset,
			double givenTemperatureGain,
			short givenRecognizedCode1,
			short givenRecognizedCode2,
			short givenRecognizedCode3,
			double[] givenSortFpaTemp,
			double givenMdDriftInitFactor,
			double givenDriftTempCoeffi1,
			double givenDriftTempCoeffi2,
			double givenDriftTempCoeffi3,
			double givenDriftTempCoeffi4,
			double[] givenMdAmbientTempCoeffi,
			// calcAmbientCoeffi()
			double[] givenAmbientConstA,
			double[] givenAmbientConstB,
			double givenCheckcamberTemp,
			// calcAmbientTempOffset()
			double[] givenAmbientOffsetConstA,
			double[] givenAmbientOffsetConstB,
			float[] givenMpdSLGain,
			float[] givenMpdSLOffset,
			boolean givenMbIsSLLoad,
			float[][] givenMppfMultiOs_OsData,
			double givenMdInitOnTimeCorrFactor,
			double[] givenMpdInitOnTimeCorr,
			double[] givenMpdMultiOs_FpaTemp,
			boolean givenMbIsMultiOs,
			boolean givenMbIsSkimOs,
			//
			double[] givenMpdSL_SkimOs,
			double[] givenMpdShutterTemp,
			boolean[] givenMAnomalousPixels,
			double givenMdShutterCenterTemp,
			int givenNumberOfVoltageAnomalousPixels,
			int givenNumberOfTemperatureAnomalousPixels
			) {
		m_pbyDeadData= Arrays.copyOf(givenMpbyDeadData,givenMpbyDeadData.length);
		temperatureOffset= givenTemperatureOffset;
		temperatureGain= givenTemperatureGain;
		recognizedCode1= givenRecognizedCode1;
		recognizedCode2= givenRecognizedCode2;
		recognizedCode3= givenRecognizedCode3;
		sortFpaTemp= Arrays.copyOf(givenSortFpaTemp,givenSortFpaTemp.length);
		m_dDriftInitFactor= givenMdDriftInitFactor;
		m_dDriftTempCoeffi1= givenDriftTempCoeffi1;
		m_dDriftTempCoeffi2= givenDriftTempCoeffi2;
		m_dDriftTempCoeffi3= givenDriftTempCoeffi3;
		m_dDriftTempCoeffi4= givenDriftTempCoeffi4;
		m_dAmbientTempCoeffi= Arrays.copyOf(givenMdAmbientTempCoeffi,givenMdAmbientTempCoeffi.length);
		// calcAmbientCoeffi();
		ambientConstA= Arrays.copyOf(givenAmbientConstA,givenAmbientConstA.length);
		ambientConstB= Arrays.copyOf(givenAmbientConstB,givenAmbientConstB.length);
		checkcamberTemp= givenCheckcamberTemp;
		// calcAmbientTempOffset();
		ambientOffsetConstA= Arrays.copyOf(givenAmbientOffsetConstA,givenAmbientOffsetConstA.length);
		ambientOffsetConstB= Arrays.copyOf(givenAmbientOffsetConstB,givenAmbientOffsetConstB.length);
		m_pdSL_Gain= Arrays.copyOf(givenMpdSLGain,givenMpdSLGain.length);
		m_pdSL_Offset= Arrays.copyOf(givenMpdSLOffset,givenMpdSLOffset.length);
		m_bIsSLLoad= givenMbIsSLLoad;
		int size1= givenMppfMultiOs_OsData.length;
		int size2= givenMppfMultiOs_OsData[0].length;
		m_ppfMultiOs_OsData= new float[size1][size2];
		for (int k=0; k < size1; k++) {
			System.arraycopy(givenMppfMultiOs_OsData[k],0,m_ppfMultiOs_OsData[k],0,size2);
		};
		m_dInitOnTimeCorrFactor= givenMdInitOnTimeCorrFactor;
		m_pdInitOnTimeCorr= Arrays.copyOf(givenMpdInitOnTimeCorr,givenMpdInitOnTimeCorr.length);
		m_pdMultiOs_FpaTemp= Arrays.copyOf(givenMpdMultiOs_FpaTemp,givenMpdMultiOs_FpaTemp.length);
		m_bIsMultiOs= givenMbIsMultiOs;
		m_bIsSkimOs= givenMbIsSkimOs;
		//
		m_pdSL_SkimOs= Arrays.copyOf(givenMpdSL_SkimOs,givenMpdSL_SkimOs.length);
		m_pdShutterTemp= Arrays.copyOf(givenMpdShutterTemp,givenMpdShutterTemp.length);
		m_AnomalousPixels= Arrays.copyOf(givenMAnomalousPixels,givenMAnomalousPixels.length);
		m_dShutterCenterTemp= givenMdShutterCenterTemp;
		numberOfVoltageAnomalousPixels= givenNumberOfVoltageAnomalousPixels;
		numberOfTemperatureAnomalousPixels= givenNumberOfTemperatureAnomalousPixels;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public byte[] getDeadData() {
		return m_pbyDeadData;
	}
	@Override
	public double getTemperatureOffset() {
		return temperatureOffset;
	}
	@Override
	public double getTemperatureGain() {
		return temperatureGain;
	}
	@Override
	public short getRecognizedCode1() {
		return recognizedCode1;
	}
	@Override
	public short getRecognizedCode2() {
		return recognizedCode2;
	}
	@Override
	public short getRecognizedCode3() {
		return recognizedCode3;
	}
	@Override
	public double[] getSortFpaTemp() {
		return sortFpaTemp;
	}
	@Override
	public double getDriftInitFactor() {
		return m_dDriftInitFactor;
	}
	@Override
	public double getDriftTempCoeffi1() {
		return m_dDriftTempCoeffi1;
	}
	@Override
	public double getDriftTempCoeffi2() {
		return m_dDriftTempCoeffi2;
	}
	@Override
	public double getDriftTempCoeffi3() {
		return m_dDriftTempCoeffi3;
	}
	@Override
	public double getDriftTempCoeffi4() {
		return m_dDriftTempCoeffi4;
	}
	@Override
	public double[] getAmbientTempCoeffi() {
		return m_dAmbientTempCoeffi;
	}
	// public calcAmbientCoeffi();
	@Override
	public double[] getAmbientConstA() {
		return ambientConstA;
	}
	@Override
	public double[] getAmbientConstB() {
		return ambientConstB;
	}
	@Override
	public double getCheckcamberTemp() {
		return checkcamberTemp;
	}
	// public calcAmbientTempOffset();
	@Override
	public double[] getAmbientOffsetConstA() {
		return ambientOffsetConstA;
	}
	@Override
	public double[] getAmbientOffsetConstB() {
		return ambientOffsetConstB;
	}
	@Override
	public float[] getSLGain() {
		return m_pdSL_Gain;
	}
	@Override
	public float[] getSLOffset() {
		return m_pdSL_Offset;
	}
	@Override
	public boolean isSLLoad() {
		return m_bIsSLLoad;
	}
	@Override
	public float[][] getMultiOsData() {
		return m_ppfMultiOs_OsData;
	}
	@Override
	public double getInitOnTimeCorrFactor() {
		return m_dInitOnTimeCorrFactor;
	}
	@Override
	public double[] getInitOnTimeCorr() {
		return m_pdInitOnTimeCorr;
	}
	@Override
	public double[] getMultiOs_FpaTemp() {
		return m_pdMultiOs_FpaTemp;
	}
	@Override
	public boolean isMultiOs() {
		return m_bIsMultiOs;
	}
	@Override
	public boolean isSkimOs() {
		return m_bIsSkimOs;
	}
	//
	@Override
	public double[] getSLSkimOs() {
		return m_pdSL_SkimOs;
	}
	@Override
	public double[] getShutterTemp() {
		return m_pdShutterTemp;
	}
	@Override
	public boolean[] getAnomalousPixels() {
		return m_AnomalousPixels;
	}
	@Override
	public double getShutterCenterTemp() {
		return m_dShutterCenterTemp;
	}
	@Override
	public int getNumberOfVoltageAnomalousPixels() {
		return numberOfVoltageAnomalousPixels;
	}
	@Override
	public int getNumberOfTemperatureAnomalousPixels() {
		return numberOfTemperatureAnomalousPixels;
	}
}
