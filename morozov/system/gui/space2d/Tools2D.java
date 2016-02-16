// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.gui.*;
import morozov.system.gui.space2d.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.awt.geom.Arc2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.Paint;
import java.awt.MultipleGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.AlphaComposite;
import java.awt.Transparency;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.math.BigInteger;

class FirstPointIsNotDefined extends RuntimeException {}
class SecondPointIsNotDefined extends RuntimeException {}
class FirstColorIsNotDefined extends RuntimeException {}
class SecondColorIsNotDefined extends RuntimeException {}
class ColorsAreNotDefined extends RuntimeException {}
class FractionsAreNotDefined extends RuntimeException {}
class CenterIsNotDefined extends RuntimeException {}
class ImageIsNotDefined extends RuntimeException {}

public class Tools2D {
	//
	public static int defaultTransparency= 0; // JDK uses 1, 2, 3
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	///////////////////////////////////////////////////////////////
	//
	public static int termToArcClosureType(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_OPEN) {
				return Arc2D.OPEN;
			} else if (code==SymbolCodes.symbolCode_E_CHORD) {
				return Arc2D.CHORD;
			} else if (code==SymbolCodes.symbolCode_E_PIE) {
				return Arc2D.PIE;
			} else {
				throw new WrongArgumentIsNotArcClosureType(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotArcClosureType(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Point2D termToPoint2D(Term value, ChoisePoint iX) {
		try {
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_p,2,iX);
			double xPoint= Converters.argumentToReal(arguments[0],iX);
			double yPoint= Converters.argumentToReal(arguments[1],iX);
			return new Point2D.Double(xPoint,yPoint);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAPoint2D(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static float[] termToFloatArray(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		float[] result= new float[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			result[n]= (float)Converters.argumentToReal(termArray[n],iX);
		};
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static RenderingHints termToRenderingHints(Term value, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			RenderingHints rh = new RenderingHints(null);
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_antialiasing) {
					try {
						boolean mode= OnOff.termOnOffDefault2Boolean(pairValue,iX);
						if (mode) {
							rh.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
						} else {
							rh.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
						}
					} catch (TermIsSymbolDefault e1) {
						rh.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_DEFAULT);
					}
				} else if (pairName==SymbolCodes.symbolCode_E_rendering) {
					Object mode= termToRenderingStrategy(pairValue,iX);
					rh.put(RenderingHints.KEY_RENDERING,mode);
				} else if (pairName==SymbolCodes.symbolCode_E_dithering) {
					Object mode= termToDitheringMode(pairValue,iX);
					rh.put(RenderingHints.KEY_DITHERING,mode);
				} else if (pairName==SymbolCodes.symbolCode_E_textAntialiasing) {
					Object mode= termToTextAntialiasingMode(pairValue,iX);
					rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,mode);
				} else if (pairName==SymbolCodes.symbolCode_E_text_LCD_contrast) {
					try {
						BigInteger contrast= Converters.termToStrictInteger(pairValue,iX,false);
						Integer mode= PrologInteger.toInteger(contrast);
						rh.put(RenderingHints.KEY_TEXT_LCD_CONTRAST,mode);
					} catch (TermIsNotAnInteger e1) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					}
				} else if (pairName==SymbolCodes.symbolCode_E_fractionalMetrics) {
					try {
						boolean mode= OnOff.termOnOffDefault2Boolean(pairValue,iX);
						if (mode) {
							rh.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
						} else {
							rh.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
						}
					} catch (TermIsSymbolDefault e1) {
						rh.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
					}
				} else if (pairName==SymbolCodes.symbolCode_E_interpolation) {
					Object mode= termToInterpolationMode(pairValue,iX);
					rh.put(RenderingHints.KEY_INTERPOLATION,mode);
				} else if (pairName==SymbolCodes.symbolCode_E_alphaInterpolation) {
					Object mode= termToAlphaInterpolationMode(pairValue,iX);
					rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION,mode);
				} else if (pairName==SymbolCodes.symbolCode_E_colorRendering) {
					Object mode= termToColorRenderingMode(pairValue,iX);
					rh.put(RenderingHints.KEY_COLOR_RENDERING,mode);
				} else if (pairName==SymbolCodes.symbolCode_E_strokeControl) {
					Object mode= termToStrokeControlMode(pairValue,iX);
					rh.put(RenderingHints.KEY_STROKE_CONTROL,mode);
				} else {
					throw new WrongArgumentIsUnknownRenderingAttribute(key);
				}
			};
			return rh;
		} else {
			throw new WrongArgumentIsNotRenderingAttributes(setEnd);
		}
	}
	//
	protected static Object termToRenderingStrategy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_SPEED) {
				return RenderingHints.VALUE_RENDER_SPEED;
			} else if (code==SymbolCodes.symbolCode_E_QUALITY) {
				return RenderingHints.VALUE_RENDER_QUALITY;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return RenderingHints.VALUE_RENDER_DEFAULT;
			} else {
				throw new WrongArgumentIsNotRenderingStrategy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotRenderingStrategy(value);
		}
	}
	//
	protected static Object termToDitheringMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_DISABLE) {
				return RenderingHints.VALUE_DITHER_DISABLE;
			} else if (code==SymbolCodes.symbolCode_E_ENABLE) {
				return RenderingHints.VALUE_DITHER_ENABLE;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return RenderingHints.VALUE_DITHER_DEFAULT;
			} else {
				throw new WrongArgumentIsNotDitheringMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotDitheringMode(value);
		}
	}
	//
	protected static Object termToTextAntialiasingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
			} else if (code==SymbolCodes.symbolCode_E_GASP) {
				return RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
			} else if (code==SymbolCodes.symbolCode_E_LCD_HRGB) {
				return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
			} else if (code==SymbolCodes.symbolCode_E_LCD_HBGR) {
				return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
			} else if (code==SymbolCodes.symbolCode_E_LCD_VRGB) {
				return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
			} else if (code==SymbolCodes.symbolCode_E_LCD_VBGR) {
				return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
			} else {
				throw new WrongArgumentIsNotTextAntialiasingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTextAntialiasingMode(value);
		}
	}
	//
	protected static Object termToInterpolationMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_NEAREST_NEIGHBOR) {
				return RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
			} else if (code==SymbolCodes.symbolCode_E_BILINEAR) {
				return RenderingHints.VALUE_INTERPOLATION_BILINEAR;
			} else if (code==SymbolCodes.symbolCode_E_BICUBIC) {
				return RenderingHints.VALUE_INTERPOLATION_BICUBIC;
			} else {
				throw new WrongArgumentIsNotInterpolationMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotInterpolationMode(value);
		}
	}
	//
	protected static Object termToAlphaInterpolationMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_SPEED) {
				return RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
			} else if (code==SymbolCodes.symbolCode_E_QUALITY) {
				return RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT;
			} else {
				throw new WrongArgumentIsNotAlphaInterpolationMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotAlphaInterpolationMode(value);
		}
	}
	//
	protected static Object termToColorRenderingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_SPEED) {
				return RenderingHints.VALUE_COLOR_RENDER_SPEED;
			} else if (code==SymbolCodes.symbolCode_E_QUALITY) {
				return RenderingHints.VALUE_COLOR_RENDER_QUALITY;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return RenderingHints.VALUE_COLOR_RENDER_DEFAULT;
			} else {
				throw new WrongArgumentIsNotColorRenderingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotColorRenderingMode(value);
		}
	}
	//
	protected static Object termToStrokeControlMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_NORMALIZE) {
				return RenderingHints.VALUE_STROKE_NORMALIZE;
			} else if (code==SymbolCodes.symbolCode_E_PURE) {
				return RenderingHints.VALUE_STROKE_PURE;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return RenderingHints.VALUE_STROKE_DEFAULT;
			} else {
				throw new WrongArgumentIsNotStrokeControlMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotStrokeControlMode(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static float[] termToFractions(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		float[] fractions= new float[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			fractions[n]= (float)Converters.argumentToReal(termArray[n],iX);
		};
		return fractions;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Color[] termToColors(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		Color[] colors= new Color[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			try {
				colors[n]= ExtendedColor.termToColor(termArray[n],iX);
			} catch (TermIsSymbolDefault e2) {
				// throw new WrongArgumentIsNotAColor(pairValue);
				if (n % 2 == 0) {
					colors[n]= Color.WHITE; // Actor Prolog default value
				} else {
					colors[n]= Color.BLACK; // Actor Prolog default value
				}
			}
		};
		return colors;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static MultipleGradientPaint.CycleMethod termToCycleMethod(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_NO_CYCLE) {
				return MultipleGradientPaint.CycleMethod.NO_CYCLE;
			} else if (code==SymbolCodes.symbolCode_E_REFLECT) {
				return MultipleGradientPaint.CycleMethod.REFLECT;
			} else if (code==SymbolCodes.symbolCode_E_REPEAT) {
				return MultipleGradientPaint.CycleMethod.REPEAT;
			} else {
				throw new WrongArgumentIsNotCycleMethod(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotCycleMethod(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static MultipleGradientPaint.ColorSpaceType termToColorSpaceType(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_LINEAR_RGB) {
				return MultipleGradientPaint.ColorSpaceType.LINEAR_RGB;
			} else if (code==SymbolCodes.symbolCode_E_SRGB) {
				return MultipleGradientPaint.ColorSpaceType.SRGB;
			} else {
				throw new WrongArgumentIsNotColorSpaceType(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotColorSpaceType(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static AffineTransform termToTransform2D(Term value, ChoisePoint iX) {
		try { // rotation
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_rotation,1,iX);
			double theta= Converters.argumentToReal(arguments[0],iX);
			AffineTransform transform= new AffineTransform();
			transform.setToRotation(theta);
			return transform;
		} catch (Backtracking b1) {
		try { // quadrantRotation
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_quadrantRotation,1,iX);
			BigInteger quadrants= Converters.argumentToRoundInteger(arguments[0],iX);
			int numquadrants= PrologInteger.toInteger(quadrants);
			AffineTransform transform= new AffineTransform();
			transform.setToQuadrantRotation(numquadrants);
			return transform;
		} catch (Backtracking b2) {
		try { // scale
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_scale,2,iX);
			double sx= Converters.argumentToReal(arguments[0],iX);
			double sy= Converters.argumentToReal(arguments[1],iX);
			AffineTransform transform= new AffineTransform();
			transform.setToScale(sx,sy);
			return transform;
		} catch (Backtracking b3) {
		try { // shear
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_shear,2,iX);
			double shx= Converters.argumentToReal(arguments[0],iX);
			double shy= Converters.argumentToReal(arguments[1],iX);
			AffineTransform transform= new AffineTransform();
			transform.setToShear(shx,shy);
			return transform;
		} catch (Backtracking b4) {
		try { // translation
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_translation,2,iX);
			double tx= Converters.argumentToReal(arguments[0],iX);
			double ty= Converters.argumentToReal(arguments[1],iX);
			AffineTransform transform= new AffineTransform();
			transform.setToTranslation(tx,ty);
			return transform;
		} catch (Backtracking b5) {
			// TranslationMatrix
			Term[] termArray= Converters.listToArray(value,iX);
			int numberOfRows= termArray.length;
			double[][] rows= new double[numberOfRows][];
			int numberOfColumns= -1;
			for (int n=0; n < numberOfRows; n++) {
				Term[] columnArray= Converters.listToArray(termArray[n],iX);
				if (numberOfColumns >= 0) {
					if (numberOfColumns != columnArray.length) {
						throw new WrongArgumentIsNotAMatrix(value);
					}
				} else {
					numberOfColumns= columnArray.length;
				};
				rows[n]= new double[numberOfColumns];
				for (int m=0; m < numberOfColumns; m++) {
					rows[n][m]= Converters.argumentToReal(columnArray[m],iX);
				}
			};
			if (numberOfRows==2 && numberOfColumns==2) {
				double[] vector= new double[4];
				vector[0]= rows[0][0];
				vector[1]= rows[1][0];
				vector[2]= rows[0][1];
				vector[3]= rows[1][1];
				AffineTransform transform= new AffineTransform(vector);
				return transform;
			} else if (numberOfRows==2 && numberOfColumns==3) {
				double[] vector= new double[6];
				vector[0]= rows[0][0];
				vector[1]= rows[1][0];
				vector[2]= rows[0][1];
				vector[3]= rows[1][1];
				vector[4]= rows[0][2];
				vector[5]= rows[1][2];
				AffineTransform transform= new AffineTransform(vector);
				return transform;
			} else {
				throw new MatrixIsNotAffineTransform(value);
			}
		}
		}
		}
		}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Font termToFont2D(Term value, Canvas2D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			HashMap<TextAttribute,Object> fontAttributes= new HashMap<TextAttribute,Object>();
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_name) {
					try {
						String faceName= termToFontName2D(pairValue,iX);
						fontAttributes.put(TextAttribute.FAMILY,faceName);
					} catch (TermIsSymbolDefault e) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_size) {
					try {
						int size= ExtendedFontSize.termToFontSize(pairValue,iX);
						int realFontSize= DefaultOptions.fontSystemSimulationMode.simulate(size);
						fontAttributes.put(TextAttribute.SIZE,realFontSize);
					} catch (TermIsSymbolDefault e) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_weight) {
					Number weight= termToFontWeight2D(pairValue,iX);
					fontAttributes.put(TextAttribute.WEIGHT,weight);
				} else if (pairName==SymbolCodes.symbolCode_E_width) {
					Number width= termToFontWidth2D(pairValue,iX);
					fontAttributes.put(TextAttribute.WIDTH,width);
				} else if (pairName==SymbolCodes.symbolCode_E_posture) {
					Number posture= termToFontPosture2D(pairValue,iX);
					fontAttributes.put(TextAttribute.POSTURE,posture);
				} else if (pairName==SymbolCodes.symbolCode_E_transform) {
					AffineTransform transform= termToTransform2D(pairValue,iX);
					fontAttributes.put(TextAttribute.TRANSFORM,transform);
				} else if (pairName==SymbolCodes.symbolCode_E_superscript) {
					Integer superscript= termToFontSuperscript2D(pairValue,iX);
					fontAttributes.put(TextAttribute.SUPERSCRIPT,superscript);
				} else if (pairName==SymbolCodes.symbolCode_E_foreground) {
					BrushAttributes attributes= BrushAttributes.termToBrushAttributes(pairValue,targetWorld,iX);
					if (attributes.isSwitch) {
						if (!attributes.fillFigures) {
							Paint paint= new Color(0,0,0,0);
							fontAttributes.put(TextAttribute.FOREGROUND,paint);
						}
					} else {
						Paint paint= attributes.paint;
						fontAttributes.put(TextAttribute.FOREGROUND,paint);
					}
				} else if (pairName==SymbolCodes.symbolCode_E_background) {
					BrushAttributes attributes= BrushAttributes.termToBrushAttributes(pairValue,targetWorld,iX);
					if (attributes.isSwitch) {
						// if (!attributes.fillFigures) {
						//	Paint paint= new Color(0,0,0,0);
						//	fontAttributes.put(TextAttribute.BACKGROUND,paint);
						// }
					} else {
						Paint paint= attributes.paint;
						fontAttributes.put(TextAttribute.BACKGROUND,paint);
					}
				} else if (pairName==SymbolCodes.symbolCode_E_underline) {
					Integer underline= termToFontUnderline2D(pairValue,iX);
					fontAttributes.put(TextAttribute.UNDERLINE,underline);
				} else if (pairName==SymbolCodes.symbolCode_E_strikethrough) {
					Boolean strikethrough= OnOff.termOnOff2Boolean(pairValue,iX);
					fontAttributes.put(TextAttribute.STRIKETHROUGH,strikethrough);
				} else if (pairName==SymbolCodes.symbolCode_E_runDirection) {
					try {
						Boolean runDirection= termToFontRunDirection2D(pairValue,iX);
						fontAttributes.put(TextAttribute.RUN_DIRECTION,runDirection);
					} catch (TermIsSymbolDefault e) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_bidiEmbedding) {
					try {
						Integer bidiEmbedding= termToFontBidiEmbedding2D(pairValue,iX);
						fontAttributes.put(TextAttribute.BIDI_EMBEDDING,bidiEmbedding);
					} catch (TermIsSymbolDefault e) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_justification) {
					Number justification= termToFontJustification2D(pairValue,iX);
					fontAttributes.put(TextAttribute.JUSTIFICATION,justification);
				} else if (pairName==SymbolCodes.symbolCode_E_inputMethodUnderline) {
					Integer inputMethodUnderline= termToInputMethodUnderline(pairValue,iX);
					fontAttributes.put(TextAttribute.INPUT_METHOD_HIGHLIGHT,inputMethodUnderline);
				} else if (pairName==SymbolCodes.symbolCode_E_swapColors) {
					Boolean swapColors= YesNo.termYesNo2Boolean(pairValue,iX);
					fontAttributes.put(TextAttribute.SWAP_COLORS,swapColors);
				} else if (pairName==SymbolCodes.symbolCode_E_kerning) {
					Integer kerning= termToFontKerning2D(pairValue,iX);
					fontAttributes.put(TextAttribute.KERNING,kerning);
				} else if (pairName==SymbolCodes.symbolCode_E_ligatures) {
					Integer ligatures= termToFontLigatures2D(pairValue,iX);
					fontAttributes.put(TextAttribute.LIGATURES,ligatures);
				} else if (pairName==SymbolCodes.symbolCode_E_tracking) {
					Number tracking= termToFontTracking2D(pairValue,iX);
					fontAttributes.put(TextAttribute.TRACKING,tracking);
				} else {
					throw new WrongArgumentIsUnknownFontAttribute(key);
				}
			};
			return new Font(fontAttributes);
		} else {
			throw new WrongArgumentIsNotFontAttributes(setEnd);
		}
	}
	//
	protected static String termToFontName2D(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_DIALOG) {
				return Font.DIALOG;
			} else if (code==SymbolCodes.symbolCode_E_DIALOG_INPUT) {
				return Font.DIALOG_INPUT;
			} else if (code==SymbolCodes.symbolCode_E_SANS_SERIF) {
				return Font.SANS_SERIF;
			} else if (code==SymbolCodes.symbolCode_E_SERIF) {
				return Font.SERIF;
			} else if (code==SymbolCodes.symbolCode_E_MONOSPACED) {
				return Font.MONOSPACED;
			} else {
				throw new WrongArgumentIsNotFontName2D(value);
			}
		} catch (TermIsNotASymbol e) {
			return ExtendedFontName.termToFontName(value,iX);
		}
	}
	//
	protected static Number termToFontWeight2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_WEIGHT_EXTRA_LIGHT) {
				return TextAttribute.WEIGHT_EXTRA_LIGHT;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_LIGHT) {
				return TextAttribute.WEIGHT_LIGHT;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_DEMILIGHT) {
				return TextAttribute.WEIGHT_DEMILIGHT;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_REGULAR) {
				return TextAttribute.WEIGHT_REGULAR;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_SEMIBOLD) {
				return TextAttribute.WEIGHT_SEMIBOLD;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_MEDIUM) {
				return TextAttribute.WEIGHT_MEDIUM;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_DEMIBOLD) {
				return TextAttribute.WEIGHT_DEMIBOLD;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_BOLD) {
				return TextAttribute.WEIGHT_BOLD;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_HEAVY) {
				return TextAttribute.WEIGHT_HEAVY;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_EXTRABOLD) {
				return TextAttribute.WEIGHT_EXTRABOLD;
			} else if (code==SymbolCodes.symbolCode_E_WEIGHT_ULTRABOLD) {
				return TextAttribute.WEIGHT_ULTRABOLD;
			} else {
				throw new WrongArgumentIsNotFontWeight2D(value);
			}
		} catch (TermIsNotASymbol e) {
			return Converters.argumentToReal(value,iX);
		}
	}
	//
	protected static Number termToFontWidth2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_WIDTH_CONDENSED) {
				return TextAttribute.WIDTH_CONDENSED;
			} else if (code==SymbolCodes.symbolCode_E_WIDTH_SEMI_CONDENSED) {
				return TextAttribute.WIDTH_SEMI_CONDENSED;
			} else if (code==SymbolCodes.symbolCode_E_WIDTH_REGULAR) {
				return TextAttribute.WIDTH_REGULAR;
			} else if (code==SymbolCodes.symbolCode_E_WIDTH_SEMI_EXTENDED) {
				return TextAttribute.WIDTH_SEMI_EXTENDED;
			} else if (code==SymbolCodes.symbolCode_E_WIDTH_EXTENDED) {
				return TextAttribute.WIDTH_EXTENDED;
			} else {
				throw new WrongArgumentIsNotFontWidth2D(value);
			}
		} catch (TermIsNotASymbol e) {
			return Converters.argumentToReal(value,iX);
		}
	}
	//
	protected static Number termToFontPosture2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_POSTURE_REGULAR) {
				return TextAttribute.POSTURE_REGULAR;
			} else if (code==SymbolCodes.symbolCode_E_POSTURE_OBLIQUE) {
				return TextAttribute.POSTURE_OBLIQUE;
			} else {
				throw new WrongArgumentIsNotFontPosture2D(value);
			}
		} catch (TermIsNotASymbol e) {
			return Converters.argumentToReal(value,iX);
		}
	}
	//
	protected static Integer termToFontSuperscript2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_SUPERSCRIPT_SUPER) {
				return TextAttribute.SUPERSCRIPT_SUPER;
			} else if (code==SymbolCodes.symbolCode_E_SUPERSCRIPT_SUB) {
				return TextAttribute.SUPERSCRIPT_SUB;
			} else {
				throw new WrongArgumentIsNotFontSuperscript2D(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return PrologInteger.toInteger(Converters.termToRoundInteger(value,iX,false));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFontSuperscript2D(value);
			}
		}
	}
	//
	protected static Integer termToFontUnderline2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return TextAttribute.UNDERLINE_ON;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return -1; // The default value is -1, which means no underline.
			} else if (code==SymbolCodes.symbolCode_E_ONE_PIXEL) {
				return TextAttribute.UNDERLINE_LOW_ONE_PIXEL; // = 1; Actor Prolog default value
			} else if (code==SymbolCodes.symbolCode_E_TWO_PIXEL) {
				return TextAttribute.UNDERLINE_LOW_TWO_PIXEL; // = 2; Actor Prolog default value
			} else if (code==SymbolCodes.symbolCode_E_DOTTED) {
				return TextAttribute.UNDERLINE_LOW_DOTTED; // = 3; Actor Prolog default value
			} else if (code==SymbolCodes.symbolCode_E_GRAY) {
				return TextAttribute.UNDERLINE_LOW_GRAY; // = 4; Actor Prolog default value
			} else if (code==SymbolCodes.symbolCode_E_DASHED) {
				return TextAttribute.UNDERLINE_LOW_DASHED; // = 5; Actor Prolog default value
			} else {
				throw new WrongArgumentIsNotFontUnderline2D(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return PrologInteger.toInteger(Converters.termToRoundInteger(value,iX,false));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFontUnderline2D(value);
			}
		}
	}
	//
	protected static Boolean termToFontRunDirection2D(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_RUN_DIRECTION_LTR) {
				return TextAttribute.RUN_DIRECTION_LTR;
			} else if (code==SymbolCodes.symbolCode_E_RUN_DIRECTION_RTL) {
				return TextAttribute.RUN_DIRECTION_RTL;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				throw TermIsSymbolDefault.instance;
			} else {
				throw new WrongArgumentIsNotFontRunDirection2D(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotFontRunDirection2D(value);
		}
	}
	//
	protected static Integer termToFontBidiEmbedding2D(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				throw TermIsSymbolDefault.instance;
			} else {
				throw new WrongArgumentIsNotFontBidiEmbedding2D(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return PrologInteger.toInteger(Converters.termToRoundInteger(value,iX,false));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFontBidiEmbedding2D(value);
			}
		}
	}
	//
	protected static Number termToFontJustification2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_JUSTIFICATION_FULL) {
				return TextAttribute.JUSTIFICATION_FULL;
			} else if (code==SymbolCodes.symbolCode_E_JUSTIFICATION_NONE) {
				return TextAttribute.JUSTIFICATION_NONE;
			} else {
				throw new WrongArgumentIsNotFontJustification2D(value);
			}
		} catch (TermIsNotASymbol e) {
			return Converters.argumentToReal(value,iX);
		}
	}
	//
	protected static Integer termToInputMethodUnderline(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return TextAttribute.UNDERLINE_LOW_ONE_PIXEL; // Actor Prolog default value
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return -1; // The default value is -1, which means no underline.
			} else if (code==SymbolCodes.symbolCode_E_ONE_PIXEL) {
				return TextAttribute.UNDERLINE_LOW_ONE_PIXEL;
			} else if (code==SymbolCodes.symbolCode_E_TWO_PIXEL) {
				return TextAttribute.UNDERLINE_LOW_TWO_PIXEL;
			} else if (code==SymbolCodes.symbolCode_E_DOTTED) {
				return TextAttribute.UNDERLINE_LOW_DOTTED;
			} else if (code==SymbolCodes.symbolCode_E_GRAY) {
				return TextAttribute.UNDERLINE_LOW_GRAY;
			} else if (code==SymbolCodes.symbolCode_E_DASHED) {
				return TextAttribute.UNDERLINE_LOW_DASHED;
			} else {
				throw new WrongArgumentIsNotFontUnderline2D(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return PrologInteger.toInteger(Converters.termToRoundInteger(value,iX,false));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFontUnderline2D(value);
			}
		}
	}
	//
	protected static Integer termToFontKerning2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return TextAttribute.KERNING_ON;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return 0; // The default value is 0, which does not request kerning.
			} else {
				throw new WrongArgumentIsNotFontKerning2D(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return PrologInteger.toInteger(Converters.termToRoundInteger(value,iX,false));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFontKerning2D(value);
			}
		}
	}
	//
	protected static Integer termToFontLigatures2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return TextAttribute.LIGATURES_ON;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return 0; // The default value is 0, which means do not use optional ligatures.
			} else {
				throw new WrongArgumentIsNotFontLigatures2D(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return PrologInteger.toInteger(Converters.termToRoundInteger(value,iX,false));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFontLigatures2D(value);
			}
		}
	}
	//
	protected static Number termToFontTracking2D(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return TextAttribute.TRACKING_TIGHT; // Actor Prolog default value
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return 0; // The default value is 0, which means no additional tracking.
			} else if (code==SymbolCodes.symbolCode_E_TRACKING_TIGHT) {
				return TextAttribute.TRACKING_TIGHT;
			} else if (code==SymbolCodes.symbolCode_E_TRACKING_LOOSE) {
				return TextAttribute.TRACKING_LOOSE;
			} else {
				throw new WrongArgumentIsNotFontTracking2D(value);
			}
		} catch (TermIsNotASymbol e) {
			return Converters.argumentToReal(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int termToCompositingRule(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_CLEAR) {
				return AlphaComposite.CLEAR;
			} else if (code==SymbolCodes.symbolCode_E_SRC) {
				return AlphaComposite.SRC;
			} else if (code==SymbolCodes.symbolCode_E_DST) {
				return AlphaComposite.DST;
			} else if (code==SymbolCodes.symbolCode_E_SRC_OVER) {
				return AlphaComposite.SRC_OVER;
			} else if (code==SymbolCodes.symbolCode_E_DST_OVER) {
				return AlphaComposite.DST_OVER;
			} else if (code==SymbolCodes.symbolCode_E_SRC_IN) {
				return AlphaComposite.SRC_IN;
			} else if (code==SymbolCodes.symbolCode_E_DST_IN) {
				return AlphaComposite.DST_IN;
			} else if (code==SymbolCodes.symbolCode_E_SRC_OUT) {
				return AlphaComposite.SRC_OUT;
			} else if (code==SymbolCodes.symbolCode_E_DST_OUT) {
				return AlphaComposite.DST_OUT;
			} else if (code==SymbolCodes.symbolCode_E_SRC_ATOP) {
				return AlphaComposite.SRC_ATOP;
			} else if (code==SymbolCodes.symbolCode_E_DST_ATOP) {
				return AlphaComposite.DST_ATOP;
			} else if (code==SymbolCodes.symbolCode_E_XOR) {
				return AlphaComposite.XOR;
			} else {
				throw new WrongArgumentIsNotAlphaCompositingRule(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotAlphaCompositingRule(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int termToBufferedImageType(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				// throw TermIsSymbolDefault.instance;
				return java.awt.image.BufferedImage.TYPE_CUSTOM;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_3BYTE_BGR) {
				return java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_4BYTE_ABGR) {
				return java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_4BYTE_ABGR_PRE) {
				return java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_BYTE_BINARY) {
				return java.awt.image.BufferedImage.TYPE_BYTE_BINARY;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_BYTE_GRAY) {
				return java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_BYTE_INDEXED) {
				return java.awt.image.BufferedImage.TYPE_BYTE_INDEXED;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_INT_ARGB) {
				return java.awt.image.BufferedImage.TYPE_INT_ARGB;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_INT_ARGB_PRE) {
				return java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_INT_BGR) {
				return java.awt.image.BufferedImage.TYPE_INT_BGR;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_INT_RGB) {
				return java.awt.image.BufferedImage.TYPE_INT_RGB;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_USHORT_555_RGB) {
				return java.awt.image.BufferedImage.TYPE_USHORT_555_RGB;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_USHORT_565_RGB) {
				return java.awt.image.BufferedImage.TYPE_USHORT_565_RGB;
			} else if (code==SymbolCodes.symbolCode_E_TYPE_USHORT_GRAY) {
				return java.awt.image.BufferedImage.TYPE_USHORT_GRAY;
			} else {
				throw new WrongArgumentIsNotBufferedImageType(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotBufferedImageType(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term bufferedImageTypeToTerm(int value) {
		long code;
		if (value==java.awt.image.BufferedImage.TYPE_3BYTE_BGR) {
			code= SymbolCodes.symbolCode_E_TYPE_3BYTE_BGR;
		} else if (value==java.awt.image.BufferedImage.TYPE_4BYTE_ABGR) {
			code= SymbolCodes.symbolCode_E_TYPE_4BYTE_ABGR;
		} else if (value==java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE) {
			code= SymbolCodes.symbolCode_E_TYPE_4BYTE_ABGR_PRE;
		} else if (value==java.awt.image.BufferedImage.TYPE_BYTE_BINARY) {
			code= SymbolCodes.symbolCode_E_TYPE_BYTE_BINARY;
		} else if (value==java.awt.image.BufferedImage.TYPE_BYTE_GRAY) {
			code= SymbolCodes.symbolCode_E_TYPE_BYTE_GRAY;
		} else if (value==java.awt.image.BufferedImage.TYPE_BYTE_INDEXED) {
			code= SymbolCodes.symbolCode_E_TYPE_BYTE_INDEXED;
		} else if (value==java.awt.image.BufferedImage.TYPE_INT_ARGB) {
			code= SymbolCodes.symbolCode_E_TYPE_INT_ARGB;
		} else if (value==java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE) {
			code= SymbolCodes.symbolCode_E_TYPE_INT_ARGB_PRE;
		} else if (value==java.awt.image.BufferedImage.TYPE_INT_BGR) {
			code= SymbolCodes.symbolCode_E_TYPE_INT_BGR;
		} else if (value==java.awt.image.BufferedImage.TYPE_INT_RGB) {
			code= SymbolCodes.symbolCode_E_TYPE_INT_RGB;
		} else if (value==java.awt.image.BufferedImage.TYPE_USHORT_555_RGB) {
			code= SymbolCodes.symbolCode_E_TYPE_USHORT_555_RGB;
		} else if (value==java.awt.image.BufferedImage.TYPE_USHORT_565_RGB) {
			code= SymbolCodes.symbolCode_E_TYPE_USHORT_565_RGB;
		} else if (value==java.awt.image.BufferedImage.TYPE_USHORT_GRAY) {
			code= SymbolCodes.symbolCode_E_TYPE_USHORT_GRAY;
		} else {
			code= SymbolCodes.symbolCode_E_default;
		};
		return new PrologSymbol(code);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int termToImageTransparency(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				// throw TermIsSymbolDefault.instance;
				return defaultTransparency;
			} else if (code==SymbolCodes.symbolCode_E_OPAQUE) {
				return Transparency.OPAQUE;
			} else if (code==SymbolCodes.symbolCode_E_BITMASK) {
				return Transparency.BITMASK;
			} else if (code==SymbolCodes.symbolCode_E_TRANSLUCENT) {
				return Transparency.TRANSLUCENT;
			} else {
				throw new WrongArgumentIsNotImageTransparency(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotImageTransparency(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term imageTransparencyToTerm(int value) {
		long code;
		if (value==Transparency.OPAQUE) {
			code= SymbolCodes.symbolCode_E_OPAQUE;
		} else if (value==Transparency.BITMASK) {
			code= SymbolCodes.symbolCode_E_BITMASK;
		} else if (value==Transparency.TRANSLUCENT) {
			code= SymbolCodes.symbolCode_E_TRANSLUCENT;
		} else {
			code= SymbolCodes.symbolCode_E_default;
		};
		return new PrologSymbol(code);
	}
}
