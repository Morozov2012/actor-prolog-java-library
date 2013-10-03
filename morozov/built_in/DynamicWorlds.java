// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.indices.*;
import morozov.terms.*;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

public abstract class DynamicWorlds extends LambdaArray {
	//
	protected HashMap<ArrayIndices,AbstractWorld> createdWorlds= new HashMap<ArrayIndices,AbstractWorld>();
	// protected Map<ArrayIndices,AbstractWorld> createdWorlds= Collections.synchronizedMap(new HashMap<ArrayIndices,AbstractWorld>());
	protected ArrayList<AbstractWorld> specialWorlds= null;
	//
	abstract public Term getBuiltInSlot_E_prototype();
	//
	protected Term accessArrayElement(ArrayIndices arrayIndices, ChoisePoint cp) throws Backtracking {
		// System.out.printf("ArrayIndices:: createdWorlds(%s) ?\n\n",arrayIndices);
		AbstractWorld staticValue= createdWorlds.get(arrayIndices);
		// System.out.printf("ArrayIndices:: createdWorlds(%s) == %s\n\n",arrayIndices,staticValue);
		SlotVariable dynamicValue;
		if (staticValue==null) {
			// Create and initialize new world
			PrologArray array= (PrologArray)getBuiltInSlot_E_prototype();
			staticValue= array.createWorld();
			array.initiateWorld(staticValue);
			if (staticValue.isSpecialWorld()) {
				if (specialWorlds==null) {
					specialWorlds= new ArrayList<AbstractWorld>();
				};
				specialWorlds.add(staticValue);
			};
			staticValue.startProcesses();
			// Remember new world
			// System.out.printf("ArrayIndices:: createdWorlds(%s):=%s\n\n",arrayIndices,staticValue);
			createdWorlds.put(arrayIndices,staticValue);
			// Create new backtrackable array item
			dynamicValue= new SlotVariable();
			// System.out.printf("ArrayIndices(2):: volume(%s):=%s\n\n",arrayIndices,dynamicValue);
			volume.put(arrayIndices,dynamicValue);
			cp.pushTrail(new HashMapState(volume,arrayIndices,dynamicValue));
		} else {
			// System.out.printf("ArrayIndices:: volume(%s) ?\n\n",arrayIndices);
			dynamicValue= volume.get(arrayIndices);
			// System.out.printf("ArrayIndices:: volume(%s) == %s\n\n",arrayIndices,dynamicValue);
			if (dynamicValue==null) {
				// Create new backtrackable array item
				dynamicValue= new SlotVariable();
				// System.out.printf("ArrayIndices(3):: volume(%s):=%s\n\n",arrayIndices,dynamicValue);
				volume.put(arrayIndices,dynamicValue);
				cp.pushTrail(new HashMapState(volume,arrayIndices,dynamicValue));
			}
		};
		// Unify array item
		dynamicValue.unifyWith(staticValue,cp);
		// System.out.printf("ArrayItem: value=%s\n",dynamicValue);
		return staticValue;
	}
	//
	public void startProcesses() {
		Collection<AbstractWorld> allWorlds= createdWorlds.values();
		Iterator<AbstractWorld> allWorldsIterator= allWorlds.iterator();
		while(allWorldsIterator.hasNext()) {
			AbstractWorld world= allWorldsIterator.next();
			world.startProcesses();
		}
	}
	public void closeFiles() {
		Collection<AbstractWorld> allWorlds= createdWorlds.values();
		Iterator<AbstractWorld> allWorldsIterator= allWorlds.iterator();
		while(allWorldsIterator.hasNext()) {
			AbstractWorld world= allWorldsIterator.next();
			world.closeFiles();
		};
		super.closeFiles();
	}
	public void stopProcesses() {
		Collection<AbstractWorld> allWorlds= createdWorlds.values();
		Iterator<AbstractWorld> allWorldsIterator= allWorlds.iterator();
		while(allWorldsIterator.hasNext()) {
			AbstractWorld world= allWorldsIterator.next();
			world.stopProcesses();
		}
	}
	//
	public boolean isSpecialWorld() {
		return true;
	}
	//
	public void finishPhaseSuccessfully() {
		if (specialWorlds != null) {
			Iterator<AbstractWorld> iterator= specialWorlds.iterator();
			while (iterator.hasNext()) {
				AbstractWorld specialWorld= iterator.next();
				specialWorld.finishPhaseSuccessfully();
			}
		}
	}
	//
	public void finishPhaseUnsuccessfully() {
		if (specialWorlds != null) {
			Iterator<AbstractWorld> iterator= specialWorlds.iterator();
			while (iterator.hasNext()) {
				AbstractWorld specialWorld= iterator.next();
				specialWorld.finishPhaseUnsuccessfully();
			}
		}
	}
}
