// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

public class ComponentState {
	//
	protected boolean isProven= false;
	protected boolean isSuspended= false;
	//
	public ComponentState(boolean p, boolean s) {
		set(p,s);
	}
	//
	public void set(boolean p, boolean s) {
		synchronized (this) {
			isProven= p;
			isSuspended= s;
		}
	}
	public boolean equals(boolean p, boolean s) {
		synchronized (this) {
			if (isProven==p && isSuspended==s) {
				return true;
			} else {
				return false;
			}
		}
	}
	public boolean isProven() {
		synchronized (this) {
			return isProven;
		}
	}
	public boolean isSuspended() {
		synchronized (this) {
			return isSuspended;
		}
	}
}
