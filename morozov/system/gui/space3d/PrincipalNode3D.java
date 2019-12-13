// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.converters.errors.*;
import morozov.system.errors.*;
import morozov.system.gui.*;
import morozov.system.gui.space3d.errors.*;
import morozov.system.gui.space3d.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Font;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.OrientedShape3D;
import com.sun.j3d.utils.geometry.Text2D;
import javax.media.j3d.Geometry;
import javax.media.j3d.Background;
import javax.media.j3d.Light;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.PointLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.ModelClip;
import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.ImageComponent2D;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.picking.PickTool;

public class PrincipalNode3D extends AuxiliaryNode3D {
	//
	public static Node argumentToNode(Term value, BranchGroup branchGroup, Group group, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, TransformGroup target, ChoisePoint iX) {
		try { // TransformGroup
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TransformGroup,1,iX);
			return attributesToTransformGroup(arguments[0],targetWorld,u,space3D,iX);
		} catch (Backtracking b1) {
		try { // ColorCube
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ColorCube,1,iX);
			return attributesToColorCube(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b2) {
		try { // RotationInterpolator
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_RotationInterpolator,1,iX);
			return attributesToRotationInterpolator(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b3) {
		try { // Shape3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Shape3D,1,iX);
			return attributesToShape3D(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b4) {
		try { // OrientedShape3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Billboard,1,iX);
			return attributesToBillboard(arguments[0],group,targetWorld,space3D,iX);
		} catch (Backtracking b5) {
		try { // Text2D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Text2D,1,iX);
			return attributesToText2D(arguments[0],group,targetWorld,space3D,iX);
		} catch (Backtracking b6) {
		try { // Background
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Background,1,iX);
			return attributesToBackground(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b7) {
		try { // AmbientLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_AmbientLight,1,iX);
			return attributesToAmbientLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b8) {
		try { // DirectionalLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_DirectionalLight,1,iX);
			return attributesToDirectionalLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b9) {
		try { // PointLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PointLight,1,iX);
			return attributesToPointLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b10) {
		try { // Box
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Box,1,iX);
			return attributesToBox(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b11) {
		try { // Cone
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Cone,1,iX);
			return attributesToCone(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b12) {
		try { // Cylinder
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Cylinder,1,iX);
			return attributesToCylinder(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b13) {
		try { // Sphere
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Sphere,1,iX);
			return attributesToSphere(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b14) {
		try { // MouseRotate
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseRotate,1,iX);
			return attributesToMouseRotate(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b15) {
		try { // MouseTranslate
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseTranslate,1,iX);
			return attributesToMouseTranslate(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b16) {
		try { // MouseWheelZoom
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseWheelZoom,1,iX);
			return attributesToMouseWheelZoom(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b17) {
		try { // MouseZoom
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseZoom,1,iX);
			return attributesToMouseZoom(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b18) {
		try { // OrbitBehavior
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_OrbitBehavior,1,iX);
			attributesToOrbitBehavior(arguments[0],group,targetWorld,u,space3D,iX);
			return null;
		} catch (Backtracking b19) {
		try { // ModelClip
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ModelClip,1,iX);
			return attributesToModelClip(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b20) {
		try { // CustomizedBehavior
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_CustomizedBehavior,1,iX);
			return attributesToCustomizedBehavior(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b21) {
		try { // Group
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Group,1,iX);
			return attributesToGroup(arguments[0],targetWorld,u,space3D,iX);
		} catch (Backtracking b22) {
		try { // BranchGroup
			return termToBranchGroup(value,targetWorld,u,space3D,iX);
		} catch (TermIsNotBranchGroup e23) {
		try { // MovingShadow
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MovingShadow,1,iX);
			return attributesToMovingShadow(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b24) {
		try { // PickCanvas
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PickCanvas,1,iX);
			CustomizedPickCanvas pc= attributesToPickCanvas(arguments[0],branchGroup,targetWorld,space3D,iX);
			targetWorld.registerPickCanvas(pc);
			pc.activateTimer();
			return null;
		} catch (Backtracking b25) {
			throw new WrongArgumentIsUnknownNode(value);
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
	}
	//
	public static void argumentToListOfNodes(BranchGroup branchGroup, Group group, Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, TransformGroup target, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				Node node= argumentToNode(nextHead,branchGroup,group,targetWorld,u,space3D,target,iX);
				if (node != null) {
					group.addChild(node);
				};
				if (debugNodes()) {
					System.out.printf("PrincipalNode3D::argumentToListOfNodes:%s.addChild(%s);\n",group,node);
				}
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			throw new WrongArgumentIsNotAList(currentTail);
		}
	}
	//
	public static void argumentToCollisionDetectorList(Term value, Node armingNode, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				Node node= argumentToCollisionDetector(nextHead,armingNode,group,targetWorld,iX);
				if (node != null) {
					group.addChild(node);
				};
				if (debugNodes()) {
					System.out.printf("PrincipalNode3D::argumentToListOfNodes:%s.addChild(%s);\n",group,node);
				}
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			throw new WrongArgumentIsNotAList(currentTail);
		}
	}
	//
	public static Group argumentToGroup(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		try { // Group
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Group,1,iX);
			return attributesToGroup(arguments[0],targetWorld,u,space3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAGroup(value);
		}
	}
	public static Group attributesToGroup(Term attributes, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Group node= new Group();
			Set<Long> nameList= setPositiveMap.keySet();
			extractGroupAttributes(node,nameList,setPositiveMap,targetWorld,u,space3D,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	protected static void extractGroupAttributes(Group node, Set<Long> nameList, HashMap<Long,Term> setPositiveMap, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_allowChildrenExtend) {
				boolean allowChildrenExtend= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowChildrenExtend) {
					node.setCapability(Group.ALLOW_CHILDREN_EXTEND);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowChildrenRead) {
				boolean allowChildrenRead= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowChildrenRead) {
					node.setCapability(Group.ALLOW_CHILDREN_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowChildrenWrite) {
				boolean allowChildrenWrite= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowChildrenWrite) {
					node.setCapability(Group.ALLOW_CHILDREN_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowCollisionBoundsRead) {
				boolean allowCollisionBoundsRead= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowCollisionBoundsRead) {
					node.setCapability(Group.ALLOW_COLLISION_BOUNDS_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowCollisionBoundsWrite) {
				boolean allowCollisionBoundsWrite= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowCollisionBoundsWrite) {
					node.setCapability(Group.ALLOW_COLLISION_BOUNDS_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_branches) {
				argumentToListOfNodes(null,node,pairValue,targetWorld,u,space3D,null,iX);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_label) {
				NodeLabel label= NodeLabel.argumentToNodeLabel(pairValue,iX);
				targetWorld.rememberNode(label,node);
				iterator.remove();
			}
		};
		extractNodeAttributes(node,node,nameList,setPositiveMap,targetWorld,iX);
	}
	//
	protected static void extractNodeAttributes(Node node, Group group, Set<Long> nameList, HashMap<Long,Term> setPositiveMap, Canvas3D targetWorld, ChoisePoint iX) {
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_label) {
				NodeLabel label= NodeLabel.argumentToNodeLabel(pairValue,iX);
				targetWorld.rememberNode(label,node);
			} else if (pairName==SymbolCodes.symbolCode_E_collisionDetectors) {
				argumentToCollisionDetectorList(pairValue,node,group,targetWorld,iX);
			} else {
				throw new WrongArgumentIsUnknownNodeAttribute(key);
			}
		}
	}
	//
	public static BranchGroup argumentToBranchGroup(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		try {
			return termToBranchGroup(value,targetWorld,u,space3D,iX);
		} catch (TermIsNotBranchGroup e) {
			throw new WrongArgumentIsNotBranchGroup(value);
		}
	}
	public static BranchGroup termToBranchGroup(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) throws TermIsNotBranchGroup {
		value= value.dereferenceValue(iX);
		if (value instanceof BufferedScene) {
			BufferedScene scene= (BufferedScene)value;
			return scene.getBranchGroup();
		} else {
			try { // BranchGroup
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_BranchGroup,1,iX);
				return attributesToBranchGroup(arguments[0],targetWorld,u,space3D,iX);
			} catch (Backtracking b) {
				throw TermIsNotBranchGroup.instance;
			}
		}
	}
	public static BranchGroup attributesToBranchGroup(Term attributes, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			BranchGroup node= new BranchGroup();
			boolean compileBranchGroup= false;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_allowDetach) {
					boolean allowDetach= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					if (allowDetach) {
						node.setCapability(BranchGroup.ALLOW_DETACH);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_compile) {
					compileBranchGroup= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_branches) {
					argumentToListOfNodes(node,node,pairValue,targetWorld,u,space3D,null,iX);
					iterator.remove();
				}
			};
			extractGroupAttributes(node,nameList,setPositiveMap,targetWorld,u,space3D,iX);
			if (compileBranchGroup) {
				node.compile();
			};
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static TransformGroup argumentToTransformGroup(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		try { // TransformGroup
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TransformGroup,1,iX);
			return attributesToTransformGroup(arguments[0],targetWorld,u,space3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotTransformGroup(value);
		}
	}
	public static TransformGroup attributesToTransformGroup(Term attributes, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			TransformGroup node= new TransformGroup();
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_allowTransformRead) {
					boolean allowTransformRead= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					if (allowTransformRead) {
						node.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowTransformWrite) {
					boolean allowTransformWrite= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					if (allowTransformWrite) {
						node.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_branches) {
					argumentToListOfNodes(null,node,pairValue,targetWorld,u,space3D,node,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_transform3D) {
					Transform3D transform3D= argumentToTransform3D(pairValue,iX);
					node.setTransform(transform3D);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_label) {
					NodeLabel label= NodeLabel.argumentToNodeLabel(pairValue,iX);
					targetWorld.rememberNode(label,node);
					iterator.remove();
				}
			};
			extractGroupAttributes(node,nameList,setPositiveMap,targetWorld,u,space3D,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static ColorCube argumentToColorCube(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // ColorCube
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ColorCube,1,iX);
			return attributesToColorCube(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotColorCube(value);
		}
	}
	public static ColorCube attributesToColorCube(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Double scale= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_scale) {
					scale= GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				}
			};
			ColorCube node;
			if (scale != null) {
				node= new ColorCube(scale);
			} else {
				node= new ColorCube();
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static RotationInterpolator argumentToRotationInterpolator(Term value, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		try { // RotationInterpolator
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_RotationInterpolator,1,iX);
			return attributesToRotationInterpolator(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotRotationInterpolator(value);
		}
	}
	public static RotationInterpolator attributesToRotationInterpolator(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			javax.media.j3d.Alpha alpha= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_alpha) {
					alpha= argumentToAlpha3D(pairValue,iX);
					iterator.remove();
				}
			};
			RotationInterpolator node;
			if (alpha != null) {
				if (target != null) {
					node= new RotationInterpolator(alpha,target);
				} else {
					throw new RotationInterpolatorMustBeInsideTransformGroup();
				}
			} else {
				throw new AlphaAttributeIsNotDefined();
			};
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					Bounds bounds= argumentToBounds(pairValue,iX);
					node.setSchedulingBounds(bounds);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_transformAxis) {
					Transform3D transform3D= argumentToTransform3D(pairValue,iX);
					node.setTransformAxis(transform3D);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_minimumAngle) {
					float minimumAngle= (float)GeneralConverters.argumentToReal(pairValue,iX);
					node.setMinimumAngle(minimumAngle);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_maximumAngle) {
					float maximumAngle= (float)GeneralConverters.argumentToReal(pairValue,iX);
					node.setMaximumAngle(maximumAngle);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Shape3D argumentToShape3D(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Shape3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Shape3D,1,iX);
			return attributesToShape3D(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAShape3D(value);
		}
	}
	public static Shape3D attributesToShape3D(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Shape3D node= new Shape3D();
			Set<Long> nameList= setPositiveMap.keySet();
			extractShape3DAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	protected static void extractShape3DAttributes(Shape3D node, Group group, Set<Long> nameList, HashMap<Long,Term> setPositiveMap, Canvas3D targetWorld, ChoisePoint iX) {
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_geometry) {
				Geometry geometry= argumentToGeometry(pairValue,iX);
				node.setGeometry(geometry);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_appearance) {
				Appearance appearance= argumentToAppearance(pairValue,targetWorld,iX);
				node.setAppearance(appearance);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowAppearanceOverrideRead) {
				boolean allowAppearanceOverrideRead= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowAppearanceOverrideRead) {
					node.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowAppearanceOverrideWrite) {
				boolean allowAppearanceOverrideWrite= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowAppearanceOverrideWrite) {
					node.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowAppearanceRead) {
				boolean allowAppearanceRead= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowAppearanceRead) {
					node.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowAppearanceWrite) {
				boolean allowAppearanceWrite= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowAppearanceWrite) {
					node.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowCollisionBoundsRead) {
				boolean allowCollisionBoundsRead= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowCollisionBoundsRead) {
					node.setCapability(Shape3D.ALLOW_COLLISION_BOUNDS_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowCollisionBoundsWrite) {
				boolean allowCollisionBoundsWrite= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowCollisionBoundsWrite) {
					node.setCapability(Shape3D.ALLOW_COLLISION_BOUNDS_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowGeometryRead) {
				boolean allowGeometryRead= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowGeometryRead) {
					node.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowGeometryWrite) {
				boolean allowGeometryWrite= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				if (allowGeometryWrite) {
					node.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_pickingDetailLevel) {
				int pickingDetailLevel= argumentToPickingDetailLevel(pairValue,iX);
				PickTool.setCapabilities(node,pickingDetailLevel);
				iterator.remove();
			}
		};
		iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_cullFace) {
				int mode= argumentToFaceCullingMode(pairValue,iX);
				Appearance appearance= node.getAppearance();
				PolygonAttributes polygonAttributes= appearance.getPolygonAttributes();
				if (polygonAttributes==null) {
					polygonAttributes= new PolygonAttributes();
					polygonAttributes.setCullFace(mode);
					appearance.setPolygonAttributes(polygonAttributes);
				} else {
					polygonAttributes.setCullFace(mode);
				};
				iterator.remove();
			}
		};
		extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
	}
	//
	public static OrientedShape3D argumentToBillboard(Term value, Group group, Canvas3D targetWorld, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		try { // OrientedShape3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Billboard,1,iX);
			return attributesToBillboard(arguments[0],group,targetWorld,space3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotABillboard(value);
		}
	}
	public static OrientedShape3D attributesToBillboard(Term attributes, Group group, Canvas3D targetWorld, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			OrientedShape3D node= new OrientedShape3D();
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_mode) {
					int mode= argumentToBillboardAlignmentMode(pairValue,iX);
					node.setAlignmentMode(mode);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_point) {
					Point3f point= term2Coordinate3f(pairValue,iX);
					node.setRotationPoint(point);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_axis) {
					Vector3f axis= term2Vector3f(pairValue,iX);
					node.setAlignmentAxis(axis);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_enableConstantScale) {
					boolean constantScaleEnable= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					node.setConstantScaleEnable(constantScaleEnable);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_scale) {
					double scale= GeneralConverters.argumentToReal(pairValue,iX);
					node.setScale(scale);
					iterator.remove();
				}
			};
			extractShape3DAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Text2D argumentToText2D(Term value, Group group, Canvas3D targetWorld, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		try { // Text2D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Text2D,1,iX);
			return attributesToText2D(arguments[0],group,targetWorld,space3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAText2D(value);
		}
	}
	public static Text2D attributesToText2D(Term attributes, Group group, Canvas3D targetWorld, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Text2D node;
			Set<Long> nameList= setPositiveMap.keySet();
			String text= null;
			Color3f color= null;
			String fontName= null;
			Integer fontSize= null;
			Integer fontStyle= null;
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_string) {
					text= pairValue.toString(iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_color) {
					color= term2Color3(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_fontName) {
					try {
						fontName= ExtendedFontName.argumentToFontNameSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_fontSize) {
					try {
						fontSize= ExtendedFontSize.argumentToFontSizeSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_fontStyle) {
					try {
						fontStyle= ExtendedFontStyle.argumentToFontStyleSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					};
					iterator.remove();
				}
			};
			if (text==null) {
				text= ""; // Actor Prolog default value
			};
			if (color==null) {
				color= new Color3f(); // Actor Prolog default value
			};
			if (fontName==null) {
				fontName= Font.SANS_SERIF; // Actor Prolog default value
			};
			if (fontSize==null) {
				fontSize= 64; // Actor Prolog default value
			};
			if (fontStyle==null) {
				fontStyle= Font.PLAIN; // Actor Prolog default value
			};
			node= new Text2D(text,color,fontName,fontSize,fontStyle);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_rectangleScaleFactor) {
					float factor= (float)GeneralConverters.argumentToReal(pairValue,iX);
					node.setRectangleScaleFactor(factor);
					iterator.remove();
				}
			};
			extractShape3DAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Background argumentToBackground(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Background
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Background,1,iX);
			return attributesToBackground(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotABackground(value);
		}
	}
	public static Background attributesToBackground(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Background node= new Background();
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_color) {
					Color3f color= term2Color3(pairValue,iX);
					node.setColor(color);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_image) {
					ImageComponent2D image= argumentToImageComponent2D(pairValue,targetWorld,iX);
					node.setImage(image);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_scaleMode) {
					int imageScaleMode= argumentToImageScaleMode(pairValue,iX);
					node.setImageScaleMode(imageScaleMode);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_applicationBounds) {
					Bounds bounds= argumentToBounds(pairValue,iX);
					node.setApplicationBounds(bounds);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static AmbientLight argumentToAmbientLight(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // AmbientLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_AmbientLight,1,iX);
			return attributesToAmbientLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAmbientLight(value);
		}
	}
	public static AmbientLight attributesToAmbientLight(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			AmbientLight node= new AmbientLight();
			Set<Long> nameList= setPositiveMap.keySet();
			extractLightAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static DirectionalLight argumentToDirectionalLight(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // DirectionalLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_DirectionalLight,1,iX);
			return attributesToDirectionalLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotDirectionalLight(value);
		}
	}
	public static DirectionalLight attributesToDirectionalLight(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			DirectionalLight node= new DirectionalLight();
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_direction) {
					Vector3f coordinates= term2Vector3f(pairValue,iX);
					node.setDirection(coordinates);
					iterator.remove();
				}
			};
			extractLightAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static PointLight argumentToPointLight(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // PointLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PointLight,1,iX);
			return attributesToPointLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotPointLight(value);
		}
	}
	public static PointLight attributesToPointLight(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			PointLight node= new PointLight();
			if (debugColors()) {
				System.out.printf("PrincipalNode3D::argumentToNode:PointLight node= new PointLight();\n");
			}
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_position) {
					Point3f point= term2Coordinate3f(pairValue,iX);
					node.setPosition(point);
					if (debugColors()) {
						System.out.printf("PrincipalNode3D::argumentToNode:node(%s).setPosition(%s)\n",node,point);
					}
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_attenuation) {
					Point3f attenuation= term2Attenuation(pairValue,iX);
					node.setAttenuation(attenuation);
					if (debugColors()) {
						System.out.printf("PrincipalNode3D::argumentToNode:node(%s).setAttenuation(%s);\n",node,attenuation);
					}
					iterator.remove();
				}
			};
			extractLightAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	protected static void extractLightAttributes(Light node, Group group, Set<Long> nameList, HashMap<Long,Term> setPositiveMap, Canvas3D targetWorld, ChoisePoint iX) {
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_color) {
				Color3f color= term2Color3(pairValue,iX);
				node.setColor(color);
				iterator.remove();
				if (debugColors()) {
					System.out.printf("PrincipalNode3D::extractLightAttributes:node(%s).setColor(%s)\n",node,color);
				}
			} else if (pairName==SymbolCodes.symbolCode_E_influencingBounds) {
				Bounds bounds= argumentToBounds(pairValue,iX);
				node.setInfluencingBounds(bounds);
				iterator.remove();
				if (debugColors()) {
					System.out.printf("PrincipalNode3D::extractLightAttributes:node(%s).setInfluencingBounds(%s)\n",node,bounds);
				}
			} else if (pairName==SymbolCodes.symbolCode_E_lightOn) {
				boolean flag= YesNoConverters.termYesNo2Boolean(pairValue,iX);
				node.setEnable(flag);
				iterator.remove();
				if (debugColors()) {
					System.out.printf("PrincipalNode3D::extractLightAttributes:node(%s).setEnable(%s)\n",node,flag);
				}
			}
		};
		extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
	}
	//
	public static Box argumentToBox(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Box
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Box,1,iX);
			return attributesToBox(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotABox(value);
		}
	}
	public static Box attributesToBox(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Set<Long> nameList= setPositiveMap.keySet();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Float xdim= 1.0f;
			Float ydim= 1.0f;
			Float zdim= 1.0f;
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_xdim) {
					xdim= (float)GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_ydim) {
					ydim= (float)GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_zdim) {
					zdim= (float)GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				}
			};
			int primFlags= Tools3D.extractPrimitiveAttributes(nameList,setPositiveMap,iX);
			Box node= new Box(xdim,ydim,zdim,primFlags,null);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_appearance) {
					Appearance appearance= argumentToAppearance(pairValue,targetWorld,iX);
					node.setAppearance(appearance);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Cone argumentToCone(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Cone
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Cone,1,iX);
			return attributesToCone(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotACone(value);
		}
	}
	public static Cone attributesToCone(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Set<Long> nameList= setPositiveMap.keySet();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Float radius= 1.0f;
			Float height= 2.0f;
			Integer xdivisions= 15;
			Integer ydivisions= 1;
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_radius) {
					radius= (float)GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_height) {
					height= (float)GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_xdivisions) {
					try {
						xdivisions= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_ydivisions) {
					try {
						ydivisions= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				}
			};
			int primFlags= Tools3D.extractPrimitiveAttributes(nameList,setPositiveMap,iX);
			Cone node= new Cone(radius,height,primFlags,xdivisions,ydivisions,null);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_appearance) {
					Appearance appearance= argumentToAppearance(pairValue,targetWorld,iX);
					node.setAppearance(appearance);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Cylinder argumentToCylinder(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Cylinder
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Cylinder,1,iX);
			return attributesToCylinder(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotACylinder(value);
		}
	}
	public static Cylinder attributesToCylinder(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Set<Long> nameList= setPositiveMap.keySet();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Float radius= 1.0f;
			Float height= 2.0f;
			Integer xdivisions= 15;
			Integer ydivisions= 1;
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_radius) {
					radius= (float)GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_height) {
					height= (float)GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_xdivisions) {
					try {
						xdivisions= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_ydivisions) {
					try {
						ydivisions= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				}
			};
			int primFlags= Tools3D.extractPrimitiveAttributes(nameList,setPositiveMap,iX);
			Cylinder node= new Cylinder(radius,height,primFlags,xdivisions,ydivisions,null);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_appearance) {
					Appearance appearance= argumentToAppearance(pairValue,targetWorld,iX);
					node.setAppearance(appearance);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Sphere argumentToSphere(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Sphere
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Sphere,1,iX);
			return attributesToSphere(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotASphere(value);
		}
	}
	public static Sphere attributesToSphere(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Set<Long> nameList= setPositiveMap.keySet();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Float radius= 1.0f;
			Integer divisions= 15;
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_radius) {
					radius= (float)GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_divisions) {
					try {
						divisions= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				}
			};
			int primFlags= Tools3D.extractPrimitiveAttributes(nameList,setPositiveMap,iX);
			Sphere node= new Sphere(radius,primFlags,divisions,null);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_appearance) {
					Appearance appearance= argumentToAppearance(pairValue,targetWorld,iX);
					node.setAppearance(appearance);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static MouseRotate argumentToMouseRotate(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MouseRotate
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseRotate,1,iX);
			return attributesToMouseRotate(arguments[0],group,targetWorld,null,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMouseRotate(value);
		}
	}
	public static MouseRotate attributesToMouseRotate(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		if (target != null) {
			HashMap<Long,Term> setPositiveMap= new HashMap<>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				MouseRotate node= new MouseRotate(target);
				Set<Long> nameList= setPositiveMap.keySet();
				extractMouseBehaviorAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
			}
		} else {
			throw new MouseBehaviorMustBeInsideTransformGroup();
		}
	}
	//
	public static MouseTranslate argumentToMouseTranslate(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MouseTranslate
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseTranslate,1,iX);
			return attributesToMouseTranslate(arguments[0],group,targetWorld,null,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMouseTranslate(value);
		}
	}
	public static MouseTranslate attributesToMouseTranslate(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		if (target != null) {
			HashMap<Long,Term> setPositiveMap= new HashMap<>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				MouseTranslate node= new MouseTranslate(target);
				Set<Long> nameList= setPositiveMap.keySet();
				extractMouseBehaviorAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
			}
		} else {
			throw new MouseBehaviorMustBeInsideTransformGroup();
		}
	}
	//
	public static MouseWheelZoom argumentToMouseWheelZoom(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MouseWheelZoom
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseWheelZoom,1,iX);
			return attributesToMouseWheelZoom(arguments[0],group,targetWorld,null,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMouseWheelZoom(value);
		}
	}
	public static MouseWheelZoom attributesToMouseWheelZoom(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		if (target != null) {
			HashMap<Long,Term> setPositiveMap= new HashMap<>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				MouseWheelZoom node= new MouseWheelZoom(target);
				Set<Long> nameList= setPositiveMap.keySet();
				extractMouseBehaviorAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
			}
		} else {
			throw new MouseBehaviorMustBeInsideTransformGroup();
		}
	}
	//
	public static MouseZoom argumentToMouseZoom(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MouseZoom
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseZoom,1,iX);
			return attributesToMouseZoom(arguments[0],group,targetWorld,null,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMouseZoom(value);
		}
	}
	public static MouseZoom attributesToMouseZoom(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		if (target != null) {
			HashMap<Long,Term> setPositiveMap= new HashMap<>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				MouseZoom node= new MouseZoom(target);
				Set<Long> nameList= setPositiveMap.keySet();
				extractMouseBehaviorAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
			}
		} else {
			throw new MouseBehaviorMustBeInsideTransformGroup();
		}
	}
	//
	protected static void extractMouseBehaviorAttributes(MouseBehavior node, Group group, Set<Long> nameList, HashMap<Long,Term> setPositiveMap, Canvas3D targetWorld, ChoisePoint iX) {
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
				Bounds bounds= argumentToBounds(pairValue,iX);
				node.setSchedulingBounds(bounds);
				iterator.remove();
			}
		};
		extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
	}
	//
	public static OrbitBehavior argumentToOrbitBehavior(Term value, Group group, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		try { // OrbitBehavior
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_OrbitBehavior,1,iX);
			return attributesToOrbitBehavior(arguments[0],group,targetWorld,u,space3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotOrbitBehavior(value);
		}
	}
	public static OrbitBehavior attributesToOrbitBehavior(Term attributes, Group group, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			boolean enableRotate= true;
			boolean enableTranslate= true;
			boolean enableZoom= true;
			boolean proportionalZoom= false;
			boolean reverseAll= false;
			boolean reverseRotate= false;
			boolean reverseTranslate= false;
			boolean reverseZoom= false;
			boolean stopZoom= false;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_enableRotate) {
					enableRotate= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_enableTranslate) {
					enableTranslate= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_enableZoom) {
					enableZoom= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_proportionalZoom) {
					proportionalZoom= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_reverseAll) {
					reverseAll= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_reverseRotate) {
					reverseRotate= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_reverseTranslate) {
					reverseTranslate= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_reverseZoom) {
					reverseZoom= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_stopZoom) {
					stopZoom= YesNoConverters.termYesNo2Boolean(pairValue,iX);
					iterator.remove();
				}
			};
			int flags= 0;
			if (!enableRotate) {
				flags |= OrbitBehavior.DISABLE_ROTATE;
			};
			if (!enableTranslate) {
				flags |= OrbitBehavior.DISABLE_TRANSLATE;
			};
			if (!enableZoom) {
				flags |= OrbitBehavior.DISABLE_ZOOM;
			};
			if (proportionalZoom) {
				flags |= OrbitBehavior.PROPORTIONAL_ZOOM;
			};
			if (reverseAll) {
				flags |= OrbitBehavior.REVERSE_ALL;
			};
			if (reverseRotate) {
				flags |= OrbitBehavior.REVERSE_ROTATE;
			};
			if (reverseTranslate) {
				flags |= OrbitBehavior.REVERSE_TRANSLATE;
			};
			if (reverseZoom) {
				flags |= OrbitBehavior.REVERSE_ZOOM;
			};
			if (stopZoom) {
				flags |= OrbitBehavior.STOP_ZOOM;
			};
			OrbitBehavior node= new OrbitBehavior(space3D,flags);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_zoomFactor) {
					double factor= GeneralConverters.argumentToReal(pairValue,iX);
					node.setZoomFactor(factor);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_minRadius) {
					double radius= GeneralConverters.argumentToReal(pairValue,iX);
					node.setMinRadius(radius);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_rotationCenter) {
					Point3d center= term2Coordinate(pairValue,iX);
					node.setRotationCenter(center);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					Bounds bounds= argumentToBounds(pairValue,iX);
					node.setSchedulingBounds(bounds);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			targetWorld.setViewPlatformBehavior(node);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static ModelClip argumentToModelClip(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // ModelClip
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ModelClip,1,iX);
			return attributesToModelClip(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotModelClip(value);
		}
	}
	public static ModelClip attributesToModelClip(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			ModelClip node= new ModelClip();
			boolean[] enables= {false,false,false,false,false,false};
			node.setEnables(enables);
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_influencingBounds) {
					Bounds bounds= argumentToBounds(pairValue,iX);
					node.setInfluencingBounds(bounds);
					iterator.remove();
				} else if (key >= 1 && key <= 6) {
					Vector4d plane= term2Vector4(pairValue,iX);
					node.setPlane((int)key,plane);
					node.setEnable((int)key,true);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Behavior argumentToCustomizedBehavior(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // CustomizedBehavior
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_CustomizedBehavior,1,iX);
			return attributesToCustomizedBehavior(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotCustomizedBehavior(value);
		}
	}
	public static Behavior attributesToCustomizedBehavior(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			CustomizedBehavior node= new CustomizedBehavior(targetWorld);
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_name) {
					BehaviorName name= BehaviorName.argumentToBehaviorName(pairValue,iX);
					node.setName(name);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_wakeupOn) {
					WakeupCondition condition= argumentToWakeupCondition(pairValue,iX);
					node.setWakeupCondition(condition);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					Bounds bounds= argumentToBounds(pairValue,iX);
					node.setSchedulingBounds(bounds);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Behavior argumentToCollisionDetector(Term value, Node armingNode, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // CollisionDetector
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_CollisionDetector,1,iX);
			return attributesToCollisionDetector(arguments[0],armingNode,group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotCollisionDetector(value);
		}
	}
	public static Behavior attributesToCollisionDetector(Term attributes, Node armingNode, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			CollisionDetector node= new CollisionDetector(targetWorld);
			CollisionDetectorSpeedHint speedHint= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_name) {
					BehaviorName name= BehaviorName.argumentToBehaviorName(pairValue,iX);
					node.setName(name);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_speedHint) {
					speedHint= CollisionDetectorSpeedHint.argumentToCollisionDetectorSpeedHint(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					Bounds bounds= argumentToBounds(pairValue,iX);
					node.setSchedulingBounds(bounds);
					iterator.remove();
				}
			};
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_wakeupOn) {
					WakeupCondition condition= argumentToCollisionDetectorWakeupCondition(pairValue,armingNode,speedHint,iX);
					node.setWakeupCondition(condition);
					iterator.remove();
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static Group argumentToMovingShadow(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MovingShadow
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MovingShadow,1,iX);
			return attributesToMovingShadow(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMovingShadow(value);
		}
	}
	public static Group attributesToMovingShadow(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			NodeLabel objectLabel= null;
			NodeLabel pointLightLabel= null;
			Vector4d plane= null;
			double standoff= 0.001; // Actor Prolog default value
			Appearance appearance= null;
			Bounds bounds= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_object) {
					objectLabel= NodeLabel.argumentToNodeLabel(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_pointLight) {
					pointLightLabel= NodeLabel.argumentToNodeLabel(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_plane) {
					plane= term2Vector4(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_standoff) {
					standoff= GeneralConverters.argumentToReal(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_appearance) {
					appearance= argumentToAppearance(pairValue,targetWorld,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					bounds= argumentToBounds(pairValue,iX);
					iterator.remove();
				}
			};
			if (objectLabel != null) {
				MovingShadow node= new MovingShadow(targetWorld,objectLabel,group,pointLightLabel,plane,standoff,appearance,bounds);
				extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new MovingShadowObjectIsNotDefined();
			}
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static CustomizedPickCanvas argumentToPickCanvas(Term value, BranchGroup branchGroup, Canvas3D targetWorld, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		try { // PickCanvas
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PickCanvas,1,iX);
			return attributesToPickCanvas(arguments[0],branchGroup,targetWorld,space3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotPickCanvas(value);
		}
	}
	public static CustomizedPickCanvas attributesToPickCanvas(Term attributes, BranchGroup branchGroup, Canvas3D targetWorld, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			if (branchGroup != null) {
				CustomizedPickCanvas node= new CustomizedPickCanvas(branchGroup,targetWorld,space3D);
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_handleMouseClicked) {
						node.setHandleMouseClicked(YesNoConverters.termYesNo2Boolean(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseEntered) {
						node.setHandleMouseEntered(YesNoConverters.termYesNo2Boolean(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseExited) {
						node.setHandleMouseExited(YesNoConverters.termYesNo2Boolean(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMousePressed) {
						node.setHandleMousePressed(YesNoConverters.termYesNo2Boolean(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseReleased) {
						node.setHandleMouseReleased(YesNoConverters.termYesNo2Boolean(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseDragged) {
						node.setHandleMouseDragged(YesNoConverters.termYesNo2Boolean(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseMoved) {
						node.setHandleMouseMoved(YesNoConverters.termYesNo2Boolean(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_isPassive) {
						node.setIsPassive(YesNoConverters.termYesNo2Boolean(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_period) {
						long timeInterval= TimeIntervalConverters.argumentMillisecondsToTimeInterval(pairValue,iX).toMillisecondsLong();
						node.setPeriod(timeInterval);
					} else if (pairName==SymbolCodes.symbolCode_E_tolerance) {
						float tolerance= (float)GeneralConverters.argumentToReal(pairValue,iX);
						node.setTolerance(tolerance);
					} else if (pairName==SymbolCodes.symbolCode_E_mode) {
						int pickingMode= argumentToPickingMode(pairValue,iX);
						node.setMode(pickingMode);
					} else {
						throw new WrongArgumentIsUnknownPickCanvasAttribute(key);
					}
				};
				return node;
			} else {
				throw new PickCanvasMustBeInsideBranchGroup();
			}
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	protected static boolean debugNodes() {
		// return true;
		return false;
	}
	//
	protected static boolean debugColors() {
		// return true;
		return false;
	}
}
