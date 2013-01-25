// (c) 2010 IRE RAS Alexei A. Morozov
package morozov.domains;

import morozov.terms.*;
public class DomainWorld extends DomainAlternative {
	protected long constantCode;
	public DomainWorld(long code) {
		constantCode= code;
	}
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		t= t.dereferenceValue(cp);
		// System.out.printf("(0) DomainWorld::domainAlternative: %s; term: %s\n",this,t);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			// System.out.printf("(1) DomainWorld::domainAlternative: %s; term: %s\n",this,t);
			if (t.thisIsWorld()) {
				// System.out.printf("(2) DomainWorld::domainAlternative: %s; term: %s (%s)\n",this,t,t.getClass());
				long[] classes= t.getClassHierarchy();
				// System.out.printf("(3) DomainWorld::classes.length: %s\n",classes.length);
				for (int i= 0; i < classes.length; i++) {
					// System.out.printf("(3i) DomainWorld::classes[%s]: %s\n",i,classes[i]);
					if (constantCode == classes[i]) {
						return true;
					}
				};
				classes= t.getInterfaceHierarchy();
				for (int i= 0; i < classes.length; i++) {
					if (constantCode == classes[i]) {
						return true;
					}
				};
				return false;
			} else {
				return false;
			}
		}
	}
}
