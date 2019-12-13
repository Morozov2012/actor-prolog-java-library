// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.worlds.*;

import java.util.TimerTask;

public class LocalTimerTask extends TimerTask {
	//
	protected ActiveWorld currentProcess;
	protected AbstractInternalWorld targetWorld;
	//
	public LocalTimerTask(ActiveWorld process, AbstractInternalWorld target) {
		currentProcess= process;
		targetWorld= target;
	}
	//
	@Override
	public void run() {
		currentProcess.receiveTimerMessage(targetWorld);
	}
}
