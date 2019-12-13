// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.frames.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.errors.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.modes.*;
import morozov.system.modes.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class MultimediaBuffer extends ReadWriteBuffer {
	//
	protected MultimediaBufferOperatingMode operatingMode;
	//
	protected AtomicReference<MultimediaBufferOperatingMode> actingMultimediaBufferOperatingMode= new AtomicReference<>();
	//
	protected CompoundFrameInterface recentFrame;
	protected boolean recentFrameIsRepeated= false;
	//
	protected CompoundFrameInterface committedFrame;
	//
	protected AtomicLong counterOfNewCompoundFrames= new AtomicLong(-1);
	//
	protected long numberOfRepeatedFrame= -1;
	//
	protected long committedFrameNumber= -1;
	protected long committedFrameTime= -1;
	protected long firstCommittedFrameNumber= -1;
	protected long firstCommittedFrameTime= -1;
	//
	///////////////////////////////////////////////////////////////
	//
	public MultimediaBuffer() {
		super(	new MultimediaDataReadingTask(),
			new DataFrameRecordingTask());
	}
	public MultimediaBuffer(GlobalWorldIdentifier id) {
		super(	id,
			new MultimediaDataReadingTask(),
			new DataFrameRecordingTask());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_operating_mode();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set operating_mode
	//
	public void setOperatingMode1s(ChoisePoint iX, Term a1) {
		setOperatingMode(MultimediaBufferOperatingModeConverters.argumentToMultimediaBufferOperatingMode(a1,iX));
	}
	public void setOperatingMode(MultimediaBufferOperatingMode value) {
		operatingMode= value;
	}
	public void getOperatingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(MultimediaBufferOperatingModeConverters.toTerm(getOperatingMode(iX)));
	}
	public void getOperatingMode0fs(ChoisePoint iX) {
	}
	public MultimediaBufferOperatingMode getOperatingMode(ChoisePoint iX) {
		if (operatingMode != null) {
			return operatingMode;
		} else {
			Term value= getBuiltInSlot_E_operating_mode();
			return MultimediaBufferOperatingModeConverters.argumentToMultimediaBufferOperatingMode(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			counterOfNewCompoundFrames.set(-1);
			recentFrame= null;
			recentFrameIsRepeated= false;
			committedFrame= null;
			numberOfRepeatedFrame= -1;
		}
	}
	//
	@Override
	protected void resetFrameRate() {
		committedFrameNumber= -1;
		committedFrameTime= -1;
		firstCommittedFrameNumber= -1;
		firstCommittedFrameTime= -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void open0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		openFile(false,fileName,iX);
	}
	public void open1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		openFile(false,fileName,iX);
	}
	//
	@Override
	public void start0s(ChoisePoint iX) {
		ExtendedFileName fileName= retrieveRealLocalFileName(iX);
		openFile(true,fileName,iX);
	}
	//
	protected void openFile(boolean activateDataTransfer, ExtendedFileName currentFileName, ChoisePoint iX) {
		MultimediaBufferOperatingMode actingOperatingMode= actingMultimediaBufferOperatingMode.get();
		if (actingOperatingMode != null) {
			switch (actingOperatingMode) {
			case RECORDING:
				break;
			case PLAYING:
				frameReadingTask.setStopAfterSingleReading(false);
				frameReadingTask.setApplyAudioDataTiming(!synchronizeAudioStreamWithFrontVideoFrame());
				if (activateDataTransfer) {
					frameReadingTask.activateReading();
				};
				break;
			case READING:
				frameReadingTask.setStopAfterSingleReading(true);
				if (activateDataTransfer) {
					frameReadingTask.activateReading();
				};
				break;
			case SPECULATIVE_READING:
				frameReadingTask.setStopAfterSingleReading(true);
				break;
			}
		} else {
			resetCounters();
			MultimediaBufferOperatingMode currentOperatingMode= getOperatingMode(iX);
			switch (currentOperatingMode) {
			case RECORDING:
				startRecording(activateDataTransfer,currentFileName,currentOperatingMode,iX);
				break;
			case PLAYING:
				frameReadingTask.setStopAfterSingleReading(false);
				frameReadingTask.setApplyAudioDataTiming(!synchronizeAudioStreamWithFrontVideoFrame());
				startReadingOrPlaying(activateDataTransfer,currentFileName,currentOperatingMode,iX);
				break;
			case READING:
				frameReadingTask.setStopAfterSingleReading(true);
				startReadingOrPlaying(activateDataTransfer,currentFileName,currentOperatingMode,iX);
				break;
			case SPECULATIVE_READING:
				frameReadingTask.setStopAfterSingleReading(true);
				startReadingOrPlaying(false,currentFileName,currentOperatingMode,iX);
				frameReadingTask.readGivenNumberOfFrames(actingReadBufferSize.get());
				break;
			}
		}
	}
	//
	protected void startRecording(boolean activateDataTransfer, ExtendedFileName currentFileName, MultimediaBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		int currentWriteBufferSize= getWriteBufferSize(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
		frameRecordingTask.setOutputDebugInformation(currentOutputDebugInformation);
		frameRecordingTask.reset(currentFileName);
		if (activateDataTransfer) {
			setActingMetadata(iX);
			actingMultimediaBufferOperatingMode.set(currentOperatingMode);
		}
	}
	//
	protected void startReadingOrPlaying(boolean activateDataTransfer, ExtendedFileName currentFileName, MultimediaBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		int currentReadBufferSize= getReadBufferSize(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		IntegerAttribute currentMaximalFrameDelay= getMaximalFrameDelay(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		//
		actingReadBufferSize.set(currentReadBufferSize);
		actingMultimediaBufferOperatingMode.set(currentOperatingMode);
		try {
			frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
			frameReadingTask.setMaximalFrameDelay(currentMaximalFrameDelay);
			frameReadingTask.setDisplayingMode(null);
			frameReadingTask.setOutputDebugInformation(currentOutputDebugInformation);
			frameReadingTask.startReading(activateDataTransfer,currentFileName,currentTimeout,currentCharacterSet,staticContext);
		} catch (Throwable e) {
			actingMultimediaBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void close0s(ChoisePoint iX) {
		frameRecordingTask.close();
		frameReadingTask.closeReading();
		actingMultimediaBufferOperatingMode.set(null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void pause0s(ChoisePoint iX) {
		MultimediaBufferOperatingMode currentOperatingMode= actingMultimediaBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			frameReadingTask.suspendReading();
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void stop0s(ChoisePoint iX) {
		stop(iX);
	}
	//
	protected void stop(ChoisePoint iX) {
		MultimediaBufferOperatingMode currentOperatingMode= actingMultimediaBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			actingMultimediaBufferOperatingMode.set(null);
			frameRecordingTask.close();
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			actingMultimediaBufferOperatingMode.set(null);
			frameReadingTask.closeReading();
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isOpen0s(ChoisePoint iX) throws Backtracking {
		MultimediaBufferOperatingMode currentOperatingMode= actingMultimediaBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			if (!frameRecordingTask.outputIsOpen()) {
				throw Backtracking.instance;
			};
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (!frameReadingTask.inputIsOpen()) {
				throw Backtracking.instance;
			};
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		MultimediaBufferOperatingMode currentOperatingMode= actingMultimediaBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (!frameReadingTask.inputIsOpen()) {
				throw Backtracking.instance;
			} else if (frameReadingTask.stopThisThread()) {
				throw Backtracking.instance;
			};
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isSuspended0s(ChoisePoint iX) throws Backtracking {
		MultimediaBufferOperatingMode currentOperatingMode= actingMultimediaBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			throw Backtracking.instance;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (!frameReadingTask.isSuspended()) {
				throw Backtracking.instance;
			};
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void eof0s(ChoisePoint iX) throws Backtracking {
		MultimediaBufferOperatingMode currentOperatingMode= actingMultimediaBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			throw Backtracking.instance;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (!frameReadingTask.eof()) {
				throw Backtracking.instance;
			};
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void commit0s(ChoisePoint iX) throws Backtracking {
		if (committedFrameWasAssignedDirectly.get()) {
			if (committedFrame==null) {
				throw Backtracking.instance;
			}
		} else {
			synchronized (numberOfRecentReceivedFrame) {
				if (recentFrame==null) {
					throw Backtracking.instance;
				};
				commit();
			}
		}
	}
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
		long time;
		if (committedFrame != null) {
			time= committedFrame.getTime();
		} else {
			time= -1;
		};
		updateCommittedFrameTime(time);
	}
	protected void updateCommittedFrameTime(long time) {
		committedFrameTime= time;
		if (firstCommittedFrameTime < 0) {
			firstCommittedFrameNumber= committedFrameNumber;
			firstCommittedFrameTime= committedFrameTime;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isCommitted0s(ChoisePoint iX) throws Backtracking {
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrameIsNull()) {
				throw Backtracking.instance;
			}
		}
	}
	//
	protected boolean committedFrameIsNull() {
		return (committedFrame==null);
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
		long deltaN;
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaN= committedFrameNumber - firstCommittedFrameNumber;
			deltaTime= committedFrameTime - firstCommittedFrameTime;
		};
		double rate= computeFrameRate(deltaN,deltaTime);
		a1.setBacktrackableValue(new PrologReal(rate),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean sendDataFrame(DataFrameInterface frame) {
		return false;
	}
	//
	@Override
	public boolean sendKinectFrame(KinectFrameInterface frame) {
		return false;
	}
	//
	@Override
	public boolean sendCompoundFrame(CompoundFrameInterface frame) {
		MultimediaBufferOperatingMode currentOperatingMode= actingMultimediaBufferOperatingMode.get();
		CompoundArrayType arrayType= frame.getCompoundArrayType();
		if (currentOperatingMode != null && currentOperatingMode==MultimediaBufferOperatingMode.RECORDING) {
			long currentFrameNumber= updateRecentFrame(frame);
			recordCompoundFrame(frame,currentFrameNumber,CompoundArrayType.DATA_FRAME);
			sendFrameObtained();
		} else {
			if (arrayType==CompoundArrayType.DESCRIPTION_FRAME) {
				acceptSettings(frame);
			};
			updateRecentFrame(frame);
			sendFrameObtained();
		};
		if (arrayType==CompoundArrayType.CONTROL_FRAME) {
			frameReadingTask.suspendReading();
		};
		return true;
	}
	//
	public void recordCompoundFrame(CompoundFrameInterface frame, long currentFrameNumber, CompoundArrayType type) {
		CompoundArrayType arrayType= frame.getCompoundArrayType();
		if (arrayType != CompoundArrayType.DESCRIPTION_FRAME) {
			if (currentFrameNumber == 0) {
				DescriptionCompoundFrame modeFrame= createDescriptionCompoundFrame();
				frameRecordingTask.store(modeFrame);
			};
			frame.setCompoundArrayType(type);
		};
		frameRecordingTask.store(frame);
	}
	//
	protected DescriptionCompoundFrame createDescriptionCompoundFrame() {
		MetadataDescription metadataDescription= createMetadataDescription();
		DescriptionCompoundFrame modeFrame= new DescriptionCompoundFrame(
			-1,
			-1,
			metadataDescription.getDescription(),
			metadataDescription.getCopyright(),
			metadataDescription.getRegistrationDate(),
			metadataDescription.getRegistrationTime());
		return modeFrame;
	}
	//
	protected void acceptSettings(CompoundFrameInterface frame) {
		DescriptionCompoundFrame modeFrame= (DescriptionCompoundFrame)frame;
		deliveredDescription.set(modeFrame.getDescription());
		deliveredCopyright.set(modeFrame.getCopyright());
		deliveredRegistrationDate.set(modeFrame.getRegistrationDate());
		deliveredRegistrationTime.set(modeFrame.getRegistrationTime());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected long updateRecentFrame(CompoundFrameInterface frame) {
		if (frame.isLightweightFrame()) {
			return -1;
		};
		synchronized (numberOfRecentReceivedFrame) {
			long currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateHistory(frame);
			recentFrame= frame;
			committedFrameWasAssignedDirectly.set(false);
			recentFrameIsRepeated= false;
			numberOfRepeatedFrame= -1;
			numberOfRecentReceivedFrame.notifyAll();
			return currentFrameNumber;
		}
	}
	//
	protected void updateHistory(CompoundFrameInterface recentDataFrame) {
		if (recentDataFrame==null) {
			return;
		};
		updateHistory(new EnumeratedCompoundFrame(recentDataFrame,numberOfRecentReceivedFrame.get()));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void acceptRequestedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedCompoundFrame selectedFrame= (EnumeratedCompoundFrame)enumeratedFrame;
		recentFrame= selectedFrame.getFrame();
		committedFrameWasAssignedDirectly.set(false);
		recentFrameIsRepeated= true;
		numberOfRepeatedFrame= selectedFrame.getNumberOfFrame();
	}
	//
	@Override
	protected void acceptRetrievedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedCompoundFrame selectedFrame= (EnumeratedCompoundFrame)enumeratedFrame;
		CompoundFrameInterface compoundFrame= selectedFrame.getFrame();
		committedFrame= compoundFrame;
		committedFrameWasAssignedDirectly.set(true);
		committedFrameNumber= numberOfRepeatedFrame;
		updateCommittedFrameTime();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected boolean isSpeculativeReadingMode() {
		MultimediaBufferOperatingMode currentOperatingMode= actingMultimediaBufferOperatingMode.get();
		return (currentOperatingMode==MultimediaBufferOperatingMode.SPECULATIVE_READING);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void assignFrameTime1s(ChoisePoint iX, Term a1) {
		TimeInterval timeInterval= TimeIntervalConverters.argumentMillisecondsToTimeInterval(a1,iX);
		if (committedFrame==null) {
			committedFrameNumber= counterOfNewCompoundFrames.incrementAndGet();
			committedFrame= new CompoundFrame(
				committedFrameNumber,
				timeInterval.toMillisecondsLong()
				);
		} else {
			committedFrame.setTime(timeInterval.toMillisecondsLong());
		}
	}
	//
	public void insertDataFrame2s(ChoisePoint iX, Term a1, Term a2) {
		String key= a1.toString();
		ReadWriteBuffer source;
		a2= a2.dereferenceValue(iX);
		if (a2 instanceof ReadWriteBuffer) {
			source= (ReadWriteBuffer)a2;
		} else {
			throw new WrongArgumentIsNotReadWriteBuffer(a2);
		};
		if (committedFrame==null) {
			committedFrameNumber= counterOfNewCompoundFrames.incrementAndGet();
			long time= System.currentTimeMillis();
			committedFrame= new CompoundFrame(
				committedFrameNumber,
				time
				);
			updateCommittedFrameTime(time);
		};
		source.extractFrame(key,committedFrame);
	}
	//
	public void insertSettingsFrame2s(ChoisePoint iX, Term a1, Term a2) {
		String key= a1.toString();
		ReadWriteBuffer source;
		a2= a2.dereferenceValue(iX);
		if (a2 instanceof ReadWriteBuffer) {
			source= (ReadWriteBuffer)a2;
		} else {
			throw new WrongArgumentIsNotReadWriteBuffer(a2);
		};
		if (committedFrame==null) {
			committedFrameNumber= counterOfNewCompoundFrames.incrementAndGet();
			long time= System.currentTimeMillis();
			committedFrame= new CompoundFrame(
				committedFrameNumber,
				time);
			updateCommittedFrameTime(time);
		};
		source.extractSettings(key,committedFrame,iX);
	}
	//
	public void insertTerm2s(ChoisePoint iX, Term a1, Term a2) {
		String key= a1.toString();
		a2= a2.dereferenceValue(iX);
		if (committedFrame==null) {
			committedFrameNumber= counterOfNewCompoundFrames.incrementAndGet();
			long time= System.currentTimeMillis();
			committedFrame= new CompoundFrame(
				committedFrameNumber,
				time);
			updateCommittedFrameTime(time);
		};
		CompoundFrame compoundFrame= (CompoundFrame)committedFrame;
		compoundFrame.insertComponent(key,a2);
	}
	//
	public void insertLabel1s(ChoisePoint iX, Term a1) {
		String key= a1.toString();
		if (committedFrame==null) {
			committedFrameNumber= counterOfNewCompoundFrames.incrementAndGet();
			long time= System.currentTimeMillis();
			committedFrame= new CompoundFrame(
				committedFrameNumber,
				time);
			updateCommittedFrameTime(time);
		};
		CompoundFrame compoundFrame= (CompoundFrame)committedFrame;
		compoundFrame.insertComponent(key,PrologUnknownValue.instance);
	}
	//
	public void retrieveFrame2s(ChoisePoint iX, Term a1, Term a2) {
		String key= a1.toString();
		ReadWriteBuffer source;
		a2= a2.dereferenceValue(iX);
		if (a2 instanceof ReadWriteBuffer) {
			source= (ReadWriteBuffer)a2;
		} else {
			throw new WrongArgumentIsNotReadWriteBuffer(a2);
		};
		if (committedFrame==null) {
			throw new CompoundFrameIsNotCommitted();
		};
		source.assignFrame(key,committedFrame,iX);
	}
	//
	public void retrieveTerm1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		String key= a1.toString();
		if (committedFrame==null) {
			throw new CompoundFrameIsNotCommitted();
		};
		CompoundFrame compoundFrame= (CompoundFrame)committedFrame;
		Term term= (Term)compoundFrame.getComponent(key);
		result.setNonBacktrackableValue(term);
	}
	public void retrieveTerm1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void deleteComponent1s(ChoisePoint iX, Term a1) {
		String key= a1.toString();
		ReadWriteBuffer source;
		if (committedFrame != null) {
			committedFrame.deleteComponent(key);
		}
	}
	//
	public void hasComponent1s(ChoisePoint iX, Term a1) throws Backtracking {
		String key= a1.toString();
		ReadWriteBuffer source;
		if (committedFrame != null) {
			if (!committedFrame.hasComponent(key)) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void frameIsFormed0s(ChoisePoint iX) throws Backtracking {
		if (committedFrame==null) {
			throw Backtracking.instance;
		}
	}
	public void isDataFrame0s(ChoisePoint iX) throws Backtracking {
		if (committedFrame==null) {
			throw Backtracking.instance;
		} else {
			CompoundArrayType arrayType= committedFrame.getCompoundArrayType();
			if (arrayType != CompoundArrayType.DATA_FRAME) {
				throw Backtracking.instance;
			}
		}
	}
	public void isControlFrame0s(ChoisePoint iX) throws Backtracking {
		if (committedFrame==null) {
			throw Backtracking.instance;
		} else {
			CompoundArrayType arrayType= committedFrame.getCompoundArrayType();
			if (arrayType != CompoundArrayType.CONTROL_FRAME) {
				throw Backtracking.instance;
			}
		}
	}
	//
	public void recordDataFrame0s(ChoisePoint iX) {
		if (committedFrame != null) {
			recordCompoundFrame(committedFrame,committedFrameNumber,CompoundArrayType.DATA_FRAME);
			committedFrame= null;
			committedFrameWasAssignedDirectly.set(false);
		} else {
			throw new CompoundFrameIsNotFormed();
		}
	}
	public void recordControlFrame0s(ChoisePoint iX) {
		if (committedFrame != null) {
			recordCompoundFrame(committedFrame,committedFrameNumber,CompoundArrayType.CONTROL_FRAME);
			committedFrame= null;
			committedFrameWasAssignedDirectly.set(false);
		} else {
			throw new CompoundFrameIsNotFormed();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void extractFrame(String key, CompoundFrameInterface container) {
		if (committedFrame != null) {
			EnumeratedCompoundFrame enumeratedFrame= new EnumeratedCompoundFrame(
				committedFrame,
				committedFrameNumber);
			container.insertComponent(key,enumeratedFrame);
		} else {
			throw new CompoundFrameIsNotCommitted();
		}
	}
	//
	@Override
	public void extractSettings(String key, CompoundFrameInterface container, ChoisePoint iX) {
		setActingMetadata(iX);
		EnumeratedCompoundFrame enumeratedFrame= new EnumeratedCompoundFrame(
			createDescriptionCompoundFrame(),
			-1);
		container.insertComponent(key,enumeratedFrame);
	}
	//
	@Override
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		EnumeratedCompoundFrame enumeratedFrame= (EnumeratedCompoundFrame)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			CompoundFrameInterface frame= enumeratedFrame.getFrame();
			CompoundArrayType arrayType= frame.getCompoundArrayType();
			if (arrayType==CompoundArrayType.DESCRIPTION_FRAME) {
				acceptSettings(frame);
			} else {
				committedFrame= frame;
				committedFrameWasAssignedDirectly.set(true);
				committedFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
				updateCommittedFrameTime();
			}
		}
	}
}
