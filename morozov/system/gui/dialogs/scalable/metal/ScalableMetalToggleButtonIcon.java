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
	public ScalableMetalToggleButtonIcon() {
		gradient= (java.util.List)UIManager.get(getGradientKey());
	}
	//
	protected abstract String getGradientKey();
}
