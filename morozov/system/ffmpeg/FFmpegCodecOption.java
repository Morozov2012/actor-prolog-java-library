// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.ffmpeg.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class FFmpegCodecOption {
	//
	protected String name;
	protected String value;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegCodecOption(String givenName, String givenValue) {
		name= givenName;
		value= givenValue;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegCodecOption[][] argumentToFFmpegCodecOptionGroups(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		ArrayList<FFmpegCodecOption[]> optionGroupArray= new ArrayList<>();
		Term currentTail= value;
		try {
			while (true) {
				Term nextHead= currentTail.getNextListHead(iX);
				FFmpegCodecOption[] optionGroup= argumentToFFmpegCodecOptions(nextHead,iX);
				optionGroupArray.add(optionGroup);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
		} catch (TermIsNotAList e2) {
			FFmpegCodecOption[] optionGroup= argumentToOptionSet(currentTail,iX);
			optionGroupArray.add(optionGroup);
		};
		return optionGroupArray.toArray(new FFmpegCodecOption[0][0]);
	}
	//
	public static FFmpegCodecOption[] argumentToOptionSet(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		ArrayList<FFmpegCodecOption> optionArray= new ArrayList<>();
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				SymbolName symbolName= SymbolNames.retrieveSymbolName(pairName);
				String textName= symbolName.toRawString(null);
				String textValue= pairValue.toString();
				FFmpegCodecOption option= new FFmpegCodecOption(textName,textValue);
				optionArray.add(option);
			}
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		};
		return optionArray.toArray(new FFmpegCodecOption[optionArray.size()]);
	}
	//
	public static FFmpegCodecOption[] argumentToFFmpegCodecOptions(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		ArrayList<FFmpegCodecOption> optionArray= new ArrayList<>();
		Term currentTail= value;
		try {
			while (true) {
				Term nextHead= currentTail.getNextListHead(iX);
				FFmpegCodecOption option= argumentToFFmpegCodecOption(nextHead,iX);
				optionArray.add(option);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
			return optionArray.toArray(new FFmpegCodecOption[optionArray.size()]);
		} catch (TermIsNotAList e2) {
			return argumentToOptionSet(currentTail,iX);
		}
	}
	//
	public static FFmpegCodecOption argumentToFFmpegCodecOption(Term value, ChoisePoint iX) {
		try {
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_option,2,iX);
			String givenName= GeneralConverters.argumentToString(arguments[0],iX);
			String givenValue= GeneralConverters.argumentToString(arguments[1],iX);
			return new FFmpegCodecOption(givenName,givenValue);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotFFmpegCodecOption(value);
		}
	}
}
