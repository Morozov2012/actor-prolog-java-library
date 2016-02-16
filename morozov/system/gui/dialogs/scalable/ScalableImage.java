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
import java.awt.Dimension;
import java.awt.Font;

public class ScalableImage extends ActiveComponent {
	//
	protected AbstractDialog dialog;
	protected String address= "";
	protected double width= 0;
	protected double height= 0;
	// protected boolean keepProportions;
	// protected int anchor;
	protected IconImage scalableIcon;
	//
	public ScalableImage(AbstractDialog tD, ChoisePoint iX, String text, double columns, double rows, boolean keepProportions, int anchor) {
		super(tD);
		dialog= tD;
		address= text;
		height= rows;
		width= columns;
		// keepProportions= flag;
		// anchor= a;
		// boolean enableAntialiasing= dialog.getTargetWorld().antialiasingIsEnabled(iX);
		String label= "";
		java.awt.image.BufferedImage image= null;
		if (!address.isEmpty()) {
			try {
				image= dialog.getTargetWorld().readImage(address,iX);
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
	// protected int getInitialTopBorder() {return 5;}
	// protected int getInitialLeftBorder() {return 5;}
	// protected int getInitialBottomBorder() {return 5;}
	// protected int getInitialRightBorder() {return 5;}
	//
	public void setFont(Font font) {
		super.setFont(font);
		if (component!=null) {
			component.setFont(font);
			Dimension dimension= LayoutUtils.computeDimension(font,component,width,height);
			scalableIcon.setSize(dimension);
		}
	}
	//
	public void putValue(Term value, ChoisePoint iX) {
		address= value.toString(iX);
		if (component!=null) {
			if (address.isEmpty()) {
				scalableIcon.setImage(null);
				((JLabel)component).setText("");
			} else {
				try {
					java.awt.image.BufferedImage image= dialog.getTargetWorld().readImage(address,iX);
					scalableIcon.setImage(image);
					((JLabel)component).setText("");
				} catch (Throwable e) {
					String label= e.toString();
					scalableIcon.setImage(null);
					((JLabel)component).setText(label);
				}
			};
			// dialog.invalidate();
			// Без команды repaint не меняется фотография
			// в примере test_117_26_image_01_jdk, если
			// диалоговое окно максимизировано.
			// dialog.repaint(); // 2013.09.04
			dialog.safelyInvalidateAndRepaint();
		}
	}
	//
	public Term getValue() {
		return new PrologString(address);
	}
	//
	public Term standardizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new PrologString("");
		} else {
			return new PrologString(value.toString(iX));
		}
	}
}
