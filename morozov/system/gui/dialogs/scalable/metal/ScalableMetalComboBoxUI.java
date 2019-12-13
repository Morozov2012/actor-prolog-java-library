/*
 * @(#)ScalableMetalComboBoxUI.java 1.0 2009/11/23
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.metal;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.UIManager;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;
import javax.swing.JComponent;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.ComponentUI;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Component;
import java.awt.Color;

/*
 * ScalableMetalComboBoxUI implementation for the Actor Prolog language
 * @version 1.0 2009/11/23
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableMetalComboBoxUI extends MetalComboBoxUI implements ScalableComboBoxUI {
	//
	public static ComponentUI createUI(JComponent c) {
		return new ScalableMetalComboBoxUI();
	}
	//
	@Override
	protected void installDefaults() {
		UIManager.put("ComboBox.squareButton",true);
		super.installDefaults();
	}
	//
	@SuppressWarnings("unchecked")
	@Override
	protected Dimension getDisplaySize() {
		ListCellRenderer renderer= comboBox.getRenderer();
		if (renderer == null) {
			renderer= new DefaultListCellRenderer();
		};
		Object prototypeValue= comboBox.getPrototypeDisplayValue();
		Component rendererComponent= renderer.getListCellRendererComponent(listBox,prototypeValue,-1,false,false);
		Dimension result= getSizeForComponent(rendererComponent);
		if ( comboBox.isEditable() ) {
			Dimension d= editor.getPreferredSize();
			result.width= Math.max(result.width,d.width);
			result.height= Math.max(result.height,d.height);
		};
		// Calculate in the padding:
		Insets comboBoxPadding= UIManager.getInsets("ComboBox.padding");
		if (comboBoxPadding!=null) {
			result.width += comboBoxPadding.left + comboBoxPadding.right;
			result.height += comboBoxPadding.top + comboBoxPadding.bottom;
		};
		return result;
	}
	@Override
	protected Dimension getSizeForComponent(Component comp) {
		currentValuePane.add(comp);
		comp.setFont(comboBox.getFont());
		Dimension d= comp.getPreferredSize();
		currentValuePane.remove(comp);
		return d;
	}
	@Override
	public void setBackground(Color c) {
	}
}
