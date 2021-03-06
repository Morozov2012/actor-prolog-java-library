// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.system.gui.space3d.errors.*;

import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.PointLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Text3D;
import javax.media.j3d.Font3D;
import javax.media.j3d.Geometry;
import javax.vecmath.Vector3d;
import com.sun.j3d.utils.geometry.Primitive;

public class NodeContainer {
	//
	protected TransformGroup transformGroup;
	protected Group group;
	protected Node node;
	protected ContentType type;
	//
	public NodeContainer(TransformGroup object) {
		transformGroup= object;
		type= ContentType.TransformGroup;
	}
	public NodeContainer(Group object) {
		group= object;
		type= ContentType.Group;
	}
	public NodeContainer(Node object) {
		node= object;
		type= ContentType.Node;
	}
	//
	public Node getNode() {
		if (type==ContentType.TransformGroup) {
			return transformGroup;
		} else if (type==ContentType.Group) {
			return group;
		} else if (type==ContentType.Node) {
			return node;
		} else {
			throw new OperationIsNotDefinedForThisTypeOfNode(type);
		}
	}
	//
	public Node getParent() {
		if (type==ContentType.TransformGroup) {
			return transformGroup.getParent();
		} else if (type==ContentType.Group) {
			return group.getParent();
		} else if (type==ContentType.Node) {
			return node.getParent();
		} else {
			throw new OperationIsNotDefinedForThisTypeOfNode(type);
		}
	}
	//
	public void setTransform(Transform3D transform) {
		if (type==ContentType.TransformGroup) {
			transformGroup.setTransform(transform);
		} else {
			throw new OperationIsNotDefinedForThisTypeOfNode(type);
		}
	}
	public void setTranslation(Vector3d vector) {
		if (type==ContentType.TransformGroup) {
			Transform3D transform= new Transform3D();
			transformGroup.getTransform(transform);
			transform.setTranslation(vector);
			setTransform(transform);
		} else {
			throw new OperationIsNotDefinedForThisTypeOfNode(type);
		}
	}
	public void setAppearance(Appearance appearance) {
		if (type==ContentType.Node) {
			if (node instanceof Shape3D) {
				((Shape3D)node).setAppearance(appearance);
			} else if (node instanceof Primitive) {
				((Primitive)node).setAppearance(appearance);
			}
		}
	}
	public void setColoringAttributes(ColoringAttributes coloringAttributes) {
		if (type==ContentType.Node) {
			if (node instanceof Shape3D) {
				Shape3D shape= (Shape3D)node;
				Appearance appearance= shape.getAppearance();
				appearance.setColoringAttributes(coloringAttributes);
			} else if (node instanceof Primitive) {
				Primitive shape= (Primitive)node;
				Appearance appearance= shape.getAppearance();
				appearance.setColoringAttributes(coloringAttributes);
			}
		}
	}
	public void setFont3D(Font3D font) {
		if (type==ContentType.Node) {
			if (node instanceof Shape3D) {
				Geometry geometry= ((Shape3D)node).getGeometry();
				if (geometry instanceof Text3D) {
					((Text3D)geometry).setFont3D(font);
				}
			}
		}
	}
	public void setString(String text) {
		if (type==ContentType.Node) {
			if (node instanceof Shape3D) {
				Geometry geometry= ((Shape3D)node).getGeometry();
				if (geometry instanceof Text3D) {
					((Text3D)geometry).setString(text);
				}
			}
		}
	}
	//
	public Shape3D retrieveShape3D(NodeLabel label) {
		if (type==ContentType.Node) {
			if (node instanceof Shape3D) {
				return (Shape3D)node;
			} else {
				throw new LabeledObjectIsNotAShape3D(label);
			}
		} else {
			throw new LabeledObjectIsNotAShape3D(label);
		}
	}
	//
	public PointLight retrievePointLight(NodeLabel label) {
		if (type==ContentType.Node) {
			if (node instanceof PointLight) {
				return (PointLight)node;
			} else {
				throw new LabeledObjectIsNotPointLight(label);
			}
		} else {
			throw new LabeledObjectIsNotPointLight(label);
		}
	}
}
