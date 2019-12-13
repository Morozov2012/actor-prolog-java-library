// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
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
	protected Color color= null;
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
	public static StrokeAndColor argumentToStrokeAndColor(Term value, ChoisePoint iX) {
		Color color= null;
		float lineWidth= 1.0f;
		int endCap= BasicStroke.CAP_SQUARE;
		int lineJoin= BasicStroke.JOIN_MITER;
		float miterLimit= 10.0f;
		float[] dashArray= null;
		float dashPhase= 0.0f;
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
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
						color= ColorAttributeConverters.argumentToColor(pairValue,iX);
					} catch (TermIsSymbolDefault e1) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_lineWidth) {
					lineWidth= (float)GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_endCap) {
					endCap= argumentToEndCap(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_lineJoin) {
					lineJoin= argumentToLineJoin(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_miterLimit) {
					miterLimit= (float)GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_dashArray) {
					dashArray= Tools2D.argumentToFloatArray(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_dashPhase) {
					dashPhase= (float)GeneralConverters.argumentToReal(pairValue,iX);
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
				color= ColorAttributeConverters.argumentToColor(value,iX);
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
	protected static int argumentToEndCap(Term value, ChoisePoint iX) {
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
	protected static int argumentToLineJoin(Term value, ChoisePoint iX) {
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
