// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class ColorMap extends DataAbstraction {
	//
	public ColorMap() {
	}
	public ColorMap(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void stringToColorMapName1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		String text= GeneralConverters.argumentToString(a1,iX);
		ColorMapName currentName= ColorMapNameConverters.stringToColorMapName(text,a1);
		result.setNonBacktrackableValue(ColorMapNameConverters.toTerm(currentName));
	}
	public void stringToColorMapName1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void stringToColorChannel1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		String text= GeneralConverters.argumentToString(a1,iX);
		ColorChannelSubstitutionName currentName= ColorSubstitutionModeConverters.stringToColorChannelSubstitutionName(text,a1);
		result.setNonBacktrackableValue(ColorSubstitutionModeConverters.toTerm(currentName));
	}
	public void stringToColorChannel1fs(ChoisePoint iX, Term a1) {
	}
}
