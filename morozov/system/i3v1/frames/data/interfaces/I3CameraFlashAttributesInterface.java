// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.frames.data.interfaces;

public interface I3CameraFlashAttributesInterface {
	//
	public byte[] getDeadData();
	public double getTemperatureOffset();
	public double getTemperatureGain();
	public short getRecognizedCode1();
	public short getRecognizedCode2();
	public short getRecognizedCode3();
	public double[] getSortFpaTemp();
	public double getDriftInitFactor();
	public double getDriftTempCoeffi1();
	public double getDriftTempCoeffi2();
	public double getDriftTempCoeffi3();
	public double getDriftTempCoeffi4();
	public double[] getAmbientTempCoeffi();
	public double[] getAmbientConstA();
	public double[] getAmbientConstB();
	public double getCheckcamberTemp();
	public double[] getAmbientOffsetConstA();
	public double[] getAmbientOffsetConstB();
	public float[] getSLGain();
	public float[] getSLOffset();
	public boolean isSLLoad();
	public float[][] getMultiOsData();
	public double getInitOnTimeCorrFactor();
	public double[] getInitOnTimeCorr();
	public double[] getMultiOs_FpaTemp();
	public boolean isMultiOs();
	public boolean isSkimOs();
	//
	public double[] getSLSkimOs();
	public double[] getShutterTemp();
	public boolean[] getAnomalousPixels();
	public double getShutterCenterTemp();
	public int getNumberOfVoltageAnomalousPixels();
	public int getNumberOfTemperatureAnomalousPixels();
}
