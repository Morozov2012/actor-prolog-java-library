/*
 * @(#)RunTimeErrorDialog.java 1.0 2011/10/20
 *
 * (c) 2011 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.special;

import target.*;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.worlds.*;

import javax.swing.JDialog;

import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dialog.ModalityType;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;

import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RunTimeErrorDialog extends JDialog implements ActionListener {
	ThreadHolder processHolder;
	Throwable error;
	String errorName;
	long textPosition;
	String position;
	long unitCode;
	String unitName;
	int sourceFileNumber;
	String sourceFileName;
	String stackTrace;
	JPanel mainPanel;
	GridBagLayout mainPanelLayout;
	JButton control_5_3_0;
	JButton control_5_2_0;
	JButton control_5_1_0;
	JPanel panel5;
	JTextArea area_3_2_5;
	JScrollPane control_3_2_5;
	JLabel control_3_1_5;
	JTextArea area_3_2_4;
	JScrollPane control_3_2_4;
	JLabel control_3_1_4;
	JButton control_4_2_3;
	JTextField control_4_1_3;
	JPanel panel4;
	JLabel control_3_1_3;
	JTextField control_3_2_2;
	JLabel control_3_1_2;
	JTextArea area_3_2_1;
	JScrollPane control_3_2_1;
	JLabel control_3_1_1;
	JPanel panel3;
	JPanel panel2;	// Titled
	JPanel panel1;
	GridBagLayout gBL3;
	GridBagConstraints gBC3;
	boolean hasCompactMode= true;
	int horizontalInset= 12; // 10;
	int verticalInset= 17; // 15;
	int textAreaWidth= 55; // 32; // 45; // 32
	public RunTimeErrorDialog(ThreadHolder listener, Window w, Throwable e, long p, long u, int s) {
		super(w);
		processHolder= listener;
		error= e;
		errorName= error.toString();
		textPosition= p;
		if (textPosition >= 0) {
			position= String.format("%d",textPosition);
		} else {
			position= "no debug information";
		};
		unitCode= u;
		if (unitCode >= 0) {
			SymbolName name= SymbolNames.retrieveSymbolName(unitCode);
			unitName= name.extractClassName();
		} else {
			unitName= "no debug information";
		};
		sourceFileNumber= s;
		if (sourceFileNumber > 0) {
			sourceFileName= DefaultOptions.programSourceFiles[sourceFileNumber-1];
		} else {
			sourceFileName= "no debug information";
		};
		ByteArrayOutputStream byteArray= new ByteArrayOutputStream();
		PrintWriter writer= new PrintWriter(byteArray);
		error.printStackTrace(writer);
		writer.flush();
		stackTrace= byteArray.toString();
	}
	public void activate() {
		assemble();
		pack();
		setNewFont(getFont());
		setModalityType(ModalityType.DOCUMENT_MODAL);
		pack();
		SpecialUtils.centre(this);
		// setVisible(true);
		DesktopUtils.safelySetVisible(true,this);
	}
	//
	public void assemble() {
		setResizable(false);
		setTitle("Run Time Error");
		mainPanel= new JPanel();
		mainPanelLayout= new GridBagLayout();
		mainPanel.setLayout(mainPanelLayout);
		GridBagConstraints mainPanelConstraints= new GridBagConstraints();
		mainPanelConstraints.ipadx= 0;
		mainPanelConstraints.ipady= 0;
		panel1= new JPanel();
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
		mainPanelConstraints.insets= new Insets(verticalInset,horizontalInset,verticalInset,horizontalInset);
		mainPanelLayout.setConstraints(panel1,mainPanelConstraints);
		mainPanel.add(panel1);
		panel2= new JPanel(); // Titled
		Border border= BorderFactory.createTitledBorder("");
		panel2.setBorder(border);
		GridBagLayout gBL2= new GridBagLayout();
		panel2.setLayout(gBL2);
		GridBagConstraints gBC2= new GridBagConstraints();
		gBC2.ipadx= 0;
		gBC2.ipady= 0;
		//
		gBC1.gridy= 1;
		gBC1.gridx= 0;
		gBC1.anchor= GridBagConstraints.CENTER;
		gBC1.fill= GridBagConstraints.HORIZONTAL;
		gBC1.weightx= 1;
		gBC1.weighty= 1;
		gBC1.insets= new Insets(0,0,verticalInset,0);
		gBL1.setConstraints(panel2,gBC1);
		panel1.add(panel2);
		panel3= new JPanel();
		// GridBagLayout gBL3= new GridBagLayout();
		gBL3= new GridBagLayout();
		panel3.setLayout(gBL3);
		// GridBagConstraints gBC3= new GridBagConstraints();
		gBC3= new GridBagConstraints();
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		//
		gBC2.gridy= 1;
		gBC2.gridx= 0;
		gBC2.anchor= GridBagConstraints.CENTER;
		gBC2.fill= GridBagConstraints.HORIZONTAL;
		gBC2.weightx= 1;
		gBC2.weighty= 1;
		gBC2.insets= new Insets(verticalInset,horizontalInset,verticalInset,horizontalInset);
		gBL2.setConstraints(panel3,gBC2);
		panel2.add(panel3);
		gBC3.gridy= 1;
		gBC3.gridx= 1;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		control_3_1_1= new JLabel("Error");
		gBC3.insets= new Insets(0,0,verticalInset,horizontalInset);
		gBL3.setConstraints(control_3_1_1,gBC3);
		panel3.add(control_3_1_1);
		gBC3.gridy= 1;
		gBC3.gridx= 2;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		control_3_2_1= new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		area_3_2_1= new JTextArea(errorName,5,textAreaWidth);
		area_3_2_1.setEditable(false);
		area_3_2_1.setLineWrap(true);
		control_3_2_1.setViewportView(area_3_2_1);
		gBC3.insets= new Insets(0,0,verticalInset,0);
		gBL3.setConstraints(control_3_2_1,gBC3);
		panel3.add(control_3_2_1);
		gBC3.gridy= 2;
		gBC3.gridx= 1;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		control_3_1_2= new JLabel("Class name");
		gBC3.insets= new Insets(0,0,verticalInset,horizontalInset);
		gBL3.setConstraints(control_3_1_2,gBC3);
		panel3.add(control_3_1_2);
		gBC3.gridy= 2;
		gBC3.gridx= 2;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		control_3_2_2= new JTextField(unitName,textAreaWidth);
		control_3_2_2.setEditable(false);
		gBC3.insets= new Insets(0,0,verticalInset,0);
		gBL3.setConstraints(control_3_2_2,gBC3);
		panel3.add(control_3_2_2);
		gBC3.gridy= 3;
		gBC3.gridx= 1;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		control_3_1_3= new JLabel("Text position");
		gBC3.insets= new Insets(0,0,verticalInset,horizontalInset);
		gBL3.setConstraints(control_3_1_3,gBC3);
		panel3.add(control_3_1_3);
		panel4= new JPanel();
		GridBagLayout gBL4= new GridBagLayout();
		panel4.setLayout(gBL4);
		GridBagConstraints gBC4= new GridBagConstraints();
		gBC4.ipadx= 0;
		gBC4.ipady= 0;
		//
		gBC3.gridy= 3;
		gBC3.gridx= 2;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.insets= new Insets(0,0,verticalInset,0);
		gBL3.setConstraints(panel4,gBC3);
		panel3.add(panel4);
		gBC4.gridy= 3;
		gBC4.gridx= 1;
		gBC4.anchor= GridBagConstraints.CENTER;
		gBC4.fill= GridBagConstraints.NONE;
		gBC4.weightx= 1;
		gBC4.weighty= 1;
		gBC4.ipadx= 0;
		gBC4.ipady= 0;
		control_4_1_3= new JTextField(position,12/*20*/);
		control_4_1_3.setEditable(false);
		gBC4.insets= new Insets(0,0,0,horizontalInset);
		gBL4.setConstraints(control_4_1_3,gBC4);
		panel4.add(control_4_1_3);
		gBC4.gridy= 3;
		gBC4.gridx= 2;
		gBC4.anchor= GridBagConstraints.CENTER;
		gBC4.fill= GridBagConstraints.NONE;
		gBC4.weightx= 1;
		gBC4.weighty= 1;
		gBC4.ipadx= 0;
		gBC4.ipady= 0;
		control_4_2_3= new JButton("Go to Position");
		gBC4.insets= new Insets(0,0,0,0);
		control_4_2_3.setMnemonic('P');
		control_4_2_3.setDisplayedMnemonicIndex(6);
		control_4_2_3.setActionCommand("goto");
		control_4_2_3.addActionListener(this);
		gBL4.setConstraints(control_4_2_3,gBC4);
		panel4.add(control_4_2_3);
		//
		control_3_1_4= new JLabel("Source file");
		control_3_2_4= new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		area_3_2_4= new JTextArea(sourceFileName,5,textAreaWidth);
		area_3_2_4.setEditable(false);
		area_3_2_4.setLineWrap(true);
		control_3_2_4.setViewportView(area_3_2_4);
		tuneSourceFileAreaInsets();
		panel3.add(control_3_1_4);
		panel3.add(control_3_2_4);
		//
		gBC3.gridy= 5;
		gBC3.gridx= 1;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		control_3_1_5= new JLabel("Stack trace");
		gBC3.insets= new Insets(0,0,0,horizontalInset);
		gBL3.setConstraints(control_3_1_5,gBC3);
		panel3.add(control_3_1_5);
		gBC3.gridy= 5;
		gBC3.gridx= 2;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		// control_3_2_5= new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		control_3_2_5= new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// area_3_2_5= new JTextArea(stackTrace,7,textAreaWidth);
		area_3_2_5= new JTextArea(stackTrace,10,textAreaWidth);
		area_3_2_5.setEditable(false);
		// area_3_2_5.setLineWrap(true);
		area_3_2_5.setLineWrap(false);
		control_3_2_5.setViewportView(area_3_2_5);
		gBC3.insets= new Insets(0,0,0,0);
		gBL3.setConstraints(control_3_2_5,gBC3);
		panel3.add(control_3_2_5);
		tuneStackTraceAreaVisibility();
		//
		panel5= new JPanel();
		GridBagLayout gBL5= new GridBagLayout();
		panel5.setLayout(gBL5);
		GridBagConstraints gBC5= new GridBagConstraints();
		gBC5.ipadx= 0;
		gBC5.ipady= 0;
		//
		gBC1.gridy= 2;
		gBC1.gridx= 0;
		gBC1.anchor= GridBagConstraints.CENTER;
		gBC1.fill= GridBagConstraints.HORIZONTAL;
		gBC1.weightx= 1;
		gBC1.weighty= 1;
		gBC1.insets= new Insets(0,0,0,0);
		gBL1.setConstraints(panel5,gBC1);
		panel1.add(panel5);
		gBC5.gridy= 0;
		gBC5.gridx= 1;
		gBC5.anchor= GridBagConstraints.WEST;
		gBC5.fill= GridBagConstraints.NONE;
		gBC5.weightx= 1;
		gBC5.weighty= 1;
		gBC5.ipadx= 0;
		gBC5.ipady= 0;
		control_5_1_0= new JButton("Stop program");
		gBC5.insets= new Insets(0,0,0,horizontalInset);
		control_5_1_0.setMnemonic('S');
		control_5_1_0.setDisplayedMnemonicIndex(0);
		control_5_1_0.setActionCommand("stop");
		control_5_1_0.addActionListener(this);
		gBL5.setConstraints(control_5_1_0,gBC5);
		panel5.add(control_5_1_0);
		gBC5.gridy= 0;
		gBC5.gridx= 2;
		gBC5.anchor= GridBagConstraints.CENTER;
		gBC5.fill= GridBagConstraints.NONE;
		gBC5.weightx= 1;
		gBC5.weighty= 1;
		gBC5.ipadx= 0;
		gBC5.ipady= 0;
		control_5_2_0= new JButton("Continue execution");
		gBC5.insets= new Insets(0,0,0,horizontalInset);
		control_5_2_0.setMnemonic('C');
		control_5_2_0.setDisplayedMnemonicIndex(0);
		control_5_2_0.setActionCommand("continue");
		control_5_2_0.addActionListener(this);
		gBL5.setConstraints(control_5_2_0,gBC5);
		panel5.add(control_5_2_0);
		gBC5.gridy= 0;
		gBC5.gridx= 3;
		gBC5.anchor= GridBagConstraints.EAST;
		gBC5.fill= GridBagConstraints.NONE;
		gBC5.weightx= 1;
		gBC5.weighty= 1;
		gBC5.ipadx= 0;
		gBC5.ipady= 0;
		control_5_3_0= new JButton("");
		tuneDialogModeButton();
		gBC5.insets= new Insets(0,0,0,0);
		control_5_3_0.setMnemonic('M');
		control_5_3_0.setDisplayedMnemonicIndex(0);
		control_5_3_0.setActionCommand("switch_mode");
		control_5_3_0.addActionListener(this);
		gBL5.setConstraints(control_5_3_0,gBC5);
		panel5.add(control_5_3_0);
		add(mainPanel);
	}
	//
	protected void tuneSourceFileAreaInsets() {
		gBC3.gridy= 4;
		gBC3.gridx= 1;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		// control_3_1_4= new JLabel("Source file");
		if (hasCompactMode) {
			gBC3.insets= new Insets(0,0,0,horizontalInset);
		} else {
			gBC3.insets= new Insets(0,0,verticalInset,horizontalInset);
		};
		gBL3.setConstraints(control_3_1_4,gBC3);
		//
		gBC3.gridy= 4;
		gBC3.gridx= 2;
		gBC3.anchor= GridBagConstraints.NORTHWEST;
		gBC3.fill= GridBagConstraints.NONE;
		gBC3.weightx= 1;
		gBC3.weighty= 1;
		gBC3.ipadx= 0;
		gBC3.ipady= 0;
		// control_3_2_4= new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// area_3_2_4= new JTextArea(sourceFileName,5,textAreaWidth);
		// area_3_2_4.setEditable(false);
		// area_3_2_4.setLineWrap(true);
		// control_3_2_4.setViewportView(area_3_2_4);
		if (hasCompactMode) {
			gBC3.insets= new Insets(0,0,0,0);
		} else {
			gBC3.insets= new Insets(0,0,verticalInset,0);
		}
		gBL3.setConstraints(control_3_2_4,gBC3);
	}
	//
	protected void tuneStackTraceAreaVisibility() {
		if (hasCompactMode) {
			// control_3_2_5.setVisible(false);
			DesktopUtils.safelySetVisible(false,control_3_2_5);
			// control_3_1_5.setVisible(false);
			DesktopUtils.safelySetVisible(false,control_3_1_5);
		} else {
			// control_3_2_5.setVisible(true);
			DesktopUtils.safelySetVisible(true,control_3_2_5);
			// control_3_1_5.setVisible(true);
			DesktopUtils.safelySetVisible(true,control_3_1_5);
		}
	}
	//
	protected void tuneDialogModeButton() {
		if (hasCompactMode) {
			control_5_3_0.setText("More info");
		} else {
			control_5_3_0.setText("Compact info");
		}
	}
	//
	protected void setNewFont(Font newFont) {
		mainPanel.setFont(newFont);
		panel1.setFont(newFont);
		panel2.setFont(newFont);
		panel3.setFont(newFont);
		control_3_1_1.setFont(newFont);
		area_3_2_1.setFont(newFont);
		control_3_2_1.setFont(newFont);
		control_3_1_2.setFont(newFont);
		control_3_2_2.setFont(newFont);
		control_3_1_3.setFont(newFont);
		panel4.setFont(newFont);
		control_4_1_3.setFont(newFont);
		control_4_2_3.setFont(newFont);
		control_3_1_4.setFont(newFont);
		area_3_2_4.setFont(newFont);
		control_3_2_4.setFont(newFont);
		control_3_1_5.setFont(newFont);
		area_3_2_5.setFont(newFont);
		control_3_2_5.setFont(newFont);
		panel5.setFont(newFont);
		control_5_1_0.setFont(newFont);
		control_5_2_0.setFont(newFont);
		control_5_3_0.setFont(newFont);
	}
	//
	public void actionPerformed(ActionEvent e) {
		if ("switch_mode".equals(e.getActionCommand())) {
			if (hasCompactMode) {
				hasCompactMode= false;
			} else {
				hasCompactMode= true;
			};
			tuneSourceFileAreaInsets();
			tuneStackTraceAreaVisibility();
			tuneDialogModeButton();
			pack();
			SpecialUtils.centre(this);
		} else if ("goto".equals(e.getActionCommand())) {
			processHolder.reportErrorPosition(error,textPosition,unitCode,sourceFileNumber);
		} else if ("stop".equals(e.getActionCommand())) {
			// setVisible(false);
			DesktopUtils.safelySetVisible(false,this);
			processHolder.stopProgram(error,textPosition,unitCode,sourceFileNumber);
		} else if ("continue".equals(e.getActionCommand())) {
			// setVisible(false);
			DesktopUtils.safelySetVisible(false,this);
		}
	}
}
