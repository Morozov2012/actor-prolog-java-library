// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalDialogTask extends TimerTask {
	//
	protected AbstractDialog targetDialog;
	protected java.util.Timer scheduler;
	protected AtomicInteger counter= new AtomicInteger(100);
	//
	public LocalDialogTask(AbstractDialog target, java.util.Timer s) {
		targetDialog= target;
		scheduler= s;
	}
	//
	@Override
	public void run() {
		int c= counter.getAndDecrement();
		if (c <= 0) {
			cancel();
			scheduler.purge();
		} else {
			targetDialog.safelyRepaint();
		}
	}
}
