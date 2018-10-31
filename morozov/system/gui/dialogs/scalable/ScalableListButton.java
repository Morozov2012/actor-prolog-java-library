/*
 * @(#)ScalableListButton.java 1.0 2009/11/23
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableListButton implementation for the Actor Prolog language
 * @version 1.0 2009/11/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JComboBox;
import java.util.ArrayList;

public class ScalableListButton extends ScalableComboBox {
	//
	public ScalableListButton(AbstractDialog tD, String[] items, double visibleRowCount, double visibleColumnCount, boolean enableSorting) {
		super(tD,items,visibleRowCount,visibleColumnCount,false,enableSorting);
		// ((JComboBox)component).addActionListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw RejectValue.instance;
		} else {
			ArrayList<Term> items= DialogUtils.listToTermArray(value,iX);
			if (items.size() >= 1) {
				return items.get(items.size()-1);
			} else {
				return new PrologInteger(0);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setGeneralForeground(Color c) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void actionPerformed(ActionEvent event) {
		if (targetDialog!=null) {
			targetDialog.reportValueUpdate(this);
			// int modifiers= event.getModifiers();
			if (component!=null && targetDialog.safelyIsShowing() && ((JComboBox)component).isPopupVisible()) {
				synchronized (component) {
					Object selectedValue= ((JComboBox)component).getSelectedItem();
					if (selectedValue!=null) {
						String command= "name:" + selectedValue.toString();
						targetDialog.actionPerformed(new ActionEvent(event.getSource(),event.getID(),command,event.getWhen(),event.getModifiers()));
					}
				}
			}
		}
	}
}
