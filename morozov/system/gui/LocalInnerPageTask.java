// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import java.util.TimerTask;

public class LocalInnerPageTask extends TimerTask {
	private InnerPage innerPage;
	//
	public LocalInnerPageTask(InnerPage page) {
		innerPage= page;
	}
	//
	public void run() {
		innerPage.safelyRepaint();
	}
}
