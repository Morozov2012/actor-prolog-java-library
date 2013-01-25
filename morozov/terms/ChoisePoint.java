// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;

import java.nio.charset.CharsetEncoder;

public class ChoisePoint {
	//
	private int initialSizeOfTrail;
	public ChoisePoint parentCP;
	private boolean choisePointState;
	private long choisePointNumber;
	public ActorRegister actorRegister;
	//
	public ChoisePoint(ChoisePoint cp) {
		parentCP= cp;
		actorRegister= cp.actorRegister;
		choisePointState= true;
		initialSizeOfTrail= actorRegister.currentProcess.trail.size();
		choisePointNumber= ++ actorRegister.currentProcess.recentChoisePointNumber;

	}
	public ChoisePoint(ChoisePoint cp, ActorRegister register) {
		parentCP= cp;
		actorRegister= register;
		choisePointState= true;
		initialSizeOfTrail= actorRegister.currentProcess.trail.size();
		choisePointNumber= ++ actorRegister.currentProcess.recentChoisePointNumber;
	}
	//
	public boolean isEnabled() {
		return choisePointState;
	}
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
	public long getChoisePointNumber() {
		return choisePointNumber;
	}
	public void pushTrail(Term v) {
		actorRegister.currentProcess.registerBinding(v);
	}
	public void freeTrail() {
		actorRegister.currentProcess.backtrack(choisePointNumber,initialSizeOfTrail);
	}
	// public String toString() {
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		if (choisePointState)
			// return "true;" + actorRegister.toString();
			return choisePointNumber + ";enabled";
			// return "true";
		else
			// return "false;" + actorRegister.toString();
			return choisePointNumber + ";disabled";
			// return "false";
	}
}
