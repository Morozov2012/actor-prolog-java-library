/*
 * @(#)AButtonGroup.java 1.0 2010/03/08
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.AbstractButton;

import java.util.Iterator;

public class AButtonGroup extends ButtonGroup {
	//
	AbstractDialog targetDialog= null;
	ActiveComponent targetComponent= null;
	//
	public AButtonGroup(AbstractDialog tD, ActiveComponent tC) {
		targetDialog= tD;
		targetComponent= tC;
	}
	//
	public void setSelected(ButtonModel m, boolean b) {
		Iterator<AbstractButton> buttonIterator= buttons.iterator();
		while (buttonIterator.hasNext()) {
			AbstractButton currentButton= buttonIterator.next();
			ButtonModel model= ((AbstractButton)currentButton).getModel();
			((ScalableToggleButtonModel)model).setUncertain(false);
			((AbstractButton)currentButton).repaint();
		};
		boolean reportEvent= false;
		if (b && !super.isSelected(m)) {
			reportEvent= true;
		}
		super.setSelected(m,b);
		if (reportEvent && targetDialog!=null) {
			targetDialog.reportValueUpdate(targetComponent);
		}
	}
}
