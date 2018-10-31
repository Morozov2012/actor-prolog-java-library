// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.ffmpeg.*;
import morozov.system.ffmpeg.errors.*;
import morozov.system.files.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class FFmpeg extends BufferedImageController implements FFmpegInterface {
	//
	protected FFmpegOperatingMode operatingMode;
	protected NumericalValue slowMotionCoefficient;
	//
	protected AtomicReference<FFmpegOperatingMode> actingFFmpegOperatingMode= new AtomicReference<>();
	protected AtomicReference<FFmpegDataReadingError> dataTransferError= new AtomicReference<>();
	//
	protected FFmpegFrameReadingTask frameReadingTask= new FFmpegFrameReadingTask(this);
	protected FFmpegFrameRecordingTask frameRecordingTask= new FFmpegFrameRecordingTask(this);
	protected FFmpegVideoSizeEstimator videoSizeEstimator= new FFmpegVideoSizeEstimator(this);
	//
	protected FFmpegFrame recentFrame;
	protected AtomicLong numberOfRecentReceivedFrame= new AtomicLong(-1);
	//
	protected FFmpegFrame committedFrame;
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
	abstract public long entry_s_FrameObtained_0();
	abstract public long entry_s_DataTransferCompletion_0();
	abstract public long entry_s_BufferOverflow_0();
	abstract public long entry_s_BufferDeallocation_0();
	abstract public long entry_s_DataTransferError_1_i();
	//
	abstract public Term getBuiltInSlot_E_operating_mode();
	abstract public Term getBuiltInSlot_E_slow_motion_coefficient();
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
	// get/set slow_motion_coefficient
	//
	public void setSlowMotionCoefficient1s(ChoisePoint iX, Term a1) {
		setSlowMotionCoefficient(NumericalValue.argumentToNumericalValue(a1,iX));
	}
	public void setSlowMotionCoefficient(NumericalValue value) {
		slowMotionCoefficient= value;
		frameReadingTask.setSlowMotionCoefficient(slowMotionCoefficient);
	}
	public void getSlowMotionCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getSlowMotionCoefficient(iX).toTerm());
	}
	public void getSlowMotionCoefficient0fs(ChoisePoint iX) {
	}
	public NumericalValue getSlowMotionCoefficient(ChoisePoint iX) {
		if (slowMotionCoefficient != null) {
			return slowMotionCoefficient;
		} else {
			Term value= getBuiltInSlot_E_slow_motion_coefficient();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void recentReadingError2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) throws Backtracking {
		FFmpegDataReadingError error= dataTransferError.get();
		if (error != null) {
			a1.setBacktrackableValue(new PrologInteger(error.getNumberOfFrameToBeAcquired()),iX);
			a2.setBacktrackableValue(new PrologString(error.getException().toString()),iX);
		} else {
			throw Backtracking.instance;
		}
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
			resetBuffer();
			FFmpegOperatingMode currentOperatingMode= getOperatingMode(iX);
			switch (currentOperatingMode) {
			case PLAYING:
				frameReadingTask.setStopAfterSingleReading(false);
				openReadingOrPlaying(currentOperatingMode,currentFileName,formatName,streams,options,iX);
				break;
			case READING:
				frameReadingTask.setStopAfterSingleReading(true);
				openReadingOrPlaying(currentOperatingMode,currentFileName,formatName,streams,options,iX);
				break;
			case WRITING:
				actingFFmpegOperatingMode.set(currentOperatingMode);
				frameRecordingTask.openWriting(currentFileName,formatName,streams,options);
				break;
			}
		}
	}
	//
	protected void openReadingOrPlaying(FFmpegOperatingMode currentOperatingMode, ExtendedFileName currentFileName, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[] options, ChoisePoint iX) {
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		actingFFmpegOperatingMode.set(currentOperatingMode);
		frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
		frameReadingTask.openReading(currentFileName,currentTimeout,currentCharacterSet,formatName,streams,options,staticContext);
	}
	//
	public void resetBuffer() {
		synchronized (numberOfRecentReceivedFrame) {
			recentFrame= null;
			numberOfRecentReceivedFrame.set(-1);
			committedFrame= null;
			committedFrameNumber= -1;
			committedFrameTime= -1;
			committedTimeBase= null;
			committedAverageFrameRate= null;
			// committedBeginningTime= -1;
			firstCommittedFrameNumber= -1;
			firstCommittedFrameTime= -1;
			dataTransferError.set(null);
		}
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
			frameReadingTask.activateReading();
			break;
		case READING:
			frameReadingTask.setStopAfterSingleReading(true);
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
	public void close0s(ChoisePoint iX) {
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
			if (!frameReadingTask.isActive()) {
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
		synchronized (numberOfRecentReceivedFrame) {
			if (recentFrame==null) {
				throw Backtracking.instance;
			};
			committedFrame= recentFrame;
			committedFrameNumber= numberOfRecentReceivedFrame.get();
		};
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
		long deltaN= committedFrameNumber - firstCommittedFrameNumber;
		double deltaTime= FFmpegTools.computeTime((committedFrameTime - firstCommittedFrameTime),committedTimeBase);
		double rate;
		if (deltaTime > 0) {
			rate= deltaN / deltaTime;
		} else {
			rate= -1.0;
		};
		a1.setBacktrackableValue(new PrologReal(rate),iX);
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
	public void frameObtained0s(ChoisePoint iX) {
	}
	public void dataTransferCompletion0s(ChoisePoint iX) {
	}
	public void bufferOverflow0s(ChoisePoint iX) {
	}
	public void bufferDeallocation0s(ChoisePoint iX) {
	}
	public void dataTransferError1s(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendFrame(FFmpegFrame frame) {
		// FFmpegOperatingMode currentOperatingMode= actingFFmpegOperatingMode.get();
		synchronized (numberOfRecentReceivedFrame) {
			recentFrame= frame;
			numberOfRecentReceivedFrame.set(frame.getNumber());
		};
		sendFrameObtained();
	}
	//
	protected void sendFrameObtained() {
		long domainSignature= entry_s_FrameObtained_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void completeDataTransfer(long numberOfAcquiredFrames) {
		dataTransferError.set(null);
		long domainSignature= entry_s_DataTransferCompletion_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	public void completeDataTransfer(long numberOfFrameToBeAcquired, Throwable e) {
		dataTransferError.set(new FFmpegDataReadingError(numberOfFrameToBeAcquired,e));
		long domainSignature= entry_s_DataTransferError_1_i();
		Term[] arguments= new Term[]{new PrologString(e.toString())};
		// 2018.10.30: AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void reportBufferOverflow() {
		long domainSignature= entry_s_BufferOverflow_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void annulBufferOverflow() {
		long domainSignature= entry_s_BufferDeallocation_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
}
