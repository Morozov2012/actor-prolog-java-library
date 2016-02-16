// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.space2d.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.awt.BasicStroke;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

public class StrokeAndColor extends BasicStroke {
	//
	public Color color= null;
	//
	public StrokeAndColor(Color c, float width, int cap, int join, float miterlimit) {
		super(width,cap,join,miterlimit);
		color= c;
	}
	public StrokeAndColor(Color c, float width, int cap, int join, float miterlimit, float[] dash, float dash_phase) {
		super(width,cap,join,miterlimit,dash,dash_phase);
		color= c;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static StrokeAndColor termToStrokeAndColor(Term value, ChoisePoint iX) {
		Color color= null;
		float lineWidth= 1.0f;
		int endCap= BasicStroke.CAP_SQUARE;
		int lineJoin= BasicStroke.JOIN_MITER;
		float miterLimit= 10.0f;
		float[] dashArray= null;
		float dashPhase= 0.0f;
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_color) {
					try {
						color= ExtendedColor.termToColor(pairValue,iX);
					} catch (TermIsSymbolDefault e1) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_lineWidth) {
					lineWidth= (float)Converters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_endCap) {
					endCap= termToEndCap(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_lineJoin) {
					lineJoin= termToLineJoin(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_miterLimit) {
					miterLimit= (float)Converters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_dashArray) {
					dashArray= Tools2D.termToFloatArray(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_dashPhase) {
					dashPhase= (float)Converters.argumentToReal(pairValue,iX);
				} else {
					throw new WrongArgumentIsUnknownPenAttribute(key);
				}
			};
			StrokeAndColor node;
			if (dashArray==null) {
				node= new StrokeAndColor(color,lineWidth,endCap,lineJoin,miterLimit);
			} else {
				node= new StrokeAndColor(color,lineWidth,endCap,lineJoin,miterLimit,dashArray,dashPhase);
			};
			return node;
		} else {
			try {
				color= ExtendedColor.termToColor(value,iX);
				StrokeAndColor node= new StrokeAndColor(color,lineWidth,endCap,lineJoin,miterLimit);
				return node;
			} catch (TermIsSymbolDefault e1) {
				throw new WrongArgumentIsNotPenAttributes(setEnd);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary operations
	///////////////////////////////////////////////////////////////
	//
	protected static int termToEndCap(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_CAP_BUTT) {
				return BasicStroke.CAP_BUTT;
			} else if (code==SymbolCodes.symbolCode_E_CAP_ROUND) {
				return BasicStroke.CAP_ROUND;
			} else if (code==SymbolCodes.symbolCode_E_CAP_SQUARE) {
				return BasicStroke.CAP_SQUARE;
			} else {
				throw new WrongArgumentIsNotEndCap(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotEndCap(value);
		}
	}
	//
	protected static int termToLineJoin(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_JOIN_BEVEL) {
				return BasicStroke.JOIN_BEVEL;
			} else if (code==SymbolCodes.symbolCode_E_JOIN_MITER) {
				return BasicStroke.JOIN_MITER;
			} else if (code==SymbolCodes.symbolCode_E_JOIN_ROUND) {
				return BasicStroke.JOIN_ROUND;
			} else {
				throw new WrongArgumentIsNotLineJoin(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotLineJoin(value);
		}
	}
}
