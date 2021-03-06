// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.gui.*;
import morozov.system.gui.space3d.*;
import morozov.system.gui.space3d.errors.*;
import morozov.system.gui.space3d.signals.*;
import morozov.system.signals.*;
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
import javax.vecmath.Vector3f;
import javax.vecmath.Color3f;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.j3d.utils.behaviors.vp.ViewPlatformBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Primitive;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Canvas3D extends BufferedImageController { // DataResourceConsumer {
	//
	protected HashMap<NodeLabel,NodeContainer> localMemory= new HashMap<>();
	protected HashMap<Node,NodeLabel> inverseTable= new HashMap<>();
	//
	protected HashSet<CustomizedPickCanvas> customizedPickCanvasList= new HashSet<>();
	//
	protected AtomicBoolean controlIsInitialized= new AtomicBoolean(false);
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
	protected Boolean enableCompatibilityMode= null;
	protected Transform3D transformVPCtoEC= null;
	protected FieldOfView fieldOfView= null;
	protected ClipDistance frontClipDistance= null;
	protected ClipDistance backClipDistance= null;
	protected Boolean enableDepthBufferFreezing= null;
	protected Boolean enableLocalEyeLighting= null;
	//
	protected AtomicReference<OrbitBehavior> orbitBehavior= new AtomicReference<>(null);
	protected AtomicReference<TransformGroup> homeTransform= new AtomicReference<>(null);
	protected AtomicReference<Transform3D> homeTransform3D= new AtomicReference<>(null);
	//
	protected long longDefaultMinimumFrameCycleTimeInMilliseconds= 0;
	protected double defaultFieldOfView= Math.PI/4.0;
	protected double defaultFrontClipDistance= 0.1;
	protected double defaultBackClipDistance= 10.0;
	//
	protected int redrawingPeriod= 310; // [ms]
	//
	protected static Transform3D identityMatrix= new Transform3D();
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
	abstract public Term getBuiltInSlot_E_projection_policy();
	abstract public Term getBuiltInSlot_E_window_resize_policy();
	abstract public Term getBuiltInSlot_E_window_movement_policy();
	abstract public Term getBuiltInSlot_E_visibility_policy();
	abstract public Term getBuiltInSlot_E_transparency_sorting_policy();
	abstract public Term getBuiltInSlot_E_minimum_frame_cycle_time();
	abstract public Term getBuiltInSlot_E_enable_compatibility_mode();
	//
	abstract public Term getBuiltInSlot_E_field_of_view();
	abstract public Term getBuiltInSlot_E_front_clip_distance();
	abstract public Term getBuiltInSlot_E_back_clip_distance();
	//
	abstract public Term getBuiltInSlot_E_enable_depth_buffer_freezing();
	abstract public Term getBuiltInSlot_E_enable_local_eye_lighting();
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
	@Override
	protected Color getDefaultBackgroundColor() {
		return Color.BLACK;
	}
	//
	@Override
	public void changeBackgroundColor(ColorAttribute eColor, ChoisePoint iX) {
		Color color;
		try {
			color= eColor.getValue();
		} catch (UseDefaultColor e) {
			color= getDefaultBackgroundColor();
		};
		synchronized (this) {
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
		setProjectionPolicy(Utils3D.argumentToProjectionPolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setProjectionPolicy(getProjectionPolicy(iX));
		}
	}
	public void setProjectionPolicy(int value) {
		projectionPolicy= value;
	}
	public void getProjectionPolicy0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(Utils3D.projectionPolicyToTerm(getProjectionPolicy(iX)));
	}
	public void getProjectionPolicy0fs(ChoisePoint iX) {
	}
	public int getProjectionPolicy(ChoisePoint iX) {
		if (projectionPolicy != null) {
			return projectionPolicy;
		} else {
			Term value= getBuiltInSlot_E_projection_policy();
			return Utils3D.argumentToProjectionPolicy(value,iX);
		}
	}
	//
	// get/set windowResizePolicy
	//
	public void setWindowResizePolicy1s(ChoisePoint iX, Term a1) {
		setWindowResizePolicy(Utils3D.argumentToWindowResizePolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setWindowResizePolicy(getWindowResizePolicy(iX));
		}
	}
	public void setWindowResizePolicy(int value) {
		windowResizePolicy= value;
	}
	public void getWindowResizePolicy0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(Utils3D.windowResizePolicyToTerm(getWindowResizePolicy(iX)));
	}
	public void getWindowResizePolicy0fs(ChoisePoint iX) {
	}
	public int getWindowResizePolicy(ChoisePoint iX) {
		if (windowResizePolicy != null) {
			return windowResizePolicy;
		} else {
			Term value= getBuiltInSlot_E_window_resize_policy();
			return Utils3D.argumentToWindowResizePolicy(value,iX);
		}
	}
	//
	// get/set windowMovementPolicy
	//
	public void setWindowMovementPolicy1s(ChoisePoint iX, Term a1) {
		setWindowMovementPolicy(Utils3D.argumentToWindowMovementPolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setWindowMovementPolicy(getWindowMovementPolicy(iX));
		}
	}
	public void setWindowMovementPolicy(int value) {
		windowMovementPolicy= value;
	}
	public void getWindowMovementPolicy0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(Utils3D.windowMovementPolicyToTerm(getWindowMovementPolicy(iX)));
	}
	public void getWindowMovementPolicy0fs(ChoisePoint iX) {
	}
	public int getWindowMovementPolicy(ChoisePoint iX) {
		if (windowMovementPolicy != null) {
			return windowMovementPolicy;
		} else {
			Term value= getBuiltInSlot_E_window_movement_policy();
			return Utils3D.argumentToWindowMovementPolicy(value,iX);
		}
	}
	//
	// get/set visibilityPolicy
	//
	public void setVisibilityPolicy1s(ChoisePoint iX, Term a1) {
		setVisibilityPolicy(Utils3D.argumentToVisibilityPolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setVisibilityPolicy(getVisibilityPolicy(iX));
		}
	}
	public void setVisibilityPolicy(int value) {
		visibilityPolicy= value;
	}
	public void getVisibilityPolicy0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(Utils3D.visibilityPolicyToTerm(getVisibilityPolicy(iX)));
	}
	public void getVisibilityPolicy0fs(ChoisePoint iX) {
	}
	public int getVisibilityPolicy(ChoisePoint iX) {
		if (visibilityPolicy != null) {
			return visibilityPolicy;
		} else {
			Term value= getBuiltInSlot_E_visibility_policy();
			return Utils3D.argumentToVisibilityPolicy(value,iX);
		}
	}
	//
	// get/set transparencySortingPolicy
	//
	public void setTransparencySortingPolicy1s(ChoisePoint iX, Term a1) {
		setTransparencySortingPolicy(Utils3D.argumentToTransparencySortingPolicy(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setTransparencySortingPolicy(getTransparencySortingPolicy(iX));
		}
	}
	public void setTransparencySortingPolicy(int value) {
		transparencySortingPolicy= value;
	}
	public void getTransparencySortingPolicy0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(Utils3D.transparencySortingPolicyToTerm(getTransparencySortingPolicy(iX)));
	}
	public void getTransparencySortingPolicy0fs(ChoisePoint iX) {
	}
	public int getTransparencySortingPolicy(ChoisePoint iX) {
		if (transparencySortingPolicy != null) {
			return transparencySortingPolicy;
		} else {
			Term value= getBuiltInSlot_E_transparency_sorting_policy();
			return Utils3D.argumentToTransparencySortingPolicy(value,iX);
		}
	}
	//
	// get/set minimumFrameCycleTime
	//
	public void setMinimumFrameCycleTime1s(ChoisePoint iX, Term a1) {
		setMinimumFrameCycleTime(WaitingIntervalConverters.argumentToWaitingInterval(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			changeMinimumFrameCycleTimeInNanos(getMinimumFrameCycleTime(iX),view);
		}
	}
	public void setMinimumFrameCycleTime(WaitingInterval value) {
		minimumFrameCycleTime= value;
	}
	public void getMinimumFrameCycleTime0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(WaitingIntervalConverters.toTerm(getMinimumFrameCycleTime(iX)));
	}
	public void getMinimumFrameCycleTime0fs(ChoisePoint iX) {
	}
	public WaitingInterval getMinimumFrameCycleTime(ChoisePoint iX) {
		if (minimumFrameCycleTime != null) {
			return minimumFrameCycleTime;
		} else {
			Term value= getBuiltInSlot_E_minimum_frame_cycle_time();
			return WaitingIntervalConverters.argumentToWaitingInterval(value,iX);
		}
	}
	//
	// get/set enableCompatibilityMode
	//
	public void setEnableCompatibilityMode1s(ChoisePoint iX, Term a1) {
		setEnableCompatibilityMode(YesNoConverters.termYesNo2Boolean(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setCompatibilityModeEnable(getEnableCompatibilityMode(iX));
		}
	}
	public void setEnableCompatibilityMode(boolean value) {
		enableCompatibilityMode= value;
	}
	public void getEnableCompatibilityMode0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getEnableCompatibilityMode(iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void getEnableCompatibilityMode0fs(ChoisePoint iX) {
	}
	public boolean getEnableCompatibilityMode(ChoisePoint iX) {
		if (enableCompatibilityMode != null) {
			return enableCompatibilityMode;
		} else {
			Term value= getBuiltInSlot_E_enable_compatibility_mode();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set fieldOfView
	//
	public void setFieldOfView1s(ChoisePoint iX, Term a1) {
		setFieldOfView(FieldOfView.argumentToFieldOfView(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			changeFieldOfView(getFieldOfView(iX),view);
		}
	}
	public void setFieldOfView(FieldOfView value) {
		fieldOfView= value;
	}
	public void getFieldOfView0ff(ChoisePoint iX, PrologVariable result) {
		FieldOfView value= getFieldOfView(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getFieldOfView0fs(ChoisePoint iX) {
	}
	public FieldOfView getFieldOfView(ChoisePoint iX) {
		if (fieldOfView != null) {
			return fieldOfView;
		} else {
			Term value= getBuiltInSlot_E_field_of_view();
			return FieldOfView.argumentToFieldOfView(value,iX);
		}
	}
	//
	// get/set frontClipDistance
	//
	public void setFrontClipDistance1s(ChoisePoint iX, Term a1) {
		setFrontClipDistance(ClipDistance.argumentToClipDistance(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			changeFrontClipDistance(getFrontClipDistance(iX),view);
		}
	}
	public void setFrontClipDistance(ClipDistance value) {
		frontClipDistance= value;
	}
	public void getFrontClipDistance0ff(ChoisePoint iX, PrologVariable result) {
		ClipDistance value= getFrontClipDistance(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getFrontClipDistance0fs(ChoisePoint iX) {
	}
	public ClipDistance getFrontClipDistance(ChoisePoint iX) {
		if (frontClipDistance != null) {
			return frontClipDistance;
		} else {
			Term value= getBuiltInSlot_E_front_clip_distance();
			return ClipDistance.argumentToClipDistance(value,iX);
		}
	}
	//
	// get/set backClipDistance
	//
	public void setBackClipDistance1s(ChoisePoint iX, Term a1) {
		setBackClipDistance(ClipDistance.argumentToClipDistance(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			changeBackClipDistance(getBackClipDistance(iX),view);
		}
	}
	public void setBackClipDistance(ClipDistance value) {
		backClipDistance= value;
	}
	public void getBackClipDistance0ff(ChoisePoint iX, PrologVariable result) {
		ClipDistance value= getBackClipDistance(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getBackClipDistance0fs(ChoisePoint iX) {
	}
	public ClipDistance getBackClipDistance(ChoisePoint iX) {
		if (backClipDistance != null) {
			return backClipDistance;
		} else {
			Term value= getBuiltInSlot_E_back_clip_distance();
			return ClipDistance.argumentToClipDistance(value,iX);
		}
	}
	//
	// get/set enableDepthBufferFreezing
	//
	public void setEnableDepthBufferFreezing1s(ChoisePoint iX, Term a1) {
		setEnableDepthBufferFreezing(YesNoConverters.termYesNo2Boolean(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setDepthBufferFreezeTransparent(getEnableDepthBufferFreezing(iX));
		}
	}
	public void setEnableDepthBufferFreezing(boolean value) {
		enableDepthBufferFreezing= value;
	}
	public void getEnableDepthBufferFreezing0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getEnableDepthBufferFreezing(iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void getEnableDepthBufferFreezing0fs(ChoisePoint iX) {
	}
	public boolean getEnableDepthBufferFreezing(ChoisePoint iX) {
		if (enableDepthBufferFreezing != null) {
			return enableDepthBufferFreezing;
		} else {
			Term value= getBuiltInSlot_E_enable_depth_buffer_freezing();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set enableLocalEyeLighting
	//
	public void setEnableLocalEyeLighting1s(ChoisePoint iX, Term a1) {
		setEnableLocalEyeLighting(YesNoConverters.termYesNo2Boolean(a1,iX));
		View view= getCurrentView();
		if (view != null) {
			view.setLocalEyeLightingEnable(getEnableLocalEyeLighting(iX));
		}
	}
	public void setEnableLocalEyeLighting(boolean value) {
		enableLocalEyeLighting= value;
	}
	public void getEnableLocalEyeLighting0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getEnableLocalEyeLighting(iX);
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(value));
	}
	public void getEnableLocalEyeLighting0fs(ChoisePoint iX) {
	}
	public boolean getEnableLocalEyeLighting(ChoisePoint iX) {
		if (enableLocalEyeLighting != null) {
			return enableLocalEyeLighting;
		} else {
			Term value= getBuiltInSlot_E_enable_local_eye_lighting();
			return YesNoConverters.termYesNo2Boolean(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set enableSceneAntialiasing
	//
	@Override
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
	// get/set transformVPCtoEC
	//
	public void setVPCtoEC1s(ChoisePoint iX, Term a1) {
		Transform3D transform3D= AuxiliaryNode3D.argumentToTransform3D(a1,iX);
		setVPCtoEC(transform3D);
		View view= getCurrentView();
		if (view != null) {
			view.setVpcToEc(transform3D);
		}
	}
	public void setVPCtoEC(Transform3D value) {
		transformVPCtoEC= value;
	}
	//
	public void resetVPCtoEC0s(ChoisePoint iX) {
		resetVPCtoEC();
	}
	public void resetVPCtoEC() {
		transformVPCtoEC= null;
		View view= getCurrentView();
		if (view != null) {
			view.setVpcToEc(identityMatrix);
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
	@Override
	public void releaseSystemResources() {
		if (graphicWindow != null) {
			graphicWindow.safelyDispose();
		};
		super.releaseSystemResources();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void clear0s(ChoisePoint iX) {
		show1s(iX,PrologEmptyList.instance);
	}
	//
	public void show1s(ChoisePoint iX, Term a1) {
		createGraphicWindowIfNecessary(iX,true);
		showCanvasAndSaveTree(a1,iX);
	}
	@Override
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
		synchronized (this) {
			showCanvas(value,iX);
			currentSceneTree= value;
		}
	}
	protected void showCanvas(Term a1, ChoisePoint iX) {
		if (canvasSpaceDoesNotExist()) {
			if (graphicWindow != null) {
				GraphicsConfiguration config= getGraphicsConfiguration(graphicWindow.getInternalFrame());
				canvasSpace= new ExtendedSpace3D(null,this,config);
				stopRenderer();
				graphicWindow.safelyAdd(canvasSpace.getControl(),0);
				startRenderer();
			} else {
				return;
			}
		};
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
			ExtendedSpace3D space3D= (ExtendedSpace3D)canvasSpace;
			javax.media.j3d.Canvas3D control= (javax.media.j3d.Canvas3D)space3D.getControl();
			simpleUniverse= new SimpleUniverse(control,0);
			setViewAttributes(simpleUniverse,iX);
			BranchGroup scene= Utils3D.argumentToBranchGroupOrNodeList(a1,this,simpleUniverse,control,iX);
			scene.setCapability(BranchGroup.ALLOW_DETACH);
			scene.compile(); // 2019-04-07
			spin.setChild(scene,0);
			BranchGroup branchGroup= new BranchGroup();
			branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
			branchGroup.addChild(spin);
			branchGroup.compile(); // 2019-04-07
			simpleUniverse.addBranchGraph(branchGroup);
			space3D.setUniverse(simpleUniverse);
			SimpleUniverse.addRenderingErrorListener(new OffScreenCanvas3DRenderingErrorListener());
		} else {
			releasePickCanvas();
			setViewAttributes(simpleUniverse,iX);
			BranchGroup scene= Utils3D.argumentToBranchGroupOrNodeList(a1,this,simpleUniverse,(javax.media.j3d.Canvas3D)canvasSpace.getControl(),iX);
			scene.setCapability(BranchGroup.ALLOW_DETACH);
			spin.setChild(scene,0);
		};
		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed:
		simpleUniverse.getViewingPlatform().setNominalViewingTransform();
		if (graphicWindow != null) {
			graphicWindow.safelyRevalidate();
		};
		saveHomeTransform();
	}
	protected void setViewAttributes(SimpleUniverse su, ChoisePoint iX) {
		//
		Viewer viewer= su.getViewer();
		View view= viewer.getView();
		//
		view.setProjectionPolicy(getProjectionPolicy(iX));
		view.setWindowResizePolicy(getWindowResizePolicy(iX));
		view.setWindowMovementPolicy(getWindowMovementPolicy(iX));
		view.setVisibilityPolicy(getVisibilityPolicy(iX));
		view.setTransparencySortingPolicy(getTransparencySortingPolicy(iX));
		changeMinimumFrameCycleTimeInNanos(getMinimumFrameCycleTime(iX),view);
		view.setCompatibilityModeEnable(getEnableCompatibilityMode(iX));
		changeVPCtoEC();
		changeFieldOfView(getFieldOfView(iX),view);
		changeFrontClipDistance(getFrontClipDistance(iX),view);
		changeBackClipDistance(getBackClipDistance(iX),view);
		view.setSceneAntialiasingEnable(getEnableSceneAntialiasing(iX));
		view.setDepthBufferFreezeTransparent(getEnableDepthBufferFreezing(iX));
		view.setLocalEyeLightingEnable(getEnableLocalEyeLighting(iX));
	}
	public void setViewPlatformBehavior(OrbitBehavior behaviour) {
		orbitBehavior.set(behaviour);
		ViewingPlatform viewingPlatform= simpleUniverse.getViewingPlatform();
		viewingPlatform.setViewPlatformBehavior(behaviour);
	}
	public void saveHomeTransform() {
		ViewingPlatform viewingPlatform= simpleUniverse.getViewingPlatform();
		TransformGroup home= viewingPlatform.getViewPlatformTransform();
		homeTransform.set(home);
		Transform3D home3D= new Transform3D();
		home.getTransform(home3D);
		homeTransform3D.set(home3D);
	}
	protected void changeMinimumFrameCycleTimeInNanos(WaitingInterval minimumFrameCycle, View view) {
		long delayInMilliseconds= minimumFrameCycle.toMillisecondsLongOrDefault(longDefaultMinimumFrameCycleTimeInMilliseconds);
		if (delayInMilliseconds < 0) {
			delayInMilliseconds= longDefaultMinimumFrameCycleTimeInMilliseconds;
		};
		view.setMinimumFrameCycleTime(delayInMilliseconds);
	}
	protected void changeVPCtoEC() {
		if (transformVPCtoEC != null) {
			View view= getCurrentView();
			if (view != null) {
				view.setVpcToEc(transformVPCtoEC);
			}
		}
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
		//
		private TransformGroup targetTG;
		private Transform3D rotation = new Transform3D();
		private double maximumAngle = (0.1*(StrictMath.PI)/360) / 10000;
		private double minimumAngle = -(0.1*(StrictMath.PI)/360) / 10000;
		private WakeupOnElapsedTime wakeup = new WakeupOnElapsedTime(redrawingPeriod);
		private boolean direction = true;
		//
		SpecialBehavior(TransformGroup tg) {
			targetTG= tg;
		}
		//
		@Override
		public void initialize() {
			wakeupOn(wakeup);
		}
		@Override
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
	@Override
	public void redraw0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		synchronized (this) {
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
	///////////////////////////////////////////////////////////////
	//
	public void setNode2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= NodeLabel.argumentToNodeLabel(a1,iX);
		synchronized (this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				if (canvasSpace != null) {
					BranchGroup newNode= PrincipalNode3D.argumentToBranchGroup(a2,this,simpleUniverse,(javax.media.j3d.Canvas3D)canvasSpace.getControl(),iX);
					newNode.compile(); // 2019-04-07
					clearGeometryCache();
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
		NodeLabel nodeLabel= NodeLabel.argumentToNodeLabel(a1,iX);
		synchronized (this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				Transform3D transform= PrincipalNode3D.argumentToTransform3D(a2,iX);
				clearGeometryCache();
				c.setTransform(transform);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setTranslation2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= NodeLabel.argumentToNodeLabel(a1,iX);
		synchronized (this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				Vector3d vector= Tools3D.term2Vector3(a2,iX);
				clearGeometryCache();
				c.setTranslation(vector);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setAppearance2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= NodeLabel.argumentToNodeLabel(a1,iX);
		synchronized (this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				Appearance appearance= AuxiliaryNode3D.argumentToAppearance(a2,this,iX);
				clearGeometryCache();
				c.setAppearance(appearance);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setColoringAttributes2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= NodeLabel.argumentToNodeLabel(a1,iX);
		synchronized (this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				ColoringAttributes coloringAttributes= AuxiliaryNode3D.argumentToColoringAttributes(a2,iX);
				clearGeometryCache();
				c.setColoringAttributes(coloringAttributes);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setFont3D2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= NodeLabel.argumentToNodeLabel(a1,iX);
		synchronized (this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				Font3D font= AuxiliaryNode3D.argumentToFont3D(a2,iX);
				clearGeometryCache();
				c.setFont3D(font);
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	public void setString2s(ChoisePoint iX, Term a1, Term a2) {
		NodeLabel nodeLabel= NodeLabel.argumentToNodeLabel(a1,iX);
		synchronized (this) {
			NodeContainer c= localMemory.get(nodeLabel);
			if (c != null) {
				clearGeometryCache();
				c.setString(a2.toString(iX));
			} else {
				throw new WrongArgumentIsUnknownNodeLabel(a1);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void startRenderer() {
		if (simpleUniverse != null) {
			javax.media.j3d.Canvas3D canvas= simpleUniverse.getCanvas();
			if (canvas != null) {
				canvas.startRenderer();
			}
		}
	}
	//
	protected void stopRenderer() {
		if (simpleUniverse != null) {
			javax.media.j3d.Canvas3D canvas= simpleUniverse.getCanvas();
			if (canvas != null) {
				canvas.stopRenderer();
			}
		}
	}
	//
	protected void clearGeometryCache() {
		Primitive.clearGeometryCache();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void initiateRegisteredCanvasSpace(CanvasSpace s, ChoisePoint iX) {
		if (currentSceneTree != null) {
			showCanvas(currentSceneTree,iX);
		}
	}
	//
	@Override
	public void saveCanvasSpaceAttributes() {
	}
	@Override
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
	@Override
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
	@Override
	protected InternalFrame3D createInternalFrameIfNecessary(ChoisePoint iX, boolean enableMovingWindowToFront) {
		Map<AbstractWorld,InternalFrame3D> innerWindows= StaticAttributes3D.retrieveInnerWindows(staticContext);
		InternalFrame3D currentGraphicWindow= innerWindows.get(this);
		boolean restoreWindow= false;
		boolean moveWindowToFront= false;
		if (currentGraphicWindow==null) {
			synchronized (this) {
				currentGraphicWindow= innerWindows.get(this);
				if (currentGraphicWindow==null) {
					currentGraphicWindow= formInternalFrame(iX);
					restoreWindow= true;
				}
			}
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
	//
	protected InternalFrame3D formInternalFrame(ChoisePoint iX) {
		//
		String currentTitle= getTitle(iX).getValueOrDefaultText("");
		//
		InternalFrame3D internalFrame3D= new InternalFrame3D(currentTitle,staticContext);
		graphicWindow= internalFrame3D;
		Map<AbstractWorld,InternalFrame3D> innerWindows= StaticAttributes3D.retrieveInnerWindows(staticContext);
		innerWindows.put(this,internalFrame3D);
		//
		internalFrame3D.safelyAddComponentListener(this);
		//
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveMainDesktopPane(staticContext);
		stopRenderer();
		desktop.safelyAdd(internalFrame3D.getInternalFrame());
		startRenderer();
		//
		canvasSpace= internalFrame3D.getCanvasSpace();
		refreshAttributesOfInternalFrame(internalFrame3D,null,iX);
		//
		return internalFrame3D;
	}
	//
	@Override
	protected void refreshAttributesOfCanvasSpace(ChoisePoint iX) {
		changeBackgroundColor(iX);
	}
	//
	public void rememberNode(NodeLabel label, TransformGroup node) {
		synchronized (this) {
			NodeContainer previousValue= localMemory.put(label,new NodeContainer(node));
			if (previousValue != null) {
				Node previousNode= previousValue.getNode();
				inverseTable.remove(previousNode);
			};
			inverseTable.put(node,label);
		}
	}
	public void rememberNode(NodeLabel label, Group node) {
		synchronized (this) {
			NodeContainer previousValue= localMemory.put(label,new NodeContainer(node));
			if (previousValue != null) {
				Node previousNode= previousValue.getNode();
				inverseTable.remove(previousNode);
			};
			inverseTable.put(node,label);
		}
	}
	public void rememberNode(NodeLabel label, Node node) {
		synchronized (this) {
			NodeContainer previousValue= localMemory.put(label,new NodeContainer(node));
			if (previousValue != null) {
				Node previousNode= previousValue.getNode();
				inverseTable.remove(previousNode);
			};
			inverseTable.put(node,label);
		}
	}
	public Shape3D retrieveShape3D(NodeLabel label) {
		synchronized (this) {
			NodeContainer c= localMemory.get(label);
			if (c != null) {
				return c.retrieveShape3D(label);
			} else {
				throw new UndefinedNodeLabel(label);
			}
		}
	}
	public PointLight retrievePointLight(NodeLabel label) {
		synchronized (this) {
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
	public void attachOffScreenCanvas0s(ChoisePoint iX) {
		createGraphicWindowIfNecessary(iX,false);
		((ExtendedSpace3D)canvasSpace).attachOffScreenCanvas();
	}
	public void detachOffScreenCanvas0s(ChoisePoint iX) {
		if (canvasSpace != null) {
			((ExtendedSpace3D)canvasSpace).detachOffScreenCanvas();
		}
	}
	//
	public void goHome0s(ChoisePoint iX) {
		goHome(0.0f,0.0f,0.0f);
	}
	public void goHome3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		NumericalValue numericalValueX= NumericalValueConverters.argumentToNumericalValue(a1,iX);
		NumericalValue numericalValueY= NumericalValueConverters.argumentToNumericalValue(a2,iX);
		NumericalValue numericalValueZ= NumericalValueConverters.argumentToNumericalValue(a3,iX);
		goHome(	(float)numericalValueX.getDoubleValue(),
			(float)numericalValueY.getDoubleValue(),
			(float)numericalValueZ.getDoubleValue());
	}
	//
	protected void goHome(float x, float y, float z) {
		if (simpleUniverse != null) {
  			ViewingPlatform platform= simpleUniverse.getViewingPlatform();
			ViewPlatformBehavior behaviour= platform.getViewPlatformBehavior();
			TransformGroup initialTransform= homeTransform.get();
			Transform3D initialTransform3D= homeTransform3D.get();
			if (initialTransform != null && initialTransform3D != null) {
				Transform3D newTransform3D= new Transform3D(initialTransform3D);
				Vector3f translation= new Vector3f();
				newTransform3D.get(translation);
				translation.add(new Vector3f(x,y,z));
				newTransform3D.setTranslation(translation);
				initialTransform.setTransform(newTransform3D);
				behaviour.goHome();
			}
		}
	}
	//
	public void getImage1s(ChoisePoint iX, Term a1) {
		createGraphicWindowIfNecessary(iX,false);
		java.awt.image.BufferedImage bufferedImage= ((ExtendedSpace3D)canvasSpace).createBufferedImage();
		modifyImage(a1,bufferedImage,iX);
	}
	public void getImage3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		int currentWidth= GeneralConverters.argumentToSmallInteger(a2,iX);
		int currentHeight= GeneralConverters.argumentToSmallInteger(a3,iX);
		createGraphicWindowIfNecessary(iX,false);
		java.awt.image.BufferedImage bufferedImage= ((ExtendedSpace3D)canvasSpace).createBufferedImage(currentWidth,currentHeight);
		modifyImage(a1,bufferedImage,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void componentHidden(ComponentEvent e) {
		DesktopUtils.selectNextInternalFrame(staticContext);
	}
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	@Override
	public void componentResized(ComponentEvent e) {
	}
	@Override
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
