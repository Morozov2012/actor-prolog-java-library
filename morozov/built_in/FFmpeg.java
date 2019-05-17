// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import static org.bytedeco.javacpp.avutil.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.ffmpeg.*;
import morozov.system.ffmpeg.errors.*;
import morozov.system.ffmpeg.interfaces.*;
import morozov.system.files.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class FFmpeg extends ReadWriteBuffer implements FFmpegInterface {
	//
	protected FFmpegOperatingMode operatingMode;
	// protected NumericalValue slowMotionCoefficient;
	// protected IntegerAttribute maximalFrameDelay;
	//
	protected AtomicReference<FFmpegOperatingMode> actingFFmpegOperatingMode= new AtomicReference<>();
	// protected AtomicReference<FFmpegDataTransferError> dataReadingError= new AtomicReference<>();
	// protected AtomicReference<FFmpegDataTransferError> dataWritingError= new AtomicReference<>();
	//
	protected FFmpegFrameReadingTask frameReadingTask= new FFmpegFrameReadingTask(this);
	protected FFmpegFrameRecordingTask frameRecordingTask= new FFmpegFrameRecordingTask(this);
	protected FFmpegVideoSizeEstimator videoSizeEstimator= new FFmpegVideoSizeEstimator(this);
	//
	protected FFmpegFrame recentFrame;
	// protected AtomicLong numberOfRecentReceivedFrame= new AtomicLong(-1);
	//
	// protected AtomicBoolean containsNewFrame= new AtomicBoolean(false);
	//
	protected FFmpegFrameInterface committedFrame;
	protected long committedFrameNumber= -1;
	protected long committedFrameTime= -1;
	protected AVRational committedTimeBase= null;
	protected AVRational committedAverageFrameRate= null;
	// protected long committedBeginningTime= -1;
	protected long firstCommittedFrameNumber= -1;
	protected long firstCommittedFrameTime= -1;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpeg() {
	}
	public FFmpeg(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// abstract public long entry_s_FrameObtained_0();
	// abstract public long entry_s_DataTransferCompletion_0();
	// abstract public long entry_s_BufferOverflow_0();
	// abstract public long entry_s_BufferDeallocation_0();
	// abstract public long entry_s_DataTransferError_1_i();
	//
	abstract public Term getBuiltInSlot_E_operating_mode();
	// abstract public Term getBuiltInSlot_E_slow_motion_coefficient();
	// abstract public Term getBuiltInSlot_E_maximal_frame_delay();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set operating_mode
	//
	public void setOperatingMode1s(ChoisePoint iX, Term a1) {
		setOperatingMode(FFmpegOperatingMode.argumentToFFmpegOperatingMode(a1,iX));
	}
	public void setOperatingMode(FFmpegOperatingMode value) {
		operatingMode= value;
	}
	public void getOperatingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(FFmpegOperatingMode.toTerm(getOperatingMode(iX)));
	}
	public void getOperatingMode0fs(ChoisePoint iX) {
	}
	public FFmpegOperatingMode getOperatingMode(ChoisePoint iX) {
		if (operatingMode != null) {
			return operatingMode;
		} else {
			Term value= getBuiltInSlot_E_operating_mode();
			return FFmpegOperatingMode.argumentToFFmpegOperatingMode(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setWriteBufferSize(int value) {
		super.setWriteBufferSize(value);
		frameRecordingTask.setWriteBufferSize(value);
	}
	//
	public void setOutputDebugInformation(BigInteger value) {
		super.setOutputDebugInformation(value);
		int mode= PrologInteger.toInteger(value);
		frameRecordingTask.setOutputDebugInformation(mode);
	}
	//
	public void setSlowMotionCoefficient(NumericalValue value) {
		super.setSlowMotionCoefficient(value);
		frameReadingTask.setSlowMotionCoefficient(value);
	}
	//
	public void setMaximalFrameDelay(IntegerAttribute value) {
		super.setMaximalFrameDelay(value);
		frameReadingTask.setMaximalFrameDelay(value);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			recentFrame= null;
			committedFrame= null;
		}
	}
	//
	protected void resetFrameRate() {
		committedTimeBase= null;
		committedAverageFrameRate= null;
		committedFrameNumber= -1;
		committedFrameTime= -1;
		firstCommittedFrameNumber= -1;
		firstCommittedFrameTime= -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void open0s(ChoisePoint iX) {
		open(iX,null,null,null,null);
	}
	public void open1s(ChoisePoint iX, Term a1) {
		open(iX,null,null,a1,null);
	}
	public void open2s(ChoisePoint iX, Term a1, Term a2) {
		open(iX,a1,null,a2,null);
	}
	public void open3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		open(iX,a1,a2,a3,null);
	}
	public void open4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		open(iX,a1,a2,a3,a4);
	}
	//
	protected void open(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		FFmpegOperatingMode actingOperatingMode= actingFFmpegOperatingMode.get();
		if (actingOperatingMode != null) {
			switch (actingOperatingMode) {
			case PLAYING:
				frameReadingTask.setStopAfterSingleReading(false);
				// frameReadingTask.activateReading();
				break;
			case READING:
				frameReadingTask.setStopAfterSingleReading(true);
				// frameReadingTask.activateReading();
				break;
			case WRITING:
				break;
			}
		} else {
			ExtendedFileName currentFileName;
			if (a1 != null) {
				currentFileName= retrieveRealLocalFileName(a1,iX);
			} else {
				currentFileName= retrieveRealLocalFileName(iX);
			};
			String formatName= null;
			if (a2 != null) {
				formatName= GeneralConverters.argumentToString(a2,iX);
			};
			FFmpegStreamDefinition[] streams= FFmpegStreamDefinition.argumentToStreamDefinitions(a3,iX);
			FFmpegCodecOption[] options= null;
			if (a4 != null) {
				options= FFmpegCodecOption.argumentToCodecOptions(a4,iX);
			};
			resetCounters();
			FFmpegOperatingMode currentOperatingMode= getOperatingMode(iX);
			switch (currentOperatingMode) {
			case PLAYING:
				frameReadingTask.setStopAfterSingleReading(false);
				openReadingOrPlaying(currentOperatingMode,currentFileName,formatName,streams,options,iX);
				break;
			case READING:
				frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
				frameReadingTask.setStopAfterSingleReading(true);
				openReadingOrPlaying(currentOperatingMode,currentFileName,formatName,streams,options,iX);
				break;
			case WRITING:
				actingFFmpegOperatingMode.set(currentOperatingMode);
				setActingMetadata(iX);
				MetadataDescription metadataDescription= createMetadataDescription();
				frameRecordingTask.setWriteBufferSize(getWriteBufferSize(iX));
				frameRecordingTask.openWriting(currentFileName,formatName,streams,options,metadataDescription);
				break;
			}
		}
	}
	//
	protected void openReadingOrPlaying(FFmpegOperatingMode currentOperatingMode, ExtendedFileName currentFileName, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[] options, ChoisePoint iX) {
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		IntegerAttribute currentMaximalFrameDelay= getMaximalFrameDelay(iX);
		actingFFmpegOperatingMode.set(currentOperatingMode);
		frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
		frameReadingTask.setMaximalFrameDelay(currentMaximalFrameDelay);
		frameReadingTask.openReading(currentFileName,currentTimeout,currentCharacterSet,formatName,streams,options,staticContext);
	}
	//
	public void start0s(ChoisePoint iX) {
		FFmpegOperatingMode actingOperatingMode= actingFFmpegOperatingMode.get();
		if (actingOperatingMode==null) {
			open(iX,null,null,null,null);
		};
		actingOperatingMode= actingFFmpegOperatingMode.get();
		switch (actingOperatingMode) {
		case PLAYING:
			frameReadingTask.setStopAfterSingleReading(false);
			frameReadingTask.setSlowMotionCoefficient(getSlowMotionCoefficient(iX));
			frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
			frameReadingTask.activateReading();
			break;
		case READING:
			frameReadingTask.setStopAfterSingleReading(true);
			frameReadingTask.setSlowMotionCoefficient(getSlowMotionCoefficient(iX));
			frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
			frameReadingTask.activateReading();
			break;
		case WRITING:
			break;
		}
	}
	//
	public void pause0s(ChoisePoint iX) {
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case PLAYING:
		case READING:
			frameReadingTask.suspendReading();
			break;
		case WRITING:
			throw new FFmpegFilesDoNotSupportThisOperationInWritingMode();
		}
	}
	//
	public void seekFrameNumber1s(ChoisePoint iX, Term a1) {
		BigInteger position= GeneralConverters.argumentToStrictInteger(a1,iX);
		seekFrameNumber(position,iX);
	}
	protected void seekFrameNumber(BigInteger position, ChoisePoint iX) {
		long targetNumber= PrologInteger.toLong(position);
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			open(iX,null,null,null,null);
		};
		currentOperatingMode= actingFFmpegOperatingMode.get();
		switch (currentOperatingMode) {
		case PLAYING:
		case READING:
			frameReadingTask.seekFrameNumber(targetNumber);
			break;
		case WRITING:
			throw new FFmpegFilesDoNotSupportThisOperationInWritingMode();
		}
	}
	//
	public void seekFrameTime1s(ChoisePoint iX, Term a1) {
		double targetTime= GeneralConverters.argumentToReal(a1,iX);
		seekFrameTime(targetTime,iX);
	}
	protected void seekFrameTime(double targetTime, ChoisePoint iX) {
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			open(iX,null,null,null,null);
		};
		currentOperatingMode= actingFFmpegOperatingMode.get();
		switch (currentOperatingMode) {
		case PLAYING:
		case READING:
			frameReadingTask.seekFrameTime(targetTime);
			break;
		case WRITING:
			throw new FFmpegFilesDoNotSupportThisOperationInWritingMode();
		}
	}
	//
	public void requestBufferedFrame1s(ChoisePoint iX, Term a1) {
		BigInteger position= GeneralConverters.argumentToStrictInteger(a1,iX);
		seekFrameNumber(position,iX);
		frameReadingTask.setStopAfterSingleReading(true);
		frameReadingTask.setSlowMotionCoefficient(getSlowMotionCoefficient(iX));
		frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
	}
	//
	public void retrieveTimedFrame1s(ChoisePoint iX, Term a1) throws Backtracking {
		double targetTime= GeneralConverters.argumentToReal(a1,iX);
		seekFrameTime(targetTime,iX);
		frameReadingTask.setStopAfterSingleReading(true);
		frameReadingTask.setSlowMotionCoefficient(getSlowMotionCoefficient(iX));
		frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
		containsNewFrame.set(false);
		frameReadingTask.activateReading();
		while (!containsNewFrame.get() && frameReadingTask.inputIsOpen() && frameReadingTask.isActive()) {
		};
		commit(iX);
	}
	//
	public void getVideoSequenceSize1s(ChoisePoint iX, Term a1) {
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		String formatName= null;
		FFmpegStreamDefinition[] streams= FFmpegStreamDefinition.argumentToStreamDefinitions(null,iX);
		FFmpegCodecOption[] options= null;
		FFmpegOperatingMode currentOperatingMode= getOperatingMode(iX);
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		// actingFFmpegOperatingMode.set(currentOperatingMode);
		long size= videoSizeEstimator.estimateVideoSize(
			currentFileName,
			currentTimeout,
			currentCharacterSet,
			formatName,
			streams,
			options,
			staticContext);
		a1.setBacktrackableValue(new PrologInteger(size),iX);
	}
	//
	public void stop0s(ChoisePoint iX) {
		stopAndClose(iX);
	}
	//
	public void close0s(ChoisePoint iX) {
		stopAndClose(iX);
	}
	//
	protected void stopAndClose(ChoisePoint iX) {
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case PLAYING:
		case READING:
			frameReadingTask.closeReading();
			actingFFmpegOperatingMode.set(null);
			break;
		case WRITING:
			frameRecordingTask.closeWriting();
			actingFFmpegOperatingMode.set(null);
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isOpen0s(ChoisePoint iX) throws Backtracking {
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case PLAYING:
		case READING:
			if (!frameReadingTask.inputIsOpen()) {
				throw Backtracking.instance;
			};
			break;
		case WRITING:
			if (!frameRecordingTask.outputIsOpen()) {
				throw Backtracking.instance;
			}
		}
	}
	//
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case PLAYING:
		case READING:
			if (!frameReadingTask.inputIsOpen()) {
				throw Backtracking.instance;
			} else if (frameReadingTask.stopThisThread()) {
				throw Backtracking.instance;
			};
			break;
		case WRITING:
			throw new FFmpegFilesDoNotSupportThisOperationInWritingMode();
		}
	}
	//
	public void isSuspended0s(ChoisePoint iX) throws Backtracking {
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case PLAYING:
		case READING:
			if (!frameReadingTask.isSuspended()) {
				throw Backtracking.instance;
			};
			break;
		case WRITING:
			throw new FFmpegFilesDoNotSupportThisOperationInWritingMode();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
	public void eof0s(ChoisePoint iX) throws Backtracking {
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case PLAYING:
		case READING:
			if (!frameReadingTask.eof()) {
				throw Backtracking.instance;
			};
			break;
		case WRITING:
			// throw new FFmpegFilesDoNotSupportThisOperationInWritingMode();
			if (!frameRecordingTask.eof()) {
				throw Backtracking.instance;
			};
			break;
		}
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
			commit(iX);
		}
	}
	protected void commit(ChoisePoint iX) throws Backtracking {
		containsNewFrame.set(false);
		synchronized (numberOfRecentReceivedFrame) {
			if (recentFrame==null) {
				throw Backtracking.instance;
			};
			committedFrame= recentFrame;
			committedFrameNumber= numberOfRecentReceivedFrame.get();
			updateCommittedFrameTime();
		}
	}
	protected void updateCommittedFrameTime() {
		if (committedFrame != null) {
			committedFrameTime= committedFrame.getTime();
			committedTimeBase= committedFrame.getTimeBase();
			committedAverageFrameRate= committedFrame.getAverageFrameRate();
			if (firstCommittedFrameTime < 0) {
				firstCommittedFrameNumber= committedFrameNumber;
				firstCommittedFrameTime= committedFrameTime;
			}
		} else {
			committedFrameTime= -1;
			committedTimeBase= null;
			committedAverageFrameRate= null;
			// committedBeginningTime= -1;
			firstCommittedFrameNumber= -1;
			firstCommittedFrameTime= -1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new PrologInteger(committedFrameNumber),iX);
	}
	//
	public void getRecentFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new PrologReal(FFmpegTools.computeTime(committedFrameTime,committedTimeBase)),iX);
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
		/*
		long deltaN= committedFrameNumber - firstCommittedFrameNumber;
		double deltaTime= FFmpegTools.computeTime((committedFrameTime - firstCommittedFrameTime),committedTimeBase);
		double rate;
		if (deltaTime > 0) {
			rate= deltaN / deltaTime;
		} else {
			rate= -1.0;
		};
		a1.setBacktrackableValue(new PrologReal(rate),iX);
		*/
	}
	//
	public void getAverageFrameRate1s(ChoisePoint iX, PrologVariable a1) {
		double frameRate= -1.0;
		if (committedAverageFrameRate != null) {
			frameRate= av_q2d(committedAverageFrameRate);
		};
		a1.setBacktrackableValue(new PrologReal(frameRate),iX);
	}
	//
	public void getRecentImage1s(ChoisePoint iX, Term value) {
		if (committedFrame != null) {
			modifyImage(value,committedFrame.getImage(),iX);
		} else {
			throw new FFmpegFrameIsNotCommitted();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (committedFrame != null) {
			a1.setBacktrackableValue(new PrologInteger(committedFrame.getWidth()),iX);
			a2.setBacktrackableValue(new PrologInteger(committedFrame.getHeight()),iX);
		} else {
			throw new FFmpegFrameIsNotCommitted();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void writeImage1s(ChoisePoint iX, Term a1) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			frameRecordingTask.store(nativeImage);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void listInputFormats0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(FFmpegTools.listInputFormats());
	}
	//
	public void listOutputFormats0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(FFmpegTools.listOutputFormats());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendFFmpegFrame(FFmpegFrame frame) {
		// FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		synchronized (numberOfRecentReceivedFrame) {
			recentFrame= frame;
			// numberOfRecentReceivedFrame.set(frame.getNumber());
			numberOfRecentReceivedFrame.incrementAndGet();
			numberOfRecentReceivedFrame.notifyAll();
		};
		sendFrameObtained();
	}
	//
	public boolean sendDataFrame(DataFrameInterface frame) {
		return false;
	}
	public boolean sendCompoundFrame(CompoundFrameInterface frame) {
		return false;
	}
	public boolean sendKinectFrame(KinectFrameInterface frame) {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractFrame(String key, CompoundFrameInterface container) {
		if (committedFrame != null) {
			EnumeratedFFmpegFrame enumeratedFrame= new EnumeratedFFmpegFrame(
				committedFrame,
				this,
				committedFrameNumber);
			container.insertComponent(key,enumeratedFrame);
		} else {
			throw new FFmpegFrameIsNotCommitted();
		}
	}
	//
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		EnumeratedFFmpegFrame enumeratedFrame= (EnumeratedFFmpegFrame)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			committedFrame= enumeratedFrame.getFrame();
			committedFrameWasAssignedDirectly.set(true);
			// committedFrameNumber= enumeratedFrame.getNumberOfFrame();
			committedFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateCommittedFrameTime();
		}
	}
}
