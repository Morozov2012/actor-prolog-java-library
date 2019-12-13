// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.sound;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.sound.errors.*;
import morozov.system.sound.frames.data.converters.*;
import morozov.system.sound.frames.data.interfaces.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.AudioFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class MicrophoneDataLine {
	//
	protected boolean isDefault= true;
	protected String mixerName;
	protected String mixerVendor;
	protected String mixerDescription;
	protected String mixerVersion;
	protected Integer dataLine;
	protected AudioFormatBaseAttributesInterface[] formats;
	protected AudioFormatBaseAttributesInterface format;
	//
	///////////////////////////////////////////////////////////////
	//
	public MicrophoneDataLine() {
	}
	public MicrophoneDataLine(
			String givenMixerName,
			String givenMixerVendor,
			String givenMixerDescription,
			String givenMixerVersion,
			Integer givenDataLine,
			AudioFormatBaseAttributesInterface[] givenFormats,
			AudioFormatBaseAttributesInterface givenFormat
			) {
		isDefault= false;
		mixerName= givenMixerName;
		mixerVendor= givenMixerVendor;
		mixerDescription= givenMixerDescription;
		mixerVersion= givenMixerVersion;
		dataLine= givenDataLine;
		formats= givenFormats;
		format= givenFormat;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isDefault() {
		return isDefault;
	}
	public String getMixerName() {
		return mixerName;
	}
	public String getMixerVendor() {
		return mixerVendor;
	}
	public String getMixerDescription() {
		return mixerDescription;
	}
	public String getMixerVersion() {
		return mixerVersion;
	}
	public int getDataLine() {
		return dataLine;
	}
	public AudioFormatBaseAttributesInterface[] getFormats() {
		return formats;
	}
	public AudioFormatBaseAttributesInterface getFormat() {
		return format;
	}
	//
	public void setCurrentAudioFormat(AudioFormatBaseAttributesInterface givenAudioFormat) {
		format= givenAudioFormat;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean matchesMixerInfo(Mixer.Info mixerInfo) {
		if (isDefault) {
			return true;
		};
		if (mixerName != null) {
			if (!mixerName.equals(mixerInfo.getName())) {
				return false;
			}
		};
		if (mixerVendor != null) {
			if (!mixerVendor.equals(mixerInfo.getVendor())) {
				return false;
			}
		};
		if (mixerDescription != null) {
			if (!mixerDescription.equals(mixerInfo.getDescription())) {
				return false;
			}
		};
		if (mixerVersion != null) {
			if (!mixerVersion.equals(mixerInfo.getVersion())) {
				return false;
			}
		};
		return true;
	}
	//
	public boolean matchesDataLineNumber(int number) {
		if (isDefault) {
			return true;
		};
		if (dataLine != null) {
			if (dataLine != number) {
				return false;
			}
		};
		return true;
	}
	//
	public boolean matchesAudioFormat(AudioFormat audioFormat) {
		if (isDefault) {
			return true;
		};
		if (format != null) {
			int channels= format.getChannels();
			if (channels != audioFormat.getChannels()) {
				return false;
			};
			String encoding= format.getEncoding();
			if (!encoding.equals(audioFormat.getEncoding().toString())) {
				return false;
			};
			float frameRate1= format.getFrameRate();
			float frameRate2= audioFormat.getFrameRate();
			if (frameRate1 > 0 && frameRate2 > 0) {
				if (frameRate1 != frameRate2) {
					return false;
				}
			};
			int frameSize= format.getFrameSize();
			if (frameSize != audioFormat.getFrameSize()) {
				return false;
			};
			float sampleRate1= format.getSampleRate();
			float sampleRate2= audioFormat.getSampleRate();
			if (sampleRate1 > 0 && sampleRate2 > 0) {
				if (sampleRate1 != sampleRate2) {
					return false;
				}
			};
			int sampleSizeInBits= format.getSampleSizeInBits();
			if (sampleSizeInBits != audioFormat.getSampleSizeInBits()) {
				return false;
			};
			boolean isBigEndian= format.getIsBigEndian();
			if (isBigEndian != audioFormat.isBigEndian()) {
				return false;
			}
		};
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static String stringDefault= "default";
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static MicrophoneDataLine argumentToMicrophoneDataLine(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new MicrophoneDataLine();
			} else {
				throw new WrongArgumentIsNotMicrophoneDataLine(value);
			}
		} catch (TermIsNotASymbol e1) {
			return argumentToMicrophoneDataLineAttributes(value,iX);
		}
	}
	//
	public static MicrophoneDataLine argumentToMicrophoneDataLineAttributes(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			String givenMixerName= null;
			String givenMixerVendor= null;
			String givenMixerDescription= null;
			String givenMixerVersion= null;
			Integer givenDataLine= null;
			AudioFormatBaseAttributesInterface[] givenFormats= null;
			AudioFormatBaseAttributesInterface givenFormat= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_mixerName) {
					givenMixerName= GeneralConverters.argumentToString(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_mixerVendor) {
					givenMixerVendor= GeneralConverters.argumentToString(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_mixerDescription) {
					givenMixerDescription= GeneralConverters.argumentToString(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_mixerVersion) {
					givenMixerVersion= GeneralConverters.argumentToString(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_dataLine) {
					givenDataLine= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_formats) {
					givenFormats= AudioFormatBaseAttributesConverters.argumentToAudioFormatBaseAttributesList(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_format) {
					givenFormat= AudioFormatBaseAttributesConverters.argumentToAudioFormatBaseAttributes(pairValue,iX);
				} else {
					throw new WrongArgumentIsNotMicrophoneDataLineAttribute(key);
				}
			};
			return new MicrophoneDataLine(
				givenMixerName,
				givenMixerVendor,
				givenMixerDescription,
				givenMixerVersion,
				givenDataLine,
				givenFormats,
				givenFormat);
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isDefault) {
			return termDefault;
		} else {
			Term termDataLine= PrologEmptySet.instance;
			if (format != null) {
				Term termAudioFormat= AudioFormatBaseAttributesConverters.toTerm(format);
				termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_format,termAudioFormat,termDataLine);
			}
			if (formats != null) {
				ArrayList<Term> audioFormatArrayList= new ArrayList<>();
				for (int f=0; f < formats.length; f++) {
					AudioFormatBaseAttributesInterface currentFormat= formats[f];
					Term termAudioFormat= AudioFormatBaseAttributesConverters.toTerm(currentFormat);
					audioFormatArrayList.add(termAudioFormat);
				};
				Term termAudioFormatList= GeneralConverters.arrayListToTerm(audioFormatArrayList);
				termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_formats,termAudioFormatList,termDataLine);
			};
			if (dataLine != null) {
				termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_dataLine,new PrologInteger(dataLine),termDataLine);
			};
			if (mixerVersion != null) {
				termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_mixerVersion,new PrologString(mixerVersion),termDataLine);
			};
			if (mixerDescription != null) {
				termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_mixerDescription,new PrologString(mixerDescription),termDataLine);
			};
			if (mixerVendor != null) {
				termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_mixerVendor,new PrologString(mixerVendor),termDataLine);
			};
			if (mixerName != null) {
				termDataLine= new PrologSet(-SymbolCodes.symbolCode_E_mixerName,new PrologString(mixerName),termDataLine);
			};
			return termDataLine;
		}
	}
}
