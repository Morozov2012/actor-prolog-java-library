// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.gui.*;
import morozov.system.gui.space2d.*;
import morozov.system.gui.space2d.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import java.awt.GraphicsConfiguration;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Arc2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Collections;
import java.math.BigInteger;
import java.lang.reflect.InvocationTargetException;

public abstract class Canvas2D extends ImageConsumer {
	//
	public AtomicBoolean mouseClickedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseEnteredEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseExitedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mousePressedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseReleasedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseDraggedEventIsEnabled= new AtomicBoolean(false);
	public AtomicBoolean mouseMovedEventIsEnabled= new AtomicBoolean(false);
	//
	public AtomicBoolean sceneAntialiasingIsEnabled= new AtomicBoolean(false);
	//
	protected static final int defaultWaitingInterval= -1;
	//
	abstract protected Term getBuiltInSlot_E_scaling_factor();
	//
	protected static final Color defaultBackgroundColor= Color.WHITE;
	//
	protected AtomicBoolean controlIsInitialized= new AtomicBoolean(false);
	protected InternalFrame2D graphicWindow= null;
	protected ExtendedSpace2D space2D= null;
	//
	protected List<Java2DCommand> actualCommands= Collections.synchronizedList(new ArrayList<Java2DCommand>());
	protected List<Java2DCommand> retractedCommands= Collections.synchronizedList(new ArrayList<Java2DCommand>());
	protected boolean implementDelayedCleaning= false;
	protected boolean suspendRedrawing= false;
	//
	protected AtomicReference<Canvas2DScalingFactor> scalingFactor= new AtomicReference<Canvas2DScalingFactor>(Canvas2DScalingFactor.INDEPENDENT);
	protected Canvas2DScalingFactor redefinedScalingFactor= null;
	//
	abstract protected Term getBuiltInSlot_E_title();
	abstract protected Term getBuiltInSlot_E_x();
	abstract protected Term getBuiltInSlot_E_y();
	abstract protected Term getBuiltInSlot_E_width();
	abstract protected Term getBuiltInSlot_E_height();
	abstract protected Term getBuiltInSlot_E_background_color();
	//
	abstract protected Term getBuiltInSlot_E_enable_scene_antialiasing();
	//
	abstract public long entry_s_Action_1_i();
	abstract public long entry_s_MouseClicked_1_i();
	abstract public long entry_s_MouseEntered_1_i();
	abstract public long entry_s_MouseExited_1_i();
	abstract public long entry_s_MousePressed_1_i();
	abstract public long entry_s_MouseReleased_1_i();
	abstract public long entry_s_MouseDragged_1_i();
	abstract public long entry_s_MouseMoved_1_i();
	abstract public long entry_s_Initialize_0();
	abstract public long entry_s_Start_0();
	abstract public long entry_s_Stop_0();
	//
	public void closeFiles() {
		if (graphicWindow != null) {
			DesktopUtils.safelyDispose(graphicWindow);
		};
		super.closeFiles();
	}
	//
	public void clear0s(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		// } else if (space2DDoesNotExist()) {
		//	return;
		// } else {
		synchronized(this) {
			actualCommands.clear();
			retractedCommands.clear();
			implementDelayedCleaning= false;
		};
		repaintAfterDelay(iX);
		show0s(iX);
		// }
	}
	//
	public void clear1s(ChoisePoint iX, Term a1) {
		// if (desktopDoesNotExist()) {
		//	return;
		// } else if (space2DDoesNotExist()) {
		//	return;
		// } else {
		boolean repaintImage= Converters.term2YesNo(a1,iX);
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
		// }
	}
	//
	public void show0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
	}
	//
	public void redraw0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized(this) {
			if (graphicWindow != null) {
				redrawInternalFrame(graphicWindow,iX);
				graphicWindow.safelyRestoreSize(staticContext);
				DesktopUtils.safelyRepaint(graphicWindow);
			} else if (space2D != null) {
				redrawCanvas2D(iX);
				DesktopUtils.safelyRepaint(space2D);
			}
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		if (space2DDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			synchronized(this) {
				if (graphicWindow != null) {
					DesktopUtils.safelySetVisible(false,graphicWindow);
				}
			}
		}
	}
	//
	public void maximize0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized(this) {
			if (graphicWindow != null) {
				DesktopUtils.safelyMaximize(graphicWindow);
			}
		}
	}
	//
	public void minimize0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized(this) {
			if (graphicWindow != null) {
				DesktopUtils.safelyMinimize(graphicWindow);
			}
		}
	}
	//
	public void restore0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
		synchronized(this) {
			if (graphicWindow != null) {
				DesktopUtils.safelyRestore(graphicWindow);
			}
		}
	}
	//
	public void isVisible0s(ChoisePoint iX) throws Backtracking {
		// if (desktopDoesNotExist()) {
		//	throw Backtracking.instance;
		if (space2DDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized(this) {
				if (graphicWindow != null) {
					if (!DesktopUtils.safelyIsVisible(graphicWindow)) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void isHidden0s(ChoisePoint iX) throws Backtracking {
		// if (desktopDoesNotExist()) {
		if (space2DDoesNotExist()) {
		} else {
			synchronized(this) {
				if (graphicWindow != null) {
					if (!DesktopUtils.safelyIsHidden(graphicWindow)) {
						throw Backtracking.instance;
					}
				}
			}
		}
	}
	//
	public void isMaximized0s(ChoisePoint iX) throws Backtracking {
		// if (desktopDoesNotExist()) {
		//	throw Backtracking.instance;
		if (space2DDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized(this) {
				if (graphicWindow != null) {
					if (!DesktopUtils.safelyIsMaximized(graphicWindow)) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void isMinimized0s(ChoisePoint iX) throws Backtracking {
		// if (desktopDoesNotExist()) {
		//	throw Backtracking.instance;
		if (space2DDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized(this) {
				if (graphicWindow != null) {
					if(!DesktopUtils.safelyIsMinimized(graphicWindow)) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void isRestored0s(ChoisePoint iX) throws Backtracking {
		// if (desktopDoesNotExist()) {
		//	throw Backtracking.instance;
		if (space2DDoesNotExist()) {
			throw Backtracking.instance;
		} else {
			synchronized(this) {
				if (graphicWindow != null) {
					if(!DesktopUtils.safelyIsRestored(graphicWindow)) {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			}
		}
	}
	//
	public void changeBackgroundColor1s(ChoisePoint iX, Term backgroundColor) {
		createGraphicWindowIfNecessary(iX,false);
		changeBackgroundColor(iX,backgroundColor);
	}
	//
	public void setScalingFactor1s(ChoisePoint iX, Term value) {
		Canvas2DScalingFactor factor= Tools2D.termToScalingFactor(value,iX);
		redefinedScalingFactor= factor;
		scalingFactor.set(factor);
		repaintAfterDelay(iX);
	}
	//
	public void setMenu1s(ChoisePoint iX, Term value) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized(this) {
			if (graphicWindow != null) {
				setMenu(iX,graphicWindow,value);
			}
		};
		repaintAfterDelay(iX);
	}
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
	public void getSize2s(ChoisePoint iX, PrologVariable width, PrologVariable height) {
		synchronized(this) {
			if (graphicWindow != null) {
				Dimension size= new Dimension();
				graphicWindow.getSize(size);
				width.value= new PrologInteger(size.width);
				height.value= new PrologInteger(size.height);
			} else {
				if (space2D != null) {
					Dimension size= new Dimension();
					space2D.getSize(size);
					width.value= new PrologInteger(size.width);
					height.value= new PrologInteger(size.height);
				} else {
					width.value= new PrologInteger(0);
					height.value= new PrologInteger(0);
				}
			}
		}
	}
	//
	public void setMesh2s(ChoisePoint iX, Term columns, Term rows) {
		double rColumns= Converters.argumentToReal(columns,iX);
		double rRows= Converters.argumentToReal(rows,iX);
		append_command(new Java2DSetMesh(rColumns,rRows));
		// repaintAfterDelay(iX);
	}
	//
	public void set_transform1s(ChoisePoint iX, Term value) {
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
		Point2DArrays arrays= Tools2D.termToPoint2DArrays(pointList,iX);
		// Polygon polygon= new Polygon(arrays.x,arrays.y,arrays.length);
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
	public void drawImage1s(ChoisePoint iX, Term a1) {
		drawImage(a1,iX);
		repaintAfterDelay(iX);
	}
	public void drawImage2s(ChoisePoint iX, Term a1, Term a2) {
		try {
			Color color= GUI_Utils.termToColor(a2,iX);
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
			Color color= GUI_Utils.termToColor(a4,iX);
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
			Color color= GUI_Utils.termToColor(a6,iX);
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
			Color color= GUI_Utils.termToColor(a10,iX);
			drawImage(a1,a2,a3,a4,a5,a6,a7,a8,a9,color,iX);
		} catch (TermIsSymbolDefault e1) {
			drawImage(a1,a2,a3,a4,a5,a6,a7,a8,a9,iX);
		};
		repaintAfterDelay(iX);
	}
	protected void drawImage(Term a1, ChoisePoint iX) {
		if (a1 instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)a1;
			append_command(new Java2DDrawImage(image.getImage(iX)));
		} else {
			java.awt.image.BufferedImage image= readImage(a1,iX);
			append_command(new Java2DDrawImage(image));
		}
	}
	protected void drawImage(Term a1, Color color, ChoisePoint iX) {
		if (a1 instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)a1;
			append_command(new Java2DDrawImage(image.getImage(iX),color));
		} else {
			java.awt.image.BufferedImage image= readImage(a1,iX);
			append_command(new Java2DDrawImage(image,color));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, ChoisePoint iX) {
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		if (a1 instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)a1;
			append_command(new Java2DDrawImage(image.getImage(iX),x1,y1));
		} else {
			java.awt.image.BufferedImage image= readImage(a1,iX);
			append_command(new Java2DDrawImage(image,x1,y1));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Color color, ChoisePoint iX) {
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		if (a1 instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)a1;
			append_command(new Java2DDrawImage(image.getImage(iX),x1,y1,color));
		} else {
			java.awt.image.BufferedImage image= readImage(a1,iX);
			append_command(new Java2DDrawImage(image,x1,y1,color));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, ChoisePoint iX) {
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		double width= Converters.argumentToReal(a4,iX);
		double height= Converters.argumentToReal(a5,iX);
		if (a1 instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)a1;
			append_command(new Java2DScaleAndDrawImage(image.getImage(iX),x1,y1,width,height));
		} else {
			java.awt.image.BufferedImage image= readImage(a1,iX);
			append_command(new Java2DScaleAndDrawImage(image,x1,y1,width,height));
		}
	}
	protected void drawImage(Term a1, Term a2, Term a3, Term a4, Term a5, Color color, ChoisePoint iX) {
		double x1= Converters.argumentToReal(a2,iX);
		double y1= Converters.argumentToReal(a3,iX);
		double width= Converters.argumentToReal(a4,iX);
		double height= Converters.argumentToReal(a5,iX);
		if (a1 instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)a1;
			append_command(new Java2DScaleAndDrawImage(image.getImage(iX),x1,y1,width,height,color));
		} else {
			java.awt.image.BufferedImage image= readImage(a1,iX);
			append_command(new Java2DScaleAndDrawImage(image,x1,y1,width,height,color));
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
		if (a1 instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)a1;
			append_command(new Java2DPickOutAndDrawImage(
				image.getImage(iX),
				destinationX1,
				destinationY1,
				destinationX2,
				destinationY2,
				sourceX1,
				sourceY1,
				sourceX2,
				sourceY2));
		} else {
			java.awt.image.BufferedImage image= readImage(a1,iX);
			append_command(new Java2DPickOutAndDrawImage(
				image,
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
		if (a1 instanceof BufferedImage) {
			BufferedImage image= (BufferedImage)a1;
			append_command(new Java2DPickOutAndDrawImage(
				image.getImage(iX),
				destinationX1,
				destinationY1,
				destinationX2,
				destinationY2,
				sourceX1,
				sourceY1,
				sourceX2,
				sourceY2,
				color));
		} else {
			java.awt.image.BufferedImage image= readImage(a1,iX);
			append_command(new Java2DPickOutAndDrawImage(
				image,
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
	public void setPen1s(ChoisePoint iX, Term value) {
		StrokeAndColor attributes= Tools2D.termToStrokeAndColor(value,iX);
		append_command(new Java2DSetPen(attributes));
	}
	//
	public void setBrush1s(ChoisePoint iX, Term value) {
		BrushAttributes attributes= Tools2D.termToBrushAttributes(value,this,iX);
		append_command(new Java2DSetBrushAttributes(attributes));
	}
	//
	public void setFont1s(ChoisePoint iX, Term value) {
		Font font= Tools2D.termToFont2D(value,this,iX);
		append_command(new Java2DSetFont(font));
	}
	//
	public void setTextAlignment2s(ChoisePoint iX, Term hA, Term vA) {
		Canvas2DHorizontalAlignment horizontalAlignment= Tools2D.termToHorizontalAlignment(hA,iX);
		Canvas2DVerticalAlignment verticalAlignment= Tools2D.termToVerticalAlignment(vA,iX);
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
			color= GUI_Utils.termToColor(value,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				Term backgroundColor= getBuiltInSlot_E_background_color();
				color= GUI_Utils.termToColor(backgroundColor,iX);
			} catch (TermIsSymbolDefault e2) {
				color= defaultBackgroundColor;
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
	public void suspendRedrawing0s(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		if (space2DDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			synchronized(this) {
				suspendRedrawing= true;
				skipDelayedRepainting(iX);
				if (space2D != null) {
					space2D.suspendRedrawing();
				}
			}
		}
	}
	public void drawNow0s(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		if (space2DDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			synchronized(this) {
				suspendRedrawing= false;
				implementDelayedCleaning= false;
				if (graphicWindow != null) {
					if (space2D != null) {
						space2D.releaseRedrawing();
					};
					graphicWindow.safelyRepaint();
				} else if (space2D != null) {
					space2D.releaseRedrawing();
					// 2013.11.16: Inner Canvas2D repainting problem
					// DesktopUtils.safelyRepaint(space2D);
					space2D.repaintAfterDelay();
				}
			}
		}
	}
	//
	public void save1s(ChoisePoint iX, Term a1) {
		String fileName= retrieveFileName(a1,iX);
		saveContent(fileName,null,iX);
	}
	public void save2s(ChoisePoint iX, Term a1, Term a2) {
		String fileName= retrieveFileName(a1,iX);
		saveContent(fileName,a2,iX);
	}
	protected void saveContent(String fileName, Term attributes, ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized(this) {
			if (graphicWindow != null) {
				Rectangle bounds= graphicWindow.getBounds(null);
				Insets insents= graphicWindow.getInsets();
				Dimension size= new Dimension();
				graphicWindow.getSize(size);
				GraphicsConfiguration gc= graphicWindow.getGraphicsConfiguration();
				java.awt.image.BufferedImage bufferedImage= gc.createCompatibleImage(
					size.width,
					size.height);
				Graphics g= bufferedImage.getGraphics();
				int x0= - (bounds.width - size.width - insents.right);
				int y0= - (bounds.height - size.height - insents.bottom);
				g.translate(x0,y0);
				graphicWindow.print(g);
				g.dispose();
				boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
				Tools2D.write(fileName,backslashIsSeparator,bufferedImage,attributes,iX);
			} else if (space2D != null) {
				Rectangle bounds= space2D.getBounds(null);
				Insets insents= space2D.getInsets();
				Dimension size= new Dimension();
				space2D.getSize(size);
				GraphicsConfiguration gc= space2D.getGraphicsConfiguration();
				if (gc != null) {
					java.awt.image.BufferedImage bufferedImage= gc.createCompatibleImage(
						size.width,
						size.height);
					Graphics g= bufferedImage.getGraphics();
					int x0= - (bounds.width - size.width - insents.right);
					int y0= - (bounds.height - size.height - insents.bottom);
					g.translate(x0,y0);
					space2D.print(g);
					g.dispose();
					boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
					Tools2D.write(fileName,backslashIsSeparator,bufferedImage,attributes,iX);
				}
			}
		}
	}
	//
	public void action1s(ChoisePoint iX, Term actionName) {
	}
	//
	public class Action1s extends Continuation {
		// private Continuation c0;
		//
		public Action1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseClicked1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseClicked1s extends Continuation {
		// private Continuation c0;
		//
		public MouseClicked1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseEntered1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseEntered1s extends Continuation {
		// private Continuation c0;
		//
		public MouseEntered1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseExited1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseExited1s extends Continuation {
		// private Continuation c0;
		//
		public MouseExited1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mousePressed1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MousePressed1s extends Continuation {
		// private Continuation c0;
		//
		public MousePressed1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseReleased1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseReleased1s extends Continuation {
		// private Continuation c0;
		//
		public MouseReleased1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseDragged1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseDragged1s extends Continuation {
		// private Continuation c0;
		//
		public MouseDragged1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseMoved1s(ChoisePoint iX, Term mouseEvent) {
	}
	//
	public class MouseMoved1s extends Continuation {
		// private Continuation c0;
		//
		public MouseMoved1s(Continuation aC, Term mouseEvent) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void initialize0s(ChoisePoint iX) {
	}
	public void start0s(ChoisePoint iX) {
	}
	public void stop0s(ChoisePoint iX) {
	}
	//
	public void registerCanvas2D(ExtendedSpace2D s) {
		synchronized(this) {
			if (space2D==null) {
				space2D= s;
				space2D.setTargetWorld(this);
				space2D.setCommands(actualCommands);
				space2D.setEnableAntialiasing(sceneAntialiasingIsEnabled);
				reviseMouseListenerStatus();
				reviseMouseMotionListenerStatus();
			}
		}
	}
	//
	public void release(boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		synchronized(this) {
			if (space2D != null && graphicWindow==null) {
				space2D.skipDelayedRepainting();
				space2D.disableMouseListener();
				space2D.disableMouseMotionListener();
				// actualCommands.clear();
				// retractedCommands.clear();
				// implementDelayedCleaning= false;
				space2D= null;
			}
		};
		long domainSignature= entry_s_Stop_0();
		callInternalProcedure(domainSignature,dialogIsModal,modalChoisePoint);
	}
	//
	public void draw(boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		if (controlIsInitialized.compareAndSet(false,true)) {
			long domainSignature1= entry_s_Initialize_0();
			callInternalProcedure(domainSignature1,dialogIsModal,modalChoisePoint);
		};
		long domainSignature2= entry_s_Start_0();
		callInternalProcedure(domainSignature2,dialogIsModal,modalChoisePoint);
	}
	// Auxiliary operations
	// protected boolean desktopDoesNotExist() {
	//	MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
	//	if (desktop==null) {
	//		return true;
	//	} else {
	//		return false;
	//	}
	// }
	public boolean space2DDoesNotExist() {
		// Map<AbstractWorld,InternalFrame2D> innerWindows= StaticAttributes2D.retrieveInnerWindows(staticContext);
		// return !innerWindows.containsKey(this);
		synchronized(this) {
			return (space2D==null);
		}
	}
	protected void createGraphicWindowIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		synchronized(this) {
			if (space2D==null && !controlIsInitialized.get()) {
				DesktopUtils.createPaneIfNecessary(staticContext);
				graphicWindow= createInternalFrameIfNecessary(iX,enableMovingWindowToFront);
				space2D= graphicWindow.space;
			} else if (graphicWindow != null) {
				if (!suspendRedrawing) {
					if (enableMovingWindowToFront) {
						DesktopUtils.safelyMoveToFront(graphicWindow);
					};
					DesktopUtils.safelySetVisible(true,graphicWindow);
				}
			}
		}
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
				graphicWindow= createInternalFrame(iX);
				restoreWindow= true;
			};
			reviseCanvas2DListenersStatus(iX);
		} else {
			moveWindowToFront= true;
		};
		if (restoreWindow) {
			graphicWindow.safelyRestoreSize(staticContext);
		};
		if (moveWindowToFront && enableMovingWindowToFront) {
			DesktopUtils.safelyMoveToFront(graphicWindow);
		};
		DesktopUtils.safelySetVisible(true,graphicWindow);
		return graphicWindow;
	}
	//
	protected InternalFrame2D createInternalFrame(ChoisePoint iX) {
		//
		// String title= getBuiltInSlot_E_title().toString(iX);
		String title= null;
		try {
			title= GUI_Utils.termToFrameTitleSafe(getBuiltInSlot_E_title(),iX);
		} catch (TermIsSymbolDefault e) {
			title= "";
		};
		//
		InternalFrame2D graphicWindow= new InternalFrame2D(this,title,staticContext,actualCommands,scalingFactor);
		Map<AbstractWorld,InternalFrame2D> innerWindows= StaticAttributes2D.retrieveInnerWindows(staticContext);
		innerWindows.put(this,graphicWindow);
		//
		// graphicWindow.addComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		desktop.add(graphicWindow);
		//
		redrawInternalFrame(graphicWindow,null,iX);
		//
		return graphicWindow;
	}
	protected void redrawInternalFrame(InternalFrame2D graphicWindow, ChoisePoint iX) {
		// String title= getBuiltInSlot_E_title().toString(iX);
		String title= null;
		try {
			title= GUI_Utils.termToFrameTitleSafe(getBuiltInSlot_E_title(),iX);
		} catch (TermIsSymbolDefault e) {
			title= "";
		};
		redrawInternalFrame(graphicWindow,title,iX);
	}
	protected void redrawInternalFrame(InternalFrame2D graphicWindow, String title, ChoisePoint iX) {
		//
		if (title != null) {
			DesktopUtils.safelySetTitle(title,graphicWindow);
		};
		//
		Term x= getBuiltInSlot_E_x();
		Term y= getBuiltInSlot_E_y();
		Term width= getBuiltInSlot_E_width().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		Term height= getBuiltInSlot_E_height().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		//
		graphicWindow.logicalWidth.set(GUI_Utils.termToSize(width,iX));
		graphicWindow.logicalHeight.set(GUI_Utils.termToSize(height,iX));
		graphicWindow.logicalX.set(GUI_Utils.termToCoordinate(x,iX));
		graphicWindow.logicalY.set(GUI_Utils.termToCoordinate(y,iX));
		//
		changeBackgroundColor(iX,getBuiltInSlot_E_background_color());
		//
		Term enableSceneAntialiasing= getBuiltInSlot_E_enable_scene_antialiasing();
		sceneAntialiasingIsEnabled.set(Converters.term2YesNo(enableSceneAntialiasing,iX));
		//
		if (redefinedScalingFactor != null) {
			scalingFactor.set(redefinedScalingFactor);
		} else {
			Canvas2DScalingFactor factor= Tools2D.termToScalingFactor(getBuiltInSlot_E_scaling_factor(),iX);
			scalingFactor.set(factor);
		}
	}
	protected void redrawCanvas2D(ChoisePoint iX) {
		//
		changeBackgroundColor(iX,getBuiltInSlot_E_background_color());
		//
		Term enableSceneAntialiasing= getBuiltInSlot_E_enable_scene_antialiasing();
		sceneAntialiasingIsEnabled.set(Converters.term2YesNo(enableSceneAntialiasing,iX));
		//
		if (redefinedScalingFactor != null) {
			scalingFactor.set(redefinedScalingFactor);
		} else {
			Canvas2DScalingFactor factor= Tools2D.termToScalingFactor(getBuiltInSlot_E_scaling_factor(),iX);
			scalingFactor.set(factor);
		}
	}
	//
	public void changeBackgroundColor(ChoisePoint iX, Term requiredColor) {
		Color color;
		try {
			color= GUI_Utils.termToColor(requiredColor,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				requiredColor= getBuiltInSlot_E_background_color();
				color= GUI_Utils.termToColor(requiredColor,iX);
			} catch (TermIsSymbolDefault e2) {
				color= defaultBackgroundColor;
			}
		};
		synchronized(this) {
			if (graphicWindow != null) {
				graphicWindow.safelySetBackground(color);
			} else if (space2D != null) {
				safelySetCanvas2DBackground(color);
			}
		}
	}
	protected void safelySetCanvas2DBackground(final Color color) {
		if (SwingUtilities.isEventDispatchThread()) {
			if (space2D != null) {
				space2D.setBackground(color);
			}
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						if (space2D != null) {
							space2D.setBackground(color);
						}
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public void setMenu(ChoisePoint iX, InternalFrame2D graphicWindow, Term value) {
		synchronized(this) {
			if (graphicWindow != null) {
				JMenuBar menuBar= MenuUtils.termToJMenuBar(value,graphicWindow,iX);
				graphicWindow.safelySetMenu(menuBar);
			}
		}
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
			reviseCanvas2DListenersStatus(iX);
		}
	}
	public void reviseCanvas2DListenersStatus(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		if (space2DDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			// DesktopUtils.safelySetVisible(false,graphicWindow);
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
				} else if (space2D != null) {
					space2D.enableMouseListener();
				}
			} else {
				if (graphicWindow != null) {
					graphicWindow.disableMouseListener();
				} else if (space2D != null) {
					space2D.disableMouseListener();
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
				} else if (space2D != null) {
					space2D.enableMouseMotionListener();
				}
			} else {
				if (graphicWindow != null) {
					graphicWindow.disableMouseMotionListener();
				} else if (space2D != null) {
					space2D.disableMouseMotionListener();
				}
			}
		}
	}
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
	public void repaintAfterDelay(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		if (space2DDoesNotExist()) {
			return;
		} else {
			if (!suspendRedrawing) {
				createGraphicWindowIfNecessary(iX,false);
				synchronized(this) {
					if (graphicWindow != null) {
						graphicWindow.repaintAfterDelay();
					} else if (space2D != null) {
						space2D.repaintAfterDelay();
					}
				}
			}
		}
	}
	public void skipDelayedRepainting(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		if (space2DDoesNotExist()) {
			return;
		} else {
			createGraphicWindowIfNecessary(iX,false);
			synchronized(this) {
				if (graphicWindow != null) {
					graphicWindow.skipDelayedRepainting();
				} else if (space2D != null) {
					space2D.skipDelayedRepainting();
				}
			}
		}
	}
	//
	protected String retrieveFileName(Term name, ChoisePoint iX) {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_always_is_separator(),iX);
		try {
			String textName= name.getStringValue(iX);
			textName= FileUtils.replaceBackslashes(textName,backslashIsSeparator);
			return FileUtils.makeRealName(textName);
		} catch (TermIsNotAString e) {
			throw new WrongTermIsNotFileName(name);
		}
	}
}
