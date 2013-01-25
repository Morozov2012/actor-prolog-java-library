// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.built_in.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.terms.*;

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
import com.sun.j3d.utils.universe.ViewingPlatform;
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
	public static Node termToNode(Term value, BranchGroup branchGroup, Group group, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, TransformGroup target, ChoisePoint iX) {
		try { // TransformGroup
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TransformGroup,1,iX);
			return attributesToTransformGroup(arguments[0],targetWorld,u,canvas3D,iX);
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
		try { // Text2D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Text2D,1,iX);
			return attributesToText2D(arguments[0],group,targetWorld,canvas3D,iX);
		} catch (Backtracking b5) {
		try { // Background
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Background,1,iX);
			return attributesToBackground(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b6) {
		try { // AmbientLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_AmbientLight,1,iX);
			return attributesToAmbientLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b7) {
		try { // DirectionalLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_DirectionalLight,1,iX);
			return attributesToDirectionalLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b8) {
		try { // PointLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PointLight,1,iX);
			return attributesToPointLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b9) {
		try { // Box
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Box,1,iX);
			return attributesToBox(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b10) {
		try { // Cone
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Cone,1,iX);
			return attributesToCone(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b11) {
		try { // Cylinder
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Cylinder,1,iX);
			return attributesToCylinder(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b12) {
		try { // Sphere
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Sphere,1,iX);
			return attributesToSphere(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b13) {
		try { // MouseRotate
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseRotate,1,iX);
			return attributesToMouseRotate(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b14) {
		try { // MouseTranslate
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseTranslate,1,iX);
			return attributesToMouseTranslate(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b15) {
		try { // MouseWheelZoom
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseWheelZoom,1,iX);
			return attributesToMouseWheelZoom(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b16) {
		try { // MouseZoom
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseZoom,1,iX);
			return attributesToMouseZoom(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b17) {
		try { // OrbitBehavior
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_OrbitBehavior,1,iX);
			attributesToOrbitBehavior(arguments[0],group,targetWorld,u,canvas3D,iX);
			return null;
		} catch (Backtracking b18) {
		try { // ModelClip
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ModelClip,1,iX);
			return attributesToModelClip(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b19) {
		try { // CustomizedBehavior
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_CustomizedBehavior,1,iX);
			return attributesToCustomizedBehavior(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b20) {
		try { // Group
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Group,1,iX);
			return attributesToGroup(arguments[0],targetWorld,u,canvas3D,iX);
		} catch (Backtracking b21) {
		try { // BranchGroup
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_BranchGroup,1,iX);
			return attributesToBranchGroup(arguments[0],targetWorld,u,canvas3D,iX);
		} catch (Backtracking b22) {
		try { // MovingShadow
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MovingShadow,1,iX);
			return attributesToMovingShadow(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b23) {
		try { // PickCanvas
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PickCanvas,1,iX);
			CustomizedPickCanvas pc=attributesToPickCanvas(arguments[0],branchGroup,targetWorld,canvas3D,iX);
			pc.activateTimer();
			return null;
		} catch (Backtracking b24) {
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
	//
	public static void termToListOfNodes(BranchGroup branchGroup, Group group, Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, TransformGroup target, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				Node node= termToNode(nextHead,branchGroup,group,targetWorld,u,canvas3D,target,iX);
				if (node != null) {
					group.addChild(node);
				};
				if (debugNodes()) {
					System.out.printf("PrincipalNode3D::termToListOfNodes:%s.addChild(%s);\n",group,node);
				}
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			throw new WrongArgumentIsNotAList(currentTail);
		// } catch (Throwable e) {
		}
	}
	//
	public static void termToCollisionDetectorList(Term value, Node armingNode, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				// Node node= termToNode(nextHead,branchGroup,group,targetWorld,u,canvas3D,target,iX);
				Node node= termToCollisionDetector(nextHead,armingNode,group,targetWorld,iX);
				if (node != null) {
					group.addChild(node);
				};
				if (debugNodes()) {
					System.out.printf("PrincipalNode3D::termToListOfNodes:%s.addChild(%s);\n",group,node);
				}
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			throw new WrongArgumentIsNotAList(currentTail);
		// } catch (Throwable e) {
		}
	}
	//
	public static Group termToGroup(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		try { // Group
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Group,1,iX);
			return attributesToGroup(arguments[0],targetWorld,u,canvas3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAGroup(value);
		}
	}
	public static Group attributesToGroup(Term attributes, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Group node= new Group();
			Set<Long> nameList= setPositiveMap.keySet();
			extractGroupAttributes(node,nameList,setPositiveMap,targetWorld,u,canvas3D,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	protected static void extractGroupAttributes(Group node, Set<Long> nameList, HashMap<Long,Term> setPositiveMap, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_allowChildrenExtend) {
				boolean allowChildrenExtend= Converters.term2YesNo(pairValue,iX);
				if (allowChildrenExtend) {
					node.setCapability(Group.ALLOW_CHILDREN_EXTEND);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowChildrenRead) {
				boolean allowChildrenRead= Converters.term2YesNo(pairValue,iX);
				if (allowChildrenRead) {
					node.setCapability(Group.ALLOW_CHILDREN_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowChildrenWrite) {
				boolean allowChildrenWrite= Converters.term2YesNo(pairValue,iX);
				if (allowChildrenWrite) {
					node.setCapability(Group.ALLOW_CHILDREN_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowCollisionBoundsRead) {
				boolean allowCollisionBoundsRead= Converters.term2YesNo(pairValue,iX);
				if (allowCollisionBoundsRead) {
					node.setCapability(Group.ALLOW_COLLISION_BOUNDS_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowCollisionBoundsWrite) {
				boolean allowCollisionBoundsWrite= Converters.term2YesNo(pairValue,iX);
				if (allowCollisionBoundsWrite) {
					node.setCapability(Group.ALLOW_COLLISION_BOUNDS_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_branches) {
				termToListOfNodes(null,node,pairValue,targetWorld,u,canvas3D,null,iX);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_label) {
				NodeLabel label= termToNodeLabel(pairValue,iX);
				targetWorld.rememberNode(label,node);
				iterator.remove();
			// } else {
			//	throw new WrongArgumentIsUnknownGroupAttribute(key);
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
				NodeLabel label= termToNodeLabel(pairValue,iX);
				targetWorld.rememberNode(label,node);
			} else if (pairName==SymbolCodes.symbolCode_E_collisionDetectors) {
				termToCollisionDetectorList(pairValue,node,group,targetWorld,iX);
			} else {
				throw new WrongArgumentIsUnknownNodeAttribute(key);
			}
		}
	}
	//
	public static BranchGroup termToBranchGroup(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		try { // BranchGroup
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_BranchGroup,1,iX);
			return attributesToBranchGroup(arguments[0],targetWorld,u,canvas3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotBranchGroup(value);
		}
	}
	public static BranchGroup attributesToBranchGroup(Term attributes, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					boolean allowDetach= Converters.term2YesNo(pairValue,iX);
					if (allowDetach) {
						node.setCapability(BranchGroup.ALLOW_DETACH);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_compile) {
					compileBranchGroup= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_branches) {
					termToListOfNodes(node,node,pairValue,targetWorld,u,canvas3D,null,iX);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownBranchGroupAttribute(key);
				}
			};
			extractGroupAttributes(node,nameList,setPositiveMap,targetWorld,u,canvas3D,iX);
			if (compileBranchGroup) {
				node.compile();
			};
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static TransformGroup termToTransformGroup(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		try { // TransformGroup
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TransformGroup,1,iX);
			return attributesToTransformGroup(arguments[0],targetWorld,u,canvas3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotTransformGroup(value);
		}
	}
	public static TransformGroup attributesToTransformGroup(Term attributes, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					boolean allowTransformRead= Converters.term2YesNo(pairValue,iX);
					if (allowTransformRead) {
						node.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowTransformWrite) {
					boolean allowTransformWrite= Converters.term2YesNo(pairValue,iX);
					if (allowTransformWrite) {
						node.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_branches) {
					termToListOfNodes(null,node,pairValue,targetWorld,u,canvas3D,node,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_transform3D) {
					Transform3D transform3D= termToTransform3D(pairValue,iX);
					node.setTransform(transform3D);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_label) {
					NodeLabel label= termToNodeLabel(pairValue,iX);
					targetWorld.rememberNode(label,node);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownTransformGroupAttribute(key);
				}
			};
			extractGroupAttributes(node,nameList,setPositiveMap,targetWorld,u,canvas3D,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static ColorCube termToColorCube(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // ColorCube
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ColorCube,1,iX);
			return attributesToColorCube(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotColorCube(value);
		}
	}
	public static ColorCube attributesToColorCube(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					try {
						scale= Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownColorCubeAttribute(key);
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
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static RotationInterpolator termToRotationInterpolator(Term value, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		try { // RotationInterpolator
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_RotationInterpolator,1,iX);
			return attributesToRotationInterpolator(arguments[0],group,targetWorld,target,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotRotationInterpolator(value);
		}
	}
	public static RotationInterpolator attributesToRotationInterpolator(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					alpha= termToAlpha3D(pairValue,iX);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownRotationInterpolatorAttribute(key);
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
					Bounds bounds= termToBounds(pairValue,iX);
					node.setSchedulingBounds(bounds);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_transformAxis) {
					Transform3D transform3D= termToTransform3D(pairValue,iX);
					node.setTransformAxis(transform3D);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_minimumAngle) {
					try {
						float minimumAngle= (float)Converters.termToReal(pairValue,iX);
						node.setMinimumAngle(minimumAngle);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_maximumAngle) {
					try {
						float maximumAngle= (float)Converters.termToReal(pairValue,iX);
						node.setMaximumAngle(maximumAngle);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownRotationInterpolatorAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static Shape3D termToShape3D(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Shape3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Shape3D,1,iX);
			return attributesToShape3D(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAShape3D(value);
		}
	}
	public static Shape3D attributesToShape3D(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Shape3D node= new Shape3D();
			Set<Long> nameList= setPositiveMap.keySet();
			extractShape3DAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	protected static void extractShape3DAttributes(Shape3D node, Group group, Set<Long> nameList, HashMap<Long,Term> setPositiveMap, Canvas3D targetWorld, ChoisePoint iX) {
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_geometry) {
				Geometry geometry= termToGeometry(pairValue,iX);
				node.setGeometry(geometry);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_appearance) {
				Appearance appearance= termToAppearance(pairValue,targetWorld,iX);
				node.setAppearance(appearance);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowAppearanceOverrideRead) {
				boolean allowAppearanceOverrideRead= Converters.term2YesNo(pairValue,iX);
				if (allowAppearanceOverrideRead) {
					node.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowAppearanceOverrideWrite) {
				boolean allowAppearanceOverrideWrite= Converters.term2YesNo(pairValue,iX);
				if (allowAppearanceOverrideWrite) {
					node.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowAppearanceRead) {
				boolean allowAppearanceRead= Converters.term2YesNo(pairValue,iX);
				if (allowAppearanceRead) {
					node.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowAppearanceWrite) {
				boolean allowAppearanceWrite= Converters.term2YesNo(pairValue,iX);
				if (allowAppearanceWrite) {
					node.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowCollisionBoundsRead) {
				boolean allowCollisionBoundsRead= Converters.term2YesNo(pairValue,iX);
				if (allowCollisionBoundsRead) {
					node.setCapability(Shape3D.ALLOW_COLLISION_BOUNDS_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowCollisionBoundsWrite) {
				boolean allowCollisionBoundsWrite= Converters.term2YesNo(pairValue,iX);
				if (allowCollisionBoundsWrite) {
					node.setCapability(Shape3D.ALLOW_COLLISION_BOUNDS_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowGeometryRead) {
				boolean allowGeometryRead= Converters.term2YesNo(pairValue,iX);
				if (allowGeometryRead) {
					node.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_allowGeometryWrite) {
				boolean allowGeometryWrite= Converters.term2YesNo(pairValue,iX);
				if (allowGeometryWrite) {
					node.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
				};
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_pickingDetailLevel) {
				int pickingDetailLevel= termToPickingDetailLevel(pairValue,iX);
				PickTool.setCapabilities(node,pickingDetailLevel);
				iterator.remove();
			// } else {
			//	throw new WrongArgumentIsUnknownShape3DAttribute(key);
			}
		};
		iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_cullFace) {
				int mode= termToFaceCullingMode(pairValue,iX);
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
			// } else {
			//	throw new WrongArgumentIsUnknownShape3DAttribute(key);
			}
		};
		extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
	}
	//
	public static Text2D termToText2D(Term value, Group group, Canvas3D targetWorld, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		try { // Text2D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Text2D,1,iX);
			return attributesToText2D(arguments[0],group,targetWorld,canvas3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAText2D(value);
		}
	}
	public static Text2D attributesToText2D(Term attributes, Group group, Canvas3D targetWorld, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
						fontName= GUI_Utils.termToFontNameSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_fontSize) {
					try {
						fontSize= GUI_Utils.termToFontSizeSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_fontStyle) {
					try {
						fontStyle= GUI_Utils.termToFontStyleSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					};
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownText2DAttribute(key);
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
					try {
						float factor= (float)Converters.termToReal(pairValue,iX);
						node.setRectangleScaleFactor(factor);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				}
			};
			extractShape3DAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static Background termToBackground(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Background
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Background,1,iX);
			return attributesToBackground(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotABackground(value);
		}
	}
	public static Background attributesToBackground(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					ImageComponent2D image= termToImageComponent2D(pairValue,targetWorld,iX);
					node.setImage(image);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_scaleMode) {
					int imageScaleMode= termToImageScaleMode(pairValue,iX);
					node.setImageScaleMode(imageScaleMode);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_applicationBounds) {
					Bounds bounds= termToBounds(pairValue,iX);
					node.setApplicationBounds(bounds);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownBackgroundAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static AmbientLight termToAmbientLight(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // AmbientLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_AmbientLight,1,iX);
			return attributesToAmbientLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAmbientLight(value);
		}
	}
	public static AmbientLight attributesToAmbientLight(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			AmbientLight node= new AmbientLight();
			Set<Long> nameList= setPositiveMap.keySet();
			extractLightAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static DirectionalLight termToDirectionalLight(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // DirectionalLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_DirectionalLight,1,iX);
			return attributesToDirectionalLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotDirectionalLight(value);
		}
	}
	public static DirectionalLight attributesToDirectionalLight(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					Vector3f coordinatest= term2Vector3f(pairValue,iX);
					node.setDirection(coordinatest);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownDirectionalLightAttribute(key);
				}
			};
			extractLightAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static PointLight termToPointLight(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // PointLight
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PointLight,1,iX);
			return attributesToPointLight(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotPointLight(value);
		}
	}
	public static PointLight attributesToPointLight(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			PointLight node= new PointLight();
			if (debugColors()) {
				System.out.printf("PrincipalNode3D::termToNode:PointLight node= new PointLight();\n");
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
						System.out.printf("PrincipalNode3D::termToNode:node(%s).setPosition(%s)\n",node,point);
					}
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_attenuation) {
					Point3f attenuation= term2Attenuation(pairValue,iX);
					node.setAttenuation(attenuation);
					if (debugColors()) {
						System.out.printf("PrincipalNode3D::termToNode:node(%s).setAttenuation(%s);\n",node,attenuation);
					}
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownPointLightAttribute(key);
				}
			};
			extractLightAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
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
				Bounds bounds= termToBounds(pairValue,iX);
				node.setInfluencingBounds(bounds);
				iterator.remove();
				if (debugColors()) {
					System.out.printf("PrincipalNode3D::extractLightAttributes:node(%s).setInfluencingBounds(%s)\n",node,bounds);
				}
			} else if (pairName==SymbolCodes.symbolCode_E_lightOn) {
				boolean flag= Converters.term2YesNo(pairValue,iX);
				node.setEnable(flag);
				iterator.remove();
				if (debugColors()) {
					System.out.printf("PrincipalNode3D::extractLightAttributes:node(%s).setEnable(%s)\n",node,flag);
				}
			// } else {
			//	throw new WrongArgumentIsUnknownLightAttribute(key);
			}
		};
		extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
	}
	//
	public static Box termToBox(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Box
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Box,1,iX);
			return attributesToBox(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotABox(value);
		}
	}
	public static Box attributesToBox(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					try {
						xdim= (float)Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_ydim) {
					try {
						ydim= (float)Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_zdim) {
					try {
						zdim= (float)Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownBoxAttribute(key);
				}
			};
			int primFlags= extractPrimitiveAttributes(nameList,setPositiveMap,iX);
			Box node= new Box(xdim,ydim,zdim,primFlags,null);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_appearance) {
					Appearance appearance= termToAppearance(pairValue,targetWorld,iX);
					node.setAppearance(appearance);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownBoxAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static Cone termToCone(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Cone
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Cone,1,iX);
			return attributesToCone(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotACone(value);
		}
	}
	public static Cone attributesToCone(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					try {
						radius= (float)Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_height) {
					try {
						height= (float)Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
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
				// } else {
				//	throw new WrongArgumentIsUnknownPointConeAttribute(key);
				}
			};
			int primFlags= extractPrimitiveAttributes(nameList,setPositiveMap,iX);
			Cone node= new Cone(radius,height,primFlags,xdivisions,ydivisions,null);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_appearance) {
					Appearance appearance= termToAppearance(pairValue,targetWorld,iX);
					node.setAppearance(appearance);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownConeAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static Cylinder termToCylinder(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Cylinder
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Cylinder,1,iX);
			return attributesToCylinder(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotACylinder(value);
		}
	}
	public static Cylinder attributesToCylinder(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					try {
						radius= (float)Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_height) {
					try {
						height= (float)Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
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
				// } else {
				//	throw new WrongArgumentIsUnknownCylinderAttribute(key);
				}
			};
			int primFlags= extractPrimitiveAttributes(nameList,setPositiveMap,iX);
			Cylinder node= new Cylinder(radius,height,primFlags,xdivisions,ydivisions,null);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_appearance) {
					Appearance appearance= termToAppearance(pairValue,targetWorld,iX);
					node.setAppearance(appearance);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownCylinderAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static Sphere termToSphere(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Sphere
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Sphere,1,iX);
			return attributesToSphere(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotASphere(value);
		}
	}
	public static Sphere attributesToSphere(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					try {
						radius= (float)Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_divisions) {
					try {
						divisions= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownSphereAttribute(key);
				}
			};
			int primFlags= extractPrimitiveAttributes(nameList,setPositiveMap,iX);
			Sphere node= new Sphere(radius,primFlags,divisions,null);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_appearance) {
					Appearance appearance= termToAppearance(pairValue,targetWorld,iX);
					node.setAppearance(appearance);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownSphereAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static MouseRotate termToMouseRotate(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MouseRotate
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseRotate,1,iX);
			return attributesToMouseRotate(arguments[0],group,targetWorld,null,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMouseRotate(value);
		}
	}
	public static MouseRotate attributesToMouseRotate(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		if (target != null) {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				MouseRotate node= new MouseRotate(target);
				Set<Long> nameList= setPositiveMap.keySet();
				extractMouseBehaviorAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} else {
			throw new MouseBehaviorMustBeInsideTransformGroup();
		}
	}
	//
	public static MouseTranslate termToMouseTranslate(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MouseTranslate
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseTranslate,1,iX);
			return attributesToMouseTranslate(arguments[0],group,targetWorld,null,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMouseTranslate(value);
		}
	}
	public static MouseTranslate attributesToMouseTranslate(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		if (target != null) {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				MouseTranslate node= new MouseTranslate(target);
				Set<Long> nameList= setPositiveMap.keySet();
				extractMouseBehaviorAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} else {
			throw new MouseBehaviorMustBeInsideTransformGroup();
		}
	}
	//
	public static MouseWheelZoom termToMouseWheelZoom(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MouseWheelZoom
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseWheelZoom,1,iX);
			return attributesToMouseWheelZoom(arguments[0],group,targetWorld,null,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMouseWheelZoom(value);
		}
	}
	public static MouseWheelZoom attributesToMouseWheelZoom(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		if (target != null) {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				MouseWheelZoom node= new MouseWheelZoom(target);
				Set<Long> nameList= setPositiveMap.keySet();
				extractMouseBehaviorAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} else {
			throw new MouseBehaviorMustBeInsideTransformGroup();
		}
	}
	//
	public static MouseZoom termToMouseZoom(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MouseZoom
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MouseZoom,1,iX);
			return attributesToMouseZoom(arguments[0],group,targetWorld,null,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMouseZoom(value);
		}
	}
	public static MouseZoom attributesToMouseZoom(Term attributes, Group group, Canvas3D targetWorld, TransformGroup target, ChoisePoint iX) {
		if (target != null) {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				MouseZoom node= new MouseZoom(target);
				Set<Long> nameList= setPositiveMap.keySet();
				extractMouseBehaviorAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
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
				Bounds bounds= termToBounds(pairValue,iX);
				node.setSchedulingBounds(bounds);
				iterator.remove();
			// } else {
			//	throw new WrongArgumentIsUnknownMouseBehaviorAttribute(key);
			}
		};
		extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
	}
	//
	public static OrbitBehavior termToOrbitBehavior(Term value, Group group, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		try { // OrbitBehavior
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_OrbitBehavior,1,iX);
			return attributesToOrbitBehavior(arguments[0],group,targetWorld,u,canvas3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotOrbitBehavior(value);
		}
	}
	public static OrbitBehavior attributesToOrbitBehavior(Term attributes, Group group, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					enableRotate= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_enableTranslate) {
					enableTranslate= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_enableZoom) {
					enableZoom= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_proportionalZoom) {
					proportionalZoom= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_reverseAll) {
					reverseAll= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_reverseRotate) {
					reverseRotate= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_reverseTranslate) {
					reverseTranslate= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_reverseZoom) {
					reverseZoom= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_stopZoom) {
					stopZoom= Converters.term2YesNo(pairValue,iX);
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
			OrbitBehavior node= new OrbitBehavior(canvas3D,flags);
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_zoomFactor) {
					try {
						double factor= Converters.termToReal(pairValue,iX);
						node.setZoomFactor(factor);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_minRadius) {
					try {
						double radius= Converters.termToReal(pairValue,iX);
						node.setMinRadius(radius);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_rotationCenter) {
					Point3d center= term2Coordinate(pairValue,iX);
					node.setRotationCenter(center);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					Bounds bounds= termToBounds(pairValue,iX);
					node.setSchedulingBounds(bounds);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownOrbitBehaviorAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			ViewingPlatform viewingPlatform= u.getViewingPlatform();
			viewingPlatform.setViewPlatformBehavior(node);
			return node;
			// return null;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static ModelClip termToModelClip(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // ModelClip
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ModelClip,1,iX);
			return attributesToModelClip(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotModelClip(value);
		}
	}
	public static ModelClip attributesToModelClip(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					Bounds bounds= termToBounds(pairValue,iX);
					node.setInfluencingBounds(bounds);
					iterator.remove();
				} else if (key >= 1 && key <= 6) {
					Vector4d plane= term2Vector4(pairValue,iX);
					node.setPlane((int)key,plane);
					node.setEnable((int)key,true);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownModelClipAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static Behavior termToCustomizedBehavior(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // CustomizedBehavior
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_CustomizedBehavior,1,iX);
			return attributesToCustomizedBehavior(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotCustomizedBehavior(value);
		}
	}
	public static Behavior attributesToCustomizedBehavior(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					BehaviorName name= termToBehaviorName(pairValue,iX);
					node.setName(name);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_wakeupOn) {
					WakeupCondition condition= termToWakeupCondition(pairValue,iX);
					node.setWakeupCondition(condition);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					Bounds bounds= termToBounds(pairValue,iX);
					node.setSchedulingBounds(bounds);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownCustomizedBehaviorAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static Behavior termToCollisionDetector(Term value, Node armingNode, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // CollisionDetector
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_CollisionDetector,1,iX);
			return attributesToCollisionDetector(arguments[0],armingNode,group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotCollisionDetector(value);
		}
	}
	public static Behavior attributesToCollisionDetector(Term attributes, Node armingNode, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					BehaviorName name= termToBehaviorName(pairValue,iX);
					node.setName(name);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_speedHint) {
					speedHint= termToCollisionDetectorSpeedHint(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					Bounds bounds= termToBounds(pairValue,iX);
					node.setSchedulingBounds(bounds);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownCustomizedBehaviorAttribute(key);
				}
			};
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_wakeupOn) {
					WakeupCondition condition= termToCollisionDetectorWakeupCondition(pairValue,armingNode,speedHint,iX);
					node.setWakeupCondition(condition);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownCustomizedBehaviorAttribute(key);
				}
			};
			extractNodeAttributes(node,group,nameList,setPositiveMap,targetWorld,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static Group termToMovingShadow(Term value, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		try { // MovingShadow
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_MovingShadow,1,iX);
			return attributesToMovingShadow(arguments[0],group,targetWorld,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotMovingShadow(value);
		}
	}
	public static Group attributesToMovingShadow(Term attributes, Group group, Canvas3D targetWorld, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
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
					objectLabel= termToNodeLabel(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_pointLight) {
					pointLightLabel= termToNodeLabel(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_plane) {
					plane= term2Vector4(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_standoff) {
					try {
						standoff= Converters.termToReal(pairValue,iX);
					} catch (TermIsNotAReal e) {
						throw new WrongArgumentIsNotNumeric(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_appearance) {
					appearance= termToAppearance(pairValue,targetWorld,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_schedulingBounds) {
					bounds= termToBounds(pairValue,iX);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownMovingShadowAttribute(key);
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
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	public static CustomizedPickCanvas termToPickCanvas(Term value, BranchGroup branchGroup, Canvas3D targetWorld, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		try { // PickCanvas
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PickCanvas,1,iX);
			return attributesToPickCanvas(arguments[0],branchGroup,targetWorld,canvas3D,iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotPickCanvas(value);
		}
	}
	public static CustomizedPickCanvas attributesToPickCanvas(Term attributes, BranchGroup branchGroup, Canvas3D targetWorld, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			if (branchGroup != null) {
				CustomizedPickCanvas node= new CustomizedPickCanvas(branchGroup,targetWorld,canvas3D);
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_handleMouseClicked) {
						node.setHandleMouseClicked(Converters.term2YesNo(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseEntered) {
						node.setHandleMouseEntered(Converters.term2YesNo(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseExited) {
						node.setHandleMouseExited(Converters.term2YesNo(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMousePressed) {
						node.setHandleMousePressed(Converters.term2YesNo(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseReleased) {
						node.setHandleMouseReleased(Converters.term2YesNo(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseDragged) {
						node.setHandleMouseDragged(Converters.term2YesNo(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_handleMouseMoved) {
						node.setHandleMouseMoved(Converters.term2YesNo(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_isPassive) {
						node.setIsPassive(Converters.term2YesNo(pairValue,iX));
					} else if (pairName==SymbolCodes.symbolCode_E_period) {
						// long timeInterval= pairValue.getLongIntegerValue(iX);
						long timeInterval= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setPeriod(timeInterval);
					} else if (pairName==SymbolCodes.symbolCode_E_tolerance) {
						try {
							float tolerance= (float)Converters.termToReal(pairValue,iX);
							node.setTolerance(tolerance);
						} catch (TermIsNotAReal e) {
							throw new WrongArgumentIsNotNumeric(pairValue);
						}
					} else if (pairName==SymbolCodes.symbolCode_E_mode) {
						int pickingMode= termToPickingMode(pairValue,iX);
						node.setMode(pickingMode);
					} else {
						throw new WrongArgumentIsUnknownPickCanvasAttribute(key);
					}
				};
				return node;
				// return null;
			} else {
				throw new PickCanvasMustBeInsideBranchGroup();
			}
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
}
