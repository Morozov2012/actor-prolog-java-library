// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.gui.*;
import morozov.system.gui.errors.*;
import morozov.system.gui.signals.*;
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
	protected AtomicReference<Canvas2DScalingFactor> actingScalingFactor= new AtomicReference<Canvas2DScalingFactor>(Canvas2DScalingFactor.INDEPENDENT);
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
	protected boolean suspendRedrawing= false;
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
	// abstract protected Term getBuiltInSlot_E_title();
	// abstract protected Term getBuiltInSlot_E_x();
	// abstract protected Term getBuiltInSlot_E_y();
	// abstract protected Term getBuiltInSlot_E_width();
	// abstract protected Term getBuiltInSlot_E_height();
	// abstract protected Term getBuiltInSlot_E_background_color();
	//
	// abstract protected Term getBuiltInSlot_E_enable_scene_antialiasing();
	//
	abstract protected Term getBuiltInSlot_E_scaling_factor();
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
		Canvas2DScalingFactor factor= Canvas2DScalingFactor.termToScalingFactor(a1,iX);
		setScalingFactor(factor);
		actingScalingFactor.set(factor);
		repaintAfterDelay(iX);
	}
	public void setScalingFactor(Canvas2DScalingFactor value) {
		scalingFactor= value;
	}
	public void getScalingFactor0ff(ChoisePoint iX, PrologVariable a1) {
		Canvas2DScalingFactor value= getScalingFactor(iX);
		a1.value= value.toTerm();
	}
	public void getScalingFactor0fs(ChoisePoint iX) {
	}
	public Canvas2DScalingFactor getScalingFactor(ChoisePoint iX) {
		if (scalingFactor != null) {
			return scalingFactor;
		} else {
			Term value= getBuiltInSlot_E_scaling_factor();
			return Canvas2DScalingFactor.termToScalingFactor(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear0s(ChoisePoint iX) {
		synchronized(this) {
			actualCommands.clear();
			retractedCommands.clear();
			implementDelayedCleaning= false;
		};
		repaintAfterDelay(iX);
		show0s(iX);
	}
	//
	public void clear1s(ChoisePoint iX, Term a1) {
		boolean repaintImage= YesNo.termYesNo2Boolean(a1,iX);
		if (repaintImage) {
			synchronized(this) {
				actualCommands.clear();
				retractedCommands.clear();
				implementDelayedCleaning= false;
			};
			repaintAfterDelay(iX);
			show0s(iX);
		} else {
			synchronized(this) {
				implementDelayedCleaning= true;
				skipDelayedRepainting(iX);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setMesh2s(ChoisePoint iX, Term columns, Term rows) {
		double rColumns= Converters.argumentToReal(columns,iX);
		double rRows= Converters.argumentToReal(rows,iX);
		append_command(new Java2DSetMesh(rColumns,rRows));
		// repaintAfterDelay(iX);
	}
	//
	public void setTransform1s(ChoisePoint iX, Term value) {
		AffineTransform transform= Tools2D.termToTransform2D(value,iX);
		append_command(new Java2DSetTransform(transform));
		repaintAfterDelay(iX);
	}
	//
	public void translate2s(ChoisePoint iX, Term x1, Term y1) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		append_command(new Java2DTranslate(rX1,rY1));
		repaintAfterDelay(iX);
	}
	//
	public void drawPoint2s(ChoisePoint iX, Term x1, Term y1) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		append_command(new Java2DDrawPoint(rX1,rY1));
		repaintAfterDelay(iX);
	}
	//
	public void drawLine4s(ChoisePoint iX, Term x1, Term y1, Term x2, Term y2) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		double rX2= Converters.argumentToReal(x2,iX);
		double rY2= Converters.argumentToReal(y2,iX);
		append_command(new Java2DDrawLine(rX1,rY1,rX2,rY2));
		repaintAfterDelay(iX);
	}
	//
	public void drawPolygon1s(ChoisePoint iX, Term pointList) {
		Point2DArrays arrays= Point2DArrays.termToPoint2DArrays(pointList,iX);
		append_command(new Java2DDrawPolygon(arrays.xPoints,arrays.yPoints));
		repaintAfterDelay(iX);
	}
	//
	public void drawRectangle4s(ChoisePoint iX, Term x1, Term y1, Term width, Term height) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		double rWidth= Converters.argumentToReal(width,iX);
		double rHeight= Converters.argumentToReal(height,iX);
		append_command(new Java2DDrawRectangle(rX1,rY1,rWidth,rHeight));
		repaintAfterDelay(iX);
	}
	//
	public void drawRoundRectangle6s(ChoisePoint iX, Term x1, Term y1, Term width, Term height, Term arcWidth, Term arcHeight) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		double rWidth= Converters.argumentToReal(width,iX);
		double rHeight= Converters.argumentToReal(height,iX);
		double rArcW= Converters.argumentToReal(arcWidth,iX);
		double rArcH= Converters.argumentToReal(arcHeight,iX);
		append_command(new Java2DDrawRoundRectangle(rX1,rY1,rWidth,rHeight,rArcW,rArcH));
		repaintAfterDelay(iX);
	}
	//
	public void drawEllipse4s(ChoisePoint iX, Term x1, Term y1, Term width, Term height) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		double rWidth= Converters.argumentToReal(width,iX);
		double rHeight= Converters.argumentToReal(height,iX);
		append_command(new Java2DDrawEllipse(rX1,rY1,rWidth,rHeight));
		repaintAfterDelay(iX);
	}
	//
	public void drawArc6s(ChoisePoint iX, Term x1, Term y1, Term width, Term height, Term start, Term extent) {
		drawArc(x1,y1,width,height,start,extent,Arc2D.OPEN,iX);
	}
	public void drawArc7s(ChoisePoint iX, Term x1, Term y1, Term width, Term height, Term start, Term extent, Term closureType) {
		int type= Tools2D.termToArcClosureType(closureType,iX);
		drawArc(x1,y1,width,height,start,extent,type,iX);
	}
	protected void drawArc(Term x1, Term y1, Term width, Term height, Term start, Term extent, int type, ChoisePoint iX) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		double rWidth= Converters.argumentToReal(width,iX);
		double rHeight= Converters.argumentToReal(height,iX);
		double rStart= Converters.argumentToReal(start,iX);
		double rExtent= Converters.argumentToReal(extent,iX);
		append_command(new Java2DDrawArc(rX1,rY1,rWidth,rHeight,rStart,rExtent,type));
		repaintAfterDelay(iX);
	}
	//
	public void drawQuadCurve6s(ChoisePoint iX, Term x1, Term y1, Term ctrlX1, Term ctrlY1, Term x2, Term y2) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		double rCtrlX1= Converters.argumentToReal(ctrlX1,iX);
		double rCtrlY1= Converters.argumentToReal(ctrlY1,iX);
		double rX2= Converters.argumentToReal(x2,iX);
		double rY2= Converters.argumentToReal(y2,iX);
		append_command(new Java2DDrawQuadCurve(rX1,rY1,rCtrlX1,rCtrlY1,rX2,rY2));
		repaintAfterDelay(iX);
	}
	//
	public void drawCubicCurve8s(ChoisePoint iX, Term x1, Term y1, Term ctrlX1, Term ctrlY1, Term ctrlX2, Term ctrlY2, Term x2, Term y2) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		double rCtrlX1= Converters.argumentToReal(ctrlX1,iX);
		double rCtrlY1= Converters.argumentToReal(ctrlY1,iX);
		double rCtrlX2= Converters.argumentToReal(ctrlX2,iX);
		double rCtrlY2= Converters.argumentToReal(ctrlY2,iX);
		double rX2= Converters.argumentToReal(x2,iX);
		double rY2= Converters.argumentToReal(y2,iX);
		append_command(new Java2DDrawCubicCurve(rX1,rY1,rCtrlX1,rCtrlY1,rCtrlX2,rCtrlY2,rX2,rY2));
		repaintAfterDelay(iX);
	}
	//
	public void drawText3s(ChoisePoint iX, Term x1, Term y1, Term text) {
		double rX1= Converters.argumentToReal(x1,iX);
		double rY1= Converters.argumentToReal(y1,iX);
		String rText= text.toString(iX);
		append_command(new Java2DDrawText(rX1,rY1,rText));
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
			Color color= ExtendedColor.termToColor(a2,iX);
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
			Color color= ExtendedColor.termToColor(a4,iX);
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
			Color color= ExtendedColor.termToColor(a6,iX);
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
			Color color= ExtendedColor.termToColor(a10,iX);
			drawImage(a1,a2,a3,a4,a5,a6,a7,a8,a9,color,iX);
		} catch (TermIsSymbolDefault e1) {
			drawImage(a1,a2,a3,a4,a5,a6,a7,a8,a9,iX);
		};
		repaintAfterDelay(iX);
	}
	protected void drawImage(Term a1, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			append_command(new Java2DDrawImage(nativeImage));
		}
	}
	protected void drawImage(Term a1, Color color, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			append_command(new Java2DDrawImage(nativeImage,color));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, ChoisePoint iX) {
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			append_command(new Java2DDrawImage(nativeImage,x1,y1));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Color color, ChoisePoint iX) {
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			append_command(new Java2DDrawImage(nativeImage,x1,y1,color));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, ChoisePoint iX) {
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		double width= Converters.argumentToReal(a4,iX);
		double height= Converters.argumentToReal(a5,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			append_command(new Java2DScaleAndDrawImage(nativeImage,x1,y1,width,height));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, Color color, ChoisePoint iX) {
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		double width= Converters.argumentToReal(a4,iX);
		double height= Converters.argumentToReal(a5,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			append_command(new Java2DScaleAndDrawImage(nativeImage,x1,y1,width,height,color));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, Term a6, Term a7, Term a8, Term a9, ChoisePoint iX) {
		double destinationX1= Converters.argumentToReal(a2,iX);
		double destinationY1= Converters.argumentToReal(a3,iX);
		double destinationX2= Converters.argumentToReal(a4,iX);
		double destinationY2= Converters.argumentToReal(a5,iX);
		double sourceX1= Converters.argumentToReal(a6,iX);
		double sourceY1= Converters.argumentToReal(a7,iX);
		double sourceX2= Converters.argumentToReal(a8,iX);
		double sourceY2= Converters.argumentToReal(a9,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			append_command(new Java2DPickOutAndDrawImage(
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
		double destinationX1= Converters.argumentToReal(a2,iX);
		double destinationY1= Converters.argumentToReal(a3,iX);
		double destinationX2= Converters.argumentToReal(a4,iX);
		double destinationY2= Converters.argumentToReal(a5,iX);
		double sourceX1= Converters.argumentToReal(a6,iX);
		double sourceY1= Converters.argumentToReal(a7,iX);
		double sourceX2= Converters.argumentToReal(a8,iX);
		double sourceY2= Converters.argumentToReal(a9,iX);
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		if (nativeImage != null) {
			append_command(new Java2DPickOutAndDrawImage(
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
		StrokeAndColor attributes= StrokeAndColor.termToStrokeAndColor(value,iX);
		append_command(new Java2DSetPen(attributes));
	}
	//
	public void setBrush1s(ChoisePoint iX, Term value) {
		BrushAttributes attributes= BrushAttributes.termToBrushAttributes(value,this,iX);
		append_command(new Java2DSetBrushAttributes(attributes));
	}
	//
	public void setFont1s(ChoisePoint iX, Term value) {
		Font font= Tools2D.termToFont2D(value,this,iX);
		append_command(new Java2DSetFont(font));
	}
	//
	public void setTextAlignment2s(ChoisePoint iX, Term hA, Term vA) {
		Canvas2DHorizontalAlignment horizontalAlignment= Canvas2DHorizontalAlignment.termToHorizontalAlignment(hA,iX);
		Canvas2DVerticalAlignment verticalAlignment= Canvas2DVerticalAlignment.termToVerticalAlignment(vA,iX);
		append_command(new Java2DSetTextAlignment(horizontalAlignment,verticalAlignment));
	}
	//
	public void setPaintMode0s(ChoisePoint iX) {
		append_command(new Java2DSetPaintMode());
	}
	//
	public void setCompositingRule1s(ChoisePoint iX, Term value) {
		int rule= Tools2D.termToCompositingRule(value,iX);
		append_command(new Java2DSetComposite(rule));
	}
	public void setCompositingRule2s(ChoisePoint iX, Term a1, Term a2) {
		int rule= Tools2D.termToCompositingRule(a1,iX);
		float alpha= (float)Converters.argumentToReal(a2,iX);
		append_command(new Java2DSetComposite(rule,alpha));
	}
	//
	public void set_XOR_Mode1s(ChoisePoint iX, Term value) {
		Color color;
		try {
			color= ExtendedColor.termToColor(value,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				color= getBackgroundColor(iX).getValue();
			} catch (UseDefaultColor e2) {
				color= getDefaultBackgroundColor();
			}
		};
		append_command(new Java2DSetXORMode(color));
	}
	//
	public void setRenderingMode1s(ChoisePoint iX, Term value) {
		RenderingHints hints;
		hints= Tools2D.termToRenderingHints(value,iX);
		append_command(new Java2DSetRenderingMode(hints));
	}
	//
	public void markDrawing0ff(ChoisePoint iX, PrologVariable value) {
		value.setValue(new PrologInteger(actualCommands.size()));
	}
	public void markDrawing0fs(ChoisePoint iX) {
	}
	//
	public void retractDrawing1s(ChoisePoint iX, Term a1) {
		try {
			synchronized(this) {
				BigInteger marker= Converters.termToStrictInteger(a1,iX,false);
				int fromIndex= PrologInteger.toInteger(marker);
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
		synchronized(this) {
			synchronized(actualCommands) {
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
		synchronized(this) {
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
			synchronized(this) {
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
		synchronized(this) {
			suspendRedrawing= false;
			implementDelayedCleaning= false;
			if (graphicWindow != null) {
				if (canvasSpace != null) {
					((ExtendedSpace2D)canvasSpace).releaseRedrawing();
				};
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				((ExtendedSpace2D)canvasSpace).releaseRedrawing();
				// 2013.11.16: Inner Canvas2D repainting problem
				// canvasSpace.safelyRepaint();
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
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		double width= Converters.argumentToReal(a4,iX);
		double height= Converters.argumentToReal(a5,iX);
		createWindowAndDrawNow(iX);
		java.awt.image.BufferedImage bufferedImage= getBufferedImage(x1,y1,width,height);
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
		GenericImageEncodingAttributes attributes= GenericImageEncodingAttributes.termToImageEncodingAttributes(a2,iX);
		saveContent(fileName,attributes,iX);
	}
	protected void saveContent(ExtendedFileName fileName, GenericImageEncodingAttributes attributes, ChoisePoint iX) {
		createWindowAndDrawNow(iX);
		synchronized(this) {
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
			int integerX= PrologInteger.toInteger(x1*factorX);
			int integerY= PrologInteger.toInteger(y1*factorY);
			int integerWidth= PrologInteger.toInteger(width*factorX);
			int integerHeight= PrologInteger.toInteger(height*factorY);
			return graphicWindow.safelyGetBufferedImage(true,integerX,integerY,integerWidth,integerHeight);
		} else if (canvasSpace != null) {
			DrawingMode drawingMode= ((ExtendedSpace2D)canvasSpace).getDrawingMode();
			double factorX= drawingMode.getFactorX();
			double factorY= drawingMode.getFactorY();
			int integerX= PrologInteger.toInteger(x1*factorX);
			int integerY= PrologInteger.toInteger(y1*factorY);
			int integerWidth= PrologInteger.toInteger(width*factorX);
			int integerHeight= PrologInteger.toInteger(height*factorY);
			return ((ExtendedSpace2D)canvasSpace).safelyGetBufferedImage(true,integerX,integerY,integerWidth,integerHeight);
		};
		return bufferedImage;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initiateRegisteredCanvasSpace(CanvasSpace s, ChoisePoint iX) {
		ExtendedSpace2D extendedSpace= (ExtendedSpace2D)s;
		// extendedSpace.setTargetWorld(this);
		extendedSpace.setScalingFactor(actingScalingFactor);
		extendedSpace.setEnableAntialiasing(sceneAntialiasingIsEnabled);
		extendedSpace.setCommands(actualCommands);
	}
	//
	protected void enableMouseListeners() {
		reviseMouseListenerStatus();
		reviseMouseMotionListenerStatus();
	}
	protected void disableMouseListeners() {
		((ExtendedSpace2D)canvasSpace).disableMouseListener();
		((ExtendedSpace2D)canvasSpace).disableMouseMotionListener();
	}
	//
	protected InternalFrame2D createInternalFrameIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		Map<AbstractWorld,InternalFrame2D> innerWindows= StaticAttributes2D.retrieveInnerWindows(staticContext);
		InternalFrame2D graphicWindow= innerWindows.get(this);
		boolean restoreWindow= false;
		boolean moveWindowToFront= false;
		if (graphicWindow==null) {
			graphicWindow= innerWindows.get(this);
			if (graphicWindow==null) {
				graphicWindow= formInternalFrame(iX);
				restoreWindow= true;
			};
			reviseCanvasListenersStatus(iX);
		} else {
			moveWindowToFront= true;
		};
		if (restoreWindow) {
			graphicWindow.safelyRestoreSize(staticContext);
		};
		if (moveWindowToFront && enableMovingWindowToFront) {
			graphicWindow.safelyMoveToFront();
		};
		graphicWindow.safelySetVisible(true);
		return graphicWindow;
	}
	protected InternalFrame2D formInternalFrame(ChoisePoint iX) {
		//
		String title= getTitle(iX).getValueOrDefaultText("");
		//
		InternalFrame2D internalFrame2D= new InternalFrame2D(this,title,staticContext,actualCommands,actingScalingFactor);
		graphicWindow= internalFrame2D;
		Map<AbstractWorld,InternalFrame2D> innerWindows= StaticAttributes2D.retrieveInnerWindows(staticContext);
		innerWindows.put(this,internalFrame2D);
		//
		internalFrame2D.safelyAddComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		desktop.safelyAdd(internalFrame2D.getInternalFrame());
		//
		canvasSpace= internalFrame2D.getCanvasSpace();
		refreshAttributesOfInternalFrame(internalFrame2D,null,iX);
		//
		return internalFrame2D;
	}
	protected void refreshAttributesOfCanvasSpace(ChoisePoint iX) {
		changeBackgroundColor(iX);
		actingScalingFactor.set(getScalingFactor(iX));
		sceneAntialiasingIsEnabled.set(getEnableSceneAntialiasing(iX));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void append_command(Java2DCommand command) {
		synchronized(this) {
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
	// protected String retrieveFileName(Term name, ChoisePoint iX) {
	//	boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparatorInLocalFileNames(getBackslashAlwaysIsSeparator(iX));
	//	try {
	//		String textName= name.getStringValue(iX);
	//		textName= FileUtils.replaceBackslashes(textName,backslashIsSeparator);
	//		return FileUtils.makeRealName(textName);
	//	} catch (TermIsNotAString e) {
	//		throw new WrongArgumentIsNotFileName(name);
	//	}
	// }
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
		ArrayList<Term> argumentList= new ArrayList<Term>();
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
		synchronized(this) {
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
		synchronized(this) {
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
