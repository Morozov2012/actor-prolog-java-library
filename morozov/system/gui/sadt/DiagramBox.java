// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import target.*;

import morozov.system.gui.sadt.signals.*;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.BasicStroke;

import java.util.Map;

public class DiagramBox {
	//
	protected String mother;
	protected int number;
	protected long boxCode;
	protected String[] boxText;
	protected double x1;
	protected double y1;
	protected double x2;
	protected double y2;
	protected int border= 3;
	protected BooleanOperation operation= BooleanOperation.AND;
	//
	public DiagramBox(String motherIdentifier, int localNumber, long nameCode, String[] text, double xx1, double yy1, double xx2, double yy2) {
		mother= motherIdentifier;
		number= localNumber;
		boxCode= nameCode;
		boxText= text;
		x1= xx1;
		y1= yy1;
		x2= xx2;
		y2= yy2;
		for (int n= 0; n < boxText.length; n ++) {
			String name= boxText[n].toLowerCase();
			if (name.contains("#atom")) {
				operation= BooleanOperation.ATOM;
				break;
			} else if (name.contains("#and")) {
				operation= BooleanOperation.AND;
				break;
			} else if (name.contains("#or")) {
				operation= BooleanOperation.OR;
				break;
			} else if (name.contains("#xor")) {
				operation= BooleanOperation.XOR;
				break;
			} else if (name.contains("#allnot")) {
				operation= BooleanOperation.ALL_NOT;
				break;
			} else if (name.contains("#notall")) {
				operation= BooleanOperation.NOT_ALL;
				break;
			}
		}
	}
	//
	public void draw(Graphics2D g2, Dimension size, Map<String,ComponentState> componentSuccess, DiagramColors diagramColors, int fontSize) {
		//
		double x= StrictMath.round(size.width * x1);
		double y= StrictMath.round(size.height * y2);
		double boxWidth= StrictMath.round(size.width * (x2-x1));
		double boxHeight= StrictMath.round(size.height * (y1-y2));
		//
		String fullIdentifier= DiagramUtils.computeFullIdentifier(mother,number);
		ComponentState state= componentSuccess.get(fullIdentifier);
		//
		float dash1[]= {5.0f,2.0f};
		BasicStroke stroke;
		Color backgroundDrawingColor;
		Color hatchColor;
		Color foregroundTextColor;
		Color backgroundTextColor;
		//
		try {
			DiagramContent graph= DiagramTable.getDiagramContent(fullIdentifier);
			if (state==null || state.isSuspended()) {
				stroke= new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,dash1,0.0f);
				backgroundDrawingColor= Color.WHITE;
				hatchColor= null;
				foregroundTextColor= diagramColors.successTextForegroundColor;
				backgroundTextColor= diagramColors.successTextBackgroundColor;
			} else {
				stroke= new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				if (state.isProven() && boxRuleIsConfirmed(graph,componentSuccess)) {
					backgroundDrawingColor= diagramColors.successDrawingBackgroundColor;
					hatchColor= diagramColors.successDrawingForegroundColor;
					foregroundTextColor= diagramColors.successTextForegroundColor;
					backgroundTextColor= diagramColors.successTextBackgroundColor;
				} else {
					backgroundDrawingColor= diagramColors.failureDrawingBackgroundColor;
					hatchColor= diagramColors.failureDrawingForegroundColor;
					foregroundTextColor= diagramColors.failureTextForegroundColor;
					backgroundTextColor= diagramColors.failureTextBackgroundColor;
				}
			}
		} catch (DiagramTableEntryDoesNotExist e) {
			if (state==null || state.isSuspended()) {
				stroke= new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,dash1,0.0f);
				backgroundDrawingColor= diagramColors.successDrawingBackgroundColor;
				hatchColor= diagramColors.successDrawingForegroundColor;
				foregroundTextColor= diagramColors.successTextForegroundColor;
				backgroundTextColor= diagramColors.successTextBackgroundColor;
			} else {
				stroke= new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				if (state.isProven()) {
					backgroundDrawingColor= diagramColors.successDrawingBackgroundColor;
					hatchColor= diagramColors.successDrawingForegroundColor;
					foregroundTextColor= diagramColors.successTextForegroundColor;
					backgroundTextColor= diagramColors.successTextBackgroundColor;
				} else {
					backgroundDrawingColor= diagramColors.failureDrawingBackgroundColor;
					hatchColor= diagramColors.failureDrawingForegroundColor;
					foregroundTextColor= diagramColors.failureTextForegroundColor;
					backgroundTextColor= diagramColors.failureTextBackgroundColor;
				}
			}
		};
		//
		g2.setColor(backgroundDrawingColor);
		g2.fill(new Rectangle2D.Double(x,y,boxWidth+1,boxHeight+1));
		//
		if (hatchColor!=null) {
			g2.setColor(hatchColor);
			double xx= x;
			while(xx <= x+boxWidth) {
				g2.fill(new Rectangle2D.Double(xx,y,3,boxHeight));
				xx= xx + 10;
			}
		};
		//
		g2.setColor(Color.BLACK);
		g2.setStroke(stroke);
		g2.draw(new Rectangle2D.Double(x,y,boxWidth,boxHeight));
		//
		Font font= DiagramUtils.computeFont(diagramColors,fontSize,foregroundTextColor,backgroundTextColor);
		//
		g2.setFont(font);
		DiagramUtils.drawText(g2,x,y,boxWidth,boxHeight,boxText);
	}
	//
	public boolean fontSizeIsSuitable(Font font, Graphics2D g2, Dimension size) {
		//
		double boxWidth= StrictMath.round(size.width * (x2-x1));
		double boxHeight= StrictMath.round(size.height * (y1-y2));
		//
		g2.setFont(font);
		//
		double textWidth= DiagramUtils.getTextWidth(g2,boxText);
		//
		if (textWidth + border*2 >= boxWidth) {
			return false;
		};
		//
		double textHeight= DiagramUtils.getTextHeight(g2,boxText);
		//
		if (textHeight + border*2 >= boxHeight) {
			return false;
		}
		//
		return true;
	}
	//
	public boolean containsPoint(Point point, Dimension size) {
		double x= StrictMath.round(size.width * x1);
		double boxWidth= StrictMath.round(size.width * (x2-x1));
		if (point.x < x || point.x > x + boxWidth) {
			return false;
		};
		double y= StrictMath.round(size.height * y2);
		double boxHeight= StrictMath.round(size.height * (y1-y2));
		if (point.y < y || point.y > y + boxHeight) {
			return false;
		};
		return true;
	}
	//
	public boolean boxRuleIsConfirmed(DiagramContent graph, Map<String,ComponentState> componentSuccess) {
		return operation.eval(graph.boxes,componentSuccess);
	}
	//
	public boolean isOK(Map<String,ComponentState> componentSuccess) {
		String fullIdentifier= DiagramUtils.computeFullIdentifier(mother,number);
		ComponentState state= componentSuccess.get(fullIdentifier);
		if (state==null || state.isSuspended()) {
			return true;
		} else if (!state.isProven()) {
			return false;
		} else {
			try {
				DiagramContent graph= DiagramTable.getDiagramContent(fullIdentifier);
				if (boxRuleIsConfirmed(graph,componentSuccess)) {
					return true;
				} else {
					return false;
				}
			} catch (DiagramTableEntryDoesNotExist e) {
				return true;
			}
		}
	}
}
