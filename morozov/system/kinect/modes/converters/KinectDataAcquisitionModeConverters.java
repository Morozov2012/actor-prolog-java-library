// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.kinect.frames.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class KinectDataAcquisitionModeConverters {
	//
	public static KinectFrameType[] argumentToKinectDataAcquisitionMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_auto) {
				return new KinectFrameType[0];
			} else {
				throw new WrongArgumentIsNotKinectDataAcquisitionMode(value);
			}
		} catch (TermIsNotASymbol e1) {
			ArrayList<KinectFrameType> arrayOfFrameTypes= new ArrayList<>();
			argumentToArrayOfKinectFrameTypes(value,arrayOfFrameTypes,iX);
			return arrayOfFrameTypes.toArray(new KinectFrameType[0]);
		}
	}
	//
	protected static void argumentToArrayOfKinectFrameTypes(Term value, ArrayList<KinectFrameType> array, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				argumentToArrayOfKinectFrameTypes(nextHead,array,iX);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			KinectFrameType frameType= KinectFrameTypeConverters.argumentToKinectFrameType(value,iX);
			if (!array.contains(frameType)) {
				array.add(frameType);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termAuto= new PrologSymbol(SymbolCodes.symbolCode_E_auto);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectFrameType[] frameTypes) {
		if (frameTypes.length <= 0) {
			return termAuto;
		} else {
			Term value= PrologEmptyList.instance;
			for (int n=0; n < frameTypes.length; n++) {
				value= new PrologList(KinectFrameTypeConverters.toTerm(frameTypes[n]),value);
			};
			return value;
		}
	}
}
