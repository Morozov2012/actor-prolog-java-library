// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.built_in.*;

import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.PointLight;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.Group;
import javax.media.j3d.Appearance;
import javax.media.j3d.Bounds;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.BoundingSphere;
import com.sun.j3d.utils.geometry.GeometryInfo;
import javax.vecmath.Vector4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Point4d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.SingularMatrixException;

import java.util.ArrayList;

public class MovingShadow extends Group {
	//
	public MovingShadow(Canvas3D targetWorld, NodeLabel dodecahedron, Node baseGroup, NodeLabel light, Vector4d plane, double standoff, Appearance appearance, Bounds bounds) {
		setCapability(Group.ALLOW_CHILDREN_WRITE);
		//
		Shape3D shadowShape= new Shape3D();
		BranchGroup branchGroup= new BranchGroup();
		branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		branchGroup.addChild(shadowShape);
		branchGroup.compile();
		addChild(branchGroup);
		//
		ShadowBehavior shadowBehavior= new ShadowBehavior(this,targetWorld,dodecahedron,baseGroup,light,plane,standoff,appearance);
		if (bounds==null) {
			bounds= new BoundingSphere();
		};
		shadowBehavior.setSchedulingBounds(bounds);
		addChild(shadowBehavior);
	}
	//
	protected static Shape3D createShadow(Shape3D dodecahedronShape, Node baseGroup, PointLight pointLight, Vector4d plane, double standoff, Appearance appearance) {
		ArrayList<TransformGroup> tr_list= new ArrayList<>();
		Node current_object= pointLight;
		while (true) {
			if (current_object==null) {
				break;
			};
			if (current_object==baseGroup) {
				break;
			};
			if (current_object instanceof TransformGroup) {
				TransformGroup currentGroup= (TransformGroup)current_object;
				tr_list.add(currentGroup);
			};
			current_object= current_object.getParent();
		};
		//
		Transform3D tr= new Transform3D();
		Transform3D rot= new Transform3D();
		for (int i=tr_list.size()-1; i >= 0; i--) {
			TransformGroup currentGroup= tr_list.get(i);
			currentGroup.getTransform(rot);
			tr.mul(rot);
		};
		//
		Point3f position= new Point3f();
		pointLight.getPosition(position);
		Transform3D localTransform= new Transform3D();
		localTransform.setTranslation(new Vector3d(position.x,position.y,position.z));
		tr.mul(localTransform);
		//
		Point3f lightPosition= new Point3f(0,0,0);
		tr.transform(lightPosition);
		//
		float[] planeCos= plane2Cos(plane);
		//
		float[] planePoint= translatePoint(planeCos,lightPosition);
		Transform3D shadowProj= createShadowProjection(lightPosition,new Point3f(planePoint),standoff);
		GeometryArray shadowGeom= createShadowGeometryArray(dodecahedronShape,baseGroup,shadowProj);
		//
		if (appearance==null) {
			appearance= new Appearance();
			ColoringAttributes colorAttr= new ColoringAttributes(0.1f,0.1f,0.1f,ColoringAttributes.FASTEST);
			appearance.setColoringAttributes(colorAttr);
			TransparencyAttributes transAttr= new TransparencyAttributes(TransparencyAttributes.BLENDED,0.90f);
			appearance.setTransparencyAttributes(transAttr);
			PolygonAttributes polyAttr= new PolygonAttributes();
			polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
			appearance.setPolygonAttributes(polyAttr);
		};
		//
		return new Shape3D(shadowGeom,appearance);
	}
	private static float[] plane2Cos(Vector4d plane) {
		//
		float[] result= new float[4];
		double A= plane.x;
		double B= plane.y;
		double C= plane.z;
		double D= plane.w;
		//
		double S= StrictMath.sqrt(A*A + B*B + C*C);
		double divisor= - StrictMath.signum(D) * S;
		//
		result[0]= (float) (A / divisor);
		result[1]= (float) (B / divisor);
		result[2]= (float) (C / divisor);
		result[3]= (float) (D / (StrictMath.signum(D) * S));
		//
		return result;
	}
	public static float[] translatePoint(float[] planeCos,Point3f lightPosition) {
		//
		float cos_alpha= planeCos[0];
		float cos_beta= planeCos[1];
		float cos_gamma= planeCos[2];
		float p= planeCos[3];
		float distance= StrictMath.abs(cos_alpha * lightPosition.x + cos_beta * lightPosition.y + cos_gamma * lightPosition.z - p);
		//
		float[] result= new float[3];
		result[0]= cos_alpha * distance + lightPosition.x;
		result[1]= cos_beta * distance + lightPosition.y;
		result[2]= cos_gamma * distance + lightPosition.z;
		//
		return result;
	}
	protected static Transform3D createShadowProjection(Point3f light, Point3f plane, double standoff) {
		Vector3f v= new Vector3f();
		v.sub(plane,light);
		//
		double[] mat= new double[16];
		for (int i= 0; i < 16; i++) {
			mat[i]= 0;
		}
		mat[0]= 1;
		mat[5]= 1;
		mat[10]= 1 - standoff;
		mat[14]= -1 / v.length();
		//
		Vector3d planeVector= new Vector3d(plane);
		Vector3d vup= new Vector3d(0,0,1);
		double angle= planeVector.angle(vup);
		double eps= Float.MIN_VALUE;
		if (StrictMath.abs(angle) < eps || StrictMath.abs(StrictMath.abs(angle)-StrictMath.PI) < eps) {
			vup= new Vector3d(0,1,0);
		};
		Transform3D shadowProj= new Transform3D();
		try {
			Transform3D proj= new Transform3D();
			proj.set(mat);
			//
			Transform3D u= new Transform3D();
			u.lookAt(new Point3d(light),new Point3d(plane),vup);
			proj.mul(u);
			//
			u.invert();
			shadowProj.mul(u,proj);
		} catch (SingularMatrixException e) {
			//
			Transform3D proj= new Transform3D();
			proj.set(mat);
			//
			Transform3D u= new Transform3D();
			u.lookAt(new Point3d(light),new Point3d(plane),new Vector3d(1,0,0));
			proj.mul(u);
			//
			u.invert();
			shadowProj.mul(u,proj);
		};
		return shadowProj;
	}
	protected static GeometryArray createShadowGeometryArray(Shape3D dodecahedronShape, Node baseGroup, Transform3D shadowProj) {
		GeometryArray ga= (GeometryArray)dodecahedronShape.getGeometry();
		GeometryInfo gi= new GeometryInfo(ga);
		gi.convertToIndexedTriangles();
		IndexedTriangleArray ita= (IndexedTriangleArray)gi.getIndexedGeometryArray();
		//
		int n= ita.getVertexCount();
		int count= ita.getIndexCount();
		//
		IndexedTriangleArray shadow= new IndexedTriangleArray(
			n,
			GeometryArray.COORDINATES,
			count);
		//
		int[] indices= new int[count];
		ita.getCoordinateIndices(0,indices);
		//
		shadow.setCoordinateIndices(0,indices);
		//
		ArrayList<TransformGroup> tr_list= new ArrayList<>();
		Node current_object= dodecahedronShape;
		while (true) {
			if (current_object==null) {
				break;
			};
			if (current_object==baseGroup) {
				break;
			};
			if (current_object instanceof TransformGroup) {
				TransformGroup currentGroup= (TransformGroup)current_object;
				tr_list.add(currentGroup);
			};
			current_object= current_object.getParent();
		};
		//
		Transform3D tr= new Transform3D(shadowProj);
		Transform3D rot= new Transform3D();
		for (int i=tr_list.size()-1; i >= 0; i--) {
			TransformGroup currentGroup= tr_list.get(i);
			currentGroup.getTransform(rot);
			tr.mul(rot);
		};
		//
		double[] vert= new double[n*3];
		Point3d currentPoint= new Point3d();
		for (int i= 0; i < n; i++) {
			ga.getCoordinate(i,currentPoint);
			Vector4d v4= new Vector4d(currentPoint);
			v4.w= 1;
			tr.transform(v4);
			Point4d p4= new Point4d(v4);
			currentPoint.project(p4);
			vert[3*i]= currentPoint.x;
			vert[3*i+1]= currentPoint.y;
			vert[3*i+2]= currentPoint.z;
		};
		shadow.setCoordinates(0,vert);
		return shadow;
	}
}
