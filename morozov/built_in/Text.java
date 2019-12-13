// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class Text extends SymbolicInformation {
	//
	protected StringBuilder textString= new StringBuilder();
	//
	///////////////////////////////////////////////////////////////
	//
	public Text() {
	}
	public Text(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setString1s(ChoisePoint iX, Term inputText) {
		textString= new StringBuilder(GeneralConverters.argumentToString(inputText,iX));
	}
	//
	@Override
	public void getString0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologString(textString.toString()));
	}
	@Override
	public void getString0fs(ChoisePoint iX) {
	}
	//
	public void clear0s(ChoisePoint iX) {
		textString= new StringBuilder();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void write1ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToString(iX,(Term[])args);
		textString.append(textBuffer);
	}
	//
	public void writeLn1ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToString(iX,(Term[])args);
		textString.append(textBuffer);
		textString.append("\n");
	}
	//
	public void writeF2ms(ChoisePoint iX, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToFormattedString(iX,(Term[])args);
		textString.append(textBuffer);
	}
	//
	public void newLine0s(ChoisePoint iX) {
		textString.append("\n");
	}
}
