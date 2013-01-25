/*
 * @(#)ScalableAbstractButton.java 1.0 2010/03/08
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;

import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.JToggleButton;

public abstract class ScalableAbstractButton extends ActiveComponent {
	public ScalableAbstractButton(AbstractDialog tD) {
		super(tD);
	}
	public void setMnemonic(char mnemonic) {
		if (component!=null) {
			((AbstractButton)component).setMnemonic(mnemonic);
		}
	}
	public void setDisplayedMnemonicIndex(int index) {
		if (component!=null) {
			((AbstractButton)component).setDisplayedMnemonicIndex(index);
		}
	}
	public void setActionCommand(String actionCommand) {
		if (component!=null) {
			((AbstractButton)component).setActionCommand(actionCommand);
		}
	}
	public void addActionListener(ActionListener l) {
		if (component!=null) {
			((AbstractButton)component).addActionListener(l);
		}
	}
	public boolean isUncertain() {
		if (component!=null) {
			ScalableToggleButtonModel model= (ScalableToggleButtonModel)((JToggleButton)component).getModel();
			return model.isUncertain();
		} else {
			return false;
		}
	}
	public void setUncertain(boolean flag) {
		if (component!=null) {
			ScalableToggleButtonModel model= (ScalableToggleButtonModel)((JToggleButton)component).getModel();
			model.setUncertain(flag);
			// component.invalidate();
			component.repaint();
		}
	}
}
