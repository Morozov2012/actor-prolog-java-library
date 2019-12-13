// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.converters;

import target.*;

import morozov.run.*;
import morozov.system.vision.vpm.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum TrackSortingCriterion {
	//
	NUMBER_OF_FRAMES {
		@Override
		public Term toTerm() {
			return term_NUMBER_OF_FRAMES;
		}
	},
	MEAN_BLOB_AREA {
		@Override
		public Term toTerm() {
			return term_MEAN_BLOB_AREA;
		}
	},
	MEAN_FOREGROUND_AREA {
		@Override
		public Term toTerm() {
			return term_MEAN_FOREGROUND_AREA;
		}
	},
	MEAN_CONTOUR_LENGTH {
		@Override
		public Term toTerm() {
			return term_MEAN_CONTOUR_LENGTH;
		}
	},
	TOTAL_DISTANCE {
		@Override
		public Term toTerm() {
			return term_TOTAL_DISTANCE;
		}
	},
	TOTAL_SHIFT {
		@Override
		public Term toTerm() {
			return term_TOTAL_SHIFT;
		}
	},
	MEAN_VELOCITY {
		@Override
		public Term toTerm() {
			return term_MEAN_VELOCITY;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static TrackSortingCriterion argumentToTrackSortingCriterion(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_NUMBER_OF_FRAMES) {
				return TrackSortingCriterion.NUMBER_OF_FRAMES;
			} else if (code==SymbolCodes.symbolCode_E_MEAN_BLOB_AREA) {
				return TrackSortingCriterion.MEAN_BLOB_AREA;
			} else if (code==SymbolCodes.symbolCode_E_MEAN_FOREGROUND_AREA) {
				return TrackSortingCriterion.MEAN_FOREGROUND_AREA;
			} else if (code==SymbolCodes.symbolCode_E_MEAN_CONTOUR_LENGTH) {
				return TrackSortingCriterion.MEAN_CONTOUR_LENGTH;
			} else if (code==SymbolCodes.symbolCode_E_TOTAL_DISTANCE) {
				return TrackSortingCriterion.TOTAL_DISTANCE;
			} else if (code==SymbolCodes.symbolCode_E_TOTAL_SHIFT) {
				return TrackSortingCriterion.TOTAL_SHIFT;
			} else if (code==SymbolCodes.symbolCode_E_MEAN_VELOCITY) {
				return TrackSortingCriterion.MEAN_VELOCITY;
			} else {
				throw new WrongArgumentIsNotTrackSortingCriterion(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTrackSortingCriterion(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_NUMBER_OF_FRAMES= new PrologSymbol(SymbolCodes.symbolCode_E_NUMBER_OF_FRAMES);
	protected static Term term_MEAN_BLOB_AREA= new PrologSymbol(SymbolCodes.symbolCode_E_MEAN_BLOB_AREA);
	protected static Term term_MEAN_FOREGROUND_AREA= new PrologSymbol(SymbolCodes.symbolCode_E_MEAN_FOREGROUND_AREA);
	protected static Term term_MEAN_CONTOUR_LENGTH= new PrologSymbol(SymbolCodes.symbolCode_E_MEAN_CONTOUR_LENGTH);
	protected static Term term_TOTAL_DISTANCE= new PrologSymbol(SymbolCodes.symbolCode_E_TOTAL_DISTANCE);
	protected static Term term_TOTAL_SHIFT= new PrologSymbol(SymbolCodes.symbolCode_E_TOTAL_SHIFT);
	protected static Term term_MEAN_VELOCITY= new PrologSymbol(SymbolCodes.symbolCode_E_MEAN_VELOCITY);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term toTerm();
}
