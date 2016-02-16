// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;
import morozov.system.gui.space3d.*;
import morozov.system.gui.space3d.errors.*;
import morozov.system.gui.space3d.signals.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Component;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.util.Enumeration;

public abstract class Canvas3D extends DataResourceConsumer {
	//
	protected HashMap<NodeLabel,NodeContainer> localMemory= new HashMap<NodeLabel,NodeContainer>();
	protected HashMap<Node,NodeLabel> inverseTable= new HashMap<Node,NodeLabel>();
	//
	protected HashSet<CustomizedPickCanvas> customizedPickCanvasList= new HashSet<CustomizedPickCanvas>();
	//
	protected AtomicBoolean controlIsInitialized= new AtomicBoolean(false);
	// protected ExtendedSpace3D space3D= null;
	// protected InternalFrame3D graphicWindow= null;
	protected SimpleUniverse simpleUniverse= null;
	protected TransformGroup spin= null;
	protected Term currentSceneTree= null;
	//
	protected Integer projectionPolicy= null;
	protected Integer windowResizePolicy= null;
	protected Integer windowMovementPolicy= null;
	protected Integer visibilityPolicy= null;
	protected Integer transparencySortingPolicy= null;
	protected WaitingInterval minimumFrameCycleTime= null;
	protected FieldOfView fieldOfView= null;
	protected ClipDistance frontClipDistance= null;
	protected ClipDistance backClipDistance= null;
	// protected Boolean enableSceneAntialiasing= null;
	protected Boolean enableDepthBufferFreezing= null;
	protected Boolean enableLocalEyeLighting= null;
	//
	// protected BigDecimal decimalDefaultMinimumFrameCycleTimeInSeconds= BigDecimal.ZERO;
	// protected BigDecimal decimalDefaultMinimumFrameCycleTimeInNanos= decimalDefaultMinimumFrameCycleTimeInSeconds.multiply(Converters.oneNanoBig);
	protected long longDefaultMinimumFrameCycleTimeInMilliseconds= 0;
	protected double defaultFieldOfView= Math.PI/4.0;
	protected double defaultFrontClipDistance= 0.1;
	protected double defaultBackClipDistance= 10.0;
	//
	// protected int redrawingPeriod= 31; // [ms]
	protected int redrawingPeriod= 310; // [ms]
	//
	public Canvas3D() {
		loadVectmath();
		spaceAttributes= new CanvasSpaceAttributes();
	}
	public Canvas3D(GlobalWorldIdentifier id) {
		super(id);
		loadVectmath();
		spaceAttributes= new CanvasSpaceAttributes();
	}
	protected void loadVectmath() {
		// This dummy call is necessary to load
		// the vecmath.jar library before the start
		// of rendering:
		Color3f color3d= new Color3f(0,0,0);
		// GLProfile.initSingleton();
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
	// abstract protected Term getBuiltInSlot_E_enable_scene_antialiasing();
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
	// abstract public long entry_s_Initialize_0();
	// abstract public long entry_s_Start_0();
	// abstract public long entry_s_Stop_0();
	//
	///////////////////////////////////////////////////////////////
	//
	protected Color getDefaultBackgroundColor() {
		return Color.BLACK;
	}
	//
	public void changeBackgroundColor(ExtendedColor eColor, ChoisePoint iX) {
		Color color;
		try {
			color= eColor.getValue();
		} catch (UseDefaultColor e) {
			color= getDefaultBackgroundColor();
		};
		synchronized(this) {
			if (graphicWindow != null) {
				graphicWindow.safelySetBackground(color);
			} else if (canvasSpace != null) {
				canvasSpace.safelySetBackground(color);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set projectionPolicy
	//
	public void setProjectionPolicy1s(ChoisePoint iX, Term a1) {
		setProjectionPolicy(Utils3D.termToProjectionPolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setProjectionPolicy(getProjectionPolicy(iX));
		}
	}
	public void setProjectionPolicy(int value) {
		projectionPolicy= value;
	}
	public void getProjectionPolicy0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= Utils3D.projectionPolicyToTerm(getProjectionPolicy(iX));
	}
	public void getProjectionPolicy0fs(ChoisePoint iX) {
	}
	public int getProjectionPolicy(ChoisePoint iX) {
		if (projectionPolicy != null) {
			return projectionPolicy;
		} else {
			Term value= getBuiltInSlot_E_projection_policy();
			return Utils3D.termToProjectionPolicy(value,iX);
		}
	}
	//
	// get/set windowResizePolicy
	//
	public void setWindowResizePolicy1s(ChoisePoint iX, Term a1) {
		setWindowResizePolicy(Utils3D.termToWindowResizePolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setWindowResizePolicy(getWindowResizePolicy(iX));
		}
	}
	public void setWindowResizePolicy(int value) {
		windowResizePolicy= value;
	}
	public void getWindowResizePolicy0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= Utils3D.windowResizePolicyToTerm(getWindowResizePolicy(iX));
	}
	public void getWindowResizePolicy0fs(ChoisePoint iX) {
	}
	public int getWindowResizePolicy(ChoisePoint iX) {
		if (windowResizePolicy != null) {
			return windowResizePolicy;
		} else {
			Term value= getBuiltInSlot_E_window_resize_policy();
			return Utils3D.termToWindowResizePolicy(value,iX);
		}
	}
	//
	// get/set windowMovementPolicy
	//
	public void setWindowMovementPolicy1s(ChoisePoint iX, Term a1) {
		setWindowMovementPolicy(Utils3D.termToWindowMovementPolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setWindowMovementPolicy(getWindowMovementPolicy(iX));
		}
	}
	public void setWindowMovementPolicy(int value) {
		windowMovementPolicy= value;
	}
	public void getWindowMovementPolicy0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= Utils3D.windowMovementPolicyToTerm(getWindowMovementPolicy(iX));
	}
	public void getWindowMovementPolicy0fs(ChoisePoint iX) {
	}
	public int getWindowMovementPolicy(ChoisePoint iX) {
		if (windowMovementPolicy != null) {
			return windowMovementPolicy;
		} else {
			Term value= getBuiltInSlot_E_window_movement_policy();
			return Utils3D.termToWindowMovementPolicy(value,iX);
		}
	}
	//
	// get/set visibilityPolicy
	//
	public void setVisibilityPolicy1s(ChoisePoint iX, Term a1) {
		setVisibilityPolicy(Utils3D.termToVisibilityPolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setVisibilityPolicy(getVisibilityPolicy(iX));
		}
	}
	public void setVisibilityPolicy(int value) {
		visibilityPolicy= value;
	}
	public void getVisibilityPolicy0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= Utils3D.visibilityPolicyToTerm(getVisibilityPolicy(iX));
	}
	public void getVisibilityPolicy0fs(ChoisePoint iX) {
	}
	public int getVisibilityPolicy(ChoisePoint iX) {
		if (visibilityPolicy != null) {
			return visibilityPolicy;
		} else {
			Term value= getBuiltInSlot_E_visibility_policy();
			return Utils3D.termToVisibilityPolicy(value,iX);
		}
	}
	//
	// get/set transparencySortingPolicy
	//
	public void setTransparencySortingPolicy1s(ChoisePoint iX, Term a1) {
		setTransparencySortingPolicy(Utils3D.termToTransparencySortingPolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setTransparencySortingPolicy(getTransparencySortingPolicy(iX));
		}
	}
	public void setTransparencySortingPolicy(int value) {
		transparencySortingPolicy= value;
	}
	public void getTransparencySortingPolicy0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= Utils3D.transparencySortingPolicyToTerm(getTransparencySortingPolicy(iX));
	}
	public void getTransparencySortingPolicy0fs(ChoisePoint iX) {
	}
	public int getTransparencySortingPolicy(ChoisePoint iX) {
		if (transparencySortingPolicy != null) {
			return transparencySortingPolicy;
		} else {
			Term value= getBuiltInSlot_E_transparency_sorting_policy();
			return Utils3D.termToTransparencySortingPolicy(value,iX);
		}
	}
	//
	// get/set minimumFrameCycleTime
	//
	public void setMinimumFrameCycleTime1s(ChoisePoint iX, Term a1) {
		setMinimumFrameCycleTime(WaitingInterval.termToWaitingInterval(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			changeMinimumFrameCycleTimeInNanos(getMinimumFrameCycleTime(iX),view);
		}
	}
	public void setMinimumFrameCycleTime(WaitingInterval value) {
		minimumFrameCycleTime= value;
	}
	public void getMinimumFrameCycleTime0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= getMinimumFrameCycleTime(iX).toTerm();
	}
	public void getMinimumFrameCycleTime0fs(ChoisePoint iX) {
	}
	public WaitingInterval getMinimumFrameCycleTime(ChoisePoint iX) {
		if (minimumFrameCycleTime != null) {
			return minimumFrameCycleTime;
		} else {
			Term value= getBuiltInSlot_E_minimum_frame_cycle_time();
			return WaitingInterval.termToWaitingInterval(value,iX);
		}
	}
	//
	// get/set fieldOfView
	//
	public void setFieldOfView1s(ChoisePoint iX, Term a1) {
		setFieldOfView(FieldOfView.termToFieldOfView(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			changeFieldOfView(getFieldOfView(iX),view);
		}
	}
	public void setFieldOfView(FieldOfView value) {
		fieldOfView= value;
	}
	public void getFieldOfView0ff(ChoisePoint iX, PrologVariable a1) {
		FieldOfView value= getFieldOfView(iX);
		a1.value= value.toTerm();
	}
	public void getFieldOfView0fs(ChoisePoint iX) {
	}
	public FieldOfView getFieldOfView(ChoisePoint iX) {
		if (fieldOfView != null) {
			return fieldOfView;
		} else {
			Term value= getBuiltInSlot_E_field_of_view();
			return FieldOfView.termToFieldOfView(value,iX);
		}
	}
	//
	// get/set frontClipDistance
	//
	public void setFrontClipDistance1s(ChoisePoint iX, Term a1) {
		setFrontClipDistance(ClipDistance.termToClipDistance(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			changeFrontClipDistance(getFrontClipDistance(iX),view);
		}
	}
	public void setFrontClipDistance(ClipDistance value) {
		frontClipDistance= value;
	}
	public void getFrontClipDistance0ff(ChoisePoint iX, PrologVariable a1) {
		ClipDistance value= getFrontClipDistance(iX);
		a1.value= value.toTerm();
	}
	public void getFrontClipDistance0fs(ChoisePoint iX) {
	}
	public ClipDistance getFrontClipDistance(ChoisePoint iX) {
		if (frontClipDistance != null) {
			return frontClipDistance;
		} else {
			Term value= getBuiltInSlot_E_front_clip_distance();
			return ClipDistance.termToClipDistance(value,iX);
		}
	}
	//
	// get/set backClipDistance
	//
	public void setBackClipDistance1s(ChoisePoint iX, Term a1) {
		setBackClipDistance(ClipDistance.termToClipDistance(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			changeBackClipDistance(getBackClipDistance(iX),view);
		}
	}
	public void setBackClipDistance(ClipDistance value) {
		backClipDistance= value;
	}
	public void getBackClipDistance0ff(ChoisePoint iX, PrologVariable a1) {
		ClipDistance value= getBackClipDistance(iX);
		a1.value= value.toTerm();
	}
	public void getBackClipDistance0fs(ChoisePoint iX) {
	}
	public ClipDistance getBackClipDistance(ChoisePoint iX) {
		if (backClipDistance != null) {
			return backClipDistance;
		} else {
			Term value= getBuiltInSlot_E_back_clip_distance();
			return ClipDistance.termToClipDistance(value,iX);
		}
	}
	//
	// get/set enableDepthBufferFreezing
	//
	public void setEnableDepthBufferFreezing1s(ChoisePoint iX, Term a1) {
		setEnableDepthBufferFreezing(YesNo.termYesNo2Boolean(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setDepthBufferFreezeTransparent(getEnableDepthBufferFreezing(iX));
		}
	}
	public void setEnableDepthBufferFreezing(boolean value) {
		enableDepthBufferFreezing= value;
	}
	public void getEnableDepthBufferFreezing0ff(ChoisePoint iX, PrologVariable a1) {
		boolean value= getEnableDepthBufferFreezing(iX);
		a1.value= YesNo.boolean2TermYesNo(value);
	}
	public void getEnableDepthBufferFreezing0fs(ChoisePoint iX) {
	}
	public boolean getEnableDepthBufferFreezing(ChoisePoint iX) {
		if (enableDepthBufferFreezing != null) {
			return enableDepthBufferFreezing;
		} else {
			Term value= getBuiltInSlot_E_enable_depth_buffer_freezing();
			return YesNo.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set enableLocalEyeLighting
	//
	public void setEnableLocalEyeLighting1s(ChoisePoint iX, Term a1) {
		setEnableLocalEyeLighting(YesNo.termYesNo2Boolean(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setLocalEyeLightingEnable(getEnableLocalEyeLighting(iX));
		}
	}
	public void setEnableLocalEyeLighting(boolean value) {
		enableLocalEyeLighting= value;
	}
	public void getEnableLocalEyeLighting0ff(ChoisePoint iX, PrologVariable a1) {
		boolean value= getEnableLocalEyeLighting(iX);
		a1.value= YesNo.boolean2TermYesNo(value);
	}
	public void getEnableLocalEyeLighting0fs(ChoisePoint iX) {
	}
	public boolean getEnableLocalEyeLighting(ChoisePoint iX) {
		if (enableLocalEyeLighting != null) {
			return enableLocalEyeLighting;
		} else {
			Term value= getBuiltInSlot_E_enable_local_eye_lighting();
			return YesNo.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set enableSceneAntialiasing
	//
	public void setEnableSceneAntialiasing1s(ChoisePoint iX, Term a1) {
		super.setEnableSceneAntialiasing1s(iX,a1);
		View view= getCurrentView();
		if (view != null) {
			view.setSceneAntialiasingEnable(getEnableSceneAntialiasing(iX));
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected View getCurrentView() {
		if (simpleUniverse != null) {
			Viewer viewer= simpleUniverse.getViewer();
			if (viewer != null) {
				View view= viewer.getView();
				if (view != null) {
					return view;
				}
			}
		};
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void closeFiles() {
		if (graphicWindow != null) {
			graphicWindow.safelyDispose();
		};
		super.closeFiles();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear0s(ChoisePoint iX) {
		// if (desktopDoesNotExist()) {
		//	return;
		// } else if (canvasSpaceDoesNotExist()) {
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
	// public void show0s(ChoisePoint iX) {
	//	createGraphicWindowIfNecessary(iX,true);
	// }
	public void show1ms(ChoisePoint iX, Term... args) {
		createGraphicWindowIfNecessary(iX,true);
		Term[] terms= (Term[])args;
		if (terms.length >= 1) {
			showCanvasAndSaveTree(terms[0],iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static GraphicsConfiguration getGraphicsConfiguration(Component c) {
		return refineGraphicsConfiguration(c.getGraphicsConfiguration());
	}
	public static GraphicsConfiguration refineGraphicsConfiguration(GraphicsConfiguration dialogGraphicsConfiguration) {
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
		if (canvasSpaceDoesNotExist()) {
			if (graphicWindow != null) {
				// GraphicsConfiguration config=
				//	SimpleUniverse.getPreferredConfiguration();
				// GraphicsConfiguration config=
				//	java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(gct3D);
				GraphicsConfiguration config= getGraphicsConfiguration(graphicWindow.getInternalFrame());
				canvasSpace= new ExtendedSpace3D(this,config);
				graphicWindow.safelyAdd(canvasSpace.getControl(),0);
			} else {
				return;
			}
		};
		// canvasSpace.safelySetBackground(getBackgroundColor(iX));
		changeBackgroundColor(iX);
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
			simpleUniverse= new SimpleUniverse((javax.media.j3d.Canvas3D)canvasSpace.getControl(),0);
			setViewAttributes(simpleUniverse,iX);
			BranchGroup scene= Utils3D.termToBranchGroupOrNodeList(a1,this,simpleUniverse,(javax.media.j3d.Canvas3D)canvasSpace.getControl(),iX);
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
			BranchGroup scene= Utils3D.termToBranchGroupOrNodeList(a1,this,simpleUniverse,(javax.media.j3d.Canvas3D)canvasSpace.getControl(),iX);
			scene.setCapability(BranchGroup.ALLOW_DETACH);
			spin.setChild(scene,0);
		};
		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		simpleUniverse.getViewingPlatform().setNominalViewingTransform();
		if (graphicWindow != null) {
			graphicWindow.safelyRevalidate();
		}
	}
	protected void setViewAttributes(SimpleUniverse simpleU, ChoisePoint iX) {
		//
		Viewer viewer= simpleUniverse.getViewer();
		View view= viewer.getView();
		//
		view.setProjectionPolicy(getProjectionPolicy(iX));
		view.setWindowResizePolicy(getWindowResizePolicy(iX));
		view.setWindowMovementPolicy(getWindowMovementPolicy(iX));
		view.setVisibilityPolicy(getVisibilityPolicy(iX));
		view.setTransparencySortingPolicy(getTransparencySortingPolicy(iX));
		changeMinimumFrameCycleTimeInNanos(getMinimumFrameCycleTime(iX),view);
		changeFieldOfView(getFieldOfView(iX),view);
		changeFrontClipDistance(getFrontClipDistance(iX),view);
		changeBackClipDistance(getBackClipDistance(iX),view);
		view.setSceneAntialiasingEnable(getEnableSceneAntialiasing(iX));
		view.setDepthBufferFreezeTransparent(getEnableDepthBufferFreezing(iX));
		view.setLocalEyeLightingEnable(getEnableLocalEyeLighting(iX));
	}
	protected void changeMinimumFrameCycleTimeInNanos(WaitingInterval minimumFrameCycle, View view) {
		long delayInMilliseconds= minimumFrameCycle.toMillisecondsLongOrDefault(longDefaultMinimumFrameCycleTimeInMilliseconds);
		if (delayInMilliseconds < 0) {
			delayInMilliseconds= longDefaultMinimumFrameCycleTimeInMilliseconds;
		};
		view.setMinimumFrameCycleTime(delayInMilliseconds);
	}
	protected void changeFieldOfView(FieldOfView fv, View view) {
		double value;
		try {
			value= fv.getValue();
		} catch (UseDefaultFieldOfView e) {
			value= defaultFieldOfView;
		};
		view.setFieldOfView(value);
	}
	protected void changeFrontClipDistance(ClipDistance fcd, View view) {
		double value;
		try {
			value= fcd.getValue();
		} catch (UseDefaultClipDistance e) {
			value= defaultFrontClipDistance;
		};
		view.setFrontClipDistance(value);
	}
	protected void changeBackClipDistance(ClipDistance bcd, View view) {
		double value;
		try {
			value= bcd.getValue();
		} catch (UseDefaultClipDistance e) {
			value= defaultBackClipDistance;
		};
		view.setBackClipDistance(value);
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
	///////////////////////////////////////////////////////////////
	//
	public void redraw0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized(this) {
			if (graphicWindow != null) {
				refreshAttributesOfInternalFrame(graphicWindow,iX);
				graphicWindow.safelyRestoreSize(staticContext);
				graphicWindow.safelyRepaint();
			} else if (canvasSpace != null) {
				changeBackgroundColor(iX);
				canvasSpace.safelyRepaint();
			}
		}
	}
	//
	// public void hide0s(ChoisePoint iX) {
	//	if (canvasSpaceDoesNotExist()) {
	//		return;
	//	} else {
	//		createGraphicWindowIfNecessary(iX,false);
	//		synchronized(this) {
	//			if (graphicWindow != null) {
	//				graphicWindow.safelySetVisible(false);
	//			}
	//		}
	//	}
	// }
	//
	// public void maximize0s(ChoisePoint iX) {
	//	createGraphicWindowIfNecessary(iX,true);
	//	synchronized(this) {
	//		if (graphicWindow != null) {
	//			DesktopUtils.safelyMaximize(graphicWindow);
	//		}
	//	}
	// }
	//
	// public void minimize0s(ChoisePoint iX) {
	//	createGraphicWindowIfNecessary(iX,true);
	//	synchronized(this) {
	//		if (graphicWindow != null) {
	//			DesktopUtils.safelyMinimize(graphicWindow);
	//		}
	//	}
	// }
	//
	// public void restore0s(ChoisePoint iX) {
	//	createGraphicWindowIfNecessary(iX,true);
	//	synchronized(this) {
	//		if (graphicWindow != null) {
	//			DesktopUtils.safelyRestore(graphicWindow);
	//		}
	//	}
	// }
	//
	// public void isVisible0s(ChoisePoint iX) throws Backtracking {
	//	if (canvasSpaceDoesNotExist()) {
	//		throw Backtracking.instance;
	//	} else {
	//		synchronized(this) {
	//			if (graphicWindow != null) {
	//				if (!DesktopUtils.safelyIsVisible(graphicWindow)) {
	//					throw Backtracking.instance;
	//				}
	//			} else {
	//				throw Backtracking.instance;
	//			}
	//		}
	//	}
	// }
	//
	// public void isHidden0s(ChoisePoint iX) throws Backtracking {
	//	if (canvasSpaceDoesNotExist()) {
	//	} else {
	//		synchronized(this) {
	//			if (graphicWindow != null) {
	//				if (!DesktopUtils.safelyIsHidden(graphicWindow)) {
	//					throw Backtracking.instance;
	//				}
	//			}
	//		}
	//	}
	// }
	//
	// public void isMaximized0s(ChoisePoint iX) throws Backtracking {
	//	if (canvasSpaceDoesNotExist()) {
	//		throw Backtracking.instance;
	//	} else {
	//		synchronized(this) {
	//			if (graphicWindow != null) {
	//				if (!DesktopUtils.safelyIsMaximized(graphicWindow)) {
	//					throw Backtracking.instance;
	//				}
	//			} else {
	//				throw Backtracking.instance;
	//			}
	//		}
	//	}
	// }
	//
	// public void isMinimized0s(ChoisePoint iX) throws Backtracking {
	//	if (canvasSpaceDoesNotExist()) {
	//		throw Backtracking.instance;
	//	} else {
	//		synchronized(this) {
	//			if (graphicWindow != null) {
	//				if(!DesktopUtils.safelyIsMinimized(graphicWindow)) {
	//					throw Backtracking.instance;
	//				}
	//			} else {
	//				throw Backtracking.instance;
	//			}
	//		}
	//	}
	// }
	//
	// public void isRestored0s(ChoisePoint iX) throws Backtracking {
	//	if (canvasSpaceDoesNotExist()) {
	//		throw Backtracking.instance;
	//	} else {
	//		synchronized(this) {
	//			if (graphicWindow != null) {
	//				if(!DesktopUtils.safelyIsRestored(graphicWindow)) {
	//					throw Backtracking.instance;
	//				}
	//			} else {
	//				throw Backtracking.instance;
	//			}
	//		}
	//	}
	// }
	//
	// public void changeBackgroundColor1s(ChoisePoint iX, Term backgroundColor) {
	// 	changeBackgroundColor(iX,backgroundColor);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public void setNode2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= NodeLabel.termToNodeLabel(a1,iX);
		synchronized(this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				if (canvasSpace != null) {
					BranchGroup newNode= PrincipalNode3D.termToBranchGroup(a2,this,simpleUniverse,(javax.media.j3d.Canvas3D)canvasSpace.getControl(),iX);
					Node parent= c.getParent();
					if (parent != null && parent instanceof Group) {
						Group group= (Group)parent;
						int index= group.indexOfChild(c.getNode());
						if (index >= 0) {
							group.setChild(newNode,index);
						}
					}
				}
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setTransform2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= NodeLabel.termToNodeLabel(a1,iX);
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
		NodeLabel nodeLabel= NodeLabel.termToNodeLabel(a1,iX);
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
		NodeLabel nodeLabel= NodeLabel.termToNodeLabel(a1,iX);
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
		NodeLabel nodeLabel= NodeLabel.termToNodeLabel(a1,iX);
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
		NodeLabel nodeLabel= NodeLabel.termToNodeLabel(a1,iX);
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
		NodeLabel nodeLabel= NodeLabel.termToNodeLabel(a1,iX);
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
	///////////////////////////////////////////////////////////////
	//
	// public void registerCanvas3D(ExtendedSpace3D s, ChoisePoint iX) {
	//	synchronized(this) {
	//		if (canvasSpaceDoesNotExist()) {
	//			canvasSpace= s;
	//			if (currentSceneTree != null) {
	//				showCanvas(currentSceneTree,iX);
	//			}
	//		}
	//	}
	// }
	//
	protected void initiateRegisteredCanvasSpace(CanvasSpace s, ChoisePoint iX) {
		if (currentSceneTree != null) {
			showCanvas(currentSceneTree,iX);
		}
	}
	//
	// public void release(boolean dialogIsModal, ChoisePoint modalChoisePoint) {
	//	synchronized(this) {
	//		if (simpleUniverse!=null && graphicWindow==null) {
	//			releasePickCanvas();
	//			simpleUniverse.removeAllLocales();
	//			simpleUniverse.cleanup();
	//			simpleUniverse= null;
	//			spin= null;
	//			canvasSpace= null;
	//		};
	//		localMemory.clear();
	//		inverseTable.clear();
	//	};
	//	long domainSignature= entry_s_Stop_0();
	//	callInternalProcedure(domainSignature,dialogIsModal,modalChoisePoint);
	// }
	//
	public void saveCanvasSpaceAttributes() {
	}
	public void clearCustomControlTables() {
		if (simpleUniverse!=null && graphicWindow==null) {
			releasePickCanvas();
			simpleUniverse.removeAllLocales();
			simpleUniverse.cleanup();
			simpleUniverse= null;
			spin= null;
		};
		localMemory.clear();
		inverseTable.clear();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void draw(boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		if (controlIsInitialized.compareAndSet(false,true)) {
			long domainSignature1= entry_s_Initialize_0();
			callInternalProcedure(domainSignature1,dialogIsModal,modalChoisePoint);
		};
		long domainSignature2= entry_s_Start_0();
		callInternalProcedure(domainSignature2,dialogIsModal,modalChoisePoint);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// protected boolean desktopDoesNotExist() {
	//	MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
	//	if (desktop==null) {
	//		return true;
	//	} else {
	//		return false;
	//	}
	// }
	// public boolean canvasSpaceDoesNotExist() {
	//	synchronized(this) {
	//		return (canvasSpace==null);
	//	}
	// }
	// protected void createGraphicWindowIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
	//	synchronized(this) {
	//		if (canvasSpaceDoesNotExist() && !controlIsInitialized.get()) {
	//			DesktopUtils.createPaneIfNecessary(staticContext);
	//			graphicWindow= createInternalFrameIfNecessary(iX,enableMovingWindowToFront);
	//		} else if (graphicWindow != null) {
	//			if (enableMovingWindowToFront) {
	//				graphicWindow.safelyMoveToFront();
	//			};
	//			graphicWindow.safelySetVisible(true);
	//		}
	//	}
	// }
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
					graphicWindow= formInternalFrame(iX);
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
			graphicWindow.safelyMoveToFront();
		};
		graphicWindow.safelySetVisible(true);
		return graphicWindow;
	}
	//
	protected InternalFrame3D formInternalFrame(ChoisePoint iX) {
		//
		String title= getTitle(iX).getValueOrDefaultText("");
		//
		InternalFrame3D internalFrame3D= new InternalFrame3D(title,staticContext);
		graphicWindow= internalFrame3D;
		Map<AbstractWorld,InternalFrame3D> innerWindows= StaticAttributes3D.retrieveInnerWindows(staticContext);
		innerWindows.put(this,internalFrame3D);
		//
		internalFrame3D.safelyAddComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(staticContext);
		desktop.safelyAdd(internalFrame3D.getInternalFrame());
		//
		canvasSpace= internalFrame3D.getCanvasSpace();
		refreshAttributesOfInternalFrame(internalFrame3D,null,iX);
		//
		return internalFrame3D;
	}
	// protected void refreshAttributesOfInternalFrame(InternalFrame3D container, ChoisePoint iX) {
	//	String title= getTitle(iX).getValueOrDefaultText("");
	//	refreshAttributesOfInternalFrame(container,title,iX);
	// }
	// protected void refreshAttributesOfInternalFrame(InternalFrame3D graphicWindow, String title, ChoisePoint iX) {
	//	//
	//	if (title != null) {
	//		graphicWindow.safelySetTitle(title);
	//	};
	//	//
	//	Term x= getBuiltInSlot_E_x();
	//	Term y= getBuiltInSlot_E_y();
	//	Term width= getBuiltInSlot_E_width().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	//	Term height= getBuiltInSlot_E_height().copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
	//	//
	//	graphicWindow.logicalWidth.set(ExtendedSize.termToExtendedSize(width,iX));
	//	graphicWindow.logicalHeight.set(ExtendedSize.termToExtendedSize(height,iX));
	//	graphicWindow.logicalX.set(ExtendedCoordinate.termToExtendedCoordinate(x,iX));
	//	graphicWindow.logicalY.set(ExtendedCoordinate.termToExtendedCoordinate(y,iX));
	//	//
	//	changeBackgroundColor(iX,getBuiltInSlot_E_background_color());
	// }
	//
	protected void refreshAttributesOfCanvasSpace(ChoisePoint iX) {
		changeBackgroundColor(iX);
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
	//public void rememberNode(NodeLabel label, Appearance node) {
	//	synchronized(this) {
	//		localMemory.put(label,new NodeContainer(node));
	//		inverseTable.put(node,label);
	//	}
	//}
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
	///////////////////////////////////////////////////////////////
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
	///////////////////////////////////////////////////////////////
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
