// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.modes.*;
import morozov.system.sound.*;
import morozov.system.sound.errors.*;
import morozov.system.sound.frames.data.*;
import morozov.system.sound.frames.data.converters.*;
import morozov.system.sound.frames.data.interfaces.*;
import morozov.system.sound.frames.errors.*;
import morozov.system.sound.frames.interfaces.*;
import morozov.terms.*;
import morozov.worlds.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Line;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioFormat;
import java.util.Collections;
import java.util.ArrayList;

public abstract class Microphone extends DataAcquisitionBuffer {
	//
	public MicrophoneDataLine defaultDataLine= null;
	//
	public MicrophoneDataLine currentMicrophoneDataLine= null;
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_default_data_line();
	//
	///////////////////////////////////////////////////////////////
	//
	public Microphone() {
		super(	new DataFrameReadingTask(),
			new DataFrameRecordingTask());
	}
	public Microphone(GlobalWorldIdentifier id) {
		super(	id,
			new DataFrameReadingTask(),
			new DataFrameRecordingTask());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set defaultDataLine
	//
	public void setDefaultDataLine1s(ChoisePoint iX, Term a1) {
		setDefaultDataLine(MicrophoneDataLine.argumentToMicrophoneDataLine(a1,iX));
	}
	public void setDefaultDataLine(MicrophoneDataLine value) {
		defaultDataLine= value;
	}
	public void getDefaultDataLine0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getDefaultDataLine(iX).toTerm());
	}
	public void getDefaultDataLine0fs(ChoisePoint iX) {
	}
	public MicrophoneDataLine getDefaultDataLine(ChoisePoint iX) {
		if (defaultDataLine != null) {
			return defaultDataLine;
		} else {
			Term value= getBuiltInSlot_E_default_data_line();
			return MicrophoneDataLine.argumentToMicrophoneDataLine(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getActualDataLine0ff(ChoisePoint iX, PrologVariable result) {
		if (currentMicrophoneDataLine != null) {
			AudioFormatBaseAttributesInterface audioFormat= recentAudioFormat.get();
			currentMicrophoneDataLine.setCurrentAudioFormat(audioFormat);
			result.setNonBacktrackableValue(currentMicrophoneDataLine.toTerm());
		} else {
			result.setNonBacktrackableValue(termDefault);
		}
	}
	public void getActualDataLine0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getMicrophoneList0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getMicrophoneList());
	}
	public void getMicrophoneList0fs(ChoisePoint iX) {
	}
	//
	protected Term getMicrophoneList() {
		ArrayList<Term> microphones= new ArrayList<>();
		Mixer.Info[] mixerInfoArray= AudioSystem.getMixerInfo();
		for (int k=0; k < mixerInfoArray.length; k++) {
			Mixer.Info mixerInfo= mixerInfoArray[k];
			Mixer mixer= AudioSystem.getMixer(mixerInfo);
			Line.Info[] targetLineInfoArray= mixer.getTargetLineInfo();
			int counter= 0;
			for (int m=0; m < targetLineInfoArray.length; m++) {
				counter++;
				Line.Info lineInfo= targetLineInfoArray[m];
				if (lineInfo instanceof DataLine.Info) {
///////////////////////////////////////////////////////////////////////
DataLine.Info dataLineInfo= (DataLine.Info)lineInfo;
AudioFormat[] audioFormatArray= dataLineInfo.getFormats();
ArrayList<Term> audioFormatArrayList= new ArrayList<>();
for (int f=0; f < audioFormatArray.length; f++) {
	AudioFormat audioFormat= audioFormatArray[f];
	Term termAudioFormat= AudioFormatBaseAttributesConverters.toTerm(audioFormat);
	audioFormatArrayList.add(termAudioFormat);
};
Term termAudioFormatList= GeneralConverters.arrayListToTerm(audioFormatArrayList);
Term termDataLine= PrologEmptySet.instance;
termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_formats,termAudioFormatList,termDataLine);
termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_dataLine,new PrologInteger(counter),termDataLine);
termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_mixerVersion,new PrologString(mixerInfo.getVersion()),termDataLine);
termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_mixerDescription,new PrologString(mixerInfo.getDescription()),termDataLine);
termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_mixerVendor,new PrologString(mixerInfo.getVendor()),termDataLine);
termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_mixerName,new PrologString(mixerInfo.getName()),termDataLine);
microphones.add(termDataLine);
///////////////////////////////////////////////////////////////////////
				}
			}
		};
		Term result= GeneralConverters.arrayListToTerm(microphones);
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected boolean synchronizeAudioStreamWithFrontVideoFrame() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void start0s(ChoisePoint iX) {
		MicrophoneDataLine microphone= getDefaultDataLine(iX);
		start(microphone,iX);
	}
	public void start1s(ChoisePoint iX, Term a1) {
		MicrophoneDataLine microphone= MicrophoneDataLine.argumentToMicrophoneDataLine(a1,iX);
		start(microphone,iX);
	}
	//
	protected void start(MicrophoneDataLine microphone, ChoisePoint iX) {
		DataAcquisitionBufferOperatingMode actingOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (actingOperatingMode==null) {
			resetCounters();
			updateAttributes(iX);
			DataAcquisitionBufferOperatingMode currentOperatingMode= getOperatingMode(iX);
			if (currentOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
				actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
				try {
					startRecording(microphone,currentOperatingMode,iX);
					return;
				} catch (Throwable e) {
					resetActingMode();
					throw e;
				}
			} else if (currentOperatingMode==DataAcquisitionBufferOperatingMode.LISTENING) {
				actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
				try {
					prepareAndActivateListening(microphone,true,iX);
					return;
				} catch (Throwable e) {
					resetActingMode();
					throw e;
				}
			}
		} else if (actingOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
			startRecording(microphone,actingOperatingMode,iX);
			return;
		} else if (actingOperatingMode==DataAcquisitionBufferOperatingMode.LISTENING) {
			prepareAndActivateListening(microphone,false,iX);
			return;
		};
		super.start(false,iX);
	}
	//
	protected void startRecording(MicrophoneDataLine microphone, DataAcquisitionBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		if (dataAcquisitionIsActive()) {
			return;
		};
		int currentWriteBufferSize= getWriteBufferSize(iX);
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
		frameRecordingTask.setOutputDebugInformation(currentOutputDebugInformation);
		frameRecordingTask.reset(currentFileName);
		setActingMetadata(iX);
		selectAndSetMicrophone(microphone,iX);
		super.startRecording(currentOperatingMode,false,iX);
	}
	//
	protected void prepareAndActivateListening(MicrophoneDataLine microphone, boolean flushBuffers, ChoisePoint iX) {
		int currentReadBufferSize= getReadBufferSize(iX);
		actingReadBufferSize.set(currentReadBufferSize);
		selectAndSetMicrophone(microphone,iX);
		super.activateDataAcquisition(flushBuffers,iX);
	}
	//
	protected void selectAndSetMicrophone(MicrophoneDataLine microphone, ChoisePoint iX) {
		if (dataAcquisitionIsActive()) {
			return;
		};
		if (microphone.isDefault()) {
			soundFramingTask.setRequestedMixerInfoAndAudioFormat(null,null);
			currentMicrophoneDataLine= new MicrophoneDataLine();
			return;
		};
		Mixer.Info[] mixerInfoArray= AudioSystem.getMixerInfo();
		for (int k=0; k < mixerInfoArray.length; k++) {
			Mixer.Info mixerInfo= mixerInfoArray[k];
			if (!microphone.matchesMixerInfo(mixerInfo)) {
				continue;
			};
			Mixer mixer= AudioSystem.getMixer(mixerInfo);
			Line.Info[] targetLineInfoArray= mixer.getTargetLineInfo();
			int counter= 0;
			for (int m=0; m < targetLineInfoArray.length; m++) {
				counter++;
				if (!microphone.matchesDataLineNumber(counter)) {
					continue;
				};
				Line.Info lineInfo= targetLineInfoArray[m];
				if (lineInfo instanceof DataLine.Info) {
///////////////////////////////////////////////////////////////////////
DataLine.Info dataLineInfo= (DataLine.Info)lineInfo;
AudioFormat[] audioFormatArray= dataLineInfo.getFormats();
ArrayList<AudioFormatBaseAttributes> audioFormatArrayList= new ArrayList<>();
for (int f=0; f < audioFormatArray.length; f++) {
	AudioFormat audioFormat= audioFormatArray[f];
	if (!microphone.matchesAudioFormat(audioFormat)) {
		continue;
	};
	AudioFormatBaseAttributes attributes= AudioFormatBaseAttributesConverters.audioFormatToAudioFormatBaseAttributes(audioFormat);
	audioFormatArrayList.add(attributes);
};
if (audioFormatArrayList.size() <= 0) {
	continue;
};
Collections.sort(audioFormatArrayList);
AudioFormatBaseAttributes firstAttributes= audioFormatArrayList.get(0);
soundFramingTask.setRequestedMixerInfoAndAudioFormat(mixerInfo,AudioFormatBaseAttributesConverters.toAudioFormat(firstAttributes));
currentMicrophoneDataLine= new MicrophoneDataLine(
	mixerInfo.getName(),
	mixerInfo.getVendor(),
	mixerInfo.getDescription(),
	mixerInfo.getVersion(),
	counter,
	audioFormatArrayList.toArray(new AudioFormatBaseAttributes[audioFormatArrayList.size()]),
	firstAttributes);
return;
///////////////////////////////////////////////////////////////////////
				}
			}
		};
		throw new MicrophoneIsNotFound(microphone.toTerm().toString());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void activateDataAcquisition(boolean flushBuffers, ChoisePoint iX) {
		MicrophoneDataLine MicrophoneDataLine= getDefaultDataLine(iX);
		selectAndSetMicrophone(MicrophoneDataLine,iX);
		super.activateDataAcquisition(flushBuffers,iX);
	}
	//
	@Override
	protected void suspendRecording(ChoisePoint iX) {
		stopMicrophone(iX);
		super.suspendRecording(iX);
	}
	@Override
	protected void suspendListening(ChoisePoint iX) {
		stopMicrophone(iX);
		super.suspendListening(iX);
	}
	//
	@Override
	protected void stopRecording(ChoisePoint iX) {
		stopMicrophone(iX);
		super.stopRecording(iX);
	}
	@Override
	protected void stopListening(ChoisePoint iX) {
		stopMicrophone(iX);
		super.stopListening(iX);
	}
	//
	protected void stopMicrophone(ChoisePoint iX) {
		soundFramingTask.stopDataTransfer();
		currentMicrophoneDataLine= null;
	}
	//
	@Override
	protected boolean dataAcquisitionIsActive() {
		return soundFramingTask.microphoneIsActive();
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
	public void extractFrame(String key, CompoundFrameInterface container) {
		if (committedAudioData != null) {
			EnumeratedDataFrame enumeratedFrame= new EnumeratedDataFrame(
				committedAudioData,
				committedAudioDataNumber);
			container.insertComponent(key,enumeratedFrame);
		} else {
			throw new AudioDataIsNotCommitted();
		}
	}
	//
	@Override
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		EnumeratedDataFrame enumeratedFrame= (EnumeratedDataFrame)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			committedAudioData= (AudioDataFrameInterface)enumeratedFrame.getFrame();
			committedAudioDataWasAssignedDirectly.set(true);
			committedAudioDataNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateCommittedAudioDataTime();
		}
	}
}
