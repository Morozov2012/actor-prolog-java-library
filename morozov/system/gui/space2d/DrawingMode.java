// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.font.TextLayout;

public class DrawingMode {
	//
	protected int windowWidth;
	protected int windowHeight;
	protected int sizeFactor;
	protected double columns= 1.0;
	protected double rows= 1.0;
	//
	protected double virtualWindowWidth;
	protected double virtualWindowHeight;
	protected double virtualFactorX;
	protected double virtualFactorY;
	//
	protected Color penColor= null;
	protected Paint paint= null;
	protected boolean fillFigures= false;
	//
	protected Canvas2DHorizontalAlignment horizontalAlignment= Canvas2DHorizontalAlignment.DEFAULT;
	protected Canvas2DVerticalAlignment verticalAlignment= Canvas2DVerticalAlignment.DEFAULT;
	//
	public DrawingMode(Dimension size, Canvas2DScalingFactor scalingFactor) {
		windowWidth= size.width;
		windowHeight= size.height;
		sizeFactor= scalingFactor.computeScalingCoefficient(windowWidth,windowHeight);
		virtualWindowWidth= windowWidth;
		virtualWindowHeight= windowHeight;
		virtualFactorX= sizeFactor;
		virtualFactorY= sizeFactor;
	}
	//
	public void setMesh(double c, double r) {
		columns= c;
		rows= r;
		virtualWindowWidth= windowWidth / columns;
		virtualWindowHeight= windowHeight / rows;
		virtualFactorX= sizeFactor / columns;
		virtualFactorY= sizeFactor / rows;
	}
	//
	public void setTextAlignment(Canvas2DHorizontalAlignment hA, Canvas2DVerticalAlignment vA) {
		horizontalAlignment= hA;
		verticalAlignment= vA;
	}
	//
	public int getWindowWidth() {
		return windowWidth;
	}
	public int getWindowHeight() {
		return windowHeight;
	}
	//
	public double getFactorX() {
		if (sizeFactor < 0) {
			return virtualWindowWidth;
		} else {
			return virtualFactorX;
		}
	}
	public double getFactorY() {
		if (sizeFactor < 0) {
			return virtualWindowHeight;
		} else {
			return virtualFactorY;
		}
	}
	//
	public void drawShape(Graphics2D g2, Shape shape) {
		if (fillFigures) {
			g2.fill(shape);
		} else {
			g2.draw(shape);
		}
	}
	//
	public void drawString(Graphics2D g2, double x, double y, String text) {
		if (text.length() <= 0) {
			return;
		};
		if (	(	horizontalAlignment==Canvas2DHorizontalAlignment.DEFAULT ||
				horizontalAlignment==Canvas2DHorizontalAlignment.LEFT ) &&
			(	verticalAlignment==Canvas2DVerticalAlignment.DEFAULT ||
				verticalAlignment==Canvas2DVerticalAlignment.BASELINE ) ) {
			g2.drawString(text,(float)x,(float)y);
		} else {
			Font font= g2.getFont();
			FontRenderContext fRC= g2.getFontRenderContext();
			TextLayout layout= new TextLayout(text,font,fRC);
			Rectangle2D rect= layout.getBounds();
			double baseX= rect.getX();
			double baseY= rect.getY();
			double width= rect.getWidth();
			double height= rect.getHeight();
			double fX= x;
			double fY= y;
			if (horizontalAlignment==Canvas2DHorizontalAlignment.RIGHT) {
				fX= x - width;
			} else if (horizontalAlignment==Canvas2DHorizontalAlignment.CENTER) {
				fX= x - width / 2;
			};
			if (verticalAlignment==Canvas2DVerticalAlignment.TOP) {
				fY= y - baseY;
			} else if (verticalAlignment==Canvas2DVerticalAlignment.BOTTOM) {
				fY= y - baseY - height;
			} else if (verticalAlignment==Canvas2DVerticalAlignment.CENTER) {
				fY= y - baseY - height / 2;
			};
			layout.draw(g2,(float)fX,(float)fY);
		}
	}
}
