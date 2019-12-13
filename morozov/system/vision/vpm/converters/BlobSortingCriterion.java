// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum BlobSortingCriterion {
	//
	SIZE {
		@Override
		public Term toTerm() {
			return term_SIZE;
		}
	},
	FOREGROUND_AREA {
		@Override
		public Term toTerm() {
			return term_FOREGROUND_AREA;
		}
	},
	CONTOUR_LENGTH {
		@Override
		public Term toTerm() {
			return term_CONTOUR_LENGTH;
		}
	},
	CENTROID_X {
		@Override
		public Term toTerm() {
			return term_CENTROID_X;
		}
	},
	CENTROID_Y {
		@Override
		public Term toTerm() {
			return term_CENTROID_Y;
		}
	},
	LEFT_X {
		@Override
		public Term toTerm() {
			return term_LEFT_X;
		}
	},
	RIGHT_X {
		@Override
		public Term toTerm() {
			return term_RIGHT_X;
		}
	},
	TOP_Y {
		@Override
		public Term toTerm() {
			return term_TOP_Y;
		}
	},
	BOTTOM_Y {
		@Override
		public Term toTerm() {
			return term_BOTTOM_Y;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static BlobSortingCriterion argumentToBlobSortingCriterion(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_SIZE) {
				return BlobSortingCriterion.SIZE;
			} else if (code==SymbolCodes.symbolCode_E_FOREGROUND_AREA) {
				return BlobSortingCriterion.FOREGROUND_AREA;
			} else if (code==SymbolCodes.symbolCode_E_CONTOUR_LENGTH) {
				return BlobSortingCriterion.CONTOUR_LENGTH;
			} else if (code==SymbolCodes.symbolCode_E_CENTROID_X) {
				return BlobSortingCriterion.CENTROID_X;
			} else if (code==SymbolCodes.symbolCode_E_CENTROID_Y) {
				return BlobSortingCriterion.CENTROID_Y;
			} else if (code==SymbolCodes.symbolCode_E_LEFT_X) {
				return BlobSortingCriterion.LEFT_X;
			} else if (code==SymbolCodes.symbolCode_E_RIGHT_X) {
				return BlobSortingCriterion.RIGHT_X;
			} else if (code==SymbolCodes.symbolCode_E_TOP_Y) {
				return BlobSortingCriterion.TOP_Y;
			} else if (code==SymbolCodes.symbolCode_E_BOTTOM_Y) {
				return BlobSortingCriterion.BOTTOM_Y;
			} else {
				throw new WrongArgumentIsNotBlobSortingCriterion(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotBlobSortingCriterion(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_SIZE= new PrologSymbol(SymbolCodes.symbolCode_E_SIZE);
	protected static Term term_FOREGROUND_AREA= new PrologSymbol(SymbolCodes.symbolCode_E_FOREGROUND_AREA);
	protected static Term term_CONTOUR_LENGTH= new PrologSymbol(SymbolCodes.symbolCode_E_CONTOUR_LENGTH);
	protected static Term term_CENTROID_X= new PrologSymbol(SymbolCodes.symbolCode_E_CENTROID_X);
	protected static Term term_CENTROID_Y= new PrologSymbol(SymbolCodes.symbolCode_E_CENTROID_Y);
	protected static Term term_LEFT_X= new PrologSymbol(SymbolCodes.symbolCode_E_LEFT_X);
	protected static Term term_RIGHT_X= new PrologSymbol(SymbolCodes.symbolCode_E_RIGHT_X);
	protected static Term term_TOP_Y= new PrologSymbol(SymbolCodes.symbolCode_E_TOP_Y);
	protected static Term term_BOTTOM_Y= new PrologSymbol(SymbolCodes.symbolCode_E_BOTTOM_Y);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
