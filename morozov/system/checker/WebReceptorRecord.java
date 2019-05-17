// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.run.*;
import morozov.worlds.*;

import java.math.BigDecimal;

public class WebReceptorRecord extends PredefinedClassRecord {
	//
	public ActorNumber actorNumber;
	public URL_Attributes attributes;
	public BigDecimal revisionPeriod;
	public BigDecimal attemptPeriod;
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
						// if (	( ok && r.revisionPeriod.equals(revisionPeriod) ) ||
						//	( !ok && r.attemptPeriod.equals(attemptPeriod) ) ) {
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
	public int hashCode() {
		// if (attributes.connectionWasSuccessful()) {
		//	return attributes.uri.hashCode(); + revisionPeriod.hashCode();
		// } else {
		//	return attributes.uri.hashCode(); + attemptPeriod.hashCode();
		// }
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
