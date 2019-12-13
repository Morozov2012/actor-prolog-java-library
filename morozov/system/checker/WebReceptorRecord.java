// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.run.*;
import morozov.worlds.*;

import java.math.BigDecimal;

public class WebReceptorRecord extends PredefinedClassRecord {
	//
	protected ActorNumber actorNumber;
	protected URL_Attributes attributes;
	protected BigDecimal revisionPeriod;
	protected BigDecimal attemptPeriod;
	//
	private static final long serialVersionUID= 0xC28BC791A248537DL; // -4428226380281916547L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.checker","WebReceptorRecord");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public WebReceptorRecord(AbstractInternalWorld world, ActorNumber aN, URL_Attributes l, BigDecimal rP, BigDecimal aP) {
		super(world);
		actorNumber= aN;
		attributes= l;
		revisionPeriod= rP;
		attemptPeriod= aP;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorNumber getActorNumber() {
		return actorNumber;
	}
	public URL_Attributes getAttributes() {
		return attributes;
	}
	public BigDecimal getRevisionPeriod() {
		return revisionPeriod;
	}
	public BigDecimal getAttemptPeriod() {
		return attemptPeriod;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if ( !(o instanceof WebReceptorRecord) ) {
				return false;
			} else {
				WebReceptorRecord r= (WebReceptorRecord) o;
				if (r.attributes.uri.equals(attributes.uri)) {
					boolean ok= attributes.connectionWasSuccessful();
					if (r.attributes.connectionWasSuccessful()==ok) {
						if (ok) {
							if (r.revisionPeriod.compareTo(revisionPeriod)==0) {
								return true;
							} else {
								return false;
							}
						} else {
							if (r.attemptPeriod.compareTo(attemptPeriod)==0) {
								return true;
							} else {
								return false;
							}
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
	}
	//
	@Override
	public int hashCode() {
		return attributes.uri.hashCode();
	}
	//
	public BigDecimal getCheckUpPeriod() {
		if (attributes.connectionWasSuccessful()) {
			return revisionPeriod;
		} else {
			return attemptPeriod;
		}
	}
}
