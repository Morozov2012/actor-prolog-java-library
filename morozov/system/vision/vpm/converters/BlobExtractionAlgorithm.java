// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum BlobExtractionAlgorithm {
	//
	TWO_PASS_BLOB_EXTRACTION {
		public Term toTerm() {
			return term_TWO_PASS_BLOB_EXTRACTION;
		}
	},
	MULTIPASS_BLOB_EXTRACTION {
		public Term toTerm() {
			return term_MULTIPASS_BLOB_EXTRACTION;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static BlobExtractionAlgorithm argumentToBlobExtractionAlgorithm(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_TWO_PASS_BLOB_EXTRACTION) {
				return BlobExtractionAlgorithm.TWO_PASS_BLOB_EXTRACTION;
			} else if (code==SymbolCodes.symbolCode_E_MULTIPASS_BLOB_EXTRACTION) {
				return BlobExtractionAlgorithm.MULTIPASS_BLOB_EXTRACTION;
			} else {
				throw new WrongArgumentIsNotBlobExtractionAlgorithm(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotBlobExtractionAlgorithm(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_TWO_PASS_BLOB_EXTRACTION= new PrologSymbol(SymbolCodes.symbolCode_E_TWO_PASS_BLOB_EXTRACTION);
	protected static Term term_MULTIPASS_BLOB_EXTRACTION= new PrologSymbol(SymbolCodes.symbolCode_E_MULTIPASS_BLOB_EXTRACTION);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
