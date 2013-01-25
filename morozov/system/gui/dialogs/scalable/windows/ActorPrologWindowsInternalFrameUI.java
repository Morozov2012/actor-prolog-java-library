/*
 * @(#)ActorPrologWindowsInternalFrameUI.java 2012/01/07
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.windows;

import com.sun.java.swing.plaf.windows.WindowsInternalFrameUI;
import javax.swing.DesktopManager;
import javax.swing.JInternalFrame;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class ActorPrologWindowsInternalFrameUI extends WindowsInternalFrameUI {
	public static ComponentUI createUI(JComponent b) {
		return new ActorPrologWindowsInternalFrameUI((JInternalFrame)b);
	}
	public ActorPrologWindowsInternalFrameUI(JInternalFrame w) {
		super(w);
	}
	protected DesktopManager createDesktopManager() {
		return new ActorPrologWindowsDesktopManager();
	}
}
