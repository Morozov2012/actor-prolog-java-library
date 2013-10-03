// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.tests;

import target.*;

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.terms.*;

import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.AbstractButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class MyDialog1 extends AbstractDialog {
	ScalableButton control_14_4_0;
	ScalableButton control_14_3_0;
	ScalableButton control_14_2_0;
	ScalableButton control_14_1_0;
	ScalablePanel panel14;
	ScalableSlider control_2_1_2;
	ScalableSlider control_2_2_1;
	ScalableListButton control_13_1_2;
	ScalableComboBox control_13_1_1;
	ScalablePanel panel13;
	ScalableList control_12_1_1;
	ScalableTitledPanel panel12;
	ScalableCheckBox control_11_1_2;
	ScalableCheckBox control_11_1_1;
	ScalableTitledPanel panel11;
	ScalablePanel panel10;
	ScalablePanel panel9;
	ScalableButtonGroup bG8;
	ScalableRadioButton control_8_3_1;
	ScalableRadioButton control_8_2_1;
	ScalableRadioButton control_8_1_1;
	ScalablePanel panel8;
	RealField control_7_1_2;
	IntegerField control_7_1_1;
	ScalablePanel panel7;
	ActivePasswordField control_6_1_2;
	ActiveTextField control_6_1_1;
	ScalablePanel panel6;
	ScalableTextArea control_5_2_1;
	ScalableTextArea control_5_1_1;
	ScalableTitledPanel panel5;
	ScalablePanel panel4;
	ScalablePanel panel3;
	ScalablePanel panel2;
	ScalableLabel control_1_0_2;
	ScalableLabel control_1_0_1;
	ScalablePanel panel1;
	public MyDialog1(Window w) {
		super(w);
	}
	protected String getPredefinedTitle() {
		return "Demo Panel";
	}
	protected Term getPredefinedTextColor() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	protected Term getPredefinedSpaceColor() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	protected Term getPredefinedFontName() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	protected Term getPredefinedFontSize() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	protected Term getPredefinedFontStyle() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	protected Term getPredefinedX() {
		return new PrologReal(5);
	}
	protected Term getPredefinedY() {
		return new PrologReal(5);
	}
	protected Term getPredefinedBackgroundColor() {
		return new PrologString("Emerald");
	}
	public void assemble(ChoisePoint iX) {
		//
		// mainPanel
		//
		mainPanel= new ScalablePanel();
		mainPanelLayout= new GridBagLayout();
		mainPanel.setLayout(mainPanelLayout);
		GridBagConstraints mainPanelConstraints= new GridBagConstraints();
		mainPanelConstraints.ipadx= 0;
		mainPanelConstraints.ipady= 0;
		//
		// panel1
		//
		panel1= new ScalablePanel();
		GridBagLayout gBL1= new GridBagLayout();
		panel1.setLayout(gBL1);
		GridBagConstraints gBC1= new GridBagConstraints();
		gBC1.ipadx= 0;
		gBC1.ipady= 0;
		//
		mainPanelConstraints.gridy= 0;
		mainPanelConstraints.gridx= 1;
		mainPanelConstraints.anchor= GridBagConstraints.CENTER;
		mainPanelConstraints.fill= GridBagConstraints.NONE;
		mainPanelConstraints.weightx= 1;
		mainPanelConstraints.weighty= 1;
		mainPanelLayout.setConstraints(panel1,mainPanelConstraints);
		panel1.setPadding(mainPanelLayout,true,true,true,true,1.5,0.7);
		panel1.setScaling(1,1);
		mainPanel.add(panel1);
		//
		// control_1_0_1
		//
		gBC1.gridy= 1;
		gBC1.gridx= 0;
		gBC1.anchor= GridBagConstraints.CENTER;
		gBC1.fill= GridBagConstraints.NONE;
		gBC1.weightx= 1;
		gBC1.weighty= 1;
		gBC1.ipadx= 0;
		gBC1.ipady= 0;
		control_1_0_1= new ScalableLabel(this,"Example of dialog window");
		control_1_0_1.setPadding(gBL1,false,false,true,false,1.5,0.7);
		control_1_0_1.setScaling(1,1);
		gBL1.setConstraints(control_1_0_1.component,gBC1);
		panel1.add(control_1_0_1.component);
		//
		// control_1_0_2
		//
		gBC1.gridy= 2;
		gBC1.gridx= 0;
		gBC1.anchor= GridBagConstraints.CENTER;
		gBC1.fill= GridBagConstraints.NONE;
		gBC1.weightx= 1;
		gBC1.weighty= 1;
		gBC1.ipadx= 0;
		gBC1.ipady= 0;
		control_1_0_2= new ScalableLabel(this,"Example of dialog window",24,TextAlignment.CENTER);
		control_1_0_2.setPadding(gBL1,false,false,true,false,1.5,0.7);
		control_1_0_2.setScaling(1,1);
		gBL1.setConstraints(control_1_0_2.component,gBC1);
		panel1.add(control_1_0_2.component);
		//
		// panel2
		//
		panel2= new ScalablePanel();
		GridBagLayout gBL2= new GridBagLayout();
		panel2.setLayout(gBL2);
		GridBagConstraints gBC2= new GridBagConstraints();
		gBC2.ipadx= 0;
		gBC2.ipady= 0;
		//
		gBC1.gridy= 3;
		gBC1.gridx= 0;
		gBC1.anchor= GridBagConstraints.CENTER;
		gBC1.fill= GridBagConstraints.NONE;
		gBC1.weightx= 1;
		gBC1.weighty= 1;
		gBL1.setConstraints(panel2,gBC1);
		panel2.setPadding(gBL1,false,false,true,false,1.5,0.7);
		panel2.setScaling(1,1);
		panel1.add(panel2);
		//
		// panel3
		//
		panel3= new ScalablePanel();
		GridBagLayout gBL3= new GridBagLayout();
		panel3.setLayout(gBL3);
		GridBagConstraints gBC3= new GridBagConstraints();
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		//
		gBC2.gridy= 1;
		gBC2.gridx= 1;
		gBC2.anchor= GridBagConstraints.CENTER;
		gBC2.fill= GridBagConstraints.NONE;
		gBC2.weightx= 1;
		gBC2.weighty= 1;
		gBL2.setConstraints(panel3,gBC2);
		panel3.setPadding(gBL2,false,false,true,true,1.5,0.7);
		panel3.setScaling(1,1);
		panel2.add(panel3);
		//
		// panel4
		//
		panel4= new ScalablePanel();
		GridBagLayout gBL4= new GridBagLayout();
		panel4.setLayout(gBL4);
		GridBagConstraints gBC4= new GridBagConstraints();
		gBC4.ipadx= 0;
		gBC4.ipady= 0;
		//
		gBC3.gridy= 1;
		gBC3.gridx= 1;
		gBC3.anchor= GridBagConstraints.CENTER;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBL3.setConstraints(panel4,gBC3);
		panel4.setPadding(gBL3,false,false,true,false,1.5,0.7);
		panel4.setScaling(1,1);
		panel3.add(panel4);
		//
		// panel5
		//
		panel5= new ScalableTitledPanel("Text Areas");
		GridBagLayout gBL5= new GridBagLayout();
		panel5.setLayout(gBL5);
		GridBagConstraints gBC5= new GridBagConstraints();
		gBC5.ipadx= 0;
		gBC5.ipady= 0;
		//
		gBC4.gridy= 1;
		gBC4.gridx= 1;
		gBC4.anchor= GridBagConstraints.CENTER;
		gBC4.fill= GridBagConstraints.NONE;
		gBC4.weightx= 1;
		gBC4.weighty= 1;
		gBL4.setConstraints(panel5,gBC4);
		panel5.setPadding(gBL4,false,false,false,false,1.5,0.7);
		panel5.setScaling(1,1);
		panel4.add(panel5);
		//
		// control_5_1_1
		//
		gBC5.gridy= 1;
		gBC5.gridx= 1;
		gBC5.anchor= GridBagConstraints.CENTER;
		gBC5.fill= GridBagConstraints.NONE;
		gBC5.weightx= 1;
		gBC5.weighty= 1;
		gBC5.ipadx= 0;
		gBC5.ipady= 0;
		control_5_1_1= new ScalableTextArea(this,"Text",2,7);
		control_5_1_1.setEditable(false);
		control_5_1_1.setPadding(gBL5,true,true,true,true,1.5,0.7);
		control_5_1_1.setScaling(1,1);
		gBL5.setConstraints(control_5_1_1.component,gBC5);
		panel5.add(control_5_1_1.component);
		//
		// control_5_2_1
		//
		gBC5.gridy= 1;
		gBC5.gridx= 2;
		gBC5.anchor= GridBagConstraints.CENTER;
		gBC5.fill= GridBagConstraints.NONE;
		gBC5.weightx= 1;
		gBC5.weighty= 1;
		gBC5.ipadx= 0;
		gBC5.ipady= 0;
		control_5_2_1= new ScalableTextArea(this,"Text",2,7);
		control_5_2_1.setPadding(gBL5,true,false,true,true,1.5,0.7);
		control_5_2_1.setScaling(1,1);
		gBL5.setConstraints(control_5_2_1.component,gBC5);
		panel5.add(control_5_2_1.component);
		//
		// panel6
		//
		panel6= new ScalablePanel();
		GridBagLayout gBL6= new GridBagLayout();
		panel6.setLayout(gBL6);
		GridBagConstraints gBC6= new GridBagConstraints();
		gBC6.ipadx= 0;
		gBC6.ipady= 0;
		//
		gBC5.gridy= 1;
		gBC5.gridx= 3;
		gBC5.anchor= GridBagConstraints.CENTER;
		gBC5.fill= GridBagConstraints.NONE;
		gBC5.weightx= 1;
		gBC5.weighty= 1;
		gBL5.setConstraints(panel6,gBC5);
		panel6.setPadding(gBL5,true,false,true,true,1.5,0.7);
		panel6.setScaling(1,1);
		panel5.add(panel6);
		//
		// control_6_1_1
		//
		gBC6.gridy= 1;
		gBC6.gridx= 1;
		gBC6.anchor= GridBagConstraints.WEST;
		gBC6.fill= GridBagConstraints.NONE;
		gBC6.weightx= 1;
		gBC6.weighty= 1;
		gBC6.ipadx= 0;
		gBC6.ipady= 0;
		control_6_1_1= new ActiveTextField(this,"Name",7);
		control_6_1_1.setPadding(gBL6,false,false,true,false,1.5,0.7);
		control_6_1_1.setScaling(1,1);
		gBL6.setConstraints(control_6_1_1.component,gBC6);
		panel6.add(control_6_1_1.component);
		//
		// control_6_1_2
		//
		gBC6.gridy= 2;
		gBC6.gridx= 1;
		gBC6.anchor= GridBagConstraints.WEST;
		gBC6.fill= GridBagConstraints.NONE;
		gBC6.weightx= 1;
		gBC6.weighty= 1;
		gBC6.ipadx= 0;
		gBC6.ipady= 0;
		control_6_1_2= new ActivePasswordField(this,"Name",7);
		control_6_1_2.setPadding(gBL6,false,false,false,false,1.5,0.7);
		control_6_1_2.setScaling(1,1);
		gBL6.setConstraints(control_6_1_2.component,gBC6);
		panel6.add(control_6_1_2.component);
		//
		// panel7
		//
		panel7= new ScalablePanel();
		GridBagLayout gBL7= new GridBagLayout();
		panel7.setLayout(gBL7);
		GridBagConstraints gBC7= new GridBagConstraints();
		gBC7.ipadx= 0;
		gBC7.ipady= 0;
		//
		gBC5.gridy= 1;
		gBC5.gridx= 4;
		gBC5.anchor= GridBagConstraints.CENTER;
		gBC5.fill= GridBagConstraints.NONE;
		gBC5.weightx= 1;
		gBC5.weighty= 1;
		gBL5.setConstraints(panel7,gBC5);
		panel7.setPadding(gBL5,true,false,true,true,1.5,0.7);
		panel7.setScaling(1,1);
		panel5.add(panel7);
		//
		// control_7_1_1
		//
		gBC7.gridy= 1;
		gBC7.gridx= 1;
		gBC7.anchor= GridBagConstraints.WEST;
		gBC7.fill= GridBagConstraints.NONE;
		gBC7.weightx= 1;
		gBC7.weighty= 1;
		gBC7.ipadx= 0;
		gBC7.ipady= 0;
		control_7_1_1= new IntegerField(this,"10000",7);
		control_7_1_1.setPadding(gBL7,false,false,true,false,1.5,0.7);
		control_7_1_1.setScaling(1,1);
		gBL7.setConstraints(control_7_1_1.component,gBC7);
		panel7.add(control_7_1_1.component);
		//
		// control_7_1_2
		//
		gBC7.gridy= 2;
		gBC7.gridx= 1;
		gBC7.anchor= GridBagConstraints.WEST;
		gBC7.fill= GridBagConstraints.NONE;
		gBC7.weightx= 1;
		gBC7.weighty= 1;
		gBC7.ipadx= 0;
		gBC7.ipady= 0;
		control_7_1_2= new RealField(this,"9.1",7);
		control_7_1_2.setPadding(gBL7,false,false,false,false,1.5,0.7);
		control_7_1_2.setScaling(1,1);
		gBL7.setConstraints(control_7_1_2.component,gBC7);
		panel7.add(control_7_1_2.component);
		//
		// panel8
		//
		panel8= new ScalablePanel();
		GridBagLayout gBL8= new GridBagLayout();
		panel8.setLayout(gBL8);
		GridBagConstraints gBC8= new GridBagConstraints();
		gBC8.ipadx= 0;
		gBC8.ipady= 0;
		//
		gBC3.gridy= 2;
		gBC3.gridx= 1;
		gBC3.anchor= GridBagConstraints.CENTER;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBL3.setConstraints(panel8,gBC3);
		panel8.setPadding(gBL3,false,false,true,false,1.5,0.7);
		panel8.setScaling(1,1);
		panel3.add(panel8);
		//
		// control_8_1_1
		//
		gBC8.gridy= 1;
		gBC8.gridx= 1;
		gBC8.anchor= GridBagConstraints.CENTER;
		gBC8.fill= GridBagConstraints.NONE;
		gBC8.weightx= 1;
		gBC8.weighty= 1;
		gBC8.ipadx= 0;
		gBC8.ipady= 0;
		control_8_1_1= new ScalableRadioButton(this,"First",1);
		control_8_1_1.setPadding(gBL8,false,false,false,true,4,1);
		control_8_1_1.setScaling(1,1);
		gBL8.setConstraints(control_8_1_1.component,gBC8);
		panel8.add(control_8_1_1.component);
		//
		// control_8_2_1
		//
		gBC8.gridy= 1;
		gBC8.gridx= 2;
		gBC8.anchor= GridBagConstraints.CENTER;
		gBC8.fill= GridBagConstraints.NONE;
		gBC8.weightx= 1;
		gBC8.weighty= 1;
		gBC8.ipadx= 0;
		gBC8.ipady= 0;
		control_8_2_1= new ScalableRadioButton(this,"Second",2);
		control_8_2_1.setPadding(gBL8,false,false,false,true,4,1);
		control_8_2_1.setScaling(1,1);
		gBL8.setConstraints(control_8_2_1.component,gBC8);
		panel8.add(control_8_2_1.component);
		//
		// control_8_3_1
		//
		gBC8.gridy= 1;
		gBC8.gridx= 3;
		gBC8.anchor= GridBagConstraints.CENTER;
		gBC8.fill= GridBagConstraints.NONE;
		gBC8.weightx= 1;
		gBC8.weighty= 1;
		gBC8.ipadx= 0;
		gBC8.ipady= 0;
		control_8_3_1= new ScalableRadioButton(this,"Third",3);
		control_8_3_1.setPadding(gBL8,false,false,false,false,4,1);
		control_8_3_1.setScaling(1,1);
		gBL8.setConstraints(control_8_3_1.component,gBC8);
		panel8.add(control_8_3_1.component);
		//
		// bG8
		//
		bG8= new ScalableButtonGroup(this);
		bG8.add(((AbstractButton)control_8_1_1.component));
		bG8.add(((AbstractButton)control_8_2_1.component));
		bG8.add(((AbstractButton)control_8_3_1.component));
		((AbstractButton)control_8_1_1.component).setSelected(true);
		//
		// panel9
		//
		panel9= new ScalablePanel();
		GridBagLayout gBL9= new GridBagLayout();
		panel9.setLayout(gBL9);
		GridBagConstraints gBC9= new GridBagConstraints();
		gBC9.ipadx= 0;
		gBC9.ipady= 0;
		//
		gBC3.gridy= 3;
		gBC3.gridx= 1;
		gBC3.anchor= GridBagConstraints.CENTER;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBL3.setConstraints(panel9,gBC3);
		panel9.setPadding(gBL3,false,false,false,false,1.5,0.7);
		panel9.setScaling(1,1);
		panel3.add(panel9);
		//
		// panel10
		//
		panel10= new ScalablePanel();
		GridBagLayout gBL10= new GridBagLayout();
		panel10.setLayout(gBL10);
		GridBagConstraints gBC10= new GridBagConstraints();
		gBC10.ipadx= 0;
		gBC10.ipady= 0;
		//
		gBC9.gridy= 1;
		gBC9.gridx= 1;
		gBC9.anchor= GridBagConstraints.CENTER;
		gBC9.fill= GridBagConstraints.NONE;
		gBC9.weightx= 1;
		gBC9.weighty= 1;
		gBL9.setConstraints(panel10,gBC9);
		panel10.setPadding(gBL9,false,false,false,true,1.5,0.7);
		panel10.setScaling(1,1);
		panel9.add(panel10);
		//
		// panel11
		//
		panel11= new ScalableTitledPanel("Checkboxes");
		GridBagLayout gBL11= new GridBagLayout();
		panel11.setLayout(gBL11);
		GridBagConstraints gBC11= new GridBagConstraints();
		gBC11.ipadx= 0;
		gBC11.ipady= 0;
		//
		gBC10.gridy= 1;
		gBC10.gridx= 1;
		gBC10.anchor= GridBagConstraints.WEST;
		gBC10.fill= GridBagConstraints.NONE;
		gBC10.weightx= 1;
		gBC10.weighty= 1;
		gBL10.setConstraints(panel11,gBC10);
		panel11.setPadding(gBL10,false,false,false,false,1.5,0.7);
		panel11.setScaling(1,1);
		panel10.add(panel11);
		//
		// control_11_1_1
		//
		gBC11.gridy= 1;
		gBC11.gridx= 1;
		gBC11.anchor= GridBagConstraints.WEST;
		gBC11.fill= GridBagConstraints.NONE;
		gBC11.weightx= 1;
		gBC11.weighty= 1;
		gBC11.ipadx= 0;
		gBC11.ipady= 0;
		control_11_1_1= new ScalableCheckBox(this,"Checkbox 1",true);
		control_11_1_1.setHasClassicStyle(true);
		control_11_1_1.setPadding(gBL11,true,true,true,true,1.5,0.7);
		control_11_1_1.setScaling(1,1);
		gBL11.setConstraints(control_11_1_1.component,gBC11);
		panel11.add(control_11_1_1.component);
		//
		// control_11_1_2
		//
		gBC11.gridy= 2;
		gBC11.gridx= 1;
		gBC11.anchor= GridBagConstraints.WEST;
		gBC11.fill= GridBagConstraints.NONE;
		gBC11.weightx= 1;
		gBC11.weighty= 1;
		gBC11.ipadx= 0;
		gBC11.ipady= 0;
		control_11_1_2= new ScalableCheckBox(this,"Checkbox 2",true);
		control_11_1_2.setPadding(gBL11,false,true,true,true,1.5,0.7);
		control_11_1_2.setScaling(1,1);
		gBL11.setConstraints(control_11_1_2.component,gBC11);
		panel11.add(control_11_1_2.component);
		//
		// panel12
		//
		panel12= new ScalableTitledPanel("List Controls");
		GridBagLayout gBL12= new GridBagLayout();
		panel12.setLayout(gBL12);
		GridBagConstraints gBC12= new GridBagConstraints();
		gBC12.ipadx= 0;
		gBC12.ipady= 0;
		//
		gBC9.gridy= 1;
		gBC9.gridx= 2;
		gBC9.anchor= GridBagConstraints.CENTER;
		gBC9.fill= GridBagConstraints.NONE;
		gBC9.weightx= 1;
		gBC9.weighty= 1;
		gBL9.setConstraints(panel12,gBC9);
		panel12.setPadding(gBL9,false,false,false,false,1.5,0.7);
		panel12.setScaling(1,1);
		panel9.add(panel12);
		//
		// control_12_1_1
		//
		gBC12.gridy= 1;
		gBC12.gridx= 1;
		gBC12.anchor= GridBagConstraints.CENTER;
		gBC12.fill= GridBagConstraints.NONE;
		gBC12.weightx= 1;
		gBC12.weighty= 1;
		gBC12.ipadx= 0;
		gBC12.ipady= 0;
		String[] stringList_12_1_1= {"One","Two","Three","Four"};
		control_12_1_1= new ScalableList(this,stringList_12_1_1,4,10,false,false);
		control_12_1_1.setMultipleSelection(true);
		control_12_1_1.setSelectedIndices(new int[]{0,2});
		control_12_1_1.setPadding(gBL12,true,true,true,true,1.5,0.7);
		control_12_1_1.setScaling(1,1);
		gBL12.setConstraints(control_12_1_1.component,gBC12);
		panel12.add(control_12_1_1.component);
		//
		// panel13
		//
		panel13= new ScalablePanel();
		GridBagLayout gBL13= new GridBagLayout();
		panel13.setLayout(gBL13);
		GridBagConstraints gBC13= new GridBagConstraints();
		gBC13.ipadx= 0;
		gBC13.ipady= 0;
		//
		gBC12.gridy= 1;
		gBC12.gridx= 2;
		gBC12.anchor= GridBagConstraints.CENTER;
		gBC12.fill= GridBagConstraints.NONE;
		gBC12.weightx= 1;
		gBC12.weighty= 1;
		gBL12.setConstraints(panel13,gBC12);
		panel13.setPadding(gBL12,true,false,true,true,1.5,0.7);
		panel13.setScaling(1,1);
		panel12.add(panel13);
		//
		// control_13_1_1
		//
		gBC13.gridy= 1;
		gBC13.gridx= 1;
		gBC13.anchor= GridBagConstraints.WEST;
		gBC13.fill= GridBagConstraints.NONE;
		gBC13.weightx= 1;
		gBC13.weighty= 1;
		gBC13.ipadx= 0;
		gBC13.ipady= 0;
		String[] stringList_13_1_1= {"One","Two","Three"};
		control_13_1_1= new ScalableComboBox(this,stringList_13_1_1,7,10,false);
		control_13_1_1.setSelectedIndex(0);
		control_13_1_1.setPadding(gBL13,false,false,true,false,1.5,0.7);
		control_13_1_1.setScaling(1,1);
		gBL13.setConstraints(control_13_1_1.component,gBC13);
		panel13.add(control_13_1_1.component);
		//
		// control_13_1_2
		//
		gBC13.gridy= 2;
		gBC13.gridx= 1;
		gBC13.anchor= GridBagConstraints.WEST;
		gBC13.fill= GridBagConstraints.NONE;
		gBC13.weightx= 1;
		gBC13.weighty= 1;
		gBC13.ipadx= 0;
		gBC13.ipady= 0;
		String[] stringList_13_1_2= {"One","Two","Three"};
		control_13_1_2= new ScalableListButton(this,stringList_13_1_2,7,10,false);
		control_13_1_2.setSelectedIndex(0);
		control_13_1_2.setPadding(gBL13,false,false,false,false,1.5,0.7);
		control_13_1_2.setScaling(1,1);
		gBL13.setConstraints(control_13_1_2.component,gBC13);
		panel13.add(control_13_1_2.component);
		//
		// control_2_2_1
		//
		gBC2.gridy= 1;
		gBC2.gridx= 2;
		gBC2.anchor= GridBagConstraints.CENTER;
		gBC2.fill= GridBagConstraints.VERTICAL;
		gBC2.weightx= 1;
		gBC2.weighty= 1;
		gBC2.ipadx= 0;
		gBC2.ipady= 0;
		control_2_2_1= new ScalableSlider(this,JSlider.VERTICAL,15,0,40,10);
		control_2_2_1.setPadding(gBL2,false,false,true,false,1.5,0.7);
		control_2_2_1.setScaling(1,1);
		gBL2.setConstraints(control_2_2_1.component,gBC2);
		panel2.add(control_2_2_1.component);
		//
		// control_2_1_2
		//
		gBC2.gridy= 2;
		gBC2.gridx= 1;
		gBC2.anchor= GridBagConstraints.CENTER;
		gBC2.fill= GridBagConstraints.HORIZONTAL;
		gBC2.weightx= 1;
		gBC2.weighty= 1;
		gBC2.ipadx= 0;
		gBC2.ipady= 0;
		control_2_1_2= new ScalableSlider(this,JSlider.HORIZONTAL,40,0,40,30);
		control_2_1_2.setPadding(gBL2,false,false,false,true,1.5,0.7);
		control_2_1_2.setScaling(1,1);
		gBL2.setConstraints(control_2_1_2.component,gBC2);
		panel2.add(control_2_1_2.component);
		//
		// panel14
		//
		panel14= new ScalablePanel();
		GridBagLayout gBL14= new GridBagLayout();
		panel14.setLayout(gBL14);
		GridBagConstraints gBC14= new GridBagConstraints();
		gBC14.ipadx= 0;
		gBC14.ipady= 0;
		//
		gBC1.gridy= 4;
		gBC1.gridx= 0;
		gBC1.anchor= GridBagConstraints.CENTER;
		gBC1.fill= GridBagConstraints.NONE;
		gBC1.weightx= 1;
		gBC1.weighty= 1;
		gBL1.setConstraints(panel14,gBC1);
		panel14.setPadding(gBL1,false,false,false,false,1.5,0.7);
		panel14.setScaling(1,1);
		panel1.add(panel14);
		//
		// control_14_1_0
		//
		gBC14.gridy= 0;
		gBC14.gridx= 1;
		gBC14.anchor= GridBagConstraints.CENTER;
		gBC14.fill= GridBagConstraints.NONE;
		gBC14.weightx= 1;
		gBC14.weighty= 1;
		gBC14.ipadx= 0;
		gBC14.ipady= 0;
		control_14_1_0= new ScalableButton(this,"Action");
		control_14_1_0.setPadding(gBL14,false,false,false,true,1.5,0.7);
		control_14_1_0.setScaling(1,1);
		control_14_1_0.setMnemonic('A');
		control_14_1_0.setDisplayedMnemonicIndex(0);
		control_14_1_0.setActionCommand("name:action");
		control_14_1_0.addActionListener(this);
		gBL14.setConstraints(control_14_1_0.component,gBC14);
		panel14.add(control_14_1_0.component);
		//
		// control_14_2_0
		//
		gBC14.gridy= 0;
		gBC14.gridx= 2;
		gBC14.anchor= GridBagConstraints.CENTER;
		gBC14.fill= GridBagConstraints.NONE;
		gBC14.weightx= 1;
		gBC14.weighty= 1;
		gBC14.ipadx= 0;
		gBC14.ipady= 0;
		control_14_2_0= new ScalableButton(this,"Verify");
		control_14_2_0.setPadding(gBL14,false,false,false,true,1.5,0.7);
		control_14_2_0.setScaling(1.2,1.3);
		control_14_2_0.setMnemonic('V');
		control_14_2_0.setDisplayedMnemonicIndex(0);
		control_14_2_0.setActionCommand("verify");
		control_14_2_0.addActionListener(this);
		gBL14.setConstraints(control_14_2_0.component,gBC14);
		panel14.add(control_14_2_0.component);
		//
		// control_14_3_0
		//
		gBC14.gridy= 0;
		gBC14.gridx= 3;
		gBC14.anchor= GridBagConstraints.CENTER;
		gBC14.fill= GridBagConstraints.NONE;
		gBC14.weightx= 1;
		gBC14.weighty= 1;
		gBC14.ipadx= 0;
		gBC14.ipady= 0;
		control_14_3_0= new ScalableButton(this,"Reset");
		control_14_3_0.setPadding(gBL14,false,false,false,true,1.5,0.7);
		control_14_3_0.setScaling(1.2,1.3);
		control_14_3_0.setMnemonic('R');
		control_14_3_0.setDisplayedMnemonicIndex(0);
		control_14_3_0.setActionCommand("reset");
		control_14_3_0.addActionListener(this);
		gBL14.setConstraints(control_14_3_0.component,gBC14);
		panel14.add(control_14_3_0.component);
		//
		// control_14_4_0
		//
		gBC14.gridy= 0;
		gBC14.gridx= 4;
		gBC14.anchor= GridBagConstraints.CENTER;
		gBC14.fill= GridBagConstraints.NONE;
		gBC14.weightx= 1;
		gBC14.weighty= 1;
		gBC14.ipadx= 0;
		gBC14.ipady= 0;
		control_14_4_0= new ScalableButton(this,"Close");
		control_14_4_0.setPadding(gBL14,false,false,false,false,1.5,0.7);
		control_14_4_0.setScaling(1,1);
		control_14_4_0.setMnemonic('C');
		control_14_4_0.setDisplayedMnemonicIndex(0);
		control_14_4_0.setActionCommand("close");
		control_14_4_0.addActionListener(this);
		gBL14.setConstraints(control_14_4_0.component,gBC14);
		panel14.add(control_14_4_0.component);
	}
	protected void setNewFont(Font newFont) {
		mainPanel.setFont(newFont);
		panel1.setFont(newFont);
		control_1_0_1.setFont(newFont);
		control_1_0_2.setFont(newFont);
		panel2.setFont(newFont);
		panel3.setFont(newFont);
		panel4.setFont(newFont);
		panel5.setFont(newFont);
		control_5_1_1.setFont(newFont);
		control_5_2_1.setFont(newFont);
		panel6.setFont(newFont);
		control_6_1_1.setFont(newFont);
		control_6_1_2.setFont(newFont);
		panel7.setFont(newFont);
		control_7_1_1.setFont(newFont);
		control_7_1_2.setFont(newFont);
		panel8.setFont(newFont);
		control_8_1_1.setFont(newFont);
		control_8_2_1.setFont(newFont);
		control_8_3_1.setFont(newFont);
		panel9.setFont(newFont);
		panel10.setFont(newFont);
		panel11.setFont(newFont);
		control_11_1_1.setFont(newFont);
		control_11_1_2.setFont(newFont);
		panel12.setFont(newFont);
		control_12_1_1.setFont(newFont);
		panel13.setFont(newFont);
		control_13_1_1.setFont(newFont);
		control_13_1_2.setFont(newFont);
		control_2_2_1.setFont(newFont);
		control_2_1_2.setFont(newFont);
		panel14.setFont(newFont);
		control_14_1_0.setFont(newFont);
		control_14_2_0.setFont(newFont);
		control_14_3_0.setFont(newFont);
		control_14_4_0.setFont(newFont);
	}
	protected void setNewBackground(Color c) {
		setBackground(c);
		if (!isDraftMode) {
			mainPanel.setBackground(c);
		};
		if (!isDraftMode) {
			panel1.setBackground(c);
		};
		control_1_0_1.setBackground(c);
		control_1_0_2.setBackground(c);
		if (!isDraftMode) {
			panel2.setBackground(c);
		};
		if (!isDraftMode) {
			panel3.setBackground(c);
		};
		if (!isDraftMode) {
			panel4.setBackground(c);
		};
		if (!isDraftMode) {
			panel5.setBackground(c);
		};
		control_5_1_1.setBackground(c);
		control_5_2_1.setBackground(c);
		if (!isDraftMode) {
			panel6.setBackground(c);
		};
		control_6_1_1.setBackground(c);
		control_6_1_2.setBackground(c);
		if (!isDraftMode) {
			panel7.setBackground(c);
		};
		control_7_1_1.setBackground(c);
		control_7_1_2.setBackground(c);
		if (!isDraftMode) {
			panel8.setBackground(c);
		};
		control_8_1_1.setBackground(c);
		control_8_2_1.setBackground(c);
		control_8_3_1.setBackground(c);
		if (!isDraftMode) {
			panel9.setBackground(c);
		};
		if (!isDraftMode) {
			panel10.setBackground(c);
		};
		if (!isDraftMode) {
			panel11.setBackground(c);
		};
		control_11_1_1.setBackground(c);
		control_11_1_2.setBackground(c);
		if (!isDraftMode) {
			panel12.setBackground(c);
		};
		control_12_1_1.setBackground(c);
		if (!isDraftMode) {
			panel13.setBackground(c);
		};
		control_13_1_1.setBackground(c);
		control_13_1_2.setBackground(c);
		control_2_2_1.setBackground(c);
		control_2_1_2.setBackground(c);
		if (!isDraftMode) {
			panel14.setBackground(c);
		};
		control_14_1_0.setBackground(c);
		control_14_2_0.setBackground(c);
		control_14_3_0.setBackground(c);
		control_14_4_0.setBackground(c);
	}
	protected void setNewForeground(Color c) {
		mainPanel.setForeground(c);
		panel1.setForeground(c);
		control_1_0_1.setForeground(c);
		control_1_0_2.setForeground(c);
		panel2.setForeground(c);
		panel3.setForeground(c);
		panel4.setForeground(c);
		panel5.setForeground(c);
		control_5_1_1.setForeground(c);
		control_5_2_1.setForeground(c);
		panel6.setForeground(c);
		control_6_1_1.setForeground(c);
		control_6_1_2.setForeground(c);
		panel7.setForeground(c);
		control_7_1_1.setForeground(c);
		control_7_1_2.setForeground(c);
		panel8.setForeground(c);
		control_8_1_1.setForeground(c);
		control_8_2_1.setForeground(c);
		control_8_3_1.setForeground(c);
		panel9.setForeground(c);
		panel10.setForeground(c);
		panel11.setForeground(c);
		control_11_1_1.setForeground(c);
		control_11_1_2.setForeground(c);
		panel12.setForeground(c);
		control_12_1_1.setForeground(c);
		panel13.setForeground(c);
		control_13_1_1.setForeground(c);
		control_13_1_2.setForeground(c);
		control_2_2_1.setForeground(c);
		control_2_1_2.setForeground(c);
		panel14.setForeground(c);
		control_14_1_0.setForeground(c);
		control_14_2_0.setForeground(c);
		control_14_3_0.setForeground(c);
		control_14_4_0.setForeground(c);
	}
	protected void setNewSpaceColor(Color c) {
		control_1_0_1.setSpaceColor(c);
		control_1_0_2.setSpaceColor(c);
		panel5.setSpaceColor(c);
		control_8_1_1.setSpaceColor(c);
		control_8_2_1.setSpaceColor(c);
		control_8_3_1.setSpaceColor(c);
		panel11.setSpaceColor(c);
		control_11_1_1.setSpaceColor(c);
		control_11_1_2.setSpaceColor(c);
		panel12.setSpaceColor(c);
		control_14_1_0.setSpaceColor(c);
		control_14_2_0.setSpaceColor(c);
		control_14_3_0.setSpaceColor(c);
		control_14_4_0.setSpaceColor(c);
	}
	protected void setNewAlarmColors(Color fc, Color bc) {
		mainPanel.setAlarmColors(fc,bc);
		panel1.setAlarmColors(fc,bc);
		control_1_0_1.setAlarmColors(fc,bc);
		control_1_0_2.setAlarmColors(fc,bc);
		panel2.setAlarmColors(fc,bc);
		panel3.setAlarmColors(fc,bc);
		panel4.setAlarmColors(fc,bc);
		panel5.setAlarmColors(fc,bc);
		control_5_1_1.setAlarmColors(fc,bc);
		control_5_2_1.setAlarmColors(fc,bc);
		panel6.setAlarmColors(fc,bc);
		control_6_1_1.setAlarmColors(fc,bc);
		control_6_1_2.setAlarmColors(fc,bc);
		panel7.setAlarmColors(fc,bc);
		control_7_1_1.setAlarmColors(fc,bc);
		control_7_1_2.setAlarmColors(fc,bc);
		panel8.setAlarmColors(fc,bc);
		control_8_1_1.setAlarmColors(fc,bc);
		control_8_2_1.setAlarmColors(fc,bc);
		control_8_3_1.setAlarmColors(fc,bc);
		panel9.setAlarmColors(fc,bc);
		panel10.setAlarmColors(fc,bc);
		panel11.setAlarmColors(fc,bc);
		control_11_1_1.setAlarmColors(fc,bc);
		control_11_1_2.setAlarmColors(fc,bc);
		panel12.setAlarmColors(fc,bc);
		control_12_1_1.setAlarmColors(fc,bc);
		panel13.setAlarmColors(fc,bc);
		control_13_1_1.setAlarmColors(fc,bc);
		control_13_1_2.setAlarmColors(fc,bc);
		control_2_2_1.setAlarmColors(fc,bc);
		control_2_1_2.setAlarmColors(fc,bc);
		panel14.setAlarmColors(fc,bc);
		control_14_1_0.setAlarmColors(fc,bc);
		control_14_2_0.setAlarmColors(fc,bc);
		control_14_3_0.setAlarmColors(fc,bc);
		control_14_4_0.setAlarmColors(fc,bc);
	}
	protected void defineDefaultButton() {
		JRootPane root= panel1.getRootPane();
		if (root!=null) {
			root.setDefaultButton((JButton)control_14_4_0.component);
		}
	}
	public void registerSlotVariables() {
		registerPortsAndRecoverPortValues(
			this,
			targetWorld,
			new DialogEntry[] {
				new DialogEntry(this,7000,control_1_0_2,false),
				new DialogEntry(this,1010,control_5_1_1,false),
				new DialogEntry(this,1020,control_5_2_1,true),
				new DialogEntry(this,1030,control_6_1_1,true),
				new DialogEntry(this,1040,control_6_1_2,true),
				new DialogEntry(this,1050,control_7_1_1,true),
				new DialogEntry(this,1060,control_7_1_2,true),
				new DialogEntry(this,2010,bG8,true),
				new DialogEntry(this,3010,control_11_1_1,true),
				new DialogEntry(this,3020,control_11_1_2,true),
				new DialogEntry(this,4011,control_12_1_1,false,DialogEntryType.RANGE),
				new DialogEntry(this,4012,control_12_1_1,true),
				new DialogEntry(this,4021,control_13_1_1,false,DialogEntryType.RANGE),
				new DialogEntry(this,4022,control_13_1_1,true),
				new DialogEntry(this,4031,control_13_1_2,false,DialogEntryType.RANGE),
				new DialogEntry(this,4032,control_13_1_2,true),
				new DialogEntry(this,5011,control_2_2_1,false,DialogEntryType.RANGE),
				new DialogEntry(this,5012,control_2_2_1,true),
				new DialogEntry(this,5021,control_2_1_2,false,DialogEntryType.RANGE),
				new DialogEntry(this,5022,control_2_1_2,true)
			},
			new DialogEntry[] {
				new DialogEntry(this,"title",false,DialogEntryType.TITLE),
				new DialogEntry(this,"x",true,DialogEntryType.X),
				new DialogEntry(this,"y",true,DialogEntryType.Y),
				new DialogEntry(this,"text_color",false,DialogEntryType.TEXT_COLOR),
				new DialogEntry(this,"space_color",false,DialogEntryType.SPACE_COLOR),
				new DialogEntry(this,"background_color",false,DialogEntryType.BACKGROUND_COLOR),
				new DialogEntry(this,"font_name",false,DialogEntryType.FONT_NAME),
				new DialogEntry(this,"font_size",true,DialogEntryType.FONT_SIZE),
				new DialogEntry(this,"font_style",false,DialogEntryType.FONT_STYLE)
			});
	}
}