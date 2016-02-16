// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
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
		try {
			textString= new StringBuilder(inputText.getStringValue(iX));
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(inputText);
		}
	}
	//
	public void getString0ff(ChoisePoint iX, PrologVariable outputText) {
		outputText.value= new PrologString(textString.toString());
	}
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
