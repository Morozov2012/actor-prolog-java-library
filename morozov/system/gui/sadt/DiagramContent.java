// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import target.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

import java.util.Map;

public class DiagramContent {
	//
	public String boxIdentifier;
	public String motherIdentifier;
	public int localNumber;
	public DiagramBox[] boxes;
	public DiagramPath[] paths;
	//
	public DiagramContent(String identifier, String mother, int self, DiagramBox[] boxList, DiagramPath[] pathList) {
		boxIdentifier= identifier;
		motherIdentifier= mother;
		localNumber= self;
		boxes= boxList;
		paths= pathList;
	}
	public void draw(Graphics g0, Dimension size, DiagramColors diagramColors, Map<String,ComponentState> componentSuccess) {
		Graphics2D g2= (Graphics2D)g0;
		int fontSize= adjustFont(g0,size,diagramColors);
		//
		Color backgroundDrawingColor= Color.WHITE;
		Color hatchColor= null;
		//
		if (!boxContextIsOK(componentSuccess)) {
			backgroundDrawingColor= diagramColors.failureDrawingBackgroundColor;
			hatchColor= diagramColors.failureDrawingForegroundColor;
		};
		//
		int width= size.width;
		int height= size.height;
		//
		g2.setColor(backgroundDrawingColor);
		g2.fill(new Rectangle2D.Double(0,0,width+1,height+1));
		//
		if (hatchColor!=null) {
			g2.setColor(hatchColor);
			double xx= 0;
			while(xx <= width) {
				g2.fill(new Rectangle2D.Double(xx,0,3,height));
				xx= xx + 10;
			}
		};
		for (int n=0; n < paths.length; n++) {
			paths[n].drawLine(g0,size);
		};
		for (int n=0; n < paths.length; n++) {
			paths[n].drawLabel(g0,size,diagramColors);
		};
		for (int n=0; n < boxes.length; n++) {
			boxes[n].draw(g0,size,componentSuccess,diagramColors,fontSize);
		}
	}
	public long getInnerBlock(Point point, Dimension size) throws NoBlockIsPointed {
		for (int n=0; n < boxes.length; n++) {
			if (boxes[n].containsPoint(point,size)) {
				return boxes[n].number;
			}
		};
		throw new NoBlockIsPointed();
	}
	//
	private int adjustFont(Graphics g0, Dimension size, DiagramColors diagramColors) {
		g0.setFont(diagramColors.initialFont);
		if (fontHasAppropriateSize(g0,size,diagramColors.initialFont)) {
			// System.out.printf("return diagramColors.fontSize=%s\n",diagramColors.fontSize);
			return diagramColors.fontSize;
		} else {
			Font currentFont= diagramColors.initialFont;
			int currentFontSize= diagramColors.fontSize;
			for (int n=0; n < boxes.length; n++) {
				// System.out.printf("n=%s) currentFontSize=%s\n",n,currentFontSize);
				if (currentFontSize <= diagramColors.minimalFontSize) {
					break;
				};
				DiagramBox currentBox= boxes[n];
				if (currentBox.fontSizeIsSuitable(currentFont,g0,size)) {
					continue;
				} else {
					if (currentBox.fontSizeIsSuitable(diagramColors.minimalFont,g0,size)) {
						Font lowerFont= diagramColors.minimalFont;
						int lowerFontSize= diagramColors.minimalFontSize;
						int upperFontSize= currentFontSize;
						int iterationNumber= 0;
						while (true) {
							iterationNumber= iterationNumber + 1;
							// System.out.printf("iterationNumber=%s) (n=%s) currentFontSize=%s\n",iterationNumber,n,currentFontSize);
							if (iterationNumber > 100) {
								break;
							};
							if (upperFontSize > lowerFontSize + 1) {
								int newFontSize= (upperFontSize + lowerFontSize) / 2;
								Font newFont= DiagramUtils.computeFont(diagramColors,newFontSize);
								if (currentBox.fontSizeIsSuitable(newFont,g0,size)) {
									lowerFont= newFont;
									lowerFontSize= newFontSize;
								} else {
									upperFontSize= newFontSize;
								}
							} else {
								currentFont= lowerFont;
								currentFontSize= lowerFontSize;
								break;
							}
						}
					} else {
						currentFont= diagramColors.minimalFont;
						currentFontSize= diagramColors.minimalFontSize;
					}
				}
			};
			g0.setFont(currentFont);
			return currentFontSize;
		}
	}
	//
	private boolean fontHasAppropriateSize(Graphics g0, Dimension size, Font font) {
		for (int n=0; n < boxes.length; n++) {
			if(!boxes[n].fontSizeIsSuitable(font,g0,size)) {
				return false;
			}
		};
		return true;
	}
	//
	public boolean boxContextIsOK(Map<String,ComponentState> componentSuccess) {
		String mother= motherIdentifier;
		int boxNumber= localNumber;
		while(true) {
			if (boxNumber > 0) {
				try {
					DiagramContent graph= DiagramTable.getDiagramContent(mother);
					DiagramBox box= graph.boxes[boxNumber-1];
					if (!box.isOK(componentSuccess)) {
						return false;
					};
					mother= graph.motherIdentifier;
					boxNumber= graph.localNumber;
				} catch (DiagramTableEntryDoesNotExist e) {
					return false;
				}
			} else {
				return true;
			}
		}
	}
}
