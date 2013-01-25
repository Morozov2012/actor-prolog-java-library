/*
 * @(#)ActorPrologMetalLookAndFeel.java 2009/12/11
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.metal;

import javax.swing.*;
import javax.swing.plaf.metal.*;

public class ActorPrologMetalLookAndFeel extends MetalLookAndFeel {
	static final String metalPackageName= "morozov.system.gui.dialogs.scalable.metal.";
	// Return an identifier for this class
	public String getID() {
		return "ActorPrologMetal";
	}
	// Return the name of our look-and-feel
	public String getName() {
		return "Actor Prolog Metal";
	}
	// Return a description of the LAF
	public String getDescription() {
		return "Actor Prolog Metal Look and Feel";
	}
	// Our LAF is not native
	public boolean isNativeLookAndFeel() {
		return false;
	}
	// Our LAF is always supported
	public boolean isSupportedLookAndFeel() {
		return true;
	}
	protected void initClassDefaults(UIDefaults table) {
		// Dummy creation of an instance of class:
		ScalableMetalCheckBoxUI c1= new ScalableMetalCheckBoxUI();
		ScalableMetalRadioButtonUI c2= new ScalableMetalRadioButtonUI();
		ScalableMetalComboBoxUI c3= new ScalableMetalComboBoxUI();
		super.initClassDefaults(table);
		Object[] uiDefaults= {
			"CheckBoxUI", metalPackageName + "ScalableMetalCheckBoxUI",
			"RadioButtonUI", metalPackageName + "ScalableMetalRadioButtonUI",
			"ComboBoxUI", metalPackageName + "ScalableMetalComboBoxUI"
		};
		table.putDefaults(uiDefaults);
	}
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);
		Object checkBoxIconLazyValue= new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				return new ScalableMetalCheckBoxIcon();
			}
		};
		Object radioButtonIconLazyValue= new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				return new ScalableMetalRadioButtonIcon();
			}
		};
		Object[] defaults= {
			"CheckBox.icon", checkBoxIconLazyValue,
			"RadioButton.icon", radioButtonIconLazyValue
			};
		table.putDefaults(defaults);
	}
}
