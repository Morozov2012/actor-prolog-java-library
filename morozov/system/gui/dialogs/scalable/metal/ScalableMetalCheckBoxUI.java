/*
 * @(#)ScalableMetalCheckBoxUI.java 1.0 2010/08/26
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.metal;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.AbstractButton;

import javax.swing.plaf.metal.MetalCheckBoxUI;

public class ScalableMetalCheckBoxUI extends MetalCheckBoxUI {
	//
	protected boolean icon_is_copied= false;
	//
	public static ComponentUI createUI(JComponent c) {
		return new ScalableMetalCheckBoxUI();
	}
	//
	@Override
	public void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		if(!icon_is_copied) {
			if (icon instanceof ScalableToggleButtonIcon) {
				icon= ((ScalableToggleButtonIcon)icon).makeCopy();
			};
			icon_is_copied= true;
		}
	}
	@Override
	public void uninstallDefaults(AbstractButton b) {
		super.uninstallDefaults(b);
		icon_is_copied= false;
	}
}
