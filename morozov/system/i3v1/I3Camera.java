// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.i3v1;

import morozov.system.*;
import morozov.system.i3v1.frames.data.*;
import morozov.system.i3v1.frames.data.interfaces.*;
import morozov.system.i3v1.interfaces.*;

import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class I3Camera {
	//
	protected AtomicReference<I3DataConsumerInterface> dataConsumer= new AtomicReference<>();
	//
	protected boolean isFlashRead= true;
	protected AtomicBoolean eliminateAnomalousPixels= new AtomicBoolean(true);
	protected AtomicBoolean applyVoltageBasedAnomalousPixelDetector= new AtomicBoolean(true);
	protected AtomicReference<Double> voltageBasedAnomalousPixelDetectorThreshold= new AtomicReference<>(10.0d);
	protected AtomicBoolean applyTemperetureBasedAnomalousPixelDetector= new AtomicBoolean(true);
	protected AtomicReference<Double> temperatureBasedAnomalousPixelDetectorThreshold= new AtomicReference<>(0.10d);
	protected AtomicBoolean isCelsius= new AtomicBoolean(true);
	protected AtomicBoolean isShutterCalibration= new AtomicBoolean(false);
	protected boolean isReadUserDeadFile= false;
	//
	protected i3EZUSBDriver m_EZUSB= new i3EZUSBDriver();
	protected CommonData commData= new CommonData();
	protected ShutterLess shutterLess= new ShutterLess(commData);
	// protected TemperatureTable tempTable= new TemperatureTable();
	//
	protected float[] m_pdSL_FpaTempArray= new float[4];
	protected float m_dFpaTemp= 0.0f;
	// protected double[] ambientConstA= new double[2];
	// protected double[] ambientConstB= new double[2];
	// protected double[] sortFpaTemp= new double[3];
	protected short recognizedCode1= (short)0;
	protected short recognizedCode2= (short)0;
	protected short recognizedCode3= (short)0;
	//
	protected byte[] flashreadData= null;
	protected byte[] pastFlashReadData= null;
	protected boolean m_bIsMultiOs= true;
	protected boolean m_bIsSkimOs= false;
	protected boolean m_bIsAGCOff= false;
	//
	protected boolean m_bIsTempCalcfinished= false;
	protected boolean bIsCalcTemperatrueMapFinished= false;
	//
	protected int m_iOffsetCount= 0;
	// protected double m_dSkimOsAvg= 0.0d;
	protected double[] TempDataDouble= null;
	protected double[] AGCDataDouble= null;
	protected int multiOsNum= 0;
	protected boolean bIsCalcSlFinished= false;
	//
	protected AtomicReference<Double> lowerTemperatureBound= new AtomicReference<>(-10.0d);
	protected AtomicReference<Double> upperTemperatureBound= new AtomicReference<>(100.0d);
	//
	protected AtomicInteger numberOfDeadPixels= new AtomicInteger(0);
	protected AtomicInteger numberOfVoltageAnomalousPixels= new AtomicInteger(0);
	protected AtomicInteger numberOfTemperatureAnomalousPixels= new AtomicInteger(0);
	//
	// protected static double[] multiChamberTemp_m5_15_40= new double[]{-5.0d, 15.0d, 40.0d};
	//
	protected static int maximalNumberOfAttempts= 100;
	protected static int maximalDeadPixelEliminationRadius= 5;
	protected static int numberOfShutterCalibrationFrames= 7;
	protected static int numberOfTemperatureBasedAnomalousPixelDetector= 7;
	protected static int shutterCentralSpotRadius= 3;
	//
	protected static final double coefficient31= 256.0d;
	protected static final double coefficient32= 8192.0d;
	protected static final double coefficient33= 16384.0d;
	protected static final double coefficient34= 24576.0d;
	//
	protected static final double coefficient41= 2.0d;
	protected static final double coefficient42= 16383.0d;
	protected static final double coefficient43= 1.5d;
	protected static final double coefficient44= 1.0d;
	protected static final double coefficient45= 1000.0d;
	protected static final double coefficient46= 1870.0d;
	protected static final double coefficient47= 3.0d;
	//
	protected static String sensorFlashDataFile= "tev1sensor.bin";
	//
	///////////////////////////////////////////////////////////////
	//
	public I3Camera() {
		TempDataDouble= new double[commData.getWindowingSize()];
		AGCDataDouble= new double[commData.getWindowingSize()];
		//
		setFlashRead(true);
		// setFlashRead(false);
		//
		setIsCelsius(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDataConsumer(I3DataConsumerInterface consumer) {
		dataConsumer.set(consumer);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getWindowingWidth() {
		return commData.getWindowingWidth();
	}
	public int getWindowingHeight() {
		return commData.getWindowingHeight();
	}
	//
	public void setFlashRead(boolean flashRead) {
		isFlashRead= flashRead;
	}
	public boolean getIsFlashRead() {
		return isFlashRead;
	}
	//
	public void setEliminateAnomalousPixels(boolean value) {
		eliminateAnomalousPixels.set(value);
	}
	public boolean getEliminateAnomalousPixels() {
		return eliminateAnomalousPixels.get();
	}
	//
	public void setApplyVoltageBasedAnomalousPixelDetector(boolean value) {
		applyVoltageBasedAnomalousPixelDetector.set(value);
	}
	public boolean getApplyVoltageBasedAnomalousPixelDetector() {
		return applyVoltageBasedAnomalousPixelDetector.get();
	}
	//
	public void setVoltageBasedAnomalousPixelDetectorThreshold(double threshold) {
		voltageBasedAnomalousPixelDetectorThreshold.set(threshold);
	}
	public double getVoltageBasedAnomalousPixelDetectorThreshold() {
		return voltageBasedAnomalousPixelDetectorThreshold.get();
	}
	//
	public void setApplyTemperatureBasedAnomalousPixelDetector(boolean value) {
		applyTemperetureBasedAnomalousPixelDetector.set(value);
	}
	public boolean getApplyTemperatureBasedAnomalousPixelDetector() {
		return applyTemperetureBasedAnomalousPixelDetector.get();
	}
	//
	public void setTemperatureBasedAnomalousPixelDetectorThreshold(double threshold) {
		temperatureBasedAnomalousPixelDetectorThreshold.set(threshold);
	}
	public double getTemperatureBasedAnomalousPixelDetectorThreshold() {
		return temperatureBasedAnomalousPixelDetectorThreshold.get();
	}
	//
	public void setIsCelsius(boolean value) {
		boolean previousValue= isCelsius.get();
		isCelsius.set(value);
		if (previousValue != value) {
			if (value) {
				commData.convFaherenheitToCelsius();
			} else {
				commData.convCelsiusToFaherenheit();
			}
		}
	}
	public boolean isCelsius() {
		return isCelsius.get();
	}
	//
	public void setLowerTemperatureBound(double value) {
		lowerTemperatureBound.set(value);
	}
	public void setUpperTemperatureBound(double value) {
		upperTemperatureBound.set(value);
	}
	//
	public double getLowerTemperatureBound() {
		return lowerTemperatureBound.get();
	}
	public double getUpperTemperatureBound() {
		return upperTemperatureBound.get();
	}
	//
	public int getNumberOfDeadPixels() {
		return numberOfDeadPixels.get();
	}
	public int getNumberOfVoltageAnomalousPixels() {
		return numberOfVoltageAnomalousPixels.get();
	}
	public int getNumberOfTemperatureAnomalousPixels() {
		return numberOfTemperatureAnomalousPixels.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setReadTimeOut(int value) {
		m_EZUSB.setReadTimeOut(value);
	}
	public int getReadTimeOut() {
		return m_EZUSB.getReadTimeOut();
	}
	//
	public void setWriteTimeOut(int value) {
		m_EZUSB.setWriteTimeOut(value);
	}
	public int getWriteTimeOut() {
		return m_EZUSB.getWriteTimeOut();
	}
	//
	public void setOutputDebugInformation(int value) {
		m_EZUSB.setOutputDebugInformation(value);
	}
	public int getOutputDebugInformation() {
		return m_EZUSB.getOutputDebugInformation();
	}
	//
	public boolean reportCriticalErrors() {
		return m_EZUSB.reportCriticalErrors();
	}
	public boolean reportAdmissibleErrors() {
		return m_EZUSB.reportAdmissibleErrors();
	}
	public boolean reportWarnings() {
		return m_EZUSB.reportWarnings();
	}
	public boolean reportFlashAttributes() {
		return m_EZUSB.reportFlashAttributes();
	}
	public boolean reportUSBTransferDelays() {
		return m_EZUSB.reportUSBTransferDelays();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public synchronized String[] findDevices() {
		return m_EZUSB.findDevices();
	}
	public synchronized boolean findDevice() {
		return m_EZUSB.findDevice();
	}
	public synchronized boolean findDevice(DeviceIdentifier identifier) {
		return m_EZUSB.findDevice(identifier);
	}
	public synchronized boolean openDevice(DeviceIdentifier identifier) {
		return m_EZUSB.openDevice(identifier);
	}
	public synchronized boolean openDevice() {
		return m_EZUSB.openDevice();
	}
	public synchronized void closeDevice() {
		m_EZUSB.closeDevice();
	}
	public synchronized void closeUSB() {
		m_EZUSB.closeUSB();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public synchronized void initCamera() {
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		float[] m_pdSL_Gain= shutterLess.m_pdSL_Gain;
		readFlashData();
		if (m_EZUSB.reportFlashAttributes()) {
			reportSensorAttributes();
		};
		countDeadPixels();
		if (isFlashRead) {
			boolean recognizedCodesAreOK= false;
			if (recognizedCode1==1 && recognizedCode2 == 1 && recognizedCode3 == 1) {
				recognizedCodesAreOK= true;
			};
			int initialNumberOfDeadPixels= 0;
			for (int pos= 0; pos < m_pbyDeadData.length; pos++) {
				if (m_pbyDeadData[pos] != (byte)0) {
					initialNumberOfDeadPixels++;
				}
			};
			int numberOfAbnormalGains= 0;
			for (int pos= 0; pos < m_pdSL_Gain.length; pos++) {
				if (m_pdSL_Gain[pos] == 0) {
					numberOfAbnormalGains++;
				}
			};
			if (	!recognizedCodesAreOK ||
				initialNumberOfDeadPixels > m_pbyDeadData.length / 2 ||
				numberOfAbnormalGains > initialNumberOfDeadPixels) {
				if (m_EZUSB.reportWarnings()) {
					writeLater("No sensor attributes are received.\n");
				};
				loadSensorAttributes();
			} else {
				saveSensorAttributes();
			}
		} else {
			loadSensorAttributes();
		}
	}
	//
	protected void readFlashData() {
		if (m_EZUSB.reportWarnings()) {
			writeLater("Requesting sensor attributes...\n");
		};
		int recevieSize= commData.getRecvByteSize();
		flashreadData= new byte[recevieSize];
		pastFlashReadData= new byte[recevieSize];
		byte[] tempchar= new byte[4];
		int slGainPos= 0;
		int slOffsetPos= 0;
		int convertInt= 0;
		int mosPos= commData.getTopCDSSize();
		int flashFrameNum= 0;
		while (flashFrameNum < 26) {
			int recvNum= 0;
			int numberOfAttempts= 0;
			while (true) {
				if (numberOfAttempts > maximalNumberOfAttempts) {
					if (m_EZUSB.reportWarnings()) {
						writeLater("Cannot read flash data!\n");
					};
					return;
				} else {
					numberOfAttempts++;
				};
				if (recvNum != flashreadData.length) {
					recvNum= m_EZUSB.read(flashreadData,flashreadData.length);
					delay(50);
				} else {
					break;
				}
			};
			if (flashFrameNum > 1) {
				if (flashreadData[0]== pastFlashReadData[0] && flashreadData[1]== pastFlashReadData[1] && flashreadData[2]== pastFlashReadData[2] && flashreadData[3]== pastFlashReadData[3] && flashreadData[4]== pastFlashReadData[4]) {
					if (m_EZUSB.reportFlashAttributes()) {
						writeLater(String.format("Is the Same Flash Frame: %d\n",flashFrameNum));
					}
				}
			};
			int pos;
			if (flashFrameNum== 0) {
				for (pos= 0; pos < commData.getActiveSize(); pos++) {
					byte value= flashreadData[(pos * 2) + 1];
					commData.m_pbyDeadData[pos]= value;
				};
				shutterLess.setTemperatureOffset((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 2] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 3] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 0] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 1] << 0) & 255)));
				shutterLess.setTemperatureGain((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 6] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 7] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 4] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 5] << 0) & 255)));
				convertInt= ((flashreadData[(commData.getActiveSize() * 2) + 8] << 8) & 65280) | ((flashreadData[(commData.getActiveSize() * 2) + 9] << 0) & 255);
				recognizedCode1= (short)convertInt;
				convertInt= (convertInt | ((flashreadData[(commData.getActiveSize() * 2) + 10] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 11] << 0) & 255);
				recognizedCode2= (short)convertInt;
				recognizedCode3= (short)((convertInt | ((flashreadData[(commData.getActiveSize() * 2) + 12] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 13] << 0) & 255));
				//
				double sortFpaTemp1= (double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 16] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 17] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 14] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 15] << 0) & 255));
				double sortFpaTemp2= (double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 20] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 21] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 18] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 19] << 0) & 255));
				double sortFpaTemp3= (double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 24] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 25] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 22] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 23] << 0) & 255));
				// sortFpaTemp[0]= sortFpaTemp1;
				// sortFpaTemp[1]= sortFpaTemp2;
				// sortFpaTemp[2]= sortFpaTemp3;
				//
				shutterLess.setFpaTemp1(sortFpaTemp1);
				shutterLess.setFpaTemp2(sortFpaTemp2);
				shutterLess.setFpaTemp3(sortFpaTemp3);
				//
				// Arrays.sort(sortFpaTemp);
				shutterLess.setDriftInitFactor((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 28] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 29] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 26] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 27] << 0) & 255)));
				shutterLess.setDriftTempCoeffi1((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 32] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 33] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 30] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 31] << 0) & 255)));
				shutterLess.setDriftTempCoeffi2((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 36] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 37] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 34] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 35] << 0) & 255)));
				shutterLess.setDriftTempCoeffi3((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 40] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 41] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 38] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 39] << 0) & 255)));
				shutterLess.setDriftTempCoeffi4((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 44] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 45] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 42] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 43] << 0) & 255)));
				shutterLess.setAmbientTempCoeffi1((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 48] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 49] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 46] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 47] << 0) & 255)));
				shutterLess.setAmbientTempCoeffi2((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 52] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 53] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 50] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 51] << 0) & 255)));
				shutterLess.setAmbientTempCoeffi3((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 56] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 57] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 54] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 55] << 0) & 255)));
				shutterLess.setAmbientTempCoeffi4((double)Float.intBitsToFloat(((((flashreadData[(commData.getActiveSize() * 2) + 60] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 61] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 58] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 59] << 0) & 255)));
				convertInt= ((((flashreadData[(commData.getActiveSize() * 2) + 64] << 24) & -16777216) | ((flashreadData[(commData.getActiveSize() * 2) + 65] << 16) & 16711680)) | ((flashreadData[(commData.getActiveSize() * 2) + 62] << 8) & 65280)) | ((flashreadData[(commData.getActiveSize() * 2) + 63] << 0) & 255);
				shutterLess.setAmbientTempCoeffi5((double)Float.intBitsToFloat(convertInt));
				// for (pos= 0; pos < 2; pos++) {
				//	ambientConstA[pos]= (multiChamberTemp_m5_15_40[pos + 1] - multiChamberTemp_m5_15_40[pos]) / (sortFpaTemp[pos + 1] - sortFpaTemp[pos]);
				//	ambientConstB[pos]= multiChamberTemp_m5_15_40[pos] - (ambientConstA[pos] * sortFpaTemp[pos]);
				// };
				shutterLess.calcAmbientCoeffi();
				shutterLess.calcAmbientTempOffset();
			} else if (flashFrameNum >= 2 && flashFrameNum < 10) {
				int tempCharPos= 0;
				for (pos= 0; pos < recevieSize / 2; pos++) {
					if (pos < commData.getActiveSize()) {
						int r21= tempCharPos + 1;
						tempchar[tempCharPos]= flashreadData[(pos * 2) + 1];
						tempCharPos= r21 + 1;
						tempchar[r21]= flashreadData[pos * 2];
						if (tempCharPos == 4) {
							tempCharPos= 0;
							convertInt= ((((tempchar[3] << 24) & -16777216) | ((tempchar[2] << 16) & 16711680)) | ((tempchar[1] << 8) & 65280)) | ((tempchar[0] << 0) & 255);
							int slGainPos2= slGainPos + 1;
							shutterLess.m_pdSL_Gain[slGainPos]= Float.intBitsToFloat(convertInt);
							slGainPos= slGainPos2;
						}
					}
				}
			} else if (flashFrameNum >= 10 && flashFrameNum < 18) {
				int tempCharPos= 0;
				for (pos= 0; pos < recevieSize / 2; pos++) {
					if (pos < commData.getActiveSize()) {
						int r21= tempCharPos + 1;
						tempchar[tempCharPos]= flashreadData[(pos * 2) + 1];
						tempCharPos= r21 + 1;
						tempchar[r21]= flashreadData[pos * 2];
						if (tempCharPos == 4) {
							tempCharPos= 0;
							convertInt= ((((tempchar[3] << 24) & -16777216) | ((tempchar[2] << 16) & 16711680)) | ((tempchar[1] << 8) & 65280)) | ((tempchar[0] << 0) & 255);
							int slOffsetPos2= slOffsetPos + 1;
							shutterLess.m_pdSL_Offset[slOffsetPos]= Float.intBitsToFloat(convertInt);
							slOffsetPos= slOffsetPos2;
						}
					}
				};
				shutterLess.m_bIsSLLoad= true;
			} else if (flashFrameNum >= 18 && flashFrameNum < 26) {
				int frameNum= flashFrameNum - 18;
				int tempCharPos= 0;
				if (frameNum % 2== 0) {
					mosPos= 0;
				};
				pos= 0;
				while (pos < recevieSize / 2) {
					if (pos < commData.getActiveSize()) {
						int r21= tempCharPos + 1;
						tempchar[tempCharPos]= flashreadData[(pos * 2) + 1];
						tempCharPos= r21 + 1;
						tempchar[r21]= flashreadData[pos * 2];
						if (tempCharPos == 4) {
							tempCharPos= 0;
							convertInt= ((((tempchar[3] << 24) & -16777216) | ((tempchar[2] << 16) & 16711680)) | ((tempchar[1] << 8) & 65280)) | ((tempchar[0] << 0) & 255);
							int mosPos2= mosPos + 1;
							commData.setMultiOsData(frameNum / 2, mosPos, Float.intBitsToFloat(convertInt));
							mosPos= mosPos2;
						}
					} else {
						int r21= tempCharPos + 1;
						tempchar[tempCharPos]= flashreadData[(pos * 2) + 1];
						tempCharPos= r21 + 1;
						tempchar[r21]= flashreadData[pos * 2];
						if (tempCharPos == 4) {
							tempCharPos= 0;
							convertInt= ((((tempchar[3] << 24) & -16777216) | ((tempchar[2] << 16) & 16711680)) | ((tempchar[1] << 8) & 65280)) | ((tempchar[0] << 0) & 255);
						};
						if (pos == commData.getActiveSize() + 2) {
							double inputValue= (double)Float.intBitsToFloat(convertInt);
							// if (!Double.isNaN(inputValue)) {
							shutterLess.m_dInitOnTimeCorrFactor= inputValue;
							// }
						} else if (pos == commData.getActiveSize() + 4) {
							double inputValue= (double)Float.intBitsToFloat(convertInt);
							// if (!Double.isNaN(inputValue)) {
							shutterLess.m_pdInitOnTimeCorr[0]= inputValue;
							// }
						} else if (pos == commData.getActiveSize() + 6) {
							double inputValue= (double)Float.intBitsToFloat(convertInt);
							// if (!Double.isNaN(inputValue)) {
							shutterLess.m_pdInitOnTimeCorr[1]= inputValue;
							// }
						} else if (pos == commData.getActiveSize() + 8) {
							double inputValue= (double)Float.intBitsToFloat(convertInt);
							// if (!Double.isNaN(inputValue)) {
							shutterLess.m_pdInitOnTimeCorr[2]= inputValue;
							// }
						} else if (pos == commData.getActiveSize() + 10) {
							double inputValue= (double)Float.intBitsToFloat(convertInt);
							// if (!Double.isNaN(inputValue)) {
							shutterLess.m_pdInitOnTimeCorr[3]= inputValue;
							// }
						} else if (pos == commData.getActiveSize() + 12) {
							double inputValue= (double)Float.intBitsToFloat(convertInt);
							// if (!Double.isNaN(inputValue)) {
							shutterLess.m_pdInitOnTimeCorr[4]= inputValue;
							// }
						} else if (pos == commData.getActiveSize() + 16) {
							commData.m_pdMultiOs_FpaTemp[frameNum / 2]= (double) Float.intBitsToFloat(convertInt);
						} else if (!(pos == commData.getActiveSize() + 18 || pos == commData.getActiveSize() + 20 || pos != commData.getActiveSize() + 22)) {
							commData.m_pdMultiOs_FpaTemp[frameNum / 2]= (double) Float.intBitsToFloat(convertInt);
						}
					};
					pos++;
				};
				m_bIsMultiOs= false;
				m_bIsSkimOs= true;
			};
			pastFlashReadData[0]= flashreadData[0];
			pastFlashReadData[1]= flashreadData[1];
			pastFlashReadData[2]= flashreadData[2];
			pastFlashReadData[3]= flashreadData[3];
			pastFlashReadData[4]= flashreadData[4];
			flashFrameNum++;
		}
	}
	//
	public void saveSensorAttributes() {
		try {
			DataOutputStream writer= new DataOutputStream(new BufferedOutputStream(new FileOutputStream(sensorFlashDataFile)));
			try {
				saveSensorAttributes(writer);
			} finally {
				writer.close();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}
	//
	public synchronized void saveSensorAttributes(DataOutputStream writer) throws IOException {
		if (m_EZUSB.reportWarnings()) {
			writeLater("Storing sensor attributes...\n");
		};
		writer.writeInt(commData.m_pbyDeadData.length);
		for (int n=0; n < commData.m_pbyDeadData.length; n++) {
			writer.writeByte(commData.m_pbyDeadData[n]);
		};
		writer.writeDouble(shutterLess.m_dTemperatureGain);
		writer.writeDouble(shutterLess.m_dTemperatureOffset);
		writer.writeShort(recognizedCode1);
		writer.writeShort(recognizedCode2);
		writer.writeShort(recognizedCode3);
		writer.writeInt(shutterLess.sortFpaTemp.length);
		for (int n=0; n < shutterLess.sortFpaTemp.length; n++) {
			writer.writeDouble(shutterLess.sortFpaTemp[n]);
		};
		writer.writeDouble(shutterLess.m_dDriftInitFactor);
		writer.writeDouble(shutterLess.m_dDriftTempCoeffi1);
		writer.writeDouble(shutterLess.m_dDriftTempCoeffi2);
		writer.writeDouble(shutterLess.m_dDriftTempCoeffi3);
		writer.writeDouble(shutterLess.m_dDriftTempCoeffi4);
		writer.writeInt(shutterLess.m_dAmbientTempCoeffi.length);
		for (int n=0; n < shutterLess.m_dAmbientTempCoeffi.length; n++) {
			writer.writeDouble(shutterLess.m_dAmbientTempCoeffi[n]);
		};
		// writer.writeInt(ambientConstA.length);
		// for (int n=0; n < ambientConstA.length; n++) {
		//	writer.writeDouble(ambientConstA[n]);
		// };
		// writer.writeInt(ambientConstB.length);
		// for (int n=0; n < ambientConstB.length; n++) {
		//	writer.writeDouble(ambientConstB[n]);
		// };
		writer.writeInt(shutterLess.m_pdSL_Gain.length);
		for (int n=0; n < shutterLess.m_pdSL_Gain.length; n++) {
			writer.writeFloat(shutterLess.m_pdSL_Gain[n]);
		};
		writer.writeInt(shutterLess.m_pdSL_Offset.length);
		for (int n=0; n < shutterLess.m_pdSL_Offset.length; n++) {
			writer.writeFloat(shutterLess.m_pdSL_Offset[n]);
		};
		writer.writeInt(commData.m_ppfMultiOs_OsData.length);
		writer.writeInt(commData.m_ppfMultiOs_OsData[0].length);
		for (int m=0; m < commData.m_ppfMultiOs_OsData.length; m++) {
			for (int n=0; n < commData.m_ppfMultiOs_OsData[0].length; n++) {
				writer.writeFloat(commData.m_ppfMultiOs_OsData[m][n]);
			}
		};
		writer.writeDouble(shutterLess.m_dInitOnTimeCorrFactor);
		writer.writeInt(shutterLess.m_pdInitOnTimeCorr.length);
		for (int n=0; n < shutterLess.m_pdInitOnTimeCorr.length; n++) {
			writer.writeDouble(shutterLess.m_pdInitOnTimeCorr[n]);
		};
		writer.writeInt(commData.m_pdMultiOs_FpaTemp.length);
		for (int n=0; n < commData.m_pdMultiOs_FpaTemp.length; n++) {
			writer.writeDouble(commData.m_pdMultiOs_FpaTemp[n]);
		}
	}
	//
	public void loadSensorAttributes() {
		try {
			DataInputStream reader= new DataInputStream(new BufferedInputStream(new FileInputStream(sensorFlashDataFile)));
			try {
				loadSensorAttributes(reader);
			} finally {
				reader.close();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}
	public synchronized void loadSensorAttributes(DataInputStream reader) throws IOException {
		if (m_EZUSB.reportWarnings()) {
			writeLater("Restoring sensor attributes...\n");
		};
		int m_pbyDeadData_length= reader.readInt();
		for (int n=0; n < m_pbyDeadData_length; n++) {
			byte value= reader.readByte();
			commData.m_pbyDeadData[n]= value;
		};
		shutterLess.m_dTemperatureGain= reader.readDouble();
		shutterLess.m_dTemperatureOffset= reader.readDouble();
		recognizedCode1= reader.readShort();
		recognizedCode2= reader.readShort();
		recognizedCode3= reader.readShort();
		int sortFpaTemp_length= reader.readInt();
		for (int n=0; n < sortFpaTemp_length; n++) {
			shutterLess.sortFpaTemp[n]= reader.readDouble();
		};
		shutterLess.m_dDriftInitFactor= reader.readDouble();
		shutterLess.m_dDriftTempCoeffi1= reader.readDouble();
		shutterLess.m_dDriftTempCoeffi2= reader.readDouble();
		shutterLess.m_dDriftTempCoeffi3= reader.readDouble();
		shutterLess.m_dDriftTempCoeffi4= reader.readDouble();
		int m_dAmbientTempCoeffi_length= reader.readInt();
		for (int n=0; n < m_dAmbientTempCoeffi_length; n++) {
			shutterLess.m_dAmbientTempCoeffi[n]= reader.readDouble();
		};
		// int ambientConstA_length= reader.readInt();
		// for (int n=0; n < ambientConstA_length; n++) {
		//	ambientConstA[n]= reader.readDouble();
		// };
		// int ambientConstB_length= reader.readInt();
		// for (int n=0; n < ambientConstB_length; n++) {
		//	ambientConstB[n]= reader.readDouble();
		// };
		shutterLess.calcAmbientCoeffi();
		shutterLess.calcAmbientTempOffset();
		int m_pdSL_Gain_length= reader.readInt();
		for (int n=0; n < m_pdSL_Gain_length; n++) {
			shutterLess.m_pdSL_Gain[n]= reader.readFloat();
		};
		int m_pdSL_Offset_length= reader.readInt();
		for (int n=0; n < m_pdSL_Offset_length; n++) {
			shutterLess.m_pdSL_Offset[n]= reader.readFloat();
		};
		shutterLess.m_bIsSLLoad= true;
		int m_ppfMultiOs_OsData_length_1= reader.readInt();
		int m_ppfMultiOs_OsData_length_2= reader.readInt();
		for (int m=0; m < m_ppfMultiOs_OsData_length_1; m++) {
			for (int n=0; n < m_ppfMultiOs_OsData_length_2; n++) {
				commData.m_ppfMultiOs_OsData[m][n]= reader.readFloat();
			}
		};
		double inputValue1= reader.readDouble();
		// if (!Double.isNaN(inputValue1)) {
		shutterLess.m_dInitOnTimeCorrFactor= inputValue1;
		// };
		int m_pdInitOnTimeCorr_length= reader.readInt();
		for (int n=0; n < m_pdInitOnTimeCorr_length; n++) {
			double inputValue2= reader.readDouble();
			// if (!Double.isNaN(inputValue2)) {
			shutterLess.m_pdInitOnTimeCorr[n]= inputValue2;
			// }
		};
		int m_pdMultiOs_FpaTemp_length= reader.readInt();
		for (int n=0; n < m_pdMultiOs_FpaTemp_length; n++) {
			commData.m_pdMultiOs_FpaTemp[n]= reader.readDouble();
		};
		m_bIsMultiOs= false;
		m_bIsSkimOs= true;
		countDeadPixels();
		if (m_EZUSB.reportFlashAttributes()) {
			reportSensorAttributes();
		}

	}
	//
	protected void reportSensorAttributes() {
		for (int n=0; n < 30; n++) {
			writeLater(String.format("commData.m_pbyDeadData[%s]: %s\n",n,commData.m_pbyDeadData[n]));
		};
		writeLater(String.format("shutterLess.m_dTemperatureOffset: %s\n",shutterLess.m_dTemperatureOffset));
		writeLater(String.format("shutterLess.m_dTemperatureGain: %s\n",shutterLess.m_dTemperatureGain));
		writeLater(String.format("recognizedCode1: %s\n",recognizedCode1));
		writeLater(String.format("recognizedCode2: %s\n",recognizedCode2));
		writeLater(String.format("recognizedCode3: %s\n",recognizedCode3));
		for (int n=0; n < shutterLess.sortFpaTemp.length; n++) {
			writeLater(String.format("shutterLess.sortFpaTemp[%s]: %s\n",n,shutterLess.sortFpaTemp[n]));
		};
		writeLater(String.format("shutterLess.m_dDriftInitFactor: %s\n",shutterLess.m_dDriftInitFactor));
		writeLater(String.format("shutterLess.m_dDriftTempCoeffi1: %s\n",shutterLess.m_dDriftTempCoeffi1));
		writeLater(String.format("shutterLess.m_dDriftTempCoeffi2: %s\n",shutterLess.m_dDriftTempCoeffi2));
		writeLater(String.format("shutterLess.m_dDriftTempCoeffi3: %s\n",shutterLess.m_dDriftTempCoeffi3));
		writeLater(String.format("shutterLess.m_dDriftTempCoeffi4: %s\n",shutterLess.m_dDriftTempCoeffi4));
		for (int n=0; n < shutterLess.m_dAmbientTempCoeffi.length; n++) {
			writeLater(String.format("shutterLess.m_dAmbientTempCoeffi[%s]: %s\n",n,shutterLess.m_dAmbientTempCoeffi[n]));
		};
		// for (int n=0; n < ambientConstA.length; n++) {
		//	writeLater(String.format("ambientConstA[%s]: %s\n",n,ambientConstA[n]));
		// };
		// for (int n=0; n < ambientConstB.length; n++) {
		//	writeLater(String.format("ambientConstB[%s]: %s\n",n,ambientConstB[n]));
		// };
		for (int n=0; n < shutterLess.ambientConstA.length; n++) {
			writeLater(String.format("shutterLess.ambientConstA[%s]: %s\n",n,shutterLess.ambientConstA[n]));
		};
		for (int n=0; n < shutterLess.ambientConstB.length; n++) {
			writeLater(String.format("shutterLess.ambientConstB[%s]: %s\n",n,shutterLess.ambientConstB[n]));
		};
		for (int n=0; n < shutterLess.ambientOffsetConstA.length; n++) {
			writeLater(String.format("shutterLess.ambientOffsetConstA[%s]: %s\n",n,shutterLess.ambientOffsetConstA[n]));
		};
		for (int n=0; n < shutterLess.ambientOffsetConstB.length; n++) {
			writeLater(String.format("shutterLess.ambientOffsetConstB[%s]: %s\n",n,shutterLess.ambientOffsetConstB[n]));
		};
		//
		writeLater(String.format("shutterLess.m_pdSL_Gain.length: %s\n",shutterLess.m_pdSL_Gain.length));
		for (int n=0; n < 30; n++) {
			writeLater(String.format("shutterLess.m_pdSL_Gain[%s]: %s\n",n,shutterLess.m_pdSL_Gain[n]));
		};
		for (int n=0; n < 30; n++) {
			writeLater(String.format("shutterLess.m_pdSL_Offset[%s]: %s\n",n,shutterLess.m_pdSL_Offset[n]));
		};
		writeLater(String.format("shutterLess.m_bIsSLLoad: %s\n",shutterLess.m_bIsSLLoad));
		for (int n=0; n < 3; n++) {
			for (int m=0; m < 3; m++) {
				writeLater(String.format("commData.m_ppfMultiOs_OsData[%s][%s]: %s\n",n,m,commData.m_ppfMultiOs_OsData[n][m]));
			}
		};
		writeLater(String.format("shutterLess.m_dInitOnTimeCorrFactor: %s\n",shutterLess.m_dInitOnTimeCorrFactor));
		for (int n=0; n < shutterLess.m_pdInitOnTimeCorr.length; n++) {
			writeLater(String.format("shutterLess.m_pdInitOnTimeCorr[%s]: %s\n",n,shutterLess.m_pdInitOnTimeCorr[n]));
		};
		for (int n=0; n < 3; n++) {
			writeLater(String.format("commData.m_pdMultiOs_FpaTemp[%s]: %s\n",n,commData.m_pdMultiOs_FpaTemp[n]));
		}
	}
	//
	protected void countDeadPixels() {
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		int counter= 0;
		for (int pos=0; pos < m_pbyDeadData.length; pos++) {
			if (m_pbyDeadData[pos] != (byte)0) {
				counter++;
			}
		};
		numberOfDeadPixels.set(counter);
	}
	//
	public synchronized I3CameraFlashAttributes collectFlashData() {
		I3CameraFlashAttributes attributes= new I3CameraFlashAttributes(
			commData.m_pbyDeadData,
			shutterLess.getTemperatureOffset(),
			shutterLess.getTemperatureGain(),
			recognizedCode1,
			recognizedCode2,
			recognizedCode3,
			shutterLess.getSortFpaTemp(),
			shutterLess.getDriftInitFactor(),
			shutterLess.getDriftTempCoeffi1(),
			shutterLess.getDriftTempCoeffi2(),
			shutterLess.getDriftTempCoeffi3(),
			shutterLess.getDriftTempCoeffi4(),
			shutterLess.getAmbientTempCoeffi(),
			// shutterLess.calcAmbientCoeffi();
			shutterLess.getAmbientConstA(),
			shutterLess.getAmbientConstB(),
			shutterLess.getCheckcamberTemp(),
			// shutterLess.calcAmbientTempOffset();
			shutterLess.getAmbientOffsetConstA(),
			shutterLess.getAmbientOffsetConstB(),
			shutterLess.m_pdSL_Gain,
			shutterLess.m_pdSL_Offset,
			shutterLess.m_bIsSLLoad,
			commData.getMultiOsData(),
			shutterLess.m_dInitOnTimeCorrFactor,
			shutterLess.m_pdInitOnTimeCorr,
			commData.m_pdMultiOs_FpaTemp,
			m_bIsMultiOs,
			m_bIsSkimOs,
			shutterLess.m_pdSL_SkimOs,
			commData.m_pdShutterTemp,
			commData.m_AnomalousPixels,
			commData.m_dShutterCenterTemp,
			numberOfVoltageAnomalousPixels.get(),
			numberOfTemperatureAnomalousPixels.get()
			);
		return attributes;
	}
	//
	public synchronized void modifyFlashData(I3CameraFlashAttributesInterface attributes) {
		commData.m_pbyDeadData= attributes.getDeadData();
		shutterLess.setTemperatureOffset(attributes.getTemperatureOffset());
		shutterLess.setTemperatureGain(attributes.getTemperatureGain());
		recognizedCode1= attributes.getRecognizedCode1();
		recognizedCode2= attributes.getRecognizedCode2();
		recognizedCode3= attributes.getRecognizedCode3();
		shutterLess.setSortFpaTemp(attributes.getSortFpaTemp());
		shutterLess.setDriftInitFactor(attributes.getDriftInitFactor());
		shutterLess.setDriftTempCoeffi1(attributes.getDriftTempCoeffi1());
		shutterLess.setDriftTempCoeffi2(attributes.getDriftTempCoeffi2());
		shutterLess.setDriftTempCoeffi3(attributes.getDriftTempCoeffi3());
		shutterLess.setDriftTempCoeffi4(attributes.getDriftTempCoeffi4());
		shutterLess.setAmbientTempCoeffi(attributes.getAmbientTempCoeffi());
		shutterLess.setAmbientConstA(attributes.getAmbientConstA());
		shutterLess.setAmbientConstB(attributes.getAmbientConstB());
		shutterLess.setCheckcamberTemp(attributes.getCheckcamberTemp());
		shutterLess.setAmbientOffsetConstA(attributes.getAmbientOffsetConstA());
		shutterLess.setAmbientOffsetConstB(attributes.getAmbientOffsetConstB());
		shutterLess.m_pdSL_Gain= attributes.getSLGain();
		shutterLess.m_pdSL_Offset= attributes.getSLOffset();
		shutterLess.m_bIsSLLoad= attributes.isSLLoad();
		commData.setMultiOsData(attributes.getMultiOsData());
		shutterLess.m_dInitOnTimeCorrFactor= attributes.getInitOnTimeCorrFactor();
		shutterLess.m_pdInitOnTimeCorr= attributes.getInitOnTimeCorr();
		commData.m_pdMultiOs_FpaTemp= attributes.getMultiOs_FpaTemp();
		m_bIsMultiOs= attributes.isMultiOs();
		m_bIsSkimOs= attributes.isSkimOs();
		shutterLess.m_pdSL_SkimOs= attributes.getSLSkimOs();
		commData.m_pdShutterTemp= attributes.getShutterTemp();
		commData.m_AnomalousPixels= attributes.getAnomalousPixels();
		commData.m_dShutterCenterTemp= attributes.getShutterCenterTemp();
		numberOfVoltageAnomalousPixels.set(attributes.getNumberOfVoltageAnomalousPixels());
		numberOfTemperatureAnomalousPixels.set(attributes.getNumberOfTemperatureAnomalousPixels());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void calibrateDevice() {
		if (m_EZUSB.isOpen()) {
			setShutterCalibration(true);
		}
	}
	//
	public synchronized void annulDeviceCalibration() {
		double[] m_pdSL_SkimOs= shutterLess.m_pdSL_SkimOs;
		double[] m_pdShutterTemp= commData.m_pdShutterTemp;
		boolean[] m_AnomalousPixels= commData.m_AnomalousPixels;
		m_iOffsetCount= 0;
		for (int pos=0; pos < commData.getWindowingSize(); pos++) {
			shutterLess.m_pdSL_SkimOs[pos]= 0.0d;
			m_AnomalousPixels[pos]= false;
			m_pdShutterTemp[pos]= 0.0d;
		};
		commData.m_dShutterCenterTemp= 0.0d;
		numberOfVoltageAnomalousPixels.set(0);
		numberOfTemperatureAnomalousPixels.set(0);
	}
	//
	public void setShutterCalibration(boolean mode) {
		isShutterCalibration.set(mode);
		if (mode) {
			annulDeviceCalibration();
		}
	}
	//
	public void stopShutterCalibration() {
		isShutterCalibration.set(false);
	}
	//
	public void shutterCalibration() {
		if(!isShutterCalibration.get()) {
			return;
		};
		doShutterCalibration();
	}
	//
	protected synchronized void doShutterCalibration() {
		// m_dSkimOsAvg= 0.0d;
		int windowingWidth= commData.getWindowingWidth();
		int windowingHeight= commData.getWindowingHeight();
		int windowingSize= commData.getWindowingSize();
		int[] m_piRecvData= commData.m_piRecvData;
		double[] m_pdSL_SkimOs= shutterLess.m_pdSL_SkimOs;
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		float[] m_pdSL_Gain= shutterLess.m_pdSL_Gain;
		float[] m_pdSL_Offset= shutterLess.m_pdSL_Offset;
		for (int pos= 0; pos < windowingSize; pos++) {
			double data= (double)m_piRecvData[pos];
			m_pdSL_SkimOs[pos]+= data;
		};
		// int pixelCounter= 0;
		m_iOffsetCount++;
		if (m_iOffsetCount >= numberOfShutterCalibrationFrames) {
			int pos1= 0;
			for (int h=0; h < windowingHeight; h++) {
				for (int w=0; w < windowingWidth; w++) {
					m_pdSL_SkimOs[pos1]/= (double)numberOfShutterCalibrationFrames;
					double interGain= 0.0d;
					double interOs= 0.0d;
					for (int order= 0; order < 4; order++) {
						interGain+= (double) (m_pdSL_FpaTempArray[order] * m_pdSL_Gain[(4 * pos1) + order]);
						interOs+= (double) (m_pdSL_FpaTempArray[order] * m_pdSL_Offset[(4 * pos1) + order]);
					};
					m_pdSL_SkimOs[pos1]= (m_pdSL_SkimOs[pos1] - interOs) * interGain;
					// if (m_pbyDeadData[pos1] == (byte)0) {
					//	m_dSkimOsAvg+= m_pdSL_SkimOs[pos1];
					//	pixelCounter++;
					// };
					// m_pdShutterTemp[pos1]= calcTemp_Table(w,h);
					pos1++;
				}
			};
			// m_dSkimOsAvg/= (double)pixelCounter;
			//
			computeShutterTemperature();
			//
			if (applyVoltageBasedAnomalousPixelDetector.get()) {
				applyVoltageBasedAnomalousPixelDetector();
			};
			if (applyTemperetureBasedAnomalousPixelDetector.get()) {
				computeTemperatureMap();
				applyTemperatureBasedAnomalousPixelDetector();
			};
			//
			computeShutterTemperature();
			//
			isShutterCalibration.set(false);
			m_bIsSkimOs= true;
			m_bIsMultiOs= false;
			I3DataConsumerInterface consumer= dataConsumer.get();
			if (consumer != null) {
				consumer.completeCalibration();
			}
		}
	}
	//
	protected void computeShutterTemperature() {
		int windowingWidth= commData.getWindowingWidth();
		int windowingHeight= commData.getWindowingHeight();
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		boolean[] m_AnomalousPixels= commData.m_AnomalousPixels;
		//
		double[] m_pdShutterTemp= commData.m_pdShutterTemp;
		shutterLess.computeTargetTemperatures(m_pdShutterTemp,isCelsius.get());
		double sum= 0.0d;
		int count= 0;
		for (int h= -shutterCentralSpotRadius; h <= shutterCentralSpotRadius; h++) {
			for (int w= -shutterCentralSpotRadius; w <= shutterCentralSpotRadius; w++) {
				int pos= (windowingWidth * (((windowingHeight / 2) + h) - 1)) + ((windowingWidth / 2) + w);
				if (m_pbyDeadData[pos] == (byte)0 && !m_AnomalousPixels[pos]) {
					sum+= m_pdShutterTemp[pos];
					count++;
				}
			}
		};
		commData.m_dShutterCenterTemp= sum / (double)count;
	}
	//
	protected void applyVoltageBasedAnomalousPixelDetector() {
		double[] m_pdSL_SkimOs= shutterLess.m_pdSL_SkimOs;
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		boolean[] m_AnomalousPixels= commData.m_AnomalousPixels;
		int length0= m_pdSL_SkimOs.length;
		if (length0 <= 0) {
			return;
		};
		double[] firstArray= new double[length0];
		int pos1= -1;
		for (int pos0=0; pos0 < length0; pos0++) {
			// if (m_pbyDeadData[pos0] == (byte)0 && !m_AnomalousPixels[pos0]) {
			if (m_pbyDeadData[pos0] == (byte)0) {
				pos1++;
				firstArray[pos1]= m_pdSL_SkimOs[pos0];
			}
		};
		int length1= pos1 + 1;
		Arrays.sort(firstArray,0,length1);
		double median;
		if (length1 % 2 == 1) {
			median= firstArray[length1/2];
		} else {
			median= (firstArray[length1/2-1]+firstArray[length1/2])/2;
		};
		double[] secondArray= new double[length1];
		pos1= 0;
		for (int pos0=0; pos0 < length0; pos0++) {
			// if (m_pbyDeadData[pos0] == (byte)0 && !m_AnomalousPixels[pos0]) {
			if (m_pbyDeadData[pos0] == (byte)0) {
				double value= m_pdSL_SkimOs[pos0] - median;
				if (value < 0) {
					value= -value;
				};
				firstArray[pos0]= value;
				secondArray[pos1]= value;
				pos1++;
			}
		};
		Arrays.sort(secondArray);
		double mad;
		if (length1 % 2 == 1) {
			mad= secondArray[length1/2];
		} else {
			mad= (secondArray[length1/2-1]+secondArray[length1/2])/2;
		};
		int counterOfVoltageBasedAnomalousPixels= 0;
		double threshold= voltageBasedAnomalousPixelDetectorThreshold.get();
		for (int pos0=0; pos0 < length0; pos0++) {
			// if (m_pbyDeadData[pos0] == (byte)0 && !m_AnomalousPixels[pos0]) {
			if (m_pbyDeadData[pos0] == (byte)0) {
				double value= firstArray[pos0];
				double relativeDeviation= value / mad;
				if (relativeDeviation > threshold) {
					m_AnomalousPixels[pos0]= true;
					counterOfVoltageBasedAnomalousPixels++;
				}
			}
		};
		numberOfVoltageAnomalousPixels.set(counterOfVoltageBasedAnomalousPixels);
		if (m_EZUSB.reportWarnings()) {
			writeLater(String.format("counterOfVoltageBasedAnomalousPixels: %s\n",counterOfVoltageBasedAnomalousPixels));
		}
	}
	//
	protected void applyTemperatureBasedAnomalousPixelDetector() {
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		boolean[] m_AnomalousPixels= commData.m_AnomalousPixels;
		// double[] m_TargetTemperatures= commData.m_TargetTemperatures;
		double[] m_TargetTemperatures= shutterLess.computeTargetTemperatures(isCelsius.get());
		int length0= m_TargetTemperatures.length;
		double[] firstArray= new double[length0];
		int pos1= -1;
		for (int pos0=0; pos0 < length0; pos0++) {
			// if (m_pbyDeadData[pos0] == (byte)0 && !m_AnomalousPixels[pos0]) {
			if (m_pbyDeadData[pos0] == (byte)0) {
				pos1++;
				firstArray[pos1]= m_TargetTemperatures[pos0];
			}
		};
		int length1= pos1 + 1;
		Arrays.sort(firstArray,0,length1);
		double median;
		if (length1 % 2 == 1) {
			median= firstArray[length1/2];
		} else {
			median= (firstArray[length1/2-1]+firstArray[length1/2])/2;
		};
		double terminationValue= median - 10 * Double.MIN_VALUE;
		double value1= firstArray[0];
		double leftBound= value1;
		double threshold= temperatureBasedAnomalousPixelDetectorThreshold.get();
		for (int k=1; k < length1; k++) {
			double value2= firstArray[k];
			if (value2 >= terminationValue) {
				break;
			};
			double delta= value2 - value1;
			if (delta > threshold) {
				leftBound= value2;
			};
			value1= value2;
		};
		terminationValue= median + 10 * Double.MIN_VALUE;
		value1= firstArray[length1-1];
		double rightBound= value1;
		for (int k=length1-2; k >= 0; k--) {
			double value2= firstArray[k];
			if (value2 <= terminationValue) {
				break;
			};
			double delta= value1 - value2;
			if (delta > threshold) {
				rightBound= value2;
			};
			value1= value2;
		};
		int counterOfTemperatureBasedAnomalousPixels= 0;
		for (int pos=0; pos < length0; pos++) {
			if (m_pbyDeadData[pos] == (byte)0) {
				double temperature= m_TargetTemperatures[pos];
				if (temperature < leftBound || temperature > rightBound) {
					m_AnomalousPixels[pos]= true;
					counterOfTemperatureBasedAnomalousPixels++;
				}
			}
		};
		numberOfTemperatureAnomalousPixels.set(counterOfTemperatureBasedAnomalousPixels);
		if (m_EZUSB.reportWarnings()) {
			writeLater(String.format("counterOfTemperatureBasedAnomalousPixels: %s\n",counterOfTemperatureBasedAnomalousPixels));
		}
	}
	//
	public void adjustTemperatureRange() {
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		boolean[] m_AnomalousPixels= commData.m_AnomalousPixels;
		double[] m_TargetTemperatures= commData.m_TargetTemperatures;
		int length0= m_TargetTemperatures.length;
		boolean isFirstElement= true;
		double leftBound= 0.0;
		double rightBound= 0.0;
		for (int pos0=0; pos0 < length0; pos0++) {
			if (m_pbyDeadData[pos0] == (byte)0 && !m_AnomalousPixels[pos0]) {
				double temperature= m_TargetTemperatures[pos0];
				if (isFirstElement) {
					isFirstElement= false;
					leftBound= temperature;
					rightBound= temperature;
				} else {
					if (temperature < leftBound) {
						leftBound= temperature;
					};
					if (temperature > rightBound) {
						rightBound= temperature;
					}
				}
			}
		};
		lowerTemperatureBound.set(leftBound);
		upperTemperatureBound.set(rightBound);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public byte[] createBuffer() {
		return createBuffer(commData.getRecvByteSize());
	}
	public static byte[] createBuffer(int iSize) {
		if (iSize % i3EZUSBDriver.PACKET_TIMES != 0) {
			iSize+= i3EZUSBDriver.PACKET_TIMES - (iSize % i3EZUSBDriver.PACKET_TIMES);
		};
		return new byte[iSize];
	}
	//
	public void delay(int temp) {
		try {
			Thread.sleep((long)temp);
		} catch (Exception e) {
			if (m_EZUSB.reportAdmissibleErrors()) {
				writeLater(String.format("Delay function error: %s\n",e));
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getRecvByteSize() {
		return commData.getRecvByteSize();
	}
	//
	public boolean setRecvData(byte[] buf) {
		if (buf.length != commData.getRecvByteSize()) {
			return false;
		} else {
			int recvSize= commData.getRecvSize();
			for (int pos= 0; pos < recvSize; pos++) {
				commData.m_piRecvData[pos]= (buf[pos * 2] & 255) + ((buf[(pos * 2) + 1] & 255) << 8);
			};
			int width= commData.getWindowingWidth();
			int height= commData.getWindowingHeight();
			int colStart= commData.getWindowingColStart();
			int colEnd= commData.getWindowingColEnd();
			int rowStart= commData.getWindowingRowStart();
			int rowEnd= commData.getWindowingRowEnd();
			int h= 0;
			int pos= 0;
			int winPos= 0;
			while (h < height) {
				int w= 0;
				while (w < width) {
					if (w >= colStart && w < colEnd && h >= rowStart && h < rowEnd) {
						commData.m_piRecvWinData[winPos]= commData.m_piRecvData[pos];
						winPos++;
					};
					w++;
					pos++;
				};
				h++;
			};
			return true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compute_FPA_Temperature() {
		int windowingWidth= commData.getWindowingWidth();
		int activeSize= commData.getActiveSize();
		float sum= 0.0f;
		int counter= 0;
		int index= 0;
		for (int y=0; y < 2; y++) {
			for (int x=0; x < windowingWidth; x++) {
				if (x >= 3) {
					sum+= (float)commData.m_piRecvData[activeSize+index];
					counter++;
				};
				index++;
			}
		};
		if (counter != 0) {
			m_dFpaTemp= (float)(((((double)(sum / (float)counter) * coefficient41 / coefficient42) * coefficient43 + coefficient44) * coefficient45 - coefficient46) / coefficient47);
		} else {
			if (m_EZUSB.reportAdmissibleErrors()) {
				writeLater("I cannot compute m_dFpaTemp.\n");
			}
		};
		m_pdSL_FpaTempArray[0]= 1.0f;
		for (int k=1; k < 4; k++) {
			m_pdSL_FpaTempArray[k]= m_pdSL_FpaTempArray[k-1] * m_dFpaTemp;
		};
		m_bIsTempCalcfinished= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void computeTemperatureMap() {
		int windowingWidth= commData.getWindowingWidth();
		int windowingEndHeight= commData.getWindowingHeight();
		int[] m_piRecvWinData= commData.m_piRecvWinData;
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		boolean[] m_AnomalousPixels= commData.m_AnomalousPixels;
		float[] m_pdSL_Gain= shutterLess.m_pdSL_Gain;
		float[] m_pdSL_Offset= shutterLess.m_pdSL_Offset;
		double[] m_pdTemperatureMap= commData.m_pdTemperatureMap;
		double[] m_pdSL_SkimOs= shutterLess.m_pdSL_SkimOs;
		int pos0= 0;
		for (int height=0; height < windowingEndHeight; height++) {
			for (int width=0; width < windowingWidth; width++) {
				if (m_pbyDeadData[pos0] == (byte)0 && !m_AnomalousPixels[pos0]) {
					double interGain= 0.0d;
					double interOs= 0.0d;
					for (int order=0; order < 4; order++) {
						interGain+= (double)(m_pdSL_FpaTempArray[order] * m_pdSL_Gain[(pos0 * 4) + order]);
						interOs+= (double)(m_pdSL_FpaTempArray[order] * m_pdSL_Offset[(pos0 * 4) + order]);
					};
					double dST_Gain= coefficient31 / interGain;
					double dST_Low= interOs;
					double dST_High= dST_Gain + dST_Low;
					double d= (coefficient32 / dST_Gain) * (double)m_piRecvWinData[pos0];
					double d2= coefficient33 * dST_High;
					m_pdTemperatureMap[pos0]= d + ((d2 - (coefficient34 * dST_Low)) / dST_Gain);
				} else {
					m_pdTemperatureMap[pos0]= 0.0d;
				};
				pos0++;
			}
		};
		bIsCalcTemperatrueMapFinished= true;
	}
	//
	public void eliminateAnomalousPixels(double[] temperatureArray) {
		int windowingWidth= commData.getWindowingWidth();
		int windowingEndHeight= commData.getWindowingHeight();
		byte[] m_pbyDeadData= commData.m_pbyDeadData;
		boolean[] m_AnomalousPixels= commData.m_AnomalousPixels;
		int temperatureArrayLength= temperatureArray.length;
		double averageTemperature= 0.0d;
		int counter= 0;
		for (int pos1= 0; pos1 < temperatureArrayLength; pos1++) {
			if (m_pbyDeadData[pos1] == (byte)0 && !m_AnomalousPixels[pos1]) {
				averageTemperature+= temperatureArray[pos1];
				counter++;
			}
		};
		if (counter > 0) {
			averageTemperature/= counter;
		};
		int pos1= 0;
		for (int height=0; height < windowingEndHeight; height++) {
			for (int width=0; width < windowingWidth; width++) {
				if (m_pbyDeadData[pos1] != (byte)0 || m_AnomalousPixels[pos1]) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
for (int radius=1; radius <= maximalDeadPixelEliminationRadius; radius++) {
	double avgTemp= 0.0d;
	int count= 0;
	for (int x= -radius; x <= radius; x++) {
		int y= -radius;
		int pos2= ((height + y) * windowingWidth) + (width + x);
		if (pos2 < 0 || pos2 >= temperatureArrayLength) {
			continue;
		};
		if (m_pbyDeadData[pos2] == (byte)0 && !m_AnomalousPixels[pos2]) {
			avgTemp+= temperatureArray[pos2];
			count++;
		}
	};
	for (int x= -radius; x <= radius; x++) {
		int y= radius;
		int pos2= ((height + y) * windowingWidth) + (width + x);
		if (pos2 < 0 || pos2 >= temperatureArrayLength) {
			continue;
		};
		if (m_pbyDeadData[pos2] == (byte)0 && !m_AnomalousPixels[pos2]) {
			avgTemp+= temperatureArray[pos2];
			count++;
		}
	};
	for (int y= -radius+1; y <= radius-1; y++) {
		int x= -radius;
		int pos2= ((height + y) * windowingWidth) + (width + x);
		if (pos2 < 0 || pos2 >= temperatureArrayLength) {
			continue;
		};
		if (m_pbyDeadData[pos2] == (byte)0 && !m_AnomalousPixels[pos2]) {
			avgTemp+= temperatureArray[pos2];
			count++;
		}
	};
	for (int y= -radius+1; y <= radius-1; y++) {
		int x= radius;
		int pos2= ((height + y) * windowingWidth) + (width + x);
		if (pos2 < 0 || pos2 >= temperatureArrayLength) {
			continue;
		};
		if (m_pbyDeadData[pos2] == (byte)0 && !m_AnomalousPixels[pos2]) {
			avgTemp+= temperatureArray[pos2];
			count++;
		}
	};
	if (count > 0) {
		temperatureArray[pos1]= avgTemp / count;
		break;
	} else {
		temperatureArray[pos1]= averageTemperature;
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				};
				pos1++;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void computeCorrectedTargetTemperatures() {
		computeCorrectedTargetTemperatures(true);
	}
	//
	public void computeCorrectedTargetTemperatures(boolean enableAnomalousPixelElimination) {
		if(isShutterCalibration.get()) {
			shutterCalibration();
		} else {
			double[] m_TargetTemperatures= shutterLess.computeTargetTemperatures(isCelsius.get());
			double[] m_pdShutterTemp= commData.m_pdShutterTemp;
			double m_dShutterCenterTemp= commData.m_dShutterCenterTemp;
			for (int k=0; k < m_pdShutterTemp.length; k++) {
				m_TargetTemperatures[k]= m_TargetTemperatures[k] - m_pdShutterTemp[k] + m_dShutterCenterTemp;
				// m_TargetTemperatures[k]= m_TargetTemperatures[k] + m_dShutterCenterTemp;
				// m_TargetTemperatures[k]= - m_pdShutterTemp[k] + m_dShutterCenterTemp;
			};
			if (enableAnomalousPixelElimination && eliminateAnomalousPixels.get()) {
				eliminateAnomalousPixels(m_TargetTemperatures);
			}
		}
	}
	//
	public double[] getTargetTemperatures() {
		return commData.getTargetTemperatures();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void test1() {
		// boolean result= flashReloadCommand();
		boolean result= flashReload();
		if (m_EZUSB.reportWarnings()) {
			writeLater(String.format("flashReloadCommand: %s\n",result));
		};
		initCamera();
	}
	public void test2() {
		sendFlashOffMode();
		if (m_EZUSB.reportWarnings()) {
			writeLater("sendFlashOffMode()\n");
		};
		initCamera();
	}
	public void test3() {
		sendFlashOnMode();
		if (m_EZUSB.reportWarnings()) {
			writeLater("sendFlashOnMode()\n");
		};
		initCamera();
	}
	//
	public boolean flashReload() {
		if (m_EZUSB.reportWarnings()) {
			writeLater("flashReload()\n");
		};
		isReadUserDeadFile= true;
		delay(50);
		while (!flashReloadCommand()) {
			delay(50);
		};
		//readFlashData();
		// if (m_EZUSB.reportFlashAttributes()) {
		//	reportSensorAttributes();
		// };
		byte[] pBuff= new byte[(commData.getSensorSize() * 2)];
		int nRecvBytes= 0;
		for (int i= 0; i < 28; i++) {
			while (nRecvBytes != pBuff.length) {
				nRecvBytes= m_EZUSB.read(pBuff,pBuff.length);
				delay(50);
			};
			nRecvBytes= 0;
		};
		delay(50);
		while (!flashReloadCommand()) {
			delay(50);
		};
		delay(50);
		readFlashData();
		if (m_EZUSB.reportFlashAttributes()) {
			reportSensorAttributes();
		};
		return true;
	}
	//
	public boolean flashReloadCommand() {
		if (m_EZUSB.reportWarnings()) {
			writeLater("flashReloadCommand()\n");
		};
		byte[] tempData= new byte[i3EZUSBDriver.PACKET_TIMES];
		int recvNum= 0;
		tempData[0]= (byte)-5;
		tempData[1]= (byte)-6;
		tempData[2]= (byte)0;
		tempData[3]= (byte)-1;
		tempData[4]= (byte)0;
		tempData[5]= (byte)0;
		delay(50);
		while (recvNum != tempData.length) {
			recvNum= m_EZUSB.write(tempData,tempData.length);
			delay(50);
		};
		delay(50);
		if (tempData.length != recvNum) {
			return false;
		};
		byte[] tempDead= new byte[commData.getRecvByteSize()];
		while (recvNum != tempDead.length) {
			recvNum= m_EZUSB.read(tempDead,tempDead.length);
			delay(50);
		};
		delay(50);
		tempData[0]= (byte)-5;
		tempData[1]= (byte)-6;
		tempData[2]= (byte)0;
		tempData[3]= (byte)-1;
		tempData[4]= (byte)1;
		tempData[5]= (byte)0;
		while (recvNum != tempData.length) {
			recvNum= m_EZUSB.write(tempData,tempData.length);
			delay(50);
		};
		return tempData.length == recvNum;
	}
	//
	public void sendFlashOffMode() {
		byte[] pBuff= new byte[i3EZUSBDriver.PACKET_TIMES];
		pBuff[2]= (byte)0;
		pBuff[3]= (byte)-1;
		pBuff[4]= (byte)0;
		pBuff[5]= (byte)0;
		do {
		} while (m_EZUSB.write(pBuff,i3EZUSBDriver.PACKET_TIMES) != i3EZUSBDriver.PACKET_TIMES);
	}
	public void sendFlashOnMode() {
		byte[] pBuff= new byte[i3EZUSBDriver.PACKET_TIMES];
		pBuff[2]= (byte)0;
		pBuff[3]= (byte)-1;
		pBuff[4]= (byte)1;
		pBuff[5]= (byte)0;
		do {
		} while (m_EZUSB.write(pBuff,i3EZUSBDriver.PACKET_TIMES) != i3EZUSBDriver.PACKET_TIMES);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double calcTableGainOffset(double _temp) {
		return ((shutterLess.getTemperatureGain() * _temp) - shutterLess.getTemperatureOffset()) + shutterLess.getTestTempOffset();
	}
	//
	public double calcAmbientChamberTemp() {
		return shutterLess.calcAmbientChamberTemp(m_dFpaTemp);
	}
	//
	public void dumpMemory() {
		// computeTempDataDouble();
		compute_FPA_Temperature();
		computeTemperatureMap();
		computeCorrectedTargetTemperatures();
		int[] m_piRecvData= commData.m_piRecvData;
		double[] map= commData.m_pdTemperatureMap;
		double[] m_pdShutterTemp= commData.m_pdShutterTemp;
		double[] m_pdSL_SkimOs= shutterLess.m_pdSL_SkimOs;
		double[] actualTemperature= commData.getTargetTemperatures();
		// double[] vector_calcTemp_Table= new double[actualTemperature.length];
		// double[] something= new double[actualTemperature.length];
		double[] targetTemperatures= commData.getTargetTemperatures();
		int windowingWidth= commData.getWindowingWidth();
		int windowingHeight= commData.getWindowingHeight();
		int pos= 0;
		for (int h=0; h < windowingHeight; h++) {
			for (int w=0; w < windowingWidth; w++) {
				pos++;
			}
		};
		try {
			String fileName11= String.format("dump_320_240.txt");
			PrintStream stream= new PrintStream(fileName11);
			if (m_EZUSB.reportWarnings()) {
				writeLater(String.format("I WILL CREATE THE FILE: %s\n",fileName11));
			};
			try {
				for (int n=0; n < AGCDataDouble.length; n++) {
					stream.printf(
						"%d\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
						m_piRecvData[n],
						TempDataDouble[n],
						AGCDataDouble[n],
						actualTemperature[n],
						m_pdShutterTemp[n],
						m_pdSL_SkimOs[n],
						// something[n],
						// vector_calcTemp_Table[n],
						map[n],
						targetTemperatures[n]);
				}
			} finally {
				stream.close();
				if (m_EZUSB.reportWarnings()) {
					writeLater("Done\n");
				}
			}
		} catch (FileNotFoundException e) {
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public int read(byte[] buf, int iSize) {
		return m_EZUSB.read(buf,iSize);
	}
	//
	synchronized public int write(byte[] buf, int iSize) {
		return m_EZUSB.write(buf,iSize);
	}
	//
	synchronized public int resetDevice() {
		int result= 0;
		// result= m_EZUSB.resetDevice();
		closeDevice();
		closeUSB();
		m_EZUSB.initializeLibUSB();
		openDevice();
		initCamera();
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeLater(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.err.print(text);
			}
		});
	}
}
