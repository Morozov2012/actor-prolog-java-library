// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import morozov.syntax.data.initializers.*;

public class ActorPrologConstructorArgument {
	//
	protected long functorName;
	protected ActorPrologPortVariety portVariety;
	protected ActorPrologInitializer initializer;
	protected int position;
	//
	public ActorPrologConstructorArgument(long f, ActorPrologPortVariety v, ActorPrologInitializer i, int p) {
		functorName= f;
		portVariety= v;
		initializer= i;
		position= p;
	}
	//
	public long getFunctorName() {
		return functorName;
	}
	public ActorPrologPortVariety getPortVariety() {
		return portVariety;
	}
	public ActorPrologInitializer getInitializer() {
		return initializer;
	}
	public int getPosition() {
		return position;
	}
}
