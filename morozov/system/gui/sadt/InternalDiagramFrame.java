// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import morozov.classes.*;
import morozov.system.gui.*;

import java.awt.Font;
import java.util.Map;

public class InternalDiagramFrame extends InnerPage {
	protected DiagramPaneNoWrap panel;
	//
	public InternalDiagramFrame(String title, String identifier, DiagramContent graph, DiagramColors diagramColors, StaticContext context, Map<String,AbstractProcess> components, Map<String,ComponentState> componentSuccess) {
		super(title);
		panel= new DiagramPaneNoWrap(this,identifier,graph,diagramColors,context,components,componentSuccess);
		add(panel);
		// MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
		// if (desktop!=null) {
		//	Dimension size= safelyGetComponentSize(desktop);
		//	logicalX.set(new ExtendedCoordinate(0));
		//	logicalY.set(new ExtendedCoordinate(0));
		//	logicalWidth.set(new ExtendedSize(size.width));
		//	logicalHeight.set(new ExtendedSize(size.height));
		// }
	}
	//
	public void setFont(Font f) {
		super.setFont(f);
		panel.setFont(f);
	}
	//
	// public void safelyRestoreSize(StaticContext context) {
	//	MainDesktopPane desktop= StaticDesktopAttributes.retrieveDesktopPane(context);
	//	if (desktop!=null) {
	//		Dimension size= safelyGetComponentSize(desktop);
	//		safelyRestoreSize(0,0,size.width,size.height);
	//	}
	// }
}
