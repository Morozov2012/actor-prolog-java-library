// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.astrohn.*;
import morozov.system.astrohn.converters.*;
import morozov.system.astrohn.errors.*;
import morozov.system.astrohn.frames.*;
import morozov.system.astrohn.frames.data.*;
import morozov.system.astrohn.frames.interfaces.*;
import morozov.system.astrohn.interfaces.*;
import morozov.system.converters.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.data.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.ip_camera.*;
import morozov.system.ip_camera.frames.*;
import morozov.system.ip_camera.frames.interfaces.*;
import morozov.system.ip_camera.interfaces.*;
import morozov.system.modes.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.awt.Color;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Astrohn extends ThermalDataAcquisitionBuffer implements AstrohnDataConsumerInterface, IPCameraDataConsumerInterface {
	//
	protected String terahertzCameraAddress;
	protected Integer terahertzCameraPort;
	protected String colorCameraURL;
	protected YesNo synchronizeTerahertzAndColorStreams;
	//
	protected AstrohnDataAcquisition terahertzDataAcquisition= new AstrohnDataAcquisition();
	protected IPCameraDataAcquisition ipCameraDataAcquisition= new IPCameraDataAcquisition();
	//
	protected AtomicInteger colorCameraDelayQueueSize= new AtomicInteger(6);
	protected ArrayDeque<IPCameraFrame> ipCameraQueue= new ArrayDeque<>(colorCameraDelayQueueSize.get());
	//
	protected AtomicLong counterOfAcquiredTerahertzFrames= new AtomicLong(-1);
	protected AtomicLong counterOfAcquiredIPCameraFrames= new AtomicLong(-1);
	//
	protected AtomicLong numberOfRecentTerahertzFrame= new AtomicLong(-1);
	protected AtomicLong numberOfRecentIPCameraFrame= new AtomicLong(-1);
	protected AtomicLong numberOfSynchronousIPCameraFrame= new AtomicLong(-1);
	//
	protected AtomicBoolean actingSynchronizeTerahertzAndColorStreams= new AtomicBoolean(false);
	//
	protected long numberOfRepeatedTerahertzFrame= -1;
	protected long numberOfRepeatedColorFrame= -1;
	//
	protected THzDataFrameInterface recentTHzDataFrame;
	protected IPCameraFrameInterface recentIPCameraFrame;
	protected IPCameraFrameInterface synchronousIPCameraFrame;
	//
	protected THzDataFrameInterface committedTHzDataFrame;
	protected IPCameraFrameInterface committedIPCameraFrame;
	protected java.awt.image.BufferedImage committedIPCameraFrameImage;
	//
	protected long committedTerahertzFrameNumber= -1;
	protected long committedColorFrameNumber= -1;
	protected long committedTerahertzFrameTime= -1;
	protected long committedColorFrameTime= -1;
	protected long firstCommittedTerahertzFrameNumber= -1;
	protected long firstCommittedColorFrameNumber= -1;
	protected long firstCommittedTerahertzFrameTime= -1;
	protected long firstCommittedColorFrameTime= -1;
	//
	protected long acceptedTerahertzFrameNumber= 0;
	protected long acceptedTerahertzFrameTime= -1;
	protected long firstAcceptedTerahertzFrameNumber= -1;
	protected long firstAcceptedTerahertzFrameTime= -1;
	//
	protected long acceptedColorFrameNumber= 0;
	protected long acceptedColorFrameTime= -1;
	protected long firstAcceptedColorFrameNumber= -1;
	protected long firstAcceptedColorFrameTime= -1;
	//
	///////////////////////////////////////////////////////////////
	//
	public Astrohn() {
		super(	new AstrohnDataReadingTask(),
			new DataFrameRecordingTask());
		connectAstrohnClassInstance();
	}
	public Astrohn(GlobalWorldIdentifier id) {
		super(	id,
			new AstrohnDataReadingTask(),
			new DataFrameRecordingTask());
		connectAstrohnClassInstance();
	}
	//
	protected void connectAstrohnClassInstance() {
		terahertzDataAcquisition.setDataConsumer(this);
		ipCameraDataAcquisition.setDataConsumer(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_terahertz_camera_address();
	abstract public Term getBuiltInSlot_E_terahertz_camera_port();
	abstract public Term getBuiltInSlot_E_color_camera_url();
	abstract public Term getBuiltInSlot_E_synchronize_terahertz_and_color_streams();
	abstract public Term getBuiltInSlot_E_color_camera_delay_queue_size();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set terahertz_camera_address
	//
	public void setTerahertzCameraAddress1s(ChoisePoint iX, Term a1) {
		String text= GeneralConverters.argumentToString(a1,iX);
		setTerahertzCameraAddress(text);
	}
	public void setTerahertzCameraAddress(String value) {
		terahertzCameraAddress= value;
	}
	public void getTerahertzCameraAddress0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologString(getTerahertzCameraAddress(iX)));
	}
	public void getTerahertzCameraAddress0fs(ChoisePoint iX) {
	}
	public String getTerahertzCameraAddress(ChoisePoint iX) {
		if (terahertzCameraAddress != null) {
			return terahertzCameraAddress;
		} else {
			Term value= getBuiltInSlot_E_terahertz_camera_address();
			return GeneralConverters.argumentToString(value,iX);
		}
	}
	//
	// get/set terahertz_camera_port
	//
	public void setTerahertzCameraPort1s(ChoisePoint iX, Term a1) {
		int value= GeneralConverters.argumentToSmallInteger(a1,iX);
		setTerahertzCameraPort(value);
	}
	public void setTerahertzCameraPort(int value) {
		terahertzCameraPort= value;
	}
	public void getTerahertzCameraPort0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getTerahertzCameraPort(iX)));
	}
	public void getTerahertzCameraPort0fs(ChoisePoint iX) {
	}
	public int getTerahertzCameraPort(ChoisePoint iX) {
		if (terahertzCameraPort != null) {
			return terahertzCameraPort;
		} else {
			Term value= getBuiltInSlot_E_terahertz_camera_port();
			return GeneralConverters.argumentToSmallInteger(value,iX);
		}
	}
	//
	// get/set color_camera_url
	//
	public void setColorCameraURL1s(ChoisePoint iX, Term a1) {
		String text= GeneralConverters.argumentToString(a1,iX);
		setColorCameraURL(text);
	}
	public void setColorCameraURL(String value) {
		colorCameraURL= value;
	}
	public void getColorCameraURL0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologString(getColorCameraURL(iX)));
	}
	public void getColorCameraURL0fs(ChoisePoint iX) {
	}
	public String getColorCameraURL(ChoisePoint iX) {
		if (colorCameraURL != null) {
			return colorCameraURL;
		} else {
			Term value= getBuiltInSlot_E_color_camera_url();
			return GeneralConverters.argumentToString(value,iX);
		}
	}
	//
	// get/set synchronize_terahertz_and_color_streams
	//
	public void setSynchronizeTerahertzAndColorStreams1s(ChoisePoint iX, Term a1) {
		setSynchronizeTerahertzAndColorStreams(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setSynchronizeTerahertzAndColorStreams(YesNo value) {
		synchronizeTerahertzAndColorStreams= value;
		actingSynchronizeTerahertzAndColorStreams.set(value.toBoolean());
	}
	public void getSynchronizeTerahertzAndColorStreams0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getSynchronizeTerahertzAndColorStreams(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getSynchronizeTerahertzAndColorStreams0fs(ChoisePoint iX) {
	}
	public YesNo getSynchronizeTerahertzAndColorStreams(ChoisePoint iX) {
		if (synchronizeTerahertzAndColorStreams != null) {
			return synchronizeTerahertzAndColorStreams;
		} else {
			Term value= getBuiltInSlot_E_synchronize_terahertz_and_color_streams();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set color_camera_delay_queue_size
	//
	public void setColorCameraDelayQueueSize1s(ChoisePoint iX, Term a1) {
		int value= GeneralConverters.argumentToSmallInteger(a1,iX);
		setColorCameraDelayQueueSize(value);
	}
	public void setColorCameraDelayQueueSize(int value) {
		colorCameraDelayQueueSize.set(value);
	}
	public void getColorCameraDelayQueueSize0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getColorCameraDelayQueueSize(iX)));
	}
	public void getColorCameraDelayQueueSize0fs(ChoisePoint iX) {
	}
	public int getColorCameraDelayQueueSize(ChoisePoint iX) {
		Integer size= colorCameraDelayQueueSize.get();
		if (size != null) {
			return size;
		} else {
			Term value= getBuiltInSlot_E_color_camera_delay_queue_size();
			return GeneralConverters.argumentToSmallInteger(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void releaseSystemResources() {
		terahertzDataAcquisition.stopDataTransfer();
		ipCameraDataAcquisition.stopDataTransfer();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setOutputDebugInformation(BigInteger value) {
		super.setOutputDebugInformation(value);
		int mode= PrologInteger.toInteger(value);
		terahertzDataAcquisition.setOutputDebugInformation(mode);
		ipCameraDataAcquisition.setOutputDebugInformation(mode);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateAttributes(ChoisePoint iX) {
		boolean mode= getSynchronizeTerahertzAndColorStreams(iX).toBoolean();
		actingSynchronizeTerahertzAndColorStreams.set(mode);
		boolean currentDoNotControlTemperatureRange= getUseRecordedTemperatureRangeCommands(iX).toBoolean();
		boolean currentAveragingMode= getAveragingMode(iX).toBoolean();
		DataFrameColorfulAttributes attributes= new DataFrameColorfulAttributes(
			counterOfRecentAttributes.incrementAndGet(),
			getAutorangingMode(iX).toBoolean(),
			getDoubleColorMapMode(iX).toBoolean(),
			DataRange.BOUNDS, // getSelectedDataRange(),
			NumericalValueConverters.toDouble(getLowerTemperatureBound(iX)),
			NumericalValueConverters.toDouble(getUpperTemperatureBound(iX)),
			NumericalValueConverters.toDouble(getLowerMainTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getUpperMainTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getLowerAuxiliaryTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getUpperAuxiliaryTemperatureQuantile(iX)),
			getMainColorMap(iX),
			getAuxiliaryColorMap(iX),
			currentAveragingMode,
			getZoomImage(iX).toBoolean(),
			getZoomingCoefficient(iX));
		actingDoNotControlTemperatureRange.set(currentDoNotControlTemperatureRange);
		actingAveragingMode.set(currentAveragingMode);
		recentAttributes.set(attributes);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			actingSynchronizeTerahertzAndColorStreams.set(false);
			counterOfAcquiredTerahertzFrames.set(-1);
			counterOfAcquiredIPCameraFrames.set(-1);
			numberOfRecentTerahertzFrame.set(-1);
			numberOfRecentIPCameraFrame.set(-1);
			numberOfSynchronousIPCameraFrame.set(-1);
			recentTHzDataFrame= null;
			recentIPCameraFrame= null;
			synchronousIPCameraFrame= null;
			numberOfRepeatedTerahertzFrame= -1;
			numberOfRepeatedColorFrame= -1;
			committedTHzDataFrame= null;
			committedIPCameraFrame= null;
			committedIPCameraFrameImage= null;
			committedCumulativeTemperatures= null;
		}
	}
	//
	protected void resetFrameRate() {
		committedTerahertzFrameNumber= -1;
		committedColorFrameNumber= -1;
		committedTerahertzFrameTime= -1;
		committedColorFrameTime= -1;
		firstCommittedTerahertzFrameNumber= -1;
		firstCommittedColorFrameNumber= -1;
		firstCommittedTerahertzFrameTime= -1;
		firstCommittedColorFrameTime= -1;
		//
		acceptedTerahertzFrameNumber= 0;
		acceptedColorFrameNumber= 0;
		acceptedTerahertzFrameTime= -1;
		acceptedColorFrameTime= -1;
		firstAcceptedTerahertzFrameNumber= -1;
		firstAcceptedColorFrameNumber= -1;
		firstAcceptedTerahertzFrameTime= -1;
		firstAcceptedColorFrameTime= -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void activateDataAcquisition(ChoisePoint iX) {
		boolean mode= getSynchronizeTerahertzAndColorStreams(iX).toBoolean();
		actingSynchronizeTerahertzAndColorStreams.set(mode);
		ActionPeriod period= getConnectionAttemptPeriod(iX);
		int givenConnectionAttemptPeriod= period.toMillisecondsOrDefault(defaultDeviceConnectionAttemptPeriod);
		ipCameraDataAcquisition.setServerAttributes(
			getColorCameraURL(iX),
			givenConnectionAttemptPeriod);
		terahertzDataAcquisition.setServerAttributes(
			getTerahertzCameraAddress(iX),
			getTerahertzCameraPort(iX),
			givenConnectionAttemptPeriod);
		int currentOutputDebugInformation= PrologInteger.toInteger(getOutputDebugInformation(iX));
		ipCameraDataAcquisition.activateDataTransfer(currentOutputDebugInformation);
		terahertzDataAcquisition.activateDataTransfer(currentOutputDebugInformation);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void suspendRecording(ChoisePoint iX) {
		ipCameraDataAcquisition.suspendDataTransfer();
		terahertzDataAcquisition.suspendDataTransfer();
		super.suspendRecording(iX);
	}
	protected void suspendListening(ChoisePoint iX) {
		ipCameraDataAcquisition.suspendDataTransfer();
		terahertzDataAcquisition.suspendDataTransfer();
		super.suspendListening(iX);
	}
	//
	protected void stopRecording(ChoisePoint iX) {
		ipCameraDataAcquisition.stopDataTransfer();
		terahertzDataAcquisition.stopDataTransfer();
		super.stopRecording(iX);
	}
	protected void stopListening(ChoisePoint iX) {
		ipCameraDataAcquisition.stopDataTransfer();
		terahertzDataAcquisition.stopDataTransfer();
		super.stopListening(iX);
	}
	//
	protected boolean dataAcquisitionIsActive() {
		return	!ipCameraDataAcquisition.isSuspended() ||
			!terahertzDataAcquisition.isSuspended();
	}
	//
	protected boolean dataAcquisitionIsSuspended() {
		return	ipCameraDataAcquisition.isSuspended() &&
			terahertzDataAcquisition.isSuspended();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) throws Backtracking {
		if (committedFrameWasAssignedDirectly.get()) {
			if (committedFrame==null) {
				throw Backtracking.instance;
			}
		} else {
			boolean mode= getSynchronizeTerahertzAndColorStreams(iX).toBoolean();
			actingSynchronizeTerahertzAndColorStreams.set(mode);
			synchronized (numberOfRecentReceivedFrame) {
				if (recentFrame==null) {
					throw Backtracking.instance;
				};
				commit(mode);
			}
		}
	}
	//
	protected void commit(boolean synchronizeStreams) {
		synchronized (numberOfRecentReceivedFrame) {
			super.commit();
			committedFrame= recentFrame;
			committedTHzDataFrame= recentTHzDataFrame;
			if (synchronizeStreams) {
				committedIPCameraFrame= synchronousIPCameraFrame;
			} else {
				committedIPCameraFrame= recentIPCameraFrame;
			};
			committedIPCameraFrameImage= null;
			if (!recentFrameIsRepeated) {
				committedTerahertzFrameNumber= numberOfRecentTerahertzFrame.get();
				if (synchronizeStreams) {
					committedColorFrameNumber= numberOfSynchronousIPCameraFrame.get();
				} else {
					committedColorFrameNumber= numberOfRecentIPCameraFrame.get();
				}
			} else {
				committedTerahertzFrameNumber= numberOfRepeatedTerahertzFrame;
				committedColorFrameNumber= numberOfRepeatedColorFrame;
			};
			updateCommittedFrameTime();
		}
	}
	//
	protected void updateCommittedFrameTime() {
		if (committedTHzDataFrame != null) {
			committedTerahertzFrameTime= committedTHzDataFrame.getTime();
		} else {
			committedTerahertzFrameTime= -1;
		};
		if (committedIPCameraFrame != null) {
			committedColorFrameTime= committedIPCameraFrame.getTime();
		} else {
			committedColorFrameTime= -1;
		};
		if (firstCommittedTerahertzFrameTime < 0) {
			firstCommittedTerahertzFrameNumber= committedTerahertzFrameNumber;
			firstCommittedTerahertzFrameTime= committedTerahertzFrameTime;
		};
		if (firstCommittedColorFrameTime < 0) {
			firstCommittedColorFrameNumber= committedColorFrameNumber;
			firstCommittedColorFrameTime= committedColorFrameTime;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentTerahertzFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		long frameNumber;
		synchronized (numberOfRecentReceivedFrame) {
			frameNumber= committedTerahertzFrameNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(frameNumber),iX);
	}
	public void getRecentTerahertzFrameNumber2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		long committedFN;
		long acceptedFN;
		synchronized (numberOfRecentReceivedFrame) {
			committedFN= committedTerahertzFrameNumber;
			acceptedFN= acceptedTerahertzFrameNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(committedFN),iX);
		a2.setBacktrackableValue(new PrologInteger(acceptedFN),iX);
	}
	public void getRecentColorFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		long frameNumber;
		synchronized (numberOfRecentReceivedFrame) {
			frameNumber= committedColorFrameNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(frameNumber),iX);
	}
	public void getRecentColorFrameNumber2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		long committedFN;
		long acceptedFN;
		synchronized (numberOfRecentReceivedFrame) {
			committedFN= committedColorFrameNumber;
			acceptedFN= acceptedColorFrameNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(committedFN),iX);
		a2.setBacktrackableValue(new PrologInteger(acceptedFN),iX);
	}
	//
	public void getRecentTerahertzFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		long frameTime;
		synchronized (numberOfRecentReceivedFrame) {
			frameTime= committedTerahertzFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(frameTime),iX);
	}
	public void getRecentTerahertzFrameTime2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		long committedFT;
		long acceptedFT;
		synchronized (numberOfRecentReceivedFrame) {
			committedFT= committedTerahertzFrameTime;
			acceptedFT= acceptedTerahertzFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(committedFT),iX);
		a2.setBacktrackableValue(new PrologInteger(acceptedFT),iX);
	}
	public void getRecentColorFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		long frameTime;
		synchronized (numberOfRecentReceivedFrame) {
			frameTime= committedColorFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(frameTime),iX);
	}
	public void getRecentColorFrameTime2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		long committedFT;
		long acceptedFT;
		synchronized (numberOfRecentReceivedFrame) {
			committedFT= committedColorFrameTime;
			acceptedFT= acceptedColorFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(committedFT),iX);
		a2.setBacktrackableValue(new PrologInteger(acceptedFT),iX);
	}
	//
	public void getRecentTerahertzFrameRelativeTime1s(ChoisePoint iX, PrologVariable a1) {
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaTime= committedTerahertzFrameTime - firstCommittedTerahertzFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaTime),iX);
	}
	public void getRecentTerahertzFrameRelativeTime2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		long deltaCommittedFrameTime;
		long deltaAcceptedFrameTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaCommittedFrameTime= committedTerahertzFrameTime - firstCommittedTerahertzFrameTime;
			deltaAcceptedFrameTime= acceptedTerahertzFrameTime - firstAcceptedTerahertzFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaCommittedFrameTime),iX);
		a2.setBacktrackableValue(new PrologInteger(deltaAcceptedFrameTime),iX);
	}
	public void getRecentColorFrameRelativeTime1s(ChoisePoint iX, PrologVariable a1) {
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaTime= committedColorFrameTime - firstCommittedColorFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaTime),iX);
	}
	public void getRecentColorFrameRelativeTime2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		long deltaCommittedFrameTime;
		long deltaAcceptedFrameTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaCommittedFrameTime= committedColorFrameTime - firstCommittedColorFrameTime;
			deltaAcceptedFrameTime= acceptedColorFrameTime - firstAcceptedColorFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaCommittedFrameTime),iX);
		a2.setBacktrackableValue(new PrologInteger(deltaAcceptedFrameTime),iX);
	}
	//
	public void getRecentTerahertzFrameRate1s(ChoisePoint iX, PrologVariable a1) {
		long deltaN;
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaN= committedTerahertzFrameNumber - firstCommittedTerahertzFrameNumber;
			deltaTime= committedTerahertzFrameTime - firstCommittedTerahertzFrameTime;
		};
		double rate= computeFrameRate(deltaN,deltaTime);
		a1.setBacktrackableValue(new PrologReal(rate),iX);
	}
	public void getRecentTerahertzFrameRate2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		long deltaCommittedFrameNumber;
		long deltaAcceptedFrameNumber;
		long deltaCommittedFrameTime;
		long deltaAcceptedFrameTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaCommittedFrameNumber= committedTerahertzFrameNumber - firstCommittedTerahertzFrameNumber;
			deltaAcceptedFrameNumber= acceptedTerahertzFrameNumber - firstAcceptedTerahertzFrameNumber;
			deltaCommittedFrameTime= committedTerahertzFrameTime - firstCommittedTerahertzFrameTime;
			deltaAcceptedFrameTime= acceptedTerahertzFrameTime - firstAcceptedTerahertzFrameTime;
		};
		double committedFrameRate= computeFrameRate(deltaCommittedFrameNumber,deltaCommittedFrameTime);
		double acceptedFrameRate= computeFrameRate(deltaAcceptedFrameNumber,deltaAcceptedFrameTime);
		a1.setBacktrackableValue(new PrologReal(committedFrameRate),iX);
		a2.setBacktrackableValue(new PrologReal(acceptedFrameRate),iX);
	}
	public void getRecentColorFrameRate1s(ChoisePoint iX, PrologVariable a1) {
		long deltaN;
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaN= committedColorFrameNumber - firstCommittedColorFrameNumber;
			deltaTime= committedColorFrameTime - firstCommittedColorFrameTime;
		};
		double rate= computeFrameRate(deltaN,deltaTime);
		a1.setBacktrackableValue(new PrologReal(rate),iX);
	}
	public void getRecentColorFrameRate2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		long deltaCommittedFrameNumber;
		long deltaAcceptedFrameNumber;
		long deltaCommittedFrameTime;
		long deltaAcceptedFrameTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaCommittedFrameNumber= committedColorFrameNumber - firstCommittedColorFrameNumber;
			deltaAcceptedFrameNumber= acceptedColorFrameNumber - firstAcceptedColorFrameNumber;
			deltaCommittedFrameTime= committedColorFrameTime - firstCommittedColorFrameTime;
			deltaAcceptedFrameTime= acceptedColorFrameTime - firstAcceptedColorFrameTime;
		};
		double committedFrameRate= computeFrameRate(deltaCommittedFrameNumber,deltaCommittedFrameTime);
		double acceptedFrameRate= computeFrameRate(deltaAcceptedFrameNumber,deltaAcceptedFrameTime);
		a1.setBacktrackableValue(new PrologReal(committedFrameRate),iX);
		a2.setBacktrackableValue(new PrologReal(acceptedFrameRate),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentTerahertzImage1s(ChoisePoint iX, Term value) throws Backtracking {
		updateAttributesIfNecessary(iX);
		TerahertzDataBuffer terahertzDataBuffer;
		DataFrameBaseAttributesInterface terahertzAttributes;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedTHzDataFrame != null) {
					terahertzAttributes= committedTHzDataFrame.getBaseAttributes();
					terahertzDataBuffer= getTerahertzDataBuffer(terahertzAttributes);
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw new AstrohnFrameIsNotCommitted();
			}
		};
		YesNo doNotControlColorMaps= getUseRecordedColorMapCommands(iX);
		int[][] mainColorMap= prepareMainColorMap(doNotControlColorMaps.toBoolean(),terahertzAttributes);
		int[][] auxiliaryColorMap= prepareAuxiliaryColorMap(doNotControlColorMaps.toBoolean(),terahertzAttributes);
		boolean isAutorangingMode;
		boolean isDoubleColorMapMode;
		double lowerDataBound;
		double upperDataBound;
		double lowerDataQuantile1;
		double upperDataQuantile1;
		double lowerDataQuantile2;
		double upperDataQuantile2;
		if (actingDoNotControlTemperatureRange.get()) {
			isAutorangingMode= terahertzAttributes.isAutorangingMode();
			isDoubleColorMapMode= terahertzAttributes.isDoubleColorMapMode();
			lowerDataBound= terahertzAttributes.getLowerDataBound();
			upperDataBound= terahertzAttributes.getUpperDataBound();
			lowerDataQuantile1= terahertzAttributes.getLowerDataQuantile1();
			upperDataQuantile1= terahertzAttributes.getUpperDataQuantile1();
			lowerDataQuantile2= terahertzAttributes.getLowerDataQuantile2();
			upperDataQuantile2= terahertzAttributes.getUpperDataQuantile2();
		} else {
			isAutorangingMode= getAutorangingMode(iX).toBoolean();
			isDoubleColorMapMode= getDoubleColorMapMode(iX).toBoolean();
			lowerDataBound= NumericalValueConverters.toDouble(getLowerTemperatureBound(iX));
			upperDataBound= NumericalValueConverters.toDouble(getUpperTemperatureBound(iX));
			lowerDataQuantile1= NumericalValueConverters.toDouble(getLowerMainTemperatureQuantile(iX));
			upperDataQuantile1= NumericalValueConverters.toDouble(getUpperMainTemperatureQuantile(iX));
			lowerDataQuantile2= NumericalValueConverters.toDouble(getLowerAuxiliaryTemperatureQuantile(iX));
			upperDataQuantile2= NumericalValueConverters.toDouble(getUpperAuxiliaryTemperatureQuantile(iX));
		};
		YesNo doNotControlZooming= getUseRecordedZoomingCommands(iX);
		YesNo doZoomImage= getZoomImage(iX);
		NumericalValue numericalZoomingCoefficient= getZoomingCoefficient(iX);
		boolean zoomIt;
		double zCoefficient;
		if (doNotControlZooming.toBoolean()) {
			zoomIt= terahertzAttributes.isZoomingMode();
			zCoefficient= terahertzAttributes.getZoomingCoefficient();
		} else {
			zoomIt= doZoomImage.toBoolean();
			zCoefficient= NumericalValueConverters.toDouble(numericalZoomingCoefficient);
		};
		AttachedImage attachedImage= DataFrameTools.temperaturesToImage(
			terahertzDataBuffer.getData(),
			terahertzDataBuffer.getMatrixWidth(),
			terahertzDataBuffer.getMatrixHeight(),
			mainColorMap,
			auxiliaryColorMap,
			isAutorangingMode,
			isDoubleColorMapMode,
			lowerDataBound,
			upperDataBound,
			lowerDataQuantile1,
			upperDataQuantile1,
			lowerDataQuantile2,
			upperDataQuantile2,
			zoomIt,
			zCoefficient);
		java.awt.image.BufferedImage nativeImage= attachedImage.getImage();
		modifyImage(value,nativeImage,iX);
	}
	//
	protected TerahertzDataBuffer getTerahertzDataBuffer(DataFrameBaseAttributesInterface attributes) {
		boolean isAveragingMode;
		if (actingDoNotControlTemperatureRange.get()) {
			isAveragingMode= attributes.isAverageMode();
		} else {
			isAveragingMode= actingAveragingMode.get();
		};
		TVFilterImageHeader terahertzPacketBody= committedTHzDataFrame.getTHzData();
		double[] data;
		if (isAveragingMode && committedCumulativeTemperatures!=null) {
			data= committedCumulativeTemperatures;
		} else {
			data= AstrohnDataAcquisition.tvFilterImageHeaderToDouble(terahertzPacketBody);
		};
		return new TerahertzDataBuffer(data,terahertzPacketBody);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentColorImage1s(ChoisePoint iX, Term value) throws Backtracking {
		updateAttributesIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage;
		DataFrameBaseAttributesInterface attributes;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedIPCameraFrame != null) {
					nativeImage= getNativeColorImage();
					attributes= committedIPCameraFrame.getBaseAttributes();
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw new AstrohnFrameIsNotCommitted();
			}
		};
		if (nativeImage==null) {
			throw Backtracking.instance;
		};
		YesNo doNotControlZooming= getUseRecordedZoomingCommands(iX);
		YesNo doZoomImage= getZoomImage(iX);
		NumericalValue numericalZoomingCoefficient= getZoomingCoefficient(iX);
		boolean zoomIt;
		double zCoefficient;
		if (doNotControlZooming.toBoolean()) {
			zoomIt= attributes.isZoomingMode();
			zCoefficient= attributes.getZoomingCoefficient();
		} else {
			zoomIt= doZoomImage.toBoolean();
			zCoefficient= NumericalValueConverters.toDouble(numericalZoomingCoefficient);
		};
		nativeImage= DataFrameTools.rotateAndZoomImage(
			nativeImage,
			zoomIt,
			zCoefficient);
		modifyImage(value,nativeImage,iX);
	}
	//
	protected java.awt.image.BufferedImage getNativeColorImage() {
		if (committedIPCameraFrameImage != null)  {
			return committedIPCameraFrameImage;
		};
		byte[] byteArray= committedIPCameraFrame.getByteArray();
		try {
			java.awt.image.BufferedImage nativeImage= IPCameraDataAcquisition.byteArrayToBufferedImage(byteArray);
			committedIPCameraFrameImage= nativeImage;
			return nativeImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentCombinedImage1s(ChoisePoint iX, Term value) throws Backtracking {
		updateAttributesIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage;
		TerahertzDataBuffer terahertzDataBuffer;
		DataFrameBaseAttributesInterface terahertzAttributes;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedTHzDataFrame != null && committedIPCameraFrame != null) {
					nativeImage= getNativeColorImage();
					terahertzAttributes= committedTHzDataFrame.getBaseAttributes();
					terahertzDataBuffer= getTerahertzDataBuffer(terahertzAttributes);
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw new AstrohnFrameIsNotCommitted();
			}
		};
		if (nativeImage==null) {
			throw Backtracking.instance;
		};
		YesNo doNotControlColorMaps= getUseRecordedColorMapCommands(iX);
		int[][] mainColorMap= prepareMainColorMap(doNotControlColorMaps.toBoolean(),terahertzAttributes);
		int[][] auxiliaryColorMap= prepareAuxiliaryColorMap(doNotControlColorMaps.toBoolean(),terahertzAttributes);
		boolean isAutorangingMode;
		boolean isDoubleColorMapMode;
		double lowerDataBound;
		double upperDataBound;
		double lowerDataQuantile1;
		double upperDataQuantile1;
		double lowerDataQuantile2;
		double upperDataQuantile2;
		if (actingDoNotControlTemperatureRange.get()) {
			isAutorangingMode= terahertzAttributes.isAutorangingMode();
			isDoubleColorMapMode= terahertzAttributes.isDoubleColorMapMode();
			lowerDataBound= terahertzAttributes.getLowerDataBound();
			upperDataBound= terahertzAttributes.getUpperDataBound();
			lowerDataQuantile1= terahertzAttributes.getLowerDataQuantile1();
			upperDataQuantile1= terahertzAttributes.getUpperDataQuantile1();
			lowerDataQuantile2= terahertzAttributes.getLowerDataQuantile2();
			upperDataQuantile2= terahertzAttributes.getUpperDataQuantile2();
		} else {
			isAutorangingMode= getAutorangingMode(iX).toBoolean();
			isDoubleColorMapMode= getDoubleColorMapMode(iX).toBoolean();
			lowerDataBound= NumericalValueConverters.toDouble(getLowerTemperatureBound(iX));
			upperDataBound= NumericalValueConverters.toDouble(getUpperTemperatureBound(iX));
			lowerDataQuantile1= NumericalValueConverters.toDouble(getLowerMainTemperatureQuantile(iX));
			upperDataQuantile1= NumericalValueConverters.toDouble(getUpperMainTemperatureQuantile(iX));
			lowerDataQuantile2= NumericalValueConverters.toDouble(getLowerAuxiliaryTemperatureQuantile(iX));
			upperDataQuantile2= NumericalValueConverters.toDouble(getUpperAuxiliaryTemperatureQuantile(iX));
		};
		YesNo doNotControlZooming= getUseRecordedZoomingCommands(iX);
		YesNo doZoomImage= getZoomImage(iX);
		NumericalValue numericalZoomingCoefficient= getZoomingCoefficient(iX);
		boolean zoomIt;
		double zCoefficient;
		if (doNotControlZooming.toBoolean()) {
			zoomIt= terahertzAttributes.isZoomingMode();
			zCoefficient= terahertzAttributes.getZoomingCoefficient();
		} else {
			zoomIt= doZoomImage.toBoolean();
			zCoefficient= NumericalValueConverters.toDouble(numericalZoomingCoefficient);
		};
		nativeImage= DataFrameTools.rotateAugmentAndZoomImage(
			nativeImage,
			terahertzDataBuffer.getData(),
			terahertzDataBuffer.getMatrixWidth(),
			terahertzDataBuffer.getMatrixHeight(),
			terahertzDataBuffer.getImageWidth(),
			terahertzDataBuffer.getImageHeight(),
			terahertzDataBuffer.getHorizontalOffset(),
			terahertzDataBuffer.getVerticalOffset(),
			mainColorMap,
			auxiliaryColorMap,
			isAutorangingMode,
			isDoubleColorMapMode,
			lowerDataBound,
			upperDataBound,
			lowerDataQuantile1,
			upperDataQuantile1,
			lowerDataQuantile2,
			upperDataQuantile2,
			zoomIt,
			zCoefficient);
		modifyImage(value,nativeImage,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentBorderedImage2s(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		updateAttributesIfNecessary(iX);
		Color backgroundColor;
		try {
			backgroundColor= ExtendedColor.argumentToColor(a2,iX);
		} catch (TermIsSymbolDefault e) {
			backgroundColor= null;
		};
		getRecentBorderedImage(a1,backgroundColor,iX);
	}
	public void getRecentBorderedImage1s(ChoisePoint iX, Term a1) throws Backtracking {
		updateAttributesIfNecessary(iX);
		getRecentBorderedImage(a1,null,iX);
	}
	//
	protected void getRecentBorderedImage(Term a1, Color backgroundColor, ChoisePoint iX) throws Backtracking {
		TerahertzDataBuffer terahertzDataBuffer;
		DataFrameBaseAttributesInterface terahertzAttributes;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedTHzDataFrame != null) {
					terahertzAttributes= committedTHzDataFrame.getBaseAttributes();
					terahertzDataBuffer= getTerahertzDataBuffer(terahertzAttributes);
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw new AstrohnFrameIsNotCommitted();
			}
		};
		YesNo doNotControlColorMaps= getUseRecordedColorMapCommands(iX);
		int[][] mainColorMap= prepareMainColorMap(doNotControlColorMaps.toBoolean(),terahertzAttributes);
		int[][] auxiliaryColorMap= prepareAuxiliaryColorMap(doNotControlColorMaps.toBoolean(),terahertzAttributes);
		boolean isAutorangingMode;
		boolean isDoubleColorMapMode;
		double lowerDataBound;
		double upperDataBound;
		double lowerDataQuantile1;
		double upperDataQuantile1;
		double lowerDataQuantile2;
		double upperDataQuantile2;
		if (actingDoNotControlTemperatureRange.get()) {
			isAutorangingMode= terahertzAttributes.isAutorangingMode();
			isDoubleColorMapMode= terahertzAttributes.isDoubleColorMapMode();
			lowerDataBound= terahertzAttributes.getLowerDataBound();
			upperDataBound= terahertzAttributes.getUpperDataBound();
			lowerDataQuantile1= terahertzAttributes.getLowerDataQuantile1();
			upperDataQuantile1= terahertzAttributes.getUpperDataQuantile1();
			lowerDataQuantile2= terahertzAttributes.getLowerDataQuantile2();
			upperDataQuantile2= terahertzAttributes.getUpperDataQuantile2();
		} else {
			isAutorangingMode= getAutorangingMode(iX).toBoolean();
			isDoubleColorMapMode= getDoubleColorMapMode(iX).toBoolean();
			lowerDataBound= NumericalValueConverters.toDouble(getLowerTemperatureBound(iX));
			upperDataBound= NumericalValueConverters.toDouble(getUpperTemperatureBound(iX));
			lowerDataQuantile1= NumericalValueConverters.toDouble(getLowerMainTemperatureQuantile(iX));
			upperDataQuantile1= NumericalValueConverters.toDouble(getUpperMainTemperatureQuantile(iX));
			lowerDataQuantile2= NumericalValueConverters.toDouble(getLowerAuxiliaryTemperatureQuantile(iX));
			upperDataQuantile2= NumericalValueConverters.toDouble(getUpperAuxiliaryTemperatureQuantile(iX));
		};
		YesNo doNotControlZooming= getUseRecordedZoomingCommands(iX);
		YesNo doZoomImage= getZoomImage(iX);
		NumericalValue numericalZoomingCoefficient= getZoomingCoefficient(iX);
		boolean zoomIt;
		double zCoefficient;
		if (doNotControlZooming.toBoolean()) {
			zoomIt= terahertzAttributes.isZoomingMode();
			zCoefficient= terahertzAttributes.getZoomingCoefficient();
		} else {
			zoomIt= doZoomImage.toBoolean();
			zCoefficient= NumericalValueConverters.toDouble(numericalZoomingCoefficient);
		};
		java.awt.image.BufferedImage nativeImage= DataFrameTools.borderAndZoomImage(
			terahertzDataBuffer.getData(),
			terahertzDataBuffer.getMatrixWidth(),
			terahertzDataBuffer.getMatrixHeight(),
			terahertzDataBuffer.getImageWidth(),
			terahertzDataBuffer.getImageHeight(),
			terahertzDataBuffer.getHorizontalOffset(),
			terahertzDataBuffer.getVerticalOffset(),
			mainColorMap,
			auxiliaryColorMap,
			isAutorangingMode,
			isDoubleColorMapMode,
			lowerDataBound,
			upperDataBound,
			lowerDataQuantile1,
			upperDataQuantile1,
			lowerDataQuantile2,
			upperDataQuantile2,
			zoomIt,
			zCoefficient,
			backgroundColor);
		modifyImage(a1,nativeImage,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getTemperature2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) throws Backtracking {
		int x= GeneralConverters.argumentToSmallInteger(a1,iX);
		int y= GeneralConverters.argumentToSmallInteger(a2,iX);
		TerahertzDataBuffer terahertzDataBuffer;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedTHzDataFrame != null) {
					DataFrameBaseAttributesInterface attributes= committedTHzDataFrame.getBaseAttributes();
					terahertzDataBuffer= getTerahertzDataBuffer(attributes);
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw new AstrohnFrameIsNotCommitted();
			}
		};
		double[] data= terahertzDataBuffer.getData();
		int columns= terahertzDataBuffer.getMatrixWidth();
		int pos0= y * columns + x;
		double targetTemperature= data[pos0];
		result.setNonBacktrackableValue(new PrologReal(targetTemperature));
	}
	public void getTemperature2fs(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
	}
	//
	public void getMinimalTemperature0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		TerahertzDataBuffer terahertzDataBuffer;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedTHzDataFrame != null) {
					DataFrameBaseAttributesInterface attributes= committedTHzDataFrame.getBaseAttributes();
					terahertzDataBuffer= getTerahertzDataBuffer(attributes);
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw new AstrohnFrameIsNotCommitted();
			}
		};
		double[] data= terahertzDataBuffer.getData();
		double minimum= data[0];
		for (int k=1; k < data.length; k++) {
			double value= data[k];
			if (minimum > value) {
				minimum= value;
			}
		};
		result.setNonBacktrackableValue(new PrologReal(minimum));
	}
	public void getMinimalTemperature0fs(ChoisePoint iX) throws Backtracking {
	}
	//
	public void getMaximalTemperature0ff(ChoisePoint iX, PrologVariable result) throws Backtracking {
		TerahertzDataBuffer terahertzDataBuffer;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedTHzDataFrame != null) {
					DataFrameBaseAttributesInterface attributes= committedTHzDataFrame.getBaseAttributes();
					terahertzDataBuffer= getTerahertzDataBuffer(attributes);
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw new AstrohnFrameIsNotCommitted();
			}
		};
		double[] data= terahertzDataBuffer.getData();
		double maximum= data[0];
		for (int k=1; k < data.length; k++) {
			double value= data[k];
			if (maximum < value) {
				maximum= value;
			}
		};
		result.setNonBacktrackableValue(new PrologReal(maximum));
	}
	public void getMaximalTemperature0fs(ChoisePoint iX) throws Backtracking {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getTerahertzImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		TVFilterImageHeader terahertzPacketBody= null;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedTHzDataFrame != null) {
					terahertzPacketBody= committedTHzDataFrame.getTHzData();
				}
			}
		};
		int columns= -1;
		int rows= -1;
		if (terahertzPacketBody != null) {
			columns= terahertzPacketBody.getColumns();
			rows= terahertzPacketBody.getRows();
		};
		a1.setBacktrackableValue(new PrologInteger(columns),iX);
		a2.setBacktrackableValue(new PrologInteger(rows),iX);
	}
	//
	public void getColorImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		java.awt.image.BufferedImage nativeColorImage= null;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				if (committedIPCameraFrame != null) {
					nativeColorImage= getNativeColorImage();
				}
			}
		};
		int width= -1;
		int height= -1;
		if (nativeColorImage != null) {
			// Note that the image is rotated:
			width= nativeColorImage.getHeight();
			height= nativeColorImage.getWidth();
		};
		a1.setBacktrackableValue(new PrologInteger(width),iX);
		a2.setBacktrackableValue(new PrologInteger(height),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setAstrohnData(TVFilterImageHeader packetBody, long time) {
		dataAcquisitionError.set(null);
		THzDataFrame frame= new THzDataFrame(
			counterOfAcquiredTerahertzFrames.incrementAndGet(),
			time,
			packetBody,
			recentAttributes.get());
		sendDataFrame(frame);
	}
	//
	public void setIPCameraData(byte[] array, long time) {
		dataAcquisitionError.set(null);
		IPCameraFrame frame1= new IPCameraFrame(
			counterOfAcquiredIPCameraFrames.incrementAndGet(),
			time,
			array,
			recentAttributes.get());
		int givenLength= colorCameraDelayQueueSize.get();
		IPCameraFrame frame2= null;
		synchronized (ipCameraQueue) {
			while (ipCameraQueue.size() >= givenLength) {
				frame2= ipCameraQueue.remove();
			};
			ipCameraQueue.add(frame1);
		};
		// ipCameraQueueTimer.schedule(
		//	new IPCameraTimerTask(this),
		//	ipCameraDelayInMilliseconds.get());
		// sendDataFrame(frame);
		if (frame2 != null) {
			sendDataFrame(frame2);
		}
	}
	// public void setAudioData(byte[] buffer, long time) {
	// }
	//
	public void sendFrameObtainedIfNecessary(DataFrameInterface frame) {
		if (actingSynchronizeTerahertzAndColorStreams.get()) {
			if (frame instanceof THzDataFrameInterface) {
				sendFrameObtained();
			}
		} else {
			sendFrameObtained();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected long updateRecentFrame(DataFrameInterface frame) {
		if (frame.isLightweightFrame()) {
			return -1;
		};
		synchronized (numberOfRecentReceivedFrame) {
			if (frame instanceof THzDataFrameInterface) {
				recentTHzDataFrame= (THzDataFrameInterface)frame;
				// numberOfRecentTerahertzFrame.set(recentTHzDataFrame.getSerialNumber());
				numberOfRecentTerahertzFrame.incrementAndGet();
				synchronousIPCameraFrame= recentIPCameraFrame;
				numberOfSynchronousIPCameraFrame.set(numberOfRecentIPCameraFrame.get());
				updateHistory(recentTHzDataFrame,synchronousIPCameraFrame);
				updateCumulativeTemperatures(recentTHzDataFrame);
				acceptedTerahertzFrameNumber++;
				acceptedTerahertzFrameTime= frame.getTime();
				if (firstAcceptedTerahertzFrameNumber < 0) {
					firstAcceptedTerahertzFrameNumber= acceptedTerahertzFrameNumber;
					firstAcceptedTerahertzFrameTime= acceptedTerahertzFrameTime;
				}
			} else if (frame instanceof IPCameraFrameInterface) {
				recentIPCameraFrame= (IPCameraFrameInterface)frame;
				// numberOfRecentIPCameraFrame.set(recentIPCameraFrame.getSerialNumber());
				numberOfRecentIPCameraFrame.incrementAndGet();
				if (synchronousIPCameraFrame==null) {
					synchronousIPCameraFrame= recentIPCameraFrame;
					numberOfSynchronousIPCameraFrame.set(numberOfRecentIPCameraFrame.get());
					updateHistory(recentTHzDataFrame,synchronousIPCameraFrame);
				};
				acceptedColorFrameNumber++;
				acceptedColorFrameTime= frame.getTime();
				if (firstAcceptedColorFrameNumber < 0) {
					firstAcceptedColorFrameNumber= acceptedColorFrameNumber;
					firstAcceptedColorFrameTime= acceptedColorFrameTime;
				}
			} else {
				return -1;
			};
			recentFrame= frame;
			committedFrameWasAssignedDirectly.set(false);
			long currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			recentFrameIsRepeated= false;
			numberOfRepeatedTerahertzFrame= -1;
			numberOfRepeatedColorFrame= -1;
			numberOfRecentReceivedFrame.notifyAll();
			return currentFrameNumber;
		}
	}
	//
	protected void updateHistory(THzDataFrameInterface recentTHzDataFrame, IPCameraFrameInterface synchronousIPCameraFrame) {
		if (recentTHzDataFrame==null || synchronousIPCameraFrame==null) {
			return;
		};
		updateHistory(new SynchronizedFrames(
				recentTHzDataFrame,
				numberOfRecentTerahertzFrame.get(),
				synchronousIPCameraFrame,
				numberOfSynchronousIPCameraFrame.get()));
	}
	//
	protected void updateCumulativeTemperatures(THzDataFrameInterface frame) {
		boolean isAveragingMode;
		if (actingDoNotControlTemperatureRange.get()) {
			DataFrameBaseAttributesInterface attributes= frame.getBaseAttributes();
			isAveragingMode= attributes.isAverageMode();
		} else {
			isAveragingMode= actingAveragingMode.get();
		};
		if (isAveragingMode) {
			numberOfAveragedFrames++;
			TVFilterImageHeader terahertzPacketBody= recentTHzDataFrame.getTHzData();
			double[] targetTemperatures= AstrohnDataAcquisition.tvFilterImageHeaderToDouble(terahertzPacketBody);
			if (cumulativeTemperatures==null) {
				cumulativeTemperatures= Arrays.copyOf(targetTemperatures,targetTemperatures.length);
			} else {
				for (int k=0; k < cumulativeTemperatures.length; k++) {
					cumulativeTemperatures[k]+= targetTemperatures[k];
				}
			}
		} else {
			resetCumulativeTemperatures();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void acceptRequestedFrame(EnumeratedFrame enumeratedFrame) {
		SynchronizedFrames synchronizedFrames= (SynchronizedFrames)enumeratedFrame;
		THzDataFrameInterface terahertzFrame= synchronizedFrames.getTerahertzFrame();
		IPCameraFrameInterface colorFrame= synchronizedFrames.getColorFrame();
		recentFrame= terahertzFrame;
		committedFrameWasAssignedDirectly.set(false);
		recentTHzDataFrame= terahertzFrame;
		recentIPCameraFrame= colorFrame;
		synchronousIPCameraFrame= colorFrame;
		recentFrameIsRepeated= true;
		numberOfRepeatedTerahertzFrame= synchronizedFrames.getNumberOfTerahertzFrame();
		numberOfRepeatedColorFrame= synchronizedFrames.getNumberOfColorFrame();
	}
	//
	protected void acceptRetrievedFrame(EnumeratedFrame enumeratedFrame) {
		SynchronizedFrames selectedPair= (SynchronizedFrames)enumeratedFrame;
		THzDataFrameInterface terahertzFrame= selectedPair.getTerahertzFrame();
		IPCameraFrameInterface colorFrame= selectedPair.getColorFrame();
		committedFrame= terahertzFrame;
		committedFrameWasAssignedDirectly.set(true);
		committedTHzDataFrame= terahertzFrame;
		committedIPCameraFrame= colorFrame;
		committedIPCameraFrameImage= null;
		committedTerahertzFrameNumber= selectedPair.getNumberOfTerahertzFrame();
		committedColorFrameNumber= selectedPair.getNumberOfColorFrame();
		updateCommittedFrameTime();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractFrame(String key, CompoundFrameInterface container) {
		if (committedFrame != null) {
			SynchronizedFrames synchronizedFrames= new SynchronizedFrames(
				committedTHzDataFrame,
				committedTerahertzFrameNumber,
				committedIPCameraFrame,
				committedColorFrameNumber);
			container.insertComponent(key,synchronizedFrames);
		} else {
			throw new AstrohnFrameIsNotCommitted();
		}
	}
	//
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		boolean mode= getSynchronizeTerahertzAndColorStreams(iX).toBoolean();
		actingSynchronizeTerahertzAndColorStreams.set(mode);
		SynchronizedFrames synchronizedFrames= (SynchronizedFrames)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			committedTHzDataFrame= synchronizedFrames.getTerahertzFrame();
			committedFrame= committedTHzDataFrame;
			committedFrameWasAssignedDirectly.set(true);
			committedIPCameraFrame= synchronizedFrames.getColorFrame();
			committedIPCameraFrameImage= null;
			// committedTerahertzFrameNumber= synchronizedFrames.getNumberOfTerahertzFrame();
			committedTerahertzFrameNumber= numberOfRecentTerahertzFrame.incrementAndGet();
			// committedColorFrameNumber= synchronizedFrames.getNumberOfColorFrame();
			committedColorFrameNumber= numberOfRecentIPCameraFrame.incrementAndGet();
			updateCumulativeTemperatures(committedTHzDataFrame);
			updateCommittedFrameTime();
		}
	}
}
