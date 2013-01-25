// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.checker.*;
import morozov.system.files.*;
import morozov.system.gui.*;
import morozov.system.gui.space3d.*;
import morozov.terms.*;

// import java.awt.*;
import java.awt.Color;
import javax.swing.SwingUtilities;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import java.io.IOException;

import javax.media.j3d.Shape3D;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.Behavior;
import javax.media.j3d.PointLight;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.View;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.vecmath.Vector3d;
import javax.vecmath.Color3f;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;

import java.util.concurrent.atomic.AtomicReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Enumeration;
import java.math.BigDecimal;
import java.math.MathContext;
import java.lang.reflect.InvocationTargetException;

public abstract class Canvas3D
		extends Alpha
		implements ComponentListener {
	//
	protected static final int defaultWaitingInterval= -1;
	//
	abstract protected Term getBuiltInSlot_E_max_waiting_time();
	abstract protected Term getBuiltInSlot_E_backslash_is_separator_always();
	//
	HashMap<NodeLabel,NodeContainer> localMemory= new HashMap<NodeLabel,NodeContainer>();
	HashMap<Node,NodeLabel> inverseTable= new HashMap<Node,NodeLabel>();
	//
	protected static final Color defaultBackgroundColor= Color.BLACK;
	//
	protected AtomicReference<Color> backgroundColor= new AtomicReference<Color>();
	protected ExtendedCanvas3D canvas3D= null;
	//
	// protected int redrawingPeriod= 31; // [ms]
	protected int redrawingPeriod= 310; // [ms]
	//
	abstract protected Term getBuiltInSlot_E_title();
	abstract protected Term getBuiltInSlot_E_x();
	abstract protected Term getBuiltInSlot_E_y();
	abstract protected Term getBuiltInSlot_E_width();
	abstract protected Term getBuiltInSlot_E_height();
	abstract protected Term getBuiltInSlot_E_background_color();
	//
	abstract protected Term getBuiltInSlot_E_projection_policy();
	abstract protected Term getBuiltInSlot_E_window_resize_policy();
	abstract protected Term getBuiltInSlot_E_window_movement_policy();
	abstract protected Term getBuiltInSlot_E_visibility_policy();
	abstract protected Term getBuiltInSlot_E_transparency_sorting_policy();
	abstract protected Term getBuiltInSlot_E_minimum_frame_cycle_time();
	//
	abstract protected Term getBuiltInSlot_E_field_of_view();
	abstract protected Term getBuiltInSlot_E_front_clip_distance();
	abstract protected Term getBuiltInSlot_E_back_clip_distance();
	//
	abstract protected Term getBuiltInSlot_E_enable_scene_antialiasing();
	abstract protected Term getBuiltInSlot_E_enable_depth_buffer_freezing();
	abstract protected Term getBuiltInSlot_E_enable_local_eye_lighting();
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
	public void clear0s(ChoisePoint iX) {
		if (desktopDoesNotExist()) {
			return;
		} else if (graphicWindowDoesNotExist()) {
			return;
		} else {
			// DesktopUtils.createPaneIfNecessary(staticContext);
			// InternalFrame3D graphicWindow= createInternalFrameIfNecessary(iX);
			// localMemory.clear();
			show1s(iX,new PrologEmptyList());
		}
	}
	//
	public void show1s(ChoisePoint iX, Term a1) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		InternalFrame3D graphicWindow= createInternalFrameIfNecessary(iX,true);
		showCanvas(a1,iX,graphicWindow);
	}
	public void show0s(ChoisePoint iX) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		createInternalFrameIfNecessary(iX,true);
	}
	//
	protected void showCanvas(Term a1, ChoisePoint iX, InternalFrame3D graphicWindow) {
		//
		// GraphicsConfiguration config=
		//	SimpleUniverse.getPreferredConfiguration();
		GraphicsConfigTemplate3D gct3D= new GraphicsConfigTemplate3D();
		gct3D.setSceneAntialiasing(GraphicsConfigTemplate3D.REQUIRED);
		GraphicsConfiguration config=
			java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(gct3D);
		//
		canvas3D= new ExtendedCanvas3D(config);
		Color background= backgroundColor.get();
		if (background != null) {
			canvas3D.setBackground(background);
		};
		//
		localMemory.clear();
		//
		// SimpleUniverse is a Convenience Utility class
		SimpleUniverse simpleU= new SimpleUniverse(canvas3D);
		//
		setViewAttributes(simpleU,iX);
		//
		BranchGroup scene= Utils3D.termToBranchGroupOrNodeList(a1,this,simpleU,canvas3D,iX);
		//
		Transform3D tr= new Transform3D();
		// tr.setScale(1.0);
		TransformGroup tg= new TransformGroup(tr);
		//
		TransformGroup spin= new TransformGroup();
		spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spin.addChild(tg);
		//
		// javax.media.j3d.Alpha alpha= new javax.media.j3d.Alpha();
		// alpha.setMode(javax.media.j3d.Alpha.INCREASING_ENABLE+javax.media.j3d.Alpha.DECREASING_ENABLE);
		// alpha.setDecreasingAlphaDuration(10000);
		// alpha.setIncreasingAlphaDuration(10000);
		//
		// RotationInterpolator rotator= new RotationInterpolator(alpha,spin);
		// rotator.setMinimumAngle((float)(-0.1*(StrictMath.PI)/360));
		// rotator.setMaximumAngle((float)(0.1*(StrictMath.PI)/360));
		// spin.addChild(rotator);
		//
		SpecialBehavior specialBehavior= new SpecialBehavior(spin);
		specialBehavior.setSchedulingBounds(new BoundingSphere());
		spin.addChild(specialBehavior);
		//
		spin.addChild(scene);
		//
		BranchGroup branchGroup= new BranchGroup();
		branchGroup.addChild(spin);
		//
		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		simpleU.getViewingPlatform().setNominalViewingTransform();
		// simpleU.addBranchGraph(scene);
		simpleU.addBranchGraph(branchGroup);
		graphicWindow.add(canvas3D);
		graphicWindow.revalidate();
	}
	protected void setViewAttributes(SimpleUniverse susimpleU, ChoisePoint iX) {
		//
		Viewer viewer= susimpleU.getViewer();
		View view= viewer.getView();
		//
		int projectionPolicy= Utils3D.termToProjectionPolicy(getBuiltInSlot_E_projection_policy(),iX);
		view.setProjectionPolicy(projectionPolicy);
		int windowResizePolicy= Utils3D.termToWindowResizePolicy(getBuiltInSlot_E_window_resize_policy(),iX);
		view.setWindowResizePolicy(windowResizePolicy);
		int windowMovementPolicy= Utils3D.termToWindowMovementPolicy(getBuiltInSlot_E_window_movement_policy(),iX);
		view.setWindowMovementPolicy(windowMovementPolicy);
		int visibilityPolicy= Utils3D.termToVisibilityPolicy(getBuiltInSlot_E_visibility_policy(),iX);
		view.setVisibilityPolicy(visibilityPolicy);
		int transparencySortingPolicy= Utils3D.termToTransparencySortingPolicy(getBuiltInSlot_E_transparency_sorting_policy(),iX);
		view.setTransparencySortingPolicy(transparencySortingPolicy);
		Term minimumFrameCycleTime= getBuiltInSlot_E_minimum_frame_cycle_time();
		try {
			BigDecimal nanos= Converters.termToTimeInterval(minimumFrameCycleTime,iX);
			BigDecimal milliseconds= nanos.divideToIntegralValue(Converters.oneMillionBig,MathContext.DECIMAL128);
			long delayInMilliseconds= PrologInteger.toLong(milliseconds);
			view.setMinimumFrameCycleTime(delayInMilliseconds);
		} catch (TermIsNotTimeInterval e1) {
			try {
				long code= minimumFrameCycleTime.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
				} else {
					throw new WrongArgumentIsNotTimeInterval(minimumFrameCycleTime);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongArgumentIsNotTimeInterval(minimumFrameCycleTime);
			}
		}
		//
		try {
			double fieldOfView= Utils3D.termToFieldOfView(getBuiltInSlot_E_field_of_view(),iX);
			view.setFieldOfView(fieldOfView);
		} catch (TermIsSymbolDefault e) {
		};
		try {
			double frontClipDistance= Utils3D.termToFrontClipDistance(getBuiltInSlot_E_front_clip_distance(),iX);
			view.setFrontClipDistance(frontClipDistance);
		} catch (TermIsSymbolDefault e) {
		};
		try {
			double backClipDistance= Utils3D.termToBackClipDistance(getBuiltInSlot_E_back_clip_distance(),iX);
			view.setBackClipDistance(backClipDistance);
		} catch (TermIsSymbolDefault e) {
		};
		//
		boolean enableSceneAntialiasing= Converters.term2YesNo(getBuiltInSlot_E_enable_scene_antialiasing(),iX);
		view.setSceneAntialiasingEnable(enableSceneAntialiasing);
		// System.out.printf("view.setSceneAntialiasingEnable=%s\n",view.getSceneAntialiasingEnable());
		boolean enableDepthBufferFreezing= Converters.term2YesNo(getBuiltInSlot_E_enable_depth_buffer_freezing(),iX);
		view.setDepthBufferFreezeTransparent(enableDepthBufferFreezing);
		boolean enableLocalEyeLighting= Converters.term2YesNo(getBuiltInSlot_E_enable_local_eye_lighting(),iX);
		view.setLocalEyeLightingEnable(enableLocalEyeLighting);
	}
	//
	public class SpecialBehavior extends Behavior {
		private TransformGroup targetTG;
		private Transform3D rotation = new Transform3D();
		// private double angle = 0.0;
		private double maximumAngle = (0.1*(StrictMath.PI)/360) / 10000;
		private double minimumAngle = -(0.1*(StrictMath.PI)/360) / 10000;
		private WakeupOnElapsedTime wakeup = new WakeupOnElapsedTime(redrawingPeriod);
		private boolean direction = true;
		SpecialBehavior(TransformGroup tg) {
			targetTG= tg;
		}
		public void initialize() {
			wakeupOn(wakeup);
		}
		public void processStimulus(Enumeration criteria) {
			double angle;
			if (direction) {
				direction= false;
				angle= maximumAngle;
			} else {
				direction= true;
				angle= minimumAngle;
			};
			rotation.rotY(angle);
			targetTG.setTransform(rotation);
			wakeupOn(wakeup);
		}
	}
	//
	public void redraw0s(ChoisePoint iX) {
		DesktopUtils.createPaneIfNecessary(staticContext);
		Map<AbstractWorld,InternalFrame3D> innerWindows= StaticAttributes3D.retrieveInnerWindows(staticContext);
		InternalFrame3D graphicWindow= innerWindows.get(this);
		if (graphicWindow==null) {
			createInternalFrameIfNecessary(iX);
		} else {
			redrawInternalFrame(graphicWindow,iX);
			graphicWindow.safelyRestoreSize(staticContext);
			DesktopUtils.safelyRepaint(graphicWindow);
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		if (desktopDoesNotExist()) {
			return;
		} else if (graphicWindowDoesNotExist()) {
			return;
		} else {
			DesktopUtils.createPaneIfNecessary(staticContext);
			InternalFrame3D graphicWindow= createInternalFrameIfNecessary(iX);
			DesktopUtils.safelySetVisible(false,graphicWindow);
		}
	}
	//
	public void changeBackgroundColor1s(ChoisePoint iX, Term backgroundColor) {
		changeBackgroundColor(iX,backgroundColor);
	}
	//
	public void setTransform2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		NodeContainer container= localMemory.get(nodeLabel);
		if (container != null) {
			Transform3D transform= PrincipalNode3D.termToTransform3D(a2,iX);
			container.setTransform(transform);
		} else {
			throw new WrongArgumentIsUnknownNodeLabel(a1);
		}
	}
	//
	public void setTranslation2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		NodeContainer container= localMemory.get(nodeLabel);
		if (container != null) {
			// Transform3D transform= PrincipalNode3D.termToTransform3D(a2,iX);
			Vector3d vector= Tools3D.term2Vector3(a2,iX);
			container.setTranslation(vector);
		} else {
			throw new WrongArgumentIsUnknownNodeLabel(a1);
		}
	}
	//
	public void setAppearance2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		NodeContainer container= localMemory.get(nodeLabel);
		if (container != null) {
			Appearance appearance= AuxiliaryNode3D.termToAppearance(a2,this,iX);
			container.setAppearance(appearance);
		} else {
			throw new WrongArgumentIsUnknownNodeLabel(a1);
		}
	}
	//
	public void setColoringAttributes2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		NodeContainer container= localMemory.get(nodeLabel);
		if (container != null) {
			ColoringAttributes coloringAttributes= AuxiliaryNode3D.termToColoringAttributes(a2,iX);
			container.setColoringAttributes(coloringAttributes);
		} else {
			throw new WrongArgumentIsUnknownNodeLabel(a1);
		}
	}
	//
	public void setFont3D2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		NodeContainer container= localMemory.get(nodeLabel);
		if (container != null) {
			Font3D font= AuxiliaryNode3D.termToFont3D(a2,iX);
			container.setFont3D(font);
		} else {
			throw new WrongArgumentIsUnknownNodeLabel(a1);
		}
	}
	//
	public void setString2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		NodeContainer container= localMemory.get(nodeLabel);
		if (container != null) {
			container.setString(a2.toString(iX));
		} else {
			throw new WrongArgumentIsUnknownNodeLabel(a1);
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
	public void mouseClicked1s(ChoisePoint iX, Term actionName) {
        }
	//
	public class MouseClicked1s extends Continuation {
		// private Continuation c0;
		//
		public MouseClicked1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseEntered1s(ChoisePoint iX, Term actionName) {
        }
	//
	public class MouseEntered1s extends Continuation {
		// private Continuation c0;
		//
		public MouseEntered1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseExited1s(ChoisePoint iX, Term actionName) {
        }
	//
	public class MouseExited1s extends Continuation {
		// private Continuation c0;
		//
		public MouseExited1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mousePressed1s(ChoisePoint iX, Term actionName) {
        }
	//
	public class MousePressed1s extends Continuation {
		// private Continuation c0;
		//
		public MousePressed1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseReleased1s(ChoisePoint iX, Term actionName) {
        }
	//
	public class MouseReleased1s extends Continuation {
		// private Continuation c0;
		//
		public MouseReleased1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseDragged1s(ChoisePoint iX, Term actionName) {
        }
	//
	public class MouseDragged1s extends Continuation {
		// private Continuation c0;
		//
		public MouseDragged1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseMoved1s(ChoisePoint iX, Term actionName) {
        }
	//
	public class MouseMoved1s extends Continuation {
		// private Continuation c0;
		//
		public MouseMoved1s(Continuation aC, Term actionName) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	// Auxiliary operations
	protected boolean desktopDoesNotExist() {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		if (desktop==null) {
			return true;
		} else {
			return false;
		}
	}
	public boolean graphicWindowDoesNotExist() {
		Map<AbstractWorld,InternalFrame3D> innerWindows= StaticAttributes3D.retrieveInnerWindows(staticContext);
		return !innerWindows.containsKey(this);
	}
	//
	protected InternalFrame3D createInternalFrameIfNecessary(ChoisePoint iX) {
		return createInternalFrameIfNecessary(iX,false);
	}
	protected InternalFrame3D createInternalFrameIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		Map<AbstractWorld,InternalFrame3D> innerWindows= StaticAttributes3D.retrieveInnerWindows(staticContext);
		InternalFrame3D graphicWindow= innerWindows.get(this);
		boolean restoreWindow= false;
		boolean moveWindowToFront= false;
		if (graphicWindow==null) {
			synchronized(this) {
				graphicWindow= innerWindows.get(this);
				if (graphicWindow==null) {
					graphicWindow= createInternalFrame(iX);
					restoreWindow= true;
				}
			}
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
	protected InternalFrame3D createInternalFrame(ChoisePoint iX) {
		//
		String title= getBuiltInSlot_E_title().toString(iX);
		//
		InternalFrame3D graphicWindow= new InternalFrame3D(title,staticContext);
		Map<AbstractWorld,InternalFrame3D> innerWindows= StaticAttributes3D.retrieveInnerWindows(staticContext);
		innerWindows.put(this,graphicWindow);
		//
		graphicWindow.addComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		desktop.add(graphicWindow);
		//
		redrawInternalFrame(graphicWindow,null,iX);
		//
		return graphicWindow;
	}
	protected void redrawInternalFrame(InternalFrame3D graphicWindow, ChoisePoint iX) {
		String title= getBuiltInSlot_E_title().toString(iX);
		redrawInternalFrame(graphicWindow,title,iX);
	}
	protected void redrawInternalFrame(InternalFrame3D graphicWindow, String title, ChoisePoint iX) {
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
	}
	//
	public void changeBackgroundColor(ChoisePoint iX, Term requiredColor) {
		Color color;
		try {
			// color= GUI_Utils.termToColor(requiredColor,iX);
			Color3f color3d= Tools3D.term2Color3OrExit(requiredColor,iX);
			color= color3d.get();
		} catch (TermIsSymbolDefault e1) {
			try {
				requiredColor= getBuiltInSlot_E_background_color();
				// color= GUI_Utils.termToColor(requiredColor,iX);
				Color3f color3d= Tools3D.term2Color3OrExit(requiredColor,iX);
				color= color3d.get();
			} catch (TermIsSymbolDefault e2) {
				color= defaultBackgroundColor;
			}
		};
		backgroundColor.set(color);
		safelySetBackground(color);
	}
	protected void safelySetBackground(final Color color) {
		if (SwingUtilities.isEventDispatchThread()) {
			if (canvas3D != null) {
				canvas3D.setBackground(color);
			}
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						if (canvas3D != null) {
							canvas3D.setBackground(color);
						}
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public BufferedImage readImage(Term fileName, ChoisePoint iX) {
		URI uri= retrieveLocationURI(fileName,iX);
		int timeout= retrieveMaxWaitingTime(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		InputStream stream;
		try {
			byte[] array= URL_Utils.getContentOfResource(uri,null,timeout,staticContext,backslashIsSeparator);
			stream= new ByteArrayInputStream(array);
		} catch (CannotRetrieveContent e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		};
		try {
			BufferedImage image= ImageIO.read(stream);
			return image;
		} catch(IOException e1) {
			throw new FileInputOutputError(uri.toString(),e1);
		}
	}
	protected URI retrieveLocationURI(Term name, ChoisePoint iX) {
		try {
			String textName= name.getStringValue(iX);
			// textName= appendExtensionIfNecessary(textName,iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
			// System.out.printf("textName=>>>%s<<<\n",textName);
			URI uri= URL_Utils.create_URI(textName,staticContext,backslashIsSeparator);
			return uri;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(name);
		}
	}
	protected int retrieveMaxWaitingTime(ChoisePoint iX) {
		Term interval= getBuiltInSlot_E_max_waiting_time();
		try {
			return URL_Utils.termToWaitingInterval(interval,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				return URL_Utils.termToWaitingInterval(DefaultOptions.waitingInterval,iX);
			} catch (TermIsSymbolDefault e2) {
				return defaultWaitingInterval;
			}
		}
	}
	//
	public void rememberNode(NodeLabel label, TransformGroup node) {
		// System.out.printf("REMEMBER:>>>%s<<<: %s\n",label,node);
		localMemory.put(label,new NodeContainer(node));
		inverseTable.put(node,label);
	}
	public void rememberNode(NodeLabel label, Group node) {
		// System.out.printf("REMEMBER:>>>%s<<<: %s\n",label,node);
		localMemory.put(label,new NodeContainer(node));
		inverseTable.put(node,label);
	}
	public void rememberNode(NodeLabel label, Node node) {
		// System.out.printf("REMEMBER:>>>%s<<<: %s\n",label,node);
		localMemory.put(label,new NodeContainer(node));
		inverseTable.put(node,label);
	}
	public Shape3D retrieveShape3D(NodeLabel label) {
		// System.out.printf("RETRIEVE:>>>%s<<<\n",label);
		NodeContainer container= localMemory.get(label);
		if (container != null) {
			return container.retrieveShape3D(label);
		} else {
			throw new UndefinedNodeLabel(label);
		}
	}
	public PointLight retrievePointLight(NodeLabel label) {
		// System.out.printf("RETRIEVE:>>>%s<<<\n",label);
		NodeContainer container= localMemory.get(label);
		if (container != null) {
			return container.retrievePointLight(label);
		} else {
			throw new UndefinedNodeLabel(label);
		}
	}
	//
	public NodeLabel retrieveNodeLabel(Node node) {
		// System.out.printf("RETRIEVE: %s\n",node);
		// Set<Node> keys= inverseTable.keySet();
		// Iterator<Node> iterator= keys.iterator();
		// while(iterator.hasNext()) {
		//	Node key= iterator.next();
		//	System.out.printf("key: %s\n",key);
		// };
		Node current_object= node;
		while (true) {
			if (current_object==null) {
				return null;
			};
			NodeLabel label= inverseTable.get(current_object);
			if (label != null) {
				return label;
			};
			current_object= current_object.getParent();
		}
	}
	//
	public void componentHidden(ComponentEvent e) {
		DesktopUtils.selectNextInternalFrame(staticContext);
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentResized(ComponentEvent e) {
	}
	public void componentShown(ComponentEvent e) {
	}
}
