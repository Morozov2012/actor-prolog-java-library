// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.classes.*;
import morozov.system.gui.dialogs.*;

import javax.swing.JFrame;
import javax.swing.JApplet;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JDesktopPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Window;

import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyVetoException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;

public class DesktopUtils {
	//
	public static final int DIALOG_LAYER_CODE= 10;
	public static final Integer DIALOG_LAYER= new Integer(DIALOG_LAYER_CODE);
	//
	protected static final int borderSize= 3;
	protected static final int viewerTitleHeight= 39;
	//
	public static void createPaneIfNecessary(StaticContext context) {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
		if (desktop==null) {
			ReentrantLock lock= StaticDesktopAttributes.retrieveDesktopGuard(context);
			lock.lock();
			try {
				desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
				if (desktop==null) {
					LookAndFeelUtils.assignLookAndFeel();
					JApplet applet= StaticContext.retrieveApplet(context);
					if (applet==null) {
						Window mainWindow= new MainWindow(context);
						StaticDesktopAttributes.setMainWindow(mainWindow,context);
						mainWindow.setVisible(true);
					} else {
						Window mainWindow= StaticContext.retrieveSystemWindow(context);
						StaticDesktopAttributes.setMainWindow(mainWindow,context);
						Rectangle appletBounds= applet.getBounds();
						if (appletBounds.width<=0 || appletBounds.height <= 0) {
							GraphicsEnvironment env= GraphicsEnvironment.getLocalGraphicsEnvironment();
							Rectangle bounds= env.getMaximumWindowBounds();
							applet.setSize(bounds.width-borderSize*2,bounds.height-borderSize*2-viewerTitleHeight);
						};
						desktop= new MainDesktopPane(context);
						StaticDesktopAttributes.setDesktopPane(desktop,context);
						applet.getContentPane().add(desktop,BorderLayout.CENTER);
						desktop.setVisible(true);
					}
				}
			} finally {
				lock.unlock();
			}
		}
	}
	// Auxiliary operations
	static class MainWindow extends JFrame {
		MainWindow(StaticContext context) {
			GraphicsEnvironment env= GraphicsEnvironment.getLocalGraphicsEnvironment();
			Rectangle bounds= env.getMaximumWindowBounds();
			setSize(bounds.width,bounds.height);
			MainDesktopPane desktop= new MainDesktopPane(context);
			StaticDesktopAttributes.setDesktopPane(desktop,context);
			setContentPane(desktop);
			try {
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			} catch (SecurityException e) {
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		}
	}
	//
	public static JPopupMenu installStandardPopupMenu(ActionListener panel) {
		JPopupMenu popup= new JPopupMenu();
		return installStandardPopupMenu(panel,popup);
	}
	public static JPopupMenu installStandardPopupMenu(ActionListener panel, JPopupMenu popup) {
		// JPopupMenu popup= new JPopupMenu();
		JMenuItem item1= new JMenuItem("Cascade Windows");
		item1.setMnemonic('A');
		item1.setDisplayedMnemonicIndex(1);
		item1.setActionCommand("Cascade");
		item1.addActionListener(panel);
		popup.add(item1);
		JMenuItem item2= new JMenuItem("Horizontal Tile");
		item2.setMnemonic('H');
		item2.setDisplayedMnemonicIndex(0);
		item2.setActionCommand("HorizontalTile");
		item2.addActionListener(panel);
		popup.add(item2);
		JMenuItem item3= new JMenuItem("Vertical Tile");
		item3.setMnemonic('V');
		item3.setDisplayedMnemonicIndex(0);
		item3.setActionCommand("VerticalTile");
		item3.addActionListener(panel);
		popup.add(item3);
		JMenuItem item4= new JMenuItem("Restore Windows");
		item4.setMnemonic('R');
		item4.setDisplayedMnemonicIndex(0);
		item4.setActionCommand("Restore");
		item4.addActionListener(panel);
		popup.add(item4);
		return popup;
	}
	//
	public static void actionPerformed(ActionEvent e, StaticContext context) {
		String name= e.getActionCommand();
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
		if (name.equals("Cascade")) {
			if (desktop!=null) {
				cascade(desktop,JLayeredPane.DEFAULT_LAYER);
			}
		} else if (name.equals("HorizontalTile")) {
			if (desktop!=null) {
				horizontalTile(desktop,JLayeredPane.DEFAULT_LAYER);
			}
		} else if (name.equals("VerticalTile")) {
			if (desktop!=null) {
				verticalTile(desktop,JLayeredPane.DEFAULT_LAYER);
			}
		} else if (name.equals("Restore")) {
			if (desktop!=null) {
				restoreFrames(context,JLayeredPane.DEFAULT_LAYER);
			}
		}
	}
	//
	public static void cascade(JDesktopPane desktopPane, int layer) {
		JInternalFrame[] frames= desktopPane.getAllFramesInLayer(layer);
		frames= deleteHiddenFrames(frames);
		if (frames.length == 0) {
			return;
		} else {
			Arrays.sort(frames,new ZOrderComparator(desktopPane));
			cascade(frames,desktopPane.getBounds());
		}
	}
	protected static void cascade(JInternalFrame[] frames, Rectangle dBounds) {
		int beginningFrame= 0;
		while (beginningFrame < frames.length) {
			int endFrame= frames.length;
			int margin= 0;
			Dimension sizeDifference= new Dimension();
			for (int i= beginningFrame; i < frames.length; i++) {
				safelyGetSizeDifference(frames[i],sizeDifference);
				int currentShift= StrictMath.max(sizeDifference.width,sizeDifference.height);
				int currentWidth= dBounds.width - margin - currentShift;
				int currentHeight= dBounds.height - margin - currentShift;
				if (currentWidth < currentShift || currentHeight < currentShift) {
					endFrame= StrictMath.max(i,beginningFrame+1);
					break;
				};
				margin= margin + currentShift; 
			};
			int width= dBounds.width - margin;
			int height= dBounds.height - margin;
			int offset= 0;
			for (int i= beginningFrame; i < endFrame; i++) {
				setIconFalse(frames[i]);
				frames[i].setBounds(dBounds.x+offset,dBounds.y+offset,width,height);
				safelyGetSizeDifference(frames[i],sizeDifference);
				offset= offset + StrictMath.max(sizeDifference.width,sizeDifference.height);
			};
			if (beginningFrame==endFrame) {
				break;
			};
			beginningFrame= endFrame;
		}
	}
	//
	public static void horizontalTile(JDesktopPane desktopPane, int layer) {
		JInternalFrame[] frames= desktopPane.getAllFramesInLayer(layer);
		frames= deleteHiddenFrames(frames);
		if (frames.length == 0) {
			return;
		} else {
			Arrays.sort(frames,new InverseZOrderComparator(desktopPane));
			horozontalTile(frames,desktopPane.getBounds());
		}
	}
	protected static void horozontalTile(JInternalFrame[] frames, Rectangle dBounds) {
		double totalNumber= frames.length;
		int columns= (int)(StrictMath.floor(StrictMath.sqrt(totalNumber)));
		int rows= (int)(StrictMath.ceil(totalNumber / columns));
		int firstRow= (int)(StrictMath.round(totalNumber - columns*(rows-1)));
		int width;
		int height;
		int verticalShift= 1;
		if (firstRow == 0) {
			rows= rows - 1;
			height= dBounds.height / rows;
			verticalShift= 0;
		} else {
			height= dBounds.height / rows;
			if (firstRow < columns) {
				rows= rows - 1;
				width= dBounds.width / firstRow;
				for (int i= 0; i < firstRow; i++) {
					setIconFalse(frames[i]);
					frames[i].setBounds(i*width,0,width,height);
				}
			} else {
				firstRow= 0;
				verticalShift= 0;
			}
		};
		width= dBounds.width / columns;
		for (int j= 0; j < rows; j++) {
			for (int i= 0; i < columns; i++) {
				int index= firstRow+j*columns+i;
				setIconFalse(frames[index]);
				frames[index].setBounds(i*width,(j+verticalShift)*height,width,height);
			}
		}
	}
	//
	public static void verticalTile(JDesktopPane desktopPane, int layer) {
		JInternalFrame[] frames= desktopPane.getAllFramesInLayer(layer);
		frames= deleteHiddenFrames(frames);
		// JInternalFrame[] frames= desktopPane.getAllFrames();
		if (frames.length == 0) {
			return;
		} else {
			Arrays.sort(frames,new InverseZOrderComparator(desktopPane));
			verticalTile(frames,desktopPane.getBounds());
		}
	}
	protected static void verticalTile(JInternalFrame[] frames, Rectangle dBounds) {
		double totalNumber= frames.length;
		int rows= (int)(StrictMath.floor(StrictMath.sqrt(totalNumber)));
		int columns= (int)(StrictMath.ceil(totalNumber / rows));
		int firstColumn= (int)(StrictMath.round(totalNumber - rows*(columns-1)));
		int width;
		int height;
		int horizontalShift= 1;
		if (firstColumn == 0) {
			columns= columns - 1;
			width= dBounds.width / columns;
			horizontalShift= 0;
		} else {
			width= dBounds.width / columns;
			if (firstColumn < rows) {
				columns= columns - 1;
				height= dBounds.height / firstColumn;
				for (int i= 0; i < firstColumn; i++) {
					setIconFalse(frames[i]);
					frames[i].setBounds(0,i*height,width,height);
				}
			} else {
				firstColumn= 0;
				horizontalShift= 0;
			}
		};
		height= dBounds.height / rows;
		for (int i= 0; i < columns; i++) {
			for (int j= 0; j < rows; j++) {
				int index= firstColumn+i*rows+j;
				setIconFalse(frames[index]);
				frames[index].setBounds((i+horizontalShift)*width,j*height,width,height);
			}
		}
	}
	//
	public static void restoreFrames(StaticContext context) {
		restoreFrames(context,JLayeredPane.DEFAULT_LAYER);
		restoreFrames(context,DIALOG_LAYER);
	}
	public static void restoreFrames(StaticContext context, int layer) {
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
		if (desktop==null) {
			return;
		};
		JInternalFrame[] frames= desktop.getAllFramesInLayer(layer);
		frames= deleteHiddenFrames(frames);
		if (frames.length == 0) {
			return;
		} else {
			Arrays.sort(frames,new InverseZOrderComparator(desktop));
			restoreFrames(frames,context);
		}
	}
	public static void restoreFrames(JInternalFrame[] frames, StaticContext context) {
		for(int n=0; n < frames.length; n++) {
			if (frames[n] instanceof InnerPage) {
				setIconFalse(frames[n]);
				((InnerPage)frames[n]).safelyRestoreSize(context);
			} else if (frames[n] instanceof ExtendedJInternalFrame) {
				((ExtendedJInternalFrame)frames[n]).safelyRestoreSize();
			}
		}
	}
	//
	public static void selectNextInternalFrame(StaticContext context) {
		// System.out.printf("DesktopUtils::selectNextInternalFrame\n");
		MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
		JInternalFrame[] frames= desktop.getAllFramesInLayer(JLayeredPane.DEFAULT_LAYER);
		// Arrays.sort(frames,new ZOrderComparator(desktop));
		Arrays.sort(frames,new InverseZOrderComparator(desktop));
		// frames= deleteHiddenFrames(frames);
		for (int i= 0; i < frames.length; i++) {
			JInternalFrame frame= frames[i];
			if (frame.isShowing()) {
				try {
					// System.out.printf("DesktopUtils::selectNextInternalFrame: %s\n",frame);
					frame.setSelected(true);
					break;
				} catch (PropertyVetoException e) {
				}
			}
		}
	}
	//
	public static JInternalFrame[] deleteHiddenFrames(JInternalFrame[] frames) {
		int numberOfVisibleFrames= 0;
		for (int n= 0; n < frames.length; n++) {
			if (frames[n].isShowing()) {
				numberOfVisibleFrames= numberOfVisibleFrames + 1;
			}
		};
		if (numberOfVisibleFrames==frames.length) {
			return frames;
		} else {
			JInternalFrame[] newList= new JInternalFrame[numberOfVisibleFrames];
			int k= 0;
			for (int n= 0; n < frames.length; n++) {
				if (frames[n].isShowing()) {
					k= k + 1;
					newList[k-1]= frames[n];
				}
			};
			return newList;
		}
	}
	//
	static class ZOrderComparator implements Comparator<JInternalFrame> {
		JLayeredPane desktop;
		public ZOrderComparator(JLayeredPane pane) {
			desktop= pane;
		}
		public int compare(JInternalFrame f1, JInternalFrame f2) {
			return desktop.getPosition(f2) - desktop.getPosition(f1);
		}
		public boolean equals(Object o) {
			if (o==null) {
				return false;
			} else {
				if ( !(o instanceof ZOrderComparator) ) {
					return false;
				} else {
					ZOrderComparator c= (ZOrderComparator) o;
					return desktop == c.desktop;
				}
			}
		}
	}
	static class InverseZOrderComparator extends ZOrderComparator {
		public InverseZOrderComparator(JLayeredPane pane) {
			super(pane);
		}
		public int compare(JInternalFrame f1, JInternalFrame f2) {
			return desktop.getPosition(f1) - desktop.getPosition(f2);
		}
	}
	//
	public static Dimension safelyGetComponentSize(final Component component) {
		if (SwingUtilities.isEventDispatchThread()) {
			return component.getSize();
		} else {
			final Dimension size= new Dimension();
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						component.getSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			};
			return size;
		}
	}
	public static void safelyGetComponentLocation(final Component component, final Point location, final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			Point point= component.getLocation();
			location.setLocation(point);
			component.getSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						Point point= component.getLocation();
						location.setLocation(point);
						component.getSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public static void safelyGetComponentLocationOnScreen(final Component component, final Point location, final Dimension size) {
		if (SwingUtilities.isEventDispatchThread()) {
			Point point= component.getLocationOnScreen();
			location.setLocation(point);
			component.getSize(size);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						Point point= component.getLocationOnScreen();
						location.setLocation(point);
						component.getSize(size);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public static void safelyGetSizeDifference(final JInternalFrame frame, final Dimension sizeDifference) {
		if (SwingUtilities.isEventDispatchThread()) {
			getSizeDifference(frame,sizeDifference);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						getSizeDifference(frame,sizeDifference);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public static void getSizeDifference(final JInternalFrame frame, final Dimension sizeDifference) {
		frame.getSize(sizeDifference);
		Dimension cd= frame.getContentPane().getSize();
		sizeDifference.width= sizeDifference.width - cd.width;
		sizeDifference.height= sizeDifference.height - cd.height;
	}
	//
	public static void setIconFalse(JInternalFrame frame) {
		try {
			frame.setIcon(false);
		} catch (PropertyVetoException e) {
		}
	}
	//
	public static void safelySetTitle(final String title, final InnerPage window) {
		if (SwingUtilities.isEventDispatchThread()) {
			window.setTitle(title);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						window.setTitle(title);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public static void safelyRepaint(final InnerPage window) {
		if (SwingUtilities.isEventDispatchThread()) {
			window.repaint();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						window.repaint();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public static void safelySetVisible(final boolean value, final InnerPage window) {
		if (SwingUtilities.isEventDispatchThread()) {
			window.setVisible(value);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						window.setVisible(value);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	public static void safelyMoveToFront(final InnerPage window) {
		if (SwingUtilities.isEventDispatchThread()) {
			window.moveToFront();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						window.moveToFront();
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
}
