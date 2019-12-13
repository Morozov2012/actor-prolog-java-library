// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui;

public class CanvasSpaceAttributes {
	//
	protected boolean controlIsInitialized= false;
	//
	synchronized public boolean initializeControlIfNecessary() {
		if (!controlIsInitialized) {
			controlIsInitialized= true;
			return true;
		} else {
			return false;
		}
	}
	synchronized public boolean controlIsNotInitialized() {
		return !controlIsInitialized;
	}
}
