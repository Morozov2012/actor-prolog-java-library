/*
 * @(#)ActorPrologWindowsLookAndFeel.java 2009/12/27
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.windows;

import javax.swing.*;
import com.sun.java.swing.plaf.windows.*;

public class ActorPrologWindowsLookAndFeel extends WindowsLookAndFeel {
	//
	static final String windowsPackageName= "morozov.system.gui.dialogs.scalable.windows.";
	// Return an identifier for this class:
	@Override
	public String getID() {
		return "ActorPrologWindows";
	}
	// Return the name of our look-and-feel:
	@Override
	public String getName() {
		return "Actor Prolog Windows";
	}
	// Return a description of the LAF:
	@Override
	public String getDescription() {
		return "Actor Prolog Windows Look and Feel";
	}
	// Our LAF is not native:
	// public boolean isNativeLookAndFeel() {
	//	return false;
	// }
	// Our LAF is always supported:
	// public boolean isSupportedLookAndFeel() {
	//	return true;
	// }
	@Override
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);
		Object[] uiDefaults= {
			"CheckBoxUI", windowsPackageName + "ScalableWindowsCheckBoxUI",
			"RadioButtonUI", windowsPackageName + "ScalableWindowsRadioButtonUI",
			"ComboBoxUI", windowsPackageName + "ScalableWindowsComboBoxUI",
			"InternalFrameUI", windowsPackageName + "ActorPrologWindowsInternalFrameUI",
			"DesktopPaneUI", windowsPackageName + "ActorPrologWindowsDesktopPaneUI"
		};
		table.putDefaults(uiDefaults);
	}
	private static void dummyInitClassDefaults(UIDefaults table) {
		// Dummy creation of an instance of class:
		ScalableWindowsCheckBoxUI c1= new ScalableWindowsCheckBoxUI();
		ScalableWindowsRadioButtonUI c2= new ScalableWindowsRadioButtonUI();
		ScalableWindowsComboBoxUI c3= new ScalableWindowsComboBoxUI();
		ActorPrologWindowsInternalFrameUI c4= new ActorPrologWindowsInternalFrameUI(null);
		ActorPrologWindowsDesktopPaneUI c5= new ActorPrologWindowsDesktopPaneUI();
	}
	@Override
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);
		Object checkBoxIconLazyValue= new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new ScalableWindowsCheckBoxIcon();
			}
		};
		Object radioButtonIconLazyValue= new UIDefaults.LazyValue() {
			@Override
			public Object createValue(UIDefaults table) {
				return new ScalableWindowsRadioButtonIcon();
			}
		};
		Object[] defaults= {
			"CheckBox.icon", checkBoxIconLazyValue,
			"RadioButton.icon", radioButtonIconLazyValue
			};
		table.putDefaults(defaults);
	}
}
