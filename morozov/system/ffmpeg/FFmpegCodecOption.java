// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.ffmpeg.errors.*;
import morozov.terms.*;
import morozov.terms.errors.*;
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
	public static FFmpegCodecOption argumentToFFmpegCodecOption(Term value, ChoisePoint iX) {
		try {
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_option,2,iX);
			String givenName= Converters.argumentToString(arguments[0],iX);
			String givenValue= Converters.argumentToString(arguments[1],iX);
			return new FFmpegCodecOption(givenName,givenValue);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotFFmpegCodecOption(value);
		}
	}
	//
	public static FFmpegCodecOption[] argumentToCodecOptions(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		ArrayList<FFmpegCodecOption> optionArray= new ArrayList<>();
		Term nextHead= null;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				FFmpegCodecOption option= argumentToFFmpegCodecOption(nextHead,iX);
				optionArray.add(option);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
		} catch (TermIsNotAList e2) {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= currentTail.exploreSetPositiveElements(setPositiveMap,iX);
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
			}
		};
		return optionArray.toArray(new FFmpegCodecOption[0]);
	}
}
