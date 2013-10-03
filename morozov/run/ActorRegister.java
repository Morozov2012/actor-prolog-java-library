// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.classes.*;

public class ActorRegister {
	public ActiveWorld currentProcess;
	public ActorNumber currentActorNumber;
	public ActorRegister(ActiveWorld cP, ActorNumber rA) {
		currentProcess= cP;
		currentActorNumber= rA;
	}
}
