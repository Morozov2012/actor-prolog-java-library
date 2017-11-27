/*
 * @(#)ScalableImage.java 1.0 2013/08/15
 *
 * Copyright 2013 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableImage implementation for the Actor Prolog language
 * @version 1.0 2013/08/15
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.system.gui.dialogs.scalable.common.*;
import morozov.system.signals.*;
import morozov.terms.*;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

public class ScalableImage extends CustomControlComponent {
	//
	protected String address= "";
	protected IconImage scalableIcon;
	//
	///////////////////////////////////////////////////////////////
	//
	public ScalableImage(AbstractDialog tD, ChoisePoint iX, String text, double columns, double rows, boolean keepProportions, int anchor) {
		super(tD,columns,rows);
		address= text;
		String label= "";
		java.awt.image.BufferedImage image= null;
		if (!address.isEmpty()) {
			try {
				image= targetDialog.getTargetWorld().readImage(address,iX);
			} catch (Throwable e) {
				label= e.toString();
			}
		};
		scalableIcon= new IconImage(image,keepProportions,anchor);
		JLabel jL= new JLabel(label,scalableIcon,SwingConstants.LEADING);
		jL.setOpaque(false);
		component= jL;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new PrologString("");
		} else {
			return new PrologString(value.toString(iX));
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void putValue(Term value, ChoisePoint iX) {
		address= value.toString(iX);
		if (component!=null) {
			java.awt.image.BufferedImage image;
			if (address.isEmpty()) {
				image= null;
			} else {
				image= targetDialog.getTargetWorld().readImage(address,iX);
			};
			safelyPutValue(image);
			// targetDialog.invalidate();
			// Без команды repaint не меняется фотография
			// в примере test_117_26_image_01_jdk, если
			// диалоговое окно максимизировано.
			// targetDialog.repaint(); // 2013.09.04
			targetDialog.safelyInvalidateAndRepaint();
		}
	}
	protected void safelyPutValue(final java.awt.image.BufferedImage image) {
		if (SwingUtilities.isEventDispatchThread()) {
			quicklyPutValue(image);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						quicklyPutValue(image);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	protected void quicklyPutValue(java.awt.image.BufferedImage image) {
		try {
			scalableIcon.setImage(image);
			((JLabel)component).setText("");
		} catch (Throwable e) {
			String label= e.toString();
			scalableIcon.setImage(null);
			((JLabel)component).setText(label);
		}
	}
	//
	public Term getValue() {
		return new PrologString(address);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDimension(Dimension dimension) {
		if (component!=null) {
			scalableIcon.setSize(dimension);
		}
	}
}
