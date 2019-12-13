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
import morozov.system.sound.frames.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public abstract class FFmpeg extends ReadWriteBuffer implements FFmpegInterface {
	//
	protected FFmpegOperatingMode operatingMode;
	protected OnOff automaticFrameRateCorrection;
	//
	protected AtomicReference<FFmpegOperatingMode> actingFFmpegOperatingMode= new AtomicReference<>();
	//
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	protected FFmpegFrameReadingTask frameReadingTask= new FFmpegFrameReadingTask(this);
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	protected FFmpegFrameRecordingTask frameRecordingTask= new FFmpegFrameRecordingTask(this);
	protected FFmpegVideoSizeEstimator videoSizeEstimator= new FFmpegVideoSizeEstimator(this);
	//
	protected FFmpegFrame recentFrame;
	//
	protected FFmpegFrameInterface committedFrame;
	protected long committedFrameNumber= -1;
	protected long committedFrameTime= -1;
	protected long committedFramePTS= -1;
	protected AVRational committedTimeBase= null;
	protected AVRational committedAverageFrameRate= null;
	protected long firstCommittedFrameNumber= -1;
	protected long firstCommittedFrameTime= -1;
	protected long firstCommittedFramePTS= -1;
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
	abstract public Term getBuiltInSlot_E_operating_mode();
	abstract public Term getBuiltInSlot_E_automatic_frame_rate_correction();
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
	// get/set automatic_frame_rate_correction
	//
	public void setAutomaticFrameRateCorrection1s(ChoisePoint iX, Term a1) {
		setAutomaticFrameRateCorrection(OnOffConverters.argument2OnOff(a1,iX));
	}
	public void setAutomaticFrameRateCorrection(OnOff value) {
		automaticFrameRateCorrection= value;
		if (frameRecordingTask != null) {
			frameRecordingTask.setAutomaticFrameRateCorrection(value.toBoolean());
		}
	}
	public void getAutomaticFrameRateCorrection0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(OnOffConverters.toTerm(getAutomaticFrameRateCorrection(iX)));
	}
	public void getAutomaticFrameRateCorrection0fs(ChoisePoint iX) {
	}
	public OnOff getAutomaticFrameRateCorrection(ChoisePoint iX) {
		if (automaticFrameRateCorrection != null) {
			return automaticFrameRateCorrection;
		} else {
			Term value= getBuiltInSlot_E_automatic_frame_rate_correction();
			return OnOffConverters.argument2OnOff(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setWriteBufferSize(int value) {
		super.setWriteBufferSize(value);
		frameRecordingTask.setWriteBufferSize(value);
	}
	//
	@Override
	public void setOutputDebugInformation(BigInteger value) {
		super.setOutputDebugInformation(value);
		int mode= Arithmetic.toInteger(value);
		frameReadingTask.setOutputDebugInformation(mode);
		frameRecordingTask.setOutputDebugInformation(mode);
	}
	//
	@Override
	public void setSlowMotionCoefficient(NumericalValue value) {
		super.setSlowMotionCoefficient(value);
		frameReadingTask.setSlowMotionCoefficient(value);
	}
	//
	@Override
	public void setMaximalFrameDelay(IntegerAttribute value) {
		super.setMaximalFrameDelay(value);
		frameReadingTask.setMaximalFrameDelay(value);
	}
	//
	@Override
	public void setAudioDataDelayCorrection(IntegerAttribute value) {
		super.setAudioDataDelayCorrection(value);
		if (frameRecordingTask != null) {
			frameRecordingTask.setAudioDataDelayCorrection(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			recentFrame= null;
			committedFrame= null;
		}
	}
	//
	@Override
	protected void resetFrameRate() {
		committedTimeBase= null;
		committedAverageFrameRate= null;
		committedFrameNumber= -1;
		committedFrameTime= -1;
		committedFramePTS= -1;
		firstCommittedFrameNumber= -1;
		firstCommittedFrameTime= -1;
		firstCommittedFramePTS= -1;
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
				break;
			case READING:
				frameReadingTask.setStopAfterSingleReading(true);
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
			FFmpegCodecOption[][] options= null;
			if (a4 != null) {
				options= FFmpegCodecOption.argumentToFFmpegCodecOptionGroups(a4,iX);
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
				OnOff currentAutomaticFrameRateCorrection= getAutomaticFrameRateCorrection(iX);
				IntegerAttribute currentAudioDataDelayCorrection= getAudioDataDelayCorrection(iX);
				frameRecordingTask.setAutomaticFrameRateCorrection(currentAutomaticFrameRateCorrection.toBoolean());
				frameRecordingTask.setAudioDataDelayCorrection(currentAudioDataDelayCorrection);
				frameRecordingTask.openWriting(currentFileName,formatName,streams,options,metadataDescription);
				break;
			}
		}
	}
	//
	protected void openReadingOrPlaying(FFmpegOperatingMode currentOperatingMode, ExtendedFileName currentFileName, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[][] options, ChoisePoint iX) {
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		IntegerAttribute currentMaximalFrameDelay= getMaximalFrameDelay(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		actingFFmpegOperatingMode.set(currentOperatingMode);
		frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
		frameReadingTask.setMaximalFrameDelay(currentMaximalFrameDelay);
		frameReadingTask.setOutputDebugInformation(currentOutputDebugInformation);
		frameReadingTask.openReading(currentFileName,currentTimeout,currentCharacterSet,formatName,streams,options,staticContext);
	}
	//
	@Override
	public void start0s(ChoisePoint iX) {
		FFmpegOperatingMode actingOperatingMode= actingFFmpegOperatingMode.get();
		boolean flushBuffers= false;
		if (actingOperatingMode==null) {
			open(iX,null,null,null,null);
			flushBuffers= true;
		};
		boolean currentOutputAudioData= getOutputAudioData(iX).toBoolean();
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		actingOperatingMode= actingFFmpegOperatingMode.get();
		switch (actingOperatingMode) {
		case PLAYING:
			activateAudioSystemIfNecessary(flushBuffers,false,currentOutputAudioData,currentOutputDebugInformation);
			frameReadingTask.setStopAfterSingleReading(false);
			frameReadingTask.setSlowMotionCoefficient(getSlowMotionCoefficient(iX));
			frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
			frameReadingTask.setOutputDebugInformation(currentOutputDebugInformation);
			frameReadingTask.activateReading();
			break;
		case READING:
			activateAudioSystemIfNecessary(flushBuffers,false,currentOutputAudioData,currentOutputDebugInformation);
			frameReadingTask.setStopAfterSingleReading(true);
			frameReadingTask.setSlowMotionCoefficient(getSlowMotionCoefficient(iX));
			frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
			frameReadingTask.setOutputDebugInformation(currentOutputDebugInformation);
			frameReadingTask.activateReading();
			break;
		case WRITING:
			break;
		}
	}
	//
	@Override
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
		long targetNumber= Arithmetic.toLong(position);
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
	@Override
	public void requestBufferedFrame1s(ChoisePoint iX, Term a1) {
		BigInteger position= GeneralConverters.argumentToStrictInteger(a1,iX);
		seekFrameNumber(position,iX);
		frameReadingTask.setStopAfterSingleReading(true);
		frameReadingTask.setSlowMotionCoefficient(getSlowMotionCoefficient(iX));
		frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
		frameReadingTask.setOutputDebugInformation(Arithmetic.toInteger(getOutputDebugInformation(iX)));
	}
	//
	@Override
	public void retrieveTimedFrame1s(ChoisePoint iX, Term a1) throws Backtracking {
		double targetTime= GeneralConverters.argumentToReal(a1,iX);
		seekFrameTime(targetTime,iX);
		frameReadingTask.setStopAfterSingleReading(true);
		frameReadingTask.setSlowMotionCoefficient(getSlowMotionCoefficient(iX));
		frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
		frameReadingTask.setOutputDebugInformation(Arithmetic.toInteger(getOutputDebugInformation(iX)));
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
		FFmpegCodecOption[][] options= null;
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
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
	@Override
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
	public void flush0s(ChoisePoint iX) {
		flush(iX);
	}
	//
	protected void flush(ChoisePoint iX) {
		FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case WRITING:
			frameRecordingTask.flushWriting();
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
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
	@Override
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
	@Override
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
			if (!frameRecordingTask.eof()) {
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
			committedFrameTime= committedFrame.getTimeInMilliseconds();
			committedFramePTS= committedFrame.getTimeInBaseUnits();
			committedTimeBase= committedFrame.getTimeBase();
			committedAverageFrameRate= committedFrame.getAverageFrameRate();
			if (firstCommittedFrameTime < 0) {
				firstCommittedFrameNumber= committedFrameNumber;
				firstCommittedFrameTime= committedFrameTime;
			}
		} else {
			committedFrameTime= -1;
			committedFramePTS= -1;
			committedTimeBase= null;
			committedAverageFrameRate= null;
			firstCommittedFrameNumber= -1;
			firstCommittedFrameTime= -1;
			firstCommittedFramePTS= -1;
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
		a1.setBacktrackableValue(new PrologInteger(committedFrameTime),iX);
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
	public void getRecentFramePTS1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new PrologInteger(committedFramePTS),iX);
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
	public void writeRealtimeImage2s(ChoisePoint iX, Term a1, Term a2) {
		long timeInMilliseconds;
		if (a2 != null) {
			timeInMilliseconds= Arithmetic.toLong(GeneralConverters.argumentToRoundInteger(a2,iX));
		} else {
			Calendar calendar= Calendar.getInstance();
			timeInMilliseconds= calendar.getTimeInMillis();
		};
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			frameRecordingTask.store(nativeImage,timeInMilliseconds);
		}
	}
	//
	public void writeAudioData1s(ChoisePoint iX, Term a1) {
		byte[] byteArray= GeneralConverters.argumentToBinary(a1,iX);
		if (byteArray != null) {
			frameRecordingTask.store(byteArray);
		}
	}
	//
	public void writeRealtimeAudioData2s(ChoisePoint iX, Term a1, Term a2) {
		long timeInMilliseconds;
		if (a2 != null) {
			timeInMilliseconds= Arithmetic.toLong(GeneralConverters.argumentToRoundInteger(a2,iX));
		} else {
			Calendar calendar= Calendar.getInstance();
			timeInMilliseconds= calendar.getTimeInMillis();
		};
		byte[] byteArray= GeneralConverters.argumentToBinary(a1,iX);
		if (byteArray != null) {
			frameRecordingTask.store(byteArray,timeInMilliseconds);
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
	@Override
	public void sendFFmpegFrame(FFmpegFrame frame) {
		synchronized (numberOfRecentReceivedFrame) {
			recentFrame= frame;
			numberOfRecentReceivedFrame.incrementAndGet();
			numberOfRecentReceivedFrame.notifyAll();
		};
		sendFrameObtained();
	}
	//
	@Override
	public void sendFFmpegAudioData(FFmpegAudioData audioData) {
		if (actingOutputAudioData.get()) {
			soundPlayingTask.generateSound(audioData.getByteArray());
		};
		synchronized (numberOfRecentReceivedAudioData) {
			AudioDataFrame frame= new EncodedAudioDataFrame(
				counterOfAcquiredAudioFrames.incrementAndGet(),
				audioData.getTimeInMilliseconds(),
				audioData.getByteArray(),
				audioData.getAudioFormatAttributes(),
				recentAttributes.get());
			recentAudioData= frame;
			numberOfRecentReceivedAudioData.incrementAndGet();
			numberOfRecentReceivedAudioData.notifyAll();
		};
		sendAudioDataObtained();
	}
	//
	@Override
	public boolean sendDataFrame(DataFrameInterface frame) {
		return false;
	}
	@Override
	public boolean sendCompoundFrame(CompoundFrameInterface frame) {
		return false;
	}
	@Override
	public boolean sendKinectFrame(KinectFrameInterface frame) {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		EnumeratedFFmpegFrame enumeratedFrame= (EnumeratedFFmpegFrame)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			committedFrame= enumeratedFrame.getFrame();
			committedFrameWasAssignedDirectly.set(true);
			committedFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateCommittedFrameTime();
		}
	}
}
