// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import java.util.TimerTask;

public class LocalCustomizedPickCanvasTask extends TimerTask {
	//
	protected CustomizedPickCanvas pickCanvas;
	//
	public LocalCustomizedPickCanvasTask(CustomizedPickCanvas canvas) {
		pickCanvas= canvas;
	}
	//
	@Override
	public void run() {
		pickCanvas.mouseWatch();
	}
}
