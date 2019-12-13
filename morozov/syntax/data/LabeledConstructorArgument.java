// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.run.*;
import morozov.syntax.data.initializers.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;

public class LabeledConstructorArgument {
	//
	protected long nameCode;
	protected ActorPrologInitializer initializer;
	protected ActorPrologPortVariety portVariety;
	protected int position;
	//
	public LabeledConstructorArgument(long c, ActorPrologInitializer i, ActorPrologPortVariety v, int p) {
		nameCode= c;
		initializer= i;
		portVariety= v;
		position= p;
	}
	//
	public long getNameCode() {
		return nameCode;
	}
	public ActorPrologInitializer getInitializer() {
		return initializer;
	}
	public ActorPrologPortVariety getPortVariety() {
		return portVariety;
	}
	public int getPosition() {
		return position;
	}
	//
	public void checkWhetherThereAreNoArrays(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		initializer.checkWhetherThereAreNoArrays(master,iX);
	}
	public void checkWhetherThereAreNoSimpleConstructors(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		initializer.checkWhetherThereAreNoSimpleConstructors(master,iX);
	}
}
