// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.system.*;
import morozov.system.checker.*;
import morozov.system.files.*;
import morozov.terms.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import java.util.List;

public abstract class WebResource extends Text {
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	protected static final int defaultWaitingInterval= -1;
	//
	abstract protected Term getBuiltInSlot_E_location();
	abstract protected Term getBuiltInSlot_E_max_waiting_time();
	abstract protected Term getBuiltInSlot_E_character_set();
	abstract protected Term getBuiltInSlot_E_backslash_is_separator_always();
	// abstract protected Term getBuiltInSlot_E_mask();
	// abstract protected Term getBuiltInSlot_E_send_full_path();
	// abstract protected Term getBuiltInSlot_E_query();
	// abstract protected Term getBuiltInSlot_E_content_type();
	//
	public void getParameters1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		try {
			String path= a2.getStringValue(iX);
			CharacterSet characterSet= retrieveCharacterSet(iX);
			int timeout= retrieveMaxWaitingTime(iX);
			a1.value= getResourceParameters(path,characterSet,timeout,iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void getParameters1fs(ChoisePoint iX, Term a1) {
	}
	public void getParameters0ff(ChoisePoint iX, PrologVariable a1) {
		URI uri= retrieveLocationURI(iX);
		CharacterSet characterSet= retrieveCharacterSet(iX);
		int timeout= retrieveMaxWaitingTime(iX);
		a1.value= getResourceParameters(uri,characterSet,timeout,iX);
		// iX.pushTrail(a1);
	}
	public void getParameters0fs(ChoisePoint iX) {
	}
	protected Term getResourceParameters(URI uri, CharacterSet characterSet, int timeout, ChoisePoint iX) {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			return attributes.toTerm();
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	protected Term getResourceParameters(String path, CharacterSet characterSet, int timeout, ChoisePoint iX) {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			URI uri= getURI(iX,path);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			return attributes.toTerm();
		} catch (Throwable e) {
			return URL_Utils.exceptionToName(e);
		}
	}
	//
	public void getContent1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		try {
			String path= a2.getStringValue(iX);
			a1.value= getResourceContent(path,iX);
			// iX.pushTrail(a1);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void getContent1fs(ChoisePoint iX, Term a1) {
	}
	public void getContent0ff(ChoisePoint iX, PrologVariable a1) {
		URI uri= retrieveLocationURI(iX);
		a1.value= getResourceContent(uri,iX);
		// iX.pushTrail(a1);
	}
	public void getContent0fs(ChoisePoint iX) {
	}
	protected Term getResourceContent(String path, ChoisePoint iX) {
		try {
			URI uri= getURI(iX,path);
			return getResourceContent(uri,iX);
		} catch (Throwable e) {
			// Term error= new PrologString(e.toString());
			// return new PrologStructure(SymbolCodes.symbolCode_E_error,new Term[]{error});
			return URL_Utils.exceptionToName(e);
		}
	}
	protected Term getResourceContent(URI uri, ChoisePoint iX) {
		try {
			URL_Utils.installCookieManagerIfNecessary(staticContext);
			CharacterSet characterSet= retrieveCharacterSet(iX);
			int timeout= retrieveMaxWaitingTime(iX);
			boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
			Term result;
			try {
				result= getContentOfResource(uri,characterSet,timeout,backslashIsSeparator);
			} catch (CannotRetrieveContent e2) {
				result= e2.getExceptionName();
			} catch (Throwable e2) {
				throw e2;
			};
			return result;
		// } catch (MalformedURLException e1) {
		} catch (Throwable e1) {
			return URL_Utils.exceptionToName(e1);
		}
	}
	protected Term getContentOfResource(URI uri, CharacterSet characterSet, int timeout, boolean backslashIsSeparator) throws CannotRetrieveContent, URISyntaxException, MalformedURLException {
		Term result;
		try {
			// URL_Utils.installCookieManagerIfNecessary(staticContext);
			URL_Attributes attributes= URL_Utils.getResourceAttributes(uri,characterSet,timeout,staticContext,backslashIsSeparator);
			try {
				if (attributes.isDirectory) {
					List<Path> list= retrieveDirectoryList(uri);
					StringBuilder buffer= new StringBuilder();
					for (int n=0; n < list.size(); n++) {
						buffer.append(FileUtils.tryToMakeRealName(list.get(n)).toString());
						buffer.append("\n");
					};
					result= new PrologString(buffer.toString());
				} else {
					String text= URL_Utils.readTextFile(attributes);
					result= new PrologString(text);
				};
				if (attributes.connectionWasSuccessful()) {
					return result;
				} else {
					throw new CannotRetrieveContent(attributes.getExceptionName());
				}
			} catch (Throwable e2) {
				throw new CannotRetrieveContent(e2);
			// } finally {
			//	attributes.safeCloseConnection();
			}
		} catch (URISyntaxException e1) {
			// Term error= new PrologString(e1.toString());
			// return new PrologStructure(SymbolCodes.symbolCode_E_error,new Term[]{error});
			throw e1;
		} catch (MalformedURLException e1) {
			// Term error= new PrologString(e1.toString());
			// return new PrologStructure(SymbolCodes.symbolCode_E_error,new Term[]{error});
			throw e1;
		} catch (IOException e1) {
			return URL_Utils.exceptionToName(e1);
		} catch (Throwable e1) {
			return URL_Utils.exceptionToName(e1);
		}
	}
	//
	public void getFullName1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String resolvedName= getFullName(iX,a2);
		a1.value= new PrologString(resolvedName);
	}
	public void getFullName1fs(ChoisePoint iX, Term a1) {
	}
	public void getFullName0ff(ChoisePoint iX, PrologVariable a1) {
		String resolvedName= getFullName(iX);
		a1.value= new PrologString(resolvedName);
	}
	public void getFullName0fs(ChoisePoint iX) {
	}
	protected String getFullName(ChoisePoint iX, Term a1) {
		String fileName= null;
		try {
			fileName= a1.getStringValue(iX);
		} catch (TermIsNotAString e1) {
			throw new WrongArgumentIsNotAString(a1);
		};
		return getFullName(iX,fileName);
	}
	protected String getFullName(ChoisePoint iX, String fileName) {
		String location= retrieveLocationString(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		return URL_Utils.getFullName(location,fileName,staticContext,backslashIsSeparator);
	}
	protected String getFullName(ChoisePoint iX) {
		String location= retrieveLocationString(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		return URL_Utils.getFullName(location,staticContext,backslashIsSeparator);
	}
	//
	public void getURL1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		String resolvedName= getFullName(iX,a2);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		a1.value= new PrologString(URL_Utils.get_URL_string(resolvedName,staticContext,backslashIsSeparator));
	}
	public void getURL1fs(ChoisePoint iX, Term a1) {
	}
	public void getURL0ff(ChoisePoint iX, PrologVariable a1) {
		String resolvedName= getFullName(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		a1.value= new PrologString(URL_Utils.get_URL_string(resolvedName,staticContext,backslashIsSeparator));
	}
	public void getURL0fs(ChoisePoint iX) {
	}
	//
	protected URI getURI(ChoisePoint iX, String fileName) throws URISyntaxException {
		String location= retrieveLocationString(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		return URL_Utils.getURI(location,fileName,staticContext,backslashIsSeparator);
	}
	//
	public void isLocalResource1s(ChoisePoint iX, Term a1) throws Backtracking {
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		try {
			String path= a1.getStringValue(iX);
			if (!URL_Utils.isLocalResource(getFullName(iX,path),backslashIsSeparator)) {
				throw new Backtracking();
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		} catch (WrongTermIsMalformedURL e) {
		}
	}
	public void isLocalResource0s(ChoisePoint iX) throws Backtracking {
		String path= getFullName(iX);
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		if (!URL_Utils.isLocalResource(path,backslashIsSeparator)) {
			throw new Backtracking();
		}
	}
	//
	protected URI retrieveLocationURI(ChoisePoint iX) {
		Term location= getBuiltInSlot_E_location();
		boolean backslashIsSeparator= FileUtils.checkIfBackslashIsSeparator(getBuiltInSlot_E_backslash_is_separator_always(),iX);
		try {
			String path= location.getStringValue(iX);
			URI uri= URL_Utils.create_URI(path,staticContext,backslashIsSeparator);
			return uri;
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(location);
		}
	}
	protected String retrieveLocationString(ChoisePoint iX) {
		Term location= getBuiltInSlot_E_location();
		try {
			return location.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(location);
		}
	}
	//
	protected CharacterSet retrieveCharacterSet(ChoisePoint iX) {
		Term value= getBuiltInSlot_E_character_set();
		return FileUtils.term2CharacterSet(value,iX);
	}
	//
	protected int retrieveMaxWaitingTime(ChoisePoint iX) {
		Term interval= getBuiltInSlot_E_max_waiting_time();
		try {
			return URL_Utils.termToWaitingInterval(interval,iX);
		} catch (TermIsSymbolDefault e1) {
			try {
				return URL_Utils.termToWaitingInterval(DefaultOptions.waitingInterval,iX);
			} catch (TermIsSymbolDefault e2) {
				return defaultWaitingInterval;
			}
		}
	}
	//
	protected List<Path> retrieveDirectoryList(URI uri) {
		// String path= uri.toString();
		// path= URL_Utils.deleteFilePrefix(path);
		// if (path.isEmpty()) {
		//	path= FileUtils.getUserDirectory();
		// };
		// Path dir= fileSystem.getPath(path);
		Path dir= fileSystem.provider().getPath(uri);
		return FileUtils.listSourceFiles(dir);
	}
	//
	public void getString0ff(ChoisePoint iX, PrologVariable a1) {
		getContent0ff(iX,a1);
	}
	public void setString1s(ChoisePoint iX, Term inputText) {
		throw new DisabledOperation();
	}
	public void clear0s(ChoisePoint iX) {
		throw new DisabledOperation();
	}
	public void write1ms(ChoisePoint iX, Term... args) {
		throw new DisabledOperation();
	}
	public void writeLn1ms(ChoisePoint iX, Term... args) {
		throw new DisabledOperation();
	}
	public void writeF2ms(ChoisePoint iX, Term... args) {
		throw new DisabledOperation();
	}
	public void newLine0s(ChoisePoint iX) {
		throw new DisabledOperation();
	}
}
