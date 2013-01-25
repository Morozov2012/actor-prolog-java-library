/*
 * @(#)ScalableTitledPanel.java 1.0 2010/04/28
 *
 * Copyright 2010 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable;

/*
 * ScalableTitledPanel implementation for the Actor Prolog language
 * @version 1.0 2010/04/28
 * @author IRE RAS Alexei A. Morozov
*/

// import morozov.classes.*;
// import morozov.system.gui.dialogs.common.*;
// import morozov.terms.*;

import javax.swing.border.*;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.Color;

import java.util.Map;
import java.util.HashMap;

public class ScalableTitledPanel extends ScalablePanel {
	protected TitledBorder border;
	protected Font colourlessFont;
	protected Color textColor;
	protected Color spaceColor;
	public ScalableTitledPanel(String title) {
		border= BorderFactory.createTitledBorder(title);
		setBorder(border);
	}
	public void setForeground(Color c) {
	}
	// public void setForeground(Color c) {
	//	super.setForeground(c);
	//	textColor= c;
	//	if (border!=null) {
	//		setFont(colourlessFont);
	//	};
	//	repaint();
	// }
	public void setSpaceColor(Color c) {
		spaceColor= c;
		if (border!=null) {
			setFont(colourlessFont);
		};
		// invalidate();
		repaint();
	}
	public void setFont(Font font) {
		super.setFont(font);
		colourlessFont= font;
		if (border!=null) {
			if (textColor!=null || spaceColor!=null) {
				Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
				if (textColor!=null) {
					map.put(TextAttribute.FOREGROUND,textColor);
				};
				font= font.deriveFont(map);
			};
			border.setTitleFont(font);
		}
	}
	// public boolean isTitledPanel() {
	//	return true;
	// }
}
