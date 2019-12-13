// (c) 2019 Alexei A. Morozov

package morozov.system.sound.frames.data.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.converters.errors.*;
import morozov.system.errors.*;
import morozov.system.sound.frames.data.*;
import morozov.system.sound.frames.data.converters.errors.*;
import morozov.system.sound.frames.data.interfaces.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class AudioFormatBaseAttributesConverters {
	//
	// protected static float defaultFrameRate= 1000.0f; // Does not operate
	// protected static float defaultFrameRate= 9000.0f; // Is supported
	protected static float defaultFrameRate= 44100.0f;
	// protected static float defaultFrameRate= 200000.0f; // Is supported
	// protected static float defaultFrameRate= 200001.0f; // Is NOT supported
	//
	///////////////////////////////////////////////////////////////
	//
	public static AudioFormatBaseAttributes[] argumentToAudioFormatBaseAttributesList(Term argumentList, ChoisePoint iX) {
		ArrayList<AudioFormatBaseAttributes> argumentArray= new ArrayList<>();
		try {
			while (true) {
				Term head= argumentList.getNextListHead(iX);
				AudioFormatBaseAttributes argument= argumentToAudioFormatBaseAttributes(head,iX);
				argumentArray.add(argument);
				argumentList= argumentList.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
			return argumentArray.toArray(new AudioFormatBaseAttributes[argumentArray.size()]);
		} catch (TermIsNotAList e2) {
			throw new WrongArgumentIsNotAList(argumentList);
		}
	}
	//
	public static AudioFormatBaseAttributes argumentToAudioFormatBaseAttributes(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Integer givenChannels= null;
			String givenEncoding= null;
			Double givenFrameRate= null;
			Integer givenFrameSize= null;
			Double givenSampleRate= null;
			Integer givenSampleSizeInBits= null;
			YesNo givenIsBigEndian= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_channels) {
					givenChannels= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_encoding) {
					givenEncoding= GeneralConverters.argumentToString(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_frameRate) {
					givenFrameRate= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_frameSize) {
					givenFrameSize= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_sampleRate) {
					givenSampleRate= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_sampleSizeInBits) {
					givenSampleSizeInBits= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_isBigEndian) {
					givenIsBigEndian= YesNoConverters.argument2YesNo(pairValue,iX);
				} else {
					throw new WrongArgumentIsNotAudioFormatBaseAttribute(key);
				}
			};
			if (givenChannels==null) {
				givenChannels= 2;
			};
			if (givenEncoding==null) {
				givenEncoding= "PCM_SIGNED";
			};
			if (givenFrameRate==null) {
				givenFrameRate= -1.0;
			};
			if (givenFrameSize==null) {
				givenFrameSize= 4;
			};
			if (givenSampleRate==null) {
				givenSampleRate= -1.0;
			};
			if (givenSampleSizeInBits==null) {
				givenSampleSizeInBits= 16;
			};
			if (givenIsBigEndian==null) {
				givenIsBigEndian= YesNo.NO;
			};
			return new AudioFormatBaseAttributes(
				givenChannels,
				givenEncoding,
				givenFrameRate.floatValue(),
				givenFrameSize,
				givenSampleRate.floatValue(),
				givenSampleSizeInBits,
				givenIsBigEndian.toBoolean());
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static AudioFormatBaseAttributes audioFormatToAudioFormatBaseAttributes(AudioFormat audioFormat) {
		return new AudioFormatBaseAttributes(
			audioFormat.getChannels(),
			audioFormat.getEncoding().toString(),
			audioFormat.getFrameRate(),
			audioFormat.getFrameSize(),
			audioFormat.getSampleRate(),
			audioFormat.getSampleSizeInBits(),
			audioFormat.isBigEndian());
	}
	//
	public static AudioFormat toAudioFormat(AudioFormatBaseAttributesInterface attributes) {
		float sampleRate= attributes.getSampleRate();
		float frameRate= attributes.getFrameRate();
		if (sampleRate <= 0) {
			sampleRate= defaultFrameRate;
		};
		if (frameRate <= 0) {
			frameRate= defaultFrameRate;
		};
		return new AudioFormat(
			toAudioFormatEncoding(attributes.getEncoding()),
			sampleRate,
			attributes.getSampleSizeInBits(),
			attributes.getChannels(),
			attributes.getFrameSize(),
			frameRate,
			attributes.getIsBigEndian());
	}
	//
	public static AudioFormat.Encoding toAudioFormatEncoding(String name) {
		name= name.toUpperCase();
		AudioFormat.Encoding encoding;
		switch (name) {
		case "ALAW":
			encoding= AudioFormat.Encoding.ALAW;
			break;
		case "PCM_FLOAT":
			encoding= AudioFormat.Encoding.PCM_FLOAT;
			break;
		case "PCM_SIGNED":
			encoding= AudioFormat.Encoding.PCM_SIGNED;
			break;
		case "PCM_UNSIGNED":
			encoding= AudioFormat.Encoding.PCM_UNSIGNED;
			break;
		case "ULAW":
			encoding= AudioFormat.Encoding.ULAW;
			break;
		default:
			encoding= AudioFormat.Encoding.PCM_SIGNED;
			break;
		};
		return encoding;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(AudioFormat format) {
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_isBigEndian,YesNoConverters.boolean2TermYesNo(format.isBigEndian()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_sampleSizeInBits,new PrologInteger(format.getSampleSizeInBits()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_sampleRate,new PrologReal(format.getSampleRate()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frameSize,new PrologInteger(format.getFrameSize()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frameRate,new PrologReal(format.getFrameRate()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_encoding,new PrologString(format.getEncoding().toString()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_channels,new PrologInteger(format.getChannels()),result);
		return result;
	}
	//
	public static Term toTerm(AudioFormatBaseAttributesInterface format) {
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_isBigEndian,YesNoConverters.boolean2TermYesNo(format.getIsBigEndian()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_sampleSizeInBits,new PrologInteger(format.getSampleSizeInBits()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_sampleRate,new PrologReal(format.getSampleRate()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frameSize,new PrologInteger(format.getFrameSize()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frameRate,new PrologReal(format.getFrameRate()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_encoding,new PrologString(format.getEncoding().toString()),result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_channels,new PrologInteger(format.getChannels()),result);
		return result;
	}
}
