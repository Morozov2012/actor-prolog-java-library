// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.space2d.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
// import java.awt.image.BufferedImage;
import java.awt.GradientPaint;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.geom.AffineTransform;

public class BrushAttributes {
	//
	public boolean isSwitch= false;
	public boolean fillFigures= true;
	public Paint paint;
	//
	public BrushAttributes(Paint p) {
		paint= p;
		isSwitch= false;
		fillFigures= true;
	}
	public BrushAttributes(boolean flag) {
		paint= null;
		isSwitch= true;
		fillFigures= flag;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static BrushAttributes termToBrushAttributes(Term value, Canvas2D targetWorld, ChoisePoint iX) {
		try { // on, off
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_on) {
					return new BrushAttributes(true);
				} else if (code==SymbolCodes.symbolCode_E_off) {
					return new BrushAttributes(false);
				} else {
					throw Backtracking.instance;
				}
			} catch (TermIsNotASymbol b1) {
				throw Backtracking.instance;
			}
		} catch (Backtracking b2) {
		try { // GradientPaint
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_GradientPaint,1,iX);
			return new BrushAttributes(attributesToGradientPaint(arguments[0],iX));
		} catch (Backtracking b3) {
		try { // LinearGradientPaint
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_LinearGradientPaint,1,iX);
			return new BrushAttributes(attributesToLinearGradientPaint(arguments[0],iX));
		} catch (Backtracking b4) {
		try { // RadialGradientPaint
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_RadialGradientPaint,1,iX);
			return new BrushAttributes(attributesToRadialGradientPaint(arguments[0],iX));
		} catch (Backtracking b5) {
		try { // TexturePaint
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TexturePaint,1,iX);
			return new BrushAttributes(attributesToTexturePaint(arguments[0],targetWorld,iX));
		} catch (Backtracking b6) {
		try { // Color
			try {
				Color color= ExtendedColor.termToColor(value,iX);
				return new BrushAttributes(color);
			} catch (TermIsSymbolDefault b7) {
				throw Backtracking.instance;
			}
		} catch (Backtracking b8) {
			throw new WrongArgumentIsNotBrushAttributes(value);
		}
		}
		}
		}
		}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary operations
	///////////////////////////////////////////////////////////////
	//
	protected static Paint attributesToGradientPaint(Term value, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Point2D p1= null;
			Point2D p2= null;
			Color color1= null;
			Color color2= null;
			boolean isCyclic= false;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_isCyclic) {
					isCyclic= YesNo.termYesNo2Boolean(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_point1) {
					p1= Tools2D.termToPoint2D(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_point2) {
					p2= Tools2D.termToPoint2D(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_color1) {
					try {
						color1= ExtendedColor.termToColor(pairValue,iX);
					} catch (TermIsSymbolDefault e1) {
						// throw new WrongArgumentIsNotAColor(pairValue);
						color1= Color.WHITE; // Actor Prolog default value
					}
				} else if (pairName==SymbolCodes.symbolCode_E_color2) {
					try {
						color2= ExtendedColor.termToColor(pairValue,iX);
					} catch (TermIsSymbolDefault e2) {
						// throw new WrongArgumentIsNotAColor(pairValue);
						color2= Color.BLACK; // Actor Prolog default value
					}
				} else {
					throw new WrongArgumentIsUnknownGradientPaintAttribute(key);
				}
			};
			if (p1 == null) {
				throw new FirstPointIsNotDefined();
			};
			if (p2 == null) {
				throw new SecondPointIsNotDefined();
			};
			if (color1 == null) {
				throw new FirstColorIsNotDefined();
			};
			if (color2 == null) {
				throw new SecondColorIsNotDefined();
			};
			return new GradientPaint(p1,color1,p2,color2,isCyclic);
		} else {
			throw new WrongArgumentIsNotGradientPaintAttributes(setEnd);
		}
	}
	//
	protected static LinearGradientPaint attributesToLinearGradientPaint(Term value, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Point2D p1= null;
			Point2D p2= null;
			float[] fractions= null;
			Color[] colors= null;
			MultipleGradientPaint.CycleMethod cycleMethod= MultipleGradientPaint.CycleMethod.NO_CYCLE;
			MultipleGradientPaint.ColorSpaceType colorSpace= null; // MultipleGradientPaint.ColorSpaceType.SRGB;
			AffineTransform gradientTransform= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_point1) {
					p1= Tools2D.termToPoint2D(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_point2) {
					p2= Tools2D.termToPoint2D(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_fractions) {
					fractions= Tools2D.termToFractions(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_colors) {
					colors= Tools2D.termToColors(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_cycleMethod) {
					cycleMethod= Tools2D.termToCycleMethod(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_colorSpace) {
					colorSpace= Tools2D.termToColorSpaceType(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_gradientTransform) {
					gradientTransform= Tools2D.termToTransform2D(pairValue,iX);
				} else {
					throw new WrongArgumentIsUnknownLinearGradientPaintAttribute(key);
				}
			};
			if (p1 == null) {
				throw new FirstPointIsNotDefined();
			};
			if (p2 == null) {
				throw new SecondPointIsNotDefined();
			};
			if (fractions == null) {
				throw new FractionsAreNotDefined();
			};
			if (colors == null) {
				throw new ColorsAreNotDefined();
			};
			if (gradientTransform == null) {
				if (colorSpace == null) {
					return new LinearGradientPaint(p1,p2,fractions,colors,cycleMethod);
				} else {
					gradientTransform= new AffineTransform();
					return new LinearGradientPaint(p1,p2,fractions,colors,cycleMethod,colorSpace,gradientTransform);
				}
			} else {
				if (colorSpace == null) {
					colorSpace= MultipleGradientPaint.ColorSpaceType.SRGB;
				};
				return new LinearGradientPaint(p1,p2,fractions,colors,cycleMethod,colorSpace,gradientTransform);
			}
		} else {
			throw new WrongArgumentIsNotLinearGradientPaintAttributes(setEnd);
		}
	}
	//
	protected static RadialGradientPaint attributesToRadialGradientPaint(Term value, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Point2D center= null;
			float radius= 10; // Actor Prolog default value
			Point2D focus= null;
			float[] fractions= null;
			Color[] colors= null;
			MultipleGradientPaint.CycleMethod cycleMethod= MultipleGradientPaint.CycleMethod.NO_CYCLE;
			MultipleGradientPaint.ColorSpaceType colorSpace= null; // MultipleGradientPaint.ColorSpaceType.SRGB;
			AffineTransform gradientTransform= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_center) {
					center= Tools2D.termToPoint2D(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_radius) {
					radius= (float)Converters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_focus) {
					focus= Tools2D.termToPoint2D(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_fractions) {
					fractions= Tools2D.termToFractions(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_colors) {
					colors= Tools2D.termToColors(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_cycleMethod) {
					cycleMethod= Tools2D.termToCycleMethod(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_colorSpace) {
					colorSpace= Tools2D.termToColorSpaceType(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_gradientTransform) {
					gradientTransform= Tools2D.termToTransform2D(pairValue,iX);
				} else {
					throw new WrongArgumentIsUnknownRadialGradientPaintAttribute(key);
				}
			};
			if (center == null) {
				throw new CenterIsNotDefined();
			};
			if (focus == null) {
				focus= center;
			};
			if (fractions == null) {
				throw new FractionsAreNotDefined();
			};
			if (colors == null) {
				throw new ColorsAreNotDefined();
			};
			if (gradientTransform == null) {
				if (colorSpace == null) {
					return new RadialGradientPaint(center,radius,focus,fractions,colors,cycleMethod);
				} else {
					gradientTransform= new AffineTransform();
					return new RadialGradientPaint(center,radius,focus,fractions,colors,cycleMethod,colorSpace,gradientTransform);
				}
			} else {
				if (colorSpace == null) {
					colorSpace= MultipleGradientPaint.ColorSpaceType.SRGB;
				};
				return new RadialGradientPaint(center,radius,focus,fractions,colors,cycleMethod,colorSpace,gradientTransform);
			}
		} else {
			throw new WrongArgumentIsNotRadialGradientPaintAttributes(setEnd);
		}
	}
	//
	protected static TexturePaint attributesToTexturePaint(Term value, Canvas2D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			java.awt.image.BufferedImage image= null;
			Point2D anchor= null;
			double width= -1;
			double height= -1;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_image) {
					image= targetWorld.readImage(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_anchor) {
					anchor= Tools2D.termToPoint2D(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_width) {
					width= Converters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_height) {
					height= Converters.argumentToReal(pairValue,iX);
				} else {
					throw new WrongArgumentIsUnknownTexturePaintAttribute(key);
				}
			};
			if (image == null) {
				throw new ImageIsNotDefined();
			};
			double x1;
			double y1;
			if (anchor == null) {
				x1= 0;
				y1= 0;
			} else {
				x1= anchor.getX();
				y1= anchor.getY();
			};
			if (width < 0) {
				width= image.getWidth();
			};
			if (height < 0) {
				height= image.getHeight();
			};
			return new TexturePaint(image,new Rectangle2D.Double(x1,y1,width,height));
		} else {
			throw new WrongArgumentIsNotTexturePaintAttributes(setEnd);
		}
	}
}
