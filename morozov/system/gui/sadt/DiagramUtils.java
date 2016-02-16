// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import target.*;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.sadt.signals.*;
import morozov.terms.*;
import morozov.worlds.*;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Component;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyVetoException;

public class DiagramUtils {
	public static String computeFullIdentifier(String motherIdentifier, long innerBlockNumber) {
		if (motherIdentifier.isEmpty()) {
			return String.format("%d",innerBlockNumber);
		} else if (motherIdentifier.equals("0")) {
			return String.format("%d",innerBlockNumber);
		} else {
			return String.format("%s.%d",motherIdentifier,innerBlockNumber);
		}
	}
	//
	public static void showDiagramPage(String identifier, StaticContext context, DiagramColors diagramColors, Map<String,AbstractProcess> components, Map<String,ComponentState> componentSuccess) {
		DesktopUtils.createPaneIfNecessary(context);
		createInternalFrameIfNecessary(identifier,context,diagramColors,components,componentSuccess);
	}
	//
	private static void createInternalFrameIfNecessary(String identifier, StaticContext context, DiagramColors diagramColors, Map<String,AbstractProcess> components, Map<String,ComponentState> componentSuccess) {
		try {
			Map<String,InternalDiagramFrame> innerWindows= StaticDiagramAttributes.retrieveInnerWindows(context);
			InternalDiagramFrame diagramWindow= innerWindows.get(identifier);
			createInternalFrameIfNecessary(diagramWindow,identifier,context,diagramColors,components,componentSuccess);
		} catch (DiagramTableEntryDoesNotExist e) {
			AbstractProcess process= components.get(identifier);
			if (process!=null) {
				AbstractInternalWorld world= process.getMainWorld();
				if (world!=null) {
					long domainSignature= world.entry_s_Show_2_ii();
					String title= formDiagramTitle(identifier);
					Term predicateArgument1= new PrologString(identifier);
					Term predicateArgument2= new PrologString(title);
					AsyncCall call= new AsyncCall(domainSignature,world,true,true,new Term[]{predicateArgument1,predicateArgument2},true);
					world.transmitAsyncCall(call,null);
				}
			}
		}
	}
	private static void createInternalFrameIfNecessary(final InternalDiagramFrame diagramWindow, final String identifier, final StaticContext context, final DiagramColors diagramColors, final Map<String,AbstractProcess> components, final Map<String,ComponentState> componentSuccess) throws DiagramTableEntryDoesNotExist {
		if (diagramWindow==null) {
			DiagramContent graph= DiagramTable.getDiagramContent(identifier);
			safelyCreateInternalFrame(identifier,graph,context,diagramColors,components,componentSuccess);
		} else {
			safelyMakeInternalFrameVisible(diagramWindow);
		}
	}
	//
	private static void safelyCreateInternalFrame(final String identifier, final DiagramContent graph, final StaticContext context, final DiagramColors diagramColors, final Map<String,AbstractProcess> components, final Map<String,ComponentState> componentSuccess) {
		if (SwingUtilities.isEventDispatchThread()) {
			createInternalFrame(identifier,graph,context,diagramColors,components,componentSuccess);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						createInternalFrame(identifier,graph,context,diagramColors,components,componentSuccess);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	private static void createInternalFrame(String identifier, DiagramContent graph, StaticContext context, DiagramColors diagramColors, Map<String,AbstractProcess> components, Map<String,ComponentState> componentSuccess) {
		String title= formDiagramTitle(identifier);
		InternalDiagramFrame diagramWindow= new InternalDiagramFrame(title,identifier,graph,diagramColors,context,components,componentSuccess);
		Map<String,InternalDiagramFrame> innerWindows= StaticDiagramAttributes.retrieveInnerWindows(context);
		innerWindows.put(identifier,diagramWindow);
		if (identifier.isEmpty()) {
			diagramWindow.getInternalFrame().setClosable(false);
		};
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
		desktop.safelyAdd(diagramWindow.getInternalFrame());
		// diagramWindow.setVisible(true);
		// DesktopUtils.safelySetVisible(true,diagramWindow);
		diagramWindow.safelySetVisible(true);
		diagramWindow.safelyRestoreSize(context);
	}
	//
	public static String formDiagramTitle(String identifier) {
		String name= "";
		try {
			name= DiagramTable.getDiagramName(identifier);
		} catch (DiagramTableEntryDoesNotExist e) {
		};
		String title= "";
		if (identifier.isEmpty()) {
			title= "a-0";
		} else {
			title= String.format("a%s: %s",identifier,name);
		};
		return title;
	}
	//
	private static void safelyMakeInternalFrameVisible(final InternalDiagramFrame diagramWindow) {
		if (SwingUtilities.isEventDispatchThread()) {
			makeInternalFrameVisible(diagramWindow);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						makeInternalFrameVisible(diagramWindow);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	private static void makeInternalFrameVisible(InternalDiagramFrame diagramWindow) {
		try {
			diagramWindow.getInternalFrame().setIcon(false);
		} catch (PropertyVetoException e) {
		};
		if (!diagramWindow.getInternalFrame().isShowing()) {
			// diagramWindow.setVisible(true);
			// DesktopUtils.safelySetVisible(true,diagramWindow);
			diagramWindow.safelySetVisible(true);
		};
		diagramWindow.getInternalFrame().toFront();
	}
	//
	public static Font computeFont(DiagramColors colors, int size) {
		return computeFont(colors.fontName,size,colors.fontStyle,colors.successTextForegroundColor,colors.successTextBackgroundColor);
	}
	public static Font computeFont(DiagramColors colors, int size, Color foreground, Color background) {
		return computeFont(colors.fontName,size,colors.fontStyle,foreground,background);
	}
	public static Font computeFont(String fontName, int fontSize, int fontStyle, Color foreground, Color background) {
		Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
		map.put(TextAttribute.FAMILY,fontName);
		boolean isBold= (fontStyle & Font.BOLD) != 0;
		boolean isItalic= (fontStyle & Font.ITALIC) != 0;
		if (isBold) {
			map.put(TextAttribute.WEIGHT,TextAttribute.WEIGHT_BOLD);
		} else {
			map.put(TextAttribute.WEIGHT,TextAttribute.WEIGHT_REGULAR);
		};
		if (isItalic) {
			map.put(TextAttribute.POSTURE,TextAttribute.POSTURE_OBLIQUE);
		} else {
			map.put(TextAttribute.POSTURE,TextAttribute.POSTURE_REGULAR);
		};
		map.put(TextAttribute.WIDTH,TextAttribute.WIDTH_REGULAR);
		int realFontSize= DefaultOptions.fontSystemSimulationMode.simulate(fontSize);
		map.put(TextAttribute.SIZE,realFontSize);
		map.put(TextAttribute.FOREGROUND,foreground);
		if (background!=null) {
			map.put(TextAttribute.BACKGROUND,background);
		};
		return new Font(map);
	}
	//
	public static double getTextWidth(Graphics2D g2, String[] boxText) {
		FontMetrics metrics= g2.getFontMetrics();
		int textWidth= 0;
		if (boxText.length > 0) {
			textWidth= metrics.stringWidth(boxText[0]);
			for (int n=1; n<boxText.length; n++) {
				int currentWidth= metrics.stringWidth(boxText[n]);
				if (currentWidth > textWidth) {
					textWidth= currentWidth;
				}
			}
		};
		return textWidth;
	}
	public static double getTextHeight(Graphics2D g2, String[] boxText) {
		Font font= g2.getFont();
		FontMetrics metrics= g2.getFontMetrics();
		FontRenderContext frc= metrics.getFontRenderContext();
		double textHeight= 0;
		if (boxText.length > 0) {
			Rectangle2D rectangle2D= font.getMaxCharBounds(frc);
			textHeight= rectangle2D.getHeight();
			if (boxText.length > 1) {
				double extraLeading= computeExtraLeading(textHeight);
				for (int n=1; n<boxText.length; n++) {
					rectangle2D= font.getMaxCharBounds(frc);
					textHeight= textHeight + extraLeading + rectangle2D.getHeight();
				}
			}
		};
		return textHeight;
	}
	public static double getLineHeight(Graphics2D g2, String textLine) {
		Font font= g2.getFont();
		FontMetrics metrics= g2.getFontMetrics();
		FontRenderContext frc= metrics.getFontRenderContext();
		Rectangle2D rectangle2D= font.getMaxCharBounds(frc);
		double textHeight= rectangle2D.getHeight();
		return textHeight;
	}
	public static double computeExtraLeading(double oneLineHeight) {
		return oneLineHeight * 0.5;
	}
	public static void drawText(Graphics2D g2, double x, double y, double boxWidth, double boxHeight, String[] boxText) {
		if (boxText.length > 0) {
			double textHeight= getTextHeight(g2,boxText);
			double firstLineHeight= getLineHeight(g2,boxText[0]);
			double extraLeading= computeExtraLeading(firstLineHeight);
			Font font= g2.getFont();
			FontMetrics metrics= g2.getFontMetrics();
			FontRenderContext frc= metrics.getFontRenderContext();
			double textX= 0;
			float descent= metrics.getDescent();
			double textY;
			if (boxHeight >= 1) {
				textY= y + (boxHeight - textHeight) / 2 + firstLineHeight - descent;
			} else {
				textY= y + firstLineHeight - descent;
			};
			if (boxText.length > 1) {
				for (int n=0; n<boxText.length; n++) {
					int lineWidth= metrics.stringWidth(boxText[n]);
					Rectangle2D rectangle2D= font.getMaxCharBounds(frc);
					double lineHeight= rectangle2D.getHeight();
					if (boxWidth >= 1) {
						textX= x + (boxWidth - lineWidth) / 2;
					} else {
						textX= x;
					};
					if (boxText[n].length() > 0) {
						g2.drawString(boxText[n],PrologInteger.toInteger(textX),PrologInteger.toInteger(textY));
					};
					textY= textY + lineHeight + extraLeading;
				}
			} else {
				int lineWidth= metrics.stringWidth(boxText[0]);
				if (boxWidth >= 1) {
					textX= x + (boxWidth - lineWidth) / 2;
				} else {
					textX= x;
				};
				if (boxHeight >= 1) {
					textY= y + (boxHeight + textHeight) / 2 - descent;
				} else {
					textY= y + firstLineHeight - descent;
				};
				if (boxText[0].length() > 0) {
					g2.drawString(boxText[0],PrologInteger.toInteger(textX),PrologInteger.toInteger(textY));
				}
			}
		}
	}
	//
	public static void showNote(Component parent, String fullIdentifier, StaticContext staticContext) {
		String title= DiagramUtils.formDiagramTitle(fullIdentifier);
		String note= "";
		try {
			note= DiagramTable.getDiagramNote(fullIdentifier);
		} catch (DiagramTableEntryDoesNotExist e) {
		};
		if (note.isEmpty()) {
			try {
				note= DiagramTable.getDiagramName(fullIdentifier);
			} catch (DiagramTableEntryDoesNotExist e) {
			};
		};
		DesktopUtils.createPaneIfNecessary(staticContext);
		JOptionPane panel= new MessageBoxPane(note,JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog= panel.createDialog(parent,title);
		dialog.setModal(true);
		dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		// dialog.setVisible(true);
		DesktopUtils.safelySetVisible(true,dialog);
	}
	//
	public static void safelyRepaintAllDiagrams(StaticContext context) {
		Map<String,InternalDiagramFrame> innerWindows= StaticDiagramAttributes.retrieveInnerWindows(context);
		Collection<InternalDiagramFrame> values= innerWindows.values();
		final InternalDiagramFrame[] frames= values.toArray(new InternalDiagramFrame[0]);
		if (SwingUtilities.isEventDispatchThread()) {
			repaintDiagrams(frames);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						repaintDiagrams(frames);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public static void repaintDiagrams(InternalDiagramFrame[] frames) {
		for (int n=0; n <= frames.length; n++) {
			frames[n].quicklyRepaint();
		}
	}
	//
	public static void safelyDisposeAllDiagrams(StaticContext context) {
		Map<String,InternalDiagramFrame> innerWindows= StaticDiagramAttributes.retrieveInnerWindows(context);
		Collection<InternalDiagramFrame> values= innerWindows.values();
		final InternalDiagramFrame[] frames= values.toArray(new InternalDiagramFrame[0]);
		if (SwingUtilities.isEventDispatchThread()) {
			disposeDiagrams(frames);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						disposeDiagrams(frames);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public static void disposeDiagrams(InternalDiagramFrame[] frames) {
		for (int n=0; n <= frames.length; n++) {
			frames[n].getInternalFrame().dispose();
		}
	}
}
