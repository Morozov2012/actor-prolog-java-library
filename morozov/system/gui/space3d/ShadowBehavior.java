// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.built_in.*;

import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.PointLight;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Appearance;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.Behavior;
import javax.vecmath.Vector4d;
import java.util.Enumeration;

public class ShadowBehavior extends Behavior {
	//
	protected MovingShadow shadowGroup;
	//
	protected Canvas3D targetWorld;
	//
	protected NodeLabel dodecahedronLabel;
	protected Shape3D dodecahedronShape;
	//
	protected NodeLabel lightLabel;
	protected PointLight pointLight;
	//
	protected Vector4d plane;
	protected Node baseGroup;
	protected double standoff;
	protected Appearance appearance;
	protected WakeupOnElapsedFrames wakeup= new WakeupOnElapsedFrames(0);
	//
	public ShadowBehavior(MovingShadow group, Canvas3D world, NodeLabel dodecahedron, Node base, NodeLabel light, Vector4d p, double s, Appearance a) {
		shadowGroup= group;
		targetWorld= world;
		dodecahedronLabel= dodecahedron;
		baseGroup= base;
		lightLabel= light;
		plane= p;
		standoff= s;
		appearance= a;
	}
	//
	@Override
	public void initialize() {
		dodecahedronShape= targetWorld.retrieveShape3D(dodecahedronLabel);
		pointLight= targetWorld.retrievePointLight(lightLabel);
		wakeupOn(wakeup);
	}
	@Override
	public void processStimulus(Enumeration enumeration) {
		Shape3D shadowShape= MovingShadow.createShadow(dodecahedronShape,baseGroup,pointLight,plane,standoff,appearance);
		BranchGroup newBG= new BranchGroup();
		newBG.setCapability(BranchGroup.ALLOW_DETACH);
		newBG.addChild(shadowShape);
		newBG.compile();
		shadowGroup.setChild(newBG,0);
		wakeupOn(wakeup);
	}
}
