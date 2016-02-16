// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.worlds.*;

import java.util.TimerTask;

public class LocalTimerTask extends TimerTask {
	private ActiveWorld currentProcess;
	private AbstractInternalWorld targetWorld;
	//
	public LocalTimerTask(ActiveWorld process, AbstractInternalWorld target) {
		currentProcess= process;
		targetWorld= target;
	}
	//
	public void run() {
		currentProcess.receiveTimerMessage(targetWorld);
	}
}
