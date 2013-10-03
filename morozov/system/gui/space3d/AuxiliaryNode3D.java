// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.gui.*;
import morozov.system.gui.space3d.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Font;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.AxisAngle4d;
import javax.media.j3d.Bounds;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Transform3D;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.PointArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.GeometryStripArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.TriangleStripArray;
import javax.media.j3d.TriangleFanArray;
import javax.media.j3d.IndexedGeometryArray;
import javax.media.j3d.IndexedPointArray;
import javax.media.j3d.IndexedLineArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.IndexedGeometryStripArray;
import javax.media.j3d.IndexedLineStripArray;
import javax.media.j3d.IndexedTriangleStripArray;
import javax.media.j3d.IndexedTriangleFanArray;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.Text3D;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.Background;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.ImageComponent2D;
import java.awt.image.BufferedImage;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.OrientedShape3D;

public class AuxiliaryNode3D extends Tools3D {
	//
	public static javax.media.j3d.Alpha termToAlpha3D(Term value, ChoisePoint iX) {
		try { // Alpha3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Alpha3D,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				javax.media.j3d.Alpha node= new javax.media.j3d.Alpha();
				boolean increasingEnable= true;
				boolean decreasingEnable= false;
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_increasingEnable) {
						increasingEnable= Converters.term2YesNo(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_decreasingEnable) {
						decreasingEnable= Converters.term2YesNo(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_increasingAlphaDuration) {
						// long increasingAlphaDuration= pairValue.getLongIntegerValue(iX);
						long increasingAlphaDuration= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setIncreasingAlphaDuration(increasingAlphaDuration);
					} else if (pairName==SymbolCodes.symbolCode_E_decreasingAlphaDuration) {
						// long decreasingAlphaDuration= pairValue.getLongIntegerValue(iX);
						long decreasingAlphaDuration= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setDecreasingAlphaDuration(decreasingAlphaDuration);
					} else if (pairName==SymbolCodes.symbolCode_E_alphaAtZeroDuration) {
						// long alphaAtZeroDuration= pairValue.getLongIntegerValue(iX);
						long alphaAtZeroDuration= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setAlphaAtZeroDuration(alphaAtZeroDuration);
					} else if (pairName==SymbolCodes.symbolCode_E_alphaAtOneDuration) {
						// long alphaAtOneDuration= pairValue.getLongIntegerValue(iX);
						long alphaAtOneDuration= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setAlphaAtOneDuration(alphaAtOneDuration);
					} else if (pairName==SymbolCodes.symbolCode_E_increasingAlphaRampDuration) {
						// long increasingAlphaRampDuration= pairValue.getLongIntegerValue(iX);
						long increasingAlphaRampDuration= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setIncreasingAlphaRampDuration(increasingAlphaRampDuration);
					} else if (pairName==SymbolCodes.symbolCode_E_decreasingAlphaRampDuration) {
						// long decreasingAlphaRampDuration= pairValue.getLongIntegerValue(iX);
						long decreasingAlphaRampDuration= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setDecreasingAlphaRampDuration(decreasingAlphaRampDuration);
					} else if (pairName==SymbolCodes.symbolCode_E_loopCount) {
						try {
							int loopCount= pairValue.getSmallIntegerValue(iX);
							node.setLoopCount(loopCount);
						} catch (TermIsNotAnInteger e) {
							throw new WrongArgumentIsNotAnInteger(pairValue);
						}
					} else if (pairName==SymbolCodes.symbolCode_E_startTime) {
						// long startTime= pairValue.getLongIntegerValue(iX);
						long startTime= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setStartTime(startTime);
					} else if (pairName==SymbolCodes.symbolCode_E_triggerTime) {
						// long triggerTime= pairValue.getLongIntegerValue(iX);
						long triggerTime= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setTriggerTime(triggerTime);
					} else if (pairName==SymbolCodes.symbolCode_E_phaseDelayDuration) {
						// long phaseDelayDuration= pairValue.getLongIntegerValue(iX);
						long phaseDelayDuration= Converters.termMillisecondsToMilliseconds(pairValue,iX);
						node.setPhaseDelayDuration(phaseDelayDuration);
					} else {
						throw new WrongArgumentIsUnknownAlphaAttribute(key);
					}
				};
				int mode= 0;
				if (increasingEnable) {
					mode |= javax.media.j3d.Alpha.INCREASING_ENABLE;
				};
				if (decreasingEnable) {
					mode |= javax.media.j3d.Alpha.DECREASING_ENABLE;
				};
				node.setMode(mode);
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAnAlpha3D(value);
		}
	}
	public static Bounds termToBounds(Term value, ChoisePoint iX) {
		try { // Bounds
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_BoundingSphere,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				BoundingSphere node= new BoundingSphere();
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_center) {
						Point3d coordinate= term2Coordinate(pairValue,iX);
						node.setCenter(coordinate);
					} else if (pairName==SymbolCodes.symbolCode_E_radius) {
						double radius= Converters.argumentToReal(pairValue,iX);
						node.setRadius(radius);
					} else {
						throw new WrongArgumentIsUnknownBoundingSphereAttribute(key);
					}
				};
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotBounds(value);
		}
	}
	public static Transform3D termToTransform3D(Term value, ChoisePoint iX) {
		try { // Transform3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Transform3D,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				Transform3D node= new Transform3D();
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_rotation) {
						AxisAngle4d angle= term2AxisAngle4(pairValue,iX);
						node.setRotation(angle);
						iterator.remove();
					}
				};
				iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_scale) {
						double scale= Converters.argumentToReal(pairValue,iX);
						node.setScale(scale);
						iterator.remove();
					}
				};
				iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_translation) {
						Vector3d vector= term2Vector3(pairValue,iX);
						node.setTranslation(vector);
						iterator.remove();
					}
				};
				iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_rotX) {
						double angle= Converters.argumentToReal(pairValue,iX);
						Transform3D tempRotate= new Transform3D();
						tempRotate.rotX(angle);
						node.mul(tempRotate);
						iterator.remove();
					}
				};
				iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_rotY) {
						double angle= Converters.argumentToReal(pairValue,iX);
						Transform3D tempRotate= new Transform3D();
						tempRotate.rotY(angle);
						node.mul(tempRotate);
						iterator.remove();
					}
				};
				iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_rotZ) {
						double angle= Converters.argumentToReal(pairValue,iX);
						Transform3D tempRotate= new Transform3D();
						tempRotate.rotZ(angle);
						node.mul(tempRotate);
						iterator.remove();
					} else {
						throw new WrongArgumentIsUnknownTransform3DAttribute(key);
					}
				};
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotATransform3D(value);
		}
	}
	public static Geometry termToGeometry(Term value, ChoisePoint iX) {
		try { // Geometry
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_geometryArray,1,iX);
			GeometryInfo node= termToGeometryInfo(arguments[0],iX);
			return node.getGeometryArray();
		} catch (Backtracking b1) {
		try { // PointArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PointArray,1,iX);
			PointArray node= attributesToPointArray(arguments[0],iX);
			return node;
		} catch (Backtracking b2) {
		try { // LineArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_LineArray,1,iX);
			LineArray node= attributesToLineArray(arguments[0],iX);
			return node;
		} catch (Backtracking b3) {
		try { // TriangleArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TriangleArray,1,iX);
			TriangleArray node= attributesToTriangleArray(arguments[0],iX);
			return node;
		} catch (Backtracking b4) {
		try { // QuadArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_QuadArray,1,iX);
			QuadArray node= attributesToQuadArray(arguments[0],iX);
			return node;
		} catch (Backtracking b5) {
		try { // LineStripArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_LineStripArray,1,iX);
			LineStripArray node= attributesToLineStripArray(arguments[0],iX);
			return node;
		} catch (Backtracking b6) {
		try { // TriangleStripArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TriangleStripArray,1,iX);
			TriangleStripArray node= attributesToTriangleStripArray(arguments[0],iX);
			return node;
		} catch (Backtracking b7) {
		try { // TriangleFanArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TriangleFanArray,1,iX);
			TriangleFanArray node= attributesToTriangleFanArray(arguments[0],iX);
			return node;
		} catch (Backtracking b8) {
		try { // IndexedPointArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_IndexedPointArray,1,iX);
			IndexedPointArray node= attributesToIndexedPointArray(arguments[0],iX);
			return node;
		} catch (Backtracking b9) {
		try { // IndexedLineArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_IndexedLineArray,1,iX);
			IndexedLineArray node= attributesToIndexedLineArray(arguments[0],iX);
			return node;
		} catch (Backtracking b10) {
		try { // IndexedTriangleArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_IndexedTriangleArray,1,iX);
			IndexedTriangleArray node= attributesToIndexedTriangleArray(arguments[0],iX);
			return node;
		} catch (Backtracking b11) {
		try { // IndexedQuadArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_IndexedQuadArray,1,iX);
			IndexedQuadArray node= attributesToIndexedQuadArray(arguments[0],iX);
			return node;
		} catch (Backtracking b12) {
		try { // IndexedLineStripArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_IndexedLineStripArray,1,iX);
			IndexedLineStripArray node= attributesToIndexedLineStripArray(arguments[0],iX);
			return node;
		} catch (Backtracking b13) {
		try { // IndexedTriangleStripArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_IndexedTriangleStripArray,1,iX);
			IndexedTriangleStripArray node= attributesToIndexedTriangleStripArray(arguments[0],iX);
			return node;
		} catch (Backtracking b14) {
		try { // IndexedTriangleFanArray
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_IndexedTriangleFanArray,1,iX);
			IndexedTriangleFanArray node= attributesToIndexedTriangleFanArray(arguments[0],iX);
			return node;
		} catch (Backtracking b15) {
		try { // Text3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Text3D,1,iX);
			Text3D node= attributesToText3D(arguments[0],iX);
			return node;
		} catch (Backtracking b16) {
			throw new WrongArgumentIsNotAGeometry(value);
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
	public static GeometryInfo termToGeometryInfo(Term value, ChoisePoint iX) {
		try { // GeometryInfo
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_GeometryInfo,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Set<Long> nameList= setPositiveMap.keySet();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				GeometryInfo node= null;
				Integer primitive= null;
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_primitive) {
						try {
							long flag= pairValue.getSymbolValue(iX);
							if (flag==SymbolCodes.symbolCode_E_POLYGON_ARRAY) {
								primitive= GeometryInfo.POLYGON_ARRAY;
							} else if (flag==SymbolCodes.symbolCode_E_QUAD_ARRAY) {
								primitive= GeometryInfo.QUAD_ARRAY;
							} else if (flag==SymbolCodes.symbolCode_E_TRIANGLE_ARRAY) {
								primitive= GeometryInfo.TRIANGLE_ARRAY;
							} else if (flag==SymbolCodes.symbolCode_E_TRIANGLE_FAN_ARRAY) {
								primitive= GeometryInfo.TRIANGLE_FAN_ARRAY;
							} else if (flag==SymbolCodes.symbolCode_E_TRIANGLE_STRIP_ARRAY) {
								primitive= GeometryInfo.TRIANGLE_STRIP_ARRAY;
							} else {
								throw new WrongArgumentIsNotGeometryInfoPrimitive(pairValue);
							};
							iterator.remove();
						} catch (TermIsNotASymbol e) {
							throw new WrongArgumentIsNotASymbol(pairValue);
						}
					}
				};
				if (primitive != null) {
					node= new GeometryInfo(primitive);
				} else {
					// Actor Prolog default GeometryInfo primitive:
					node= new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
				};
				boolean generateNormals= false;
				Double creaseAngle= null;
				boolean stripify= false;
				iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_coordinates) {
						Point3d[] coordinates= term2Coordinates(pairValue,iX);
						node.setCoordinates(coordinates);
					} else if (pairName==SymbolCodes.symbolCode_E_coordinateIndices) {
						int[] coordinateIndices= term2Indices(pairValue,iX);
						node.setCoordinateIndices(coordinateIndices);
					} else if (pairName==SymbolCodes.symbolCode_E_stripCounts) {
						int[] stripCounts= term2StripCounts(pairValue,iX);
						node.setStripCounts(stripCounts);
					} else if (pairName==SymbolCodes.symbolCode_E_generateNormals) {
						generateNormals= Converters.term2YesNo(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_creaseAngle) {
						creaseAngle= Converters.argumentToReal(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_stripify) {
						stripify= Converters.term2YesNo(pairValue,iX);
					} else {
						throw new WrongArgumentIsUnknownGeometryInfoAttribute(key);
					}
				};
				if (generateNormals) {
					NormalGenerator ng;
					if (creaseAngle==null) {
						ng= new NormalGenerator();
					} else {
						ng= new NormalGenerator(creaseAngle);
					};
					ng.generateNormals(node);
				};
				if (stripify) {
					Stripifier st= new Stripifier();
					st.stripify(node);
				};
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotGeometryInfo(value);
		}
	}
	public static Appearance termToAppearance(Term value, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Appearance
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Appearance,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				Appearance node= new Appearance();
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_material) {
						Material material= termToMaterial(pairValue,iX);
						node.setMaterial(material);
					} else if (pairName==SymbolCodes.symbolCode_E_coloringAttributes) {
						ColoringAttributes coloringAttributes= termToColoringAttributes(pairValue,iX);
						node.setColoringAttributes(coloringAttributes);
					} else if (pairName==SymbolCodes.symbolCode_E_polygonAttributes) {
						PolygonAttributes polygonAttributes= termToPolygonRenderingMode(pairValue,iX);
						node.setPolygonAttributes(polygonAttributes);
					} else if (pairName==SymbolCodes.symbolCode_E_transparencyAttributes) {
						TransparencyAttributes transparencyAttributes= termToObjectTransparency(pairValue,iX);
						node.setTransparencyAttributes(transparencyAttributes);
					} else if (pairName==SymbolCodes.symbolCode_E_texture) {
						Texture texture= termToTexture2D(pairValue,targetWorld,iX);
						node.setTexture(texture);
					} else if (pairName==SymbolCodes.symbolCode_E_allowMaterialRead) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_MATERIAL_READ);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowMaterialWrite) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowColoringAttributesRead) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowColoringAttributesWrite) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowPolygonAttributesRead) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowPolygonAttributesWrite) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowTransparencyAttributesRead) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowTransparencyAttributesWrite) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowTextureRead) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_TEXTURE_READ);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowTextureWrite) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
						};
					} else {
						throw new WrongArgumentIsUnknownAppearanceAttribute(key);
					}
				};
				// node.setLineAttributes(new LineAttributes(1,LineAttributes.PATTERN_SOLID,true));
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAnAppearance(value);
		}
	}
	protected static Texture2D termToTexture2D(Term value, Canvas3D targetWorld, ChoisePoint iX) {
		try { // Texture2D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Texture2D,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				ImageComponent2D image= null;
				int format= Texture.RGBA;
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_image) {
						image= termToImageComponent2D(pairValue,targetWorld,iX);
						iterator.remove();
					} else if (pairName==SymbolCodes.symbolCode_E_format) {
						format= termToTextureFormat(pairValue,iX);
						iterator.remove();
					}
				};
				if (image != null) {
					Texture2D node= new Texture2D(
						Texture.BASE_LEVEL,
						format,
						image.getWidth(),
						image.getHeight());
					node.setImage(0,image);
					iterator= nameList.iterator();
					while(iterator.hasNext()) {
						long key= iterator.next();
						long pairName= - key;
						Term pairValue= setPositiveMap.get(key);
						if (pairName==SymbolCodes.symbolCode_E_enableTextureMapping) {
							boolean enableTextureMapping= Converters.term2YesNo(pairValue,iX);
							node.setEnable(enableTextureMapping);
						} else if (pairName==SymbolCodes.symbolCode_E_magnificationFilter) {
							int magFilter= termToMagnificationFilter(pairValue,iX);
							node.setMagFilter(magFilter);
						} else if (pairName==SymbolCodes.symbolCode_E_minificationFilter) {
							int minFilter= termToMinificationFilter(pairValue,iX);
							node.setMinFilter(minFilter);
						} else {
							throw new WrongArgumentIsUnknownTexture2DAttribute(key);
						}
					};
					return node;
				} else {
					throw new ImageFileNameIsNotDefined();
				}
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotATexture2D(value);
		}
	}
	protected static int termToTextureFormat(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_INTENSITY) {
				return Texture.INTENSITY;
			} else if (flag==SymbolCodes.symbolCode_E_LUMINANCE) {
				return Texture.LUMINANCE;
			} else if (flag==SymbolCodes.symbolCode_E_ALPHA) {
				return Texture.ALPHA;
			} else if (flag==SymbolCodes.symbolCode_E_LUMINANCE_ALPHA) {
				return Texture.LUMINANCE_ALPHA;
			} else if (flag==SymbolCodes.symbolCode_E_RGB) {
				return Texture.RGB;
			} else if (flag==SymbolCodes.symbolCode_E_RGBA) {
				return Texture.RGBA;
			} else {
				throw new WrongArgumentIsNotTextureFormat(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static int termToMagnificationFilter(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_FASTEST) {
				return Texture.FASTEST;
			} else if (flag==SymbolCodes.symbolCode_E_NICEST) {
				return Texture.NICEST;
			} else if (flag==SymbolCodes.symbolCode_E_BASE_LEVEL_POINT) {
				return Texture.BASE_LEVEL_POINT;
			} else if (flag==SymbolCodes.symbolCode_E_BASE_LEVEL_LINEAR) {
				return Texture.BASE_LEVEL_LINEAR;
			} else if (flag==SymbolCodes.symbolCode_E_LINEAR_SHARPEN) {
				return Texture.LINEAR_SHARPEN;
			} else if (flag==SymbolCodes.symbolCode_E_LINEAR_SHARPEN_RGB) {
				return Texture.LINEAR_SHARPEN_RGB;
			} else if (flag==SymbolCodes.symbolCode_E_LINEAR_SHARPEN_ALPHA) {
				return Texture.LINEAR_SHARPEN_ALPHA;
			} else if (flag==SymbolCodes.symbolCode_E_FILTER4) {
				return Texture.FILTER4;
			} else {
				throw new WrongArgumentIsNotMagnificationFilter(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static int termToMinificationFilter(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_FASTEST) {
				return Texture.FASTEST;
			} else if (flag==SymbolCodes.symbolCode_E_NICEST) {
				return Texture.NICEST;
			} else if (flag==SymbolCodes.symbolCode_E_BASE_LEVEL_POINT) {
				return Texture.BASE_LEVEL_POINT;
			} else if (flag==SymbolCodes.symbolCode_E_BASE_LEVEL_LINEAR) {
				return Texture.BASE_LEVEL_LINEAR;
			} else if (flag==SymbolCodes.symbolCode_E_MULTI_LEVEL_POINT) {
				return Texture.MULTI_LEVEL_POINT;
			} else if (flag==SymbolCodes.symbolCode_E_MULTI_LEVEL_LINEAR) {
				return Texture.MULTI_LEVEL_LINEAR;
			} else if (flag==SymbolCodes.symbolCode_E_FILTER4) {
				return Texture.FILTER4;
			} else {
				throw new WrongArgumentIsNotMinificationFilter(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	public static Material termToMaterial(Term value, ChoisePoint iX) {
		try { // Material
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Material,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				Material node= new Material();
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_ambientColor) {
						Color3f color= term2Color3(pairValue,iX);
						node.setAmbientColor(color);
					} else if (pairName==SymbolCodes.symbolCode_E_emissiveColor) {
						Color3f color= term2Color3(pairValue,iX);
						node.setEmissiveColor(color);
					} else if (pairName==SymbolCodes.symbolCode_E_diffuseColor) {
						// Color3f color= term2Color3(pairValue,iX);
						// node.setDiffuseColor(color);
						extractDiffuseColor(node,pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_specularColor) {
						Color3f color= term2Color3(pairValue,iX);
						node.setSpecularColor(color);
					} else if (pairName==SymbolCodes.symbolCode_E_shininess) {
						float shininess= (float)Converters.argumentToReal(pairValue,iX);
						node.setShininess(shininess);
					} else if (pairName==SymbolCodes.symbolCode_E_lightingEnable) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						node.setLightingEnable(mode);
					} else {
						throw new WrongArgumentIsUnknownMaterialAttribute(key);
					}
				};
				return node;
			} else {
				throw new WrongArgumentIsNotAttributeSet(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAMaterial(value);
		}
	}
	//
	public static ColoringAttributes termToColoringAttributes(Term value, ChoisePoint iX) {
		try { // ColoringAttributes
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_ColoringAttributes,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				ColoringAttributes node= new ColoringAttributes();
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_color) {
						Color3f color= term2Color3(pairValue,iX);
						node.setColor(color);
					} else if (pairName==SymbolCodes.symbolCode_E_shadeModel) {
						int model= termToShadeModel(pairValue,iX);
						node.setShadeModel(model);
					} else if (pairName==SymbolCodes.symbolCode_E_allowColorRead) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(ColoringAttributes.ALLOW_COLOR_READ);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowColorWrite) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowShadeModelRead) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(ColoringAttributes.ALLOW_SHADE_MODEL_READ);
						};
					} else if (pairName==SymbolCodes.symbolCode_E_allowShadeModelWrite) {
						boolean mode= Converters.term2YesNo(pairValue,iX);
						if (mode) {
							node.setCapability(ColoringAttributes.ALLOW_SHADE_MODEL_WRITE);
						};
					} else {
						throw new WrongArgumentIsUnknownColoringAttribute(key);
					}
				};
				return node;
			} else {
				throw new WrongArgumentIsNotColoringAttribute(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotColoringAttribute(value);
		}
	}
	protected static PolygonAttributes termToPolygonRenderingMode(Term value, ChoisePoint iX) {
		try { // PolygonAttributes
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_PolygonAttributes,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				int polygonRasterizationMode= PolygonAttributes.POLYGON_FILL;
				int cullFace= PolygonAttributes.CULL_BACK;
				float polygonOffsetBias= 0.0f;
				float polygonOffsetFactor= 0.0f;
				boolean backFaceNormalFlip= false;
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_rasterizationMode) {
						polygonRasterizationMode= termToRasterizationMode(pairValue,iX);
						// iterator.remove();
					} else if (pairName==SymbolCodes.symbolCode_E_cullFace) {
						cullFace= termToFaceCullingMode(pairValue,iX);
						// iterator.remove();
					} else if (pairName==SymbolCodes.symbolCode_E_polygonOffsetBias) {
						polygonOffsetBias= (float)Converters.argumentToReal(pairValue,iX);
						// iterator.remove();
					} else if (pairName==SymbolCodes.symbolCode_E_polygonOffsetFactor) {
						polygonOffsetFactor= (float)Converters.argumentToReal(pairValue,iX);
						// iterator.remove();
					} else if (pairName==SymbolCodes.symbolCode_E_backFaceNormalFlip) {
						backFaceNormalFlip= Converters.term2YesNo(pairValue,iX);
						// iterator.remove();
					} else {
						throw new WrongArgumentIsUnknownPolygonAttribute(key);
					}
				};
				PolygonAttributes node= new PolygonAttributes(
					polygonRasterizationMode,
					cullFace,
					polygonOffsetBias,
					backFaceNormalFlip,
					polygonOffsetFactor);
				return node;
			} else {
				throw new WrongArgumentIsNotPolygonRenderingMode(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotPolygonRenderingMode(value);
		}
	}
	protected static int termToShadeModel(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_FASTEST) {
				return ColoringAttributes.FASTEST;
			} else if (flag==SymbolCodes.symbolCode_E_NICEST) {
				return ColoringAttributes.NICEST;
			} else if (flag==SymbolCodes.symbolCode_E_SHADE_FLAT) {
				return ColoringAttributes.SHADE_FLAT;
			} else if (flag==SymbolCodes.symbolCode_E_SHADE_GOURAUD) {
				return ColoringAttributes.SHADE_GOURAUD;
			} else {
				throw new WrongArgumentIsNotShadeModel(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static int termToRasterizationMode(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_POLYGON_POINT) {
				return PolygonAttributes.POLYGON_POINT;
			} else if (flag==SymbolCodes.symbolCode_E_POLYGON_LINE) {
				return PolygonAttributes.POLYGON_LINE;
			} else if (flag==SymbolCodes.symbolCode_E_POLYGON_FILL) {
				return PolygonAttributes.POLYGON_FILL;
			} else {
				throw new WrongArgumentIsNotRasterizationMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static int termToFaceCullingMode(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_CULL_BACK) {
				return PolygonAttributes.CULL_BACK;
			} else if (flag==SymbolCodes.symbolCode_E_CULL_FRONT) {
				return PolygonAttributes.CULL_FRONT;
			} else if (flag==SymbolCodes.symbolCode_E_CULL_NONE) {
				return PolygonAttributes.CULL_NONE;
			} else {
				throw new WrongArgumentIsNotFaceCullingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static TransparencyAttributes termToObjectTransparency(Term value, ChoisePoint iX) {
		try { // ObjectTransparency
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_TransparencyAttributes,1,iX);
			Term attributes= arguments[0];
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				TransparencyAttributes node= new TransparencyAttributes();
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_transparency) {
						float transparency= (float)Converters.argumentToReal(pairValue,iX);
						node.setTransparency(transparency);
					} else if (pairName==SymbolCodes.symbolCode_E_transparencyMode) {
						int mode= termToTransparencyMode(pairValue,iX);
						node.setTransparencyMode(mode);
					} else if (pairName==SymbolCodes.symbolCode_E_dstBlendFunction) {
						int mode= termToBlendFunction(pairValue,iX);
						node.setDstBlendFunction(mode);
					} else if (pairName==SymbolCodes.symbolCode_E_srcBlendFunction) {
						int mode= termToBlendFunction(pairValue,iX);
						node.setSrcBlendFunction(mode);
					} else {
						throw new WrongArgumentIsUnknownTransparencyAttribute(key);
					}
				};
				return node;
			} else {
				throw new WrongArgumentIsNotObjectTransparency(setEnd);
			}
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotObjectTransparency(value);
		}
	}
	protected static int termToTransparencyMode(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_NONE) {
				return TransparencyAttributes.NONE;
			} else if (flag==SymbolCodes.symbolCode_E_FASTEST) {
				return TransparencyAttributes.FASTEST;
			} else if (flag==SymbolCodes.symbolCode_E_NICEST) {
				return TransparencyAttributes.NICEST;
			} else if (flag==SymbolCodes.symbolCode_E_SCREEN_DOOR) {
				return TransparencyAttributes.SCREEN_DOOR;
			} else if (flag==SymbolCodes.symbolCode_E_BLENDED) {
				return TransparencyAttributes.BLENDED;
			} else {
				throw new WrongArgumentIsNotTransparencyMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static int termToBlendFunction(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_BLEND_ZERO) {
				return TransparencyAttributes.BLEND_ZERO;
			} else if (flag==SymbolCodes.symbolCode_E_BLEND_ONE) {
				return TransparencyAttributes.BLEND_ONE;
			} else if (flag==SymbolCodes.symbolCode_E_BLEND_SRC_ALPHA) {
				return TransparencyAttributes.BLEND_SRC_ALPHA;
			} else if (flag==SymbolCodes.symbolCode_E_BLEND_ONE_MINUS_SRC_ALPHA) {
				return TransparencyAttributes.BLEND_ONE_MINUS_SRC_ALPHA;
			} else if (flag==SymbolCodes.symbolCode_E_BLEND_DST_COLOR) {
				return TransparencyAttributes.BLEND_DST_COLOR;
			} else if (flag==SymbolCodes.symbolCode_E_BLEND_ONE_MINUS_DST_COLOR) {
				return TransparencyAttributes.BLEND_ONE_MINUS_DST_COLOR;
			} else if (flag==SymbolCodes.symbolCode_E_BLEND_SRC_COLOR) {
				return TransparencyAttributes.BLEND_SRC_COLOR;
			} else if (flag==SymbolCodes.symbolCode_E_BLEND_ONE_MINUS_SRC_COLOR) {
				return TransparencyAttributes.BLEND_ONE_MINUS_SRC_COLOR;
			} else {
				throw new WrongArgumentIsNotBlendFunction(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	public static PointArray attributesToPointArray(Term attributes, ChoisePoint iX) {
		return (PointArray)attributesToPlainGeometryArray(PlainGeometryArrayType.POINT,attributes,iX);
	}
	public static LineArray attributesToLineArray(Term attributes, ChoisePoint iX) {
		return (LineArray)attributesToPlainGeometryArray(PlainGeometryArrayType.LINE,attributes,iX);
	}
	public static TriangleArray attributesToTriangleArray(Term attributes, ChoisePoint iX) {
		return (TriangleArray)attributesToPlainGeometryArray(PlainGeometryArrayType.TRIANGLE,attributes,iX);
	}
	public static QuadArray attributesToQuadArray(Term attributes, ChoisePoint iX) {
		return (QuadArray)attributesToPlainGeometryArray(PlainGeometryArrayType.QUAD,attributes,iX);
	}
	protected static GeometryArray attributesToPlainGeometryArray(PlainGeometryArrayType type, Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			int vertexCount= 0; // Actor Prolog default value
			boolean includeVertexPositions= true; // Actor Prolog default value
			boolean includePerVertexNormals= false;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_vertexCount) {
					try {
						vertexCount= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_includeVertexPositions) {
					includeVertexPositions= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_includePerVertexNormals) {
					includePerVertexNormals= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				}
			};
			int vertexFormat= 0;
			if (includeVertexPositions) {
				vertexFormat |= GeometryArray.COORDINATES;
			};
			if (includePerVertexNormals) {
				vertexFormat |= GeometryArray.NORMALS;
			};
			GeometryArray node= null;
			switch (type) {
			case POINT:
				node= new PointArray(vertexCount,vertexFormat);
				break;
			case LINE:
				node= new LineArray(vertexCount,vertexFormat);
				break;
			case TRIANGLE:
				node= new TriangleArray(vertexCount,vertexFormat);
				break;
			case QUAD:
				node= new QuadArray(vertexCount,vertexFormat);
				break;
			};
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_coordinates) {
					Point3d[] coordinates= term2Coordinates(pairValue,iX);
					node.setCoordinates(0,coordinates);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_normals) {
					Vector3f[] normals= term2Normals(pairValue,iX);
					node.setNormals(0,normals);
					iterator.remove();
				}
			};
			extractGeometryAttributes(node,nameList,setPositiveMap,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	public static LineStripArray attributesToLineStripArray(Term attributes, ChoisePoint iX) {
		return (LineStripArray)attributesToGeometryStripArray(GeometryStripArrayType.LINE,attributes,iX);
	}
	public static TriangleStripArray attributesToTriangleStripArray(Term attributes, ChoisePoint iX) {
		return (TriangleStripArray)attributesToGeometryStripArray(GeometryStripArrayType.TRIANGLE,attributes,iX);
	}
	public static TriangleFanArray attributesToTriangleFanArray(Term attributes, ChoisePoint iX) {
		return (TriangleFanArray)attributesToGeometryStripArray(GeometryStripArrayType.FAN,attributes,iX);
	}
	public static GeometryStripArray attributesToGeometryStripArray(GeometryStripArrayType type, Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			int vertexCount= 0; // Actor Prolog default value
			boolean includeVertexPositions= true; // Actor Prolog default value
			boolean includePerVertexNormals= false;
			int[] stripVertexCounts= new int[0]; // Actor Prolog default value
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_vertexCount) {
					try {
						vertexCount= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_includeVertexPositions) {
					includeVertexPositions= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_includePerVertexNormals) {
					includePerVertexNormals= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_stripVertexCounts) {
					stripVertexCounts= term2StripCounts(pairValue,iX);
					iterator.remove();
				}
			};
			int vertexFormat= 0;
			if (includeVertexPositions) {
				vertexFormat |= GeometryArray.COORDINATES;
			};
			if (includePerVertexNormals) {
				vertexFormat |= GeometryArray.NORMALS;
			};
			GeometryStripArray node= null;
			switch (type) {
			case LINE:
				node= new LineStripArray(vertexCount,vertexFormat,stripVertexCounts);
				break;
			case TRIANGLE:
				node= new TriangleStripArray(vertexCount,vertexFormat,stripVertexCounts);
				break;
			case FAN:
				node= new TriangleFanArray(vertexCount,vertexFormat,stripVertexCounts);
				break;
			};
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_coordinates) {
					Point3d[] coordinates= term2Coordinates(pairValue,iX);
					node.setCoordinates(0,coordinates);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_normals) {
					Vector3f[] normals= term2Normals(pairValue,iX);
					node.setNormals(0,normals);
					iterator.remove();
				}
			};
			extractGeometryAttributes(node,nameList,setPositiveMap,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	public static IndexedPointArray attributesToIndexedPointArray(Term attributes, ChoisePoint iX) {
		return (IndexedPointArray)attributesToIndexedGeometryArray(PlainGeometryArrayType.POINT,attributes,iX);
	}
	public static IndexedLineArray attributesToIndexedLineArray(Term attributes, ChoisePoint iX) {
		return (IndexedLineArray)attributesToIndexedGeometryArray(PlainGeometryArrayType.LINE,attributes,iX);
	}
	public static IndexedTriangleArray attributesToIndexedTriangleArray(Term attributes, ChoisePoint iX) {
		return (IndexedTriangleArray)attributesToIndexedGeometryArray(PlainGeometryArrayType.TRIANGLE,attributes,iX);
	}
	public static IndexedQuadArray attributesToIndexedQuadArray(Term attributes, ChoisePoint iX) {
		return (IndexedQuadArray)attributesToIndexedGeometryArray(PlainGeometryArrayType.QUAD,attributes,iX);
	}
	public static IndexedGeometryArray attributesToIndexedGeometryArray(PlainGeometryArrayType type, Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			int vertexCount= 0; // Actor Prolog default value
			boolean includeVertexPositions= true; // Actor Prolog default value
			boolean includePerVertexNormals= false;
			int indexCount= 0; // Actor Prolog default value
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_vertexCount) {
					try {
						vertexCount= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_includeVertexPositions) {
					includeVertexPositions= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_includePerVertexNormals) {
					includePerVertexNormals= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_indexCount) {
					try {
						indexCount= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				}
			};
			int vertexFormat= 0;
			if (includeVertexPositions) {
				vertexFormat |= GeometryArray.COORDINATES;
			};
			if (includePerVertexNormals) {
				vertexFormat |= GeometryArray.NORMALS;
			};
			IndexedGeometryArray node= null;
			switch (type) {
			case POINT:
				node= new IndexedPointArray(vertexCount,vertexFormat,indexCount);
				break;
			case LINE:
				node= new IndexedLineArray(vertexCount,vertexFormat,indexCount);
				break;
			case TRIANGLE:
				node= new IndexedTriangleArray(vertexCount,vertexFormat,indexCount);
				break;
			case QUAD:
				node= new IndexedQuadArray(vertexCount,vertexFormat,indexCount);
				break;
			};
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_coordinates) {
					Point3d[] coordinates= term2Coordinates(pairValue,iX);
					node.setCoordinates(0,coordinates);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_coordinateIndices) {
					int[] coordinateIndices= term2Indices(pairValue,iX);
					node.setCoordinateIndices(0,coordinateIndices);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_normals) {
					Vector3f[] normals= term2Normals(pairValue,iX);
					node.setNormals(0,normals);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_normalIndices) {
					int[] normalIndices= term2Indices(pairValue,iX);
					node.setNormalIndices(0,normalIndices);
					iterator.remove();
				}
			};
			extractGeometryAttributes(node,nameList,setPositiveMap,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	public static IndexedLineStripArray attributesToIndexedLineStripArray(Term attributes, ChoisePoint iX) {
		return (IndexedLineStripArray)attributesToIndexedGeometryStripArray(GeometryStripArrayType.LINE,attributes,iX);
	}
	public static IndexedTriangleStripArray attributesToIndexedTriangleStripArray(Term attributes, ChoisePoint iX) {
		return (IndexedTriangleStripArray)attributesToIndexedGeometryStripArray(GeometryStripArrayType.TRIANGLE,attributes,iX);
	}
	public static IndexedTriangleFanArray attributesToIndexedTriangleFanArray(Term attributes, ChoisePoint iX) {
		return (IndexedTriangleFanArray)attributesToIndexedGeometryStripArray(GeometryStripArrayType.FAN,attributes,iX);
	}
	public static IndexedGeometryStripArray attributesToIndexedGeometryStripArray(GeometryStripArrayType type, Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			int vertexCount= 0; // Actor Prolog default value
			boolean includeVertexPositions= true; // Actor Prolog default value
			boolean includePerVertexNormals= false;
			int indexCount= 0; // Actor Prolog default value
			int[] stripIndexCounts= new int[0]; // Actor Prolog default value
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_vertexCount) {
					try {
						vertexCount= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_includeVertexPositions) {
					includeVertexPositions= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_includePerVertexNormals) {
					includePerVertexNormals= Converters.term2YesNo(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_indexCount) {
					try {
						indexCount= pairValue.getSmallIntegerValue(iX);
					} catch (TermIsNotAnInteger e) {
						throw new WrongArgumentIsNotAnInteger(pairValue);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_stripIndexCounts) {
					stripIndexCounts= term2Indices(pairValue,iX);
					iterator.remove();
				}
			};
			int vertexFormat= 0;
			if (includeVertexPositions) {
				vertexFormat |= GeometryArray.COORDINATES;
			};
			if (includePerVertexNormals) {
				vertexFormat |= GeometryArray.NORMALS;
			};
			IndexedGeometryStripArray node= null;
			switch (type) {
			case LINE:
				node= new IndexedLineStripArray(vertexCount,vertexFormat,indexCount,stripIndexCounts);
				break;
			case TRIANGLE:
				node= new IndexedTriangleStripArray(vertexCount,vertexFormat,indexCount,stripIndexCounts);
				break;
			case FAN:
				node= new IndexedTriangleFanArray(vertexCount,vertexFormat,indexCount,stripIndexCounts);
				break;
			};
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_coordinates) {
					Point3d[] coordinates= term2Coordinates(pairValue,iX);
					node.setCoordinates(0,coordinates);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_coordinateIndices) {
					int[] coordinateIndices= term2Indices(pairValue,iX);
					node.setCoordinateIndices(0,coordinateIndices);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_normals) {
					Vector3f[] normals= term2Normals(pairValue,iX);
					node.setNormals(0,normals);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_normalIndices) {
					int[] normalIndices= term2Indices(pairValue,iX);
					node.setNormalIndices(0,normalIndices);
					iterator.remove();
				}
			};
			extractGeometryAttributes(node,nameList,setPositiveMap,iX);
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	public static Text3D termToText3D(Term value, ChoisePoint iX) {
		try { // Text3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Text3D,1,iX);
			return attributesToText3D(arguments[0],iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAText3D(value);
		}
	}
	public static Text3D attributesToText3D(Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Font3D font= null;
			String text= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_font3D) {
					font= termToFont3D(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_string) {
					text= pairValue.toString(iX);
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownText3DAttribute(key);
				}
			};
			Text3D node;
			if (text != null) {
				if (font != null) {
					node= new Text3D(font,text);
				} else {
					throw new Font3DIsNotDefined();
				}
			} else {
				if (font != null) {
					node= new Text3D(font);
				} else {
					node= new Text3D();
				}
			};
			VerticalAlignment verticalAlignment= null;
			iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_position) {
					Point3f position= term2Coordinate3f(pairValue,iX);
					node.setPosition(position);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_horizontalAlignment) {
					int horizontalAlignment= term2Text3DHorizontalAlignment(pairValue,iX);
					node.setAlignment(horizontalAlignment);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_verticalAlignment) {
					verticalAlignment= term2Text3DVerticalAlignment(pairValue,iX);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_path) {
					int path= term2Text3DPath(pairValue,iX);
					node.setPath(path);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_characterSpacing) {
					float characterSpacing= (float)Converters.argumentToReal(pairValue,iX);
					node.setCharacterSpacing(characterSpacing);
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowAlignmentRead) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_ALIGNMENT_READ);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowAlignmentWrite) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_ALIGNMENT_WRITE);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowBoundingBoxRead) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_BOUNDING_BOX_READ);
					};
					iterator.remove();
				// } else if (pairName==SymbolCodes.symbolCode_E_allowBoundingBoxWrite) {
				//	boolean mode= Converters.term2YesNo(pairValue,iX);
				//	if (mode) {
				//		node.setCapability(Text3D.ALLOW_BOUNDING_BOX_WRITE);
				//	};
				//	iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowCharacterSpacingRead) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_CHARACTER_SPACING_READ);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowCharacterSpacingWrite) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_CHARACTER_SPACING_WRITE);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowFont3DRead) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_FONT3D_READ);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowFont3DWrite) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_FONT3D_WRITE);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowPathRead) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_PATH_READ);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowPathWrite) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_PATH_WRITE);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowPositionRead) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_POSITION_READ);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowPositionWrite) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_POSITION_WRITE);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowStringRead) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_STRING_READ);
					};
					iterator.remove();
				} else if (pairName==SymbolCodes.symbolCode_E_allowStringWrite) {
					boolean mode= Converters.term2YesNo(pairValue,iX);
					if (mode) {
						node.setCapability(Text3D.ALLOW_STRING_WRITE);
					};
					iterator.remove();
				// } else {
				//	throw new WrongArgumentIsUnknownText3DAttribute(key);
				}
			};
			extractGeometryAttributes(node,nameList,setPositiveMap,iX);
			if (verticalAlignment != null) {
				BoundingBox boundingBox= new BoundingBox();
				node.getBoundingBox(boundingBox);
				Point3f basePosition= new Point3f();
				node.getPosition(basePosition);
				Point3d lowerPoint= new Point3d();
				boundingBox.getLower(lowerPoint);
				Point3d upperPoint= new Point3d();
				boundingBox.getUpper(upperPoint);
				if (verticalAlignment==VerticalAlignment.CENTER) {
					node.setPosition(new Point3f(basePosition.x,(float)(basePosition.y-(upperPoint.y-lowerPoint.y)/2),basePosition.z));
				} else if (verticalAlignment==VerticalAlignment.TOP) {
					node.setPosition(new Point3f(basePosition.x,(float)(basePosition.y-(upperPoint.y-lowerPoint.y)),basePosition.z));
				// } else if (verticalAlignment==VerticalAlignment.BOTTOM) {
				}
			};
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	protected static int term2Text3DHorizontalAlignment(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_ALIGN_CENTER) {
				return Text3D.ALIGN_CENTER;
			} else if (flag==SymbolCodes.symbolCode_E_ALIGN_FIRST) {
				return Text3D.ALIGN_FIRST;
			} else if (flag==SymbolCodes.symbolCode_E_ALIGN_LAST) {
				return Text3D.ALIGN_LAST;
			} else {
				throw new WrongArgumentIsNotText3DHorizontalAlignment(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static VerticalAlignment term2Text3DVerticalAlignment(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_ALIGN_CENTER) {
				return VerticalAlignment.CENTER;
			} else if (flag==SymbolCodes.symbolCode_E_ALIGN_TOP) {
				return VerticalAlignment.TOP;
			} else if (flag==SymbolCodes.symbolCode_E_ALIGN_BOTTOM) {
				return VerticalAlignment.BOTTOM;
			} else {
				throw new WrongArgumentIsNotText3DVerticalAlignment(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static int term2Text3DPath(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_PATH_LEFT) {
				return Text3D.PATH_LEFT;
			} else if (flag==SymbolCodes.symbolCode_E_PATH_RIGHT) {
				return Text3D.PATH_RIGHT ;
			} else if (flag==SymbolCodes.symbolCode_E_PATH_UP) {
				return Text3D.PATH_UP;
			} else if (flag==SymbolCodes.symbolCode_E_PATH_DOWN) {
				return Text3D.PATH_DOWN;
			} else {
				throw new WrongArgumentIsNotText3DPath(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	public static Font3D termToFont3D(Term value, ChoisePoint iX) {
		try { // Font3D
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_Font3D,1,iX);
			return attributesToFont3D(arguments[0],iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotAFont3D(value);
		}
	}
	public static Font3D attributesToFont3D(Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			String fontName= null;
			Integer fontSize= null;
			Integer fontStyle= null;
			Double tessellationTolerance= null;
			FontExtrusion extrudePath= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_fontName) {
					try {
						fontName= GUI_Utils.termToFontNameSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_fontSize) {
					try {
						fontSize= GUI_Utils.termToFontSizeSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_fontStyle) {
					try {
						fontStyle= GUI_Utils.termToFontStyleSafe(pairValue,iX);
					} catch (TermIsSymbolDefault e) {
					}
				} else if (pairName==SymbolCodes.symbolCode_E_tessellationTolerance) {
					tessellationTolerance= Converters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_extrudePath) {
					extrudePath= termToFontExtrusion(pairValue,iX);
				} else {
					throw new WrongArgumentIsUnknownFont3DAttribute(key);
				}
			};
			if (fontName==null) {
				fontName= Font.SANS_SERIF; // Actor Prolog default value
			};
			if (fontSize==null) {
				fontSize= 1; // Actor Prolog default value
			};
			if (fontStyle==null) {
				fontStyle= Font.PLAIN; // Actor Prolog default value
			};
			// Passing null for the FontExtrusion parameter results
			// in no extrusion being done.
			// if (extrudePath==null) {
			//	extrudePath= new FontExtrusion(); // Actor Prolog default value
			// };
			Font3D node;
			Font font= new Font(fontName,fontStyle,fontSize);
			if (tessellationTolerance==null) {
				node= new Font3D(font,extrudePath);
			} else {
				node= new Font3D(font,tessellationTolerance,extrudePath);
			};
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	public static FontExtrusion termToFontExtrusion(Term value, ChoisePoint iX) {
		try { // FontExtrusion
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_FontExtrusion,1,iX);
			return attributesToFontExtrusion(arguments[0],iX);
		} catch (Backtracking b) {
			throw new WrongArgumentIsNotFontExtrusion(value);
		}
	}
	public static FontExtrusion attributesToFontExtrusion(Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Double depth= null;
			Double tessellationTolerance= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_depth) {
					depth= Converters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_tessellationTolerance) {
					tessellationTolerance= Converters.argumentToReal(pairValue,iX);
				} else {
					throw new WrongArgumentIsUnknownFontExtrusionAttribute(key);
				}
			};
			FontExtrusion node;
			Line2D.Double extrusionShape= null;
			if (depth != null) {
				extrusionShape= new Line2D.Double(0,0,depth,0);
			};
			if (tessellationTolerance==null) {
				node= new FontExtrusion(extrusionShape);
			} else {
				node= new FontExtrusion(extrusionShape,tessellationTolerance);
			};
			return node;
		} else {
			throw new WrongArgumentIsNotAttributeSet(setEnd);
		}
	}
	protected static void extractDiffuseColor(Material node, Term value, ChoisePoint iX) {
		try {
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_color4,4,iX);
			float r= (float)Converters.argumentToReal(arguments[0],iX);
			float g= (float)Converters.argumentToReal(arguments[1],iX);
			float b= (float)Converters.argumentToReal(arguments[2],iX);
			float a= (float)Converters.argumentToReal(arguments[3],iX);
			node.setDiffuseColor(r,g,b,a);
		} catch (Backtracking b) {
			Color3f color= term2Color3(value,iX);
			node.setDiffuseColor(color);
		}
	}
	protected static void extractGeometryAttributes(Geometry node, Set<Long> nameList, HashMap<Long,Term> setPositiveMap, ChoisePoint iX) {
		Iterator<Long> iterator= nameList.iterator();
		while(iterator.hasNext()) {
			long key= iterator.next();
			long pairName= - key;
			Term pairValue= setPositiveMap.get(key);
			if (pairName==SymbolCodes.symbolCode_E_allowIntersect) {
				boolean mode= Converters.term2YesNo(pairValue,iX);
				if (mode) {
					node.setCapability(Geometry.ALLOW_INTERSECT);
				}
			} else {
				throw new WrongArgumentIsUnknownGeometryAttribute(key);
			}
		}
	}
	protected static int termToPickingDetailLevel(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_INTERSECT_TEST) {
				return PickTool.INTERSECT_TEST;
			} else if (flag==SymbolCodes.symbolCode_E_INTERSECT_COORD) {
				return PickTool.INTERSECT_COORD;
			} else if (flag==SymbolCodes.symbolCode_E_INTERSECT_FULL) {
				return PickTool.INTERSECT_FULL;
			} else {
				throw new WrongArgumentIsNotPickingDetailLevel(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static int termToPickingMode(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_BOUNDS) {
				return PickTool.BOUNDS;
			} else if (flag==SymbolCodes.symbolCode_E_GEOMETRY) {
				return PickTool.GEOMETRY;
			} else if (flag==SymbolCodes.symbolCode_E_GEOMETRY_INTERSECT_INFO) {
				return PickTool.GEOMETRY_INTERSECT_INFO;
			} else {
				throw new WrongArgumentIsNotPickingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static ImageComponent2D termToImageComponent2D(Term value, Canvas3D targetWorld, ChoisePoint iX) {
		// try {
			// String fileName= value.getStringValue(iX);
			BufferedImage image= targetWorld.readImage(value,iX);
			TextureLoader textureLoader= new TextureLoader(image);
			return textureLoader.getImage();
		// } catch (TermIsNotAString e) {
		//	throw new WrongArgumentIsNotImageFileName(value);
		// }
	}
	protected static int termToImageScaleMode(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_SCALE_FIT_ALL) {
				return Background.SCALE_FIT_ALL;
			} else if (flag==SymbolCodes.symbolCode_E_SCALE_FIT_MAX) {
				return Background.SCALE_FIT_MAX;
			} else if (flag==SymbolCodes.symbolCode_E_SCALE_FIT_MIN) {
				return Background.SCALE_FIT_MIN;
			} else if (flag==SymbolCodes.symbolCode_E_SCALE_NONE) {
				return Background.SCALE_NONE;
			} else if (flag==SymbolCodes.symbolCode_E_SCALE_NONE_CENTER) {
				return Background.SCALE_NONE_CENTER;
			} else if (flag==SymbolCodes.symbolCode_E_SCALE_REPEAT) {
				return Background.SCALE_REPEAT;
			} else {
				throw new WrongArgumentIsNotImageScaleMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
	protected static int termToBillboardAlignmentMode(Term value, ChoisePoint iX) {
		try {
			long flag= value.getSymbolValue(iX);
			if (flag==SymbolCodes.symbolCode_E_ROTATE_ABOUT_AXIS) {
				return OrientedShape3D.ROTATE_ABOUT_AXIS;
			} else if (flag==SymbolCodes.symbolCode_E_ROTATE_ABOUT_POINT) {
				return OrientedShape3D.ROTATE_ABOUT_POINT;
			} else if (flag==SymbolCodes.symbolCode_E_ROTATE_NONE) {
				return OrientedShape3D.ROTATE_NONE;
			} else {
				throw new WrongArgumentIsNotBillboardAlignmentMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotASymbol(value);
		}
	}
}
