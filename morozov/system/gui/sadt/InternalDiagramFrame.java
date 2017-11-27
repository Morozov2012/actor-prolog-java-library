// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.worlds.*;

import java.util.Map;

public class InternalDiagramFrame extends InnerPage {
	protected DiagramPaneNoWrap panel;
	//
	public InternalDiagramFrame(String title, String identifier, DiagramContent graph, DiagramColors diagramColors, StaticContext context, Map<String,AbstractProcess> components, Map<String,ComponentState> componentSuccess) {
		super(title);
		panel= new DiagramPaneNoWrap(getInternalFrame(),identifier,graph,diagramColors,context,components,componentSuccess);
		getInternalFrame().add(panel);
	}
	//
	public void quicklyRepaint() {
		panel.repaint();
	}
}
