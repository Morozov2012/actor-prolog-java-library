/*
 * @(#)ScalableAbstractButton.java 1.0 2010/03/08
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;

import javax.swing.AbstractButton;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public abstract class ScalableAbstractButton extends ActiveComponent {
	//
	public ScalableAbstractButton(AbstractDialog tD) {
		super(tD);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setMnemonic(char mnemonic) {
		if (component!=null) {
			((AbstractButton)component).setMnemonic(mnemonic);
		}
	}
	public int getMnemonic() {
		if (component!=null) {
			return ((AbstractButton)component).getMnemonic();
		} else {
			return KeyEvent.KEY_LOCATION_UNKNOWN;
		}
	}
	//
	public void setDisplayedMnemonicIndex(int index) {
		if (component!=null) {
			((AbstractButton)component).setDisplayedMnemonicIndex(index);
		}
	}
	public int getDisplayedMnemonicIndex() {
		if (component!=null) {
			return ((AbstractButton)component).getDisplayedMnemonicIndex();
		} else {
			return -1;
		}
	}
	//
	public void setActionCommand(String actionCommand) {
		if (component!=null) {
			((AbstractButton)component).setActionCommand(actionCommand);
		}
	}
	public String getActionCommand() {
		if (component!=null) {
			return ((AbstractButton)component).getActionCommand();
		} else {
			return "";
		}
	}
	//
	public void addActionListener(ActionListener l) {
		if (component!=null) {
			((AbstractButton)component).addActionListener(l);
		}
	}
	public void removeActionListener(ActionListener l) {
		if (component!=null) {
			((AbstractButton)component).removeActionListener(l);
		}
	}
	//
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
			component.repaint();
		}
	}
}
