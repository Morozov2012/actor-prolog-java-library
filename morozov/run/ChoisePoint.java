// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.run.errors.*;
import morozov.terms.*;

import java.nio.charset.CharsetEncoder;

public class ChoisePoint {
	//
	protected int initialSizeOfTrail;
	protected ChoisePoint parentCP;
	protected boolean choisePointState;
	protected long choisePointNumber;
	protected ActorRegister actorRegister;
	//
	public ChoisePoint(ChoisePoint cp) {
		parentCP= cp;
		actorRegister= cp.actorRegister;
		choisePointState= true;
		initialSizeOfTrail= actorRegister.currentProcess.getTrailSize();
		choisePointNumber= actorRegister.currentProcess.incrementAndGetRecentChoisePointNumber();

	}
	public ChoisePoint(ChoisePoint cp, ActorRegister register) {
		parentCP= cp;
		actorRegister= register;
		choisePointState= true;
		initialSizeOfTrail= actorRegister.currentProcess.getTrailSize();
		choisePointNumber= actorRegister.currentProcess.incrementAndGetRecentChoisePointNumber();
	}
	//
	public int getInitialSizeOfTrail() {
		return initialSizeOfTrail;
	}
	public ChoisePoint getParentCP() {
		return parentCP;
	}
	public boolean isEnabled() {
		return choisePointState;
	}
	public long getChoisePointNumber() {
		return choisePointNumber;
	}
	public ActorRegister getActorRegister() {
		return actorRegister;
	}
	//
	public void disable(ChoisePoint baseChoisePoint) {
		if (this!=baseChoisePoint) {
			choisePointState= false;
			if (parentCP != null) {
				parentCP.disable(baseChoisePoint);
			} else {
				throw new IllegalChoisePointNumber();
			}
		}
	}
	public void disable(long baseChoisePointNumber) {
		if (choisePointNumber != baseChoisePointNumber) {
			choisePointState= false;
			if (parentCP != null) {
				parentCP.disable(baseChoisePointNumber);
			} else {
				throw new IllegalChoisePointNumber();
			}
		}
	}
	public void enable() {
		choisePointState= true;
	}
	public void pushTrail(Term v) {
		actorRegister.currentProcess.registerBinding(v);
	}
	public void freeTrail() {
		actorRegister.currentProcess.backtrack(choisePointNumber,initialSizeOfTrail);
	}
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (choisePointState) {
			return choisePointNumber + ";enabled";
		} else {
			return choisePointNumber + ";disabled";
		}
	}
}
