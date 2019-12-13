/*
 * @(#)ActorPrologWindowsDesktopPaneUI.java 2012/01/07
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.windows;

import com.sun.java.swing.plaf.windows.WindowsDesktopPaneUI;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class ActorPrologWindowsDesktopPaneUI extends WindowsDesktopPaneUI {
	//
	public static ComponentUI createUI(JComponent c) {
		return new ActorPrologWindowsDesktopPaneUI();
	}
	//
	@Override
	protected void installDesktopManager() {
		desktopManager= desktop.getDesktopManager();
		if(desktopManager == null) {
			desktopManager= new ActorPrologWindowsDesktopManager();
			desktop.setDesktopManager(desktopManager);
		}
	}
	@Override
	protected void installDefaults() {
		super.installDefaults();
	}
}
