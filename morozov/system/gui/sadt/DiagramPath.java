// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;

import java.util.HashMap;
import java.awt.font.TextAttribute;
import java.awt.Font;

public class DiagramPath {
	//
	protected String mother;
	protected double x;
	protected double y;
	protected String[] label;
	protected DiagramArc[] arcs;
	//
	public DiagramPath(String motherIdentifier, double xx, double yy, String[] text, DiagramArc[] arcList) {
		mother= motherIdentifier;
		x= xx;
		y= yy;
		label= text;
		arcs= arcList;
	}
	//
	public void correctArcs(DiagramBox[] boxes) {
		int nArcs= arcs.length;
		if (nArcs > 0 && boxes.length > 0) {
			arcs[0].correctStartPoint(boxes);
			arcs[nArcs-1].correctEndPoint(boxes);
		}
	}
	public void drawLine(Graphics2D g2, Dimension size) {
		for (int n=0; n < arcs.length; n++) {
			arcs[n].draw(g2,size);
		};
	}
	public void drawLabel(Graphics2D g2, Dimension size, DiagramColors diagramColors) {
		if (x <= Double.MIN_VALUE && y <= Double.MIN_VALUE) {
			return;
		} else if (label.length > 0) {
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
			HashMap<TextAttribute,Object> map= new HashMap<>();
			map.put(TextAttribute.FOREGROUND,diagramColors.labelTextColor);
			Font font= g2.getFont().deriveFont(map);
			g2.setFont(font);
			DiagramUtils.drawText(g2,textX,textY,0,0,label);
		}
	}
}
