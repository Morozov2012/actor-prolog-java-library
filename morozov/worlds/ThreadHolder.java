// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.worlds;

import target.*;

import morozov.run.*;
import morozov.system.gui.*;
import morozov.system.gui.dialogs.special.*;
import morozov.system.files.*;

import java.nio.file.Path;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.awt.Window;

import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadHolder extends Thread {
	protected ActiveWorld currentProcess;
	protected AtomicBoolean hasMessagesToBeProcessed= new AtomicBoolean(true);
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	protected static String errorReportFile= "runtime.log";
	//
	public ThreadHolder(ActiveWorld process) {
		currentProcess= process;
	}
	//
	public void run() {
		currentProcess.hasUpdatedPort.set(true);
		while (!stopThisThread.get()) {
			try {
				if (hasMessagesToBeProcessed.compareAndSet(true,false)) {
					try {
						currentProcess.acceptFlowMessages();
						currentProcess.acceptDirectMessage();
						currentProcess.acceptTimerMessage();
					} finally {
						currentProcess.sendStateOfProcess();
					}
				} else {
					synchronized (this) {
						while (!hasMessagesToBeProcessed.get() && !stopThisThread.get()) {
							wait();
						}
					}
				}
			} catch (InterruptedException e) {
			} catch (ThreadDeath e) {
				return;
			} catch (CodedErrorExit e) {
				if (e.isNormalTerminationOfTheProgram()) {
					System.exit(0);
				} else {
					reportThrowable(e);
					currentProcess.hasUpdatedPort.set(true);
				}
			} catch (ProcessedErrorExit e) {
				if (e.isNormalTerminationOfTheProgram()) {
					System.exit(0);
				} else {
					reportThrowable(e);
					currentProcess.hasUpdatedPort.set(true);
				}
			} catch (Throwable e) {
				reportThrowable(e);
				currentProcess.hasUpdatedPort.set(true);
			}
		}
	}
	//
	protected void reportThrowable(Throwable e) {
		if (DefaultOptions.runtimeErrorReport) {
			long debugPosition= currentProcess.debugPosition;
			long debugUnit= currentProcess.debugUnit;
			int debugFileNumber= currentProcess.debugFileNumber;
			Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(currentProcess.staticContext);
			DesktopUtils.safelySetVisible(true,mainWindow);
			RunTimeErrorDialog dialog= new RunTimeErrorDialog(this,mainWindow,e,debugPosition,debugUnit,debugFileNumber);
			dialog.activate();
		}
	}
	//
	public final void wakeUp() {
		hasMessagesToBeProcessed.set(true);
		synchronized (this) {
			notify();
		}
	}
	//
	public final void terminate() {
		stopThisThread.set(true);
	}
	//
	public void stopProgram(Throwable error, long p, long u, int s) {
		StaticContext.retrieveRootProcess(currentProcess.staticContext).stopProcesses();
		reportErrorPosition(error,p,u,s);
		Runtime runtime= Runtime.getRuntime();
		runtime.exit(1);
	}
	//
	public void reportErrorPosition(Throwable error, long p, long u, int s) {
		String textPosition;
		if (p >= 0) {
			textPosition= String.format("%d",p);
		} else {
			textPosition= "no debug information";
		};
		String unitName;
		String unitNameInfo;
		if (u >= 0) {
			SymbolName name= SymbolNames.retrieveSymbolName(u);
			unitName= name.extractClassName();
			unitNameInfo= "unit: " + unitName;
		} else {
			unitName= "no debug information";
			unitNameInfo= "unit code: " + String.format("%d",u);
		};
		String fileName;
		String fileNameInfo;
		if (s > 0) {
			fileName= DefaultOptions.programSourceFiles[s-1];
			fileNameInfo= "file: " + fileName;
		} else {
			fileName= "no debug information";
			fileNameInfo= "file number: " + String.format("%d",s);
		};
		System.err.printf("Runtime error; pos: %s; %s; %s\n",textPosition,unitNameInfo,fileNameInfo);
		error.printStackTrace();
		try {
			String systemPath= SimpleFileName.getUserDirectory();
			Path path= fileSystem.getPath(systemPath,errorReportFile);
			PrintWriter writer= new PrintWriter(path.toFile());
			writer.format(
				"Runtime error;\n%d\n%s\n%d\n%s\n%d\n%s\n",
				p,unitName,u,fileName,s,error.toString());
			error.printStackTrace(writer);
			writer.close();
		} catch (Throwable e) {
		}
	}
}
