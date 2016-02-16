/*
 * @(#)ScalableToggleButtonModel.java 1.0 2009/11/17
 *
 * Copyright 2009 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ScalableToggleButtonModel implementation for the Actor Prolog language
 * @version 1.0 2009/11/13
 * @author IRE RAS Alexei A. Morozov
*/

import javax.swing.JToggleButton.ToggleButtonModel;

public class ScalableToggleButtonModel extends ToggleButtonModel {
	//
	protected boolean classicStyleFlag= false;
	protected boolean uncertaintyFlag= false;
	//
	protected long buttonNumber= 0;
	protected String buttonText= "";
	//
	public ScalableToggleButtonModel(long number, String text) {
		this(false,number,text);
	}
	public ScalableToggleButtonModel(boolean selected, long number, String text) {
		super();
		buttonNumber= number;
		buttonText= text;
		setSelected(selected);
	}
	//
	public boolean hasClassicStyle() {
		return classicStyleFlag;
	}
	//
	public void setHasClassicStyle(boolean flag) {
		classicStyleFlag= flag;
	}
	//
	public boolean isUncertain() {
		return uncertaintyFlag;
	}
	//
	public void setUncertain(boolean flag) {
		uncertaintyFlag= flag;
	}
	//
	public void setSelected(boolean flag) {
		uncertaintyFlag= false;
		super.setSelected(flag);
	}
	//
	public long getNumber() {
		return buttonNumber;
	}
	public String getText() {
		return buttonText;
	}
}
