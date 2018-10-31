/*
 * @(#)ScalableTable.java 1.0 2012/08/23
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableTable implementation for the Actor Prolog language
 * @version 1.0 2012/08/23
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class ScalableTable extends ActiveComponent {
	//
	protected boolean enableMultiselection= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalableTable(AbstractDialog tD, double length, double height, ScalableTableColors colors, ScalableTableColumnDescription[] columnDescriptions, Term initialValue, ChoisePoint iX) {
		super(tD);
		ATable table= new ATable(tD,this,length,height,colors,columnDescriptions,initialValue,iX);
		component= table;
		table.setMultipleSelection(enableMultiselection);
		table.addFocusListener(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw RejectValue.instance;
		} else {
			ArrayList<Term> items= DialogUtils.tableToTermArray(value,iX);
			if (enableMultiselection) {
				return GeneralConverters.arrayListToTerm(items);
			} else {
				if (items.size() >= 1) {
					return new PrologList(items.get(items.size()-1),PrologEmptyList.instance);
				} else {
					return PrologEmptyList.instance;
				}
			}
		}
	}
	//
	public Term standardizeRange(Term value, ChoisePoint iX) {
		try {
			String text= value.getStringValue(iX);
			return value.dereferenceValue(iX);
		} catch (TermIsNotAString e1) {
			ArrayList<ArrayList<String>> items= DialogUtils.tableToStringArray(value,iX);
			return GeneralConverters.stringArrayToListOfList(items);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			((ATable)component).putValue(value,iX);
			// if (targetDialog!=null) {
			//	targetDialog.reportValueUpdate(this);
			// }
		}
	}
	//
	public void putRange(Term value, ChoisePoint iX) {
		if (component!=null) {
			((ATable)component).putRange(value,iX);
		}
	}
	//
	public Term getValue() {
		if (component!=null) {
			return ((ATable)component).getValue();
		} else {
			return PrologUnknownValue.instance;
		}
	}
	//
	public Term getRange() {
		if (component!=null) {
			return ((ATable)component).getRange();
		} else {
			// return PrologEmptyList.instance;
			return PrologUnknownValue.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setMultipleSelection(boolean flag) {
		enableMultiselection= flag;
		if (component!=null) {
			((ATable)component).setMultipleSelection(flag);
		}
	}
	//
	public void setSelectedIndex(int index) {
		if (component!=null) {
			((ATable)component).setSelectedIndex(index);
		}
	}
	//
	public void setSelectedIndices(int[] indices) {
		if (component!=null) {
			((ATable)component).setSelectedIndices(indices);
		}
	}
}
