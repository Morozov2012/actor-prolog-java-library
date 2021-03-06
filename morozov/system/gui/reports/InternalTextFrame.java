// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.reports;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.*;
import morozov.terms.*;

import java.awt.event.ActionEvent;
import javax.swing.text.StyledDocument;
import javax.swing.SwingUtilities;

import java.lang.reflect.InvocationTargetException;

public class InternalTextFrame extends InnerPage {
	//
	protected TextPaneNoWrap panel;
	//
	public InternalTextFrame(Report world, String title, ReportSpaceAttributes a) {
		super(title);
		panel= new TextPaneNoWrap();
		ExtendedReportSpace extendedReportSpace= new ExtendedReportSpace(null,world,panel);
		canvasSpace= extendedReportSpace;
		extendedReportSpace.setAttributes(a);
		extendedReportSpace.enableMouseListener();
		internalFrame.add(extendedReportSpace.getControl());
		panel.addFocusListener(this);
	}
	//
	public void safelySetStyledDocument(final StyledDocument doc) {
		if (SwingUtilities.isEventDispatchThread()) {
			panel.setStyledDocument(doc);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						panel.setStyledDocument(doc);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void actionPerformed(ActionEvent event) {
		if (canvasSpace != null) {
			CustomControl targetWorld= canvasSpace.getTargetWorld();
			if (targetWorld != null) {
				long domainSignature= ((Report)targetWorld).entry_s_Action_1_i();
				Term predicateArgument= new PrologString(event.getActionCommand());
				Term[] arguments= new Term[]{predicateArgument};
				AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
				targetWorld.transmitAsyncCall(call,null);
			}
		}
	}
}
