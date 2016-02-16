// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.worlds;

import morozov.run.*;
import morozov.system.*;
import morozov.system.datum.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractWorld extends ActorNumber {
	//
	protected static AtomicLong globalWorldCounter= new AtomicLong(0);
	protected transient GlobalWorldIdentifier globalWorldIdentifier= null;
	//
	final static protected MethodSignature[] emptySignatureList= new MethodSignature[0];
	//
	///////////////////////////////////////////////////////////////
	//
	public AbstractWorld() {
		globalWorldIdentifier= new GlobalWorldIdentifier(globalWorldCounter.incrementAndGet());
	}
	public AbstractWorld(GlobalWorldIdentifier id) {
		globalWorldIdentifier= id;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getInternalWorldClass(AbstractWorld currentWorld, ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw Backtracking.instance;
	}
	//
	abstract public AbstractInternalWorld internalWorld();
	//
	abstract public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList);
	abstract public void withdrawRequest(Resident resident);
	//
	abstract public void receiveAsyncCall(AsyncCall item);
	abstract public boolean isInternalWorldOf(AbstractProcess currentProcess);
	abstract public void startProcesses();
	abstract public void closeFiles();
	abstract public void stopProcesses();
	abstract public MethodSignature[] getMethodSignatures();
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		worlds.add(this);
	}
	//
	public Term substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term value= map.get(this);
		if (value != null) {
			return value;
		} else {
			return PrologUnknownValue.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void transmitAsyncCall(AsyncCall newItem, ChoisePoint iX) {
		try {
			AbstractWorld receiver= newItem.computeHashCodeAndCopyArguments(iX);
			receiver.receiveAsyncCall(newItem);
		} catch (TermIsDummyWorld e) {
		} catch (TermIsUnboundVariable e) {
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int hashCode() {
		return globalWorldIdentifier.hashCode();
	}
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToWorld(globalWorldIdentifier);
		} else {
			return false;
		}
	}
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithWorld(globalWorldIdentifier);
		} else {
			return 1;
		}
	}
	public boolean isEqualToWorld(GlobalWorldIdentifier id2) {
		return globalWorldIdentifier.equals(id2);
	}
	public int compareWithWorld(GlobalWorldIdentifier id2) {
		return globalWorldIdentifier.compare(id2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public GlobalWorldIdentifier getGlobalWorldIdentifier() {
		return globalWorldIdentifier;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isWorld(Term v, ChoisePoint cp) throws Backtracking {
		if (this != v) {
			throw Backtracking.instance;
		}
	}
	public boolean thisIsOwnWorld() {
		return true;
	}
	public boolean isSpecialWorld() {
		return false;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isWorld(this,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (encodeWorlds) {
			ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
			try {
				ObjectOutputStream objectStream= new DataStoreOutputStream(outputStream);
				try {
					objectStream.writeObject(this);
				} finally {
					objectStream.close();
				}
			} catch (IOException e) {
				throw new DataSerializingError(e);
			};
			byte[] byteArray= outputStream.toByteArray();
			String outputString= Converters.byteArray2String(byteArray);
			return "('" + outputString + "')";
		} else {
			return super.toString(cp,isInner,provideStrictSyntax,encodeWorlds,encoder);
		}
	}
}
