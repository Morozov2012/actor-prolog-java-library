// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class SlotVariableValue {
	//
	public ActiveWorld slotValueOwner;
	// public Term visibleValue; // oldValue
	public Term actualValue; // newValue
	public Term recentValue; // oldValue
	public HashSet<ActorNumber> oldActors= new HashSet<ActorNumber>();
	public HashSet<ActorNumber> newActors= new HashSet<ActorNumber>();
	public boolean isSuspendingPort= false;
	public boolean isProtectingPort= false;
	public boolean portIsUpdated= false;
	public Term portValue;
	public boolean portValueIsProtected= false;
	public ActiveWorld portValueOwner;
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
	public int hashCode() {
		if (actualValue==null) {
			// The hash code for the null reference is zero.
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
	private String slotValueOwnerText() {
		if (slotValueOwner==null) {
			return "null";
		} else {
			return slotValueOwner.toString();
		}
	}
	private String actualValueText() {
		if (actualValue==null) {
			return "null";
		} else {
			return actualValue.toString();
		}
	}
	private String recentValueText() {
		if (recentValue==null) {
			return "null";
		} else {
			return recentValue.toString();
		}
	}
	private String portValueText() {
		if (portValue==null) {
			return "null";
		} else {
			return portValue.toString();
		}
	}
	private String portValueOwnerText() {
		if (portValueOwner==null) {
			return "null";
		} else {
			return portValueOwner.toString();
		}
	}
}
