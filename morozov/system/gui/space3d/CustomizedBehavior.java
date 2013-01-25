// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import morozov.built_in.*;
import morozov.classes.*;
import morozov.terms.*;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;

public class CustomizedBehavior extends Behavior {
	//
	protected Canvas3D targetWorld= null;
	protected BehaviorName name;
	protected WakeupCondition wakeupCondition;
	//
	public CustomizedBehavior(Canvas3D target) {
		targetWorld= target;
	}
	//
	public void setName(BehaviorName n) {
		name= n;
	}
	//
	public void setWakeupCondition(WakeupCondition condition) {
		wakeupCondition= condition;
	}
	//
	public void initialize() {
		wakeupOn(wakeupCondition);
	}
	//
	public void processStimulus(Enumeration criteria) {
		// System.out.printf("CustomizedBehavior::processStimulus(%s)\n",criteria);
		long domainSignature= targetWorld.entry_s_Action_1_i();
		Term predicateArgument= name.toTerm();
		Term[] arguments= new Term[]{predicateArgument};
		AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
		targetWorld.receiveAsyncCall(call);
		wakeupOn(wakeupCondition);
	}
}
