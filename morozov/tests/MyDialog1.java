// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.tests;

import target.*;

import morozov.run.*;
import morozov.system.*;
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
	//
	protected ScalableButton control_14_4_0;
	protected ScalableButton control_14_3_0;
	protected ScalableButton control_14_2_0;
	protected ScalableButton control_14_1_0;
	protected ScalablePanel panel14;
	protected ScalableSlider control_2_1_2;
	protected ScalableSlider control_2_2_1;
	protected ScalableListButton control_13_1_2;
	protected ScalableComboBox control_13_1_1;
	protected ScalablePanel panel13;
	protected ScalableList control_12_1_1;
	protected ScalableTitledPanel panel12;
	protected ScalableCheckBox control_11_1_2;
	protected ScalableCheckBox control_11_1_1;
	protected ScalableTitledPanel panel11;
	protected ScalablePanel panel10;
	protected ScalablePanel panel9;
	protected ScalableButtonGroup bG8;
	protected ScalableRadioButton control_8_3_1;
	protected ScalableRadioButton control_8_2_1;
	protected ScalableRadioButton control_8_1_1;
	protected ScalablePanel panel8;
	protected RealField control_7_1_2;
	protected IntegerField control_7_1_1;
	protected ScalablePanel panel7;
	protected ActivePasswordField control_6_1_2;
	protected ActiveTextField control_6_1_1;
	protected ScalablePanel panel6;
	protected ScalableTextArea control_5_2_1;
	protected ScalableTextArea control_5_1_1;
	protected ScalableTitledPanel panel5;
	protected ScalablePanel panel4;
	protected ScalablePanel panel3;
	protected ScalablePanel panel2;
	protected ScalableLabel control_1_0_2;
	protected ScalableLabel control_1_0_1;
	protected ScalablePanel panel1;
	//
	public MyDialog1(YesNoDefault isModal, boolean isTopLevel, boolean isAlwaysOnTop, boolean closingConfirmation, YesNoDefault exitOnClose, Window parent) {
		super(isModal,isTopLevel,isAlwaysOnTop,closingConfirmation,exitOnClose,parent);
	}
	//
	@Override
	protected String getPredefinedTitle() {
		return "Demo Panel";
	}
	@Override
	protected Term getPredefinedTextColor() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	@Override
	protected Term getPredefinedSpaceColor() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	@Override
	protected Term getPredefinedFontName() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	@Override
	protected Term getPredefinedFontSize() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	@Override
	protected Term getPredefinedFontStyle() {
		return new PrologSymbol(SymbolCodes.symbolCode_E_default);
	}
	@Override
	protected Term getPredefinedX() {
		return new PrologReal(5);
	}
	@Override
	protected Term getPredefinedY() {
		return new PrologReal(5);
	}
	@Override
	protected Term getPredefinedBackgroundColor() {
		return new PrologString("Emerald");
	}
	@Override
	public void assemble(ChoisePoint iX) {
		//
		// mainPanel
		//
		mainPanel= new ScalablePanel(this);
		mainPanelLayout= new GridBagLayout();
		mainPanel.setLayout(mainPanelLayout);
		GridBagConstraints mainPanelConstraints= new GridBagConstraints();
		mainPanelConstraints.ipadx= 0;
		mainPanelConstraints.ipady= 0;
		//
		// panel1
		//
		panel1= new ScalablePanel(this);
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
		panel2= new ScalablePanel(this);
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
		panel3= new ScalablePanel(this);
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
		panel4= new ScalablePanel(this);
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
		panel5= new ScalableTitledPanel(this,"Text Areas");
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
		panel6= new ScalablePanel(this);
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
		panel7= new ScalablePanel(this);
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
		panel8= new ScalablePanel(this);
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
		panel9= new ScalablePanel(this);
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
		panel10= new ScalablePanel(this);
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
		panel11= new ScalableTitledPanel(this,"Checkboxes");
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
		panel12= new ScalableTitledPanel(this,"List Controls");
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
		panel13= new ScalablePanel(this);
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
		panel14= new ScalablePanel(this);
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
	@Override
	protected void setGeneralFont(Font font) {
		mainPanel.setGeneralFont(font);
		panel1.setGeneralFont(font);
		control_1_0_1.setGeneralFont(font);
		control_1_0_2.setGeneralFont(font);
		panel2.setGeneralFont(font);
		panel3.setGeneralFont(font);
		panel4.setGeneralFont(font);
		panel5.setGeneralFont(font);
		control_5_1_1.setGeneralFont(font);
		control_5_2_1.setGeneralFont(font);
		panel6.setGeneralFont(font);
		control_6_1_1.setGeneralFont(font);
		control_6_1_2.setGeneralFont(font);
		panel7.setGeneralFont(font);
		control_7_1_1.setGeneralFont(font);
		control_7_1_2.setGeneralFont(font);
		panel8.setGeneralFont(font);
		control_8_1_1.setGeneralFont(font);
		control_8_2_1.setGeneralFont(font);
		control_8_3_1.setGeneralFont(font);
		panel9.setGeneralFont(font);
		panel10.setGeneralFont(font);
		panel11.setGeneralFont(font);
		control_11_1_1.setGeneralFont(font);
		control_11_1_2.setGeneralFont(font);
		panel12.setGeneralFont(font);
		control_12_1_1.setGeneralFont(font);
		panel13.setGeneralFont(font);
		control_13_1_1.setGeneralFont(font);
		control_13_1_2.setGeneralFont(font);
		control_2_2_1.setGeneralFont(font);
		control_2_1_2.setGeneralFont(font);
		panel14.setGeneralFont(font);
		control_14_1_0.setGeneralFont(font);
		control_14_2_0.setGeneralFont(font);
		control_14_3_0.setGeneralFont(font);
		control_14_4_0.setGeneralFont(font);
	}
	@Override
	protected void setGeneralBackground(Color c) {
		super.setGeneralBackground(c);
		if (!isDraftMode) {
			mainPanel.setGeneralBackground(c);
		};
		if (!isDraftMode) {
			panel1.setGeneralBackground(c);
		};
		control_1_0_1.setGeneralBackground(c);
		control_1_0_2.setGeneralBackground(c);
		if (!isDraftMode) {
			panel2.setGeneralBackground(c);
		};
		if (!isDraftMode) {
			panel3.setGeneralBackground(c);
		};
		if (!isDraftMode) {
			panel4.setGeneralBackground(c);
		};
		if (!isDraftMode) {
			panel5.setGeneralBackground(c);
		};
		control_5_1_1.setGeneralBackground(c);
		control_5_2_1.setGeneralBackground(c);
		if (!isDraftMode) {
			panel6.setGeneralBackground(c);
		};
		control_6_1_1.setGeneralBackground(c);
		control_6_1_2.setGeneralBackground(c);
		if (!isDraftMode) {
			panel7.setGeneralBackground(c);
		};
		control_7_1_1.setGeneralBackground(c);
		control_7_1_2.setGeneralBackground(c);
		if (!isDraftMode) {
			panel8.setGeneralBackground(c);
		};
		control_8_1_1.setGeneralBackground(c);
		control_8_2_1.setGeneralBackground(c);
		control_8_3_1.setGeneralBackground(c);
		if (!isDraftMode) {
			panel9.setGeneralBackground(c);
		};
		if (!isDraftMode) {
			panel10.setGeneralBackground(c);
		};
		if (!isDraftMode) {
			panel11.setGeneralBackground(c);
		};
		control_11_1_1.setGeneralBackground(c);
		control_11_1_2.setGeneralBackground(c);
		if (!isDraftMode) {
			panel12.setGeneralBackground(c);
		};
		control_12_1_1.setGeneralBackground(c);
		if (!isDraftMode) {
			panel13.setGeneralBackground(c);
		};
		control_13_1_1.setGeneralBackground(c);
		control_13_1_2.setGeneralBackground(c);
		control_2_2_1.setGeneralBackground(c);
		control_2_1_2.setGeneralBackground(c);
		if (!isDraftMode) {
			panel14.setGeneralBackground(c);
		};
		control_14_1_0.setGeneralBackground(c);
		control_14_2_0.setGeneralBackground(c);
		control_14_3_0.setGeneralBackground(c);
		control_14_4_0.setGeneralBackground(c);
	}
	@Override
	protected void setGeneralForeground(Color c) {
		mainPanel.setGeneralForeground(c);
		panel1.setGeneralForeground(c);
		control_1_0_1.setGeneralForeground(c);
		control_1_0_2.setGeneralForeground(c);
		panel2.setGeneralForeground(c);
		panel3.setGeneralForeground(c);
		panel4.setGeneralForeground(c);
		panel5.setGeneralForeground(c);
		control_5_1_1.setGeneralForeground(c);
		control_5_2_1.setGeneralForeground(c);
		panel6.setGeneralForeground(c);
		control_6_1_1.setGeneralForeground(c);
		control_6_1_2.setGeneralForeground(c);
		panel7.setGeneralForeground(c);
		control_7_1_1.setGeneralForeground(c);
		control_7_1_2.setGeneralForeground(c);
		panel8.setGeneralForeground(c);
		control_8_1_1.setGeneralForeground(c);
		control_8_2_1.setGeneralForeground(c);
		control_8_3_1.setGeneralForeground(c);
		panel9.setGeneralForeground(c);
		panel10.setGeneralForeground(c);
		panel11.setGeneralForeground(c);
		control_11_1_1.setGeneralForeground(c);
		control_11_1_2.setGeneralForeground(c);
		panel12.setGeneralForeground(c);
		control_12_1_1.setGeneralForeground(c);
		panel13.setGeneralForeground(c);
		control_13_1_1.setGeneralForeground(c);
		control_13_1_2.setGeneralForeground(c);
		control_2_2_1.setGeneralForeground(c);
		control_2_1_2.setGeneralForeground(c);
		panel14.setGeneralForeground(c);
		control_14_1_0.setGeneralForeground(c);
		control_14_2_0.setGeneralForeground(c);
		control_14_3_0.setGeneralForeground(c);
		control_14_4_0.setGeneralForeground(c);
	}
	@Override
	protected void setGeneralSpaceColor(Color c) {
		control_1_0_1.setGeneralSpaceColor(c);
		control_1_0_2.setGeneralSpaceColor(c);
		panel5.setGeneralSpaceColor(c);
		control_8_1_1.setGeneralSpaceColor(c);
		control_8_2_1.setGeneralSpaceColor(c);
		control_8_3_1.setGeneralSpaceColor(c);
		panel11.setGeneralSpaceColor(c);
		control_11_1_1.setGeneralSpaceColor(c);
		control_11_1_2.setGeneralSpaceColor(c);
		panel12.setGeneralSpaceColor(c);
		control_14_1_0.setGeneralSpaceColor(c);
		control_14_2_0.setGeneralSpaceColor(c);
		control_14_3_0.setGeneralSpaceColor(c);
		control_14_4_0.setGeneralSpaceColor(c);
	}
	@Override
	protected void setAlarmColors(Color fc, Color bc) {
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
	@Override
	protected void defineDefaultButton() {
		JRootPane root= panel1.getRootPane();
		if (root!=null) {
			root.setDefaultButton((JButton)control_14_4_0.component);
		}
	}
	@Override
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
