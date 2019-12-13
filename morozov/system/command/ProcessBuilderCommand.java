// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.command;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.command.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ProcessBuilderCommand {
	//
	protected String text;
	protected boolean isAutomatic;
	protected boolean isExtensionSpecific;
	//
	public ProcessBuilderCommand() {
		text= "";
		isAutomatic= true;
		isExtensionSpecific= false;
	}
	public ProcessBuilderCommand(String value) {
		text= value;
		isAutomatic= false;
		isExtensionSpecific= false;
	}
	public ProcessBuilderCommand(String value, boolean mode) {
		if (mode) {
			text= value;
			isAutomatic= true;
			isExtensionSpecific= true;
		} else {
			text= value;
			isAutomatic= false;
			isExtensionSpecific= false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String getText() {
		return text;
	}
	public boolean isAutomatic() {
		return isAutomatic;
	}
	public boolean isExtensionSpecific() {
		return isExtensionSpecific;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ProcessBuilderCommand argumentToProcessBuilderCommand(Term value, ChoisePoint iX) {
		try {
			String commandText= value.getStringValue(iX);
			return new ProcessBuilderCommand(commandText);
		} catch (TermIsNotAString e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_auto) {
					return new ProcessBuilderCommand();
				} else {
					throw new WrongArgumentIsNotACommand(value);
				}
			} catch (TermIsNotASymbol e2) {
				try {
					long code= value.getStructureFunctor(iX);
					if (code==SymbolCodes.symbolCode_E_auto) {
						Term[] argumentList= value.getStructureArguments(iX);
						if (argumentList.length==1) {
							String argument= GeneralConverters.argumentToString(argumentList[0],iX);
							return new ProcessBuilderCommand(argument,true);
						} else {
							throw new WrongArgumentIsNotACommand(value);
						}
					} else {
						throw new WrongArgumentIsNotACommand(value);
					}
				} catch (TermIsNotAStructure e3) {
					throw new WrongArgumentIsNotACommand(value);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isAutomatic) {
			if (isExtensionSpecific) {
				Term[] arguments= new Term[1];
				arguments[0]= new PrologString(text);
				return new PrologStructure(SymbolCodes.symbolCode_E_auto,arguments);
			} else {
				return new PrologSymbol(SymbolCodes.symbolCode_E_auto);
			}
		} else {
			return new PrologString(text);
		}
	}
}
