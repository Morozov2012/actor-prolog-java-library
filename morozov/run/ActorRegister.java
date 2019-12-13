// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.worlds.*;

public class ActorRegister {
	//
	protected ActiveWorld currentProcess;
	protected ActorNumber currentActorNumber;
	//
	public ActorRegister(ActiveWorld cP, ActorNumber rA) {
		currentProcess= cP;
		currentActorNumber= rA;
	}
	//
	public ActiveWorld getCurrentProcess() {
		return currentProcess;
	}
	public ActorNumber getCurrentActorNumber() {
		return currentActorNumber;
	}
}
