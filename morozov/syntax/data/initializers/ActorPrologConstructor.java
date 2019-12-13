// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.data.initializers.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.terms.*;

public class ActorPrologConstructor extends ActorPrologInitializer {
	//
	protected long nameCode;
	protected LabeledConstructorArgument[] arguments;
	protected boolean isProcessconstructor;
	//
	protected static Term termNil= new PrologSymbol(SymbolCodes.symbolCode_E_nil);
	//
	public ActorPrologConstructor(long c, LabeledConstructorArgument[] a, boolean iPC, int p) {
		super(p);
		nameCode= c;
		arguments= a;
		isProcessconstructor= iPC;
	}
	//
	public long getNameCode() {
		return nameCode;
	}
	public LabeledConstructorArgument[] getArguments() {
		return arguments;
	}
	public boolean isProcessconstructor() {
		return isProcessconstructor;
	}
	//
	@Override
	public void checkWhetherThereAreNoArrays(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		for (int k=0; k < arguments.length; k++) {
			arguments[k].checkWhetherThereAreNoArrays(master,iX);
		}
	}
	@Override
	public void checkWhetherThereAreNoSimpleConstructors(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (!isProcessconstructor) {
			master.handleError(new SimpleConstructorCannotBeAnArgumentOfAnArrayOfProcesses(getPosition()),iX);
		};
		for (int k=0; k < arguments.length; k++) {
			arguments[k].checkWhetherThereAreNoSimpleConstructors(master,iX);
		}
	}
	//
	@Override
	public Term toActorPrologTerm() {
		long structureNameCode;
		if (isProcessconstructor) {
			structureNameCode= SymbolCodes.symbolCode_E_process_constructor;
		} else {
			structureNameCode= SymbolCodes.symbolCode_E_simple_constructor;
		};
		Term termArguments= termNil;
		for (int k=arguments.length-1; k >= 0; k--) {
			LabeledConstructorArgument argument= arguments[k];
			Term[] termArray= new Term[5];
			termArray[0]= new PrologSymbol(argument.getNameCode());
			termArray[1]= argument.getPortVariety().toTerm();
			termArray[2]= argument.getInitializer().toActorPrologTerm();
			termArray[3]= new PrologInteger(argument.getPosition());
			termArray[4]= termArguments;
			termArguments= new PrologStructure(SymbolCodes.symbolCode_E_a,termArray);
		};
		Term[] internalArray= new Term[3];
		internalArray[0]= new PrologSymbol(nameCode);
		internalArray[1]= termArguments;
		internalArray[2]= new PrologInteger(position);
		return new PrologStructure(structureNameCode,internalArray);
	}
}
