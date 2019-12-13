// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.converters.errors.*;
import morozov.system.files.*;
import morozov.system.gui.*;
import morozov.system.gui.errors.*;
import morozov.system.gui.space2d.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.awt.Color;
import java.awt.geom.Arc2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.Font;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Collections;
import java.math.BigInteger;

public abstract class Canvas2D extends BufferedImageController {
	//
	public Canvas2DScalingFactor scalingFactor= null;
	//
	protected AtomicReference<Canvas2DScalingFactor> actingScalingFactor= new AtomicReference<>(Canvas2DScalingFactor.INDEPENDENT);
	//
	public AtomicBoolean sceneAntialiasingIsEnabled= new AtomicBoolean(false);
	//
	public AtomicBoolean mouseClickedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseEnteredEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseExitedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mousePressedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseReleasedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseDraggedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseMovedEventIsEnabled= new AtomicBoolean(false);
	//
	protected List<Java2DCommand> actualCommands= Collections.synchronizedList(new ArrayList<Java2DCommand>());
	protected List<Java2DCommand> retractedCommands= Collections.synchronizedList(new ArrayList<Java2DCommand>());
	protected boolean implementDelayedCleaning= false;
	//
	public Canvas2D() {
		spaceAttributes= new CanvasSpaceAttributes();
	}
	public Canvas2D(GlobalWorldIdentifier id) {
		super(id);
		spaceAttributes= new CanvasSpaceAttributes();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_scaling_factor();
	//
	abstract public long entry_s_Action_1_i();
	abstract public long entry_s_MouseClicked_1_i();
	abstract public long entry_s_MouseEntered_1_i();
	abstract public long entry_s_MouseExited_1_i();
	abstract public long entry_s_MousePressed_1_i();
	abstract public long entry_s_MouseReleased_1_i();
	abstract public long entry_s_MouseDragged_1_i();
	abstract public long entry_s_MouseMoved_1_i();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set scalingFactor
	//
	public void setScalingFactor1s(ChoisePoint iX, Term a1) {
		Canvas2DScalingFactor factor= Canvas2DScalingFactor.argumentToScalingFactor(a1,iX);
		setScalingFactor(factor);
		actingScalingFactor.set(factor);
		repaintAfterDelay(iX);
	}
	public void setScalingFactor(Canvas2DScalingFactor value) {
		scalingFactor= value;
	}
	public void getScalingFactor0ff(ChoisePoint iX, PrologVariable result) {
		Canvas2DScalingFactor value= getScalingFactor(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getScalingFactor0fs(ChoisePoint iX) {
	}
	public Canvas2DScalingFactor getScalingFactor(ChoisePoint iX) {
		if (scalingFactor != null) {
			return scalingFactor;
		} else {
			Term value= getBuiltInSlot_E_scaling_factor();
			return Canvas2DScalingFactor.argumentToScalingFactor(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear0s(ChoisePoint iX) {
		synchronized (this) {
			actualCommands.clear();
			retractedCommands.clear();
			implementDelayedCleaning= false;
		};
		repaintAfterDelay(iX);
		show0s(iX);
	}
	//
	public void clear1s(ChoisePoint iX, Term a1) {
		boolean repaintImage= YesNoConverters.termYesNo2Boolean(a1,iX);
		if (repaintImage) {
			synchronized (this) {
				actualCommands.clear();
				retractedCommands.clear();
				implementDelayedCleaning= false;
			};
			repaintAfterDelay(iX);
			show0s(iX);
		} else {
			synchronized (this) {
				implementDelayedCleaning= true;
				skipDelayedRepainting(iX);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setMesh2s(ChoisePoint iX, Term columns, Term rows) {
		double rColumns= GeneralConverters.argumentToReal(columns,iX);
		double rRows= GeneralConverters.argumentToReal(rows,iX);
		appendCommand(new Java2DSetMesh(rColumns,rRows));
	}
	//
	public void setTransform1s(ChoisePoint iX, Term value) {
		AffineTransform transform= Tools2D.argumentToTransform2D(value,iX);
		appendCommand(new Java2DSetTransform(transform));
		repaintAfterDelay(iX);
	}
	//
	public void translate2s(ChoisePoint iX, Term x1, Term y1) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		appendCommand(new Java2DTranslate(rX1,rY1));
		repaintAfterDelay(iX);
	}
	//
	public void drawPoint2s(ChoisePoint iX, Term x1, Term y1) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		appendCommand(new Java2DDrawPoint(rX1,rY1));
		repaintAfterDelay(iX);
	}
	//
	public void drawLine4s(ChoisePoint iX, Term x1, Term y1, Term x2, Term y2) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		double rX2= GeneralConverters.argumentToReal(x2,iX);
		double rY2= GeneralConverters.argumentToReal(y2,iX);
		appendCommand(new Java2DDrawLine(rX1,rY1,rX2,rY2));
		repaintAfterDelay(iX);
	}
	//
	public void drawPolygon1s(ChoisePoint iX, Term pointList) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(pointList,iX);
		appendCommand(new Java2DDrawPolygon(arrays.getXPoints(),arrays.getYPoints()));
		repaintAfterDelay(iX);
	}
	//
	public void drawRectangle4s(ChoisePoint iX, Term x1, Term y1, Term width, Term height) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		double rWidth= GeneralConverters.argumentToReal(width,iX);
		double rHeight= GeneralConverters.argumentToReal(height,iX);
		appendCommand(new Java2DDrawRectangle(rX1,rY1,rWidth,rHeight));
		repaintAfterDelay(iX);
	}
	//
	public void drawRoundRectangle6s(ChoisePoint iX, Term x1, Term y1, Term width, Term height, Term arcWidth, Term arcHeight) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		double rWidth= GeneralConverters.argumentToReal(width,iX);
		double rHeight= GeneralConverters.argumentToReal(height,iX);
		double rArcW= GeneralConverters.argumentToReal(arcWidth,iX);
		double rArcH= GeneralConverters.argumentToReal(arcHeight,iX);
		appendCommand(new Java2DDrawRoundRectangle(rX1,rY1,rWidth,rHeight,rArcW,rArcH));
		repaintAfterDelay(iX);
	}
	//
	public void drawEllipse4s(ChoisePoint iX, Term x1, Term y1, Term width, Term height) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		double rWidth= GeneralConverters.argumentToReal(width,iX);
		double rHeight= GeneralConverters.argumentToReal(height,iX);
		appendCommand(new Java2DDrawEllipse(rX1,rY1,rWidth,rHeight));
		repaintAfterDelay(iX);
	}
	//
	public void drawArc6s(ChoisePoint iX, Term x1, Term y1, Term width, Term height, Term start, Term extent) {
		drawArc(x1,y1,width,height,start,extent,Arc2D.OPEN,iX);
	}
	public void drawArc7s(ChoisePoint iX, Term x1, Term y1, Term width, Term height, Term start, Term extent, Term closureType) {
		int type= Tools2D.argumentToArcClosureType(closureType,iX);
		drawArc(x1,y1,width,height,start,extent,type,iX);
	}
	protected void drawArc(Term x1, Term y1, Term width, Term height, Term start, Term extent, int type, ChoisePoint iX) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		double rWidth= GeneralConverters.argumentToReal(width,iX);
		double rHeight= GeneralConverters.argumentToReal(height,iX);
		double rStart= GeneralConverters.argumentToReal(start,iX);
		double rExtent= GeneralConverters.argumentToReal(extent,iX);
		appendCommand(new Java2DDrawArc(rX1,rY1,rWidth,rHeight,rStart,rExtent,type));
		repaintAfterDelay(iX);
	}
	//
	public void drawQuadCurve6s(ChoisePoint iX, Term x1, Term y1, Term ctrlX1, Term ctrlY1, Term x2, Term y2) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		double rCtrlX1= GeneralConverters.argumentToReal(ctrlX1,iX);
		double rCtrlY1= GeneralConverters.argumentToReal(ctrlY1,iX);
		double rX2= GeneralConverters.argumentToReal(x2,iX);
		double rY2= GeneralConverters.argumentToReal(y2,iX);
		appendCommand(new Java2DDrawQuadCurve(rX1,rY1,rCtrlX1,rCtrlY1,rX2,rY2));
		repaintAfterDelay(iX);
	}
	//
	public void drawCubicCurve8s(ChoisePoint iX, Term x1, Term y1, Term ctrlX1, Term ctrlY1, Term ctrlX2, Term ctrlY2, Term x2, Term y2) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		double rCtrlX1= GeneralConverters.argumentToReal(ctrlX1,iX);
		double rCtrlY1= GeneralConverters.argumentToReal(ctrlY1,iX);
		double rCtrlX2= GeneralConverters.argumentToReal(ctrlX2,iX);
		double rCtrlY2= GeneralConverters.argumentToReal(ctrlY2,iX);
		double rX2= GeneralConverters.argumentToReal(x2,iX);
		double rY2= GeneralConverters.argumentToReal(y2,iX);
		appendCommand(new Java2DDrawCubicCurve(rX1,rY1,rCtrlX1,rCtrlY1,rCtrlX2,rCtrlY2,rX2,rY2));
		repaintAfterDelay(iX);
	}
	//
	public void drawText3s(ChoisePoint iX, Term x1, Term y1, Term text) {
		double rX1= GeneralConverters.argumentToReal(x1,iX);
		double rY1= GeneralConverters.argumentToReal(y1,iX);
		String rText= text.toString(iX);
		appendCommand(new Java2DDrawText(rX1,rY1,rText));
		repaintAfterDelay(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void drawImage1s(ChoisePoint iX, Term a1) {
		drawImage(a1,iX);
		repaintAfterDelay(iX);
	}
	public void drawImage2s(ChoisePoint iX, Term a1, Term a2) {
		try {
			Color color= ColorAttributeConverters.argumentToColor(a2,iX);
			drawImage(a1,color,iX);
		} catch (TermIsSymbolDefault e1) {
			drawImage(a1,iX);
		};
		repaintAfterDelay(iX);
	}
	public void drawImage3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		drawImage(a1,a2,a3,iX);
		repaintAfterDelay(iX);
	}
	public void drawImage4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		try {
			Color color= ColorAttributeConverters.argumentToColor(a4,iX);
			drawImage(a1,a2,a3,color,iX);
		} catch (TermIsSymbolDefault e1) {
			drawImage(a1,a2,a3,iX);
		};
		repaintAfterDelay(iX);
	}
	public void drawImage5s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5) {
		drawImage(a1,a2,a3,a4,a5,iX);
		repaintAfterDelay(iX);
	}
	public void drawImage6s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5, Term a6) {
		try {
			Color color= ColorAttributeConverters.argumentToColor(a6,iX);
			drawImage(a1,a2,a3,a4,a5,color,iX);
		} catch (TermIsSymbolDefault e1) {
			drawImage(a1,a2,a3,a4,a5,iX);
		};
		repaintAfterDelay(iX);
	}
	public void drawImage9s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9) {
		drawImage(a1,a2,a3,a4,a5,a6,a7,a8,a9,iX);
		repaintAfterDelay(iX);
	}
	public void drawImage10s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9, Term a10) {
		try {
			Color color= ColorAttributeConverters.argumentToColor(a10,iX);
			drawImage(a1,a2,a3,a4,a5,a6,a7,a8,a9,color,iX);
		} catch (TermIsSymbolDefault e1) {
			drawImage(a1,a2,a3,a4,a5,a6,a7,a8,a9,iX);
		};
		repaintAfterDelay(iX);
	}
	protected void drawImage(Term a1, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			appendCommand(new Java2DDrawImage(nativeImage));
		}
	}
	protected void drawImage(Term a1, Color color, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			appendCommand(new Java2DDrawImage(nativeImage,color));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, ChoisePoint iX) {
		double x1= GeneralConverters.argumentToReal(a2,iX);
		double y1= GeneralConverters.argumentToReal(a3,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			appendCommand(new Java2DDrawImage(nativeImage,x1,y1));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Color color, ChoisePoint iX) {
		double x1= GeneralConverters.argumentToReal(a2,iX);
		double y1= GeneralConverters.argumentToReal(a3,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			appendCommand(new Java2DDrawImage(nativeImage,x1,y1,color));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, ChoisePoint iX) {
		double x1= GeneralConverters.argumentToReal(a2,iX);
		double y1= GeneralConverters.argumentToReal(a3,iX);
		double currentWidth= GeneralConverters.argumentToReal(a4,iX);
		double currentHeight= GeneralConverters.argumentToReal(a5,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			appendCommand(new Java2DScaleAndDrawImage(nativeImage,x1,y1,currentWidth,currentHeight));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, Color color, ChoisePoint iX) {
		double x1= GeneralConverters.argumentToReal(a2,iX);
		double y1= GeneralConverters.argumentToReal(a3,iX);
		double currentWidth= GeneralConverters.argumentToReal(a4,iX);
		double currentHeight= GeneralConverters.argumentToReal(a5,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			appendCommand(new Java2DScaleAndDrawImage(nativeImage,x1,y1,currentWidth,currentHeight,color));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9, ChoisePoint iX) {
		double destinationX1= GeneralConverters.argumentToReal(a2,iX);
		double destinationY1= GeneralConverters.argumentToReal(a3,iX);
		double destinationX2= GeneralConverters.argumentToReal(a4,iX);
		double destinationY2= GeneralConverters.argumentToReal(a5,iX);
		double sourceX1= GeneralConverters.argumentToReal(a6,iX);
		double sourceY1= GeneralConverters.argumentToReal(a7,iX);
		double sourceX2= GeneralConverters.argumentToReal(a8,iX);
		double sourceY2= GeneralConverters.argumentToReal(a9,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			appendCommand(new Java2DPickOutAndDrawImage(
				nativeImage,
				destinationX1,
				destinationY1,
				destinationX2,
				destinationY2,
				sourceX1,
				sourceY1,
				sourceX2,
				sourceY2));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9, Color color, ChoisePoint iX) {
		double destinationX1= GeneralConverters.argumentToReal(a2,iX);
		double destinationY1= GeneralConverters.argumentToReal(a3,iX);
		double destinationX2= GeneralConverters.argumentToReal(a4,iX);
		double destinationY2= GeneralConverters.argumentToReal(a5,iX);
		double sourceX1= GeneralConverters.argumentToReal(a6,iX);
		double sourceY1= GeneralConverters.argumentToReal(a7,iX);
		double sourceX2= GeneralConverters.argumentToReal(a8,iX);
		double sourceY2= GeneralConverters.argumentToReal(a9,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			appendCommand(new Java2DPickOutAndDrawImage(
				nativeImage,
				destinationX1,
				destinationY1,
				destinationX2,
				destinationY2,
				sourceX1,
				sourceY1,
				sourceX2,
				sourceY2,
				color));
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setPen1s(ChoisePoint iX, Term value) {
		StrokeAndColor attributes= StrokeAndColor.argumentToStrokeAndColor(value,iX);
		appendCommand(new Java2DSetPen(attributes));
	}
	//
	public void setBrush1s(ChoisePoint iX, Term value) {
		BrushAttributes attributes= BrushAttributes.argumentToBrushAttributes(value,this,iX);
		appendCommand(new Java2DSetBrushAttributes(attributes));
	}
	//
	public void setFont1s(ChoisePoint iX, Term value) {
		Font font= Tools2D.argumentToFont2D(value,this,iX);
		appendCommand(new Java2DSetFont(font));
	}
	//
	public void setTextAlignment2s(ChoisePoint iX, Term hA, Term vA) {
		Canvas2DHorizontalAlignment horizontalAlignment= Canvas2DHorizontalAlignment.argumentToHorizontalAlignment(hA,iX);
		Canvas2DVerticalAlignment verticalAlignment= Canvas2DVerticalAlignment.argumentToVerticalAlignment(vA,iX);
		appendCommand(new Java2DSetTextAlignment(horizontalAlignment,verticalAlignment));
	}
	//
	public void setPaintMode0s(ChoisePoint iX) {
		appendCommand(new Java2DSetPaintMode());
	}
	//
	public void setCompositingRule1s(ChoisePoint iX, Term value) {
		int rule= Tools2D.argumentToCompositingRule(value,iX);
		appendCommand(new Java2DSetComposite(rule));
	}
	public void setCompositingRule2s(ChoisePoint iX, Term a1, Term a2) {
		int rule= Tools2D.argumentToCompositingRule(a1,iX);
		float alpha= (float)GeneralConverters.argumentToReal(a2,iX);
		appendCommand(new Java2DSetComposite(rule,alpha));
	}
	//
	public void set_XOR_Mode1s(ChoisePoint iX, Term value) {
		Color color;
		try {
			color= ColorAttributeConverters.argumentToColor(value,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				color= getBackgroundColor(iX).getValue();
			} catch (UseDefaultColor e2) {
				color= getDefaultBackgroundColor();
			}
		};
		appendCommand(new Java2DSetXORMode(color));
	}
	//
	public void setRenderingMode1s(ChoisePoint iX, Term value) {
		RenderingHints hints;
		hints= Tools2D.argumentToRenderingHints(value,iX);
		appendCommand(new Java2DSetRenderingMode(hints));
	}
	//
	public void markDrawing0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(actualCommands.size()));
	}
	public void markDrawing0fs(ChoisePoint iX) {
	}
	//
	public void retractDrawing1s(ChoisePoint iX, Term a1) {
		try {
			synchronized (this) {
				BigInteger marker= GeneralConverters.termToStrictInteger(a1,iX,false);
				int fromIndex= Arithmetic.toInteger(marker);
				synchronized (actualCommands) {
					int size= actualCommands.size();
					if (fromIndex <= size-1 && fromIndex >= 0) {
						int index= size - 1;
						ListIterator i= actualCommands.listIterator(size);
						while (i.hasPrevious()) {
							if (index < fromIndex) {
								break;
							};
							i.previous();
							i.remove();
							index= index - 1;
						};
						retractedCommands.clear();
					}
				};
				implementDelayedCleaning= false;
			};
			repaintAfterDelay(iX);
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(a1);
		}
	}
	//
	public void undoDrawing0s(ChoisePoint iX) {
		synchronized (this) {
			synchronized (actualCommands) {
				int size= actualCommands.size();
				if (size > 0) {
					retractedCommands.add(actualCommands.get(size-1));
					actualCommands.remove(size-1);
				} else {
					return;
				}
			};
			implementDelayedCleaning= false;
		};
		repaintAfterDelay(iX);
	}
	//
	public void redoDrawing0s(ChoisePoint iX) {
		synchronized (this) {
			int size= retractedCommands.size();
			if (size > 0) {
				actualCommands.add(retractedCommands.get(size-1));
				retractedCommands.remove(size-1);
				implementDelayedCleaning= false;
				repaintAfterDelay(iX);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void suspendRedrawing0s(ChoisePoint iX) {
		if (canvasSpaceDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			synchronized (this) {
				suspendRedrawing= true;
				skipDelayedRepainting(iX);
				if (canvasSpace != null) {
					((ExtendedSpace2D)canvasSpace).suspendRedrawing();
				}
			}
		}
	}
	//
	public void drawNow0s(ChoisePoint iX) {
		if (canvasSpaceDoesNotExist()) {
			return;
		} else {
			createWindowAndDrawNow(iX);
		}
	}
	protected void createWindowAndDrawNow(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized (this) {
			suspendRedrawing= false;
			implementDelayedCleaning= false;
			if (graphicWindow != null) {
				if (canvasSpace != null) {
					((ExtendedSpace2D)canvasSpace).releaseRedrawing();
				};
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				((ExtendedSpace2D)canvasSpace).releaseRedrawing();
				canvasSpace.repaintAfterDelay();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getImage1s(ChoisePoint iX, Term a1) {
		createWindowAndDrawNow(iX);
		java.awt.image.BufferedImage bufferedImage= getBufferedImage();
		modifyImage(a1,bufferedImage,iX);
	}
	public void getImage5s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4, Term a5) {
		double x1= GeneralConverters.argumentToReal(a2,iX);
		double y1= GeneralConverters.argumentToReal(a3,iX);
		double currentWidth= GeneralConverters.argumentToReal(a4,iX);
		double currentHeight= GeneralConverters.argumentToReal(a5,iX);
		createWindowAndDrawNow(iX);
		java.awt.image.BufferedImage bufferedImage= getBufferedImage(x1,y1,currentWidth,currentHeight);
		modifyImage(a1,bufferedImage,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void save1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
		saveContent(fileName,attributes,iX);
	}
	public void save2s(ChoisePoint iX, Term a1, Term a2) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		GenericImageEncodingAttributes attributes= GenericImageEncodingAttributes.argumentToImageEncodingAttributes(a2,iX);
		saveContent(fileName,attributes,iX);
	}
	protected void saveContent(ExtendedFileName fileName, GenericImageEncodingAttributes attributes, ChoisePoint iX) {
		createWindowAndDrawNow(iX);
		synchronized (this) {
			java.awt.image.BufferedImage bufferedImage= getBufferedImage();
			if (bufferedImage != null) {
				writeImage(fileName,bufferedImage,attributes);
			}
		}
	}
	//
	protected java.awt.image.BufferedImage getBufferedImage() {
		if (graphicWindow != null) {
			return graphicWindow.safelyGetBufferedImage(false,0,0,0,0);
		} else if (canvasSpace != null) {
			return ((ExtendedSpace2D)canvasSpace).safelyGetBufferedImage(false,0,0,0,0);
		} else {
			return null;
		}
	}
	protected java.awt.image.BufferedImage getBufferedImage(double x1, double y1, double width, double height) {
		java.awt.image.BufferedImage bufferedImage= null;
		if (graphicWindow != null) {
			DrawingMode drawingMode= ((InternalFrame2D)graphicWindow).getDrawingMode();
			double factorX= drawingMode.getFactorX();
			double factorY= drawingMode.getFactorY();
			int integerX= Arithmetic.toInteger(x1*factorX);
			int integerY= Arithmetic.toInteger(y1*factorY);
			int integerWidth= Arithmetic.toInteger(width*factorX);
			int integerHeight= Arithmetic.toInteger(height*factorY);
			return graphicWindow.safelyGetBufferedImage(true,integerX,integerY,integerWidth,integerHeight);
		} else if (canvasSpace != null) {
			DrawingMode drawingMode= ((ExtendedSpace2D)canvasSpace).getDrawingMode();
			double factorX= drawingMode.getFactorX();
			double factorY= drawingMode.getFactorY();
			int integerX= Arithmetic.toInteger(x1*factorX);
			int integerY= Arithmetic.toInteger(y1*factorY);
			int integerWidth= Arithmetic.toInteger(width*factorX);
			int integerHeight= Arithmetic.toInteger(height*factorY);
			return ((ExtendedSpace2D)canvasSpace).safelyGetBufferedImage(true,integerX,integerY,integerWidth,integerHeight);
		};
		return bufferedImage;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void initiateRegisteredCanvasSpace(CanvasSpace s, ChoisePoint iX) {
		ExtendedSpace2D extendedSpace= (ExtendedSpace2D)s;
		extendedSpace.setScalingFactor(actingScalingFactor);
		extendedSpace.setEnableAntialiasing(sceneAntialiasingIsEnabled);
		extendedSpace.setCommands(actualCommands);
	}
	//
	@Override
	protected void enableMouseListeners() {
		reviseMouseListenerStatus();
		reviseMouseMotionListenerStatus();
	}
	@Override
	protected void disableMouseListeners() {
		((ExtendedSpace2D)canvasSpace).disableMouseListener();
		((ExtendedSpace2D)canvasSpace).disableMouseMotionListener();
	}
	//
	@Override
	protected InternalFrame2D createInternalFrameIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		Map<AbstractWorld,InternalFrame2D> innerWindows= StaticAttributes2D.retrieveInnerWindows(staticContext);
		InternalFrame2D currentGraphicWindow= innerWindows.get(this);
		boolean restoreWindow= false;
		boolean moveWindowToFront= false;
		if (currentGraphicWindow==null) {
			currentGraphicWindow= innerWindows.get(this);
			if (currentGraphicWindow==null) {
				currentGraphicWindow= formInternalFrame(iX);
				restoreWindow= true;
			};
			reviseCanvasListenersStatus(iX);
		} else {
			moveWindowToFront= true;
		};
		if (restoreWindow) {
			currentGraphicWindow.safelyRestoreSize(staticContext);
		};
		if (moveWindowToFront && enableMovingWindowToFront) {
			currentGraphicWindow.safelyMoveToFront();
		};
		currentGraphicWindow.safelySetVisible(true);
		return currentGraphicWindow;
	}
	protected InternalFrame2D formInternalFrame(ChoisePoint iX) {
		//
		String currentTitle= getTitle(iX).getValueOrDefaultText("");
		//
		InternalFrame2D internalFrame2D= new InternalFrame2D(this,currentTitle,staticContext,actualCommands,actingScalingFactor);
		graphicWindow= internalFrame2D;
		Map<AbstractWorld,InternalFrame2D> innerWindows= StaticAttributes2D.retrieveInnerWindows(staticContext);
		innerWindows.put(this,internalFrame2D);
		//
		internalFrame2D.safelyAddComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(staticContext);
		desktop.safelyAdd(internalFrame2D.getInternalFrame());
		//
		canvasSpace= internalFrame2D.getCanvasSpace();
		refreshAttributesOfInternalFrame(internalFrame2D,null,iX);
		//
		return internalFrame2D;
	}
	@Override
	protected void refreshAttributesOfCanvasSpace(ChoisePoint iX) {
		changeBackgroundColor(iX);
		actingScalingFactor.set(getScalingFactor(iX));
		sceneAntialiasingIsEnabled.set(getEnableSceneAntialiasing(iX));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void appendCommand(Java2DCommand command) {
		synchronized (this) {
			if (implementDelayedCleaning) {
				actualCommands.clear();
				retractedCommands.clear();
				implementDelayedCleaning= false;
			}
		};
		actualCommands.add(command);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void enableMouseEvents1ms(ChoisePoint iX, Term... args) {
		createGraphicWindowIfNecessary(iX,false);
		controlMouseEvents(iX,true,(Term[])args);
	}
	//
	public void disableMouseEvents1ms(ChoisePoint iX, Term... args) {
		createGraphicWindowIfNecessary(iX,false);
		controlMouseEvents(iX,false,(Term[])args);
	}
	//
	public void controlMouseEvents(ChoisePoint iX, boolean mode, Term[] args) {
		ArrayList<Term> argumentList= new ArrayList<>();
		for(int i= 0; i < args.length; i++) {
			Term item= args[i];
			if (item.thisIsArgumentNumber()) {
				Term extraItems= args[i+1];
				for (int j= 0; j < item.getNumber(); j++) {
					if (j > 0) {
						extraItems= extraItems.getExistentTail();
					};
					Term extraItem= extraItems.getExistentHead();
					argumentList.add(extraItem);
				};
				break;
			} else {
				argumentList.add(item);
			}
		};
		try {
			for(int i= 0; i < argumentList.size(); i++) {
				Term value= argumentList.get(i);
				try {
					long code= value.getSymbolValue(iX);
					if (code==SymbolCodes.symbolCode_E_MOUSE_CLICKED) {
						mouseClickedEventIsEnabled.set(mode);
					} else if (code==SymbolCodes.symbolCode_E_MOUSE_ENTERED) {
						mouseEnteredEventIsEnabled.set(mode);
					} else if (code==SymbolCodes.symbolCode_E_MOUSE_EXITED) {
						mouseExitedEventIsEnabled.set(mode);
					} else if (code==SymbolCodes.symbolCode_E_MOUSE_PRESSED) {
						mousePressedEventIsEnabled.set(mode);
					} else if (code==SymbolCodes.symbolCode_E_MOUSE_RELEASED) {
						mouseReleasedEventIsEnabled.set(mode);
					} else if (code==SymbolCodes.symbolCode_E_MOUSE_DRAGGED) {
						mouseDraggedEventIsEnabled.set(mode);
					} else if (code==SymbolCodes.symbolCode_E_MOUSE_MOVED) {
						mouseMovedEventIsEnabled.set(mode);
					} else {
						throw new WrongArgumentIsNotMouseEventType(value);
					}
				} catch (TermIsNotASymbol e1) {
					throw new WrongArgumentIsNotMouseEventType(value);
				}
			}
		} finally {
			reviseCanvasListenersStatus(iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void reviseCanvasListenersStatus(ChoisePoint iX) {
		if (canvasSpaceDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			reviseMouseListenerStatus();
			reviseMouseMotionListenerStatus();
		}
	}
	public void reviseMouseListenerStatus() {
		synchronized (this) {
			if (	mouseClickedEventIsEnabled.get() ||
				mouseEnteredEventIsEnabled.get() ||
				mouseExitedEventIsEnabled.get() ||
				mousePressedEventIsEnabled.get() ||
				mouseReleasedEventIsEnabled.get()) {
				if (graphicWindow != null) {
					graphicWindow.enableMouseListener();
				} else if (canvasSpace != null) {
					((ExtendedSpace2D)canvasSpace).enableMouseListener();
				}
			} else {
				if (graphicWindow != null) {
					graphicWindow.disableMouseListener();
				} else if (canvasSpace != null) {
					((ExtendedSpace2D)canvasSpace).disableMouseListener();
				}
			}
		}
	}
	public void reviseMouseMotionListenerStatus() {
		synchronized (this) {
			if (	mouseDraggedEventIsEnabled.get() ||
				mouseMovedEventIsEnabled.get()) {
				if (graphicWindow != null) {
					graphicWindow.enableMouseMotionListener();
				} else if (canvasSpace != null) {
					((ExtendedSpace2D)canvasSpace).enableMouseMotionListener();
				}
			} else {
				if (graphicWindow != null) {
					graphicWindow.disableMouseMotionListener();
				} else if (canvasSpace != null) {
					((ExtendedSpace2D)canvasSpace).disableMouseMotionListener();
				}
			}
		}
	}
}
