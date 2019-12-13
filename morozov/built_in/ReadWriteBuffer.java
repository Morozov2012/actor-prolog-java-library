// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.converters.interfaces.*;
import morozov.system.frames.data.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.i3v1.frames.data.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.modes.*;
import morozov.system.sound.*;
import morozov.system.sound.frames.data.interfaces.*;
import morozov.system.sound.frames.errors.*;
import morozov.system.sound.frames.interfaces.*;
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

public abstract class ReadWriteBuffer
		extends BufferedImageController
		implements
			DataFrameConsumerInterface,
			DataFrameProviderInterface {
	//
	///////////////////////////////////////////////////////////////
	// Data Frame Reading/Writing Processes & Modes
	///////////////////////////////////////////////////////////////
	//
	protected DataFrameReadingTask frameReadingTask;
	protected DataFrameRecordingTask frameRecordingTask;
	//
	protected AtomicLong numberOfRecentReceivedFrame= new AtomicLong(-1);
	//
	protected AtomicBoolean containsNewFrame= new AtomicBoolean(false);
	protected AtomicBoolean committedFrameWasAssignedDirectly= new AtomicBoolean(false);
	//
	protected LinkedList<EnumeratedFrame> history= new LinkedList<>();
	//
	protected AtomicReference<FrameTransferError> dataReadingError= new AtomicReference<>();
	protected AtomicReference<FrameTransferError> dataWritingError= new AtomicReference<>();
	//
	///////////////////////////////////////////////////////////////
	// Audio Data Acquisition Processes & Modes
	///////////////////////////////////////////////////////////////
	//
	protected SoundPlayingTask soundPlayingTask= new SoundPlayingTask();
	//
	///////////////////////////////////////////////////////////////
	// Audio Data Frame Acquisition
	///////////////////////////////////////////////////////////////
	//
	protected AtomicLong counterOfAcquiredAudioFrames= new AtomicLong(-1);
	protected AtomicLong numberOfRecentReceivedAudioData= new AtomicLong(-1);
	//
	protected AtomicReference<AudioFormatBaseAttributesInterface> recentAudioFormat= new AtomicReference<>();
	//
	protected AudioDataFrameInterface recentAudioData;
	protected AudioDataFrameInterface committedAudioData;
	//
	protected long committedAudioDataNumber= -1;
	protected long committedAudioDataTime= -1;
	protected long firstCommittedAudioDataNumber= -1;
	protected long firstCommittedAudioDataTime= -1;
	//
	protected AtomicBoolean containsNewAudioData= new AtomicBoolean(false);
	protected AtomicBoolean committedAudioDataWasAssignedDirectly= new AtomicBoolean(false);
	//
	protected boolean recentAudioDataIsRepeated= false;
	protected long numberOfRepeatedAudioData= -1;
	//
	///////////////////////////////////////////////////////////////
	// Data Frame Attributes
	///////////////////////////////////////////////////////////////
	//
	protected AtomicLong counterOfRecentAttributes= new AtomicLong(-1);
	//
	protected AtomicReference<DataFrameColorfulAttributesInterface> recentAttributes= new AtomicReference<>();
	//
	///////////////////////////////////////////////////////////////
	// Reading/Writing Attributes
	///////////////////////////////////////////////////////////////
	//
	protected Integer writeBufferSize;
	protected Integer readBufferSize;
	protected NumericalValue slowMotionCoefficient;
	protected IntegerAttribute maximalFrameDelay;
	protected BigInteger outputDebugInformation;
	//
	protected YesNo outputAudioData;
	protected IntegerAttribute audioDataDelayCorrection;
	//
	protected AtomicInteger actingReadBufferSize= new AtomicInteger(defaultReadBufferSize);
	protected AtomicBoolean actingOutputAudioData= new AtomicBoolean(false);
	//
	protected static int defaultReadBufferSize= 0;
	//
	protected static int maximalFrameWaitingTime= 1000;
	//
	///////////////////////////////////////////////////////////////
	// Data Frame Text Attributes
	///////////////////////////////////////////////////////////////
	//
	protected TextAttribute description;
	protected TextAttribute copyright;
	protected TextAttribute registrationDate;
	protected TextAttribute registrationTime;
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
	///////////////////////////////////////////////////////////////
	// Data Frame Color Maps
	///////////////////////////////////////////////////////////////
	//
	protected AtomicLong ownMainColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong ownAuxiliaryColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong recordedMainColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong recordedAuxiliaryColorMapSerialNumber= new AtomicLong(-1);
	//
	protected AtomicReference<DetailedColorMap> ownMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> ownAuxiliaryColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> recordedMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> recordedAuxiliaryColorMap= new AtomicReference<>();
	//
	protected AtomicReference<DetailedColorMap> deliveredMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> deliveredAuxiliaryColorMap= new AtomicReference<>();
	//
	protected Term deliveredMainColorMapTerm;
	protected Term deliveredAuxiliaryColorMapTerm;
	//
	///////////////////////////////////////////////////////////////
	// Data Acquisition Attributes
	///////////////////////////////////////////////////////////////
	//
	protected static YesNo defaultZoomMode= YesNo.NO;
	protected static NumericalValue defaultZoomingCoefficient= new NumericalValue(5.0);
	protected static OnOff defaultAutorangingMode= OnOff.OFF;
	protected static OnOff defaultDoubleColorMapMode= OnOff.OFF;
	protected static OnOff defaultAveragingMode= OnOff.OFF;
	//
	protected static NumericalValue defaultLowerTemperatureBound= new NumericalValue(0.0d);
	protected static NumericalValue defaultUpperTemperatureBound= new NumericalValue(65_536.0d);
	protected static NumericalValue defaultLowerTemperatureQuantile1= new NumericalValue(0.0d);
	protected static NumericalValue defaultUpperTemperatureQuantile1= new NumericalValue(65_536.0d);
	protected static NumericalValue defaultLowerTemperatureQuantile2= new NumericalValue(0.0d);
	protected static NumericalValue defaultUpperTemperatureQuantile2= new NumericalValue(65_536.0d);
	//
	protected static IterativeDetailedColorMap defaultMainColorMap= IterativeDetailedColorMap.getDefaultMainColorMap();
	protected static IterativeDetailedColorMap defaultAuxiliaryColorMap= IterativeDetailedColorMap.getDefaultAuxiliaryColorMap();
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
	abstract public long entry_s_AudioDataObtained_0();
	abstract public long entry_s_DataTransferCompletion_0();
	abstract public long entry_s_BufferOverflow_0();
	abstract public long entry_s_BufferDeallocation_0();
	abstract public long entry_s_DataTransferError_1_i();
	//
	public Term getBuiltInSlot_E_description() {
		return termEmptyString;
	}
	public Term getBuiltInSlot_E_copyright() {
		return termEmptyString;
	}
	public Term getBuiltInSlot_E_registration_date() {
		return termEmptyString;
	}
	public Term getBuiltInSlot_E_registration_time() {
		return termEmptyString;
	}
	//
	abstract public Term getBuiltInSlot_E_write_buffer_size();
	abstract public Term getBuiltInSlot_E_read_buffer_size();
	//
	abstract public Term getBuiltInSlot_E_output_audio_data();
	abstract public Term getBuiltInSlot_E_audio_data_delay_correction();
	//
	abstract public Term getBuiltInSlot_E_slow_motion_coefficient();
	abstract public Term getBuiltInSlot_E_maximal_frame_delay();
	abstract public Term getBuiltInSlot_E_output_debug_information();
	//
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set description
	//
	public void setDescription1s(ChoisePoint iX, Term a1) {
		setDescription(TextAttributeConverters.argumentToTextAttribute(a1,iX));
	}
	public void setDescription(TextAttribute value) {
		description= value;
	}
	public void getDescription0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(TextAttributeConverters.toTerm(getDescription(iX)));
	}
	public void getDescription0fs(ChoisePoint iX) {
	}
	public TextAttribute getDescription(ChoisePoint iX) {
		if (description != null) {
			return description;
		} else {
			Term value= getBuiltInSlot_E_description();
			return TextAttributeConverters.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set copyright
	//
	public void setCopyright1s(ChoisePoint iX, Term a1) {
		setCopyright(TextAttributeConverters.argumentToTextAttribute(a1,iX));
	}
	public void setCopyright(TextAttribute value) {
		copyright= value;
	}
	public void getCopyright0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(TextAttributeConverters.toTerm(getCopyright(iX)));
	}
	public void getCopyright0fs(ChoisePoint iX) {
	}
	public TextAttribute getCopyright(ChoisePoint iX) {
		if (copyright != null) {
			return copyright;
		} else {
			Term value= getBuiltInSlot_E_copyright();
			return TextAttributeConverters.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set registration_date
	//
	public void setRegistrationDate1s(ChoisePoint iX, Term a1) {
		setRegistrationDate(TextAttributeConverters.argumentToTextAttribute(a1,iX));
	}
	public void setRegistrationDate(TextAttribute value) {
		registrationDate= value;
	}
	public void getRegistrationDate0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(TextAttributeConverters.toTerm(getRegistrationDate(iX)));
	}
	public void getRegistrationDate0fs(ChoisePoint iX) {
	}
	public TextAttribute getRegistrationDate(ChoisePoint iX) {
		if (registrationDate != null) {
			return registrationDate;
		} else {
			Term value= getBuiltInSlot_E_registration_date();
			return TextAttributeConverters.argumentToTextAttribute(value,iX);
		}
	}
	//
	// get/set registration_time
	//
	public void setRegistrationTime1s(ChoisePoint iX, Term a1) {
		setRegistrationTime(TextAttributeConverters.argumentToTextAttribute(a1,iX));
	}
	public void setRegistrationTime(TextAttribute value) {
		registrationTime= value;
	}
	public void getRegistrationTime0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(TextAttributeConverters.toTerm(getRegistrationTime(iX)));
	}
	public void getRegistrationTime0fs(ChoisePoint iX) {
	}
	public TextAttribute getRegistrationTime(ChoisePoint iX) {
		if (registrationTime != null) {
			return registrationTime;
		} else {
			Term value= getBuiltInSlot_E_registration_time();
			return TextAttributeConverters.argumentToTextAttribute(value,iX);
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
		setMaximalFrameDelay(IntegerAttributeConverters.argumentToIntegerAttribute(a1,iX));
	}
	public void setMaximalFrameDelay(IntegerAttribute value) {
		maximalFrameDelay= value;
		if (frameReadingTask != null) {
			frameReadingTask.setMaximalFrameDelay(value);
		}
	}
	public void getMaximalFrameDelay0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(IntegerAttributeConverters.toTerm(getMaximalFrameDelay(iX)));
	}
	public void getMaximalFrameDelay0fs(ChoisePoint iX) {
	}
	public IntegerAttribute getMaximalFrameDelay(ChoisePoint iX) {
		if (maximalFrameDelay != null) {
			return maximalFrameDelay;
		} else {
			Term value= getBuiltInSlot_E_maximal_frame_delay();
			return IntegerAttributeConverters.argumentToIntegerAttribute(value,iX);
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
		if (frameReadingTask != null) {
			frameReadingTask.setOutputDebugInformation(Arithmetic.toInteger(value));
		};
		if (frameRecordingTask != null) {
			frameRecordingTask.setOutputDebugInformation(Arithmetic.toInteger(value));
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
	// get/set output_audio_data
	//
	public void setOutputAudioData1s(ChoisePoint iX, Term a1) {
		setOutputAudioData(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setOutputAudioData(YesNo value) {
		outputAudioData= value;
		actingOutputAudioData.set(outputAudioData.toBoolean());
		if (frameReadingTask != null) {
			frameReadingTask.setOutputAudioData(value);
		}
	}
	public void getOutputAudioData0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.toTerm(getOutputAudioData(iX)));
	}
	public void getOutputAudioData0fs(ChoisePoint iX) {
	}
	public YesNo getOutputAudioData(ChoisePoint iX) {
		if (outputAudioData != null) {
			return outputAudioData;
		} else {
			Term value= getBuiltInSlot_E_output_audio_data();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set audio_data_delay_correction
	//
	public void setAudioDataDelayCorrection1s(ChoisePoint iX, Term a1) {
		setAudioDataDelayCorrection(IntegerAttributeConverters.argumentToIntegerAttribute(a1,iX));
	}
	public void setAudioDataDelayCorrection(IntegerAttribute value) {
		audioDataDelayCorrection= value;
		if (frameReadingTask != null) {
			frameReadingTask.setAudioDataDelayCorrection(value);
		}
	}
	public void getAudioDataDelayCorrection0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(IntegerAttributeConverters.toTerm(getAudioDataDelayCorrection(iX)));
	}
	public void getAudioDataDelayCorrection0fs(ChoisePoint iX) {
	}
	public IntegerAttribute getAudioDataDelayCorrection(ChoisePoint iX) {
		if (audioDataDelayCorrection != null) {
			return audioDataDelayCorrection;
		} else {
			Term value= getBuiltInSlot_E_audio_data_delay_correction();
			return IntegerAttributeConverters.argumentToIntegerAttribute(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateAttributesIfNecessary(ChoisePoint iX) {
		if (recentAttributes.get()==null) {
			updateAttributes(iX);
		}
	}
	//
	protected void updateAttributes(ChoisePoint iX) {
		DataFrameColorfulAttributes attributes= new DataFrameColorfulAttributes(
			counterOfRecentAttributes.incrementAndGet(),
			getAutorangingMode(iX).toBoolean(),
			getDoubleColorMapMode(iX).toBoolean(),
			DataRange.BOUNDS,
			NumericalValueConverters.toDouble(getLowerTemperatureBound(iX)),
			NumericalValueConverters.toDouble(getUpperTemperatureBound(iX)),
			NumericalValueConverters.toDouble(getLowerMainTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getUpperMainTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getLowerAuxiliaryTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getUpperAuxiliaryTemperatureQuantile(iX)),
			getMainColorMap(iX),
			getAuxiliaryColorMap(iX),
			getAveragingMode(iX).toBoolean(),
			getZoomImage(iX).toBoolean(),
			getZoomingCoefficient(iX));
		recentAttributes.set(attributes);
	}
	//
	public YesNo getZoomImage(ChoisePoint iX) {
		return defaultZoomMode;
	}
	public NumericalValue getZoomingCoefficient(ChoisePoint iX) {
		return defaultZoomingCoefficient;
	}
	//
	public OnOff getAutorangingMode(ChoisePoint iX) {
		return defaultAutorangingMode;
	}
	public OnOff getAveragingMode(ChoisePoint iX) {
		return defaultAveragingMode;
	}
	public OnOff getDoubleColorMapMode(ChoisePoint iX) {
		return defaultDoubleColorMapMode;
	}
	//
	public IterativeDetailedColorMap getMainColorMap(ChoisePoint iX) {
		return defaultMainColorMap;
	}
	public IterativeDetailedColorMap getAuxiliaryColorMap(ChoisePoint iX) {
		return defaultAuxiliaryColorMap;
	}
	//
	public NumericalValue getLowerTemperatureBound(ChoisePoint iX) {
		return defaultLowerTemperatureBound;
	}
	public NumericalValue getUpperTemperatureBound(ChoisePoint iX) {
		return defaultUpperTemperatureBound;
	}
	public NumericalValue getLowerMainTemperatureQuantile(ChoisePoint iX) {
		return defaultLowerTemperatureQuantile1;
	}
	public NumericalValue getUpperMainTemperatureQuantile(ChoisePoint iX) {
		return defaultUpperTemperatureQuantile1;
	}
	public NumericalValue getLowerAuxiliaryTemperatureQuantile(ChoisePoint iX) {
		return defaultLowerTemperatureQuantile2;
	}
	public NumericalValue getUpperAuxiliaryTemperatureQuantile(ChoisePoint iX) {
		return defaultUpperTemperatureQuantile2;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateOwnMainColorMap(DataFrameBaseAttributesInterface attributes) {
		boolean doUpdateColorMap= false;
		if (ownMainColorMap.get()==null) {
			doUpdateColorMap= true;
		} else {
			long serialNumber= attributes.getSerialNumber();
			if (ownMainColorMapSerialNumber.get() != serialNumber) {
				doUpdateColorMap= true;
			}
		};
		if (doUpdateColorMap) {
			if (attributes instanceof ThermalDataFrameColorfulAttributes) {
				ownMainColorMap.set(((ThermalDataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
			} else if (attributes instanceof DataFrameColorfulAttributes) {
				ownMainColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
			} else {
				DataColorMap dataMainColorMap= attributes.getMainColorMap();
				ownMainColorMap.set(new IterativeDetailedColorMap(dataMainColorMap));
			};
			ownMainColorMapSerialNumber.set(attributes.getSerialNumber());
		}
	}
	protected void updateOwnAuxiliaryColorMap(DataFrameBaseAttributesInterface attributes) {
		boolean doUpdateColorMap= false;
		if (ownAuxiliaryColorMap.get()==null) {
			doUpdateColorMap= true;
		} else {
			long serialNumber= attributes.getSerialNumber();
			if (ownAuxiliaryColorMapSerialNumber.get() != serialNumber) {
				doUpdateColorMap= true;
			}
		};
		if (doUpdateColorMap) {
			if (attributes instanceof ThermalDataFrameColorfulAttributes) {
				ownAuxiliaryColorMap.set(((ThermalDataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
			} else if (attributes instanceof DataFrameColorfulAttributes) {
				ownAuxiliaryColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
			} else {
				DataColorMap dataAuxiliaryColorMap= attributes.getAuxiliaryColorMap();
				ownAuxiliaryColorMap.set(new IterativeDetailedColorMap(dataAuxiliaryColorMap));
			};
			ownAuxiliaryColorMapSerialNumber.set(attributes.getSerialNumber());
		}
	}
	//
	protected void updateRecordedMainColorMap(DataFrameBaseAttributesInterface attributes) {
		boolean doUpdateColorMap= false;
		if (recordedMainColorMap.get()==null) {
			doUpdateColorMap= true;
		} else {
			long serialNumber= attributes.getSerialNumber();
			if (recordedMainColorMapSerialNumber.get() != serialNumber) {
				doUpdateColorMap= true;
			}
		};
		if (doUpdateColorMap) {
			if (attributes instanceof ThermalDataFrameColorfulAttributes) {
				recordedMainColorMap.set(((ThermalDataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
			} else if (attributes instanceof DataFrameColorfulAttributes) {
				recordedMainColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
			} else {
				DataColorMap dataMainColorMap= attributes.getMainColorMap();
				recordedMainColorMap.set(new IterativeDetailedColorMap(dataMainColorMap));
			};
			recordedMainColorMapSerialNumber.set(attributes.getSerialNumber());
		}
	}
	protected void updateRecordedAuxiliaryColorMap(DataFrameBaseAttributesInterface attributes) {
		boolean doUpdateColorMap= false;
		if (recordedAuxiliaryColorMap.get()==null) {
			doUpdateColorMap= true;
		} else {
			long serialNumber= attributes.getSerialNumber();
			if (recordedAuxiliaryColorMapSerialNumber.get() != serialNumber) {
				doUpdateColorMap= true;
			}
		};
		if (doUpdateColorMap) {
			if (attributes instanceof ThermalDataFrameColorfulAttributes) {
				recordedAuxiliaryColorMap.set(((ThermalDataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
			} else if (attributes instanceof DataFrameColorfulAttributes) {
				recordedAuxiliaryColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
			} else {
				DataColorMap dataAuxiliaryColorMap= attributes.getAuxiliaryColorMap();
				recordedAuxiliaryColorMap.set(new IterativeDetailedColorMap(dataAuxiliaryColorMap));
			};
			recordedAuxiliaryColorMapSerialNumber.set(attributes.getSerialNumber());
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
			String currentDescription= deliveredDescription.get();
			if (currentDescription != null) {
				deliveredDescriptionTerm= new PrologString(currentDescription);
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
			String currentDescription= deliveredCopyright.get();
			if (currentDescription != null) {
				deliveredCopyrightTerm= new PrologString(currentDescription);
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
			String currentDescription= deliveredRegistrationDate.get();
			if (currentDescription != null) {
				deliveredRegistrationDateTerm= new PrologString(currentDescription);
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
			String currentDescription= deliveredRegistrationTime.get();
			if (currentDescription != null) {
				deliveredRegistrationTimeTerm= new PrologString(currentDescription);
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
		};
		synchronized (numberOfRecentReceivedAudioData) {
			resetAudioDataCounters();
		}
	}
	public void resetFrameRate0s(ChoisePoint iX) {
		resetFrameRateAndAudioDataRate();
	}
	//
	protected void resetFrameRateAndAudioDataRate() {
		synchronized (numberOfRecentReceivedFrame) {
			resetFrameRate();
		};
		synchronized (numberOfRecentReceivedAudioData) {
			resetAudioDataRate();
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
			counterOfRecentAttributes.set(-1);
			numberOfRecentReceivedFrame.set(-1);
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
	protected void resetAudioDataCounters() {
		synchronized (numberOfRecentReceivedAudioData) {
			numberOfRecentReceivedAudioData.notifyAll();
			recentAudioData= null;
			recentAudioDataIsRepeated= false;
			committedAudioData= null;
			committedAudioDataWasAssignedDirectly.set(false);
			numberOfRecentReceivedAudioData.set(-1);
			numberOfRepeatedAudioData= -1;
			containsNewAudioData.set(false);
			resetAudioDataRate();
		}
	}
	//
	protected void resetFrameRate() {
	}
	//
	protected void resetAudioDataRate() {
		committedAudioDataNumber= -1;
		committedAudioDataTime= -1;
		firstCommittedAudioDataNumber= -1;
		firstCommittedAudioDataTime= -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void start0s(ChoisePoint iX) {
	}
	public void pause0s(ChoisePoint iX) {
	}
	@Override
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
	public void audioDataIsCommitted0s(ChoisePoint iX) throws Backtracking {
		synchronized (numberOfRecentReceivedAudioData) {
			if (committedAudioDataIsNull()) {
				throw Backtracking.instance;
			}
		}
	}
	//
	protected boolean committedAudioDataIsNull() {
		return (committedAudioData==null);
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
	public void containsNewAudioData0s(ChoisePoint iX) throws Backtracking {
		containsNewAudioData();
	}
	//
	protected void containsNewAudioData() throws Backtracking {
		if (!containsNewAudioData.get()) {
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
	public void commitAudioData0s(ChoisePoint iX) throws Backtracking {
		if (committedAudioDataWasAssignedDirectly.get()) {
			if (committedAudioData==null) {
				throw Backtracking.instance;
			}
		} else {
			synchronized (numberOfRecentReceivedAudioData) {
				if (recentAudioData==null) {
					throw Backtracking.instance;
				};
				commitAudioData();
			}
		}
	}
	//
	protected void commitAudioData() {
		synchronized (numberOfRecentReceivedAudioData) {
			containsNewAudioData.set(false);
			committedAudioData= recentAudioData;
			if (!recentAudioDataIsRepeated) {
				committedAudioDataNumber= numberOfRecentReceivedAudioData.get();
			} else {
				committedAudioDataNumber= numberOfRepeatedAudioData;
			};
			updateCommittedAudioDataTime();
		}
	}
	//
	protected void updateCommittedAudioDataTime() {
		if (committedAudioData != null) {
			committedAudioDataTime= committedAudioData.getTime();
		} else {
			committedAudioDataTime= -1;
		};
		if (firstCommittedAudioDataTime < 0) {
			firstCommittedAudioDataNumber= committedAudioDataNumber;
			firstCommittedAudioDataTime= committedAudioDataTime;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected int[][] prepareMainColorMap(boolean doNotControlColorMaps, DataFrameBaseAttributesInterface attributes) {
		int[][] colorMap;
		if (doNotControlColorMaps) {
			updateRecordedMainColorMap(attributes);
			colorMap= recordedMainColorMap.get().toColors();
		} else {
			updateOwnMainColorMap(recentAttributes.get());
			colorMap= ownMainColorMap.get().toColors();
		};
		return colorMap;
	}
	//
	protected int[][] prepareAuxiliaryColorMap(boolean doNotControlColorMaps, DataFrameBaseAttributesInterface attributes) {
		int[][] colorMap;
		if (doNotControlColorMaps) {
			updateRecordedAuxiliaryColorMap(attributes);
			colorMap= recordedAuxiliaryColorMap.get().toColors();
		} else {
			updateOwnAuxiliaryColorMap(recentAttributes.get());
			colorMap= ownAuxiliaryColorMap.get().toColors();
		};
		return colorMap;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getDeliveredColorMaps2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (deliveredMainColorMapTerm==null) {
			DetailedColorMap colorMap= deliveredMainColorMap.get();
			if (colorMap != null) {
				deliveredMainColorMapTerm= ColorMapConverters.detailedColorMapToTerm(colorMap);
			}
		};
		if (deliveredAuxiliaryColorMapTerm==null) {
			DetailedColorMap colorMap= deliveredAuxiliaryColorMap.get();
			if (colorMap != null) {
				deliveredAuxiliaryColorMapTerm= ColorMapConverters.detailedColorMapToTerm(colorMap);
			}
		};
		a1.setBacktrackableValue(deliveredMainColorMapTerm,iX);
		a2.setBacktrackableValue(deliveredAuxiliaryColorMapTerm,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void frameObtained0s(ChoisePoint iX) {
	}
	public void audioDataObtained0s(ChoisePoint iX) {
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
	public void getRecentAudioData0ff(ChoisePoint iX, PrologVariable result) {
		synchronized (numberOfRecentReceivedAudioData) {
			if (committedAudioData != null) {
				byte[] nativeAudioData= ((AudioDataFrameInterface)committedAudioData).getAudioData();
				result.setNonBacktrackableValue(new PrologBinary(nativeAudioData));
			} else {
				throw new AudioDataIsNotCommitted();
			}
		}
	}
	public void getRecentAudioData0fs(ChoisePoint iX) {
	}
	//
	public void replayCommittedAudioData0s(ChoisePoint iX) {
		synchronized (numberOfRecentReceivedAudioData) {
			if (committedAudioData != null) {
				byte[] audioDataArray= committedAudioData.getAudioData();
				soundPlayingTask.startDataTransfer();
				soundPlayingTask.generateSound(audioDataArray);
			} else {
				throw new AudioDataIsNotCommitted();
			}
		}
	}
	//
	public void replayAudioData1s(ChoisePoint iX, Term a1) {
		byte[] byteArray= GeneralConverters.argumentToBinary(a1,iX);
		synchronized (numberOfRecentReceivedAudioData) {
			soundPlayingTask.startDataTransfer();
			soundPlayingTask.generateSound(byteArray);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentAudioDataNumber1s(ChoisePoint iX, PrologVariable a1) {
		long frameNumber;
		synchronized (numberOfRecentReceivedAudioData) {
			frameNumber= committedAudioDataNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(frameNumber),iX);
	}
	//
	public void getRecentAudioDataTime1s(ChoisePoint iX, PrologVariable a1) {
		long frameTime;
		synchronized (numberOfRecentReceivedAudioData) {
			frameTime= committedAudioDataTime;
		};
		a1.setBacktrackableValue(new PrologInteger(frameTime),iX);
	}
	//
	public void getRecentAudioDataRelativeTime1s(ChoisePoint iX, PrologVariable a1) {
		long deltaTime;
		synchronized (numberOfRecentReceivedAudioData) {
			deltaTime= committedAudioDataTime - firstCommittedAudioDataTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaTime),iX);
	}
	//
	public void getRecentAudioDataRate1s(ChoisePoint iX, PrologVariable a1) {
		long deltaN;
		long deltaTime;
		synchronized (numberOfRecentReceivedAudioData) {
			deltaN= committedAudioDataNumber - firstCommittedAudioDataNumber;
			deltaTime= committedAudioDataTime - firstCommittedAudioDataTime;
		};
		double rate= computeFrameRate(deltaN,deltaTime);
		a1.setBacktrackableValue(new PrologReal(rate),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void flushAudioBuffer0s(ChoisePoint iX) {
		flushAudioBuffer();
	}
	//
	protected void flushAudioBuffer() {
		soundPlayingTask.stopDataTransfer();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void activateAudioSystemIfNecessary(boolean flushBuffers, boolean currentAttachAudioData, boolean currentOutputAudioData, int currentOutputDebugInformation) {
		actingOutputAudioData.set(currentOutputAudioData);
		if (currentOutputAudioData) {
			soundPlayingTask.setOutputDebugInformation(currentOutputDebugInformation);
			if (flushBuffers) {
				soundPlayingTask.stopDataTransfer();
			};
			soundPlayingTask.startDataTransfer();
		}
	}
	//
	protected void resetActingMode() {
		actingOutputAudioData.set(false);
	}
	//
	protected void stopAudioSystemIfNecessary(ChoisePoint iX) {
		actingOutputAudioData.set(false);
		soundPlayingTask.stopDataTransfer();
	}
	protected void doStopAudioSystemIfNecessary() {
		actingOutputAudioData.set(false);
		soundPlayingTask.stopDataTransfer();
	}
	//
	protected boolean synchronizeAudioStreamWithFrontVideoFrame() {
		return true;
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
	protected void sendAudioDataObtained() {
		containsNewAudioData.set(true);
		long domainSignature= entry_s_AudioDataObtained_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void completeDataReading(long numberOfAcquiredFrames) {
		dataReadingError.set(null);
		synchronized (numberOfRecentReceivedFrame) {
			numberOfRecentReceivedFrame.notifyAll();
		};
		synchronized (numberOfRecentReceivedAudioData) {
			numberOfRecentReceivedAudioData.notifyAll();
		};
		long domainSignature= entry_s_DataTransferCompletion_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	@Override
	public void completeDataReading(long numberOfFrameToBeAcquired, Throwable e) {
		dataReadingError.set(new FrameTransferError(numberOfFrameToBeAcquired,e));
		synchronized (numberOfRecentReceivedFrame) {
			numberOfRecentReceivedFrame.notifyAll();
		};
		synchronized (numberOfRecentReceivedAudioData) {
			numberOfRecentReceivedAudioData.notifyAll();
		};
		long domainSignature= entry_s_DataTransferError_1_i();
		Term[] arguments= new Term[]{new PrologString(e.toString())};
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	@Override
	public void completeDataWriting(long numberOfFrame, Throwable e) {
		dataWritingError.set(new FrameTransferError(numberOfFrame,e));
		long domainSignature= entry_s_DataTransferError_1_i();
		Term[] arguments= new Term[]{new PrologString(e.toString())};
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	@Override
	public void reportBufferOverflow() {
		long domainSignature= entry_s_BufferOverflow_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	//
	@Override
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
		// SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm:ss z",Locale.ENGLISH);
		// y 	Year 	Year 	1996; 96
		// M 	Month in year 	Month 	July; Jul; 07
		// d 	Day in month 	Number 	10
		// X 	Time zone 	ISO 8601 time zone 	-08; -0800; -08:00
		// Text: For formatting, if the number of pattern letters
		// is 4 or more, the full form is used; otherwise a
		// short or abbreviated form is used if available.
		// For parsing, both forms are accepted, independent of
		// the number of pattern letters.
		// Number: For formatting, the number of pattern letters
		// is the minimum number of digits, and shorter numbers
		// are zero-padded to this amount. For parsing, the number
		// of pattern letters is ignored unless it's needed to
		// separate two adjacent fields.
		// Year: If the formatter's Calendar is the Gregorian
		// calendar, the following rules are applied.
		// For formatting, if the number of pattern letters is 2,
		// the year is truncated to 2 digits; otherwise it is
		// interpreted as a number.
		// Month: If the number of pattern letters is 3 or more,
		// the month is interpreted as text; otherwise, it is
		// interpreted as a number.
		// ISO 8601 Time zone:
		// For formatting, if the offset value from GMT is 0,
		// "Z" is produced. If the number of pattern letters
		// is 1, any fraction of an hour is ignored. For example,
		// if the pattern is "X" and the time zone is "GMT+05:30",
		// "+05" is produced.
		SimpleDateFormat timeFormat= new SimpleDateFormat("yyy-MM-dd HH:mm:ssXXX",Locale.ENGLISH);
		String textDate= dateFormat.format(date);
		String textTime= timeFormat.format(date);
		String fileRegistrationDate= actingRegistrationDate.get().getValue(textDate);
		String fileRegistrationTime= actingRegistrationTime.get().getValue(textTime);
		return new MetadataDescription(
			fileDescription,
			fileCopyright,
			fileRegistrationDate,
			fileRegistrationTime);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static double computeFrameRate(boolean canUseDelta, long deltaN, long deltaTime) {
		double rate;
		if (canUseDelta && deltaTime > 0 && deltaN > 0) {
			rate= deltaN * 1000.0 / deltaTime;
		} else {
			rate= -1.0;
		};
		return rate;
	}
	protected static double computeFrameRate(long deltaN, long deltaTime) {
		double rate;
		if (deltaTime > 0 && deltaN > 0) {
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
		TimeInterval timeInterval= TimeIntervalConverters.argumentMillisecondsToTimeInterval(a1,iX);
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
		};
		break;
	} else {
		selectedEnumeratedFrame= currentEnumeratedFrame;
		delay1= delay2;
		numberOfTargetFramesToBeRead= bufferSize - historySize + relativeNumber - 1;
	}
};
containsNewFrame.set(false);
containsNewAudioData.set(false);
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
