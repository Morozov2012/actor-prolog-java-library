// (c) 2015-2019 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.frames.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.errors.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.gui.*;
import morozov.system.gui.space2d.*;
import morozov.system.modes.*;
import morozov.system.signals.*;
import morozov.system.webcam.*;
import morozov.system.webcam.converters.*;
import morozov.system.webcam.errors.*;
import morozov.system.webcam.interfaces.*;
import morozov.terms.*;
import morozov.worlds.*;

import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamEvent;

import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.TimeoutException;

public abstract class Webcam
		extends ZoomDataAcquisitionBuffer
		implements
			WebcamDiscoveryListener,
			WebcamDataConsumerInterface {
	//
	public WebcamName defaultName= null;
	//
	protected WebcamAcquisitionTask webcamAcquisitionTask= new WebcamAcquisitionTask(this);
	//
	protected com.github.sarxos.webcam.Webcam currentWebcam= null;
	protected boolean isConnected= false;
	protected boolean isWebcamArrayListener= false;
	//
	protected AtomicLong counterOfAcquiredFrames= new AtomicLong(-1);
	//
	protected long numberOfRepeatedFrame= -1;
	//
	protected int initialValueOfFirstFrameIndicator= -3;
	//
	protected long committedFrameNumber= -1;
	protected long committedFrameTime= -1;
	protected long firstCommittedFrameNumber= initialValueOfFirstFrameIndicator;
	protected long firstCommittedFrameTime= initialValueOfFirstFrameIndicator;
	//
	protected static WaitingInterval defaultWaitingInterval= new WaitingInterval(true,false);
	protected static long longDefaultInterval= 1000 * 60;	// 1 minute
	protected static long longAnyInterval= Long.MAX_VALUE;
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_default_name();
	//
	abstract public long entry_s_WebcamAdded_1_i();
	abstract public long entry_s_WebcamRemoved_1_i();
	abstract public long entry_s_WebcamStarted_1_i();
	abstract public long entry_s_WebcamStopped_1_i();
	abstract public long entry_s_WebcamDisposed_1_i();
	//
	///////////////////////////////////////////////////////////////
	//
	public Webcam() {
		super(	new WebcamDataReadingTask(),
			new DataFrameRecordingTask());
	}
	public Webcam(GlobalWorldIdentifier id) {
		super(	id,
			new WebcamDataReadingTask(),
			new DataFrameRecordingTask());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set defaultName
	//
	public void setDefaultName1s(ChoisePoint iX, Term a1) {
		setDefaultName(WebcamName.argumentToWebcamName(a1,iX));
	}
	public void setDefaultName(WebcamName value) {
		defaultName= value;
	}
	public void getDefaultName0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getDefaultName(iX).toTerm());
	}
	public void getDefaultName0fs(ChoisePoint iX) {
	}
	public WebcamName getDefaultName(ChoisePoint iX) {
		if (defaultName != null) {
			return defaultName;
		} else {
			Term value= getBuiltInSlot_E_default_name();
			return WebcamName.argumentToWebcamName(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getActualName0ff(ChoisePoint iX, PrologVariable result) {
		if (isConnected) {
			if (currentWebcam != null) {
				result.setNonBacktrackableValue(new PrologString(currentWebcam.getName()));
			} else {
				throw new WebcamIsNotConnected();
			}
		} else {
			throw new WebcamIsNotConnected();
		}
	}
	public void getActualName0fs(ChoisePoint iX) {
		if (isConnected) {
			if (currentWebcam == null) {
				throw new WebcamIsNotConnected();
			}
		} else {
			throw new WebcamIsNotConnected();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set Default Resolution
	//
	public void setDefaultResolution1s(ChoisePoint iX, Term a1) {
		WebcamResolution resolution= WebcamResolution.argumentToWebcamResolution(a1,iX);
		setWidth(resolution.getExtendedWidth());
		setHeight(resolution.getExtendedHeight());
	}
	public void setDefaultResolution2s(ChoisePoint iX, Term a1, Term a2) {
		setWidth(ExtendedSize.argumentToExtendedSize(a1,iX));
		setHeight(ExtendedSize.argumentToExtendedSize(a2,iX));
	}
	public void getDefaultResolution2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		a1.setBacktrackableValue(getWidth(iX).toTerm(),iX);
		a2.setBacktrackableValue(getHeight(iX).toTerm(),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setActualResolution1s(ChoisePoint iX, Term a1) {
		WebcamResolution resolution= WebcamResolution.argumentToWebcamResolution(a1,iX);
		setActualResolution(resolution.getWidth(),resolution.getHeight());
	}
	public void setActualResolution2s(ChoisePoint iX, Term a1, Term a2) {
		int viewWidth= GeneralConverters.argumentToSmallInteger(a1,iX);
		int viewHeight= GeneralConverters.argumentToSmallInteger(a2,iX);
		setActualResolution(viewWidth,viewHeight);
	}
	//
	protected void setActualResolution(int viewWidth, int viewHeight) {
		if (isConnected) {
			if (currentWebcam != null) {
				Dimension viewSize= new Dimension(viewWidth,viewHeight);
				isConnected= false;
				currentWebcam.close();
				currentWebcam.setCustomViewSizes(new Dimension[]{viewSize});
				currentWebcam.setViewSize(viewSize);
				boolean result= currentWebcam.open(true);
				if (result) {
					isConnected= true;
				} else {
					currentWebcam= null;
					webcamAcquisitionTask.setCurrentWebcam(null);
				}
			} else {
				throw new WebcamIsNotConnected();
			}
		} else {
			throw new WebcamIsNotConnected();
		}
	}
	//
	public void getActualResolution2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (isConnected && currentWebcam != null) {
			Dimension size= currentWebcam.getViewSize();
			a1.setBacktrackableValue(new PrologInteger(size.width),iX);
			a2.setBacktrackableValue(new PrologInteger(size.height),iX);
		} else {
			int currentWidth= -1;
			int currentHeight= -1;
			if (committedFrame != null && committedFrame instanceof RGBFrame) {
				RGBFrame rgbFrame= (RGBFrame)committedFrame;
				currentWidth= rgbFrame.getWidth();
				currentHeight= rgbFrame.getHeight();
			};
			a1.setBacktrackableValue(new PrologInteger(currentWidth),iX);
			a2.setBacktrackableValue(new PrologInteger(currentHeight),iX);
		}
	}
	//
	public void getViewSizes0ff(ChoisePoint iX, PrologVariable result) {
		if (isConnected) {
			if (currentWebcam != null) {
				Dimension[] sizes= currentWebcam.getViewSizes();
				result.setNonBacktrackableValue(GeneralConverters.dimensionArrayToList(sizes));
			} else {
				throw new WebcamIsNotConnected();
			}
		} else {
			throw new WebcamIsNotConnected();
		}
	}
	public void getViewSizes0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getWebcamList0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getWebcamList(defaultWaitingInterval));
	}
	public void getWebcamList0fs(ChoisePoint iX) {
	}
	public void getWebcamList1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		WaitingInterval interval= WaitingIntervalConverters.argumentToWaitingInterval(a1,iX);
		result.setNonBacktrackableValue(getWebcamList(interval));
	}
	public void getWebcamList1fs(ChoisePoint iX, Term a1) {
	}
	//
	protected Term getWebcamList(WaitingInterval interval) {
		try {
			List<com.github.sarxos.webcam.Webcam> webcams;
			try {
				long timeout= interval.toMillisecondsLongOrDefault();
				webcams= com.github.sarxos.webcam.Webcam.getWebcams(timeout);
			} catch (UseDefaultInterval e) {
				webcams= com.github.sarxos.webcam.Webcam.getWebcams(longDefaultInterval);
			} catch (UseAnyInterval e) {
				webcams= com.github.sarxos.webcam.Webcam.getWebcams(longAnyInterval);
			};
			ArrayList<String> names= new ArrayList<>();
			for (com.github.sarxos.webcam.Webcam webcam : webcams) {
				names.add(webcam.getName());
			};
			Term result= GeneralConverters.stringArrayToList(names);
			return result;
		} catch (TimeoutException e) {
			throw new WebcamTimeoutException();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void watchWebcamList1s(ChoisePoint iX, PrologVariable a1) {
		if (isWebcamArrayListener) {
			a1.setBacktrackableValue(termYes,iX);
		} else {
			a1.setBacktrackableValue(termNo,iX);
		}
	}
	public void watchWebcamList1s(ChoisePoint iX, Term a1) {
		YesNo mode= YesNoConverters.argument2YesNo(a1,iX);
		if (mode==YesNo.YES) {
			if (!isWebcamArrayListener) {
				com.github.sarxos.webcam.Webcam.getWebcams();
				com.github.sarxos.webcam.Webcam.addDiscoveryListener(this);
				isWebcamArrayListener= true;
			}
		} else {
			if (isWebcamArrayListener) {
				com.github.sarxos.webcam.Webcam.removeDiscoveryListener(this);
				isWebcamArrayListener= false;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void webcamFound(WebcamDiscoveryEvent event) {
		long domainSignature= entry_s_WebcamAdded_1_i();
		processWebcamDiscoveryEvent(domainSignature,event);
	}
	@Override
	public void webcamGone(WebcamDiscoveryEvent event) {
		long domainSignature= entry_s_WebcamRemoved_1_i();
		processWebcamDiscoveryEvent(domainSignature,event);
	}
	//
	protected void processWebcamDiscoveryEvent(long domainSignature, WebcamDiscoveryEvent event) {
		Term predicateArgument= new PrologString(event.getWebcam().getName());
		Term[] arguments= new Term[]{predicateArgument};
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void webcamAdded1s(ChoisePoint iX, Term a1) {
	}
	//
	public void webcamRemoved1s(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void webcamOpen(WebcamEvent event) {
		long domainSignature= entry_s_WebcamStarted_1_i();
		processWebcamEvent(domainSignature,event);
	}
	@Override
	public void webcamImageObtained(java.awt.image.BufferedImage nativeImage) {
		if (nativeImage==null) {
			return;
		};
		DataAcquisitionBufferOperatingMode actingOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (actingOperatingMode==null) {
			return;
		};
		if (	actingOperatingMode==DataAcquisitionBufferOperatingMode.LISTENING ||
			actingOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
			RGBFrame frame= new RGBFrame(
				nativeImage,
				convertImageToBytes(nativeImage),
				counterOfAcquiredFrames.incrementAndGet(),
				System.currentTimeMillis(),
				recentAttributes.get());
			flushAudioSystemIfNecessary();
			sendDataFrame(frame);
		}
	}
	@Override
	public void webcamClosed(WebcamEvent event) {
		long domainSignature= entry_s_WebcamStopped_1_i();
		processWebcamEvent(domainSignature,event);
	}
	@Override
	public void webcamDisposed(WebcamEvent event) {
		long domainSignature= entry_s_WebcamDisposed_1_i();
		processWebcamEvent(domainSignature,event);
	}
	//
	protected void processWebcamEvent(long domainSignature, WebcamEvent event) {
		Term predicateArgument= new PrologString(event.getSource().getName());
		Term[] arguments= new Term[]{predicateArgument};
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void webcamStarted1s(ChoisePoint iX, Term a1) {
	}
	public void webcamStopped1s(ChoisePoint iX, Term a1) {
	}
	public void webcamDisposed1s(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			counterOfAcquiredFrames.set(-1);
			numberOfRepeatedFrame= -1;
		}
	}
	//
	@Override
	protected void resetFrameRate() {
		super.resetFrameRate();
		committedFrameNumber= -1;
		committedFrameTime= -1;
		firstCommittedFrameNumber= initialValueOfFirstFrameIndicator;
		firstCommittedFrameTime= initialValueOfFirstFrameIndicator;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void start0s(ChoisePoint iX) {
		WebcamName webcamName= getDefaultName(iX);
		start(webcamName,defaultWaitingInterval,iX);
	}
	public void start1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		WebcamName webcamName= WebcamName.argumentToWebcamName(a1,iX);
		boolean value= start(webcamName,defaultWaitingInterval,iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void start1fs(ChoisePoint iX, Term a1) {
		WebcamName webcamName= WebcamName.argumentToWebcamName(a1,iX);
		start(webcamName,defaultWaitingInterval,iX);
	}
	public void start2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		WebcamName webcamName= WebcamName.argumentToWebcamName(a1,iX);
		WaitingInterval interval= WaitingIntervalConverters.argumentToWaitingInterval(a2,iX);
		boolean value= start(webcamName,interval,iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void start2fs(ChoisePoint iX, Term a1, Term a2) {
		WebcamName webcamName= WebcamName.argumentToWebcamName(a1,iX);
		WaitingInterval interval= WaitingIntervalConverters.argumentToWaitingInterval(a2,iX);
		start(webcamName,interval,iX);
	}
	//
	protected boolean start(WebcamName webcamName, WaitingInterval interval, ChoisePoint iX) {
		DataAcquisitionBufferOperatingMode actingOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (actingOperatingMode==null) {
			resetCounters();
			updateAttributes(iX);
			DataAcquisitionBufferOperatingMode currentOperatingMode= getOperatingMode(iX);
			if (currentOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
				actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
				try {
					return startRecording(webcamName,interval,currentOperatingMode,iX);
				} catch (Throwable e) {
					resetActingMode();
					throw e;
				}
			} else if (currentOperatingMode==DataAcquisitionBufferOperatingMode.LISTENING) {
				actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
				try {
					return prepareAndActivateListening(webcamName,interval,true,iX);
				} catch (Throwable e) {
					resetActingMode();
					throw e;
				}
			}
		} else if (actingOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
			return startRecording(webcamName,interval,actingOperatingMode,iX);
		} else if (actingOperatingMode==DataAcquisitionBufferOperatingMode.LISTENING) {
			return prepareAndActivateListening(webcamName,interval,false,iX);
		};
		super.start(false,iX);
		return true;
	}
	//
	protected boolean startRecording(WebcamName webcamName, WaitingInterval interval, DataAcquisitionBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		if (isConnected) {
			return true;
		};
		int currentWriteBufferSize= getWriteBufferSize(iX);
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
		frameRecordingTask.setOutputDebugInformation(currentOutputDebugInformation);
		frameRecordingTask.reset(currentFileName);
		setActingMetadata(iX);
		super.startRecording(currentOperatingMode,false,iX);
		return checkAndActivateWebcam(webcamName,interval,iX);
	}
	//
	protected boolean prepareAndActivateListening(WebcamName webcamName, WaitingInterval interval, boolean flushBuffers, ChoisePoint iX) {
		int currentReadBufferSize= getReadBufferSize(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		actingReadBufferSize.set(currentReadBufferSize);
		webcamAcquisitionTask.setOutputDebugInformation(currentOutputDebugInformation);
		super.activateDataAcquisition(flushBuffers,iX);
		return checkAndActivateWebcam(webcamName,interval,iX);
	}
	//
	protected boolean checkAndActivateWebcam(WebcamName webcamName, WaitingInterval interval, ChoisePoint iX) {
		if (isConnected) {
			return true;
		};
		ExtendedSize eW= getWidth(iX);
		ExtendedSize eH= getHeight(iX);
		try {
			try {
				String textName= webcamName.getTextName();
				List<com.github.sarxos.webcam.Webcam> webcams;
				try {
					long timeout= interval.toMillisecondsLongOrDefault();
					webcams= com.github.sarxos.webcam.Webcam.getWebcams(timeout);
				} catch (UseDefaultInterval e) {
					webcams= com.github.sarxos.webcam.Webcam.getWebcams(longDefaultInterval);
				} catch (UseAnyInterval e) {
					webcams= com.github.sarxos.webcam.Webcam.getWebcams(longAnyInterval);
				};
				boolean nameIsFound= false;
				for (com.github.sarxos.webcam.Webcam webcam : webcams) {
					if (textName.equals(webcam.getName())) {
						currentWebcam= webcam;
						webcamAcquisitionTask.setCurrentWebcam(currentWebcam);
						nameIsFound= true;
						break;
					}
				};
				if (!nameIsFound) {
					throw new WebcamIsNotFound(textName);
				}
			} catch (UseDefaultName e1) {
				try {
					long timeout= interval.toMillisecondsLongOrDefault();
					currentWebcam= com.github.sarxos.webcam.Webcam.getDefault(timeout);
					webcamAcquisitionTask.setCurrentWebcam(currentWebcam);
				} catch (UseDefaultInterval e) {
					currentWebcam= com.github.sarxos.webcam.Webcam.getDefault(longDefaultInterval);
					webcamAcquisitionTask.setCurrentWebcam(currentWebcam);
				} catch (UseAnyInterval e) {
					currentWebcam= com.github.sarxos.webcam.Webcam.getDefault(longAnyInterval);
					webcamAcquisitionTask.setCurrentWebcam(currentWebcam);
				};
				if (currentWebcam==null) {
					throw new DefaultWebcamIsNotFound();
				}
			};
			if (currentWebcam==null) {
				throw new WebcamIsNotFound(webcamName.toString());
			};
			WebcamListener[] webcamListeners= currentWebcam.getWebcamListeners();
			boolean isElement= false;
			for (int k=0; k < webcamListeners.length; k++) {
				if (webcamListeners[k].equals(this)) {
					isElement= true;
					break;
				}
			};
			if (!isElement) {
				currentWebcam.addWebcamListener(webcamAcquisitionTask);
			};
			try {
				int viewWidth= eW.getIntegerValue();
				int viewHeight= eH.getIntegerValue();
				Dimension viewSize= new Dimension(viewWidth,viewHeight);
				currentWebcam.setCustomViewSizes(new Dimension[]{viewSize});
				currentWebcam.setViewSize(viewSize);
			} catch(UseDefaultSize e) {
			};
			boolean result= currentWebcam.open(true);
			if (!result) {
				currentWebcam= null;
				webcamAcquisitionTask.setCurrentWebcam(null);
			};
			isConnected= result;
			return result;
		} catch (TimeoutException e) {
			currentWebcam= null;
			webcamAcquisitionTask.setCurrentWebcam(null);
			throw new WebcamTimeoutException();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void activateDataAcquisition(boolean flushBuffers, ChoisePoint iX) {
		WebcamName webcamName= getDefaultName(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		super.activateDataAcquisition(flushBuffers,iX);
		checkAndActivateWebcam(webcamName,defaultWaitingInterval,iX);
	}
	//
	@Override
	protected void suspendRecording(ChoisePoint iX) {
		stopWebcam(iX);
		super.suspendRecording(iX);
	}
	@Override
	protected void suspendListening(ChoisePoint iX) {
		stopWebcam(iX);
		super.suspendListening(iX);
	}
	//
	@Override
	protected void stopRecording(ChoisePoint iX) {
		stopWebcam(iX);
		super.stopRecording(iX);
	}
	@Override
	protected void stopListening(ChoisePoint iX) {
		stopWebcam(iX);
		super.stopListening(iX);
	}
	//
	protected void stopWebcam(ChoisePoint iX) {
		if (isConnected) {
			if (currentWebcam != null) {
				currentWebcam.close();
				currentWebcam= null;
				webcamAcquisitionTask.setCurrentWebcam(null);
			};
			isConnected= false;
		}
	}
	//
	@Override
	protected boolean dataAcquisitionIsActive() {
		if (isConnected) {
			if (currentWebcam != null) {
				return currentWebcam.isOpen();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	//
	@Override
	protected boolean dataAcquisitionIsSuspended() {
		return !dataAcquisitionIsActive();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void commit() {
		synchronized (numberOfRecentReceivedFrame) {
			super.commit();
			committedFrame= recentFrame;
			if (!recentFrameIsRepeated) {
				committedFrameNumber= numberOfRecentReceivedFrame.get();
			} else {
				committedFrameNumber= numberOfRepeatedFrame;
			};
			updateCommittedFrameTime();
		}
	}
	//
	protected void updateCommittedFrameTime() {
		if (committedFrame != null) {
			committedFrameTime= committedFrame.getTime();
		} else {
			committedFrameTime= -1;
		};
		if (firstCommittedFrameTime < 0) {
			if (firstCommittedFrameTime == -1) {
				firstCommittedFrameNumber= committedFrameNumber;
				firstCommittedFrameTime= committedFrameTime;
			} else {
				firstCommittedFrameTime++;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		long frameNumber;
		synchronized (numberOfRecentReceivedFrame) {
			frameNumber= committedFrameNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(frameNumber),iX);
	}
	//
	public void getRecentFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		long frameTime;
		synchronized (numberOfRecentReceivedFrame) {
			frameTime= committedFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(frameTime),iX);
	}
	//
	public void getRecentFrameRelativeTime1s(ChoisePoint iX, PrologVariable a1) {
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaTime= committedFrameTime - firstCommittedFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaTime),iX);
	}
	//
	public void getRecentFrameRate1s(ChoisePoint iX, PrologVariable a1) {
		boolean canUseDelta;
		long deltaN;
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			canUseDelta= (firstCommittedFrameNumber >= 0);
			deltaN= committedFrameNumber - firstCommittedFrameNumber;
			deltaTime= committedFrameTime - firstCommittedFrameTime;
		};
		double currentFrameRate= computeFrameRate(canUseDelta,deltaN,deltaTime);
		a1.setBacktrackableValue(new PrologReal(currentFrameRate),iX);
	}
	//
	public void getCameraFrameRate1s(ChoisePoint iX, PrologVariable a1) {
		double currentFrameRate;
		DataAcquisitionBufferOperatingMode actingOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (	actingOperatingMode==DataAcquisitionBufferOperatingMode.LISTENING ||
			actingOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
			if (isConnected) {
				if (currentWebcam != null) {
					currentFrameRate= currentWebcam.getFPS();
				} else {
					throw new WebcamIsNotConnected();
				}
			} else {
				throw new WebcamIsNotConnected();
			};
		} else {
			long deltaN;
			long deltaTime;
			synchronized (numberOfRecentReceivedFrame) {
				deltaN= committedFrameNumber - firstCommittedFrameNumber;
				deltaTime= committedFrameTime - firstCommittedFrameTime;
			};
			currentFrameRate= computeFrameRate(deltaN,deltaTime);
		};
		a1.setBacktrackableValue(new PrologReal(currentFrameRate),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage1s(ChoisePoint iX, Term value) {
		updateAttributesIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage;
		DataFrameBaseAttributesInterface attributes;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				attributes= committedFrame.getBaseAttributes();
				nativeImage= getImage((RGBFrameInterface)committedFrame);
			} else {
				throw new DataFrameIsNotCommitted();
			}
		};
		if (nativeImage==null) {
			throw new DataFrameContainsNoImage();
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
		nativeImage= DataFrameTools.zoomImage(
			nativeImage,
			zoomIt,
			zCoefficient);
		modifyImage(value,nativeImage,iX);
	}
	//
	public java.awt.image.BufferedImage getImage(RGBFrameInterface frame) {
		java.awt.image.BufferedImage nativeImage= frame.getNativeImage();
		if (nativeImage==null) {
			nativeImage= Space2DWriter.bytesToImage(frame.getByteArray());
			frame.setNativeImage(nativeImage);
		};
		return nativeImage;
	}
	//
	public void getImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		java.awt.image.BufferedImage nativeImage= null;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				nativeImage= getImage((RGBFrameInterface)committedFrame);
			}
		};
		int currentWidth= -1;
		int currentHeight= -1;
		if (nativeImage != null) {
			currentWidth= nativeImage.getWidth();
			currentHeight= nativeImage.getHeight();
		};
		a1.setBacktrackableValue(new PrologInteger(currentWidth),iX);
		a2.setBacktrackableValue(new PrologInteger(currentHeight),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected long acceptFrame(DataFrameInterface frame, long currentFrameNumber) {
		if (frame instanceof RGBFrameInterface) {
			RGBFrameInterface colorFrame= (RGBFrameInterface)frame;
			currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateHistory(colorFrame);
		};
		currentFrameNumber= super.acceptFrame(frame,currentFrameNumber);
		if (currentFrameNumber >= 0) {
			numberOfRepeatedFrame= -1;
		};
		return currentFrameNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void acceptRequestedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedDataFrame selectedFrame= (EnumeratedDataFrame)enumeratedFrame;
		recentFrame= selectedFrame.getFrame();
		committedFrameWasAssignedDirectly.set(false);
		recentFrameIsRepeated= true;
		numberOfRepeatedFrame= selectedFrame.getNumberOfFrame();
	}
	//
	@Override
	protected void acceptRetrievedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedDataFrame selectedFrame= (EnumeratedDataFrame)enumeratedFrame;
		RGBFrameInterface colorFrame= (RGBFrameInterface)selectedFrame.getFrame();
		committedFrame= colorFrame;
		committedFrameWasAssignedDirectly.set(true);
		committedFrameNumber= selectedFrame.getNumberOfFrame();
		updateCommittedFrameTime();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void extractFrame(String key, CompoundFrameInterface container) {
		if (committedFrame != null) {
			EnumeratedDataFrame enumeratedFrame= new EnumeratedDataFrame(
				committedFrame,
				committedFrameNumber);
			container.insertComponent(key,enumeratedFrame);
		} else {
			throw new DataFrameIsNotCommitted();
		}
	}
	//
	@Override
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		EnumeratedDataFrame enumeratedFrame= (EnumeratedDataFrame)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			committedFrame= enumeratedFrame.getFrame();
			committedFrameWasAssignedDirectly.set(true);
			committedFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateCommittedFrameTime();
		}
	}
}
