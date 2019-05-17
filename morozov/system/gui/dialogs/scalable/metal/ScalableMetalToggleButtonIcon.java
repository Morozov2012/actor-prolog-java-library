/*
 * @(#)ScalableMetalToggleButtonIcon.java 1.0 2009/11/13
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.metal;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.UIManager;

/*
 * ScalableMetalToggleButtonIcon implementation for the Actor Prolog language
 * @version 1.0 2009/11/13
 * @author IRE RAS Alexei A. Morozov
*/

public abstract class ScalableMetalToggleButtonIcon extends ScalableToggleButtonIcon {
	//
	protected java.util.List gradient= null;
	//
	private static final long serialVersionUID= 0x853F5D9FF4934E03L; // -8845248201547887101L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.gui.dialogs.scalable.metal","ScalableMetalToggleButtonIcon");
	// }
	//
	public ScalableMetalToggleButtonIcon() {
		gradient= (java.util.List)UIManager.get(getGradientKey());
	}
	//
	abstract protected String getGradientKey();
}
