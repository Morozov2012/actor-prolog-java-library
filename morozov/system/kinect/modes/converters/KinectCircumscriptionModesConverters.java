// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class KinectCircumscriptionModesConverters {
	//
	public static KinectCircumscriptionMode[] argumentToKinectCircumscriptionModes(Term value, ChoisePoint iX) {
		ArrayList<KinectCircumscriptionMode> arrayOfModes= new ArrayList<>();
		argumentToArrayOfKinectCircumscriptionModes(value,arrayOfModes,iX);
		return arrayOfModes.toArray(new KinectCircumscriptionMode[arrayOfModes.size()]);
	}
	//
	protected static void argumentToArrayOfKinectCircumscriptionModes(Term value, ArrayList<KinectCircumscriptionMode> array, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				argumentToArrayOfKinectCircumscriptionModes(nextHead,array,iX);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			KinectCircumscriptionMode item= KinectCircumscriptionModeConverters.argumentToKinectCircumscriptionMode(value,iX);
			if (!array.contains(item)) {
				array.add(item);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(KinectCircumscriptionMode[] items) {
		if (items.length == 1) {
			return KinectCircumscriptionModeConverters.toTerm(items[0]);
		} else {
			Term value= PrologEmptyList.instance;
			for (int n=0; n < items.length; n++) {
				value= new PrologList(KinectCircumscriptionModeConverters.toTerm(items[n]),value);
			};
			return value;
		}
	}
}
