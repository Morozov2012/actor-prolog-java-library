// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1;

import java.lang.reflect.Array;

public class CommonData {
	//
	protected int m_iCameraMode;
	protected int m_iSensorWidth;
	protected int m_iSensorHeight;
	protected int m_iSensorSize;
	protected int m_iActiveHeight;
	protected int m_iActiveSize;
	protected int m_iTopCDSSize;
	protected int m_iBottomCDSSize;
	protected boolean m_bIsTopCDS;
	protected boolean m_bIsBottomCDS;
	//
	protected int m_iRecvSize;
	protected int m_iRecvByteSize;
	//
	protected int m_iWindowingColStart;
	protected int m_iWindowingColEnd;
	protected int m_iWindowingColSize;
	protected int m_iWindowingRowStart;
	protected int m_iWindowingRowEnd;
	protected int m_iWindowingRowSize;
	protected int m_iWindowingSize;
	//
	protected double[] m_pdMultiOs_FpaTemp= new double[4];
	protected float[][] m_ppfMultiOs_OsData;
	protected byte[] m_pbyDeadData;
	protected boolean[] m_AnomalousPixels;
	//
	protected int[] m_piRecvData;
	protected int[] m_piRecvWinData;
	//
	protected double[] m_pdTemperatureMap;
	protected double[] m_TargetTemperatures;
	//
	protected double[] m_pdShutterTemp;
	protected double m_dShutterCenterTemp= 0.0d;
	// protected double m_ReferentCenterTemp= 0.0d;
	//
	///////////////////////////////////////////////////////////////
	//
	public CommonData() {
		setSensor();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getSensorSize() {
		return m_iSensorSize;
	}
	public int getActiveSize() {
		return m_iActiveSize;
	}
	public int getRecvByteSize() {
		return m_iRecvByteSize;
	}
	public int getRecvSize() {
		return m_iRecvSize;
	}
	public int getTopCDSSize() {
		return m_iTopCDSSize;
	}
	//
	public int getWindowingColStart() {
		return m_iWindowingColStart;
	}
	public int getWindowingColEnd() {
		return m_iWindowingColEnd;
	}
	public int getWindowingRowStart() {
		return m_iWindowingRowStart;
	}
	public int getWindowingRowEnd() {
		return m_iWindowingRowEnd;
	}
	public int getWindowingWidth() {
		return m_iWindowingColSize;
	}
	public int getWindowingHeight() {
		return m_iWindowingRowSize;
	}
	public int getWindowingSize() {
		return m_iWindowingSize;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setSensor() {
		m_iCameraMode= 0;
		m_iSensorWidth= 640;
		m_iSensorHeight= 482;
		m_iSensorSize= m_iSensorWidth * m_iSensorHeight;
		m_iActiveHeight= 480;
		m_iActiveSize= m_iSensorWidth * m_iActiveHeight;
		m_iTopCDSSize= m_iSensorWidth * 4;
		m_iBottomCDSSize= m_iSensorWidth * 4;
		m_bIsTopCDS= false;
		m_bIsBottomCDS= false;
		setRecv();
		setWindowing(0,m_iSensorWidth,0,m_iActiveHeight);
	}
	public void setRecv() {
		m_iRecvSize= m_iActiveSize;
		if (m_iCameraMode == 0) {
			m_iRecvSize+= m_iSensorWidth * 2;
		};
		m_iRecvByteSize= m_iRecvSize * 2;
		if (m_iRecvByteSize % i3EZUSBDriver.PACKET_TIMES != 0) {
			m_iRecvByteSize= ((m_iRecvByteSize / i3EZUSBDriver.PACKET_TIMES) + 1) * i3EZUSBDriver.PACKET_TIMES;
		};
		m_piRecvData= new int[(m_iRecvByteSize / 2)];
	}
	public void setWindowing(int _colStart, int _colEnd, int _rowStart, int _rowEnd) {
		m_iWindowingColStart= _colStart;
		m_iWindowingColEnd= _colEnd;
		m_iWindowingColSize= m_iWindowingColEnd - m_iWindowingColStart;
		m_iWindowingRowStart= _rowStart;
		m_iWindowingRowEnd= _rowEnd;
		m_iWindowingRowSize= m_iWindowingRowEnd - m_iWindowingRowStart;
		m_iWindowingSize= m_iWindowingColSize * m_iWindowingRowSize;
		m_ppfMultiOs_OsData= (float[][]) Array.newInstance(Float.TYPE,new int[]{4,m_iWindowingSize});
		m_pbyDeadData= new byte[m_iWindowingSize];
		m_AnomalousPixels= new boolean[m_iWindowingSize];
		m_piRecvWinData= new int[m_iWindowingSize];
		m_pdTemperatureMap= new double[m_iWindowingSize];
		m_TargetTemperatures= new double[m_iWindowingSize];
		m_pdShutterTemp= new double[m_iWindowingSize];
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setMultiOsData(float[][] data) {
		m_ppfMultiOs_OsData= data;
	}
	public void setMultiOsData(int multiNum, int pos, float data) {
		m_ppfMultiOs_OsData[multiNum][pos]= data;
	}
	//
	public float[][] getMultiOsData() {
		return m_ppfMultiOs_OsData;
	}
	public float getMultiOsData(int multiNum, int pos) {
		return m_ppfMultiOs_OsData[multiNum][pos];
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[] getTargetTemperatures() {
		return m_TargetTemperatures;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static double convCelsiusToFaherenheit(double _celsius) {
		return ((9.0d * _celsius) / 5.0d) + 32.0d;
	}
	public static double[] convCelsiusToFaherenheit(double[] array) {
		for (int k=0; k < array.length; k++) {
			array[k]= ((9.0d * array[k]) / 5.0d) + 32.0d;
		};
		return array;
	}
	public void convCelsiusToFaherenheit() {
		m_TargetTemperatures= convCelsiusToFaherenheit(m_TargetTemperatures);
	}
	//
	public static double convFaherenheitToCelsius(double _faherenheit) {
		return (5.0d * (_faherenheit - 32.0d)) / 9.0d;
	}
	public static double[] convFaherenheitToCelsius(double[] array) {
		for (int k=0; k < array.length; k++) {
			array[k]= (5.0d * (array[k] - 32.0d)) / 9.0d;
		};
		return array;
	}
	public void convFaherenheitToCelsius() {
		m_TargetTemperatures= convFaherenheitToCelsius(m_TargetTemperatures);
	}
}
