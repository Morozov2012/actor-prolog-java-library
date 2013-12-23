// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.gui.*;
import morozov.system.gui.space3d.*;
import morozov.system.gui.space3d.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Component;
import javax.swing.SwingUtilities;

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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.util.Enumeration;
import java.math.BigDecimal;
import java.math.MathContext;
import java.lang.reflect.InvocationTargetException;

// import javax.media.opengl.GLProfile;

public abstract class Canvas3D
		extends ImageConsumer
		implements ComponentListener {
	//
	protected HashMap<NodeLabel,NodeContainer> localMemory= new HashMap<NodeLabel,NodeContainer>();
	protected HashMap<Node,NodeLabel> inverseTable= new HashMap<Node,NodeLabel>();
	//
	protected HashSet<CustomizedPickCanvas> customizedPickCanvasList= new HashSet<CustomizedPickCanvas>();
	//
	protected static final Color defaultBackgroundColor= Color.BLACK;
	//
	protected AtomicReference<Color> backgroundColor= new AtomicReference<Color>();
	//
	protected AtomicBoolean controlIsInitialized= new AtomicBoolean(false);
	protected ExtendedSpace3D space3D= null;
	protected InternalFrame3D graphicWindow= null;
	protected SimpleUniverse simpleUniverse= null;
	protected TransformGroup spin= null;
	protected Term currentSceneTree= null;
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
	abstract public long entry_s_Initialize_0();
	abstract public long entry_s_Start_0();
	abstract public long entry_s_Stop_0();
	//
	public Canvas3D() {
		// This dummy call is necessary to load
		// the vecmath.jar library before the start
		// of rendering:
		Color3f color3d= new Color3f(0,0,0);
		// GLProfile.initSingleton();
	}
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
		// } else if (space3DDoesNotExist()) {
		//	return;
		// } else {
		show1s(iX,PrologEmptyList.instance);
		// }
	}
	//
	public void show1s(ChoisePoint iX, Term a1) {
		createGraphicWindowIfNecessary(iX,true);
		showCanvasAndSaveTree(a1,iX);
	}
	public void show0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,true);
	}
	//
	public static GraphicsConfiguration getGraphicsConfiguration(Component c) {
		return getGraphicsConfiguration(c.getGraphicsConfiguration());
	}
	public static GraphicsConfiguration getGraphicsConfiguration(GraphicsConfiguration dialogGraphicsConfiguration) {
		GraphicsConfigTemplate3D gct3D= new GraphicsConfigTemplate3D();
		gct3D.setSceneAntialiasing(GraphicsConfigTemplate3D.REQUIRED);
		GraphicsDevice device;
		if (dialogGraphicsConfiguration==null) {
			device= java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		} else {
			device= dialogGraphicsConfiguration.getDevice();
		};
		GraphicsConfiguration bestGraphicsConfiguration= device.getBestConfiguration(gct3D);
		return bestGraphicsConfiguration;
	}
	//
	protected void showCanvasAndSaveTree(Term value, ChoisePoint iX) {
		value= value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
		synchronized(this) {
			showCanvas(value,iX);
			currentSceneTree= value;
		}
	}
	protected void showCanvas(Term a1, ChoisePoint iX) {
		if (space3D==null) {
			if (graphicWindow != null) {
				// GraphicsConfiguration config=
				//	SimpleUniverse.getPreferredConfiguration();
				// GraphicsConfiguration config=
				//	java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(gct3D);
				GraphicsConfiguration config= getGraphicsConfiguration(graphicWindow);
				space3D= new ExtendedSpace3D(config);
				graphicWindow.add(space3D,0);
			} else {
				return;
			}
		};
		Color background= backgroundColor.get();
		if (background != null) {
			space3D.setBackground(background);
		};
		localMemory.clear();
		inverseTable.clear();
		if (spin==null) {
			Transform3D tr= new Transform3D();
			TransformGroup tg= new TransformGroup(tr);
			spin= new TransformGroup();
			spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			spin.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
			spin.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
			spin.setCapability(BranchGroup.ALLOW_DETACH);
			spin.addChild(tg);
			SpecialBehavior specialBehavior= new SpecialBehavior(spin);
			specialBehavior.setSchedulingBounds(new BoundingSphere());
			spin.addChild(specialBehavior);
		};
		if (simpleUniverse==null) {
			simpleUniverse= new SimpleUniverse(space3D,0);
			setViewAttributes(simpleUniverse,iX);
			BranchGroup scene= Utils3D.termToBranchGroupOrNodeList(a1,this,simpleUniverse,space3D,iX);
			scene.setCapability(BranchGroup.ALLOW_DETACH);
			spin.setChild(scene,0);
			BranchGroup branchGroup= new BranchGroup();
			branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
			branchGroup.addChild(spin);
			simpleUniverse.addBranchGraph(branchGroup);
		} else {
			releasePickCanvas();
			// simpleUniverse.removeAllLocales();
			// simpleUniverse.cleanup();
			setViewAttributes(simpleUniverse,iX);
			BranchGroup scene= Utils3D.termToBranchGroupOrNodeList(a1,this,simpleUniverse,space3D,iX);
			scene.setCapability(BranchGroup.ALLOW_DETACH);
			spin.setChild(scene,0);
		};
		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		simpleUniverse.getViewingPlatform().setNominalViewingTransform();
		if (graphicWindow != null) {
			graphicWindow.revalidate();
		}
	}
	protected void setViewAttributes(SimpleUniverse simpleU, ChoisePoint iX) {
		//
		Viewer viewer= simpleU.getViewer();
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
		createGraphicWindowIfNecessary(iX,false);
		synchronized(this) {
			if (graphicWindow != null) {
				redrawInternalFrame(graphicWindow,iX);
				graphicWindow.safelyRestoreSize(staticContext);
				DesktopUtils.safelyRepaint(graphicWindow);
			} else if (space3D != null) {
				changeBackgroundColor(iX,getBuiltInSlot_E_background_color());
				DesktopUtils.safelyRepaint(space3D);
			}
		}
	}
	//
	public void hide0s(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		if (space3DDoesNotExist()) {
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
		if (space3DDoesNotExist()) {
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
		if (space3DDoesNotExist()) {
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
		if (space3DDoesNotExist()) {
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
		if (space3DDoesNotExist()) {
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
		if (space3DDoesNotExist()) {
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
		changeBackgroundColor(iX,backgroundColor);
	}
	//
	public void setNode2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		synchronized(this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				BranchGroup newNode= PrincipalNode3D.termToBranchGroup(a2,this,simpleUniverse,space3D,iX);
				Node parent= c.getParent();
				if (parent != null && parent instanceof Group) {
					Group group= (Group)parent;
					int index= group.indexOfChild(c.getNode());
					if (index >= 0) {
						group.setChild(newNode,index);
					}
				}
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setTransform2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		synchronized(this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				Transform3D transform= PrincipalNode3D.termToTransform3D(a2,iX);
				c.setTransform(transform);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setTranslation2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		synchronized(this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				// Transform3D transform= PrincipalNode3D.termToTransform3D(a2,iX);
				Vector3d vector= Tools3D.term2Vector3(a2,iX);
				c.setTranslation(vector);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setAppearance2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		synchronized(this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				Appearance appearance= AuxiliaryNode3D.termToAppearance(a2,this,iX);
				c.setAppearance(appearance);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setColoringAttributes2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		synchronized(this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				ColoringAttributes coloringAttributes= AuxiliaryNode3D.termToColoringAttributes(a2,iX);
				c.setColoringAttributes(coloringAttributes);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setFont3D2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		synchronized(this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				Font3D font= AuxiliaryNode3D.termToFont3D(a2,iX);
				c.setFont3D(font);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setString2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= Tools3D.termToNodeLabel(a1,iX);
		synchronized(this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				c.setString(a2.toString(iX));
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
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
	public void mouseClicked1s(ChoisePoint iX, Term NodeLabels) {
	}
	//
	public class MouseClicked1s extends Continuation {
		// private Continuation c0;
		//
		public MouseClicked1s(Continuation aC, Term NodeLabels) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseEntered1s(ChoisePoint iX, Term NodeLabels) {
	}
	//
	public class MouseEntered1s extends Continuation {
		// private Continuation c0;
		//
		public MouseEntered1s(Continuation aC, Term NodeLabels) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseExited1s(ChoisePoint iX, Term NodeLabels) {
	}
	//
	public class MouseExited1s extends Continuation {
		// private Continuation c0;
		//
		public MouseExited1s(Continuation aC, Term NodeLabels) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mousePressed1s(ChoisePoint iX, Term NodeLabels) {
	}
	//
	public class MousePressed1s extends Continuation {
		// private Continuation c0;
		//
		public MousePressed1s(Continuation aC, Term NodeLabels) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseReleased1s(ChoisePoint iX, Term NodeLabels) {
	}
	//
	public class MouseReleased1s extends Continuation {
		// private Continuation c0;
		//
		public MouseReleased1s(Continuation aC, Term NodeLabels) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseDragged1s(ChoisePoint iX, Term NodeLabels) {
	}
	//
	public class MouseDragged1s extends Continuation {
		// private Continuation c0;
		//
		public MouseDragged1s(Continuation aC, Term NodeLabels) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void mouseMoved1s(ChoisePoint iX, Term NodeLabels) {
	}
	//
	public class MouseMoved1s extends Continuation {
		// private Continuation c0;
		//
		public MouseMoved1s(Continuation aC, Term NodeLabels) {
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
	public void registerCanvas3D(ExtendedSpace3D s, ChoisePoint iX) {
		synchronized(this) {
			if (space3D==null) {
				space3D= s;
				if (currentSceneTree != null) {
					showCanvas(currentSceneTree,iX);
				}
			}
		}
	}
	//
	public void release(boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		synchronized(this) {
			if (simpleUniverse!=null && graphicWindow==null) {
				releasePickCanvas();
				simpleUniverse.removeAllLocales();
				simpleUniverse.cleanup();
				simpleUniverse= null;
				spin= null;
				space3D= null;
			};
			localMemory.clear();
			inverseTable.clear();
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
	public boolean space3DDoesNotExist() {
		// Map<AbstractWorld,InternalFrame3D> innerWindows= StaticAttributes3D.retrieveInnerWindows(staticContext);
		// return !innerWindows.containsKey(this);
		synchronized(this) {
			return (space3D==null);
		}
	}
	protected void createGraphicWindowIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		synchronized(this) {
			if (space3D==null && !controlIsInitialized.get()) {
				DesktopUtils.createPaneIfNecessary(staticContext);
				graphicWindow= createInternalFrameIfNecessary(iX,enableMovingWindowToFront);
			} else if (graphicWindow != null) {
				if (enableMovingWindowToFront) {
					DesktopUtils.safelyMoveToFront(graphicWindow);
				};
				DesktopUtils.safelySetVisible(true,graphicWindow);
			}
		}
	}
	//
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
		// String title= getBuiltInSlot_E_title().toString(iX);
		String title= null;
		try {
			title= GUI_Utils.termToFrameTitleSafe(getBuiltInSlot_E_title(),iX);
		} catch (TermIsSymbolDefault e) {
			title= "";
		};
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
	protected void redrawInternalFrame(InternalFrame3D container, ChoisePoint iX) {
		// String title= getBuiltInSlot_E_title().toString(iX);
		String title= null;
		try {
			title= GUI_Utils.termToFrameTitleSafe(getBuiltInSlot_E_title(),iX);
		} catch (TermIsSymbolDefault e) {
			title= "";
		};
		redrawInternalFrame(container,title,iX);
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
			if (space3D != null) {
				space3D.setBackground(color);
			}
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						if (space3D != null) {
							space3D.setBackground(color);
						}
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	public void rememberNode(NodeLabel label, TransformGroup node) {
		synchronized(this) {
			localMemory.put(label,new NodeContainer(node));
			inverseTable.put(node,label);
		}
	}
	public void rememberNode(NodeLabel label, Group node) {
		synchronized(this) {
			localMemory.put(label,new NodeContainer(node));
			inverseTable.put(node,label);
		}
	}
	public void rememberNode(NodeLabel label, Node node) {
		synchronized(this) {
			localMemory.put(label,new NodeContainer(node));
			inverseTable.put(node,label);
		}
	}
	public Shape3D retrieveShape3D(NodeLabel label) {
		synchronized(this) {
			NodeContainer c= localMemory.get(label);
			if (c != null) {
				return c.retrieveShape3D(label);
			} else {
				throw new UndefinedNodeLabel(label);
			}
		}
	}
	public PointLight retrievePointLight(NodeLabel label) {
		synchronized(this) {
			NodeContainer c= localMemory.get(label);
			if (c != null) {
				return c.retrievePointLight(label);
			} else {
				throw new UndefinedNodeLabel(label);
			}
		}
	}
	//
	public NodeLabel retrieveNodeLabel(Node node) {
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
	//
	public void registerPickCanvas(CustomizedPickCanvas pc) {
		customizedPickCanvasList.add(pc);
	}
	public void releasePickCanvas() {
		Iterator<CustomizedPickCanvas> nodeList= customizedPickCanvasList.iterator();
		while (nodeList.hasNext()) {
			CustomizedPickCanvas node= nodeList.next();
			node.release();
		};
		customizedPickCanvasList.clear();
	}
}
