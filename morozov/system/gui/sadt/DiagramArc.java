// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.awt.Color;

public class DiagramArc {
	//
	protected double x1;
	protected double y1;
	protected double x2;
	protected double y2;
	//
	public DiagramArc(double xx1, double yy1, double xx2, double yy2) {
		x1= xx1;
		y1= yy1;
		x2= xx2;
		y2= yy2;
	}
	//
	public void correctStartPoint(DiagramBox[] boxes) {
		if (boxes.length > 0) {
			if ((x2 - x1) > (y2 - y1)) {
				double minimalDistance= StrictMath.abs(boxes[0].x2-x1);
				double newX= boxes[0].x2;
				for (int n=1; n < boxes.length; n++) {
					double currentDistance= StrictMath.abs(boxes[n].x2-x1);
					if (currentDistance < minimalDistance) {
						minimalDistance= currentDistance;
						newX= boxes[n].x2;
					}
				};
				x1= newX;
			} else {
				double minimalDistance= StrictMath.abs(boxes[0].y1-y1);
				double newY= boxes[0].y1;
				for (int n=1; n < boxes.length; n++) {
					double currentDistance= StrictMath.abs(boxes[n].y1-y1);
					if (currentDistance < minimalDistance) {
						minimalDistance= currentDistance;
						newY= boxes[n].y1;
					}
				};
				y1= newY;
			}
		}
	}
	public void correctEndPoint(DiagramBox[] boxes) {
		if (boxes.length > 0) {
			if ((x2 - x1) > (y2 - y1)) {
				double minimalDistance= StrictMath.abs(boxes[0].x1-x2);
				double newX= boxes[0].x1;
				for (int n=1; n < boxes.length; n++) {
					double currentDistance= StrictMath.abs(boxes[n].x1-x2);
					if (currentDistance < minimalDistance) {
						minimalDistance= currentDistance;
						newX= boxes[n].x1;
					}
				};
				x2= newX;
			} else {
				double minimalDistance= StrictMath.abs(boxes[0].y2-y2);
				double newY= boxes[0].y2;
				for (int n=1; n < boxes.length; n++) {
					double currentDistance= StrictMath.abs(boxes[n].y2-y2);
					if (currentDistance < minimalDistance) {
						minimalDistance= currentDistance;
						newY= boxes[n].y2;
					}
				};
				y2= newY;
			}
		}
	}
	public void draw(Graphics2D g2, Dimension size) {
		double xA= StrictMath.round(size.width * x1);
		double yA= StrictMath.round(size.height * y1);
		double xB= StrictMath.round(size.width * x2);
		double yB= StrictMath.round(size.height * y2);
		g2.setColor(Color.BLACK);
		g2.draw(new Line2D.Double(xA,yA,xB,yB));
	}
}
