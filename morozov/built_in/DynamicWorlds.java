// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.indices.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

public abstract class DynamicWorlds extends LambdaArray {
	//
	protected HashMap<ArrayIndices,AbstractWorld> createdWorlds= new HashMap<>();
	protected ArrayList<AbstractInternalWorld> specialWorlds= null;
	//
	public DynamicWorlds() {
	}
	public DynamicWorlds(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	abstract public Term getBuiltInSlot_E_prototype();
	//
	@Override
	protected Term accessArrayElement(ArrayIndices arrayIndices, ChoisePoint cp) throws Backtracking {
		AbstractWorld staticValue= createdWorlds.get(arrayIndices);
		SlotVariable dynamicValue;
		if (staticValue==null) {
			// Create and initialize new world:
			PrologArray array= (PrologArray)getBuiltInSlot_E_prototype();
			staticValue= array.createWorld();
			array.initiateWorld(staticValue);
			if (staticValue.isSpecialWorld()) {
				if (specialWorlds==null) {
					specialWorlds= new ArrayList<>();
				};
				AbstractInternalWorld internalWorld= (AbstractInternalWorld)staticValue;
				specialWorlds.add(internalWorld);
			};
			staticValue.startProcesses();
			// Remember new world:
			createdWorlds.put(arrayIndices,staticValue);
			// Create new backtrackable array item:
			dynamicValue= new SlotVariable();
			volume.put(arrayIndices,dynamicValue);
			cp.pushTrail(new HashMapState(volume,arrayIndices,dynamicValue));
		} else {
			dynamicValue= volume.get(arrayIndices);
			if (dynamicValue==null) {
				// Create new backtrackable array item:
				dynamicValue= new SlotVariable();
				volume.put(arrayIndices,dynamicValue);
				cp.pushTrail(new HashMapState(volume,arrayIndices,dynamicValue));
			}
		};
		// Unify array item:
		dynamicValue.unifyWith(staticValue,cp);
		return staticValue;
	}
	//
	@Override
	public void startProcesses() {
		Collection<AbstractWorld> allWorlds= createdWorlds.values();
		Iterator<AbstractWorld> allWorldsIterator= allWorlds.iterator();
		while(allWorldsIterator.hasNext()) {
			AbstractWorld world= allWorldsIterator.next();
			world.startProcesses();
		}
	}
	//
	@Override
	public void releaseSystemResources() {
		Collection<AbstractWorld> allWorlds= createdWorlds.values();
		Iterator<AbstractWorld> allWorldsIterator= allWorlds.iterator();
		while(allWorldsIterator.hasNext()) {
			AbstractWorld world= allWorldsIterator.next();
			world.releaseSystemResources();
		};
		super.releaseSystemResources();
	}
	//
	@Override
	public void stopProcesses() {
		Collection<AbstractWorld> allWorlds= createdWorlds.values();
		Iterator<AbstractWorld> allWorldsIterator= allWorlds.iterator();
		while(allWorldsIterator.hasNext()) {
			AbstractWorld world= allWorldsIterator.next();
			world.stopProcesses();
		}
	}
	//
	@Override
	public boolean isSpecialWorld() {
		return true;
	}
	//
	@Override
	public void finishPhaseSuccessfully() {
		if (specialWorlds != null) {
			Iterator<AbstractInternalWorld> iterator= specialWorlds.iterator();
			while (iterator.hasNext()) {
				AbstractInternalWorld specialWorld= iterator.next();
				specialWorld.finishPhaseSuccessfully();
			}
		}
	}
	//
	@Override
	public void finishPhaseUnsuccessfully() {
		if (specialWorlds != null) {
			Iterator<AbstractInternalWorld> iterator= specialWorlds.iterator();
			while (iterator.hasNext()) {
				AbstractInternalWorld specialWorld= iterator.next();
				specialWorld.finishPhaseUnsuccessfully();
			}
		}
	}
}
