// (c) 2010-2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.system.checker.errors.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.command.*;
import morozov.system.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Files;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.applet.AppletContext;
import java.awt.Desktop;
import javax.swing.JApplet;

public abstract class Application extends Alpha {
	//
	public ProcessBuilderCommand command= null;
	public String arguments= null;
	public WindowMode windowMode= null;
	public Boolean enableMultipleInstances= null;
	//
	protected long processCounter= 0;
	protected HashSet<Process> accountableProceses= new HashSet<Process>();
	protected HashSet<PendingProcess> supervisors= new HashSet<PendingProcess>();
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	public Application() {
	}
	public Application(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	abstract protected Term getBuiltInSlot_E_command();
	abstract protected Term getBuiltInSlot_E_arguments();
	abstract protected Term getBuiltInSlot_E_window_mode();
	abstract protected Term getBuiltInSlot_E_enable_multiple_instances();
	// abstract protected Term getBuiltInSlot_E_backslash_always_is_separator();
	//
	abstract public long entry_s_End_1_i();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set command
	//
	public void setCommand1s(ChoisePoint iX, Term a1) {
		ProcessBuilderCommand text= ProcessBuilderCommand.termToProcessBuilderCommand(a1,iX);
		setCommand(text);
	}
	public void setCommand(ProcessBuilderCommand value) {
		command= value;
	}
	public void getCommand0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= getCommand(iX).toTerm();
	}
	public void getCommand0fs(ChoisePoint iX) {
	}
	public ProcessBuilderCommand getCommand(ChoisePoint iX) {
		if (command != null) {
			return command;
		} else {
			Term value= getBuiltInSlot_E_command();
			return ProcessBuilderCommand.termToProcessBuilderCommand(value,iX);
		}
	}
	//
	// get/set arguments
	//
	public void setArguments1s(ChoisePoint iX, Term a1) {
		String text= Converters.argumentToString(a1,iX);
		setArguments(text);
	}
	public void setArguments(String value) {
		arguments= value;
	}
	public void getArguments0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= new PrologString(getArguments(iX));
	}
	public void getArguments0fs(ChoisePoint iX) {
	}
	public String getArguments(ChoisePoint iX) {
		if (arguments != null) {
			return arguments;
		} else {
			Term value= getBuiltInSlot_E_arguments();
			return Converters.argumentToString(value,iX);
		}
	}
	//
	// get/set windowMode
	//
	public void setWindowMode1s(ChoisePoint iX, Term a1) {
		WindowMode mode= WindowMode.termToWindowMode(a1,iX);
		setWindowMode(mode);
	}
	public void setWindowMode(WindowMode value) {
		windowMode= value;
	}
	public void getWindowMode0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= getWindowMode(iX).toTerm();
	}
	public void getWindowMode0fs(ChoisePoint iX) {
	}
	public WindowMode getWindowMode(ChoisePoint iX) {
		if (windowMode != null) {
			return windowMode;
		} else {
			Term value= getBuiltInSlot_E_window_mode();
			return WindowMode.termToWindowMode(value,iX);
		}
	}
	//
	// get/set enableMultipleInstances
	//
	public void setEnableMultipleInstances1s(ChoisePoint iX, Term a1) {
		setEnableMultipleInstances(YesNo.termYesNo2Boolean(a1,iX));
	}
	public void setEnableMultipleInstances(boolean value) {
		enableMultipleInstances= value;
	}
	public void getEnableMultipleInstances0ff(ChoisePoint iX, PrologVariable a1) {
		boolean value= getEnableMultipleInstances(iX);
		a1.value= YesNo.boolean2TermYesNo(value);
	}
	public void getEnableMultipleInstances0fs(ChoisePoint iX) {
	}
	public boolean getEnableMultipleInstances(ChoisePoint iX) {
		if (enableMultipleInstances != null) {
			return enableMultipleInstances;
		} else {
			return YesNo.termYesNo2Boolean(getBuiltInSlot_E_enable_multiple_instances(),iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void closeFiles() {
		Iterator<PendingProcess> processList= supervisors.iterator();
		while (processList.hasNext()) {
			PendingProcess p= processList.next();
			p.cancel();
			p.interrupt();
		};
		super.closeFiles();
	}
	//
	public void activate0s(ChoisePoint iX) {
		ProcessBuilderCommand command= getCommand(iX);
		String arguments= getArguments(iX);
		activate(iX,command,arguments,true);
	}
	public void activate1s(ChoisePoint iX, Term a1) {
		ProcessBuilderCommand command= ProcessBuilderCommand.termToProcessBuilderCommand(a1,iX);
		String arguments= getArguments(iX);
		activate(iX,command,arguments,false);
	}
	public void activate2s(ChoisePoint iX, Term a1, Term a2) {
		ProcessBuilderCommand command= ProcessBuilderCommand.termToProcessBuilderCommand(a1,iX);
		String arguments= Converters.argumentToString(a2,iX);
		activate(iX,command,arguments,false);
	}
	protected void activate(ChoisePoint iX, ProcessBuilderCommand command, String arguments, boolean isAccountableSubprocess) {
		if (command.isAutomatic) {
			if (command.isExtensionSpecific) {
				boolean backslashIsSeparator= getBackslashAlwaysIsSeparator(iX);
				boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
				SimpleFileName simpleFile= SimpleFileName.termToSimpleFileName(arguments,backslashIsSeparator,acceptOnlyURI);
				String extension= command.text;
				RelativeFileName relativeFile= simpleFile.formRelativeFileName(true,extension);
				arguments= relativeFile.toString();
			};
			activateBrowser(arguments,iX);
		} else {
			boolean enableMultipleInstancesFlag= getEnableMultipleInstances(iX);
			activateProcessBuilder(command.text,arguments,isAccountableSubprocess,enableMultipleInstancesFlag);
		}
	}
	//
	public void close0s(ChoisePoint iX) {
		Iterator<Process> processList= accountableProceses.iterator();
		while (processList.hasNext()) {
			Process p= processList.next();
			p.destroy();
			processCounter--;
		};
		accountableProceses.clear();
		processCounter= 0;
	}
	//
	public void end1s(ChoisePoint iX, Term exitValue) {
	}
	//
	public class End1s extends Continuation {
		//
		public End1s(Continuation aC, Term exitValue) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	protected void activateProcessBuilder(String commandText, String arguments, boolean isAccountableSubprocess, boolean enableMultipleInstancesFlag) {
		try {
			if (isAccountableSubprocess) {
				if (!enableMultipleInstancesFlag && processCounter > 0) {
					return;
				} else {
					ProcessBuilder builder= new ProcessBuilder(commandText,arguments);
					Process p= builder.start();
					processCounter++;
					accountableProceses.add(p);
					long domainSignature= entry_s_End_1_i();
					PendingProcess supervisor= new PendingProcess(this,p,domainSignature,supervisors);
					supervisors.add(supervisor);
					supervisor.start();
				}
			} else {
				new ProcessBuilder(commandText,arguments).start();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(commandText,e);
		// } catch (InterruptedException e) {
		}
	}
	//
	protected void activateBrowser(String commandText, ChoisePoint iX) {
		JApplet applet= StaticContext.retrieveApplet(staticContext);
		if (applet != null) {
			AppletContext ac= applet.getAppletContext();
			try {
				boolean backslashIsSeparator= getBackslashAlwaysIsSeparator(iX);
				boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
				SimpleFileName simpleFile= SimpleFileName.termToSimpleFileName(commandText,backslashIsSeparator,acceptOnlyURI);
				ExtendedFileName extendedFile= simpleFile.formRealFileNameBasedOnPath(false,false,"",null,staticContext);
				URL codeBase= applet.getCodeBase();
				if (codeBase != null) {
					URL url= new URL(codeBase,extendedFile.get_URI_OfResource().toASCIIString());
					ac.showDocument(url,"_blank");
				} else { // A probable bug in JDK 1.7.0_40
					activateDesktopApplication(commandText,false,iX);
				}
			} catch (MalformedURLException e) {
				throw new WrongArgumentIsMalformedURL(commandText,e);
			}
		} else {
			activateDesktopApplication(commandText,false,iX);
		}
	}
	protected void activateDesktopApplication(String commandText, boolean quotesAreEliminated, ChoisePoint iX) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop= Desktop.getDesktop();
			boolean backslashIsSeparator= getBackslashAlwaysIsSeparator(iX);
			boolean acceptOnlyURI= getAcceptOnlyUniformResourceIdentifiers(iX);
			SimpleFileName simpleFile= SimpleFileName.termToSimpleFileName(commandText,backslashIsSeparator,acceptOnlyURI);
			try {
				ExtendedFileName extendedFile= simpleFile.formRealFileNameBasedOnPath(false,false,"",null,staticContext);
				if (extendedFile.getIsLocalResource()) {
					Path path= extendedFile.getPathOfLocalResource();
					if (Files.exists(path)) {
						desktop.open(path.toFile());
					} else if (quotesAreEliminated) {
						// throw new WrongArgumentIsNotFileName(new PrologString(commandText));
						desktop.open(path.toFile());
					} else {
						commandText= FileUtils.deleteQuotationMarks(commandText);
						activateDesktopApplication(commandText,true,iX);
					}
				} else {
					desktop.browse(extendedFile.get_URI_OfResource());
				}
			// } catch (URISyntaxException e) {
			} catch (WrongArgumentIsMalformedURL e) {
				if (quotesAreEliminated) {
					throw new WrongArgumentIsNotFileName(new PrologString(commandText));
				} else {
					commandText= FileUtils.deleteQuotationMarks(commandText);
					activateDesktopApplication(commandText,true,iX);
				}
			} catch (NullPointerException e) {
				// File is null.
				throw new WrongArgumentIsNotFileName(new PrologString(commandText));
			} catch (IllegalArgumentException e) {
				// The specified file doesn't exist.
				throw new FileIsNotFound(commandText);
			} catch (UnsupportedOperationException e) {
				// The current platform does not support the Desktop.Action.OPEN action.
				throw new ApplicationIsNotSupported();
			} catch (IOException e) {
				// The specified file has no associated application or the associated application fails to be launched.
				throw new FileInputOutputError(commandText,e);
			// } catch (SecurityException e) {
				// A security manager exists and its SecurityManager.checkRead(java.lang.String) method
				// denies read access to the file, or it denies the AWTPermission("showWindowWithoutWarningBanner")
				// permission, or the calling thread is not allowed to create a subprocess.
			}
		}
	}
}

class PendingProcess extends Thread {
	//
	protected Application targetWorld;
	protected Process observedProcess;
	protected long domainSignature;
	protected HashSet<PendingProcess> supervisors;
	protected AtomicBoolean isActual= new AtomicBoolean(true);
	//
	public PendingProcess(Application target, Process process, long signature, HashSet<PendingProcess> supervisorList) {
		targetWorld= target;
		observedProcess= process;
		domainSignature= signature;
		supervisors= supervisorList;
	}
	//
	public void run() {
		Term[] arguments= new Term[1];
		try {
			int exitValue= observedProcess.waitFor();
			arguments[0]= new PrologInteger(exitValue);
		} catch (InterruptedException e) {
			arguments[0]= new PrologSymbol(SymbolCodes.symbolCode_E_interrupted_exception);
		};
		supervisors.remove(this);
		if (isActual.get()) {
			AsyncCall item= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
			targetWorld.transmitAsyncCall(item,null);
		}
	}
	//
	public void cancel() {
		isActual.set(false);
	}
}
