// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;

import java.util.HashMap;
import java.awt.font.TextAttribute;
import java.awt.Font;

public class DiagramPath {
	public String mother;
	public double x;
	public double y;
	public String[] label;
	public DiagramArc[] arcs;
	public DiagramPath(String motherIdentifier, double xx, double yy, String[] text, DiagramArc[] arcList) {
		mother= motherIdentifier;
		x= xx;
		y= yy;
		label= text;
		arcs= arcList;
	}
	public void correctArcs(DiagramBox[] boxes) {
		int nArcs= arcs.length;
		if (nArcs > 0 && boxes.length > 0) {
			arcs[0].correctStartPoint(boxes);
			arcs[nArcs-1].correctEndPoint(boxes);
		}
	}
	public void drawLine(Graphics g0, Dimension size) {
		for (int n=0; n < arcs.length; n++) {
			arcs[n].draw(g0,size);
		};
	}
	public void drawLabel(Graphics g0, Dimension size, DiagramColors diagramColors) {
		if (x <= Double.MIN_VALUE && y <= Double.MIN_VALUE) {
			return;
		} else if (label.length > 0) {
			Graphics2D g2= (Graphics2D)g0;
			double realX= StrictMath.round(size.width * x);
			double realY= StrictMath.round(size.height * y);
			//
			FontMetrics metrics= g2.getFontMetrics();
			double textWidth= DiagramUtils.getTextWidth(g2,label);
			double textHeight= DiagramUtils.getTextHeight(g2,label);
			double firstLineHeight= DiagramUtils.getLineHeight(g2,label[0]);
			double extraLeading= DiagramUtils.computeExtraLeading(firstLineHeight);
			//
			int numberOfExtraLines= label.length - 1;
			//
			int borderWidth= 2;
			int borderHeight= 1;
			//
			g2.setColor(diagramColors.labelSpaceColor);
			g2.fill(new Rectangle2D.Double(
				realX - borderWidth,
				realY - textHeight - borderHeight,
				textWidth + borderWidth*2,
				textHeight + borderHeight*2));
			//
			double textX= realX;
			double textY= realY - textHeight;
			// g2.setColor(diagramColors.labelTextColor);
			// g2.setColor(Color.RED);
			// g2.setBackground(Color.CYAN);
			// g2.setPaint(Color.CYAN);
			// StringCharacterIterator ta= new StringCharacterIterator();
			// g2.drawString(ta,100,100);
			HashMap<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
			map.put(TextAttribute.FOREGROUND,diagramColors.labelTextColor);
			Font font= g2.getFont().deriveFont(map);
			g2.setFont(font);
			// g2.drawString("BoxText[0]",100,100);
			DiagramUtils.drawText(g2,textX,textY,0,0,label);
		}
	}
}      	
