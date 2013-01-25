// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.checker.*;
import morozov.system.files.*;
import morozov.system.*;
import morozov.terms.*;

import java.io.IOException;
import java.applet.AppletContext;
import javax.swing.JApplet;
import java.awt.Desktop;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;

public abstract class Application extends Alpha {
	//
	protected long processCounter= 0;
	protected HashSet<Process> accountableProceses= new HashSet<Process>();
	//
	abstract protected Term getBuiltInSlot_E_command();
	abstract protected Term getBuiltInSlot_E_arguments();
	abstract protected Term getBuiltInSlot_E_window_mode();
	abstract protected Term getBuiltInSlot_E_enable_multiple_instances();
	abstract protected Term getBuiltInSlot_E_backslash_is_separator_always();
	//
	abstract public long entry_s_End_1_i();
	//
	public void activate2s(ChoisePoint iX, Term command, Term arguments) {
		try {
			String text= arguments.getStringValue(iX);
			activate(iX,command,text,false);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(arguments);
		}
	}
	public void activate1s(ChoisePoint iX, Term command) {
		activate(iX,command,"",false);
	}
	public void activate0s(ChoisePoint iX) {
		Term command= getBuiltInSlot_E_command();
		Term arguments= getBuiltInSlot_E_arguments();
		try {
			String text= arguments.getStringValue(iX);
			activate(iX,command,text,true);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(arguments);
		}
	}
	protected void activate(ChoisePoint iX, Term command, String arguments, boolean isAccountableSubprocess) {
		try {
			String commandText= command.getStringValue(iX);
			activateProcessBuilder(commandText,arguments,isAccountableSubprocess,iX);
		} catch (TermIsNotAString e1) {
			try {
				long code= command.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_auto) {
					activateBrowser(arguments,iX);
				} else {
					throw new WrongTermIsNotACommand(command);
				}
			} catch (TermIsNotASymbol e2) {
				try {
					long code= command.getStructureFunctor(iX);
					if (code==SymbolCodes.symbolCode_E_auto) {
						activateBrowser(arguments,iX);
					} else {
						throw new WrongTermIsNotACommand(command);
					}
				} catch (TermIsNotAStructure e3) {
					throw new WrongTermIsNotACommand(command);
				}
			}
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
	protected void activateProcessBuilder(String command, String arguments, boolean isAccountableSubprocess, ChoisePoint iX) {
		Term instanceControl= getBuiltInSlot_E_enable_multiple_instances();
		try {
			boolean enableMultipleInstances= Converters.term2YesNo(instanceControl,iX);
			if (isAccountableSubprocess) {
				if (!enableMultipleInstances && processCounter > 0) {
					return;
				} else {
					ProcessBuilder builder= new ProcessBuilder(command,arguments);
					Process p= builder.start();
					processCounter++;
					accountableProceses.add(p);
					long domainSignature= entry_s_End_1_i();
					PendingProcess supervisor= new PendingProcess(this,p,domainSignature);
					supervisor.start();
				}
			} else {
				new ProcessBuilder(command,arguments).start();
			}
		} catch (IOException e) {
			throw new FileInputOutputError(command,e);
		// } catch (InterruptedException e) {
		}
	}
	//
	protected void activateBrowser(String arguments, ChoisePoint iX) {
		JApplet applet= StaticContext.retrieveApplet(staticContext);
		if (applet != null) {
			AppletContext ac= applet.getAppletContext();
			try {
				URL codeBase= applet.getCodeBase();
				URL url= new URL(codeBase,arguments);
				ac.showDocument(url,"_blank");
			} catch (MalformedURLException e) {
				throw new WrongTermIsMalformedURL(e);
			}
		} else {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop= Desktop.getDesktop();
				boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
				if (URL_Utils.isLocalResource(arguments,backslashIsSeparator)) {
					try {
						java.io.File file= new java.io.File(arguments);
						if (file.exists()) {
							desktop.open(file);
						} else {
							String truncatedName= FileUtils.deleteQuotationMarks(arguments);
							file= new java.io.File(truncatedName);
							if (file.exists()) {
								desktop.open(file);
							} else {
								browseResource(desktop,arguments);
							}
						}
					} catch (NullPointerException e) {
						// File is null.
						throw new WrongTermIsNotFileName(new PrologString(arguments));
					} catch (IllegalArgumentException e) {
						// The specified file doesn't exist.
						throw new FileIsNotFound();
					} catch (UnsupportedOperationException e) {
						// The current platform does not support the Desktop.Action.OPEN action.
						throw new ApplicationIsNotSupported();
					} catch (IOException e) {
						// The specified file has no associated application or the associated application fails to be launched.
						throw new FileInputOutputError(arguments,e);
					// } catch (SecurityException e) {
						// A security manager exists and its SecurityManager.checkRead(java.lang.String) method
						// denies read access to the file, or it denies the AWTPermission("showWindowWithoutWarningBanner")
						// permission, or the calling thread is not allowed to create a subprocess.
					}
				} else {
					browseResource(desktop,arguments);
				}
			} else {
				throw new ApplicationIsNotSupported();
			}
		}
	}
	//
	protected void browseResource(Desktop desktop, String arguments) {
		try {
			desktop.browse(new URI(arguments));
		} catch (URISyntaxException e) {
			throw new WrongTermIsMalformedURL(e);
		} catch (NullPointerException e) {
			// Uri is null.
			throw new WrongTermIsNotFileName(new PrologString(arguments));
		} catch (UnsupportedOperationException e) {
			// The current platform does not support the Desktop.Action.BROWSE action.
			throw new ApplicationIsNotSupported();
		} catch (IOException e) {
			// The user default browser is not found, or it fails to be launched, or the default handler application failed to be launched.
			// 2011.04.03
			// throw new FileInputOutputError(arguments,e);
		// } catch (SecurityException e) {
			// A security manager exists and it denies the AWTPermission("showWindowWithoutWarningBanner") permission,
			// or the calling thread is not allowed to create a subprocess; and not invoked from within an applet or
			// Java Web Started application IllegalArgumentException - if the necessary permissions are not available
			// and the URI can not be converted to a URL.
		}
	}
}

class PendingProcess extends Thread {
	//
	protected Application targetWorld;
	protected Process observedProcess;
	protected long domainSignature;
	//
	public PendingProcess(Application target, Process process, long signature) {
		targetWorld= target;
		observedProcess= process;
		domainSignature= signature;
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
		AsyncCall item= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
		targetWorld.receiveAsyncCall(item);
	}
}
