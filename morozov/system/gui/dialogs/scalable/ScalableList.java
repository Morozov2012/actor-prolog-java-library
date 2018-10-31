/*
 * @(#)ScalableList.java 1.0 2010/03/09
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableList implementation for the Actor Prolog language
 * @version 1.0 2010/03/09
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;

import java.util.ArrayList;

public class ScalableList extends ActiveComponent {
	//
	protected boolean enableMultiselection= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalableList(AbstractDialog tD, String[] stringList, double visibleRowCount, double visibleColumnCount, boolean enableSorting, boolean useTabStops) {
		super(tD);
		AList list= new AList(tD,this,stringList,visibleRowCount,visibleColumnCount,enableSorting,useTabStops);
		component= list;
		list.setMultipleSelection(enableMultiselection);
		list.addFocusListener(this);
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
			if (enableMultiselection) {
				return GeneralConverters.arrayListToTerm(items);
			} else {
				if (items.size() >= 1) {
					return items.get(items.size()-1);
				} else {
					return PrologEmptyList.instance;
				}
			}
		}
	}
	//
	public Term standardizeRange(Term value, ChoisePoint iX) {
		ArrayList<String> items= DialogUtils.listToStringArray(value,iX);
		return GeneralConverters.stringArrayToList(items);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
		if (component!=null) {
			((AList)component).putValue(value,iX);
			// if (targetDialog!=null) {
			//	targetDialog.reportValueUpdate(this);
			// }
		}
	}
	//
	public void putRange(Term value, ChoisePoint iX) {
		if (component!=null) {
			((AList)component).putRange(value,iX);
		}
	}
	//
	public Term getValue() {
		if (component!=null) {
			return ((AList)component).getValue();
		} else {
			return PrologUnknownValue.instance;
		}
	}
	//
	public Term getRange() {
		if (component!=null) {
			return ((AList)component).getRange();
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
			((AList)component).setMultipleSelection(flag);
		}
	}
	//
	public void setSelectedIndex(int index) {
		if (component!=null) {
			((AList)component).setSelectedIndex(index);
		}
	}
	//
	public void setSelectedIndices(int[] indices) {
		if (component!=null) {
			((AList)component).setSelectedIndices(indices);
		}
	}
}
