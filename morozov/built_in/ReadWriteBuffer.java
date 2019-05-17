// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.converters.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Locale;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ReadWriteBuffer extends BufferedImageController implements DataFrameConsumerInterface, DataFrameProviderInterface {
	//
	protected DataFrameReadingTask frameReadingTask;
	protected DataFrameRecordingTask frameRecordingTask;
	//
	protected LinkedList<EnumeratedFrame> history= new LinkedList<>();
	protected AtomicBoolean committedFrameWasAssignedDirectly= new AtomicBoolean(false);
	//
	protected TextAttribute description;
	protected TextAttribute copyright;
	protected TextAttribute registrationDate;
	protected TextAttribute registrationTime;
	//
	protected Integer writeBufferSize;
	protected Integer readBufferSize;
	protected NumericalValue slowMotionCoefficient;
	protected IntegerAttribute maximalFrameDelay;
	protected BigInteger outputDebugInformation;
	//
	protected AtomicReference<TextAttribute> actingDescription= new AtomicReference<>();
	protected AtomicReference<TextAttribute> actingCopyright= new AtomicReference<>();
	protected AtomicReference<TextAttribute> actingRegistrationDate= new AtomicReference<>();
	protected AtomicReference<TextAttribute> actingRegistrationTime= new AtomicReference<>();
	//
	protected AtomicReference<String> deliveredDescription= new AtomicReference<>();
	protected AtomicReference<String> deliveredCopyright= new AtomicReference<>();
	protected AtomicReference<String> deliveredRegistrationDate= new AtomicReference<>();
	protected AtomicReference<String> deliveredRegistrationTime= new AtomicReference<>();
	//
	protected Term deliveredDescriptionTerm;
	protected Term deliveredCopyrightTerm;
	protected Term deliveredRegistrationDateTerm;
	protected Term deliveredRegistrationTimeTerm;
	//
	protected static String defaultDescription= "Actor Prolog";
	protected static String defaultCopyright= "(c) www.fullvision.ru";
	//
	protected static int defaultReadBufferSize= 0;
	protected AtomicInteger actingReadBufferSize= new AtomicInteger(defaultReadBufferSize);
	//
	protected AtomicReference<FrameTransferError> dataReadingError= new AtomicReference<>();
	protected AtomicReference<FrameTransferError> dataWritingError= new AtomicReference<>();
	//
	protected AtomicLong numberOfRecentReceivedFrame= new AtomicLong(-1);
	//
	// protected boolean recentFrameIsRepeated= false;
	//
	// protected DataFrameInterface recentFrame;
	// protected DataFrameInterface committedFrame;
	//
	protected AtomicBoolean containsNewFrame= new AtomicBoolean(false);
	//
	protected static int maximalFrameWaitingTime= 1000;
	//
	protected static final Term termEmptyString= new PrologString("");
	//
	///////////////////////////////////////////////////////////////
	//
	public ReadWriteBuffer() {
	}
	public ReadWriteBuffer(
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		frameReadingTask= givenFrameReadingTask;
		frameRecordingTask= givenFrameRecordingTask;
		connectReadWriteBufferClassInstance();
	}
	public ReadWriteBuffer(GlobalWorldIdentifier id) {
		super(id);
	}
	public ReadWriteBuffer(
			GlobalWorldIdentifier id,
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		super(id);
		frameReadingTask= givenFrameReadingTask;
		frameRecordingTask= givenFrameRecordingTask;
		connectReadWriteBufferClassInstance();
	}
	//
	protected void connectReadWriteBufferClassInstance() {
		if (frameReadingTask != null) {
			frameReadingTask.setDataConsumer(this);
		};
		if (frameRecordingTask != null) {
			frameRecordingTask.setDataProvider(this);
		}
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
	protected Term getBuiltInSlot_E_description() {
		return termEmptyString;
	}
	protected Term getBuiltInSlot_E_copyright() {
		return termEmptyString;
	}
	protected Term getBuiltInSlot_E_registration_date() {
		return termEmptyString;
	}
	protected Term getBuiltInSlot_E_registration_time() {
		return termEmptyString;
	}
	//
	abstract public Term getBuiltInSlot_E_write_buffer_size();
	abstract public Term getBuiltInSlot_E_read_buffer_size();
	abstract public Term getBuiltInSlot_E_slow_motion_coefficient();
	abstract public Term getBuiltInSlot_E_maximal_frame_delay();
	abstract public Term getBuiltInSlot_E_output_debug_information();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set description
	//
	public void setDescription1s(ChoisePoint iX, Term a1) {
		setDescription(TextAttribute.argumentToTextAttribute(a1,iX));
	}
	public void setDescription(TextAttribute value) {
		description= value;
	}
	public void getDescription0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getDescription(iX).toTerm());
	}
	public void getDescription0fs(ChoisePoint iX) {
	}
	public TextAttribute getDescription(ChoisePoint iX) {
		if (description != null) {
			return description;
		} else {
			Term value= getBuiltInSlot_E_description();
			return TextAttribute.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set copyright
	//
	public void setCopyright1s(ChoisePoint iX, Term a1) {
		setCopyright(TextAttribute.argumentToTextAttribute(a1,iX));
	}
	public void setCopyright(TextAttribute value) {
		copyright= value;
	}
	public void getCopyright0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getCopyright(iX).toTerm());
	}
	public void getCopyright0fs(ChoisePoint iX) {
	}
	public TextAttribute getCopyright(ChoisePoint iX) {
		if (copyright != null) {
			return copyright;
		} else {
			Term value= getBuiltInSlot_E_copyright();
			return TextAttribute.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set registration_date
	//
	public void setRegistrationDate1s(ChoisePoint iX, Term a1) {
		setRegistrationDate(TextAttribute.argumentToTextAttribute(a1,iX));
	}
	public void setRegistrationDate(TextAttribute value) {
		registrationDate= value;
	}
	public void getRegistrationDate0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getRegistrationDate(iX).toTerm());
	}
	public void getRegistrationDate0fs(ChoisePoint iX) {
	}
	public TextAttribute getRegistrationDate(ChoisePoint iX) {
		if (registrationDate != null) {
			return registrationDate;
		} else {
			Term value= getBuiltInSlot_E_registration_date();
			return TextAttribute.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set registration_time
	//
	public void setRegistrationTime1s(ChoisePoint iX, Term a1) {
		setRegistrationTime(TextAttribute.argumentToTextAttribute(a1,iX));
	}
	public void setRegistrationTime(TextAttribute value) {
		registrationTime= value;
	}
	public void getRegistrationTime0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getRegistrationTime(iX).toTerm());
	}
	public void getRegistrationTime0fs(ChoisePoint iX) {
	}
	public TextAttribute getRegistrationTime(ChoisePoint iX) {
		if (registrationTime != null) {
			return registrationTime;
		} else {
			Term value= getBuiltInSlot_E_registration_time();
			return TextAttribute.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set write_buffer_size
	//
	public void setWriteBufferSize1s(ChoisePoint iX, Term a1) {
		setWriteBufferSize(GeneralConverters.argumentToSmallInteger(a1,iX));
	}
	public void setWriteBufferSize(int value) {
		writeBufferSize= value;
		if (frameRecordingTask != null) {
			frameRecordingTask.setWriteBufferSize(writeBufferSize);
		}
	}
	public void getWriteBufferSize0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getWriteBufferSize(iX)));
	}
	public void getWriteBufferSize0fs(ChoisePoint iX) {
	}
	public int getWriteBufferSize(ChoisePoint iX) {
		if (writeBufferSize != null) {
			return writeBufferSize;
		} else {
			Term value= getBuiltInSlot_E_write_buffer_size();
			return GeneralConverters.argumentToSmallInteger(value,iX);
		}
	}
	//
	// get/set read_buffer_size
	//
	public void setReadBufferSize1s(ChoisePoint iX, Term a1) {
		setReadBufferSize(GeneralConverters.argumentToSmallInteger(a1,iX));
	}
	public void setReadBufferSize(int value) {
		readBufferSize= value;
		actingReadBufferSize.set(value);
	}
	public void getReadBufferSize0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getReadBufferSize(iX)));
	}
	public void getReadBufferSize0fs(ChoisePoint iX) {
	}
	public int getReadBufferSize(ChoisePoint iX) {
		if (readBufferSize != null) {
			return readBufferSize;
		} else {
			Term value= getBuiltInSlot_E_read_buffer_size();
			return GeneralConverters.argumentToSmallInteger(value,iX);
		}
	}
	//
	// get/set slow_motion_coefficient
	//
	public void setSlowMotionCoefficient1s(ChoisePoint iX, Term a1) {
		setSlowMotionCoefficient(NumericalValueConverters.argumentToNumericalValue(a1,iX));
	}
	public void setSlowMotionCoefficient(NumericalValue value) {
		slowMotionCoefficient= value;
		if (frameReadingTask != null) {
			frameReadingTask.setSlowMotionCoefficient(slowMotionCoefficient);
		}
	}
	public void getSlowMotionCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(NumericalValueConverters.toTerm(getSlowMotionCoefficient(iX)));
	}
	public void getSlowMotionCoefficient0fs(ChoisePoint iX) {
	}
	public NumericalValue getSlowMotionCoefficient(ChoisePoint iX) {
		if (slowMotionCoefficient != null) {
			return slowMotionCoefficient;
		} else {
			Term value= getBuiltInSlot_E_slow_motion_coefficient();
			return NumericalValueConverters.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set maximal_frame_delay
	//
	public void setMaximalFrameDelay1s(ChoisePoint iX, Term a1) {
		setMaximalFrameDelay(IntegerAttribute.argumentToIntegerAttribute(a1,iX));
	}
	public void setMaximalFrameDelay(IntegerAttribute value) {
		maximalFrameDelay= value;
		if (frameReadingTask != null) {
			frameReadingTask.setMaximalFrameDelay(value);
		}
	}
	public void getMaximalFrameDelay0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getMaximalFrameDelay(iX).toTerm());
	}
	public void getMaximalFrameDelay0fs(ChoisePoint iX) {
	}
	public IntegerAttribute getMaximalFrameDelay(ChoisePoint iX) {
		if (maximalFrameDelay != null) {
			return maximalFrameDelay;
		} else {
			Term value= getBuiltInSlot_E_maximal_frame_delay();
			return IntegerAttribute.argumentToIntegerAttribute(value,iX);
		}
	}
	//
	// get/set output_debug_information
	//
	public void setOutputDebugInformation1s(ChoisePoint iX, Term a1) {
		setOutputDebugInformation(GeneralConverters.argumentToStrictInteger(a1,iX));
	}
	public void setOutputDebugInformation(BigInteger value) {
		outputDebugInformation= value;
		if (frameRecordingTask != null) {
			frameRecordingTask.setOutputDebugInformation(PrologInteger.toInteger(value));
		}
	}
	public void getOutputDebugInformation0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getOutputDebugInformation(iX)));
	}
	public void getOutputDebugInformation0fs(ChoisePoint iX) {
	}
	public BigInteger getOutputDebugInformation(ChoisePoint iX) {
		if (outputDebugInformation != null) {
			return outputDebugInformation;
		} else {
			Term value= getBuiltInSlot_E_output_debug_information();
			return GeneralConverters.argumentToStrictInteger(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDeliveredDescription(String value) {
		deliveredDescriptionTerm= null;
		deliveredDescription.set(value);
	}
	public void setDeliveredCopyright(String value) {
		deliveredCopyrightTerm= null;
		deliveredCopyright.set(value);
	}
	public void setDeliveredRegistrationTime(String value) {
		deliveredRegistrationTimeTerm= null;
		deliveredRegistrationTime.set(value);
	}
	public void setDeliveredRegistrationDate(String value) {
		deliveredRegistrationDateTerm= null;
		deliveredRegistrationDate.set(value);
	}
	//
	public void getDeliveredDescription1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredDescriptionTerm==null) {
			String description= deliveredDescription.get();
			if (description != null) {
				deliveredDescriptionTerm= new PrologString(description);
			}
		};
		if (deliveredDescriptionTerm != null) {
			a1.setBacktrackableValue(deliveredDescriptionTerm,iX);
		} else {
			a1.setBacktrackableValue(termEmptyString,iX);
		}
	}
	//
	public void getDeliveredCopyright1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredCopyrightTerm==null) {
			String description= deliveredCopyright.get();
			if (description != null) {
				deliveredCopyrightTerm= new PrologString(description);
			}
		};
		if (deliveredCopyrightTerm != null) {
			a1.setBacktrackableValue(deliveredCopyrightTerm,iX);
		} else {
			a1.setBacktrackableValue(termEmptyString,iX);
		}
	}
	//
	public void getDeliveredRegistrationDate1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredRegistrationDateTerm==null) {
			String description= deliveredRegistrationDate.get();
			if (description != null) {
				deliveredRegistrationDateTerm= new PrologString(description);
			}
		};
		if (deliveredRegistrationDateTerm != null) {
			a1.setBacktrackableValue(deliveredRegistrationDateTerm,iX);
		} else {
			a1.setBacktrackableValue(termEmptyString,iX);
		}
	}
	//
	public void getDeliveredRegistrationTime1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredRegistrationTimeTerm==null) {
			String description= deliveredRegistrationTime.get();
			if (description != null) {
				deliveredRegistrationTimeTerm= new PrologString(description);
			}
		};
		if (deliveredRegistrationTimeTerm != null) {
			a1.setBacktrackableValue(deliveredRegistrationTimeTerm,iX);
		} else {
			a1.setBacktrackableValue(termEmptyString,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void recentReadingError2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) throws Backtracking {
		FrameTransferError error= dataReadingError.get();
		if (error != null) {
			a1.setBacktrackableValue(new PrologInteger(error.getNumberOfFrame()),iX);
			a2.setBacktrackableValue(new PrologString(error.getException().toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void recentWritingError2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) throws Backtracking {
		FrameTransferError error= dataWritingError.get();
		if (error != null) {
			a1.setBacktrackableValue(new PrologInteger(error.getNumberOfFrame()),iX);
			a2.setBacktrackableValue(new PrologString(error.getException().toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetAllCounters0s(ChoisePoint iX) {
		synchronized (numberOfRecentReceivedFrame) {
			resetCounters();
		}
	}
	public void resetFrameRate0s(ChoisePoint iX) {
		synchronized (numberOfRecentReceivedFrame) {
			resetFrameRate();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			numberOfRecentReceivedFrame.notifyAll();
			committedFrameWasAssignedDirectly.set(false);
			synchronized (history) {
				history.clear();
			};
			numberOfRecentReceivedFrame.set(-1);
			// recentFrame= null;
			// recentFrameIsRepeated= false;
			// committedFrame= null;
			containsNewFrame.set(false);
			//
			deliveredDescription.set(null);
			deliveredCopyright.set(null);
			deliveredRegistrationDate.set(null);
			deliveredRegistrationTime.set(null);
			//
			deliveredDescriptionTerm= null;
			deliveredCopyrightTerm= null;
			deliveredRegistrationDateTerm= null;
			deliveredRegistrationTimeTerm= null;
			//
			dataReadingError.set(null);
			dataWritingError.set(null);
			//
			resetFrameRate();
		}
	}
	//
	protected void resetFrameRate() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void start0s(ChoisePoint iX) {
	}
	public void pause0s(ChoisePoint iX) {
	}
	public void stop0s(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isOpen0s(ChoisePoint iX) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isSuspended0s(ChoisePoint iX) throws Backtracking {
		throw Backtracking.instance;
	}
	public void isCommitted0s(ChoisePoint iX) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void containsNewFrame0s(ChoisePoint iX) throws Backtracking {
		containsNewFrame();
	}
	//
	protected void containsNewFrame() throws Backtracking {
		if (!containsNewFrame.get()) {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void eof0s(ChoisePoint iX) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) throws Backtracking {
		if (committedFrameWasAssignedDirectly.get()) {
			return;
		} else {
			throw Backtracking.instance;
		}
	}
	//
	protected void commit() {
		containsNewFrame.set(false);
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
	protected void sendFrameObtained() {
		containsNewFrame.set(true);
		long domainSignature= entry_s_FrameObtained_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void completeDataReading(long numberOfAcquiredFrames) {
		// actingDataAcquisitionBufferOperatingMode.set(null);
		dataReadingError.set(null);
		synchronized (numberOfRecentReceivedFrame) {
			numberOfRecentReceivedFrame.notifyAll();
		};
		long domainSignature= entry_s_DataTransferCompletion_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	public void completeDataReading(long numberOfFrameToBeAcquired, Throwable e) {
		// actingDataAcquisitionBufferOperatingMode.set(null);
		dataReadingError.set(new FrameTransferError(numberOfFrameToBeAcquired,e));
		synchronized (numberOfRecentReceivedFrame) {
			numberOfRecentReceivedFrame.notifyAll();
		};
		long domainSignature= entry_s_DataTransferError_1_i();
		Term[] arguments= new Term[]{new PrologString(e.toString())};
		// 2018.10.30: AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void completeDataWriting(long numberOfFrame, Throwable e) {
		// actingDataAcquisitionBufferOperatingMode.set(null);
		dataWritingError.set(new FrameTransferError(numberOfFrame,e));
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
	//
	///////////////////////////////////////////////////////////////
	//
	protected void setActingMetadata(ChoisePoint iX) {
		actingDescription.set(getDescription(iX));
		actingCopyright.set(getCopyright(iX));
		actingRegistrationDate.set(getRegistrationDate(iX));
		actingRegistrationTime.set(getRegistrationTime(iX));
	}
	//
	protected MetadataDescription createMetadataDescription() {
		String fileDescription= actingDescription.get().getValue(defaultDescription);
		String fileCopyright= actingCopyright.get().getValue(defaultCopyright);
		Calendar timer= new GregorianCalendar();
		Date date= timer.getTime();
		DateFormat dateFormat= DateFormat.getDateInstance(DateFormat.LONG,Locale.ENGLISH);
		// DateFormat timeFormat= DateFormat.getTimeInstance(DateFormat.FULL,Locale.ENGLISH);
		SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm:ss z",Locale.ENGLISH);
		String textDate= dateFormat.format(date);
		String textTime= timeFormat.format(date);
		String fileRegistrationDate= actingRegistrationDate.get().getValue(textDate);
		String fileRegistrationTime= actingRegistrationTime.get().getValue(textTime);
		return new MetadataDescription(fileDescription,fileCopyright,fileRegistrationDate,fileRegistrationTime);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static double computeFrameRate(long deltaN, long deltaTime) {
		double rate;
		if (deltaTime > 0) {
			rate= deltaN * 1000.0 / deltaTime;
		} else {
			rate= -1.0;
		};
		return rate;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateHistory(EnumeratedFrame frame) {
		if (frame==null) {
			return;
		};
		synchronized (history) {
			history.addLast(frame);
			if (history.size() > actingReadBufferSize.get()) {
				history.removeFirst();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void requestBufferedFrame1s(ChoisePoint iX, Term a1) {
		int number= GeneralConverters.argumentToSmallInteger(a1,iX);
		requestBufferedFrame(number);
	}
	//
	protected void requestBufferedFrame(int number) {
		int relativeNumber= number - 1;
		if (relativeNumber < 0) {
			relativeNumber= 0;
		};
		synchronized (numberOfRecentReceivedFrame) {
			EnumeratedFrame enumeratedFrame;
			synchronized (history) {
				int bufferSize= actingReadBufferSize.get();
				int historySize= history.size();
				int maximalIndex= bufferSize - 1;
				if (historySize < bufferSize) {
					maximalIndex= historySize - 1;
					relativeNumber= relativeNumber * historySize / bufferSize;
				};
				if (relativeNumber > maximalIndex) {
					relativeNumber= maximalIndex;
				};
				if (relativeNumber < 0 || relativeNumber >= historySize) {
					return;
				};
				enumeratedFrame= history.get(relativeNumber);
			};
			acceptRequestedFrame(enumeratedFrame);
		};
		sendFrameObtained();
	}
	//
	protected void acceptRequestedFrame(EnumeratedFrame enumeratedFrame) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void retrieveTimedFrame1s(ChoisePoint iX, Term a1) throws Backtracking {
		TimeInterval timeInterval= TimeInterval.argumentMillisecondsToTimeInterval(a1,iX);
		if (frameReadingTask != null) {
			if (!retrieveTimedFrame(timeInterval.toMillisecondsLong())) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	protected boolean retrieveTimedFrame(long targetTime) {
		boolean isSpeculativeReadingMode= isSpeculativeReadingMode();
		while (true) {
			boolean waitNewFrames= false;
			int numberOfTargetFramesToBeRead= 0;
			synchronized (numberOfRecentReceivedFrame) {
				synchronized (history) {
					int bufferSize= actingReadBufferSize.get();
					int historySize= history.size();
					if (historySize <= 0) {
						if (isSpeculativeReadingMode) {
							frameReadingTask.readGivenNumberOfFrames(bufferSize);
							waitNewFrames= true;
						}
						// continue;
					} else {
						EnumeratedFrame firstEnumeratedFrame= history.getFirst();
						EnumeratedFrame lastEnumeratedFrame= history.getLast();
						long minimalTime= firstEnumeratedFrame.getTime();
						long maximalTime= lastEnumeratedFrame.getTime();
						if (targetTime >= minimalTime) {
							if (targetTime <= maximalTime) {
///////////////////////////////////////////////////////////////////////
ListIterator<EnumeratedFrame> iterator= history.listIterator(0);
int relativeNumber= 0;
EnumeratedFrame selectedEnumeratedFrame= firstEnumeratedFrame;
long delay1= targetTime - minimalTime;
if (delay1 < 0) {
	delay1= -delay1;
};
//long DELAY= delay1;
while (iterator.hasNext()) {
	EnumeratedFrame currentEnumeratedFrame= iterator.next();
	relativeNumber++;
	long time2= currentEnumeratedFrame.getTime();
	long delay2= targetTime - time2;
	if (delay2 < 0) {
		delay2= -delay2;
	};
	if (time2 >= targetTime) {
		if (delay2 < delay1) {
			selectedEnumeratedFrame= currentEnumeratedFrame;
			numberOfTargetFramesToBeRead= bufferSize - historySize + relativeNumber - 1;
//DELAY= delay2;
		};
		break;
	} else {
		selectedEnumeratedFrame= currentEnumeratedFrame;
		delay1= delay2;
		numberOfTargetFramesToBeRead= bufferSize - historySize + relativeNumber - 1;
//DELAY= delay1;
	}
};
containsNewFrame.set(false);
acceptRetrievedFrame(selectedEnumeratedFrame);
if (isSpeculativeReadingMode && numberOfTargetFramesToBeRead > 0) {
	frameReadingTask.readGivenNumberOfFrames(numberOfTargetFramesToBeRead);
};
return true;
///////////////////////////////////////////////////////////////////////
							} else { // Read several frames
///////////////////////////////////////////////////////////////////////
if (isSpeculativeReadingMode) {
	frameReadingTask.readFramesUntilGivenTime(targetTime,bufferSize);
	waitNewFrames= true;
}
///////////////////////////////////////////////////////////////////////
							}
						} else { // Suspend reading of the frames
							if (isSpeculativeReadingMode) {
								frameReadingTask.suspendReading();
							};
							return false;
						}
					}
				}; // history
				if (waitNewFrames) {
					try {
						numberOfRecentReceivedFrame.wait(maximalFrameWaitingTime);
					} catch (InterruptedException e) {
					}
				}
			}; // numberOfRecentReceivedFrame
			if (!isSpeculativeReadingMode) {
				break;
			}
		};
		return false;
	}
	//
	protected boolean isSpeculativeReadingMode() {
		return false;
	}
	//
	protected void acceptRetrievedFrame(EnumeratedFrame enumeratedFrame) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractFrame(String key, CompoundFrameInterface container) {
	}
	public void extractSettings(String key, CompoundFrameInterface container, ChoisePoint iX) {
		extractFrame(key,container);
	}
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
	}
}
