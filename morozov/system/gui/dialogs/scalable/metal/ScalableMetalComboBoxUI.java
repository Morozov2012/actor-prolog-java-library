/*
 * @(#)ScalableMetalComboBoxUI.java 1.0 2009/11/23
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.metal;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/*
 * ScalableMetalComboBoxUI implementation for the Actor Prolog language
 * @version 1.0 2009/11/23
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableMetalComboBoxUI extends MetalComboBoxUI implements ScalableComboBoxUI {
	public static ComponentUI createUI(JComponent c) {
		return new ScalableMetalComboBoxUI();
	}
	//
	protected void installDefaults() {
		// UIManager.put("ComboBox.squareButton",false);
		UIManager.put("ComboBox.squareButton",true);
		super.installDefaults();
	}
	//
	@SuppressWarnings("unchecked")
	protected Dimension getDisplaySize() {
		Dimension result= new Dimension();
		ListCellRenderer renderer= comboBox.getRenderer();
		if (renderer == null) {
			renderer= new DefaultListCellRenderer();
		};
		Object prototypeValue= comboBox.getPrototypeDisplayValue();
		Component rendererComponent= renderer.getListCellRendererComponent(listBox,prototypeValue,-1,false,false);
		result= getSizeForComponent(rendererComponent);
		if ( comboBox.isEditable() ) {
			Dimension d= editor.getPreferredSize();
			result.width= Math.max(result.width,d.width);
			result.height= Math.max(result.height,d.height);
		};
		// calculate in the padding
		Insets padding= UIManager.getInsets("ComboBox.padding");
		if (padding!=null) {
			result.width += padding.left + padding.right;
			result.height += padding.top + padding.bottom;
		};
		return result;
	}
	protected Dimension getSizeForComponent(Component comp) {
		currentValuePane.add(comp);
		comp.setFont(comboBox.getFont());
		Dimension d= comp.getPreferredSize();
		currentValuePane.remove(comp);
		return d;
	}
	public void setBackground(Color c) {
		// background= c;
		// button.setBackground(c);
	}
}
