// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.gui.*;
import morozov.system.gui.space3d.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;
import javax.vecmath.AxisAngle4d;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import com.sun.j3d.utils.geometry.Primitive;

public class Tools3D {
	//
	public static Point3d[] term2Coordinates(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		Point3d[] points= new Point3d[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			points[n]= term2Coordinate(termArray[n],iX);
		};
		return points;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Point3d term2Coordinate(Term value, ChoisePoint iX) {
		try { // Point3d
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_p,3,iX);
			double x= Converters.argumentToReal(arguments[0],iX);
			double y= Converters.argumentToReal(arguments[1],iX);
			double z= Converters.argumentToReal(arguments[2],iX);
			return new Point3d(x,y,z);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAPoint3D(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Point3f term2Coordinate3f(Term value, ChoisePoint iX) {
		try { // Point3f
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_p,3,iX);
			float x= (float)Converters.argumentToReal(arguments[0],iX);
			float y= (float)Converters.argumentToReal(arguments[1],iX);
			float z= (float)Converters.argumentToReal(arguments[2],iX);
			return new Point3f(x,y,z);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAPoint3D(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int[] term2Indices(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		int[] indices= new int[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			try {
				indices[n]= termArray[n].getSmallIntegerValue(iX);
			} catch (TermIsNotAnInteger e) {
				throw new WrongArgumentIsNotAnInteger(termArray[n]);
			}
		};
		return indices;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int[] term2StripCounts(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		int[] counts= new int[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			try {
				counts[n]= termArray[n].getSmallIntegerValue(iX);
			} catch (TermIsNotAnInteger e) {
				throw new WrongArgumentIsNotAnInteger(termArray[n]);
			}
		};
		return counts;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Vector3f[] term2Normals(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		Vector3f[] normals= new Vector3f[termArray.length];
		for (int n=0; n < termArray.length; n++) {
			normals[n]= term2Vector3f(termArray[n],iX);
		};
		return normals;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Color3f term2Color3(Term value, ChoisePoint iX) {
		try {
			return term2Color3OrExit(value,iX);
		} catch (TermIsSymbolDefault e) {
			return new Color3f();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Color3f term2Color3OrExit(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try { // Color3f
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_color3,3,iX);
			float r= (float)Converters.argumentToReal(arguments[0],iX);
			float g= (float)Converters.argumentToReal(arguments[1],iX);
			float b= (float)Converters.argumentToReal(arguments[2],iX);
			return new Color3f(r,g,b);
		} catch (Backtracking b1) {
			try { // Color3f
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_color4,4,iX);
				float r= (float)Converters.argumentToReal(arguments[0],iX);
				float g= (float)Converters.argumentToReal(arguments[1],iX);
				float b= (float)Converters.argumentToReal(arguments[2],iX);
				float a= (float)Converters.argumentToReal(arguments[3],iX);
				return new Color3f(new Color(r,g,b,a));
			} catch (Backtracking b2) {
				Color color= ExtendedColor.termToColor(value,iX);
				return new Color3f(color);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Point3f term2Attenuation(Term value, ChoisePoint iX) {
		try { // Attenuation
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_a,3,iX);
			float constant= (float)Converters.argumentToReal(arguments[0],iX);
			float linear= (float)Converters.argumentToReal(arguments[1],iX);
			float quadratic= (float)Converters.argumentToReal(arguments[2],iX);
			return new Point3f(constant,quadratic,quadratic);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAnAttenuation(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Vector3d term2Vector3(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		if (termArray.length==3) {
			double x= Converters.argumentToReal(termArray[0],iX);
			double y= Converters.argumentToReal(termArray[1],iX);
			double z= Converters.argumentToReal(termArray[2],iX);
			return new Vector3d(x,y,z);
		} else {
			throw new WrongNumberOfElementsInList(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Vector3f term2Vector3f(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		if (termArray.length==3) {
			float x= (float)Converters.argumentToReal(termArray[0],iX);
			float y= (float)Converters.argumentToReal(termArray[1],iX);
			float z= (float)Converters.argumentToReal(termArray[2],iX);
			return new Vector3f(x,y,z);
		} else {
			throw new WrongNumberOfElementsInList(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Vector4d term2Vector4(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		if (termArray.length==4) {
			double x= Converters.argumentToReal(termArray[0],iX);
			double y= Converters.argumentToReal(termArray[1],iX);
			double z= Converters.argumentToReal(termArray[2],iX);
			double w= Converters.argumentToReal(termArray[3],iX);
			return new Vector4d(x,y,z,w);
		} else {
			throw new WrongNumberOfElementsInList(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static AxisAngle4d term2AxisAngle4(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		if (termArray.length==4) {
			double x= Converters.argumentToReal(termArray[0],iX);
			double y= Converters.argumentToReal(termArray[1],iX);
			double z= Converters.argumentToReal(termArray[2],iX);
			double angle= Converters.argumentToReal(termArray[3],iX);
			return new AxisAngle4d(x,y,z,angle);
		} else {
			throw new WrongNumberOfElementsInList(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static WakeupCondition termToWakeupCondition(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_KEY_PRESSED) {
				return new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
			} else if (flag==SymbolCodes.symbolCode_E_KEY_RELEASED) {
				return new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
			} else if (flag==SymbolCodes.symbolCode_E_KEY_TYPED) {
				return new WakeupOnAWTEvent(KeyEvent.KEY_TYPED);
			} else {
				throw new WrongArgumentIsNotWakeupCondition(value);
			}
		} catch (TermIsNotASymbol e) {
			// try { // WakeupOnCollisionEntry
			//	Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_WakeupOnCollisionEntry,1,iX);
			//	return attributesToWakeupOnCollisionEntry(arguments[0],target,iX);
			// } catch (Backtracking b1) {
			// try { // WakeupOnCollisionExit
			//	Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_WakeupOnCollisionExit,1,iX);
			//	return attributesToWakeupOnCollisionExit(arguments[0],target,iX);
			// } catch (Backtracking b2) {
			// try { // WakeupOnCollisionMovement
			//	Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_WakeupOnCollisionMovement,1,iX);
			//	return attributesToWakeupOnCollisionMovement(arguments[0],target,iX);
			// } catch (Backtracking b3) {
			try { // WakeupOnElapsedFrames
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ElapsedFrames,1,iX);
				return attributesToWakeupOnElapsedFrames(arguments[0],iX);
			} catch (Backtracking b4) {
			try { // WakeupOnElapsedTime
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ElapsedTime,1,iX);
				return attributesToWakeupOnElapsedTime(arguments[0],iX);
			} catch (Backtracking b5) {
				throw new WrongArgumentIsNotWakeupCondition(value);
			}
			}
			// }
			// }
			// }
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static WakeupOnElapsedFrames attributesToWakeupOnElapsedFrames(Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			int frameCount= 0; // Actor Prolog default value
			boolean isPassive= false;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_frameCount) {
					try {
						frameCount= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					}
				} else if (pairName==SymbolCodes.symbolCode_E_isPassive) {
					isPassive= YesNo.termYesNo2Boolean(pairValue,iX);
				} else {
					throw new WrongArgumentIsUnknownElapsedFramesAttribute(key);
				}
			};
			WakeupOnElapsedFrames node= new WakeupOnElapsedFrames(frameCount,isPassive);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static WakeupOnElapsedTime attributesToWakeupOnElapsedTime(Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			long elapsedTime= 1000; // Actor Prolog default value
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_frameCount) {
					// Milliseconds expected
					// elapsedTime= pairValue.getLongIntegerValue(iX);
					elapsedTime= TimeInterval.termMillisecondsToTimeInterval(pairValue,iX).toMillisecondsLong();
				} else {
					throw new WrongArgumentIsUnknownElapsedTimeAttribute(key);
				}
			};
			WakeupOnElapsedTime node= new WakeupOnElapsedTime(elapsedTime);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static WakeupCondition termToCollisionDetectorWakeupCondition(Term value, Node armingNode, CollisionDetectorSpeedHint speedHint, ChoisePoint iX) {
		WakeupCondition condition;
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_ENTRY) {
				if (speedHint==null) {
					condition= new WakeupOnCollisionEntry(armingNode);
				} else if (speedHint==CollisionDetectorSpeedHint.USE_GEOMETRY) {
					condition= new WakeupOnCollisionEntry(armingNode,WakeupOnCollisionEntry.USE_GEOMETRY);
				} else {
					condition= new WakeupOnCollisionEntry(armingNode,WakeupOnCollisionEntry.USE_BOUNDS);
				}
			} else if (flag==SymbolCodes.symbolCode_E_EXIT) {
				if (speedHint==null) {
					condition= new WakeupOnCollisionExit(armingNode);
				} else if (speedHint==CollisionDetectorSpeedHint.USE_GEOMETRY) {
					condition= new WakeupOnCollisionExit(armingNode,WakeupOnCollisionExit.USE_GEOMETRY);
				} else {
					condition= new WakeupOnCollisionExit(armingNode,WakeupOnCollisionExit.USE_BOUNDS);
				}
			} else if (flag==SymbolCodes.symbolCode_E_MOVEMENT) {
				if (speedHint==null) {
					condition= new WakeupOnCollisionMovement(armingNode);
				} else if (speedHint==CollisionDetectorSpeedHint.USE_GEOMETRY) {
					condition= new WakeupOnCollisionMovement(armingNode,WakeupOnCollisionMovement.USE_GEOMETRY);
				} else {
					condition= new WakeupOnCollisionMovement(armingNode,WakeupOnCollisionMovement.USE_BOUNDS);
				}
			} else {
				throw new WrongArgumentIsNotCollisionDetectorWakeupCondition(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotCollisionDetectorWakeupCondition(value);
		};
		return condition;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int extractPrimitiveAttributes(Set<Long> nameList, HashMap<Long,Term> setPositiveMap, ChoisePoint iX) {
		boolean enableAppearanceModify= false;
		boolean enableGeometryPicking= false;
		boolean generateNormals= true;
		boolean generateNormalsInward= false;
		boolean generateTextureCoords= false;
		boolean generateTextureCoordsYUp= false;
		boolean geometryNotShared= false;
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_enableAppearanceModify) {
				enableAppearanceModify= YesNo.termYesNo2Boolean(pairValue,iX);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_enableGeometryPicking) {
				enableGeometryPicking= YesNo.termYesNo2Boolean(pairValue,iX);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_generateNormals) {
				generateNormals= YesNo.termYesNo2Boolean(pairValue,iX);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_generateNormalsInward) {
				generateNormalsInward= YesNo.termYesNo2Boolean(pairValue,iX);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_generateTextureCoords) {
				generateTextureCoords= YesNo.termYesNo2Boolean(pairValue,iX);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_generateTextureCoordsYUp) {
				generateTextureCoordsYUp= YesNo.termYesNo2Boolean(pairValue,iX);
				iterator.remove();
			} else if (pairName==SymbolCodes.symbolCode_E_geometryNotShared) {
				geometryNotShared= YesNo.termYesNo2Boolean(pairValue,iX);
				iterator.remove();
			}
		};
		int primFlags= 0;
		if (enableAppearanceModify) {
			primFlags |= Primitive.ENABLE_APPEARANCE_MODIFY;
		};
		if (enableGeometryPicking) {
			primFlags |= Primitive.ENABLE_GEOMETRY_PICKING;
		};
		if (generateNormals) {
			primFlags |= Primitive.GENERATE_NORMALS;
		};
		if (generateNormalsInward) {
			primFlags |= Primitive.GENERATE_NORMALS_INWARD;
		};
		if (generateTextureCoords) {
			primFlags |= Primitive.GENERATE_TEXTURE_COORDS;
		};
		if (generateTextureCoordsYUp) {
			primFlags |= Primitive.GENERATE_TEXTURE_COORDS_Y_UP;
		};
		if (geometryNotShared) {
			primFlags |= Primitive.GEOMETRY_NOT_SHARED;
		};
		return primFlags;
	}
}
