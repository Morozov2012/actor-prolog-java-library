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
	protected Color background;
	protected JButton button;
	public static ComponentUI createUI(JComponent c) {
		return new ScalableWindowsComboBoxUI();
	}
	protected JButton createArrowButton() {
		button = new ScalableComboBoxArrowButton(
					BasicArrowButton.SOUTH,
					// BasicArrowButton.WEST,
					UIManager.getColor("ComboBox.buttonBackground"),
					UIManager.getColor("ComboBox.buttonShadow"),
					UIManager.getColor("ComboBox.buttonDarkShadow"),
					UIManager.getColor("ComboBox.buttonHighlight"));
		if (background != null) {
			button.setBackground(background);
		};
		button.setName("ComboBox.arrowButton");
		// button.setInsets(new Insets(10,15,3,4));
		return button;
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
		}
		// sameBaseline= true;
		Object prototypeValue= comboBox.getPrototypeDisplayValue();
		// Calculates the dimension based on the prototype value
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
		background= c;
		if (button != null && background != null) {
			button.setBackground(background);
		}
	}
}

class ScalableComboBoxArrowButton extends BasicArrowButton {
	public ScalableComboBoxArrowButton(int direction, Color background, Color shadow, Color darkShadow, Color highlight) {
		super(direction,background,shadow,darkShadow,highlight);
	}
	//
	public Dimension getPreferredSize() {
		// return new Dimension(16,16);
		return new Dimension((int)StrictMath.round(16*(2.0/5)),16);
		// return new Dimension((int)StrictMath.round(8*(2.0/5)),8);
		// return new Dimension((int)StrictMath.round(32*(2.0/5)),32);
		// return new Dimension((int)StrictMath.round(64*(2.0/5)),64);
	}
	public Insets getInsets() {
		Insets insets= super.getInsets();
		insets.set(insets.bottom,(int)StrictMath.round(insets.left*(2.0/5)),insets.top,(int)StrictMath.round(insets.right*(2.0/5)));
		return insets;
	}
}
