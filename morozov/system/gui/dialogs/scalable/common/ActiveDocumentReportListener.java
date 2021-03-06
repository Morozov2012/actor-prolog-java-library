/*
 * @(#)ReportListener.java 1.0 2011/03/06
 *
 * Copyright 2011 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

public interface ActiveDocumentReportListener {
	abstract public void reportSuccess();
	abstract public void reportFailure();
}
