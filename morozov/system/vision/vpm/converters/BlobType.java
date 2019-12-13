// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class BlobType {
	//
	protected long code;
	protected boolean isNumericalName= false;
	//
	public BlobType(long c, boolean mode) {
		code= c;
		isNumericalName= mode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static BlobType argumentToBlobType(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			return new BlobType(code,false);
		} catch (TermIsNotASymbol e1) {
			try {
				long code= value.getLongIntegerValue(iX);
				return new BlobType(code,true);
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotBlobType(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean equals(Object o) {
		if (o==null) {
			return false;
		} else {
			if (o instanceof BlobType) {
				BlobType t= (BlobType)o;
				if (	isNumericalName==t.isNumericalName &&
					code==t.code) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (isNumericalName) {
			return new PrologInteger(code);
		} else {
			return new PrologSymbol(code);
		}
	}
}
