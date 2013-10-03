// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import morozov.system.gui.*;

public class InternalTextFrame extends InnerPage {
	//
	public ExtendedReportSpace scrollPane;
	public TextPaneNoWrap panel;
	//
	public InternalTextFrame(String title, ReportSpaceAttributes a) {
		super(title);
		panel= new TextPaneNoWrap();
		scrollPane= new ExtendedReportSpace(panel);
		scrollPane.setAttributes(a);
		scrollPane.enableMouseListener();
		// panel.initiate(scrollPane);
		add(scrollPane);
	}
}
