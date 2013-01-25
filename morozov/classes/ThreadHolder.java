// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.classes;

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
	// protected volatile boolean stopThisThread= false;
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
			// System.out.printf("ThreadHolder(1):%s:stopThisThread=%s\n",this,stopThisThread.get());
			try {
				// System.out.printf("ThreadHolder(2):%s:hasMessagesToBeProcessed=%s\n",this,hasMessagesToBeProcessed.get());
				if (hasMessagesToBeProcessed.compareAndSet(true,false)) {
					// System.out.printf("ThreadHolder(3):%s:%s.acceptFlowMessages()\n",this,currentProcess);
					currentProcess.acceptFlowMessages();
					currentProcess.acceptDirectMessage();
					currentProcess.acceptTimerMessage();
					currentProcess.sendStateOfProcess();
				} else {
					synchronized(this) {
						while (!hasMessagesToBeProcessed.get() && !stopThisThread.get()) {
							// System.out.printf("ThreadHolder(4):%s:wait\n",this);
							wait();
							// System.out.printf("ThreadHolder(5):%s:O.K.wait\n",this);
						}
					}
				}
				// System.out.printf("ThreadHolder(3->6):%s:hasMessagesToBeProcessed=%s\n",this,hasMessagesToBeProcessed.get());
			} catch (InterruptedException e) {
			} catch (ThreadDeath e) {
				return;
			} catch (Throwable e) {
				if (DefaultOptions.runtimeErrorReport) {
					long debugPosition= currentProcess.debugPosition;
					long debugUnit= currentProcess.debugUnit;
					int debugFileNumber= currentProcess.debugFileNumber;
					Window mainWindow= StaticDesktopAttributes.retrieveMainWindow(currentProcess.staticContext);
					RunTimeErrorDialog dialog= new RunTimeErrorDialog(this,mainWindow,e,debugPosition,debugUnit,debugFileNumber);
					dialog.activate();
				};
				currentProcess.hasUpdatedPort.set(true);
				// System.out.printf("ThreadHolder:O.K.\n");
			}
		}
	}
	//
	public final void wakeUp() {
		hasMessagesToBeProcessed.set(true);
		// System.out.printf("ThreadHolder(W.1):%s:wakeUp\n",this);
		synchronized(this) {
			notify();
		}
		// System.out.printf("ThreadHolder(W.2):%s:wakeUp\n",this);
	}
	//
	public final void terminate() {
		// System.out.printf("ThreadHolder:terminate()\n");
		stopThisThread.set(true);
		// System.out.printf("stopThisThread=%s\n",stopThisThread.get());
	}
	//
	public void stopProgram(Throwable error, long p, long u, int s) {
		StaticContext.retrieveRootProcess(currentProcess.staticContext).stopProcesses();
		reportErrorPosition(error,p,u,s);
		Runtime runtime= Runtime.getRuntime();
		// try {
		//	runtime.wait(1000);
		// } catch (InterruptedException e) {
		// };
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
			String systemPath= FileUtils.getUserDirectory();
			Path path= fileSystem.getPath(systemPath,errorReportFile);
			// BufferedWriter bufferedWriter= Files.newBufferedWriter(path,Charset.defaultCharset());
			PrintWriter writer= new PrintWriter(path.toFile());
			writer.format(
				"Runtime error;\n%d\n%s\n%d\n%s\n%d\n%s\n",
				p,unitName,u,fileName,s,error.toString());
			error.printStackTrace(writer);
			// bufferedWriter.write(e1.toString());
			// bufferedWriter.close();
			writer.close();
		} catch (Throwable e) {
		}
	}
}
