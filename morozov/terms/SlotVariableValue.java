// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.worlds.*;

import java.io.Serializable;
import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class SlotVariableValue implements Cloneable, Serializable {
	//
	public ActiveWorld slotValueOwner;
	public Term actualValue;
	public Term recentValue;
	public HashSet<ActorNumber> oldActors= new HashSet<>();
	public HashSet<ActorNumber> newActors= new HashSet<>();
	public boolean isSuspendingPort= false;
	public boolean isProtectingPort= false;
	public boolean portIsUpdated= false;
	public Term portValue;
	public boolean portValueIsProtected= false;
	public ActiveWorld portValueOwner;
	//
	private static final long serialVersionUID= 0xB7D96DBBA0C684CFL; // -5199003642198850353L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","SlotVariableValue");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SlotVariableValue(ActiveWorld owner) {
		slotValueOwner= owner;
	}
	public SlotVariableValue(ActiveWorld owner, boolean flag1, boolean flag2) {
		slotValueOwner= owner;
		isSuspendingPort= flag1;
		isProtectingPort= flag2;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int hashCode() {
		if (actualValue==null) {
			// The hash code for the null
			// reference is zero:
			return 0;
		} else {
			return actualValue.hashCode();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isPlainPort() {
		return !isProtectingPort && !isSuspendingPort;
	}
	public boolean isProtectingPort() {
		return isProtectingPort && !isSuspendingPort;
	}
	public boolean isSuspendingPort() {
		return isSuspendingPort;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (cp==null) {
			return toString();
		} else {
			if (actualValue==null) {
				return null;
			} else {
				return	actualValue.toString(cp,isInner,provideStrictSyntax,encodeWorlds,encoder) +
					isSuspendingPort +
					isProtectingPort +
					newActors.toString();
			}
		}
	}
	@Override
	public String toString() {
		return	"\n{\n" +
			"slotValueOwner:" + slotValueOwnerText() + ";\n" +
			"recentValue:" + recentValueText() + ";\n" +
			"actualValue:" + actualValueText() + ";\n" +
			"oldActors:" + ActorNumber.toCompactString(oldActors) + ";\n" +
			"newActors:" + ActorNumber.toCompactString(newActors) + ";\n" +
			"isSuspendingPort:" + isSuspendingPort + ";\n" +
			"isProtectingPort:" + isProtectingPort + ";\n" +
			"portIsUpdated:" + portIsUpdated + ";\n" +
			"portValue:" + portValueText() + ";\n" +
			"portValueIsProtected:" + portValueIsProtected + ";\n" +
			"portValueOwner:" + portValueOwnerText() + "\n}\n";
	}
	protected String slotValueOwnerText() {
		if (slotValueOwner==null) {
			return "null";
		} else {
			return slotValueOwner.toString();
		}
	}
	protected String actualValueText() {
		if (actualValue==null) {
			return "null";
		} else {
			return actualValue.toString();
		}
	}
	protected String recentValueText() {
		if (recentValue==null) {
			return "null";
		} else {
			return recentValue.toString();
		}
	}
	protected String portValueText() {
		if (portValue==null) {
			return "null";
		} else {
			return portValue.toString();
		}
	}
	protected String portValueOwnerText() {
		if (portValueOwner==null) {
			return "null";
		} else {
			return portValueOwner.toString();
		}
	}
}
