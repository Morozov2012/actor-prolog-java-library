/*
 * @(#)ScalableWindowsComboBoxUI.java 1.0 2009/12/29
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.windows;

import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.UIManager;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.ComponentUI;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;

/*
 * ScalableWindowsComboBoxUI implementation for the Actor Prolog language
 * @version 1.0 2009/12/29
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableWindowsComboBoxUI extends BasicComboBoxUI implements ScalableComboBoxUI {
	//
	protected Color background;
	protected JButton button;
	//
	public static ComponentUI createUI(JComponent c) {
		return new ScalableWindowsComboBoxUI();
	}
	//
	@Override
	protected JButton createArrowButton() {
		button = new ScalableComboBoxArrowButton(
					BasicArrowButton.SOUTH,
					UIManager.getColor("ComboBox.buttonBackground"),
					UIManager.getColor("ComboBox.buttonShadow"),
					UIManager.getColor("ComboBox.buttonDarkShadow"),
					UIManager.getColor("ComboBox.buttonHighlight"));
		if (background != null) {
			button.setBackground(background);
		};
		button.setName("ComboBox.arrowButton");
		return button;
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
		}
		Object prototypeValue= comboBox.getPrototypeDisplayValue();
		// Calculates the dimension based on the prototype value:
		Component rendererComponent= renderer.getListCellRendererComponent(listBox,prototypeValue,-1,false,false);
		Dimension result= getSizeForComponent(rendererComponent);
		if ( comboBox.isEditable() ) {
			Dimension d= editor.getPreferredSize();
			result.width= Math.max(result.width,d.width);
			result.height= Math.max(result.height,d.height);
		};
		//
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
		background= c;
		if (button != null && background != null) {
			button.setBackground(background);
		}
	}
}
