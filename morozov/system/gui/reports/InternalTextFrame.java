// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import morozov.classes.*;
import morozov.system.gui.*;
import morozov.terms.*;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.JScrollPane;
import javax.swing.text.StyleConstants;
import java.awt.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class InternalTextFrame extends InnerPage {
	//
	public TextPaneNoWrap panel;
	public JScrollPane scrollPane;
	public AtomicReference<Term> textColor= new AtomicReference<Term>();
	public AtomicReference<Term> spaceColor= new AtomicReference<Term>();
	//
	protected AtomicReference<MutableAttributeSet> rootTextStyle= new AtomicReference<MutableAttributeSet>(new SimpleAttributeSet());
	protected Map<Color,MutableAttributeSet> transparentTextStyles= Collections.synchronizedMap(new HashMap<Color,MutableAttributeSet>());
	//
	public InternalTextFrame(String title, StaticContext context) {
		super(title);
		panel= new TextPaneNoWrap(context);
		panel.setEditable(false);
		panel.setAutoscrolls(true);
		scrollPane= new JScrollPane(panel);
		panel.initiate(scrollPane);
		add(scrollPane);
	}
	//
	public void updateRootTextStyleUnderline(boolean isUnderlined) {
		synchronized(this) {
			MutableAttributeSet style= rootTextStyle.get();
			StyleConstants.setUnderline(style,isUnderlined);
			rootTextStyle.set(style);
		}
	}
	public void updateRootTextStyleBackground(Color color) {
		synchronized(this) {
			MutableAttributeSet style= rootTextStyle.get();
			StyleConstants.setBackground(style,color);
			rootTextStyle.set(style);
		}
	}
	//
	public MutableAttributeSet getOpaqueTextStyle(Color textColor, Color spaceColor) {
		MutableAttributeSet textStyle= new SimpleAttributeSet();
		textStyle.setResolveParent(rootTextStyle.get());
		StyleConstants.setForeground(textStyle,textColor);
		StyleConstants.setBackground(textStyle,spaceColor);
		return textStyle;
	}
	//
	public MutableAttributeSet getTransparentTextStyle(Color textColor) {
		MutableAttributeSet textStyle= transparentTextStyles.get(textColor);
		if (textStyle==null) {
			textStyle= new SimpleAttributeSet();
			textStyle.setResolveParent(rootTextStyle.get());
			StyleConstants.setForeground(textStyle,textColor);
			transparentTextStyles.put(textColor,textStyle);
		};
		return textStyle;
	}
}
