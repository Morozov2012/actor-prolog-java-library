/*
 * @(#)LookAndFeelUtils.java 1.0 2010/01/05
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui;

import morozov.system.gui.dialogs.scalable.metal.*;
import morozov.system.gui.dialogs.scalable.windows.*;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LookAndFeelUtils {
	static public synchronized void assignLookAndFeel() {
		boolean useMetalLookAndFeel= false;
		String osName= System.getProperty("os.name");
		// if (false && (osName != null) && (osName.indexOf("Windows") != -1)) {
		if ((osName != null) && (osName.indexOf("Windows") != -1)) {
			try {
				UIManager.setLookAndFeel(new ActorPrologWindowsLookAndFeel());
			} catch (UnsupportedLookAndFeelException e) {
				useMetalLookAndFeel= true;
			}
		} else {
			useMetalLookAndFeel= true;
		};
		// useMetalLookAndFeel= true; // DEBUG: 2011.01.21
		if (useMetalLookAndFeel) {
			try {
				UIManager.setLookAndFeel(new ActorPrologMetalLookAndFeel());
			} catch (UnsupportedLookAndFeelException e) {
				throw new UnsupportedLookAndFeelError();
			}
		}
	}
}
