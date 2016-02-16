/*
 * @(#)InputDialog.java 1.0 2011/02/12
 *
 * (c) 2011 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.special;

import morozov.system.*;
import morozov.system.gui.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowAdapter;

public class InputDialog extends JDialog implements PropertyChangeListener {
	//
	protected boolean integerNumberExpected;
	protected String errorMessage;
	protected String typedText= null;
	protected JTextField textField; // = new JTextField(10);
	protected JOptionPane optionPane;
	//
	public String getValidatedText() {
		return typedText;
	}
	//
	public InputDialog(boolean mode, String title, String prompt, String initialValue, String message) {
		super(JOptionPane.getRootFrame(),title,true);
		integerNumberExpected= mode;
		textField= new JTextField(initialValue,10);
		errorMessage= message;
		Object[] targetControls= {prompt,textField};
		optionPane= new JOptionPane(targetControls,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
		setContentPane(optionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
				}
			});
		addComponentListener(
			new ComponentAdapter() {
				public void componentShown(ComponentEvent ce) {
					textField.requestFocusInWindow();
				}
			});
		optionPane.addPropertyChangeListener(this);
		pack();
		SpecialUtils.centre(this);
		// setVisible(true);
		DesktopUtils.safelySetVisible(true,this);
	}
	//
	public void propertyChange(PropertyChangeEvent e) {
		String prop= e.getPropertyName();
		if (		isShowing() &&
				(e.getSource() == optionPane) &&
				(	prop.equals(JOptionPane.VALUE_PROPERTY) ||
					prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))
				) {
			Object value= optionPane.getValue();
			if (value==null) {
				return;
			} else if (value instanceof Integer) {
				int answer= (Integer)value;
				optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
				if (answer==JOptionPane.OK_OPTION) {
					typedText= textField.getText();
					try {
						if (integerNumberExpected) {
							BigInteger result= Converters.stringToStrictInteger(typedText);
						} else {
							double result= Converters.stringToReal(typedText);
							if (Double.isNaN(result)) {
								throw TermIsNotAReal.instance;
							} else if (Double.isInfinite(result)) {
								throw TermIsNotAReal.instance;
							}
						};
						clearAndHide();
					} catch (TermIsNotAnInteger error) {
						processSyntaxError();
					} catch (TermIsNotAReal error) {
						processSyntaxError();
					} catch (Throwable error) {
						processSyntaxError();
					}
				} else {
					typedText= null;
					clearAndHide();
				}
			} else {
				return;
			}
		}
	}
	protected void processSyntaxError() {
		textField.selectAll();
		JOptionPane.showMessageDialog(
			InputDialog.this,
			errorMessage,
			"",
			JOptionPane.ERROR_MESSAGE);
		typedText= null;
		textField.requestFocusInWindow();
	}
	protected void clearAndHide() {
		textField.setText(null);
		// setVisible(false);
		DesktopUtils.safelySetVisible(false,this);
	}
}
