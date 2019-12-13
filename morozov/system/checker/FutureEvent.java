// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.run.*;

import java.math.BigInteger;
import java.math.BigDecimal;

public class FutureEvent {
	//
	protected BigInteger eventTime;
	protected ActorNumber actorNumber;
	protected URL_Attributes recentAttributes;
	protected BigDecimal revisionPeriod;
	protected BigDecimal attemptPeriod;
	//
	public FutureEvent(BigInteger eT, ActorNumber aN, URL_Attributes attributes, BigDecimal rP, BigDecimal aP) {
		eventTime= eT;
		actorNumber= aN;
		recentAttributes= attributes;
		revisionPeriod= rP;
		attemptPeriod= aP;
	}
}
